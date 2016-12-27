package com.joeldholmes.services;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.joeldholmes.enums.BibleVersionEnum;
import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.BibleVerseResource;
import com.joeldholmes.services.interfaces.ISearchService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchServiceTests {

	@Autowired
	ISearchService searchService;
	
	@Test
	public void testSearchBibleText() throws Exception{
		List<BibleVerseResource> results = searchService.searchBibleText("hatred");
		Assert.assertNotNull(results);
		Assert.assertTrue(!results.isEmpty());
		
		Assert.assertEquals(194, results.size());
	}
	
	@Test
	public void testSearchAllBibleText_no_results() throws Exception{
		List<BibleVerseResource> results = searchService.searchBibleText("asdflkja;sldkfapoisdfja;sldjf;aosidjf;lasjdf;");
		Assert.assertNull(results);
	}
	
	@Test(expected=ServiceException.class)
	public void testSearchAllBibleText_null() throws Exception{
		searchService.searchBibleText(null);
	}
	
	@Test(expected=ServiceException.class)
	public void testSearchAllBibleText_empty() throws Exception{
		searchService.searchBibleText("");
	}
	
	@Test
	public void testSearchBibleTextWithVersion() throws Exception{
		List<BibleVerseResource> results = searchService.searchBibleText(BibleVersionEnum.NIV, "hatred");
		Assert.assertNotNull(results);
		Assert.assertTrue(!results.isEmpty());
		
		Assert.assertEquals(11, results.size());
	}
	
	@Test
	public void testSearchBibleTextWithVersion_null_version() throws Exception{
		List<BibleVerseResource> results = searchService.searchBibleText(null, "hatred");
		Assert.assertNotNull(results);
		Assert.assertTrue(!results.isEmpty());
		
		Assert.assertEquals(194, results.size());
	}
	
	@Test
	public void testSearchAllBibleTextWithVersion_no_results() throws Exception{
		List<BibleVerseResource> results = searchService.searchBibleText(BibleVersionEnum.NIV,"asdflkja;sldkfapoisdfja;sldjf;aosidjf;lasjdf;");
		Assert.assertNull(results);
	}
	
	@Test(expected=ServiceException.class)
	public void testSearchAllBibleTextWithVersion_null_term() throws Exception{
		searchService.searchBibleText(BibleVersionEnum.NIV, null);
	}
	
	@Test(expected=ServiceException.class)
	public void testSearchAllBibleTextWithVersion_empty_term() throws Exception{
		searchService.searchBibleText(BibleVersionEnum.NIV, "");
	}
}
