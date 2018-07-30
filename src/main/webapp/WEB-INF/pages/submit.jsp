<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="basePath" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信息录入</title>
	<script src="${basePath}/js/jquery.js"></script>
    <script src="${basePath}/js/bootstrap-transition.js"></script>
    <script src="${basePath}/js/bootstrap-alert.js"></script>
    <script src="${basePath}/js/bootstrap-modal.js"></script>
    <script src="${basePath}/js/bootstrap-dropdown.js"></script>
    <script src="${basePath}/js/bootstrap-scrollspy.js"></script>
    <script src="${basePath}/js/bootstrap-tab.js"></script>
    <script src="${basePath}/js/bootstrap-tooltip.js"></script>
    <script src="${basePath}/js/bootstrap-popover.js"></script>
    <script src="${basePath}/js/bootstrap-button.js"></script>
    <script src="${basePath}/js/bootstrap-collapse.js"></script>
    <script src="${basePath}/js/bootstrap-carousel.js"></script>
    <script src="${basePath}/js/bootstrap-typeahead.js"></script>
    <script src="${basePath}/js/fileinput.min.js"></script>
    <link href="${basePath}/css/bootstrap.css" rel="stylesheet">
    <link href="${basePath}/css/bootstrap-responsive.css" rel="stylesheet">
    <link href="${basePath}/css/fileinput.min.css" rel="stylesheet">
    <style>
    	body{
            width: 100%;
            margin-top: 40px;
    	}
        .info-box{
            display: flex;
            position: relative;
            width: 100%;
        }
        .info-box input{
            position: absolute;
            right: 0;
            width: 80%;
        }
        textarea{
            position: absolute;
            right: 0;
            width: 80%;
            height:300%;
        }
        .footer{
            display: flex;
            flex-direction: row-reverse;
            width:100%;
        }
        .footer input{
            width: 40%;
            margin: 20px;
            float:right;
        }
        .file-box input{
        	
        }
        .file-box .input-box{
        	width:80%;
        	float:right;
        }
        .form-group{
        	/* display:flex; */
        }
        .form-group *{
        	display:inline-block;
        }
        .form-group *{
        	display:inline-block;
        }
        .form-group .form-control{
        	float:right;
        }
        
        #dateFormat{
        	float:right;
        }
        
        
        body{
            width: 100%;
            margin-top: 40px;
    	}
        #save{
        	width:90%;
        	padding:20px;
        	margin:auto;
        }
        #upload{
        	width:90%;
        	padding:20px;
        	margin:auto;
        }
        .saveMethod{
        	background: linear-gradient(to right, #0083cc, #0048cc);
        	-webkit-background-clip: text;
        	color: transparent;
        }
        .form-box{
        	width:50%;
        	float:left;
        }
        .title{
        	margin-left:20px;
        }
    </style>
    <script type="text/javascript">
    $(document).ready(function(){
    	bindEvent();
    	addPPT();
    });
    
    function bindEvent(){
    	$('.my-file-input').change(function(){
    		var id = $(this).attr('id');
    		var type = id.substring(id.indexOf('-')+1);
    		console.log(type);
    		$('#photoCover-'+type).val($(this).val());
    		console.log($(this).val());
    	});
    }
	</script>  
</head>
<body>
<div class="title">
<h1>IQ alliance</h1>
<h2>微信小程序信息录入系统</h2>
</div>
<div class="form-box">
<form method="post" action="save.do" enctype="application/x-www-form-urlencoded" id="save">
	<h3 class="saveMethod">选择录入基本信息导入数据</h3>
    <div class="info-box">
         发布时间:<input name="time"></input>
    </div>
    	<br/>
   	 <p id="dateFormat">提示：格式为yyyy/mm/dd(如2018/04/21)</p>
    <br/><br/>
	
    <div class="info-box">
         文章主题:<textarea name="theme" rows="10" cols="30"></textarea>
    </div><br/><br/><br/>
    <div class="info-box">
         图片链接:<textarea name="cover" rows="10" cols="30"></textarea>
    </div><br/><br/><br/>
    <div class="info-box">
         文章链接:<textarea name="detailUrl" rows="10" cols="30"></textarea>
    </div><br/><br/><br/>
    <div class="info-box">
         简要梗概:<textarea name="digest" rows="10" cols="30"></textarea>
    </div><br/><br/><br/>
    
	 <div class="form-group">
	   <label for="name">信息种类</label>
	   <select class="form-control" name="kind">
	     <option value="0">默认</option>
	     <option value="1">智联联盟</option>
	     <option value="2">Tid大会</option>
	   </select>
	 </div>
    
    <div class="footer">
        <input type="submit" class="btn btn-large btn-primary" value="提交"/>
    </div>
</form></div>

<div class="form-box">
<form method="post" action="uploadCrawler.do" enctype="multipart/form-data" id="upload">
    <div id="file-boxes">
    	<h3 class="saveMethod">选择导入爬虫文本导入数据</h3>
	    <div class="file-box">
	        导入爬虫文件:
	        <div class="input-box">
	        	<input id="file-text" type="file" style="display:none" name="text" class="my-file-input">  
				<div class="input-append">  
				    <input id="photoCover-text" class="input-large" type="text">  
				    <a class="btn btn-primary" onclick="$('input[id=file-text]').click();">Browse</a>  
				</div>  
			</div>
	    </div><br/>
    </div>
	<div>必须是特定的微信爬虫文件,不要随意上传txt文件</div>
    
    <div class="footer">
        <input type="submit" class="btn btn-large btn-primary" value="上传"/>
    </div>
</form></div>
</body>
</html>