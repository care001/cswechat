package com.jc.listeners;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import com.jc.entity.AccessToken;
import com.jc.entity.AesKey;
import com.jc.entity.OAuthAccessToken;
import com.jc.util.BaseUtil;

/**
 * servlet listener，initialize accesstokenMap、MyBatis pool、user tag list
 * @author tom
 * 2016.1.6
 */
public class InitializeListerner implements ServletContextListener{
	
	private static Logger logger = Logger.getLogger(InitializeListerner.class);

	public static AccessToken accessToken;
	public static AesKey aesKey;
	public static SqlSessionFactory sqlSessionFactory;
	public static SqlSessionFactory wkxfSessionFactory;
	public static OAuthAccessToken oauthAccessToken;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("already destory accesstoken");
		logger.info("already destory MyBatis pool");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// initialize accesstoken entity
		accessToken = new AccessToken();
		oauthAccessToken=new OAuthAccessToken();
		String rootPath = new BaseUtil().getRootPath();
		Properties props = new Properties();
		try {
			FileInputStream fis = new FileInputStream(rootPath+"/config/access_token.properties");
			props.load(fis);
			accessToken.setAppId(props.getProperty("appId"));
			accessToken.setAppSecret(props.getProperty("appSecret"));
			accessToken.setSleepTime(Integer.parseInt(props.getProperty("sleepTime")));
			accessToken.setEncodingAESKey(props.getProperty("encodingAESKey"));
			accessToken.setToken(props.getProperty("token"));
			
			oauthAccessToken.setAppId(props.getProperty("appId"));
			oauthAccessToken.setAppSecret(props.getProperty("appSecret"));
			oauthAccessToken.setSleepTime(Integer.parseInt(props.getProperty("sleepTime")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// initialize accesstoken
		accessToken.updateAccessToken();

		logger.info("initialize accesstoken");
		
		// initialize MyBatis pool
		String resource = "config/conf.xml";
		String resource2 = "config/wkxfconf.xml";
		try {
			InputStream inputStream = Resources.getResourceAsStream(resource);
			InputStream inputStream2 = Resources.getResourceAsStream(resource2);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			wkxfSessionFactory = new SqlSessionFactoryBuilder().build(inputStream2);
		} catch (IOException e) {
			logger.error("read Mybatis config file is failure");
			e.printStackTrace(); 
		}
		
		logger.info("initialize MyBatis pool");
	}

}