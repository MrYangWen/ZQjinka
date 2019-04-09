package com.ximei.tiny.chaobiao;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.tiny.gasxm.R;
import com.ximei.tiny.database.BDhelper;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.FileUtils;
import com.ximei.tiny.tools.IniReader;
import com.ximei.tiny.tools.ToDBFfile;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
/*
 * 上传表册activtiy
 * 找出ximei文件下所有的dbf文件
 * 点击可以上传
 * 
 */
public class YCbiaocelist extends Activity {

	Cursor cursor;
	FileUtils fileutil;
	ToDBFfile todbf;
	private ListView myListView;
	private String pathsdcard;
	File[] temp;
	private TextView uplogin;
	String scsj,DeleteTableStr;

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		//取消状态标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.uploadbc);
		this.fileutil = new FileUtils();
		this.pathsdcard = this.fileutil.getSDPATH();
		this.uplogin = ((TextView) findViewById(R.id.uplogin));
		this.myListView = ((ListView) findViewById(R.id.myListView));
		try {
			DeleteTableStr = new IniReader("SysSet.ini", YCbiaocelist.this).getValue("FreqSet", "DeleteTable");
			if(DeleteTableStr==null)DeleteTableStr="00";
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			DeleteTableStr="00";
			e.printStackTrace();
		}
		//遍历所有的文件
		this.temp = new File(this.pathsdcard + "XiMei/").listFiles();
		//list的点击事件
		this.myListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				//得到点击的文件名
				String bcname = (String) ((HashMap) YCbiaocelist.this.myListView.getItemAtPosition(arg2)).get("itemTitle");
				//得到上传的时间
				scsj = new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
				//得到对应的数据库名
				String databasename = bcname.substring(0, bcname.length() - 4);
				SQLiteDatabase readdb = new BDhelper(YCbiaocelist.this,databasename).getReadableDatabase();
				//查询ximeitable的数据内容
				cursor = readdb.query("ximeitable", null,null, null, null, null, null);			
				//数据内容为空
				if(cursor.getCount() == 0){
					Toast.makeText(YCbiaocelist.this, "已经上传", Toast.LENGTH_SHORT).show();
					//删除原文件和数据库
					if(DeleteTableStr.equals("01"))
				       new File(pathsdcard + "ximei/" + bcname).delete();//20161008
				   	YCbiaocelist.this.deleteDatabase(databasename);
				}else{
					//数据不为空，取出气表状态，抄表时间，气表气量，中继器flag
					todbf = new ToDBFfile();
				    String[] arrayOfString1 = new String[cursor.getCount()];
					String[] arrayOfString2 = new String[cursor.getCount()];
					String[] arrayOfString3 = new String[cursor.getCount()];
					String[] arrayOfString4 = new String[cursor.getCount()];
					String[] arrayOfString5 = new String[cursor.getCount()];
					String[] arrayOfString6 = new String[cursor.getCount()];
					
					String oldpath = pathsdcard + "ximei/" + bcname;
					String newpath = pathsdcard + "newximei/" + bcname;
					String newpath1 = pathsdcard + "alldata/" + scsj+bcname;
					int i =0;
					while (cursor.moveToNext()){
						
						arrayOfString1[i] = cursor.getString(cursor.getColumnIndex("qbztbh"))+ "信息";
						arrayOfString2[i] = cursor.getString(cursor.getColumnIndex("qbql"));
						arrayOfString3[i] = cursor.getString(cursor.getColumnIndex("cbjlid"));
						arrayOfString4[i] = cursor.getString(cursor.getColumnIndex("flag"));
						arrayOfString5[i] = cursor.getString(cursor.getColumnIndex("state"));
						arrayOfString6[i] = cursor.getString(cursor.getColumnIndex("jzqflag"));
						i++;
						
					 }
					
					try {
						//把数据库文件转为newximei下的dbf文件
						todbf.databasetoDBF(oldpath, newpath, arrayOfString1, arrayOfString2, arrayOfString3, arrayOfString4,arrayOfString5,arrayOfString6);
						todbf.databasetoDBF(oldpath, newpath1, arrayOfString1, arrayOfString2, arrayOfString3, arrayOfString4,arrayOfString5,arrayOfString6);
						YCbiaocelist.this.deleteDatabase(databasename);
						Toast.makeText(YCbiaocelist.this, "上传成功", Toast.LENGTH_SHORT).show();
						//删除原文件
						if(DeleteTableStr.equals("01"))
					       new File(oldpath).delete();  //20161008
					    YCbiaocelist.this.onStart();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Toast.makeText(YCbiaocelist.this, "上传失败", Toast.LENGTH_SHORT).show();
					}
					
				}
				
			}
			


			
			
		});
		
		
		
		}
		
	
		
	
	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if (paramInt == 4)
			finish();
		return false;
	}
	
	




	public void onStart() {
		super.onStart();
		ArrayList<HashMap<String, String>> localArrayList = new ArrayList<HashMap<String, String>>();
		
		
		if (this.temp.length == 0) {
			this.uplogin.setText("无目标表册");
			

		} else {

			this.uplogin.setText("所有表册");
			//遍历所有文件
			for (int i = 0; i < temp.length; i++) {
				HashMap<String, String> hashmap = new HashMap<String, String>();
				String bcname = temp[i].getName();
				String databasename = bcname.substring(0, bcname.length() - 4);

				BDhelper helper = new BDhelper(YCbiaocelist.this, databasename);
				SQLiteDatabase readdb = helper.getReadableDatabase();

				Cursor cursor = readdb.query("ximeitable", null, null, null,
						null, null, null);

				if (cursor.getCount() == 0) {

					hashmap.put("itemTitle", temp[i].getName());
					hashmap.put("itemContent", "已上传");

				} else {

					hashmap.put("itemTitle", temp[i].getName());
					hashmap.put("itemContent", "未上传");

				}

				localArrayList.add(hashmap);

			}
           //设置数据源
			SimpleAdapter simpleadapter = new SimpleAdapter(this,
					localArrayList, R.layout.list_item, new String[] {
							"itemTitle", "itemContent" }, new int[] {
							R.id.qbbh, R.id.qbzd });
			myListView.setAdapter(simpleadapter);
			

		}

	}
	
}	

