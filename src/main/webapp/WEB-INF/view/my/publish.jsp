<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
String htmlData = request.getParameter("content1") != null ? request.getParameter("content1") : "";
%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8" />
	<title>KindEditor JSP</title>
		<link rel="stylesheet" href="/resource/kindeditor/themes/default/default.css" />
	<link rel="stylesheet" href="/resource/kindeditor/plugins/code/prettify.css" />
	<script charset="utf-8" src="/resource/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="/resource/kindeditor/lang/zh-CN.js"></script>
	<script charset="utf-8" src="/resource/kindeditor/plugins/code/prettify.js"></script>
	<script>
		KindEditor.ready(function(K) {
			window.editor1 = K.create('textarea[name="content1"]', {
				cssPath : '/resource/kindeditor/plugins/code/prettify.css',
				uploadJson : '/resource/kindeditor/jsp/upload_json.jsp',
				fileManagerJson : '/resource/kindeditor/jsp/file_manager_json.jsp',
				allowFileManager : true,
				afterCreate : function() {
					var self = this;
					K.ctrl(document, 13, function() {
						self.sync();
						document.forms['example'].submit();
					});
					K.ctrl(self.edit.doc, 13, function() {
						self.sync();
						document.forms['example'].submit();
					});
				}
			});
			prettyPrint();
		});
	</script>
</head>
<body>
	<form id="publishForm">
		<div class="form-group">
			<label for="title">文章标题</label>
			<input type="text" id="title" class="form-control" name="title">
		</div>
		<div class="form-group form-inline">
			所属栏目:<select class="form-control" id="channel" name="channelId">
					<option value="0">请选择</option>
				  </select>
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
		<textarea name="content1" cols="100" rows="8" style="width:920px;height:200px;visibility:hidden;"><%=htmlspecialchars(htmlData)%></textarea>
		<br />
		<input type="button" class="btn btn-info" value="发布文章" onclick="publish()" />
	</form>
</body>
<script type="text/javascript">
	$(function(){
		$.get("/channel/channels",function(channelList){
			var html = "";
			//遍历栏目为下拉框赋值
			$(channelList).each(function(index,channel){
				html+="<option value="+channel.id+">"+channel.name+"</option>";
			})
			$("select#channel").append(html);
		},"json");
	});
	//为栏目添加改变时间，获取对应的分类信息
	$("select#channel").change(function(){
		$("select#category option:gt(0)").remove();
		//获取当前的栏目id
		var channelId = $(this).val();
		if(channelId){
			$.get("/channel/categories",
					{"channelId":channelId},
				function(categoryList){
					var html = "";
					//遍历栏目为下拉框赋值
					$(categoryList).each(function(index,category){
						html+="<option value="+category.id+">"+category.name+"</option>";
					})
					$("select#category").append(html);
				},"json");
		}
	})
	function publish(){
		var formData = new FormData($("#publishForm")[0]);
		//封装文章内容
		formData.set("content",editor1.html());
		$.ajax({
			url:"/my/publish",
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
<%!
private String htmlspecialchars(String str) {
	str = str.replaceAll("&", "&amp;");
	str = str.replaceAll("<", "&lt;");
	str = str.replaceAll(">", "&gt;");
	str = str.replaceAll("\"", "&quot;");
	return str;
}
%>