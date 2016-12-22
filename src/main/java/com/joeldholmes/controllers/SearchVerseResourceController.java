package com.joeldholmes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.repository.SearchVerseRepository;
import com.joeldholmes.resources.SearchVerseResource;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.annotations.JsonApiFindAll;
import io.katharsis.repository.annotations.JsonApiResourceRepository;

@Component
@JsonApiResourceRepository(SearchVerseResource.class)
public class SearchVerseResourceController {
	
	@Autowired
	SearchVerseRepository searchRepository;
	
	@HystrixCommand(commandKey="SearchVerseFindAll", groupKey="SearchVerse", threadPoolKey="SearchVerse")
	@JsonApiFindAll
	public Iterable<SearchVerseResource> findAll(QueryParams params) throws ServiceException{
		return searchRepository.findAll(params);
	}
}
