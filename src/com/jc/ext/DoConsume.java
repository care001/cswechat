package com.jc.ext;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.jc.dao.ConsumeMapper;
import com.jc.entity.Consume;
import com.jc.entity.User;
import com.jc.util.BaseUtil;
import com.jc.util.Coder;
import com.jc.util.ConfigByFile;
import com.jc.util.CookieUtil;
import com.jc.util.MyBatisUtil;
import com.jc.util.NoCardUtil;
import com.jc.util.RsaUtil;

/**
 * 
 * @author 	qianjia
 * 2016.6.01
 */
@WebServlet("/DoConsume.ext")
public class DoConsume extends HttpServlet{

	private static final long serialVersionUID = 4689183699420922501L;
	private static Logger logger = Logger.getLogger(DoConsume.class);
    
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String money = req.getParameter("money");
		String client = req.getParameter("client");
		String remark = req.getParameter("remark");
		JSONObject result = new JSONObject();
		JSONObject result2 = new JSONObject();
		User user=CookieUtil.getUserByCookie(req);
		String paytime=Coder.formatTime(new Date());
		boolean flag=false;
		String desc="";
		String back="";
		String clientid="";
		byte[] decodedData;
		double amount=0.00;
		String business="";
		
		Map<String, Object> xfget = new HashMap<String, Object>();;
		try {
			amount=Double.valueOf(money);
		} catch (Exception e) {
			logger.info("金额转换错误");
		}
		//取cookie中的用户
		if(user==null||user.getUserid()<=0||!user.getStatus().equals("1")){
			try {
				result.put("flag", false);
				result.put("desc", "未登陆或无效账户");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			PrintWriter out = resp.getWriter();
			out.print(result.toString());
			out.close();
		}else{
			//请求扣费接口
			try {
				business=user.getUsername();
				String busNo = String.valueOf(user.getMerchantid());
				String userid = String.valueOf(user.getUserid());
				//解密
				try {
					byte[] encodedData=Coder.parseHexStr2Byte(client);
					decodedData = RsaUtil.decryptByPrivateKey(encodedData, RsaUtil.privateKey);
			        client = new String(decodedData);
				} catch (Exception e) {
					logger.info("解密错误");
				}
				logger.info("-------->解析二维码->金额 ："+money+";工号： "+client+";消费点 ："+business);
				if(client.contains(ConfigByFile.mid_String)&&!client.endsWith(ConfigByFile.mid_String)){
		        	String[] clients=client.split(ConfigByFile.mid_String);
		        	clientid=clients[0];
		        	if(Coder.checkTime(clients[1], ConfigByFile.payCheckTime)){
		        		int conflag=-1;
		        		try {
		        			logger.info("-------> 通过二维码触发一卡通扣费："+"消费工号："+clientid+"消费金额："+amount);
		        			xfget=NoCardUtil.PosGetXF(clientid, amount, business, busNo,userid);//busNo暂无用
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
		                desc="该二维码已失效";
		        	}
		        }else{
		        	flag=false;
		        	desc="无效二维码";
		        }

				try {
					result2.put("userId", clientid);
					result2.put("date", paytime);
					result2.put("location", business);
					result2.put("gold", Double.valueOf((String)(xfget.get("gold")==null?"0":xfget.get("gold"))));
					result2.put("cash", Double.valueOf((String)(xfget.get("cash")==null?"0":xfget.get("cash"))));
					result2.put("status", flag);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				//推送扣费结果
				String miwem="";
				try {
					miwem=RsaUtil.encryptByPrivateKeyseg(result2.toString().getBytes(), RsaUtil.privateKey);
				} catch (Exception e) {
					logger.info("加密失败");
					e.printStackTrace();
				}
				back = BaseUtil.sendPost(ConfigByFile.sendPayUrl, miwem);
				logger.info("消费推送请求返回："+back);
				
				result.put("flag", flag);
				result.put("desc", desc);
				result.put("money", money);
				result.put("clientid", clientid);
				result.put("business", user.getUserid());
				result.put("paytime", paytime);
		     } catch (Exception e1) {
				e1.printStackTrace();
			}
			String sendverify=(String) (paytime+","+(xfget.get("gold")==null?"0.0":xfget.get("gold"))+","+(xfget.get("cash")==null?"0.0":xfget.get("cash")));
			HttpSession httpsession = req.getSession();
			httpsession.setAttribute("sendverify", sendverify);
			PrintWriter out = resp.getWriter();
			out.print(result.toString());
			out.close();
			//保存消费历史
			SqlSession session = MyBatisUtil.getSession();
			try {
				logger.info("---------------->二维码扫描本地记录存储开始");
				ConsumeMapper mapper = session.getMapper(ConsumeMapper.class);
				logger.info("金诚币："+(String) xfget.get("gold")+";现金："+(String) xfget.get("cash"));
				if(Double.valueOf((String) xfget.get("gold"))!=0){
					logger.info("---------------->金诚币消费");
					if(flag){
						Consume consume=new Consume();
						consume.setClientid(clientid);
						consume.setMerchantid(user.getMerchantid());
						consume.setCsmerchat(business);
						consume.setMername(user.getMername());
						consume.setCssum(Double.valueOf((String) xfget.get("gold")));
						consume.setMaketime(paytime);
						consume.setPushflag(back);
						consume.setRemark(remark==null?"":remark);
						consume.setUserid(user.getUserid());
						consume.setWallettype("7");//金诚币消费
						consume.setClientname((String)xfget.get("clientname"));
						consume.setCsstatus("1");
						logger.info("---------------->成功");
						mapper.insert(consume);
					}else{
						logger.info("---------------->消费失败");
					}
				}
				if(Double.valueOf((String)xfget.get("cash"))!=0){
					logger.info("---------------->现金消费");
					if(flag){
						Consume consume=new Consume();
						consume.setClientid(clientid);
						consume.setMerchantid(user.getMerchantid());
						consume.setCsmerchat(business);
						consume.setMername(user.getMername());
						consume.setCssum(Double.valueOf((String) xfget.get("cash")));
						consume.setMaketime(paytime);
						consume.setPushflag(back);
						consume.setRemark(remark==null?"":remark);
						consume.setUserid(user.getUserid());
						consume.setWallettype("8");//现金消费
						consume.setClientname((String)xfget.get("clientname"));
						consume.setCsstatus("1");
						logger.info("---------------->成功");
						mapper.insert(consume);
					}else{
						logger.info("---------------->消费失败");
					}
					
				}
				
				logger.info("-------> 存储二维码扣"+clientid+"工号："+amount+"元记录成功,提示信息："+desc);
			} catch (Exception e) {
				logger.info("-------> 存储二维码扣"+clientid+"工号："+amount+"元记录失败,提示信息："+desc+e);
			}finally{
				session.close();
			}
			logger.info("---------------->二维码扫描本地记录存储结束");
		}
		
	}


}
