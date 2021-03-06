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

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.jc.util.BaseUtil;

@WebServlet({"/GetJSSDK.ext"})
public class GetJSSDK
  extends HttpServlet
{
  private static final long serialVersionUID = 4689183699420922501L;
  private static Logger logger = Logger.getLogger(GetJSSDK.class);
  
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
  {
    JSONObject result = new JSONObject();
    //String url = "http://wxcard.gold-finance.com.cn/jsp/confirmMoney.html";
    String url=req.getParameter("jssdkurl");
    logger.info("jssdk url:" + url);
    String jssdk = "";
    jssdk = BaseUtil.getJsapiTicket();
    Map<String, String> ret = sign(jssdk, url);
    try
    {
      for (Map.Entry<String, String> entry : ret.entrySet()) {
        result.put((String)entry.getKey(), entry.getValue());
      }
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
    PrintWriter out = resp.getWriter();
    out.print(result.toString());
    out.close();
  }
  
  public static Map<String, String> sign(String jsapi_ticket, String url)
  {
    Map<String, String> ret = new HashMap<String, String>();
    String nonce_str = create_nonce_str();
    String timestamp = create_timestamp();
    
    String signature = "";
    
    //注意这里参数名必须全部小写，且必须有序
    String string1 = "jsapi_ticket=" + jsapi_ticket + 
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
    logger.info("url:"+url+" jsapi_ticket:"+jsapi_ticket+" nonce_str:"+nonce_str+" timestamp:"+timestamp+" signature:"+signature);
    return ret;
  }
  
  private static String byteToHex(byte[] hash)
  {
    Formatter formatter = new Formatter();
    for (byte b : hash)
    {
        formatter.format("%02x", b);
    }
    String result = formatter.toString();
    formatter.close();
    return result;
  }
  
  private static String create_nonce_str()
  {
    return UUID.randomUUID().toString();
  }
  
  private static String create_timestamp()
  {
    return Long.toString(System.currentTimeMillis() / 1000L);
  }
}
