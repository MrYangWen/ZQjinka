package com.ximei.tiny.collector;


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
import android.widget.TextView;
import android.widget.Toast;

import com.tiny.gasxm.R;
import com.ximei.tiny.backinfoview.BackCaiJiInFoActivity;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.GetTotalPack;
import com.ximei.tiny.tools.ToHexStr;
/*
 * 设置抄表时间activity
 * 
 * 
 */
public class CaiJiSetCBTimeActivity extends Activity
{
  String CRCmsg;
  private Button caijibt;
  private TextView caijihint,ShowTxt;
  private EditText caijiinput,MeterInput,CommondType;  
  String caijitype;
  CRC crc;
  GetTotalPack gettarget;
  String headmsg;
  int inputnub;
  String inputstr;
  Intent intent;
  String ordermsg;
  String overmsg;
  String target;
  ToHexStr tohex;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    //取消标题状态栏
    requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(1024, 1024);
    setContentView(R.layout.caijiinput);
    this.caijiinput = ((EditText)findViewById(R.id.caijiinput));
    this.caijibt = ((Button)findViewById(R.id.caijibt));
    this.caijihint = ((TextView)findViewById(R.id.caijihint));
    this.intent = getIntent();
    this.tohex = new ToHexStr();
    this.crc = new CRC();
    this.gettarget = new GetTotalPack();
    this.overmsg = this.intent.getStringExtra("overmsg");
    this.target = this.intent.getStringExtra("target");
    this.headmsg = "5A5A00FE02";
    //设置TextView的值起到提示作用
    this.caijihint.setText("设置抄表时间");
    this.caijiinput.setHint("年、月、日、时、分、频度");
    this.caijibt.setHint("确定");
    this.MeterInput = ((EditText)findViewById(R.id.meterinput));
    CommondType     = ((EditText)findViewById(R.id.commondType));
    ShowTxt         = ((TextView)findViewById(R.id.showtxt));   
    MeterInput.setVisibility(View.INVISIBLE);
    CommondType.setVisibility(View.INVISIBLE);
    ShowTxt.setVisibility(View.INVISIBLE);
    //注册监听事件
    this.caijibt.setOnClickListener(new OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        CaiJiSetCBTimeActivity.this.inputstr = CaiJiSetCBTimeActivity.this.caijiinput.getText().toString();
        if (CaiJiSetCBTimeActivity.this.inputstr.length() == 12)
        {
          String str1 = CaiJiSetCBTimeActivity.this.inputstr.substring(0, 2);   //年
          String str2 = CaiJiSetCBTimeActivity.this.inputstr.substring(2, 4);   //月
          String str3 = CaiJiSetCBTimeActivity.this.inputstr.substring(4, 6);   //日
          String str4 = CaiJiSetCBTimeActivity.this.inputstr.substring(6, 8);   //时
          String str5 = CaiJiSetCBTimeActivity.this.inputstr.substring(8, 10);  //分
          String str6 = CaiJiSetCBTimeActivity.this.inputstr.substring(10, 12); //秒          
          int y = Integer.parseInt(str1);
          int m = Integer.parseInt(str2);
          int d = Integer.parseInt(str3);
          int h = Integer.parseInt(str4);
          int f = Integer.parseInt(str5);
          int s = Integer.parseInt(str6);
          if(m<=12)
          {
	          if (d < 32)
	          {
	            if (h < 25)
	            {
	              if (f < 61)
	              {
	                if (((s < 3)&&(s>0))||(s==11))
	                {
//	                  String str7 =  CaiJiSetCBTimeActivity.this.tohex.toHexStr(str1);
//	                  String str8 =  CaiJiSetCBTimeActivity.this.tohex.toHexStr(str2);
//	                  String str9 =  CaiJiSetCBTimeActivity.this.tohex.toHexStr(str3);
//	                  String str10 = CaiJiSetCBTimeActivity.this.tohex.toHexStr(str4);
//	                  String str11 = CaiJiSetCBTimeActivity.this.tohex.toHexStr(str5);
//	                  String str12 = CaiJiSetCBTimeActivity.this.tohex.toHexStr(str6);                  
	                  //String mday = new SimpleDateFormat("MMyy").format(new Date());
	                  //String hexmday = new ToHexStr().toHexStr(mday);
	                  CaiJiSetCBTimeActivity.this.CRCmsg = ("09" + target + "01" + "07" + "01" + str1 +str2 + str3 + str4 + str5 + str6).toUpperCase();
	                  CaiJiSetCBTimeActivity.this.ordermsg = (headmsg + CRCmsg + crc.CRC_CCITT(1, CRCmsg).toUpperCase())+overmsg+"5B5B/";
	                  Log.e("test", ordermsg);
	                  CaiJiSetCBTimeActivity.this.intent.putExtra("order", CaiJiSetCBTimeActivity.this.ordermsg);
	                  CaiJiSetCBTimeActivity.this.intent.setClass(CaiJiSetCBTimeActivity.this, BtXiMeiService.class);
	                  CaiJiSetCBTimeActivity.this.startService(CaiJiSetCBTimeActivity.this.intent);
	                  CaiJiSetCBTimeActivity.this.intent.setClass(CaiJiSetCBTimeActivity.this, BackCaiJiInFoActivity.class);
	                  CaiJiSetCBTimeActivity.this.startActivity(CaiJiSetCBTimeActivity.this.intent);
	                  return;
	                }
	                Toast.makeText(CaiJiSetCBTimeActivity.this, "抄表频率只能是01,02,11", Toast.LENGTH_SHORT).show();
	                return;
	              }
	              Toast.makeText(CaiJiSetCBTimeActivity.this, "分钟大于60", Toast.LENGTH_SHORT).show();
	              return;
	            }
	            Toast.makeText(CaiJiSetCBTimeActivity.this, "时间大于24", Toast.LENGTH_SHORT).show();
	            return;
	          }
	          Toast.makeText(CaiJiSetCBTimeActivity.this, "日期大于31", Toast.LENGTH_SHORT).show();
	          return;        	  
          }
          Toast.makeText(CaiJiSetCBTimeActivity.this, "月大于12", Toast.LENGTH_SHORT).show();
          return;   
        }
        Toast.makeText(CaiJiSetCBTimeActivity.this, "请输入正确的时间", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
