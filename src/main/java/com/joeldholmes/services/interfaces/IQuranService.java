package com.joeldholmes.services.interfaces;

import java.util.List;

import com.joeldholmes.enums.QuranVersionEnum;
import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.QuranVerseResource;

public interface IQuranService {
	
	List<QuranVerseResource> getVersesInChapter(QuranVersionEnum version, String chapterName) throws ServiceException;
	
	List<QuranVerseResource> getVersesInChapter(QuranVersionEnum version, int chapter) throws ServiceException;

	
	List<QuranVerseResource> getVersesFromString(String verses) throws ServiceException;

	List<QuranVerseResource> getVersesFromString(QuranVersionEnum version, String verses) throws ServiceException;

	List<QuranVerseResource> getVerses(QuranVersionEnum version, Integer chapter, Integer verse,
			Integer throughChapter, Integer throughVerse) throws ServiceException;
	
	List<QuranVerseResource> getVerses(QuranVersionEnum version, String chapterName,  Integer verse,
			String throughChapterName, Integer throughVerse) throws ServiceException;

	QuranVerseResource getSingleVerse(QuranVersionEnum version,  int chapter, int verse) throws ServiceException;
	
	QuranVerseResource getSingleVerse(QuranVersionEnum version,  String chapterName, int verse) throws ServiceException;

	QuranVerseResource getSingleVerse(int chapter, int verse) throws ServiceException;
	
	QuranVerseResource getSingleVerse(String chapterName, int verse) throws ServiceException;

	List<QuranVerseResource> getVersesInChapter(int chapter) throws ServiceException;
	
	List<QuranVerseResource> getVersesInChapter(String chapterName) throws ServiceException;

	QuranVerseResource getVerseById(String id) throws ServiceException;

	List<QuranVerseResource> getVersesByIds(List<String> ids);

}
