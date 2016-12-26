package com.joeldholmes.repository;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.SearchTextResource;
import com.joeldholmes.services.interfaces.ISearchService;
import com.joeldholmes.utils.QueryParamUtils;

import io.katharsis.queryParams.QueryParams;

@Repository
public class SearchTextRepository {

	@Autowired
	ISearchService searchService;
	
	public Iterable<SearchTextResource> findAll(QueryParams params) throws ServiceException{
		Sort sort = new Sort(Direction.DESC, Arrays.asList("score"));
		
		Map<String, String> filters = QueryParamUtils.getSingleFilters(params);
		if(!filters.containsKey("searchTerm")){
			return null;
		}
		String searchTerm = filters.get("searchTerm");
		if(searchTerm==null){
			return null;
		}
		
		searchTerm = searchTerm.trim();
		return searchService.searchBibleText(searchTerm);
	}
}
