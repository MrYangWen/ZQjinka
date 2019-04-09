package com.ximei.tiny.collector;
import java.text.SimpleDateFormat;

import com.tiny.gasxm.R;
import com.ximei.tiny.backinfoview.BackCaiJiInFoActivity;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.GetmsgID;
import com.ximei.tiny.tools.ToInverted;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetMeterParActivity extends Activity {
	  private Button   BtnSetPar;
	  private EditText EdtIOTMeterID;
	  private EditText EdtStepPriceEnable;
	  private EditText EdtAdjustPriceStyleSel;
	  private EditText EdtStepPrice1;
	  private EditText EdtStepPrice2;
	  private EditText EdtStepPrice3;	  
	  private EditText EdtStepPrice4;
	  private EditText EdtStepPrice5;
	  private EditText EdtAdjustPriceTime;
	  private EditText EdtStepEnableTime;
	  private EditText EdtStepVolume1;	  
	  private EditText EdtStepVolume2;
	  private EditText EdtStepVolume3;	
	  private EditText EdtStepVolume4;
	  private EditText EdtStepChargePeriod;
	  private EditText EdtSettleStyle;	  
	  private EditText EdtStepNum;
	  private EditText EdtStepJieSuan;	  
	  Intent intent=new Intent();
	  private SharedPreferences sp;
	  CRC crc = new CRC();
	  String overmsg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    //取消标题状态栏
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setmeter_par);
	    BtnSetPar=(Button)findViewById(R.id.zjqbt);
	    EdtIOTMeterID=    (EditText)findViewById(R.id.IOTMeterID);
	    EdtStepPriceEnable=   (EditText)findViewById(R.id.StepPriceEnable);
	    EdtAdjustPriceStyleSel=(EditText)findViewById(R.id.AdjustPriceStyleSel);
	    EdtStepPrice1=(EditText)findViewById(R.id.StepPrice1);
	    EdtStepPrice2=(EditText)findViewById(R.id.StepPrice2);
	    EdtStepPrice3=(EditText)findViewById(R.id.StepPrice3);	 
	    EdtStepPrice4=(EditText)findViewById(R.id.StepPrice4);
	    EdtStepPrice5=(EditText)findViewById(R.id.StepPrice5);		
	    EdtAdjustPriceTime=    (EditText)findViewById(R.id.AdjustPriceTime);
	    EdtStepEnableTime=   (EditText)findViewById(R.id.StepEnableTime);
	    EdtStepVolume1=(EditText)findViewById(R.id.StepVolume1);
	    EdtStepVolume2=(EditText)findViewById(R.id.StepVolume2);
	    EdtStepVolume3=(EditText)findViewById(R.id.StepVolume3);
	    EdtStepVolume4=(EditText)findViewById(R.id.StepVolume4);	 
	    EdtStepChargePeriod=(EditText)findViewById(R.id.StepChargePeriod);
	    EdtSettleStyle=(EditText)findViewById(R.id.SettleStyle);	
	    EdtStepNum=(EditText)findViewById(R.id.StepNum);
	    EdtStepJieSuan=(EditText)findViewById(R.id.StepJieSuan);	
	    // 注册通信成功和失败广播
        sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);   
	    EdtIOTMeterID.setText(sp.getString("EdtIOTMeterID", ""));  
	    EdtStepPriceEnable.setText(sp.getString("EdtStepPriceEnable", ""));	    
	    EdtAdjustPriceStyleSel.setText(sp.getString("EdtAdjustPriceStyleSel", ""));	 
	    EdtStepPrice1.setText(sp.getString("EdtStepPrice1", ""));	 
	    EdtStepPrice2.setText(sp.getString("EdtStepPrice2", ""));	 
	    EdtStepPrice3.setText(sp.getString("EdtStepPrice3", ""));	 
	    EdtStepPrice4.setText(sp.getString("EdtStepPrice4", ""));	 
	    EdtStepPrice5.setText(sp.getString("EdtStepPrice5", ""));	 
	    EdtAdjustPriceTime.setText(sp.getString("EdtAdjustPriceTime", ""));	 
	    EdtStepEnableTime.setText(sp.getString("EdtStepEnableTime", ""));	 
	    EdtStepVolume1.setText(sp.getString("EdtStepVolume1", ""));	 
	    EdtStepVolume2.setText(sp.getString("EdtStepVolume2", ""));	 
	    EdtStepVolume3.setText(sp.getString("EdtStepVolume3", ""));	 
	    EdtStepVolume4.setText(sp.getString("EdtStepVolume4", ""));	 
	    EdtStepChargePeriod.setText(sp.getString("EdtStepChargePeriod", ""));	 
	    EdtSettleStyle.setText(sp.getString("EdtSettleStyle", ""));	 
	    EdtStepNum.setText(sp.getString("EdtStepNum", ""));	 
	    EdtStepJieSuan.setText(sp.getString("EdtStepJieSuan", ""));	 
	    overmsg = getIntent().getStringExtra("overmsg");
 		    
	    BtnSetPar.setOnClickListener(new View.OnClickListener()
	    {
	      public void onClick(View paramAnonymousView)
	      {
	        try
	        {
	        	//toinver.toinverted(String.format("%06X",Integer.parseInt(EdtIOTMeterID.getText().toString()))) 
	    		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyMMddhhmmss");
	    		String date = sDateFormat.format(new java.util.Date());	
	        	ToInverted toinver = new ToInverted();
				String target="";  //06694462
				byte[] dat=new byte[100]; 
				String headmsg,CRCmsg = null,ordermsg = null;				
				headmsg ="5A5A00FE01";
				target+=String.format("%02X",(Integer.parseInt(EdtAdjustPriceStyleSel.getText().toString())))
						+String.format("%02X", Integer.parseInt(EdtAdjustPriceTime.getText().toString().substring(0, 2)))
                        +String.format("%02X", Integer.parseInt(EdtAdjustPriceTime.getText().toString().substring(2, 4)))
                        +String.format("%02X", Integer.parseInt(EdtAdjustPriceTime.getText().toString().substring(4, 6)))
					    +toinver.toinverted(String.format("%04X", Integer.parseInt(EdtStepPrice1.getText().toString())))
						+toinver.toinverted(String.format("%04X", Integer.parseInt(EdtStepPrice2.getText().toString())))
						+toinver.toinverted(String.format("%04X", Integer.parseInt(EdtStepPrice3.getText().toString())))
						+toinver.toinverted(String.format("%04X", Integer.parseInt(EdtStepPrice4.getText().toString())))
						+toinver.toinverted(String.format("%04X", Integer.parseInt(EdtStepPrice5.getText().toString())))
						+toinver.toinverted(String.format("%04X", Integer.parseInt(EdtStepVolume1.getText().toString())))
						+toinver.toinverted(String.format("%04X", Integer.parseInt(EdtStepVolume2.getText().toString())))
						+toinver.toinverted(String.format("%04X", Integer.parseInt(EdtStepVolume3.getText().toString())))
						+toinver.toinverted(String.format("%04X", Integer.parseInt(EdtStepVolume4.getText().toString())))
						+toinver.toinverted(String.format("%02X", Integer.parseInt(EdtStepChargePeriod.getText().toString())))
						+String.format("%02X",(Integer.parseInt(EdtSettleStyle.getText().toString())))
						+String.format("%02X",Integer.parseInt(date.substring(0, 2)))
						+String.format("%02X",Integer.parseInt(date.substring(2, 4)))
						+String.format("%02X",Integer.parseInt(date.substring(4, 6)))
						+String.format("%02X",Integer.parseInt(date.substring(6, 8)))
						+String.format("%02X",Integer.parseInt(date.substring(8, 10)))
						+String.format("%02X",Integer.parseInt(date.substring(10, 12)));
				        
			
				GetmsgID localGetmsgID = new GetmsgID();
				//String str5 = localGetmsgID.getMsgID(Integer.toHexString(Integer.parseInt(EdtIOTMeterID.getText().toString()))).toUpperCase();
				String str5=localGetmsgID.GetMeterAddr(EdtIOTMeterID.getText().toString()).toUpperCase();
				CRCmsg = ("09"+str5+"64"+"1E"+ target).toUpperCase();
	            ordermsg = (headmsg + CRCmsg + crc.CRC_CCITT(1, CRCmsg).toUpperCase()+overmsg+"5B5B/");
	            intent.putExtra("order", ordermsg);
	            intent.setClass(SetMeterParActivity.this, BtXiMeiService.class);
	            SetMeterParActivity.this.startService(SetMeterParActivity.this.intent);        
	        	Intent localIntent = new Intent();
				localIntent.setClass(SetMeterParActivity.this,BackCaiJiInFoActivity.class);
				SetMeterParActivity.this.startActivity(localIntent);   
 
	        }
	        catch (Exception localException)
	        {
	          Toast.makeText(SetMeterParActivity.this, "输入错误请重新输入", 0).show();
	        }
	      }
	    });	    
	}
	@Override
    protected void onPause() {
        super.onPause();
        Editor editor = sp.edit();          
        editor.putString("EdtIOTMeterID",   EdtIOTMeterID.getText().toString()); 
	    editor.putString("EdtAdjustPriceStyleSel", EdtAdjustPriceStyleSel.getText().toString());
	    editor.putString("EdtStepPriceEnable", EdtStepPriceEnable.getText().toString());
	    editor.putString("EdtStepPrice1", EdtStepPrice1.getText().toString());
        editor.putString("EdtStepPrice2",   EdtStepPrice2.getText().toString()); 
	    editor.putString("EdtStepPrice3", EdtStepPrice3.getText().toString());
	    editor.putString("EdtStepPrice4", EdtStepPrice4.getText().toString());
	    editor.putString("EdtStepPrice5", EdtStepPrice5.getText().toString());
        editor.putString("EdtAdjustPriceTime",   EdtAdjustPriceTime.getText().toString()); 
	    editor.putString("EdtStepEnableTime", EdtStepEnableTime.getText().toString());
	    editor.putString("EdtStepVolume1", EdtStepVolume1.getText().toString());
	    editor.putString("EdtStepVolume2", EdtStepVolume2.getText().toString());
        editor.putString("EdtStepVolume3",   EdtStepVolume3.getText().toString()); 
	    editor.putString("EdtStepVolume4", EdtStepVolume4.getText().toString());
	    editor.putString("EdtStepChargePeriod", EdtStepChargePeriod.getText().toString());
	    editor.putString("EdtSettleStyle", EdtSettleStyle.getText().toString());
	    editor.putString("EdtStepNum", EdtStepNum.getText().toString());
	    editor.putString("EdtStepJieSuan", EdtStepJieSuan.getText().toString());
		editor.commit();        
    }		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.setzjq_par, menu);
		return true;
	}

}
