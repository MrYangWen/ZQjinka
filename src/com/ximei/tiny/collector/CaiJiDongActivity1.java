package com.ximei.tiny.collector;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.tiny.gasxm.R;
import com.ximei.tiny.database.BDhelper;
import com.ximei.tiny.tools.Containstr;
import com.ximei.tiny.tools.FirstSubDong;
import com.ximei.tiny.tools.SubDong;

public class CaiJiDongActivity1 extends Activity {
	private TextView biaocelogin;
	private String caijitype, cfqbbh;
	private String databasename;
	Intent intent;
	Intent intent1;
	ListView myListView;
	Containstr ishave;
	ArrayList<String> numlist;
	private String overmsg;
	String[] qbdzlist;
	SubDong subdong;
	FirstSubDong firstsub;
	ArrayList<String> tempqbdz = new ArrayList<String>();
	ArrayList<String> sendqbbh = new ArrayList<String>();

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.biaoce);
		this.intent1 = new Intent();
		this.subdong = new SubDong();
		firstsub = new FirstSubDong();
		this.ishave = new Containstr();
		this.intent = getIntent();
		// 得到intent传递的数据
		this.numlist = this.intent.getStringArrayListExtra("alldong");
		this.databasename = this.intent.getStringExtra("databasename");
		this.caijitype = this.intent.getStringExtra("caijitype");
		this.qbdzlist = this.intent.getStringArrayExtra("qbdz");
		this.overmsg = this.intent.getStringExtra("overmsg");
		cfqbbh = "";

		// 得到listview句柄
		this.myListView = ((ListView) findViewById(R.id.myListView));
		ArrayList<HashMap<String, String>> localArrayList = new ArrayList<HashMap<String, String>>();
		this.biaocelogin = ((TextView) findViewById(R.id.biaocelogin));
		// 遍历楼栋标识numlist
		if (this.numlist.size() == 0) {
			this.biaocelogin.setText("没有可选择的楼栋");
			this.biaocelogin.setTextSize(25.0F);
		} else {

			this.biaocelogin.setText("所有楼栋");
			this.biaocelogin.setTextSize(30.0F);

			HashMap<String, String> localHashMap2 = new HashMap<String, String>();
			localHashMap2.put("baioceTitle", "全部楼栋");
			localArrayList.add(localHashMap2);
			// 存放到相应的localArrayList中形成数据源
			for (int i = 0; i < this.numlist.size(); i++) {
				HashMap<String, String> localHashMap = new HashMap<String, String>();
				localHashMap.put("baioceTitle", (String) this.numlist.get(i)
						+ "栋");
				localArrayList.add(localHashMap);
			}

		}

		// 设置适配器数据源
		SimpleAdapter localSimpleAdapter = new SimpleAdapter(
				CaiJiDongActivity1.this, localArrayList, R.layout.list_biaoce,
				new String[] { "baioceTitle" }, new int[] { R.id.biaocename });
		this.myListView.setAdapter(localSimpleAdapter);
		// 设置监听器
		this.myListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				// 取得点击的String（02栋）
				String str1 = (String) ((HashMap) CaiJiDongActivity1.this.myListView
						.getItemAtPosition(arg2)).get("baioceTitle");

				if (str1.equals("全部楼栋")) {

					for (int i = 0; i < qbdzlist.length; i++) {
						tempqbdz.add(qbdzlist[i]);

					}

				} else {

					// 取得栋前的标识（多少栋）
					String dongflag = str1.substring(0, str1.indexOf("栋"));

					// 找出气表地址中包含该栋的地址
					if (qbdzlist[0].indexOf("栋") != -1) {
						for (int i = 0; i < qbdzlist.length; i++) {

							if (subdong.QueryDong(qbdzlist[i], "栋").equals(
									dongflag)) {

								tempqbdz.add(qbdzlist[i]);
							}

						} // 找出气表地址中包含该栋的地址list
					} else if (qbdzlist[0].indexOf("幢") != -1) {
						for (int i = 0; i < qbdzlist.length; i++) {

							if (subdong.QueryDong(qbdzlist[i], "幢").equals(
									dongflag)) {

								tempqbdz.add(qbdzlist[i]);
							}

						}
					} else if (qbdzlist[0].indexOf("-") != -1) {
						for (int i = 0; i < qbdzlist.length; i++) {

							if (firstsub.FirstQueryDong(qbdzlist[i], "-")
									.equals(dongflag)) {

								tempqbdz.add(qbdzlist[i]);
							}

						}

					}
				}
				String[] sendqbdz = new String[tempqbdz.size()];
				// 把取得的气表地址tempqbdz转化为String数组sendqbbh
				for (int i = 0; i < tempqbdz.size(); i++) {

					sendqbdz[i] = tempqbdz.get(i);

				}
				
					BDhelper localBDhelper = new BDhelper(
							CaiJiDongActivity1.this,
							CaiJiDongActivity1.this.databasename);
					SQLiteDatabase readerdb = localBDhelper
							.getReadableDatabase();
					Cursor localCursor = readerdb.query("ximeitable", null,
							null, null, null, null, null);

					while (localCursor.moveToNext()) {
						String str3 = localCursor.getString(localCursor
								.getColumnIndex("qbbh"));
						String str4 = localCursor.getString(localCursor
								.getColumnIndex("dzms"));
						String str5 = localCursor.getString(localCursor
								.getColumnIndex("qbztbh"));
						String str6 = localCursor.getString(localCursor
								.getColumnIndex("jzqflag"));

						if (!str3.equals("") && ishave.isHave(sendqbdz, str4)
								&& str5.equals("未抄") && str6.equals("")) {

							if (Integer.parseInt(str3) < 16777215) {
								CaiJiDongActivity1.this.sendqbbh.add(str3);
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

						Toast.makeText(CaiJiDongActivity1.this,
								"共" + String.valueOf(sendqbbh.size()) + "只",
								Toast.LENGTH_LONG).show();
						;
						intent1.putExtra("caijitype", caijitype);
						intent1.putExtra("overmsg", overmsg);
						intent1.putExtra("databasename", databasename);
						intent1.putStringArrayListExtra("qbbhlist", sendqbbh);
						intent1.setClass(CaiJiDongActivity1.this,
								CaiJiInputActivity.class);
						CaiJiDongActivity1.this.startActivity(intent1);
					} else {
						Toast.makeText(CaiJiDongActivity1.this, "没有表号",
								Toast.LENGTH_SHORT).show();
					}
					CaiJiDongActivity1.this.sendqbbh.clear();
					CaiJiDongActivity1.this.tempqbdz.clear();

				}
			

		});

	}

	protected void onResume() {
		super.onResume();
	}
}
