package com.ximei.tiny.tools;

public class GetTotalPack {

	private String Msgnub;

	public String gettotalpack(String MsgID, int flag) {
		
		
		 if(flag == 8){
			 
			 if(MsgID.length()==8)
				{
					
					String msgsub1=MsgID.substring(0, 2);
					String msgsub2=MsgID.substring(2, 4);
					String msgsub3=MsgID.substring(4, 6);
					String msgsub4=MsgID.substring(6, 8);
				    Msgnub=msgsub4+msgsub3+msgsub2+msgsub1;

					
				}
				if(MsgID.length()==7)
				{
					
					String msgsub1=MsgID.substring(0, 1);
					String msgsub2=MsgID.substring(1, 3);
					String msgsub3=MsgID.substring(3, 5);
					String msgsub4=MsgID.substring(4, 7);
				    Msgnub=msgsub4+msgsub3+msgsub2+0+msgsub1;

					
				}
				
				if(MsgID.length()==6)
				{
					
					String msgsub1=MsgID.substring(0, 2);
					String msgsub2=MsgID.substring(2, 4);
					String msgsub3=MsgID.substring(4, 6);
				    Msgnub=msgsub3+msgsub2+msgsub1+00;

					
				}
				if(MsgID.length()==5)
				{
					
					String msgsub1=MsgID.substring(0, 1);
					String msgsub2=MsgID.substring(1, 3);
					String msgsub3=MsgID.substring(3, 5);
					Msgnub=msgsub3+msgsub2+"0"+msgsub1+00;
					
		           
				}
				if(MsgID.length()==4)
				{
					
					String msgsub2=MsgID.substring(0, 2);
					String msgsub3=MsgID.substring(2, 4);
			        Msgnub=msgsub3+msgsub2+"0000";
					
				}
				if(MsgID.length()==3)
				{
					
					String msgsub2=MsgID.substring(0, 1);
					String msgsub3=MsgID.substring(1, 3);
					 Msgnub=msgsub3+"0"+msgsub2+"0000";
					
					
				}
				if(MsgID.length()==2)
				{
					
					
					String msgsub3=MsgID.substring(0, 2);
					 Msgnub=msgsub3+"000000";
				}
				if(MsgID.length()==1)
				{
					
					
					String msgsub3=MsgID.substring(0, 1);
					 Msgnub="0"+msgsub3+"000000";
				}
				
				
				
				
			}
		
        
	    if(flag == 6){
			
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
			
			
			
			
		}
		
		
		if (flag == 4) {

			if (MsgID.length() == 4) {

				String msgsub1 = MsgID.substring(0, 2);
				String msgsub2 = MsgID.substring(2, 4);

				Msgnub = msgsub2 + msgsub1;

			}
			if (MsgID.length() == 3) {

				String msgsub1 = MsgID.substring(0, 1);
				String msgsub2 = MsgID.substring(1, 3);

				Msgnub = msgsub2 + "0" + msgsub1;

			}
			if (MsgID.length() == 2) {

				String msgsub2 = MsgID.substring(0, 2);

				Msgnub = msgsub2 + "00";

			}

			if (MsgID.length() == 1) {

				String msgsub3 = MsgID.substring(0, 1);
				Msgnub = "0" + msgsub3 + "00";
			}

		}
		
		if(flag==2){
			
			
			if(MsgID.length()==2){
			   Msgnub=MsgID;
			
			}
			if(MsgID.length()==1){
				String msgsub3 = MsgID.substring(0, 1);
				Msgnub = "0" + msgsub3;
				
			}
			
		}

		return Msgnub;

	}

}

