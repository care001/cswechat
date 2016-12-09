package com.jc.robot;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jc.util.BaseUtil;

/**
 * call tuling robot http api
 * @author tom
 * 2015.12.15
 */
public abstract class TuLing {
	
	private static Logger logger = Logger.getLogger(TuLing.class);

	public static String chatWithTuLing(String content,String openId){
		String reply = "";
		String url = "http://www.tuling123.com/openapi/api";
		
		// delete space
		content = content.replace(" ", "");
		String param = "key=085a6f5a22de986c2d6f3829ecc6ec97&info=" + content + "&userid=" + openId;
		String result = BaseUtil.sendGet(url, param);
		
		try {
			JSONObject jsonResult = new JSONObject(result);
			Integer code = jsonResult.getInt("code");
			if(code == 100000){
				String text = jsonResult.getString("text");
				reply = text;
			}
			if(code == 200000){
				String text = jsonResult.getString("text");
				url = jsonResult.getString("url");
				if(url.equals("")){
					reply = text;
				}else{
					reply = text+"\n<a href=\""+url+"\">点击这里</a>";
				}
			}
			if(code == 302000){
				String text = jsonResult.getString("text");
				JSONArray list = jsonResult.getJSONArray("list");
				for(int i=0;i<list.length();i++){
					jsonResult = (JSONObject) list.get(i);
					String article = jsonResult.getString("article");
					String source = jsonResult.getString("source");
					String detailurl = jsonResult.getString("detailurl");
					reply = reply + article+"\n"+source+"\n"+"<a href=\""+detailurl+"\">点击这里</a>\n";
				}
				reply = text+"\n"+reply;
			}
			if(code == 308000){
				String text = jsonResult.getString("text");
				JSONArray list = jsonResult.getJSONArray("list");
				jsonResult = (JSONObject) list.get(0);
				String name = jsonResult.getString("name");
				String info = jsonResult.getString("info");
				String detailurl = jsonResult.getString("detailurl");
				reply = text+"\n"+name+"\n"+info+"<a href=\""+detailurl+"\">点击这里</a>";
			}
		} catch (JSONException e) {
			logger.warn("json format is not legal");
			return "error";
		}
		
		return reply;
	}
}
