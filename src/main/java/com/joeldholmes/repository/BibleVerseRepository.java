package com.joeldholmes.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.joeldholmes.enums.BibleVersionEnum;
import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.BibleVerseResource;
import com.joeldholmes.services.interfaces.IBibleService;
import com.joeldholmes.services.interfaces.ISearchService;
import com.joeldholmes.utils.ErrorCodes;
import com.joeldholmes.utils.QueryParamUtils;

import io.katharsis.queryParams.QueryParams;

@Repository
public class BibleVerseRepository {

	@Autowired
	IBibleService bibleService;
	
	@Autowired
	ISearchService searchService;
	
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
			try{
				resources.addAll(bibleService.getVersesFromString(filterParams.get("displayVerse")));
			}catch(ServiceException e){
				if(!e.getErrorCode().equalsIgnoreCase(ErrorCodes.INVALID_INPUT)){
					throw e;
				}
			}
		}
		else if(filterParams.containsKey("book")
				&& filterParams.containsKey("chapter")
				&& filterParams.containsKey("endChapter")
				&& filterParams.containsKey("verse")
				&& filterParams.containsKey("endVerse")){
			
			String book = filterParams.get("book");
			String startChapter = filterParams.get("chapter");
			String endChapter = filterParams.get("endChapter");
			String startVerse = filterParams.get("verse");
			String endVerse = filterParams.get("endVerse");
			
			resources.addAll(bibleService.getVerses(version, book, startChapter, startVerse, endChapter, endVerse));
			
		}
		else if(filterParams.containsKey("book")
				&& filterParams.containsKey("chapter")
				&& filterParams.containsKey("verse")
				&& filterParams.containsKey("endVerse")){

			String book = filterParams.get("book");
			String startChapter = filterParams.get("chapter");
			String startVerse = filterParams.get("verse");
			String endVerse = filterParams.get("endVerse");
			
			resources.addAll(bibleService.getVerses(version, book, startChapter, startVerse, null, endVerse));
			
		}
		else if(filterParams.containsKey("book")
				&& filterParams.containsKey("chapter")
				&& filterParams.containsKey("endChapter")){
			
			String book = filterParams.get("book");
			String startChapter = filterParams.get("chapter");
			String endChapter = filterParams.get("endChapter");
			
			resources.addAll(bibleService.getVerses(version, book, startChapter, null, endChapter, null));

		}
		else if(filterParams.containsKey("book") 
				&& filterParams.containsKey("chapter") 
				&& filterParams.containsKey("verse")){
			
			String book = filterParams.get("book");
			String chapter = filterParams.get("chapter");
			String verse = filterParams.get("verse");
			
			
			resources.addAll(bibleService.getVerses(version, book, chapter, verse, null, null));

		}
		else if(filterParams.containsKey("book") && filterParams.containsKey("chapter")){
			String book = filterParams.get("book");
			String chapter = filterParams.get("chapter");
			
			resources.addAll(bibleService.getVerses(version, book, chapter, null, null, null));

		}
		else if(filterParams.containsKey("verseContent") && filterParams.containsKey("version")){
			String searchTerm = filterParams.get("verseContent");
			resources.addAll(searchService.searchBibleText(version, searchTerm));
		}
		else if(filterParams.containsKey("verseContent")){
			String searchTerm = filterParams.get("verseContent");
			resources.addAll(searchService.searchBibleText(searchTerm));
		}
		
		
		return resources;
	}
}
