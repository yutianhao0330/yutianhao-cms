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
<title>文章列表</title>
<style type="text/css">
.ex {
	width: 250px;
	white-space: nowrap; /*不换行的*/
	overflow: hidden; /*超出范围隐藏*/
	text-overflow: ellipsis; /*超出用省略号 */
}
</style>
</head>
<body>
	<form id="queryForm">
		<div class="mt-2 mb-2 form-inline">
			<div class="form-group form-inline">
				<input type="hidden" name="pageNum" value="${pageNum}">
				<input type="hidden" name="pageSize" value="${pageSize}">
				文章标题&nbsp; <input id="title" class="form-control" type="text"
					name="title" value="${article.title}">
			</div>
			<div class="form-group form-inline ml-4">
				审核状态&nbsp; <select id="status" class="form-control" name="status">
					<option value="2" 
						<c:if test="${article.status==2}">selected="selected"</c:if>>全部</option>
					<option value="1" 
						<c:if test="${article.status==1}">selected="selected"</c:if>>已通过</option>
					<option value="0" 
						<c:if test="${article.status==0}">selected="selected"</c:if>>待审</option>
					<option value="-1" 
						<c:if test="${article.status==-1}">selected="selected"</c:if>>未通过</option>
				</select>
			</div>
			<button type="button" class="btn btn-info ml-3" onclick="page(1)">查询</button>
		</div>
	</form>

	<table class="table table-hover table-bordered text-center table-striped">
		<thead class="thead-dark">
			<tr>
				<th>序号</th>
				<th style="width:250px;">文章标题</th>
				<th>所属栏目</th>
				<th>所属分类</th>
				<th>作者</th>
				<th>发布时间</th>
				<th>是否热门</th>
				<th>状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<c:forEach items="${requestScope.articleList}" var="article" varStatus="index">
			<tr>
				<td>${index.count}</td>
				<td>
					<div class="ex">
						${article.title}
					</div>
				</td>
				<td>${article.channel.name}</td>
				<td>${article.category.name}</td>
				<td>${article.user.username}</td>
				<td><fmt:formatDate value="${article.created}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					<c:if test="${article.hot==0}">
						<input type="button" class="btn btn-info btn-sm" value="否" onclick="toggleHot(${article.id},this)"/>
					</c:if>
					<c:if test="${article.hot==1}">
						<input type="button" class="btn btn-danger btn-sm" value="是" onclick="toggleHot(${article.id},this)"/>
					</c:if>
				</td>
				<td>${article.status==0?"待审":article.status==1?"通过":"未通过"}</td>
				<td>
					<button type="button" class="btn btn-link" onclick="detail(${article.id})" data-toggle="modal" data-target="#exampleModalLong">
						  详情
					</button>
				</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="100">
				<jsp:include page="/WEB-INF/view/common/pages.jsp"></jsp:include>
			</td>
		</tr>
	</table>
	
	<!-- 模态框 -->
	<div class="modal fade" id="exampleModalLong" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
	  <div class="modal-dialog modal-lg" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="exampleModalLongTitle">
	        	<span id="articleTitle"></span>
	        	<br>
	        	<input type="hidden" id="articleId">
	        	<span id="author" class="font-weight-light" style="font-size: 18px"></span>&nbsp;
	        	<span id="created" class="font-weight-light" style="font-size: 18px"></span>
	        </h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body" id="content">
	      	 
	      </div>
	      <div class="modal-footer">
	      	<span id="msg" class="text-danger"></span>
	      	<span id="statusInfo" class="font-weight-light" style="font-size: 18px"></span>
       		<input id="success" type="button" class="btn btn-success" value="通过"  onclick="check(1)">
       		<input id="fail" type="button" class="btn btn-warning" value="驳回"  onclick="check(-1)">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
	      </div>
	    </div>
	  </div>
	</div>
</body>
<script type="text/javascript">
	function page(pageNum){
		$(":hidden[name='pageNum']").val(pageNum);
		var info = $("#queryForm").serialize();
		$("#center").load("/admin/articles?"+info);
	}
	function detail(id){
		//查询文章详情
		$(":hidden#articleId").val(id);
		$("#msg").empty();
		$("#articleTitle").empty();
		$("#author").empty();
		$("#created").empty();
		$("#content").empty();
		$.get("/my/article",{"id":id},function(article){
			$("#articleTitle").append(article.title);
			$("#author").append(article.user.username);
			$("#created").append(new Date(article.created).toLocaleString());
			$("#content").append(article.content);
			if(article.status==1){
				$("input#fail").show();
				$("input#success").hide();
				$("#statusInfo").html("已通过&nbsp;");
			}else if(article.status==-1){
				$("input#success").show();
				$("input#fail").hide();
				$("#statusInfo").html("已驳回&nbsp;");
			}else{
				$("input#success").show();
				$("input#fail").show();
				$("#statusInfo").empty();
			}
		},"json");
	}
	function check(status){
		$("#statusInfo").empty();
		var id = $(":hidden#articleId").val();
		$.ajax({
			url:"/admin/update",
			data:{"id":id,"status":status},
			success:function(success){
				if(success){
					$("#msg").text("操作成功");
				}else{
					$("#msg").text("操作失败");
				}
			}
		})
	}
	$("#exampleModalLong").on("hide.bs.modal",function(){
		$(".modal-backdrop").remove();
		$("body").removeClass("modal-open");
		$("body").removeAttr("style");
		var pageNum = $(":hidden[name='pageNum']").val();
		page(pageNum);
	})
	function toggleHot(id,obj){
		var hotVal = $(obj).val();
		var hot = (hotVal=="是"?0:1);
		$.ajax({
			url:"/admin/update",
			data:{"id":id,"hot":hot},
			success:function(success){
				if(success){
					$(obj).removeClass();
					if(hot){
						$(obj).val("是");
						$(obj).prop("class","btn btn-danger btn-sm");
					}else{
						$(obj).val("否");
						$(obj).prop("class","btn btn-info btn-sm");
					}
				}else{
					alert("操作失败!");
				}
			}
		})
	}
</script>
</html>