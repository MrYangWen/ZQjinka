package com.ximei.tiny.collector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.tiny.gasxm.R;
import com.ximei.tiny.database.BDhelper;
import com.ximei.tiny.tools.ClearReportArray;
import com.ximei.tiny.tools.DBFException;
import com.ximei.tiny.tools.DBFReader;
import com.ximei.tiny.tools.FileUtils;
import com.ximei.tiny.tools.SubDong;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/*
 * 
 * 
 * 没有用到此activity
 */
public class CaiJiAddbhActivity extends Activity{
	
	private String BCname;
	private String[] QBBHarray;
	private String[] QBDZarray;
	private String[] QBXMarray;
	private String[] QBZTarray;
	private String[] QBcbjlid;
	private String[] QBql;
	private String[] alldong;
	private TextView biaocelogin;
	String caijitype;
	ClearReportArray clearreport;
	SubDong subdong;
	private String databasename;

	String dongflag;
	private int filecount;
	private ArrayList<String> list;
	ListView myListView;
	String overmsg;
	private String pathsdcard;
	String qbbh;
	int qbbhint;
	String qbdz;
	private String[] qbdzlist;
	File[] temp;
	FileInputStream fis;
	FileInputStream fis1;
	Intent localIntent;
	FileUtils localFileUtils;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.biaoce);
		 localFileUtils = new FileUtils();

	    localIntent = getIntent();
		this.clearreport = new ClearReportArray();
		this.subdong = new SubDong();
		this.caijitype = localIntent.getStringExtra("caijitype");
		this.overmsg = localIntent.getStringExtra("overmsg");
		this.pathsdcard = localFileUtils.getSDPATH();
		this.biaocelogin = ((TextView) findViewById(R.id.biaocelogin));
		this.myListView = ((ListView) findViewById(R.id.myListView));
		ArrayList<HashMap<String, String>> localArrayList = new ArrayList<HashMap<String, String>>();

		this.temp = new File("/" + this.pathsdcard + "/XiMei").listFiles();
		if (this.temp.length == 0) {
			this.biaocelogin.setText("无目标表册");
			this.biaocelogin.setTextSize(35.0F);

		} else {
			this.biaocelogin.setText("目标表册");
			this.biaocelogin.setTextSize(35.0F);
			for (int i = 0; i < temp.length; i++) {

				HashMap<String, String> localHashMap = new HashMap<String, String>();
				localHashMap.put("baioceTitle", this.temp[i].getName());
				localArrayList.add(localHashMap);
			}

			SimpleAdapter localSimpleAdapter = new SimpleAdapter(this,
					localArrayList, R.layout.list_biaoce,
					new String[] { "baioceTitle" },
					new int[] { R.id.biaocename });
			this.myListView.setAdapter(localSimpleAdapter);
			this.myListView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub

					String str = ((HashMap) arg0.getItemAtPosition(arg2)).get("baioceTitle").toString();
					
                   
					
					
					BCname = pathsdcard + "ximei/" + str;
					databasename = BCname.substring(18, -4 + BCname.length());

					try {
						fis = new FileInputStream(BCname);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						Toast.makeText(CaiJiAddbhActivity.this, "表册没找到",
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

						for (int i = 0; i < filecount; i++) {
							rowValues = reader.nextRecord();
							String qbdz = rowValues[3].toString()
									.replaceAll(" ", "").trim();
							if (!qbdz.substring(qbdz.length() - 1,
									qbdz.length()).equals("号")) {

								qbdz = qbdz + "号";
							}

							qbdzlist[i] = qbdz;
							QBDZarray[i] = rowValues[3].toString().replaceAll(
									" ", "");
							String qbbh = rowValues[0].toString().trim();
							if (qbbh.equals("")) {
								QBBHarray[i] = "";
							} else {
								qbbhint = Integer.parseInt(qbbh);
								QBBHarray[i] = String.valueOf(qbbhint);
							}
							QBcbjlid[i] = rowValues[6].toString().trim();
							QBZTarray[i] = rowValues[7].toString().trim();
							QBXMarray[i] = rowValues[9].toString().trim();
							QBql[i] = rowValues[4].toString().trim();
							dongflag = rowValues[3].toString().trim();

							if (dongflag.indexOf("栋") != -1)
								alldong[i] = subdong.QueryDong(rowValues[3]
										.toString().replaceAll(" ", ""), "栋");
							if (dongflag.indexOf("幢") != -1)
								alldong[i] = subdong.QueryDong(rowValues[3]
										.toString().replaceAll(" ", ""), "幢");

						}
					} catch (DBFException e) {
						// TODO Auto-generated catch block
						Toast.makeText(CaiJiAddbhActivity.this, "DBF文件错误",
								Toast.LENGTH_SHORT).show();

					}
					Arrays.sort(alldong);
					list = clearreport.ClearReport(alldong);

					BDhelper dbhelper = new BDhelper(CaiJiAddbhActivity.this,
							databasename);
					SQLiteDatabase db = dbhelper.getWritableDatabase();
					SQLiteDatabase dbreader = dbhelper.getReadableDatabase();
					Cursor cursor = dbreader.query("ximeitable", null, null,
							null, null, null, null);
					if (cursor.getCount() == 0) {
						for (int i = 0; i < filecount; i++)
						{
							db.execSQL(
									"insert into ximeitable values(?,?,?,?,?,?,?,?)",
									new String[] { QBBHarray[i], QBcbjlid[i],
											QBDZarray[i], QBZTarray[i],
											QBXMarray[i], QBql[i], "", "" });
						}
						File file = new File(pathsdcard + "newximei/" + str);
						if(file.exists()){
							
							try {
								 fis1 = new FileInputStream(pathsdcard + "newximei/" + str);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								Toast.makeText(CaiJiAddbhActivity.this, "表册没找到",
										Toast.LENGTH_SHORT).show();
							}
							DBFReader reader1;
							try {
								reader1 = new DBFReader(fis1);

								reader1.setCharactersetName("gb2312");
								filecount = reader1.getRecordCount();
								

								Object rowValues[];
                               
								for (int i = 0; i < filecount; i++) {
									rowValues = reader1.nextRecord();
									
									String qbbh = rowValues[0].toString().trim();
									String flag = rowValues[18].toString().trim();
                                    ContentValues Values = new ContentValues();
                                    Values.put("flag", flag);
                                    db.update("ximeitable", Values, "qbbh=?", new String[]{qbbh});
									

								}
							} catch (DBFException e) {
								// TODO Auto-generated catch block
								Toast.makeText(CaiJiAddbhActivity.this, "DBF文件错误",
										Toast.LENGTH_SHORT).show();

							}
							
							
						}
						
					}
					 if(caijitype.equals("caijizwcs")){
	                	   
	                    	localIntent.putExtra("caijitype", caijitype);
							localIntent.putExtra("databasename", databasename);
							localIntent.putExtra("overmsg", overmsg);
	                    	localIntent.putExtra("alldong", list);
	                    	localIntent.putExtra("qbdz", qbdzlist);
	                    	localIntent.setClass(CaiJiAddbhActivity.this, CaiJiDongActivity.class);
	                    	CaiJiAddbhActivity.this.startActivity(localIntent);
	                    	
							
							
							
						}
						
					
				}

			});

		}
	}
}