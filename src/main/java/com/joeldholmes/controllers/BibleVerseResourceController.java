package com.joeldholmes.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.repository.BibleVerseRepository;
import com.joeldholmes.resources.BibleVerseResource;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.annotations.JsonApiFindAll;
import io.katharsis.repository.annotations.JsonApiFindOne;
import io.katharsis.repository.annotations.JsonApiResourceRepository;

@Component
@RestController
@JsonApiResourceRepository(BibleVerseResource.class)
public class BibleVerseResourceController {

	@Autowired
	BibleVerseRepository bibleRepository;

	@HystrixCommand(commandKey="BibleVerseFindOne", groupKey="BibleVerse", threadPoolKey="BibleVerse")
	@JsonApiFindOne
	public BibleVerseResource findOne(String id) throws ServiceException{
		return bibleRepository.findOne(id);
	}
	
	@HystrixCommand(commandKey="BibleVerseFindAll", groupKey="BibleVerse", threadPoolKey="BibleVerse")
	@JsonApiFindAll
	public Iterable<BibleVerseResource> findAll(List<String> ids, QueryParams params) throws ServiceException{
		return bibleRepository.findAll(ids, params);
	}
	
	@HystrixCommand(commandKey="BibleVerseFindAll", groupKey="BibleVerse", threadPoolKey="BibleVerse")
	@JsonApiFindAll
	public Iterable<BibleVerseResource> findAll(QueryParams params) throws ServiceException{
		return bibleRepository.findAll(params);
	}
}
