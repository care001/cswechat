<%@page import="com.jc.entity.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% HttpSession session2=request.getSession();
	User user=(User)session2.getAttribute("admin");
	String showname="游客";
	String islogin="0";
	if(user==null||user.getUserid()<=0){
		islogin="0";
	}else{
		showname=user.getUsername();
		islogin="1";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>金诚集团无卡消费后台管理系统</title>
<link href="css/admin.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="top">
<input type="hidden" value='<%=islogin %>' id="islogin">
  <p class="topLeft">无卡消费后台管理系统</p>
  <p class="topRight">您好，<%=showname %><a href="javascript:void(0);" class="set">&nbsp;</a><a href="javascript:void(0);" class="logout" id="logout">&nbsp;</a></p>
</div>
<script src="js/jquery-1.9.1.min.js"></script>

<script>
 $(document).ready(function(){
	if($("#islogin").val()==0){
		top.location.href="login.html";
	}
 	$("#logout").click(function(){
 		$.ajax({ 
         	type: "post", 
            cache: false, 
            dataType:'json',
            url: "Loginout.ext",
            data:{
            time:"2016-06"
            },
			success: function(msg) {
            	top.location.href="login.html";
            }       
       	});
       	
 	});

 });
</script>
</body>
</html>