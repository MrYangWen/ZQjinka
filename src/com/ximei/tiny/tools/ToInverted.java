package com.ximei.tiny.tools;
public class ToInverted {
	
public String toinverted(String str){
		
		int len = str.length();
	    String strmsg=""; 
	    
	    try{
	    if(len%2==0)
	    {
	    	
		   for(int i =0;i<len ;i+=2)
		  {   
			String str1 = str.substring(len-2-i, len-i);
			strmsg= strmsg+str1;
			
		   }
        }
	    }catch (Exception e)
	    {
	    	System.out.println("格式错误");
	    }
		
		return strmsg;
	}

}
