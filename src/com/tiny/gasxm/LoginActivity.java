package com.tiny.gasxm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ximei.tiny.database.UserBDhelper;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.service.CaijiService;
import com.ximei.tiny.service.DataBaseService;
import com.ximei.tiny.tools.ExitApplication;
import com.ximei.tiny.tools.FileUtils;

/*
 * 用户登录界面，根据不同的用户名进去不同的界面
 * 用户名和密码不区分大小写
 * 
 * 
 * 
 * 
 */
public class LoginActivity extends Activity implements OnClickListener {

	UserBDhelper bdhelper;
	FileUtils fileutil;
	Intent intent;
	private Button loginbt;
	private TextView guoshi;
	String password;
	private EditText passwordinput;
	String power;
	String querypassword;
	String queryusername;
	SQLiteDatabase readdb;
	private Button regsiterbt;
	String username, pathsdcard;
	private EditText usernameinput;
	SQLiteDatabase writerdb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取消标题栏和状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.login);
		//ExitApplication.getInstance().addActivity(this);
		
		LoginActivity.this.deleteDatabase("userdb");

		this.username = "";
		this.password = "";
		this.intent = new Intent();
		// 删除数据库
		//LoginActivity.this.deleteDatabase("userdb");
		// 得到数据库userdb的操作权限，得到对应的读写方法
		this.bdhelper = new UserBDhelper(this, "userdb");
		this.readdb = this.bdhelper.getReadableDatabase();
		this.writerdb = this.bdhelper.getWritableDatabase();
		// 判断手机sdcard里面是否存在ximei和newximei文件夹

		fileutil = new FileUtils();
		pathsdcard = fileutil.getSDPATH();
		

		// 如果不存在就创建ximei
		if (!fileutil.isFileExist("ximei")) {
			fileutil.creatSDDir("ximei");
		}
		// 如果不存在就创建newximei
		if (!fileutil.isFileExist("newximei")) {
			fileutil.creatSDDir("newximei");
		}
		// 如果不存在就创建alldata
		if (!fileutil.isFileExist("alldata")) {
			fileutil.creatSDDir("alldata");
		}

		// 写入默认的用用户名和密码
		if (this.readdb.query("usertable", null, null, null, null, null, null).getCount() == 0){
			this.writerdb.execSQL(
					"insert into usertable values(?,?,?,?,?,?,?)",
					new String[] { "ADMIN", "ADMIN", "H", "66666666", "", "",
							"" });
			
			this.writerdb.execSQL(
					"insert into usertable values(?,?,?,?,?,?,?)",
					new String[] { "", "", "H", "66666666", "", "",
							"" });

		
		}
		
		Cursor cursor =readdb.query("usertable", null, null, null, null, null, null);
		while(cursor.moveToNext()){
		    String str = cursor.getString(cursor.getColumnIndex("username"));
			
			Log.e("test",str );
			
		}
		// 得到相应的输入框和按钮句柄
		this.usernameinput = ((EditText) findViewById(R.id.username));
		this.passwordinput = ((EditText) findViewById(R.id.password));
		this.regsiterbt = ((Button) findViewById(R.id.register));
		this.loginbt = ((Button) findViewById(R.id.login));
		this.guoshi = (TextView) findViewById(R.id.guoshi);
		// 注册监听器
		this.regsiterbt.setOnClickListener(this);
		this.loginbt.setOnClickListener(this);

		

	}

	// 按钮监听内容
	public void onClick(View v) {

		// TODO Auto-generated method stub

		switch (v.getId()) {
		// 监听登录按钮
		case R.id.login:

			// 得到输入的用户名和密码
			this.username = this.usernameinput.getText().toString()
					.toUpperCase();
			this.password = this.passwordinput.getText().toString()
					.toUpperCase();
			// 根据用户名查找用户信息
			SQLiteDatabase localSQLiteDatabase = this.readdb;
			String[] arrayOfString = new String[1];
			arrayOfString[0] = this.username;
			Cursor localCursor = localSQLiteDatabase.query("usertable", null,
					"username=?", arrayOfString, null, null, null);
			// 如果查找内容为空，提示用户名或密码错误
			if (localCursor.getCount() == 0) {
				Toast.makeText(this, "用户不存在", Toast.LENGTH_SHORT).show();

			} else {
				// 如果查找内容不为空，得到相应的密码和权限
				while (localCursor.moveToNext()) {
					queryusername = localCursor.getString(localCursor
							.getColumnIndex("username"));
					querypassword = localCursor.getString(localCursor
							.getColumnIndex("password"));
					power = localCursor.getString(localCursor
							.getColumnIndex("power"));
				}
				if (!password.equals(querypassword)) {
					Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();

				} else {

					// Toast.makeText(LoginActivity.this,
					// power,Toast.LENGTH_SHORT).show();
					// 根据用户权限跳传到相应的activtiy界面
					Intent intent = new Intent();
					intent.putExtra("power", power);
					intent.putExtra("username", queryusername);
					intent.setClass(LoginActivity.this, MainActivity.class);
					LoginActivity.this.startActivity(intent);

				}
			}

			break;
		// 监听注册按钮
		case R.id.register:
			// 得到WifiManager，打开wifi跳转到注册界面
//			WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//			wifiManager.setWifiEnabled(true);
			intent.setClass(this, RegisterActivity.class);
			startActivity(this.intent);

			break;

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
				//System.exit(0);
				new Thread(new stopappthread()).start();
				 //ExitApplication.getInstance().exit(context);
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

			// 发送断开蓝牙连接广播

			Intent intent4 = new Intent("android.intent.action.disbtconnect");
			LoginActivity.this.sendBroadcast(intent4);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 停止CaijiService
			Intent intent1 = new Intent();
			intent1.setClass(LoginActivity.this, CaijiService.class);
			LoginActivity.this.stopService(intent1);
			 //停止DataBaseService
			Intent intent2 = new Intent();
			intent2.setClass(LoginActivity.this, DataBaseService.class);
			LoginActivity.this.stopService(intent2);
			 //停止BtXiMeiService
			Intent intent3 = new Intent();
			 intent.setClass(LoginActivity.this, BtXiMeiService.class);
			 LoginActivity.this.stopService(intent3);
			 finish();
			 System.exit(0);

		}

	}

}
