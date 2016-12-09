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

import com.jc.dao.AgencyMapper;
import com.jc.entity.Agency;
import com.jc.entity.AgencyExample;
import com.jc.util.Coder;
import com.jc.util.MyBatisUtil;

/**
 * 
 * @author qianjia
 * 2015.6.5
 */
@WebServlet("/manage/ShowAgency.ext")
public class ShowAgency extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;
	private static final int PAGESIZE=10;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject result = new JSONObject();
		String pagenumstr = req.getParameter("pagenumstr");
		int pagenow=1;
		List<Agency> agencies=new ArrayList<Agency>();
		SqlSession session = MyBatisUtil.getSession();
		AgencyMapper mapper=session.getMapper(AgencyMapper.class);
		AgencyExample example=new AgencyExample();
		int agentsum=mapper.countByExample(example);
		if(pagenumstr!=null&&Coder.isNumeric(pagenumstr)){
			pagenow=Integer.parseInt(pagenumstr);
			example.setLimitStart((pagenow-1)*PAGESIZE);
		}else{
			example.setLimitStart(0);
		}
		example.setLimitSize(PAGESIZE);
		agencies=mapper.selectByExample(example);
		
		session.close();
		int pageall=1;
		if(agentsum%PAGESIZE==0){
			if(agentsum/PAGESIZE==0){
				pageall= 1;
			}else{
				pageall= agentsum/PAGESIZE;
			}
		}else{
			pageall=(agentsum/PAGESIZE)+1;
		}
		try {
			result.put("agencies", agencies);
			result.put("agentsum", agentsum);
			result.put("pageall", pageall);
			result.put("pagenow", pagenow); 
		} catch (JSONException e) {
			e.printStackTrace();
		}
		PrintWriter out = resp.getWriter();
		out.print(result.toString());
		out.close();
	}

	

}