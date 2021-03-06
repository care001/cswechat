package com.jc.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;

import com.jc.dao.UserMapper;
import com.jc.entity.User;
import com.jc.entity.UserExample;

public class CookieUtil {
	//存储登录用户信息cook名
	public static final String LOGINCOOK="weixin_user";
	public static User getUserByCookie(HttpServletRequest req){
		SqlSession session = null;
		String name="";
		String pwd="";
		Cookie[] cookies = req.getCookies();
		try {
			if(cookies!=null){
				for(int i=0;i<cookies.length;i++){
					if(cookies[i].getName().equals(LOGINCOOK)){
						if(cookies[i].getValue()!=null&&!cookies[i].getValue().trim().equals("")){
							if(cookies[i].getValue().split("-")[0]!=null&&cookies[i].getValue().split("-")[1]!=null)
								name = (String)cookies[i].getValue().split("-")[0];
								pwd=cookies[i].getValue().split("-")[1];
						}
						
					}
				}
			}
			
		} catch (Exception e) {
			
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
		session.close();
		if(uiUser!=null&&uiUser.getPassword().equals(pwd)&&uiUser.getStatus().equals("1")){
			return uiUser;
		}else{
			return null;	
		}
		
	}
	
	public static Map<String, String> checkOauth(String openid,HttpServletRequest req){
		Cookie[] cookies = req.getCookies();
		Map<String, String> map=new HashMap<String, String>();
   	  	try {
			if(cookies!=null){
				for(int i=0;i<cookies.length;i++){
					if(cookies[i].getName().equals(openid+"name")&&cookies[i].getValue()!=null&&!cookies[i].getValue().trim().equals("")){
						map.put("name", cookies[i].getValue());
					}
					if(cookies[i].getName().equals(openid+"img")&&cookies[i].getValue()!=null&&!cookies[i].getValue().trim().equals("")){
						map.put("img", cookies[i].getValue());
					}
				}
			}
				
		} catch (Exception e) {
				
		}

   	  	return map;
 
	}

}
