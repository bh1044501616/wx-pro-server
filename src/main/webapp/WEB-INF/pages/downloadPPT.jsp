<%@ page language="java" contentType="text/html;UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="basePath" value="${pageContext.request.contextPath}"></c:set>
<c:set var="baseUrl" value="http://localhost:8080/smallPorject1.0/"></c:set>
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
        background: #eee;
        width: 100%;
    }
    #dates{
    	width:95%;
    	margin:auto;
    	background:#fff;
    	border-bottom:1px solid #eee;
    	height:50px;
    	margin-top:30px;
    }
    #dates span{
    	width:23.75%;
    	font-size:40px;
    	display:inline-block;
    	text-align:center;
    	height:50px;
    	line-height:50px;
    }
    
    #contents{
        width: 95%;
        margin: auto;
        background: #fff;
    }
    .content{
        width: 100%;
        border-bottom:1px solid #eee;
    }
    .domain{
        font-size: 30px;
        height:50px;
        line-height:50px;
    }
    .detail{
        font-size: 25px;
        display: flex;
    }
    .detail .loc{
        margin-left: 20px;
    }
    .detail{
        font-size: 25px;
        display: flex;
    }
    .topics .topic_box{
        font-size: 20px;
    }
     .topics .topic_box .info-box{
        display: flex;
    }
    .topics .topic_box .topic{
        margin-left: 20px;
    }
    .topics .topic_box .lecture{
        margin-left: 20px;
        display:block;
    }
    .topics .topic_box .lecture .top-box .left-box img{
        height:50px;
        width:50px;
    }
    .topics .topic_box .lecture .top-box,bottom-box{
        display:flex;
    }
    div{
    	margin-bottom:5px;
    	padding:5px;
    }
</style>
<script type="text/javascript">
    var contentFormat = '<div class="content" onclick="checkDetail(this)" data-ids="[ids]" href="aaa"><div class="domain">[domain]</div><div class="detail"><div class="time">9:00-17:00</div><div class="loc">[loc]</div></div><div class="topics">[topic]</div></div>';
    var topicFormat = '<div class="topic_box" id="[id]"><div class="info-box"><div class="begintime">[begintime]</div><div class="endtime">-[endtime]</div></div><div class="topic">[topic]</div></div>';
	$(document).ready(function(){
		doLoadScheduleByDate('20180801');
    });
	
	function doLoadScheduleByDate(dateStr){
		var contentsObj = $("div#contents");
        $.ajax({
            url:'info/' + dateStr + '.do',
            success:function(res){
            	var contents = res.data;
                var contentsStr = "";
                for(let i=0;i<contents.length;i++){
	                var domain = contents[i].domain;
	                var loc = contents[i].loc; 
	
	                var content = contentFormat.replace('[domain]',domain).replace('[loc]',loc);
	                var topics = contents[i].topics;
	                var topicsStr = "";
	                var ids = new Array();
                   	for(let j=0;j<topics.length;j++){
	                   var begintime = topics[j].begintime;
	                   var endtime = topics[j].endtime;
	                   var topic = topics[j].topic;
	                   var id = topics[j].id;
	                   var topicItem = topicFormat.replace('[begintime]',begintime).replace('[endtime]',endtime).replace('[topic]',topic).replace("[id]",id);
	                   topicsStr += topicItem;
	                   
	                   var id = topics[j].id;
	                   ids.push(id);
                   	}
                   	content = content.replace('[topic]',topicsStr);

                   	//绑定值
                   	idsStr = '[' + ids[0];
                   	for(let j=1;j<ids.length;j++){
                   		idsStr += ','+ids[j];
                   		if(j == ids.length - 1){
                   			idsStr += ']';
                   		}
                   	}
                 	content = content.replace('[ids]',idsStr);
                   	contentsStr += content;
               	}
               	contentsObj.html(contentsStr);
            }
        });
	}
	
	function checkDetail(obj){
		
		let idsStr = $(obj).attr("data-ids");
		var ids = $.parseJSON(idsStr);
		
		//
		$.ajax({
			url:'detail.do',
			method:'POST',
			dataType:'json',
			data:{
				ids:idsStr
			},
			success:function(res){
				var sourceContent = $($(obj).html());
				var modal = $("");
				var detailedContent = $.extend(true,modal,sourceContent);
				var topics = res.data;
				let HTML = '<div class="lecture"><div class="top-box"><div class="left-box"><img src="[pic]"/></div><div class="right-box"><div class="name">[name]</div><div class="intro">[introduction]</div></div></div><div class="bottom-box"><div class="ppt">[pptName]</div><a class="download" src="images/download.jpg" href="[ppt]">下载</a></div></div>'
				
				for(let i=0;i<ids.length;i++){
					let topic = topics[ids[i]];
					var lecturersStr = "";
					for(let j=0;j<topic.length;j++){
						let lecturer = topic[j];
					 	var lectuerStr = HTML.replace("[pic]",lecturer.pic).replace('[name]',lecturer.name).replace('[introduction]',lecturer.introduction).replace('[pptName]',lecturer.pptName).replace('[ppt]',lecturer.ppt);
					 	lecturersStr += lectuerStr;
					}
					modal.find("div#" + ids[i]).append(lecturersStr);
				}
				
				$("div.modal-body").html(modal);
				$('div#myModal').modal('show');
			}
		})
	}
	
	
	
	function updateContents(obj){
		console.log('a');
		var dateStr = $(obj).attr("data-date");
		doLoadScheduleByDate(dateStr);
	}
	
</script>  
</head>
<body>
	<div id="dates">
		<span data-date="20180715" onclick="updateContents(this)">2018-07-15</span>
		<span data-date="20180716" onclick="updateContents(this)">2018-07-16</span>
		<span data-date="20180717" onclick="updateContents(this)">2018-07-17</span>
		<span data-date="20180718" onclick="updateContents(this)">2018-07-18</span>
	</div>
    <div id="contents">
    </div>
    
    
    
    <div class="modal fade" id="myModal">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title" id="myModalLabel">详细信息</h4>
	            </div>
	            <div class="modal-body">在这里添加一些文本</div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	            </div>
	        </div><!-- /.modal-content -->
	    </div><!-- /.modal -->
	</div>
</body>
</html>