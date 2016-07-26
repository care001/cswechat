package com.jc.ext;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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

import cn.com.goldfinance.mapper.wkxf.HrmsEmpMapper;

import com.jc.util.Coder;
import com.jc.util.JDBCUtil;
import com.jc.util.MyBatisUtil;
import com.jc.util.NoCardUtil;
import com.jc.util.RsaUtil;

@WebServlet("/QueryBalance.ext")
public class QueryBalance extends HttpServlet{
	/**
	 * 
	 * @author qianjia
	 * 2016.6.6
	 * 无卡消费查询
	 *         
	 *
	 */
	private static final long serialVersionUID = 1L;
	JDBCUtil jdbcUtil=JDBCUtil.getInstance();
	private static Logger logger=Logger.getLogger(QueryBalance.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//String client=IOUtils.toString(req.getInputStream());
		String client=req.getReader().readLine();
		JSONObject result = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		byte[] decodedData;
		String clientid="";
		boolean flag=false;
		double IDBalance=0;
		double ICBalance=0;
		double goldcoin=0;
		double inactive=0;
		//解密
		try {
			byte[] encodedData=Coder.parseHexStr2Byte(client);
			decodedData = RsaUtil.decryptByPrivateKey(encodedData, RsaUtil.privateKey);
			client = new String(decodedData);
			jsonObject = new JSONObject(client);
			clientid=jsonObject.getString("clientid");
		} catch (Exception e) {
			logger.info("解密失败");	
		}
		
		try {
			SqlSession session = MyBatisUtil.getwkxfSession();
			HrmsEmpMapper hrmsEmpMapper = session.getMapper(HrmsEmpMapper.class);
			int cardId=hrmsEmpMapper.getMaxCardIDByEmpNo(clientid);
			goldcoin=NoCardUtil.PosMoney(cardId, 7);
			double cash=NoCardUtil.PosMoney(cardId, 8);
			BigDecimal b1 = new BigDecimal(Double.toString(goldcoin));
	        BigDecimal b2 = new BigDecimal(Double.toString(cash));
			IDBalance=b1.add(b2).doubleValue();
			Map<String, Double> icmap=NoCardUtil.getICBalance(clientid);
			ICBalance=icmap.get("total");
			inactive=icmap.get("ntotal");
			flag=true;
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	
		try {
			result.put("status", flag);
			result.put("IDBalance", IDBalance);
			result.put("ICBalance", ICBalance);
			result.put("inactive", inactive);
			result.put("IDgold", goldcoin);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		byte[] data = result.toString().getBytes();
		String miwem="";
		try {
			miwem=RsaUtil.encryptByPrivateKeyseg(data, RsaUtil.privateKey);
		} catch (Exception e) {
			logger.info("加密失败");
			e.printStackTrace();
		}
		PrintWriter out = resp.getWriter();
		out.print(miwem);
		out.close();
	}
}

