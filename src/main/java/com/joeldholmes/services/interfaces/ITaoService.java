package com.joeldholmes.services.interfaces;

import java.util.List;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.TaoVerseResource;

public interface ITaoService {

	List<TaoVerseResource> getVersesInChapter(int chapter) throws ServiceException;
	List<TaoVerseResource> getVersesFromString(String verses) throws ServiceException;

	List<TaoVerseResource> getVerses(Integer chapter, Integer verse,
			Integer throughChapter, Integer throughVerse) throws ServiceException;
	

	TaoVerseResource getSingleVerse(int chapter, int verse) throws ServiceException;

	TaoVerseResource getVerseById(String id) throws ServiceException;

	List<TaoVerseResource> getVersesByIds(List<String> ids);
}
