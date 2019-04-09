package com.ximei.tiny.chaobiao;

import android.R.string;
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
import com.ximei.tiny.tools.FirstSubDong;
import com.ximei.tiny.tools.GetmsgID;
import com.ximei.tiny.tools.SubDong;
import com.ximei.tiny.tools.ToInverted;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/*
 * 选择相应表册activity
 * 用GridView控件显示表册名
 * 
 * 
 * 
 */
public class TargetBCActivity extends Activity {
	private String[] QBBHarray;
	private String[] QBDZarray;
	private String[] QBXMarray;
	private String[] QBZTarray;
	private String[] QBcbjlid;
	private String[] QBql;
	private String[] alldong, alldanyuan,allspace,allAddrNum;
	String bcpath;
	String cbtype;
	ClearReportArray clearreport;
	Containstr contain;
	SubDong sub;
	FirstSubDong Firstsub;
	String databasename;
	String dongflag;
	int filecount,errorflg, CurrentRow=0;
	FileUtils fileutil;
	private TextView hint;
	Intent intent;
	private ArrayList<String> list;
	String overmsg,biaotype;
	private ProgressDialog progressDialog;
	long qbbhint;
	private String[] qbdzlist;
	File[] temp;
	FileInputStream fis;
	FileInputStream fis1;
	BDhelper dbhelper;
	SQLiteDatabase dbwriter;
	SQLiteDatabase dbreader;
	private RadioButton oldmeter, newmeter;
	private RadioGroup metertype;
	private String StrAddrType;

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.groupcbfs);
		// 得到GridView控件
		GridView localGridView = (GridView) findViewById(R.id.gridview);
		hint = ((TextView) findViewById(R.id.cbfshint));
		errorflg=0;
		hint.setText("选择目标表册");
		intent = getIntent();
		fileutil = new FileUtils();
		clearreport = new ClearReportArray();
		contain = new Containstr();
		sub = new SubDong();
		Firstsub=new FirstSubDong();
		overmsg = intent.getStringExtra("overmsg");
		cbtype = intent.getStringExtra("cbtype");
		ArrayList<HashMap<String, Object>> localArrayList = new ArrayList<HashMap<String, Object>>();
		// 把ximei文件夹下所有的的文件存入temp中
		Log.e("test", fileutil.getSDPATH());
		this.temp = new File("/" + fileutil.getSDPATH() + "/ximei").listFiles();
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
		localGridView.setAdapter(new SimpleAdapter(this, localArrayList,R.layout.gridview_meun,
				new String[] { "ItemImage", "ItemText" }, new int[] {R.id.ItemImage, R.id.ItemText }));
		// 设置监听器
		localGridView.setOnItemClickListener(new ItemClickListener());
	}
	// 设置点击表册名的监听事件
	class ItemClickListener implements AdapterView.OnItemClickListener {
		public void onItemClick(AdapterView<?> paramAdapterView,View paramView, int paramInt, long paramLong) {
			// 得到点击表册的表册名
			try{
				String str = ((HashMap) paramAdapterView.getItemAtPosition(paramInt)).get("ItemText").toString();
				// 得到表册名的全路径
				bcpath = (TargetBCActivity.this.fileutil.getSDPATH() + "ximei/" + str);
				//得到西美目录下"/"表册前的数据长度			
				int datalen=bcpath.lastIndexOf("/");
				// 得到对应数据库名
				databasename = TargetBCActivity.this.bcpath.substring(datalen+1, -4+ TargetBCActivity.this.bcpath.length());	
		        try {
					fis = new FileInputStream(bcpath);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					Toast.makeText(TargetBCActivity.this, "表册未找到",Toast.LENGTH_SHORT).show();
				}
				DBFReader reader;
				GetmsgID localGetmsgID = new GetmsgID();
				errorflg=0;
				int i=0,row=0;
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
					allAddrNum=new String[filecount];
					alldanyuan = new String[filecount];
					allspace = new String[filecount];
					Object rowValues[];
					// 一条一条读取dbf文件放入相应的String数组中
					for (i = 0; i < filecount; i++,row++) 
					{
						CurrentRow=i;
//						if(i==171)
//							CurrentRow=CurrentRow;
						rowValues = reader.nextRecord();
						String qbdz = rowValues[3].toString().replaceAll(" ", "").trim();
						if (!qbdz.substring(qbdz.length() - 1,qbdz.length()).equals("号")) {
							qbdz = qbdz + "号";
						}
						qbdzlist[i] = qbdz;
						QBDZarray[i] = qbdz;
						String qbbh = rowValues[0].toString().trim();
//						if((qbbh==null)qbbh="0";
//						if((qbbh==null)||(qbbh.equals("null")))
//							qbbh="00";
//						if((qbbh.length()>8)&&(qbbh.length()<14))
//							qbbh="00";
						try {     //20170607解决表号不对应的问题
							if(localGetmsgID.CheckMeterID(qbbh)!=null)
							{
								QBBHarray[i]=localGetmsgID.CheckMeterID(qbbh);
								StrAddrType="LongAddr";
							}
							else 
							{
								Toast.makeText(TargetBCActivity.this, CurrentRow+"行错误,表号:"+qbbh,Toast.LENGTH_SHORT).show();
								return;
							}
/*							if(QBBHarray[i]==null){
								StrAddrType="ShortAddr";
							}
							else if(QBBHarray[i].length()==14){
							    StrAddrType="LongAddr";
							}
							else{
								StrAddrType="ShortAddr";
								QBBHarray[i]=String.format("%08d", Integer.parseInt(qbbh));
							}*/							
						} catch (Exception e) {
							// TODO: handle exception
						}
						QBcbjlid[i] = rowValues[6].toString().trim();
						QBZTarray[i] = rowValues[7].toString().trim();
						QBXMarray[i] = rowValues[9].toString().trim();
						QBql[i] = rowValues[4].toString().trim();
						dongflag = rowValues[3].toString().trim();
						String dongflag1=rowValues[3].toString().trim().substring(0, rowValues[3].toString().trim().length()-1);
						// 取出栋标识
						if (dongflag.indexOf("栋") != -1){
							alldong[i] = sub.QueryDong(rowValues[3].toString().replaceAll(" ", ""), "栋");
							allAddrNum[i]=sub.QueryDong(rowValues[3].toString().substring(rowValues[3].toString().indexOf("栋")-3, rowValues[3].toString().indexOf("栋")).replaceAll(" ", ""), "号");		
						}
//						else if (dongflag1.indexOf("号") != -1){
//							alldong[i] = sub.QueryDong(dongflag1, "号");
//							allAddrNum[i]="1";
//							//allAddrNum[i]=sub.QueryDong(rowValues[3].toString().substring(rowValues[3].toString().indexOf("幢")-3, rowValues[3].toString().indexOf("幢")).replaceAll(" ", ""), "号");								
//							// 取出单元标识
//						}
						else if (dongflag.indexOf("幢") != -1){
							alldong[i] = sub.QueryDong(rowValues[3].toString().replaceAll(" ", ""), "幢");
							allAddrNum[i]=sub.QueryDong(rowValues[3].toString().substring(rowValues[3].toString().indexOf("幢")-3, rowValues[3].toString().indexOf("幢")).replaceAll(" ", ""), "号");								
							// 取出单元标识
						}else if (dongflag.indexOf("单元") != -1) {
							allAddrNum[i]=sub.QueryDong(rowValues[3].toString().substring(rowValues[3].toString().indexOf("栋")-3, rowValues[3].toString().indexOf("单元")).replaceAll(" ", ""), "号");								
							alldanyuan[i] = sub.QueryDong(rowValues[3].toString().replaceAll(" ", ""), "单元");		
							// 取出“-”标识
						}else if(dongflag.indexOf("-")!=-1){	
							allspace[i]=Firstsub.FirstQueryDong(rowValues[3].toString().replaceAll(" ", ""), "-");
						}else if(dongflag.indexOf("层")!=-1){	
							allspace[i]=Firstsub.FirstQueryDong(rowValues[3].toString().replaceAll(" ", ""), "层");
						}
						else {			
							errorflg=1;		
						}
						
					}
				} catch (DBFException e) {
					// TODO Auto-generated catch block
					Toast.makeText(TargetBCActivity.this, CurrentRow+"行错误",Toast.LENGTH_SHORT).show();
				} 
			     filecount=row;
				 dbhelper = new BDhelper(TargetBCActivity.this,databasename);
				 dbwriter = dbhelper.getWritableDatabase();
				 dbreader = dbhelper.getReadableDatabase();
				 Cursor cursor = dbreader.query("ximeitable", null, null, null,null, null, null);
				//判断原数据库总行数和dbf文件总行数是否一致
				 int count=cursor.getCount();
					if(filecount!=cursor.getCount())
					{
						//如果不一直，删除原数据库，写入新数据
						TargetBCActivity.this.deleteDatabase(databasename);
						//重新写入新数据
						BDhelper dbhelper1 = new BDhelper(TargetBCActivity.this,databasename);
						SQLiteDatabase db1 = dbhelper1.getWritableDatabase();
//						/////////////////////////////////////////////////////////
//						String data1=""+new Date().getTime();
//						db1.beginTransaction();
//						ArrayList<String> arrays=new ArrayList<String>();
//						for (int j = 0; j <filecount; j++) {
//						 String buffer=	"insert into ximeitable values('"+ QBBHarray[j]+"','"+ QBcbjlid[j]+"','"+ QBDZarray[j]+"','"+ QBZTarray[j]+"','"+ QBXMarray[j]+"','"+ QBql[j]+"','开阀','','')";
//						 arrays.add(buffer);
//						};
//						//String[] dataStrings= arrays.to;
//						for (int j = 0; j < filecount; j++) {
//							String dataString= arrays.get(j);
//							db1.execSQL(dataString);				
//						}
//						db1.setTransactionSuccessful();
//						db1.inTransaction();
//						db1.endTransaction();
						//String data2=""+new Date().getTime();
						//Toast.makeText(TargetBCActivity.this, data2+"---"+data1,Toast.LENGTH_SHORT).show();
						///////////////////////////////////
						for (i = 0; i < filecount; i++) {
							db1.execSQL("insert into ximeitable values(?,?,?,?,?,?,?,?,?)",
									new String[] { QBBHarray[i], QBcbjlid[i],QBDZarray[i], QBZTarray[i],QBXMarray[i], QBql[i],"开阀", "", "" });
						}		
						File file = new File(fileutil.getSDPATH() + "newximei/"+ str);
						// 判断newximei下是否存在表册
						if (file.exists()) {
							Toast.makeText(TargetBCActivity.this, "文件存在",Toast.LENGTH_SHORT).show();
							try {
								fis1 = new FileInputStream(fileutil.getSDPATH()+ "newximei/" + str);
							}catch(FileNotFoundException e) {
								// TODO Auto-generated catch block
								Toast.makeText(TargetBCActivity.this, "表册没找到",Toast.LENGTH_SHORT).show();
							}
							// 如果存在读取第一个字段气表表号和第19个字段中继器标示
							DBFReader reader1;
							try {
								reader1 = new DBFReader(fis1);
								reader1.setCharactersetName("gb2312");
								filecount = reader1.getRecordCount();
								Object rowValues1[];
								// 根据气表表号写入中继器标示
								for (i = 0; i < filecount; i++) {
									rowValues1 = reader1.nextRecord();
									String qbbh = rowValues1[0].toString().trim();
									String flag = rowValues1[18].toString().trim();
									ContentValues Values = new ContentValues();
									Values.put("flag", flag);
									db1.update("ximeitable", Values, "qbbh=?",new String[] { qbbh });
								}
								db1.close();
							} catch (DBFException e) {
								// TODO Auto-generated catch block
								Toast.makeText(TargetBCActivity.this, "DBF文件错误",Toast.LENGTH_SHORT).show();
							}

						}		
					}

			    if (alldong[0]!=null && errorflg==0 ){
					//progressDialog.dismiss();
					// 对气表地址alldong数组排序
					Arrays.sort(alldong);
					Arrays.sort(allAddrNum);
					// 去掉重复的转成list
					list = clearreport.ClearReport(alldong);
					Intent localIntent = new Intent();
					localIntent.putStringArrayListExtra("alldong", list);
					list = clearreport.ClearReport(allAddrNum);
					localIntent.putStringArrayListExtra("allAddrNum", list);
					localIntent.putExtra("overmsg", overmsg);
					localIntent.putExtra("qbdz", qbdzlist);
					localIntent.putExtra("databasename", databasename);
					localIntent.putExtra("cbtype", cbtype);
					localIntent.putExtra("metertype", biaotype);
					localIntent.putExtra("dongtype", "yes");
					localIntent.putExtra("AddrType", StrAddrType);
					localIntent.setClass(TargetBCActivity.this,GroupCBTabActivity.class);
					TargetBCActivity.this.startActivity(localIntent);
				}else if(alldanyuan[0]!=null && errorflg==0){
					//progressDialog.dismiss();
					// 对气表地址alldong数组排序				
					Arrays.sort(alldanyuan);
					Arrays.sort(allAddrNum);
					// 去掉重复的转成list
					list = clearreport.ClearReport(alldanyuan);	
					Intent localIntent = new Intent();
					localIntent.putStringArrayListExtra("alldong", list);
					list = clearreport.ClearReport(allAddrNum);
					localIntent.putStringArrayListExtra("allAddrNum", list);
					localIntent.putExtra("overmsg", overmsg);
					localIntent.putExtra("qbdz", qbdzlist);
					localIntent.putExtra("databasename", databasename);
					localIntent.putExtra("metertype", biaotype);
					localIntent.putExtra("cbtype", cbtype);
					localIntent.putExtra("dongtype", "no");
					localIntent.putExtra("AddrType", StrAddrType);
					localIntent.setClass(TargetBCActivity.this,GroupCBTabActivity.class);
					TargetBCActivity.this.startActivity(localIntent);
					
				}else if(allspace[0]!=null && errorflg==0){
					
					//progressDialog.dismiss();
					// 对气表地址alldong数组排序
					Arrays.sort(allspace);
					// 去掉重复的转成list
					list = clearreport.ClearReport(allspace);
					Intent localIntent = new Intent();
					localIntent.putStringArrayListExtra("alldong", list);
					localIntent.putExtra("overmsg", overmsg);
					localIntent.putExtra("qbdz", qbdzlist);
					localIntent.putExtra("databasename", databasename);
					localIntent.putExtra("metertype", biaotype);
					localIntent.putExtra("cbtype", cbtype);
					localIntent.putExtra("dongtype", "yes");
					localIntent.putExtra("AddrType", StrAddrType);
					localIntent.setClass(TargetBCActivity.this,GroupCBTabActivity.class);
					TargetBCActivity.this.startActivity(localIntent);			
				}else if(errorflg==1){
					//Toast.makeText(TargetBCActivity.this, "表地址错误只能单抄",Toast.LENGTH_SHORT).show();
					Intent localIntent = new Intent();
					localIntent.putExtra("overmsg", overmsg);
					localIntent.putExtra("qbdz", qbdzlist);
					localIntent.putExtra("databasename", databasename);
					localIntent.putExtra("metertype", biaotype);
					localIntent.putExtra("cbtype", "infoview");
					localIntent.putExtra("AddrType", StrAddrType);
					localIntent.setClass(TargetBCActivity.this,GroupCBTabActivity.class);
					TargetBCActivity.this.startActivity(localIntent);		
				}
			
			}catch(Exception e){
				
				Toast.makeText(TargetBCActivity.this, CurrentRow+"行地址错误",Toast.LENGTH_SHORT).show();
				
			}	 
		}	
	}	
}
