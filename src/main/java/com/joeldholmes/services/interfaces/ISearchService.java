package com.joeldholmes.services.interfaces;

import java.util.List;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.BibleVerseResource;

public interface ISearchService {
	
	List<BibleVerseResource> searchBibleText(String term) throws ServiceException;
}
