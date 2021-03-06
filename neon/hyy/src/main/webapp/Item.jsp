<%@ page language="java" import="java.util.*, ajax.model.*, ajax.model.entity.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Item item = (Item) request.getAttribute("item");
int previous = item.getId() - 1;
int next = item.getId() + 1;

%>

<style>
	.font-white{
		color:white;
	}
</style>

<jsp:include page="views/includes/header.jsp"></jsp:include>



<div class="aj-body-left">
	<div style="height:10px;"></div>
	
	<jsp:include page="views/item/one.jsp"></jsp:include>


	<div class="btn-group" role="group">
	    <button class="btn btn-danger">
	    	<a class="font-white" href="OneJoke?id=<%=previous %>">
		    	<span class="glyphicon glyphicon-chevron-left"></span>
		        Previous</button></a>
	</div>
	
	<div class="btn-group" role="group">
	    <button id="aj-random-access-btn" class="btn btn-info" min="<%=Joke.minJokeId %>" max="<%=Joke.maxJokeId %>">
	        <span class="glyphicon glyphicon-random"></span>
	        Random</button>
	</div>
		
	<div class="btn-group" role="group">
	    <button class="btn btn-warning">
	    	<a class="font-white" href="OneJoke?id=<%=next %>">
		    	<span class="glyphicon glyphicon-chevron-right"></span>
		        Next
	    	</a>
	    </button>
	</div>
	
</div>

<div class="aj-body-right">
	<jsp:include page="views/includes/userLogin.jsp"></jsp:include>
	
	<jsp:include page="views/includes/allJokeTypesForHomePage.jsp"></jsp:include>
	
	<jsp:include page="/views/huodong/huodong.jsp"></jsp:include>

</div>

<div style="height:10px;"></div>


<script>
	$(function () {
		var btn = $("#aj-random-access-btn");
		btn.on("click", function () {
			var min = parseInt($(this).attr("min"));
			var max = parseInt($(this).attr("max"));
			
			var random = Math.floor(min + max * Math.random());
			location.href = "?id=" + random;
		})
	})
</script>

<%@ include file="/views/includes/footer.jsp" %>