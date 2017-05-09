package com.joeldholmes.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.joeldholmes.enums.BibleVersionEnum;
import com.joeldholmes.exceptions.APIException;
import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.BibleVerseResource;
import com.joeldholmes.services.interfaces.IBibleService;
import com.joeldholmes.utils.ErrorCodes;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.katharsis.queryspec.FilterSpec;
import io.katharsis.queryspec.QuerySpec;
import io.katharsis.repository.ResourceRepositoryBase;
import io.katharsis.resource.list.ResourceList;

@Component
public class BibleVerseResourceController extends ResourceRepositoryBase<BibleVerseResource, String> {

	@Autowired
	IBibleService bibleService;
	
	public BibleVerseResourceController(){
		super(BibleVerseResource.class);
	}

//	@HystrixCommand(commandKey="BibleVerseFindOne", groupKey="BibleVerse", threadPoolKey="BibleVerse")
	public BibleVerseResource findOne(String id, QuerySpec querySpec){
		return bibleService.getVerseById(id);
	}
	
	//@HystrixCommand(commandKey="BibleVerseFindAll", groupKey="BibleVerse", threadPoolKey="BibleVerse")
	public ResourceList<BibleVerseResource> findAll(Iterable<String> ids, QuerySpec querySpec) throws ServiceException{
		return querySpec.apply(bibleService.getVersesByIds(ids));
	}
	
//	@HystrixCommand(commandKey="BibleVerseFindAll", groupKey="BibleVerse", threadPoolKey="BibleVerse")
	public ResourceList<BibleVerseResource> findAll(QuerySpec querySpec) {
		List<BibleVerseResource> resources = new ArrayList<BibleVerseResource>(); 
		List<String> books = new ArrayList<String>();
		Set<BibleVersionEnum> versions = new HashSet<BibleVersionEnum>();
		
		List<FilterSpec> filters = querySpec.getFilters();
		for(FilterSpec filter: filters){
			String attribute = filter.getAttributePath().iterator().next();
			if(attribute.equalsIgnoreCase("book")){
				books.add((String) filter.getValue());
			}
			else if(attribute.equalsIgnoreCase("version")){
				String versionString = (String) filter.getValue();
				BibleVersionEnum version = BibleVersionEnum.findByAbbreviation(versionString);
				if(version != null)
					versions.add(version);
			}
		}
		
		if(books.isEmpty()){
			throw new APIException(ErrorCodes.NULL_INPUT, "Book Name is required", HttpStatus.BAD_REQUEST);
		}
		
		if(versions.isEmpty()){
			versions = Collections.singleton(BibleVersionEnum.NIV);
		}
		
		for(String book: books){
			for(BibleVersionEnum version: versions){
				resources.addAll(bibleService.getVersesInBook(version, book));
			}
		}
		
		return querySpec.apply(resources);
	}
}
