package com.joeldholmes.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.BibleVerseResource;
import com.joeldholmes.resources.QuranVerseResource;
import com.joeldholmes.resources.SearchVerseResource;
import com.joeldholmes.resources.TaoVerseResource;
import com.joeldholmes.services.interfaces.IBibleService;
import com.joeldholmes.services.interfaces.IQuranService;
import com.joeldholmes.services.interfaces.ITaoService;
import com.joeldholmes.utils.QueryParamUtils;

import io.katharsis.queryParams.QueryParams;

@Repository
public class SearchVerseRepository {
	
	protected transient Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IBibleService bibleService;
	
	@Autowired
	IQuranService quranService;
	
	@Autowired
	ITaoService taoService;
	
	public List<SearchVerseResource> findAll(QueryParams params){
		
		SearchVerseResource result = new SearchVerseResource();
		
		Map<String, Set<String>> filters = QueryParamUtils.getFilters(SearchVerseResource.class, params);
		Set<String> filterVerseStrings = filters.get("searchTerm");
		
		List<BibleVerseResource> bibleVerses = new ArrayList<BibleVerseResource>();
		List<QuranVerseResource> quranVerses = new ArrayList<QuranVerseResource>();
		List<TaoVerseResource> taoVerses = new ArrayList<TaoVerseResource>();
		
		if(filterVerseStrings!=null && !filterVerseStrings.isEmpty()){
			String filterVerse = filterVerseStrings.iterator().next();
			for(String verseString :filterVerse.split(";")){
				verseString = verseString.toLowerCase().trim();
				boolean quranOnly = (verseString.contains("qura")||verseString.contains("qu'ran")||verseString.contains("koran"));
				boolean taoOnly = (verseString.contains("tao")||verseString.contains("tao-te-ching"));
				
				verseString = verseString.replace("quran", "").replace("qu'ran", "").replace("koran", "").replace("tao", "").replace("tao-te-ching", "");
	
				if(!taoOnly){
					try {
						quranVerses.addAll(quranService.getVersesFromString(verseString));
					} catch (ServiceException e) {
						log.warn("{} is not a valid Quran Verse", verseString);
					}
				}
				if(!quranOnly){
					try {
						taoVerses.addAll(taoService.getVersesFromString(verseString));
					} catch (ServiceException e) {
						log.warn("{} is not a valid Tao Verse", verseString);
					}
				}
				if(!(quranOnly || taoOnly)){
					try {
						bibleVerses.addAll(bibleService.getVersesFromString(verseString));
					} catch (ServiceException e) {
						log.warn("{} is not a valid Bible Verse", verseString);
					}
				}
			
			}
			
			result.bibleVerses = bibleVerses;
			result.quranVerses = quranVerses;
			result.taoVerses = taoVerses;
			result.id = filterVerse.hashCode();
			result.searchTerm = filterVerse;
			
		}
		
		return Arrays.asList(result);
	}

}
