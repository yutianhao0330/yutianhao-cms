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
<title>${article.title}</title>
</head>
<link rel="stylesheet" href="/resource/css/bootstrap.min.css">
<link rel="stylesheet" href="/resource/css/index.css">
<script type="text/javascript" src="/resource/js/jquery-3.2.1.js"></script>
<script type="text/javascript" src="/resource/js/popper.min.js"></script>
<script type="text/javascript" src="/resource/js/bootstrap.min.js"></script>
<body>
	<div class="container-fluid">
		<div class="row" style="height:55px">
			<div class="col-md-12 bg-dark pt-2" >
				<font color="white" size="5">下载APP</font>
				<div class="float-sm-right">
					<c:if test="${sessionScope.user==null}">
						<button class="btn btn-link btn-sm text-decoration-none"
							data-toggle="modal" data-target="#register" onclick="toLogin()">
							<font color="white">登录</font>
						</button>
						<button class="btn btn-link btn-sm text-decoration-none"
							data-toggle="modal" data-target="#register" onclick="reg()">
							<font color="white">注册</font>
						</button>
					</c:if>
					<c:if test="${sessionScope.user!=null}">
						<div class="btn-group dropleft">
							<button type="button" class="btn btn-sm btn-secondary dropdown-toggle"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false">${sessionScope.user.username}</button>
							<div class="dropdown-menu">
								<!-- Dropdown menu links -->
								<ul>
									<li><a href="/my" target="_blank">个人中心</a></li>
									<li><a onclick="logout()" href="#">注销</a></li>
								</ul>				
							</div>
						</div>
					</c:if>
				</div>
			</div>
		</div>
		<div class="row mt-2">
			<div class="col-md-2">
				<div id="left">
					<ul>
						<li class="mt-3"><a class="channel-item" href="#">评论:${article.commentNum}</a></li>
						<li><a class="channel-item" href="#">转发</a></li>
						<li><a class="channel-item" href="#">微博</a></li>
						<li><a class="channel-item" href="#">Qzone</a></li>
						<li><a class="channel-item" href="#">微信</a></li>
					</ul>
				</div>
			</div>
			<!-- 文章详情 -->
			<div class="col-md-7">
				<p>
					<a href="/?channelId=${article.channel.id}">${article.channel.name}</a>
					->
					<a href="/?channelId=${article.channel.id}&categoryId=${article.category.id}">${article.category.name}</a>
					->
					正文
				</p>
				<h3>${article.title}</h3>
				<p>
					${article.user.username}  &nbsp;
					<fmt:formatDate value="${article.created}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</p>
				<p>
					${article.content}
				</p>
			</div>
			<!-- 右侧边栏 -->
			<div class="col-md-3">
				<div class="card" style="width: 18rem;">
					<div class="card-header">
						<b>${user.username}的其他文章</b>
					</div>
					<div class="card-body">
						<ul class="list-unstyled">
							<c:if test="${empty requestScope.articleList}">
								暂无文章
							</c:if>
							<c:forEach items="${requestScope.articleList}" var="article">
								<li class="media">
									<a href="/detail?id=${article.id}" target="_blank" class="text-dark font-weight-bold">
										<img src="/pic/${article.picture}" class="mr-3 rounded" alt="..." style="width:50px;height:50px">
									</a>
									<div class="media-body ex">
										<a href="/detail?id=${article.id}" target="_blank" class="text-dark">${article.title}</a>
									</div>
								</li>
								<hr>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>	
			
		</div>
	</div>
	
<!-- 注册登录页面 -->
<div class="modal fade" id="register" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="title"></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body" id="passport">
        	
      </div>
    </div>
  </div>
</div>
</body>
<script type="text/javascript">
	//注册
	function reg(){
		$("#passport").load("/passport/reg");
		$("#title").text("用户注册");
	}
	function toLogin(){
		$("#passport").load("/passport/login");
		$("#title").text("用户登录");
	}
	function logout(){
		$.ajax({
			url:"/passport/logout",
			success:function(success){
				if(success){
					location.reload();
				}else{
					alert("异常错误!");
				}
			}
		})
	}
</script>
</html>