package com.joeldholmes.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.joeldholmes.enums.BibleVersionEnum;
import com.joeldholmes.enums.QuranVersionEnum;
import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.BibleVerseResource;
import com.joeldholmes.resources.QuranVerseResource;
import com.joeldholmes.services.interfaces.IQuranService;
import com.joeldholmes.utils.QueryParamUtils;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.queryParams.params.FilterParams;

@Repository
public class QuranVerseRepository {

	@Autowired
	IQuranService quranService;
	
	public QuranVerseResource findOne(String id) throws ServiceException{
		return quranService.getVerseById(id);
	}
	
	public List<QuranVerseResource> findAll(QueryParams params) throws ServiceException{
		Map<String, Set<String>> filterParams = QueryParamUtils.getFilters(QuranVerseResource.class, params);
		if(filterParams.isEmpty()){
			return null;
		}
		
		QuranVersionEnum version;
		if(!filterParams.containsKey("version")){
			version = QuranVersionEnum.SHAKIR;
		}
		else{
			version = QuranVersionEnum.findByName(filterParams.get("version").iterator().next());
		}
		
		
		List<QuranVerseResource> resources = new ArrayList<QuranVerseResource>();
		Set<String> verses = filterParams.get("displayVerse");
		Set<String> ids = filterParams.get("id");
		
		if(verses!=null && !verses.isEmpty()){
			for(String verse: verses){
				resources.addAll(quranService.getVersesFromString(version, verse));
			}
		}
		else if(ids!=null && !ids.isEmpty()){
			List<String> idList = new ArrayList<String>();
			for(String id : ids){
				idList.addAll(Arrays.asList(id.split(",")));
			}
			resources =  quranService.getVersesByIds(idList);
		}
		return resources;
	}

	public List<QuranVerseResource> findAll(List<String> ids, QueryParams params) {
		return quranService.getVersesByIds(ids);
	}
}
