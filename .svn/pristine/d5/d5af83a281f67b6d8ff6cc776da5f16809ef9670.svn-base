package com.jc.ext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.jc.dao.ConsumeMapper;
import com.jc.dao.UserMapper;
import com.jc.entity.Consume;
import com.jc.entity.User;
import com.jc.entity.UserExample;
import com.jc.util.BaseUtil;
import com.jc.util.Coder;
import com.jc.util.ConfigByFile;
import com.jc.util.MyBatisUtil;
import com.jc.util.NoCardUtil;
import com.jc.util.RsaUtil;
@WebServlet("/webConsume.ext")
public class WebConsume extends HttpServlet{
	/**
	 * 
	 * @author qianjia
	 * 2016.6.6
	 * 无卡消费扣费
	 *         
	 *
	 */
	private static final long serialVersionUID = 1L; 
	private static Logger logger = Logger.getLogger(WebConsume.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String client = IOUtils.toString(req.getInputStream());
		byte[] decodedData=null;
		JSONObject jsonObject = new JSONObject();
		double amount = 0;
		String clientid="";
		String loginname="test";
		String business = "";
		String busNo = "";
		String userid = "";
		String password = "";
		JSONObject result = new JSONObject();
		JSONObject result2 = new JSONObject();
		Map<String, Object> xfget = new HashMap<String, Object>();;
		String paytime=Coder.formatTime(new Date());
		boolean flag=false;
		String desc="";
		String back="";
		
		try {
			byte[] encodedData=Coder.parseHexStr2Byte(client);
			decodedData = RsaUtil.decryptByPrivateKey(encodedData, RsaUtil.privateKey);
			String jsonstr=new String(decodedData);
			jsonObject = new JSONObject(jsonstr);
		} catch (Exception e) {
			logger.info("解密失败。。");
		}
		
		
		try {
			amount = jsonObject.getDouble("amount");
			clientid = jsonObject.getString("clientid");
			loginname = jsonObject.getString("loginname");
			password = jsonObject.getString("password");
			password=Coder.EncoderByMd5(password);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	
		SqlSession session = MyBatisUtil.getSession();
		UserMapper mapper = session.getMapper(UserMapper.class);
		UserExample example=new UserExample();
		example.or().andLoginnameEqualTo(loginname);
		User user = null;
		example.setLimitStart(0);
		example.setLimitSize(10);
		List<User> uiUsers = mapper.selectByExample(example);
		if(uiUsers.size()>0){
			user=uiUsers.get(0);
		}
		session.close();
		if(user!=null&&user.getStatus().equals("99")&&user.getPassword().equals(password)){
			business = user.getUsername();
			busNo = String.valueOf(user.getMerchantid());
			userid = String.valueOf(user.getUserid());
			int conflag=-1;
    		try {
    			logger.info("------->"+loginname+" 通过接口触发一卡通扣费："+"消费工号："+clientid+"消费金额："+amount);
    			xfget=NoCardUtil.PosGetXF(clientid, amount, business, busNo,userid);
    			conflag=(Integer)xfget.get("conflag");
    			desc=(String)xfget.get("desc");
        		if(conflag==0){
        			flag=true;
        		}else{
        			flag=false;
        		}
        		
			} catch (Exception e) {
				e.printStackTrace();
			}
        		
        	
        }else{
        	flag=false;
        	desc="账户登录错误或该账户无法发起扣款";
        }

		try {
			result2.put("userId", clientid);
			result2.put("date", paytime);
			result2.put("location", business);
			result2.put("gold", Double.valueOf((String)(xfget.get("gold")==null?"0.00":xfget.get("gold"))));
			result2.put("cash", Double.valueOf((String)(xfget.get("cash")==null?"0.00":xfget.get("cash"))));
			result2.put("status", flag);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		//推送扣费结果	
		String miwem2="";
		try {
			miwem2=RsaUtil.encryptByPrivateKeyseg(result2.toString().getBytes(), RsaUtil.privateKey);
		} catch (Exception e) {
			logger.info("加密失败");
			e.printStackTrace();
		}
		back = BaseUtil.sendPost(ConfigByFile.sendPayUrl, miwem2);
	
		try {
			result.put("flag", flag);
			result.put("desc", desc);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// encrypt
		byte[] data = result.toString().getBytes();
		String miwem="";
		try {
			byte[] encodedData = RsaUtil.encryptByPrivateKey(data, RsaUtil.privateKey);
			miwem=RsaUtil.parseByte2HexStr(encodedData);
		} catch (Exception e) {
			logger.info("加密失败");
			e.printStackTrace();
		}
		PrintWriter out = resp.getWriter();
		out.print(miwem);
		out.close();
		
		session = MyBatisUtil.getSession();
		//保存消费历史
		try {
			logger.info("---------------->接口消费本地记录存储开始");
			ConsumeMapper mapper2 = session.getMapper(ConsumeMapper.class);
			if(Double.valueOf((String) xfget.get("gold"))!=0){
				if(flag){
					Consume consume=new Consume();
					consume.setClientid(clientid);
					consume.setMerchantid(user.getMerchantid());
					consume.setCsmerchat(business);
					consume.setMername(user.getMername());
					consume.setCssum(Double.valueOf((String) xfget.get("gold")));
					consume.setMaketime(paytime);
					consume.setPushflag(back);
					consume.setRemark(desc);
					consume.setUserid(user.getUserid());
					consume.setWallettype("7");//金诚币消费
					consume.setClientname((String)xfget.get("clientname"));
					consume.setCsstatus("1");
					mapper2.insert(consume);
				}
			}
			if(Double.valueOf((String)xfget.get("cash"))!=0){
				if(flag){
					Consume consume=new Consume();
					consume.setClientid(clientid);
					consume.setMerchantid(user.getMerchantid());
					consume.setCsmerchat(business);
					consume.setMername(user.getMername());
					consume.setCssum(Double.valueOf((String) xfget.get("cash")));
					consume.setMaketime(paytime);
					consume.setPushflag(back);
					consume.setRemark(desc);
					consume.setUserid(user.getUserid());
					consume.setWallettype("8");//现金消费
					consume.setClientname((String)xfget.get("clientname"));
					consume.setCsstatus("1");
					mapper2.insert(consume);
				}
			}
			logger.info("------->"+loginname+" 存储扣费"+clientid+"工号："+amount+"元记录成功,提示信息："+desc);
			
		} catch (Exception e) {
			logger.info("------->"+loginname+" 存储扣费"+clientid+"工号："+amount+"元记录失败：,提示信息："+desc+e);
		}finally{
			session.close();
		}
	}
	
	
}
