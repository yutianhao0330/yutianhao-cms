<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- 视窗 -->
<meta name="viewport" content="width=device-width,initial-scale=1.0">
<title>Insert title here</title>
</head>
<body>
	<input type="hidden" name="pageNum" value="${requestScope.pageNum}">
	<ul class="list-unstyled">
		<c:forEach items="${requestScope.commentList}" var="comment">
			<li class="media pt-2">
				<div class="media-body ml-2">
					${comment.content}
					<p class="pt-2">
						评论时间:&nbsp;
						<fmt:formatDate value="${comment.created}"
							pattern="yyyy-MM-dd HH:mm:ss" /><br>
						评论文章: 
						<a href="/detail?id=${comment.article.id}#commentPosition" target="_blank" style="text-decoration: none">
							${comment.article.title}
						</a>
						<button type="button" class="btn btn-link"
							onclick="delcomment(${comment.id},${comment.article.id})">删除评论</button>
					</p>
				</div>
			</li>
			<hr>
		</c:forEach>
	</ul>
	<div>
		<jsp:include page="/WEB-INF/view/common/pages.jsp" />
		共${page.total}条评论
	</div>
</body>
<script type="text/javascript">
	function page(pageNum){
		$("#center").load("/my/comments?pageNum="+pageNum);
	}
	function delcomment(id,articleId){
		if(confirm("确定删除这条评论吗？")){
			$.get("/my/deleteComment",{"id":id,"articleId":articleId},function(success){
				if(success){
					var pageNum = $(":hidden[name='pageNum']").val();
					$("#center").load("/my/comments?pageNum="+pageNum);
				}else{
					alert("删除失败!");
				}
			})
		}
	}
</script>
</html>