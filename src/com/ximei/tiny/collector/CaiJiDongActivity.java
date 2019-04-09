package com.ximei.tiny.collector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tiny.gasxm.R;
import com.ximei.tiny.tools.ClearReportArray;
import com.ximei.tiny.tools.Containstr;
import com.ximei.tiny.tools.FirstSubDong;

import com.ximei.tiny.tools.SubDong;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/*
 * 和抄表DongActivity功能一样
 * 
 */
public class CaiJiDongActivity extends Activity {
	private TextView biaocelogin;
	ClearReportArray clearreport;
	Containstr contain;
	private ArrayList<String> danyuanlist;
	private String databasename;
	private String datatype;
	private ArrayList<String> huxinglist;
	Intent intent;
	Intent intent1;
	Containstr ishave;
	ListView myListView;
	private ArrayList<String> numlist;
	private String overmsg;
	String[] qbdz;
	String[] qbdzlist;
	SubDong subdong;
	FirstSubDong firstsub;
	List<String> tempqbdz = new ArrayList<String>();

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.biaoce);
		this.intent1 = new Intent();
		this.subdong = new SubDong();
		this.firstsub = new FirstSubDong();
		this.ishave = new Containstr();
		this.clearreport = new ClearReportArray();
		this.contain = new Containstr();
		this.intent = getIntent();
		this.overmsg = this.intent.getStringExtra("overmsg");
		this.numlist = this.intent.getStringArrayListExtra("alldong");
		this.databasename = this.intent.getStringExtra("databasename");
		this.datatype = this.intent.getStringExtra("caijitype");
		Log.e("test", datatype);
		this.qbdzlist = this.intent.getStringArrayExtra("qbdz");
		this.myListView = ((ListView) findViewById(R.id.myListView));
		this.biaocelogin = ((TextView) findViewById(R.id.biaocelogin));

		ArrayList<HashMap<String, String>> localArrayList = new ArrayList<HashMap<String, String>>();

		if (this.numlist.size() == 0) {
			this.biaocelogin.setText("没有可选择的楼栋");
			this.biaocelogin.setTextSize(25.0F);
		} else {

			this.biaocelogin.setText("所有楼栋");
			this.biaocelogin.setTextSize(30.0F);
			for (int i = 0; i < this.numlist.size(); i++) {
				HashMap<String, String> localHashMap = new HashMap<String, String>();
				localHashMap.put("baioceTitle", (String) this.numlist.get(i)
						+ "栋");
				localArrayList.add(localHashMap);
			}

		}

		SimpleAdapter localSimpleAdapter = new SimpleAdapter(
				CaiJiDongActivity.this, localArrayList, R.layout.list_biaoce,
				new String[] { "baioceTitle" }, new int[] { R.id.biaocename });
		this.myListView.setAdapter(localSimpleAdapter);

		this.myListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				String str1 = (String) ((HashMap) CaiJiDongActivity.this.myListView
						.getItemAtPosition(arg2)).get("baioceTitle");
				String dongflag = str1.substring(0, str1.indexOf("栋"));

				if (qbdzlist[0].indexOf("栋") != -1) {
					for (int i = 0; i < qbdzlist.length; i++) {

						if (subdong.QueryDong(qbdzlist[i], "栋")
								.equals(dongflag)) {

							tempqbdz.add(qbdzlist[i]);
							
						}

					}
				} else if (qbdzlist[0].indexOf("幢") != -1) {
					for (int i = 0; i < qbdzlist.length; i++) {

						if (subdong.QueryDong(qbdzlist[i], "幢")
								.equals(dongflag)) {

							tempqbdz.add(qbdzlist[i]);
						}

					}
				} else if (qbdzlist[0].indexOf("-") != -1) {
					for (int i = 0; i < qbdzlist.length; i++) {

						if (firstsub.FirstQueryDong(qbdzlist[i], "-").equals(
								dongflag)) {

							tempqbdz.add(qbdzlist[i]);
						}

					}
				}

				qbdz = new String[tempqbdz.size()];

				for (int i = 0; i < tempqbdz.size(); i++) {

					qbdz[i] = tempqbdz.get(i);

				}

				if (contain.isHave(qbdz, "单元")) {

					String[] alldanyuan = new String[qbdz.length];

					for (int i = 0; i < qbdz.length; i++) {

						alldanyuan[i] = subdong.QueryDong(qbdz[i], "单元");

					}

					Arrays.sort(alldanyuan);
					danyuanlist = clearreport.ClearReport(alldanyuan);

					CaiJiDongActivity.this.intent1.putStringArrayListExtra(
							"danyuansing", CaiJiDongActivity.this.danyuanlist);
					CaiJiDongActivity.this.intent1.putExtra("datatype",
							CaiJiDongActivity.this.datatype);
					CaiJiDongActivity.this.intent1.putExtra("databasename",
							CaiJiDongActivity.this.databasename);
					CaiJiDongActivity.this.intent1.putExtra("qbdz",
							CaiJiDongActivity.this.qbdz);
					CaiJiDongActivity.this.intent1.setClass(
							CaiJiDongActivity.this, CaiJiDanYuanActivity.class);
					CaiJiDongActivity.this
							.startActivity(CaiJiDongActivity.this.intent1);

				} else {
					String[] allhuxing = new String[qbdz.length];

					for (int i = 0; i < qbdz.length; i++) {

						allhuxing[i] = subdong.QueryDong(qbdz[i], "号");
						// Log.e("test", qbdz[i]);
					}

					Arrays.sort(allhuxing);
					huxinglist = clearreport.ClearReport(allhuxing);
					CaiJiDongActivity.this.intent1.putStringArrayListExtra(
							"huxingsing", CaiJiDongActivity.this.huxinglist);
					CaiJiDongActivity.this.intent1.putExtra("qbdz", qbdz);
					CaiJiDongActivity.this.intent1.putExtra("datatype",
							CaiJiDongActivity.this.datatype);
					CaiJiDongActivity.this.intent1.putExtra("databasename",
							CaiJiDongActivity.this.databasename);
					CaiJiDongActivity.this.intent1.setClass(
							CaiJiDongActivity.this, CaiJiHuXingActivity.class);
					CaiJiDongActivity.this
							.startActivity(CaiJiDongActivity.this.intent1);

				}

				tempqbdz.clear();

			}
		});

	}

	protected void onResume() {
		super.onResume();
	}
}
