package com.joeldholmes.services.interfaces;

import com.joeldholmes.exceptions.ServiceException;

public interface IReligiousTextIndexService {

	int maxBibleBookChapters(String book) throws ServiceException;
	int maxBibleBookChapterVerses(String book, int chapter) throws ServiceException;
	int maxQuranChapters();
	int maxQuranChapterVerses(int chapter) throws ServiceException;
	int maxTaoChapters() throws ServiceException;
	int maxTaoChapterVerses(int chapter) throws ServiceException;
	int quranChapterNameLookup(String name) throws ServiceException;
	
}
