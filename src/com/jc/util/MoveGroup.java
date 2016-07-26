package com.jc.util;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * move user group util
 * @author tom
 * 2015.12.22
 */
public abstract class MoveGroup {
	
	private static Logger logger = Logger.getLogger(MoveGroup.class);
	
	public static void moveGroupById(String openId,String groupId){
		String url = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=" + BaseUtil.getAccessToken();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.accumulate("openid", openId);
			jsonObject.accumulate("to_groupid", groupId);
		} catch (JSONException e) {
			logger.error("move user group is failure");
			e.printStackTrace();
		}
		String result = BaseUtil.sendPost(url, jsonObject.toString());
		logger.info(result);
	}
}