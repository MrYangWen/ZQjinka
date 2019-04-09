package com.ximei.tiny.chaobiao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tiny.gasxm.R;
import com.ximei.tiny.database.BDhelper;
import com.ximei.tiny.tools.ClearReportArray;
import com.ximei.tiny.tools.Containstr;
import com.ximei.tiny.tools.DBFException;
import com.ximei.tiny.tools.DBFReader;
import com.ximei.tiny.tools.FileUtils;
import com.ximei.tiny.tools.SubDong;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
 * 选择相应表册activity
 * 用GridView控件显示表册名
 * 
 * 
 * 
 */
public class TargetBCActivity01 extends Activity {
	private String[] QBBHarray;
	private String[] QBDZarray;
	private String[] QBXMarray;
	private String[] QBZTarray;
	private String[] QBcbjlid;
	private String[] QBql;
	private String[] alldong;
	String bcpath;
	String cbtype;
	ClearReportArray clearreport;
	Containstr contain;
	SubDong sub;
	String databasename,biaotype;
	String dongflag;
	int filecount;
	FileUtils fileutil;
	private TextView hint;
	Intent intent;
	private ArrayList<String> list;
	String overmsg;
	private ProgressDialog progressDialog;
	long qbbhint;
	private String[] qbdzlist;
	File[] temp;
	FileInputStream fis;
	FileInputStream fis1;
	private int xingshu=0;
	private RadioButton oldmeter, newmeter;
	private RadioGroup metertype;

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.groupcbfs);
		// 得到GridView控件
		GridView localGridView = (GridView) findViewById(R.id.gridview);
		hint = ((TextView) findViewById(R.id.cbfshint));
		hint.setText("选择目标表册");
		intent = getIntent();
		fileutil = new FileUtils();
		clearreport = new ClearReportArray();
		contain = new Containstr();
		sub = new SubDong();
		overmsg = this.intent.getStringExtra("overmsg");
		cbtype = this.intent.getStringExtra("cbtype");
		ArrayList<HashMap<String, Object>> localArrayList = new ArrayList<HashMap<String, Object>>();
		// 把ximei文件夹下所有的的文件存入temp中
		this.temp = new File("/" + this.fileutil.getSDPATH() + "/XiMei")
				.listFiles();
		// 遍历temp
		if (this.temp.length == 0) {
			this.hint.setText("没有数据表册");
		} else {
			// 存入localArrayList中形成GridView数据源
			for (int i = 0; i < this.temp.length; i++) {
				HashMap<String, Object> localHashMap = new HashMap<String, Object>();
				localHashMap.put("ItemImage", R.drawable.bc);
				localHashMap.put("ItemText", this.temp[i].getName());
				localArrayList.add(localHashMap);

			}

		}
		
		// 新增单选框
		oldmeter = (RadioButton) findViewById(R.id.oldmeter);
		newmeter = (RadioButton) findViewById(R.id.newmeter);
		metertype = (RadioGroup) findViewById(R.id.metertype);
		biaotype = "mrmeter";

		metertype.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (oldmeter.getId() == checkedId) {
					biaotype = "oldmeter";

				} else if (newmeter.getId() == checkedId) {
					biaotype = "newmeter";

				}

			}
		});
		
		
		// 设置适配器数据源
		localGridView.setAdapter(new SimpleAdapter(this, localArrayList,
				R.layout.gridview_meun,
				new String[] { "ItemImage", "ItemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText }));
		// 设置监听器
		localGridView.setOnItemClickListener(new ItemClickListener());

	}

	// 设置点击表册名的监听事件
	class ItemClickListener implements AdapterView.OnItemClickListener {

		public void onItemClick(AdapterView<?> paramAdapterView,
				View paramView, int paramInt, long paramLong) {
			try{
			// 得到点击表册的表册名
			String str = ((HashMap) paramAdapterView
					.getItemAtPosition(paramInt)).get("ItemText").toString();
			// 得到表册名的全路径
			bcpath = (TargetBCActivity01.this.fileutil.getSDPATH() + "ximei/" + str);
			//得到西美目录下"/"表册前的数据长度
			
			int datalen=bcpath.lastIndexOf("/");
			// 得到对应数据库名
			databasename = TargetBCActivity01.this.bcpath.substring(datalen+1, -4
					+ TargetBCActivity01.this.bcpath.length());

			try {
				fis = new FileInputStream(bcpath);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Toast.makeText(TargetBCActivity01.this, "表册没找到",
						Toast.LENGTH_SHORT).show();
			}
			DBFReader reader;
			try {
				reader = new DBFReader(fis);

				reader.setCharactersetName("gb2312");
				filecount = reader.getRecordCount();
				QBDZarray = new String[filecount];
				qbdzlist = new String[filecount];
				QBBHarray = new String[filecount];
				QBcbjlid = new String[filecount];
				QBZTarray = new String[filecount];
				QBXMarray = new String[filecount];
				QBql = new String[filecount];
				alldong = new String[filecount];

				Object rowValues[];
                //一条一条读取dbf文件放入相应的String数组中
				for (int i = 0; i < filecount; i++) {
					rowValues = reader.nextRecord();
					String qbdz = rowValues[3].toString();//.replaceAll(" ", "").trim();
					
					qbdzlist[i] = qbdz;
					QBDZarray[i] = rowValues[3].toString().replaceAll(" ", "");
					String qbbh = rowValues[0].toString().trim();
					if (qbbh.equals("")||qbbh.equals("null")) {
						QBBHarray[i] = "";
					} else {
						qbbhint = Integer.parseInt(qbbh);
						QBBHarray[i] = String.valueOf(qbbhint);
					}
					QBcbjlid[i] = rowValues[6].toString().trim();
					QBZTarray[i] = rowValues[7].toString().trim();
					QBXMarray[i] = rowValues[9].toString().trim();
					QBql[i] = rowValues[4].toString().trim();
					xingshu=i;
				
				}
			} catch (DBFException e) {
				// TODO Auto-generated catch block
				Toast.makeText(TargetBCActivity01.this, "DBF文件错误",
						Toast.LENGTH_SHORT).show();
				Log.e("test", e.toString());

			}
			

			BDhelper dbhelper = new BDhelper(TargetBCActivity01.this,
					databasename);
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			SQLiteDatabase dbreader = dbhelper.getReadableDatabase();
			Cursor cursor = dbreader.query("ximeitable", null, null, null,
					null, null, null);
			//判断原数据库总行数和dbf文件总行数是否一致
			if (cursor.getCount() != filecount) {
				//如果不一直，删除原数据库，写入新数据
				TargetBCActivity01.this.deleteDatabase(databasename);
				//重新写入新数据
				BDhelper dbhelper1 = new BDhelper(TargetBCActivity01.this,
						databasename);
				SQLiteDatabase db1 = dbhelper1.getWritableDatabase();
				
				for (int i = 0; i < filecount; i++) {
					db1.execSQL(
							"insert into ximeitable values(?,?,?,?,?,?,?,?,?)",
							new String[] { QBBHarray[i], QBcbjlid[i],
									QBDZarray[i], QBZTarray[i], QBXMarray[i],
									QBql[i], "", "","" });
				}

			

			}

			Intent localIntent = new Intent();
			localIntent.putStringArrayListExtra("alldong", list);
			localIntent.putExtra("overmsg", overmsg);
			localIntent.putExtra("qbdz", qbdzlist);
			localIntent.putExtra("databasename", databasename);
			localIntent.putExtra("metertype", biaotype);
			localIntent.putExtra("cbtype", cbtype);
			localIntent.setClass(TargetBCActivity01.this,
					GroupCBTabActivity.class);
			TargetBCActivity01.this.startActivity(localIntent);
			
			}catch(Exception e){
				Toast.makeText(TargetBCActivity01.this, "抄表册字段格式错误"+String.valueOf(xingshu),
						Toast.LENGTH_SHORT).show();
				Log.e("test", e.toString());
				
			}

		}
	}
}
