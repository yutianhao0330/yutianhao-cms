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
<title>用户列表</title>
</head>
<body>
	<form id="queryForm">
		<div class="mt-2 mb-2 form-inline">
			<div class="form-group form-inline">
				<label for="username">用户名:</label> <input type="text"
					name="username" id="username" class="form-control"
					value="${user.username}">
			</div>
			<div class="form-group form-inline ml-4">
				用户状态&nbsp; <select id="status" class="form-control" name="locked">
					<option value="2"
						<c:if test="${user.locked==2}">selected="selected"</c:if>>全部</option>
					<option value="1"
						<c:if test="${user.locked==1}">selected="selected"</c:if>>封禁</option>
					<option value="0"
						<c:if test="${user.locked==0}">selected="selected"</c:if>>正常</option>
				</select>
			</div>
			<input type="button" class="btn btn-warning ml-2" value="查询"
				onclick="page(1)">
		</div>
		<input type="hidden" name="pageNum" value="${pageNum}">
		<input type="hidden" name="pageSize" value="${pageSize}">
	</form>
	<div class="container-fluid">
		<table class="table table-striped table-bordered text-center">
			<thead class="thead-dark">
				<tr>
					<th>序号</th>
					<th>用户名</th>
					<th>昵称</th>
					<th>生日</th>
					<th>性别</th>
					<th>注册时间</th>
					<th>修改时间</th>
					<th>用户状态</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${requestScope.userList}" var="user" varStatus="i">
					<tr>
						<td>${i.count}</td>
						<td>${user.username}</td>
						<td>${user.nickname}</td>
						<td>
							<fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd"/>
						</td>
						<td>${user.gender==1?"男":"女"}</td>
						<td>
							<fmt:formatDate value="${user.created}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<td>
							<fmt:formatDate value="${user.updated}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<td>
							<c:if test="${user.role==1}">管理员</c:if>
							<c:if test="${user.role==0}">
								<c:if test="${user.locked==0}">
									<input type="button" class="btn btn-success" value="正常" onclick="toggleLocked(${user.id},this)">
								</c:if>
								<c:if test="${user.locked==1}">
									<input type="button" class="btn btn-warning" value="封禁" onclick="toggleLocked(${user.id},this)">
								</c:if>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="100">
						<jsp:include page="/WEB-INF/view/common/pages.jsp"/>
					</td>
				</tr>
			</tfoot>
			<tr>
			</tr>
		</table>
	</div>
</body>
<script type="text/javascript">
	function page(pageNum){
		$(":hidden[name='pageNum']").val(pageNum);
		var info = $("#queryForm").serialize();
		$("#center").load("/admin/users?"+info);
	}
	function toggleLocked(id,obj){
		var lockedVal = $(obj).val();
		var locked = (lockedVal=="正常"?1:0);
		$.get("/admin/updateUser",{"id":id,"locked":locked},function(success){
			if(success){
				$(obj).removeClass();
				if(locked){
					$(obj).val("封禁");
					$(obj).prop("class","btn btn-warning");
				}else{
					$(obj).val("正常");
					$(obj).prop("class","btn btn-success");
				}
			}else{
				alert("操作失败!");
			}
		})
	} 
</script>
</html>