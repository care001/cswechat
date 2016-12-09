
wx.ready(function () {

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

  




  // 9.1.2 扫描二维码并返回结果
  document.querySelector('#scanQRCode1').onclick = function () {
    wx.scanQRCode({
      needResult: 1,
      success: function (res) {
        var client = res.resultStr;
       
         $.ajax({ 
         type: "post", 
                cache: false, 
                dataType: 'json',
                url: "/DoConsume.ext",
                data:{
					money:$("#number").val(),
					client:client
                 },
				success: function(msg) {
				
                   	if(msg.flag==true){
                   	
                   		post('/jsp/succ.jsp', {business:msg.business,paytime:msg.paytime ,clientid: msg.clientid ,money: msg.money}); 
                   	}else{
                   		alert(msg.desc);
                   	}
  		          }       
           });
        
      }
    });
  };

  

  var shareData = {
    title: '微信JS-SDK Demo',
    desc: '微信JS-SDK,帮助第三方为用户提供更优质的移动web服务',
    link: 'http://demo.open.weixin.qq.com/jssdk/',
    imgUrl: 'http://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRt8Qia4lv7k3M9J1SKqKCImxJCt7j9rHYicKDI45jRPBxdzdyREWnk0ia0N5TMnMfth7SdxtzMvVgXg/0'
  };
  wx.onMenuShareAppMessage(shareData);
  wx.onMenuShareTimeline(shareData);

  function decryptCode(code, callback) {
    $.getJSON('/jssdk/decrypt_code.php?code=' + encodeURI(code), function (res) {
      if (res.errcode == 0) {
        codes.push(res.code);
      }
    });
  }
});

wx.error(function (res) {
  alert(res.errMsg);
});

