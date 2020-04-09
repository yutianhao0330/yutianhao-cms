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
<title>文章列表</title>
</head>
<body>
		<ul class="list-unstyled">
			<c:forEach items="${requestScope.articleList}" var="article">
				<li class="media pt-2">
 				   <img src="/pic/${article.picture}" style="width:156px;height:100px" class="mr-3 rounded" alt="...">
				   <div class="media-body ml-2">
				     <h5 class="mt-0 mb-1">
				     	${article.title}
				     </h5>
				     <p class="pt-2">
				     	<fmt:formatDate value="${article.created}" pattern="yyyy-MM-dd HH:mm:ss"/>
				     	<c:if test="${article.status==0}">
				     		<font color="#8B7500">审核中</font>
				     	</c:if>
				     	<c:if test="${article.status==1}">
				     		<font color="green">审核通过</font>
				     	</c:if>
				     	<c:if test="${article.status==-1}">
				     		<font color="red">审核未通过</font>
				     	</c:if>
				     	<button type="button" class="btn btn-link" onclick="detail(${article.id})" data-toggle="modal" data-target="#exampleModalLong">
						  详情
						</button>
				     </p>
				   </div>
				 </li>
				 <hr>
			</c:forEach>
		</ul>
	
		<div>
			<jsp:include page="/WEB-INF/view/common/pages.jsp"/>	
		</div>
	
	<!-- 模态框 -->
	<div class="modal fade" id="exampleModalLong" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
	  <div class="modal-dialog modal-lg" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="exampleModalLongTitle">
	        	<span id="title"></span>
	        </h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body" id="content">
	      	 
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
	      </div>
	    </div>
	  </div>
	</div>
</body>
<script type="text/javascript">
	function page(pageNum){
		$("#center").load("/my/articles?pageNum="+pageNum);
	}
	function detail(id){
		//查询文章详情
		$("#title").empty();
		$("#content").empty();
		$.get("/my/article",{"id":id},function(article){
			$("#title").append(article.title);
			$("#content").append(article.content);
		},"json");
	}
</script>
</html>