package com.yutianhao.cms.service.impl;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yutianhao.cms.domain.Article;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application.xml")
public class ArticleRepositoryTest {
	//@Autowired
	//private ArticleRepository articleRepository;
	//@Autowired
	//private ElasticsearchTemplate elasticsearchTemplate;
	/*
	@Test
	public void testFind() {
		Sort sort = new Sort(Direction.DESC,"id");
		Pageable pageable = PageRequest.of(0, 10, sort);
		Page<Article> page = articleRepository.findByTitleOrContent("中国", "中国", pageable);
		for(Article article:page.getContent()) {
			System.out.println(article.getId()+"::"+article.getTitle());
		}
	}
	*/
	/*
	@Test
	public void testESTemplate() {
		Pageable pageable = PageRequest.of(0, 10, Direction.DESC,"id");
		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery("中国", new String[] {"title","content"});
		SearchQuery query = new NativeSearchQueryBuilder().withQuery(queryBuilder)
				.withPageable(pageable).build();
		AggregatedPage<Article> page = elasticsearchTemplate.queryForPage(query, Article.class);
		for(Article article:page.getContent()) {
			System.out.println(article.getId()+"::"+article.getTitle());
		}
	}
	*/
	/*
	 * private static <T> PageInfo<T> pageToPageInfo(Page<T> page){
	 * 
	 * com.github.pagehelper.Page<T> gitpage = new com.github.pagehelper.Page<T>();
	 * 
	 * }
	 */
}
