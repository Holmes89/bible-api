package com.joeldholmes.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joeldholmes.entity.VerseEntity;
import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.repository.IVerseRepository;
import com.joeldholmes.resources.BibleVerseResource;
import com.joeldholmes.services.interfaces.ISearchService;
import com.joeldholmes.utils.ErrorCodes;

@Service("SearchTextService")
public class SearchTextService implements ISearchService {

	@Autowired
	IVerseRepository verseRepository;

	@Override
	public List<BibleVerseResource> searchBibleText(String term) throws ServiceException {
		if(term==null||term.isEmpty()){
			throw new ServiceException("Search term cannot be null or empty", ErrorCodes.NULL_INPUT);
		}
		List<VerseEntity> entities = verseRepository.searchAllBibleText(term);
		if(entities==null||entities.isEmpty()){
			return null;
		}
		return convertEntitiesToDTOs(entities);
	}
	
	private List<BibleVerseResource> convertEntitiesToDTOs(List<VerseEntity> entities){
		List<BibleVerseResource> dtos = new ArrayList<BibleVerseResource>();
		for(VerseEntity verseEntity: entities){
			dtos.add(new BibleVerseResource(verseEntity));
		}
		Collections.sort(dtos);
		return dtos;
	}


}
