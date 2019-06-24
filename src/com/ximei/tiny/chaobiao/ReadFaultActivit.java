package com.ximei.tiny.chaobiao;

import java.util.ArrayList;
import java.util.List;

import com.tiny.gasxm.R;
import com.ximei.tiny.backinfoview.BackSingleCBActivity;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.GetmsgID;
import com.ximei.tiny.tools.JinKaAgreement;
import com.ximei.tiny.tools.TypeConvert;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReadFaultActivit extends Activity{
	
	private Spinner sp;
	private List<String> slist = new ArrayList<String>();
	private EditText bh,starnum,count;
	private Button bt;
	private String event="11",qbbh,starn,countn,ordermsg;
	Intent intent;
	JinKaAgreement jk;
	
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.singlecb3);
		intent = getIntent();
		jk = new JinKaAgreement();
		this.bt =(Button) findViewById(R.id.faultbt);
		this.bh = (EditText) findViewById(R.id.faultbh);
		this.starnum = (EditText) findViewById(R.id.faultstarnum);
		this.count = (EditText) findViewById(R.id.faultcount);
		this.sp = (Spinner) findViewById(R.id.spinner1);
		slist.add("强磁干扰");
		slist.add("控制阀故障");
		slist.add("传感器故障");
		slist.add("电池上电");
		slist.add("电池下电");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinnerth,slist);
		//this.sp.color
		this.sp.setAdapter(adapter);
		this.sp.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				TextView tv = (TextView)view;
				tv.setTextColor(Color.BLACK);
				switch(slist.get(position)) {
					case "强磁干扰":event="11";break;
					case "控制阀故障":event="22";break;
					case "传感器故障":event="23";break;
					case "电池上电":event="31";break;
					case "电池下电":event="32";break;
					default:break;
				}
			}
		});
		bt.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GetmsgID localGetmsgID = new GetmsgID();
				qbbh = bh.getEditableText().toString();
				qbbh = localGetmsgID.CheckMeterID(qbbh);
				if(qbbh == null) {
					Toast.makeText(ReadFaultActivit.this, "表号输入有误", 0).show();
					return;
				}
				starn = starnum.getEditableText().toString();
				if(starn.equals("") || starn.equals(null)) {
					Toast.makeText(ReadFaultActivit.this, "开始序号输入有误", 0).show();
					return;
				}
				countn = count.getEditableText().toString();
				if(countn.equals("") || countn.equals(null) || Integer.parseInt(countn)>255) {
					Toast.makeText(ReadFaultActivit.this, "查询条数输入有误输入有误", 0).show();
					return;
				}
				starn = TypeConvert.intToHex(Integer.parseInt(starn));
				countn = TypeConvert.intToHex(Integer.parseInt(countn));
			       		  	  // 长度      起始符                                                                            控制字0                            	控制字1 								控制字2								控制字3		源节点     表号	数据域
				String CRCmsg = "17"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+qbbh+"03040C"+starn+countn+event;
				try {
					//加上CRC效验码
					ordermsg = CRCmsg+ jk.getCrcjy(CRCmsg);
					//加上(异或)校验和
					ordermsg = ordermsg+TypeConvert.yiHuo(ordermsg);
					//数据域加密
					ordermsg = jk.decrypt(ordermsg);
					//获取加密后的异或校验
					ordermsg = ordermsg.substring(0,ordermsg.length()-2)+TypeConvert.yiHuo(ordermsg);
					//Log.e("test", ordermsg);
					intent.putExtra("order", ordermsg);
					intent.setClass(ReadFaultActivit.this, BtXiMeiService.class);
					startService(ReadFaultActivit.this.intent);
		
					Intent localIntent = new Intent();
					localIntent.putExtra("Comm", "01");
					localIntent.setClass(ReadFaultActivit.this,BackSingleCBActivity.class);
					ReadFaultActivit.this.startActivity(localIntent);
				}catch (Exception e) {
					Log.e("test", e.toString());
				}
			}
			
		});
	}

}
