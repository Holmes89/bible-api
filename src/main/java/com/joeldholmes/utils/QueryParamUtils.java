package com.joeldholmes.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.queryParams.RestrictedPaginationKeys;
import io.katharsis.queryParams.RestrictedSortingValues;
import io.katharsis.queryParams.params.FilterParams;
import io.katharsis.queryParams.params.SortingParams;


public class QueryParamUtils {

//	public static <T> Map<String, Set<String>> getFilters(Class<T> resource, QueryParams qParam){
//		Map<String, Set<String>> results = new HashMap<String, Set<String>>();
//		Map<String, FilterParams> qParamMap = qParam.getFilters().getParams();
//		
//		for(Field f: resource.getFields()){
//			String fieldName = f.getName();
//			FilterParams filterParam = qParamMap.get(fieldName);
//			if(filterParam!=null){
//				Set<String> filterSet = filterParam.getParams().get("");
//				if(filterSet!=null && !filterSet.isEmpty()){
//					results.put(fieldName, filterSet);
//				}
//			}
//		}
//		return results;
//	}
//	
//	public static Map<String, Set<String>> getFilters(QueryParams qParam){
//		Map<String, Set<String>> results = new HashMap<String, Set<String>>();
//		Map<String, FilterParams> qParamMap = qParam.getFilters().getParams();
//		
//		for(String key: qParamMap.keySet()){
//			FilterParams filterParam = qParamMap.get(key);
//			Set<String> filterSet = filterParam.getParams().get("");
//			if(filterSet!=null && !filterSet.isEmpty()){
//				results.put(key, filterSet);
//			}
//		}
//		return results;
//	}
	
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
	
//	public static Pageable getPageable(QueryParams params){
//		Sort sort = null;
//		Map<String, SortingParams> sortingParams = params.getSorting().getParams();
//		if (sortingParams != null && sortingParams.size() > 0)
//		{
//			Direction dir = Direction.DESC;
//			List<String> sortList = new ArrayList<String>();
//			for (SortingParams p : sortingParams.values())
//			{
//				//this is [name] or [shortName] above
//				for (String sortP : p.getParams().keySet())
//				{
//					sortList.add(sortP);
//					RestrictedSortingValues rsv = p.getParams().get(sortP);
//					dir = rsv == RestrictedSortingValues.asc ? Direction.ASC : Direction.DESC;
//				}
//			}
//			
//			if(sortList.size() > 0)
//			{
//				sort = new Sort(dir, sortList);
//			}
//		}
//		return getPageable(params, sort);
//		
//	}
//	
//	public static Pageable getPageable(QueryParams params, Sort sort){
//		Map<RestrictedPaginationKeys,Integer> pagination = params.getPagination();
//		PageRequest pag = null;
//		if (pagination != null && pagination.size() > 0)
//		{
//			//PageRequest is page based, if using offset 
//			
//			Integer pageNumber = pagination.get(RestrictedPaginationKeys.number);
//			Integer pageSize = pagination.get(RestrictedPaginationKeys.size);
//			
//			//spring data doesn't support offset paging.
//			//Integer offset = pagination.get(RestrictedPaginationKeys.offset);
//			//Integer limit = pagination.get(RestrictedPaginationKeys.limit);
//			
//			if (pageNumber == null){
//				pageNumber=0;
//			}
//			if(pageSize == null){
//				pageSize=25;
//			}
//			pag = new PageRequest(pageNumber, pageSize, sort);
//				
//			
//		}
//		else if(sort !=null ){
//			pag = new PageRequest(0, 25, sort);
//		}
//		else{
//			pag = new PageRequest(0, 25);
//		}
//		
//		return pag;
//	}

	
}
