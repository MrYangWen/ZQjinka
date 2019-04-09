package com.ximei.tiny.collector;

import android.app.Activity;
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
import com.ximei.tiny.database.BDhelper;
import com.ximei.tiny.tools.Containstr;
import com.ximei.tiny.tools.SubDong;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * 和HuXingActivity功能一样。
 * 就是把地址按户型分开
 * 
 * 
 */
public class CaiJiHuXingActivity extends Activity {
	private TextView biaocelogin;
	private String databasename;
	private String datatype;
	ArrayList<String> huxinglist;
	Intent intent;
	Intent intent1;
	String flag;
	Containstr ishave;
	ListView myListView;
	private String overmsg;
	String[] qbdzlist;
	ArrayList<String> sendqbbh = new ArrayList<String>();
	SubDong subdong;
	List<String> tempqbdz = new ArrayList<String>();

	public void onCreate(Bundle paramBundle) {

		super.onCreate(paramBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.biaoce);
		this.intent1 = new Intent();
		this.subdong = new SubDong();
		this.ishave = new Containstr();
		this.intent = getIntent();
		this.huxinglist = this.intent.getStringArrayListExtra("huxingsing");
		this.databasename = this.intent.getStringExtra("databasename");
		this.datatype = this.intent.getStringExtra("datatype");
		this.qbdzlist = this.intent.getStringArrayExtra("qbdz");
		this.overmsg = this.intent.getStringExtra("overmsg");
		this.myListView = ((ListView) findViewById(R.id.myListView));
		this.biaocelogin = ((TextView) findViewById(R.id.biaocelogin));
		ArrayList<HashMap<String, String>> localArrayList = new ArrayList<HashMap<String, String>>();

		if (this.huxinglist.size() == 0) {
			this.biaocelogin.setText("没有可选择的户型");
			this.biaocelogin.setTextSize(25.0F);
		} else {

			this.biaocelogin.setText("所有户型");
			this.biaocelogin.setTextSize(30.0F);
			for (int i = 0; i < this.huxinglist.size(); i++) {
				HashMap<String, String> localHashMap = new HashMap<String, String>();
				localHashMap.put("baioceTitle", (String) this.huxinglist.get(i)
						+ "号户型");
				localArrayList.add(localHashMap);
			}

		}

		SimpleAdapter localSimpleAdapter = new SimpleAdapter(
				CaiJiHuXingActivity.this, localArrayList, R.layout.list_biaoce,
				new String[] { "baioceTitle" }, new int[] { R.id.biaocename });
		this.myListView.setAdapter(localSimpleAdapter);
		this.myListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				 Log.e("test", datatype);

				String str1 = (String) ((HashMap) CaiJiHuXingActivity.this.myListView
						.getItemAtPosition(arg2)).get("baioceTitle");
				String dongflag = str1.substring(0, str1.indexOf("号户型"));

				for (int i = 0; i < qbdzlist.length; i++) {
					
					

					if (subdong.QueryDong(qbdzlist[i], "号").equals(dongflag)) {

						tempqbdz.add(qbdzlist[i]);
						

					}

				}

				String[] sendqbdz = new String[tempqbdz.size()];

				for (int i = 0; i < tempqbdz.size(); i++) {

					sendqbdz[i] = tempqbdz.get(i);
                    
				
				}

				BDhelper localBDhelper = new BDhelper(CaiJiHuXingActivity.this,
						CaiJiHuXingActivity.this.databasename);
				SQLiteDatabase readerdb = localBDhelper.getReadableDatabase();
				Cursor localCursor = readerdb.query("ximeitable", null, null,
						null, null, null, null);

				while (localCursor.moveToNext()) {
					String str3 = localCursor.getString(localCursor
							.getColumnIndex("qbbh"));
					String str4 = localCursor.getString(localCursor
							.getColumnIndex("dzms"));
					String str5 = localCursor.getString(localCursor
							.getColumnIndex("qbztbh"));
					String str6 = localCursor.getString(localCursor
							.getColumnIndex("flag"));
					
					
				
					// 2014-11-16衡阳修改
					/*
					 * if ((!str3.equals("")) && (str5.equals("未抄"))) {
					 * 
					 * CaiJiHuXingActivity.this.sendqbbh.add(str3); }
					 */

					if ((!str3.equals("")) && (ishave.isHave(sendqbdz, str4))
							&& (str5.equals("未抄"))&& str6.equals("")) {

						if (Integer.parseInt(str3) < 16777215) {
							CaiJiHuXingActivity.this.sendqbbh.add(str3);
						}
					}

				
				}
				 if (sendqbbh.size() > 0) {
					 
					 for (int i = 0; i < sendqbbh.size() - 1; i++) {
							for (int j = sendqbbh.size() - 1; j > i; j--) {
								if (sendqbbh.get(j).equals(sendqbbh.get(i))) {
									Log.e("test", "重复表号" + sendqbbh.get(j));
									sendqbbh.remove(sendqbbh.get(j));

								}

							}

						}

						Toast.makeText(CaiJiHuXingActivity.this,
								"共" + String.valueOf(sendqbbh.size()) + "只",
								Toast.LENGTH_LONG).show();
					 					 
				   
					intent1.putExtra("caijitype", datatype);
					intent1.putExtra("overmsg", overmsg);
					intent1.putExtra("databasename", databasename);
					intent1.putStringArrayListExtra("qbbhlist", sendqbbh);
					intent1.setClass(CaiJiHuXingActivity.this,CaiJiInputActivity.class);
					CaiJiHuXingActivity.this.startActivity(intent1);
				} else {
					Toast.makeText(CaiJiHuXingActivity.this, "没有未组网表号",
							Toast.LENGTH_SHORT).show();
				}
				CaiJiHuXingActivity.this.sendqbbh.clear();
				CaiJiHuXingActivity.this.tempqbdz.clear();

			}
		});

	}
}
