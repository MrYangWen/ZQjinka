package com.ximei.tiny.backinfoview;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.tiny.gasxm.R;
import com.ximei.tiny.database.BDhelper;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.Containstr;
import com.ximei.tiny.tools.DBFReader;
import com.ximei.tiny.tools.FileUtils;
import com.ximei.tiny.tools.GetAllQbbh;
import com.ximei.tiny.tools.GetRawData;
import com.ximei.tiny.tools.GetTotalPack;
import com.ximei.tiny.tools.GetmsgID;
import com.ximei.tiny.tools.SaveQbbh;
import com.ximei.tiny.tools.SendQbbh;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.TimerTask;

/*
 * 组网数据返回activity
 * 通过BroadcastReceiver，handler，更行activity
 * 
 * 
 */
public class BackJiZhongQiActivity extends Activity {
	String CRCmsg;
	private TextView backlogin;
	int chuanshuflag;
	CRC crc;
	String databasename;
	String datatype;
	SQLiteDatabase dbreader;
	SQLiteDatabase dbwriter;
	private TextView fail;
	GetTotalPack getpack;
	GetTotalPack gettotalpack;

	String headmsg, overmsg;
	String jzqflag;
	ArrayList<String> list, newlist, qbbhdata;
	NewAmrReceiver newAmrReceiver;
	String ordermsg;
	private ProgressBar pro1;
	String[] qbbhlist, newqbbhlist;
	int qingqiuflag;
	SendQbbh sendqbbh;
	private TextView succeed;
	String target;
	FileUtils fileutils;
	GetAllQbbh getqbbh;
	Containstr contain;
	Intent intent;
	private String[] rowdata;
	private int sendflag, backflag;
	private Thread SendThread;
	TimerTask task;
	BDhelper bdhelper;

	GetmsgID getmsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.backsinglecb);
		// 初始化数据
		qingqiuflag = 0;
		backflag = 0;
		headmsg = "5A5A00FE02";
		overmsg = "AA40F903";
		gettotalpack = new GetTotalPack();
		crc = new CRC();
		getpack = new GetTotalPack();
		sendqbbh = new SendQbbh();
		fileutils = new FileUtils();
		getqbbh = new GetAllQbbh();
		contain = new Containstr();
		intent = getIntent();
		datatype = intent.getStringExtra("datatype");
		databasename = intent.getStringExtra("databasename");
		getmsg = new GetmsgID();
		// 得到相关控件的句柄
		succeed = (TextView) findViewById(R.id.succeed);
		fail = (TextView) findViewById(R.id.fail);
		pro1 = (ProgressBar) findViewById(R.id.pro1);
		backlogin = (TextView) findViewById(R.id.backlogin);

		// 传输表号（需组网）操作
		if (datatype.equals("caijicsbh")) {

			// 得到intent传递过来的相关数据

			target = intent.getStringExtra("target");
			databasename = intent.getStringExtra("databasename");
			jzqflag = intent.getStringExtra("jzqflag");
			backlogin.setText("请求传输数据包......");
			// 启动请求传输表号线程
			new Thread(new RequestzwThread()).start();
		}
		// 传输表号（无需组网）操作
		if (datatype.equals("nozwcsbh")) {
			qbbhdata = intent.getStringArrayListExtra("qbbhlist");
			target = intent.getStringExtra("target");
			databasename = intent.getStringExtra("databasename");
			jzqflag = intent.getStringExtra("jzqflag");
			backlogin.setText("请求传输数据包......");
			// 启动请求传输表号线程
			new Thread(new RequestThread()).start();
		}
		// 采集抄表数据
		if (datatype.equals("caijidata")) {
			backlogin.setText("正在传输数据通信中......");
			new Thread(new MyThread()).start();

		}
		// 采集组网表号
		if (this.datatype.equals("caijibh")) {
			this.backlogin.setText("正在采集组网表号......");
			new Thread(new MyThread()).start();
		}

		// 注册请求传输表号广播接受信息

		// 新amr传输表号广播；
		newAmrReceiver = new NewAmrReceiver();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction("android.intent.newxmr.qqcsbh");
		filter2.addAction("android.intent.newxmr.backcsbh");
		BackJiZhongQiActivity.this.registerReceiver(newAmrReceiver, filter2);

		// 注册采集表信息广播接受信息

		IntentFilter filter1 = new IntentFilter();
		filter1.addAction("android.com.tiny.sendtotalpack");
		filter1.addAction("android.intent.action.caijifinishflag");

		BackJiZhongQiActivity.this.registerReceiver(caijiinfo, filter1);

		// 得到数据库操作信息
		BDhelper dbhelper = new BDhelper(BackJiZhongQiActivity.this,
				databasename);
		dbreader = dbhelper.getReadableDatabase();
		dbwriter = dbhelper.getWritableDatabase();

	}

	// 接受采集抄表数据信息
	private BroadcastReceiver caijiinfo = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// 处理返回数据包信息

			if (intent.getAction().equals("android.com.tiny.sendtotalpack")) {
				BackJiZhongQiActivity.this.backlogin.setText("请求成功，正在采集抄表数据");
				int i = intent.getIntExtra("totalpack", 0);
				int j = intent.getIntExtra("nowpack", 0);

				Message message = handler.obtainMessage();
				message.what = 2;
				Bundle b = new Bundle();
				b.putInt("nowpack", j);
				b.putInt("totalpack", i);
				message.setData(b);
				handler.sendMessage(message);

			}
			if (intent.getAction().equals("android.intent.action.caijifinishflag")) {
				BackJiZhongQiActivity.this.backlogin.setText("传输完成");
				pro1.setVisibility(8);
				Intent intentBusy = new Intent("android.intent.action.busy");
				intentBusy.putExtra("State", "idle");
				sendBroadcast(intentBusy);					

			}

		}

	};

	// newamr请求传输成功广播信息
	private class NewAmrReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("android.intent.newxmr.qqcsbh")) {
				backlogin.setText("请求成功正在传输数据包......");

				sendflag = 0;
				// Log.e("test", "请求成功发送第一包");
				sendqbbhpack(sendflag);

			} else {

				try {
					String[] qbbhmsg = null;
					ContentValues values = new ContentValues();
					// 写入数据库
					values.put("jzqflag", target);
					if (sendflag < qbbhlist.length) {

						Log.e("test", qbbhlist[sendflag]);
						qbbhmsg = SaveQbbh.writeqbbh(qbbhlist[sendflag]);
						Log.e("test", qbbhmsg[0]);

					} else {

						Log.e("test", newqbbhlist[sendflag - qbbhlist.length]);
						qbbhmsg = SaveQbbh.writeqbbh(newqbbhlist[sendflag
								- qbbhlist.length]);
						Log.e("test", qbbhmsg[0]);

					}

					for (int i = 0; i < qbbhmsg.length; i++) {

						dbwriter.update("ximeitable", values, "qbbh=?",
								new String[] { qbbhmsg[i] });
					}

				} catch (Exception e) {
					Log.e("test", e.toString());
				}
				
				sendflag++;
				SendThread.interrupt();
				try {
					SendThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.e("test", "sendflag="+String.valueOf(sendflag));
				if (sendflag < qbbhlist.length + newqbbhlist.length) {
					sendqbbhpack(sendflag);

				}else{
					Message message = handler.obtainMessage();
					message.what = 4;
					Bundle b = new Bundle();
					b.putInt("sendflag", sendflag);
					//b.putString("order", ordermsg);
					message.setData(b);
					handler.sendMessage(message);
					
					
					
					
				}

			}
		}
	}

	// 用于更新activity显示
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 要做的事情

			if (msg.what == 1) {

				if (BackJiZhongQiActivity.this.backlogin.getText().toString()
						.equals("请求传输数据包......")) {
					BackJiZhongQiActivity.this.fail.setText("请求失败 ");
					BackJiZhongQiActivity.this.pro1.setVisibility(8);
					BackJiZhongQiActivity.this.backlogin.setText("");
				}
				if (BackJiZhongQiActivity.this.backlogin.getText().toString()
						.equals("请求采集抄表数据......")) {
					BackJiZhongQiActivity.this.fail.setText("通信失败 ");
					BackJiZhongQiActivity.this.pro1.setVisibility(8);
					BackJiZhongQiActivity.this.backlogin.setText("");
				}
				if (BackJiZhongQiActivity.this.backlogin.getText().toString()
						.equals("正在采集组网表号......")) {
					BackJiZhongQiActivity.this.fail.setText("通信失败 ");
					BackJiZhongQiActivity.this.pro1.setVisibility(8);
					BackJiZhongQiActivity.this.backlogin.setText("");
				}
				if (BackJiZhongQiActivity.this.backlogin.getText().toString()
						.equals("正在传输数据通信中......")) {
					BackJiZhongQiActivity.this.fail.setText("通信失败 ");
					BackJiZhongQiActivity.this.pro1.setVisibility(8);
					BackJiZhongQiActivity.this.backlogin.setText("");
				}

			}

			if (msg.what == 2) {

				Bundle b = msg.getData();
				String now = String.valueOf(b.getInt("nowpack"));
				String total = String.valueOf(b.getInt("totalpack"));
				String order = b.getString("order");
				fail.setText(now + "/" + total);
				
//				if (now.equals(sendflag+1)) {
//					backlogin.setText("数据传输完成");
//					pro1.setVisibility(8);
//
//				}
				// fail.setText(order);

			}

			if (msg.what == 3) {

				backlogin.setText("传输失败，请重新传输");

			}
			
			if (msg.what == 4) {

				Bundle b = msg.getData();
				String sendnub = String.valueOf(b.getInt("sendflag"));
				
			
				Log.e("test", "sendflag="+String.valueOf(sendnub));
				
					backlogin.setText("数据传输完成");
					pro1.setVisibility(8);
					Intent intentBusy = new Intent("android.intent.action.busy");
					intentBusy.putExtra("State", "idle");
					sendBroadcast(intentBusy);					
				// fail.setText(order);

			}

			super.handleMessage(msg);
		}
	};

	// 传输表号线程
	public void sendqbbhpack(int sendflag) {

		Intent intent1 = new Intent();
		Log.e("test", "进入发送数据方法");
		// 发送表号时间线程
		SendThread = new Thread(new sendthread());
		SendThread.start();

		// 新amr无需组网传输表号

		int i = sendflag;
		if (i < qbbhlist.length) {
			int k = qbbhlist[i].length() / 2 + 2;
			String datalen = Integer.toHexString(k);
			String qbcount = Integer.toHexString((k - 2) / 3);
			CRCmsg = ("09" + target + "0B" + getpack.gettotalpack(datalen, 2)
					+ "000" + qbcount + qbbhlist[i]).toUpperCase();
			ordermsg = headmsg + CRCmsg
					+ crc.CRC_CCITT(1, CRCmsg).toUpperCase() + overmsg
					+ "5B5B/";

		} else {

			int j = newqbbhlist[i - qbbhlist.length].length() / 2 + 2;
			String newqbcount = Integer.toHexString((j - 2) / 3);
			String newdatalen = Integer.toHexString(j);
			CRCmsg = ("09" + target + "0B"
					+ getpack.gettotalpack(newdatalen, 2) + "010" + newqbcount + newqbbhlist[i
					- qbbhlist.length]).toUpperCase();
			ordermsg = headmsg + CRCmsg
					+ crc.CRC_CCITT(1, CRCmsg).toUpperCase() + overmsg
					+ "5B5B/";

		}

		// Log.e("test", ordermsg);
		intent1.putExtra("order", ordermsg);
		intent1.setClass(BackJiZhongQiActivity.this, BtXiMeiService.class);
		BackJiZhongQiActivity.this.startService(intent1);

		Message message = handler.obtainMessage();
		message.what = 2;
		Bundle b = new Bundle();
		b.putInt("nowpack", i + 1);
		b.putInt("totalpack", qbbhlist.length + newqbbhlist.length);
		b.putString("order", ordermsg);
		message.setData(b);
		handler.sendMessage(message);

	}

	// 无须组网 加载原始表册
	public class RequestThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 得到原始表册的全路径
			rowdata = GetRawData.RawData();

			chuanshuflag = 1;
			// 得到intent传递过来的相关数据

			list = new ArrayList<String>();
			newlist = new ArrayList<String>();
			// 得到数据库操作信息
			BDhelper dbhelper = new BDhelper(BackJiZhongQiActivity.this,databasename);

			// 遍历所有表号加入list中
			for (int i = 0; i < qbbhdata.size(); i++) {

				String qbbh = qbbhdata.get(i);
				if (!qbbh.equals("")) {
					// 把表号转换为8位
					String allqbbh = getqbbh.GetQbbh(qbbh, 8);
					//Log.e("test", allqbbh);

					// 得到qbbh的第四位转化为String类型
					String comqbbh = String.valueOf(allqbbh.charAt(3));
					//得到表号前4位
					
					// 得到qbbh的第3位转化为int类型
					int  year = Integer.parseInt(String.valueOf(allqbbh.charAt(2)));
					
					//String comqbbh1 = allqbbh.substring(0, 4);

					if (contain.isHave(rowdata, allqbbh) || ((year==5||year==6) && (comqbbh.equals("9")||equals("8")))) {

						newlist.add(qbbh);
						Log.e("test", "新表"+qbbh);

					} else {
						list.add(qbbh);
					}


				}

			}

			// 分成每包10个String数组
			// cc1101表

			qbbhlist = sendqbbh.toarrayqbbh(list, 10);

			// 1278表
			newqbbhlist = sendqbbh.toarrayqbbh(newlist, 10);

			// 发送无需组网请求命令

			CRCmsg = "09" + target + "0A00";
			ordermsg = (headmsg + CRCmsg
					+ crc.CRC_CCITT(1, CRCmsg).toUpperCase() + overmsg + "5B5B/")
					.toUpperCase();

			intent.putExtra("order", ordermsg);
			intent.setClass(BackJiZhongQiActivity.this, BtXiMeiService.class);
			BackJiZhongQiActivity.this.startService(intent);
			new Thread(new MyThread()).start();
			// new Thread(new sendpackThread()).start();

		}

	}

	// 加载原始表册需要组网传输表号
	public class RequestzwThread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 得到intent传递过来的相关数据
			list = new ArrayList<String>();
			newlist = new ArrayList<String>();
			Log.e("test", "需要组网请求线程");
			// 得到原始表册的全路径
			String filepath = fileutils.getSDPATH() + "alldata/"+ "rawdata.dbf";

			try {
				FileInputStream fis = new FileInputStream(filepath);
				DBFReader reader = new DBFReader(fis);
				reader.setCharactersetName("gb2312");
				int filecount = reader.getRecordCount();
				rowdata = new String[filecount];
				Object rowValues[];
				for (int i = 0; i < filecount; i++) {
					rowValues = reader.nextRecord();
					rowdata[i] = rowValues[0].toString().trim();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("test", e.toString());
			}

			chuanshuflag = 1;
			// 通过jzqflag查找测试组网的表号
			try{
				Thread.sleep(100);
				Cursor cursor = dbreader.rawQuery("select qbbh from ximeitable where jzqflag=?",new String[] { jzqflag });		
				// 遍历所有表号加入list中
				while (cursor.moveToNext()) {

					String qbbh = cursor.getString(cursor.getColumnIndex("qbbh"));
					if (!qbbh.equals("")) {
						// 把表号转换为8位
						String allqbbh = getqbbh.GetQbbh(qbbh, 8);

						// 得到qbbh的第四位转化为String类型
						String comqbbh = String.valueOf(allqbbh.charAt(3));
	                    //得到表号前4位

						// 得到qbbh的第3位转化为int类型
						int  year = Integer.parseInt(String.valueOf(allqbbh.charAt(2)));
						
						
						//String comqbbh1 = allqbbh.substring(0, 4);

						if (contain.isHave(rowdata, allqbbh) || ((year==5||year==6) && (comqbbh.equals("9")||comqbbh.equals("8")))) {

							newlist.add(qbbh);

						} else {
							list.add(qbbh);
						}
//						
//						// 得到qbbh的第3位转化为int类型
//						int  year = Integer.parseInt(String.valueOf(qbbh.charAt(2)));
//						if (contain.isHave(rowdata, allqbbh) || (year>4 && comqbbh.equals("9"))) {
	//
//							newlist.add(qbbh);
	//
//						} else {
//							list.add(qbbh);
//						}

					}

				}				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("test", e.toString());
			}

			// 分成每包10个String数组
			// cc1101表
			qbbhlist = sendqbbh.toarrayqbbh(list, 10);

			// 1278表
			newqbbhlist = sendqbbh.toarrayqbbh(newlist, 10);

			// 发送无需组网请求命令

			CRCmsg = ("09" + target + "0A00").toUpperCase();
			ordermsg = (headmsg + CRCmsg
					+ crc.CRC_CCITT(1, CRCmsg).toUpperCase() + overmsg + "5B5B/")
					.toUpperCase();
			Log.e("test", ordermsg);
			intent.putExtra("order", ordermsg);
			intent.setClass(BackJiZhongQiActivity.this, BtXiMeiService.class);
			BackJiZhongQiActivity.this.startService(intent);
			new Thread(new MyThread()).start();
			// new Thread(new sendpackThread()).start();

		}

	}

	// 通信时间线程,大于20秒通信失败
	public class MyThread implements Runnable {
		public void run() {
			// TODO Auto-generated method stub

			try {
				Thread.sleep(20000);// 线程暂停20秒，单位毫秒
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);// 发送消息
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// 传输表号线程
	public class sendthread implements Runnable {
		public void run() {
			// TODO Auto-generated method stub

			try {
				Thread.sleep(12000);// 线程暂停20秒，单位毫秒
				backflag++;
				if (backflag > 4) {
					Log.e("test", "等待时间大于12秒线程结束");
					Message message = new Message();
					message.what = 3;
					handler.sendMessage(message);// 发送消息
				} else {

					sendqbbhpack(sendflag);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.e("test", "收到返回消息线程结束");

			}

		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		BackJiZhongQiActivity.this.unregisterReceiver(newAmrReceiver);
		BackJiZhongQiActivity.this.unregisterReceiver(caijiinfo);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			finish();

			// do something...
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}