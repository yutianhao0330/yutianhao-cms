package com.yutianhao.cms.service;

import com.github.pagehelper.PageInfo;
import com.yutianhao.cms.domain.Comment;

/**
 * 
    * @ClassName: CommentService
    * @Description: 评论类的service接口
    * @author thyu
    * @date 2020年4月12日
    *
 */
public interface CommentService {
	/**
	 * 
	    * @Title: insert
	    * @Description: 发布评论
	    * @param @param comment
	    * @param @return    参数
	    * @return boolean    返回类型
	    * @throws
	 */
	boolean insert(Comment comment);
	/**
	 * 
	    * @Title: getCommentsByArticleId
	    * @Description: 根据文章id获取评论信息
	    * @param @param articleId
	    * @param @param pageNum 
	    * @param @param pageSize
	    * @param @return    参数
	    * @return PageInfo<Comment>    返回类型
	    * @throws
	 */
	PageInfo<Comment> getCommentsByArticleId(Integer articleId,Integer pageNum,Integer pageSize);
	/**
	 * 
	    * @Title: getCommentsByUserId
	    * @Description: 根据用户id获取评论信息
	    * @param @param userId
	    * @param @param pageNum 
	    * @param @param pageSize
	    * @param @return    参数
	    * @return PageInfo<Comment>    返回类型
	    * @throws
	 */
	PageInfo<Comment> getCommentsByUserId(Integer userId,Integer pageNum,Integer pageSize);
	/**
	 * 
	    * @Title: deleteComment
	    * @Description: 删除评论
	    * @param @param comment
	    * @param @return    参数
	    * @return boolean    返回类型
	    * @throws
	 */
	boolean deleteComment(Comment comment);
}
