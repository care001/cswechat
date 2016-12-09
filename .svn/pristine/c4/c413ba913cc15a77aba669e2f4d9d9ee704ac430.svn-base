package com.jc.manage.ext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
import com.jc.entity.MerchantExample;
import com.jc.entity.User;
import com.jc.entity.UserExample;
import com.jc.util.Coder;
import com.jc.util.MyBatisUtil;

/**
 * 
 * @author qianjia
 * 2015.6.5
 */
@WebServlet("/manage/LeftManage.ext")
public class LeftManage extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		JSONObject result = new JSONObject();
		List<Merchant> merchants=new ArrayList<Merchant>();
		List<User> users=new ArrayList<User>();
		String merchantstr=req.getParameter("merchantid");//商户id
		SqlSession session = MyBatisUtil.getSession();
		if(merchantstr!=null&&Coder.isNumeric(merchantstr)){
			int merchantid=Integer.parseInt(merchantstr);
			UserMapper mapper=session.getMapper(UserMapper.class);
			UserExample example=new UserExample();
			example.or().andMerchantidEqualTo(merchantid).andStatusEqualTo("1");
			example.or().andMerchantidEqualTo(merchantid).andStatusEqualTo("99");
			users=mapper.selectByExample(example);
		}else if(merchantstr.equals("allmer")){
			MerchantMapper mapper = session.getMapper(MerchantMapper.class);
			merchants=mapper.selectByExample(new MerchantExample());
		}
		
		
		session.close();
		try {
			result.put("merchants", merchants);
			result.put("users", users);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		PrintWriter out = resp.getWriter();
		out.print(result.toString());
		out.close();
	}

}