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
<title>收藏列表</title>
</head>
<body>
		<input type="hidden" name="pageNum" value="${requestScope.pageNum}">
		<ul class="list-unstyled">
			<c:forEach items="${requestScope.collectList}" var="collect">
				<li class="media pt-2">
					<a href="${collect.url}" target="_blank">
 				   		<img src="/pic/${collect.article.picture}" style="width:156px;height:100px" class="mr-3 rounded" alt="...">
 				   	</a>
				   <div class="media-body ml-2">
				     <h5 class="mt-0 mb-1">
				     	<a href="${collect.url}" target="_blank" style="text-decoration: none;color: black">
				     		${collect.text}
				     	</a>
				     </h5>
				     <p class="pt-2">
				     	收藏时间:&nbsp;
				     	<fmt:formatDate value="${collect.created}" pattern="yyyy-MM-dd HH:mm:ss"/>
				     	<button type="button" class="btn btn-link" onclick="discollect(${collect.id})">
						  	删除收藏
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
</body>
<script type="text/javascript">
	function page(pageNum){
		$("#center").load("/my/collects?pageNum="+pageNum);
	}
	function discollect(id){
		$.get("/my/discollect",{"id":id},function(success){
			if(success){
				var pageNum = $(":hidden[name='pageNum']").val();
				$("#center").load("/my/collects?pageNum="+pageNum);
			}else{
				alert("操作失败!");
			}
		})
	}
</script>
</html>