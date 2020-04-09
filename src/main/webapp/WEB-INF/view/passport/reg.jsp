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
<title>用户注册</title>
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
				<label for="repassword">确认密码</label> 
				<input type="password" name="repassword" class="form-control" id="repassword"> 
			</div>
			<div class="form-group">
				<label>性别</label>&nbsp;&nbsp;
				<div class="form-group form-check form-check-inline">
					<input type="radio" name="gender" class="form-check-input" checked="checked" id="male" value="1">
					<label class="form-check-label" for="male">男</label> 
				</div>
				<div class="form-group form-check form-check-inline">
					<input type="radio" name="gender" class="form-check-input" id="female" value="0">
					<label class="form-check-label" for="female">女</label> 
				</div>
			</div>
			<div class="form-group">
				<button class="btn btn-info" type="submit">注册</button>
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
					rangelength:[2,6],
					remote:{
						url:"/passport/checkUserName",
						type:"post",
						dataType:"json",
						data:{username: function(){return $("#username").val();}}
					}
				},
				password:{
					required:true,
					rangelength:[6,12],
				},
				repassword:{
					equalTo:"#password",
				},
			},
			//不满足规则的消息提示
			messages:{
				username:{
					required:"用户名不能为空",
					rangelength:"用户名长度在2到6之间",
					remote:"用户名已经存在",
				},
				password:{
					required:"密码不能为空",
					rangelength:"密码长度在6到12之间",
				},
				repassword:{
					equalTo:"两次输入密码不一致",
				},
			},
			submitHandler:function(){
				$.post("/passport/reg",$("#form1").serialize(),function(result){
					if(result.code==200){//注册成功
						$("#title").text(result.msg);
						$("#passport").load("/passport/login");
					}else{//注册失败
						$("#msg").text(result.msg);
					}
				})
			}
			
		})
	})
</script>
</html>