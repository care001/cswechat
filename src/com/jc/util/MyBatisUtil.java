package com.jc.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.jc.listeners.InitializeListerner;

/**
 * mybatis util
 * @author tom
 * 2015.12.2
 */

public abstract class MyBatisUtil {
	/*private static SqlSessionFactory sqlSessionFactory;
	static{
		String resource = "config/wkxfconf.xml";
		try {
			InputStream inputStream = Resources.getResourceAsStream(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	/**
	 * get mybatis session,wether or not open auto commit
	 * @param Boolean commit
	 * @return SqlSession session
	 */
	public static SqlSession getSession(Boolean commit){
		SqlSessionFactory sqlSessionFactory = InitializeListerner.sqlSessionFactory;
		SqlSession session = sqlSessionFactory.openSession(commit);
		
		return session;
	}
	
	/**
	 * get mybatis session
	 * @return session
	 */
	public static SqlSession getSession(){
		SqlSessionFactory sqlSessionFactory = InitializeListerner.sqlSessionFactory;
		SqlSession session = sqlSessionFactory.openSession(true);
		
		return session;
	}
	
	/**
	 * get mybatis session,wether or not open auto commit
	 * @param Boolean commit
	 * @return SqlSession session
	 */
	public static SqlSession getwkxfSession(Boolean commit){
		SqlSessionFactory sqlSessionFactory = InitializeListerner.wkxfSessionFactory;
		SqlSession session = sqlSessionFactory.openSession(commit);
		
		return session;
	}
	
	/**
	 * get mybatis session
	 * @return session
	 */
	public static SqlSession getwkxfSession(){
		/*String resource2 = "config/wkxfconf.xml";
		SqlSessionFactory wkxfSessionFactory=null;
		try {

			InputStream inputStream2 = Resources.getResourceAsStream(resource2);
			
			wkxfSessionFactory = new SqlSessionFactoryBuilder().build(inputStream2);
		} catch (IOException e) {
			e.printStackTrace(); 
		}
		SqlSession session =wkxfSessionFactory.openSession(true);*/
		SqlSession session = InitializeListerner.wkxfSessionFactory.openSession(true);
		
		return session;
	}
}