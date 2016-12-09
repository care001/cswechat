<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="cn.com.goldfinance.domain.wkxf.AccTransdetail"%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0,  user-scalable=no;" name="viewport" />
    <meta name="format-detection" content="telphone=yes, email=yes" />
    <title>金诚卡消费明细</title>
    <link rel="stylesheet" type="text/css" href="jsp/css/app.css"/>
	<link rel="stylesheet" type="text/css" href="jsp/css/weui.css"/>
	<style type="text/css">
    	body { background:#f5f5f5;}
    </style>    
</head>
<body ontouchstart="">
				<div class="select-box">
				     <div class="weui_cell y-cellbox">
				        <div class="weui_cell_hd">
				        <label for="" class="weui_label"></label></div>
				        <div class="weui_cell_bd weui_cell_primary">
				          <input class="weui_input" name="year"  id="year" type="month" value=<%=request.getAttribute("year") %>>
				        </div>
				      </div>
				     <a href="javascript:sub();" class="weui_btn weui_btn_mini weui_btn_primary y-btn-select">查询</a>
				   <!--  <input type="submit" class="weui_btn weui_btn_mini weui_btn_primary y-btn-select" />查询111 -->
				</div>
		<input type="hidden" id="empNo" name="empNo" value=<%=request.getAttribute("empNo") %>>
            
            <table border="0" cellspacing="0" cellpadding="0" class="table-count" id="list">
                <tr class="tab">
                    <th>时间</th>
                    <th>类型</th>
                    <th>金额</th>
                    <th>详情</th>
                </tr>
                
                 <%
                 List<AccTransdetail> list=(List<AccTransdetail>)request.getAttribute("list");
                 for(int i=0;i<list.size();i++){
                	 AccTransdetail p=(AccTransdetail)list.get(i);
 				%>
				<tr>
				<td><%=p.getAcctransday() %></td>
				<td><%=p.getType() %></td>
				<td><%=p.getAmount() %></td>
				<td><%=p.getDetail() %></td>
				</tr>
 				<%} %>
              </table>
       
     </body>
	
	<script src="js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript">
	
	function post(URL, PARAMS) {
	var temp = document.createElement("form");
	temp.action = URL;
	temp.method = "post";
	temp.style.display = "none";
	for (var x in PARAMS) {
		var opt = document.createElement("textarea");
		opt.name = x;
		opt.value = PARAMS[x];
		temp.appendChild(opt);
	}
document.body.appendChild(temp);
	temp.submit();
}

function sub(){
var empno=$("#empNo").val();
var year=$("#year").val();
	post('PersonalQuery.ext', {"empNo":empno,"year":year}); 
} 


	
	</script>
</html>