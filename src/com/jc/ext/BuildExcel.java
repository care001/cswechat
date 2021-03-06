package com.jc.ext;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import com.jc.entity.Consume;
import com.jc.entity.User;
import com.jc.util.Coder;
import com.jc.util.PowerDataOperUtil;
/**
 * 
 * @author qianjia
 * 2016.6.5
 */
@WebServlet("/admin/BuildExcel.ext")
public class BuildExcel extends HttpServlet{
	private static final long serialVersionUID = -3692170308778424641L;
	private static Logger logger = Logger.getLogger(BuildExcel.class);
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String startdate = req.getParameter("startdate");
		String enddate = req.getParameter("enddate");
		String userid = req.getParameter("userid");
		String merchantid = req.getParameter("merchantid");
		HttpSession httpsession=req.getSession();
		User user=(User)httpsession.getAttribute("admin");
		if(user==null||user.getUserid()<=0){
			logger.info("请登陆。。");
		}else{
			Map<String, Object> map=new HashMap<String, Object>();
			if(user.getStatus().equals("1")||user.getStatus().equals("99")){
				map.put("userid", user.getUserid());
				map.put("Merchantid", user.getMerchantid());
			}else if(user.getStatus().equals("2")){
				map.put("userid", userid);
				map.put("Merchantid", user.getMerchantid());
			}else if(user.getStatus().equals("3")||user.getStatus().equals("4")){
				map.put("userid", userid);
				map.put("Merchantid", merchantid);
			}
			map.put("seardate1", Coder.formatStartDay(startdate));
			map.put("seardate2", Coder.formatEndDay(enddate));
			List<Consume> consumes=PowerDataOperUtil.getAllHisByPower(map, user.getStatus());
			//excel生成
			HSSFWorkbook wkb = new HSSFWorkbook();
		    HSSFSheet sheet=wkb.createSheet("消费记录表");
		    
		    //excel样式设置  start。。
		    sheet.autoSizeColumn(1);
		    sheet.setDefaultRowHeightInPoints(30);//设置缺省列高
		    sheet.setDefaultColumnWidth(35);//设置缺省列宽
		    HSSFCellStyle style = wkb.createCellStyle();//excel头部样式
		    HSSFCellStyle style1 = wkb.createCellStyle();//excel标题样式
		    HSSFCellStyle style2 = wkb.createCellStyle();//excel数据样式
		    HSSFCellStyle style3 = wkb.createCellStyle();//excel问题数据样式
		    
		    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		    style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		    style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		    style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		    
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
		    cell.setCellValue("金诚卡无卡消费-消费记录表("+startdate+"~"+enddate+" 注：每日结算节点为17:00)");
		    cell.setCellStyle(style);
		    //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		    sheet.addMergedRegion(new CellRangeAddress(0,0,0,7));
		    //在sheet里创建第二行
		    HSSFRow row2=sheet.createRow(1);
		    row2.setHeightInPoints(30);
		    //创建单元格并设置单元格内容
		    HSSFCell cell0=row2.createCell(0);cell0.setCellValue("消费日期");
		    HSSFCell cell1=row2.createCell(1);cell1.setCellValue("消费账号");
		    HSSFCell cell2=row2.createCell(2);cell2.setCellValue("消费用户"); 
		    HSSFCell cell3=row2.createCell(3);cell3.setCellValue("消费金额");    
		    HSSFCell cell4=row2.createCell(4);cell4.setCellValue("消费点");
		    HSSFCell cell5=row2.createCell(5);cell5.setCellValue("消费商户");
		    HSSFCell cell6=row2.createCell(6);cell6.setCellValue("消费钱包");
		    HSSFCell cell7=row2.createCell(7);cell7.setCellValue("备注");
		    cell0.setCellStyle(style1);
		    cell1.setCellStyle(style1);cell2.setCellStyle(style1);
		    cell3.setCellStyle(style1);cell4.setCellStyle(style1);
		    cell5.setCellStyle(style1);cell6.setCellStyle(style1);
		    cell7.setCellStyle(style1);
		    //在sheet里创建第三行
		    if(consumes!=null&&consumes.size()>0){
		    	for (int i = 0; i <consumes.size(); i++) {
		    		HSSFRow row=sheet.createRow(i+2);
		    		row.setHeightInPoints(30);
		    		HSSFCell cel1=row.createCell(0);cel1.setCellValue(consumes.get(i).getMaketime());
		    		HSSFCell cel2=row.createCell(1);cel2.setCellValue(consumes.get(i).getClientid());
		    		HSSFCell cel3=row.createCell(2);cel3.setCellValue(consumes.get(i).getClientname());
		    		HSSFCell cel4=row.createCell(3);cel4.setCellValue(Double.valueOf(consumes.get(i).getCssum()));
		    		HSSFCell cel5=row.createCell(4);cel5.setCellValue(consumes.get(i).getCsmerchat());
		    		HSSFCell cel6=row.createCell(5);cel6.setCellValue(consumes.get(i).getMername());
		    		HSSFCell cel7=row.createCell(6);
		    		if(consumes.get(i).getWallettype().equals("5")){
		    			cel7.setCellValue("IC金诚币");
		    		}else if(consumes.get(i).getWallettype().equals("6")){
		    			cel7.setCellValue("IC现金");
		    		}else if(consumes.get(i).getWallettype().equals("7")){
		    			cel7.setCellValue("ID金诚币");
		    		}else if(consumes.get(i).getWallettype().equals("8")){
		    			cel7.setCellValue("ID现金");
		    		}
		    		HSSFCell cel8=row.createCell(7);cel8.setCellValue(consumes.get(i).getRemark());
		    		cel1.setCellStyle(style2);
		    		cel2.setCellStyle(style2);
		    		cel3.setCellStyle(style2);
		    		cel4.setCellStyle(style2);
		    		cel5.setCellStyle(style2);
		    		cel6.setCellStyle(style2);
		    		cel7.setCellStyle(style2);
		    		cel8.setCellStyle(style2);
				}
		    }
		    
		    //输出Excel文件
		    OutputStream output=resp.getOutputStream();
		    resp.reset();
		    resp.setHeader("Content-disposition", "attachment; filename="+user.getLoginname()+"-history.xls");
		    resp.setContentType("application/msexcel");        
		    wkb.write(output);
		    output.close();
		    wkb.close();
		}
		
	}

	

}
