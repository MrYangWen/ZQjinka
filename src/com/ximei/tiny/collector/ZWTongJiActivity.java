package com.ximei.tiny.collector;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.tiny.gasxm.R;
import com.ximei.tiny.database.BDhelper;

import java.util.ArrayList;
import java.util.HashMap;
/*
 * 组网统计显示activity
 * 
 */
public class ZWTongJiActivity extends Activity
{
  String databasename;
  String flagtype;
  private ListView myListView;
  int total;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    //取消标题状态栏
    requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(1024, 1024);
    setContentView(R.layout.mylist);
    Intent localIntent = getIntent();
    this.databasename = localIntent.getStringExtra("databasename");
    //得到组网统计类型flagtype
    this.flagtype = localIntent.getStringExtra("flagtype");
    this.total = 0;
    SQLiteDatabase readerdb = new BDhelper(this, this.databasename).getReadableDatabase();
    //根据flagtype查出对应的数量
    Cursor cursor = readerdb.query("ximeitable", null, null, null, null, null, this.flagtype);
    while(cursor.moveToNext()){
  	  
  	    String str1 = cursor.getString(cursor.getColumnIndex("qbbh"));
        String str3 = cursor.getString(cursor.getColumnIndex(this.flagtype));
        if ((!str1.equals("")) && (!str3.equals("")))
        {
        	total++;
        }
  	   
    }
  }

  protected void onStart()
  {
    super.onStart();
    this.myListView = ((ListView)findViewById(R.id.myListView));
    ArrayList<HashMap<String, String>> localArrayList = new ArrayList<HashMap<String, String>>();
    SQLiteDatabase localSQLiteDatabase = new BDhelper(this, this.databasename).getReadableDatabase();
    Cursor localCursor1 = localSQLiteDatabase.query("ximeitable", null, null, null, null, null, this.flagtype);
    
   
    
      HashMap<String,String> localHashMap1 = new HashMap<String,String>();
      localHashMap1.put("itemTitle", "所属中继器");
      localHashMap1.put("itemContent", "组网成功" + String.valueOf(this.total) + "只" + "/表号地址");
      localArrayList.add(localHashMap1);
      
      while(localCursor1.moveToNext()){
    	  
    	  String str1 = localCursor1.getString(localCursor1.getColumnIndex("qbbh"));
          String str2 = localCursor1.getString(localCursor1.getColumnIndex("dzms"));
          String str3 = localCursor1.getString(localCursor1.getColumnIndex(this.flagtype));
          if ((!str1.equals("")) && (!str3.equals("")))
          {
            HashMap<String,String> localHashMap2 = new HashMap<String,String>();
            localHashMap2.put("itemTitle", str3);
            localHashMap2.put("itemContent", str1 + str2);
            localArrayList.add(localHashMap2);
          }
    	   
      }
      SimpleAdapter localSimpleAdapter = new SimpleAdapter(this, localArrayList, R.layout.list_item, new String[] { "itemTitle", "itemContent" }, new int[] { R.id.qbbh, R.id.qbzd });
      this.myListView.setAdapter(localSimpleAdapter);
      
    
  }
}
