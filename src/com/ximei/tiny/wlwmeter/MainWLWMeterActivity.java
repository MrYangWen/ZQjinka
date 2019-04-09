package com.ximei.tiny.wlwmeter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.tiny.gasxm.R;

public class MainWLWMeterActivity extends Activity {

	TextView hint, backmsgtv;
	String qbbh, urlpath, netString, netinfo;
	Intent intent;
	private static final String[] wlwmeterhint = { "开阀控制", "关阀控制", "查询状态","气表充值", "单表实时抄表" };
	private static final int[] imageid = { R.drawable.qxqg, R.drawable.qzgf,R.drawable.cxzt, R.drawable.qbcz, R.drawable.cb };
	private View view;
	private AlertDialog selfdialog;
	private ProgressDialog progressdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.xmwlw);
		// 得到GridView控件
		GridView localGridView = (GridView) findViewById(R.id.gridview);
		hint = ((TextView) findViewById(R.id.cbfshint));
		backmsgtv = ((TextView) findViewById(R.id.backmsg));
		hint.setText("物联网表");
		intent = getIntent();
		netinfo = "";

		qbbh = intent.getStringExtra("qbbh");
		Log.e("test", qbbh);
		// initview();

		// 用list<map>存放所用功能菜单作为数据源
		ArrayList<HashMap<String, Object>> localArrayList = new ArrayList<HashMap<String, Object>>();

		for (int i = 0; i < wlwmeterhint.length; i++) {

			HashMap<String, Object> localHashMap = new HashMap<String, Object>();
			localHashMap.put("ItemImage", imageid[i]);
			localHashMap.put("ItemText", wlwmeterhint[i]);
			localArrayList.add(localHashMap);

		}

		// 设置适配器数据源
		localGridView.setAdapter(new SimpleAdapter(this, localArrayList,
				R.layout.gridview_meun,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText }));
		// 设置监听器
		localGridView.setOnItemClickListener(new ItemClickListener());

	}

	public void initview() {
		// 创建view从当前activity获取loginactivity
		LayoutInflater inflater = (LayoutInflater) getApplicationContext()
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.recharge, null);

		final EditText username = (EditText) view
				.findViewById(R.id.txt_username);
		final EditText password = (EditText) view
				.findViewById(R.id.txt_password);
		username.setText("XXXX");
		password.setText("XXXX"); // 为了测试方便所以在这里初始化弹出框是填上账号密码
		AlertDialog.Builder ad = new AlertDialog.Builder(
				MainWLWMeterActivity.this);
		ad.setView(view);
		ad.setTitle("账号登陆");
		selfdialog = ad.create();

		selfdialog.setButton("登陆", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 获取输入框的用户名密码

				String usernamestr = username.getText().toString();
				String passwordstr = password.getText().toString();
				// ProgressDialog progressdialog
				// =ProgressDialog.show(MainWLWMeterActivity.this, "请等待...",
				// "正在为您登陆...");
				// refreshHandler.sleep(100);
				// dialog.cancel();
			}
		});
		selfdialog.setButton2("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				selfdialog.cancel();
			}
		});
		selfdialog.show();
	}

	// 设置点击表册名的监听事件
	class ItemClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			String str = ((HashMap) arg0.getItemAtPosition(arg2)).get(
					"ItemText").toString();

			if (str.equals("开阀控制")) {

				// initview();
				progressdialog = ProgressDialog.show(MainWLWMeterActivity.this,
						"开阀", "正在请求开阀请等待...");
				urlpath = "http://58.17.247.66:8080/gashub/hub/tap/2051/"
						+ qbbh;

			}
			if (str.equals("关阀控制")) {
				progressdialog = ProgressDialog.show(MainWLWMeterActivity.this,
						"关阀", "正在请求关阀请等待...");

				urlpath = "http://58.17.247.66:8080/gashub/hub/tap/2052/"
						+ qbbh;

			}
			if (str.equals("查询状态")) {
				progressdialog = ProgressDialog.show(MainWLWMeterActivity.this,
						"查询状态", "正在查询关阀状态请等待...");
				urlpath = "http://58.17.247.66:8080/gashub/hub/tap/2053/"
						+ qbbh;

			}
			if (str.equals("气表充值")) {

				// urlpath=" http://192.168.1.115:8080/gashub/api/imeter/pay?metercode="+qbbh+"&payDate=2015-09-30&money=300.00";

			}
			if (str.equals("单表实时抄表")) {
				progressdialog = ProgressDialog.show(MainWLWMeterActivity.this,
						"实时抄表", "正在实时抄表请等待...");
				urlpath = "http://58.17.247.66:8080/gashub/hub/readmeter/2042/"
						+ qbbh;
				

			}

			new Thread(new DowNetString(str)).start();
			new Thread(new timeOutThread()).start();

		}

	}

	class DowNetString implements Runnable {

		String CtrlOrder;

		public DowNetString(String str) {
			CtrlOrder = str;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			try {

				netString = HtmlService.gethtml(urlpath);
				netinfo = netString;
				Log.e("test", netString);
				Message msg = new Message();
				if (CtrlOrder.equals("开阀控制")) {
					msg.what = 1;
					NetHandler.sendMessage(msg);
				}
				if (CtrlOrder.equals("关阀控制")) {
					msg.what = 2;
					NetHandler.sendMessage(msg);
				}
				if (CtrlOrder.equals("查询状态")) {
					msg.what = 3;
					NetHandler.sendMessage(msg);
				}
				if (CtrlOrder.equals("气表充值")) {
					msg.what = 4;
					NetHandler.sendMessage(msg);
				}
				if (CtrlOrder.equals("单表实时抄表")) {
					msg.what = 5;
					NetHandler.sendMessage(msg);
				}

				netinfo = "";

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
				if (netinfo.equals("")) {
					progressdialog.dismiss();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	Handler NetHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			if (msg.what == 1) {
				Log.e("test", "开阀返回");
				try {
					JSONObject dataJson = new JSONObject(netString);
					String statusCode = dataJson.getString("statusCode");
					String backmsg = dataJson.getString("message");

					// JSONObject attrs=dataJson.getJSONObject("attrs");
					// String info=attrs.getString("cardinfo");
					progressdialog.dismiss();
					backmsgtv.setText(backmsg);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (msg.what == 2) {
				Log.e("test", "关阀返回");
				try {
					JSONObject dataJson = new JSONObject(netString);
					String statusCode = dataJson.getString("statusCode");
					String backmsg = dataJson.getString("message");

					// JSONObject attrs=dataJson.getJSONObject("attrs");
					// String info=attrs.getString("cardinfo");
					progressdialog.dismiss();
					backmsgtv.setText(backmsg);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (msg.what == 3) {
				try {
					JSONObject dataJson = new JSONObject(netString);
					String backmsg = dataJson.getString("message");
					progressdialog.dismiss();
					backmsgtv.setText(backmsg);

				} catch (Exception e) {

					e.printStackTrace();
				}

			}
			if (msg.what == 4) {
				try {
					// JSONObject dataJson=new JSONObject(nethtml);
					// JSONObject attrs=dataJson.getJSONObject("attrs");
					// String info=attrs.getString("cardinfo");
					hint.setText(netString);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (msg.what == 5) {
				try {
					JSONObject dataJson = new JSONObject(netString);
					String backmsg = dataJson.getString("message");
					progressdialog.dismiss();
					backmsgtv.setText(backmsg);

				} catch (Exception e) {

					e.printStackTrace();
				}

			}

		}

	};

}
