package com.ximei.tiny.chaobiao;

import com.tiny.gasxm.R;
import com.ximei.tiny.backinfoview.BackSingleCBActivity;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.JinKaAgreement;
import com.ximei.tiny.tools.TypeConvert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class ReadHistoryActivity extends Activity{
	
	private EditText qbbh,singlestartnum,singlecount;
	private Button querybutton;
	private RadioButton rbeveryday, rbjiesuanday;
	Intent intent;
	String bh,history,startnum,count,ordermsg,CRCmsg;
	JinKaAgreement jk;
	
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.singlecb2);
		jk = new JinKaAgreement();
		// 得到相应控件的句柄
		this.qbbh = ((EditText) findViewById(R.id.historyqbbh));
		this.singlestartnum = ((EditText) findViewById(R.id.singlestartnum));
		this.singlecount = ((EditText) findViewById(R.id.singlecount));
		this.querybutton = ((Button) findViewById(R.id.historybt));
		this.rbeveryday = ((RadioButton) findViewById(R.id.rbeveryday));
		this.rbjiesuanday = ((RadioButton) findViewById(R.id.rbjiesuanday));
		this.intent = getIntent();
		
		
		this.querybutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				bh = qbbh.getEditableText().toString();
				if(bh.equals("") || bh.equals(null) || bh.length()!=14) {
					Toast.makeText(ReadHistoryActivity.this, "表号输入有误", 0).show();
					return;
				}
				startnum = singlestartnum.getEditableText().toString();
				if(startnum.equals("") || startnum.equals(null)) {
					Toast.makeText(ReadHistoryActivity.this, "开始序号输入有误", 0).show();
					return;
				}
				count = singlecount.getEditableText().toString();
				if(count.equals("") || count.equals(null) || Integer.parseInt(count)>255) {
					Toast.makeText(ReadHistoryActivity.this, "查询条数输入有误输入有误", 0).show();
					return;
				}
				startnum = TypeConvert.intToHex(Integer.parseInt(startnum));
				count = TypeConvert.intToHex(Integer.parseInt(count));
				if(rbeveryday.isChecked()) {
					history = "08";
				}
				if(rbjiesuanday.isChecked()) {
					history = "07";
				}
				Log.e("test", "起始序号："+startnum+"  条数："+count+"   查询方式："+history);
				
				       // 长度      起始符                                                                            控制字0                            	控制字1 								控制字2								控制字3		源节点     表号	数据域
				CRCmsg = "16"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+bh+"0303"+history+startnum+count;
				//加上CRC效验码
				ordermsg = CRCmsg+ jk.getCrcjy(CRCmsg);
				//加上(异或)校验和
				ordermsg = ordermsg+TypeConvert.yiHuo(ordermsg);
				//数据域加密
				ordermsg = jk.decrypt(ordermsg);
				//获取加密后的异或校验
				ordermsg = ordermsg.substring(0,ordermsg.length()-2)+TypeConvert.yiHuo(ordermsg);
				intent.putExtra("order", ordermsg);
				intent.setClass(ReadHistoryActivity.this, BtXiMeiService.class);
				startService(ReadHistoryActivity.this.intent);
	
				Intent localIntent = new Intent();
				localIntent.putExtra("Comm", "00");
				localIntent.setClass(ReadHistoryActivity.this,BackSingleCBActivity.class);
				ReadHistoryActivity.this.startActivity(localIntent);
			}
		});
	}
	
	

}
