package com.tiny.gasxm;

import java.io.IOException;

import org.json.JSONObject;

import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.service.CaijiService;
import com.ximei.tiny.service.DataBaseService;
import com.ximei.tiny.service.BtXiMeiService.ATconnect;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.QydmtoHex;
import com.ximei.tiny.wlwmeter.HtmlService;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 项目的主Activity，所有的Fragment都嵌入在这里。
 * 
 * @author guolin
 */
@TargetApi(11)
public class MainActivity extends Activity implements OnClickListener {
	// 区域代码为66666666
	private String overmsg, ipadd;
	private TextView BtnBtState,BtnBattState,BtnVersion;
	QydmtoHex qydmtohex;

	/**
	 * 用于展示消息的Fragment
	 */
	private CBFragment cbFragment;

	/**
	 * 用于展示联系人的Fragment
	 */
	private AZTQFragment toolFragment;

	/**
	 * 用于展示动态的Fragment
	 */
	private SHWHFragment shwhFragment;

	/**
	 * 用于展示设置的Fragment
	 */
	private AMRFragment amrFragment;

	/**
	 * 消息界面布局
	 */
	private View messageLayout;

	/**
	 * 联系人界面布局
	 */
	private View contactsLayout;

	/**
	 * 动态界面布局
	 */
	private View newsLayout;

	/**
	 * 设置界面布局
	 */
	private View settingLayout;

	/**
	 * 在Tab布局上显示消息图标的控件
	 */
	private ImageView messageImage;

	/**
	 * 在Tab布局上显示联系人图标的控件
	 */
	private ImageView contactsImage;

	/**
	 * 在Tab布局上显示动态图标的控件
	 */
	private ImageView newsImage;

	/**
	 * 在Tab布局上显示设置图标的控件
	 */
	private ImageView settingImage;

	/**
	 * 在Tab布局上显示消息标题的控件
	 */
	private TextView messageText;

	/**
	 * 在Tab布局上显示联系人标题的控件
	 */
	private TextView contactsText;

	/**
	 * 在Tab布局上显示动态标题的控件
	 */
	private TextView newsText;

	/**
	 * 在Tab布局上显示设置标题的控件
	 */
	private TextView settingText;
	private String urlpath;
	private BluetoothAdapter mBluetoothAdapter; 
	private FragmentManager fragmentManager;
	private ProgressDialog progressdialog;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		// 初始化布局元素
		qydmtohex = new QydmtoHex();
		overmsg = qydmtohex.qydmtohex("66666666");
		initViews();
		// 注册Receiver来获取蓝牙设备相关的结果
		IntentFilter intent = new IntentFilter();
		// 监听蓝牙连接状态的广播
		intent.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		intent.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		intent.addAction(BluetoothDevice.ACTION_CLASS_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
		registerReceiver(BTConnectstate, intent);
		BattDectreceiver battDectreceiver = new BattDectreceiver();
		IntentFilter localIntentFilter = new IntentFilter();
		localIntentFilter.addAction("android.intent.action.BattDect");
		registerReceiver(battDectreceiver, localIntentFilter);		
		// 启动蓝牙通信service
//        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);  
//		 mBluetoothAdapter = bluetoothManager.getAdapter(); 
		Log.e("test", "开始启动service");
		Intent intent1 = new Intent();
		intent1.putExtra("order", "");
		intent1.setClass(MainActivity.this, BtXiMeiService.class);
		startService(intent1);		
		fragmentManager = getFragmentManager();
		// 第一次启动时选中第0个tab
		setTabSelection(0);

	}

	/**
	 * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
	 */
	@SuppressLint("NewApi")
	private void initViews() {
		messageLayout = findViewById(R.id.message_layout);
		contactsLayout = findViewById(R.id.contacts_layout);
		newsLayout = findViewById(R.id.news_layout);
		settingLayout = findViewById(R.id.setting_layout);
		messageImage = (ImageView) findViewById(R.id.message_image);
		contactsImage = (ImageView) findViewById(R.id.contacts_image);
		newsImage = (ImageView) findViewById(R.id.news_image);
		settingImage = (ImageView) findViewById(R.id.setting_image);
		messageText = (TextView) findViewById(R.id.message_text);
		contactsText = (TextView) findViewById(R.id.contacts_text);
		newsText = (TextView) findViewById(R.id.news_text);
		settingText = (TextView) findViewById(R.id.setting_text);
		BtnBtState =(TextView) findViewById(R.id.Btstate);		
		BtnBattState =(TextView) findViewById(R.id.Battstate);
		BtnVersion =  (TextView) findViewById(R.id.HardVersion);
		messageLayout.setOnClickListener(this);
		contactsLayout.setOnClickListener(this);
		newsLayout.setOnClickListener(this);
		settingLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.message_layout:
			// 当点击了消息tab时，选中第1个tab
			setTabSelection(0);
			break;
		case R.id.contacts_layout:
			// 当点击了联系人tab时，选中第2个tab
			setTabSelection(1);
			break;
		case R.id.news_layout:
			// 当点击了动态tab时，选中第3个tab
			setTabSelection(2);
			break;
		case R.id.setting_layout:
			// 当点击了设置tab时，选中第4个tab
			// setTabSelection(3)
			LoginDialog dialog = new LoginDialog(this, "登录",
					new LoginDialog.OnCustomDialogListener() {

						@Override
						public void back(String qlcs) {
							Log.e("test", qlcs);
							try {

								String[] namepsw = qlcs.split("\\|");
								String username = namepsw[0];
								String psw = namepsw[1];
								ipadd = namepsw[2];

								Log.e("test", username);
								Log.e("test", psw);
								Log.e("test", ipadd);

								urlpath = "http://"
										+ ipadd
										+ ":8080/gashub/api/imeter/login?username="
										+ username + "&password=" + psw;
								
								progressdialog =ProgressDialog.show(MainActivity.this,"正在登录", "正在登录请等待...");
								Log.e("test", urlpath);
								new Thread(new DowNetString()).start();
								new Thread(new timeOutThread()).start();
							} catch (Exception e) {
								Log.e("test", "输入不正确");
							}

						}
					});
			dialog.show();
			break;
		default:
			break;
		}
	}

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 * 
	 * 
	 * @param index
	 *            每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
	 */
	@SuppressLint({ "NewApi", "NewApi" })
	private void setTabSelection(int index) {
		// 每次选中之前先清楚掉上次的选中状态
		clearSelection();

		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		Bundle args = new Bundle();
		args.putString("overmsg", overmsg);
		args.putString("ipadd", ipadd);
		switch (index) {
		case 0:
			// 当点击了消息tab时，改变控件的图片和文字颜色
			messageImage.setImageResource(R.drawable.cbdown);
			messageText.setTextColor(Color.WHITE);
			if (cbFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				cbFragment = new CBFragment();

				cbFragment.setArguments(args);

				transaction.add(R.id.content, cbFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(cbFragment);
			}
			break;
		case 1:
			// 当点击了联系人tab时，改变控件的图片和文字颜色
			contactsImage.setImageResource(R.drawable.tqdown);
			contactsText.setTextColor(Color.WHITE);
			if (shwhFragment == null) {
				// 如果ContactsFragment为空，则创建一个并添加到界面上
				shwhFragment = new SHWHFragment();
				shwhFragment.setArguments(args);
				transaction.add(R.id.content, shwhFragment);
			} else {
				// 如果ContactsFragment不为空，则直接将它显示出来
				transaction.show(shwhFragment);
			}
			break;
		case 2:
			// 当点击了动态tab时，改变控件的图片和文字颜色
			newsImage.setImageResource(R.drawable.shdown);
			newsText.setTextColor(Color.WHITE);
			if (amrFragment == null) {
				// 如果NewsFragment为空，则创建一个并添加到界面上
				amrFragment = new AMRFragment();
				amrFragment.setArguments(args);
				transaction.add(R.id.content, amrFragment);
			} else {
				// 如果NewsFragment不为空，则直接将它显示出来
				transaction.show(amrFragment);
			}
			break;
		case 3:
		default:
			// 当点击了设置tab时，改变控件的图片和文字颜色
			settingImage.setImageResource(R.drawable.amrdown);
			settingText.setTextColor(Color.WHITE);

			if (toolFragment == null) {
				// 如果SettingFragment为空，则创建一个并添加到界面上
				toolFragment = new AZTQFragment();
				toolFragment.setArguments(args);
				transaction.add(R.id.content, toolFragment);
			} else {
				// 如果SettingFragment不为空，则直接将它显示出来
				transaction.show(toolFragment);
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void clearSelection() {
		messageImage.setImageResource(R.drawable.cbup);
		messageText.setTextColor(Color.parseColor("#82858b"));
		contactsImage.setImageResource(R.drawable.tqup);
		contactsText.setTextColor(Color.parseColor("#82858b"));
		newsImage.setImageResource(R.drawable.shup);
		newsText.setTextColor(Color.parseColor("#82858b"));
		settingImage.setImageResource(R.drawable.amrup);
		settingText.setTextColor(Color.parseColor("#82858b"));
	}

	class DowNetString implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			try {
                
				String netString = HtmlService.gethtml(urlpath);
				JSONObject dataJson = new JSONObject(netString);
				String statusCode = dataJson.getString("statusCode");
				String backmsg = dataJson.getString("message");
				Log.e("test", statusCode);
				Message msg = new Message();
				if (statusCode.equals("200") || backmsg.equals("登录成功!")) {

					msg.what = 1;
					NetHandler.sendMessage(msg);

				} else {

					msg.what = 2;
					NetHandler.sendMessage(msg);

				}

				// Log.e("test", netString);
				// Message msg = new Message();

			} catch (Exception e) {
				Log.e("test", e.toString());
				// Toast.makeText(DataActivity.this, "获得图片失败", 1).show();
			}

		}

	}
	class timeOutThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			try {
				Thread.sleep(60000);
				Message msg = new Message();
				msg.what = 3;
				NetHandler.sendMessage(msg);
					
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	Handler NetHandler = new Handler() {

		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				Log.e("test", "handle");
				progressdialog.dismiss();
				setTabSelection(3);
			}
			if (msg.what == 2) {
				Log.e("test", "登录失败");
				Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
				progressdialog.dismiss();
			}
			if (msg.what == 3) {
				Log.e("test", "登录失败");
				Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
				progressdialog.dismiss();
			}


		}

	};

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (cbFragment != null) {
			transaction.hide(cbFragment);
		}
		if (shwhFragment != null) {
			transaction.hide(shwhFragment);
		}
		if (amrFragment != null) {
			transaction.hide(amrFragment);
		}
		if (toolFragment != null) {
			transaction.hide(toolFragment);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			// 创建退出对话框
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// 设置对话框标题
			isExit.setTitle("确定退出程序");

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
				// System.exit(0);
				new Thread(new stopappthread()).start();
				// ExitApplication.getInstance().exit(context);
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框

				break;
			default:
				break;
			}
		}
	};

	public class stopappthread implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
		//	 mBluetoothAdapter.disable();
			// 发送断开蓝牙连接广播 
			Intent intent4 = new Intent("android.intent.action.disbtconnect");
			MainActivity.this.sendBroadcast(intent4);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 停止CaijiService
			Intent intent1 = new Intent();
			intent1.setClass(MainActivity.this, CaijiService.class);
			MainActivity.this.stopService(intent1);
			// 停止DataBaseService
			Intent intent2 = new Intent();
			intent2.setClass(MainActivity.this, DataBaseService.class);
			MainActivity.this.stopService(intent2);
			// 停止BtXiMeiService
			Intent intent3 = new Intent();
			intent3.setClass(MainActivity.this, BtXiMeiService.class);
			MainActivity.this.stopService(intent3);
			finish();
			System.exit(0);

		}

	}

	private class BattDectreceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO 自动生成的方法存根
			if (intent.getAction().equals("android.intent.action.BattDect")) 
			{
                CRC crc =new CRC();
				String msg = intent.getStringExtra("resmsg");
				
				if(msg.substring(22, 26).equals(crc.CRC_CCITT(1, msg.substring(4, 22)).toUpperCase()))
				{
					int Vol=Integer.parseInt(msg.substring(18, 22), 16);
					BtnVersion.setText("模块版本："+msg.substring(16, 18));
					if(Vol<=350)
					{
						BtnBattState.setTextColor(0xffff0000); //红色
					}
					else
					{
						BtnBattState.setTextColor(0xff000000); //黑色
					}	
					BtnBattState.setText("电池电压："+Vol/100+'.'+Vol%100+'V');	
					BtnBtState.setTextColor(0xff000000); //黑色
					BtnBtState.setText("蓝牙状态：已连接");
					
				}
		    }
		
	}
	}
	// 广播接收器监听filter事件处理
	private final BroadcastReceiver BTConnectstate = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
					
				//BtnBtState.setTextColor(0xff000000); //黑色
				//BtnBtState.setText("蓝牙状态：已连接");
			}
			else if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
				BtnBtState.setTextColor(0xffff0000); //红色
				BtnBtState.setText("蓝牙状态：已断开");
				BtnBattState.setText("电池电压：--");
				BtnVersion.setText("模块版本：--");
			}

		}

	};
}
