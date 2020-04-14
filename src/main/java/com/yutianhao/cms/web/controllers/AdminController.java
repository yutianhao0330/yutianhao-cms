package com.yutianhao.cms.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.yutianhao.cms.domain.Article;
import com.yutianhao.cms.domain.User;
import com.yutianhao.cms.service.ArticleService;
import com.yutianhao.cms.service.UserService;
/**
 * 
    * @ClassName: AdminController
    * @Description: 管理员中心
    * @author thyu
    * @date 2020年4月3日
    *
 */
@Controller
@RequestMapping("admin")
public class AdminController {
	@Autowired
	private ArticleService articleService;
	@Autowired
	private UserService userService;
	/**
	 * 
	    * @Title: index
	    * @Description: 进入管理员的首页
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	@RequestMapping(value= {"","/","index"})
	public String index() {
		return "admin/index";
	}
	/**
	 * 
	    * @Title: articles
	    * @Description: 查询文章列表
	    * @param @param model
	    * @param @param article
	    * @param @param pageNum
	    * @param @param pageSize
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	@RequestMapping("articles")
	public String articles(Model model,Article article,@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "8")Integer pageSize) {
		if(null==article || null==article.getStatus()) {
			article.setStatus(0);
		}
		PageInfo<Article> page = articleService.getArticleList(article, pageNum, pageSize);
		model.addAttribute("page", page);
		model.addAttribute("articleList", page.getList());
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("article", article);
		return "admin/articles";
	}
	/**
	 * 
	    * @Title: getArticle
	    * @Description: 根据id获得文章信息
	    * @param @param id
	    * @param @return    参数
	    * @return Article    返回类型
	    * @throws
	 */
	@GetMapping("article")
	@ResponseBody
	public Article getArticle(Integer id) {
		return articleService.getById(id);
	}
	/**
	 * 
	    * @Title: update
	    * @Description: 更新文章的方法
	    * @param @param article
	    * @param @return    参数
	    * @return boolean    返回类型
	    * @throws
	 */
	@RequestMapping("update")
	@ResponseBody
	public boolean update(Article article) {
		return articleService.update(article);
	}
	/**
	 * 
	    * @Title: users
	    * @Description: 查询用户列表
	    * @param @param model
	    * @param @param username
	    * @param @param pageNum
	    * @param @param pageSize
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	@RequestMapping("users")
	public String users(Model model,User user,@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "4")Integer pageSize) {
		PageInfo<User> page = userService.getUserList(user, pageNum, pageSize);
		model.addAttribute("page", page);
		model.addAttribute("userList", page.getList());
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("user", user);
		return "admin/users";
	}
	/**
	 * 
	    * @Title: updateUser
	    * @Description: 修改用户信息
	    * @param @param user
	    * @param @return    参数
	    * @return boolean    返回类型
	    * @throws
	 */
	@RequestMapping("updateUser")
	@ResponseBody
	public boolean updateUser(User user) {
		return userService.updateUser(user);
	}
}
