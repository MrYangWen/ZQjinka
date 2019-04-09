package com.ximei.tiny.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.ximei.tiny.database.BDhelper;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.Containstr;
import com.ximei.tiny.tools.FileOpertion;
import com.ximei.tiny.tools.FileUtils;
import com.ximei.tiny.tools.GetTotalPack;
import com.ximei.tiny.tools.GetmsgID;
import com.ximei.tiny.tools.SubDong;
import com.ximei.tiny.tools.ToHexStr;
import com.ximei.tiny.tools.ToInverted;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*
 * 此service主要用于集抄表
 * 是连接通信service和activity桥梁
 * 
 * 
 */
public class DataBaseService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private static final String TAG = "XiMeiDemo";
	private boolean  TimeoutFlag=true;
	private int leiji, jidian;
	private String ordercount;
	final int MAXmeternum = 16777215;
	final int MINmeternum = 0;
	FileUtils fileutils;
	SubDong sub;
	ToHexStr tohex;
	CRC crc;
	ToInverted toinver;
	//String allbackmsg = null;
	private String headmsg, overmsg, ordermsg, backputongqbbh, leijmsg, state;
	String stopmsg, stoporder, bcpath, databasename, cbfangshi, putongqbdz,metertype,jichaoqbdz;
	String StrAddrType,MeterIdTmpStr;
	private int qbcount, jichaoflag;
	Map<Integer, Integer> count;
	boolean issend;
	GetmsgID getmsg;
	SQLiteDatabase readdb;
	SQLiteDatabase writedb;
	int nowshu, succeednub, failnub,cbflag;
	List<String> listbackmsg;
	List<String> listbackmsg1;
	String[] qbdzlist;
	MyReceiver receiver;
	PutongReceiver putongreceiver;
	Containstr ishave;
	GetTotalPack gettotal;
	String allbackmsg1="";
	FileOpertion fileopertion=new FileOpertion();
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		// 初始化数据
		listbackmsg = new ArrayList<String>();
		listbackmsg1 = new ArrayList<String>();
		tohex = new ToHexStr();
		crc = new CRC();
		ishave = new Containstr();
		toinver = new ToInverted();
		gettotal = new GetTotalPack();
		headmsg = "5A5A00FE01";
		overmsg = "AA40F9035B5B/";
		sub = new SubDong();
		fileutils = new FileUtils();
		getmsg = new GetmsgID();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d(TAG, "datastart");
		qbcount = 0;
		issend = true;
		jichaoflag = 1;
		cbflag=0;
		nowshu = 0;
		succeednub = 0;
		failnub = 0;
		listbackmsg.clear();
		listbackmsg.add(0, "");
		listbackmsg1.clear();
		listbackmsg1.add(0, "");
		// 注册快速抄表广播
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.MY_BROADCAST");
		registerReceiver(receiver, filter);

		// 注册停止service广播
		MyReceiver1 receiver1 = new MyReceiver1();
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction("android.intent.action.MY_BROADCAST2");
		registerReceiver(receiver1, filter1);
		// 注册普通抄表广播
		putongreceiver = new PutongReceiver();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction("android.intent.action.putongcb_BROADCAST");
		registerReceiver(putongreceiver, filter2);

		cbfangshi = intent.getStringExtra("cbfangshi");
		databasename = intent.getStringExtra("databasename");
		metertype = intent.getStringExtra("metertype");
		StrAddrType=intent.getStringExtra("AddrType");
		Log.e("test", cbfangshi);
		Log.e("test", databasename);
		Intent intentBusy = new Intent("android.intent.action.busy");
		intentBusy.putExtra("State", "busy");
		sendBroadcast(intentBusy);	
		if (cbfangshi.equals("kuaisucb")) {
			qbdzlist = intent.getStringArrayExtra("sendqbdzlist");
			// 启动快速集抄线程
			MyThread thread1 = new MyThread();
			new Thread(thread1).start();

		}
		if (cbfangshi.equals("putongcb")) {
			qbdzlist = intent.getStringArrayExtra("sendqbdzlist");
			// 启动普通抄表线程
			PutongThread putongthread = new PutongThread();
			new Thread(putongthread).start();
		}
		// 得到对数据库操作权限
		BDhelper dbhelper = new BDhelper(DataBaseService.this, databasename);
		readdb = dbhelper.getReadableDatabase();
		writedb = dbhelper.getWritableDatabase();
		return super.onStartCommand(intent, flags, startId);
	}

	// 停止service广播
	private class MyReceiver1 extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.d(TAG, "stop");
			stoporder = intent.getStringExtra("stoporder");

			if (stoporder.equals("stop")) {
				DataBaseService.this.unregisterReceiver(receiver);
				DataBaseService.this.unregisterReceiver(putongreceiver);
				stopSelf();
				issend = false;

			} else {

				issend = true;
			}

		}

	}

	// 快速集抄广播
	private class MyReceiver extends BroadcastReceiver {

		public void onReceive(Context paramContext, Intent paramIntent) {

			try {
				Thread.sleep(300);
				cbflag=1;
				String CmdStr="";
				String allbackmsg = null;
				int backdatalen;
				String backcrcstr;
				String backcrcmsg;
				String backcrc;
				String backmsg;
				allbackmsg=paramIntent.getStringExtra("allmsg");	
				allbackmsg1=allbackmsg;
				if(StrAddrType.equals("LongAddr"))
				{
					// 返回数据长度
					backdatalen = Integer.parseInt(allbackmsg.substring((14+8), (16+8)), 16);
					// 返回CRC应校验的字符串
					backcrcstr = allbackmsg.substring(4,(16+8 + 2 * backdatalen));
					// 返回CRC的值
					backcrcmsg = allbackmsg.substring((16+8 + 2 * backdatalen), (20+8 + 2 * backdatalen));
					// 返回数据内容

					// 返回数据命令
					CmdStr = allbackmsg.substring((12+8), (14+8));					
					// 通过计算得出返回CRC的值
					backcrc = crc.CRC_CCITT(1, backcrcstr).toUpperCase();
					backmsg = allbackmsg.substring((16+8),(16+8 + 2 * backdatalen));
					backputongqbbh = allbackmsg.substring(6, (12+8));
				}else{				
					// 返回数据长度转化为整型
					backdatalen = Integer.parseInt(allbackmsg.substring(14, 16), 16);
					// 返回CRC应校验的字符串
					backcrcstr = allbackmsg.substring(4,(16 + 2 * backdatalen));
					// 返回CRC的值
					backcrcmsg = allbackmsg.substring((16 + 2 * backdatalen), (20 + 2 * backdatalen));
					// 通过计算得出返回CRC的值
					backcrc = crc.CRC_CCITT(1, backcrcstr).toUpperCase();

					CmdStr=allbackmsg.substring(12, 14);	
					backputongqbbh = allbackmsg.substring(6, 12);//paramIntent.getStringExtra("qbbh");	
					backmsg = allbackmsg.substring(16,(16 + 2 * backdatalen));
				}
				if(!backcrc.equals(backcrcmsg))
				{
					String log="异常记录(校验不通过)："+allbackmsg+"\r\n";
					fileopertion.writeTxtToFile(log);
					return;
				}
				//backmsg = paramIntent.getStringExtra("msg");
				Log.e("test", "服务" + backmsg);
				//backputongqbbh = paramIntent.getStringExtra("qbbh");
				String leijistr = toinver.toinverted(backmsg.substring(0, 8));
				leiji = Integer.parseInt(leijistr, 16);
				String jidianstr = toinver.toinverted(backmsg.substring(16, 24));
				jidian = Integer.parseInt(jidianstr, 16);
				state = backmsg.substring(24, 28);
				if ((leiji + jidian) > 9) {
					leijmsg = Integer.toString((leiji + jidian) / 10);
				} else {
					leijmsg = Integer.toString(0);
				}
				// 通用远传表，返回0，2，6个月输差统计表册抄表返回数据
				//if((CmdStr.equals("6D"))&&((backmsg.length() == 80)||(backmsg.length() == 56)||(backmsg.length() == 32))){
				if((CmdStr.equals("6D"))){
					String qbbh="";
					if(backputongqbbh.length()!=14)
					{
					  // qbbh = String.valueOf(Integer.parseInt(getmsg.getMsgID(backputongqbbh), 16));
					   qbbh=String.format("%08d", Integer.parseInt(getmsg.getMsgID(backputongqbbh), 16));
					}
					else
					   qbbh=backputongqbbh;
		  			if((MeterIdTmpStr.length()==14))
		  			{
		  				if(Integer.parseInt(MeterIdTmpStr.substring(6, 14))==Long.parseLong(qbbh))
		  					qbbh=MeterIdTmpStr;
		  			}									
					String cbsj = new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date());
					ContentValues localContentValues = new ContentValues();
					localContentValues.put("qbztbh", "已抄");
					localContentValues.put("qbql", leijmsg);
					localContentValues.put("cbjlid", cbsj);
					localContentValues.put("state", state);
					DataBaseService.this.writedb.update("ximeitable", localContentValues, "qbbh=?",new String[] { qbbh });
					// 发送抄表成功广播
					Intent localIntent = new Intent("com.tiny.backinfo.chaobiao");
					localIntent.putExtra("qbcount", nowshu);
					localIntent.putExtra("failflag", failnub);
					localIntent.putExtra("qbdz", jichaoqbdz);
					localIntent.putExtra("backinfo", "backsucceed");
					DataBaseService.this.sendBroadcast(localIntent);

				}
				else
				{
					String log="异常记录(解析不对)："+allbackmsg+"\r\n";
					fileopertion.writeTxtToFile(log);					
				}
//				// 通用远传表，返回0，2，6个月输差统计表册抄表返回数据
//				if (backmsg.length() == 56 || backmsg.length() == 32
//						|| backmsg.length() == 80) {
//					String qbbh = String.valueOf(Integer.parseInt(
//							getmsg.getMsgID(backputongqbbh), 16));
//					String cbsj = new SimpleDateFormat("yyyyMMdd hh:mm:ss")
//							.format(new Date());
//					ContentValues localContentValues = new ContentValues();
//					localContentValues.put("qbztbh", "已抄");
//					localContentValues.put("qbql", leijmsg);
//					localContentValues.put("cbjlid", cbsj);
//					localContentValues.put("state", state);
//					DataBaseService.this.writedb
//							.update("ximeitable", localContentValues, "qbbh=?",
//									new String[] { qbbh });
//					// 发送抄表成功广播
//					Intent localIntent = new Intent(
//							"com.tiny.backinfo.chaobiao");
//					localIntent.putExtra("qbcount", nowshu);
//					localIntent.putExtra("failflag", failnub);
//					localIntent.putExtra("qbdz", jichaoqbdz);
//					localIntent.putExtra("backinfo", "backsucceed");
//					DataBaseService.this.sendBroadcast(localIntent);
//
//				}
//				// gpm返回数据23，32个字节
//				if (backmsg.length() == 64 || backmsg.length() == 46) {
//
//					String qbbh = String.valueOf(Integer.parseInt(
//							getmsg.getMsgID(backputongqbbh), 16));
//					String cbsj = new SimpleDateFormat("yyyyMMdd hh:mm:ss")
//							.format(new Date());
//					ContentValues localContentValues = new ContentValues();
//					localContentValues.put("qbztbh", "已抄");
//					localContentValues.put("qbql", leijmsg);
//					localContentValues.put("cbjlid", cbsj);
//					String log="快速集抄："+backmsg+"\r\n"+"累计气量："+leijmsg+"表号："+qbbh+" 抄表时间："+cbsj+"\r\n";
//					fileopertion.writeTxtToFile(log);
//					DataBaseService.this.writedb
//							.update("ximeitable", localContentValues, "qbbh=?",
//									new String[] { qbbh });
//					// 发送抄表成功广播
//					Intent localIntent = new Intent(
//							"com.tiny.backinfo.chaobiao");
//					localIntent.putExtra("qbcount", nowshu);
//					localIntent.putExtra("failflag", failnub);
//					localIntent.putExtra("qbdz", putongqbdz);
//					localIntent.putExtra("backinfo", "backsucceed");
//					DataBaseService.this.sendBroadcast(localIntent);
//
//				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("test", e.toString());
			}

		}
	}

	// 普通机抄广播
	private class PutongReceiver extends BroadcastReceiver {
		private PutongReceiver() {
		}
		public void onReceive(Context paramContext, Intent paramIntent) {
		try {
			String CmdStr="";
			int backdatalen;
			String backcrcstr;
			String backcrcmsg;
			String backcrc;
			String backmsg;
			String allbackmsg = null;
			allbackmsg=paramIntent.getStringExtra("allmsg");
			allbackmsg1=allbackmsg;
			if(StrAddrType.equals("LongAddr"))
			{
				// 返回数据长度
				backdatalen = Integer.parseInt(allbackmsg.substring((14+8), (16+8)), 16);
				// 返回CRC应校验的字符串
				backcrcstr = allbackmsg.substring(4,(16+8 + 2 * backdatalen));
				// 返回CRC的值
				backcrcmsg = allbackmsg.substring((16+8 + 2 * backdatalen), (20+8 + 2 * backdatalen));
				// 返回数据内容
//				String backmsg1 = allbackmsg.substring(16+8,(16+8 + 2 * backdatalen));
				backmsg = allbackmsg.substring(16+8,(16+8 + 2 * backdatalen));
				backputongqbbh = allbackmsg.substring(6, (12+8));
				// 返回数据命令
				CmdStr = allbackmsg.substring(12+8, (14+8));					
				// 通过计算得出返回CRC的值
				backcrc = crc.CRC_CCITT(1, backcrcstr).toUpperCase();					
			}else{				
				// 返回数据长度转化为整型
				backdatalen = Integer.parseInt(allbackmsg.substring(14, 16), 16);
				// 返回CRC应校验的字符串
				backcrcstr = allbackmsg.substring(4,(16 + 2 * backdatalen));
				// 返回CRC的值
				backcrcmsg = allbackmsg.substring((16 + 2 * backdatalen), (20 + 2 * backdatalen));
				// 通过计算得出返回CRC的值
				backcrc = crc.CRC_CCITT(1, backcrcstr).toUpperCase();

				
				CmdStr=allbackmsg.substring(12, 14);	
				backputongqbbh = allbackmsg.substring(6, 12);//paramIntent.getStringExtra("qbbh");
				backmsg = allbackmsg.substring(16,(16 + 2 * backdatalen));
			}
			if(!backcrc.equals(backcrcmsg))
			{
				String log="异常记录(校验不通过)："+allbackmsg+"\r\n";
				fileopertion.writeTxtToFile(log);
				return;
			}
			//backmsg = paramIntent.getStringExtra("resmsg");
			//backputongqbbh = paramIntent.getStringExtra("qbbh");
			String leijistr = toinver.toinverted(backmsg.substring(0, 8));
			leiji = Integer.parseInt(leijistr, 16);
			String jidianstr = toinver.toinverted(backmsg.substring(16, 24));
			jidian = Integer.parseInt(jidianstr, 16);
			state = backmsg.substring(24, 28);
			if ((leiji + jidian) > 9) {
				leijmsg = Integer.toString((leiji + jidian) / 10);
			} else {
				leijmsg = Integer.toString(0);
			}
			// 通用远传表，返回0，2，6个月输差统计表册抄表返回数据
			//if((CmdStr.equals("6D"))&&((backmsg.length() == 80)||(backmsg.length() == 56)||(backmsg.length() == 32)))  //新表80 旧表56 更旧表32
			if((CmdStr.equals("6D")))  //新表80 旧表56 更旧表32
			{
				String qbbh="";
				if(backputongqbbh.length()!=14)
				{
				  // qbbh = String.valueOf(Integer.parseInt(getmsg.getMsgID(backputongqbbh), 16));
				   qbbh=String.format("%08d", Integer.parseInt(getmsg.getMsgID(backputongqbbh), 16));
				}
				else
				   qbbh=backputongqbbh;
	  			if((MeterIdTmpStr.length()==14))
	  			{
	  				if(Integer.parseInt(MeterIdTmpStr.substring(6, 14))==Long.parseLong(qbbh))
	  					qbbh=MeterIdTmpStr;
	  			}
				String cbsj = new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date());
				ContentValues localContentValues = new ContentValues();
				localContentValues.put("qbztbh", "已抄");
				localContentValues.put("qbql", leijmsg);
				localContentValues.put("cbjlid", cbsj);
				localContentValues.put("state", state);
				String log="普通集抄："+backmsg+"\r\n"+"累计气量："+leijmsg+"qbbh:"+qbbh+"抄表时间"+cbsj+"\r\n";
				fileopertion.writeTxtToFile(log);				
				DataBaseService.this.writedb.update("ximeitable",localContentValues, "qbbh=?", new String[] { qbbh });
				// 发送抄表成功广播
				Intent localIntent = new Intent("com.tiny.backinfo.chaobiao");
				localIntent.putExtra("qbcount", nowshu);
				localIntent.putExtra("failflag", failnub);
				localIntent.putExtra("qbdz", putongqbdz);
				localIntent.putExtra("backinfo", "backsucceed");
				DataBaseService.this.sendBroadcast(localIntent);
				TimeoutFlag=false;
			}
			else
			{
				String log="异常记录(解析不对)："+allbackmsg+"\r\n";
				TimeoutFlag=false;
				fileopertion.writeTxtToFile(log);					
			}
//			if (backmsg.length() == 56 || backmsg.length() == 32|| backmsg.length() == 80 ||backmsg.length() == 88) {
//				String qbbh = String.valueOf(Integer.parseInt(getmsg.getMsgID(backputongqbbh), 16));
//				String cbsj = new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date());
//				ContentValues localContentValues = new ContentValues();
//				localContentValues.put("qbztbh", "已抄");
//				localContentValues.put("qbql", leijmsg);
//				localContentValues.put("cbjlid", cbsj);
//				localContentValues.put("state", state);
//				String log="普通集抄："+backmsg+"\r\n"+"累计气量："+leijmsg+"qbbh:"+qbbh+"抄表时间"+cbsj+"\r\n";
//				fileopertion.writeTxtToFile(log);				
//				DataBaseService.this.writedb.update("ximeitable",localContentValues, "qbbh=?", new String[] { qbbh });
//				// 发送抄表成功广播
//				Intent localIntent = new Intent("com.tiny.backinfo.chaobiao");
//				localIntent.putExtra("qbcount", nowshu);
//				localIntent.putExtra("failflag", failnub);
//				localIntent.putExtra("qbdz", putongqbdz);
//				localIntent.putExtra("backinfo", "backsucceed");
//				DataBaseService.this.sendBroadcast(localIntent);
//
//			}
//			// gpm返回数据23，32个字节
//			if (backmsg.length() == 64 || backmsg.length() == 46) {
//
//				String qbbh = String.valueOf(Integer.parseInt(
//						getmsg.getMsgID(backputongqbbh), 16));
//				String cbsj = new SimpleDateFormat("yyyyMMdd hh:mm:ss")
//						.format(new Date());
//				ContentValues localContentValues = new ContentValues();
//				localContentValues.put("qbztbh", "已抄");
//				localContentValues.put("qbql", leijmsg);
//				localContentValues.put("cbjlid", cbsj);
//				String log="普通集抄："+backmsg+"\r\n"+"累计气量："+leijmsg+"qbbh:"+qbbh+"抄表时间"+cbsj+"\r\n";
//				fileopertion.writeTxtToFile(log);	
//				DataBaseService.this.writedb.update("ximeitable",
//						localContentValues, "qbbh=?", new String[] { qbbh });
//				// 发送抄表成功广播
//				Intent localIntent = new Intent("com.tiny.backinfo.chaobiao");
//				localIntent.putExtra("qbcount", nowshu);
//				localIntent.putExtra("failflag", failnub);
//				localIntent.putExtra("qbdz", putongqbdz);
//				localIntent.putExtra("backinfo", "backsucceed");
//				DataBaseService.this.sendBroadcast(localIntent);
//
//			}
//		}
		}catch(Exception e){
	//		Log.e("test", e.toString());
		}
	}
 }
	// 普通集抄线程
	public class PutongThread implements Runnable {

		public synchronized void run() {
			// TODO Auto-generated method stub
			if (issend) {

				String CRCmsg, ordermsg, hexTimemsg, Timemsg, msgvalue;
				BDhelper dbhelper = new BDhelper(DataBaseService.this,databasename);
				SQLiteDatabase readdb = dbhelper.getReadableDatabase();
				SQLiteDatabase writedb = dbhelper.getWritableDatabase();
				GetmsgID getmsg = new GetmsgID();
				Cursor cursor = readdb.query("ximeitable", null, null, null,null, null, null);
				SimpleDateFormat df = new SimpleDateFormat("ssmmHHddMMyy");
				Timemsg = df.format(new Date());
				hexTimemsg = tohex.toHexStr(Timemsg);
				Intent intent = new Intent();
				while (cursor.moveToNext()) {
					String qbbh = cursor.getString(cursor.getColumnIndex("qbbh"));
					putongqbdz = cursor.getString(cursor.getColumnIndex("dzms"));
					String qbzd = cursor.getString(cursor.getColumnIndex("qbztbh"));
					try {
						if (!qbbh.equals("") && ishave.isHave(qbdzlist, putongqbdz)&& qbzd.equals("未抄") && issend) {
							if (!issend) {
								break;
							}
							nowshu++;
							//int qbbhint = Integer.parseInt(qbbh);
							//if (qbbhint <= MAXmeternum && qbbhint >= MINmeternum)
							metertype="mrmeter";
							MeterIdTmpStr=qbbh;
							msgvalue=qbbh;//getmsg.GetMeterAddr(qbbh);
							if(msgvalue.length()==14){
							    StrAddrType="LongAddr";
							}else{
								StrAddrType="ShortAddr";
							}
							if((qbbh.length()==14)&&(StrAddrType.equals("ShortAddr")))
								metertype="newmeter";
							if(msgvalue!=null)						
							{
								//String qbbhhexstr = Integer.toHexString(qbbhint);
								//msgvalue = getmsg.getMsgID(qbbhhexstr);
								CRCmsg = "09" + msgvalue + "9A" + "06" + hexTimemsg;
								ordermsg = headmsg + CRCmsg+ crc.CRC_CCITT(1, CRCmsg).toUpperCase()+ overmsg;
								Log.e("test", ordermsg);
								intent.putExtra("order", ordermsg);
								intent.putExtra("metertype", metertype);
								intent.setClass(DataBaseService.this,BtXiMeiService.class);
								DataBaseService.this.startService(intent);
							} else {
								Intent intent1 = new Intent("com.tiny.sendqbbh.error");
								intent1.putExtra("qbbh", qbbh);
								intent1.putExtra("qbdz", putongqbdz);
								sendBroadcast(intent1);
							}
							try {
								allbackmsg1 = "";
								int TimeCount=0;
								while(TimeoutFlag){
									Thread.sleep(1000);
									TimeCount++;
									if(TimeCount>=14){
										break;
									}
								}
								TimeoutFlag=true;
								if (allbackmsg1.equals("")) {
									failnub++;
									Intent intent1 = new Intent("com.tiny.backinfo.chaobiao");
									intent1.putExtra("qbcount", nowshu);
									intent1.putExtra("failflag", failnub);
									intent1.putExtra("backinfo", "backfail");
									intent1.putExtra("qbdz", jichaoqbdz);
									sendBroadcast(intent1);
								}

							} catch (Exception e) {
								// TODO Auto-generated catch block
//								e.printStackTrace();
							}

						}						
						
					} catch (Exception e) {
						e.toString();
						// TODO: handle exception
					}

				}

			}

		}
	}

	// 快速集抄发唤醒波线程
	public class MyThread implements Runnable {

		public synchronized void run() {
			// TODO Auto-generated method stub
			if (issend) {
				try {
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);// 发送消息
					Thread.sleep(12000);// 线程暂停12秒，单位毫秒
					MyThread1 bhthread1 = new MyThread1();
					new Thread(bhthread1).start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 快速集抄线程
	public class MyThread1 implements Runnable {

		public synchronized void run() {
			// TODO Auto-generated method stub

			Intent intent = new Intent();

			if (issend) {

				String CRCmsg, ordermsg, hexTimemsg, Timemsg, msgvalue, qbbhhexstr;
				int qbbhint;
				GetmsgID getmsg = new GetmsgID();
				BDhelper dbhelper = new BDhelper(DataBaseService.this,databasename);
				SQLiteDatabase readdb = dbhelper.getReadableDatabase();
				// SQLiteDatabase writedb = dbhelper.getWritableDatabase();
				Cursor cursor = readdb.query("ximeitable", null, null, null,null, null, null);
				while (cursor.moveToNext()) {

					String qbbh = cursor.getString(cursor.getColumnIndex("qbbh"));
					jichaoqbdz = cursor.getString(cursor.getColumnIndex("dzms"));
					String qbzd = cursor.getString(cursor.getColumnIndex("qbztbh"));
					try {
						if (!qbbh.equals("") && ishave.isHave(qbdzlist, jichaoqbdz)&& qbzd.equals("未抄") && issend) {
							if (!issend) {
								break;
							}
							nowshu++;
							//qbbhint = Integer.parseInt(qbbh);
							//if (qbbhint <= MAXmeternum && qbbhint >= MINmeternum) 
							MeterIdTmpStr=qbbh;
							msgvalue=getmsg.GetMeterAddr(qbbh);
							if(msgvalue!=null)	
							{
							//	qbbhhexstr = Integer.toHexString(qbbhint);
							//	msgvalue = getmsg.getMsgID(qbbhhexstr);
								CRCmsg = "09" + msgvalue + "A4" + "00";
								ordermsg = headmsg + CRCmsg+ crc.CRC_CCITT(1, CRCmsg).toUpperCase()+ overmsg;
								//Log.e("test", "A4命令" + ordermsg);
								intent.putExtra("metertype", metertype);
								intent.putExtra("order", ordermsg);
								intent.setClass(DataBaseService.this,BtXiMeiService.class);
								startService(intent);
							} 
							else 
							{
								Intent intent1 = new Intent("com.tiny.sendqbbh.error");
								intent1.putExtra("qbbh", qbbh);
								intent1.putExtra("qbdz", jichaoqbdz);
								sendBroadcast(intent1);

							}
							try {
								cbflag=0;
								//backmsg = "";
								Thread.sleep(2500);
								if (cbflag==0) {
									failnub++;
									Intent intent1 = new Intent("com.tiny.backinfo.chaobiao");
									intent1.putExtra("qbcount", nowshu);
									intent1.putExtra("failflag", failnub);
									intent1.putExtra("backinfo", "backfail");
									intent1.putExtra("qbdz", jichaoqbdz);
									sendBroadcast(intent1);
								}

							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}						
					} catch (Exception e) {
						// TODO: handle exception
						e.toString();
					}

				}
				//发送让所有表睡眠指令
				String str4 = new SimpleDateFormat("ssmmHHddMMyy").format(new Date());
				String str5 = tohex.toHexStr(str4);
			    // 1278快速命令
				
				String str7 = "09" + "FFFFFF" + "A8" + "08" + str5+ "0000";
				if(StrAddrType.equals("LongAddr"))
				   str7 = "09" + "FFFFFFFFFFFFFF" + "A8" + "08" + str5+ "0000";	
				Log.e("test", "A8命令......" + str7);
				String str8 = headmsg + str7+ crc.CRC_CCITT(1, str7).toUpperCase()+ overmsg;			  
				Intent localIntent = new Intent();
			    localIntent.putExtra("metertype", metertype);
			    localIntent.putExtra("order", str8);
			    localIntent.setClass(DataBaseService.this, BtXiMeiService.class);
			    startService(localIntent); 					
				
				  //循环3次快速集抄
				
				
//				nowshu=0;
//				failnub=0;
//				jichaoflag++;
//				Log.e("test", String.valueOf(jichaoflag));
//				if(jichaoflag<=3){
//					// 启动快速集抄线程
//					MyThread thread1 = new MyThread();
//					new Thread(thread1).start();	
//					
//				}
				
				
			}
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 要做的事情

			if (msg.what == 1) { 
				sendorder();
			}
			super.handleMessage(msg);
		}
	};

	public void sendorder() {

		{
			Cursor localCursor = readdb.query("ximeitable", null, null, null,null, null, null);
			while (true) {
				if (!localCursor.moveToNext()) {
					String str4 = new SimpleDateFormat("ssmmHHddMMyy").format(new Date());
					String str5 = this.tohex.toHexStr(str4);
					String str6 = Integer.toHexString(this.qbcount).toUpperCase();
					if (str6.length() == 1)
						this.ordercount = ("0" + str6);
					if (str6.length() > 1)
						this.ordercount = str6;
					if (this.qbcount != 0) {
						// 1278快速抄表测试，表唤醒时间3秒
						int alltime = qbcount * 3 + 10;
						Log.e("test", String.valueOf(alltime));
						String hexalltime = Integer.toHexString(alltime);
						String sendalltimer = gettotal.gettotalpack(hexalltime,4);
						// 1278快速命令
						String str7 = "09" + "FFFFFF" + "A8" + "08" + str5+ sendalltimer;
						if(StrAddrType.equals("LongAddr"))
							str7 = "09" + "FFFFFFFFFFFFFF" + "A8" + "08" + str5+ sendalltimer;
						Log.e("test", "A8命令......" + str7);
						// String str7 = "09" + "FFFFFF" + "A8" + "08" + str5+
						// this.ordercount + "01";
						String str8 = this.headmsg + str7+ this.crc.CRC_CCITT(1, str7).toUpperCase()+ this.overmsg;			  
					   Intent localIntent = new Intent();
					   localIntent.putExtra("metertype", metertype);
					   localIntent.putExtra("order", str8);
					   localIntent.setClass(this, BtXiMeiService.class);
					   startService(localIntent); 	
					}
					return;
				}
				String str1 = localCursor.getString(localCursor.getColumnIndex("qbbh"));
				String str2 = localCursor.getString(localCursor.getColumnIndex("dzms"));
				String str3 = localCursor.getString(localCursor.getColumnIndex("qbztbh"));
				if ((!str1.equals(""))&& (this.ishave.isHave(this.qbdzlist, str2))&& (str3.equals("未抄")))
					this.qbcount = (1 + this.qbcount);
			}
		}

	}

}