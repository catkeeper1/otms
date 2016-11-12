package com.ckr.otms.common.util;

public class StringUtil {
	public static boolean isNull(String str){
		
		if(str == null){
			return true;
		}
		
		if(str.length() == 0){
			return true;
		}
		
		return false;
		
	}
}
