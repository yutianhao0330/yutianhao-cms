package com.yutianhao.cms.web.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.github.pagehelper.PageInfo;
import com.thyu.common.utils.RandomUtil;
import com.yutianhao.cms.domain.Article;
import com.yutianhao.cms.domain.Category;
import com.yutianhao.cms.domain.Channel;
import com.yutianhao.cms.domain.User;
import com.yutianhao.cms.service.ArticleService;
import com.yutianhao.cms.service.CategoryService;
import com.yutianhao.cms.service.ChannelService;
import com.yutianhao.cms.service.SlideService;
import com.yutianhao.cms.service.UserService;
/**
 * 
    * @ClassName: IndexController
    * @Description: cms首页控制器
    * @author thyu
    * @date 2020年4月7日
    *
 */
@Controller
public class IndexController {
	@Autowired
	private ChannelService channelService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private SlideService slideService;
	@Autowired
	private UserService userService;
	/**
	 * 
	    * @Title: index
	    * @Description: 进入首页
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	@RequestMapping(value = {"","/","index"})
	public String index(Model model,Article article,@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "4")Integer pageSize) {
		//查询条件放回作用域
		model.addAttribute("article", article);
		
		//查询所有的栏目
		List<Channel> channelList = channelService.getChannelList();
		model.addAttribute("channelList", channelList);
		setAvailable(article);
		
		//如果栏目不为空，则查询栏目下的分类
		if(null!=article.getChannelId()) {
			//根据栏目id查询分类
			List<Category> categoryList = categoryService.getCategoriesByChannelId(article.getChannelId());
			model.addAttribute("categoryList", categoryList);
			//查询该栏目下的所有文章或分类下的所有文章
			PageInfo<Article> page = articleService.getArticleList(article, pageNum, pageSize);
			model.addAttribute("page", page);
			model.addAttribute("articleList",page.getList());
		}else {
			//查询热点文章
			Article hotArticle = new Article();
			//查询的文章应为已发布，未删除，html类型
			setAvailable(hotArticle);
			hotArticle.setHot(1);
			PageInfo<Article> page = articleService.getArticleList(hotArticle, pageNum, pageSize);
			model.addAttribute("page", page);
			model.addAttribute("articleList",page.getList());
			//查询广告
			model.addAttribute("slideList", slideService.getSlideList());
		}
		
		//右侧显示当前栏目的最新五篇文章,如果是首页面则显示所有文章的近5篇
		Article recentArticle = new Article();
		setAvailable(recentArticle);
		if(null!=article.getChannelId()) {
			recentArticle.setChannelId(article.getChannelId());
			//查询栏目名称
			model.addAttribute("channelName", channelService.getChannelById(article.getChannelId()).getName());
		}
		PageInfo<Article> recentArticles = articleService.getArticleList(recentArticle, 1, 5);
		model.addAttribute("recentArticles",recentArticles.getList());
		
		//查询最近24小时的热门文章，并在里面随机四篇
		Article recent24HourHotArticle = new Article();
		setAvailable(recent24HourHotArticle);
		recent24HourHotArticle.setHot(1);
		//先查询出所有热点文章
		List<Article> hotArticles = articleService.getArticleList(recent24HourHotArticle, 1, Integer.MAX_VALUE).getList();
		//选取最近24小时的
		Calendar limit = Calendar.getInstance();
		limit.add(Calendar.HOUR, -24);
		Date limitTime = limit.getTime();
		List<Article> hot24Articles = hotArticles.stream().filter((e)->(e.getCreated().getTime()>limitTime.getTime())).collect(Collectors.toList());
		//要显示四条，满足条件的文章数小于等于4的话，直接全部返回，否则从中随机抽取四篇
		int size = hot24Articles.size();
		if(size>4) {
			int[] random = RandomUtil.subRandom(0, size-1, 4);
			//对数组排序，保证结果是按照xml中默认的时间排序
			Arrays.sort(random);
			List<Article> articles24 = new ArrayList<Article>();
			for(int i=0;i<random.length;i++) {
				articles24.add(hot24Articles.get(random[i]));
			}
			model.addAttribute("hot24Articles", articles24);
		}else {
			model.addAttribute("hot24Articles", hot24Articles);
		}
		return "index/index";
	}
	/**
	 * 
	    * @Title: setAvailable
	    * @Description: 设置首页板块文章的查询条件
	    * @param @param article    参数
	    * @return void    返回类型
	    * @throws
	 */
	private void setAvailable(Article article) {
		//查询的文章应为已发布，未删除，html类型
		article.setStatus(1);
		article.setDeleted(0);
		article.setContentType(0);
	}
	/**
	 * 
	    * @Title: detail
	    * @Description: 跳转到文章详情页
	    * @param @param id
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	@RequestMapping("detail")
	public String detail(Model model,Integer id) {
		//查询文章
		Article article = articleService.getById(id);
		model.addAttribute("article", article);
		//获取文章的userId
		Integer userId = article.getUserId();
		//根据userId获取这个作者的所有文章
		Article queryArticle = new Article();
		setAvailable(queryArticle);
		queryArticle.setUserId(userId);
		List<Article> userArticleList = articleService.getArticleList(queryArticle, 1, Integer.MAX_VALUE).getList();
		//过滤，选出这个作者的最近三篇文章(不包括当前这篇)
		List<Article> articleList = userArticleList.stream().filter(e->(e.getId()!=id)).limit(3).collect(Collectors.toList());
		model.addAttribute("articleList", articleList);
		//获取作者信息放入作用域
		User user = userService.getUserById(userId);
		model.addAttribute("user", user);
		return "index/article";
	}
}
