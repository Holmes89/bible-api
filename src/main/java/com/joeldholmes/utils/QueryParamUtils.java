package com.joeldholmes.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.queryParams.params.FilterParams;


public class QueryParamUtils {

	
	public static Map<String, String> getSingleFilters(QueryParams params) {
		Map<String, String> results = new HashMap<String, String>();
		Map<String, FilterParams> qParamMap = params.getFilters().getParams();
		
		for(String key: qParamMap.keySet()){
			FilterParams filterParam = qParamMap.get(key);
			Set<String> filterSet = filterParam.getParams().get("");
			if(filterSet!=null && !filterSet.isEmpty()){
				if(filterSet.iterator().hasNext())
					results.put(key, filterSet.iterator().next());
			}
		}
		return results;
	}
}
