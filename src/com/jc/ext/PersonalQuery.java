package com.jc.ext;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import cn.com.goldfinance.domain.wkxf.AccTransdetail;
import cn.com.goldfinance.domain.wkxf.CoinPojo;
import cn.com.goldfinance.domain.wkxf.Dev;
import cn.com.goldfinance.domain.wkxf.Emp;
import cn.com.goldfinance.mapper.wkxf.AccTransdetailMapper;
import cn.com.goldfinance.mapper.wkxf.DevMapper;
import cn.com.goldfinance.mapper.wkxf.EmpMapper;

import com.jc.dao.ConsumeMapper;
import com.jc.entity.Consume;
import com.jc.entity.ConsumeExample;
import com.jc.util.Coder;
import com.jc.util.MyBatisUtil;
import com.jc.util.RsaUtil;

/**
 * 
 * @author qianjia
 * 2015.6.5
 */
@WebServlet("/PersonalQuery.ext")
public class PersonalQuery extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;
	private static Logger logger = Logger.getLogger(PersonalQuery.class);

	private static String IN_STATUS = "0";
	private static String OUT_STATUS = "1";
	
	private Map<String, String> map = new HashMap<String, String>();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String rsaempNo = req.getParameter("empNo");
		String year = req.getParameter("year");
		String empNo="";
		  
		try {
			byte[] encodedData=Coder.parseHexStr2Byte(rsaempNo);
			byte[] decodedData = RsaUtil.decryptByPrivateKey(encodedData, RsaUtil.privateKey);
			empNo = new String(decodedData);
		} catch (Exception e) {
			logger.info("解密失败");	
		}
		CoinPojo coinPojo = null;
		SqlSession session = MyBatisUtil.getwkxfSession();
		EmpMapper empMapper = session.getMapper(EmpMapper.class);
		AccTransdetailMapper accTransdetailMapper = session.getMapper(AccTransdetailMapper.class);
		DevMapper devMapper = session.getMapper(DevMapper.class);
		SqlSession session2 = MyBatisUtil.getSession();
		ConsumeMapper consumeMapper = session2.getMapper(ConsumeMapper.class);
		if (year != null&&!year.trim().equals("")) {
		// 切割时间格式2016-04
			String[] years = year.split("-");
			String yea = years[0];
			String mon = (Integer.parseInt(years[1]) - 1) + "";
			coinPojo = Coder.getCoinPojo(yea, mon);
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
			year = dateFormat.format(new Date());
			coinPojo = Coder.getCoinPojo(new Date());
		}
	
		Emp emp = empMapper.getEmpByEmpNo(empNo);
		coinPojo.setEmpId(emp.getEmpId());
		List<AccTransdetail> list = accTransdetailMapper
				.getAccTransdetailByCoinPojo(coinPojo);
				
		List<Dev> li=devMapper.getAllDev();
		if(list.size()>0){
			for(Dev dev:li){
				map.put(dev.getDevId(), dev.getDevName());
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
				if(accTransdetail.getDevId()!=null&&!accTransdetail.getDevId().equals("")){
					accTransdetail.setDetail((String)map.get(accTransdetail.getDevId()));
				}
				
						

			}
		}
		Map<String, String> queryMap=Coder.FirstandLastday(year);
		ConsumeExample example=new ConsumeExample();
		example.or().andCsstatusEqualTo("1").andClientidEqualTo(empNo).andMaketimeBetween(queryMap.get("first"), queryMap.get("last"));
		List<Consume> list2=consumeMapper.selectByExample(example);
		if(list2!=null&&list2.size()>0){
			for (int i = 0; i < list2.size(); i++) {
				Consume consume=list2.get(i);
				AccTransdetail accTransdetail=new AccTransdetail();
				accTransdetail.setAcctransday(consume.getMaketime());
				if(consume.getWallettype().equals("7")){
					accTransdetail.setType("ID金诚币消费");
				}else if(consume.getWallettype().equals("8")){
					accTransdetail.setType("ID现金消费");
				}
				accTransdetail.setAmount(Coder.getTwoDec(String.valueOf(consume.getCssum())));
				accTransdetail.setDetail(consume.getCsmerchat());
				list.add(accTransdetail);
			}
		}
		//按时间排序
		Collections.sort(list, new Comparator<AccTransdetail>() {  
            public int compare(AccTransdetail arg0, AccTransdetail arg1) {  
                String time0=arg0.getAcctransday();
                String time1=arg1.getAcctransday();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                int tt=0;
                try {
					if (df.parse(time1).getTime() > df.parse(time0).getTime()) {  
						tt=1;  
					} else if (df.parse(time1).getTime() == df.parse(time0).getTime()) {  
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
		session.close();
		session2.close();
		RequestDispatcher rd = req.getRequestDispatcher("jsp/personal.jsp");
	    req.setAttribute("year", year);
	    req.setAttribute("list", list);
	    req.setAttribute("empNo", rsaempNo);
	    rd.forward(req,resp);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

}