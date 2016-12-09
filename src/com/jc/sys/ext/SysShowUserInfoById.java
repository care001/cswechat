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

import com.jc.dao.MerchantMapper;
import com.jc.dao.UserMapper;
import com.jc.entity.Merchant;
import com.jc.entity.User;
import com.jc.util.Coder;
import com.jc.util.MyBatisUtil;

@WebServlet("/sys/SysShowUserInfoById.ext")
public class SysShowUserInfoById extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject result = new JSONObject();
		User user=new User();
		Merchant merchant=new Merchant();
		String userid=req.getParameter("userid");
		String merchantid=req.getParameter("merchantid");
		SqlSession session = MyBatisUtil.getSession();
		UserMapper mapper=session.getMapper(UserMapper.class);
		MerchantMapper mermapper=session.getMapper(MerchantMapper.class);
		if(userid!=null&&Coder.isNumeric(userid)){
			user=mapper.selectByPrimaryKey(Integer.parseInt(userid));
		}else if(merchantid!=null&&Coder.isNumeric(merchantid)){
			merchant=mermapper.selectByPrimaryKey(Integer.parseInt(merchantid));
		}
		
		session.close();
		
		try {
			result.put("user", new JSONObject(user));
			result.put("merchant", new JSONObject(merchant));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		PrintWriter out = resp.getWriter();
		out.print(result.toString());
		out.close();
	}


}