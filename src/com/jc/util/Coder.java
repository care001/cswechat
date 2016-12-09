package com.jc.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Encoder;
import cn.com.goldfinance.domain.wkxf.CoinPojo;


  
/** 
 * 基础加密组件 
 *  
 * @author 梁栋 
 * @version 1.0 
 * @since 1.0 
 */  
public abstract class Coder {  
    public static final String KEY_SHA = "SHA";  
    public static final String KEY_MD5 = "MD5";  
    public static final String MD5ADD="csweixin";
  
    /** 
     * MAC算法可选以下多种算法 
     *  
     * <pre> 
     * HmacMD5  
     * HmacSHA1  
     * HmacSHA256  
     * HmacSHA384  
     * HmacSHA512 
     * </pre> 
     */  
    public static final String KEY_MAC = "HmacMD5";  
  
    /** 
     * BASE64解密 
     *  
     * @param key 
     * @return 
     * @throws Exception 
     */  
    public static byte[] decryptBASE64(String key) throws Exception {  
        return Base64.decodeBase64(key);  
    }  
  
    /** 
     * BASE64加密 
     *  
     * @param key 
     * @return 
     * @throws Exception 
     */  
    public static String encryptBASE64(byte[] key) throws Exception {  
        return Base64.encodeBase64String(key);
    }  
  
    /** 
     * MD5加密 
     *  
     * @param data 
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptMD5(byte[] data) throws Exception {  
  
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);  
        md5.update(data);  
  
        return md5.digest();  
  
    }  
  
    /** 
     * SHA加密 
     *  
     * @param data 
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptSHA(byte[] data) throws Exception {  
  
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);  
        sha.update(data);  
  
        return sha.digest();  
  
    }  
  
    /** 
     * 初始化HMAC密钥 
     *  
     * @return 
     * @throws Exception 
     */  
    public static String initMacKey() throws Exception {  
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);  
  
        SecretKey secretKey = keyGenerator.generateKey();  
        return encryptBASE64(secretKey.getEncoded());  
    }  
  
    /** 
     * HMAC加密 
     *  
     * @param data 
     * @param key 
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptHMAC(byte[] data, String key) throws Exception {  
  
        SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);  
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());  
        mac.init(secretKey);  
  
        return mac.doFinal(data);  
  
    }
    
    /**
     * 将二进制转换成16进制 
     * @param buf 
     * @return 
     */  
    public static String parseByte2HexStr(byte buf[]) {  
            StringBuffer sb = new StringBuffer();  
            for (int i = 0; i < buf.length; i++) {  
                    String hex = Integer.toHexString(buf[i] & 0xFF);  
                    if (hex.length() == 1) {  
                            hex = '0' + hex;  
                    }  
                    sb.append(hex.toUpperCase());  
            }  
            return sb.toString();  
    } 
    
    /**
     * 将16进制转换为二进制 
     * @param hexStr 
     * @return 
     */  
    public static byte[] parseHexStr2Byte(String hexStr) {  
            if (hexStr.length() < 1)  
                    return null;  
            byte[] result = new byte[hexStr.length()/2];  
            for (int i = 0;i< hexStr.length()/2; i++) {  
                    int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
                    int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
                    result[i] = (byte) (high * 16 + low);  
            }  
            return result;  
    }
    /**
     * md5加密密码
     * @param hexStr 
     * @return 
     */ 
    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        //确定计算方法
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        String pwd=str+MD5ADD;
        //加密后的字符串
        String newstr=base64en.encode(md5.digest(pwd.getBytes("utf-8")));
        return newstr;
    }
    /**时间格式化*/
    public static String formatTime(Date time){
    	DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(time);
    }
    /**时间格式化*/
    public static String formatDateStr(String datastr){
    	DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	DateFormat fmt2 =new SimpleDateFormat("yyyy-MM-dd");
        try {
			return fmt.format(fmt2.parse(datastr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return fmt.format(new Date());
    }
    /**与现在时间校验是否过时*/
    public static boolean checkTime(String systime,double overtime){
	 	double time;
	 	try {
	 		time=Double.valueOf(systime);
	 	} catch (Exception e) {
	 		e.printStackTrace();
	 		return false;
	 		
	 	}
	 
    	double nowtime=new Date().getTime();
    	if(nowtime<time+overtime){
    		return true;
    	}else{
    		return false;
    	}
    }
    /**cs查询1*/
    public static CoinPojo getCoinPojo(String year,String month){
		int mon=Integer.parseInt(month)+1;
		CoinPojo coinPojo=new CoinPojo();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.MONTH, Integer.parseInt(month));
		int end=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		coinPojo.setStartTime(year+"-"+mon+"-01");
		coinPojo.setEndTime(year+"-"+mon+"-"+end);
		return coinPojo;
		
	}
    /**cs查询2*/
    public static CoinPojo getCoinPojo(Date d){
		CoinPojo coinPojo=new CoinPojo();
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		cal.add(Calendar.DAY_OF_MONTH , 1);
		coinPojo.setStartTime(year+"-"+month+"-01");
		d=cal.getTime();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String endTime=dateFormat.format(d);
		coinPojo.setEndTime(endTime);
		return coinPojo;
		
	}
    /**查询日期转化*/
    public static Map<String, String> FirstandLastday(String datestr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM");
        Date date=null;
        Map<String, String> map = new HashMap<String, String>();
       
        if(datestr.split("-").length>2&&!datestr.split("-")[2].equals("0")){//查询到日
        	 try {
     			date = df.parse(datestr);
     		} catch (ParseException e) {
     			e.printStackTrace();
     		}
             Calendar calendar = Calendar.getInstance();
             calendar.setTime(date);
             calendar.add(Calendar.DATE, -1);
             Date theDate = calendar.getTime();
             String day_first = df.format(theDate);
        	try {
        		datestr=df.format(df.parse(datestr));
			} catch (Exception e) {
				// TODO: handle exception
			}
        	map.put("first", new StringBuffer().append(day_first).append(" 17:00:01").toString());
            map.put("last", new StringBuffer().append(datestr).append(" 17:00:00").toString());
        }else if(datestr.split("-").length>2&&!datestr.split("-")[1].equals("0")&&datestr.split("-")[2].equals("0")){//后台商户查询到月
        	try {
    			date = df2.parse(datestr);
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            calendar.set(Calendar.DATE, 1);        //设置为该月第一天
            calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
            Date theDate = calendar.getTime();

            GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
            gcLast.setTime(theDate);

            String day_first = df.format(gcLast.getTime());
            StringBuffer str = new StringBuffer().append(day_first).append(" 17:00:01");
            day_first = str.toString();

            calendar.add(Calendar.MONTH, 2);    //加一个月
            calendar.set(Calendar.DATE, 1);        //设置为该月第一天
            calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
            String day_last = df.format(calendar.getTime());
            StringBuffer endStr = new StringBuffer().append(day_last).append(" 17:00:00");
            day_last = endStr.toString();

            
            map.put("first", day_first);
            map.put("last", day_last);
        	
        }else if(datestr.split("-").length>1&&!datestr.split("-")[1].equals("0")){//个人流水查询到月
        	try {
    			date = df2.parse(datestr);
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Date theDate = calendar.getTime();

            GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
            gcLast.setTime(theDate);
            gcLast.set(Calendar.DAY_OF_MONTH, 1);
            String day_first = df.format(gcLast.getTime());
            StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
            day_first = str.toString();

            calendar.add(Calendar.MONTH, 1);    //加一个月
            calendar.set(Calendar.DATE, 1);        //设置为该月第一天
            calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
            String day_last = df.format(calendar.getTime());
            StringBuffer endStr = new StringBuffer().append(day_last).append(" 23:59:59");
            day_last = endStr.toString();

            
            map.put("first", day_first);
            map.put("last", day_last);
        }
		
        return map;
    }
    /**小数强转两位小数*/
    public static String getTwoDec(String douval){
    	if(douval==null||douval.equals("")){
    		return "0.00";
    	}else{
    		DecimalFormat df   = new DecimalFormat("#0.00");
        	double val=Double.valueOf(douval);
        	return df.format(val).toString();
    	}
    	
    	
    }
    /**生成一定长度的数字串*/
    public static String getResetPwd(int numsize){
    	StringBuffer value = new StringBuffer();
    	Random random = new Random();
    	for(int i=0;i<numsize;i++){
    		value.append(random.nextInt(9));
    	}
    	return value.toString();
    	
    }
    /**判断字符串能否转换成int*/
    public static boolean isNumeric(String str){
    	if(str==null||str.trim().equals("")){
    		return false;
    	}
    	try {  
    	    Integer.parseInt(str);  
    	    return true;  
    	} catch (NumberFormatException e) {  
    	    return false;  
    	} 

     } 
    
    /**起时间格式化*/
    public static String formatStartDay(String datestr){
    	DateFormat fmt2 =new SimpleDateFormat("yyyy-MM-dd");
    	Date date=null;
    	if(datestr==null||datestr.trim().equals("")){
    		return "";
    	}else{
    		try {
    			date = fmt2.parse(datestr);
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
        	Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
            Date theDate = calendar.getTime();

            GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
            gcLast.setTime(theDate);
            String day_first = fmt2.format(gcLast.getTime());
            StringBuffer str = new StringBuffer().append(day_first).append(" 17:00:01");
            return str.toString();
    	}
    	
    }
    /**止时间格式化*/
    public static String formatEndDay(String datestr){
    	if(datestr==null||datestr.trim().equals("")){
    		return "";
    	}else{
    		StringBuffer str = new StringBuffer().append(datestr).append(" 17:00:00");
    		return str.toString();
    	}
        
        
    }
    
    
    public static void main(String[] args) {
		
		
	}
}  