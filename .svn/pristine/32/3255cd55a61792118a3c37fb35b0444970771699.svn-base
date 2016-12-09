package com.jc.ext;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.jc.util.CookieUtil;

/**
 * 
 * @author qianjia
 * 2016.6.5
 */
@WebServlet("/DelCookie.ext")
public class DelCookie extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;
	private static Logger logger = Logger.getLogger(DelCookie.class);
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Cookie[] cookies = req.getCookies();
		JSONObject result = new JSONObject();
		try {
			if(cookies!=null){
				for(int i=0;i<cookies.length;i++){
					if(cookies[i].getName().equals(CookieUtil.LOGINCOOK)){
						Cookie cookie = new Cookie(CookieUtil.LOGINCOOK, null);
						cookie.setMaxAge(0);
						cookie.setPath("/");//根据你创建cookie的路径进行填写
						resp.addCookie(cookie);			
					}
				}
			}
			
		} catch (Exception e) {
			logger.info("delete cookie:"+e);
		}
		
		PrintWriter out = resp.getWriter();
		out.print(result.toString());
		out.close();
	}

}