package com.jc.manage.ext;

import java.io.IOException;
import java.io.OutputStream;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

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
@WebServlet("/manage/BuildWaterExcel.ext")
public class BuildWaterExcel extends HttpServlet{

	private static final long serialVersionUID = -3692170308778424641L;

	private static String IN_STATUS = "0";
	private static String OUT_STATUS = "1";
	
	private Map<String, User> map = new HashMap<String, User>();
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String merchantstr=req.getParameter("merchantid");//商户id
		String clientid=req.getParameter("clientid");//员工号
		String watertype=req.getParameter("watertype");//流水类型
		String startday=req.getParameter("startday");//开始时间
		String endday=req.getParameter("endday");//结束时间
		String paytype=req.getParameter("paytype");//流水类型
		int merchantid=0;
		List<Integer> devIds=null;
		List<Integer> secids=null;
		List<String> secidstrs=null;
		List<Consume> list2=new ArrayList<Consume>();
		List<AccTransdetail> list=new ArrayList<AccTransdetail>();
		Map<String, Object> sermap=new HashMap<String, Object>();
		
		
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
			sermap.put("empId", emp.getEmpId());
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
			startday=Coder.formatStartDay(startday);
			sermap.put("startTime", startday);
			criteria.andMaketimeGreaterThanOrEqualTo(startday);
		}//起日期
		if(endday!=null&&endday!=""){
			endday=Coder.formatEndDay(endday);
			sermap.put("endtime", endday);
			criteria.andMaketimeLessThanOrEqualTo(endday);
		}//止日期
		if(watertype!=null&&!watertype.trim().equals("")){
			secids=new ArrayList<Integer>();
			secidstrs=new ArrayList<String>();
			if(watertype.equals("ID")){
				secids.add(7);
				secids.add(8);
			}else if(watertype.equals("IC")){
				secids=new ArrayList<Integer>();
				secids.add(5);
				secids.add(6);
			}else if(watertype.equals("ICCash")){
				secids=new ArrayList<Integer>();
				secids.add(6);
			}else if(watertype.equals("ICGold")){
				secids=new ArrayList<Integer>();
				secids.add(5);
			}else if(watertype.equals("IDCash")){
				secids=new ArrayList<Integer>();
				secids.add(8);
			}else if(watertype.equals("IDGold")){
				secids=new ArrayList<Integer>();
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
		
		//excel
		HSSFWorkbook wkb = new HSSFWorkbook();
	    HSSFSheet sheet=wkb.createSheet("流水明细表");
	    
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
	    cell.setCellValue("金诚无卡消费-流水明细表");
	    cell.setCellStyle(style);
	    //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
	    sheet.addMergedRegion(new CellRangeAddress(0,0,0,9));
	    //在sheet里创建第二行
	    HSSFRow row2=sheet.createRow(1);
	    row2.setHeightInPoints(30);
	    //创建单元格并设置单元格内容
	    HSSFCell cell0=row2.createCell(0);cell0.setCellValue("消费日期");
	    HSSFCell cell1=row2.createCell(1);cell1.setCellValue("消费账号");
	    HSSFCell cell2=row2.createCell(2);cell2.setCellValue("姓名");
	    HSSFCell cell3=row2.createCell(3);cell3.setCellValue("消费类型");
	    HSSFCell cell4=row2.createCell(4);cell4.setCellValue("收入"); 
	    HSSFCell cell5=row2.createCell(5);cell5.setCellValue("支出"); 
	    HSSFCell cell6=row2.createCell(6);cell6.setCellValue("累积金额");    
	    HSSFCell cell7=row2.createCell(7);cell7.setCellValue("消费点（操作员）");
	    HSSFCell cell8=row2.createCell(8);cell8.setCellValue("所属商户");
	    HSSFCell cell9=row2.createCell(9);cell9.setCellValue("备注");
	    cell0.setCellStyle(style1);
	    cell1.setCellStyle(style1);cell2.setCellStyle(style1);
	    cell3.setCellStyle(style1);cell4.setCellStyle(style1);
	    cell5.setCellStyle(style1);cell6.setCellStyle(style1);
	    cell7.setCellStyle(style1);cell8.setCellStyle(style1);
	    cell9.setCellStyle(style1);
	    //在sheet里创建第三行
	    if(list2!=null&&list2.size()>0){
	    	for (int i = 0; i <list2.size(); i++) {
	    		HSSFRow row=sheet.createRow(i+2);
	    		row.setHeightInPoints(30);
	    		HSSFCell cel1=row.createCell(0);cel1.setCellValue(list2.get(i).getMaketime());
	    		HSSFCell cel2=row.createCell(1);cel2.setCellValue(list2.get(i).getClientid());
	    		HSSFCell cel3=row.createCell(2);cel3.setCellValue(list2.get(i).getClientname());
	    		String status=list2.get(i).getCsstatus();
	    		String type=list2.get(i).getWallettype();
	    		//10自动充值，11手工充值，12转帐收入，20消费，21退款支出，22销户支出，23转帐支出，24补帐消费
	    		HSSFCell cel4=row.createCell(3);
	    		HSSFCell cel5=row.createCell(4);
	    		HSSFCell cel6=row.createCell(5);
	    		if(status.equals("1")||status.equals("20")){
	    			if(type.equals("5")){
	    				cel4.setCellValue("IC金诚币消费");
	    			}else if(type.equals("6")){
	    				cel4.setCellValue("IC现金消费");
	    			}else if(type.equals("7")){
	    				cel4.setCellValue("ID金诚币消费");
	    			}else if(type.equals("8")){
	    				cel4.setCellValue("ID现金消费");
	    			}
	    			cel6.setCellValue(list2.get(i).getCssum());
	    		}else if(status.equals("10")){
	    			cel4.setCellValue("自动充值");
	    			cel5.setCellValue(list2.get(i).getCssum());
	    		}else if(status.equals("11")){
	    			cel4.setCellValue("手工充值");
	    			cel5.setCellValue(list2.get(i).getCssum());
	    		}else if(status.equals("12")){
	    			cel4.setCellValue("转帐收入");
	    			cel5.setCellValue(list2.get(i).getCssum());
	    		}else if(status.equals("21")){
	    			cel4.setCellValue("退款支出");
	    			cel6.setCellValue(list2.get(i).getCssum());
	    		}else if(status.equals("22")){
	    			cel4.setCellValue("销户支出");
	    			cel6.setCellValue(list2.get(i).getCssum());
	    		}else if(status.equals("23")){
	    			cel4.setCellValue("转帐支出");
	    			cel6.setCellValue(list2.get(i).getCssum());
	    		}else if(status.equals("24")){
	    			cel4.setCellValue("补帐消费");
	    			cel6.setCellValue(list2.get(i).getCssum());
	    		}
	    		
	    		HSSFCell cel7=row.createCell(6);cel7.setCellValue(list2.get(i).getCssum());
	    		HSSFCell cel8=row.createCell(7);cel8.setCellValue(list2.get(i).getCsmerchat());
	    		HSSFCell cel9=row.createCell(8);cel9.setCellValue(list2.get(i).getMername());
	    		HSSFCell cel10=row.createCell(9);cel10.setCellValue(list2.get(i).getRemark());
	    		
	    		cel1.setCellStyle(style2);
	    		cel2.setCellStyle(style2);
	    		cel3.setCellStyle(style2);
	    		cel4.setCellStyle(style2);
	    		cel5.setCellStyle(style2);
	    		cel6.setCellStyle(style2);
	    		cel7.setCellStyle(style2);
	    		cel8.setCellStyle(style2);
	    		cel9.setCellStyle(style2);
	    		cel10.setCellStyle(style2);
			}
	    }
	    
	    //输出Excel文件
	    OutputStream output=resp.getOutputStream();
	    resp.reset();
	    resp.setHeader("Content-disposition", "attachment; filename=water-history.xls");
	    resp.setContentType("application/msexcel");        
	    wkb.write(output);
	    output.close();
	    wkb.close();
	}

}
