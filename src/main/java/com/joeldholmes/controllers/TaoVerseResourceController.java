package com.joeldholmes.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.repository.TaoVerseRepository;
import com.joeldholmes.resources.TaoVerseResource;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.annotations.JsonApiFindAll;
import io.katharsis.repository.annotations.JsonApiFindOne;
import io.katharsis.repository.annotations.JsonApiResourceRepository;

@Component
@RestController
@JsonApiResourceRepository(TaoVerseResource.class)
public class TaoVerseResourceController {

	@Autowired
	TaoVerseRepository taoRepository;

	@HystrixCommand(commandKey="TaoVerseFindOne", groupKey="TaoVerse", threadPoolKey="TaoVerse")
	@JsonApiFindOne
	public TaoVerseResource findOne(String id) throws ServiceException{
		return taoRepository.findOne(id);
	}
	
	@HystrixCommand(commandKey="TaoVerseFindAll", groupKey="TaoVerse", threadPoolKey="TaoVerse")
	@JsonApiFindAll
	public Iterable<TaoVerseResource> findAll(List<String> ids, QueryParams params) throws ServiceException{
		return taoRepository.findAll(ids, params);
	}
	
	@HystrixCommand(commandKey="TaoVerseFindAll", groupKey="TaoVerse", threadPoolKey="TaoVerse")
	@JsonApiFindAll
	public Iterable<TaoVerseResource> findAll(QueryParams params) throws ServiceException{
		return taoRepository.findAll(params);
	}
}
