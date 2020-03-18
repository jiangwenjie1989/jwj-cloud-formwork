package com.cloud.common.redis;

import com.cloud.common.constants.RedisCacheKeys;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RedisNumberCreate {

	@Autowired
	private RedisCacheSupport<String> cache;
	
	/**
	 * 获取用户编号
	 * @return
	 */
	public String getUserNumber(){
		
		long number=cache.sCard(RedisCacheKeys.USER_NUMBER_POOL);
		 if(number<=0){
			String maxNum=cache.getCached(RedisCacheKeys.USER_NUMBER_MAX);
			if(maxNum==null){
				maxNum="100000";
			}
			int start=Integer.valueOf(maxNum);
			int end=start+5000;
			cache.put(RedisCacheKeys.USER_NUMBER_MAX, end+"");
			List<String> ss=Lists.newArrayList();
			List<String> s=Lists.newArrayList();
			for (int i = start; i < end; i++) {
				//12345678  87654321 66666666  88888888 99999999
//				if(MobileTool.matcher(String.valueOf(i), "(\\d)\\1{2}")){
//					ss.add(i+"");
//				}else{
//				s.add(i+"");
//				}
				s.add(i+"");
			}
			cache.sAdd(RedisCacheKeys.USER_NUMBER_POOL, s.toArray());
		 }
		 Object userNumber=cache.sPop(RedisCacheKeys.USER_NUMBER_POOL);
		 return userNumber==null?"":userNumber.toString();
	}
}
