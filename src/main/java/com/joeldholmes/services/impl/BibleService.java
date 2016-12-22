package com.joeldholmes.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joeldholmes.entity.VerseEntity;
import com.joeldholmes.enums.BibleVersionEnum;
import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.repository.IVerseRepository;
import com.joeldholmes.resources.BibleVerseResource;
import com.joeldholmes.services.interfaces.IBibleService;
import com.joeldholmes.services.interfaces.IReligiousTextIndexService;
import com.joeldholmes.utils.ErrorCodes;

@Service("BibleService")
public class BibleService implements IBibleService{
	
	@Autowired
	IVerseRepository verseRepository;
	
	@Autowired
	IReligiousTextIndexService indexService;
	
	@Override
	public List<BibleVerseResource> getVersesInChapter(BibleVersionEnum version, String book, int chapter) throws ServiceException{
		if(version == null){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Version cannot be null");
		}
		if(book == null){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Book cannot be null");
		}
		book = book.toLowerCase().trim();
		List<BibleVerseResource> dtos = new ArrayList<BibleVerseResource>();
		
		int chapterSize = indexService.maxBibleBookChapters(book);
		
		if((chapter < 1) || (chapter > chapterSize)){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not exist in book");
		}
		List<VerseEntity> versesInChapter = verseRepository.getBibleVersesInChapter(version.getAbbr(), book, chapter);
			
		dtos.addAll(convertEntitiesToDTOs(versesInChapter));
		return dtos;
	}
	
	
	@Override
	public List<BibleVerseResource> getVerses(BibleVersionEnum version, String book, Integer chapter, Integer verse, Integer throughChapter, Integer throughVerse) throws ServiceException{
		if(version == null){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Version cannot be null");
		}
		if(book == null){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Book cannot be null");
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
		
		book = book.toLowerCase().trim();
		
		List<BibleVerseResource> dtos = new ArrayList<BibleVerseResource>();
	
		
		int chapterSize = indexService.maxBibleBookChapters(book);
		
		if((chapter < 1) || (chapter > chapterSize)){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not exist in book");
		}
		
		if((chapter != null) && (verse == null) && (throughChapter == null) && (throughVerse == null)){
			return getVersesInChapter(version, book, chapter);
		}
		else if((chapter != null) && (verse == null) && (throughChapter != null) && (throughVerse == null)){
			
			List<VerseEntity> versesInChapter = verseRepository.getBibleVersesInChapterRange(version.getAbbr(), book, chapter, throughChapter);
			dtos.addAll(convertEntitiesToDTOs(versesInChapter));
			return dtos;
		}
		else if((throughChapter == null) && (throughVerse == null)){
			
			BibleVerseResource singleDTO = getSingleVerse(version, book, chapter, verse);
			dtos.add(singleDTO);
			return dtos;
		}
		else if((throughChapter == null) && (throughVerse != null)){
			if(throughVerse<verse){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Start verse cannot be less than end verse");
			}
			int maxVerseSize = indexService.maxBibleBookChapterVerses(book, chapter);
			
			if((verse < 1) || (verse > maxVerseSize)){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not contain verse");
			}
			
			if((throughVerse < 1) || (throughVerse > maxVerseSize)){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not contain verse");
			}
			
			List<VerseEntity> versesInChapter = verseRepository.getBibleVersesInChapter(version.getAbbr(), book, chapter, verse, throughVerse);
			
			dtos.addAll(convertEntitiesToDTOs(versesInChapter));
			return dtos;
		}
		else if((throughChapter != null) && (throughVerse != null)){
			if(verse==null){
				verse=1;
			}
			
			if((chapter < 1) || (chapter > chapterSize)){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not exist in book");
			}
			if((throughChapter < 1) || (throughChapter > chapterSize)){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not exist in book");
			}
			
			int versesInChapter = indexService.maxBibleBookChapterVerses(book, chapter);
			if((verse < 1) || (verse > versesInChapter)){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not contain verse");
			}
			
			int versesInThroughChapter = indexService.maxBibleBookChapterVerses(book, throughChapter);
			if((throughVerse < 1) || (throughVerse > versesInThroughChapter)){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not contain verse");
			}
			//First Verse Set
			List<VerseEntity> entities = verseRepository.getBibleVersesInChapter(version.getAbbr(), book, chapter, verse, versesInChapter);
			
			//End Verse Set
			 entities.addAll(verseRepository.getBibleVersesInChapter(version.getAbbr(), book, throughChapter, 1, throughVerse));
			 
			 if(throughChapter-chapter>1){
				 entities.addAll(verseRepository.getBibleVersesInChapterRange(version.getAbbr(), book, chapter+1, throughChapter-1));
			 }
			 dtos.addAll(convertEntitiesToDTOs(entities));
		}
		return dtos;
	}
	
	@Override
	public List<BibleVerseResource> getVersesFromString(BibleVersionEnum version, String verses) throws ServiceException{
		if(version == null){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Version cannot be null");
		}
		if(verses == null || verses.isEmpty()){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Verse cannot be null or empty");
		}
		
		verses = verses.replaceAll("\\s+", " ");
		List<BibleVerseResource> verseList = new ArrayList<BibleVerseResource>();
		
		String fullRegex = "(\\d?\\s?\\w+)\\s([\\d:]+)-?([\\d:]+)?";
		String chapterVerseRegex = "([\\d:]+)-?([\\d:]+)?";
		
		Pattern fullRegexPattern = Pattern.compile(fullRegex);
		Pattern chapterVersePattern =Pattern.compile(chapterVerseRegex);
		
		String book = null;
		Integer startChapter = null;
		Integer startVerse = null;
		Integer endChapter = null;
		Integer endVerse = null;
		
		String[] verseArray = verses.trim().split(",");
		for(String verse: verseArray){
			verse = verse.trim();
			Matcher m = fullRegexPattern.matcher(verse);	
			if(m.matches()){
				book = m.group(1);
				String[] cv = m.group(2).split(":");
				startChapter = Integer.parseInt(cv[0]);
				if(cv.length==2){
					startVerse = Integer.parseInt(cv[1]);
				}
				else if(cv.length>2){
					throw new ServiceException(ErrorCodes.INVALID_INPUT, "Improperly formatted verse request");
				}
				if(m.group(3)!=null){
					cv = m.group(3).split(":");
					if(cv.length==1){
						if(startVerse == null)
							endChapter = Integer.parseInt(cv[0]);
						else{
							endVerse = Integer.parseInt(cv[0]);
						}
					}
					else if(cv.length==2){
						endChapter = Integer.parseInt(cv[0]);
						endVerse = Integer.parseInt(cv[1]);
					}
					else{
						throw new ServiceException(ErrorCodes.INVALID_INPUT, "Improperly formatted verse request");
					}
				}
			}
			else if(verse.matches(chapterVerseRegex)){
				m = chapterVersePattern.matcher(verse);
				if(m.matches()){
					String[] nextCv = m.group(1).split(":");
					if(startVerse!=null){
						if(nextCv.length==1){
							startVerse = Integer.parseInt(nextCv[0]);
						}
						else if(nextCv.length==2){
							startChapter = Integer.parseInt(nextCv[0]);
							startVerse = Integer.parseInt(nextCv[1]);
						}
						else{
							throw new ServiceException(ErrorCodes.INVALID_INPUT, "Improperly formatted verse request");
						}
					}
					else{
						if(nextCv.length==1){
							startVerse = Integer.parseInt(nextCv[0]);
						}
						else if(nextCv.length==2){
							startChapter = Integer.parseInt(nextCv[0]);
							startVerse = Integer.parseInt(nextCv[1]);
						}
						else{
							throw new ServiceException(ErrorCodes.INVALID_INPUT, "Improperly formatted verse request");
						}
					}
					if(m.group(2)!=null){
						nextCv = m.group(2).split(":");
						if(startChapter!=null && startVerse!=null){
							if(nextCv.length==1){
								endVerse = Integer.parseInt(nextCv[0]);
							}
							else if(nextCv.length==2){
								endChapter = Integer.parseInt(nextCv[0]);
								endVerse = Integer.parseInt(nextCv[1]);
							}
							else{
								throw new ServiceException(ErrorCodes.INVALID_INPUT, "Improperly formatted verse request");
							}
						}
						else if(startChapter!=null){
							if(nextCv.length==1){
								endChapter = Integer.parseInt(nextCv[0]);
							}
							else if(nextCv.length==2){
								endChapter = Integer.parseInt(nextCv[0]);
								endVerse = Integer.parseInt(nextCv[1]);
							}
							else{
								return null;
							}
						}
						else if(startVerse!=null){
							if(nextCv.length==1){
								endVerse = Integer.parseInt(nextCv[0]);
							}
							else if(nextCv.length==2){
								endChapter = Integer.parseInt(nextCv[0]);
								endVerse = Integer.parseInt(nextCv[1]);
							}
							else{
								throw new ServiceException(ErrorCodes.INVALID_INPUT, "Improperly formatted verse request");
							}
						}
					}
				}
			}
			else{
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Improperly formatted verse request");
			}
			//get verses here.
			verseList.addAll(getVerses(version, book, startChapter, startVerse, endChapter, endVerse));
			if(endChapter!=null)
				startChapter = endChapter;
			if(endVerse!=null)
				startVerse = endVerse;
			endChapter = null;
			endVerse = null;
			
		}
		
		return verseList;
	}
	
	@Override
	public List<BibleVerseResource> getVersesFromString(String verses) throws ServiceException{
		return getVersesFromString(BibleVersionEnum.NIV, verses);
	}
	
	@Override
	public BibleVerseResource getSingleVerse(BibleVersionEnum version, String book, int chapter, int verse) throws ServiceException{
		
		if(version == null){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Version cannot be null");
		}
		if(book == null || book.isEmpty()){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Book cannot be null");
		}
		
		book = book.toLowerCase().trim();
		
		int chapterSize = indexService.maxBibleBookChapters(book);
		
		if((chapter < 1) || (chapter > chapterSize)){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not exist in book");
		}
		
		int maxVerseSize = indexService.maxBibleBookChapterVerses(book, chapter);
		
		if((verse < 1) || (verse > maxVerseSize)){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter does not contain verse");
		}
		
		VerseEntity entity = verseRepository.getSingleBibleVerse(version.getAbbr(), book, chapter, verse);
		
		return new BibleVerseResource(entity);
		
	}
	
	@Deprecated
	@Override
	public List<BibleVerseResource> getVersesInRange(BibleVersionEnum version, String book, Integer chapter, Integer verse,
			Integer throughChapter, Integer throughVerse) throws ServiceException {

		return getVerses(version, book, chapter, verse, throughChapter, throughVerse);
	}
	
	private List<BibleVerseResource> convertEntitiesToDTOs(List<VerseEntity> entities){
		List<BibleVerseResource> dtos = new ArrayList<BibleVerseResource>();
		for(VerseEntity verseEntity: entities){
			dtos.add(new BibleVerseResource(verseEntity));
		}
		Collections.sort(dtos);
		return dtos;
	}


	@Override
	public BibleVerseResource getVerseById(String id) throws ServiceException {
		VerseEntity verseEntity = verseRepository.getBibleVerseById(id);
		if(verseEntity == null){
			return null;
		}
		return new BibleVerseResource(verseEntity);
	}
	
	@Override
	public List<BibleVerseResource> getVersesByIds(List<String> ids) throws ServiceException {
		List<VerseEntity> verseEntities = verseRepository.findAll(ids);
		if(verseEntities == null){
			return null;
		}
		List<BibleVerseResource> results = new ArrayList<BibleVerseResource>();
		for(VerseEntity verse : verseEntities){
			results.add(new BibleVerseResource(verse));
		}
		return results;
	}


}
