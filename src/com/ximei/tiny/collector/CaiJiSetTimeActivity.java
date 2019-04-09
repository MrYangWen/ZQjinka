package com.ximei.tiny.collector;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.tiny.gasxm.R;
import com.ximei.tiny.backinfoview.BackCaiJiInFoActivity;
import com.ximei.tiny.backinfoview.BackSingleCBActivity;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.ToHexStr;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
/*
 * 设置时间activity
 * GridView控件
 * 
 */
public class CaiJiSetTimeActivity extends Activity
{
  String CRCmsg;
  CRC crc;
  String headmsg;
  private TextView hint;
  Intent intent;
  String ordermsg;
  String overmsg;
  String target;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    //取消标题状态栏
    requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(1024, 1024);
    setContentView(R.layout.groupcbfs);
    //得到GridView控件
    GridView localGridView = (GridView)findViewById(R.id.gridview);
    this.hint = ((TextView)findViewById(R.id.cbfshint));
    this.hint.setText("选择设置时间");
    this.intent = getIntent();
    this.crc = new CRC();
    this.overmsg = this.intent.getStringExtra("overmsg");
    this.target = this.intent.getStringExtra("target");
    this.headmsg = "5A5A00FE02";
    //把'设置系统时间和设置抄表时间加入'localArrayList形成数据源
    ArrayList<HashMap<String,Object>> localArrayList = new ArrayList<HashMap<String,Object>>();
    HashMap<String,Object> localHashMap1 = new HashMap<String,Object>();
    localHashMap1.put("ItemImage", R.drawable.szxtsj);
    localHashMap1.put("ItemText", "设置系统时间");
    HashMap<String,Object> localHashMap2 = new HashMap<String,Object>();
    localHashMap2.put("ItemImage", R.drawable.szcbsj);
    localHashMap2.put("ItemText", "设置抄表时间");
    HashMap<String,Object> localHashMap3 = new HashMap<String,Object>();
    localHashMap3.put("ItemImage", R.drawable.qjtz);
    localHashMap3.put("ItemText", "查询当前时间");
    localArrayList.add(localHashMap1);
    localArrayList.add(localHashMap2);
    localArrayList.add(localHashMap3);
    //设置适配器数据源
    localGridView.setAdapter(new SimpleAdapter(this, localArrayList, R.layout.gridview_meun, new String[] { "ItemImage", "ItemText" }, new int[] { R.id.ItemImage, R.id.ItemText }));
    localGridView.setOnItemClickListener(new ItemClickListener());
  }
  //监听点击事件
  class ItemClickListener implements OnItemClickListener
  {
    
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      String str1 = ((HashMap)paramAdapterView.getItemAtPosition(paramInt)).get("ItemText").toString();
      if (str1.equals("设置系统时间"))
      {
        //String ss= new StringBuilder(String.valueOf(overmsg)).append("5B5B/").toString();
        String str2 = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
//        String str3 = new ToHexStr().toHexStr(str2);
        CaiJiSetTimeActivity.this.CRCmsg = ("09" + target + "01" + "07" + "00" + str2).toUpperCase();
        CaiJiSetTimeActivity.this.ordermsg = (headmsg + CRCmsg + crc.CRC_CCITT(1, CRCmsg).toUpperCase())+overmsg+"5B5B/";
        CaiJiSetTimeActivity.this.intent.putExtra("order", CaiJiSetTimeActivity.this.ordermsg);
        CaiJiSetTimeActivity.this.intent.setClass(CaiJiSetTimeActivity.this, BtXiMeiService.class);
        CaiJiSetTimeActivity.this.startService(CaiJiSetTimeActivity.this.intent);
        CaiJiSetTimeActivity.this.intent.setClass(CaiJiSetTimeActivity.this, BackCaiJiInFoActivity.class);
        CaiJiSetTimeActivity.this.startActivity(CaiJiSetTimeActivity.this.intent);
      }
      if (str1.equals("设置抄表时间"))
      {
        CaiJiSetTimeActivity.this.intent.putExtra("target", CaiJiSetTimeActivity.this.target);
        CaiJiSetTimeActivity.this.intent.putExtra("overmsg", CaiJiSetTimeActivity.this.overmsg);
        CaiJiSetTimeActivity.this.intent.setClass(CaiJiSetTimeActivity.this, CaiJiSetCBTimeActivity.class);
        CaiJiSetTimeActivity.this.startActivity(CaiJiSetTimeActivity.this.intent);
      }
      if (str1.equals("查询当前时间"))
      {
    	  CaiJiSetTimeActivity.this.CRCmsg = ("09" + target + "07" + "01" + "00").toUpperCase();
          CaiJiSetTimeActivity.this.ordermsg = (headmsg + CRCmsg + crc.CRC_CCITT(1, CRCmsg).toUpperCase())+overmsg+"5B5B/";
          CaiJiSetTimeActivity.this.intent.putExtra("order", CaiJiSetTimeActivity.this.ordermsg);
          CaiJiSetTimeActivity.this.intent.setClass(CaiJiSetTimeActivity.this, BtXiMeiService.class);
          CaiJiSetTimeActivity.this.intent.putExtra("Comm", "00");
          CaiJiSetTimeActivity.this.startService(CaiJiSetTimeActivity.this.intent);
          CaiJiSetTimeActivity.this.intent.setClass(CaiJiSetTimeActivity.this, BackSingleCBActivity.class);
          CaiJiSetTimeActivity.this.startActivity(CaiJiSetTimeActivity.this.intent);
      }
    }
  }
}