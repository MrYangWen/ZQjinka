package com.ximei.tiny.chaobiao;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tiny.gasxm.R;
import com.ximei.tiny.tools.FileUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DelBiaoCeActivity extends Activity
{
  Cursor cursor;
  FileUtils fileutil;
  private ListView myListView;
  private String pathsdcard;
  File[] temp;
  private TextView uplogin;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(1024, 1024);
    setContentView(R.layout.uploadbc);
    this.fileutil = new FileUtils();
    this.pathsdcard = this.fileutil.getSDPATH();
    this.uplogin = ((TextView)findViewById(R.id.uplogin));
  }

  protected void onStart()
  {
    super.onStart();
    this.temp = new File(this.pathsdcard + "XiMei/").listFiles();
    this.myListView = ((ListView)findViewById(R.id.myListView));
    ArrayList<HashMap<String,String>> localArrayList = new ArrayList<HashMap<String,String>>();
    if (this.temp.length == 0){
      this.uplogin.setText("无目标表册");
    }else{
    	 uplogin.setText("所有表册");
    	 for (int i = 0; i < this.temp.length; i++)
         {
           HashMap<String,String> localHashMap1 = new HashMap<String,String>();
           localHashMap1.put("itemTitle", this.temp[i].getName());
           localHashMap1.put("itemContent", "删除");
           localArrayList.add(localHashMap1);
           
         }
    	 
    	 HashMap<String,String> localHashMap2 = new HashMap<String,String>();
         localHashMap2.put("itemTitle", "系统数据");
         localHashMap2.put("itemContent", "删除");
         localArrayList.add(localHashMap2);
    }
   
      
    SimpleAdapter simpleadapter = new SimpleAdapter(this,
			localArrayList, R.layout.list_item, new String[] {
					"itemTitle", "itemContent" }, new int[] {
					R.id.qbbh, R.id.qbzd });
	myListView.setAdapter(simpleadapter);
      this.myListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
        public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
        {
          String str1 = (String)((HashMap)DelBiaoCeActivity.this.myListView.getItemAtPosition(paramAnonymousInt)).get("itemTitle");
          String str2 = str1.substring(0, -4 + str1.length());
          if (str1.equals("系统数据"))
          {
            DelBiaoCeActivity.this.deleteDatabase("userdb");
            new File(DelBiaoCeActivity.this.pathsdcard + "XiMei/" + str1).delete();
            DelBiaoCeActivity.this.onStart();
           
          }
          DelBiaoCeActivity.this.deleteDatabase(str2);
          new File(DelBiaoCeActivity.this.pathsdcard + "XiMei/" + str1).delete();
          DelBiaoCeActivity.this.onStart();
        }
      });
     
    
     
   
  }
}
