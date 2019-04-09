package com.ximei.tiny.backinfoview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tiny.gasxm.R;
import com.ximei.tiny.database.BDhelper;
import com.ximei.tiny.service.DataBaseService;
import com.ximei.tiny.tools.Containstr;
import com.ximei.tiny.tools.SubDong;
import com.ximei.tiny.tools.ToInverted;
/*
 * 集抄返回信息activity
 * 集抄过程中显示已抄数量
 * 抄表成功和失败数量
 * 总共多少只表
 * 
 */
public class BackBiaoCeActivity extends Activity {

	private TextView succeedfail;
	private TextView dongshu;
	private String backinfo, filepath, databasename;
	private ProgressBar pro;

	private TextView qbdz, backlogin, allshu, shenyushu;
	private int countdong, countall, succeedflag, failflag;
	String[] qbdzlist;

	SubDong sub;
	ToInverted toinver;
	Containstr ishave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.backbiaoce);

		sub = new SubDong();
		ishave = new Containstr();
		Intent intent = getIntent();
		succeedflag = 0;
		failflag = 0;
        //接受intent传递过来的数据
		//filepath = intent.getStringExtra("filepath");
		qbdzlist = intent.getStringArrayExtra("sendqbdzlist");
		databasename = intent.getStringExtra("databasename");

	
        //得到相应的控件
		pro = (ProgressBar) findViewById(R.id.probaioce);
		succeedfail = (TextView) findViewById(R.id.succeefail);
		dongshu = (TextView) findViewById(R.id.dongshu);
		qbdz = (TextView) findViewById(R.id.qbdz);
		backlogin = (TextView) findViewById(R.id.backloginbiaoce);
		allshu = (TextView) findViewById(R.id.allshu);
		shenyushu = (TextView) findViewById(R.id.shenyushu);
		backlogin.setText("通信中......");
        // 注册通信成功和失败广播
		MyReceiver receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.tiny.backinfo.chaobiao");
		filter.addAction("com.tiny.sendqbbh.error");

		registerReceiver(receiver, filter);
        //开启线程查询出qbdzlist未抄表的数量
		new Thread(new MyThread()).start();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			// 创建退出对话框
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// 设置对话框标题
			isExit.setTitle("确定退出本次抄表");

			// 设置对话框消息
			// isExit.setMessage("确定要退出吗");
			// 添加选择按钮并注册监听
			isExit.setButton("确定", listener);
			isExit.setButton2("取消", listener);
			// 显示对话框
			isExit.show();

		}

		return false;

	}

	/** 监听对话框里面的button点击事件 */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				Intent intentBusy = new Intent("android.intent.action.busy");
				intentBusy.putExtra("State", "idle");
				sendBroadcast(intentBusy);	
				
				Intent intent1 = new Intent("android.intent.action.MY_BROADCAST2");
				intent1.putExtra("stoporder", "stop");
				sendBroadcast(intent1);
				Intent intent = new Intent();
				intent.setClass(BackBiaoCeActivity.this, DataBaseService.class);
				BackBiaoCeActivity.this.stopService(intent);
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};
    //接受抄表信息广播消息
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals("com.tiny.backinfo.chaobiao")) {
				try {
					//返回数据
					backinfo = intent.getStringExtra("backinfo");
					//已抄数量
					countall = intent.getIntExtra("qbcount", 0);
					String qbdzmsg = intent.getStringExtra("qbdz");
					//抄表失败数量
					failflag = intent.getIntExtra("failflag", 0);
					shenyushu.setText("抄表成功"
							+ String.valueOf(countall - failflag) + "户" + "/"
							+ "抄表失败" + String.valueOf(failflag) + "户");

					dongshu.setText("已经抄表数量" + String.valueOf(countall) + "户");
					qbdz.setText(qbdzmsg);
					if (backinfo.equals("backfail")) {
						succeedfail.setText("通讯失败");
					}
					if (backinfo.equals("backsucceed")) {
						succeedfail.setText("通讯成功");

					}
					if (countall == countdong) {

						MyThread1 mythread1 = new MyThread1();
						new Thread(mythread1).start();

					}
				} catch (Exception e) {

					Toast.makeText(BackBiaoCeActivity.this, "失败",
							Toast.LENGTH_SHORT).show();
				}

			}
            //判断表号错误信息
			if (action.equals("com.tiny.sendqbbh.error")) {

				String qbbherror = intent.getStringExtra("qbbh");
				String qbdzerror = intent.getStringExtra("qbdz");
				qbdz.setText(qbdzerror);
				Toast.makeText(BackBiaoCeActivity.this,
						"表号" + qbbherror + "错误", Toast.LENGTH_SHORT).show();

			}

		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 要做的事情
			Bundle b = msg.getData();
			if (msg.what == 1) {
                //抄表开始的时候统计的总数
				int alldong = b.getInt("alldong");

				dongshu.setText("");

				allshu.setText("总共" + String.valueOf(alldong) + "户");

			}

			if (msg.what == 2) {
               //抄表结束后的统计
				int weichao = b.getInt("weichao");
				dongshu.setText("抄表失败" + String.valueOf(weichao) + "户");

				shenyushu.setText("抄表成功" + String.valueOf(countdong - weichao)
						+ "户");
				succeedfail.setText("");
				qbdz.setText("");
				backlogin.setText("");

				pro.setVisibility(View.GONE);

			}

			super.handleMessage(msg);
		}
	};
    //抄表结束后启动线程查出未抄信息
	public class MyThread1 implements Runnable {

		public void run() {
			// TODO Auto-generated method stub

			try {
				Thread.sleep(1500);
				BDhelper dbhelper = new BDhelper(BackBiaoCeActivity.this,
						databasename);

				SQLiteDatabase readdb = dbhelper.getReadableDatabase();

				Cursor cursor = readdb.query("ximeitable", null, null, null,
						null, null, null);

				int weichao = 0;

				while (cursor.moveToNext()) {

					String qbbh = cursor.getString(cursor
							.getColumnIndex("qbbh"));
					String qbdz = cursor.getString(cursor
							.getColumnIndex("dzms"));
					String qbzd = cursor.getString(cursor
							.getColumnIndex("qbztbh"));

					if (!qbbh.equals("") && ishave.isHave(qbdzlist, qbdz)
							&& qbzd.equals("未抄")) {

						weichao++;
					}
				}

				Message msg = new Message();
				msg.what = 2;

				Bundle b = new Bundle();// 存放数据

				b.putInt("weichao", weichao);
				msg.setData(b);
				BackBiaoCeActivity.this.handler.sendMessage(msg);
				
				
				Intent intentBusy = new Intent("android.intent.action.busy");
				intentBusy.putExtra("State", "idle");
				sendBroadcast(intentBusy);			
				

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	//启动线程查出未抄表数量
	public class MyThread implements Runnable {
		public void run() {
			// TODO Auto-generated method stub

			BDhelper dbhelper = new BDhelper(BackBiaoCeActivity.this,
					databasename);

			SQLiteDatabase readdb = dbhelper.getReadableDatabase();

			Cursor cursor = readdb.query("ximeitable", null, null, null, null,
					null, null);

			while (cursor.moveToNext()) {

				String qbbh = cursor.getString(cursor.getColumnIndex("qbbh"));
				String qbdz = cursor.getString(cursor.getColumnIndex("dzms"));
				String qbzd = cursor.getString(cursor.getColumnIndex("qbztbh"));

				if (!qbbh.equals("") && ishave.isHave(qbdzlist, qbdz)
						&& qbzd.equals("未抄")) {

					countdong++;
				}
			}

			Message msg = new Message();
			msg.what = 1;

			Bundle b = new Bundle();// 存放数据
			b.putInt("alldong", countdong);
			msg.setData(b);
			BackBiaoCeActivity.this.handler.sendMessage(msg);

		}

	}
}
