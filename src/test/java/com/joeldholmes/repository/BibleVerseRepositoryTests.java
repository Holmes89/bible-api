package com.joeldholmes.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.joeldholmes.enums.BibleVersionEnum;
import com.joeldholmes.resources.BibleVerseResource;
import com.joeldholmes.services.interfaces.IBibleService;
import com.joeldholmes.services.interfaces.ISearchService;

import io.katharsis.queryParams.DefaultQueryParamsParser;
import io.katharsis.queryParams.QueryParams;
import io.katharsis.queryParams.QueryParamsBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BibleVerseRepositoryTests {

	@Autowired
	BibleVerseRepository repo;
	
	@MockBean
	IBibleService bibleService;
	
	@MockBean
	ISearchService searchService;
	
	@Before
	public void init() throws Exception{
		BibleVerseResource resource = new BibleVerseResource();
		List<BibleVerseResource> resourceList = Collections.singletonList(resource);
		
		Mockito.when(bibleService.getVerseById(Mockito.anyString())).thenReturn(resource);
		Mockito.when(bibleService.getVersesByIds(Mockito.anyList())).thenReturn(resourceList);
		Mockito.when(bibleService.getVersesFromString(Mockito.any(BibleVersionEnum.class), Mockito.anyString())).thenReturn(resourceList);
		Mockito.when(searchService.searchBibleText(Mockito.any(BibleVersionEnum.class), Mockito.anyString())).thenReturn(resourceList);
		Mockito.when(bibleService.getVerses(Mockito.any(BibleVersionEnum.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(resourceList);
	}
	
	@Test
	public void testFindOne() throws Exception{
		BibleVerseResource result = repo.findOne("1231");
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testFindAllList() throws Exception{
		List<BibleVerseResource> results = repo.findAll(Collections.singletonList("asdfa"), new QueryParams());
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
	}
	
	@Test
	public void testFindAllNoParams() throws Exception{
		List<BibleVerseResource> results = repo.findAll(emptyParams());
		Assert.assertNull(results);
	}
	
	@Test
	public void testFindAllDisplayVerse() throws Exception{
		List<BibleVerseResource> results = repo.findAll(createParams("filter[displayVerse]", "Joel 1:1"));
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
	}
	
	@Test
	public void testFindAllSearch() throws Exception{
		List<BibleVerseResource> results = repo.findAll(createParams("filter[search]", "Joel 1:1"));
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
	}
	
	@Test
	public void testFindAllBookChapterVerseThroughChapterThroughVerse() throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		params.put("filter[book]","foo");
		params.put("filter[startChapter]", "1");
		params.put("filter[endChapter]", "2");
		params.put("filter[startVerse]", "1");
		params.put("filter[endVerse]", "1");
		params.put("filter[version]", "NLT");
		List<BibleVerseResource> results = repo.findAll(createSingleParams(params));
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
	}
	
	@Test
	public void testFindAllBookChapterVerseThroughVerse() throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		params.put("filter[book]","foo");
		params.put("filter[startChapter]", "1");
		params.put("filter[startVerse]", "1");
		params.put("filter[endVerse]", "2");
		params.put("filter[version]", "NLT");
		List<BibleVerseResource> results = repo.findAll(createSingleParams(params));
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
	}

	@Test
	public void testFindAllBookChapterThroughChapter() throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		params.put("filter[book]","foo");
		params.put("filter[startChapter]", "1");
		params.put("filter[endChapter]", "2");
		params.put("filter[version]", "NLT");
		List<BibleVerseResource> results = repo.findAll(createSingleParams(params));
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
	}
	
	@Test
	public void testFindAllBookChapterVerse() throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		params.put("filter[book]","foo");
		params.put("filter[chapter]", "1");
		params.put("filter[verse]", "1");
		params.put("filter[version]", "NLT");
		List<BibleVerseResource> results = repo.findAll(createSingleParams(params));
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
	}
	
	@Test
	public void testFindAllBookChapter() throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		params.put("filter[book]","foo");
		params.put("filter[chapter]", "1");
		params.put("filter[version]", "NLT");
		List<BibleVerseResource> results = repo.findAll(createSingleParams(params));
		Assert.assertNotNull(results);
		Assert.assertFalse(results.isEmpty());
	}
	
	private QueryParams createParams(String param, String value){
		Map<String, Set<String>> queryParams = new HashMap<String, Set<String>>();
		Set<String> valueSet = Collections.singleton(value);
		queryParams.put(param, valueSet);
		
		return createParams(queryParams);
	}
	
	private QueryParams createSingleParams(Map<String, String> queryParams){
		Map<String, Set<String>> params = new HashMap<String, Set<String>>();
		for(String key: queryParams.keySet()){
			String value = queryParams.get(key);
			params.put(key, Collections.singleton(value));
		}
		
		return createParams(params);
	}
	
	private QueryParams createParams(Map<String, Set<String>> queryParams){
		
		QueryParamsBuilder sut = new QueryParamsBuilder(new DefaultQueryParamsParser());
		return sut.buildQueryParams(queryParams);
	}
	
	private QueryParams emptyParams(){
		Map<String, Set<String>> queryParams = new HashMap<String, Set<String>>();
		return createParams(queryParams);
	}
}
