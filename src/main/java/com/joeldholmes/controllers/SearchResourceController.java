package com.joeldholmes.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.joeldholmes.enums.BibleVersionEnum;
import com.joeldholmes.exceptions.APIException;
import com.joeldholmes.resources.BibleVerseResource;
import com.joeldholmes.resources.SearchResource;
import com.joeldholmes.services.interfaces.IBibleService;
import com.joeldholmes.services.interfaces.ISearchService;
import com.joeldholmes.utils.ErrorCodes;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.katharsis.queryspec.FilterSpec;
import io.katharsis.queryspec.QuerySpec;
import io.katharsis.repository.ResourceRepositoryBase;
import io.katharsis.resource.list.ResourceList;

@Component
public class SearchResourceController extends ResourceRepositoryBase<SearchResource, String> {

	@Autowired
	IBibleService bibleService;
	
	@Autowired
	ISearchService searchService;
	
	public SearchResourceController(){
		super(SearchResource.class);
	}

//	@HystrixCommand(commandKey="SearchFindOne", groupKey="Search", threadPoolKey="Search")
	public SearchResource findOne(String id, QuerySpec querySpec){
		
		BibleVersionEnum version = getVersion(querySpec);
		List<BibleVerseResource> verseResults = null;
		List<BibleVerseResource> termResults = null;
		
		SearchResource resource = new SearchResource();
		resource.search = id;
		resource.hasTermSearchResults = false;
		resource.hasVerseSearchResults = false;
		resource.totalCount = 0;
		resource.verses = new ArrayList<BibleVerseResource>();
		
		if(bibleService.isValidVerse(id)){
			verseResults = bibleService.getVersesFromString(version, id);
		}
		termResults = searchService.searchBibleText(version, id);
		
		if(verseResults != null){
			resource.hasVerseSearchResults = true;
			resource.verses.addAll(verseResults);
		}
		if(termResults != null){
			resource.hasTermSearchResults = true;
			resource.verses.addAll(termResults);
		}
		
		resource.populate();
		
		return resource;
	}
	
	@HystrixCommand(commandKey="SearchFindAll", groupKey="Search", threadPoolKey="Search")
	public ResourceList<SearchResource> findAll(QuerySpec querySpec) {
		throw new APIException(ErrorCodes.NULL_INPUT, "Not Implemented", HttpStatus.NOT_IMPLEMENTED);	
	}
	
	public BibleVersionEnum getVersion(QuerySpec querySpec){
		for(FilterSpec filter: querySpec.getFilters()){
			String attribute = filter.getAttributePath().iterator().next();
			if(attribute.equalsIgnoreCase("version")){
				String versionString = (String) filter.getValue();
				BibleVersionEnum version = BibleVersionEnum.findByAbbreviation(versionString);
				if(version != null)
					return version;
			}
		}
		return null;
	}
}
