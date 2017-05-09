package com.joeldholmes.resources;

import java.util.ArrayList;
import java.util.List;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;
import io.katharsis.resource.annotations.JsonApiToMany;

@JsonApiResource(type="searchResult")
public class SearchResource {

	@JsonApiId
	public String search;
	
	public int totalCount;
	public boolean hasTermSearchResults;
	public boolean hasVerseSearchResults;
	public List<String> verseIds;
	
	@JsonApiToMany
	public List<BibleVerseResource> verses;
	
	public void populate(){
		totalCount = verses.size();
		verseIds = new ArrayList<String>();
		for(BibleVerseResource resource: verses){
			verseIds.add(resource.displayVerse);
		}
	}
}
