package com.joeldholmes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.repository.BibleVerseRepository;
import com.joeldholmes.resources.BibleVerseResource;
import com.joeldholmes.services.interfaces.IBibleService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.katharsis.queryspec.QuerySpec;
import io.katharsis.repository.ResourceRepositoryBase;
import io.katharsis.repository.annotations.JsonApiFindAll;
import io.katharsis.repository.annotations.JsonApiFindOne;
import io.katharsis.resource.list.ResourceList;

@Component
@RestController
public class BibleVerseResourceController extends ResourceRepositoryBase<BibleVerseResource, String> {

	@Autowired
	BibleVerseRepository bibleRepository;
	
	@Autowired
	IBibleService bibleService;
	
	public BibleVerseResourceController(){
		super(BibleVerseResource.class);
	}

	@HystrixCommand(commandKey="BibleVerseFindOne", groupKey="BibleVerse", threadPoolKey="BibleVerse")
	@JsonApiFindOne
	public BibleVerseResource findOne(String id, QuerySpec querySpec){
		return bibleService.getVerseById(id);
	}
	
	@HystrixCommand(commandKey="BibleVerseFindAll", groupKey="BibleVerse", threadPoolKey="BibleVerse")
	@JsonApiFindAll
	public ResourceList<BibleVerseResource> findAll(Iterable<String> ids, QuerySpec querySpec) throws ServiceException{
		return querySpec.apply(bibleService.getVersesByIds(ids));
	}
	
	@HystrixCommand(commandKey="BibleVerseFindAll", groupKey="BibleVerse", threadPoolKey="BibleVerse")
	public ResourceList<BibleVerseResource> findAll(QuerySpec querySpec) {
		return querySpec.apply(bibleRepository.findAll(querySpec));
	}
}
