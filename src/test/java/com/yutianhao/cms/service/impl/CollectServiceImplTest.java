package com.yutianhao.cms.service.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yutianhao.cms.domain.Article;
import com.yutianhao.cms.enums.ArticleEnum;
import com.yutianhao.cms.service.CollectService;

public class CollectServiceImplTest extends SpringJunit{
	
	@Autowired
	private CollectService collectService;
	@Test
	public void testInsert() {
		
	}

	@Test
	public void testGetCollectsByUserId() {
		
	}

	@Test
	public void testDeleteCollectById() {
	
	}

	@Test
	public void testDeleteCollectByUAId() {
		ArticleEnum html = ArticleEnum.PIC;
		System.out.println(html.getCode());
	}

	@Test
	public void testIsCollected() {
		System.out.println(collectService.isCollected(3, 105));
	}

}
