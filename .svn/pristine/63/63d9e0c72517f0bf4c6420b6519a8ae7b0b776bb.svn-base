package com.jc.main;

import org.apache.log4j.Logger;
import org.easywechat.msg.NewsMsg;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jc.listeners.InitializeListerner;
import com.jc.util.AesUtil;
import com.jc.util.BaseUtil;

/**
 * Get latest news
 * @author tom
 * 2015.12.22
 */
public abstract class GetNews {
	
	private static Logger logger = Logger.getLogger(GetNews.class);
	
	/**
	 * 获取官方最新动态
	 * @param newsMsg 图文类型实体
	 * @param channelId 需要获得的信息类型
	 * @return NewsMsg 图文类型实体
	 */
	public static NewsMsg getNews(NewsMsg newsMsg,String channelId){
		String url = "http://weixinapi.gold-finance.com.cn/jcwechatweb/getArticle.action";
		String param = "channelId="+channelId;
		String result = BaseUtil.sendPost(url, param);
		
		// decrypt
		try {
			result = AesUtil.decrypt(result, InitializeListerner.accessToken.getToken());
		} catch (NullPointerException e){
			logger.error("getArticle.action response is null");
			return null;
		}
		try {
			JSONArray jsonArray = new JSONArray(result);
			JSONObject jsonObject = null;
			String title = "";
			String picUrl = "";
			String description = "";
			url = "";
			if(jsonArray.length() == 0){
				return null;
			}
			for(int i=0;i<jsonArray.length();i++){
				jsonObject = (JSONObject) jsonArray.get(i);
				title = jsonObject.get("title").toString();
				picUrl = "http://www.gold-finance.com.cn/"+jsonObject.get("picUrl").toString();
				description = jsonObject.get("description").toString();
				url = "http://weixinapi.gold-finance.com.cn/jcwechatweb"+jsonObject.get("url").toString();
				newsMsg.add(title, description, picUrl, url);
			}
		} catch (JSONException e) {
			logger.error("json format is not legal");
		}
		
		return newsMsg;
	}

	/**
	 * 获取官方投顾资讯
	 * @param newsMsg 图文类型实体
	 * @param channelId 需要获得的信息类型
	 * @return NewsMsg 图文类型实体
	 */
	public static NewsMsg getInfo(NewsMsg newsMsg, String channelId) {
		String url = "http://weixinapi.gold-finance.com.cn/jcwechatweb/getNews.action";
		String param = "channelId="+channelId;
		String result = BaseUtil.sendPost(url, param);
		
		// decrypt
		try {
			result = AesUtil.decrypt(result, InitializeListerner.accessToken.getToken());
		} catch (NullPointerException e){
			logger.error("getNews.action response is null");
			return null;
		}
		
		try {
			JSONArray jsonArray = new JSONArray(result);
			JSONObject jsonObject = null;
			String title = "";
			String picUrl = "";
			String description = "";
			url = "";
			if(jsonArray.length() == 0){
				return null;
			}
			for(int i=0;i<jsonArray.length();i++){
				jsonObject = (JSONObject) jsonArray.get(i);
				title = jsonObject.get("title").toString();
				picUrl = "http://www.gold-finance.com.cn/"+jsonObject.get("picUrl").toString();
				description = jsonObject.get("description").toString();
				url = "http://weixinapi.gold-finance.com.cn/jcwechatweb"+jsonObject.get("url").toString();
				newsMsg.add(title, description, picUrl, url);
			}
		} catch (JSONException e) {
			logger.error("json format is not legal");
			return null;
		}
		
		return newsMsg;
	}
}