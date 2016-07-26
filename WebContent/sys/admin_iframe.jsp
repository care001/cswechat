<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.jc.entity.User"%>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0,  user-scalable=no;" name="viewport" />
<meta name="format-detection" content="telphone=yes, email=yes" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>金诚集团无卡消费后台管理系统</title>
<link href="css/admin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.9.1.min.js"></script> 
</head>

<body>
<input type="hidden" id="merchantid" value=<%=request.getAttribute("merchantid") %>/>
    <div class="bread">
      <p class="breadNav">首页&nbsp; &gt; &nbsp;管理员信息</p>
      <p class="lineBread"><img src="images/lineBread.jpg" /></p>
    </div>
    <div class="content">
      <div class="articleList">
	   <div class="search">
          <div class="groupButtton">
          	<button type="submit" class="addBtn">添加</button>            
          </div>
        </div>
        <table width="100%" cellspacing="1" cellpadding="0" border="0">
          <thead>
            <tr>
              <th width="140" class="noLeftBorder">名称</th>
              <th width="200">类型</th>
              <th width="200">账号</th>
              <th width="200" class="noRightBorder">密码</th>
			  <th width="200">联系人</th>
			  <th width="200">联系人电话</th>
			  <th width="200">联系人地址</th>
			  <th width="140"></th>
            </tr>
          </thead>
          <tbody>
             <%
           		List<User> list=(List<User>)request.getAttribute("users");
           		
                 for(int i=0;i<list.size();i++){
                	 User user=(User)list.get(i);
 				%>
				<tr>
				<td class="noLeftBorder"><%=user.getUsername() %></td>
				<% String status=user.getStatus(); 
				if(status.equals("1")){%>
				<td>消费点账号</td>
				<%}else if(status.equals("2")){%>
				<td>商户账号</td>
				<%}else if(status.equals("3")){%>
				<td>一般管理员</td>
				<%}else if(status.equals("4")){%>
				<td>运营管理员</td>
				<%}else if(status.equals("5")){%>
				<td>系统管理员</td>
				<%}else if(status.equals("99")){%>
				<td>线上消费点账号</td>
				<%}
				%>
				<td><%=user.getLoginname() %></td>
				<td class="noRightBorder"><button id="reset">重置</button></td>
				<td><%=user.getContact()==null?"无":user.getContact() %></td>
				<td><%=user.getPhone()==null?"无":user.getPhone() %></td>
				<td><%=user.getAddress()==null?"无":user.getAddress() %></td>
				<td><a href="#">删除</a></td>
				</tr>
 				<%} %> 
                 
          </tbody>
        </table>
        <div class="listPage">
        <a href="javascript:void(0);" onclick="changePage(<%=request.getAttribute("merchantid") %>,<%=request.getAttribute("pageno") %>,<%=request.getAttribute("pageall") %>,0);">首页</a> 
        <a href="javascript:void(0);" class="gray" onclick="changePage(<%=request.getAttribute("merchantid") %>,<%=request.getAttribute("pageno") %>,<%=request.getAttribute("pageall") %>,1);">上一页</a> 
        <a href="javascript:void(0);" class="gray" onclick="changePage(<%=request.getAttribute("merchantid") %>,<%=request.getAttribute("pageno") %>,<%=request.getAttribute("pageall") %>,2);">下一页</a> 
        <a href="javascript:void(0);" class="gray" onclick="changePage(<%=request.getAttribute("merchantid") %>,<%=request.getAttribute("pageno") %>,<%=request.getAttribute("pageall") %>,3);">尾页</a>
        &nbsp;&nbsp;共<%=request.getAttribute("pagesum") %>条，当前第<span><%=request.getAttribute("pageno") %></span>/<%=request.getAttribute("pageall") %>页</div>
      </div>
    </div>
<div id="fullbg"></div>
<div id="dialog"> 
     <img class="delete" src="images/iconDelete.png">
<div class="dialogcon">
			<div id="name">名称:&nbsp;&nbsp;<input type="text" name="fname" id="name1" placeholder="请输入名称"/></div>
			<div id="type1">类型:&nbsp;&nbsp;
			<select id="selid">
				<option value="0">请选择</option>
				<option value="3">管理员</option>
				<option value="2">商户账号</option>
				<option value="1">消费点</option>
				<option value="SH">商户</option>
			</select>
			
			<select id="subselid">
				<option>请选择</option>
			</select>
			</div>
			
			<div id="fnum">账号:&nbsp;&nbsp;<input type="text" name="fnum" id="fnum1" value="" placeholder="请输入账号"/><br/>	
				密码:&nbsp;&nbsp;<input type="password" name="psw" id="password" value="" placeholder="请输入密码"/></div>
			<div id="bank">姓名:&nbsp;&nbsp;<input type="text" name="bank1" id="contact" value="" placeholder="请输入联系人姓名"/><br/>
						   电话:&nbsp;&nbsp;<input type="text" name="bank2" id="phone" value="" placeholder="请输入联系人电话"/><br/>
						   地址:&nbsp;&nbsp;<input type="text" name="bank3" id="address" value="" placeholder="请输入地址"/></div>
	</div>
	<div><input type="button" value="添加" id="addbutton"/></div>
	
</div> 
</body>
<script>

 $("#addbutton").click(function(){
 
       var addtype= $.trim($("#selid").val());
       var username=$.trim($("#name1").val());
       var loginname=$.trim($("#fnum1").val());
       var password=$.trim($("#password").val());
       var merchantid=$.trim($("#subselid").val());
       var contact=$.trim($("#contact").val());
       var phone=$.trim($("#phone").val());
       var address=$.trim($("#address").val());
       if(addtype==3){
       		addtype=$("#subselid").val();
       	}
       	//验证通过后提交执行添加操作
       if(checkNull()){
       	$.ajax({ 
         type: "post", 
                cache: false, 
                dataType: 'json',
                url: "AddUser.ext",
                data:{
					addtype:addtype,
					username:username,
					loginname:loginname,
					password:password,
					merchantid:merchantid,
					contact:contact,
					phone:phone,
					address:address
                 },
				success: function(msg) {
                   if (msg.flag) {
 						alert("添加成功");
 						$(".delete").click();
                   } else {
                   		alert(msg.desc);
                   }
                }       
           });
       }
       
      //根据添加角色进行验证
       function checkNull(){
      
      	 $(".err").hide();
       	if(addtype==1||addtype==2){
       		return checkone($("#name1"))&&checkone($("#fnum1"))&&checkone($("#password"))&&checkone($("#subselid"))&&checkone($("#contact"))&&checkone($("#phone"))&&
       		checkone($("#address"));
       	}else if(addtype==3){
       		return checkone($("#name1"))&&checkone($("#fnum1"))&&checkone($("#password"))&&checkone($("#subselid"));
       	}else if(addtype=='SH'){
       		return checkone($("#name1"))&&checkone($("#fnum1"))&&checkone($("#password"))&&checkone($("#contact"))&&checkone($("#phone"))&&
       		checkone($("#address"));
       	}else{
       		return false;
       	}
       	
       }
       //验证value是否为空
       function checkone(one){
       	if($.trim(one.val())==""){
       		one.parent().append('<span class="err" style="color:red;font-size:10px;">该项不能为空</span>');
       		return false;
       	}
       	return true;
       }
});

//dialog内容
/* function changeCity(){
		var collCities = [['请选择'],
		                  ['一般管理员','运营管理员'],
						  [''],
						  ['已选择商户']];						  
		var oSelNode = document.getElementById("selid");
		var oSubselNode = document.getElementById("subselid");
		var index = oSelNode.selectedIndex;
		var arrCity = collCities[index];				
		oSubselNode.length = 0;
			for(var x=0; x<arrCity.length; x++){
							var oOptNode = document.createElement("option");
							oOptNode.innerHTML = arrCity[x];							
							oSubselNode.appendChild(oOptNode);
						}
								
		var s=$("#selid").val();
		if(s==0){
			$("#subselid").show();
			$("#bank").hide();
			$("#dialog").css("height","400px")
		}	
		if(s==1){
			$("#subselid").show();
			$("#linkman").hide();
			$("#dialog").css("height","400px")
		}		
		if(s==2){
			$("#subselid").hide();
			$("#bank").show();
			$("#dialog").css("height","520px")
		}
		if(s==3){
			$("#subselid").show();
			$("#bank").hide();
			$("#dialog").css("height","400px")
		}


} */
//显示灰色 jQuery 遮罩层 
$(".addBtn").click(function(){
		$("#selid").change();
	    var bh = $(window).height(); 
		var bw = $(window).width(); 
		$("#fullbg").css({ 
		height:bh, 
		width:bw, 
		display:"block",
		});
		$("#dialog").css("height","500px"); 
		$("#dialog").show();
					 
		$("#bank").hide();
		//changeCity();
});
//关闭灰色罩层
$(".delete").click(function(){
	$("#fullbg,#dialog").hide();
});

function changePage(merid,pageno,pagesize,type){
	if(type=="0"){
		if(pageno==1){
			alert("已经是首页了");
		}else{
		window.location.href='UserList.ext?merchantid='+merid+'&pageno=1';
		}
		
	}else if(type=="1"){
		var nowpage=Number(pageno)-1;
		if(nowpage>=1){
			window.location.href='UserList.ext?merchantid='+merid+'&pageno='+nowpage;
		}else{
			alert("已经是第一页了");
		}
		
	}else if(type=="2"){
		var nowpage=Number(pageno)+1;
		if(nowpage<=pagesize){
			window.location.href='UserList.ext?merchantid='+merid+'&pageno='+nowpage;
		}else{
			alert("已经是最后一页了");
		}
		
	}else if(type=="3"){
		if(pageno==pagesize){
			alert("已经是尾页了");
		}else{
			window.location.href='UserList.ext?merchantid='+merid+'&pageno='+pagesize;
		}
		
	}
}	

$(function(){
	
	$("#subselid").hide();
	$("#selid").change(function(){
		var selid=$("#selid").val();
		$("#bank").hide();
		$("#subselid").hide();
		if(selid==1||selid==2){//
			$("#fnum").show();
			$("#subselid").show();
			$("#bank").show();

			$.ajax({ 
         		type: "post", 
                cache: false, 
                dataType: 'json',
                url: "LeftSys.ext",
                data:{
					},
				success: function(msg) {
					jQuery("#subselid").empty();
                   if (msg.merchants.length>0) {
                   	for(var i=0;i<msg.merchants.length;i++){
                   		 jQuery("#subselid").append("<option value='"+(msg.merchants[i].merchantid)+"'>"+msg.merchants[i].mername+"</option>");
                   	} 
                   }
                }       
           }); 
		}else if(selid==3){
			$("#fnum").show();
			$("#subselid").show();
			jQuery("#subselid").empty();
			jQuery("#subselid").append("<option value='3'>一般管理员</option>");
			jQuery("#subselid").append("<option value='4'>运营管理员</option>");
		}else if(selid=='SH'){
			$("#fnum").hide();
			$("#subselid").hide();
			$("#bank").show();
			jQuery("#subselid").empty();
		}
		
	});
});
</script>
</html>