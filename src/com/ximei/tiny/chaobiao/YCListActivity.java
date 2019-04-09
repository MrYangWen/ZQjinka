package com.ximei.tiny.chaobiao;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.tiny.gasxm.R;
import com.ximei.tiny.database.BDhelper;

import java.util.ArrayList;
import java.util.HashMap;
/*
 * 已抄信息activity
 * 只是统计已抄未抄数量
 * 显示已抄气量
 */
public class YCListActivity extends Activity {
	String databasename,cbfangshi;
	private ListView myListView;
	BDhelper bdhelper;
	SQLiteDatabase readerdb;
	SQLiteDatabase writerdb;
	int i, j;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.mylist);
		this.myListView = ((ListView) findViewById(R.id.myListView));
		i = 0;
		j = 0;
		// 查询数据库得到所有未抄和已抄数量
		databasename = getIntent().getStringExtra("databasename");
		cbfangshi = getIntent().getStringExtra("cbfangshi");
		bdhelper = new BDhelper(YCListActivity.this, databasename);
		readerdb = bdhelper.getReadableDatabase();
		writerdb = bdhelper.getWritableDatabase();
		Cursor cursor = readerdb.query("ximeitable", null, null, null, null,null, null);
 	       if(cbfangshi.equals("bcqg")){
 	  		while (cursor.moveToNext()) {
 	  			String qbzt = cursor.getString(cursor.getColumnIndex("state"));
 	  			if (qbzt.equals("开阀")) {
 	  				i++;
 	  			}
 	  			if (qbzt.equals("关阀")) {
 	  				j++;
 	  			}

 	  		}
 	         }else{
 	      	   
 	      	   while (cursor.moveToNext()) {
 	     			String qbzt = cursor.getString(cursor.getColumnIndex("qbztbh"));
 	     			if (qbzt.equals("已抄")) {
 	     				i++;
 	     			}
 	     			if (qbzt.equals("未抄")) {
 	     				j++;
 	     			}

 	     		  }
 	         } 		   	
	}

	protected void onStart() {
		super.onStart();

		Cursor cursor1 = readerdb.query("ximeitable", null, null, null, null,null, null);
		ArrayList<HashMap<String, String>> localArrayList;
			if(cbfangshi.equals("bcqg")){
				localArrayList = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> localHashMap1 = new HashMap<String, String>();
				localHashMap1.put("itemTitle", "未关阀" + String.valueOf(i) + "户");
				localHashMap1.put("itemContent", "已关阀 " + String.valueOf(j) + "户");
				localArrayList.add(localHashMap1);
				// 遍历cursor1得到未抄信息放入localArrayList形成数据源
				while (cursor1.moveToNext()) {
					String qbzt = cursor1.getString(cursor1.getColumnIndex("state"));
					String qbbh = cursor1.getString(cursor1.getColumnIndex("qbbh"));
					String qbdz = cursor1.getString(cursor1.getColumnIndex("dzms"));
					String qbql = cursor1.getString(cursor1.getColumnIndex("qbql"));
					if ((!qbbh.equals("")) && (qbzt.equals("关阀"))) {
						HashMap<String, String> localHashMap2 = new HashMap<String, String>();
						localHashMap2.put("itemTitle", "关阀成功");
						localHashMap2.put("itemContent", qbbh + qbdz);
						localArrayList.add(localHashMap2);
					}
				}
				}else{
					
					localArrayList = new ArrayList<HashMap<String, String>>();
					HashMap<String, String> localHashMap1 = new HashMap<String, String>();
					localHashMap1.put("itemTitle", "已抄" + String.valueOf(i) + "户");
					localHashMap1.put("itemContent", "未抄" + String.valueOf(j) + "户");
					localArrayList.add(localHashMap1);
					// 遍历cursor1得到未抄信息放入localArrayList形成数据源
					while (cursor1.moveToNext()) {
						String qbzt = cursor1.getString(cursor1.getColumnIndex("qbztbh"));
						String qbbh = cursor1.getString(cursor1.getColumnIndex("qbbh"));
						String qbdz = cursor1.getString(cursor1.getColumnIndex("dzms"));
						String qbql = cursor1.getString(cursor1.getColumnIndex("qbql"));

						if ((!qbbh.equals("")) && (qbzt.equals("已抄"))) {
							HashMap<String, String> localHashMap2 = new HashMap<String, String>();
							localHashMap2.put("itemTitle", qbql);
							localHashMap2.put("itemContent", qbbh + qbdz);
							localArrayList.add(localHashMap2);
						}
					}	
				}
				// 设置适配器和数据源
				SimpleAdapter localSimpleAdapter = new SimpleAdapter(this,
						localArrayList, R.layout.list_item, new String[] { "itemTitle",
								"itemContent" }, new int[] { R.id.qbbh, R.id.qbzd });
				this.myListView.setAdapter(localSimpleAdapter);
				
				this.myListView.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
					}

				});			
	}
}
