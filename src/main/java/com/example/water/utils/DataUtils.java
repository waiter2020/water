package com.example.water.utils;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtils {
	public static String createJsonString(Object value)
	{
		Gson gson = new Gson();
		String gString = gson.toJson(value);
		return gString;
	}
	
	public static boolean isNumeric(String str)
    {
           Pattern pattern = Pattern.compile("[0-9]*");
          Matcher isNum = pattern.matcher(str);
          if( !isNum.matches() )
          {
                return false;
          }
          return true;
    }
	
	public static long getTimeInMillis(int year,int month,int day,int hour,int minute,int second){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hour, minute, second);
		return cal.getTimeInMillis();
	}
	
	public static String getTimeString(){
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		StringBuffer sb = new StringBuffer();
		sb.append(year);
		sb.append(String.format("%02d", month));
		sb.append(String.format("%02d", day));
		sb.append(String.format("%02d", hour));
		sb.append(String.format("%02d", minute));
		sb.append(String.format("%02d", second));
		return sb.toString();
		
	}
	
	

}
