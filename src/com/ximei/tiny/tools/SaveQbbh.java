package com.ximei.tiny.tools;


public class SaveQbbh {
	
	

	public static String[] writeqbbh(String str) {
		
		GetmsgID getmsg = new GetmsgID();
		

		int qbcount = str.length() / 6;
		String[] saveqbbhlist = new String[qbcount];

		for (int i = 0; i < qbcount; i++) {

			String qbbhhex = str.substring(6 * i, 6 * i + 6);
          
			String qbbhmsg = String.valueOf(Integer.parseInt(
					getmsg.getMsgID(qbbhhex), 16));
			
			saveqbbhlist[i]=qbbhmsg;

		}

		return saveqbbhlist;

	}

}
