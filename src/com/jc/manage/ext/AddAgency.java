package com.jc.manage.ext;

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

import com.jc.dao.AgencyMapper;
import com.jc.entity.Agency;
import com.jc.entity.AgencyExample;
import com.jc.util.MyBatisUtil;


/**
 * 
 * @author qianjia
 * 2015.6.5
 */
@WebServlet("/manage/AddAgency.ext")
public class AddAgency extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject result = new JSONObject();
		String name=req.getParameter("name");
		String code=req.getParameter("code");
		boolean flag=false;
		String desc="添加异常";
		if (name!=null&&code!=null) {
			SqlSession session = MyBatisUtil.getSession();
			AgencyMapper mapper=session.getMapper(AgencyMapper.class);
			AgencyExample example=new AgencyExample();
			example.or().andAgencynoEqualTo(code);
			List<Agency> agencys=mapper.selectByExample(example);
			if(agencys!=null&&agencys.size()>0){ 
				flag=false;
				desc="该机构码"+code+"已存在";
			}else{
				Agency agency=new Agency();
				agency.setAgency(name);
				agency.setAgencyno(code);
				mapper.insert(agency);
				flag=true;
				desc="添加"+name+"机构码成功";
			}
			session.close();
			
		}else{
			desc="请输入正确的机构码名称和编码";
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
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}


}