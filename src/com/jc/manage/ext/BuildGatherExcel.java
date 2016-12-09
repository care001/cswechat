package com.jc.manage.ext;


import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

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
@WebServlet("/manage/BuildGatherExcel.ext")
public class BuildGatherExcel extends HttpServlet{
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
		//excel生成
		HSSFWorkbook wkb = new HSSFWorkbook();
	    HSSFSheet sheet=wkb.createSheet("流水记录表");
	    
	    //excel样式设置  start。。
	    sheet.autoSizeColumn(1);
	    sheet.setDefaultRowHeightInPoints(30);//设置缺省列高
	    sheet.setDefaultColumnWidth(20);//设置缺省列宽
	    HSSFCellStyle style = wkb.createCellStyle();//excel头部样式
	    HSSFCellStyle style1 = wkb.createCellStyle();//excel标题样式
	    HSSFCellStyle style2 = wkb.createCellStyle();//excel数据样式
	    HSSFCellStyle style3 = wkb.createCellStyle();//excel问题数据样式
	    
	    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
  	  	
	    HSSFFont fontSearch = wkb.createFont();
	    fontSearch.setFontHeightInPoints((short) 14);
	    fontSearch.setFontName("宋体");
	    fontSearch.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    
	    HSSFFont fontSearch1 = wkb.createFont();
	    fontSearch1.setColor(HSSFColor.BLACK.index);
	    fontSearch1.setFontHeightInPoints((short) 12);
	    fontSearch1.setFontName("宋体");
	    
	    HSSFFont fontSearch3 = wkb.createFont();
	    fontSearch3.setColor(HSSFColor.RED.index);
	    
	    style.setFont(fontSearch);
	    style1.setFont(fontSearch1);
	    style3.setFont(fontSearch3);
	    //excel样式设置  end。。
	    
	    //在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
	    HSSFRow row1=sheet.createRow(0);
	    row1.setHeightInPoints(30);
	    //创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
	    HSSFCell cell=row1.createCell(0);
	    //设置单元格内容
	    cell.setCellValue("金诚无卡消费-流水汇总表（"+startday+"~"+endday+"）");
	    cell.setCellStyle(style);
	    //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
	    sheet.addMergedRegion(new CellRangeAddress(0,0,0,10));
	    
	    HSSFRow row2=sheet.createRow(1);
	    row2.setHeightInPoints(20);
	    sheet.addMergedRegion(new CellRangeAddress(1,2,0,0));
	    HSSFCell cells1=row2.createCell(0);
	    cells1.setCellValue("卡类");
	    cells1.setCellStyle(style);
	    
	    sheet.addMergedRegion(new CellRangeAddress(1,2,1,1));
	    HSSFCell cells2=row2.createCell(1);
	    cells2.setCellValue("消费类型");
	    cells2.setCellStyle(style);
	    
	    sheet.addMergedRegion(new CellRangeAddress(1,1,2,4));
	    HSSFCell cells3=row2.createCell(2);
	    cells3.setCellValue("收入");
	    cells3.setCellStyle(style);
	    
	    sheet.addMergedRegion(new CellRangeAddress(1,1,5,9));
	    HSSFCell cells4=row2.createCell(5);
	    cells4.setCellValue("支出");
	    cells4.setCellStyle(style);
	    
	    sheet.addMergedRegion(new CellRangeAddress(1,2,10,10));
	    HSSFCell cells5=row2.createCell(10);
	    cells5.setCellValue("余额");
	    cells5.setCellStyle(style);
	    
	   
	    
	    //在sheet里创建第二行
	    HSSFRow row4=sheet.createRow(2);
	    row4.setHeightInPoints(30);
	    //创建单元格并设置单元格内容
	   // HSSFCell cell0=row4.createCell(0);cell0.setCellValue("卡类");
	    //HSSFCell cell1=row4.createCell(1);cell1.setCellValue("消费类型");
	    HSSFCell cell2=row4.createCell(2);cell2.setCellValue("自动充值"); 
	    HSSFCell cell3=row4.createCell(3);cell3.setCellValue("手动充值");    
	    HSSFCell cell4=row4.createCell(4);cell4.setCellValue("转账收入");
	    HSSFCell cell5=row4.createCell(5);cell5.setCellValue("消费");
	    HSSFCell cell6=row4.createCell(6);cell6.setCellValue("退款支出");
	    HSSFCell cell7=row4.createCell(7);cell7.setCellValue("销户支出");
	    HSSFCell cell8=row4.createCell(8);cell8.setCellValue("转账支出");
	    HSSFCell cell9=row4.createCell(9);cell9.setCellValue("补账消费");
	    //HSSFCell cell10=row4.createCell(10);cell10.setCellValue("余额");
	    cell2.setCellStyle(style);
	    cell3.setCellStyle(style);cell4.setCellStyle(style);
	    cell5.setCellStyle(style);cell6.setCellStyle(style);
	    cell7.setCellStyle(style);cell8.setCellStyle(style);
	    cell9.setCellStyle(style);
	    
	    
		
		
	    //在sheet里创建第三行
	    for (int i = 3; i <8; i++) {
    		HSSFRow row=sheet.createRow(i);
    		row.setHeightInPoints(30);
    		if(i==3){
    			sheet.addMergedRegion(new CellRangeAddress(3,4,0,0));
    			HSSFCell cells=row.createCell(0);
    			cells.setCellValue("ID钱包");
    			cells.setCellStyle(style);
    			HSSFCell celld=row.createCell(1);
    			celld.setCellValue("ID现金");
    			celld.setCellStyle(style);
    			HSSFCell cel3=row.createCell(2);cel3.setCellValue(excelGather.getIdxjsr1().doubleValue());
    			HSSFCell cel4=row.createCell(3);cel4.setCellValue(excelGather.getIdxjsr2().doubleValue());
    			HSSFCell cel5=row.createCell(4);cel5.setCellValue(excelGather.getIdxjsr3().doubleValue());
 	    		HSSFCell cel6=row.createCell(5);cel6.setCellValue((excelGather.getIdxjzc1().add(idxj)).doubleValue());
 	    		HSSFCell cel7=row.createCell(6);cel7.setCellValue(excelGather.getIdxjzc2().doubleValue());
 	    		HSSFCell cel8=row.createCell(7);cel8.setCellValue(excelGather.getIdxjzc3().doubleValue());
 	    		HSSFCell cel9=row.createCell(8);cel9.setCellValue(excelGather.getIdxjzc4().doubleValue());
 	    		HSSFCell cel10=row.createCell(9);cel10.setCellValue(excelGather.getIdxjzc5().doubleValue());
 	    		BigDecimal value=(excelGather.getIdxjsr1().add(excelGather.getIdxjsr2()).add(excelGather.getIdxjsr3()))
 	    				.subtract((excelGather.getIdxjzc1().add(idxj)).add(excelGather.getIdxjzc2()).add(excelGather.getIdxjzc3()).add(excelGather.getIdxjzc4()).add(excelGather.getIdxjzc5()));
 	    		HSSFCell cel11=row.createCell(10);cel11.setCellValue(value.doubleValue());
 	    		cel3.setCellStyle(style2);
 	    		cel4.setCellStyle(style2);
 	    		cel5.setCellStyle(style2);
 	    		cel6.setCellStyle(style2);
 	    		cel7.setCellStyle(style2);
 	    		cel8.setCellStyle(style2);
 	    		cel9.setCellStyle(style2);
 	    		cel10.setCellStyle(style2);
 	    		cel11.setCellStyle(style2);
    		}else if(i==4){
    			 HSSFCell celld=row.createCell(1);
    			 celld.setCellValue("ID金诚币");
    			 celld.setCellStyle(style);
    			 HSSFCell cel3=row.createCell(2);cel3.setCellValue(excelGather.getIdjcsr1().doubleValue());
     			HSSFCell cel4=row.createCell(3);cel4.setCellValue(excelGather.getIdjcsr2().doubleValue());
     			HSSFCell cel5=row.createCell(4);cel5.setCellValue(excelGather.getIdjcsr3().doubleValue());
  	    		HSSFCell cel6=row.createCell(5);cel6.setCellValue((excelGather.getIdjczc1().add(idjcb)).doubleValue());
  	    		HSSFCell cel7=row.createCell(6);cel7.setCellValue(excelGather.getIdjczc2().doubleValue());
  	    		HSSFCell cel8=row.createCell(7);cel8.setCellValue(excelGather.getIdjczc3().doubleValue());
  	    		HSSFCell cel9=row.createCell(8);cel9.setCellValue(excelGather.getIdjczc4().doubleValue());
  	    		HSSFCell cel10=row.createCell(9);cel10.setCellValue(excelGather.getIdjczc5().doubleValue());
  	    		BigDecimal value=(excelGather.getIdjcsr1().add(excelGather.getIdjcsr2()).add(excelGather.getIdjcsr3()))
 	    				.subtract((excelGather.getIdjczc1().add(idjcb)).add(excelGather.getIdjczc2()).add(excelGather.getIdjczc3()).add(excelGather.getIdjczc4()).add(excelGather.getIdjczc5()));
 	    		HSSFCell cel11=row.createCell(10);cel11.setCellValue(value.doubleValue());
  	    		cel3.setCellStyle(style2);
  	    		cel4.setCellStyle(style2);
  	    		cel5.setCellStyle(style2);
  	    		cel6.setCellStyle(style2);
  	    		cel7.setCellStyle(style2);
  	    		cel8.setCellStyle(style2);
  	    		cel9.setCellStyle(style2);
  	    		cel10.setCellStyle(style2);
  	    		cel11.setCellStyle(style2);
    		}else if(i==5){
    			 sheet.addMergedRegion(new CellRangeAddress(5,6,0,0));
    			 HSSFCell cells=row.createCell(0);
    			 cells.setCellValue("IC钱包");
    			 cells.setCellStyle(style);
    			 HSSFCell celld=row.createCell(1);
    			 celld.setCellValue("IC现金");
    			 celld.setCellStyle(style);
    			 HSSFCell cel3=row.createCell(2);cel3.setCellValue(excelGather.getIcxjsr1().doubleValue());
      			HSSFCell cel4=row.createCell(3);cel4.setCellValue(excelGather.getIcxjsr2().doubleValue());
      			HSSFCell cel5=row.createCell(4);cel5.setCellValue(excelGather.getIcxjsr3().doubleValue());
   	    		HSSFCell cel6=row.createCell(5);cel6.setCellValue(excelGather.getIcxjzc1().doubleValue());
   	    		HSSFCell cel7=row.createCell(6);cel7.setCellValue(excelGather.getIcxjzc2().doubleValue());
   	    		HSSFCell cel8=row.createCell(7);cel8.setCellValue(excelGather.getIcxjzc3().doubleValue());
   	    		HSSFCell cel9=row.createCell(8);cel9.setCellValue(excelGather.getIcxjzc4().doubleValue());
   	    		HSSFCell cel10=row.createCell(9);cel10.setCellValue(excelGather.getIcxjzc5().doubleValue());
   	    		BigDecimal value=(excelGather.getIcxjsr1().add(excelGather.getIcxjsr2()).add(excelGather.getIcxjsr3()))
 	    				.subtract(excelGather.getIcxjzc1().add(excelGather.getIcxjzc2()).add(excelGather.getIcxjzc3()).add(excelGather.getIcxjzc4()).add(excelGather.getIcxjzc5()));
 	    		HSSFCell cel11=row.createCell(10);cel11.setCellValue(value.doubleValue());
   	    		cel3.setCellStyle(style2);
   	    		cel4.setCellStyle(style2);
   	    		cel5.setCellStyle(style2);
   	    		cel6.setCellStyle(style2);
   	    		cel7.setCellStyle(style2);
   	    		cel8.setCellStyle(style2);
   	    		cel9.setCellStyle(style2);
   	    		cel10.setCellStyle(style2);
   	    		cel11.setCellStyle(style2);
    		}else if(i==6){
    			HSSFCell celld=row.createCell(1);
    			celld.setCellValue("IC金诚币");
    			celld.setCellStyle(style);
    			 HSSFCell cel3=row.createCell(2);cel3.setCellValue(excelGather.getIcjcsr1().doubleValue());
       			HSSFCell cel4=row.createCell(3);cel4.setCellValue(excelGather.getIcjcsr2().doubleValue());
       			HSSFCell cel5=row.createCell(4);cel5.setCellValue(excelGather.getIcjcsr3().doubleValue());
    	    		HSSFCell cel6=row.createCell(5);cel6.setCellValue(excelGather.getIcjczc1().doubleValue());
    	    		HSSFCell cel7=row.createCell(6);cel7.setCellValue(excelGather.getIcjczc2().doubleValue());
    	    		HSSFCell cel8=row.createCell(7);cel8.setCellValue(excelGather.getIcjczc3().doubleValue());
    	    		HSSFCell cel9=row.createCell(8);cel9.setCellValue(excelGather.getIcjczc4().doubleValue());
    	    		HSSFCell cel10=row.createCell(9);cel10.setCellValue(excelGather.getIcjczc5().doubleValue());
    	    		BigDecimal value=(excelGather.getIcjcsr1().add(excelGather.getIcjcsr2()).add(excelGather.getIcjcsr3()))
     	    				.subtract(excelGather.getIcjczc1().add(excelGather.getIcjczc2()).add(excelGather.getIcjczc3()).add(excelGather.getIcjczc4()).add(excelGather.getIcjczc5()));
     	    		HSSFCell cel11=row.createCell(10);cel11.setCellValue(value.doubleValue());
    	    		cel3.setCellStyle(style2);
    	    		cel4.setCellStyle(style2);
    	    		cel5.setCellStyle(style2);
    	    		cel6.setCellStyle(style2);
    	    		cel7.setCellStyle(style2);
    	    		cel8.setCellStyle(style2);
    	    		cel9.setCellStyle(style2);
    	    		cel10.setCellStyle(style2);
    	    		cel11.setCellStyle(style2);
    		}else if(i==7){

    			HSSFCell cells=row.createCell(0);
   			 	cells.setCellValue("ID转IC");
   			 	cells.setCellStyle(style);
    			HSSFCell celld=row.createCell(1);
    			celld.setCellValue("金诚币");
    			celld.setCellStyle(style);
    			HSSFCell cel3=row.createCell(2);cel3.setCellValue("");
       			HSSFCell cel4=row.createCell(3);cel4.setCellValue("");
       			HSSFCell cel5=row.createCell(4);cel5.setCellValue("");
    	    		HSSFCell cel6=row.createCell(5);cel6.setCellValue("");
    	    		HSSFCell cel7=row.createCell(6);cel7.setCellValue("");
    	    		HSSFCell cel8=row.createCell(7);cel8.setCellValue("");
    	    		HSSFCell cel9=row.createCell(8);cel9.setCellValue("");
    	    		HSSFCell cel10=row.createCell(9);cel10.setCellValue("");
    	    		HSSFCell cel11=row.createCell(10);cel11.setCellValue(idtoic.doubleValue());
    	    		cel3.setCellStyle(style2);
    	    		cel4.setCellStyle(style2);
    	    		cel5.setCellStyle(style2);
    	    		cel6.setCellStyle(style2);
    	    		cel7.setCellStyle(style2);
    	    		cel8.setCellStyle(style2);
    	    		cel9.setCellStyle(style2);
    	    		cel10.setCellStyle(style2);
    	    		cel11.setCellStyle(style2);
    		}
    		
    		//HSSFCell cel1=row.createCell(0);cel1.setCellValue(consumes.get(i).getMaketime());
    		//HSSFCell cel2=row.createCell(1);cel2.setCellValue(consumes.get(i).getClientid());
    		
    		
    		
    		
		}
	  //输出Excel文件
	    OutputStream output=resp.getOutputStream();
	    resp.reset();
	    resp.setHeader("Content-disposition", "attachment; filename=GatherExcel.xls");
	    resp.setContentType("application/msexcel");        
	    wkb.write(output);
	    output.close();
	    wkb.close();	
    }
    

}

