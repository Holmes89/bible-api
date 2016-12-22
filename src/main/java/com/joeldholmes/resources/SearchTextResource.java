package com.joeldholmes.resources;

import org.apache.commons.lang3.text.WordUtils;

import com.joeldholmes.entity.VerseEntity;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;

@JsonApiResource(type="search-text-results")
public class SearchTextResource{
	
	@JsonApiId
	public String id;
	
	public String displayVerse;
	
	public String religiousText;
	
	public String searchContent;
	
	public String searchTerm;
	
	public Double score;
	
	public SearchTextResource(){
		super();
	}
	public SearchTextResource(VerseEntity entity){
		this();
		
		String entityBook = entity.getBook();
		String entityChapterTitle= entity.getChapterTitle();
		
		this.id = entity.getId();
		this.displayVerse = ""; 
		
		if(entityBook!=null && !entityBook.isEmpty()){
			this.displayVerse += WordUtils.capitalizeFully(entityBook)+" ";
		}
		if(entityChapterTitle!=null && !entityBook.isEmpty()){
			this.displayVerse += WordUtils.capitalizeFully(entityChapterTitle)+" ";
		}
		this.displayVerse += entity.getChapter()+":";
		this.displayVerse+=entity.getVerse();
		
		this.religiousText = entity.getReligiousText();
		this.searchContent=entity.getContent();
	}

}
