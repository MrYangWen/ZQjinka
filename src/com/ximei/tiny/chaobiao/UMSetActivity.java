package com.ximei.tiny.chaobiao;

import com.tiny.gasxm.R;
import com.tiny.gasxm.R.layout;
import com.tiny.gasxm.R.menu;
import com.ximei.tiny.backinfoview.BackCaiJiInFoActivity;
import com.ximei.tiny.backinfoview.BackSingleCBActivity;
import com.ximei.tiny.collector.SetzjqParActivity;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.GetmsgID;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UMSetActivity extends Activity implements OnClickListener{
	private TextView caijihint;	
	private EditText EdtPulseUnit,EdtmeterAddr,EdtRunMode,EdtUmFactor1,EdtUmFactor2,EdtUmFactor3,EdtUmFactor4,EdtUmFactor5,EdtUmFactor6,EdtUmFactor7;
	private Button   BtnSetPar;
	Intent intent;
	String overmsg;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    //取消标题状态栏	
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);		
		setContentView(R.layout.activity_umset);
		intent = getIntent();
	    caijihint = ((TextView)findViewById(R.id.caijihint));
	    caijihint.setText("超声波系数修正");	
	    BtnSetPar=(Button)findViewById(R.id.zjqbt);
	    BtnSetPar.setOnClickListener(this);
	    EdtRunMode=   (EditText)findViewById(R.id.meterRunMode);
	    EdtPulseUnit = (EditText)findViewById(R.id.pulseunit);
	    EdtUmFactor1= (EditText)findViewById(R.id.um1);
	    EdtUmFactor2= (EditText)findViewById(R.id.um2);
	    EdtUmFactor3= (EditText)findViewById(R.id.um3);
	    EdtUmFactor4= (EditText)findViewById(R.id.um4);
	    EdtUmFactor5= (EditText)findViewById(R.id.um5);
	    EdtUmFactor6= (EditText)findViewById(R.id.um6);
	    EdtUmFactor7= (EditText)findViewById(R.id.um7);
	    EdtmeterAddr= (EditText)findViewById(R.id.zjqAddr);
	    overmsg     =  intent.getStringExtra("overmsg");
	    sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);  
	    EdtRunMode.setText(sp.getString("EdtRunMode", "")); 
	    EdtPulseUnit.setText(sp.getString("EdtPulseUnit", ""));
	    EdtmeterAddr.setText(sp.getString("EdtmeterAddr", "")); 
	    EdtUmFactor1.setText(sp.getString("EdtUmFactor1", "")); 
	    EdtUmFactor2.setText(sp.getString("EdtUmFactor2", "")); 
	    EdtUmFactor3.setText(sp.getString("EdtUmFactor3", "")); 
	    EdtUmFactor4.setText(sp.getString("EdtUmFactor4", "")); 
	    EdtUmFactor5.setText(sp.getString("EdtUmFactor5", "")); 
	    EdtUmFactor6.setText(sp.getString("EdtUmFactor6", "")); 
	    EdtUmFactor7.setText(sp.getString("EdtUmFactor7", ""));    	    
	}
	@Override
    protected void onPause() {
        super.onPause();
        Editor editor = sp.edit();  
        editor.putString("EdtRunMode", EdtRunMode.getText().toString()); 
        editor.putString("EdtPulseUnit", EdtPulseUnit.getText().toString()); 
        editor.putString("EdtmeterAddr", EdtmeterAddr.getText().toString()); 
        editor.putString("EdtUmFactor1", EdtUmFactor1.getText().toString()); 
        editor.putString("EdtUmFactor2", EdtUmFactor2.getText().toString()); 
        editor.putString("EdtUmFactor3", EdtUmFactor3.getText().toString()); 
        editor.putString("EdtUmFactor4", EdtUmFactor4.getText().toString()); 
        editor.putString("EdtUmFactor5", EdtUmFactor5.getText().toString()); 
        editor.putString("EdtUmFactor6", EdtUmFactor6.getText().toString()); 
        editor.putString("EdtUmFactor7", EdtUmFactor7.getText().toString()); 
		editor.commit();        
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.umset, menu);
		return true;
	}
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
        try
        {
			 Intent intentBusy = new Intent("android.intent.action.busy");
			 intentBusy.putExtra("State", "busy");
			 sendBroadcast(intentBusy);	
			
			 GetmsgID localGetmsgID = new GetmsgID();	        	
        	 CRC crc = new CRC();
             String str=String.format("%02X", Integer.parseInt(EdtRunMode.getText().toString()));
             str+=String.format("%02X", Integer.parseInt(EdtPulseUnit.getText().toString(), 10));

             str+=String.format("%02X", Integer.parseInt(EdtUmFactor1.getText().toString(), 10));
             str+=String.format("%02X", Integer.parseInt(EdtUmFactor2.getText().toString(), 10));
             str+=String.format("%02X", Integer.parseInt(EdtUmFactor3.getText().toString(), 10));
             str+=String.format("%02X", Integer.parseInt(EdtUmFactor4.getText().toString(), 10));
             str+=String.format("%02X", Integer.parseInt(EdtUmFactor5.getText().toString(), 10));
             str+=String.format("%02X", Integer.parseInt(EdtUmFactor6.getText().toString(), 10));
             str+=String.format("%02X", Integer.parseInt(EdtUmFactor7.getText().toString(), 10));
			 int intID = Integer.parseInt(EdtmeterAddr.getText().toString());
		     String MsgID = Integer.toHexString(intID);
             String Msgvalue = localGetmsgID.getMsgID(MsgID).toUpperCase();
			 String CRCmsg = ("09" + Msgvalue + "9B" + "09" + str);
			 String ordermsg = "5A5A00FE01" + CRCmsg+ crc.CRC_CCITT(1, CRCmsg).toUpperCase()+ overmsg + "5B5B/";
			 intent.putExtra("order", ordermsg);
			 intent.putExtra("metertype", "newmeter");
			 intent.setClass(UMSetActivity.this, BtXiMeiService.class);
			 startService(UMSetActivity.this.intent);

				Intent localIntent = new Intent();
				localIntent.putExtra("Comm", "00");
				localIntent.setClass(UMSetActivity.this,BackSingleCBActivity.class);
				startActivity(localIntent);                 
        }
        catch (Exception localException)
        {
          Toast.makeText(this, localException.toString(), 0).show();
        }		
	}

}
