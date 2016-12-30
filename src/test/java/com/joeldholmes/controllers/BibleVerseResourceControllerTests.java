package com.joeldholmes.controllers;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.joeldholmes.repository.BibleVerseRepository;
import com.joeldholmes.resources.BibleVerseResource;

import io.katharsis.queryParams.QueryParams;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BibleVerseResourceControllerTests {

	
	@MockBean
	BibleVerseRepository bibleRepository;
	
	@Autowired
	BibleVerseResourceController controller;
	
	private BibleVerseResource resource = new BibleVerseResource(); 
	
	@Test
	public void testFindOne() throws Exception{
		Mockito.when(bibleRepository.findOne(Mockito.anyString())).thenReturn(resource);
		BibleVerseResource result = controller.findOne("asdf");
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testFindAllIds() throws Exception{
		Mockito.when(bibleRepository.findAll(Mockito.anyList(), Mockito.any(QueryParams.class))).thenReturn(Collections.singletonList(resource));
		Iterable<BibleVerseResource> result = controller.findAll(Collections.singletonList("joytoworld"), new QueryParams());
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testFindAll() throws Exception{
		Mockito.when(bibleRepository.findAll(Mockito.any(QueryParams.class))).thenReturn(Collections.singletonList(resource));
		Iterable<BibleVerseResource> result = controller.findAll(new QueryParams());
		Assert.assertNotNull(result);
	}
	
}
