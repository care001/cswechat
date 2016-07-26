package com.jc.util;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


public class Test2 {
	private String publicKey;
	private String privateKey;


	public void setUp() throws Exception {
		Map<String, Object> keyMap = RsaUtil.initKey();

		publicKey = RsaUtil.getPublicKey(keyMap);
		privateKey = RsaUtil.getPrivateKey(keyMap);
		System.err.println("公钥: \n\r" + publicKey);
		System.err.println("私钥： \n\r" + privateKey);
	}


	public void test() throws Exception {
		System.err.println("公钥加密——私钥解密");
		String inputStr = "abc";
		byte[] data = inputStr.getBytes();

		byte[] encodedData = RsaUtil.encryptByPublicKey(data, publicKey);

		System.err.println("加密密文：" + RsaUtil.parseByte2HexStr(encodedData));

		byte[] decodedData = RsaUtil.decryptByPrivateKey(encodedData, privateKey);

		String outputStr = new String(decodedData);
		System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
		//assertEquals(inputStr, outputStr);

	}


	public void testSign() throws Exception {
		System.err.println("私钥加密——公钥解密");
		String inputStr = "sign";
		byte[] data = inputStr.getBytes();

		byte[] encodedData = RsaUtil.encryptByPrivateKey(data, privateKey);

		byte[] decodedData = RsaUtil.decryptByPublicKey(encodedData, publicKey);

		String outputStr = new String(decodedData);
		System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
		//assertEquals(inputStr, outputStr);

		System.err.println("私钥签名——公钥验证签名");
		// 产生签名
		String sign = RsaUtil.sign(encodedData, privateKey);
		System.err.println("签名:\r" + sign);

		// 验证签名
		boolean status = RsaUtil.verify(encodedData, publicKey, sign);
		System.err.println("状态:\r" + status);
		//assertTrue(status);

	}
	
	public static void main(String[] args) {
		  /*String datastr="然后慢慢的穿上了西装，做了一个曾经幻想过的真正的大人，到后来又发现其实穿着西装的人才是最不单纯的。在这种纠结，矛盾中，又学会了一些世俗。完了创业，结婚，走到今天，我开始慢慢写一本自己的书。就像一个莽撞的小年轻进入到江湖一样，这本书在我身边开始有不同的人加入之后，故事就会变得精彩，变得厚重。从我的家人开始，再到后来很多金诚的同事，就像一个个身怀绝技的高手一样，都陪伴着我一起去闯荡这个江湖，从刀光剑影到慢慢穿上西装，不去刻意的做些什么，就让故事这样走下去，走到了今天"
		  		+ "。有很多老员工，我觉得真的应该说是老伙计，他们中有的上下班就要花掉四个多小时，却一直干到了今天；有的陪我一起换了7次办公室，最后我们拥有了自己的一幢楼；有的在公司最困难，离职人最多的时期，拒绝了所有猎头的挖角，陪着金诚撑了过来。";
		  String array=null;
		  try {
			array=RsaUtil.encryptByPrivateKeyseg(datastr.getBytes(), RsaUtil.privateKey);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		  System.out.println(array);
		System.out.println(array.split(",")[0]);*/
		JSONObject result = new JSONObject();
		try {
			result.put("status", false);
			result.put("clientid", "0002621");
			result.put("date", "1999-01-01 12:22:03");
			result.put("desc", "数据库修改失败, 返积极-s回:-1000");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		System.out.println(result.toString().getBytes().length);
		
	}

}
