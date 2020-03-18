package com.cloud.common.utils;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;


public class ApiSign {

	private static final Logger logger = LoggerFactory.getLogger(ApiSign.class);
	
	/**
	 * 签名生成算法
	 * @param HashMap<String,String> params 请求参数集，所有参数必须已转换为字符串类型
	 * @param String secret 签名密钥
	 * @return 签名
	 * @throws IOException
	 */
	public static String getSignature(Map<String, String[]> reqParas,String channelType,String salt,String requestId) throws IOException{
		Map<String,String> params=getParameterMap(reqParas);
		params.put("channelType", channelType);
		params.put("salt", salt);
		params.put("requestId", requestId);
		
		// 先将参数以其参数名的字典序升序进行排序
	    Map<String, String> sortedParams = new TreeMap<String, String>(params);
	    Set<Entry<String, String>> entrys = sortedParams.entrySet();
	 
	    // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
	    StringBuilder basestring = new StringBuilder();
	    for (Entry<String, String> param : entrys) {
	    	String paramV = URLEncoder.encode(param.getValue(), "UTF-8");
	    	String value = URLDecoder.decode(paramV, "UTF-8");
//	    	if("iOS".equals(channelType)){
//	    		value=value.replaceAll("\\+", "%20");
//	    	} 
	        basestring.append(param.getKey()).append("=").append(value).append("&");
	    }
	    
	    //将请求参数安装英文字母升序拼接后md5得到字符串
		String stringMd5 = Md5Utils.MD5(basestring.toString());
		return stringMd5;
	}
	
	
	public static Map<String,String> getParameterMap(Map<String, String[]> reqParas) {
	    // 返回值Map
		Map<String,String> returnMap = Maps.newHashMap();
	    Iterator<Entry<String, String[]>> entries = reqParas.entrySet().iterator();
	    Entry<String, String[]> entry;
	    
	    String name = "";
	    String value = "";
	    while (entries.hasNext()) {
	        entry = entries.next();
	        name = (String) entry.getKey();
	        Object valueObj = entry.getValue();
	        if(null == valueObj){
	            value = "";
	        }else if(valueObj instanceof String[]){
	            String[] values = (String[])valueObj;
	            for(int i=0;i<values.length;i++){
	                value += values[i] + ",";
	            }
	            value = value.substring(0, value.length()-1);
	        }else{
	            value = valueObj.toString();
	        }
	        returnMap.put(name, value);
	        name = "";
		    value = "";
	    }
	    return returnMap;
	}
	
	/** 
    * 生成随机字符串 
    * @param length  字符串长度 
    * @return 
    */  
   public static String getRandomString(int length) { //length表示生成字符串的长度    
       String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";       
       Random random = new Random();       
       StringBuffer sb = new StringBuffer();       
       for (int i = 0; i < length; i++) {       
           int number = random.nextInt(base.length());       
           sb.append(base.charAt(number));       
       }       
       return sb.toString();       
    }   
	
}
