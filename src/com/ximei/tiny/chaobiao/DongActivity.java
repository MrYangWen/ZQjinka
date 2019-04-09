package com.ximei.tiny.chaobiao;

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
import android.widget.Toast;

import com.tiny.gasxm.R;
import com.ximei.tiny.backinfoview.BackBiaoCeActivity;

import com.ximei.tiny.service.DataBaseService;
import com.ximei.tiny.tools.FirstSubDong;
import com.ximei.tiny.tools.SubDong;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * 楼栋抄表activity
 * 以整栋楼为目标抄表
 * 使用listview显示栋数
 */
public class DongActivity extends Activity {
	private TextView biaocelogin;
	private String cbfangshi,metertype;
	private String databasename;
	Intent intent;
	Intent intent1;
	ListView myListView;
	ArrayList<String> numlist;
	private String overmsg;
	String[] qbdzlist;
	SubDong subdong;
	FirstSubDong firstsub;
	List<String> tempqbdz = new ArrayList<String>();

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.biaoce);
		this.intent1 = new Intent();
		this.subdong = new SubDong();
		firstsub = new FirstSubDong();
		this.intent = getIntent();
		// 得到intent传递的数据
		this.numlist = this.intent.getStringArrayListExtra("alldong");
		this.databasename = this.intent.getStringExtra("databasename");
		this.cbfangshi = this.intent.getStringExtra("cbfangshi");
		this.qbdzlist = this.intent.getStringArrayExtra("qbdz");
		this.overmsg = this.intent.getStringExtra("overmsg");
		metertype = this.intent.getStringExtra("metertype");

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
		SimpleAdapter localSimpleAdapter = new SimpleAdapter(DongActivity.this,
				localArrayList, R.layout.list_biaoce,
				new String[] { "baioceTitle" }, new int[] { R.id.biaocename });
		this.myListView.setAdapter(localSimpleAdapter);
		// 设置监听器
		this.myListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				// 取得点击的String（02栋）
				String str1 = (String) ((HashMap) DongActivity.this.myListView
						.getItemAtPosition(arg2)).get("baioceTitle");

				if (str1.equals("全部楼栋")) {
					
					for(int i = 0;i<qbdzlist.length;i++){
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
				String[] sendqbbh = new String[tempqbdz.size()];
				// 把取得的气表地址tempqbdz转化为String数组sendqbbh
				for (int i = 0; i < tempqbdz.size(); i++) {

					sendqbbh[i] = tempqbdz.get(i);
					
					// Toast.makeText(DongActivity.this, tempqbdz.get(i),
					// Toast.LENGTH_SHORT).show();
				}

				intent1.putExtra("sendqbdzlist", sendqbbh);
				intent1.putExtra("databasename", databasename);
				intent1.putExtra("cbfangshi", cbfangshi);
				intent1.putExtra("overmsg", overmsg);
				intent1.putExtra("metertype", metertype);
				intent1.putExtra("AddrType", intent.getStringExtra("AddrType"));
				intent1.setClass(DongActivity.this, BackBiaoCeActivity.class);
				startActivity(intent1);
				intent1.setClass(DongActivity.this, DataBaseService.class);
				startService(intent1);

				tempqbdz.clear();

			}

		});

	}

	protected void onResume() {
		super.onResume();
	}
}
