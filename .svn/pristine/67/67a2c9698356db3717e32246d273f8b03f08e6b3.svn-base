package com.jc.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.jc.listeners.InitializeListerner;

/**
 * get openid by code
 * @author tom
 * 2015.12.8
 */
public abstract class GetOpenId {

	public static String getOpenIdByCode(String code){
		String openId = null;
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
		String param = "appid=" + InitializeListerner.accessToken.getAppId() + "&secret=" + InitializeListerner.accessToken.getAppSecret() 
				+ "&code=" + code + "&grant_type=authorization_code";
		JSONObject jsonResult;
		
		String result = BaseUtil.sendGet(url, param);
		try {
			jsonResult = new JSONObject(result);
			openId = jsonResult.getString("openid");
		} catch (JSONException e) {
			e.printStackTrace();
			openId = "errorcode";
		}
		
		return openId;
	}
}