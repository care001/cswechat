package com.jc.bus.ext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

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
import com.jc.util.Coder;
import com.jc.util.MyBatisUtil;
/**
 * 
 * @author qianjia
 * 2015.7.25
 */
@WebServlet("/bus/UpdateUserByBus.ext")
public class UpdateUserByBus extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String useridstr = req.getParameter("userid");
		HttpSession httpSession=req.getSession();
		User admin=(User)httpSession.getAttribute("admin");//查看登陆账户
		int userid=Integer.parseInt(useridstr);
		SqlSession session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		User user=mapper.selectByPrimaryKey(userid);
		String password=Coder.getResetPwd(6);
		boolean flag=false;
		String desc="初始错误";
		try {
			if(admin!=null&&user.getStatus().equals("1")&&user.getMerchantid().equals(admin.getMerchantid())){
				user.setPassword(Coder.EncoderByMd5(password));
				user.setRemark(Coder.formatTime(new Date())+":"+admin.getUsername()+"进行了密码重置");
				mapper.updateByPrimaryKey(user);
				flag=true;
				desc="重置后密码为："+password;
			}else{
				flag=false;
				desc="重置失败，你没有该对象的重置权限";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.close();
		}
		JSONObject result = new JSONObject();
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
