package com.ximei.tiny.tools;

public class ToHexStr {
	
    public String toHexStr(String str){
		
		int len = str.length();
	    String strmsg=""; 
		for(int i =0;i<len ;i+=2)
		{
			String str1 = str.substring(i, i+2);
			try{
			int hexint1 = Integer.parseInt(str1);
			String hexstr1 = Integer.toHexString(hexint1);
			if(hexstr1.length()<2){
				hexstr1="0"+hexstr1;
			}
			strmsg= strmsg+hexstr1;
			}catch(NumberFormatException e ){
				System.out.println("转换错误");
			}
		}
		
		
		return strmsg.toUpperCase();
	}

}