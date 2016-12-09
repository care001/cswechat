package com.jc.sys.ext;


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
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.jc.dao.UserMapper;
import com.jc.entity.User;
import com.jc.entity.UserExample;
import com.jc.util.Coder;
import com.jc.util.MyBatisUtil;

/**
 * 
 * @author qianjia
 * 2015.6.5
 */
@WebServlet("/sys/UserListByMerId.ext")
public class UserListByMerId extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;
	private static Logger logger = Logger.getLogger(UserListByMerId.class);
	private static final int PAGESIZE=10;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String merchantstr=req.getParameter("merchantid");
		String pagenostr = req.getParameter("pageno");
		List<User> users=new ArrayList<User>();
		JSONObject result = new JSONObject();
		int pageno=1;
		if(pagenostr!=null&&!pagenostr.equals("")){
			pageno=Integer.valueOf(pagenostr).intValue();
		}
		if(merchantstr!=null&&Coder.isNumeric(merchantstr)){
			int merchantid=Integer.valueOf(merchantstr);
			SqlSession session = MyBatisUtil.getSession();
			UserMapper mapper = session.getMapper(UserMapper.class);
			UserExample example=new UserExample();
			example.or().andMerchantidEqualTo(merchantid);
			int count=mapper.countByExample(example);
			example.clear();
			example.or().andMerchantidEqualTo(merchantid);
			if(merchantid==-1){
				example.setOrderByClause("status desc");
			}
			example.setLimitSize(PAGESIZE);
			example.setLimitStart((pageno-1)*PAGESIZE);
			users=mapper.selectByExample(example);
			session.close();
			
		    int pageall=1;
		    if(count%PAGESIZE==0){
		    	if(count/PAGESIZE==0){
		    		pageall= 1;
				}else{
					pageall= count/PAGESIZE;
				}
			}else{
				pageall= count/PAGESIZE+1;
			}
		    
		    try {
				result.put("users", users);
				result.put("usersize", users.size());
				result.put("pageall", pageall);
				result.put("pagenow", pageno);  
			} catch (Exception e) {
				logger.info(e);
			}
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
