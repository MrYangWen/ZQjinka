package com.ximei.tiny.tools;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * 金卡协议-加解密、CRC校验、异或校验
 * @author 杨文
 * date 2019-4-13
 */
public class JinKaAgreement {
	
	final String[] key= {"6A","4A","6A","4B","95","B5","95","B4"};

	/**
	 * 验证CRC校验的方法
	 * @param  crcData1  数据
	 * @param  crcData2  crc寄存器
	 * @return 返回CRC校验结果
	 */
	public int CRCJY(int crcData,int crcReg) {
		
		int  i;
	    for (i = 0; i < 8; i++) {
	    	int jg = ((crcReg & 0x8000) >> 8) ^ (crcData & 0x80);
	        if (jg!=0) {
	        	 crcReg = (char) ((crcReg << 1) ^ 0x8005);
	        }
	        else {
	        	crcReg = (char) (crcReg << 1);
	        }
	        crcData <<= 1;
	    }
	    return crcReg;
	}
	/**
	 * 金卡校验CRC是否正确的方法
	 * @param data 需要验证CRC的数据
	 * @return 返回结果Success为正确，error为错误
	 */
	@SuppressLint("DefaultLocale")
	public String getCRC(String data) {
		int len = data.length();//获取数据长度
		int[] datai = new int[(len-6)/2];//初始化数据数组
		for(int i=0,j=0;i<len-6;i+=2,j++) {//将数据保存的int数组
			datai[j] = Integer.valueOf(data.substring(i,i+2),16);
		}
		int checksum =0xFFFF;
		for(int i=0;i<datai.length;i++) {
			checksum = CRCJY(datai[i], checksum);
		}
		String crc1 = Integer.toHexString(checksum).toUpperCase();
		String crc2 = data.substring(len-6, len-2).toUpperCase();
		Log.e("test", crc1+"：本地校验CRC<======>原始CRC:"+crc2);
		if(crc1.equals(crc2)) {
			return "Success";
		}
		return "error";
	}
	/**
	 * 得到数据的CRC校验码
	 * @param data
	 * @return
	 */
	public String getCrcjy(String data) {
		int len = data.length();//获取数据长度
		int[] datai = new int[(len)/2];//初始化数据数组
		for(int i=0,j=0;i<len;i+=2,j++) {//将数据保存的int数组
			datai[j] = Integer.valueOf(data.substring(i,i+2),16);
		}
		int checksum =0xFFFF;
		for(int i=0;i<datai.length;i++) {
			checksum = CRCJY(datai[i], checksum);
		}
		String crc1 = Integer.toHexString(checksum).toUpperCase();
		return crc1;
	}
	
	/**
	 * 数据加解密
	 * @param data 需要解密的数据
	 * @return
	 */
	public String decrypt(String data) {
		String datastart= data.substring(0,30);//截取数据域前面的数据
		String dataend = data.substring(data.length()-6);//截取数据域后面的数据
		String datamiddle = "";//解密后的数据域
		for(int i=30;i<data.length()-6;i+=2) {
			String yh = TypeConvert.yihuo(TypeConvert.hexStrTo2Str(data.substring(i,i+2)),TypeConvert.hexStrTo2Str(key[(i/2)%8]));
			datamiddle+=TypeConvert.strTohexStr(yh);
		}
		return datastart+datamiddle+dataend;
	}
}
