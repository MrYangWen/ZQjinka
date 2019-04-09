package com.ximei.tiny.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.FileUtils;
import com.ximei.tiny.tools.GetmsgID;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/*
 * 用于和模块通信的service
 * 还用handler，BroadcastReceiver,更新activity
 * 
 * 
 */
public class XimeiService extends Service {
	private static final String TAG = "XiMeiDemo";
	private final static int USBAccessoryWhat = 0;
	public static final int UPDATE_LED_SETTING = 1;
	public static final int PUSHBUTTON_STATUS_CHANGE = 0x34;
	public static final int POT_STATUS_CHANGE = 3;
	public static final int APP_CONNECT = (int) 0xFE;
	public static final int APP_DISCONNECT = (int) 0xFF;
	public static final int LED_0_ON = 0x01;
	public static final int MSG_VALUE = 5;
	public static final int POT_UPPER_LIMIT = 100;
	public static final int POT_LOWER_LIMIT = 0;

	private boolean deviceAttached = false;
	private String order, sendaddress, backaddress, backcrc,
			sendorder, backorder,senddatastr;
	private String backdatastr, backdatamsg, backcrcstr, backcrcmsg,
			 sendsetstr, backhead;
	private int backdatalen,senddatalen;

	private USBAccessoryManager accessoryManager;
	private CRC crc;
	GetmsgID getmsg;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		// 启动USBAccessoryManager类
		super.onCreate();
		accessoryManager = new USBAccessoryManager(handler, USBAccessoryWhat);
		crc = new CRC();
		getmsg = new GetmsgID();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		Log.d(TAG, "start service");

		accessoryManager.enable(this, intent);
		order = intent.getStringExtra("order");
		// 发送命令字
		sendorder = order.substring(18, 20);
		// 发送数据表地址
		sendaddress = order.substring(12, 18);
		// 发送数据长度
		senddatalen = Integer.parseInt(order.substring(20, 22), 16);
		// 发送数据内容
		senddatastr = order.substring(22, 22+2*senddatalen);
		// 设置表地址操作发送表地址
		sendsetstr = order.substring(22, 28);

		handler.post(r);

		return super.onStartCommand(intent, flags, startId);

	}

	public void disconnectAccessory() {
		if (deviceAttached == false) {
			return;
		}

		Log.d(TAG, "disconnectAccessory()");

		Log.d(TAG, "没有连接");

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 用于向模块写数据的commandPacket
			byte[] commandPacket = new byte[1024];
			// byte[] ReadMeterPacket = new byte[20];

			switch (msg.what) {

			case MSG_VALUE:

				if (accessoryManager.isConnected() == false) {
					return;
				}

				commandPacket = order.getBytes();
				accessoryManager.write(commandPacket);

				break;

			case USBAccessoryWhat:
				switch (((USBAccessoryManagerMessage) msg.obj).type) {
				case READ:
					if (accessoryManager.isConnected() == false) {
						return;
					}
					try {

						while (true) {
							if (accessoryManager.available() < 2) {

								break;
							}

							accessoryManager.read(commandPacket);

							String backinfo = new String(commandPacket);

							// 返回协议头
							backhead = backinfo.substring(0, 4);
							// 返回表地址
							backaddress = backinfo.substring(6, 12);
							// 返回数据长度
							backdatastr = backinfo.substring(14, 16);
							// 返回数据命令
							backorder = backinfo.substring(12, 14);

							// 返回数据长度转化为整型
							backdatalen = Integer.parseInt(backdatastr, 16);
							// 返回CRC应校验的字符串
							backcrcstr = backinfo.substring(4,
									16 + 2 * backdatalen);
							// 返回CRC的值
							backcrcmsg = backinfo.substring(
									16 + 2 * backdatalen, 20 + 2 * backdatalen);
							// 返回数据内容
							backdatamsg = backinfo.substring(16,
									16 + 2 * backdatalen);

							// 通过计算得出返回CRC的值
							backcrc = crc.CRC_CCITT(1, backcrcstr)
									.toUpperCase();
							
							
							  //数据存入sdcard
						    FileUtils fileutils = new FileUtils();					    
						    File file1;
							try {
								file1 = fileutils
										.creatSDFile("newximei/backdata.txt");
								FileWriter filewriter1 = new FileWriter(file1);
								filewriter1.write(backinfo);
								filewriter1.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							
							

							// 返回命令为"c3"的对集中器的操作，BackCaiJiInFoActivity处用到
							if (backorder.equals("C3")) {
								Intent intent = new Intent(
										"android.com.tiny.action.backc3order");
								intent.putExtra("backmsg", backdatamsg);
								XimeiService.this.sendBroadcast(intent);
							}
							// 测试组网，表示组网结果的返回
							if ((backorder.equals("C3"))
									|| (backorder.equals("C9"))) {
								Intent intent = new Intent(
										"android.com.tiny.action.testzw");
								intent.putExtra("backmsg", backdatamsg);
								intent.putExtra("backorder", backorder);
								XimeiService.this.sendBroadcast(intent);
							}
							// 测试组网，表示正在组网广播
							if (backhead.equals("FE01")) {
								Intent intent = new Intent(
										"android.com.tiny.action.testfeo1");
								intent.putExtra("backmsg", backorder);
								XimeiService.this.sendBroadcast(intent);
							}
							// 对中集器请求操作返回信息 BackJiZhongQiActivity
							if (backorder.equals("DE")) {
								Intent intent = new Intent(
										"android.intent.action.JZqingqiu_BROADCAST");
								intent.putExtra("backmsg", backdatamsg);
								XimeiService.this.sendBroadcast(intent);
							}
							// 发送采集抄表数据广播，CaiJiService用到
							if (backorder.equals("C6")) {
								Intent intent = new Intent(
										"android.intent.action.databackinfo");
								intent.putExtra("datalen", backdatastr);
								intent.putExtra("backmsg", backdatamsg);
								XimeiService.this.sendBroadcast(intent);
							}
							// 发送采集组网表号广播，CaiJiService用到
							if (XimeiService.this.backorder.equals("C8")) {
								Intent intent = new Intent(
										"android.intent.action.databackqbbh");
								intent.putExtra("datalen", backdatastr);
								intent.putExtra("backmsg", backdatamsg);
								XimeiService.this.sendBroadcast(intent);
							}
							// 修改表地址返回信息
							if ((sendorder.equals("E9"))
									&& (backcrc.equals(backcrcmsg))) {
								Intent intent = new Intent();
								intent.putExtra("backorder", backorder);
								//intent.setClass(XimeiService.this,BackInFoActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								XimeiService.this.startActivity(intent);
							}
							// 设置表地址操作
							if ((sendorder.equals("EB"))
									&& (backcrc.equals(backcrcmsg))
									&& (sendsetstr.equals(backaddress))) {
								Intent intent = new Intent();
								intent.putExtra("backorder", backorder);
								//intent.setClass(XimeiService.this,BackInFoActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								XimeiService.this.startActivity(intent);

							}
							// 设置链路操作
							if (XimeiService.this.backorder.equals("C7")) {
								Intent intent = new Intent(
										"android.com.tiny.action.setlianlu");
								intent.putExtra("backorder", backorder);
								XimeiService.this.sendBroadcast(intent);
							}

							if ((backcrc.equals(backcrcmsg))
									&& (sendaddress.equals(backaddress))
									&& (backhead.equals("FDFD"))) {
								// 普通气表查询返回信息，发送普通抄表广播
								if ((sendorder.equals("9A"))
										&& (!backdatamsg.equals(""))) {
									

									Intent intent = new Intent();
									intent.setAction("android.intent.action.putongcb_BROADCAST");
									intent.putExtra("resmsg", backdatamsg);
									intent.putExtra("qbbh", sendaddress);
									XimeiService.this.sendBroadcast(intent);

								}
								// 快速点对点抄，发送快速抄表广播
								if ((sendorder.equals("A4"))
										&& (!backdatamsg.equals(""))
										&& (!backorder.equals("21"))) {
									Intent intent = new Intent(
											"android.intent.action.MY_BROADCAST");
									intent.putExtra("msg", backdatamsg
											+ backaddress);
									XimeiService.this.sendBroadcast(intent);
								}
								// 气表维护.机电同步.气表初始化.出厂清除返回信息
								if ((sendorder.equals("51"))
										|| (sendorder.equals("CC"))
										|| (sendorder.equals("41"))
										|| (sendorder.equals("A3"))) {
									Intent intent = new Intent();
									intent.putExtra("backorder", backorder);
									//intent.setClass(XimeiService.this,BackInFoActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									XimeiService.this.startActivity(intent);
								}
								
								// 调价操作
								if (sendorder.equals("80")) {
									Intent intent = new Intent();
									intent.putExtra("backorder", backorder);
									if(backorder.equals("F0")){
										 SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
									     String Timemsg = localSimpleDateFormat.format(new Date());
										 String qbbh = String.valueOf(Integer.parseInt(getmsg.getMsgID(backaddress), 16));
										 String price = String.valueOf(Integer.parseInt(getmsg.getMsgID(senddatastr.substring(0, 6)), 16));
										try {
											file1 = fileutils
													.creatSDFile("newximei/price.txt");
											FileWriter filewriter1 = new FileWriter(file1,true);
											filewriter1.write(Timemsg+"表号"+qbbh+"价格："+price+"分/方"+"调价成功\r\n");
											filewriter1.close();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										
										
										
									}
									//intent.setClass(XimeiService.this,BackInFoActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									XimeiService.this.startActivity(intent);
								}
							}

						}
					} catch (Exception e) {

						Log.d(TAG, "backdata error");

					}
					break;
				case CONNECTED:
					break;
				case READY:
					Intent localIntent2 = new Intent(
							"android.intent.action.connected_BROADCAST");
					XimeiService.this.sendBroadcast(localIntent2);

					break;
				case DISCONNECTED:
					disconnectAccessory();
					// usb连接线断开弹出提示框，
					AlertDialog.Builder localBuilder = new AlertDialog.Builder(
							XimeiService.this.getApplicationContext());
					localBuilder.setTitle("提示");
					localBuilder.setPositiveButton("确定", null);
					localBuilder.setIcon(17301659);
					localBuilder.setMessage("USB连接断开，请重新连接");
					AlertDialog localAlertDialog = localBuilder.create();
					localAlertDialog.getWindow().setType(2003);
					localAlertDialog.show();
					// 发送usb断开广播
					accessoryManager.disable(XimeiService.this);
					Intent localIntent1 = new Intent(
							"android.intent.action.disconnected_BROADCAST");
					XimeiService.this.sendBroadcast(localIntent1);
					break;
				}

				break;
			default:
				break;
			} // switch
		} // handleMessage
	}; // handler

	Runnable r = new Runnable() {

		// @Override
		public void run() {
			// TODO Auto-generated method stub

			Message msg = handler.obtainMessage();
			msg.what = MSG_VALUE;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			handler.sendMessage(msg);

		}

	};

}
