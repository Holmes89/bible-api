package com.joeldholmes.resources;

import java.util.List;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;
import io.katharsis.resource.annotations.JsonApiToMany;

@JsonApiResource(type="search-verse-results")
public class SearchVerseResource {
	
	@JsonApiId
	public Integer id;
	
	public String searchTerm;
	
	//@JsonApiToMany(lazy = false)
	public List<BibleVerseResource> bibleVerses;
	
	//@JsonApiToMany(lazy = false)
	public List<QuranVerseResource> quranVerses;
	
	//@JsonApiToMany(lazy = false)
	public List<TaoVerseResource> taoVerses;
	
	public SearchVerseResource(){
		super();
	}

}
