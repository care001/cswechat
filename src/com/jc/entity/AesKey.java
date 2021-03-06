package com.jc.entity;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jc.listeners.InitializeListerner;
import com.jc.util.XmlStringMap;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

/**
 * aes key entity
 * @author tom
 * 2015.12.8
 */
public class AesKey {
	private static Logger logger = Logger.getLogger(AesKey.class);
	
	private String msgSignature;
	private String timeStamp;
	private String nonce;
	private boolean isAesKey;
	private String token;
	private String appId;
	private String encodingAESKey;
	
	// initialize
	public AesKey(String msgSignature,String timeStamp,String nonce){
		this.msgSignature = msgSignature;
		this.timeStamp = timeStamp;
		this.nonce = nonce;
		this.token = InitializeListerner.accessToken.getToken();
		this.encodingAESKey = InitializeListerner.accessToken.getEncodingAESKey();
		this.appId = InitializeListerner.accessToken.getAppId();
		this.isAesKey = true;
	}
	
	// initialize dont need encrypt
	public AesKey(boolean isAesKey){
		this.isAesKey = isAesKey;
	}

	/**
	 * decrypt
	 * @param req
	 * @return Map<String, String>
	 */
	public Map<String, String> decryptAseKey(String req){
		String result = "";
		Map<String, String> map = new HashMap<String, String>();
		try {
			WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAESKey, appId);
			result = pc.decryptMsg(msgSignature, timeStamp, nonce, req);
		} catch (AesException e) {
			logger.error("WXBizMsgCrypt解密失败");
			e.printStackTrace();
		}
		
		// xml string to map
		map = XmlStringMap.xmlStringToMap(result);

		return map;
	}

	/**
	 * encrypt
	 * @param resp
	 * @return String
	 */
	public String encryptAseKey(String resp){
		String responseXml = "";
		if(!isAesKey){
			return resp;
		}
		try {
			WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(token, encodingAESKey, appId);
			responseXml = msgCrypt.encryptMsg(resp, timeStamp, nonce);
		} catch (AesException e) {
			logger.error("WXBizMsgCrypt加密失败");
			e.printStackTrace();
		}
		
		return responseXml;
	}
}