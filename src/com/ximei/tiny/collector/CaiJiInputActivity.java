package com.ximei.tiny.collector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tiny.gasxm.R;
import com.ximei.tiny.backinfoview.BackCaiJiInFoActivity;
import com.ximei.tiny.backinfoview.BackJiZhongQiActivity;
import com.ximei.tiny.backinfoview.BackSingleCBActivity;
import com.ximei.tiny.chaobiao.SingleCBActivity;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.service.CaijiService;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.GetTotalPack;
import java.util.ArrayList;
/*
 * 
 * 集中器输入信息处理activity
 * 
 */
public class CaiJiInputActivity extends Activity
{
  String CRCmsg;
  private Button caijibt;
  private TextView caijihint,ShowTxt;
  private EditText caijiinput,MeterInput,CommondType;
  String caijitype;
  CRC crc;
  String databasename;
  GetTotalPack gettarget;
  String headmsg;
  int inputnub;
  String inputstr;
  Intent intent;
  String ordermsg;
  String overmsg;
  ArrayList<String> qbbhlist = new ArrayList<String>();
  String target;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    //取消标题状态栏
    requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(1024, 1024);
    setContentView(R.layout.caijiinput);
    this.caijiinput = ((EditText)findViewById(R.id.caijiinput));
    this.MeterInput = ((EditText)findViewById(R.id.meterinput));
    CommondType     = ((EditText)findViewById(R.id.commondType));
    ShowTxt         = ((TextView)findViewById(R.id.showtxt));
    this.caijibt = ((Button)findViewById(R.id.caijibt));
    this.caijihint = ((TextView)findViewById(R.id.caijihint));
    this.intent = getIntent();
    this.gettarget = new GetTotalPack();
    this.crc = new CRC();
    MeterInput.setVisibility(View.INVISIBLE);
    CommondType.setVisibility(View.INVISIBLE);
    ShowTxt.setVisibility(View.INVISIBLE);
    this.overmsg = this.intent.getStringExtra("overmsg");
    this.caijitype = this.intent.getStringExtra("caijitype");
    this.databasename = this.intent.getStringExtra("databasename");
    this.headmsg = "5A5A00FE02";
    if (this.caijitype.equals("settime"))
    {
        this.caijihint.setText("设置时间");
        this.caijiinput.setHint("请输入节点地址");
        this.caijibt.setHint("确定");
    }
    if (this.caijitype.equals("queryzt"))
    {
      this.caijihint.setText("查询中继器状态");
      this.caijiinput.setHint("请输入节点地址");
      this.caijibt.setHint("确定");
    }
    if (this.caijitype.equals("delbh"))
    {
      MeterInput.setVisibility(View.VISIBLE);	
      CommondType.setVisibility(View.VISIBLE);	
      ShowTxt.setVisibility(View.VISIBLE);	
      this.caijihint.setText("清除集中器表号");
      MeterInput.setHint("要删除的表地址");
      CommondType.setHint("请输入表类型,见提示 ");
      this.caijiinput.setHint("请输入节点地址");
      this.caijibt.setHint("确定");
    }
    if (this.caijitype.equals("addbh"))
    {
      MeterInput.setVisibility(View.VISIBLE);	
      CommondType.setVisibility(View.VISIBLE);	
      ShowTxt.setVisibility(View.VISIBLE);	
      this.caijihint.setText("添加集中器表号");
      MeterInput.setHint("要添加的表地址");
      CommondType.setHint("请输入表类型,见提示 ");
      this.caijiinput.setHint("请输入节点地址");
      this.caijibt.setHint("确定");
    }  
    if (this.caijitype.equals("cbcs")) //抄表测试
    {
      MeterInput.setVisibility(View.VISIBLE);	
      CommondType.setVisibility(View.VISIBLE);	
      ShowTxt.setVisibility(View.VISIBLE);	
      this.caijihint.setText("抄表测试");
      MeterInput.setHint("请输入表地址");
      CommondType.setHint("请输入表类型,见提示 ");
      this.caijiinput.setHint("请输入节点地址");
      this.caijibt.setHint("确定");
    }     
    if (this.caijitype.equals("caijibh"))
    {
      this.caijihint.setText("采集已组网表号");
      this.caijiinput.setHint("请输入节点地址");
      this.caijibt.setHint("确定");
    }
    if (this.caijitype.equals("caijidata"))
    {
      this.caijihint.setText("采集抄表数据");
      this.caijiinput.setHint("请输入节点地址");
      this.caijibt.setHint("确定");
    }
    if (this.caijitype.equals("caijicsbh"))
    {
      this.caijihint.setText("传输表号到中继器");
      this.caijiinput.setHint("请输入节点地址");
      this.caijibt.setHint("确定");
    }
    if (this.caijitype.equals("nozwcsbh"))
    {
      qbbhlist = this.intent.getStringArrayListExtra("qbbhlist");	
      this.caijihint.setText("传输表号到中继器");
      this.caijiinput.setHint("请输入节点地址");
      this.caijibt.setHint("确定");
     
    }
    if (this.caijitype.equals("caijizwcs"))
    {
      this.caijihint.setText("测试组网");
      this.caijiinput.setHint("请输入节点地址");
      this.caijibt.setHint("确定");
      this.qbbhlist = this.intent.getStringArrayListExtra("qbbhlist");
      this.databasename = this.intent.getStringExtra("databasename");
    }
    if (this.caijitype.equals("setip"))
    {
      this.caijihint.setText("设置IP地址");
      this.caijiinput.setHint("请输入IP地址");
      this.caijibt.setHint("确定");
    
    }
    
   
    
    
    this.caijibt.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
		Intent intentBusy = new Intent("android.intent.action.busy");
		intentBusy.putExtra("State", "busy");
		sendBroadcast(intentBusy);	
        try
        {
          inputstr = caijiinput.getText().toString();
          inputnub = Integer.parseInt(inputstr);
          //输入的目标地址转化为16进制
          target = gettarget.gettotalpack(Integer.toHexString(inputnub), 6);
          //组网测试
          if (CaiJiInputActivity.this.caijitype.equals("caijizwcs"))
          {
            intent.putExtra("qbbh", qbbhlist);
            intent.putExtra("target", target);
            intent.putExtra("datatype", caijitype);
            intent.putExtra("databasename", databasename);
            intent.setClass(CaiJiInputActivity.this, CaiJiZWActivity.class);
            CaiJiInputActivity.this.startActivity(intent);
          }
          //设置中集器时间
          if (CaiJiInputActivity.this.caijitype.equals("settime"))
          {
            intent.putExtra("overmsg", overmsg);
            intent.putExtra("target", target);
            intent.setClass(CaiJiInputActivity.this, CaiJiSetTimeActivity.class);
            CaiJiInputActivity.this.startActivity(intent);
          }
          //删除表号
          if (CaiJiInputActivity.this.caijitype.equals("delbh"))
          {
        	  String tmpstr="";
        	  if(Integer.parseInt(CommondType.getText().toString())==0x01){ //1101
        		  tmpstr="00";
        	  }else if(Integer.parseInt(CommondType.getText().toString())==0x02){//pic1278
        		  tmpstr="01";
        	  }else if(Integer.parseInt(CommondType.getText().toString())==0x03){//msp1278
        		  tmpstr="11";
        	  }else if(Integer.parseInt(CommondType.getText().toString())==0xFF){//所有表
        		  tmpstr="FF";
        	  }else{
        		  Toast.makeText(CaiJiInputActivity.this, "错误的表类型", 0).show(); 
        	  }
 			 CRCmsg = ("09" + target + "04" + "05" + tmpstr + "01"+
 					gettarget.gettotalpack(Integer.toHexString(Integer.parseInt(MeterInput.getText().toString())), 6)).toUpperCase();
 	         ordermsg = (headmsg + CRCmsg + crc.CRC_CCITT(1, CRCmsg).toUpperCase()+overmsg+"5B5B/");  
 	         intent.putExtra("order", CaiJiInputActivity.this.ordermsg);
 	         intent.setClass(CaiJiInputActivity.this, BtXiMeiService.class);
 	         CaiJiInputActivity.this.startService(CaiJiInputActivity.this.intent);
 	         intent.setClass(CaiJiInputActivity.this, BackCaiJiInFoActivity.class);
 	         CaiJiInputActivity.this.startActivity(intent); 
           
          }
          if (CaiJiInputActivity.this.caijitype.equals("addbh"))
          {
        	  String tmpstr="";
        	  if(Integer.parseInt(CommondType.getText().toString())==0x01){ //1101
        		  tmpstr="00";
        	  }else if(Integer.parseInt(CommondType.getText().toString())==0x02){//pic1278
        		  tmpstr="01";
        	  }else if(Integer.parseInt(CommondType.getText().toString())==0x03){//msp1278
        		  tmpstr="11";
        	  }else if(Integer.parseInt(CommondType.getText().toString())==0xFF){//所有表
        		  tmpstr="FF";
        	  }else{
        		  Toast.makeText(CaiJiInputActivity.this, "错误的表类型", 0).show(); 
        	  }
 			 CRCmsg = ("09" + target + "03" + "05" + tmpstr + "01"+
 					gettarget.gettotalpack(Integer.toHexString(Integer.parseInt(MeterInput.getText().toString())), 6)).toUpperCase();
 	         ordermsg = (headmsg + CRCmsg + crc.CRC_CCITT(1, CRCmsg).toUpperCase()+overmsg+"5B5B/");  
 	         intent.putExtra("order", CaiJiInputActivity.this.ordermsg);
 	         intent.setClass(CaiJiInputActivity.this, BtXiMeiService.class);
 	         CaiJiInputActivity.this.startService(CaiJiInputActivity.this.intent);
 	         intent.setClass(CaiJiInputActivity.this, BackCaiJiInFoActivity.class);
 	         CaiJiInputActivity.this.startActivity(intent); 
           
          }          
          //查询中继器状态
          if (CaiJiInputActivity.this.caijitype.equals("queryzt"))
          {
            CRCmsg = ("09" + target + "0500").toUpperCase();
            ordermsg = (headmsg + CRCmsg + crc.CRC_CCITT(1, CRCmsg).toUpperCase()+overmsg+"5B5B/");
            intent.putExtra("order", ordermsg);
            
            intent.setClass(CaiJiInputActivity.this, BtXiMeiService.class);
            CaiJiInputActivity.this.startService(CaiJiInputActivity.this.intent);        
        	Intent localIntent = new Intent();
        	localIntent.putExtra("Comm", "00");
			localIntent.setClass(CaiJiInputActivity.this,BackSingleCBActivity.class);
			CaiJiInputActivity.this.startActivity(localIntent);
          }
          //采集数据
          if (CaiJiInputActivity.this.caijitype.equals("caijidata"))
          {
            CRCmsg = ("09" + target  + "06" + "00").toUpperCase();
            ordermsg = (headmsg + CRCmsg + crc.CRC_CCITT(1, CRCmsg).toUpperCase()+overmsg+"5B5B/");
            intent.putExtra("overmsg", overmsg);
            intent.putExtra("order", ordermsg);
            intent.putExtra("target", target);
            intent.putExtra("datatype", caijitype);
            intent.putExtra("databasename",databasename);
            intent.setClass(CaiJiInputActivity.this, CaijiService.class);
            CaiJiInputActivity.this.startService(intent);
            intent.setClass(CaiJiInputActivity.this, BackJiZhongQiActivity.class);
            CaiJiInputActivity.this.startActivity(intent);
          }
          //采集组网表号
          if (CaiJiInputActivity.this.caijitype.equals("caijibh"))
          {
            CRCmsg = ("2C" + target + "5A" + "07" + "888888" + "01" + "FFFFBB");
            ordermsg = (headmsg + CRCmsg + crc.CRC_CCITT(1, CRCmsg).toUpperCase())+overmsg+"5B5B/";
            intent.putExtra("overmsg", overmsg);
            intent.putExtra("order", ordermsg);
            intent.putExtra("target", target);
            intent.putExtra("datatype", caijitype);
            intent.putExtra("databasename", databasename);
            intent.setClass(CaiJiInputActivity.this, CaijiService.class);
            CaiJiInputActivity.this.startService(intent);
            intent.setClass(CaiJiInputActivity.this, BackJiZhongQiActivity.class);
            CaiJiInputActivity.this.startActivity(intent);
          }
          //传输（要组网）表号
          //抄表测试
          if (CaiJiInputActivity.this.caijitype.equals("cbcs"))
          {
//            intent.putExtra("datatype", caijitype);
//            intent.putExtra("databasename", databasename);
//            intent.putExtra("target", target);
//            intent.putExtra("jzqflag", inputstr);
//            intent.setClass(CaiJiInputActivity.this, BackJiZhongQiActivity.class);
//            CaiJiInputActivity.this.startActivity(intent);
        	  String tmpstr="";
        	  if(Integer.parseInt(CommondType.getText().toString())==0x01){ //1101
        		  tmpstr="00";
        	  }else if(Integer.parseInt(CommondType.getText().toString())==0x02){//pic1278
        		  tmpstr="01";
        	  }else if(Integer.parseInt(CommondType.getText().toString())==0x03){//msp1278
        		  tmpstr="11";
        	  }else{
        		  Toast.makeText(CaiJiInputActivity.this, "错误的表类型", 0).show(); 
        	  }
 			 CRCmsg = ("09" + target + "0C" + "04" + tmpstr +gettarget.gettotalpack(Integer.toHexString(Integer.parseInt(MeterInput.getText().toString())), 6)).toUpperCase();
 	         ordermsg = (headmsg + CRCmsg + crc.CRC_CCITT(1, CRCmsg).toUpperCase()+overmsg+"5B5B/");  
 	         intent.putExtra("order", CaiJiInputActivity.this.ordermsg);
 	         intent.putExtra("Comm", "01");
 	         intent.setClass(CaiJiInputActivity.this, BtXiMeiService.class);
 	         CaiJiInputActivity.this.startService(CaiJiInputActivity.this.intent);
 	         
 	         intent.setClass(CaiJiInputActivity.this, BackSingleCBActivity.class);
 	         CaiJiInputActivity.this.startActivity(intent);         	  
          }
        //传输（无需组网）表号
          if (CaiJiInputActivity.this.caijitype.equals("nozwcsbh"))
          {
            intent.putExtra("datatype", caijitype);
            intent.putExtra("databasename", databasename);
            intent.putExtra("target", target);
            intent.putExtra("jzqflag", inputstr);
            intent.putStringArrayListExtra("qbbhlist", qbbhlist);
            intent.setClass(CaiJiInputActivity.this, BackJiZhongQiActivity.class);
            CaiJiInputActivity.this.startActivity(intent);
          }
          return;
        }
        catch (Exception localException)
        {
          Toast.makeText(CaiJiInputActivity.this, "输入错误请重新输入", 0).show();
        }
      }
    });
  }

}