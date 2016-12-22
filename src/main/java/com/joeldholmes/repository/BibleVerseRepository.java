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
		Map<String, Set<String>> filterParams = QueryParamUtils.getFilters(BibleVerseResource.class, params);
		if(filterParams.isEmpty()){
			return null;
		}
		
		BibleVersionEnum version;
		if(!filterParams.containsKey("version")){
			version = BibleVersionEnum.NIV;
		}
		else{
			version = BibleVersionEnum.findByAbbreviation(filterParams.get("version").iterator().next());
		}
		
		List<BibleVerseResource> resources = new ArrayList<BibleVerseResource>();
		
		Set<String> verses = filterParams.get("displayVerse");
		Set<String> ids = filterParams.get("id");
		
		if(verses!=null && !verses.isEmpty()){
			for(String verse: verses){
				resources.addAll(bibleService.getVersesFromString(version, verse));
			}
		}
		else if(ids!=null && !ids.isEmpty()){
			List<String> idList = new ArrayList<String>();
			for(String id : ids){
				idList.addAll(Arrays.asList(id.split(",")));
			}
			resources =  bibleService.getVersesByIds(idList);
		}
		return resources;
	}
}
