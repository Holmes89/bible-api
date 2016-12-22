package com.joeldholmes.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.repository.QuranVerseRepository;
import com.joeldholmes.resources.QuranVerseResource;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.annotations.JsonApiFindAll;
import io.katharsis.repository.annotations.JsonApiFindOne;
import io.katharsis.repository.annotations.JsonApiResourceRepository;

@Component
@RestController
@JsonApiResourceRepository(QuranVerseResource.class)
public class QuranVerseResourceController {

	@Autowired
	QuranVerseRepository quranRepository;

	@HystrixCommand(commandKey="QuranVerseFindOne", groupKey="QuranVerse", threadPoolKey="QuranVerse")
	@JsonApiFindOne
	public QuranVerseResource findOne(String id) throws ServiceException{
		return quranRepository.findOne(id);
	}
	
	@HystrixCommand(commandKey="QuranVerseFindAll", groupKey="QuranVerse", threadPoolKey="QuranVerse")
	@JsonApiFindAll
	public Iterable<QuranVerseResource> findAll(List<String> ids, QueryParams params) throws ServiceException{
		return quranRepository.findAll(ids, params);
	}
	
	@HystrixCommand(commandKey="QuranVerseFindAll", groupKey="QuranVerse", threadPoolKey="QuranVerse")
	@JsonApiFindAll
	public Iterable<QuranVerseResource> findAll(QueryParams params) throws ServiceException{
		return quranRepository.findAll(params);
	}
}
