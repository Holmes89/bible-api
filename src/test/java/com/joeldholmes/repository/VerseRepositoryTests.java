package com.joeldholmes.repository;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.joeldholmes.entity.VerseEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VerseRepositoryTests {

	@Autowired
	IVerseRepository verseRepo;
	
	@Test
	public void testGetSingleBibleVerse() throws Exception{
		String expectedVerseContent = "Before them fire devours, behind them a flame blazes. Before them the land is like the garden of Eden, behind them, a desert waste- nothing escapes them.";
		VerseEntity verse = verseRepo.getSingleBibleVerse("niv", "joel", 2, 3);
		Assert.assertNotNull(verse);
		Assert.assertEquals(expectedVerseContent, verse.getContent());
	}
	
	@Test
	public void testGetBibleVersesInChapter() throws Exception{
		List<VerseEntity> verses = verseRepo.getBibleVersesInChapter("niv","joel", 2);
		Assert.assertNotNull(verses);
		Assert.assertTrue(!verses.isEmpty());
		Assert.assertEquals(32, verses.size());
	}
	
	@Test
	public void testGetBibleVersesInChapterWithVerses() throws Exception{
		List<VerseEntity> verses = verseRepo.getBibleVersesInChapter("niv","joel", 2, 3, 7);
		Assert.assertNotNull(verses);
		Assert.assertTrue(!verses.isEmpty());
		Assert.assertEquals(5, verses.size());
	}
	
	@Test
	public void testGetBibleVersesInChapterRange() throws Exception{
		List<VerseEntity> verses = verseRepo.getBibleVersesInChapterRange("niv","joel", 2, 3);
		Assert.assertNotNull(verses);
		Assert.assertTrue(!verses.isEmpty());
		Assert.assertEquals(53, verses.size());
	}
	
	
	@Test
	public void testSearchAllBibleText() throws Exception{
		List<VerseEntity> verses = verseRepo.searchAllBibleText("hatred");
		Assert.assertNotNull(verses);
		Assert.assertTrue(!verses.isEmpty());
		Assert.assertEquals(29, verses.size());
	}

	
	@Test
	public void testGetAll() throws Exception{
		List<VerseEntity> verses = verseRepo.findAll(Arrays.asList("57a4a461a69a6d457a4a87b1", "57a4a45ea69a6d457a4a6ad5"));
		Assert.assertNotNull(verses);
		Assert.assertTrue(!verses.isEmpty());
		Assert.assertEquals(2, verses.size());
	}

}
