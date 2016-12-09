package com.jc.util;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * 无卡配置文件工具类*/

public class ConfigByFile {
	private static Properties properties = new Properties();
	static{
		try {
			String rootPath = new BaseUtil().getRootPath();
			FileInputStream fs=new FileInputStream(rootPath+"/config/configfile.properties");
			properties.load(fs);
			fs.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static String sendPayUrl = properties.getProperty("sendPayUrl");//企业号消息发送接口
	public static long payCheckTime = Long.parseLong(properties.getProperty("payCheckTime"));//二维码有效时间
	public static String mid_String = properties.getProperty("mid_String");//加密盐值
	public static String agency_No = properties.getProperty("agency_No");//默认现金发行主体
	public static int cookieMaxAge = Integer.parseInt(properties.getProperty("cookieMaxAge"));//微信消费点登陆cookie保存时间
	public static String jCAgency_No = properties.getProperty("jCAgency_No"); //金诚币默认发行主体

}
