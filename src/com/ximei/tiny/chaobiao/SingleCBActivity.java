package com.ximei.tiny.chaobiao;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.tiny.gasxm.R;
import com.ximei.tiny.backinfoview.BackSingleCBActivity;
import com.ximei.tiny.backinfoview.BackSingleInFoActivity;
import com.ximei.tiny.backinfoview.BackSingleCBActivity.MyThread;
import com.ximei.tiny.database.UserBDhelper;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.GetmsgID;
import com.ximei.tiny.tools.JinKaAgreement;
import com.ximei.tiny.tools.QydmtoHex;
import com.ximei.tiny.tools.ToHexStr;
import com.ximei.tiny.tools.TypeConvert;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * 单个抄表Activity
 * 一个气表表号输入框
 * 
 * 
 * 
 */
public class SingleCBActivity extends Activity {
	String CRCmsg;
	final int MAXmeternum = 16777215;
	final int MINmeternum = 0;
	String MsgID;
	String Msgvalue;
	String Ordermsg;
	String StrID;
	String Timemsg;
	UserBDhelper bdhelper;
	public CRC crc;
	private EditText editvalue,editvalue1;
	private TextView dccb;
	String headmsg;
	String hexTimemsg,cbfs;
	private int intID;
	Intent intent;
	String ordermsg;
	String overmsg, biaotype;
	private Button querybutton;
	QydmtoHex qydmtohex;
	private RadioButton oldmeter, newmeter;
	private RadioGroup metertype;
	TypeConvert typeConvert;
	int count=0;
	JinKaAgreement jk;
	String cbflag = "";
	MyReceiver myreceiver;
	Thread td;
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.singlecb);
		this.intent = getIntent();
		cbfs = this.intent.getStringExtra("function");
		// 得到相应控件的句柄
		this.editvalue = ((EditText) findViewById(R.id.singleqbbh));
		this.editvalue1 = ((EditText) findViewById(R.id.singlecbcount));
		this.dccb = ((TextView) findViewById(R.id.dccb));
		if(cbfs.equals("singlecb")) {
			editvalue1.setVisibility(View.GONE);
		}
		this.querybutton = ((Button) findViewById(R.id.singlequery));
		
		this.qydmtohex = new QydmtoHex();
		this.overmsg = this.intent.getStringExtra("overmsg");
		
		Log.e("test", overmsg);
		jk = new JinKaAgreement();
		// 新增单选框
		oldmeter = (RadioButton) findViewById(R.id.oldmeter);
		newmeter = (RadioButton) findViewById(R.id.newmeter);
		metertype = (RadioGroup) findViewById(R.id.metertype);
		biaotype = "mrmeter";
		biaotype = "newmeter";
		if(cbfs.equals("singlecb1")) {
			this.dccb.setText("多次抄表");
			metertype.setVisibility(View.VISIBLE);
			oldmeter.setText("唤醒模式");
			newmeter.setText("不唤醒模式");
		}
		metertype.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (oldmeter.getId() == checkedId) {
					biaotype = "oldmeter";

				} else if (newmeter.getId() == checkedId) {
					biaotype = "newmeter";
				}

			}
		});
		this.myreceiver = new MyReceiver();
		// 注册普通抄表广播
		IntentFilter localIntentFilter = new IntentFilter();
		localIntentFilter.addAction("android.intent.action.putongcb_yes");
		registerReceiver(myreceiver, localIntentFilter);
		
		// 为查询按钮注册监听器
		this.querybutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {

				Intent intentBusy = new Intent("android.intent.action.busy");
				intentBusy.putExtra("State", "busy");
				sendBroadcast(intentBusy);	
				
				GetmsgID localGetmsgID = new GetmsgID();
				ToHexStr localToHexStr = new ToHexStr();
				crc = new CRC();
				headmsg = "5A5A00FE01";
				SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ssmmHHddMMyy");
				Timemsg = localSimpleDateFormat.format(new Date());
				hexTimemsg = localToHexStr.toHexStr(SingleCBActivity.this.Timemsg);
				StrID = editvalue.getEditableText().toString();
				    String addr=localGetmsgID.CheckMeterID(StrID);
				    
					if(editvalue1.getEditableText().toString() == null || editvalue1.getEditableText().toString().equals("")) {
						count =1;
					}else {
						count = Integer.parseInt(editvalue1.getEditableText().toString());
					}
					if(count>500) {
						Toast.makeText(SingleCBActivity.this, "抄表次数过多", 0).show();
						return;
					}
					if(addr!=null && !addr.equals(""))
					{
						//CRCmsg = ("09" + addr + "9A" + "06" + hexTimemsg);
						//ordermsg = headmsg + CRCmsg+ crc.CRC_CCITT(1, CRCmsg).toUpperCase()+ overmsg + "5B5B/";
						SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
						       // 长度      起始符                                                                            控制字0                            	控制字1 								控制字2								控制字3		源节点     表号	数据域
						CRCmsg = "1D"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+addr+"030106030702"+df.format(new Date()).toString();
						//加上CRC效验码
						ordermsg = CRCmsg+ jk.getCrcjy(CRCmsg);
						//加上(异或)校验和
						ordermsg = ordermsg+TypeConvert.yiHuo(ordermsg);
						//数据域加密
						ordermsg = jk.decrypt(ordermsg);
						//获取加密后的异或校验
						ordermsg = ordermsg.substring(0,ordermsg.length()-2)+TypeConvert.yiHuo(ordermsg);
						intent.putExtra("order", ordermsg);
						if(StrID.length()==14) biaotype = "newmeter";
						intent.putExtra("metertype", biaotype);
						intent.setClass(SingleCBActivity.this, BtXiMeiService.class);
						
						Intent localIntent = new Intent();
						if(cbfs.equals("singlecb")) {
							localIntent.putExtra("Comm", "01");
							localIntent.setClass(SingleCBActivity.this,BackSingleCBActivity.class);
							SingleCBActivity.this.startActivity(localIntent);
							startService(SingleCBActivity.this.intent);
						}else if(cbfs.equals("singlecb1")){
							localIntent.putExtra("Comm", "00");
							localIntent.putExtra("count",count);
							localIntent.setClass(SingleCBActivity.this,BackSingleCBActivity.class);
							SingleCBActivity.this.startActivity(localIntent);
							
							if(oldmeter.isChecked()) {
								String hxsj = TypeConvert.intToHex(count*5+13);
								while(hxsj.length()!=4) {
									hxsj = "0"+hxsj;
								}
								hxsj = hxsj.substring(2, 4)+hxsj.substring(0,2);
								String hxmsg = "17"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+"FFFFFFFFFFFFFF"+"0304E002"+hxsj;
								//加上CRC效验码
								hxmsg = hxmsg+ jk.getCrcjy(hxmsg);
								//加上(异或)校验和
								hxmsg = hxmsg+TypeConvert.yiHuo(hxmsg);
								//数据域加密
								hxmsg = jk.decrypt(hxmsg);
								//获取加密后的异或校验
								hxmsg = hxmsg.substring(0,hxmsg.length()-2)+TypeConvert.yiHuo(hxmsg);
								Intent intenth = new Intent();
								intenth.putExtra("order", hxmsg);
								intenth.setClass(SingleCBActivity.this, BtXiMeiService.class);
								startService(intenth);
							}
							td = new Thread(new tr());
							td.start();
						}
					}
					else
					{
						Toast.makeText(SingleCBActivity.this, "表号输入有误", 0).show();
					}
					return;
			}
		});
	}
	public class tr extends Thread{

		@Override
		public void run() {
			
			try {
				if(oldmeter.isChecked()) {
					Thread.sleep(13000);
				}
				for(;count>0;count--) {
					cbflag="";
					intent.putExtra("count", count);
					startService(SingleCBActivity.this.intent);
					
						while(cbflag!="ok") {
							Thread.sleep(1000);
							if(cbflag.equals("stop")) {
								break;
							}
						}
						if(cbflag.equals("stop")) {
							break;
						}
					}
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void onDestroy() {
		unregisterReceiver(myreceiver);
		super.onDestroy();
	};
	
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO 自动生成的方法存根
			if (intent.getAction().equals("android.intent.action.putongcb_yes")) {
				cbflag="ok";
			}
			if(intent.getStringExtra("flag").equals("stop") && cbfs.equals("singlecb1")) {
				cbflag="stop";
				String hxmsg = "17"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+"FFFFFFFFFFFFFF"+"0304E002"+"0000";
				//加上CRC效验码
				hxmsg = hxmsg+ jk.getCrcjy(hxmsg);
				//加上(异或)校验和
				hxmsg = hxmsg+TypeConvert.yiHuo(hxmsg);
				//数据域加密
				hxmsg = jk.decrypt(hxmsg);
				//获取加密后的异或校验
				hxmsg = hxmsg.substring(0,hxmsg.length()-2)+TypeConvert.yiHuo(hxmsg);
				Intent intenth1 = new Intent();
				intenth1.putExtra("order", hxmsg);
				intenth1.setClass(SingleCBActivity.this, BtXiMeiService.class);
				startService(intenth1);
				Log.e("test", "发送关闭唤醒");
			}
		}
		
	}
}
