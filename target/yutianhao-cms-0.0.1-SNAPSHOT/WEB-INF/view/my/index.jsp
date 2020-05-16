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
<title>个人中心</title>
</head>
<link rel="stylesheet" href="/resource/css/bootstrap.min.css">
<script type="text/javascript" src="/resource/js/jquery-3.2.1.js"></script>
<script type="text/javascript" src="/resource/js/bootstrap.min.js"></script>
<body>
	<div class="container-fluid">
		<!-- head -->
		<div class="row">
			<div class="col-md-12" style="background-color: #4682B4;height:70px;padding-top:5px">
				<img alt="" class="rounded-circle" src="/resource/images/logo.png" style="width:65px;height:65px;margin-right:20px">
				<font color="white" size="5">今日头条-个人中心</font>
				<c:if test="${sessionScope.user!=null}">
					<font color="white" style="float: right" class="mr-5 mt-2">当前用户:${sessionScope.user.username}</font>
				</c:if>
			</div>
		</div>
		<!-- body -->
		<div class="row" style="height:800px;padding-top:5px">
			<!-- left -->
			<div class="col-md-2 bg-info pt-2 rounded">
				<div class="list-group mt-2 text-center" >
				  <a href="#" url="/my/articles" class="list-group-item list-group-item-warning active">
				  	  我的文章
				  </a>
				  <a href="#" url="/my/publish" class="list-group-item list-group-item-warning ">发布文章</a>
				  <a href="#" url="/my/publishpic" class="list-group-item list-group-item-warning ">发布图文</a>
				  <a href="#" url="/my/collects" class="list-group-item list-group-item-warning ">我的收藏</a>
				  <a href="#" url="/my/comments" class="list-group-item list-group-item-warning ">我的评论</a>
				  <a href="#" url="/my/detail" class="list-group-item list-group-item-warning ">个人设置</a>
				</div>
			</div>
			<!-- 内容显示区域 -->
			<div class="col-md-10" id="center" style="background-color:#EEEED1">
				<div style="display:none">
					<!-- 包含kindEditor的内容 -->
					<jsp:include page="/resource/kindeditor/jsp/demo.jsp"></jsp:include>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	//为菜单添加绑定事件
	$(function(){
		$("#center").load("/my/articles");
		$("a").click(function(){
			var url = $(this).attr("url");
			$("a").removeClass("active");
			$(this).addClass("active");
			$("#center").load(url);
		})
	})
	
</script>
</html>