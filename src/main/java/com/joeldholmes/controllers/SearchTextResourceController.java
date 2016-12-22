package com.joeldholmes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.repository.SearchTextRepository;
import com.joeldholmes.resources.SearchTextResource;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.annotations.JsonApiFindAll;
import io.katharsis.repository.annotations.JsonApiResourceRepository;

@Component
@JsonApiResourceRepository(SearchTextResource.class)
public class SearchTextResourceController {

	
	@Autowired
	SearchTextRepository searchRepository;
	
	@HystrixCommand(commandKey="SearchTextFindAll", groupKey="SearchText", threadPoolKey="SearchText")
	@JsonApiFindAll
	public Iterable<SearchTextResource> findAll(QueryParams params) throws ServiceException{
		return searchRepository.findAll(params);
	}
}
