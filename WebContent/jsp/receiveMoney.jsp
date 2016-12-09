<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>已收款</title>
    <link rel="stylesheet" href="css/weui.css"/>
    <link rel="stylesheet" href="css/noCard.css"/>
</head>
<body>
<section class="receiveMoney">
	<div class="weui_msg">
	    <div class="weui_icon_area"><i class="icon_success"></i></div>
	    <div class="weui_text_area">
	        <h2 class="weui_msg_title">已收款￥<em id="showmoney">0.00</em>元</h2>
	        <p class="weui_msg_desc tanksText">感谢您使用金诚集团无卡消费系统！</p>
	    </div>
	</div>
</section>
<script src="js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
function getParam(name) {
	 var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if (r != null)
         return decodeURI(r[2]);   //对参数进行decodeURI解码
     return null;      
  }
  $(function(){
  	var money= getParam("money");
  	$("#showmoney").html(money);
  });
</script>
</body>
</html>
