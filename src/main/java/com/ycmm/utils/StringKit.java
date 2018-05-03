package com.ycmm.utils;

import java.util.Arrays;

public class StringKit {

	public static String arrayToString(String[] strArray) {
		
		if(strArray == null || strArray.length < 1) {
			return "";
		}
		
		String s = Arrays.toString(strArray).replaceFirst("\\[", "");
		
		if(s == null || s.trim().length() == 0) {
			return "";
		}
		
		return s.substring(0, s.length() - 1);
	}
	
	
	public static void main(String[] args) {
		String[] s = new String[]{"sds", "dd", "aaa"};
		String string = arrayToString(s);
		System.out.println(string);
	}
}
