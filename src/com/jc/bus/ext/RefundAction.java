package com.jc.bus.ext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.jc.dao.ConsumeMapper;
import com.jc.dao.RefundMapper;
import com.jc.entity.Consume;
import com.jc.entity.Refund;
import com.jc.entity.User;
import com.jc.util.Coder;
import com.jc.util.MyBatisUtil;
import com.jc.util.NoCardUtil;

@WebServlet("/bus/Refund.ext")
public class RefundAction extends HttpServlet{
	/**
	 * 
	 * @author qianjia
	 * 2017.1.12
	 * 退款
	 *         
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger=Logger.getLogger(RefundAction.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String orderidstr=req.getParameter("orderid");
		String remark = req.getParameter("remark");
		int orderid = Integer.parseInt(orderidstr);
		JSONObject result = new JSONObject();
		SqlSession session = MyBatisUtil.getSession();
		ConsumeMapper consumeMapper = session.getMapper(ConsumeMapper.class);
		RefundMapper refundMapper = session.getMapper(RefundMapper.class);
		Map<String, Object> map=null;
		Consume consume = consumeMapper.selectByPrimaryKey(orderid);
		boolean flag = false;
		String desc = "";
		User user=(User)req.getSession().getAttribute("admin");
		if(user==null||!user.getStatus().equals("2")||!consume.getMerchantid().equals(user.getMerchantid())){
			desc = "未登陆或无效账户";
		}else if(consume.getPushflag().equals("100")){
			desc = "该记录已进行过退款";
		}else{
			//退款操作
			map = NoCardUtil.idRefund(consume.getClientid(), consume.getCssum()*-1, consume.getCsmerchat(), String.valueOf(consume.getUserid()), Integer.parseInt(consume.getWallettype()));
			desc = (String) map.get("desc");
			int intFlag = (Integer)map.get("conflag");
			if(intFlag==0){
				flag=true;
				desc = consume.getClientname()+": 退款"+consume.getCssum()+"元成功";
				logger.info("---->"+desc);
				consume.setPushflag("100");
				consumeMapper.updateByPrimaryKey(consume);
			}
			
		}
		try {
			result.put("flag", flag);
			result.put("desc", desc);
		} catch (JSONException e) {
			 
		}
		PrintWriter out = resp.getWriter();
		out.print(result.toString());
		out.close();
		//退款记录查询
		if(flag){
			Refund refund = new Refund();
			refund.setClientid(consume.getClientid());
			refund.setCssum(consume.getCssum());
			refund.setMaketime(Coder.formatTime(new Date()));
			refund.setMakeuser(user.getLoginname());
			refund.setOrderid(orderid);
			refund.setRemark(remark);
			refund.setUserid(consume.getUserid());
			refundMapper.insert(refund);
			logger.info("---->退款记录存储成功");
		}
		session.close();
		
		
	}
}

