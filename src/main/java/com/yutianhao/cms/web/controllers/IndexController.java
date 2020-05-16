package com.yutianhao.cms.web.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.elasticsearch.action.search.SearchAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.thyu.common.utils.RandomUtil;
import com.yutianhao.cms.domain.Article;
import com.yutianhao.cms.domain.Category;
import com.yutianhao.cms.domain.Channel;
import com.yutianhao.cms.domain.Collect;
import com.yutianhao.cms.domain.Comment;
import com.yutianhao.cms.domain.Slide;
import com.yutianhao.cms.domain.User;
import com.yutianhao.cms.enums.ArticleEnum;
import com.yutianhao.cms.service.ArticleService;
import com.yutianhao.cms.service.CategoryService;
import com.yutianhao.cms.service.ChannelService;
import com.yutianhao.cms.service.CollectService;
import com.yutianhao.cms.service.CommentService;
import com.yutianhao.cms.service.SlideService;
import com.yutianhao.cms.service.UserService;
import com.yutianhao.cms.util.CMSException;
import com.yutianhao.cms.util.CMSResult;
import com.yutianhao.cms.util.HLUtils;
import com.yutianhao.cms.vo.ArticlePicVO;
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
	@Autowired
	private CollectService collectService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	/**
	 * 
	    * @Title: search
	    * @Description: 搜索框下从es中查询结果
	    * @param @param key
	    * @param @param model
	    * @param @param pageNum
	    * @param @param pageSize
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("search")
	public String search(HttpServletResponse response,String key,Model model,@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "4")Integer pageSize) {
		
		
		//如果key是空，回到首页
		if(null==key || key.length()==0) {
			try {
				response.sendRedirect("/index");
				return null;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		//从es中查询 
		long start = System.currentTimeMillis();
		PageInfo<Article> page = (PageInfo<Article>)HLUtils.findByHighLight(elasticsearchTemplate, Article.class, pageNum, pageSize, new String[]{"title"}, "id", key);
		long end = System.currentTimeMillis();
		System.err.println("es查询时间: "+(end-start)+"ms");
		model.addAttribute("articleList", page.getList());
		model.addAttribute("key", key);
		model.addAttribute("page", page);
		System.out.println(page);
		
		//用redis优化，查询栏目列表
		List<Channel> channelList = redisTemplate.opsForList().range("channelList", 0, -1);
		if(channelList==null || channelList.size()==0) {
			channelList = channelService.getChannelList();
			redisTemplate.opsForList().rightPushAll("channelList", channelList);
		}
		model.addAttribute("channelList", channelList);
		
		//右侧显示当前栏目的最新五篇文章,如果是首页面则显示所有文章的近5篇
		Article recentArticle = new Article();
		setAvailable(recentArticle);
		//==================redis优化最新五篇文章,每5分钟同步一次======================
		int recentId = 0;
		List<Article> recentArticles = (List<Article>)redisTemplate.opsForValue().get("recentArticles"+recentId);
		if(null==recentArticles || recentArticles.size()==0) {
			recentArticles = articleService.getArticleList(recentArticle, 1, 5).getList();
			redisTemplate.opsForValue().set("recentArticles"+recentId,recentArticles);
			redisTemplate.expire("recentArticles"+recentId, 5, TimeUnit.MINUTES);
		}
		model.addAttribute("recentArticles",recentArticles);
		
		//查询最近24小时的热门文章，并在里面随机四篇，使用redis优化
		List<Article> hot24Articles = (List<Article>)redisTemplate.opsForValue().get("hot24Articles");
		if(null==hot24Articles) {
			Article recent24HourHotArticle = new Article();
			setAvailable(recent24HourHotArticle);
			recent24HourHotArticle.setHot(1);
			//先查询出所有热点文章
			List<Article> hotArticles = articleService.getArticleList(recent24HourHotArticle, 1, Integer.MAX_VALUE).getList();
			//选取最近24小时的
			Calendar limit = Calendar.getInstance();
			limit.add(Calendar.HOUR, -24);
			Date limitTime = limit.getTime();
			hot24Articles = hotArticles.stream().filter((e)->(e.getCreated().getTime()>limitTime.getTime())).collect(Collectors.toList());
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
				redisTemplate.opsForValue().set("hot24Articles",articles24);
			}else {
				model.addAttribute("hot24Articles", hot24Articles);
				redisTemplate.opsForValue().set("hot24Articles",hot24Articles);
			}
			redisTemplate.expire("hot24Articles", 5, TimeUnit.MINUTES);
		}else {
			model.addAttribute("hot24Articles", hot24Articles);
		}
		
		
		return "index/index";
	}
	
	
	/**
	 * 
	    * @Title: index
	    * @Description: 进入首页
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"","/","index"})
	public String index(Model model,Article article,@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "4")Integer pageSize) {
		long start = System.currentTimeMillis();
		
		//查询条件放回作用域
		model.addAttribute("article", article);
		//用redis优化，查询栏目列表
		List<Channel> channelList = redisTemplate.opsForList().range("channelList", 0, -1);
		if(channelList==null || channelList.size()==0) {
			channelList = channelService.getChannelList();
			redisTemplate.opsForList().rightPushAll("channelList", channelList);
		}
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
			
			//查询热点文章,使用redis优化首页数据
			Article hotArticle = new Article();
			//查询的文章应为已发布，未删除，html类型
			setAvailable(hotArticle);
			hotArticle.setHot(1);
			PageInfo<Article> page;
			if(pageNum==1) {
				page = (PageInfo<Article>)redisTemplate.opsForValue().get("hotArticles");
				if(null==page || page.getList().size()==0) {
					page = articleService.getArticleList(hotArticle, pageNum, pageSize);
					redisTemplate.opsForValue().set("hotArticles",page);
					redisTemplate.expire("hotArticles", 5, TimeUnit.MINUTES);
				}
			}else {
				page = articleService.getArticleList(hotArticle, pageNum, pageSize);
			}
			model.addAttribute("page", page);
			model.addAttribute("articleList",page.getList());
			
			//查询广告,用redis优化
			List<Slide> slideList = (List<Slide>)redisTemplate.opsForValue().get("slideList");
			if(null==slideList || slideList.size()==0) {
				slideList = slideService.getSlideList();
				redisTemplate.opsForValue().set("slideList",slideList);
				redisTemplate.expire("slideList", 5, TimeUnit.MINUTES);
			}
			model.addAttribute("slideList", slideList);
		}
		
		//右侧显示当前栏目的最新五篇文章,如果是首页面则显示所有文章的近5篇
		Article recentArticle = new Article();
		setAvailable(recentArticle);
		if(null!=article.getChannelId()) {
			recentArticle.setChannelId(article.getChannelId());
			//查询栏目名称
			model.addAttribute("channelName", channelService.getChannelById(article.getChannelId()).getName());
		}
		//==================redis优化最新五篇文章,每5分钟同步一次======================
		int recentId = (null==article.getChannelId()?0:article.getChannelId());
		List<Article> recentArticles = (List<Article>)redisTemplate.opsForValue().get("recentArticles"+recentId);
		if(null==recentArticles || recentArticles.size()==0) {
			recentArticles = articleService.getArticleList(recentArticle, 1, 5).getList();
			redisTemplate.opsForValue().set("recentArticles"+recentId,recentArticles);
			redisTemplate.expire("recentArticles"+recentId, 5, TimeUnit.MINUTES);
		}
		model.addAttribute("recentArticles",recentArticles);
		
		//查询最近24小时的热门文章，并在里面随机四篇，使用redis优化
		List<Article> hot24Articles = (List<Article>)redisTemplate.opsForValue().get("hot24Articles");
		if(null==hot24Articles) {
			Article recent24HourHotArticle = new Article();
			setAvailable(recent24HourHotArticle);
			recent24HourHotArticle.setHot(1);
			//先查询出所有热点文章
			List<Article> hotArticles = articleService.getArticleList(recent24HourHotArticle, 1, Integer.MAX_VALUE).getList();
			//选取最近24小时的
			Calendar limit = Calendar.getInstance();
			limit.add(Calendar.HOUR, -24);
			Date limitTime = limit.getTime();
			hot24Articles = hotArticles.stream().filter((e)->(e.getCreated().getTime()>limitTime.getTime())).collect(Collectors.toList());
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
				redisTemplate.opsForValue().set("hot24Articles",articles24);
			}else {
				model.addAttribute("hot24Articles", hot24Articles);
				redisTemplate.opsForValue().set("hot24Articles",hot24Articles);
			}
			redisTemplate.expire("hot24Articles", 5, TimeUnit.MINUTES);
		}else {
			model.addAttribute("hot24Articles", hot24Articles);
		}
		
		long end = System.currentTimeMillis();
		System.err.println("访问首页时间: "+(end-start)+"ms");
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
	@SuppressWarnings("unchecked")
	@RequestMapping("detail")
	public String detail(Model model,HttpServletRequest request,Integer id,HttpSession session,@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "5")Integer pageSize) {
		//查询文章
		Article article = articleService.getById(id);
		model.addAttribute("article", article);
		//更新点击量
		String ip = request.getRemoteAddr();
		String key = "Hits_"+id+"_"+ip;
		if(!redisTemplate.hasKey(key)) {
			threadPoolTaskExecutor.execute(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					article.setHits(article.getHits()+1);
					articleService.update(article);
					redisTemplate.opsForValue().set(key, "");
					redisTemplate.expire(key, 5, TimeUnit.MINUTES);
				}
			});
		}
		
		//获取文章的userId
		Integer userId = article.getUserId();
		//根据userId获取这个作者的所有文章
		Article queryArticle = new Article();
		setAvailable(queryArticle);
		queryArticle.setUserId(userId);
		List<Article> userArticleList = articleService.getArticleList(queryArticle, 1, Integer.MAX_VALUE).getList();
		//过滤，选出这个作者的最近三篇文章(不包括当前这篇)
		List<Article> articleList = userArticleList.stream().filter(e->(e.getId()!=id.intValue())).limit(3).collect(Collectors.toList());
		model.addAttribute("articleList", articleList);
		//获取作者信息放入作用域
		User user = userService.getUserById(userId);
		model.addAttribute("user", user);
		//查询当前文章是否已被收藏
		User currentUser = (User)session.getAttribute("user");
		if(null!=currentUser && collectService.isCollected(currentUser.getId(), id)) {
			model.addAttribute("isCollected", true);
		}else {
			model.addAttribute("isCollected", false);
		}
		//查询当前文章的评论内容
		PageInfo<Comment> page = commentService.getCommentsByArticleId(id, pageNum, pageSize);
		model.addAttribute("page", page);
		model.addAttribute("commentList", page.getList());
		//如果当前文章是图文类文章，需要把图文信息解析出来放到作用域中
		if(article.getContentType()==ArticleEnum.PIC.getCode()) {
			String content = article.getContent();
			List<ArticlePicVO> pics = new ArrayList<ArticlePicVO>();
			Gson gson = new Gson();
			JsonArray array = new JsonParser().parse(content).getAsJsonArray();
			for (JsonElement element : array) {
				pics.add(gson.fromJson(element, ArticlePicVO.class));
			}
			model.addAttribute("pics", pics);
			System.out.println(pics);
		}
		return "index/article";
	}
	/**
	 * 
	    * @Title: disCollect
	    * @Description: 取消收藏
	    * @param @param userId
	    * @param @param articleId
	    * @param @return    参数
	    * @return boolean    返回类型
	    * @throws
	 */
	@RequestMapping("discollect")
	@ResponseBody
	public boolean disCollect(Integer userId,Integer articleId) {
		return collectService.deleteCollectByUAId(userId, articleId);
	}
	/**
	 * 
	    * @Title: disCollect
	    * @Description: 收藏文章
	    * @param @param collect
	    * @param @return    参数
	    * @return boolean    返回类型
	    * @throws
	 */
	@RequestMapping("collect")
	@ResponseBody
	public CMSResult<Collect> collect(Collect collect,HttpSession session) {
		CMSResult<Collect> result = new CMSResult<Collect>();
		
		try {
			if(null==session.getAttribute("user")) {
				throw new CMSException("请登录");
			}
			collect.setCreated(new Date());
			collectService.insert(collect);
			result.setCode(200);
		} catch (CMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(500);
			result.setMsg(e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			result.setCode(505);
			result.setMsg("未知错误!");
		}
		return result;
	}
	/**
	 * 
	    * @Title: comment
	    * @Description: 发表评论
	    * @param @param comment
	    * @param @param session
	    * @param @return    参数
	    * @return CMSResult<Comment>    返回类型
	    * @throws
	 */
	@RequestMapping("comment")
	@ResponseBody
	public CMSResult<Comment> comment(Comment comment,HttpSession session){
		CMSResult<Comment> result = new CMSResult<Comment>();
		try {
			if(null==session.getAttribute("user")) {
				throw new CMSException("请登录");
			}
			comment.setCreated(new Date());
			commentService.insert(comment);
			result.setCode(200);
		} catch (CMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(500);
			result.setMsg(e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			result.setCode(505);
			result.setMsg("未知错误!");
		}
		return result;
	}
}
