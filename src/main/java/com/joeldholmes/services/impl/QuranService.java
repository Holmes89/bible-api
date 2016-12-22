package com.joeldholmes.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joeldholmes.entity.VerseEntity;
import com.joeldholmes.enums.QuranVersionEnum;
import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.repository.IVerseRepository;
import com.joeldholmes.resources.BibleVerseResource;
import com.joeldholmes.resources.QuranVerseResource;
import com.joeldholmes.services.interfaces.IQuranService;
import com.joeldholmes.services.interfaces.IReligiousTextIndexService;
import com.joeldholmes.utils.ErrorCodes;

@Service("QuranService")
public class QuranService implements IQuranService {

	@Autowired
	IVerseRepository verseRepository;
	
	@Autowired
	IReligiousTextIndexService indexService;
	
	private final int MAX_CHAPTER_SIZE = 114;
		
	@Override
	public List<QuranVerseResource> getVersesInChapter(QuranVersionEnum version, int chapter) throws ServiceException {
		if(version == null){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Version cannot be null");
		}
		if((chapter < 1) || (chapter > MAX_CHAPTER_SIZE)){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not exist in book");
		}
		List<VerseEntity> verses = verseRepository.getQuranVersesInChapter(version.getName(), chapter);
		return convertEntitiesToDTOs(verses);
	}
	
	@Override
	public List<QuranVerseResource> getVersesInChapter(QuranVersionEnum version, String chapterName) throws ServiceException {
		if(chapterName==null || chapterName.isEmpty()){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Chapter Name cannot be null");
		}
		int chapter = indexService.quranChapterNameLookup(chapterName);
		return getVersesInChapter(version, chapter);
	}

	@Override
	public List<QuranVerseResource> getVersesInChapter(int chapter) throws ServiceException {
		return getVersesInChapter(QuranVersionEnum.SHAKIR, chapter);
	}

	@Override
	public List<QuranVerseResource> getVersesInChapter(String chapterName) throws ServiceException {
		return getVersesInChapter(QuranVersionEnum.SHAKIR, chapterName);
	}

	@Override
	public List<QuranVerseResource> getVersesFromString(String verses) throws ServiceException {
		return getVersesFromString(QuranVersionEnum.SHAKIR, verses);
	}

	@Override
	public List<QuranVerseResource> getVersesFromString(QuranVersionEnum version, String verses) throws ServiceException {
		if(version == null){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Version cannot be null");
		}
		if(verses == null || verses.isEmpty()){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Verse cannot be null or empty");
		}
		
		verses = verses.replaceAll("\\s+", " ");
		verses = sanitizeVerseString(verses);
		List<QuranVerseResource> verseList = new ArrayList<QuranVerseResource>();
		
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
			verseList.addAll(getVerses(version, startChapter, startVerse, endChapter, endVerse));
			if(endChapter!=null)
				startChapter = endChapter;
			if(endVerse!=null)
				startVerse = endVerse;
			if(startVerse == null){
				startChapter=null;
			}
			endChapter = null;
			endVerse = null;
			
		}
		
		return verseList;
	}

	@Override
	public List<QuranVerseResource> getVerses(QuranVersionEnum version, Integer chapter, Integer verse, Integer throughChapter, Integer throughVerse) throws ServiceException {
		if(version == null){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Version cannot be null");
		}
		if(chapter == null){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Chapter cannot be null");
		}
		if(chapter == throughChapter){
			throughChapter = null;
		}
		if((throughChapter!=null) && (chapter > throughChapter)){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Start Chapter cannot be less than end chapter");
		}
		
		
		List<QuranVerseResource> dtos = new ArrayList<QuranVerseResource>();
	
		
		
		if((chapter < 1) || (chapter > MAX_CHAPTER_SIZE)){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not exist");
		}
		
		if((chapter != null) && (verse == null) && (throughChapter == null) && (throughVerse == null)){
			return getVersesInChapter(version, chapter);
		}
		else if((chapter != null) && (verse == null) && (throughChapter != null) && (throughVerse == null)){
			
			List<VerseEntity> versesInChapter = verseRepository.getQuranVersesInChapterRange(version.getName(), chapter, throughChapter);
			dtos.addAll(convertEntitiesToDTOs(versesInChapter));
			return dtos;
		}
		else if((throughChapter == null) && (throughVerse == null)){
			
			QuranVerseResource singleDTO = getSingleVerse(version, chapter, verse);
			dtos.add(singleDTO);
			return dtos;
		}
		else if((throughChapter == null) && (throughVerse != null)){
			if(throughVerse<verse){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Start verse cannot be less than end verse");
			}
			int maxVerseSize = indexService.maxQuranChapterVerses(chapter);
			
			if((verse < 1) || (verse > maxVerseSize)){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not contain verse");
			}
			
			if((throughVerse < 1) || (throughVerse > maxVerseSize)){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not contain verse");
			}
			
			List<VerseEntity> versesInChapter = verseRepository.getQuranVersesInChapter(version.getName(), chapter, verse, throughVerse);
			
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
			
			int versesInChapter = indexService.maxQuranChapterVerses(chapter);
			if((verse < 1) || (verse > versesInChapter)){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not contain verse");
			}
			
			int versesInThroughChapter = indexService.maxQuranChapterVerses(chapter);
			if((throughVerse < 1) || (throughVerse > versesInThroughChapter)){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not contain verse");
			}
			//First Verse Set
			List<VerseEntity> entities = verseRepository.getQuranVersesInChapter(version.getName(), chapter, verse, versesInChapter);
			
			//End Verse Set
			 entities.addAll(verseRepository.getQuranVersesInChapter(version.getName(), throughChapter, 1, throughVerse));
			 
			 if(throughChapter-chapter>1){
				 entities.addAll(verseRepository.getQuranVersesInChapterRange(version.getName(), chapter+1, throughChapter-1));
			 }
			 dtos.addAll(convertEntitiesToDTOs(entities));
		}
		return dtos;
	}
	
	@Override
	public List<QuranVerseResource> getVerses(QuranVersionEnum version, String chapterName, Integer verse, String throughChapterName, Integer throughVerse) throws ServiceException {
		int chapter = indexService.quranChapterNameLookup(chapterName);
		int throughChapter = indexService.quranChapterNameLookup(throughChapterName);
		return getVerses(version, chapter, verse, throughChapter, throughVerse);
	}

	@Override
	public QuranVerseResource getSingleVerse(QuranVersionEnum version, int chapter, int verse) throws ServiceException {
		if(version == null){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Version cannot be null");
		}
		
		
		if((chapter < 1) || (chapter > MAX_CHAPTER_SIZE)){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not exist in book");
		}
		
		int maxVerseSize = indexService.maxQuranChapterVerses(chapter);
		
		if((verse < 1) || (verse > maxVerseSize)){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not contain verse");
		}
		
		VerseEntity entity = verseRepository.getSingleQuranVerse(version.getName(),  chapter, verse);
		
		return new QuranVerseResource(entity);
		
	}

	@Override
	public QuranVerseResource getSingleVerse(QuranVersionEnum version, String chapterName, int verse) throws ServiceException {
			if(chapterName==null || chapterName.isEmpty()){
				throw new ServiceException(ErrorCodes.NULL_INPUT, "Chapter Name cannot be null");
			}
			
			int chapter = indexService.quranChapterNameLookup(chapterName);
			return getSingleVerse(version, chapter, verse);
	}
	
	@Override
	public QuranVerseResource getSingleVerse(int chapter, int verse) throws ServiceException{
		return getSingleVerse(QuranVersionEnum.SHAKIR, chapter ,verse);
	}
	
	@Override
	public QuranVerseResource getSingleVerse(String chapterName, int verse) throws ServiceException{
		return getSingleVerse(QuranVersionEnum.SHAKIR, chapterName ,verse);
	}
	
	@Override
	public QuranVerseResource getVerseById(String id) throws ServiceException {
		VerseEntity verseEntity = verseRepository.getQuranVerseById(id);
		if(verseEntity == null){
			return null;
		}
		return new QuranVerseResource(verseEntity);
	}

	private List<QuranVerseResource> convertEntitiesToDTOs(List<VerseEntity> entities){
		List<QuranVerseResource> dtos = new ArrayList<QuranVerseResource>();
		for(VerseEntity verseEntity: entities){
			dtos.add(new QuranVerseResource(verseEntity));
		}
		Collections.sort(dtos);
		return dtos;
	}
	
	private String sanitizeVerseString(String verse) throws ServiceException{
		String bookRegex = "([A-z\\s]+)";
		Pattern bookRegexPattern = Pattern.compile(bookRegex);
		String newVerse = verse;
		Matcher m = bookRegexPattern.matcher(verse);
		while(m.find()){
			String book = m.group(1).trim();
			if(book==null || book.isEmpty())
				continue;
			int chapter = indexService.quranChapterNameLookup(book.toLowerCase());
			newVerse = newVerse.replace(book, chapter+":");
			
		}
		verse = newVerse;
		return verse;
	}

	@Override
	public List<QuranVerseResource> getVersesByIds(List<String> ids) {
		List<VerseEntity> verseEntities = verseRepository.findAll(ids);
		if(verseEntities == null){
			return null;
		}
		List<QuranVerseResource> results = new ArrayList<QuranVerseResource>();
		for(VerseEntity verse : verseEntities){
			results.add(new QuranVerseResource(verse));
		}
		return results;
	}
}
