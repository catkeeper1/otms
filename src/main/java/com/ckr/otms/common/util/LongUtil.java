package com.ckr.otms.common.util;

public class LongUtil {
	public static Long parse(String str){
		
		try{
			return Long.valueOf(str);
		}catch(NumberFormatException e){
			return null;
		}
		
	}
}
