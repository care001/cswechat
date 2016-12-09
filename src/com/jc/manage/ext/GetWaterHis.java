package com.jc.manage.ext;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import cn.com.goldfinance.domain.wkxf.AccTransdetail;
import cn.com.goldfinance.domain.wkxf.Emp;
import cn.com.goldfinance.mapper.wkxf.AccTransdetailMapper;
import cn.com.goldfinance.mapper.wkxf.EmpMapper;

import com.jc.dao.ConfigMapper;
import com.jc.dao.ConsumeMapper;
import com.jc.dao.UserMapper;
import com.jc.entity.Config;
import com.jc.entity.ConfigExample;
import com.jc.entity.Consume;
import com.jc.entity.ConsumeExample;
import com.jc.entity.ConsumeExample.Criteria;
import com.jc.entity.User;
import com.jc.util.Coder;
import com.jc.util.MyBatisUtil;

/**
 * 
 * @author qianjia
 * 2015.6.5
 */
@WebServlet("/manage/GetWaterHis.ext")
public class GetWaterHis extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;
	private static Logger logger = Logger.getLogger(GetWaterHis.class);
	private static final int PAGESIZE=10;
	private static String IN_STATUS = "0";
	private static String OUT_STATUS = "1";
	
	private Map<String, User> map = new HashMap<String, User>();
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String merchantstr=req.getParameter("merchantid");//商户id
		String clientid=req.getParameter("clientid");//员工号
		String watertype=req.getParameter("watertype");//流水钱包类型类型
		String startday=req.getParameter("startday");//开始时间
		String endday=req.getParameter("endday");//结束时间
		String pagenostr = req.getParameter("pagenumstr");//页码
		String paytype=req.getParameter("paytype");//流水类型
		int pageno=1;
		int merchantid=0;
		List<Integer> devIds=null;
		List<Integer> secids=null;
		List<String> secidstrs=null;
		List<Consume> list2=new ArrayList<Consume>();
		List<AccTransdetail> list=new ArrayList<AccTransdetail>();
		Map<String, Object> sermap=new HashMap<String, Object>();
		if(pagenostr!=null&&!pagenostr.equals("")){
			pageno=Integer.valueOf(pagenostr).intValue();
		}
		
		SqlSession session = MyBatisUtil.getwkxfSession();
		EmpMapper empMapper = session.getMapper(EmpMapper.class);
		ConsumeExample example=new ConsumeExample();
		Criteria criteria=example.createCriteria();
		AccTransdetailMapper accTransdetailMapper = session.getMapper(AccTransdetailMapper.class);
		
		SqlSession session2 = MyBatisUtil.getSession();
		ConsumeMapper consumeMapper = session2.getMapper(ConsumeMapper.class);
		ConfigMapper configMapper = session2.getMapper(ConfigMapper.class);
		UserMapper userMapper = session2.getMapper(UserMapper.class);
		if(clientid!=null&&clientid!=""){
			Emp emp = empMapper.getEmpByEmpNo(clientid);
			criteria.andClientidEqualTo(clientid);
			if(emp!=null&&emp.getEmpId()!=null){
				sermap.put("empId", emp.getEmpId());
			}else{
				sermap.put("empId", "-1");
			}
			
		}//员工号
		if(merchantstr!=null&&Coder.isNumeric(merchantstr)){
			merchantid=Integer.parseInt(merchantstr);
			criteria.andMerchantidEqualTo(merchantid);
			ConfigExample configExample=new ConfigExample();
			configExample.createCriteria().andStatusEqualTo("ICMER").andReservesEqualTo(merchantstr);
			List<Config> configs=configMapper.selectByExample(configExample);
			devIds=new ArrayList<Integer>();
			if(configs!=null&&configs.size()>0){
				for (int i = 0; i < configs.size(); i++) {
					String nameString=configs.get(i).getName();
					if(Coder.isNumeric(nameString)){
						devIds.add(Integer.parseInt(nameString));
					}
				}
				sermap.put("devIds", devIds);
			}else{
				devIds.add(0);
				sermap.put("devIds", devIds);
			}
		}//商户
		
		
		if(startday!=null&&startday!=""){
			sermap.put("startTime", Coder.formatStartDay(startday));
			criteria.andMaketimeGreaterThanOrEqualTo( Coder.formatStartDay(startday));
		}//起日期
		if(endday!=null&&endday!=""){
			sermap.put("endtime", Coder.formatEndDay(endday));
			criteria.andMaketimeLessThanOrEqualTo(Coder.formatEndDay(endday));
		}//止日期
		if(watertype!=null&&!watertype.trim().equals("")){
			secids=new ArrayList<Integer>();
			secidstrs=new ArrayList<String>();
			if(watertype.equals("ID")){
				secids.add(7);
				secids.add(8);
			}else if(watertype.equals("IC")){
				secids.add(5);
				secids.add(6);
			}else if(watertype.equals("ICCash")){
				secids.add(6);
			}else if(watertype.equals("ICGold")){
				secids.add(5);
			}else if(watertype.equals("IDCash")){
				secids.add(8);
			}else if(watertype.equals("IDGold")){
				secids.add(7);
			}
			
		}
		if(secids!=null&&secids.size()>0){
			for (int i = 0; i < secids.size(); i++) {
				secidstrs.add(String.valueOf(secids.get(i)));
			}
			sermap.put("secids", secids);
			criteria.andWallettypeIn(secidstrs);
		}//流水钱包类型
		if(paytype!=null&&!paytype.trim().equals("")){
			List<Integer> types=new ArrayList<Integer>();
			if(paytype.equals("topup")){
				types.add(10);
				types.add(11);
				criteria.andCsstatusEqualTo("-1");
			}else if(paytype.equals("pay")){
				types.add(20);
				types.add(22);
				types.add(24);
				criteria.andCsstatusEqualTo("1");
			}else if(paytype.equals("refund")){
				types.add(21);
				criteria.andCsstatusEqualTo("-1");
			}else if(paytype.equals("idtoic")){
				types.add(-1);
				criteria.andCsstatusEqualTo("2");
			}
			sermap.put("types", types);
		}//流水类型
		
		list = accTransdetailMapper
				.getAccTransdetailByMap(sermap);//C3消费记录
		
		list2=consumeMapper.selectByExample(example);//无卡消费 消费记录
		
		ConfigExample configExample=new ConfigExample();
		configExample.createCriteria().andStatusEqualTo("ICMER");
		List<Config> li=configMapper.selectByExample(configExample);
		for(Config config:li){
			User user=null;
			if(config.getValue()!=null&&Coder.isNumeric(config.getValue())){
				user=userMapper.selectByPrimaryKey(Integer.parseInt(config.getValue()));
			}
			if(user!=null){
				map.put(config.getName(), user);
			}
			
		}
		
				


		/**
		 * 10自动充值，11手工充值，12转帐收入，20消费，21退款支出，22销户支出，23转帐支出，24补帐消费
		 */
		if (list.size() > 0) {
			for (AccTransdetail accTransdetail : list) {
				if ("10".equals(accTransdetail.getAcctranstype())) {
					accTransdetail.setType("自动充值");
					accTransdetail
							.setAmount(Coder.getTwoDec(accTransdetail.getImoneyvalue() == null ? accTransdetail
									.getOmoneyvalue() : accTransdetail
									.getImoneyvalue()));
					if(accTransdetail.getDevId()==null||accTransdetail.getDevId().equals("")){
						accTransdetail.setDetail("自动充值");
					}
					accTransdetail.setStatus(IN_STATUS);
				} else if ("11".equals(accTransdetail.getAcctranstype())) {
					accTransdetail.setType("手工充值");
					accTransdetail
							.setAmount(Coder.getTwoDec(accTransdetail.getImoneyvalue() == null ? accTransdetail
									.getOmoneyvalue() : accTransdetail
									.getImoneyvalue()));
					if(accTransdetail.getDevId()==null||accTransdetail.getDevId().equals("")){
						accTransdetail.setDetail("手工充值");
					}
					accTransdetail.setStatus(IN_STATUS);
				} else if ("12".equals(accTransdetail.getAcctranstype())) {
					accTransdetail.setType("转帐收入");
					accTransdetail
							.setAmount(Coder.getTwoDec(accTransdetail.getImoneyvalue() == null ? accTransdetail
									.getOmoneyvalue() : accTransdetail
									.getImoneyvalue()));
					if(accTransdetail.getDevId()==null||accTransdetail.getDevId().equals("")){
						accTransdetail.setDetail("转账收入");
					}
					accTransdetail.setStatus(IN_STATUS);
				} else if ("20".equals(accTransdetail.getAcctranstype())) {
					if (accTransdetail.getSecId().equals("5")) {
						accTransdetail.setType("IC金诚币消费");
					}else if(accTransdetail.getSecId().equals("6")){
						accTransdetail.setType("IC现金消费");
					}else if(accTransdetail.getSecId().equals("7")){
						accTransdetail.setType("ID金诚币消费");
					}else if(accTransdetail.getSecId().equals("8")){
						accTransdetail.setType("ID现金消费");
					}else{
						accTransdetail.setType("消费");
					}
					
					accTransdetail
							.setAmount(Coder.getTwoDec(accTransdetail.getImoneyvalue() == null ? accTransdetail
									.getOmoneyvalue() : accTransdetail
									.getImoneyvalue()));
					if(accTransdetail.getDevId()==null||accTransdetail.getDevId().equals("")){
						accTransdetail.setDetail("消费");
					}
					accTransdetail.setStatus(OUT_STATUS);
				} else if ("21".equals(accTransdetail.getAcctranstype())) {
					accTransdetail.setType("退款支出");
					accTransdetail
							.setAmount(Coder.getTwoDec(accTransdetail.getImoneyvalue() == null ? accTransdetail
									.getOmoneyvalue() : accTransdetail
									.getImoneyvalue()));
					if(accTransdetail.getDevId()==null||accTransdetail.getDevId().equals("")){
						accTransdetail.setDetail("退款支出");
					}
					accTransdetail.setStatus(OUT_STATUS);
				} else if ("22".equals(accTransdetail.getAcctranstype())) {
					accTransdetail.setType("销户支出");
					accTransdetail
							.setAmount(Coder.getTwoDec(accTransdetail.getImoneyvalue() == null ? accTransdetail
									.getOmoneyvalue() : accTransdetail
									.getImoneyvalue()));
					if(accTransdetail.getDevId()==null||accTransdetail.getDevId().equals("")){
						accTransdetail.setDetail("销户支出");
					}
					accTransdetail.setStatus(OUT_STATUS);
				} else if ("23".equals(accTransdetail.getAcctranstype())) {
					accTransdetail.setType("转帐支出");
					accTransdetail
							.setAmount(Coder.getTwoDec(accTransdetail.getImoneyvalue() == null ? accTransdetail
									.getOmoneyvalue() : accTransdetail
									.getImoneyvalue()));
					if(accTransdetail.getDevId()==null||accTransdetail.getDevId().equals("")){
						accTransdetail.setDetail("转账支出");
					}
					accTransdetail.setStatus(OUT_STATUS);
				} else if ("24".equals(accTransdetail.getAcctranstype())) {
					accTransdetail.setType("补帐消费");
					accTransdetail
							.setAmount(Coder.getTwoDec(accTransdetail.getImoneyvalue() == null ? accTransdetail
									.getOmoneyvalue() : accTransdetail
									.getImoneyvalue()));
					if(accTransdetail.getDevId()==null||accTransdetail.getDevId().equals("")){
						accTransdetail.setDetail("补账消费");
					}
					accTransdetail.setStatus(OUT_STATUS);
				}
				//
				Consume consume=new Consume();
				Emp emp2=empMapper.getEmpByEmpId(Integer.parseInt(accTransdetail.getEmpId()));
				if (accTransdetail.getDevId()!=null&&map.containsKey(accTransdetail.getDevId())) {
					User user2=map.get(accTransdetail.getDevId());
					consume.setMerchantid(user2.getMerchantid());
					consume.setCsmerchat(user2.getUsername());
					consume.setMername(user2.getMername());
					consume.setUserid(user2.getUserid());
					consume.setCsstatus("1");
				}else{
					consume.setMerchantid(-2);
					consume.setCsmerchat(accTransdetail.getOprtid()==null?"":accTransdetail.getOprtid());
					consume.setMername("");
					consume.setUserid(-2);
					consume.setCsstatus(accTransdetail.getAcctranstype());
				}
				
				
				consume.setClientid(emp2.getEmpNo());
				consume.setCssum(Double.valueOf(accTransdetail.getAmount()));
				consume.setMaketime(accTransdetail.getAcctransday());
				consume.setPushflag("400");
				consume.setRemark(accTransdetail.getRemark()==null?"":accTransdetail.getRemark());
				consume.setWallettype(accTransdetail.getSecId());//金诚币消费 
				consume.setClientname(emp2.getEmpName());
				
				list2.add(consume);		

			}
			
		}
		session.close();
		session2.close();

		//按时间排序
		Collections.sort(list2, new Comparator<Consume>() {  
            public int compare(Consume arg0, Consume arg1) {  
                String time0=arg0.getMaketime();
                String time1=arg1.getMaketime();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                int tt=0;
                try {
					if (df.parse(time0).getTime() > df.parse(time1).getTime()) {  
						tt=1;  
					} else if (df.parse(time0).getTime() > df.parse(time1).getTime()) {  
						tt=0;  
					} else {  
						tt=-1;  
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}  
                return tt;
            }  
        }); 
		JSONObject result = new JSONObject();
		int pageall=1;
		int size=list2.size();
		try {
			if(size%PAGESIZE==0){
				if(size/PAGESIZE==0){
					pageall=1;
				}else{
					pageall=size/PAGESIZE;
				}
			}else{
				pageall=(size/PAGESIZE)+1;
			}
			//list2 = list2.subList((pageno-1)*PAGESIZE, pageno*PAGESIZE>size?size:pageno*PAGESIZE);

			result.put("consumes", list2);
			result.put("conssum", size);
			result.put("pageall",pageall);
			result.put("pagenow", pageno);
		} catch (Exception e) {
			logger.info(e);
		}
		PrintWriter out = resp.getWriter();
		out.print(result.toString());
		out.close();
	}


	

}
