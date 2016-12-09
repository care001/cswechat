package com.jc.sys.ext;

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
import org.apache.log4j.Logger;
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
@WebServlet("/sys/UpdateUser.ext")
public class UpdateUser extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UpdateUser.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		String useridstr = req.getParameter("userid");
		HttpSession httpSession=req.getSession();
		User admin=(User)httpSession.getAttribute("admin");//查看登陆账户
		boolean flag=false;
		String desc="重置密码失败";
		if(useridstr!=null&&Coder.isNumeric(useridstr)){
			int userid=Integer.parseInt(useridstr);
			SqlSession session = MyBatisUtil.getSession();
			UserMapper mapper = session.getMapper(UserMapper.class);
			User user=mapper.selectByPrimaryKey(userid);
			String password=Coder.getResetPwd(6);
			try {
				user.setPassword(Coder.EncoderByMd5(password));
				user.setRemark(Coder.formatTime(new Date())+":"+admin.getUsername()+"进行了密码重置");
				mapper.updateByPrimaryKey(user);
				desc="重置后密码为："+password;
				logger.info(admin.getUsername()+"重置了userid为"+useridstr+"的账户密码，重置后为："+password);
				flag=true;
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				session.close();
			}
		}else{
			flag=false;
			desc="该账户不存在";
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
