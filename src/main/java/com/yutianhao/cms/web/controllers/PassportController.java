package com.yutianhao.cms.web.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import com.yutianhao.cms.domain.User;
import com.yutianhao.cms.service.UserService;
import com.yutianhao.cms.util.CMSException;
import com.yutianhao.cms.util.CMSResult;
/**
 * 
    * @ClassName: PassportController
    * @Description: 系统入口
    * @author thyu
    * @date 2020年4月9日
    *
 */
@Controller
@RequestMapping("passport")
public class PassportController {
	@Autowired
	private UserService userService;
	/**
	 * 
	    * @Title: reg
	    * @Description: 去注册页面
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	@GetMapping("reg")
	public String reg() {
		return "passport/reg";
	}
	@PostMapping("reg")
	@ResponseBody
	public CMSResult<User> register(Model model,User user) {
		CMSResult<User> result = new CMSResult<User>();
		try {
			userService.register(user);
			result.setCode(200);//错误码
			result.setMsg("恭喜注册成功,请登录");
		} catch (CMSException e) {
			// 捕获自定义异常
			e.printStackTrace();
			result.setCode(500);//错误码
			result.setMsg(e.getMessage());//错误消息
		} catch(Exception e) {
			e.printStackTrace();
			result.setCode(505);//错误码
			result.setMsg("未知错误，请联系管理员");//错误消息
		}
		return result;
	}
	/**
	 * 
	    * @Title: checkUserName
	    * @Description: 校验姓名
	    * @param @param username
	    * @param @return    参数
	    * @return boolean    返回类型
	    * @throws
	 */
	@RequestMapping("checkUserName")
	@ResponseBody
	public boolean checkUserName(String username) {
		return null==userService.getUserByName(username);
	}
	/**
	 * 
	    * @Title: login
	    * @Description: 去登陆页面
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	@GetMapping("login")
	public String login() {
		return "passport/login";
	}
	@PostMapping("login")
	@ResponseBody
	public CMSResult<User> login(User user,HttpSession session){
		CMSResult<User> result = new CMSResult<User>();
		try {
			User u = userService.login(user);
			result.setCode(200);
			result.setMsg("登录成功");
			//放session
			session.setAttribute("user", u);
		} catch (CMSException e) {
			// 捕获自定义异常
			e.printStackTrace();
			result.setCode(500);//错误码
			result.setMsg(e.getMessage());//错误消息
		} catch(Exception e) {
			e.printStackTrace();
			result.setCode(505);//错误码
			result.setMsg("未知错误，请联系管理员");//错误消息
		}
		return result;
	}
	/**
	 * 
	    * @Title: logout
	    * @Description: 注销用户
	    * @param @param session
	    * @param @return    参数
	    * @return String    返回类型
	    * @throws
	 */
	@RequestMapping("logout")
	@ResponseBody
	public boolean logout(HttpSession session) {
		session.invalidate();
		return true;
	}
}
