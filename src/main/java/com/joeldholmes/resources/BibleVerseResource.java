package com.joeldholmes.resources;

import org.apache.commons.lang3.text.WordUtils;

import com.joeldholmes.entity.VerseEntity;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;

@JsonApiResource(type="verses")
public class BibleVerseResource implements Comparable<BibleVerseResource>{

	@JsonApiId
	public String id;
	
	public String displayVerse;
	
	public String book;
	
	public int chapter;
	
	public int verse;
	
	public String verseContent;
	
	public String version;

	public BibleVerseResource(VerseEntity entity){
		super();
		String entityBook = entity.getBook();
		
		if(entityBook!=null){
			this.book = WordUtils.capitalizeFully(entityBook);
		}
		
		this.chapter=entity.getChapter();
		this.verse=entity.getVerse();
		this.verseContent=entity.getContent();
		this.id = entity.getId();
		this.version = entity.getVersion();
		this.displayVerse = this.book+" "+this.chapter+":"+this.verse;
		
	}

	@Override
	public int compareTo(BibleVerseResource other) {
		
		if(!this.book.equals(other.book)){
			return this.book.compareTo(other.book);
		}
		
		if(this.chapter>other.chapter){
			return 1;
		}
		else if(this.chapter<other.chapter){
			return -1;
		}
		
		if(this.verse>other.verse){
			return 1;
		}
		else if(this.verse<other.verse){
			return -1;
		}
		
		return 0;
	}
}
