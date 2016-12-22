package com.joeldholmes.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.TaoVerseResource;
import com.joeldholmes.services.interfaces.ITaoService;
import com.joeldholmes.utils.QueryParamUtils;

import io.katharsis.queryParams.QueryParams;

@Repository
public class TaoVerseRepository {

	@Autowired
	ITaoService taoService;
	
	public TaoVerseResource findOne(String id) throws ServiceException{
		return taoService.getVerseById(id);
	}
	
	public List<TaoVerseResource> findAll(List<String> ids, QueryParams params) {
		return taoService.getVersesByIds(ids);
	}
	
	public List<TaoVerseResource> findAll(QueryParams params) throws ServiceException{
		Map<String, Set<String>> filterParams = QueryParamUtils.getFilters(TaoVerseResource.class, params);
		if(filterParams.isEmpty()){
			return null;
		}
		
		List<TaoVerseResource> resources = new ArrayList<TaoVerseResource>();
		Set<String> verses = filterParams.get("displayVerse");
		Set<String> ids = filterParams.get("id");
		
		
		if(verses!=null && !verses.isEmpty()){
			for(String verse: verses){
				resources.addAll(taoService.getVersesFromString(verse));
			}
		}
		else if(ids!=null && !ids.isEmpty()){
			List<String> idList = new ArrayList<String>();
			for(String id : ids){
				idList.addAll(Arrays.asList(id.split(",")));
			}
			resources =  taoService.getVersesByIds(idList);
		}
		return resources;
	}
}
