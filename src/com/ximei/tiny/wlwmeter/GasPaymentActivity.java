package com.ximei.tiny.wlwmeter;

import org.json.JSONException;
import org.json.JSONObject;

import com.tiny.gasxm.R;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.StrToHex;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GasPaymentActivity extends Activity implements OnClickListener {

	private TextView infohint, nature, payNum, userCode, userName;
	private EditText payment;
	private Button paymentbt;
	Intent intent;
	private String naturemsg, payNummsg, userCodemsg, userNamemsg, paymentmsg,
			payMoney, urlpath, crcmsg, ordermsg, backmsg, ipadd;

	String usercardmsg = "";
	private BroadcastReceiver recardrecevie;
	private ProgressDialog progressdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 取消状态标题栏
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.payment);
		// 初始化界面
		initview();

		recardrecevie = new RwCardRecevie();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction("android.intent.tiny.rwcard");
		registerReceiver(recardrecevie, filter2);

	}

	public class RwCardRecevie extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			backmsg = intent.getStringExtra("backmsg").trim();

			if (backmsg.equals("OK")) {
				Log.e("test", "写卡成功");
				urlpath = "http://" + ipadd
						+ ":8080/gashub/api/imeter/lastpay?userCode="
						+ userCodemsg + "&content=" + usercardmsg
						+ "&meterType=00";
				;
				Log.e("test", urlpath);
				new Thread(new DowNetString("payok")).start();
				

			} else if (backmsg.equals("FAIL")) {
				infohint.setText("写卡失败");
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		try {
			payMoney = payment.getEditableText().toString().trim();
			if (payMoney.equals("")) {

			} else {
//				int payint = Integer.parseInt(payMoney) * 100;
//				payMoney = String.valueOf(payint);
				Log.e("test", payMoney);
				urlpath = "http://" + ipadd
						+ ":8080/gashub/api/imeter/pay?userCode=" + userCodemsg
						+ "&payNum=" + payNummsg + "&payMoney=" + payMoney
						+ "&content=" + usercardmsg + "&meterType=00";
				// Log.e("test", urlpath);
				new Thread(new DowNetString("payrequest")).start();
				new Thread(new TimeOutThread()).start();
				progressdialog =ProgressDialog.show(GasPaymentActivity.this,"正在充值", "正在充值请等待...");
				
				
			}
		} catch (Exception e) {

			infohint.setText("重新输入");
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
			if (CtrlOrder.equals("payrequest")) {
				try {

					String netString = HtmlService.gethtml(urlpath);
					Log.e("test", netString);
					JSONObject dataJson = new JSONObject(netString);
					String statusCode = dataJson.getString("statusCode");
					
					Message message = NetHandler.obtainMessage();
					if (statusCode.equals("200")) {

						String backmsg = dataJson.getString("message");

						JSONObject attrs = dataJson.getJSONObject("attrs");
						String card = attrs.getString("card");
						message.what = 1;
						Bundle b = new Bundle();
						b.putString("cardmsg", card);
						message.setData(b);
						NetHandler.sendMessage(message);
					}else{
						
						message.what = 3;
						NetHandler.sendMessage(message);
						
					}

				} catch (Exception e) {
					Log.e("test", e.toString());

				}

			}

			if (CtrlOrder.equals("payok")) {
				String netString;
				try {
					netString = HtmlService.gethtml(urlpath);
					JSONObject dataJson = new JSONObject(netString);
					String statusCode = dataJson.getString("statusCode");
					String backmsg = dataJson.getString("message");
					Log.e("test", netString);
					if (statusCode.equals("200")) {

						Message message = NetHandler.obtainMessage();
						message.what = 2;
						// Bundle b = new Bundle();
						// b.putString("cardmsg", card);
						// message.setData(b);
						NetHandler.sendMessage(message);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	Handler NetHandler = new Handler() {

		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				Bundle b = msg.getData();
				String cardmsg = b.getString("cardmsg");
				JSONObject dataJson1;
				try {

					dataJson1 = new JSONObject(cardmsg);
					String usercardinfo = dataJson1.getString("content")
							.substring(0, 414);
					String password = dataJson1.getString("password");
					Log.e("test", usercardinfo);
					Log.e("test",
							"usercardinfo"
									+ String.valueOf(usercardinfo.length()));
					Log.e("test", password);

					crcmsg = StrToHex.ToHexString("A3752C" + "A3752C" + 30
							+ usercardinfo);
					ordermsg = "WCWPSW[A3752C" + "A3752C" + 30 + usercardinfo
							+ crcmsg + "]\r";

					Log.e("test", ordermsg);
					intent.putExtra("order", ordermsg);
					intent.setClass(GasPaymentActivity.this,
							BtXiMeiService.class);
					GasPaymentActivity.this.startService(intent);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("test", "充值请求" + e.toString());
					// Log.e("test", "写卡失败");
				}

			}

			if (msg.what == 2) {

				infohint.setText("缴费成功");
				progressdialog.dismiss();
				payment.setText("");
			}
			if (msg.what == 3) {

				infohint.setText("请求返回信息错误");
				progressdialog.dismiss();
				payment.setText("");
			}

		}

	};

	private class TimeOutThread implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(5000);
				if(!infohint.equals("缴费成功")){
					progressdialog.dismiss();
				}
				

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void initview() {
		infohint = (TextView) findViewById(R.id.infohint);
		nature = (TextView) findViewById(R.id.nature);
		payNum = (TextView) findViewById(R.id.payNum);
		userCode = (TextView) findViewById(R.id.userCode);
		userName = (TextView) findViewById(R.id.userName);
		payment = (EditText) findViewById(R.id.paymentinput);
		paymentbt = (Button) findViewById(R.id.paymentbt);
		paymentbt.setOnClickListener(this);
		intent = getIntent();

		paymentmsg = intent.getStringExtra("paymentmsg");
		// Log.e("test", paymentmsg);
		ipadd = intent.getStringExtra("ipadd");
		usercardmsg = intent.getStringExtra("usercardmsg");

		JSONObject dataJson1;
		try {
			dataJson1 = new JSONObject(paymentmsg);
			naturemsg = dataJson1.getString("billingTypeName");
			payNummsg = dataJson1.getString("payNum");
			userCodemsg = dataJson1.getString("userCode");
			userNamemsg = dataJson1.getString("userName");
			nature.setText("计费方式:" + naturemsg);
			payNum.setText("充值次数" + payNummsg);
			userCode.setText("用户编号:" + userCodemsg);
			userName.setText("用户姓名:" + userNamemsg);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("test", e.toString());
		}

	}

}
