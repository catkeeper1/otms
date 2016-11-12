package com.ckr.otms.common.util;

public class LongUtil {
	public static Long parse(String str){
		
		try{
			return new Long(str);
		}catch(NumberFormatException e){
			return null;
		}
		
	}
}
