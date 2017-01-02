package com.joeldholmes.services;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.joeldholmes.entity.VerseEntity;
import com.joeldholmes.enums.BibleVersionEnum;
import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.repository.IVerseRepository;
import com.joeldholmes.resources.BibleVerseResource;
import com.joeldholmes.services.interfaces.IBibleService;
import com.joeldholmes.services.interfaces.IReligiousTextIndexService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BibleServiceTests {

	@Autowired
	IBibleService bibleService;
	
	@MockBean(name="verseRepository")
	IVerseRepository verseRepo;
	
	@MockBean(name="ReligiousTextIndexService")
	IReligiousTextIndexService indexService;
	
	@Before
	public void init() throws Exception{
		VerseEntity entity = new VerseEntity();
		
		entity.setId("asd");
		entity.setBook("foo");
		entity.setChapter(1);
		entity.setVerse(1);
		entity.setVersion("niv");
		entity.setContent("asdf");
		
		List<VerseEntity> entityList = new ArrayList<VerseEntity>();
		entityList.add(entity);
		
		when(verseRepo.getBibleVersesInChapter(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(entityList);
		when(verseRepo.getBibleVersesInChapter(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(entityList);
		when(verseRepo.getBibleVersesInChapterRange(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(entityList);
		when(verseRepo.getSingleBibleVerse(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(entity);
		
		when(indexService.maxBibleBookChapters(Mockito.anyString())).thenReturn(20);
		when(indexService.maxBibleBookChapterVerses(Mockito.anyString(), Mockito.anyInt())).thenReturn(20);
	}
	
	@Test
	public void testGetVersesInChapter() throws Exception{
		List<BibleVerseResource> results = bibleService.getVersesInChapter(BibleVersionEnum.NIV, "foo", 1);
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVersesInChapter_nullVersion() throws Exception{
		bibleService.getVersesInChapter(null, "foo", 1);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVersesInChapter_nullBook() throws Exception{
		bibleService.getVersesInChapter(BibleVersionEnum.NIV, null, 1);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVersesInChapter_invalidChapter() throws Exception{
		bibleService.getVersesInChapter(BibleVersionEnum.NIV, "foo", -1);
	}
	
	@Test
	public void testGetVersesStrings() throws Exception{
		List<BibleVerseResource> results = bibleService.getVerses(BibleVersionEnum.NIV, "John", "1", "1", "2", "2");
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVersesStrings_nullVersion() throws Exception{
		bibleService.getVerses(null, "John", "1", "1", "2", "2");
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVersesStrings_nullBook() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, null, "1", "1", "2", "2");
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVersesStrings_nullStartChapter() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, "John", null, "1", "2", "2");
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVersesStrings_numberFormat() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, "John", "sadf", "1", "2", "2");
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_nullVersion() throws Exception{
		bibleService.getVerses(null, "John", 1, 1, 2, 2);
	}
	

	@Test(expected=ServiceException.class)
	public void testGetVerses_nullBook() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, null, 1, 1, 2, 2);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_nullStartChapter() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, "John", null, 1, 2, 2);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidThroughChapter() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, "John", 1, null, -2, null);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidThroughChapter2() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, "John", 1, 1, -2, 2);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidChapter() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, "John", -1, 1, null, null);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidChapter2() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, "John", -1, 1, 2, 2);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidVerse() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, "John", 1, null, -2, 2);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidVerse2() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, "John", 1, 1, -2, 2);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidVerse3() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, "John", 1, -1, null, 2);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidVerse4() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, "John", 1, 5, null, 2);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidVerse5() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, "John", 1, 5, null, 200);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidThroughVerse() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, "John", 1, null, 2, -2);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_invalidThroughVerse2() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, "John", 1, 1, 2, 200);
	}
	
	@Test(expected=ServiceException.class)
	public void testGetVerses_bothInvalidVerse() throws Exception{
		bibleService.getVerses(BibleVersionEnum.NIV, "John", 1, 2, 2, 200);
	}

	@Test
	public void testGetVerses_sameThroughChapter() throws Exception{
		List<BibleVerseResource> results = bibleService.getVerses(BibleVersionEnum.NIV, "John", 1, 1, 1, 2);
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
	}
	
	@Test
	public void testGetVerses_justChapter() throws Exception{
		List<BibleVerseResource> results = bibleService.getVerses(BibleVersionEnum.NIV, "John", 1, null, null, null);
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
	}
	
	@Test
	public void testGetVerses_throughChapter() throws Exception{
		List<BibleVerseResource> results = bibleService.getVerses(BibleVersionEnum.NIV, "John", 1, null, 2, null);
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
	}
	
	@Test
	public void testGetVerses_singleVerse() throws Exception{
		List<BibleVerseResource> results = bibleService.getVerses(BibleVersionEnum.NIV, "John", 1, null, 2, 2);
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
	}
	
	@Test
	public void testGetVerses_nullStartVerse() throws Exception{
		List<BibleVerseResource> results = bibleService.getVerses(BibleVersionEnum.NIV, "John", 1, 2, null, null);
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
	}
	
	@Test
	public void testGetVerses_range() throws Exception{
		List<BibleVerseResource> results = bibleService.getVerses(BibleVersionEnum.NIV, "John", 1, 2, 4, 2																																																																																																			);
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
	}
}

