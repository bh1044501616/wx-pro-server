<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="basePath" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
<head>
	<title>提交结果</title>
	<style type="text/css">
		*{
			margin: 0;
			padding: 0;
		}
		#messageBox{
			margin: auto;
		}
		div{
			text-align: center;
			margin-top: 30px;
		}
		div .message{
			color:#777;
		}
		#image{
			width:10%;
		}
	</style>
	<script type="text/javascript">
	</script>
</head>
<body>
	<%
		String state = "error";
		String state_temp = null;
		state_temp = (String)request.getAttribute("state");
		System.out.println(state_temp);
		if(state_temp!=null&&!"".equals(state_temp)){
			state = state_temp;
			System.out.println("state：null" + (state_temp==null));
			System.out.println("state:''" + "".equals(state_temp));
		}
		String message = "页面错误";
		String message_temp = (String)request.getAttribute("message");
		System.out.println(message_temp);
		if(message_temp!=null&&!"".equals(message_temp)){
			message = message_temp;
			System.out.println("message：null" + message_temp==null);
			System.out.println("message:''" + "".equals(message_temp));
		}
	%>
	<div id="messageBox">
		<div class="image">
			<img id="image" src="${basePath}/images/<%=state%>.png">
		</div>
		<div class="message">
			<h1><%=message %></h1>
		</div>
	</div>
</body>
</html>