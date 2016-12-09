package com.jc.bus.ext;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.json.JSONException;
import org.json.JSONObject;

import com.jc.dao.UserMapper;
import com.jc.entity.User;
import com.jc.entity.UserExample;
import com.jc.util.Coder;
import com.jc.util.MyBatisUtil;

/**
 * 
 * @author qianjia
 * 2015.6.5
 */
@WebServlet("/bus/AddUserByBus.ext")
public class AddUserByBus extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject result = new JSONObject();
		String desc="";
		boolean flag=false;
		String username = req.getParameter("username");
		String loginname = req.getParameter("loginname");
		String password = req.getParameter("password");
		String contact = req.getParameter("contact");
		String phone = req.getParameter("phone");
		String address = req.getParameter("address");
		SqlSession session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		UserExample example=new UserExample();
		example.or().andLoginnameEqualTo(loginname);
		List<User> uiUsers = mapper.selectByExample(example);//查询是否创建的账户已存在
		HttpSession httpSession=req.getSession();
		User admin=(User)httpSession.getAttribute("admin");//查看登陆账户
		try {
			if(admin==null||admin.getUserid()<=0||!admin.getStatus().equals("2")){
				flag=false;
				desc="未登陆或者登陆的不是商户账号";
			}else if(uiUsers!=null&&uiUsers.size()>0){
				flag=false;
				desc="该登陆账号名已被注册";
			}else{
				User user=new User();
				user.setAddress(address);
				user.setContact(contact);
				user.setLoginname(loginname);
				user.setMaketime(Coder.formatTime(new Date()));
				user.setMerchantid(admin.getMerchantid());
				user.setMername(admin.getMername());
				user.setPassword(Coder.EncoderByMd5(password));
				user.setPhone(phone);
				user.setStatus("1");
				user.setRemark(admin.getUsername()+"创建了该消费点");
				user.setUsername(username);
				mapper.insert(user);
				flag=true;
				desc="添加成功";
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		try {
			result.put("flag", flag);
			result.put("desc", desc);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		PrintWriter out = resp.getWriter();
		out.print(result.toString());
		out.close();
	}

}