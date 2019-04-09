package com.ximei.tiny.tools;

public class FirstSubDong {
	
	 public String FirstQueryDong(String paramString1, String paramString2)
	  {
	    int i = paramString1.indexOf(paramString2);
	    int j = i - 1;
	    try
	    {
	      String str1 = paramString1.substring(j, i);
	      String str2 = paramString1.substring(i - 2, i);
	      String str5 = paramString1.substring(i - 2, j);
	      int k = Integer.parseInt(str1);
	      int m = k;
	      try
	      {
	        m = Math.abs(Integer.parseInt(str2));
	        if (m < 10)
	          return "0" + String.valueOf(m);
	        String str4 = String.valueOf(m);
	        return str4;
	      }
	      catch (NumberFormatException e)
	      {
	    	  try{
	     		 Integer.parseInt(str5, 16);
	     		 return str5+String.valueOf(m);
	     	 }catch(NumberFormatException e1){
	     		 
	     		 String str3 = "0" + String.valueOf(m);
	             return str3;
	     		 
	     	 }
	      }
	    }
	    catch (Exception localException)
	    {
	    }
	    return paramString1.substring(i - 1, i);
	  }

}
