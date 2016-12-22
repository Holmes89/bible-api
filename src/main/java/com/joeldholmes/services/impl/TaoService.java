package com.joeldholmes.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joeldholmes.entity.VerseEntity;
import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.repository.IVerseRepository;
import com.joeldholmes.resources.QuranVerseResource;
import com.joeldholmes.resources.TaoVerseResource;
import com.joeldholmes.services.interfaces.IReligiousTextIndexService;
import com.joeldholmes.services.interfaces.ITaoService;
import com.joeldholmes.utils.ErrorCodes;

@Service("TaoService")
public class TaoService implements ITaoService {

	@Autowired
	IVerseRepository verseRepository;

	@Autowired
	IReligiousTextIndexService indexService;

	private final int MAX_CHAPTER_SIZE=81;

	@Override
	public List<TaoVerseResource> getVerses(Integer chapter, Integer verse, Integer throughChapter, Integer throughVerse) throws ServiceException {
		if(chapter == null){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Chapter cannot be null");
		}
		if(chapter == throughChapter){
			throughChapter = null;
		}
		if((throughChapter!=null) && (chapter > throughChapter)){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Start Chapter cannot be less than end chapter");
		}


		List<TaoVerseResource> dtos = new ArrayList<TaoVerseResource>();



		if((chapter < 1) || (chapter > MAX_CHAPTER_SIZE)){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not exist");
		}

		if((chapter != null) && (verse == null) && (throughChapter == null) && (throughVerse == null)){
			return getVersesInChapter(chapter);
		}
		else if((chapter != null) && (verse == null) && (throughChapter != null) && (throughVerse == null)){

			List<VerseEntity> versesInChapter = verseRepository.getTaoVersesInChapterRange(chapter, throughChapter);
			dtos.addAll(convertEntitiesToDTOs(versesInChapter));
			return dtos;
		}
		else if((throughChapter == null) && (throughVerse == null)){

			TaoVerseResource singleDTO = getSingleVerse(chapter, verse);
			dtos.add(singleDTO);
			return dtos;
		}
		else if((throughChapter == null) && (throughVerse != null)){
			if(throughVerse<verse){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Start verse cannot be less than end verse");
			}
			int maxVerseSize = indexService.maxTaoChapterVerses(chapter);

			if((verse < 1) || (verse > maxVerseSize)){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not contain verse");
			}

			if((throughVerse < 1) || (throughVerse > maxVerseSize)){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not contain verse");
			}

			List<VerseEntity> versesInChapter = verseRepository.getTaoVersesInChapter(chapter, verse, throughVerse);

			dtos.addAll(convertEntitiesToDTOs(versesInChapter));
			return dtos;
		}
		else if((throughChapter != null) && (throughVerse != null)){
			if(verse==null){
				verse=1;
			}

			if((chapter < 1) || (chapter > MAX_CHAPTER_SIZE)){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not exist in book");
			}
			if((throughChapter < 1) || (throughChapter > MAX_CHAPTER_SIZE)){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not exist in book");
			}

			int versesInChapter = indexService.maxTaoChapterVerses(chapter);
			if((verse < 1) || (verse > versesInChapter)){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not contain verse");
			}

			int versesInThroughChapter = indexService.maxTaoChapterVerses(chapter);
			if((throughVerse < 1) || (throughVerse > versesInThroughChapter)){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not contain verse");
			}
			//First Verse Set
			List<VerseEntity> entities = verseRepository.getTaoVersesInChapter(chapter, verse, versesInChapter);

			//End Verse Set
			entities.addAll(verseRepository.getTaoVersesInChapter(throughChapter, 1, throughVerse));

			if(throughChapter-chapter>1){
				entities.addAll(verseRepository.getTaoVersesInChapterRange(chapter+1, throughChapter-1));
			}
			dtos.addAll(convertEntitiesToDTOs(entities));
		}
		return dtos;
	}

	@Override
	public List<TaoVerseResource> getVersesInChapter(int chapter) throws ServiceException {
		if((chapter < 1) || (chapter > MAX_CHAPTER_SIZE)){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not exist in book");
		}
		List<VerseEntity> verses = verseRepository.getTaoVersesInChapter(chapter);
		return convertEntitiesToDTOs(verses);
	}

	@Override
	public TaoVerseResource getSingleVerse(int chapter, int verse) throws ServiceException {
		if((chapter < 1) || (chapter > MAX_CHAPTER_SIZE)){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not exist in book");
		}

		int maxVerseSize = indexService.maxTaoChapterVerses(chapter);

		if((verse < 1) || (verse > maxVerseSize)){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not contain verse");
		}

		VerseEntity entity = verseRepository.getSingleTaoVerse( chapter, verse);

		return new TaoVerseResource(entity);
	}

	@Override
	public List<TaoVerseResource> getVersesFromString(String verses) throws ServiceException {
		if(verses == null || verses.isEmpty()){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Verse cannot be null or empty");
		}
		
		verses = verses.replaceAll("\\s+", " ");
		List<TaoVerseResource> verseList = new ArrayList<TaoVerseResource>();
		
		String chapterVerseRegex = "([\\d:\\s]+)-?([\\d:\\s]+)?";
		Pattern chapterVersePattern =Pattern.compile(chapterVerseRegex);

		Integer startChapter = null;
		Integer startVerse = null;
		Integer endChapter = null;
		Integer endVerse = null;
		
		String[] verseArray = verses.trim().split(",");
		for(String verse: verseArray){
			verse = verse.trim();
			if(verse.matches(chapterVerseRegex)){
				Matcher m = chapterVersePattern.matcher(verse);
				if(m.matches()){
					String chapterVerse = m.group(1);
					String throughChapterVerse = m.group(2);
					String[] cvSplit = chapterVerse.split(":");
					//Chapter declaration
					if(chapterVerse.contains(":")){
						startChapter=Integer.parseInt(cvSplit[0].trim());
						if(cvSplit.length==2){
							startVerse = Integer.parseInt(cvSplit[1].trim());
						}
						else{
							startVerse = null;
						}
						endVerse = null;
						endChapter = null;
					}
					else{
						if(startChapter==null){
							startChapter = Integer.parseInt(cvSplit[0].trim());
						}
						else{
							startVerse = Integer.parseInt(cvSplit[0].trim());
						}
						
					}
					if(throughChapterVerse!=null){
						if(throughChapterVerse.contains(":")){
							cvSplit = throughChapterVerse.split(":");
							endChapter=Integer.parseInt(cvSplit[0].trim());
							if(cvSplit.length==2){
								endVerse = Integer.parseInt(cvSplit[1].trim());
							}
							else{
								endVerse = null;
							}
						}
						else{
							if(startVerse==null){
								endChapter = Integer.parseInt(throughChapterVerse.trim());
							}
							else{
								endVerse = Integer.parseInt(throughChapterVerse.trim());
							}
						}
					}
				}
			}
			else{
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Improperly formatted verse request");
			}
			//get verses here.
			verseList.addAll(getVerses(startChapter, startVerse, endChapter, endVerse));
			if(endChapter!=null)
				startChapter = endChapter;
			if(endVerse!=null)
				startVerse = endVerse;
			if(startVerse==null){
				startChapter=null;	
			}
			endChapter = null;
			endVerse = null;
			
		}
		
		return verseList;
	}
	
	@Override
	public TaoVerseResource getVerseById(String id) throws ServiceException {
		VerseEntity verseEntity = verseRepository.getTaoVerseById(id);
		if(verseEntity == null){
			return null;
		}
		return new TaoVerseResource(verseEntity);
	}
	
	private List<TaoVerseResource> convertEntitiesToDTOs(List<VerseEntity> entities){
		List<TaoVerseResource> dtos = new ArrayList<TaoVerseResource>();
		for(VerseEntity verseEntity: entities){
			dtos.add(new TaoVerseResource(verseEntity));
		}
		Collections.sort(dtos);
		return dtos;
	}

	@Override
	public List<TaoVerseResource> getVersesByIds(List<String> ids) {
		List<VerseEntity> verseEntities = verseRepository.findAll(ids);
		if(verseEntities == null){
			return null;
		}
		List<TaoVerseResource> results = new ArrayList<TaoVerseResource>();
		for(VerseEntity verse : verseEntities){
			results.add(new TaoVerseResource(verse));
		}
		return results;
	}

}
