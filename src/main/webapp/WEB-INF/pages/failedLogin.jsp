<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="basePath" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="${basePath}/css/bootstrap.css" rel="stylesheet">
<style>
	div{
		width:40%;
		margin:auto;
		margin-top:50px;
	}
</style>
</head>
<body>
	<div class="alert alert-danger">账号或密码错误，请返回重试！</div>
</body>
</html>