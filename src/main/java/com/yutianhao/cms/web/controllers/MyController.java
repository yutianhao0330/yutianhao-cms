package com.yutianhao.cms.web.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.yutianhao.cms.domain.Article;
import com.yutianhao.cms.domain.User;
import com.yutianhao.cms.service.ArticleService;
import com.yutianhao.cms.service.UserService;
/**
 * 
    * @ClassName: MyController
    * @Description: 个人中心的控制器
    * @author thyu
    * @date 2020年4月1日
    *
 */
@Controller
@RequestMapping("my")
public class MyController {
	@Autowired
	private ArticleService articleService;
	@Autowired
	private UserService userService;
	//进入个人中心首页支持三种访问方式
	@RequestMapping(value = {"","/","index"})
	public String index() {
		return "my/index";
	}
	/**
	 * 
	    * @Title: articles
	    * @Description: 显示我的文章
	    * @param @param model
	    * @param @param article
	    * @param @param pageNum
	    * @param @param pageSize
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	@RequestMapping("articles")
	public String articles(Model model,HttpSession session,Article article,@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "3")Integer pageSize) {
		User user = (User)(session.getAttribute("user"));
		if(null!=user) {
			article.setUserId(user.getId());
		}else {
			article.setUserId(-1);
		}
		PageInfo<Article> page = articleService.getArticleList(article, pageNum, pageSize);
		model.addAttribute("page", page);
		model.addAttribute("articleList", page.getList());
		return "my/articles";
	}
	/**
	 * 
	    * @Title: publish
	    * @Description: 去发布文章页面
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	@GetMapping("publish")
	public String publish() {
		return "my/publish";
	}
	/**
	 * 
	    * @Title: publish
	    * @Description: 执行发布
	    * @param @param article
	    * @param @param file
	    * @param @return    参数
	    * @return boolean    返回类型
	    * @throws
	 */
	@PostMapping("publish")
	@ResponseBody
	public boolean publish(Article article,MultipartFile file,HttpSession session) {
		//判断是否上传了文件
		if(null!=file && !file.isEmpty()) {
			String path = "E:/upload/cms/";
			String oldName = file.getOriginalFilename();
			String fileName = UUID.randomUUID().toString()+oldName.substring(oldName.lastIndexOf("."));
			try {
				file.transferTo(new File(path,fileName));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			article.setPicture(fileName);
		}
		//暂时先写死数据,待后续模块完成后改动
		User user = (User)(session.getAttribute("user"));
		article.setUserId(user.getId());
		article.setStatus(0);
		article.setCreated(new Date());
		article.setHits(0);
		article.setHot(0);
		article.setDeleted(0);
		article.setContentType(0);//普通html文本
		return articleService.insert(article);
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
	    * @Title: detail
	    * @Description: 去用户详情页
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	@RequestMapping("detail")
	public String detail() {
		return "my/detail";
	}
	/**
	 * 
	    * @Title: updateUser
	    * @Description: 更新用户信息
	    * @param @param user
	    * @param @return    参数
	    * @return boolean    返回类型
	    * @throws
	 */
	@RequestMapping("updateUser")
	@ResponseBody
	public boolean updateUser(User user,HttpSession session) {
		boolean success = userService.updateUser(user);
		//更新session中的用户信息
		if(null!=user.getId()) {
			session.removeAttribute("user");
			User newUser = userService.getUserById(user.getId());
			session.setAttribute("user", newUser);
		}
		return success;
	}
}
