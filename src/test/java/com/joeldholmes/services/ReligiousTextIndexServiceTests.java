package com.joeldholmes.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.services.interfaces.IReligiousTextIndexService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReligiousTextIndexServiceTests {
	
	@Autowired
	private IReligiousTextIndexService textIndexService;
	
	@Test
	public void testMaxBibleBookChapter() throws Exception{
		Assert.assertEquals(3, textIndexService.maxBibleBookChapters("JOEL"));
		Assert.assertEquals(3, textIndexService.maxBibleBookChapters("joel"));
		Assert.assertEquals(3, textIndexService.maxBibleBookChapters("joEl"));
		Assert.assertEquals(3, textIndexService.maxBibleBookChapters("joEl "));
		Assert.assertEquals(0, textIndexService.maxBibleBookChapters("joElasdf"));
		
	}
	
	@Test(expected=ServiceException.class)
	public void testMaxBibleBookChapter_null_book() throws Exception{
		textIndexService.maxBibleBookChapters(null);
	}
	
	@Test(expected=ServiceException.class)
	public void testMaxBibleBookChapter_empty_book() throws Exception{
		textIndexService.maxBibleBookChapters("");
	}
	
	@Test
	public void testMaxBibleBookChapterVerses() throws Exception{
		Assert.assertEquals(32, textIndexService.maxBibleBookChapterVerses("JOEL", 2));
		Assert.assertEquals(32, textIndexService.maxBibleBookChapterVerses("joel", 2));
		Assert.assertEquals(32, textIndexService.maxBibleBookChapterVerses("joEl", 2));
		Assert.assertEquals(32, textIndexService.maxBibleBookChapterVerses("joEl ", 2));
		Assert.assertEquals(0, textIndexService.maxBibleBookChapterVerses("joEl ", 200));
		Assert.assertEquals(0, textIndexService.maxBibleBookChapterVerses("joElasdf", 2));
		
	}
	
	@Test(expected=ServiceException.class)
	public void testMaxBibleBookChapterVerses_null_book() throws Exception{
		textIndexService.maxBibleBookChapterVerses(null, 2);
	}
	
	@Test(expected=ServiceException.class)
	public void testMaxBibleBookChapterVerses_empty_book() throws Exception{
		textIndexService.maxBibleBookChapterVerses("", 2);
	}
	
	@Test(expected=ServiceException.class)
	public void testMaxBibleBookChapterVerses_invalid_chapter() throws Exception{
		textIndexService.maxBibleBookChapterVerses("joel", -2);
	}

}
