package com.yutianhao.cms.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yutianhao.cms.dao.ArticleMapper;
import com.yutianhao.cms.domain.Article;
import com.yutianhao.cms.repository.ArticleRepository;

/**
 * 
    * @ClassName: ImportMysql2Es
    * @Description: TODO(这里用一句话描述这个类的作用)
    * @author thyu
    * @date 2020年5月6日
    *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application.xml")
public class ImportMysql2Es {
	@Autowired
	private ArticleMapper articleMapper;
	@Autowired
	private ArticleRepository articleRepository;
	@Test
	public void testImport() {
		Article article = new Article();
		article.setStatus(1);
		article.setDeleted(0);
		article.setContentType(0);
		//查询mysql
		List<Article> articleList = articleMapper.getArticleList(article);
		//存入ES
		articleRepository.saveAll(articleList);
	}
}
