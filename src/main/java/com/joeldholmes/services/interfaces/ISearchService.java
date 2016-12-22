package com.joeldholmes.services.interfaces;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.SearchTextResource;

public interface ISearchService {
	
	Iterable<SearchTextResource> searchAllText(String term, Pageable page) throws ServiceException;
	List<SearchTextResource> searchBibleText(String term) throws ServiceException;
	List<SearchTextResource> searchQuranText(String term) throws ServiceException;
	List<SearchTextResource> searchTaoText(String term) throws ServiceException;
}
