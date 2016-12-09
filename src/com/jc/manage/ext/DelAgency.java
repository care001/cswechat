package com.jc.manage.ext;

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

import com.jc.dao.AgencyMapper;
import com.jc.entity.Agency;
import com.jc.util.Coder;
import com.jc.util.MyBatisUtil;

/**
 * 
 * @author qianjia
 * 2015.6.5
 */
@WebServlet("/manage/DelAgency.ext")
public class DelAgency extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject result = new JSONObject();
		String agencyidstr=req.getParameter("agencyid");
		boolean flag=false;
		String desc="删除异常";
		if (agencyidstr!=null&&Coder.isNumeric(agencyidstr)) {
			int agencyid=Integer.parseInt(agencyidstr);
			SqlSession session = MyBatisUtil.getSession();
			AgencyMapper mapper=session.getMapper(AgencyMapper.class);
			Agency agency=mapper.selectByPrimaryKey(agencyid);
			mapper.deleteByPrimaryKey(agencyid);
			session.close();
			flag=true;
			desc="删除"+agency.getAgency()+"成功";
		}else{
			desc="该机构码不存在";
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