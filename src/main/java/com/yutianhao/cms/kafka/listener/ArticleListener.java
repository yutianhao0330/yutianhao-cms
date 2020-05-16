package com.yutianhao.cms.kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListener;

import com.alibaba.fastjson.JSON;
import com.yutianhao.cms.dao.ArticleMapper;
import com.yutianhao.cms.domain.Article;

/**
 * 
    * @ClassName: ArticleListener
    * @Description: kafka消费者端
    * @author thyu
    * @date 2020年5月13日
    *
 */
public class ArticleListener implements MessageListener<String, String>{
	@Autowired
	private ArticleMapper articleMapper;
	@Override
	public void onMessage(ConsumerRecord<String, String> data) {
		// TODO Auto-generated method stub
		String json = data.value();
		//将接收到的json字符串解析为article对象
		Article article = JSON.parseObject(json, Article.class);
		//存入mysql
		articleMapper.insert(article);
	}
	
}
