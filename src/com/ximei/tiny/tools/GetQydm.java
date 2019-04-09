package com.ximei.tiny.tools;

public class GetQydm {
	
	
	public  String getkey(String order,String len,String crc,String qbbh ){
		int key_code1=0;
		int key_code0=0;
		 key_code1 = 0XFF;
		 key_code1 ^= 0XFE;
		 key_code1 ^= 0X01;
		 key_code1 ^= Integer.parseInt(crc, 16);
		 key_code1 ^= Integer.parseInt(qbbh.substring(0, 2), 16);       //注：tst[0]~tst[2] 表示 表地址低中高 三个字节 

		 key_code0 = 0XFF;
		 key_code0 ^= Integer.parseInt(qbbh.substring(2, 4), 16);
		 key_code0 ^= Integer.parseInt(qbbh.substring(4, 6), 16);
		 key_code0 ^= Integer.parseInt(order, 16);       //发送命令字
		 key_code0 ^= Integer.parseInt(len, 16);//数据长度
		 
		 
	     return Integer.toHexString( key_code0)+Integer.toHexString( key_code1);
	}
	
	

}
