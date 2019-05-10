package com.ximei.tiny.chaobiao;

import com.tiny.gasxm.R;
import com.ximei.tiny.backinfoview.BackSingleCBActivity;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.GetmsgID;
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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class ReadHistoryActivity extends Activity{
	
	private EditText qbbh,singlestartnum,singlecount;
	private Button querybutton;
	private RadioButton rbeveryday, rbjiesuanday;
	private TextView tvbt;
	private RadioGroup rg;
	Intent intent;
	String bh,history,startnum,count,ordermsg,CRCmsg,bugtype;
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
		this.tvbt = ((TextView) findViewById(R.id.rdbt));
		this.singlestartnum = ((EditText) findViewById(R.id.singlestartnum));
		this.singlecount = ((EditText) findViewById(R.id.singlecount));
		this.querybutton = ((Button) findViewById(R.id.historybt));
		this.rbeveryday = ((RadioButton) findViewById(R.id.rbeveryday));
		this.rbjiesuanday = ((RadioButton) findViewById(R.id.rbjiesuanday));
		this.rg = ((RadioGroup) findViewById(R.id.metertype1));
		this.intent = getIntent();
		bugtype = this.intent.getStringExtra("bugtype");
		if(bugtype.equals("sbdz")) {
			this.tvbt.setText("设表地址");
			this.rbeveryday.setText("设置");
			this.rbjiesuanday.setText("修改");
			this.singlecount.setVisibility(View.GONE);
			this.singlestartnum.setHint("请输入新表地址");
			this.querybutton.setHint("设置");
			singlestartnum.setVisibility(View.GONE);
			rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO 自动生成的方法存根
					if(checkedId == rbeveryday.getId()) {
						singlestartnum.setVisibility(View.GONE);
					}
					if(checkedId == rbjiesuanday.getId()) {
						singlestartnum.setVisibility(View.VISIBLE);
					}
				}
			});
		}
		if(bugtype.equals("ccsz")) {
			this.tvbt.setText("出厂设置");
			this.rbeveryday.setText("出厂设置");
			this.rbjiesuanday.setText("机电同步");
			this.singlecount.setVisibility(View.GONE);
			this.singlestartnum.setHint("请输入气表读数");
			this.singlestartnum.setInputType(8194);
			this.querybutton.setHint("设置");
		}
		if(bugtype.equals("bhx"))  {
			this.tvbt.setText("表唤醒");
			this.singlecount.setVisibility(View.GONE);
			this.singlestartnum.setHint("请输入唤醒时间(秒)");
			this.querybutton.setHint("唤醒");
			//this.rg.setVisibility(View.GONE);
			this.rbeveryday.setText("指定唤醒");
			this.rbjiesuanday.setText("广播唤醒");
			rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO 自动生成的方法存根
					if(checkedId == rbeveryday.getId()) {
						qbbh.setVisibility(View.VISIBLE);
					}
					if(checkedId == rbjiesuanday.getId()) {
						qbbh.setVisibility(View.GONE);
					}
				}
			});
		}
		if(bugtype.equals("hhcc")) {
			this.tvbt.setText("恢复出厂");
			this.rbeveryday.setText("清除表号");
			this.rbjiesuanday.setText("保留表号");
			this.singlecount.setVisibility(View.GONE);
			this.singlestartnum.setVisibility(View.GONE);
			this.querybutton.setHint("设置");
		}
		this.querybutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				GetmsgID localGetmsgID = new GetmsgID();
				bh = qbbh.getEditableText().toString();
				if(rbjiesuanday.isChecked() && bugtype.equals("bhx")) {
					bh = "FFFFFFFFFFFFFF";
				}else {
					bh = localGetmsgID.CheckMeterID(bh);
					if(bh == null) {
						Toast.makeText(ReadHistoryActivity.this, "表号输入有误", 0).show();
						return;
					}
				}
				//历史记录
				if(bugtype.equals("history")) {
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
					       // 长度      起始符                                                                            控制字0                            	控制字1 								控制字2								控制字3		源节点     表号	数据域
					CRCmsg = "16"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+bh+"0303"+history+startnum+count;
				}else if(bugtype.equals("sbdz")) {//设置表地址
					if(rbeveryday.isChecked()) {
						history = "00";
				   				// 长度      起始符                                                                            控制字0                            	控制字1 								控制字2								控制字3		源节点     表号	数据域
						CRCmsg = "1D"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+"FFFFFFFFFFFFFF"+"030AE000"+history+bh;
					}
					if(rbjiesuanday.isChecked()) {
						startnum = singlestartnum.getEditableText().toString();
						startnum = localGetmsgID.CheckMeterID(startnum);
						if(startnum == null) {
							Toast.makeText(ReadHistoryActivity.this, "新表号输入有误", 0).show();
							return;
						}
						history = "01";
								// 长度      起始符                                                                            控制字0                            	控制字1 								控制字2								控制字3		源节点     表号	数据域
						CRCmsg = "1D"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+bh+"030AE000"+history+startnum;
					}
				    
				}else if(bugtype.equals("ccsz")) {//出厂设置
					startnum = singlestartnum.getEditableText().toString();
					if(startnum == null || startnum.equals("")) {
						Toast.makeText(ReadHistoryActivity.this, "请输入气表读数", 0).show();
						return;
					}
					float num = Float.parseFloat(startnum)*10;
					int number = (int) num;
					if(number>999999) {
						Toast.makeText(ReadHistoryActivity.this, "气表读数输入过大", 0).show();
						return;
					}
					String qbds = TypeConvert.intToHex(number);
					/*if(qbds.length()>6) {
						Toast.makeText(ReadHistoryActivity.this, "气表读数输入过大", 0).show();
						return;
					}*/
					while(qbds.length()!=8) {
						qbds = "0"+qbds;
					}
					if(rbeveryday.isChecked()) {
						history = "00";
					}
					if(rbjiesuanday.isChecked()) {
						history = "01";
					}
			   				// 长度      起始符                                                                            控制字0                            	控制字1 								控制字2								控制字3		源节点     表号	数据域
					CRCmsg = "1D"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+bh+"0307E001"+history+qbds;
				}else if(bugtype.equals("bhx")) {//唤醒
					startnum = singlestartnum.getEditableText().toString();
					if(startnum == null || startnum.equals("")) {
						Toast.makeText(ReadHistoryActivity.this, "请输入唤醒时间", 0).show();
						return;
					}
					int num = Integer.parseInt(startnum);
					String hxsj = TypeConvert.intToHex(num);
					if(hxsj.length()>4) {
						Toast.makeText(ReadHistoryActivity.this, "唤醒时间输入过长", 0).show();
						return;
					}
					while(hxsj.length()!=4) {
						hxsj = "0"+hxsj;
					}
					hxsj = hxsj.substring(2, 4)+hxsj.substring(0,2);
					if(rbeveryday.isChecked()) {
						history = "00";
					}
					if(rbjiesuanday.isChecked()) {
						history = "01";
					}
			   				// 长度      起始符                                                                            控制字0                            	控制字1 								控制字2								控制字3		源节点     表号	数据域
					CRCmsg = "17"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+bh+"0304E002"+hxsj;
				}else if(bugtype.equals("hhcc")) {//恢复出厂化
					if(rbeveryday.isChecked()) {
						history = "00";
					}
					if(rbjiesuanday.isChecked()) {
						history = "01";
					}
			   				// 长度      起始符                                                                            控制字0                            	控制字1 								控制字2								控制字3		源节点     表号	数据域
					CRCmsg = "16"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+bh+"0303E004"+history;
				}
				try {
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
					localIntent.putExtra("Comm", "01");
					localIntent.setClass(ReadHistoryActivity.this,BackSingleCBActivity.class);
					ReadHistoryActivity.this.startActivity(localIntent);
				}catch (Exception e) {
					Log.e("test", e.toString());
				}
			}
		});
	}
	
	

}
