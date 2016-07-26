package com.jc.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.jc.util.BaseUtil;

/**
 * access token entity
 * @author tom
 * 2015.12.2
 */
public class OAuthAccessToken {
	private static Logger logger = Logger.getLogger(OAuthAccessToken.class);
	
	private SimpleDateFormat sdf=new SimpleDateFormat("MMddHHmmss");
	private String appId;
	private String appSecret;
	private String accessToken;
	private String requestTime;
	private Integer sleepTime;
	private String openid;
	
	
	public OAuthAccessToken(){
	}

	

	public String getAppId() {
		return appId;
	}



	public void setAppId(String appId) {
		this.appId = appId;
	}



	public String getAppSecret() {
		return appSecret;
	}



	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}



	public String getAccessToken() {
		return accessToken;
	}



	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}



	public String getRequestTime() {
		return requestTime;
	}



	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}



	public Integer getSleepTime() {
		return sleepTime;
	}



	public void setSleepTime(Integer sleepTime) {
		this.sleepTime = sleepTime;
	}



	public String getOpenid() {
		return openid;
	}



	public void setOpenid(String openid) {
		this.openid = openid;
	}



	/**
	 * check accesstoken whether or not past due
	 * @return
	 */
	public boolean checkAccessToken(){
		Integer now = Integer.parseInt(sdf.format(new Date()));
		if(requestTime!=null&&requestTime.trim()!=""){
			Integer oldTime = Integer.parseInt(requestTime);
			if(now - oldTime >= sleepTime){
				return false;
			}else {
				return true;
			}
		}else{
			return false;
		}
		
		
		
	}

	/**
	 * update accesstoken
	 */
	public void updateAccessToken(String code){
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
		String param = "appid="+appId+"&secret="+appSecret+"&code="+code+"&grant_type=authorization_code";
		String result = BaseUtil.sendGet(url, param);
		String pattern = "(.*)(\"access_token\":\")(.*)(\",\"expires_in\":)(.*)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(result);
		;
		if (m.find()) {
	         accessToken = m.group(3);
	         openid=result.split(",")[3].split(":")[1].replace("\"", "");
	         requestTime = sdf.format(new Date());
	    } else {
	         logger.error("updateAccessToken is failure");
	    }
	}
	

	
}
