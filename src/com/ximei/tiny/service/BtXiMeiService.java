package com.ximei.tiny.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.ximei.tiny.backinfoview.BackInFoActivity;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.Containstr;
import com.ximei.tiny.tools.FileOpertion;
import com.ximei.tiny.tools.FileUtils;
import com.ximei.tiny.tools.GetAllQbbh;
import com.ximei.tiny.tools.GetRawData;
import com.ximei.tiny.tools.GetmsgID;
import com.ximei.tiny.tools.IniReader;
import com.ximei.tiny.tools.JinKaAgreement;
import com.ximei.tiny.tools.TypeConvert;

import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class BtXiMeiService extends Service {

	public final int MSG_READ = 1;
	public final int MSG_WRITE = 2;
	int conflag;
	Boolean firstFlag=false,Idle;
	FileOpertion fileopertion=new FileOpertion();
	BluetoothAdapter btAdapt;
	private BluetoothSocket mmSocket;
	private String[] rowdata;
	// private BluetoothDevice mmDevice;
	BluetoothDevice btDev;
	final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	UUID uuid;
	boolean MeterType=false;  
	private String order, allorer,MeterAddrStr,sendaddress, backaddress, backcrc,sendorder, backorder, senddatastr, sendhead;
	private String backdatastr, backdatamsg, backcrcstr, backcrcmsg,sendsetstr, backhead;
	private int backdatalen, senddatalen;
	private String headstr, endstr, metertype,backrand;
	private CRC crc;
	private FileUtils fileutils;
	private String FreqStr;
	GetmsgID getmsg;
	GetAllQbbh getqbbh;
	Containstr contain;
	JinKaAgreement jk;
	AlertDialog.Builder localBuilder;
	AlertDialog localAlertDialog;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		super.onCreate();
		conflag = 0;
		headstr = "";
		endstr = "";
		Idle=true;
		crc = new CRC();
		jk = new JinKaAgreement();
		getmsg = new GetmsgID();
		getqbbh = new GetAllQbbh();
		fileutils = new FileUtils();
		contain = new Containstr();
		btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能
		localBuilder = new AlertDialog.Builder(BtXiMeiService.this.getApplicationContext());
		localAlertDialog = localBuilder.create();
		try {
			FreqStr = new IniReader("SysSet.ini", BtXiMeiService.this).getValue("FreqSet", "TestFreq");
			if(FreqStr==null)
				FreqStr="75AB85";
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			FreqStr="75AB85";
			e.printStackTrace();
		}
		if (btAdapt != null) {
			Object[] lstDevice = btAdapt.getBondedDevices().toArray();
			if (lstDevice.length == 0) {
				// 蓝牙连接断开弹出提示框，
				AlertDialog.Builder localBuilder = new AlertDialog.Builder(BtXiMeiService.this.getApplicationContext());
				localBuilder.setTitle("提示");
				localBuilder.setPositiveButton("确定", null);
				localBuilder.setIcon(17301659);
				localBuilder.setMessage("未发现配对设备，请先配对");
				AlertDialog localAlertDialog = localBuilder.create();
				localAlertDialog.getWindow().setType(2003);
				localAlertDialog.show();

			} else {
				BluetoothDevice device = (BluetoothDevice) lstDevice[0];
				// String btname = device.getName();
				String btaddress = device.getAddress();
				uuid = UUID.fromString(SPP_UUID);
				btDev = btAdapt.getRemoteDevice(btaddress);
				new Thread(new ATconnect()).start();
				//new Thread(new BattDect()).start();
			}
		}

		new Thread(new LoadDataThrad()).start();

		// 注册Receiver来获取蓝牙设备相关的结果
		IntentFilter intent = new IntentFilter();
		// 监听蓝牙连接状态的广播
		intent.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		intent.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		intent.addAction("android.intent.action.disbtconnect");
		intent.addAction("android.intent.action.busy");
		registerReceiver(Connectstate, intent);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		order = intent.getStringExtra("order");
		String log=fileopertion.getCurTime()+"发送:\r\n"+order;
		fileopertion.writeTxtToFile(log);
		try {
			if(order.length()>=40)
			{
				if(order.substring(0, 40).equals("5A5A00FE0009000000000048E2AA40F9035B5B01")) //发送的是获取电压数据
				{
					  writer("|" + "5A5A00FE0009000000000048E2AA40F9035B5B01" + "/");  //电池电压检测
					  return super.onStartCommand(intent, flags, startId);
				}
			}
			if(order.substring(0, 4).equals("5A5A")){
				Log.e("test", "RFwrite");
				metertype = intent.getStringExtra("metertype");
				if (metertype == null) {
					metertype = "mrmeter";
				}
				// 发送协议头
				sendhead = order.substring(0, 10);
				int startPos,endPos;
				senddatalen = Integer.parseInt(order.substring(20, 22), 16);
				startPos=22+senddatalen*2;
				endPos=26+senddatalen*2;
				if(endPos>order.length()){
					endPos=order.length();
					startPos=order.length();
				}
				String CrcStr=order.substring(startPos,endPos);
				if(crc.CRC_CCITT(1,order.substring(10, startPos)).toUpperCase().equals(CrcStr))
				{
					MeterType=false;
					// 发送命令字
					sendorder = order.substring(18, 20);
					// 发送数据表地址
					sendaddress = order.substring(12, 18);
					// 发送数据内容
					//senddatastr = order.substring(22, 22 + 2 * senddatalen);
					// 设置表地址操作发送表地址
					sendsetstr = order.substring(22, 28);					
				}
				else
				{
					MeterType=true;
					//senddatalen = Integer.parseInt(order.substring(28, 30), 16);
					// 发送命令字
					sendorder = order.substring(26, 28);
					// 发送数据表地址
					sendaddress = order.substring(12, 26);
					// 发送数据内容
					//senddatastr = order.substring(30, 30 + 2 * senddatalen);
					// 设置表地址操作发送表地址
					sendsetstr = order.substring(30, 44);						
				}				
//				// 发送命令字
//				sendorder = order.substring(18, 20);
//				// 发送数据表地址
//				sendaddress = order.substring(12, 18);
//				// 发送数据长度
//				senddatalen = Integer.parseInt(order.substring(20, 22), 16);
//				// 发送数据内容
//				senddatastr = order.substring(22, 22 + 2 * senddatalen);
//				// 设置表地址操作发送表地址
//				sendsetstr = order.substring(22, 28);

				handler.post(r);
		    }else{
		    	Log.e("test", "cardwrite");
		    	writer(order);
		    	Message msg = new Message();
		    	try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				msg.obj = "32122180670200000104181044001537764CAB4B158D57B46A4A684B95B5954B95B5954B952C0CB46A4A6A4B953694B64205EB".getBytes();
				msg.what = MSG_READ;
				handler.sendMessage(msg);
		    }	
				
				
			
		} catch (Exception e) {
			// handler.post(r);
			Log.e("test", e.toString());

		}
		return super.onStartCommand(intent, flags, startId);
	}

	// 接收其他线程消息的Handler
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// 处理消息
			switch (msg.what) {
			case MSG_READ:

				byte[] backmsg1 = (byte[]) msg.obj;
				String backmsg = new String(backmsg1);
				Log.e("test", "返回数据：" + backmsg);
				//蓝牙模块
				if(backmsg.substring(0, 16).equals("FCFC09000000F003")){
	    				Intent StIntent = new Intent("android.intent.action.BattDect");
	    				StIntent.putExtra("resmsg", backmsg);
	    				BtXiMeiService.this.sendBroadcast(StIntent);  
						headstr = "";
						endstr = "";
	    				return;
	            }
				
				String yh1 = TypeConvert.yiHuo(backmsg);
				String yh2 = backmsg.substring(backmsg.length()-2);
				Log.e("test","返回数据异或："+yh2+"---本地异或校验："+yh1);
				if(!yh1.equals(yh2)) {
					Log.e("test","异或校验失败！数据不正确");
					return;
				}else {
					backmsg = jk.decrypt(backmsg);//数据解密
					Log.e("test", "返回数据解密后："+backmsg);
					String crcresult = jk.getCRC(backmsg);
					if(crcresult.equals("error")) {
						Log.e("test","CRC校验失败！数据不正确");
						return;
					}
					String signT = backmsg.substring(30, 32);//获取标签域
					String signV = backmsg.substring(34, 36);//获取值域标签
					Log.e("test", signT+"===="+signV);
					
					//抄表
					if(signT.equals("83") && signV.equals("06")) {
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Intent intent = new Intent();
						intent.setAction("android.intent.action.putongcb_BROADCAST");
						
						intent.putExtra("resmsg", backmsg);
						intent.putExtra("sendorder",signV);
						BtXiMeiService.this.sendBroadcast(intent);
						return;
					}
					//开阀
					if(signT.equals("83") && signV.equals("00")) {
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Intent intent = new Intent();
						intent.setAction("android.intent.action.putongcb_BROADCAST");
						
						intent.putExtra("resmsg", backmsg);
						intent.putExtra("sendorder",signV);
						BtXiMeiService.this.sendBroadcast(intent);
						return;
					}
					//关阀
					if(signT.equals("83") && signV.equals("01")) {
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Intent intent = new Intent();
						intent.setAction("android.intent.action.putongcb_BROADCAST");
						
						intent.putExtra("resmsg", backmsg);
						intent.putExtra("sendorder",signV);
						BtXiMeiService.this.sendBroadcast(intent);
						return;
					}
					//写RTC
					if(signT.equals("83") && signV.equals("02")) {
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Intent intent = new Intent();
						intent.setAction("android.intent.action.putongcb_BROADCAST");
						
						intent.putExtra("resmsg", backmsg);
						intent.putExtra("sendorder",signV);
						BtXiMeiService.this.sendBroadcast(intent);
						return;
					}
					//读RTC
					if(signT.equals("83") && signV.equals("03")) {
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Intent intent = new Intent();
						intent.setAction("android.intent.action.putongcb_BROADCAST");
						
						intent.putExtra("resmsg", backmsg);
						intent.putExtra("sendorder",signV);
						BtXiMeiService.this.sendBroadcast(intent);
						return;
					}
					//读历史记录（结算日）
					if(signT.equals("83") && signV.equals("07")) {
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Intent intent = new Intent();
						intent.setAction("android.intent.action.putongcb_BROADCAST");
						
						intent.putExtra("resmsg", backmsg);
						intent.putExtra("sendorder",signV);
						BtXiMeiService.this.sendBroadcast(intent);
						return;
					}
					//读历史记录（每天）
					if(signT.equals("83") && signV.equals("08")) {
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Intent intent = new Intent();
						intent.setAction("android.intent.action.putongcb_BROADCAST");
						
						intent.putExtra("resmsg", backmsg);
						intent.putExtra("sendorder",signV);
						BtXiMeiService.this.sendBroadcast(intent);
						return;
					}
					
				}
				
				
				
				
				
				
				
				
				
				
				
				String log=fileopertion.getCurTime()+"原始接收:\r\n"+backmsg;
				fileopertion.writeTxtToFile(log);
                if (backmsg.indexOf("****") == 1) {
					headstr += backmsg;
				} 
                else if(backmsg.indexOf("****") == 1)
                {			
					endstr = backmsg;
					String backinfo = headstr + endstr;				
					try {
		                if(backinfo.substring(0, 16).equals("FCFC09000000F003")){
		    				Intent StIntent = new Intent("android.intent.action.BattDect");
		    				StIntent.putExtra("resmsg", backinfo);
		    				BtXiMeiService.this.sendBroadcast(StIntent);  
							headstr = "";
							endstr = "";
		    				return;
		                }							
						// 返回协议头
						backhead = backinfo.substring(0, 4);
						if (!backhead.equals("FE01")) {
							//返回随机数
							backrand=backinfo.substring(4, 6);
							// 返回表地址
							if(MeterType==true)
							{
								backrand=backinfo.substring(4, 6);
								// 返回表地址
								backaddress = backinfo.substring(6, (12+8));
								// 返回数据长度
								backdatastr = backinfo.substring((14+8), (16+8));
								// 返回数据命令
								backorder = backinfo.substring((12+8), (14+8));
								// 返回数据长度转化为整型
								backdatalen = Integer.parseInt(backdatastr, 16);
								// 返回CRC应校验的字符串
								backcrcstr = backinfo.substring(4,(16+8 + 2 * backdatalen));
								// 返回CRC的值
								backcrcmsg = backinfo.substring((16+8 + 2 * backdatalen), (20+8 + 2 * backdatalen));
								// 返回数据内容
								backdatamsg = backinfo.substring((16+8),(16+8 + 2 * backdatalen));
								// 通过计算得出返回CRC的值
								backcrc = crc.CRC_CCITT(1, backcrcstr).toUpperCase();
								String log1=fileopertion.getCurTime()+"接收:\r\n"+backinfo+"表ID："+backaddress;
								fileopertion.writeTxtToFile(log1);
								
							}else{							
								backaddress = backinfo.substring(6, 12);
								// 返回数据长度
								backdatastr = backinfo.substring(14, 16);
								// 返回数据命令
								backorder = backinfo.substring(12, 14);
								// 返回数据长度转化为整型
								backdatalen = Integer.parseInt(backdatastr, 16);
								// 返回CRC应校验的字符串
								backcrcstr = backinfo.substring(4,(16 + 2 * backdatalen));
								// 返回CRC的值
								backcrcmsg = backinfo.substring((16 + 2 * backdatalen), (20 + 2 * backdatalen));
								// 返回数据内容
								backdatamsg = backinfo.substring(16,(16 + 2 * backdatalen));
								// 通过计算得出返回CRC的值
								backcrc = crc.CRC_CCITT(1, backcrcstr).toUpperCase();
								String MeterAddr=backaddress.substring(4, 6)+backaddress.substring(2, 4)+backaddress.substring(0, 2);
								MeterAddrStr=""+Integer.parseInt(MeterAddr, 16);
								String log1=fileopertion.getCurTime()+"接收:\r\n"+backinfo+"表ID："+Integer.parseInt(MeterAddr, 16);
								fileopertion.writeTxtToFile(log1);
								
							}
														
						}
						// 新中继器返回命令操作
						// 请求传输表号

						if (sendorder.equals("0A")) {

							if (backorder.equals("F0")) {
								Intent intent = new Intent("android.intent.newxmr.qqcsbh");
								BtXiMeiService.this.sendBroadcast(intent);
							}

						}

						// 新集中传输表号命令
						if (sendorder.equals("0B")) {
							Intent intent = new Intent("android.intent.newxmr.backcsbh");
							intent.putExtra("backmsg", backdatamsg);
							intent.putExtra("backorder", backorder);
							BtXiMeiService.this.sendBroadcast(intent);
						}
						// 新中继器采集抄表数据
						if (sendorder.equals("06")) {

							if (backorder.equals("F0")) {
								Log.e("test", "新amr采集数据");
								Intent intent = new Intent("android.intent.newxmr.cjcbsj");
								intent.putExtra("backdatalen",String.valueOf(backdatalen));
								intent.putExtra("backdatamsg", backdatamsg);
								BtXiMeiService.this.sendBroadcast(intent);

							}

						}

						// 新集中器测试组网，表示组网结果的返回
//						if (sendorder.equals("0C")) {
//							Intent intent = new Intent("android.com.tiny.action.testzw");
//							intent.putExtra("backmsg", backdatamsg);
//							intent.putExtra("backorder", backorder);
//							BtXiMeiService.this.sendBroadcast(intent);
//						}

						//

						// 新中继器删除表号设置时间

						if (sendorder.equals("04")||sendorder.equals("00")|| sendorder.equals("01")||sendorder.equals("64")) {

							if (backorder.equals("F0")) {

								Intent intent = new Intent("android.com.tiny.action.backc3order");
								intent.putExtra("backdatalen",String.valueOf(backdatalen));
								intent.putExtra("backmsg", backorder);
								BtXiMeiService.this.sendBroadcast(intent);

							}

						}

						// 新中继器修改中继器地址操作
						if (sendorder.equals("02")||sendorder.equals("03")) {

							if (backorder.equals("F0")) {

								Intent intent = new Intent(
										"android.com.tiny.action.changenub");
								intent.putExtra("backdatalen",
										String.valueOf(backdatalen));
								intent.putExtra("backmsg", backorder);
								intent.putExtra("resrandrand", backrand);
								BtXiMeiService.this.sendBroadcast(intent);

							}

						}
						// 新中继器查询状态返回操作

						if (sendorder.equals("05") || sendorder.equals("07")|| sendorder.equals("09")) {

							if (backorder.equals("F0")) {

								Intent intent = new Intent();
								intent.setAction("android.com.tiny.action.queryzt");
								intent.putExtra("resmsg", backdatamsg);
								intent.putExtra("resrand",backrand);
								// intent.putExtra("qbbh", sendaddress);
								BtXiMeiService.this.sendBroadcast(intent);

							}
						}

						// 快速点对点抄，发送快速抄表广播
						if ((sendorder.equals("A4"))&& (!backdatamsg.equals(""))&& (!backorder.equals("21"))&& (backcrcmsg.equals(backcrc))) {
							Intent intent = new Intent("android.intent.action.MY_BROADCAST");
							intent.putExtra("msg", backdatamsg);
							intent.putExtra("allmsg", backinfo);
							intent.putExtra("qbbh", sendaddress);
							intent.putExtra("resrand", backrand);
							BtXiMeiService.this.sendBroadcast(intent);
						}

						// 对中集器请求操作返回信息 BackJiZhongQiActivity
						if (backorder.equals("DE")) {
							Intent intent = new Intent("android.intent.action.JZqingqiu_BROADCAST");
							intent.putExtra("backmsg", backdatamsg);
							BtXiMeiService.this.sendBroadcast(intent);
						}
						// 发送采集抄表数据广播，CaiJiService用到
						if (backorder.equals("C6")) {
							Intent intent = new Intent("android.intent.action.databackinfo");
							intent.putExtra("datalen", backdatastr);
							intent.putExtra("backmsg", backdatamsg);
							BtXiMeiService.this.sendBroadcast(intent);
						}
						// 发送采集组网表号广播，CaiJiService用到
						if (BtXiMeiService.this.backorder.equals("C8")) {
							Intent intent = new Intent("android.intent.action.databackqbbh");
							intent.putExtra("datalen", backdatastr);
							intent.putExtra("backmsg", backdatamsg);
							BtXiMeiService.this.sendBroadcast(intent);
						}
						if (sendorder.equals("0C")) {   //抄表测试
							
							if (backorder.equals("F0")) {
								Intent intent = new Intent();
								intent.setAction("android.intent.action.putongcb_BROADCAST");
								intent.putExtra("resmsg", backdatamsg);
								intent.putExtra("sendorder","0C");
								intent.putExtra("qbbh", sendaddress);
								intent.putExtra("resrand", backrand);
								BtXiMeiService.this.sendBroadcast(intent);
							}
						}						

						// 修改表地址返回信息,出厂设置
						if (((sendorder.equals("E9"))||sendorder.equals("A3"))&& (backcrc.equals(backcrcmsg))) {
							Intent intent = new Intent();
							intent.putExtra("backorder", backorder);
							intent.setClass(BtXiMeiService.this,BackInFoActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							BtXiMeiService.this.startActivity(intent);
						}
						// 设置表地址操作
						if ((sendorder.equals("EB"))&& (backcrc.equals(backcrcmsg))&& (sendsetstr.equals(backaddress))) {
							Intent intent = new Intent();
							intent.putExtra("backorder", backorder);
							intent.setClass(BtXiMeiService.this,BackInFoActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							BtXiMeiService.this.startActivity(intent);

						}
						// 设置链路操作
						if (BtXiMeiService.this.backorder.equals("C7")) {
							Intent intent = new Intent("android.com.tiny.action.setlianlu");
							intent.putExtra("backorder", backorder);
							BtXiMeiService.this.sendBroadcast(intent);
						}

						if ((backcrc.equals(backcrcmsg))&& (sendaddress.equals(backaddress))&& (backhead.equals("FDFD"))) {
							// 普通气表查询返回信息，发送普通抄表广播
							if ((sendorder.equals("9A"))&& (!backdatamsg.equals(""))&& (backcrcmsg.equals(backcrc))) {
								Thread.sleep(400);
								Intent intent = new Intent();
								intent.setAction("android.intent.action.putongcb_BROADCAST");
								
								intent.putExtra("resmsg", backdatamsg);
								intent.putExtra("allmsg", backinfo);
								//intent.putExtra("sendorder","9A");
								intent.putExtra("sendorder",backorder);
								intent.putExtra("qbbh", sendaddress);
								BtXiMeiService.this.sendBroadcast(intent);

							}

							// 2014-12-1测试远传充值，开气

							if (sendorder.equals("48")|| sendorder.equals("42")|| sendorder.equals("45")) {
								Intent intent = new Intent();
								intent.putExtra("backorder", backorder);
								intent.setClass(BtXiMeiService.this,BackInFoActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								BtXiMeiService.this.startActivity(intent);

							}
							/*
							 * // 气表维护.机电同步.气表初始化.出厂清除返回信息 if
							 * ((sendorder.equals("51")) ||
							 * (sendorder.equals("CC")) ||
							 * (sendorder.equals("41")) ||
							 * (sendorder.equals("A3"))) { Intent intent = new
							 * Intent(); intent.putExtra("backorder",
							 * backorder); intent.setClass(BtXiMeiService.this,
							 * BackInFoActivity.class);
							 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							 * BtXiMeiService.this.startActivity(intent); }
							 */
							// 2015-04-23大坪管理站修改表册关阀
							if ((sendorder.equals("51"))|| (sendorder.equals("9B"))|| (sendorder.equals("CC"))|| (sendorder.equals("41")||(sendorder.equals("80")))) {
								Intent intent = new Intent();
								intent.putExtra("backorder", backorder);
								intent.setClass(BtXiMeiService.this,BackInFoActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								BtXiMeiService.this.startActivity(intent);
								// new Thread(new SendKGFThread()).start();
								Intent intent1 = new Intent();
								intent1.setAction("com.ximei.tiny.daping_BCQG");
								intent1.putExtra("backorder", backorder);
								intent1.putExtra("qbbh", sendaddress);
								BtXiMeiService.this.sendBroadcast(intent1);
								Log.e("test", "发送关阀广播");
							}

						}
						headstr = "";
						endstr = "";

					} catch (Exception e) {
						Log.e("test", "返回数据错误");
					}

				}
				break;

			case MSG_WRITE:
				// 2015-2-4更新新旧抄表
				// 得到发送的表号
				try {
					allorer = order.substring(0, order.length() - 5);					
					if(MeterType==true)
					{
						writer("|" + allorer + "5B5B01"+FreqStr+"0100000000000003A9/");
					    break;
					}
					String qbbhstr = String.valueOf(Integer.parseInt(getmsg.getMsgID(sendaddress), 16));
					if(sendorder.equals("EB")){
						qbbhstr = String.valueOf(Integer.parseInt(getmsg.getMsgID(sendsetstr), 16));
					}
					// 把表号转换为8位
					String qbbh = getqbbh.GetQbbh(qbbhstr, 8);
					// 得到qbbh的第四位转化为String类型
					String comqbbh = String.valueOf(qbbh.charAt(3));
					// 得到qbbh的第3位转化为int类型
					int  year = Integer.parseInt(String.valueOf(qbbh.charAt(2)));
					// 得到前4位是0349的表号
					// String comqbbh1 = qbbh.substring(0, 4);
					if(qbbh.substring(0, 3).equals("080")||qbbh.substring(0, 3).equals("090")||qbbh.substring(0, 3).equals("100")
							||qbbh.substring(0, 3).equals("110")||qbbh.substring(0, 3).equals("120")||qbbh.substring(0, 3).equals("010"))
					{						
						Log.e("test", "新表命令");
						writer("|" + allorer + "5B5B01/");						
					}
					else if (sendhead.equals("5A5A00FE02")) {
						writer("|" + allorer + "5B5B01/");
					} else if (metertype.equals("mrmeter")) {
						if (contain.isHave(rowdata, qbbh) ||((year==5||year==6)&&(comqbbh.equals("9")||comqbbh.equals("8")))) {
						//if(true){
							// 发送新表命令
							Log.e("test", "新表命令");
							writer("|" + allorer + "5B5B01/");
						} else if (sendorder.equals("A8")) {
							// 发送新表命令
							Log.e("test", "A8命令");
							writer("|" + allorer + "5B5B01/");
						} else {
							// 发送旧表命令
							Log.e("test", "旧表命令");
							writer("|" + allorer + "5B5B00/");
						}

					} else if (metertype.equals("oldmeter")) {
						// 发送旧表命令
						Log.e("test", "旧表命令");
						writer("|" + allorer + "5B5B00/");
					} else if (metertype.equals("newmeter")) {
						// 发送新表命令
						Log.e("test", "新表命令");
						writer("|" + allorer + "5B5B01/");
					}

				} catch (Exception e) {
					Log.e("test", "异常发送新表命令");
					writer("|" + allorer + "5B5B01/");
				}
				// writer("|" + order);
				break;
			case 6:
				//Log.e("test", "开始连接");
				break;
			case 7:
				Log.e("test", "连接成功");
				writer("|" + "5A5A00FE0009000000000048E2AA40F9035B5B01" + "/");  //电池电压检测
				// 开启通信线程
				new Thread(new readThread()).start();
				break;
			case 8:
				//Log.e("test", "连接失败");
				break;
			}
			super.handleMessage(msg);
		}

	};

	// 广播接收器监听filter事件处理
	private final BroadcastReceiver Connectstate = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
				Log.e("test", "连接成功状态");
				localAlertDialog.cancel();
				// 蓝牙连接成功弹出提示框，
				localBuilder.setTitle("提示");
				localBuilder.setPositiveButton("确定", null);
				localBuilder.setIcon(17301659);
				localBuilder.setMessage("蓝牙连接成功");
				localAlertDialog = localBuilder.create();
				localAlertDialog.getWindow().setType(2003);
				localAlertDialog.show();
				firstFlag=false;
				conflag = 1;
			} else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
				// Log.e("test", "连接失败状态");
				conflag = 0;
				localAlertDialog.cancel();
				try {
					mmSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 蓝牙连接断开弹出提示框，
				localBuilder.setTitle("提示");
				localBuilder.setPositiveButton("确定", null);
				localBuilder.setIcon(17301659);
				localBuilder.setMessage("蓝牙连接断开，怎在重新连接");
				localAlertDialog = localBuilder.create();
				localAlertDialog.getWindow().setType(2003);
				localAlertDialog.show();
				// 重新连接蓝牙设备
				new Thread(new ATconnect()).start();
			} else if ("android.intent.action.disbtconnect".equals(action)) {

				try {
					Log.e("test", "收到蓝牙断开广播");
					if (mmSocket != null) {
						mmSocket.close();
					}
					stopSelf();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}else if("android.intent.action.busy".equals(action)){
				if(intent.getStringExtra("State").equals("busy"))
				  Idle=false;
				else
				  Idle=true;	
			}

		}

	};

	// 自动8秒一次连接蓝牙模块线程
	public class ATconnect implements Runnable {

		public void run() {
			// TODO Auto-generated method stub

			while (true) {

				new Thread(new ConnectThread(btDev)).start();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (conflag == 1) {

					break;
				}
			}

		}

	}
	public class BattDect implements Runnable{

		@Override
		public void run() {
			// TODO 自动生成的方法存根
		  while(true)
		  {
			try {
				//if(conflag==1)
				{
				  if(firstFlag==false)
				  {
					firstFlag=true;
					Thread.sleep(1000);
				  }
				  else
				  {
					 Thread.sleep(10000);    
				  }
				  if(Idle==true)
				  {
				    writer("|" + "5A5A00FE0009000000000048E2AA40F9035B5B01" + "/");  //电池电压检测
					String log=fileopertion.getCurTime()+"发送:\r\n"+"5A5A00FE0009000000000048E2AA40F9035B5B01/";
					fileopertion.writeTxtToFile(log);
				  }
				}
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		  }
		}
		
	}

	public class SendKGFThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(2000);
				Intent intent1 = new Intent();
				intent1.setAction("com.ximei.tiny.daping_BCQG");
				intent1.putExtra("backorder", backorder);
				intent1.putExtra("qbbh", sendaddress);
				BtXiMeiService.this.sendBroadcast(intent1);
				Log.e("test", "发送关阀广播");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	// 加载原始数据线程

	public class LoadDataThrad implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			// 2015-01-22更改内容（新表和旧表都可以抄表）
			// 得到原始表册的全路径

			try {
				rowdata = GetRawData.RawData();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("test", "原始表册乜有" + e.toString());
			}

		}

	}

	// 作为客户端连接蓝牙线程
	public class ConnectThread implements Runnable {

		public ConnectThread(BluetoothDevice device) {

			BluetoothSocket tmp = null;
			// mmDevice = device;

			try {

				tmp = device.createRfcommSocketToServiceRecord(uuid);

			} catch (IOException e) {
			}
			mmSocket = tmp;
		}

		public void run() {

			btAdapt.cancelDiscovery();

			try {

				handler.obtainMessage(6).sendToTarget();
				mmSocket.connect();
				Thread.sleep(1000);
				handler.obtainMessage(7, mmSocket).sendToTarget();
				// conflag=1;

			} catch (IOException | InterruptedException connectException) {
				// Unable to connect; close the socket and get out
				try {
					mmSocket.close();
				} catch (IOException closeException) {
				}

				handler.obtainMessage(8).sendToTarget();
				return;

			}

		}

	}

	// 读取数据
	private class readThread extends Thread {
		public void run() {
			Log.e("test", "进入读取数据线程");
			byte[] buffer = new byte[1024];
			int bytes;
			InputStream mmInStream = null;

			try {

				mmInStream = mmSocket.getInputStream();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.e("test", "开始循环读取");
			while (true) {
				try {
					// Read from the InputStream
					// Log.e("test", "判断多少个字节");
					if (mmInStream == null) {

						break;
					}
					if ((bytes = mmInStream.read(buffer)) > 0) {
						// Log.e("test", String.valueOf(bytes));
						byte[] buf_data = new byte[bytes];
						for (int i = 0; i < bytes; i++) {
							buf_data[i] = buffer[i];
						}
						// Log.e("test", "发送到ui");
						String s = new String(buf_data);
						// Log.e("test", s);
						Message msg = new Message();

						msg.obj = buf_data;
						msg.what = MSG_READ;
						handler.sendMessage(msg);
						// Log.e("test", "发送完成");
					}

				} catch (IOException e) {
					try {
						mmInStream.close();

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				}
			}
		}
	}

	// 发送数据
	private void writer(String msg) {
		if (mmSocket == null) {

			return;
		}
		try {
			OutputStream os = mmSocket.getOutputStream();
			os.write(msg.getBytes());
			Log.e("test", "发送模块" + msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 发送线程
	Runnable r = new Runnable() {

		// @Override
		public void run() {
			// TODO Auto-generated method stub

			Message msg = handler.obtainMessage();
			msg.what = MSG_WRITE;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			handler.sendMessage(msg);

		}

	};

}
