package com.joeldholmes.services;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.TaoVerseResource;
import com.joeldholmes.services.interfaces.ITaoService;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TaoServiceTests {

	@Autowired
	ITaoService taoService;

	@Test
	public void testGetSingleVerse() throws Exception{
		List<TaoVerseResource> dtos = taoService.getVerses(2, 3, null, null);
		Assert.assertEquals(1, dtos.size());
		TaoVerseResource result = dtos.iterator().next();
		Assert.assertEquals(2, result.chapter);
		Assert.assertEquals(3, result.verse);
		Assert.assertNotNull(result.verseContent);
	}
	
	@Test
	public void testGetMultipleVersesSingleChapter() throws Exception{
		List<TaoVerseResource> dtos = taoService.getVerses(2, 3, null, 4);
		Assert.assertEquals(2, dtos.size());
		TaoVerseResource result = dtos.iterator().next();
		Assert.assertEquals(2, result.chapter);
		Assert.assertEquals(3, result.verse);
		Assert.assertNotNull(result.verseContent);
		result = dtos.get(dtos.size()-1);
		Assert.assertEquals(2, result.chapter);
		Assert.assertEquals(4, result.verse);
		Assert.assertNotNull(result.verseContent);
	}
	
	@Test
	public void testGetMultipleVersesSameChapter() throws Exception{
		List<TaoVerseResource> dtos = taoService.getVerses(2, 3, 2, 4);
		Assert.assertEquals(2, dtos.size());
		TaoVerseResource result = dtos.iterator().next();
		Assert.assertEquals(2, result.chapter);
		Assert.assertEquals(3, result.verse);
		Assert.assertNotNull(result.verseContent);
		result = dtos.get(dtos.size()-1);
		Assert.assertEquals(2, result.chapter);
		Assert.assertEquals(4, result.verse);
		Assert.assertNotNull(result.verseContent);
	}
	
	@Test
	public void testGetMultipleVersesMultipleChapter() throws Exception{
		List<TaoVerseResource> dtos = taoService.getVerses(2, 3, 3, 3);
		Assert.assertEquals(5, dtos.size());
		TaoVerseResource result = dtos.iterator().next();
		Assert.assertEquals(2, result.chapter);
		Assert.assertEquals(3, result.verse);
		Assert.assertNotNull(result.verseContent);
		result = dtos.get(dtos.size()-1);
		Assert.assertEquals(3, result.chapter);
		Assert.assertEquals(3, result.verse);
		Assert.assertNotNull(result.verseContent);
	}
	
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_nullChapter() throws Exception{
		taoService.getVerses(null, 3, null, null);
	}
	
	
	public void testGetVerses_nullVerse() throws Exception{
		List<TaoVerseResource> dtos = taoService.getVerses(2, null, null, null);
		Assert.assertEquals(4, dtos.size());
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidChapter() throws Exception{
		taoService.getVerses(-1, 2, null, null);
	}
	
	
	public void testGetVerses_throughChapter_nullVerse() throws Exception{
		List<TaoVerseResource> dtos = taoService.getVerses(2, 2, 3, null);
		Assert.assertEquals(6, dtos.size());
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_nullThroughChapter_invalidVerse() throws Exception{
		taoService.getVerses(2, 2, null, 99);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_nullThroughChapter_invalidLowerVerse() throws Exception{
		taoService.getVerses(2, 2, null, 1);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidLowerThroughChapter() throws Exception{
		taoService.getVerses(2, 2, 1, 1);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidThroughChapter() throws Exception{
		taoService.getVerses(2, 2, 99, 1);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_throughChapter_invalidVerse() throws Exception{
		taoService.getVerses(2, 2, 3, -1);
	}
	
	@Test
	public void testGetVersesInChapter() throws Exception{
		List<TaoVerseResource> verses = taoService.getVersesInChapter(2);
		Assert.assertEquals(4, verses.size());
		TaoVerseResource result = verses.get(verses.size()-1);
		Assert.assertEquals(2, result.chapter);
		Assert.assertEquals(4, result.verse);
		Assert.assertNotNull(result.verseContent);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVersesInChapter_invalidChapter() throws Exception{
		taoService.getVersesInChapter(200);
	}
	
	@Test
	public void testGetVersesFromString() throws Exception{
		List<TaoVerseResource> verses = taoService.getVersesFromString("2:1-3");
		Assert.assertEquals(3, verses.size());
		verses = taoService.getVersesFromString("2:1, 3-4");
		Assert.assertEquals(3, verses.size());
		verses = taoService.getVersesFromString("2:1, 1:3-4");
		Assert.assertEquals(3, verses.size());
		verses = taoService.getVersesFromString("2");
		Assert.assertEquals(4, verses.size());
		verses = taoService.getVersesFromString("1-2");
		Assert.assertEquals(8, verses.size());
		verses = taoService.getVersesFromString("1-2, 5");
		Assert.assertEquals(10, verses.size());
		verses = taoService.getVersesFromString("1-2, 5:1");
		Assert.assertEquals(9, verses.size());
		verses = taoService.getVersesFromString("5:1, 2");
		Assert.assertEquals(2, verses.size());
	}
}
