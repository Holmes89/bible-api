package com.joeldholmes.services.interfaces;

import java.util.List;

import com.joeldholmes.enums.BibleVersionEnum;
import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.BibleVerseResource;

public interface ISearchService {
	
	List<BibleVerseResource> searchBibleText(String term) throws ServiceException;
	
	List<BibleVerseResource> searchBibleText(BibleVersionEnum version, String term) throws ServiceException;
}
