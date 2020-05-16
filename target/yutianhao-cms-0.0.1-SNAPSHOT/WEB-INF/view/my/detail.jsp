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
<title>个人信息</title>
</head>
<script type="text/javascript" src="/resource/js/jquery-3.2.1.js"></script>
<script type="text/javascript" src="/resource/js/my97datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/resource/js/bootstrap.min.js"></script>
<body>
	<div class="container">
		<form id="updForm">
			<input type="hidden" name="id" value="${sessionScope.user.id}">
			<div class="form-group">
				<label for="username">用户名</label>
				<input type="text" class="form-control" id="username" 
				value="${sessionScope.user.username}" disabled="disabled">
			</div>
			<div class="form-group">
				<label for="nickname">昵称</label>
				<input type="text" class="form-control" id="nickname" 
				value="${sessionScope.user.nickname}" name="nickname">
			</div>
			<div class="form-group">
				<label for="birthday">生日</label>
				<input type="text" class="form-control" id="birthday" 
					value="<fmt:formatDate value="${sessionScope.user.birthday}" pattern="yyyy-MM-dd"/>" 
					name="birthday" onclick="WdatePicker()">
			</div>
			<div class="form-group form-inline">
				性别&nbsp;&nbsp;&nbsp;
				<div class="form-check form-check-inline">
					<input type="radio" name="gender" id="male"
						class="form-check-input" value="1" 
						<c:if test="${sessionScope.user.gender==1}">checked='checked'</c:if>
						>
					<label for="male" class="form-check-label">男</label>
				</div>
				<div class="form-check form-check-inline">
					<input type="radio" name="gender" id="female"
						class="form-check-input" value="0"
						<c:if test="${sessionScope.user.gender==0}">checked='checked'</c:if>
						>
					<label for="female" class="form-check-label">女</label>
				</div>
			</div>
			<div class="form-group">
				<button type="button" class="btn btn-info" onclick="upd()">保存</button>
				<button type="reset" class="btn btn-warning">重置</button>
			</div>
		</form>
	</div>
</body>
<script type="text/javascript">
	function upd(){
		$.ajax({
			url:"my/updateUser",
			type:"post",
			data:$("#updForm").serialize(),
			success:function(success){
				if(success){
					alert("保存成功");
					$("#center").load("/my/detail");
				}else{
					alert("保存失败！");
				}
				
			}
		})
	}
</script>
</html>