package com.jc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * 创建自定义菜单
 * @author tom
 * 2015.12.3
 */
public class CreateMenu {
	
	private static Logger logger = Logger.getLogger(CreateMenu.class);
	
	/**
	 * 生成主菜单
	 * @throws IOException
	 */
	public void createMenu() throws IOException{
		String menu = "";

	    File file = new File(new BaseUtil().getRootPath() + "/config/create_menu.json");
	    try {
	        FileInputStream in=new FileInputStream(file);
	        int len = 0;
	        byte[] buffer = new byte[1024];
	        while((len = in.read(buffer)) != -1){
	        	menu += new String(buffer, 0, len, "UTF-8");
	        }
	        in.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + BaseUtil.getAccessToken();
        logger.info(BaseUtil.sendPost(url, menu));
	}
	
	/**
	 * 生成次要菜单
	 * @throws IOException
	 */
	public void createMenu2() throws IOException{
		String menu = "";

	    File file = new File(new BaseUtil().getRootPath() + "/config/create_menu2.json");
	    try {
	        FileInputStream in = new FileInputStream(file);
	        int len = 0;
	        byte[] buffer = new byte[1024];
	        while((len = in.read(buffer)) != -1){
	        	menu += new String(buffer, 0, len, "UTF-8");
	        }
	        in.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
        String url = "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=" + BaseUtil.getAccessToken();
        logger.info(BaseUtil.sendPost(url, menu));
	}
}
