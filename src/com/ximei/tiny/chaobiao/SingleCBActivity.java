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
	TypeConvert typeConvert;
	JinKaAgreement jk;
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
		jk = new JinKaAgreement();
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
				    String addr=StrID;
					if(addr!=null && !addr.equals(""))
					{
						//CRCmsg = ("09" + addr + "9A" + "06" + hexTimemsg);
						//ordermsg = headmsg + CRCmsg+ crc.CRC_CCITT(1, CRCmsg).toUpperCase()+ overmsg + "5B5B/";
						       // 长度      起始符                                                                            控制字0                            	控制字1 								控制字2								控制字3		源节点     表号	数据域
						CRCmsg = "1D"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+addr+"030106030702190301130901";
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
