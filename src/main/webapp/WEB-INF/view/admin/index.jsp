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
<title>今日头条-管理员中心</title>
</head>
<link rel="stylesheet" href="/resource/css/bootstrap.min.css">
<script type="text/javascript" src="/resource/js/jquery-3.2.1.js"></script>
<script type="text/javascript" src="/resource/js/bootstrap.min.js"></script>
<body>
	<div class="container-fluid">
		<div class="row bg-dark">
			<div class="col-md-12" style="height:70px">
				<img class="rounded-circle" src="/resource/images/logo.png" style="width:70px;height:70px;margin-right:20px">
				<font color="white" size="5">管理员中心</font>
			</div>
		</div>
		<div class="row" style="height:650px;padding-top:5px">
			<div class="col-md-2 bg-info rounded" >
				<div class="list-group mt-2 text-center">
				  <a href="#" url="/admin/articles"  class="list-group-item list-group-item-warning active">
				  	 审核文章
				  </a>
				  <a href="#" url="/admin/users"  class="list-group-item list-group-item-warning ">管理用户</a>
				  <a href="#"   class="list-group-item list-group-item-warning ">管理栏目</a>
				  <a href="#"  class="list-group-item list-group-item-warning ">系统设置</a>
				</div>
			</div>
			<div class="col-md-10" id="center" style="background-color:#EEEED1">
				
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function(){
		$("#center").load("/admin/articles");
		$("a").click(function(){
			var url = $(this).attr("url");
			$("a").removeClass("active");
			$(this).addClass("active");
			$("#center").load(url);
		})
	})
</script>
</html>