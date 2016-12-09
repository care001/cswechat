package com.jc.ext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.jc.dao.UserMapper;
import com.jc.entity.User;
import com.jc.entity.UserExample;
import com.jc.util.Coder;
import com.jc.util.ConfigByFile;
import com.jc.util.CookieUtil;
import com.jc.util.MyBatisUtil;

/**
 * login
 * @author qianjia
 * 2016.6.1
 */
@WebServlet("/Login.ext")
public class Login extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;
	private static Logger logger = Logger.getLogger(Login.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		String pwd = req.getParameter("pwd");
		JSONObject result = new JSONObject();
		SqlSession session = null;
		boolean hascook=false;
		if(name==null||pwd==null||name.equals("")||pwd.equals("")){
			name="";
			pwd="";
			Cookie[] cookies = req.getCookies();
			try {
				if(cookies!=null){
					for(int i=0;i<cookies.length;i++){
						if(cookies[i].getName().equals(CookieUtil.LOGINCOOK)){
							if(cookies[i].getValue()!=null&&!cookies[i].getValue().trim().equals("")){
								if(cookies[i].getValue().split("-")[0]!=null&&cookies[i].getValue().split("-")[1]!=null)
								name=cookies[i].getValue().split("-")[0];
								pwd=cookies[i].getValue().split("-")[1];
								hascook=true;
							}
							
						}
					}
				}
				
			} catch (Exception e) {
				logger.info("login cookie:"+e);
			}
		}
		
		
		session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		UserExample example=new UserExample();
		example.or().andLoginnameEqualTo(name);
		User uiUser = null;
		List<User> uiUsers = mapper.selectByExample(example);
		if(uiUsers.size()>0){
			uiUser=uiUsers.get(0);
		}
		try {
			if(!hascook){
				pwd=Coder.EncoderByMd5(pwd);
			}
			if(uiUser!=null&&uiUser.getPassword().equals(pwd)){
				if(uiUser.getStatus().equals("1")){
					result.put("loginstatus", "1");
					result.put("loginname", uiUser.getLoginname());
					result.put("maketime", uiUser.getMaketime());
					result.put("merchantid", uiUser.getMerchantid());
					result.put("remark", uiUser.getRemark());
					result.put("userid", uiUser.getUserid());
					result.put("username", uiUser.getUsername());
					if(!hascook){
						Cookie cookie = new Cookie(CookieUtil.LOGINCOOK, name+"-"+pwd);
						cookie.setMaxAge(ConfigByFile.cookieMaxAge);
						cookie.setPath("/");
						resp.addCookie(cookie);	
					}
				}else{
					result.put("loginstatus", "0");
					result.put("reason", "非消费点账户不可用");
				}
			}else{
				result.put("loginstatus", "0");
				result.put("reason", "用户名或密码错误");
				
			}
		} catch (Exception e) {
			logger.info("login err:"+e);
		}finally{
			session.close();	
		}
		
		PrintWriter out = resp.getWriter();
		out.print(result.toString());
		out.close();
	}

}