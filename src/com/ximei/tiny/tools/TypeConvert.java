package com.ximei.tiny.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;

import com.tiny.gasxm.R;
import com.ximei.tiny.chaobiao.SingleCBActivity;
import com.ximei.tiny.service.BtXiMeiService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;


@SuppressLint("DefaultLocale")
public class TypeConvert{
	
	private static String key ="01020304050607080000000000000000";
	
	/**
	 * hex字符串转二进制字符串
	 */
	public static String hexStrTo2Str(String Str) {
		String S = "";
		Str = Str.toUpperCase();
		for(int i =0;i<Str.length();i++) {
			switch(Str.charAt(i)) {
			case '0':S+="0000";break;
			case '1':S+="0001";break;
			case '2':S+="0010";break;
			case '3':S+="0011";break;
			case '4':S+="0100";break;
			case '5':S+="0101";break;
			case '6':S+="0110";break;
			case '7':S+="0111";break;
			case '8':S+="1000";break;
			case '9':S+="1001";break;
			case 'A':S+="1010";break;
			case 'B':S+="1011";break;
			case 'C':S+="1100";break;
			case 'D':S+="1101";break;
			case 'E':S+="1110";break;
			case 'F':S+="1111";break;
			}
		}
		return S;
	}
	/**
	 * 二进制字符串转hex字符串
	 */
	@SuppressLint("DefaultLocale")
	public static String strTohexStr(String Str) {
		String S = "";
		for(int i =0;i<Str.length();i+=4) {
			switch(Str.substring(i, i+4)) {
			case "0000":S+="0";break;
			case "0001":S+="1";break;
			case "0010":S+="2";break;
			case "0011":S+="3";break;
			case "0100":S+="4";break;
			case "0101":S+="5";break;
			case "0110":S+="6";break;
			case "0111":S+="7";break;
			case "1000":S+="8";break;
			case "1001":S+="9";break;
			case "1010":S+="A";break;
			case "1011":S+="B";break;
			case "1100":S+="C";break;
			case "1101":S+="D";break;
			case "1110":S+="E";break;
			case "1111":S+="F";break;
			}
		}
		return S;
	}
    /**
     * 字符串转换成十六进制字符串
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }
    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    /**
     * 数组转换成十六进制字符串
     * @param bArray byte[]
     * @return HexString
     */
    public static final String bytesToHexString(byte[] bArray) {
        if (bArray == null || bArray.length==0){
            return null;
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2){
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
    /**
     * 十六进制字符串转换成字符串
     * @param hexStr
     * @return String
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
    
    /**
     * 字符串转化成为16进制字符串
     * @param s
     * @return
     */
    public static String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }
    
    
    
    /**
     * hex字符串转int
     * @param hexString String str = "000AB"
     * @return
     */
    public static int hexString2Int(String hexString){
        Integer num = Integer.valueOf(hexString,16);
        return num;
    }
    /**
     * 把byte转为字符串的bit
     */
    public static String byteToBitString(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }
    /**
     * 把byte转为字符串数组的bit
     */
    public static String[] byteToBitStrings(byte b) {
        String[] bit = new String[8];
        bit[0] = ""+ (byte) ((b >> 7) & 0x1);
        bit[1] = ""+ (byte) ((b >> 6) & 0x1);
        bit[2] = ""+ (byte) ((b >> 5) & 0x1);
        bit[3] = ""+ (byte) ((b >> 4) & 0x1);
        bit[4] = ""+ (byte) ((b >> 3) & 0x1);
        bit[5] = ""+ (byte) ((b >> 2) & 0x1);
        bit[6] = ""+ (byte) ((b >> 1) & 0x1);
        bit[7] = ""+ (byte) ((b >> 0) & 0x1);
        return bit;
    }
    public static void main(String[] args){
        String hexString = "3A60432A5C01211F291E0F4E0C132825";
        byte[] result = hexStringToBytes(hexString);
        System.out.println(new String(result));
        System.out.println(bytesToHexString(result));
    }
    //base64字符串转byte[]
    public static byte[] base64String2ByteFun(String base64Str){
        return Base64.decodeBase64(base64Str);
    }
    //byte[]转base64
    public static String byte2Base64StringFun(byte[] b){
        return Base64.encodeBase64String(b);
    }
    /**
     * 数据异或校验
     */
    public static String yihuo(String msgA,String msgB) {
    	String result= "";
    	for(int i = 0;i<msgA.length();i++) {
    		if(msgA.charAt(i)==msgB.charAt(i)) {
    			result+="0";
    		}else {
    			result+="1";
    		}
    	}
    	return result;
    }
    /**
     * 数据异或
     */
    public static String yiHuo(String msg) {
    	String result ="";
    	try {
    		String[] yhsj=new String[msg.length()/2];
    		for(int i=0;i<msg.length();i+=2) {
    			yhsj[i/2] = msg.substring(i, i+2);
    		}
    		for(int i=0;i<yhsj.length-1;i++) {
    			if(i==0) {
    				String a = "00";
    				String b = yhsj[i];
    				result = yihuo(hexStrTo2Str(a),hexStrTo2Str(b));
    			}
    			else {
    				String a = yhsj[i];
    				result = yihuo(result,hexStrTo2Str(a));
    			}
    		}
    	}catch(Exception e) {
    		Log.e("error",""+e.getMessage());
    		Log.e("error",""+e.toString());
    	}
    	result = strTohexStr(result);
    	//Log.e("异或",""+result);
    	return result;
    }
    /**
     * 数据补码
     */
    public static String sjbm(String msg) {
    	String result=msg;
    	if(result.length()%32!=0) {
    		result+="80";
			while(result.length()%32!=0) {
				result+="00";
			}
		}
    	return result;
    }
    /**
     * int转hex字符串
     */
    public static String intToHex(int num) {
        //StringBuffer s = new StringBuffer();
    	if(num == 0) {
    		return "00";
    	}
        StringBuilder sb = new StringBuilder(8);
        String result;
        char []b = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        while(num != 0){
            sb = sb.append(b[num%16]);
            num = num/16;            
        }
        result = sb.reverse().toString();
        if(result.length()%2!=0) {
        	result ="0"+result;
        }
        return result;
    }
    /**
     * 获取hex时间
     */
    @SuppressLint("SimpleDateFormat")
	public static String getHexTime() {
    	String result="";
    	try {
			//获取时间
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
			SimpleDateFormat sdf3 = new SimpleDateFormat("HH");
			SimpleDateFormat sdf4 = new SimpleDateFormat("mm");
			SimpleDateFormat sdf5 = new SimpleDateFormat("ss");
			result +=intToHex(Integer.parseInt(sdf.format(date).substring(0, 2)));
			result +=intToHex(Integer.parseInt(sdf.format(date).substring(2)));
			result +=intToHex(Integer.parseInt(sdf1.format(date)));
			result +=intToHex(Integer.parseInt(sdf2.format(date)));
			result +=intToHex(Integer.parseInt(sdf3.format(date)));
			result +=intToHex(Integer.parseInt(sdf4.format(date)));
			result +=intToHex(Integer.parseInt(sdf5.format(date)));
		}catch(Exception e){
			Log.e("error","获取hex时间报错"+e.getMessage());
		}
    	return result;
    }
    /**
     * 传入表号和功能码，返回数据帧
     */
    public static String getMsg(String bh,String gongnm,String...strings) {
    	String result="";
    	//明文
    	String AESmsg = bh+gongnm;
    	//头部
    	String headmsg ="68"+"01"+TypeConvert.intToHex(AESmsg.length()/2+2);
    	//数据校验
    	String yhmsg = headmsg+AESmsg;
    	String yh =yiHuo(yhmsg);
    	AESmsg = AESmsg + yh;
    	//明文补码
    	AESmsg =sjbm(AESmsg);
		//加密
		try {
			AESmsg = AESUtil.encrypt(AESmsg,key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		headmsg = "68"+"01"+TypeConvert.intToHex(AESmsg.length()/2+1);
		if(strings.length>0) {
			headmsg = "68"+strings[0]+"01"+TypeConvert.intToHex(AESmsg.length()/2+1);
		}
		result = headmsg+AESmsg+"16";
    	return result;
    }
    /**
     * 传入表号，数据和功能码，返回数据帧
     */
    public static String getMsg2(String bh,String data,String gongnm,String...strings) {
    	String result="";
    	//明文
    	String AESmsg = bh+gongnm+data;
    	//头部
    	String headmsg ="68"+"01"+TypeConvert.intToHex(AESmsg.length()/2+2);
    	//数据校验
    	String yhmsg = headmsg+AESmsg;
    	String yh =yiHuo(yhmsg);
    	AESmsg = AESmsg + yh;
    	//明文补码
    	AESmsg =sjbm(AESmsg);
		//加密
		try {
			AESmsg = AESUtil.encrypt(AESmsg,key);
			Log.e("test", AESmsg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		headmsg = "68"+"01"+TypeConvert.intToHex(AESmsg.length()/2+1);
		if(strings.length>0) {
			headmsg = "68"+strings[0]+"01"+TypeConvert.intToHex(AESmsg.length()/2+1);
		}
		result = headmsg+AESmsg+"16";
    	return result;
    }
    /**
     * 去填充函数
     * @param oldMsg 传入填充数据
     * @return 返回去填充数据
     */
    public static String getNewMsg(String oldMsg) {
    	String newMsg = oldMsg;
    	try {
    		if(oldMsg.lastIndexOf("80")!=-1) {
    			//判断是不是最后面
    			if(oldMsg.substring(oldMsg.length()-2)!="80") {
    				String str = oldMsg.substring(oldMsg.lastIndexOf("80")+1);
        			for(int i=0;i<str.length();i++) {
        				if(str.charAt(i)!='0') {
        					return newMsg;
        				}
        			}
            		newMsg = oldMsg.substring(0,oldMsg.lastIndexOf("80"));
    			}else {
    				newMsg = oldMsg.substring(0,oldMsg.length()-2);
    			}
        	}
    	}catch(Exception e) {
    		Log.e("test","去填充函数error:"+e.toString());
    	}
    	
    	return newMsg;
    }
    
    public static boolean getYh(String backmsg) {
    	//提取数据开头
		String headstr = backmsg.substring(0, 6);
		//提取数据结尾
		String endstr = backmsg.substring((backmsg.length())-2);
		//提取加密数据
		String backaesmsg = backmsg.substring(6,(backmsg.length())-2);
		//解密数据
		try {
			backaesmsg = AESUtil.decrypt(backaesmsg,key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//获取功能码
		String sendorder = backaesmsg.substring(10,12);
		//去填充数据
		String newMsg = TypeConvert.getNewMsg(backaesmsg);
		Log.e("test","去填充数据:"+newMsg);
		//提取异或码
		String yhMsg = newMsg.substring(newMsg.length()-2);
		Log.e("test","提取异或码:"+yhMsg);
		//组合完整数据 计算数据长度
		newMsg = newMsg + endstr;
		//组合数据计算异或
		newMsg = headstr.substring(0,4)+TypeConvert.intToHex(newMsg.length()/2)+newMsg.substring(0,newMsg.length()-4);
		//计算异或
		String yhMsg1 = TypeConvert.yiHuo(newMsg);
		Log.e("test","计算异或:"+yhMsg1);
		if(!yhMsg.equals(yhMsg1)) {
			Log.e("test", "============异或校验未通过");
			return true; 
		}else {
			Log.e("test", "============异或校验通过");
			return false;
		}
    }
    /**
     * 传入数据进行拆分
     * @return
     */
    public static String getMsg3(String Msg2) {
    	String data="";
    	try {
	    	//获取累计气量，拆分
	    	if(Msg2.indexOf(".")==-1) {//没有小数点
	    		if(Msg2.length()>5) {
	    			return "error";
	    		}
	    		Msg2 = intToHex(Integer.parseInt(Msg2));
	    		while(Msg2.length()%2!=0) {
	    			Msg2 = "0"+Msg2;
	    		}
	    		for(int i=Msg2.length();i>0;i-=2) {
	    			data+=Msg2.substring(i-2, i);
	    		}
	    		while(data.length()!=6) {
	    			data =data+"0";
	    		}
	    		while(data.length()!=8) {
	    			data = "0"+data;
	    		}
	    	}else {//有小数点
	    		String rightnum="";//小数点右边
	    		String leftnum="";//小数点左边
	    		rightnum = Msg2.substring(Msg2.indexOf(".")+1);
	    		if(rightnum.length()==1) {//补齐长度
	    			rightnum = rightnum+"0";
	    		}
	    		leftnum = Msg2.substring(0,Msg2.indexOf("."));
	    		if(rightnum.length()>2) {
	    			return "error";
	    		}
	    		if(leftnum.length()>5) {
	    			return "error";
	    		}
	    		rightnum = intToHex(Integer.parseInt(rightnum));
	    		data+=rightnum;
	    		leftnum = intToHex(Integer.parseInt(leftnum));
	    		while(leftnum.length()%2!=0) {
	    			leftnum ="0"+leftnum;
	    		}
	    		for(int i=leftnum.length();i>0;i-=2) {
	    			data+=leftnum.substring(i-2, i);
	    		}
	    		while(data.length()!=8) {
	    			data =data+"0";
	    		}
	    	}
    	
    	}catch(Exception e) {
    		Log.e("error","历史气量拆分trycatch:"+e.getMessage());
    	}
    	return data;
    }
    /**
     * 此函数用于（主动上报参数设置）中的主信道和备用信道的组合字符串
     * @param zxdt 主信道
     * @param byxdt 备用信道
     * @return 返回组合后转换成hex的字符串
     */
    public static String getXinDao(String zxdt,String byxdt) {
    	String xdmsg ="";
		switch(zxdt) {
		case "3":
			switch(byxdt) {
			case "4": xdmsg="0000000000000011";break;
			case "5": xdmsg="0000000000000101";break;
			case "6": xdmsg="0000000000001001";break;
			case "7": xdmsg="0000000000010001";break;
			case "8": xdmsg="0000000000100001";break;
			case "9": xdmsg="0000000001000001";break;
			case "10":xdmsg="0000000010000001";break;
			case "11":xdmsg="0000000100000001";break;
			case "12":xdmsg="0000001000000001";break;
			case "13":xdmsg="0000010000000001";break;
			case "14":xdmsg="0000100000000001";break;
			case "15":xdmsg="0001000000000001";break;
			case "16":xdmsg="0010000000000001";break;
			case "17":xdmsg="0100000000000001";break;
			case "18":xdmsg="1000000000000001";break;
			}break;
		case "4":
			switch(byxdt) {
			case "5": xdmsg="0000000000000110";break;
			case "6": xdmsg="0000000000001010";break;
			case "7": xdmsg="0000000000010010";break;
			case "8": xdmsg="0000000000100010";break;
			case "9": xdmsg="0000000001000010";break;
			case "10":xdmsg="0000000010000010";break;
			case "11":xdmsg="0000000100000010";break;
			case "12":xdmsg="0000001000000010";break;
			case "13":xdmsg="0000010000000010";break;
			case "14":xdmsg="0000100000000010";break;
			case "15":xdmsg="0001000000000010";break;
			case "16":xdmsg="0010000000000010";break;
			case "17":xdmsg="0100000000000010";break;
			case "18":xdmsg="1000000000000010";break;
			}break;
		case "5":
			switch(byxdt) {
			case "6": xdmsg="0000000000001100";break;
			case "7": xdmsg="0000000000010100";break;
			case "8": xdmsg="0000000000100100";break;
			case "9": xdmsg="0000000001000100";break;
			case "10":xdmsg="0000000010000100";break;
			case "11":xdmsg="0000000100000100";break;
			case "12":xdmsg="0000001000000100";break;
			case "13":xdmsg="0000010000000100";break;
			case "14":xdmsg="0000100000000100";break;
			case "15":xdmsg="0001000000000100";break;
			case "16":xdmsg="0010000000000100";break;
			case "17":xdmsg="0100000000000100";break;
			case "18":xdmsg="1000000000000100";break;
			}break;
		case "6":
			switch(byxdt) {
			case "7": xdmsg="0000000000011000";break;
			case "8": xdmsg="0000000000101000";break;
			case "9": xdmsg="0000000001001000";break;
			case "10":xdmsg="0000000010001000";break;
			case "11":xdmsg="0000000100001000";break;
			case "12":xdmsg="0000001000001000";break;
			case "13":xdmsg="0000010000001000";break;
			case "14":xdmsg="0000100000001000";break;
			case "15":xdmsg="0001000000001000";break;
			case "16":xdmsg="0010000000001000";break;
			case "17":xdmsg="0100000000001000";break;
			case "18":xdmsg="1000000000001000";break;
			}break;
		case "7":
			switch(byxdt) {
			case "8": xdmsg="0000000000110000";break;
			case "9": xdmsg="0000000001010000";break;
			case "10":xdmsg="0000000010010000";break;
			case "11":xdmsg="0000000100010000";break;
			case "12":xdmsg="0000001000010000";break;
			case "13":xdmsg="0000010000010000";break;
			case "14":xdmsg="0000100000010000";break;
			case "15":xdmsg="0001000000010000";break;
			case "16":xdmsg="0010000000010000";break;
			case "17":xdmsg="0100000000010000";break;
			case "18":xdmsg="1000000000010000";break;
			}break;
		case "8":
			switch(byxdt) {
			case "9": xdmsg="0000000001100000";break;
			case "10":xdmsg="0000000010100000";break;
			case "11":xdmsg="0000000100100000";break;
			case "12":xdmsg="0000001000100000";break;
			case "13":xdmsg="0000010000100000";break;
			case "14":xdmsg="0000100000100000";break;
			case "15":xdmsg="0001000000100000";break;
			case "16":xdmsg="0010000000100000";break;
			case "17":xdmsg="0100000000100000";break;
			case "18":xdmsg="1000000000100000";break;
			}break;
		case "9":
			switch(byxdt) {
			case "10":xdmsg="0000000011000000";break;
			case "11":xdmsg="0000000101000000";break;
			case "12":xdmsg="0000001001000000";break;
			case "13":xdmsg="0000010001000000";break;
			case "14":xdmsg="0000100001000000";break;
			case "15":xdmsg="0001000001000000";break;
			case "16":xdmsg="0010000001000000";break;
			case "17":xdmsg="0100000001000000";break;
			case "18":xdmsg="1000000001000000";break;
			}break;
		case "10":
			switch(byxdt) {
			case "11":xdmsg="0000000110000000";break;
			case "12":xdmsg="0000001010000000";break;
			case "13":xdmsg="0000010010000000";break;
			case "14":xdmsg="0000100010000000";break;
			case "15":xdmsg="0001000010000000";break;
			case "16":xdmsg="0010000010000000";break;
			case "17":xdmsg="0100000010000000";break;
			case "18":xdmsg="1000000010000000";break;
			}break;
		case "11":
			switch(byxdt) {
			case "12":xdmsg="0000001100000000";break;
			case "13":xdmsg="0000010100000000";break;
			case "14":xdmsg="0000100100000000";break;
			case "15":xdmsg="0001000100000000";break;
			case "16":xdmsg="0010000100000000";break;
			case "17":xdmsg="0100000100000000";break;
			case "18":xdmsg="1000000100000000";break;
			}break;
		case "12":
			switch(byxdt) {
			case "13":xdmsg="0000011000000000";break;
			case "14":xdmsg="0000101000000000";break;
			case "15":xdmsg="0001001000000000";break;
			case "16":xdmsg="0010001000000000";break;
			case "17":xdmsg="0100001000000000";break;
			case "18":xdmsg="1000001000000000";break;
			}break;
		case "13":
			switch(byxdt) {
			case "14":xdmsg="0000110000000000";break;
			case "15":xdmsg="0001010000000000";break;
			case "16":xdmsg="0010010000000000";break;
			case "17":xdmsg="0100010000000000";break;
			case "18":xdmsg="1000010000000000";break;
			}break;
		case "14":
			switch(byxdt) {
			case "15":xdmsg="0001100000000000";break;
			case "16":xdmsg="0010100000000000";break;
			case "17":xdmsg="0100100000000000";break;
			case "18":xdmsg="1000100000000000";break;
			}break;
		case "15":
			switch(byxdt) {
			case "16":xdmsg="0011000000000000";break;
			case "17":xdmsg="0101000000000000";break;
			case "18":xdmsg="1001000000000000";break;
			}break;
		case "16":
			switch(byxdt) {
			case "17":xdmsg="0110000000000000";break;
			case "18":xdmsg="1010000000000000";break;
			}break;
		case "17":
			switch(byxdt) {
			case "18":xdmsg="1100000000000000";break;
			}break;
		}
		xdmsg = TypeConvert.strTohexStr(xdmsg);
		return xdmsg;
    }
    
}