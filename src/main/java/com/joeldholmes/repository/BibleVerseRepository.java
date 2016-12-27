package com.joeldholmes.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.joeldholmes.enums.BibleVersionEnum;
import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.BibleVerseResource;
import com.joeldholmes.services.interfaces.IBibleService;
import com.joeldholmes.utils.QueryParamUtils;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.queryParams.params.FilterParams;

@Repository
public class BibleVerseRepository {

	@Autowired
	IBibleService bibleService;
	
	public BibleVerseResource findOne(String id) throws ServiceException{
		return bibleService.getVerseById(id);
	}
	
	public List<BibleVerseResource> findAll(List<String> ids, QueryParams params) throws ServiceException{
		return bibleService.getVersesByIds(ids);
	}
	
	public List<BibleVerseResource> findAll(QueryParams params) throws ServiceException{
		Map<String, String> filterParams = QueryParamUtils.getSingleFilters(params);
		if(filterParams.isEmpty()){
			return null;
		}
		
		BibleVersionEnum version;
		if(!filterParams.containsKey("version")){
			version = BibleVersionEnum.NIV;
		}
		else{
			version = BibleVersionEnum.findByAbbreviation(filterParams.get("version"));
		}
		
		List<BibleVerseResource> resources = new ArrayList<BibleVerseResource>();
			
		if(filterParams.containsKey("displayVerse")){
			resources.addAll(bibleService.getVersesFromString(version, filterParams.get("displayVerse")));
		}
		else if(filterParams.containsKey("book")
				&& filterParams.containsKey("startChapter")
				&& filterParams.containsKey("endChapter")
				&& filterParams.containsKey("startVerse")
				&& filterParams.containsKey("endVerse")){
			
		}
		else if(filterParams.containsKey("book")
				&& filterParams.containsKey("startChapter")
				&& filterParams.containsKey("startVerse")
				&& filterParams.containsKey("endVerse")){
			
		}
		else if(filterParams.containsKey("book")
				&& filterParams.containsKey("startChapter")
				&& filterParams.containsKey("endChapter")){
			
		}
		else if(filterParams.containsKey("book") 
				&& filterParams.containsKey("chapter") 
				&& filterParams.containsKey("verse")){
			
		}
		else if(filterParams.containsKey("book") && filterParams.containsKey("chapter")){
			
		}
		else if(filterParams.containsKey("book")){
			
		}
		
		return resources;
	}
}
