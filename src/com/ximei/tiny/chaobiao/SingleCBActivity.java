package com.ximei.tiny.chaobiao;

import android.app.Activity;
import android.content.Intent;
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

import com.tiny.gasxm.R;
import com.ximei.tiny.backinfoview.BackSingleCBActivity;
import com.ximei.tiny.database.UserBDhelper;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.GetmsgID;
import com.ximei.tiny.tools.QydmtoHex;
import com.ximei.tiny.tools.ToHexStr;
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
	private EditText editvalue;
	String headmsg;
	String hexTimemsg;
	private int intID;
	Intent intent;
	String ordermsg;
	String overmsg, biaotype;
	private Button querybutton;
	QydmtoHex qydmtohex;
	private RadioButton oldmeter, newmeter;
	private RadioGroup metertype;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.singlecb);
		// 得到相应控件的句柄
		this.editvalue = ((EditText) findViewById(R.id.singleqbbh));
		this.querybutton = ((Button) findViewById(R.id.singlequery));
		this.intent = getIntent();
		this.qydmtohex = new QydmtoHex();
		this.overmsg = this.intent.getStringExtra("overmsg");
		Log.e("test", overmsg);

		// 新增单选框
		oldmeter = (RadioButton) findViewById(R.id.oldmeter);
		newmeter = (RadioButton) findViewById(R.id.newmeter);
		metertype = (RadioGroup) findViewById(R.id.metertype);
		biaotype = "mrmeter";
		biaotype = "newmeter";
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

		// 未查询按钮注册监听器
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
//					SingleCBActivity.this.intID = Integer.parseInt(SingleCBActivity.this.StrID);
//					SingleCBActivity.this.MsgID = Integer.toHexString(SingleCBActivity.this.intID);
//					// 判断气表表号的范围是不是在0-16777215之间
//					if ((SingleCBActivity.this.intID > 16777215)|| (SingleCBActivity.this.intID < 0)|| (SingleCBActivity.this.MsgID.length() > 6))
//						Toast.makeText(SingleCBActivity.this, "表号输入错误请重新输入", 0).show();
//					if ((SingleCBActivity.this.MsgID.length() < 7)&& (SingleCBActivity.this.MsgID.length() > 0)) 
					//String addr=localGetmsgID.GetMeterAddr(StrID);
				    String addr=localGetmsgID.CheckMeterID(StrID);
					if(addr!=null)
					{
						// 得到表号转化为16进制
						//Msgvalue = localGetmsgID.getMsgID(MsgID).toUpperCase();
						CRCmsg = ("09" + addr + "9A" + "06" + hexTimemsg);
						ordermsg = headmsg + CRCmsg+ crc.CRC_CCITT(1, CRCmsg).toUpperCase()+ overmsg + "5B5B/";
						intent.putExtra("order", ordermsg);
						if(StrID.length()==14) biaotype = "newmeter";
						intent.putExtra("metertype", biaotype);
						intent.setClass(SingleCBActivity.this, BtXiMeiService.class);
						startService(SingleCBActivity.this.intent);

						Intent localIntent = new Intent();
						localIntent.putExtra("Comm", "00");
						localIntent.setClass(SingleCBActivity.this,BackSingleCBActivity.class);
						SingleCBActivity.this.startActivity(localIntent);
					}
					else
					{
						Toast.makeText(SingleCBActivity.this, "表号输入有误", 0).show();
					}
					return;
			}
		});
	}
}
