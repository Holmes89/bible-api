package com.joeldholmes.services.interfaces;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.SearchTextResource;

public interface ISearchService {
	
	List<SearchTextResource> searchBibleText(String term) throws ServiceException;
}
