package com.ximei.tiny.wlwmeter;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tiny.gasxm.R;
import com.ximei.tiny.tools.GetQydm;
import com.ximei.tiny.tools.GetTotalPack;
import com.ximei.tiny.wlwmeter.MainWLWMeterActivity.DowNetString;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class WLWBugHandleActivity extends Activity implements OnClickListener {

	private EditText editvalue;
	private TextView infohint;
	private Button querybutton;
	private Intent intent;
	String netinfo;
	private ProgressDialog progressdialog;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 取消状态标题栏
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.wlwinput);
		this.infohint = ((TextView) findViewById(R.id.infohint));
		this.editvalue = ((EditText) findViewById(R.id.bughandleinput));
		this.querybutton = ((Button) findViewById(R.id.bughandlebt));
		this.intent = getIntent();

		infohint.setText("输入物联网表号");
		infohint.setTextSize(30);
		querybutton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		netinfo="";
		String qbbh = editvalue.getEditableText().toString();
		//String urlpath = "http://192.168.1.115:8080/gashub/hub/tap/view?factoryCode="+ qbbh;
		String urlpath = "http://58.17.247.66:8080/gashub/hub/tap/view?factoryCode="+ qbbh;
		progressdialog = ProgressDialog.show(WLWBugHandleActivity.this,
				"请等待", "正在发送请求命令...");
		new Thread(new DowNetString(urlpath)).start();
		new Thread(new timeOutThread()).start();

	

	}
	 Handler NetHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				if(msg.what==1){
					Toast.makeText(WLWBugHandleActivity.this, "此表号不存在", Toast.LENGTH_SHORT).show();
				
				}
				
				
				
			}
	    	
	    };
	
	 
	    class timeOutThread implements Runnable {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					Thread.sleep(30000);
					if (netinfo.equals("")) {
						progressdialog.dismiss();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	class DowNetString implements Runnable {

		String urlpath;

		public DowNetString(String str) {
			urlpath = str;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			Log.e("test", urlpath);
			String netString = "";
			try {
				netString = HtmlService.gethtml(urlpath);
				
				netinfo=netString;
				progressdialog.dismiss();
				if (netString.equals("")) {
					Log.e("test", "请求失败");
				} else {
					Log.e("test", "请求成功");
					Log.e("test", netString);
					
						JSONObject dataJson = new JSONObject(netString);
						if(dataJson.isNull("attrs")){
							 Message msg=new Message();
							 msg.what=1;
				    	     NetHandler.sendMessage(msg);
						}else{
							JSONObject attrs = dataJson.getJSONObject("attrs");
							
							String gasmeter = attrs.getString("gasmeter");
							JSONObject dataJson1 = new JSONObject(gasmeter);
							String accountId = dataJson1.getString("id");
							Log.e("test", "id=" + accountId);
							intent.putExtra("qbbh", accountId);
							intent.setClass(WLWBugHandleActivity.this,MainWLWMeterActivity.class);
							WLWBugHandleActivity.this.startActivity(intent);
						}
						
					

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	
	
	
	

}
