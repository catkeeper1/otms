package com.ckr.otms.common.util;

public class IntegerUtil {
	public static Integer parse(String str){
		
		try{
			return Integer.valueOf(str);
		}catch(NumberFormatException e){
			return null;
		}
		
	}
}
