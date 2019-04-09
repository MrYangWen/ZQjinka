package com.ximei.tiny.collector;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tiny.gasxm.R;
import com.ximei.tiny.database.BDhelper;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.Containstr;
import com.ximei.tiny.tools.GetAllQbbh;
import com.ximei.tiny.tools.GetTotalPack;
import com.ximei.tiny.tools.GetmsgID;
import com.ximei.tiny.tools.SendQbbh;

/*
 * 组网测试activity
 * 有顶楼，底楼测试
 * 写入表号
 */
public class CaiJiZWActivity extends Activity implements OnClickListener {

	String CRCmsg;
	String headmsg;
	private String jzqflag;
	private Button loginbt;
	String ordermsg;
	ArrayList<String> qbbhlist;
	private Button sendbt;
	SendQbbh sendqbbh;
	private String target;
	private EditText tgcishu;
	int tongguo;
	private TextView tongxincishu;
	private TextView tongxininfo;

	int topflag;
	private TextView topmsg;
	int topsucceed;
	int total;
	private EditText totalcishu;
	ArrayList<String> zwfaillist;
	private Button zwtongjibt;
	private String buttombh;
	private Button buttombt;
	private String topbh;
	private Button topbt;
	int buttomflag;

	private TextView buttommsg;
	int buttomsucceed;
	int cishu;
	int timerflag;
	CRC crc;
	String databasename;
	SQLiteDatabase dbreader;
	SQLiteDatabase dbwriter;
	GetmsgID getmsg;
	GetAllQbbh getqbbh;
	Containstr contain;;
	GetTotalPack gettotalpack;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去掉标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.caijizw);
		// 初始化数据
		headmsg = "5A5A00FE02";
		crc = new CRC();
		gettotalpack = new GetTotalPack();
		getmsg = new GetmsgID();
		getqbbh = new GetAllQbbh();
		sendqbbh = new SendQbbh();
		contain =new Containstr();
	
		timerflag = 0;
		// 得到intent传过来的数据
		Intent localIntent = getIntent();
		this.qbbhlist = localIntent.getStringArrayListExtra("qbbh");
		this.target = localIntent.getStringExtra("target");
		this.databasename = localIntent.getStringExtra("databasename");
		this.jzqflag = String.valueOf(Integer.parseInt(
				this.getmsg.getMsgID(this.target), 16));
		// 得到相关组建的句柄
		this.loginbt = ((Button) findViewById(R.id.zwtest));
		this.buttombt = ((Button) findViewById(R.id.buttombt));
		this.topbt = ((Button) findViewById(R.id.topbt));
		this.sendbt = ((Button) findViewById(R.id.sendbt));
		this.zwtongjibt = ((Button) findViewById(R.id.zwtongjibt));
		this.buttommsg = ((TextView) findViewById(R.id.buttommsg));
		this.topmsg = ((TextView) findViewById(R.id.topmsg));
		this.tongxininfo = ((TextView) findViewById(R.id.tongxininfo));
		this.tongxincishu = ((TextView) findViewById(R.id.tongxincishu));
		this.totalcishu = ((EditText) findViewById(R.id.totalcishu));
		this.tgcishu = ((EditText) findViewById(R.id.tgcishu));
		// 设置句柄初始值
		this.totalcishu.setText("1");
		this.tgcishu.setText("1");
		this.loginbt.setText("测     试     组    网");
		this.buttombt.setText("底楼测试");
		this.topbt.setText("顶楼测试");
		this.sendbt.setText("写入表号");
		this.zwtongjibt.setText("组网统计");
		// 得到数据库操作权限
		BDhelper localBDhelper = new BDhelper(this, this.databasename);
		this.dbreader = localBDhelper.getReadableDatabase();
		this.dbwriter = localBDhelper.getWritableDatabase();
		this.buttomflag = 0;
		this.topflag = (-1 + this.qbbhlist.size());
		this.buttombt.setOnClickListener(this);
		this.topbt.setOnClickListener(this);
		this.sendbt.setOnClickListener(this);
		this.zwtongjibt.setOnClickListener(this);
		// 注册组网广播
		IntentFilter localIntentFilter = new IntentFilter();
		localIntentFilter.addAction("android.com.tiny.action.testzw");
		localIntentFilter.addAction("android.com.tiny.action.testfeo1");
		registerReceiver(this.ZwangReceiver, localIntentFilter);

	}

	// 测试组网广播
	private BroadcastReceiver ZwangReceiver = new BroadcastReceiver() {
		public void onReceive(Context paramAnonymousContext, Intent intent) {

			timerflag = 1;

			try {
				
				if (intent.getAction().equals("android.com.tiny.action.testzw")) {
					String backmsg = intent.getStringExtra("backmsg");
					String backorder = intent.getStringExtra("backorder");
					Log.e("test", "返回命令"+backorder);
					Log.e("test", "返回数据"+backmsg);
					Log.e("test", "底楼表号"+buttombh);
					Log.e("test", "顶楼表号"+topbh);
					
					// 返回成功命令F0
					//FE0109A6826FBEA01454D18DD94DD19414D4D6D55B5B/
					if (backorder.equals("F0")) {
						String backqbbh = backmsg.substring(0, 6);
						// 如果组网成功buttomsucceed+1，再赋给buttomsucceed（底楼测试成功次数）；
						if (backqbbh.equals(buttombh)) {
							tongxininfo.setText("组网测试成功");

							buttomsucceed = (1 + buttomsucceed);
							Log.e("test", "底楼测试成功");
						}
						// 如果组网成功topsucceed+1，再赋给topsucceed（顶楼测试成功次数）；
						if (backqbbh.equals(topbh)) {
							tongxininfo.setText("组网测试成功");
                         
							topsucceed = (1 + topsucceed);
							Log.e("test", "顶楼测试成功");
						}
					}
					// 返回失败命令c3
					if (backorder.equals("F1")) {
						String backqbbh = backmsg.substring(2, 8);
						if (backqbbh.equals(buttombh))
							tongxininfo.setText("组网测试失败");
						Log.e("test", "底楼测试失败");
						if (backqbbh.equals(topbh))
						    tongxininfo.setText("顶楼测试失败");
					}
					
					backmsg="";
					backorder="";
				}
			

			} catch (Exception e) {

				Toast.makeText(CaiJiZWActivity.this, "返回数据错误",
						Toast.LENGTH_SHORT).show();
			}

			
			
		}
	};
	// 处理发送数据线程ButtomThread和TopThread传递的数据，更新activitiyi
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle b = msg.getData();
			int cishu = b.getInt("cishu");
			// 底楼测试ButtomThread数据
			if (msg.what == 1) {
				tongxincishu.setText("测试次数:" + String.valueOf(cishu) + "次."
						+ "通过" + String.valueOf(buttomsucceed) + "次");
				
				Log.e("test", String.valueOf(buttomsucceed));
				Log.e("test", String.valueOf(tongguo));
				// 如果测试次数和总次数相等
				if (cishu == total) {
					tongxininfo.setText(String.valueOf(buttomflag) + "楼测试完成");
					if (topflag + 2 == 1) {

						topbt.setEnabled(false);
					} else {
						topbt.setEnabled(true);
					}
					if (buttomflag == qbbhlist.size()) {

						buttombt.setEnabled(false);
					} else {
						buttombt.setEnabled(true);
					}
					// 测试成功次数大于功过次数
					if (buttomsucceed >= tongguo) {

						buttommsg.setText(String.valueOf(buttomflag) + "搂测试成功");
						buttombt.setEnabled(false);

					} else {
						buttommsg.setText(String.valueOf(buttomflag) + "搂测试失败");
					}

				}

			}
			// 顶楼测试topThread数据
			if (msg.what == 2) {

				tongxincishu.setText("测试次数:" + String.valueOf(cishu) + "次."
						+ "通过" + String.valueOf(topsucceed) + "次");
				// 如果测试次数和总次数相等
				
				Log.e("test", String.valueOf(topsucceed));
				Log.e("test", String.valueOf(tongguo));
				if (cishu == total) {
					tongxininfo.setText(String.valueOf(topflag + 2) + "楼测试完成");
					if (topflag + 2 == 1) {

						topbt.setEnabled(false);
					} else {
						topbt.setEnabled(true);
					}
					if (buttomflag == qbbhlist.size()) {

						buttombt.setEnabled(false);
					} else {
						buttombt.setEnabled(true);
					}
					// 测试成功次数大于功过次数
					if (topsucceed >= tongguo) {
						topmsg.setText(String.valueOf(topflag + 2) + "搂测试成功");
						topbt.setEnabled(false);
					} else {
						topmsg.setText(String.valueOf(topflag + 2) + "搂测试失败");
					}

				}
			}
		}

	};

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		/*
		 * if (this.buttomflag1 + this.topflag1 == 0)
		 * this.sendbt.setEnabled(false);
		 */
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.buttombt:

			topbt.setEnabled(false);
			buttombt.setEnabled(false);
			tongxininfo.setText("通信中......");
			tongxincishu.setText("");

			// 如果测试次数不写默认为5次
			if (!totalcishu.getText().toString().equals("")) {
				total = Integer.parseInt(totalcishu.getText().toString());
			} else {
				total = 1;
			}
			// 如果通过次数不写默认为3次
			if (!tgcishu.getText().toString().equals("")) {
				tongguo = Integer.parseInt(tgcishu.getText().toString());
			} else {
				tongguo = 1;
			}

			// 启动线程参数buttomflag是为了得到对应数组的表号
			Log.e("test", "开启组网测试线程");
			new Thread(new ButtomThread(buttomflag)).start();
			// new Thread(new timerThread()).start();

			buttomsucceed = 0;
			buttomflag++;

			buttommsg.setText(String.valueOf(buttomflag) + "搂测试中......");
			buttommsg.setTextColor(Color.RED);
			topmsg.setText("");

			break;

		case R.id.topbt:

			topbt.setEnabled(false);
			buttombt.setEnabled(false);
			// new Thread(new timerThread()).start();
			tongxininfo.setText("通信中......");
			tongxincishu.setText("");

			// 如果测试次数不写默认为5次
			if (!totalcishu.getText().toString().equals("")) {
				total = Integer.parseInt(totalcishu.getText().toString());
			} else {
				total = 1;
			}
			// 如果通过次数不写默认为3次
			if (!tgcishu.getText().toString().equals("")) {
				tongguo = Integer.parseInt(tgcishu.getText().toString());
			} else {
				tongguo = 1;
			}
			// 启动线程参数TopThread是为了得到对应数组的表号
			new Thread(new TopThread(topflag)).start();
			Log.e("test", "开启顶楼测试线程");
			topsucceed = 0;
			topflag--;

			topmsg.setText(String.valueOf(topflag + 2) + "搂测试中......");
			topmsg.setTextColor(Color.RED);
			buttommsg.setText("");

			break;
		case R.id.sendbt:
			// 得到应该真实传递的表号数组qbbhlist
			int k = qbbhlist.size();

			for (int j = 1; j < k - topflag - 1; j++) {

				qbbhlist.remove(k - j);

			}

			for (int i = 0; i < buttomflag - 1; i++) {

				qbbhlist.remove(0);

			}

			for (int i = 0; i < qbbhlist.size(); i++) {
				ContentValues valuesflag = new ContentValues();
				String jzqnb="2492"+getqbbh.GetQbbh(jzqflag,6);
				Log.e("test", jzqnb);
				valuesflag.put("jzqflag", jzqnb);

				dbwriter.update("ximeitable", valuesflag, "qbbh=?",
						new String[] { qbbhlist.get(i) });

			}

			Toast.makeText(CaiJiZWActivity.this, "写入成功", Toast.LENGTH_SHORT)
					.show();

			break;
		}

	}

	class TopThread implements Runnable {

		int buttom;

		public TopThread(int arg2) {

			this.buttom = arg2;
		}

		public void run() {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			
			for (cishu = 1; cishu <= total; cishu++) {

                String qbbh = qbbhlist.get(buttom);
				
				// 把表号转换为8位
				String allqbbh = getqbbh.GetQbbh(qbbh,8);
				
				// 得到qbbh的第四位转化为String类型
				String comqbbh = String.valueOf(allqbbh.charAt(3));
             
				topbh = getmsg.getMsgID(Integer.toHexString(Integer
						.parseInt((String) qbbhlist.get(buttom))));
                if ( comqbbh.equals("9")|| comqbbh.equals("8")) {
                	
                	CRCmsg = ("09" + target + "0C" + "04"+"01" + topbh ).toUpperCase();
                	
                }else{
                	
                	CRCmsg = ("09" + target + "0C" + "04"+"00" + topbh ).toUpperCase();
                }
				ordermsg = headmsg + CRCmsg
						+ crc.CRC_CCITT(1, CRCmsg).toUpperCase()
						+ "AA40F9035B5B/";
				intent.putExtra("order", ordermsg);
				Log.e("test", "顶楼数据" + ordermsg);
				intent.setClass(CaiJiZWActivity.this, BtXiMeiService.class);
				CaiJiZWActivity.this.startService(intent);

				try {
					Thread.sleep(22500);
					Message msg = handler.obtainMessage();
					msg.what = 2;
					Bundle b = new Bundle();
					b.putInt("cishu", cishu);
					b.putString("order", ordermsg);
					msg.setData(b);
					handler.sendMessage(msg);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	
	class ButtomThread implements Runnable {

		int buttom;

		public ButtomThread(int arg2) {

			this.buttom = arg2;
		}

		public void run() {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			Log.e("test", "底楼开始发送数据到模块");
			for (cishu = 1; cishu <= total; cishu++) {
				
				String qbbh = qbbhlist.get(buttom);
				
				// 把表号转换为8位
				String allqbbh = getqbbh.GetQbbh(qbbh,8);
				
				// 得到qbbh的第四位转化为String类型
				String comqbbh = String.valueOf(allqbbh.charAt(3));
             
				buttombh = getmsg.getMsgID(Integer.toHexString(Integer
						.parseInt((String) qbbhlist.get(buttom))));
                if ( comqbbh.equals("9")|| comqbbh.equals("8")) {
                	
                	CRCmsg = ("09" + target + "0C" + "04"+"01" + buttombh ).toUpperCase();
                	
                }else{
                	
                	CRCmsg = ("09" + target + "0C" + "04"+"00" + buttombh ).toUpperCase();
                }

				
				ordermsg = headmsg + CRCmsg+ crc.CRC_CCITT(1, CRCmsg).toUpperCase()+ "AA40F9035B5B/";

				intent.putExtra("order", ordermsg);
				intent.setClass(CaiJiZWActivity.this, BtXiMeiService.class);
				CaiJiZWActivity.this.startService(intent);

				try {
					Thread.sleep(22500);
					Message msg = handler.obtainMessage();
					msg.what = 1;
					Bundle b = new Bundle();
					b.putInt("cishu", cishu);
					b.putString("order", ordermsg);
					msg.setData(b);
					handler.sendMessage(msg);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

}
