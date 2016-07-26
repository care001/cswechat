package com.jc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.jc.listeners.InitializeListerner;

/**
 * base util
 * @author tom
 * 2015.12.2
 */
public class BaseUtil {
	private static Logger logger = Logger.getLogger(BaseUtil.class);
	
	/**
	 * get project root path
	 * @return String rootPath
	 */
	public String getRootPath(){
		String rootPath = this.getClass().getClassLoader().getResource("/").getPath();
		return rootPath;
	}
	
	/**
	 * get newest accesstoken
	 * @return
	 */
	public static String getAccessToken(){
		String newAccessToken;
		if(InitializeListerner.accessToken.checkAccessToken()){
			newAccessToken = InitializeListerner.accessToken.getAccessToken();
		}else{
			InitializeListerner.accessToken.updateAccessToken();
			newAccessToken = InitializeListerner.accessToken.getAccessToken();
		}
		return newAccessToken;
	}
	
	/**
	 * get newest jsapiTicket
	 * @return
	 */
	public static String getJsapiTicket(){
		String newJsapiTicket;
		if(InitializeListerner.accessToken.checkjsapiTicket()){
			newJsapiTicket = InitializeListerner.accessToken.getJsapi_ticket();
		}else{
			InitializeListerner.accessToken.updatejsapiTicket();
			newJsapiTicket = InitializeListerner.accessToken.getJsapi_ticket();
		}
		return newJsapiTicket;
	}
	
	/**
     * send request to url by method GET
     * @param url
     * @param param name1=value1&name2=value2
     * @return result
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            
            // Open connection between the URL
            URLConnection connection = realUrl.openConnection();
            
            // Set the request of the general properties
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            
            // Open connection
            connection.connect();
            
            // Set BufferedReader input stream to read response
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        	logger.error("Send GET request failure\n" + e.toString() + "\n" + url);
			e.printStackTrace();
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    
    /**
     * send request to url by method POST
     * @param url
     * @param param
     * @return result
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            
            // Open connection between the URL
            URLConnection conn = realUrl.openConnection();
            
            // Set the request of the general properties
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            try{
            	new JSONObject(param);
            	conn.setRequestProperty("Content-Type", "application/json");
            }catch(JSONException e){
            	// do not set content-type
            }
            
            // Send POST request must be set
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            // Get URLConnection object output stream
            out = new PrintWriter(conn.getOutputStream());
            
            // Send param
            out.print(param);
            
            // flush output stream buffer
            out.flush();
            
            // Set BufferedReader input stream to read response
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("Send POST request failure\n" + e.toString() + "\n" + url);
            e.printStackTrace();
        }finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            } catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
