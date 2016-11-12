package com.ckr.otms.common.util;

public class IntegerUtil {
	public static Integer parse(String str){
		
		try{
			return new Integer(str);
		}catch(NumberFormatException e){
			return null;
		}
		
	}
}
