package com.ximei.tiny.tools;

import com.ximei.tiny.service.BtXiMeiService;

public class GetmsgID {
	
	private String Msgnub;
	public String getMsgID(String MsgID){
		
		
		
		if(MsgID.length()==6)
		{
			
			String msgsub1=MsgID.substring(0, 2);
			String msgsub2=MsgID.substring(2, 4);
			String msgsub3=MsgID.substring(4, 6);
		    Msgnub=msgsub3+msgsub2+msgsub1;

			
		}
		if(MsgID.length()==5)
		{
			
			String msgsub1=MsgID.substring(0, 1);
			String msgsub2=MsgID.substring(1, 3);
			String msgsub3=MsgID.substring(3, 5);
			Msgnub=msgsub3+msgsub2+"0"+msgsub1;
			
           
		}
		if(MsgID.length()==4)
		{
			String msgsub2=MsgID.substring(0, 2);
			String msgsub3=MsgID.substring(2, 4);
	        Msgnub=msgsub3+msgsub2+"00";
		}
		if(MsgID.length()==3)
		{
			
			String msgsub2=MsgID.substring(0, 1);
			String msgsub3=MsgID.substring(1, 3);
			 Msgnub=msgsub3+"0"+msgsub2+"00";	
		}
		if(MsgID.length()==2)
		{	
			String msgsub3=MsgID.substring(0, 2);
			 Msgnub=msgsub3+"0000";
		}
		if(MsgID.length()==1)
		{
			
			
			String msgsub3=MsgID.substring(0, 1);
			 Msgnub="0"+msgsub3+"0000";
		}		
		return Msgnub.toUpperCase();
	}     
	public String GetMeterAddr(String InputInfo)
	{
		try{
		if(InputInfo.length()==14){
			
			Msgnub=InputInfo;
			String AddrStr = new IniReader("SysSet.ini", null).getValue("FreqSet", "AddrSection");
			if(Integer.parseInt(Msgnub.substring(0, 8))<Integer.parseInt(AddrStr))  //24951703之前的为8位地址
			{
				int id=0;
				  id=Integer.parseInt(InputInfo.substring(6,14));
				if (id > 0 && id < 16777215){
					String str=String.format("%06X",id);
					Msgnub=str.substring(4, 6)+str.substring(2, 4)+str.substring(0, 2);						
				}else{
					return "00";
				}				
				
			}
			
		}else{
			
			int id=0;
			  id=Integer.parseInt(InputInfo);
			if (id >= 0 && id < 16777215){
				String str=String.format("%06X",id);
				Msgnub=str.substring(4, 6)+str.substring(2, 4)+str.substring(0, 2);						
			}else{
				return "00";
			}
		}
		}catch(Exception e){
			e.toString();
			return "00";
		}
		return Msgnub.toUpperCase();
	}		
	public String CheckMeterID(String strTmp)
	{
		//String strTmp;
	    int [] aArray;
	    int iLen, i;
	    aArray = new int [15];
	    int p = 10;
	    int numerator;
	    int checkCode;
	    if(strTmp.length()!=15)
	       return null;
	    //strTmp = "122245875956129";
	    iLen = strTmp.length();
	    aArray[0] = 0;
	    for(i = 0; i < iLen; i++) {
	      String sNum = strTmp.substring(i, i + 1);
	      aArray[i] = Integer.parseInt(sNum);
	    }
	    for(i = 0; i < 14; i++) {
	      numerator = (p + (aArray[i])) % 10;
	      if(numerator == 0) {
	        numerator = 10;
	      }
	      p = (numerator * 2) % 11;
	    }
	    checkCode = 11 - p;
	    if(checkCode > 9) {
	      checkCode = 0;
	    }
	    if(checkCode==Integer.parseInt(strTmp.substring(14, 15)))
	    {
	    	
	    	return strTmp.substring(0,14);
	    }
	    else 
	    {
			return null;
		}
	    
	   // System.out.println(checkCode);
	  }
}
