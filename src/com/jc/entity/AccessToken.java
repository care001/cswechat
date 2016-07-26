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
public class AccessToken {
	private static Logger logger = Logger.getLogger(AccessToken.class);
	
	private SimpleDateFormat sdf=new SimpleDateFormat("MMddHHmmss");
	private String token;
	private String appId;
	private String appSecret;
	private String accessToken;
	private String requestTime;
	private String jtreqTime;
	private String jsapi_ticket;
	private Integer sleepTime;
	private String encodingAESKey;
	
	
	public AccessToken(){
	}

	public String getEncodingAESKey(){
		return encodingAESKey;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		AccessToken.logger = logger;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
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

	public void setToken(String token) {
		this.token = token;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setEncodingAESKey(String encodingAESKey) {
		this.encodingAESKey = encodingAESKey;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getToken(){
		return token;
	}

	public String getAppId(){
		return appId;
	}

	public String getAppSecret(){
		return appSecret;
	}

	public String getJtreqTime() {
		return jtreqTime;
	}

	public void setJtreqTime(String jtreqTime) {
		this.jtreqTime = jtreqTime;
	}

	public String getJsapi_ticket() {
		return jsapi_ticket;
	}

	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}

	/**
	 * check accesstoken whether or not past due
	 * @return
	 */
	public boolean checkAccessToken(){
		Integer now = Integer.parseInt(sdf.format(new Date()));
		Integer oldTime = Integer.parseInt(requestTime);
		if(now - oldTime >= sleepTime){
			return false;
		}
		return true;
	}

	/**
	 * update accesstoken
	 */
	public void updateAccessToken(){
		String url = "https://api.weixin.qq.com/cgi-bin/token";
		String param = "grant_type=client_credential&appid="+appId+"&secret="+appSecret;
		String result = BaseUtil.sendGet(url, param);
		
		String pattern = "(.*)(\"access_token\":\")(.*)(\",\"expires_in\":)(.*)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(result);
		if (m.find()) {
	         accessToken = m.group(3);
	         requestTime = sdf.format(new Date());
	    } else {
	         logger.error("updateAccessToken is failure");
	    }
	}
	
	/**
	 * check jsapiTicket whether or not past due
	 * @return
	 */
	public boolean checkjsapiTicket(){
		Integer now = Integer.parseInt(sdf.format(new Date()));
		
		
		if(jtreqTime==null){
			return false;
		}else{
			Integer oldTime = Integer.parseInt(jtreqTime);
			if(now - oldTime >= sleepTime){
				return false;
			}else{
				return true;
			}
		}
		
	}

	/**
	 * update accesstoken
	 */
	public void updatejsapiTicket(){
		String  accessstr=BaseUtil.getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
		String param = "access_token="+accessstr+"&type=jsapi";
		String result = BaseUtil.sendGet(url, param);
		
		String pattern = "(.*)(\"ticket\":\")(.*)(\",\"expires_in\":)(.*)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(result);
		if (m.find()) {
	         jsapi_ticket = m.group(3);
	         jtreqTime = sdf.format(new Date());
	    } else {
	         logger.error("updatejsapiTicket is failure");
	    }
	}
		/* String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	        String requestUrl = url.replace("ACCESS_TOKEN", accessstr);
	        // 发起GET请求获取凭证
	        JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
	        if (null != jsonObject) {
	            try {
	            	jsapi_ticket = jsonObject.getString("ticket");
	            	jtreqTime = sdf.format(new Date());
	            } catch (JSONException e) {
	                // 获取token失败
	                logger.info("获取token失败 errcode:{} errmsg:{}"+jsonObject.getInt("errcode")+ jsonObject.getString("errmsg"));
	            }
	        }*/
	
}
