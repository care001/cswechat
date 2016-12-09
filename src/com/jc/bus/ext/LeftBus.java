package com.jc.bus.ext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

import com.jc.dao.MerchantMapper;
import com.jc.dao.UserMapper;
import com.jc.entity.Merchant;
import com.jc.entity.User;
import com.jc.entity.UserExample;
import com.jc.util.MyBatisUtil;

/**
 * 
 * @author qianjia
 * 2015.6.5
 */
@WebServlet("/bus/LeftBus.ext")
public class LeftBus extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String merchantstr=req.getParameter("merchantid");
		JSONObject result = new JSONObject();
		Merchant merchant=new Merchant();
		List<User> users=new ArrayList<User>();
		HttpSession httpSession=req.getSession();
		User user=(User)httpSession.getAttribute("admin");
		SqlSession session = MyBatisUtil.getSession();
		if(user!=null&&user.getStatus().equals("2")){
			if(merchantstr!=null&&merchantstr.equals("info")){
				MerchantMapper mapper = session.getMapper(MerchantMapper.class);
				merchant=mapper.selectByPrimaryKey(user.getMerchantid());
			}else{
				int merchantid=user.getMerchantid();
				UserMapper mapper=session.getMapper(UserMapper.class);
				UserExample example=new UserExample();
				example.or().andMerchantidEqualTo(merchantid).andStatusEqualTo("1");
				example.or().andMerchantidEqualTo(merchantid).andStatusEqualTo("99");
				users=mapper.selectByExample(example);
			}
			
		}
		session.close();
		try {
			result.put("merchant", new JSONObject(merchant));
			result.put("users", users);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		PrintWriter out = resp.getWriter();
		out.print(result.toString());
		out.close();
	}

}