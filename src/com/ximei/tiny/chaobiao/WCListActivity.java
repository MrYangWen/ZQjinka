package com.ximei.tiny.chaobiao;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.tiny.gasxm.R;
import com.ximei.tiny.backinfoview.BackSingleCBActivity;
import com.ximei.tiny.database.BDhelper;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.CRC;
import com.ximei.tiny.tools.FileOpertion;
import com.ximei.tiny.tools.GetmsgID;
import com.ximei.tiny.tools.ToHexStr;
import com.ximei.tiny.tools.ToInverted;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/*
 * 未抄信息统计activity
 * 使用listview空间
 * 点击未抄气表可以进行抄表
 * 
 */
public class WCListActivity extends Activity {
	String CRCmsg;
	final int MAXmeternum = 16777215;
	final int MINmeternum = 0;
	String MsgID;
	String Msgvalue,MeterIdTmpStr;
	String Ordermsg;
	String StrID;
	String Timemsg;
	public CRC crc;
	String databasename, cbfangshi,metertype;
	GetmsgID getmsg;
	String headmsg;
	String hexTimemsg;
	private int intID;
	private ListView myListView;
	MyReceiver myreceiver;
	String ordermsg,StrAddrType;
	String overmsg;
	ToInverted toinver;
	BDhelper bdhelper;
	SQLiteDatabase readerdb;
	SQLiteDatabase writerdb;
	ArrayList<HashMap<String, String>> localArrayList;
	FileOpertion fileopertion=new FileOpertion();
	int i, j, pointflag;
	GetmsgID localGetmsgID;
	ToHexStr localToHexStr;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.mylist);
		pointflag = 0;
		getmsg = new GetmsgID();
		toinver = new ToInverted();
		// 注册广播接受单个抄表信息
		this.myreceiver = new MyReceiver();
		IntentFilter localIntentFilter = new IntentFilter();
		localIntentFilter.addAction("com.ximei.tiny.daping_BCQG");
		localIntentFilter.addAction("android.intent.action.putongcb_BROADCAST");
		
		registerReceiver(myreceiver, localIntentFilter);
		databasename = getIntent().getStringExtra("databasename");
		cbfangshi = getIntent().getStringExtra("cbfangshi");
		metertype = getIntent().getStringExtra("metertype");
		Log.e("test", metertype);
		headmsg = "5A5A00FE01";
		overmsg = getIntent().getStringExtra("overmsg") + "5B5B/";

		localGetmsgID = new GetmsgID();
		localToHexStr = new ToHexStr();
		crc = new CRC();

	}

	protected void onStart() {
		super.onStart();
		i = 0;
		j = 0;
		// 查询数据库得到所有未抄和已抄数量
		bdhelper = new BDhelper(WCListActivity.this, databasename);
		writerdb = bdhelper.getWritableDatabase();
		readerdb = bdhelper.getReadableDatabase();
		try {
			if (cbfangshi.equals("bcqg")) {
				Cursor cursor = readerdb.query("ximeitable", null, null, null,null, null, null);
				while (cursor.moveToNext()) {
					String qbzt = cursor.getString(cursor.getColumnIndex("state"));
					if (qbzt.equals("开阀")) {
						i++;
					}
					if (qbzt.equals("关阀")) {
						j++;
					}
				}
				Cursor cursor1 = readerdb.query("ximeitable", null, null, null,null, null, null);
				myListView = ((ListView) findViewById(R.id.myListView));
				localArrayList = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> localHashMap1 = new HashMap<String, String>();
				localHashMap1.put("itemTitle", "未关阀 " + String.valueOf(i) + "户");
				localHashMap1.put("itemContent", "已关阀 " + String.valueOf(j) + "户");
				localArrayList.add(localHashMap1);
				// 遍历cursor1得到未抄信息放入localArrayList形成数据源
				while (cursor1.moveToNext()) {
					String qbzt = cursor1.getString(cursor1.getColumnIndex("state"));
					String qbbh = cursor1.getString(cursor1.getColumnIndex("qbbh"));
					String qbdz = cursor1.getString(cursor1.getColumnIndex("dzms"));
					if (qbzt.equals("开阀")) {
						HashMap<String, String> localHashMap2 = new HashMap<String, String>();
						localHashMap2.put("itemTitle", qbbh);
						localHashMap2.put("itemContent", qbdz);
						localArrayList.add(localHashMap2);
					}
				}

			} else {
				Cursor cursor = readerdb.query("ximeitable", null, null, null,null, null, null);
				while (cursor.moveToNext()) {
					String qbzt = cursor.getString(cursor.getColumnIndex("qbztbh"));
					if (qbzt.equals("未抄")) {
						i++;
					}
					if (qbzt.equals("已抄")) {
						j++;
					}
				}
				Cursor cursor1 = readerdb.query("ximeitable", null, null, null,null, null, null);
				myListView = ((ListView) findViewById(R.id.myListView));
				localArrayList = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> localHashMap1 = new HashMap<String, String>();
				localHashMap1.put("itemTitle", "未抄 " + String.valueOf(i) + "户");
				localHashMap1.put("itemContent", "已抄 " + String.valueOf(j) + "户");
				localArrayList.add(localHashMap1);
				// 遍历cursor1得到未抄信息放入localArrayList形成数据源
				while (cursor1.moveToNext()) {
					String qbzt = cursor1.getString(cursor1.getColumnIndex("qbztbh"));
					String qbbh = cursor1.getString(cursor1.getColumnIndex("qbbh"));
					String qbdz = cursor1.getString(cursor1.getColumnIndex("dzms"));
					if (qbzt.equals("未抄")) {
						HashMap<String, String> localHashMap2 = new HashMap<String, String>();
						localHashMap2.put("itemTitle", qbbh);
						localHashMap2.put("itemContent", qbdz);
						localArrayList.add(localHashMap2);
					}
				}
			}
			
		} catch (Exception e) {
			e.toString();
			// TODO: handle exception
		}
		// 设置适配器和数据源
		SimpleAdapter localSimpleAdapter = new SimpleAdapter(this,localArrayList, R.layout.list_item, new String[] { "itemTitle","itemContent" }, new int[] { R.id.qbbh, R.id.qbzd });
		myListView.setAdapter(localSimpleAdapter);
		// 定位到指定的行
		localSimpleAdapter.notifyDataSetInvalidated();
		myListView.setSelectionFromTop(pointflag, pointflag);
		// 设置监听器
		myListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				// TODO Auto-generated method stub
				// 当点击时进行单个抄表
				Intent intentBusy = new Intent("android.intent.action.busy");
				intentBusy.putExtra("State", "busy");
				sendBroadcast(intentBusy);
				try {
					if (cbfangshi.equals("bcqg")){
						Log.e("test", "关阀指令");
						String str = (String) ((HashMap) myListView.getItemAtPosition(arg2)).get("itemTitle");
						MeterIdTmpStr=str;
						Intent localIntent1 = new Intent();
						pointflag = arg2;	
						//intID = Integer.parseInt(str);
						//if (intID <= MAXmeternum && intID >= MINmeternum) 	
						Msgvalue=localGetmsgID.GetMeterAddr(str);
						if(Msgvalue.length()==14){
						    StrAddrType="LongAddr";
						}else{
							StrAddrType="ShortAddr";
						}
						if((str.length()==14)&&(StrAddrType.equals("ShortAddr")))
							metertype="newmeter";
						if(Msgvalue!=null)
						{
							//MsgID = Integer.toHexString(intID);
							//Msgvalue = localGetmsgID.getMsgID(MsgID).toUpperCase();
							CRCmsg = ("03"+Msgvalue + "51020300");
							ordermsg = headmsg + CRCmsg+ crc.CRC_CCITT(1, CRCmsg).toUpperCase()+ overmsg;
							
							localIntent1.putExtra("order", ordermsg);
							localIntent1.putExtra("metertype", metertype);
							localIntent1.putExtra("Comm", "01");
							localIntent1.putExtra("AddrType", StrAddrType);
							localIntent1.setClass(WCListActivity.this,BtXiMeiService.class);
							WCListActivity.this.startService(localIntent1);
							Intent localIntent2 = new Intent();
							localIntent2.setClass(WCListActivity.this,BackSingleCBActivity.class);
							WCListActivity.this.startActivity(localIntent2);
						}
					} else {
						String str = (String) ((HashMap) myListView.getItemAtPosition(arg2)).get("itemTitle");
						MeterIdTmpStr=str;
						Intent localIntent1 = new Intent();
						pointflag = arg2;
						SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("ssmmHHddMMyy");
						Timemsg = localSimpleDateFormat.format(new Date());
						hexTimemsg = localToHexStr.toHexStr(WCListActivity.this.Timemsg);
						try {
							//intID = Integer.parseInt(str);
							//if (intID <= MAXmeternum && intID >= MINmeternum) 
							Msgvalue=localGetmsgID.GetMeterAddr(str);
							if(Msgvalue.length()==14){
							    StrAddrType="LongAddr";
							}else{
								StrAddrType="ShortAddr";
							}
							if((str.length()==14)&&(StrAddrType.equals("ShortAddr")))
								metertype="newmeter";	
							if(Msgvalue!=null)
							{
								//MsgID = Integer.toHexString(intID);
								//Msgvalue = localGetmsgID.getMsgID(MsgID).toUpperCase();
								CRCmsg = "09" + Msgvalue + "9A" + "06" + hexTimemsg;
								ordermsg = headmsg + CRCmsg+ crc.CRC_CCITT(1, CRCmsg).toUpperCase()+ overmsg;
								localIntent1.putExtra("metertype", metertype);
								localIntent1.putExtra("order", ordermsg);
								localIntent1.putExtra("AddrType", StrAddrType);
								localIntent1.setClass(WCListActivity.this,BtXiMeiService.class);
								WCListActivity.this.startService(localIntent1);
								Intent localIntent2 = new Intent();
								localIntent2.putExtra("Comm", "01");
								localIntent2.setClass(WCListActivity.this,BackSingleCBActivity.class);
								WCListActivity.this.startActivity(localIntent2);
							} else {
								Toast.makeText(WCListActivity.this,"表号" + str + "错误", Toast.LENGTH_SHORT).show();
							}
						} catch (NumberFormatException localNumberFormatException) {
							Toast.makeText(WCListActivity.this.getApplicationContext(),"你选择的表号是：" +Msgvalue,Toast.LENGTH_SHORT).show();

						}
					}					
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(WCListActivity.this.getApplicationContext(),"你选择的表号是：" + Msgvalue,Toast.LENGTH_SHORT).show();

				}
			}
		});
	}
	// 单个抄表成功后的广播
	private class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
          if(intent.getAction().equals("android.intent.action.putongcb_BROADCAST")){
        	  Log.e("test", "收到单抄广播");
			// 得到气表气量
    		 String qbll = null;
  			 String backmsg = null;
  			 String qbbhhex;
  			 String leijistr = null;
  			 int leiji=0;
  			 String jidianstr = null ;
  			 int jidian=0;
  			 String state= null; 
  			String qbbhmsg ="";
        	  try{
    			backmsg = intent.getStringExtra("resmsg");
    			qbbhhex = intent.getStringExtra("qbbh");
    			leijistr = toinver.toinverted(backmsg.substring(0, 8));
    			leiji = Integer.parseInt(leijistr, 16);
    			jidianstr = toinver.toinverted(backmsg.substring(16, 24));
    			jidian = Integer.parseInt(jidianstr, 16);
    			state = backmsg.substring(24, 28);
    			if ((leiji + jidian) > 9) {
    				qbll = Integer.toString((leiji + jidian) / 10);
    			} else {
    				qbll = Integer.toString(0);
    			}
    			// 得到系统当前时间
    			// 得到气表表号
    			
      			if(qbbhhex.length()!=14)
      			{
      			    //qbbhmsg  = String.valueOf(Integer.parseInt(getmsg.getMsgID(qbbhhex), 16));
      			    qbbhmsg=String.format("%08d", Integer.parseInt(getmsg.getMsgID(qbbhhex), 16));
      			}
      			else
      				qbbhmsg=qbbhhex; 
      			if((MeterIdTmpStr.length()==14))
      			{
      				if(Integer.parseInt(MeterIdTmpStr.substring(6, 14))==Long.parseLong(qbbhmsg))
      					qbbhmsg=MeterIdTmpStr;
      			}
    			// 得到抄表时间
    			SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
    			String cbtime = df1.format(new Date());
    			// 通用远传表返回信息
    			if (backmsg.length() == 56 || backmsg.length() == 32|| backmsg.length() == 80||backmsg.length() == 84) {
    				ContentValues values = new ContentValues();
    				// 写入数据库
    				values.put("qbztbh", "已抄");
    				values.put("qbql", qbll);
    				values.put("cbjlid", cbtime);
    				writerdb.update("ximeitable", values, "qbbh=?",new String[] { qbbhmsg });
    			}
    			// 返回gpm表46,64个字节
    			if (backmsg.length() == 64 || backmsg.length() == 46) {
    				ContentValues values = new ContentValues();
    				// 写入数据库
    				values.put("qbztbh", "已抄");
    				values.put("qbql", qbll);
    				values.put("cbjlid", cbtime);
    				values.put("state", state);
    				writerdb.update("ximeitable", values, "qbbh=?",new String[] { qbbhmsg });
    			}        		  
        	  }catch(Exception e){
        			String log=fileopertion.getCurTime()+"异常记录:\r\n"+e.toString();
        			fileopertion.writeTxtToFile(log); 
        			log=fileopertion.getCurTime()+"异常记录:\r\n"+qbll+':'+backmsg+':'+leijistr+':'+jidianstr+':'+qbbhmsg+':'+state;
        			fileopertion.writeTxtToFile(log);         			
					Toast.makeText(WCListActivity.this, e.toString(),Toast.LENGTH_SHORT).show();

        	  }
			
          }else if(intent.getAction().equals("com.ximei.tiny.daping_BCQG")){
        	  Log.e("test", "收到关阀广播");
        	  String backorder = intent.getStringExtra("backorder");
        	  String qbbhhex = intent.getStringExtra("qbbh");
        	  // 得到气表表号
	  		  String qbbhmsg ="";
	  			if(qbbhhex.length()!=14)
	  			    qbbhmsg  = String.valueOf(Integer.parseInt(getmsg.getMsgID(qbbhhex), 16));
	  			else
	  				qbbhmsg=qbbhhex;        	  
  			  //String qbbhmsg = String.valueOf(Integer.parseInt(getmsg.getMsgID(qbbhhex), 16));
  			  if(backorder.equals("F0")){
  			      ContentValues values = new ContentValues();
			      // 写入数据库
			      values.put("state", "关阀");
			      writerdb.update("ximeitable", values, "qbbh=?",new String[] { qbbhmsg });	
  			}
        	Log.e("test", backorder);  
          }
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			unregisterReceiver(myreceiver);
		}
		return false;
	}

}