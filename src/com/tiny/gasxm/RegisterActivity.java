package com.tiny.gasxm;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ximei.tiny.database.UserBDhelper;
import com.ximei.tiny.tools.MacToSerial;
/*
 * 用户注册界面，自动生成的序列号。
 * 用户填写用户名和密码(有相应的提示)
 * 厂家给出注册码
 */
public class RegisterActivity extends Activity implements OnClickListener {
	String backonemac;
	String backpower;
	String backqydm;
	String backtwomac;
	UserBDhelper bdhelper;
	MacToSerial getserial;
	Intent intent;
	String macstr;
	String password;
	String passwordhint;
	private EditText passwordinput;
	String queryusername;
	SQLiteDatabase readdb;
	private EditText registerinput;
	String registernb;
	String registernbhint;
	private Button regsiterbt;
	private EditText serialinput;
	String serialstr;
	String timestr;
	private TextView tvpassword;
	private TextView tvregister;
	private TextView tvusername;
	String username;
	String usernamehint;
	private EditText usernameinput;
	SQLiteDatabase writerdb;
	SQLiteDatabase readerdb;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		username = "";
		queryusername = "";
		password = "";
		registernb = "";

		timestr = "";
		getserial = new MacToSerial();
		//得到对数据库userdb的操作权限，相应的读写方法
		bdhelper = new UserBDhelper(this, "userdb");
		readdb = bdhelper.getReadableDatabase();
		writerdb = bdhelper.getWritableDatabase();
		readerdb = bdhelper.getReadableDatabase();
		intent = new Intent();
		//得到输入文本和按钮句柄
		usernameinput = ((EditText) findViewById(R.id.registerusername));
		passwordinput = ((EditText) findViewById(R.id.regsiterpassword));
		serialinput = ((EditText) findViewById(R.id.serial));
		registerinput = ((EditText) findViewById(R.id.regsiternb));
		tvusername = ((TextView) findViewById(R.id.usernamehint));
		tvpassword = ((TextView) findViewById(R.id.passwordhint));
		tvregister = ((TextView) findViewById(R.id.registernbhint));
		regsiterbt = ((Button) findViewById(R.id.registerbt));
		//注册按钮监听器
		regsiterbt.setOnClickListener(this);
//		//得到手机mac地址
//		macstr=getLocalMacAddress();
//		//mac地址去掉":"
//		serialstr = macstr.replaceAll(":", "").toUpperCase();
//		//设置序列号的值
//		serialinput.setText(getserial.mactoserial(serialstr, 2));
		serialinput.setFocusable(false);
        //用户名得失焦点事件监听器
		usernameinput.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
                    //得到焦点置为空
					tvusername.setText("");
					usernameinput.setText("");

				} else {
                    //失去焦点的时候判断用户名为空提示不能为空
					username = usernameinput.getText().toString().toUpperCase();
					if (username.equals("")) {
						tvusername.setText("用户名不能为空");
						tvusername.setTextColor(-65536);

					} else {
						//用户名不为空查找数据库，判断是否存在
						SQLiteDatabase localSQLiteDatabase = RegisterActivity.this.readdb;
						String[] arrayOfString = new String[1];
						arrayOfString[0] = username;
						Cursor localCursor = localSQLiteDatabase.query(
								"usertable", null, "username=?", arrayOfString,
								null, null, null);
						if (localCursor.getCount() != 0) {
                            //如果存在提示
							tvusername.setText("用户名已存在");
							tvusername.setTextColor(-65536);
							// queryusername =
							// localCursor.getString(localCursor.getColumnIndex("username"));
						}

					}

				}

			}
		});
		//密码得失焦点事件监听器
		passwordinput.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View paramAnonymousView,
					boolean paramAnonymousBoolean) {
				if (paramAnonymousBoolean) {
					 //得到焦点置为空
					tvpassword.setText("");
					passwordinput.setText("");

				} else {
					//失去焦点提示不能为空
					password = passwordinput.getText().toString();
					if (password.equals("")) {
						tvpassword.setText("密码不能为空");
						tvpassword.setTextColor(-65536);

					}

				}
			}
		});
		//注册码得失焦点事件监听器
		registerinput.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View paramAnonymousView,
					boolean paramAnonymousBoolean) {
//				if (paramAnonymousBoolean) 
//				  {  //得到焦点置为空
//					 tvregister.setText("");
//					 registerinput.setText("");
//				  } else {
//
//					registernb = registerinput.getText().toString().toUpperCase();
//					if (registernb.equals("")) 
//					 {  //如果为空提示不能为空
//						tvregister.setText("注册码不能为空");
//						tvregister.setTextColor(-65536);
//					 }
//					if (!registernb.equals(""))
//					  { 
//						//不为空但是长度不等于21提示注册码错误
//						if (registernb.length() != 21) {
//							tvregister.setText("注册码错误");
//							tvregister.setTextColor(-65536);
//
//						} else {
//							try {
//								backtwomac = getserial.mactoserial(registernb.substring(0, 12), 3);
//								backonemac = getserial.mactoserial(backtwomac,2);
//								backqydm = getserial.mactoserial(registernb.substring(12, 20), 3);
//								backpower = registernb.substring(20, 21);
//								if (!backonemac.equals(serialstr)) {
//									//注册码通过计算后与序列号不等历史校验失败
//									tvregister.setText("注册码校验错误");
//									tvregister.setTextColor(-65536);
//
//								}
//							} catch (Exception localException) {
//								tvregister.setText("注册码校验错误");
//								tvregister.setTextColor(-65536);
//							}
//						}
//
//					}

//				}
			}
		});
	}
	
	
	//当activity处于onPause状态结束activtiy
			@Override
			protected void onPause() {
				// TODO Auto-generated method stub
				super.onPause();
				finish();
			}
  



   //得到mac地址方法
	public String getLocalMacAddress() {
		return ((WifiManager) getSystemService("wifi")).getConnectionInfo()
				.getMacAddress();
	}
   //监听注册按钮时间
	public void onClick(View paramView) {
		switch (paramView.getId()) {

		case R.id.registerbt:
            //得到用户输入的用户名，密码，注册码。
			username = usernameinput.getText().toString().toUpperCase();
			password = passwordinput.getText().toString().toUpperCase();
			registernb = registerinput.getText().toString().toUpperCase();
			//根据用户名查找用户是否存在
			
			//2015-4-28新增快捷注册
			SQLiteDatabase localSQLiteDatabase2 = this.writerdb;
			String[] arrayOfString2 = new String[7];
			arrayOfString2[0] = this.username;
			arrayOfString2[1] = this.password;
			arrayOfString2[2] = "H";
			arrayOfString2[3] = "66666666";
			arrayOfString2[4] = "";
			arrayOfString2[5] = "";
			arrayOfString2[6] = "";
			
			localSQLiteDatabase2.execSQL(
					"insert into usertable values(?,?,?,?,?,?,?)",arrayOfString2);
			Toast.makeText(this, "注册成功,请返回重新登录", Toast.LENGTH_SHORT).show();
//			intent.setClass(this, LoginActivity.class);
//			startActivity(this.intent);
			
           //注释原来需要序列号的注册方法			
//			
//			SQLiteDatabase localSQLiteDatabase1 = this.readdb;
//			String[] arrayOfString1 = new String[1];
//			arrayOfString1[0] = username;
//		
//			Cursor localCursor = localSQLiteDatabase1.query("usertable", null,"username=?", arrayOfString1, null, null, null);
//			//用户名不存在
//			if (localCursor.getCount() == 0) {
//				//提示用户名不能为空
//				if (username.equals("")) {
//
//					tvusername.setText("用户名不能为空");
//					tvusername.setTextColor(-65536);
//				}
//				//提示密码不能为空
//				if (password.equals("")) {
//					tvpassword.setText("密码不能为空");
//					tvpassword.setTextColor(-65536);
//				}
//				//提示注册码不能为空
//				if (registernb.equals("")) {
//					tvregister.setText("注册码不能为空");
//					tvregister.setTextColor(-65536);
//				} else {
//
//					if (registernb.length() == 21) {
//						try{
//						backtwomac = getserial.mactoserial(registernb.substring(0, 12), 3);
//						backonemac = getserial.mactoserial(backtwomac, 2);
//						backqydm = getserial.mactoserial(registernb.substring(12, 20), 3);
//						backpower = this.registernb.substring(20, 21);
//						if (!backonemac.equals(serialstr)) 
//						{
//							tvregister.setText("注册码校验错误");
//							tvregister.setTextColor(-65536);
//						}
//						}catch(Exception e){
//							
//							tvregister.setText("注册码校验错误");
//							tvregister.setTextColor(-65536);
//						}
//					}else{
//						tvregister.setText("注册码校验错误");
//						tvregister.setTextColor(-65536);
//						
//					}
//
//				}
//               //校验成功注册写入数据库，返回到登录界面
//				if ((!username.equals("")) && (!this.password.equals(""))&& (registernb.length() == 21)&& (backonemac.equals(serialstr))) {
//					SQLiteDatabase localSQLiteDatabase2 = this.writerdb;
//					String[] arrayOfString2 = new String[7];
//					arrayOfString2[0] = this.username;
//					arrayOfString2[1] = this.password;
//					arrayOfString2[2] = this.backpower;
//					arrayOfString2[3] = this.backqydm;
//					arrayOfString2[4] = this.backonemac;
//					arrayOfString2[5] = "";
//					arrayOfString2[6] = "";
//					Log.e("test", username);
//					Log.e("test", password);
//					Log.e("test", backpower);
//					Log.e("test", backqydm);
//					Log.e("test", backonemac);
//					
//					localSQLiteDatabase2.execSQL(
//							"insert into usertable values(?,?,?,?,?,?,?)",arrayOfString2);
//					
//					Cursor cursor =readerdb.query("usertable", null, null, null, null, null, null);
//					while(cursor.moveToNext()){
//					    String str = cursor.getString(cursor.getColumnIndex("username"));
//						
//						Log.e("test",str );
//						
//					}
//					//Toast.makeText(this, "注册成功,请重新登录", Toast.LENGTH_SHORT).show();
//					intent.setClass(this, LoginActivity.class);
//					startActivity(this.intent);
//
//				}
//
//			} else {
//
//				while (localCursor.moveToNext()) {
//					queryusername = localCursor.getString(localCursor
//							.getColumnIndex("username"));
//
//				}
//
//				if (!this.username.equals(this.queryusername)) {
//
//					this.tvusername.setText("用户名已存在");
//					this.tvusername.setTextColor(-65536);
//				}
//
//			}

		}
	}

}
