<%@ page language="java" import="java.util.*, ajax.model.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


Integer pageIndex = (Integer)request.getAttribute("page");
%>

<style>
	.font-white{
		color:white;
	}
</style>


<jsp:include page="views/includes/header.jsp"></jsp:include>
<jsp:include page="views/huodong/homeImagesRoll.jsp"></jsp:include>


<jsp:include page="views/joke/IndexPageChoice.jsp"></jsp:include>
<div style="height:10px;"></div>

 
 <c:forEach items="${jokes }" var="item">
 	<c:set scope="request" var="joke" value="${item }" />
 	<jsp:include page="views/joke/one.jsp"></jsp:include>
 </c:forEach>


<div style="height:10px;"></div>
<jsp:include page="views/joke/IndexPageChoice.jsp"></jsp:include>


<jsp:include page="views/includes/footer.jsp"></jsp:include>