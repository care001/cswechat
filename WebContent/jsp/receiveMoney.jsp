<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>已收款</title>
    <link rel="stylesheet" href="jsp/css/weui.css"/>
    <link rel="stylesheet" href="jsp/css/noCard.css"/>
</head>
<body>
<section class="receiveMoney">
	<div class="weui_msg">
	    <div class="weui_icon_area"><i class="icon_success"></i></div>
	    <div class="weui_text_area">
	        <h2 class="weui_msg_title">已收款￥<em><%=Double.valueOf((String)request.getAttribute("money")) %></em>元</h2>
	        <p class="weui_msg_desc tanksText">感谢您使用金诚集团无卡消费系统！</p>
	    </div>
	</div>
</section>
</body>
</html>
