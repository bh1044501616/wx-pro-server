<%@ page language="java" contentType="text/html;UTF-8" pageEncoding="UTF-8"%>
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
    		margin:auto;
            width: 40%;
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
        }
        .footer input{
            width: 20%;
            margin: 20px;
        }
        .file-box input{
        	
        }
        .file-box .input-box{
        	width:80%;
        	float:right;
        }
    </style>
    <script type="text/javascript">
    	var pptIndex = 1;
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
	    function addPPT(){
	    	$('#file-ppt'+pptIndex).change(function(){
	    		$('#file-boxes').append(
	    				'<div class="file-box">'+
	    		        'ppt'+ (++pptIndex) +':'+
	    		        '<div class="input-box">'+
	    		        	'<input id="file-ppt'+ pptIndex +'" type="file" style="display:none" name="ppt'+ pptIndex +'" class="my-file-input">'+  
	    					'<div class="input-append">'+
	    					    '<input id="photoCover-ppt'+pptIndex+'" class="input-large" type="text">'+ 
	    					    '<a class=\"btn btn-primary\" onclick=\"$(\'#file-ppt'+pptIndex+'\').click();\">Browse</a>'+
	    					'</div>'+
	    				'</div>'+
	    		    '</div><br/>'	
	    		);
	    		addPPT();
	    		bindEvent();
	    	});
	    }
	</script>  
</head>
<body>
<h1>IQ alliance</h1>
<h2>微信小程序信息录入系统</h2>
<form method="post" action="save.do" enctype="multipart/form-data">
    <div class="info-box">
        大会时间：<input type="text" name="meetingTime"/>
    </div><br/>
    <div class="info-box">
        大会主题:<textarea name="meetingTheme" rows="10" cols="30"></textarea>
    </div><br/><br/><br/>
    <div class="info-box">
        主讲人:<input type="text" name="lecturerName"/>
    </div><br/>
    <div id="file-boxes">
	    <div class="file-box">
	        主讲人照片:<div class="input-box">
	        	<input id="file-image" type="file" style="display:none" name="image" class="my-file-input">  
				<div class="input-append">  
				    <input id="photoCover-image" class="input-large" type="text">  
				    <a class="btn btn-primary" onclick="$('input[id=file-image]').click();">Browse</a>  
				</div>  
			</div>
	    </div><br/>
	    <div class="file-box">
	        大会视频:<div class="input-box">
	        	<input id="file-video" type="file" style="display:none" name="video" class="my-file-input">  
				<div class="input-append">  
				    <input id="photoCover-video" class="input-large" type="text">  
				    <a class="btn btn-primary" onclick="$('input[id=file-video]').click();">Browse</a>  
				</div>  
			</div>
	    </div><br/>
	    <div class="file-box">
	        ppt1:
	        <div class="input-box">
	        	<input id="file-ppt1" type="file" style="display:none" name="ppt1" class="my-file-input">  
				<div class="input-append">
				    <input id="photoCover-ppt1" class="input-large" type="text">  
				    <a class="btn btn-primary" onclick="$('input[id=file-ppt1]').click();">Browse</a>  
				</div>  
			</div>
	    </div><br/>
    </div>
    
    <div class="footer">
        <input type="submit" class="btn btn-large btn-primary" value="上传"/>
    </div>
</form>
</body>
</html>