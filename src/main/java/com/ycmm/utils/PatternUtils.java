package com.ycmm.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {

	/**
	 * 
	 * @param regex
	 * @param src
	 * @param match
	 *              true为全匹配，false为包含
	 * @return    
	 */ 
	public static boolean regex(String regex, String src, boolean match) {
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(src);
		
		if(match) {
			return matcher.matches();
		}
		
		return matcher.find();
	}
}
