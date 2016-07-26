package com.jc.sys.ext;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.jc.entity.Merchant;
import com.jc.entity.MerchantExample;
import com.jc.util.MyBatisUtil;

/**
 * 
 * @author qianjia
 * 2015.6.5
 */
@WebServlet("/sys/LeftSys.ext")
public class LeftSys extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject result = new JSONObject();
		
		SqlSession session = MyBatisUtil.getSession();
		MerchantMapper mapper = session.getMapper(MerchantMapper.class);
		List<Merchant> merchants=mapper.selectByExample(new MerchantExample());
		session.close();
		try {
			result.put("merchants", merchants);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		PrintWriter out = resp.getWriter();
		out.print(result.toString());
		out.close();
	}



}