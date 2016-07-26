// JavaScript Document

 $("#addbutton").click(function(){ 
           //添加内容           
			var $namevalue = $("#name1").val();			
			var a = $("#selid").val();
		if(a==0){
			var $typetd = $("<td></td>");
			$typetd.text("请选择");		
		}
		if(a==1){
			var b = $("#subselid").val();
			var $typetd = $("<td></td>");
			$typetd.text(b);	
		}
		if(a==2){
			var $typetd = $("<td></td>");
			$typetd.text("商家");	
		}
		if(a==3){
			var $typetd = $("<td></td>");
			$typetd.text("消费点");
		}		
			var $numvalue = $("#fnum1").val();
			var $pswvalue = $("#psw1").val();
			var $bank1value = $("#bank1").val();
			var $bank2value = $("#bank1").val();
			var $bank3value = $("#bank1").val();
			
			var $tr = $("<tr></tr>");
			
			var $nametd = $("<td></td>");
			$nametd.text($namevalue);
			
						
			var $numtd = $("<td></td>");
			$numtd.text($numvalue);
			
			var $pswtd = $("<td></td>");
			$pswtd.text($pswvalue);
			 
			var $bank1td = $("<td></td>");
			$bank1td.text($bank1value);
			
			var $bank2td = $("<td></td>");
			$bank2td.text($bank2value);
			
			var $bank3td = $("<td></td>");
			$bank3td.text($bank3value);
			
			var $atd = $("<td></td>");
			var $a = $("<a></a>");
			$a.attr("href","#");
			$a.text("删除");
			$atd.append($a);
			
			$tr.append($nametd);
			$tr.append($typetd);
			$tr.append($numtd);
			$tr.append($pswtd);
			$tr.append($bank1td);
			$tr.append($bank2td);
			$tr.append($bank3td);
			$tr.append($atd);
		
			var $tbody = $("tbody");
			$tbody.append($tr);
			
		
			
		//删除
		$a.click(function(){
	 	var $trElement = $a.parent().parent();
		var $na = $trElement.children().eq(0).text();
		var flag = confirm("你确认要删除"+$na+"这个用户吗？");
		if(flag){
	    	$trElement.remove();
			return false;
		}else{
			return false;
		}
	 });
	 $("#dialog").hide();
	 $("#fullbg").hide();
	 
	 //重置
	 var $button = $("<button></button>");
	 $pswtd.append($button);
	 $button.attr("id","reset")
	 $button.text("重置")
	 $button.click(function(){
	 	$pswtd.html('123456<button id="reset">重置</button>');
	 });
});

//dialog内容
function changeCity(){
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
			$("#bank").hide();
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


}
//显示灰色 jQuery 遮罩层 
$(".addBtn").click(function(){
	    var bh = $(window).height(); 
		var bw = $(window).width(); 
		$("#fullbg").css({ 
		height:bh, 
		width:bw, 
		display:"block" 
		}); 
		$("#dialog").show(); 
		$("#bank").hide();
});
//关闭灰色罩层
$(".delete").click(function(){
	$("#fullbg,#dialog").hide();
});
	
