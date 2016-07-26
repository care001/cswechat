package org.easywechat.util;

/**  
 * Oauth2类  
 * @author Engineer.Jsp 
 * @date 2014.10.13  
 */  
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.jc.util.BaseUtil;
  
public class GOauth2Core {  
    public static String GET_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx19f885b88db57ae4&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=a123#wechat_redirect";  
    /** 
     * 企业获取code地址处理 
     * @param appid 企业的CorpID 
     * @param redirect_uri 授权后重定向的回调链接地址，请使用urlencode对链接进行处理 
     * @param response_type 返回类型，此时固定为：code 
     * @param scope 应用授权作用域，此时固定为：snsapi_base 
     * @param state 重定向后会带上state参数，企业可以填写a-zA-Z0-9的参数值 
     * @param #wechat_redirect 微信终端使用此参数判断是否需要带上身份信息 
     * 员工点击后，页面将跳转至 redirect_uri/?code=CODE&state=STATE，企业可根据code参数获得员工的userid 
     * */  
    /*public static String GetCode(){  
        String get_code_url = "";  
        get_code_url = GET_CODE.replace("REDIRECT_URI", WeixinUtil.URLEncoder(ParamesAPI.REDIRECT_URI));  
        return get_code_url;  
    } */
    public static String CODE_TO_USERURL="https://api.weixin.qq.com/sns/userinfo";//"https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo";
    public static String CODE_TO_USERPARAM ="access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";// "access_token=ACCESS_TOKEN&code=CODE&agentid=AGENTID";  
      
    /** 
     * 根据code获取成员信息 
     * @param access_token 调用接口凭证 
     * @param code 通过员工授权获取到的code，每次员工授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期 
     * @param agentid 跳转链接时所在的企业应用ID 
     * 管理员须拥有agent的使用权限；agentid必须和跳转链接时所在的企业应用ID相同 
     * */  
    public static Map<String,String> GetUserID (String access_token,String oppenid){  
        Map<String, String> map=new HashMap<String, String>();  
        CODE_TO_USERPARAM = CODE_TO_USERPARAM.replace("ACCESS_TOKEN", access_token).replace("OPENID", oppenid);  
        String result = BaseUtil.sendGet(CODE_TO_USERURL, CODE_TO_USERPARAM);
        JSONObject jsStr =null;
        try {
			jsStr = new JSONObject(result);
			System.out.println("用户信息："+result);
			map.put("username", jsStr.getString("nickname"));
			map.put("userimg", jsStr.getString("headimgurl"));
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
        
        return map;  
    }  
  
}  
