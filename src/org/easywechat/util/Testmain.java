package org.easywechat.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.com.goldfinance.mapper.wkxf.HrmsEmpMapper;

import com.jc.util.Coder;
import com.jc.util.JDBCUtil;
import com.jc.util.NoCardUtil;

public class Testmain {
	private static JDBCUtil jdbcUtil=JDBCUtil.getInstance();
	private static SqlSessionFactory sqlSessionFactory;
	static{
		String resource = "config/wkxfconf.xml";
		try {
			InputStream inputStream = Resources.getResourceAsStream(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static int PosPutXF(String empNo,double money){
		SqlSession session = sqlSessionFactory.openSession(true);
		HrmsEmpMapper hrmsEmpMapper = session.getMapper(HrmsEmpMapper.class);
		Connection con=null;
		int cardId=hrmsEmpMapper.getMaxCardIDByEmpNo(empNo);
		session.close();
		CallableStatement ca=null;
		try {
			con=jdbcUtil.getConnection();
			ca =con.prepareCall("{? = call up_PosPutXF(?,?,?,?,?,?,?,?)}");
			ca.registerOutParameter(1, Types.INTEGER);//返回 0成功 100重复数据
    		ca.setInt(2,cardId);//卡流水号
    		ca.setInt(3,7);//钱包
    		ca.setInt(4,10);
    		ca.setDouble(5,money);
    		ca.setString(6,"cscz");
    		ca.setInt(7,1);
    		ca.setString(8,"cs");
    		ca.setString(9,"cs");
    		ca.execute();
    		return ca.getInt(1);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		return -100;
	}
	
	public static double PosMoney(String empNo ,int secid){
		SqlSession session = sqlSessionFactory.openSession(true);
		HrmsEmpMapper hrmsEmpMapper = session.getMapper(HrmsEmpMapper.class);
		Connection con=null;
		int cardid=hrmsEmpMapper.getMaxCardIDByEmpNo(empNo);
		session.close();
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
 	public static void main(String[] args) {
		
		System.out.println(Coder.formatTime(new Date()));
		try {
			System.out.println(Coder.EncoderByMd5("Gold2016"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		String emp="0002621";
		//System.out.println(NoCardUtil.getICBalance("0001355"));
 		System.out.println("充值前余额："+PosMoney(emp,7));
 		//System.out.println(PosPutXF(emp,5000));
 		System.out.println("充值后余额："+PosMoney(emp,7));
		/*SqlSession session=sqlSessionFactory.openSession(true);
		HrmsEmpMapper hrmsEmpMapper = session.getMapper(HrmsEmpMapper.class);
		int cardId=hrmsEmpMapper.getMaxCardIDByEmpNo("2001000");
		double goldcoin=NoCardUtil.PosMoney(cardId, 7);
		double cash=NoCardUtil.PosMoney(cardId, 8);
		BigDecimal b1 = new BigDecimal(Double.toString(goldcoin));
        BigDecimal b2 = new BigDecimal(Double.toString(cash));
		System.out.println("-------------------------->"+b1.add(b2).doubleValue());*/
		//System.out.println(Coder.formatStartDay("2016-07-15")+"~~~~~~"+Coder.formatEndDay("2016-08-08"));
		
	}
}
