<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="basePath" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>智联联盟微信小程序管理系统</title>
<link href="${basePath}/css/bootstrap.css" rel="stylesheet">
<style>
	div{
		margin:auto;
		width:50%;
		text-align:center;
	}
	button{
		margin:auto;
		margin-top:50px;
		width:50%;
		height:50px;
	}
</style>
</head>
<body>
	<div>
		<p>
	    <a href="meeting/upload.do">
	    	<button type="button" class="btn btn-primary" 
    			data-toggle="button">Tid大会文件上传
			</button>
	    </a>
	    </p>
	    <p>
	    <a href="news/upload.do">
	    	<button type="button" class="btn btn-primary" 
    			data-toggle="button">更新动态
			</button>
	    </a>
	    </p>
	 </div>
</body>
</html>