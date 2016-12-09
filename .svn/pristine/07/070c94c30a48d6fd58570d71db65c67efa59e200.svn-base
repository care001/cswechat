package com.jc.ext;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.easywechat.util.GOauth2Core;

import com.jc.listeners.InitializeListerner;
import com.jc.util.CookieUtil;
import com.jc.util.GetOpenId;
/**
 * 
 * @author qianjia
 * 2016.6.5
 */
@WebServlet("/OAuth2Servlet.ext")
public class OAuth2Servlet extends HttpServlet{  
  
    private static final long serialVersionUID = 1L;  
    private final static int cookieMaxAge = 60 * 60 * 24 * 30;
    private static Logger logger = Logger.getLogger(OAuth2Servlet.class);
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
          request.setCharacterEncoding("UTF-8");  
          response.setCharacterEncoding("UTF-8");
          String openid =null;
          String access_token =null;
           
          String code = request.getParameter("code");
          String tableNo=request.getParameter("state");
          if (!"authdeny".equals(code)) {
        	  /*InitializeListerner.oauthAccessToken.updateAccessToken(code);
        	  openid = InitializeListerner.oauthAccessToken.getOpenid();*/
        	  openid=GetOpenId.getOpenIdByCode(code);
        	  Map<String, String> map=CookieUtil.checkOauth(openid, request);
        	  if(map!=null&&map.get("name")!=null&&!map.get("name").equals("")&&map.get("img")!=null&&!map.get("name").equals("")){
        		  request.setAttribute("UserID",map.get("name") );
        		  request.setAttribute("UserImg", map.get("img"));
        		  request.setAttribute("tableNo", tableNo);
        	  }else{
        		  access_token = InitializeListerner.oauthAccessToken.getAccessToken();	  
              	  //Map<String,String> oauthmap = GOauth2Core.GetUserID(access_token, InitializeListerner.oauthAccessToken.getOpenid());
              	  Map<String,String> oauthmap = GOauth2Core.GetUserID(access_token, openid );
        		  request.setAttribute("UserID", oauthmap.get("username"));
        		  request.setAttribute("UserImg", oauthmap.get("userimg")); 
        		  Cookie cookie = new Cookie(openid+"name", oauthmap.get("username"));
        		  Cookie cookie2 = new Cookie(openid+"img", oauthmap.get("userimg"));
        		  request.setAttribute("tableNo", tableNo);
        		  cookie.setMaxAge(cookieMaxAge);
        		  cookie2.setMaxAge(cookieMaxAge);
        		  cookie.setPath("/"); 
        		  cookie2.setPath("/");
        		  response.addCookie(cookie);
        		  response.addCookie(cookie2);	
        	  }
        	  
          }else{  
              logger.info("授权获取失败，至于为什么，自己找原因。。。");  
          }  
          // 跳转到index.jsp  
          request.getRequestDispatcher("index.jsp").forward(request, response);  
        }     
  
}