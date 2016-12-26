package com.joeldholmes.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joeldholmes.entity.VerseEntity;
import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.repository.IVerseRepository;
import com.joeldholmes.resources.SearchTextResource;
import com.joeldholmes.services.interfaces.IBibleService;
import com.joeldholmes.services.interfaces.ISearchService;
import com.joeldholmes.utils.ErrorCodes;

@Service("SearchTextService")
public class SearchTextService implements ISearchService {

	@Autowired
	IVerseRepository verseRepository;

	@Autowired
	IBibleService bibleService;
	


	@Override
	public List<SearchTextResource> searchBibleText(String term) throws ServiceException {
		if(term==null||term.isEmpty()){
			throw new ServiceException("Search term cannot be null or empty", ErrorCodes.NULL_INPUT);
		}
		List<VerseEntity> entities = verseRepository.searchAllBibleText(term);
		if(entities==null||entities.isEmpty()){
			return null;
		}
		return entitiesToDTOs(entities, term);
	}
	
	private List<SearchTextResource> entitiesToDTOs(Iterable<VerseEntity> entities, String term){
		List<SearchTextResource> list = new ArrayList<SearchTextResource>();
		for(VerseEntity entity: entities){
			SearchTextResource searchResource = new SearchTextResource(entity);
			searchResource.searchTerm = term;
			list.add(searchResource);
		}
		
		return list;
	}
	


}
