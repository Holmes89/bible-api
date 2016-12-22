package com.joeldholmes.repository;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.joeldholmes.entity.VerseEntity;

@RunWith(SpringJUnit4ClassRunner.class)
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
	public void testGetSingleQuranVerse() throws Exception{
		String expectedVerseContent = "Those who believe in the unseen and keep up prayer and spend out of what We have given them.";
		VerseEntity verse = verseRepo.getSingleQuranVerse("shakir", 2, 3);
		Assert.assertNotNull(verse);
		Assert.assertEquals(expectedVerseContent, verse.getContent());
	}
	
	@Test
	public void testGetQuranVersesInChapter() throws Exception{
		List<VerseEntity> verses = verseRepo.getQuranVersesInChapter("shakir", 13);
		Assert.assertNotNull(verses);
		Assert.assertTrue(!verses.isEmpty());
		Assert.assertEquals(43, verses.size());
	}
	
	@Test
	public void testGetQuranVersesInChapterWithVerses() throws Exception{
		List<VerseEntity> verses = verseRepo.getQuranVersesInChapter("shakir", 2, 3, 7);
		Assert.assertNotNull(verses);
		Assert.assertTrue(!verses.isEmpty());
		Assert.assertEquals(5, verses.size());
	}
	
	@Test
	public void testGetQuranVersesInChapterRange() throws Exception{
		List<VerseEntity> verses = verseRepo.getQuranVersesInChapterRange("shakir", 13, 14);
		Assert.assertNotNull(verses);
		Assert.assertTrue(!verses.isEmpty());
		Assert.assertEquals(95, verses.size());
	}
	
	@Test
	public void testGetSingleTaoVerse() throws Exception{
		String expectedVerseContent = "Therefore the sage manages affairs without doing anything, and conveys his instructions without the use of speech.";
		VerseEntity verse = verseRepo.getSingleTaoVerse(2, 3);
		Assert.assertNotNull(verse);
		Assert.assertEquals(expectedVerseContent, verse.getContent());
	}
	
	@Test
	public void testGetTaoVersesInChapter() throws Exception{
		List<VerseEntity> verses = verseRepo.getTaoVersesInChapter(2);
		Assert.assertNotNull(verses);
		Assert.assertTrue(!verses.isEmpty());
		Assert.assertEquals(4, verses.size());
	}
	
    @Test
	public void testGetTaoVersesInChapterWithVerses() throws Exception{
		List<VerseEntity> verses = verseRepo.getTaoVersesInChapter(2, 2, 4);
		Assert.assertNotNull(verses);
		Assert.assertTrue(!verses.isEmpty());
		Assert.assertEquals(3, verses.size());
	}
	
	@Test
	public void testGetTaoVersesInChapterRange() throws Exception{
		List<VerseEntity> verses = verseRepo.getTaoVersesInChapterRange(2, 4);
		Assert.assertNotNull(verses);
		Assert.assertTrue(!verses.isEmpty());
		Assert.assertEquals(10, verses.size());
	}
	
	@Test
	public void testSearchAllText() throws Exception{
		PageRequest pageRequest = new PageRequest(0, 25);
		Iterable<VerseEntity> verses = verseRepo.searchAllText("hatred", pageRequest);
		Iterator<VerseEntity> iter = verses.iterator();
		Assert.assertNotNull(verses);
		Assert.assertTrue(verses.iterator().hasNext());
		int count=0;
		
		while(iter.hasNext()){
			VerseEntity entity = iter.next();
			count++;
		}
		Assert.assertEquals(25, count);
	}
	
	@Test
	public void testSearchAllBibleText() throws Exception{
		List<VerseEntity> verses = verseRepo.searchAllBibleText("hatred");
		Assert.assertNotNull(verses);
		Assert.assertTrue(!verses.isEmpty());
		Assert.assertEquals(29, verses.size());
	}

	
	@Test
	public void testSearchAllQuranText() throws Exception{
		List<VerseEntity> verses = verseRepo.searchAllQuranText("Jesus");
		Assert.assertNotNull(verses);
		Assert.assertTrue(!verses.isEmpty());
		Assert.assertEquals(25, verses.size());
	}

	
	@Test
	public void testSearchAllTaoText() throws Exception{
		List<VerseEntity> verses = verseRepo.searchAllTaoText("love");
		Assert.assertNotNull(verses);
		Assert.assertTrue(!verses.isEmpty());
		Assert.assertEquals(8, verses.size());
	}
	
	@Test
	public void testGetAll() throws Exception{
		List<VerseEntity> verses = verseRepo.findAll(Arrays.asList("57a4a461a69a6d457a4a87b1", "57a4a45ea69a6d457a4a6ad5"));
		Assert.assertNotNull(verses);
		Assert.assertTrue(!verses.isEmpty());
		Assert.assertEquals(2, verses.size());
	}

}
