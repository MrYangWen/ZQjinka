package com.ximei.tiny.backinfoview;



import com.tiny.gasxm.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

/*
 * 对集中器相关信息设置返回activtiy
 * 通过BroadcastReceiver，handler，更新activity
 * 
 */
public class BackCaiJiInFoActivity extends Activity {
	private TextView backlogin;
	private TextView fail;
	//更新activtiy主要显示通信失败
	Handler handler = new Handler() {
		public void handleMessage(Message paramAnonymousMessage) {
			if ((paramAnonymousMessage.what == 2)&& (!BackCaiJiInFoActivity.this.fail.getText().toString().equals("操作成功"))
				 && (!BackCaiJiInFoActivity.this.fail.getText().toString().equals("设置成功"))) {
				BackCaiJiInFoActivity.this.fail.setText("操作失败 ");
				BackCaiJiInFoActivity.this.pro1.setVisibility(8);
				BackCaiJiInFoActivity.this.backlogin.setText("");

				Intent intentBusy = new Intent("android.intent.action.busy");
				intentBusy.putExtra("State", "idle");
				sendBroadcast(intentBusy);	
			}
			super.handleMessage(paramAnonymousMessage);
		}
	};
	MyReceiver myreceiver;
	private ProgressBar pro1;
	private TextView succeed;
	String testzw;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		//取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.backsinglecb);
		this.succeed = ((TextView) findViewById(R.id.succeed));
		this.fail = ((TextView) findViewById(R.id.fail));
		this.pro1 = ((ProgressBar) findViewById(R.id.pro1));
		this.backlogin = ((TextView) findViewById(R.id.backlogin));
		this.backlogin.setText("通信中......");
		//注册设置广播(返回命令为c3的)_
		this.myreceiver = new MyReceiver();
		IntentFilter localIntentFilter = new IntentFilter();
		localIntentFilter.addAction("android.com.tiny.action.backc3order");
		localIntentFilter.addAction("android.com.tiny.action.changenub");
		registerReceiver(this.myreceiver, localIntentFilter);
		//开启时间线程
		new Thread(new MyThreadtest()).start();
	}

	protected void onStop() {
		super.onStop();
		unregisterReceiver(this.myreceiver);
		finish();
	}
    //设置成功广播消息
	private class MyReceiver extends BroadcastReceiver {

		public void onReceive(Context paramContext, Intent paramIntent) {
			
			if (paramIntent.getAction().equals("android.com.tiny.action.backc3order")) {
				String backmsg = paramIntent.getStringExtra("backmsg");
				String datalen = paramIntent.getStringExtra("backdatalen");
				Log.e("test", datalen);
				
				Intent intentBusy = new Intent("android.intent.action.busy");
				intentBusy.putExtra("State", "idle");
				sendBroadcast(intentBusy);	
				if(backmsg.equals("F0")&&datalen.equals("8")){
					BackCaiJiInFoActivity.this.fail.setText("操作成功");
					BackCaiJiInFoActivity.this.pro1.setVisibility(8);
					BackCaiJiInFoActivity.this.backlogin.setText("");
					
				}
				if(backmsg.equals("F0")&&(datalen.equals("2")||datalen.equals("1"))){
					BackCaiJiInFoActivity.this.fail.setText("操作成功");
					BackCaiJiInFoActivity.this.pro1.setVisibility(8);
					BackCaiJiInFoActivity.this.backlogin.setText("");
					
				}
//				
			}
			
			if(paramIntent.getAction().equals("android.com.tiny.action.changenub")){
				
				Intent intentBusy = new Intent("android.intent.action.busy");
				intentBusy.putExtra("State", "idle");
				sendBroadcast(intentBusy);	
				
				BackCaiJiInFoActivity.this.fail.setText("修改成功");
				BackCaiJiInFoActivity.this.pro1.setVisibility(8);
				BackCaiJiInFoActivity.this.backlogin.setText("");
				
			}
			
		}
	}
    //时间线程
	public class MyThreadtest implements Runnable {

		public void run() {
			try {
				Thread.sleep(12000L);
				Message localMessage = new Message();
				localMessage.what = 2;
				BackCaiJiInFoActivity.this.handler.sendMessage(localMessage);
				return;
			} catch (InterruptedException localInterruptedException) {
				localInterruptedException.printStackTrace();
			}
		}
	}
}