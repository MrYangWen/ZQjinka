package com.ximei.tiny.collector;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tiny.gasxm.R;
import com.ximei.tiny.chaobiao.TargetBCActivity;
import com.ximei.tiny.database.BDhelper;
import com.ximei.tiny.tools.ClearReportArray;
import com.ximei.tiny.tools.DBFException;
import com.ximei.tiny.tools.DBFReader;
import com.ximei.tiny.tools.FileUtils;
import com.ximei.tiny.tools.FirstSubDong;
import com.ximei.tiny.tools.SubDong;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
 * 集中器表册选择activity
 * 用ListView控件显示表册名
 * 
 */
public class CaiJiBiaoCeActivity extends Activity {
	private String BCname;
	private String[] QBBHarray;
	private String[] QBDZarray;
	private String[] QBXMarray;
	private String[] QBZTarray;
	private String[] QBcbjlid;
	private String[] QBql;
	private String[] alldong,alldanyuan,allspace;;
	private TextView biaocelogin;
	String caijitype;
	ClearReportArray clearreport;
	SubDong sub;
	FirstSubDong Firstsub;
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

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.biaoce);
		FileUtils localFileUtils = new FileUtils();

		localIntent = getIntent();
		this.clearreport = new ClearReportArray();
		sub = new SubDong();
		Firstsub=new FirstSubDong();

		this.caijitype = localIntent.getStringExtra("caijitype");
		this.overmsg = localIntent.getStringExtra("overmsg");
		this.pathsdcard = localFileUtils.getSDPATH();
		this.biaocelogin = ((TextView) findViewById(R.id.biaocelogin));
		// 得到ListView控件
		this.myListView = ((ListView) findViewById(R.id.myListView));
		ArrayList<HashMap<String, String>> localArrayList = new ArrayList<HashMap<String, String>>();
		// 把ximei文件夹下所有的的文件存入temp中
		this.temp = new File("/" + this.pathsdcard + "/XiMei").listFiles();
		// 遍历temp
		if (this.temp.length == 0) {
			this.biaocelogin.setText("无目标表册");
			this.biaocelogin.setTextSize(35.0F);

		} else {
			// 存入localArrayList中形成ListView数据源
			this.biaocelogin.setText("目标表册");
			this.biaocelogin.setTextSize(35.0F);
			for (int i = 0; i < temp.length; i++) {

				HashMap<String, String> localHashMap = new HashMap<String, String>();
				localHashMap.put("baioceTitle", this.temp[i].getName());
				localArrayList.add(localHashMap);
			}
			// 设置适配器数据源
			SimpleAdapter localSimpleAdapter = new SimpleAdapter(this,
					localArrayList, R.layout.list_biaoce,
					new String[] { "baioceTitle" },
					new int[] { R.id.biaocename });
			this.myListView.setAdapter(localSimpleAdapter);
			// 设置点击表册名的监听事件
			this.myListView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					// 得到点击表册的表册名
					String str = ((HashMap) arg0.getItemAtPosition(arg2)).get(
							"baioceTitle").toString();
					// 得到表册名的全路径
					BCname = pathsdcard + "ximei/" + str;
					int datalen=BCname.lastIndexOf("/");
					// 得到对应数据库名
					databasename = BCname.substring(datalen+1, -4+ BCname.length());
					try {
						fis = new FileInputStream(BCname);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						Toast.makeText(CaiJiBiaoCeActivity.this, "表册没找到",Toast.LENGTH_SHORT).show();
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
						alldanyuan = new String[filecount];
						allspace = new String[filecount];
						Object rowValues[];
						// 一条一条读取dbf文件放入相应的String数组中
						for (int i = 0; i < filecount; i++) {
							rowValues = reader.nextRecord();
							String qbdz = rowValues[3].toString().replaceAll(" ", "").trim();
							if (!qbdz.substring(qbdz.length() - 1,qbdz.length()).equals("号")) {

								qbdz = qbdz + "号";
							}
							qbdzlist[i] = qbdz;
							QBDZarray[i] = qbdz;
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
							dongflag = rowValues[3].toString().trim();

							// 取出栋标识
							if (dongflag.indexOf("栋") != -1){
								alldong[i] = sub.QueryDong(rowValues[3]
										.toString().replaceAll(" ", ""), "栋");
								
							}else if (dongflag.indexOf("幢") != -1){
								
								alldong[i] = sub.QueryDong(rowValues[3]
										.toString().replaceAll(" ", ""), "幢");
								// 取出单元标识
							}else if (dongflag.indexOf("单元") != -1) {

								alldanyuan[i] = sub.QueryDong(rowValues[3]
										.toString().replaceAll(" ", ""), "单元");
							     
								
								// 取出“-”标识
							}else if(dongflag.indexOf("-")!=-1){
								
								allspace[i]=Firstsub.FirstQueryDong(rowValues[3]
										.toString().replaceAll(" ", ""), "-");
							}

						}
					} catch (DBFException e) {
						// TODO Auto-generated catch block
						Toast.makeText(CaiJiBiaoCeActivity.this, "DBF文件错误",
								Toast.LENGTH_SHORT).show();

					}
					
					BDhelper dbhelper = new BDhelper(CaiJiBiaoCeActivity.this,
							databasename);
					SQLiteDatabase db = dbhelper.getWritableDatabase();
					SQLiteDatabase dbreader = dbhelper.getReadableDatabase();
					Cursor cursor = dbreader.query("ximeitable", null, null,
							null, null, null, null);
					//判断原数据库总行数和dbf文件总行数是否一致
					if(filecount!=cursor.getCount()){
						//如果不一直，删除原数据库，写入新数据
						CaiJiBiaoCeActivity.this.deleteDatabase(databasename);
						//重新写入新数据
						BDhelper dbhelper1 = new BDhelper(CaiJiBiaoCeActivity.this,
								databasename);
						SQLiteDatabase db1 = dbhelper1.getWritableDatabase();
						
						for (int i = 0; i < filecount; i++) {
							db1.execSQL("insert into ximeitable values(?,?,?,?,?,?,?,?,?)",
									new String[] { QBBHarray[i], QBcbjlid[i],QBDZarray[i], QBZTarray[i],QBXMarray[i], QBql[i],"", "", "" });
						}
						File file = new File(pathsdcard + "newximei/" + str);
						// 判断newximei下是否存在表册
						if (file.exists()) {

							try {
								fis1 = new FileInputStream(pathsdcard + "newximei/" + str);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								Toast.makeText(CaiJiBiaoCeActivity.this,"表册没找到", Toast.LENGTH_SHORT).show();
							}
							DBFReader reader1;
							try {
								reader1 = new DBFReader(fis1);

								reader1.setCharactersetName("gb2312");
								filecount = reader1.getRecordCount();

								Object rowValues[];
								// 如果存在读取第一个字段气表表号和第19个字段中继器标示
								for (int i = 0; i < filecount; i++) {
									rowValues = reader1.nextRecord();

									String qbbh = rowValues[0].toString().trim();
									String flag = rowValues[18].toString().trim();
									ContentValues Values = new ContentValues();
									Values.put("flag", flag);
									db.update("ximeitable", Values, "qbbh=?",new String[] { qbbh });

								}
								db.close();
							} catch (DBFException e) {
								// TODO Auto-generated catch block
								Toast.makeText(CaiJiBiaoCeActivity.this,"DBF文件错误", Toast.LENGTH_SHORT).show();
							}

						}

					}

					localIntent.putExtra("caijitype", caijitype);
					localIntent.putExtra("databasename", databasename);
					localIntent.putExtra("overmsg", overmsg);
                   //跳转到组网测试activity
					if (caijitype.equals("caijizwcs")) {
                      if(alldong[0]!=null){
                    	// 对气表地址alldong数组排序
      					Arrays.sort(alldong);
      					// 去掉重复的转成list
      					list = clearreport.ClearReport(alldong);
						localIntent.putExtra("alldong", list);
						localIntent.putExtra("qbdz", qbdzlist);
						localIntent.setClass(CaiJiBiaoCeActivity.this,ZwcsTabActivity.class);
						CaiJiBiaoCeActivity.this.startActivity(localIntent);
                      }else if(alldanyuan[0]!=null){
                    	// 对气表地址alldong数组排序
      					Arrays.sort(alldanyuan);
      					// 去掉重复的转成list
      					list = clearreport.ClearReport(alldanyuan);
                    	localIntent.putExtra("danyuansing", list);
  						localIntent.putExtra("qbdz", qbdzlist);
  						localIntent.setClass(CaiJiBiaoCeActivity.this,
  								ZwcsTabActivity.class);
  						CaiJiBiaoCeActivity.this.startActivity(localIntent); 
                    	  
                    	  
                      }else if(allspace[0]!=null){
                    	// 对气表地址alldong数组排序
        				Arrays.sort(allspace);
        					// 去掉重复的转成list
        				list = clearreport.ClearReport(allspace);
                      	localIntent.putExtra("alldong", list);
    					localIntent.putExtra("qbdz", qbdzlist);
    					//localIntent.putExtra("datatype", caijitype);
    					localIntent.setClass(CaiJiBiaoCeActivity.this,ZwcsTabActivity.class);
    					CaiJiBiaoCeActivity.this.startActivity(localIntent);  
                      }

					}
					//挑转到组网统计activity
					if (caijitype.equals("caijizwtj")) {
						localIntent.putExtra("overmsg", overmsg);
						localIntent.setClass(CaiJiBiaoCeActivity.this,ShowZWInFoActivity.class);
						CaiJiBiaoCeActivity.this.startActivity(localIntent);
					}
                   
					if ( caijitype.equals("caijibh")|| caijitype.equals("caijidata")|| caijitype.equals("caijicsbh")) {
						localIntent.putExtra("overmsg", overmsg);
						localIntent.setClass(CaiJiBiaoCeActivity.this,CaiJiInputActivity.class);
						CaiJiBiaoCeActivity.this.startActivity(localIntent);
					}
					 //无需组网传输表号activity
					if (caijitype.equals("nozwcsbh")){
                      if(alldong[0]!=null){
                    	// 对气表地址alldong数组排序
      					Arrays.sort(alldong);
      					// 去掉重复的转成list
      					list = clearreport.ClearReport(alldong);
						localIntent.putExtra("alldong", list);
						localIntent.putExtra("qbdz", qbdzlist);
						localIntent.putExtra("overmsg", overmsg);
						localIntent.setClass(CaiJiBiaoCeActivity.this,CaiJiTabActivity.class);
						CaiJiBiaoCeActivity.this.startActivity(localIntent);
                      }else if(alldanyuan[0]!=null){
                    	// 对气表地址alldong数组排序
      					Arrays.sort(alldanyuan);
      					// 去掉重复的转成list
      					list = clearreport.ClearReport(alldanyuan);
                    	localIntent.putExtra("danyuansing", list);
  						localIntent.putExtra("qbdz", qbdzlist);
  						localIntent.putExtra("overmsg", overmsg);
						localIntent.setClass(CaiJiBiaoCeActivity.this,CaiJiTabActivity.class);
						CaiJiBiaoCeActivity.this.startActivity(localIntent);      	  
                      }else if(allspace[0]!=null){
                    	// 对气表地址alldong数组排序
        				Arrays.sort(allspace);
        					// 去掉重复的转成list
        				list = clearreport.ClearReport(allspace);
                      	localIntent.putExtra("alldong", list);
    					localIntent.putExtra("qbdz", qbdzlist);
    					localIntent.putExtra("overmsg", overmsg);
						localIntent.setClass(CaiJiBiaoCeActivity.this,CaiJiTabActivity.class);
						CaiJiBiaoCeActivity.this.startActivity(localIntent);  
                      }

					}
					
				}

			});

		}
	}
}