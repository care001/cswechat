package com.jc.sys.ext;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.json.JSONException;
import org.json.JSONObject;

import com.jc.dao.UserMapper;
import com.jc.entity.User;
import com.jc.util.MyBatisUtil;
/**
 * 
 * @author qianjia
 * 2015.7.25
 */
@WebServlet("/sys/DelUser.ext")
public class DelUser extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String useridstr = req.getParameter("userid");
		int userid=Integer.parseInt(useridstr);
		boolean flag=false;
		String desc="删除异常";
		SqlSession session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		try {
			User user=mapper.selectByPrimaryKey(userid);
			if(user!=null){
				mapper.deleteByPrimaryKey(userid);
				desc="删除 "+user.getUsername()+"账户成功";
				flag=true;
			}else{
				desc="该账户不存在";
				flag=false;
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
