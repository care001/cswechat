package com.jc.manage.ext;


import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;

import cn.com.goldfinance.domain.wkxf.ExcelGather;

import com.jc.dao.ConsumeMapper;
import com.jc.entity.ConsumeExample;
import com.jc.util.Coder;
import com.jc.util.MyBatisUtil;
import com.jc.util.PowerDataOperUtil;
/**
 * 
 * @author qianjia
 * 2016.6.5
 */
@WebServlet("/manage/GetGatherHis.ext")
public class GetGatherHis extends HttpServlet{
	private static final long serialVersionUID = -3692170308778424641L;
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String startday=req.getParameter("startday");//开始时间
		String endday=req.getParameter("endday");//结束时间
		startday=Coder.formatStartDay(startday);
		endday=Coder.formatEndDay(endday);
		ExcelGather excelGather=PowerDataOperUtil.getExcelGatherByDay(startday, endday);
		SqlSession session2 = MyBatisUtil.getSession();
		ConsumeMapper consumeMapper = session2.getMapper(ConsumeMapper.class);
		ConsumeExample example=new ConsumeExample();
		example.createCriteria().andCsstatusEqualTo("1").andWallettypeEqualTo("7").andMaketimeBetween(startday, endday);
		BigDecimal idjcb=consumeMapper.sumMoneyByExample(example);//ID金诚币消费
		example.clear();
		example.createCriteria().andCsstatusEqualTo("1").andWallettypeEqualTo("8").andMaketimeBetween(startday, endday);
		BigDecimal idxj=consumeMapper.sumMoneyByExample(example);//ID现金消费
		example.clear();
		example.createCriteria().andCsstatusEqualTo("2").andMaketimeBetween(startday, endday);
		BigDecimal idtoic=consumeMapper.sumMoneyByExample(example);//ID转IC
		session2.close();
		BigDecimal jcbzc=excelGather.getIdjczc1().add(idjcb);
		BigDecimal xjzc=excelGather.getIdxjzc1().add(idxj);
		excelGather.setIdjczc1(jcbzc);
		excelGather.setIdxjzc1(xjzc);
		excelGather.setIdxjsum((excelGather.getIdxjsr1().add(excelGather.getIdxjsr2()).add(excelGather.getIdxjsr3()))
 				.subtract(excelGather.getIdxjzc1().add(excelGather.getIdxjzc2()).add(excelGather.getIdxjzc3()).add(excelGather.getIdxjzc4()).add(excelGather.getIdxjzc5())));
		excelGather.setIdjcsum((excelGather.getIdjcsr1().add(excelGather.getIdjcsr2()).add(excelGather.getIdjcsr3()))
 				.subtract(excelGather.getIdjczc1().add(excelGather.getIdjczc2()).add(excelGather.getIdjczc3()).add(excelGather.getIdjczc4()).add(excelGather.getIdjczc5())));
		excelGather.setIcxjsum((excelGather.getIcxjsr1().add(excelGather.getIcxjsr2()).add(excelGather.getIcxjsr3()))
 				.subtract(excelGather.getIcxjzc1().add(excelGather.getIcxjzc2()).add(excelGather.getIcxjzc3()).add(excelGather.getIcxjzc4()).add(excelGather.getIcxjzc5())));
		excelGather.setIcjcsum((excelGather.getIcjcsr1().add(excelGather.getIcjcsr2()).add(excelGather.getIcjcsr3()))
 				.subtract(excelGather.getIcjczc1().add(excelGather.getIcjczc2()).add(excelGather.getIcjczc3()).add(excelGather.getIcjczc4()).add(excelGather.getIcjczc5())));
		JSONObject result = new JSONObject();
		try {
			result.put("excelGather",new JSONObject(excelGather));
			result.put("IDToIC", idtoic);
		} catch (Exception e) {
		}
		PrintWriter out = resp.getWriter();
		out.print(result.toString());
		out.close();
    }
    
}

