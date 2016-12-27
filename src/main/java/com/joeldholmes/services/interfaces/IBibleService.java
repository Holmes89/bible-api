package com.joeldholmes.services.interfaces;

import java.util.List;

import com.joeldholmes.enums.BibleVersionEnum;
import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.BibleVerseResource;

public interface IBibleService {

	@Deprecated
	List<BibleVerseResource> getVersesInRange(BibleVersionEnum version, String book, Integer chapter, Integer verse,	Integer throughChapter, Integer throughVerse) throws ServiceException;

	List<BibleVerseResource> getVersesInChapter(BibleVersionEnum version, String book, int chapter) throws ServiceException;

	List<BibleVerseResource> getVersesFromString(String verses) throws ServiceException;

	List<BibleVerseResource> getVersesFromString(BibleVersionEnum version, String verses) throws ServiceException;

	List<BibleVerseResource> getVerses(BibleVersionEnum version, String book, Integer chapter, Integer verse,
			Integer throughChapter, Integer throughVerse) throws ServiceException;

	BibleVerseResource getSingleVerse(BibleVersionEnum version, String book, int chapter, int verse) throws ServiceException;

	BibleVerseResource getVerseById(String id) throws ServiceException;

	List<BibleVerseResource> getVersesByIds(List<String> id) throws ServiceException;

	List<BibleVerseResource> getVerses(BibleVersionEnum version, String book, String startChapter, String startVerse,
			String endChapter, String endVerse) throws ServiceException;
}
