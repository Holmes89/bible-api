package com.joeldholmes.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import io.katharsis.queryParams.DefaultQueryParamsParser;
import io.katharsis.queryParams.QueryParams;
import io.katharsis.queryParams.QueryParamsBuilder;


public class QueryParamUtilsTests {
	
	@Test
	public void testGetSingleFilters(){
		//Construtor for test coverage
		QueryParamUtils util = new QueryParamUtils();
		
		QueryParams params = createParams("filters[foo]", "bar");
		Map<String, String> filters = QueryParamUtils.getSingleFilters(params);
		Assert.assertEquals(1, filters.keySet().size());
		Assert.assertEquals(1, filters.values().size());
		Assert.assertNotNull(filters.get("foo"));
		Assert.assertEquals("bar", filters.get("foo"));
	}

	private QueryParams createParams(String param, String value){
		Map<String, Set<String>> queryParams = new HashMap<String, Set<String>>();
		Set<String> valueSet = Collections.singleton(value);
		queryParams.put(param, valueSet);
		
		return createParams(queryParams);
	}
	
	private QueryParams createParams(Map<String, Set<String>> queryParams){
		
		QueryParamsBuilder sut = new QueryParamsBuilder(new DefaultQueryParamsParser());
		return sut.buildQueryParams(queryParams);
	}
}
