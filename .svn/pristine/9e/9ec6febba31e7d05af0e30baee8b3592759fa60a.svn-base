package com.jc.ext;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.json.JSONException;
import org.json.JSONObject;

import com.jc.dao.MerchantMapper;
import com.jc.dao.UserMapper;
import com.jc.entity.Merchant;
import com.jc.entity.User;
import com.jc.util.Coder;
import com.jc.util.MyBatisUtil;

@WebServlet("/admin/ShowInfoByMyself.ext")
public class ShowInfoByMyself extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject result = new JSONObject();
		String userid=req.getParameter("userid");
		HttpSession httpSession=req.getSession();
		User admin=(User)httpSession.getAttribute("admin");
		User user=new User();
		Merchant merchant=new Merchant();
		SqlSession session = MyBatisUtil.getSession();
		UserMapper mapper=session.getMapper(UserMapper.class);
		MerchantMapper mermapper=session.getMapper(MerchantMapper.class);
		if(admin!=null){
			if(admin.getStatus().equals("1")||admin.getStatus().equals("99")){
				user=mapper.selectByPrimaryKey(admin.getUserid());
			}else if(admin.getStatus().equals("2")){
				merchant=mermapper.selectByPrimaryKey(admin.getMerchantid());
				if(userid!=null&&Coder.isNumeric(userid)){
					user=mapper.selectByPrimaryKey(Integer.parseInt(userid));
				}
			}
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
