package com.joeldholmes.resources;

import com.joeldholmes.entity.VerseEntity;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;

@JsonApiResource(type="tao-verses")
public class TaoVerseResource implements Comparable<TaoVerseResource>{

	@JsonApiId
	public String id;
	
	public int chapter;
	
	public int verse;
	
	public String displayVerse;
	
	public String verseContent;

	public TaoVerseResource(VerseEntity entity) {
		super();
		this.id = entity.getId();
		this.chapter=entity.getChapter();
		this.verse=entity.getVerse();
		this.verseContent=entity.getContent();
		this.displayVerse = this.chapter+":"+this.verse;
	}

	
	@Override
	public int compareTo(TaoVerseResource other) {
				
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
