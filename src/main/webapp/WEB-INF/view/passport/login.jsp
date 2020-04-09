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
<title>用户登录</title>
</head>
<link rel="stylesheet" href="/resource/css/jquery/screen.css">
<link rel="stylesheet" href="/resource/css/bootstrap.min.css">
<script type="text/javascript" src="/resource/js/jquery-3.2.1.js"></script>
<script type="text/javascript" src="/resource/js/jquery.validate.js"></script>
<script type="text/javascript" src="/resource/js/bootstrap.min.js"></script>
<body>
	<div class="container">
		<form id="form1">
			<div class="form-group">
				<label for="username">用户名</label> 
				<input type="text" name="username" class="form-control" id="username"> 
			</div>
			<div class="form-group">
				<label for="password">密码</label> 
				<input type="password" name="password" class="form-control" id="password"> 
			</div>
			<div class="form-group">
				<button class="btn btn-info" type="submit">登录</button>
				<button class="btn btn-warning" type="reset">重置</button>
				<span id="msg" class="text-danger"></span>
			</div>
		</form>
	</div>
</body>
<script type="text/javascript">
	jQuery.validator.setDefaults({
		errorPlacement: function(error, element) {
		//把错误信息放到其父节点后面
		error.appendTo(element.parent());
	}});
	$(function(){
		$("#form1").validate({
			//定义校验规则
			rules:{
				username:{
					required:true,
				},
				password:{
					required:true,
				},
			},
			//不满足规则的消息提示
			messages:{
				username:{
					required:"用户名不能为空",
				},
				password:{
					required:"密码不能为空",
				},
			},
			submitHandler:function(){
				$.post("/passport/login",$("#form1").serialize(),function(result){
					if(result.code==200){//登录成功
						location.reload();
					}else{//登陆失败
						$("#msg").text(result.msg);
					}
				})
			}
			
		})
	})
</script>
</html>