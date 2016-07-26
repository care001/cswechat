package com.jc.util;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import cn.com.goldfinance.domain.wkxf.AccHead;
import cn.com.goldfinance.domain.wkxf.AccHeadPojo;
import cn.com.goldfinance.domain.wkxf.AccTransdetail;
import cn.com.goldfinance.domain.wkxf.Emp;
import cn.com.goldfinance.domain.wkxf.GrpPutMoney;
import cn.com.goldfinance.domain.wkxf.PutMoney;
import cn.com.goldfinance.mapper.wkxf.AccHeadMapper;
import cn.com.goldfinance.mapper.wkxf.AccTransdetailMapper;
import cn.com.goldfinance.mapper.wkxf.EmpMapper;
import cn.com.goldfinance.mapper.wkxf.GrpPutMoneyMapper;
import cn.com.goldfinance.mapper.wkxf.HrmsEmpMapper;
import cn.com.goldfinance.mapper.wkxf.PutMoneyMapper;

public class NoCardUtil {
	private static JDBCUtil jdbcUtil=JDBCUtil.getInstance();
	private static Logger logger = Logger.getLogger(NoCardUtil.class);
	private static String COMPANY_SECID="5"; 
	private static String PERSON_SECID="6";
	/*
	 * ID消费**/
	public static Map<String, Object> PosGetXF(String clientid,double amount,String business,String busNo,String userid){
		Map<String, Object> map=new HashMap<String, Object>();
		Connection con=null;
		CallableStatement ca=null;
		SqlSession session = MyBatisUtil.getwkxfSession();
		HrmsEmpMapper hrmsEmpMapper = session.getMapper(HrmsEmpMapper.class);
		int conflag=-10;
		try {
			int cardId=hrmsEmpMapper.getMaxCardIDByEmpNo(clientid);
			String empName=hrmsEmpMapper.getEmpNameByEmpNo(clientid);
			map.put("clientname", empName);
			System.out.println("cardid:"+cardId);
			double balance=PosMoney(cardId, 7);
			con=jdbcUtil.getConnection();
			con.setAutoCommit(false);
			if(balance>=amount){
				ca =con.prepareCall("{? = call up_PosGetXF(?,?,?,?,?,?,?,?,?)}");
				ca.registerOutParameter(1, Types.INTEGER);//返回 0成功 100重复数据
	    		ca.setInt(2,cardId);//卡流水号
	    		ca.setInt(3,7);//钱包
	    		ca.setDouble(4,amount);//消费金额
	    		ca.setString(5,business);//商户名称（消费去向）
	    		ca.setInt(6,38);//操作员id
	    		ca.setString(7,userid);//电脑名称
	    		ca.setString(8,"weixin");//电脑用户名称
	    		ca.setInt(9,10);//消费方式 10-第三方
	    		ca.setInt(10,0);//IC钱包
	    		ca.execute();
	    		conflag=ca.getInt(1);
	    		map.put("gold", String.valueOf(amount));
	    		map.put("cash", "0.0");
			}else {
				double balance2=PosMoney(cardId, 8);
				BigDecimal b1 = new BigDecimal(Double.toString(balance));
		        BigDecimal b2 = new BigDecimal(Double.toString(balance2));
		        BigDecimal b3 = new BigDecimal(Double.toString(amount));
		        double balsum= b1.add(b2).doubleValue();
		        double poor= b3.subtract(b1).doubleValue();
				if(balsum>=amount){
					if(balance>0){
						ca =con.prepareCall("{? = call up_PosGetXF(?,?,?,?,?,?,?,?,?)}");
						ca.registerOutParameter(1, Types.INTEGER);//返回 0成功 100重复数据
			    		ca.setInt(2,cardId);//卡流水号
			    		ca.setInt(3,7);//钱包
			    		ca.setDouble(4,balance);//消费金额
			    		ca.setString(5,business);//商户名称（消费去向）
			    		ca.setInt(6,38);//操作员id
			    		ca.setString(7,userid);//电脑名称
			    		ca.setString(8,"weixin");//电脑用户名称
			    		ca.setInt(9,10);//消费方式 10-第三方
			    		ca.setInt(10,0);//IC钱包
			    		ca.execute();
			    		conflag=ca.getInt(1);
			    		
			    	}else{
						conflag=0;
					}
					
		    		if(conflag==0){
		    			conflag=-10;
		    			ca =con.prepareCall("{? = call up_PosGetXF(?,?,?,?,?,?,?,?,?)}");
						ca.registerOutParameter(1, Types.INTEGER);//返回 0成功 100重复数据
			    		ca.setInt(2,cardId);//卡流水号
			    		ca.setInt(3,8);//钱包
			    		ca.setDouble(4,poor);//消费金额
			    		ca.setString(5,business);//商户名称（消费去向）
			    		ca.setInt(6,38);//操作员id
			    		ca.setString(7,userid);//电脑名称
			    		ca.setString(8,"weixin");//电脑用户名称
			    		ca.setInt(9,10);//消费方式 10-第三方
			    		ca.setInt(10,0);//IC钱包
			    		ca.execute();
			    		conflag=ca.getInt(1);
			    		map.put("gold", String.valueOf(balance));
			    		map.put("cash", String.valueOf(poor));
		    		}
				}else{
					map.put("gold", String.valueOf(amount));
	    			map.put("cash", "0.0");
					map.put("conflag", -1);
					map.put("desc", "余额不足");
					logger.info("余额不足。。");
				}
	    		
			}
			if(conflag==0){
				con.commit();
				map.put("conflag", 0);
				map.put("desc", "成功支付");
				logger.info("--------->"+clientid+"一卡通系统扣费"+amount+"成功");
			}else{
				con.rollback();
				if(conflag!=-10){
					map.put("conflag", -1);
					map.put("desc", "数据库修改失败, 返回:"+conflag);
					logger.info("数据库修改失败，数据已回滚..返回标志："+conflag);
				}
			}
		} catch (Exception e) {
			try {
				if (con!=null) {
					con.rollback();
				}
				map.put("gold", String.valueOf(amount));
	    		map.put("cash", "0.0");
				map.put("conflag", -1);
				map.put("desc", "数据库修改出错");
				logger.info("数据库修改出错，数据已回滚..");
				return map;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			
		}finally{
			session.close();
			try {
				if (ca!=null) {
					ca.close();
				}
				jdbcUtil.free(con, null, null);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return map;
	}
	/*
	 * ID余额获取**/
	public static double PosMoney(int cardid ,int secid){
		Connection con=null;
		double balance=0;
		try {
			con=jdbcUtil.getConnection();
    		CallableStatement ca=con.prepareCall("{call up_PosMoney(?,?)}");
    		ca.setInt(1,cardid);
    		ca.setInt(2,secid);
    		ca.execute();
    		ResultSet rs = ca.getResultSet();
    		if (rs.next()) {
    			balance=rs.getDouble(1);
			}
    		con.close();
    		ca.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			jdbcUtil.free(con, null, null);
		}
		return balance;
	}
	
	/*
	 * IC余额**/
	public static Map<String, Double> getICBalance(String empno){
		Map<String, Double> ic=new HashMap<String, Double>();
		SqlSession session = MyBatisUtil.getwkxfSession();
		EmpMapper empMapper = session.getMapper(EmpMapper.class);
		AccHeadMapper accHeadMapper = session.getMapper(AccHeadMapper.class);
		AccTransdetailMapper accTransdetailMapper = session.getMapper(AccTransdetailMapper.class);
		
		GrpPutMoneyMapper grpPutMoneyMapper = session.getMapper(GrpPutMoneyMapper.class);
		PutMoneyMapper putMoneyMapper = session.getMapper(PutMoneyMapper.class);
		Emp emp = empMapper.getEmpByEmpNo(empno);
		double total = 0;
		double total1 = 0;
		double total2 = 0;
		double ntotal1 = 0;
		double ntotal2 = 0;
		int maxcardID=0;
		int max1=0;
		int max2=0;
		if (emp != null) {
			List<AccTransdetail> list = accTransdetailMapper
					.getAccTransdetailByEmpId(emp.getEmpId());
			if(list.size()>0){
				for (AccTransdetail accTransdetail : list) {
					if(Integer.parseInt(accTransdetail.getCardId())>maxcardID){
						maxcardID=Integer.parseInt(accTransdetail.getCardId());
					}
				}
				for (AccTransdetail accTransdetail : list) {
					if(accTransdetail.getCardId().equals(maxcardID+"")){
						if(accTransdetail.getSecId().equals("5")){
							if(Integer.parseInt(accTransdetail.getCardusenum()) >=max1){
								max1=Integer.parseInt(accTransdetail.getCardusenum());
								total1=Double.parseDouble(accTransdetail.getCardmoneyvalue());
							}
						}
						if(accTransdetail.getSecId().equals("6")){
							if(Integer.parseInt(accTransdetail.getCardusenum()) >=max2){
								max2=Integer.parseInt(accTransdetail.getCardusenum());
								total2=Double.parseDouble(accTransdetail.getCardmoneyvalue());
							}
						}
					}
					
				}
			}else{
				AccHeadPojo accHeadPojo2=new AccHeadPojo();
				accHeadPojo2.setSecId(PERSON_SECID);
				accHeadPojo2.setEmpId(emp.getEmpId());
				List<PutMoney> liPut =putMoneyMapper.getGrpPutMoneyByAccHeadPojo(accHeadPojo2);
				for(PutMoney putMoney:liPut){
					if(Integer.parseInt(putMoney.getCardUseNum()) >=max1){
						max1=Integer.parseInt(putMoney.getCardUseNum());
						total1=Double.parseDouble(putMoney.getCardMoneyAfter());
					}
				}
			}
			AccHeadPojo accHeadPojo=new AccHeadPojo();
			accHeadPojo.setSecId(COMPANY_SECID);
			accHeadPojo.setEmpId(emp.getEmpId());
			List<AccHead> listaccHead =accHeadMapper.getAccHeadByAccHeadPojo(accHeadPojo);
			if(listaccHead.size() >0){
					for(AccHead accHead:listaccHead){
						List<GrpPutMoney> liGrp =grpPutMoneyMapper.getGrpPutMoneyByAccountno(accHead.getAccountno());
						for(GrpPutMoney grpPutMoney:liGrp){
							if(grpPutMoney.getActPutMoneyValue()==null){
								ntotal1=new BigDecimal(Double.toString(ntotal1)).add(new BigDecimal(grpPutMoney.getPlanPutMoneyValue())).doubleValue();
							}
						}
					}
					
					
			}
			/**
			 * 新增 未激活ic余额
			 * 2061.7.18 qj
			 * start*/
			ntotal2=ntotal1;
			AccHeadPojo accHeadPojo2=new AccHeadPojo();
			accHeadPojo2.setSecId(PERSON_SECID);
			accHeadPojo2.setEmpId(emp.getEmpId());
			List<AccHead> listaccHead2 =accHeadMapper.getAccHeadByAccHeadPojo(accHeadPojo2);
			if(listaccHead2.size() >0){
					for(AccHead accHead:listaccHead2){
						List<GrpPutMoney> liGrp =grpPutMoneyMapper.getGrpPutMoneyByAccountno(accHead.getAccountno());
						for(GrpPutMoney grpPutMoney:liGrp){
							if(grpPutMoney.getActPutMoneyValue()==null){
								ntotal2=new BigDecimal(Double.toString(ntotal2)).add(new BigDecimal(grpPutMoney.getPlanPutMoneyValue())).doubleValue();
							}
						}
					}
					
					
			}
			/**
			 * 新增 未激活ic余额
			 * 2061.7.18 qj
			 * end*/
			
			//total=total1+total2+ntotal1;
			total=new BigDecimal(Double.toString(total1)).add(new BigDecimal(Double.toString(total2))).add(new BigDecimal(Double.toString(ntotal2))).doubleValue();
			
		} else {
			total=0.0;
			ntotal2=0.0;
		}
		ic.put("total", total);
		ic.put("ntotal", ntotal2);
		session.close();
		return ic;
	}
	
	/*
	 * ID转IC**/
	public static Map<String, Object> ICToID(String clientid,double amount){
		Map<String, Object> map=new HashMap<String, Object>();
		double ICBalance=getICBalance(clientid).get("total");
		if(new BigDecimal(Double.toString(ICBalance)).add(new BigDecimal(Double.toString(amount))).doubleValue()>=5000){
			map.put("gold", String.valueOf(amount));
    		map.put("cash", "0.0");
			map.put("conflag", -1);
			map.put("desc", "IC余额不能超过5000");
			logger.info(clientid+"：IC余额不能超过5000");
			return map;
		}else{
			Connection con=null;
			CallableStatement ca=null;
			SqlSession session = MyBatisUtil.getwkxfSession();
			HrmsEmpMapper hrmsEmpMapper = session.getMapper(HrmsEmpMapper.class);
			int conflag=-10;
			try {
				int cardId=hrmsEmpMapper.getMaxCardIDByEmpNo(clientid);
				String empName=hrmsEmpMapper.getEmpNameByEmpNo(clientid);
				map.put("clientname", empName);
				double balance=PosMoney(cardId, 7);
				con=jdbcUtil.getConnection();
				con.setAutoCommit(false);
				if(balance>=amount){
					ca =con.prepareCall("{? = call up_PosGetXF(?,?,?,?,?,?,?,?,?)}");
					ca.registerOutParameter(1, Types.INTEGER);//返回 0成功 100重复数据
		    		ca.setInt(2,cardId);//卡流水号
		    		ca.setInt(3,7);//钱包
		    		ca.setDouble(4,amount);//消费金额
		    		ca.setString(5,"ID金诚币转入");//商户名称（消费去向）
		    		ca.setInt(6,37);//操作员id
		    		ca.setString(7,clientid);//电脑名称
		    		ca.setString(8,"weixin");//电脑用户名称
		    		ca.setInt(9,20);//消费方式 20-转账
		    		ca.setInt(10,5);//IC钱包
		    		ca.execute();
		    		conflag=ca.getInt(1);
		    		map.put("gold", String.valueOf(amount));
		    		map.put("cash", "0.0");
				}else {
					/*double balance2=PosMoney(cardId, 8);
					BigDecimal b1 = new BigDecimal(Double.toString(balance));
			        BigDecimal b2 = new BigDecimal(Double.toString(balance2));
			        BigDecimal b3 = new BigDecimal(Double.toString(amount));
			        double balsum= b1.add(b2).doubleValue();
			        double poor= b3.subtract(b1).doubleValue();
					if(balsum>=amount){
						if(balance>0){
							ca =con.prepareCall("{? = call up_PosGetXF(?,?,?,?,?,?,?,?,?)}");
							ca.registerOutParameter(1, Types.INTEGER);//返回 0成功 100重复数据
				    		ca.setInt(2,cardId);//卡流水号
				    		ca.setInt(3,7);//钱包
				    		ca.setDouble(4,balance);//消费金额
				    		ca.setString(5,"ID金诚币转入");//商户名称（消费去向）
				    		ca.setInt(6,37);//操作员id
				    		ca.setString(7,clientid);//电脑名称
				    		ca.setString(8,"weixin");//电脑用户名称
				    		ca.setInt(9,20);//消费方式 10-第三方
				    		ca.setInt(10,5);//IC钱包
				    		ca.execute();
				    		conflag=ca.getInt(1);
				    		
				    	}else{
							conflag=0;
						}
						
			    		if(conflag==0){
			    			conflag=-10;
			    			ca =con.prepareCall("{? = call up_PosGetXF(?,?,?,?,?,?,?,?,?)}");
							ca.registerOutParameter(1, Types.INTEGER);//返回 0成功 100重复数据
				    		ca.setInt(2,cardId);//卡流水号
				    		ca.setInt(3,8);//钱包
				    		ca.setDouble(4,poor);//消费金额
				    		ca.setString(5,"ID现金转入");//商户名称（消费去向）
				    		ca.setInt(6,37);//操作员id
				    		ca.setString(7,clientid);//电脑名称
				    		ca.setString(8,"weixin");//电脑用户名称
				    		ca.setInt(9,20);//消费方式 10-第三方
				    		ca.setInt(10,5);//IC钱包 
				    		ca.execute();
				    		conflag=ca.getInt(1);
				    		map.put("gold", String.valueOf(balance));
				    		map.put("cash", String.valueOf(poor));
			    		}
					}else{
						map.put("gold", String.valueOf(amount));
		    			map.put("cash", "0.0");
						map.put("conflag", -1);
						map.put("desc", "余额不足");
						logger.info("余额不足。。");
					}*/
					map.put("gold", String.valueOf(amount));
	    			map.put("cash", "0.0");
					map.put("conflag", -1);
					map.put("desc", "余额不足");
				}
				if(conflag==0){
					con.commit();
					map.put("conflag", 0);
					map.put("desc", "成功转账");
				}else{
					con.rollback();
					if(conflag!=-10){
						map.put("conflag", -1);
						map.put("desc", "数据库修改失败, 返回:"+conflag);
						logger.info("数据库修改失败，数据已回滚..返回标志："+conflag);
					}
				}
			} catch (Exception e) {
				try {
					if (con!=null) {
						con.rollback();
					}
					map.put("conflag", -1);
					map.put("desc", "数据库修改出错");
					logger.info("数据库修改出错，数据已回滚..");
					return map;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
				
			}finally{
				session.close();
				try {
					if (ca!=null) {
						ca.close();
					}
					jdbcUtil.free(con, null, null);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}
		return map;
	}
	public static void main(String[] args) {
		System.out.println(getICBalance("0001866"));
	}
}