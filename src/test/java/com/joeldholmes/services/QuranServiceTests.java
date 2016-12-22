package com.joeldholmes.services;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.joeldholmes.enums.QuranVersionEnum;
import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.QuranVerseResource;
import com.joeldholmes.services.interfaces.IQuranService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class QuranServiceTests {

	@Autowired
	IQuranService quranService;

	@Test
	public void testGetSingleVerse() throws Exception{
		List<QuranVerseResource> dtos = quranService.getVerses(QuranVersionEnum.PICKTHALL, 2, 3, null, null);
		Assert.assertEquals(1, dtos.size());
		QuranVerseResource result = dtos.iterator().next();
		Assert.assertEquals("The Cow", result.chapterTitle);
		Assert.assertEquals(2, result.chapter);
		Assert.assertEquals(3, result.verse);
		Assert.assertNotNull(result.verseContent);
	}
	
	@Test
	public void testGetMultipleVersesSingleChapter() throws Exception{
		List<QuranVerseResource> dtos = quranService.getVerses(QuranVersionEnum.PICKTHALL, 2, 3, null, 6);
		Assert.assertEquals(4, dtos.size());
		QuranVerseResource result = dtos.iterator().next();
		Assert.assertEquals("The Cow", result.chapterTitle);
		Assert.assertEquals(2, result.chapter);
		Assert.assertEquals(3, result.verse);
		Assert.assertNotNull(result.verseContent);
		result = dtos.get(dtos.size()-1);
		Assert.assertEquals("The Cow", result.chapterTitle);
		Assert.assertEquals(2, result.chapter);
		Assert.assertEquals(6, result.verse);
		Assert.assertNotNull(result.verseContent);
	}
	
	@Test
	public void testGetMultipleVersesSameChapter() throws Exception{
		List<QuranVerseResource> dtos = quranService.getVerses(QuranVersionEnum.PICKTHALL,  2, 3, 2, 6);
		Assert.assertEquals(4, dtos.size());
		QuranVerseResource result = dtos.iterator().next();
		Assert.assertEquals("The Cow", result.chapterTitle);
		Assert.assertEquals(2, result.chapter);
		Assert.assertEquals(3, result.verse);
		Assert.assertNotNull(result.verseContent);
		result = dtos.get(dtos.size()-1);
		Assert.assertEquals("The Cow", result.chapterTitle);
		Assert.assertEquals(2, result.chapter);
		Assert.assertEquals(6, result.verse);
		Assert.assertNotNull(result.verseContent);
	}
	
	@Test
	public void testGetMultipleVersesMultipleChapter() throws Exception{
		List<QuranVerseResource> dtos = quranService.getVerses(QuranVersionEnum.PICKTHALL, 2, 3, 3, 3);
		Assert.assertEquals(287, dtos.size());
		QuranVerseResource result = dtos.iterator().next();
		Assert.assertEquals("The Cow", result.chapterTitle);
		Assert.assertEquals(2, result.chapter);
		Assert.assertEquals(3, result.verse);
		Assert.assertNotNull(result.verseContent);
		result = dtos.get(dtos.size()-1);
		Assert.assertEquals("The Family Of Imran", result.chapterTitle);
		Assert.assertEquals(3, result.chapter);
		Assert.assertEquals(3, result.verse);
		Assert.assertNotNull(result.verseContent);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_nullType() throws Exception{
		quranService.getVerses(null, 2, 3, null, null);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_nullChapter() throws Exception{
		Integer chapter =null;
		quranService.getVerses(QuranVersionEnum.PICKTHALL, chapter, 3, null, null);
	}
	
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidChapter() throws Exception{
		quranService.getVerses(QuranVersionEnum.PICKTHALL, -1, 2, null, null);
	}
	
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_nullThroughChapter_invalidVerse() throws Exception{
		quranService.getVerses(QuranVersionEnum.PICKTHALL, 2, 2, null, -99);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_nullThroughChapter_invalidLowerVerse() throws Exception{
		quranService.getVerses(QuranVersionEnum.PICKTHALL, 2, 2, null, 1);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidLowerThroughChapter() throws Exception{
		quranService.getVerses(QuranVersionEnum.PICKTHALL, 2, 2, 1, 1);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidThroughChapter() throws Exception{
		quranService.getVerses(QuranVersionEnum.PICKTHALL, 2, 2, 99999, 1);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_throughChapter_invalidVerse() throws Exception{
		quranService.getVerses(QuranVersionEnum.PICKTHALL,  2, 2, 3, -1);
	}
	
	@Test
	public void testGetVersesInChapter() throws Exception{
		List<QuranVerseResource> verses = quranService.getVersesInChapter(QuranVersionEnum.PICKTHALL, 2);
		Assert.assertEquals(286, verses.size());
		QuranVerseResource result = verses.get(verses.size()-1);
		Assert.assertEquals("The Cow", result.chapterTitle);
		Assert.assertEquals(2, result.chapter);
		Assert.assertEquals(286, result.verse);
		Assert.assertNotNull(result.verseContent);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVersesInChapter_nullVersion() throws Exception{
		quranService.getVersesInChapter(null, 2);
	}
	
	
	@Test(expected=ServiceException.class)
	public void testGetVersesInChapter_invalidChapter() throws Exception{
		quranService.getVersesInChapter(QuranVersionEnum.PICKTHALL, 20000);
	}
	
	@Test
	public void testGetVersesFromString() throws Exception{
		List<QuranVerseResource> verses = quranService.getVersesFromString("The Opening, The Thunder, 3:32-33");
		Assert.assertEquals(52, verses.size());
		verses = quranService.getVersesFromString("The Opening 1, The Thunder, 3:32-33");
		Assert.assertEquals(46, verses.size());
		verses = quranService.getVersesFromString("The Opening 1, 3, The Thunder, 3:32-33");
		Assert.assertEquals(47, verses.size());
		verses = quranService.getVersesFromString("The Opening 1-3, 6-7, The Thunder, 3:32-33");
		Assert.assertEquals(50, verses.size());
	}
	
	@Test
	public void testGetVersesFromString_chapterNumbers() throws Exception{
		List<QuranVerseResource> verses = quranService.getVersesFromString("1-2");
		Assert.assertEquals(293, verses.size());
		verses = quranService.getVersesFromString("1:1-5");
		Assert.assertEquals(5, verses.size());
		verses = quranService.getVersesFromString("The Opening, 32-33");
		Assert.assertEquals(110, verses.size());
	}
	
}

