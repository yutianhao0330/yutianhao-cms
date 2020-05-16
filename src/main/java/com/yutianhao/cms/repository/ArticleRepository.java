package com.yutianhao.cms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.yutianhao.cms.domain.Article;

public interface ArticleRepository extends ElasticsearchRepository<Article, Integer>{
	Page<Article> findByTitle(String title,Pageable pageable);
	
	Page<Article> findByContent(String content,Pageable pageable);
	
	Page<Article> findByTitleAndContent(String title,String content,Pageable pageable);
	
	Page<Article> findByTitleOrContent(String title,String content,Pageable pageable);
}
