package com.ximei.tiny.tools;

import java.util.ArrayList;

public class SendQbbh {

	String msg = "";
	GetmsgID getmsg = new GetmsgID();

	public String[] toarrayqbbh(ArrayList<String> qbbhlist, int total) {
        
		String[] qbbharray;
		int cishu = qbbhlist.size() / total;
		
		if(qbbhlist.size()%total!=0){
		 qbbharray = new String[cishu+1];
		 
		 for (int i = 0; i < cishu + 1; i++) {

				if (i == cishu) {
					for (int j = i * total; j < qbbhlist.size(); j++) {

						msg += getmsg.getMsgID(Integer.toHexString(Integer.parseInt(qbbhlist.get(j))));

					}

				} else {

					for (int j = i * total; j < (i+1) * total; j++) {
						msg += getmsg.getMsgID(Integer.toHexString(Integer.parseInt(qbbhlist.get(j))));

					}

				}
				
				qbbharray[i]=msg;
				
				msg="";

			}
		 
		 
		}else{
			 qbbharray = new String[cishu];
			 
			 for (int i = 0; i < cishu; i++) {

					

						for (int j = i * total; j < (i+1) * total; j++) {
							msg += getmsg.getMsgID(Integer.toHexString(Integer.parseInt(qbbhlist.get(j))));

						}

					
					
					qbbharray[i]=msg;
					
					msg="";

				}
			
		}
		

		return qbbharray;

	}

}