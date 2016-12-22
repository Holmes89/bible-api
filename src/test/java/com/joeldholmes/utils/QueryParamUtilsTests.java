package com.joeldholmes.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.joeldholmes.resources.SearchTextResource;

import io.katharsis.queryParams.DefaultQueryParamsParser;
import io.katharsis.queryParams.QueryParams;
import io.katharsis.queryParams.QueryParamsBuilder;


public class QueryParamUtilsTests {
	
	@Test
	public void testGetFilter(){
		Map<String, Set<String>> params = new HashMap<String, Set<String>>();
		params.put("filter[searchTerm]", Collections.singleton("foo"));
		params.put("filter[blah]", Collections.singleton("bar"));
		QueryParams qParams = this.createParams(params);
		
		Map<String, Set<String>> filters = QueryParamUtils.getFilters(SearchTextResource.class, qParams);
		
		Assert.assertEquals(1, filters.size());
		Assert.assertNotNull(filters.get("searchTerm"));
		Assert.assertNull(filters.get("blah"));
	}
	
	private QueryParams createParams(Map<String, Set<String>> queryParams){
		 QueryParamsBuilder sut = new QueryParamsBuilder(new DefaultQueryParamsParser());
		 return sut.buildQueryParams(queryParams);
	}

}
