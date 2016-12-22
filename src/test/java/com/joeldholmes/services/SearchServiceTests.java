package com.joeldholmes.services;

import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.SearchTextResource;
import com.joeldholmes.services.interfaces.ISearchService;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SearchServiceTests {

	@Autowired
	ISearchService searchService;
	
	private PageRequest pageRequest = new PageRequest(0, 50);
	
	@Test
	public void testSearchAllText() throws Exception{
		
		Iterable<SearchTextResource> results = searchService.searchAllText("hatred", pageRequest);
		Assert.assertNotNull(results);
		Iterator<SearchTextResource> iterator = results.iterator();
		Assert.assertTrue(iterator.hasNext());
		
		int bibleCount = 0;
		int quranCount = 0;
		results.iterator();
		while(iterator.hasNext()){
			SearchTextResource verse = iterator.next();
			String text = verse.religiousText;
			if(text.equalsIgnoreCase("bible"))
				bibleCount++;
			else
				quranCount++;
		}
		
		Assert.assertEquals(29, bibleCount);
		Assert.assertEquals(16, quranCount);
	}
	
	@Test
	public void testSearchAllText_no_results() throws Exception{
		Iterable<SearchTextResource> results = searchService.searchAllText("asdflkja;sldkfapoisdfja;sldjf;aosidjf;lasjdf;", pageRequest);
		Assert.assertFalse(results.iterator().hasNext());
	}
	
	@Test(expected=ServiceException.class)
	public void testSearchAllText_null() throws Exception{
		searchService.searchAllText(null, pageRequest);
	}
	
	@Test(expected=ServiceException.class)
	public void testSearchAllText_empty() throws Exception{
		searchService.searchAllText("", pageRequest);
	}
	
	@Test
	public void testSearchBibleText() throws Exception{
		List<SearchTextResource> results = searchService.searchBibleText("hatred");
		Assert.assertNotNull(results);
		Assert.assertTrue(!results.isEmpty());
		
		Assert.assertEquals(29, results.size());
	}
	
	@Test
	public void testSearchAllBibleText_no_results() throws Exception{
		List<SearchTextResource> results = searchService.searchBibleText("asdflkja;sldkfapoisdfja;sldjf;aosidjf;lasjdf;");
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
	public void testSearchQuranText() throws Exception{
		List<SearchTextResource> results = searchService.searchQuranText("Jesus");
		Assert.assertNotNull(results);
		Assert.assertTrue(!results.isEmpty());
		
		Assert.assertEquals(25, results.size());
	}
	
	@Test
	public void testSearchAllQuranText_no_results() throws Exception{
		List<SearchTextResource> results = searchService.searchQuranText("asdflkja;sldkfapoisdfja;sldjf;aosidjf;lasjdf;");
		Assert.assertNull(results);
	}
	
	@Test(expected=ServiceException.class)
	public void testSearchAllQuranText_null() throws Exception{
		searchService.searchQuranText(null);
	}
	
	@Test(expected=ServiceException.class)
	public void testSearchAllQuranText_empty() throws Exception{
		searchService.searchQuranText("");
	}
	
	@Test
	public void testSearchTaoText() throws Exception{
		List<SearchTextResource> results = searchService.searchTaoText("Love");
		Assert.assertNotNull(results);
		Assert.assertTrue(!results.isEmpty());
		
		Assert.assertEquals(8, results.size());
	}
	
	@Test
	public void testSearchAllTaoText_no_results() throws Exception{
		List<SearchTextResource> results = searchService.searchTaoText("asdflkja;sldkfapoisdfja;sldjf;aosidjf;lasjdf;");
		Assert.assertNull(results);
	}
	
	@Test(expected=ServiceException.class)
	public void testSearchAllTaoText_null() throws Exception{
		searchService.searchTaoText(null);
	}
	
	@Test(expected=ServiceException.class)
	public void testSearchAllTaoText_empty() throws Exception{
		searchService.searchTaoText("");
	}
}
