package com.jc.ext;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.jc.util.BaseUtil;

/**
 * 
 * @author 	qianjia
 * 2016.6.1
 */
@WebServlet("/GetJSSDK.ext")
public class GetJSSDK extends HttpServlet{

	private static final long serialVersionUID = 4689183699420922501L;
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject result = new JSONObject();
		String jssdk="";
		jssdk=BaseUtil.getJsapiTicket();
		String url = req.getRequestURL().toString();
		url = req.getScheme() +"://" + req.getServerName() + req.getContextPath()+"/jsp/confirmMoney.html";
		System.out.println("url:"+url);
		//url="http://6ee31be9.ngrok.natapp.cn/jsp/sweepcode.jsp";
	    Map<String, String> ret = sign(jssdk, url);
	    try {
	    for (Map.Entry<String, String> entry : ret.entrySet()) {
	        result.put(entry.getKey()+"", entry.getValue());
	    }
System.out.println(result.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// encrypt
		PrintWriter out = resp.getWriter();
		out.print(result.toString());
		out.close();
	}



	    public static Map<String, String> sign(String jsapi_ticket, String url) {
	        Map<String, String> ret = new HashMap<String, String>();
	        String nonce_str = create_nonce_str();
	        String timestamp = create_timestamp();
	        String string1;
	        String signature = "";

	        //注意这里参数名必须全部小写，且必须有序
	        string1 = "jsapi_ticket=" + jsapi_ticket +
	                  "&noncestr=" + nonce_str +
	                  "&timestamp=" + timestamp +
	                  "&url=" + url;

	        try
	        {
	            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	            crypt.reset();
	            crypt.update(string1.getBytes("UTF-8"));
	            signature = byteToHex(crypt.digest());
	        }
	        catch (NoSuchAlgorithmException e)
	        {
	            e.printStackTrace();
	        }
	        catch (UnsupportedEncodingException e)
	        {
	            e.printStackTrace();
	        }

	        ret.put("url", url);
	        ret.put("jsapi_ticket", jsapi_ticket);
	        ret.put("nonceStr", nonce_str);
	        ret.put("timestamp", timestamp);
	        ret.put("signature", signature);

	        return ret;
	    }

	    private static String byteToHex(final byte[] hash) {
	        Formatter formatter = new Formatter();
	        for (byte b : hash)
	        {
	            formatter.format("%02x", b);
	        }
	        String result = formatter.toString();
	        formatter.close();
	        return result;
	    }

	    private static String create_nonce_str() {
	        return UUID.randomUUID().toString();
	    }

	    private static String create_timestamp() {
	        return Long.toString(System.currentTimeMillis() / 1000);
	    }
	  /*  private static String getUrl(){
	        HttpServletRequest request = ServletActionContext.getRequest();
	         
	        StringBuffer requestUrl = request.getRequestURL();
	         
	        String queryString = request.getQueryString();
	        String url = requestUrl +"?"+queryString;
	        return url;
	    }*/

}