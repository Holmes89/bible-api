package com.joeldholmes.resources;

import org.apache.commons.lang3.text.WordUtils;

import com.joeldholmes.entity.VerseEntity;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;

@JsonApiResource(type="quran-verses")
public class QuranVerseResource implements Comparable<QuranVerseResource>{

	@JsonApiId
	public String id;
	
	public String chapterTitle;
	
	public String displayVerse;
	
	public int chapter;
	
	public int verse;
	
	public String verseContent;
	
	public QuranVerseResource(VerseEntity entity){
		super();
		String chapterTitle = entity.getChapterTitle();
		if(chapterTitle!=null){
			this.chapterTitle=  WordUtils.capitalizeFully(chapterTitle);
		}
		this.chapter=entity.getChapter();
		this.verse=entity.getVerse();
		this.verseContent=entity.getContent();
		this.displayVerse = this.chapterTitle+" "+this.verse;
		this.id = entity.getId();
	}

	@Override
	public int compareTo(QuranVerseResource other) {
				
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
