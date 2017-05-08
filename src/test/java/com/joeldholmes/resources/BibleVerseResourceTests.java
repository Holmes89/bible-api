package com.joeldholmes.resources;

import org.junit.Assert;
import org.junit.Test;

import com.joeldholmes.resources.BibleVerseResource;


public class BibleVerseResourceTests {
	
	@Test
	public void testEquals(){
		BibleVerseResource resource = getResource();
		BibleVerseResource otherResource = getResource();
		
		Assert.assertEquals(0, resource.compareTo(otherResource));
		
		otherResource = getResource();
		otherResource.book = "bad";
		Assert.assertEquals(1, resource.compareTo(otherResource));
		
		otherResource = getResource();
		otherResource.book = "zad";
		Assert.assertEquals(-1, resource.compareTo(otherResource));
		
		
		otherResource = getResource();
		otherResource.chapter = 0;
		Assert.assertEquals(1, resource.compareTo(otherResource));
		
		otherResource = getResource();
		otherResource.verse = 0;
		Assert.assertEquals(1, resource.compareTo(otherResource));
		
		otherResource = getResource();
		otherResource.chapter = 100;
		Assert.assertEquals(-1, resource.compareTo(otherResource));
		
		otherResource = getResource();
		otherResource.verse = 100;
		Assert.assertEquals(-1, resource.compareTo(otherResource));
		
	}

	private BibleVerseResource getResource(){
		BibleVerseResource resource = new BibleVerseResource();
		resource.book="joel";
		resource.chapter=2;
		resource.verse=1;
		resource.verseContent="bar";
		resource.version = "niv";
		
		return resource;
	}
}
