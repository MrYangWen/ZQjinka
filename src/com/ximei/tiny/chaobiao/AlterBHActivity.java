package com.ximei.tiny.chaobiao;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tiny.gasxm.R;
import com.ximei.tiny.backinfoview.BackCaiJiInFoActivity;
import com.ximei.tiny.backinfoview.BackSingleCBActivity;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.GetNowPacket;
import com.ximei.tiny.tools.GetmsgID;
import com.ximei.tiny.tools.ToHexStr;

/*
 *  修改activity。
 *  有两个输入框，对应相应的操作
 * 
 * 
 * 
 */
public class AlterBHActivity extends Activity {
	String CRCmsg;
	final int MAXmeternum = 16777215;
	final int MINmeternum = 0;
	CRC crc;
	String headmsg;
	private EditText input1;
	String input1hex;
	String input1msg;
	int input1nub;
	private EditText input2,input3;
	String input2hex;
	String input2msg;
	int input2nub;
	Intent intent;
	String ordermsg,biaotype;
	String overmsg;
	private Button shouhoubt;
	private TextView shouhouhint;
	String shouhoutype;
	 private RadioButton oldmeter,newmeter;
	 private RadioGroup metertype;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消状态标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.shouhou);
		intent = getIntent();
		crc = new CRC();
		// 根据intent得到overmsg，shouhoutype
		overmsg =     intent.getStringExtra("overmsg");
		shouhoutype = intent.getStringExtra("shouhoutype");
		shouhouhint = ((TextView) findViewById(R.id.shouhouhint));
		input1 =      ((EditText) findViewById(R.id.shouhouinput1));
		input2 =      ((EditText) findViewById(R.id.shouhouinput2));
		input3 =      ((EditText) findViewById(R.id.shouhouinput3));
		shouhoubt =   ((Button) findViewById(R.id.shouhoubt));
		
		
		oldmeter = (RadioButton)findViewById(R.id.oldmeter);
		newmeter = (RadioButton)findViewById(R.id.newmeter);
		metertype = (RadioGroup)findViewById(R.id.metertype);
		biaotype="mrmeter";	
		//新增单选框
		metertype.setOnCheckedChangeListener(new OnCheckedChangeListener() {	
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(oldmeter.getId()==checkedId){
					biaotype="oldmeter";
				}else if(newmeter.getId()==checkedId){
					biaotype="newmeter";		
				}
			}
		});
		input3.setVisibility(View.INVISIBLE);
		// 根据shouhoutype得到相应的shouhouhint值
		if (this.shouhoutype.equals("gbdz")) {
			this.shouhouhint.setText("修改表地址");
			this.input1.setHint("原表地址");
			this.input2.setHint("新表地址");
		}
		if (this.shouhoutype.equals("bctj")) {
			this.shouhouhint.setText("表册调价");
			this.input1.setHint("价格/分");
			this.input2.setHint("调价日期/年月日");
		}
		if (this.shouhoutype.equals("jdtb")) {
			this.shouhouhint.setText("机电同步气量");
			this.input1.setHint("同步气量");
			this.input2.setHint("气表地址");
		}
		if (this.shouhoutype.equals("setip")) {
			this.shouhouhint.setText("设置IP地址");
			this.input1.setHint("IP地址");
			this.input2.setHint("端口号");
		}
		if (this.shouhoutype.equals("setlianlu")) {
			this.shouhouhint.setText("设置网络链路");
			this.input1.setHint("目的中集器");
			this.input2.setHint("下级中集器");
		}
		if (this.shouhoutype.equals("alterjzq")) {
			this.shouhouhint.setText("修改集中器地址");
			this.input1.setHint("原集中器地址");
			this.input1.setText("16777215");
			this.input2.setHint("新集中器地址");
		}
		if (this.shouhoutype.equals("dccjsj")) {
			this.shouhouhint.setText("单次采集数据");
			this.input1.setHint("中器地址");
			this.input2.setHint("采集目标包");
		}
		if (this.shouhoutype.equals("PriceAdjust")) {
			this.shouhouhint.setText("气价设置");
			this.input1.setHint("请输入表地址");
			this.input2.setHint("请输入气价");
			this.input3.setHint("请输入年月日");
			//input3.setVisibility(View.VISIBLE);
		}		
		this.shouhouhint.setTextSize(30.0F);
		this.shouhouhint.setTextColor(-16776961);

		// 设置按钮监听器
		this.shouhoubt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				GetmsgID localGetmsgID = new GetmsgID();
				new ToHexStr();
				headmsg = "5A5A00FE01";
				input1msg = input1.getText().toString();
				input2msg = input2.getText().toString();
				Intent intentBusy = new Intent("android.intent.action.busy");
				intentBusy.putExtra("State", "busy");
				sendBroadcast(intentBusy);				
				try {
                    //修改表地址操作
					if (shouhoutype.equals("gbdz")) {
						
//						input1nub = Integer.parseInt(input1msg);
//						input1hex = Integer.toHexString(input1nub);
//						input2nub = Integer.parseInt(input2msg);
//						input2hex = Integer.toHexString(input2nub);
						String Addr1=localGetmsgID.GetMeterAddr(input1msg);
						String Addr2=localGetmsgID.GetMeterAddr(input2msg);
						if ((Addr1!=null)&&(Addr2!=null)) {
							//String str5 = localGetmsgID.getMsgID(input1hex).toUpperCase();
							//String str6 = localGetmsgID.getMsgID(input2hex).toUpperCase();
							String DataLen=String.format("%02X",Addr2.length()/2);
							CRCmsg = ("09" + Addr1 + "E9" + DataLen + Addr2);
							ordermsg = headmsg + CRCmsg+ crc.CRC_CCITT(1, CRCmsg).toUpperCase()+ overmsg + "5B5B/";
							intent.putExtra("order", ordermsg);
							intent.putExtra("metertype",biaotype);
							intent.setClass(AlterBHActivity.this,BtXiMeiService.class);
							startService(intent);
							Intent localIntent3 = new Intent();
							localIntent3.putExtra("Comm", "00");
							localIntent3.setClass(AlterBHActivity.this,BackSingleCBActivity.class);
							AlterBHActivity.this.startActivity(localIntent3);

						} else {
							Toast.makeText(AlterBHActivity.this, "表号输入错误",Toast.LENGTH_SHORT).show();
						}
					}                    
					else if (shouhoutype.equals("dccjsj")) {//单次采集中继器数据
						input1nub = Integer.parseInt(input1msg);
						input1hex = Integer.toHexString(input1nub);
						input2nub = Integer.parseInt(input2msg);
						input2hex = Integer.toHexString(input2nub);
						if (input1nub >= 0 && input1nub <= 16777215&& input2nub >= 0 && input2nub <= 65535) {
							String str3 = localGetmsgID.getMsgID(input1hex).toUpperCase();
							String str4 = GetNowPacket.gettotalpack(input2hex, 4).toUpperCase();
							CRCmsg = ("09" + str3 + "09" + "02" + str4);
							ordermsg = ("5A5A00FE02" + CRCmsg+ crc.CRC_CCITT(1, CRCmsg).toUpperCase()+ overmsg + "5B5B/");
							intent.putExtra("order", ordermsg);
							intent.putExtra("Comm", "00");
							intent.setClass(AlterBHActivity.this,BtXiMeiService.class);
							AlterBHActivity.this.startService(intent);
							Intent localIntent2 = new Intent();
							localIntent2.putExtra("Comm", "00");
							localIntent2.setClass(AlterBHActivity.this,BackSingleCBActivity.class);
							AlterBHActivity.this.startActivity(localIntent2);

						} else {
							Toast.makeText(AlterBHActivity.this, "表号输入错误",Toast.LENGTH_SHORT).show();
						}

					} 
					else if (shouhoutype.equals("alterjzq")) {//修改集中器地址
						input1nub = Integer.parseInt(input1msg);
						input1hex = Integer.toHexString(input1nub);
						input2nub = Integer.parseInt(input2msg);
						input2hex = Integer.toHexString(input2nub);
						if (input1nub >= 0 && input1nub <= 16777215&& input2nub >= 0 && input2nub <= 16777215) {
							String str3 = localGetmsgID.getMsgID(input1hex).toUpperCase();
							String str4 = localGetmsgID.getMsgID(input2hex).toUpperCase();
							CRCmsg = ("09" + str3 + "02" + "04" + "00" + str4);
							ordermsg = ("5A5A00FE02" + CRCmsg+ crc.CRC_CCITT(1, CRCmsg).toUpperCase()+ overmsg + "5B5B/");
							intent.putExtra("order", ordermsg);
							intent.setClass(AlterBHActivity.this,BtXiMeiService.class);
							AlterBHActivity.this.startService(intent);
							Intent localIntent2 = new Intent();
							localIntent2.setClass(AlterBHActivity.this,BackCaiJiInFoActivity.class);
							AlterBHActivity.this.startActivity(localIntent2);

						} else {

							Toast.makeText(AlterBHActivity.this, "表号输入错误",Toast.LENGTH_SHORT).show();
						}
					}
					else if (shouhoutype.equals("jdtb")) { //机电同步气量
						String str1 = localGetmsgID.getMsgID(Integer.toHexString((int) (10.0F * Float.parseFloat(input1msg))));
//						input2nub = Integer.parseInt(input2msg);
//						AlterBHActivity.this.input2hex = Integer.toHexString(input2nub);
						String Addr=localGetmsgID.CheckMeterID(input2msg);
						if ((Addr!=null)) {

							//String str2 = localGetmsgID.getMsgID(input2hex).toUpperCase();
							CRCmsg = ("09" + Addr + "CC" + "03" + str1.toUpperCase());
							ordermsg = headmsg + CRCmsg+ crc.CRC_CCITT(1, CRCmsg).toUpperCase()+ overmsg + "5B5B/";
							intent.putExtra("order", ordermsg);
							intent.putExtra("metertype",biaotype);
							intent.setClass(AlterBHActivity.this,BtXiMeiService.class);
							startService(intent);
							Intent localIntent1 = new Intent();
							localIntent1.putExtra("Comm", "00");
							localIntent1.setClass(AlterBHActivity.this,BackSingleCBActivity.class);
							AlterBHActivity.this.startActivity(localIntent1);

						} else {
							Toast.makeText(AlterBHActivity.this, "表号输入错误",Toast.LENGTH_SHORT).show();
						}

					}
					else if(shouhoutype.equals("PriceAdjust")) //气价调整
					{
						//String str1 = localGetmsgID.getMsgID(Integer.toHexString((int) (10.0F * Float.parseFloat(input2msg))));
						String str1=String.format("%06X", (int) (100.0F * Float.parseFloat(input2msg)));
						str1=str1.substring(4, 6)+str1.substring(2, 4)+str1.substring(0, 2);
						String timeStr = new SimpleDateFormat("yyMMdd").format(new Date());
                        //String timeStr=input3.getText().toString();
                        //timeStr=timeStr.substring(4, 6)+timeStr.substring(2, 4)+timeStr.substring(0, 2);
//						input2nub = Integer.parseInt(input2msg);
//						AlterBHActivity.this.input2hex = Integer.toHexString(input2nub);
						String Addr=localGetmsgID.GetMeterAddr(input1msg);
						if ((Addr!=null)) {

							//String str2 = localGetmsgID.getMsgID(input2hex).toUpperCase();
							CRCmsg = ("09" + Addr + "80" + "06" + str1.toUpperCase())+timeStr;
							ordermsg = headmsg + CRCmsg+ crc.CRC_CCITT(1, CRCmsg).toUpperCase()+ overmsg + "5B5B/";
							intent.putExtra("order", ordermsg);
							intent.putExtra("metertype",biaotype);
							intent.setClass(AlterBHActivity.this,BtXiMeiService.class);
							startService(intent);
							Intent localIntent1 = new Intent();
							localIntent1.putExtra("Comm", "00");
							localIntent1.setClass(AlterBHActivity.this,BackSingleCBActivity.class);
							AlterBHActivity.this.startActivity(localIntent1);

						} else {
							Toast.makeText(AlterBHActivity.this, "表号输入错误",Toast.LENGTH_SHORT).show();
						}						
						
						
					}
					

				} catch (Exception e) {
					Toast.makeText(AlterBHActivity.this, "输入错误请重新输入",
							Toast.LENGTH_SHORT).show();

				}

			}
		});
	}
}
