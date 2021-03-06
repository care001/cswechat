package com.jc.ext;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.easywechat.util.SendMessage;

import com.jc.entity.User;
import com.jc.util.CookieUtil;
import com.jc.util.GetOpenId;
/**
 * 
 * @author qianjia
 * 2016.6.5
 */
@WebServlet("/ConsumeSuc.ext")
public class ConsumeSuc extends HttpServlet{  
  
    private static final long serialVersionUID = 1L;  
    private static Logger logger = Logger.getLogger(ConsumeSuc.class);
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
          request.setCharacterEncoding("UTF-8");  
          response.setCharacterEncoding("UTF-8");
          String openid =null;
          String code = request.getParameter("code");
          String state = request.getParameter("state");
          User user=CookieUtil.getUserByCookie(request);
          String business="";
          String paytime="";
          String clientid="";
          String money="";
          if(state!=null&&state.split(";")[3]!=null){
        	  business=state.split(";")[0];
        	  paytime=state.split(";")[1];
        	  clientid=state.split(";")[2];
        	  money=state.split(";")[3];
          }
          HttpSession httpsession = request.getSession();
          String sendverify=(String)httpsession.getAttribute("sendverify");
          String verifytime=sendverify.split(",")[0];
          String gold=sendverify.split(",")[1];
          String cash=sendverify.split(",")[2];
          if (!"authdeny".equals(code)&&user!=null&&user.getUserid()>0&&paytime.equals(verifytime)) {
        	  httpsession.setAttribute("sendverify", "");
        	  openid=GetOpenId.getOpenIdByCode(code);
        	  business=user.getUsername();
        	  String content=business+"于 "+paytime+" 成功收款"+clientid+"员工:"+Double.valueOf(money)+"元(金诚币:"+gold+"元,现金:"+cash+"元)。" ;
        	  SendMessage.sendTextMessageToUser(content, openid);
        	}else{     
              logger.info("就是不发提示消息，至于为什么，自己找原因。。。");  
          }  
          // 跳转到index.jsp
          /*request.setAttribute("money", money);
          request.getRequestDispatcher("jsp/receiveMoney.jsp").forward(request, response);*/
          //重定向
          //response.sendRedirect("jsp/receiveMoney.jsp?money="+money);
        }
    	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {    
    		doGet(request, response); 
       
    	}
}
