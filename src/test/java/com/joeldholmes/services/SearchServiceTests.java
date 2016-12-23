package com.joeldholmes.services;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.resources.SearchTextResource;
import com.joeldholmes.services.interfaces.ISearchService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchServiceTests {

	@Autowired
	ISearchService searchService;
	
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
}
