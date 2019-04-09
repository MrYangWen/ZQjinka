package com.ximei.tiny.tools;

public class StrToHex {
	
	public static String ToHexString(String msg){
		int revlueint=0;
		
		for(int i=0;i<msg.length()/2;i++){
			
			String hexstr=msg.substring(i*2, (i+1)*2);
			int hexint=Integer.parseInt(hexstr, 16);
			revlueint+=hexint;
		}
		System.out.println(revlueint);
		int key=revlueint%256;
		String returnmsg=String.valueOf(Integer.toHexString(key));
		if(returnmsg.length()==1){
			
			returnmsg="0"+returnmsg;
		}
		
		return returnmsg.toUpperCase();
	}
	
	

	

}
