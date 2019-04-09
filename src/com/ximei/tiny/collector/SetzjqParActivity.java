package com.ximei.tiny.collector;

import java.util.ArrayList;

import com.tiny.gasxm.R;
import com.tiny.gasxm.R.layout;
import com.tiny.gasxm.R.menu;
import com.ximei.tiny.backinfoview.BackCaiJiInFoActivity;
import com.ximei.tiny.backinfoview.BackJiZhongQiActivity;
import com.ximei.tiny.backinfoview.BackSingleCBActivity;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.service.CaijiService;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.GetTotalPack;
import com.ximei.tiny.tools.ToInverted;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SetzjqParActivity extends Activity {
	  private TextView caijihint;
	  private Button   BtnSetPar;
	  private EditText EdtZjqAddr;
	  private EditText EdtAreaCode;
	  private EditText EdtPic1101Fast;
	  private EditText EdtPic1101Slow;
	  private EditText EdtPic1278Fast;
	  private EditText EdtPic1278Slow;	  
	  private EditText EdtMsp1278Fast;
	  private EditText EdtMsp1278Slow;	 
	  private ToInverted toinver;
	  String caijitype;
	  CRC crc;
	  String headmsg;
	  Intent intent;
	  String ordermsg;
	  String overmsg;
	  String CRCmsg;
	  String target;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    //取消标题状态栏
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);		
		setContentView(R.layout.activity_setzjq_par);
		 this.intent = getIntent();
	    caijihint = ((TextView)findViewById(R.id.caijihint));
	    caijihint.setText("设置中继器工作参数");	
	    BtnSetPar=(Button)findViewById(R.id.zjqbt);
	    EdtZjqAddr=    (EditText)findViewById(R.id.zjqAddr);
	    EdtAreaCode=   (EditText)findViewById(R.id.areaCode);
	    EdtPic1101Fast=(EditText)findViewById(R.id.pic1101fastcount);
	    EdtPic1101Slow=(EditText)findViewById(R.id.pic1101slowcount);
	    EdtPic1278Fast=(EditText)findViewById(R.id.pic1278fastcount);
	    EdtPic1278Slow=(EditText)findViewById(R.id.pic1278slowcount);	 
	    EdtMsp1278Fast=(EditText)findViewById(R.id.msp1278fastcount);
	    EdtMsp1278Slow=(EditText)findViewById(R.id.msp1278slowcount);	
	    headmsg = "5A5A00FE02";
	    crc = new CRC();
	    toinver = new ToInverted();
	    overmsg = this.intent.getStringExtra("overmsg");
	    BtnSetPar.setOnClickListener(new View.OnClickListener()
	    {
	      public void onClick(View paramAnonymousView)
	      {
	        try
	        {
	        	//target=Integer.toHexString(Integer.parseInt(EdtZjqAddr.getText().toString()));
	        	target=toinver.toinverted(String.format("%06X",Integer.parseInt(EdtZjqAddr.getText().toString())));
	        	target+="000A";
	        	target+=toinver.toinverted(String.format("%08X",Integer.parseInt(EdtAreaCode.getText().toString())));
	        	target+=String.format("%02X",Integer.parseInt(EdtPic1101Fast.getText().toString()));
	        	target+=String.format("%02X",Integer.parseInt(EdtPic1101Slow.getText().toString()));
	        	target+=String.format("%02X",Integer.parseInt(EdtPic1278Fast.getText().toString()));
	        	target+=String.format("%02X",Integer.parseInt(EdtPic1278Slow.getText().toString()));
	        	target+=String.format("%02X",Integer.parseInt(EdtMsp1278Fast.getText().toString()));
	        	target+=String.format("%02X",Integer.parseInt(EdtMsp1278Slow.getText().toString()));
	            CRCmsg = ("09" + target).toUpperCase();
	            ordermsg = (headmsg + CRCmsg + crc.CRC_CCITT(1, CRCmsg).toUpperCase()+overmsg+"5B5B/");
	            intent.putExtra("order", ordermsg);
	            intent.setClass(SetzjqParActivity.this, BtXiMeiService.class);
	            SetzjqParActivity.this.startService(SetzjqParActivity.this.intent);        
	        	Intent localIntent = new Intent();
				localIntent.setClass(SetzjqParActivity.this,BackCaiJiInFoActivity.class);
				SetzjqParActivity.this.startActivity(localIntent);

	        }
	        catch (Exception localException)
	        {
	          Toast.makeText(SetzjqParActivity.this, "输入错误请重新输入", 0).show();
	        }
	      }
	    });	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setzjq_par, menu);
		return true;
	}

}
