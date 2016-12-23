package com.joeldholmes.services.interfaces;

import com.joeldholmes.exceptions.ServiceException;

public interface IReligiousTextIndexService {

	int maxBibleBookChapters(String book) throws ServiceException;
	int maxBibleBookChapterVerses(String book, int chapter) throws ServiceException;
	
}
