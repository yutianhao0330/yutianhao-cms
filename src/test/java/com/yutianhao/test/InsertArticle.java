package com.yutianhao.test;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.thyu.common.utils.DateUtil;
import com.thyu.common.utils.FileUtil;
import com.thyu.common.utils.RandomUtil;
import com.yutianhao.cms.dao.ArticleMapper;
import com.yutianhao.cms.domain.Article;
import com.yutianhao.cms.repository.ArticleRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application.xml")
public class InsertArticle {
	@Autowired
	private ArticleMapper articleMapper;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@Autowired
	private ArticleRepository articleRepository;
	@Test
	public void testInsert() throws IOException {
		File file = new File("E:\\articles");
		File[] files = file.listFiles();
		for (File aFile : files) {
			Article article = new Article();
			//设置文章标题
			article.setTitle(aFile.getName().replace(".txt", ""));
			//设置文章内容
			String content = FileUtil.readFile(aFile, "utf8");
			article.setContent(content);
			//设置文章摘要
			if(content.length()<140) {
				article.setSummary(content);
			}else {
				article.setSummary(content.substring(0, 140));
			}
			//设置点击量(0到100次)
			article.setHits(RandomUtil.random(0, 100));
			//随机热门，热门的概率是10%
			article.setHot(Math.random()>0.9?1:0);
			//随机频道
			article.setChannelId(RandomUtil.random(1, 9));
			//随机发布日期
			Calendar start = Calendar.getInstance();
			start.set(2019, 0, 1);
			article.setCreated(DateUtil.randomDate(start.getTime(), new Date()));
			//随机其他字段
			article.setUserId(5);
			article.setStatus(1);
			article.setDeleted(0);
			article.setContentType(0);
			article.setCommentNum(0);
			
			//kafka发送数据
			kafkaTemplate.send("articles",JSON.toJSONString(article));
		}
	}
	@Test
	public void import2ES() {
		Article article = new Article();
		article.setDeleted(0);
		article.setStatus(1);
		article.setContentType(0);
		List<Article> articleList = articleMapper.getArticleList(article);
		articleRepository.saveAll(articleList);
	}
}
