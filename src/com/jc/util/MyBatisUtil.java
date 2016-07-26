package com.jc.util;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

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
		
		SqlSession session = InitializeListerner.wkxfSessionFactory.openSession(true);
		
		return session;
	}
}