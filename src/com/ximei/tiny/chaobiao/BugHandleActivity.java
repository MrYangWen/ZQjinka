package com.ximei.tiny.chaobiao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tiny.gasxm.R;
import com.ximei.tiny.backinfoview.BackInFoActivity;
import com.ximei.tiny.backinfoview.BackSingleCBActivity;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.FileUtils;
import com.ximei.tiny.tools.GetQydm;
import com.ximei.tiny.tools.GetTotalPack;
import com.ximei.tiny.tools.GetmsgID;
import com.ximei.tiny.tools.JinKaAgreement;
import com.ximei.tiny.tools.ToHexStr;
import com.ximei.tiny.tools.TypeConvert;

import android.widget.RadioGroup.OnCheckedChangeListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 *  修改activity。
 *  有一个输入框，对应相应的操作
 * 
 * 
 * 
 */
public class BugHandleActivity extends Activity {
	String CRCmsg;
	final int MAXmeternum = 16777215;
	final int MINmeternum = 0;
	String MsgID;
	String Msgvalue;
	String Ordermsg;
	String StrID;
	String Timemsg;
	String bugtype,biaotype;
	public CRC crc;
	private EditText editvalue;
	private float floatvalue;
	String headmsg;
	String hexTimemsg;
	private TextView infohint;
	private int intID;
	Intent intent;
	String ordermsg;
	String overmsg;
	private Button querybutton;
	private GetQydm getqydm;
	private String userpw,hfpw,sibiao,maxcz,guanfa,guoliu,touzhi,czcs,czlx,cze,tze;
    private GetTotalPack sendhex;
    private RadioButton oldmeter,newmeter,LongAddr;
    private RadioGroup metertype;
    private RadioGroup addrType;
    JinKaAgreement jk;
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 取消状态标题栏
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.bughandle);
		this.infohint = ((TextView) findViewById(R.id.infohint));
		this.editvalue = ((EditText) findViewById(R.id.bughandleinput));
		this.querybutton = ((Button) findViewById(R.id.bughandlebt));
		oldmeter = (RadioButton)findViewById(R.id.oldmeter);
		newmeter = (RadioButton)findViewById(R.id.newmeter);
		metertype = (RadioGroup)findViewById(R.id.metertype);
		addrType=(RadioGroup)findViewById(R.id.AddrType);
		LongAddr=(RadioButton)findViewById(R.id.LongAddr);
		LongAddr.setVisibility(View.INVISIBLE);
		biaotype="mrmeter";
		this.intent = getIntent();
		jk = new JinKaAgreement();
		getqydm = new GetQydm();
		sendhex = new GetTotalPack();
		// 根据intent得到overmsg，shouhoutype
		this.overmsg = this.intent.getStringExtra("overmsg");
		this.bugtype = this.intent.getStringExtra("bugtype");	
		//新增单选框
		metertype.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(oldmeter.getId()==checkedId){
					biaotype="oldmeter";	
				}else if(newmeter.getId()==checkedId){
					biaotype="newmeter";
				}
			}
		});	
	  //2014-12-1测试ic卡test.txt文件数据
		
		FileUtils fileutil = new FileUtils();
		String pathsdcard = fileutil.getSDPATH();

		File icdata = new File(pathsdcard + "ICCAED/test.txt"); 
		byte[] tempByte = new byte[1024];

		try {
			InputStream in = new FileInputStream(icdata);
			int len = in.read(tempByte);
			byte[] b = new byte[len];
			for (int i = 0; i < len; i++) {

				b[i] = tempByte[i];
			}
			String str = new String(b);
			String[] data = str.split("\\|");

			userpw = data[1].substring(5, data[1].length());
			hfpw = data[2].substring(5, data[2].length());
			sibiao = data[3].substring(4, data[3].length());
			maxcz = data[4].substring(5, data[4].length());
			guanfa = data[5].substring(3, data[5].length());
			guoliu = data[6].substring(3, data[6].length());
			touzhi = data[7].substring(3, data[7].length());
			czcs = data[8].substring(4, data[8].length());
			czlx = data[9].substring(4, data[9].length());
			cze = data[10].substring(3, data[10].length());
			tze = data[11].substring(3, data[11].length());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  if (this.bugtype.equals("qbaz")) {
				this.infohint.setText("气表安装");
				this.editvalue.setHint("输入表地址");
			}
		  if (this.bugtype.equals("qbtz")) {
				this.infohint.setText("气表透支");
				this.editvalue.setHint("输入表地址");
			}
		  if (this.bugtype.equals("xrtc")) {
				this.infohint.setText("写RTC");
				this.editvalue.setHint("输入表地址");
			}
		  if (this.bugtype.equals("drtc")) {
				this.infohint.setText("读RTC");
				this.editvalue.setHint("输入表地址");
			}
		  if (this.bugtype.equals("qbcz")) {
				this.infohint.setText("气表充值");
				this.editvalue.setHint("输入表地址");
			}
		//2014-12-1测试ic卡test.txt文件数据
		//2014-12-15综合测试
		  if(this.bugtype.equals("zhcs")){
			  this.infohint.setText("综合测试");
			  this.editvalue.setText("11");	  
		  }
		
		// 根据shouhoutype得到相应的infohint值
		if (this.bugtype.equals("sbdz")) {
			this.infohint.setText("设置表地址");
			this.editvalue.setHint("输入设置表地址");
			LongAddr.setVisibility(View.VISIBLE);
		}
		if (this.bugtype.equals("testbh")) {
			this.infohint.setText("测试写地址");
			LongAddr.setVisibility(View.VISIBLE);
			this.editvalue.setHint("输入气表地址");
		}
		if (this.bugtype.equals("qcgf"))
			this.infohint.setText("强制关阀");
		if (this.bugtype.equals("qxqg"))
			this.infohint.setText("取消强关");
		if (this.bugtype.equals("fmlq"))
			this.infohint.setText("阀门漏气");
		if (this.bugtype.equals("sbgz"))
			this.infohint.setText("死表故障");
		if (this.bugtype.equals("ghgh"))
			this.infohint.setText("干簧管坏");
		if (this.bugtype.equals("ccqh"))
			this.infohint.setText("RF模块坏");
		if (this.bugtype.equals("qcgj"))
			this.infohint.setText("强磁攻击");
		if (this.bugtype.equals("bcsh"))
			this.infohint.setText("气表初始化");
		if (this.bugtype.equals("czsz")) {
			this.infohint.setText("出厂设置");
			this.editvalue.setHint("出厂同步气量");
			LongAddr.setVisibility(View.VISIBLE);
		}
		if (this.bugtype.equals("txcssz")) {
			this.infohint.setText("通信参数设置");
			this.editvalue.setHint("请输入表地址");
		}
		if(bugtype.equals("GetVoltage"))
		{
			
			ordermsg="5A5A00FE0009000000000048E2AA40F9035B5B01";
			intent.putExtra("order",ordermsg);
			intent.putExtra("metertype",biaotype);
			intent.setClass(BugHandleActivity.this, BtXiMeiService.class);
			startService(BugHandleActivity.this.intent);
			finish();
            					
		}		
		this.infohint.setTextSize(30.0F);
		this.querybutton.setOnClickListener(new OnClickListener() {
			public void onClick(View paramAnonymousView) {
				GetmsgID localGetmsgID = new GetmsgID();
				ToHexStr localToHexStr = new ToHexStr();
				BugHandleActivity.this.crc = new CRC();
				BugHandleActivity.this.headmsg = "5A5A00FE01";
				//得到系统当前时间
				SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ssmmHHddMMyy");
				Timemsg = localSimpleDateFormat.format(new Date());
				hexTimemsg = localToHexStr.toHexStr(Timemsg);
				StrID = editvalue.getEditableText().toString();

				String Broadcastaddr="";
				if(LongAddr.isChecked())
					Broadcastaddr="FFFFFFFFFFFFFF";
				else
					Broadcastaddr="FFFFFF";					
				Intent intentBusy = new Intent("android.intent.action.busy");
				intentBusy.putExtra("State", "busy");
				sendBroadcast(intentBusy);					
				if (bugtype.equals("txcssz")) {
					
				}
				//设置出厂设置
				else if (bugtype.equals("czsz")) {
					floatvalue = Float.parseFloat(StrID);
					String str = localGetmsgID.getMsgID(Integer.toHexString((int) (10.0F * floatvalue)));
					CRCmsg = ("09"+Broadcastaddr+"A304"+ str.toUpperCase() + "05");
					ordermsg = (headmsg+ CRCmsg+ crc.CRC_CCITT(1,CRCmsg).toUpperCase()+ overmsg + "5B5B/");
					intent.putExtra("order",ordermsg);
					intent.putExtra("metertype",biaotype);
					intent.setClass(BugHandleActivity.this, BtXiMeiService.class);
					startService(BugHandleActivity.this.intent);
					//editvalue.setText("");
					//2015-01-21出厂修改
					Intent localIntent2 = new Intent();
					localIntent2.putExtra("Comm", "00");
					localIntent2.setClass(BugHandleActivity.this,BackSingleCBActivity.class);
					startActivity(localIntent2);
				}
//				else if(bugtype.equals("qbaz")||bugtype.equals("qbcz")||bugtype.equals("qbtz")){
//					//得到表号 
//					Msgvalue = localGetmsgID.getMsgID(BugHandleActivity.this.MsgID).toUpperCase();	
//					if(bugtype.equals("qbaz")){	
//						int userpwint = Integer.parseInt(userpw);
//						String hexuserpw = Integer.toHexString(userpwint);
//						String senduserpw =sendhex.gettotalpack(hexuserpw, 6);
//						int hfpwint = Integer.parseInt(hfpw);
//						String hexhfpw = Integer.toHexString(hfpwint);
//						String sendhfpw =sendhex.gettotalpack(hexhfpw, 6);
//						int maxczint = Integer.parseInt(maxcz);
//						String hexmaxcz = Integer.toHexString(maxczint);
//						String sendmaxcz =sendhex.gettotalpack(hexmaxcz, 4);
//						int guanfaint = Integer.parseInt(guanfa);
//						String hexguanfa = Integer.toHexString(guanfaint);
//						String sendguanfa =sendhex.gettotalpack(hexguanfa, 4);
//						int guoliuint = Integer.parseInt(guoliu);
//						String hexguoliu = Integer.toHexString(guoliuint);
//						String sendguoliu =sendhex.gettotalpack(hexguoliu, 4);
//						int touzhiint = Integer.parseInt(touzhi);
//						String hextouzhi = Integer.toHexString(touzhiint);
//						String sendtouzhi =sendhex.gettotalpack(hextouzhi, 4);
//						int sibiaoint = Integer.parseInt(sibiao);
//						String hexsibiao = Integer.toHexString(sibiaoint);
//						String sendsibiao =sendhex.gettotalpack(hexsibiao, 2);
//						CRCmsg = ("05"+Msgvalue+"48"+"0F"+senduserpw+sendhfpw+sendsibiao+sendmaxcz+sendguanfa+sendguoliu+sendtouzhi).toUpperCase();
//					}
//					if(bugtype.equals("qbcz")){	
//						int czeint = Integer.parseInt(cze);
//						String hexcze = Integer.toHexString(czeint);
//						String sendcze =sendhex.gettotalpack(hexcze,6);	
//						int czcsint = Integer.parseInt(czcs);
//						String hexczcs = Integer.toHexString(czcsint);
//						String sendczcs =sendhex.gettotalpack(hexczcs,4);	
//						int czlxint = Integer.parseInt(czlx);
//						String hexczlx = Integer.toHexString(czlxint);
//						String sendczlx =sendhex.gettotalpack(hexczlx,2);
//						CRCmsg = ("05"+Msgvalue+"42"+"06"+sendcze+sendczcs+sendczlx).toUpperCase();
//						
//					}
//					if(bugtype.equals("qbtz")){
//						
//						int tzeint = Integer.parseInt(tze);
//						String hextze = Integer.toHexString(tzeint);
//						String sendtze =sendhex.gettotalpack(hextze,4);
//						
//						CRCmsg = ("05"+Msgvalue+"45"+"02"+sendtze).toUpperCase();
//					}
//					ordermsg = (headmsg+ CRCmsg+ crc.CRC_CCITT(1,CRCmsg).toUpperCase()+overmsg+ "5B5B/");
//					Log.e("test", ordermsg);
//					intent.putExtra("bugtype",bugtype);
//					intent.putExtra("order",ordermsg);
//					
//					BugHandleActivity.this.intent.putExtra("metertype",biaotype);
//					intent.setClass(BugHandleActivity.this, BtXiMeiService.class);
//					BugHandleActivity.this.startService(intent);
//					Intent localIntent1 = new Intent();
//					localIntent1.putExtra("Comm", "00");
//					localIntent1.setClass(BugHandleActivity.this,BackSingleCBActivity.class);
//					BugHandleActivity.this.startActivity(localIntent1);	
//					
//				}
//				else if(bugtype.equals("zhcs")){
//					
//					FileUtils fileutil = new FileUtils();
//					String pathsdcard = fileutil.getSDPATH();
//					File icdata = new File(pathsdcard + "ICCAED/alltest.txt"); 
//					byte[] tempByte = new byte[1024];
//						InputStream in;
//						try {
//							in = new FileInputStream(icdata);
//							int len = in.read(tempByte);
//							byte[] b = new byte[len];
//							for (int i = 0; i < len; i++) {
//
//								b[i] = tempByte[i];
//							}
//							String str = (new String(b)).trim();
//							CRCmsg = str.substring(10, str.length());
//							ordermsg = (str+crc.CRC_CCITT(1,BugHandleActivity.this.CRCmsg).toUpperCase()+overmsg+ "5B5B/");
//							Log.e("test", ordermsg);
//							intent.putExtra("bugtype",bugtype);
//							BugHandleActivity.this.intent.putExtra("metertype",biaotype);
//							intent.putExtra("order",ordermsg);
//							
//							intent.setClass(BugHandleActivity.this, BtXiMeiService.class);
//							BugHandleActivity.this.startService(intent);
//							Intent localIntent1 = new Intent();
//							localIntent1.putExtra("Comm", "00");
//							localIntent1.setClass(BugHandleActivity.this,BackSingleCBActivity.class);
//							BugHandleActivity.this.startActivity(localIntent1);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//				}
				else{
					
					//intID = Integer.parseInt(BugHandleActivity.this.StrID);
					//MsgID = Integer.toHexString(BugHandleActivity.this.intID);
					try {
//						if ((BugHandleActivity.this.intID > 16777215)|| (BugHandleActivity.this.intID < 0)
//								|| (BugHandleActivity.this.MsgID.length() > 6))
//							Toast.makeText(BugHandleActivity.this,"表号输入错误请重新输入", Toast.LENGTH_SHORT).show();
//						if ((BugHandleActivity.this.MsgID.length() < 7)&& (BugHandleActivity.this.MsgID.length() > 0)) 
						//Msgvalue=localGetmsgID.GetMeterAddr(StrID);
						Msgvalue=StrID;
						//if((StrID.length()==14))
						biaotype="newmeter";						
						if(Msgvalue!=null && !Msgvalue.equals(""))
						{
							//Msgvalue = localGetmsgID.getMsgID(BugHandleActivity.this.MsgID).toUpperCase();
							String DataLen=String.format("%02X",Msgvalue.length()/2+6);
                           //强制关阀
							if (bugtype.equals("qcgf")) {
							       // 	      长度      起始符                                                                            控制字0                            	控制字1 								控制字2								控制字3		源节点     表号	数据域
								CRCmsg = "14"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+StrID+"030101";
								//加上CRC效验码
								ordermsg = CRCmsg+ jk.getCrcjy(CRCmsg);
								//加上(异或)校验和
								ordermsg = ordermsg+TypeConvert.yiHuo(ordermsg);
								//数据域加密
								ordermsg = jk.decrypt(ordermsg);
								//获取加密后的异或校验
								ordermsg = ordermsg.substring(0,ordermsg.length()-2)+TypeConvert.yiHuo(ordermsg);
							}
								//CRCmsg = ("03"+ Msgvalue + "51020300");
							//取消强关
							if (bugtype.equals("qxqg")) {
								   //     长度      起始符                                                                            控制字0                            	控制字1 								控制字2								控制字3		源节点     表号	数据域
								CRCmsg = "14"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+StrID+"030100";
								//加上CRC效验码
								ordermsg = CRCmsg+ jk.getCrcjy(CRCmsg);
								//加上(异或)校验和
								ordermsg = ordermsg+TypeConvert.yiHuo(ordermsg);
								//数据域加密
								ordermsg = jk.decrypt(ordermsg);
								//获取加密后的异或校验
								ordermsg = ordermsg.substring(0,ordermsg.length()-2)+TypeConvert.yiHuo(ordermsg);
							}
							//写RTC
							if(bugtype.equals("xrtc")) {
								SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
								   //     长度      起始符                                                                            控制字0                            	控制字1 								控制字2								控制字3		源节点     表号	数据域
								CRCmsg = "1A"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+StrID+"030702"+df.format(new Date()).toString();
								//加上CRC效验码
								ordermsg = CRCmsg+ jk.getCrcjy(CRCmsg);
								//加上(异或)校验和
								ordermsg = ordermsg+TypeConvert.yiHuo(ordermsg);
								//数据域加密
								ordermsg = jk.decrypt(ordermsg);
								//获取加密后的异或校验
								ordermsg = ordermsg.substring(0,ordermsg.length()-2)+TypeConvert.yiHuo(ordermsg);
								
							}
							//读RTC
							if(bugtype.equals("drtc")) {
								   //     长度      起始符                                                                            控制字0                            	控制字1 								控制字2								控制字3		源节点     表号	数据域
								CRCmsg = "14"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+StrID+"030703";
								//加上CRC效验码
								ordermsg = CRCmsg+ jk.getCrcjy(CRCmsg);
								//加上(异或)校验和
								ordermsg = ordermsg+TypeConvert.yiHuo(ordermsg);
								//数据域加密
								ordermsg = jk.decrypt(ordermsg);
								//获取加密后的异或校验
								ordermsg = ordermsg.substring(0,ordermsg.length()-2)+TypeConvert.yiHuo(ordermsg);
								
							}
								//CRCmsg = ("03"+ Msgvalue + "51020E00");
							//阀门漏气
							if (bugtype.equals("fmlq"))
								CRCmsg = ("03"+ Msgvalue + "51020D00");
							//死表故障
							if (bugtype.equals("sbgz"))
								CRCmsg = ("03"+ Msgvalue + "51020C00");
							//干簧管故障
							if (bugtype.equals("ghgh"))
								CRCmsg = ("03"+ Msgvalue + "51020500");
							//RF故障
							if (bugtype.equals("ccqh"))
								CRCmsg = ("03"+ Msgvalue + "51020900");
							//强磁攻击
							if (bugtype.equals("qcgj"))
								CRCmsg = ("03"+ Msgvalue + "51020A00");
							//设表地址
							if (bugtype.equals("sbdz"))
								CRCmsg = ("09"+Broadcastaddr+"EB"+DataLen+ Msgvalue + hexTimemsg);
                            //测试设置表地址
							if (bugtype.equals("testbh"))
								CRCmsg = ("09"+Broadcastaddr+"EB"+DataLen+ Msgvalue + hexTimemsg);
							//气表初始化
							if (bugtype.equals("bcsh"))
								CRCmsg = ("09"+ Msgvalue+ "41" + "00");

							//ordermsg = (headmsg+ CRCmsg+ crc.CRC_CCITT(1,CRCmsg).toUpperCase()+overmsg+ "5B5B/");
						
							Log.e("test", ordermsg);
							intent.putExtra("bugtype",bugtype);
							intent.putExtra("metertype",biaotype);
							intent.putExtra("order",ordermsg);
							
							intent.setClass(BugHandleActivity.this, BtXiMeiService.class);
							startService(BugHandleActivity.this.intent);
							Intent localIntent1 = new Intent();
							localIntent1.putExtra("Comm", "00");
							localIntent1.setClass(BugHandleActivity.this,BackSingleCBActivity.class);
							BugHandleActivity.this.startActivity(localIntent1);
						}
						else {
							Toast.makeText(BugHandleActivity.this,"输入表号出错", Toast.LENGTH_SHORT).show();
						}
					} catch (NumberFormatException localNumberFormatException) {
						Toast.makeText(BugHandleActivity.this,"输入表号出错", Toast.LENGTH_SHORT).show();
					}
				}
			}

		});
	}
}