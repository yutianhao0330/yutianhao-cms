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
<title>发布图文</title>
</head>
<body>
	<div class="container-fluid">
		<form id="publishForm">
			<div class="form-group">
				<label for="title">图文标题</label>
				<input type="text" id="title" class="form-control" name="title">
			</div>
			<div class="form-group form-inline">
				所属栏目:&nbsp;图片&nbsp;<input type="hidden" name="channelId" value="9">
				所属分类:<select class="form-control" id="category" name="categoryId">
					  	<option value="0">请选择</option>
					  </select>
			</div>
			<div>
				<div class="form-group">
				    <input type="file" name="file" class="form-control-file" id="uploadfile">
				</div>
			</div>
			<!-- 文章內容 -->
			<br />
			<button type="button" class="btn btn-warning" onclick="addCard()">添加图文</button>
			<input type="button" class="btn btn-info" value="发布图文" onclick="publish()" />
			<hr color="red">
			<div id="picarea">
				<div id="carddiv">
					<div class="card" style="width: 18rem;float: left">
						<input type="file" name="picfiles" class="form-control-file">
						<div class="card-body">
							<label>图片信息: </label> 
							<textarea rows="5" cols="5" name="picinfos" class="form-control"></textarea>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
	
</body>
<script type="text/javascript">
	$(function(){
		$.get("/channel/categories",
				{"channelId":9},
			function(categoryList){
				var html = "";
				//遍历栏目为下拉框赋值
				$(categoryList).each(function(index,category){
					html+="<option value="+category.id+">"+category.name+"</option>";
				})
				$("select#category").append(html);
			},"json");
	});
	function addCard(){
		var html = $("#carddiv").html();
		$("#picarea").append(html);
	}
	function publish(){
		var formData = new FormData($("#publishForm")[0]);
		//封装文章内容
		formData.set("content",editor1.html());
		$.ajax({
			url:"/my/publishpic",
			type:"post",
			data:formData,
			processData:false,
			contentType:false,
			success:function(success){
				if(success){
					alert("发布成功!");
					//发布成功直接跳转到我的文章页面
					location.href="/my";
				}else{
					alert("添加文章失败!");
				}
			}
		})
	}
</script>
</html>