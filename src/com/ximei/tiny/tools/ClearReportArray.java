package com.ximei.tiny.tools;
import java.util.ArrayList;

public class ClearReportArray {
	
	
	
	public  ArrayList<String> ClearReport(String[] num){
		
		ArrayList<String> list = new ArrayList<String>();
         for (int i = 0; i < num.length; i++) {
             if (!list.contains(num[i])) {
                 list.add(num[i]); 
             }
         }
		
		
		
		return list;
	}

}
