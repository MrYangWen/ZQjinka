package com.ximei.tiny.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ximei.tiny.backinfoview.BackJiZhongQiActivity.sendthread;
import com.ximei.tiny.database.BDhelper;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.Comparelist;
import com.ximei.tiny.tools.Datatimer;
import com.ximei.tiny.tools.FileOpertion;
import com.ximei.tiny.tools.FileUtils;
import com.ximei.tiny.tools.GetTotalPack;
import com.ximei.tiny.tools.GetmsgID;
import com.ximei.tiny.tools.ToInverted;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/*
 * 处理集中器操作service
 * 
 * 
 * 
 */
public class CaijiService extends Service {

	int backinfoflag;
	String backmsg = "";
	Comparelist comparelist;
	CRC crc;
	Cursor cursor;
	String databasename;
	Datatimer datatimer;
	SQLiteDatabase dbreader;
	SQLiteDatabase dbwriter;
	FileUtils fileutils;
	GetmsgID getmsg;
	GetTotalPack gettotalpack;
	String headmsg;
	int index ;
	int nowpack;
	private String order;
	Intent sendintent;
	String target;
	List<Integer> temp = new ArrayList<Integer>();
	ToInverted toinver;
	int total;
	FileWriter WriterPacket;
	private Thread SendThread;
	FileOpertion fileopertion=new FileOpertion();
	public IBinder onBind(Intent paramIntent) {
		return null;
	}

	public void onCreate() {
		super.onCreate();
		// 启动service是初始化数据
		this.headmsg = "5A5A00FE02";
		this.crc = new CRC();
		this.fileutils = new FileUtils();
		this.toinver = new ToInverted();
		this.getmsg = new GetmsgID();
		this.gettotalpack = new GetTotalPack();
		this.comparelist = new Comparelist();
		this.sendintent = new Intent();
		this.backinfoflag = 0;
		File file1;
		try {
			file1 = fileutils.creatSDFile("alldata/backinfo.txt");
			WriterPacket = new FileWriter(file1,true);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 注册采集抄表数据，组网表号广播
		IntentFilter localIntentFilter1 = new IntentFilter();
		localIntentFilter1.addAction("android.intent.newxmr.cjcbsj");
		localIntentFilter1.addAction("android.intent.action.databackinfo");
		localIntentFilter1.addAction("android.intent.action.databackqbbh");
		registerReceiver(this.backinfoReceiver, localIntentFilter1);
		// 注册停止service广播
		MyReceiver1 localMyReceiver1 = new MyReceiver1();
		IntentFilter localIntentFilter2 = new IntentFilter();
		localIntentFilter1.addAction("android.intent.action.MY_BROADCAST2");
		registerReceiver(localMyReceiver1, localIntentFilter2);
	}

	public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
		//得到intent传递的数据发送到通信service（BtXiMeiService）
		this.order = paramIntent.getStringExtra("order");
		this.target = paramIntent.getStringExtra("target");
		this.databasename = paramIntent.getStringExtra("databasename");
		index = 0;
		paramIntent.putExtra("order", order);
		paramIntent.setClass(this, BtXiMeiService.class);
		startService(paramIntent);
		//得到对数据库读写的操作权限
		BDhelper localBDhelper = new BDhelper(this, this.databasename);
		this.dbreader = localBDhelper.getReadableDatabase();
		this.dbwriter = localBDhelper.getWritableDatabase();
		return super.onStartCommand(paramIntent, paramInt1, paramInt2);
	}
   //停止service广播
	private class MyReceiver1 extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getStringExtra("stoporder").equals("stop"))
				CaijiService.this.stopSelf();
		}
	}
   //接受采集抄表数据和组网表号广播
	private BroadcastReceiver backinfoReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			try{
			    //新amr返回数据
			    if(intent.getAction().equals("android.intent.newxmr.cjcbsj")){	
				String backdatalen=intent.getStringExtra("backdatalen");
				backmsg=intent.getStringExtra("backdatamsg");
				Log.e("test", "数据长度"+backdatalen);
//				//启动时间线程定时器
//				SendThread = new Thread(new sendthread());
//				SendThread.start();	
				if(backdatalen.equals("20")){
					//000A 0000 000A 000A FFFF FFFF 02B9 DA4C5B5B	
				//CC1101表总数
					String CCtotalstr = backmsg.substring(0, 4);
					int cctotal = Integer.parseInt(CCtotalstr, 16);
					Log.e("test", "1101总数"+String.valueOf(cctotal));
					
				//SX1278表总数
					String SXtotalstr = backmsg.substring(4, 8);
					int sxtotal = Integer.parseInt(SXtotalstr, 16);	
					Log.e("test", "1278总数"+String.valueOf(sxtotal));
				
			    //上次cc	1101集抄总数
					
					String SCCCtotalstr = backmsg.substring(12, 16);
					int sccctotal = Integer.parseInt(SCCCtotalstr, 16);
                 //上次cc1101集抄成功数
					
					String ccjcstr = backmsg.substring(16, 20);
					int ccjc = Integer.parseInt(ccjcstr, 16);
					if(ccjc==65535){
						ccjc=0;
					}
					Log.e("test", "1101成功"+String.valueOf(ccjc));
				  //上次sx1278集抄总数
					
					String SCSXtotalstr = backmsg.substring(16, 20);
					int scsxtotal = Integer.parseInt(SCSXtotalstr, 16);
					
	               //上次PICsx1278集抄成功数
						
					String sxjcstr = backmsg.substring(24, 28);
					int sxjc = Integer.parseInt(sxjcstr, 16);
					if(sxjc==65535){
						sxjc=0;
					}
		            //上次MSP1278集抄成功数
					String mspsxjcstr = backmsg.substring(32, 36);
					int mspsxjc = Integer.parseInt(mspsxjcstr, 16);
					if(mspsxjc==65535){
						mspsxjc=0;
					}					
					Log.e("test", "1278成功"+String.valueOf(mspsxjc));
				  //当前电池电压
					String batterystr = backmsg.substring(36, 40);
					double battery = Integer.parseInt(batterystr, 16)/100.0;
					Log.e("test", "电池"+String.valueOf(battery));
					total=sxjc+ccjc+mspsxjc;				
//					// 写入表号和集中器地址到txt文件
//					try {
//					
//						WriterPacket.write(String.valueOf(total)+ "|");
//						
//
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}	
				}
				if(backdatalen.equals("30")){	
					index++;
					//结束时间线程
//					SendThread.interrupt();
//					try {
//						SendThread.join();
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					String nowpackstr = backmsg.substring(2,6);
					nowpack = Integer.parseInt(nowpackstr, 16);
					Log.e("test", "当前包"+String.valueOf(nowpack));				
					// 返回气表表号
					String qbbhhex = backmsg.substring(8, 14);
					String qbbhstr = String.valueOf(Integer.parseInt(getmsg.getMsgID(qbbhhex), 16));
					Log.e("test", "表号"+qbbhstr);	
					//返回抄表日期
					String cbdate=backmsg.substring(14, 20);
					Log.e("test", cbdate);		
					//返回抄表时间
					String cbtime=backmsg.substring(22, 28);
					Log.e("test", cbtime);
					
					// 返回累计气量
					int leiji = Integer.parseInt(toinver.toinverted(backmsg.substring(28, 36)), 16);
					Log.e("test", "累计气量"+String.valueOf(leiji));
					// 返回剩余气量
					long shengyu = Long.parseLong(toinver.toinverted(backmsg.substring(36, 44)), 16);
					if (shengyu > 0x7fffffffL) {
						shengyu = shengyu - Long.parseLong("FFFFFFFF", 16);
					}	
					// 返回机电同步气量
					int jidian = Integer.parseInt(toinver.toinverted(backmsg.substring(44, 52)), 16);
					Log.e("test", "机电同步气量"+String.valueOf(jidian));
					// 返回气表状态
					int state = Integer.parseInt(toinver.toinverted(backmsg.substring(52, 56)), 16);
					// 返回充值次数
					int czcs =Integer.parseInt(toinver.toinverted(backmsg.substring(56, 60)), 16);
					// 得到最后抄表数据写入数据库
					String qbql = Integer.toString( (leiji + jidian) / 10);
					String log="采集数据："+backmsg+"\r\n"+"累计气量："+leiji+"qbbh:"+qbbhstr+"抄表时间"+cbdate+cbtime+"\r\n";
					fileopertion.writeTxtToFile(log);						
					Log.e("test", "气量"+qbql);
					ContentValues contentvalues1 = new ContentValues();
					contentvalues1.put("qbztbh", "已抄");
					contentvalues1.put("qbql", qbql);
					String str="20"+cbdate+" "+cbtime.substring(0, 2)+":"+cbtime.substring(2, 4)+":"+cbtime.substring(4, cbtime.length());
					contentvalues1.put("cbjlid", str);
					dbwriter.update("ximeitable", contentvalues1, "qbbh=?",new String[] { qbbhstr });		
					//写入包数地址到txt文件
					try {				
						WriterPacket.write( qbbhstr+"气量"+qbql+"|");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 发送广播到Activity
					sendintent.setAction("android.com.tiny.sendtotalpack");
					sendintent.putExtra("totalpack", total);
					sendintent.putExtra("nowpack", nowpack);
					sendBroadcast(sendintent);		
                   if(nowpack==total){	
   					try {
						WriterPacket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
   					Intent intent1 = new Intent();
  					intent1.setAction("android.intent.action.caijifinishflag");
  					CaijiService.this.sendBroadcast(intent1);  
					}
                   
                 //启动时间线程定时器
   				 // SendThread.start();
					
				}									
			}

			if (intent.getAction().equals("android.intent.action.databackinfo")) {
				if (intent.getStringExtra("datalen").equals("07")) {
					Intent intent2 = new Intent("android.com.tiny.nodata");
					sendBroadcast(intent2);
				} else { // 得到返回所有数据
					backmsg = intent.getStringExtra("backmsg");
					// 返回本机地址
					String localadd = String.valueOf(Integer.parseInt(getmsg.getMsgID(backmsg.substring(0, 6)), 16));
					// 返回总包数
					String totalstr = toinver.toinverted(backmsg.substring(6, 8));
					total = Integer.parseInt(totalstr, 16);
					// 返回当前包数
					String nowpackstr = toinver.toinverted(backmsg.substring(8,10));
					nowpack = Integer.parseInt(nowpackstr, 16);
					// 返回抄表完成时间
					long cbsj = Integer.parseInt(toinver.toinverted(backmsg.substring(10, 16)), 16);
					// 得到手机取数据时间
					SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					String getdatatime = simpledateformat.format(new Date());

					// 返回所属中继器地址
					String zjqadd = String.valueOf(Integer.parseInt(toinver.toinverted(backmsg.substring(16, 22)), 16));
					// 返回气表表号
					String qbbhhex = backmsg.substring(22, 28);
					String qbbhstr = String.valueOf(Integer.parseInt(getmsg.getMsgID(qbbhhex), 16));
					// 返回数据内容命令字
					String backorder = toinver.toinverted(backmsg.substring(28,
							30));
					// 返回数据内容长度
					long datalen = Integer.parseInt(toinver.toinverted(backmsg.substring(30, 32)), 16);

					// 返回累计气量
					int leiji = Integer.parseInt(toinver.toinverted(backmsg.substring(32, 40)), 16);
					// 返回剩余气量
					long shengyu = Long.parseLong(toinver.toinverted(backmsg.substring(40, 48)), 16);
					if (shengyu > 0x7fffffffL) {
						shengyu = shengyu - Long.parseLong("FFFFFFFF", 16);
					}
					// 返回机电同步气量
					int jidian = Integer.parseInt(toinver.toinverted(backmsg.substring(48, 56)), 16);
					// 返回气表状态
					Integer.parseInt(toinver.toinverted(backmsg.substring(56, 60)), 16);
					// 返回充值次数
					Integer.parseInt(toinver.toinverted(backmsg.substring(60, 64)), 16);
					// 得到最后抄表数据写入数据库
					String qbql = Float.toString((float) (leiji + jidian) / 10F);
					ContentValues contentvalues1 = new ContentValues();
					contentvalues1.put("qbztbh", "已抄");
					contentvalues1.put("qbql", qbql);
					contentvalues1.put("cbjlid", getdatatime);
					contentvalues1.put("flag", zjqadd);
					dbwriter.update("ximeitable", contentvalues1, "qbbh=?",new String[] { qbbhstr });	
					// 发送广播到Activity
					sendintent.setAction("android.com.tiny.sendtotalpack");
					sendintent.putExtra("totalpack", total);
					sendintent.putExtra("nowpack", index);
					sendBroadcast(sendintent);
				}

			}

			if (intent.getAction().equals("android.intent.action.databackqbbh")) {

				if (intent.getStringExtra("datalen").equals("07")) {
					Intent intent2 = new Intent("android.com.tiny.nodata");
					sendBroadcast(intent2);
				} else {
					String backdata = intent.getStringExtra("backmsg");
					// 返回总包数
					String totalstr = toinver.toinverted(backdata.substring(6,8));
					total = Integer.parseInt(totalstr, 16);
					// 返回先传包数
					String nowpackstr = toinver.toinverted(backdata.substring(8, 10));
					nowpack = Integer.parseInt(nowpackstr, 16);
					// 节点地址
					String jiedianadd = String.valueOf(Integer.parseInt(toinver.toinverted(backdata.substring(10, 16)), 16));
					// 气表地址总数
					String allqbbh = backdata.substring(20, backdata.length());
					// 所有十进制表示的表号
					String qbbhstr = "";
					// 写入中继器地址到数据库
					for (int i = 0; i < allqbbh.length() / 6; i++) {
						String qbbhhex = allqbbh.substring(i * 6, (i + 1) * 6);
						String qbbh = String.valueOf(Integer.parseInt(getmsg.getMsgID(qbbhhex), 16));
						ContentValues localContentValues1 = new ContentValues();
						localContentValues1.put("flag", jiedianadd);
						dbwriter.update("ximeitable", localContentValues1,"qbbh=?", new String[] { qbbh });
						qbbhstr = qbbhstr + qbbh + "|";
					}
					// 发送广播到Activity
					sendintent.setAction("android.com.tiny.sendtotalpack");
					sendintent.putExtra("totalpack",CaijiService.this.total);
					sendintent.putExtra("nowpack",CaijiService.this.nowpack);
					sendBroadcast(CaijiService.this.sendintent);
				}

			}
			
			}catch(Exception e){
				Log.e("test", "返回错误数据"+e.toString());
				
			}

		}

	};
	
	// 传输表号线程
		public class sendthread implements Runnable {
			public void run() {
				// TODO Auto-generated method stub

				try {
					Thread.sleep(4000);// 线程暂停20秒，单位毫秒
				
				
					Log.e("test", "时间大于2秒接受数据完成");

				
				} catch (Exception e) {
					// TODO Auto-generated catch block
				     Log.e("test", "采集数据异常"+e.toString());
                      
				}

			}
		}
	
	

}
