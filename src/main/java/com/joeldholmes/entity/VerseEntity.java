package com.joeldholmes.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

@Document(collection="bibleVerses")
public class VerseEntity implements Comparable<VerseEntity>{
	
	@Id
	private String id;
	
	private Integer chapter; 
	private String version;
	
	@TextIndexed
	private String content;
	private Integer verse;
	private String book;
	private String chapterTitle;
	private String religiousText;
	
	@TextScore
	private Float score;
	
	public Integer getChapter() {
		return chapter;
	}
	public void setChapter(Integer chapter) {
		this.chapter = chapter;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getVerse() {
		return verse;
	}
	public void setVerse(Integer verse) {
		this.verse = verse;
	}
	public String getBook() {
		return book;
	}
	public void setBook(String book) {
		this.book = book;
	}
	public String getChapterTitle() {
		return chapterTitle;
	}
	public void setChapterTitle(String chapterTitle) {
		this.chapterTitle = chapterTitle;
	}
	public String getReligiousText() {
		return religiousText;
	}
	public void setReligiousText(String religiousText) {
		this.religiousText = religiousText;
	}
	public String getId() {
		return id;
	}
	
	@Override
	public int compareTo(VerseEntity other) {
		if(this.book!=null){
			if(!this.getBook().equals(other.getBook())){
				return this.getBook().compareTo(other.getBook());
			}
		}
		
		if(this.getChapter()>other.getChapter()){
			return 1;
		}
		else if(this.getChapter()<other.getChapter()){
			return -1;
		}
		
		if(this.getVerse()>other.getVerse()){
			return 1;
		}
		else if(this.getVerse()<other.getVerse()){
			return -1;
		}
		
		return 0;
	}
	public Float getScore() {
		return score;
	}
	public void setScore(Float score) {
		this.score = score;
	}

	
	
}
