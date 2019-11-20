package com.ximei.tiny.backinfoview;


import com.tiny.gasxm.R;
import com.ximei.tiny.tools.GetmsgID;
import com.ximei.tiny.tools.ToInverted;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 单个抄表通信activity
 * 通信失败显示失败
 * 抄表成功跳转页面
 * 
 */
public class BackSingleCBActivity extends Activity {
	private TextView backlogin;
	private TextView fail;
	MyReceiver myreceiver;
	private ProgressBar pro1;
	private TextView succeed,allmsg;
	String testzw;
	String Comm;
	String databasename;
	ToInverted toinver;
	GetmsgID getmsg;
	int count=1;
	int oknum=0,nonum=0;
	int i=0,time=21,t2=0,testnum=0;
	String okflag="no",flag="ok";
	String msg;
	String sendorder;
	// 通信失败handler更新activity
	Handler handler = new Handler() {
		public void handleMessage(Message paramAnonymousMessage) {
			if (paramAnonymousMessage.what == 1) {
				BackSingleCBActivity.this.fail.setText("通信失败 ");
				BackSingleCBActivity.this.pro1.setVisibility(8);
				BackSingleCBActivity.this.backlogin.setText("");
			}
			if (paramAnonymousMessage.what == 2) {
				succeed.setText("成功"+oknum+"次");
				fail.setText("失败"+nonum+"次    包括数据错误："+testnum+"次");
				allmsg.setText("第"+(oknum+nonum+1)+"/"+count+"次");
			}
			super.handleMessage(paramAnonymousMessage);
		}
	};

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.backsinglecb);
		this.succeed = ((TextView) findViewById(R.id.succeed));
		this.allmsg = ((TextView) findViewById(R.id.allmsg));
		this.fail = ((TextView) findViewById(R.id.fail));
		this.pro1 = ((ProgressBar) findViewById(R.id.pro1));
		this.backlogin = ((TextView) findViewById(R.id.backlogin));
		this.backlogin.setText("通信中......");
		this.myreceiver = new MyReceiver();
		Comm=getIntent().getStringExtra("Comm");
		count=getIntent().getIntExtra("count", 1);
		toinver = new ToInverted();
		getmsg = new GetmsgID();
		// 注册普通抄表广播
		IntentFilter localIntentFilter = new IntentFilter();
		localIntentFilter.addAction("android.intent.action.putongcb_BROADCAST");
		localIntentFilter.addAction("android.com.tiny.action.queryzt");
		localIntentFilter.addAction("android.com.tiny.action.testgb");//测试广播
		registerReceiver(this.myreceiver, localIntentFilter);
		// 启动时间线程（超过时间提示抄表失败）
		new Thread(new MyThread()).start();
		oknum=0;
		nonum=0;
		if(Comm.equals("00")) {
			t2=6;
			time=19;
			new Thread(new MyThread1()).start();
		}
		if(Comm.equals("02")) {
			t2=14;
			time=14;
			new Thread(new MyThread1()).start();
		}
	}

	protected void onDestroy() {
		//unregisterReceiver(this.myreceiver);
		flag ="stop";
		finish();
		super.onDestroy();
	}
	
	public void stop() {
		flag ="stop";
		unregisterReceiver(this.myreceiver);
		Intent intentBusy1 = new Intent("android.intent.action.putongcb_yes");
		intentBusy1.putExtra("flag", "stop");
		sendBroadcast(intentBusy1);
		finish();
	}
	
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-exitTime) > 2000){  
	            Toast.makeText(getApplicationContext(), "再按一次退出抄表", Toast.LENGTH_SHORT).show();                                
	            exitTime = System.currentTimeMillis();
	        } else {
	        	stop();
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}
	// 接受抄表成功广播消息
	private class MyReceiver extends BroadcastReceiver {//5696709

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Intent intentBusy = new Intent("android.intent.action.busy");
			intentBusy.putExtra("State", "idle");
			sendBroadcast(intentBusy);
			if (intent.getAction().equals("android.com.tiny.action.testgb")) {
				testnum++;
				fail.setText("失败"+nonum+"次    包括数据错误："+testnum+"次");
			}
			if (intent.getAction().equals("android.intent.action.putongcb_BROADCAST")) {
				i=0;
				okflag="ok";
				oknum++;
				Intent intentBusy1 = new Intent("android.intent.action.putongcb_yes");
				intentBusy1.putExtra("flag", "yes2");
				sendBroadcast(intentBusy1);
				// 跳传到显示抄表信息activity
				msg = intent.getStringExtra("resmsg");
				sendorder= intent.getStringExtra("sendorder");
				int cot = intent.getIntExtra("count", 0);
				if(cot == 1 || Comm.equals("01") || oknum+nonum == count) {
					Intent localIntent = new Intent();
					localIntent.putExtra("resmsg", msg);
					localIntent.putExtra("oknum", oknum);
					localIntent.putExtra("count", count);
					localIntent.putExtra("sendorder", sendorder);
					localIntent.setClass(BackSingleCBActivity.this,BackSingleInFoActivity.class);
					localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					BackSingleCBActivity.this.startActivity(localIntent);
					stop();
				}
				
			}
			if (intent.getAction().equals("android.com.tiny.action.queryzt")) {
				// 跳传到查询中继器信息activity
				String msg = intent.getStringExtra("resmsg");
				Intent localIntent = new Intent();
				localIntent.putExtra("resmsg", msg);
				//String sendorder= intent.getStringExtra("sendorder");
				//localIntent.putExtra("sendorder", sendorder);
				localIntent.setClass(BackSingleCBActivity.this,BackSingleInFoActivity.class);
				localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				BackSingleCBActivity.this.startActivity(localIntent);

			}
			

		}

	}
  // 时间线程13秒后显示通信失败
	public class MyThread implements Runnable {

		public void run() {
			try {
				if(Comm.equals("01")) {
					Thread.sleep(15000L);
					Message localMessage = new Message();
					localMessage.what = 1;
					BackSingleCBActivity.this.handler.sendMessage(localMessage);
					return;
				}
				/*else if(Comm.equals("00"))
					Thread.sleep(count*13000+10);
				flag ="stop";*/
				
			} catch (InterruptedException localInterruptedException) {
				localInterruptedException.printStackTrace();
			}
		}
	}
	// 
		public class MyThread1 implements Runnable {

			public void run() {
				try {
					while(flag.equals("ok")) {
						Message localMessage = new Message();
						localMessage.what = 2;
						BackSingleCBActivity.this.handler.sendMessage(localMessage);
						Thread.sleep(1000);
						i++;
						if(i>time) {
							Intent intentBusy1 = new Intent("android.intent.action.putongcb_yes");
							intentBusy1.putExtra("flag", "yes1");
							sendBroadcast(intentBusy1);
							nonum++;
							if(nonum+oknum == count) {
								flag ="stop";
								Intent localIntent = new Intent();
								localIntent.putExtra("resmsg", msg);
								localIntent.putExtra("oknum", oknum);
								localIntent.putExtra("count", count);
								localIntent.putExtra("sendorder", sendorder);
								localIntent.setClass(BackSingleCBActivity.this,BackSingleInFoActivity.class);
								localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								BackSingleCBActivity.this.startActivity(localIntent);
								stop();
							}
							time=t2;
							i=0;
						}
					}
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
}
