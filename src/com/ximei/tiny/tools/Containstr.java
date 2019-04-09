package com.ximei.tiny.tools;


public class Containstr {
	
	 public  boolean isHave(String[] strs,String s){
		  
		  for(int i=0;i<strs.length;i++){
		   if(strs[i].indexOf(s)!=-1){
			   
		    return true;
		    
		   }
		  }
		  return false;
		 }
	
}

