package com.joeldholmes.services.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
	
	private final static String FULL_REGEX = "(\\d?\\s?\\w+)\\s([\\d:]+)-?([\\d:]+)?";
	private final static String CHAPTER_VERSE_REGEX = "([\\d:]+)-?([\\d:]+)?";
	private final static String ID_REGEX = "(\\d?\\s?\\w+)\\s(\\d+):(\\d+)\\s([A-Z]+)";
	
	private final static Pattern ID_PATTERN = Pattern.compile(ID_REGEX);
	private final static Pattern FULL_REGEX_PATTERN = Pattern.compile(FULL_REGEX);
	private final static Pattern CHAPTER_VERSE_PATTERN = Pattern.compile(CHAPTER_VERSE_REGEX);

	private static final String DEFAULT_ENC = "UTF-8";
	
	@Override
	public boolean isValidVerse(String verseString){
		if(verseString == null || verseString.isEmpty()){
			return false;
		}
		verseString = decode(verseString);
		Matcher matcher = FULL_REGEX_PATTERN.matcher(verseString);
		return matcher.matches();
	}
	
	@Override
	public List<BibleVerseResource> getVersesInBook(BibleVersionEnum version, String book) throws ServiceException{
		if(version == null){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Version cannot be null");
		}
		if(book == null){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Book cannot be null");
		}
		book = book.toLowerCase().trim();
		List<BibleVerseResource> dtos = new ArrayList<BibleVerseResource>();
		
		List<VerseEntity> entities = verseRepository.getBibleVersesInBook(version.getAbbr(), book.toLowerCase());
		dtos.addAll(convertEntitiesToDTOs(entities));
		return dtos;
	}
	
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
	public List<BibleVerseResource> getVerses(BibleVersionEnum version, String book, String startChapter, String startVerse, String endChapter, String endVerse) throws ServiceException{
		
		if(version == null){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Version cannot be null");
		}
		if(book == null || book.isEmpty()){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Book cannot be null");
		}
		if(startChapter == null || startChapter.isEmpty()){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Chapter cannot be null");
		}
		Integer startChapterInt = null, startVerseInt = null, endChapterInt= null, endVerseInt = null;
		
		try{
			if(startChapter!=null && !startChapter.isEmpty()){
				startChapterInt = Integer.parseInt(startChapter);
			}
			if(startVerse!=null && !startVerse.isEmpty()){
				startVerseInt = Integer.parseInt(startVerse);
			}
			if(endChapter!=null && !endChapter.isEmpty()){
				endChapterInt = Integer.parseInt(endChapter);
			}
			if(endVerse!=null && !endVerse.isEmpty()){
				endVerseInt = Integer.parseInt(endVerse);
			}
			
			return getVerses(version, book, startChapterInt, startVerseInt, endChapterInt, endVerseInt);
			
		}catch(NumberFormatException e){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Improperly formatted Chapter or Verse");
		}
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
			version = BibleVersionEnum.NIV;
		}
		if(verses == null || verses.isEmpty()){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Verse cannot be null or empty");
		}
		verses = decode(verses);
		verses = verses.replaceAll("\\s+", " ");
		List<BibleVerseResource> verseList = new ArrayList<BibleVerseResource>();
		
		String book = null;
		Integer startChapter = null;
		Integer startVerse = null;
		Integer endChapter = null;
		Integer endVerse = null;
		
		String[] verseArray = verses.trim().split(",");
		for(String verse: verseArray){
			verse = verse.trim();
			Matcher m = FULL_REGEX_PATTERN.matcher(verse);	
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
			else if(verse.matches(CHAPTER_VERSE_REGEX)){
				m = CHAPTER_VERSE_PATTERN.matcher(verse);
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
		if(id == null || id.trim().isEmpty()){
			return null;
		}
		id = id.trim();
		try {
			id = URLDecoder.decode(id, DEFAULT_ENC);
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Unexpected encoding error");
		}
		
		Matcher matcher = ID_PATTERN.matcher(id);
		if(matcher.matches()){
			String book = matcher.group(1).trim();
			Integer chapter = Integer.parseInt(matcher.group(2));
			Integer verse = Integer.parseInt(matcher.group(3));
			String versionString = matcher.group(4);
			BibleVersionEnum version = BibleVersionEnum.findByAbbreviation(versionString);
			if(version == null){
				throw new ServiceException(ErrorCodes.INVALID_INPUT, "Invalid Bible Version");
			}
			return getSingleVerse(version, book, chapter, verse);
		}
		else{
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Invalid Bible Verse Id");
		}
		
		
	}
	
	@Override
	public List<BibleVerseResource> getVersesByIds(Iterable<String> ids) throws ServiceException {
		if(ids == null){
			return null;
		}
		
		List<BibleVerseResource> results = new ArrayList<BibleVerseResource>();
		for(String id : ids){
			results.add(getVerseById(id));
		}
		return results;
	}
	
	private String decode(String encoded) throws ServiceException{
		try {
			return URLDecoder.decode(encoded, DEFAULT_ENC);
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Unexpected encoding error");
		}
	}


}
