package com.joeldholmes.services.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joeldholmes.entity.VerseEntity;
import com.joeldholmes.enums.BibleVersionEnum;
import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.repository.IVerseRepository;
import com.joeldholmes.resources.BibleVerseResource;
import com.joeldholmes.services.interfaces.ISearchService;
import com.joeldholmes.utils.ErrorCodes;

@Service("SearchTextService")
public class SearchTextService implements ISearchService {

	@Autowired
	IVerseRepository verseRepository;

	private static final String DEFAULT_ENC = "UTF-8";
	
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
		return dtos;
	}

	@Override
	public List<BibleVerseResource> searchBibleText(BibleVersionEnum version, String term) throws ServiceException {
		if(version==null){
			return searchBibleText(term);
		}
		if(term==null||term.isEmpty()){
			throw new ServiceException("Search term cannot be null or empty", ErrorCodes.NULL_INPUT);
		}
		term = decode(term);
		List<VerseEntity> entities = verseRepository.searchAllBibleTextAndVersion(version.getAbbr(), term);
		if(entities==null||entities.isEmpty()){
			return null;
		}
		return convertEntitiesToDTOs(entities);
	}
	
	private String decode(String encoded) throws ServiceException{
		try {
			return URLDecoder.decode(encoded, DEFAULT_ENC);
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Unexpected encoding error");
		}
	}



}
