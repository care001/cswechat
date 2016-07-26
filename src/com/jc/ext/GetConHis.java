package com.jc.ext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.jc.entity.Consume;
import com.jc.entity.User;
import com.jc.util.Coder;
import com.jc.util.PowerDataOperUtil;

/**
 * 
 * @author qianjia
 * 2015.6.5
 */
@WebServlet("/admin/GetConHis.ext")
public class GetConHis extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;
	private static Logger logger = Logger.getLogger(GetConHis.class);
	private static final int PAGESIZE=10;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String seardate = req.getParameter("seardate");
		String pagenumstr = req.getParameter("pagenumstr");
		int pagenum=1;
		if(pagenumstr==null||pagenumstr.equals("")){
			pagenumstr="1";
		}else{
			pagenum=Integer.parseInt(pagenumstr);
		}
		//seardate="2016-06";
		JSONObject result = new JSONObject();
		HttpSession httpSession=req.getSession();
		User user=(User)httpSession.getAttribute("admin");
		if(user==null||user.getUserid()<=0){
			try {
				result.put("desc", "您还没有登陆");
				result.put("consize", "err");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			logger.info("请登陆。。");
		}else{
			try {
				Map<String, String> serdates=Coder.FirstandLastday(seardate);
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("userid", user.getUserid());
				map.put("Merchantid", user.getMerchantid());
				map.put("seardate1", serdates.get("first"));
				map.put("seardate2", serdates.get("last"));
				map.put("pagestart", (pagenum-1)*PAGESIZE);
				map.put("pagesize", PAGESIZE);
				Map<String,Object> retmap=PowerDataOperUtil.getHisByPower(map, user.getStatus());
				logger.info("conhis size:"+retmap.get("consize"));
				result.put("consize", retmap.get("consize"));
				result.put("consumes", (List<Consume>)retmap.get("consumes"));
				result.put("pagesum", retmap.get("pagesum"));
				result.put("pageall", retmap.get("pageall"));
				result.put("pagenow", pagenumstr);
				result.put("Mername", user.getMerchantid()+"");
			} catch (Exception e) {
				logger.info(e);
			}
		}
		
		PrintWriter out = resp.getWriter();
		out.print(result.toString());
		out.close();
	}
	

}