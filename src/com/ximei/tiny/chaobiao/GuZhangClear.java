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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class GuZhangClear extends Activity{
	
	private EditText qbbh;
	private Button querybutton;
	private CheckBox qcgr,kzf,cgq;//强磁干扰，控制阀故障，传感器故障
	Intent intent;
	String bh,ordermsg,CRCmsg,ck1,ck2,ck3;
	JinKaAgreement jk;
	
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.singlecb4);
		jk = new JinKaAgreement();
		// 得到相应控件的句柄
		this.qbbh = ((EditText) findViewById(R.id.gzqcbh));
		this.querybutton = ((Button) findViewById(R.id.gzqcbt));
		/*this.qcgr = (CheckBox) findViewById(R.id.checkBox1);
		this.kzf = (CheckBox) findViewById(R.id.CheckBox01);
		this.cgq = (CheckBox) findViewById(R.id.CheckBox02);*/
		this.intent = getIntent();
		querybutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GetmsgID localGetmsgID = new GetmsgID();
				// TODO Auto-generated method stub
				bh = qbbh.getEditableText().toString();
				bh = localGetmsgID.CheckMeterID(bh);
				if(bh == null) {
					Toast.makeText(GuZhangClear.this, "表号输入有误", 0).show();
					return;
				}
				/*if(qcgr.isChecked()) {
					ck1="01";
				}else {
					ck1="00";
				}
				if(kzf.isChecked()) {
					ck2="01";
				}else {
					ck2="00";
				}
				if(cgq.isChecked()) {
					ck3="01";
				}else {
					ck3="00";
				}*/
				CRCmsg = "15"+"12"+TypeConvert.strTohexStr("00100000")+TypeConvert.strTohexStr("10000000")+TypeConvert.strTohexStr("01100111")+TypeConvert.strTohexStr("00000010")+"0000"+bh+"0305E005";
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
				intent.setClass(GuZhangClear.this, BtXiMeiService.class);
				startService(GuZhangClear.this.intent);
	
				Intent localIntent = new Intent();
				localIntent.putExtra("Comm", "01");
				localIntent.setClass(GuZhangClear.this,BackSingleCBActivity.class);
				GuZhangClear.this.startActivity(localIntent);
			}
		});
	}
	
	

}
