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
import com.ximei.tiny.tools.ClearReportArray;
import com.ximei.tiny.tools.Containstr;
import com.ximei.tiny.tools.FirstSubDong;
import com.ximei.tiny.tools.SubDong;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/*
 * 户型抄表activity
 * 以整栋楼为目标下划分单元和户型
 * 使用listview显示单元和户型
 */
public class DongActivity1 extends Activity {
	private TextView biaocelogin;
	private String cbfangshi,metertype;
	ClearReportArray clearreport;
	private ArrayList<String> danyuanlist;
	private String databasename;
	private ArrayList<String> huxinglist;
	Intent intent;
	Intent intent1;
	Containstr contain;
	ListView myListView;
	ArrayList<String> numlist;
	private String overmsg;
	String[] qbdz;
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
		firstsub= new FirstSubDong();
		this.contain = new Containstr();
		clearreport = new ClearReportArray();
		this.intent = getIntent();
		// 得到intent传递的数据
		this.numlist = this.intent.getStringArrayListExtra("alldong");
		this.databasename = this.intent.getStringExtra("databasename");
		this.cbfangshi = this.intent.getStringExtra("cbfangshi");
		this.qbdzlist = this.intent.getStringArrayExtra("qbdz");
		this.overmsg = this.intent.getStringExtra("overmsg");
		this.metertype = this.intent.getStringExtra("metertype");
		this.myListView = ((ListView) findViewById(R.id.myListView));
		this.biaocelogin = ((TextView) findViewById(R.id.biaocelogin));
		ArrayList<HashMap<String, String>> localArrayList = new ArrayList<HashMap<String, String>>();
		// 遍历楼栋标识numlist
		if (this.numlist.size() == 0) {
			this.biaocelogin.setText("没有可选择的楼栋");
			this.biaocelogin.setTextSize(25.0F);
		} else {

			this.biaocelogin.setText("所有楼栋");
			this.biaocelogin.setTextSize(30.0F);
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
				DongActivity1.this, localArrayList, R.layout.list_biaoce,
				new String[] { "baioceTitle" }, new int[] { R.id.biaocename });
		this.myListView.setAdapter(localSimpleAdapter);
		// 设置监听器
		this.myListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// 取得点击的String（02栋）
				String str1 = (String) ((HashMap) DongActivity1.this.myListView
						.getItemAtPosition(arg2)).get("baioceTitle");
				// 取得栋前的标识（多少栋）
				String dongflag = str1.substring(0, str1.indexOf("栋"));
				// 找出气表地址中包含该栋的地址tempqbdz
				if (qbdzlist[0].indexOf("栋") != -1) {
					for (int i = 0; i < qbdzlist.length; i++) {

						if (subdong.QueryDong(qbdzlist[i], "栋")
								.equals(dongflag)) {

							tempqbdz.add(qbdzlist[i]);
						}

					} // 找出气表地址中包含该栋的地址tempqbdz
				}else if (qbdzlist[0].indexOf("幢") != -1) {
					for (int i = 0; i < qbdzlist.length; i++) {

						if (subdong.QueryDong(qbdzlist[i], "幢")
								.equals(dongflag)) {

							tempqbdz.add(qbdzlist[i]);
						}

					}
				}else if(qbdzlist[0].indexOf("-") != -1){
					for (int i = 0; i < qbdzlist.length; i++) {

						if (firstsub.FirstQueryDong(qbdzlist[i], "-")
								.equals(dongflag)) {

							tempqbdz.add(qbdzlist[i]);
						}

					}
					
					
					
				}
				
				// 把取得的气表地址tempqbdz转化为String数组qbdz
				qbdz = new String[tempqbdz.size()];

				for (int i = 0; i < tempqbdz.size(); i++) {

					qbdz[i] = tempqbdz.get(i);

				}
               //判断里面含有单元还是户型号进行相应的操作
				if (contain.isHave(qbdz, "单元")) {

					String[] alldanyuan = new String[qbdz.length];

					for (int i = 0; i < qbdz.length; i++) {

						alldanyuan[i] = subdong.QueryDong(qbdz[i], "单元");

					}

					Arrays.sort(alldanyuan);
					danyuanlist = clearreport.ClearReport(alldanyuan);
					intent1.putExtra("databasename", databasename);
					intent1.putExtra("cbfangshi", cbfangshi);
					intent1.putExtra("overmsg", overmsg);
					intent1.putExtra("qbdz", qbdz);
					intent1.putExtra("danyuansing", danyuanlist);
					intent1.putExtra("metertype", metertype);
					intent1.putExtra("AddrType", getIntent().getStringExtra("AddrType"));
					intent1.setClass(DongActivity1.this, DanYuanActivity.class);
					DongActivity1.this.startActivity(intent1);
					

				} else {
					String[] allhuxing = new String[qbdz.length];

					for (int i = 0; i < qbdz.length; i++) {

						allhuxing[i] = subdong.QueryDong(qbdz[i], "号");
						

					}

					Arrays.sort(allhuxing);
					huxinglist = clearreport.ClearReport(allhuxing);
					intent1.putExtra("databasename", databasename);
					intent1.putExtra("cbfangshi", cbfangshi);
					intent1.putExtra("overmsg", overmsg);
					intent1.putExtra("qbdz", qbdz);
					intent1.putExtra("metertype", metertype);
					intent1.putExtra("huxingsing", huxinglist);
					intent1.putExtra("AddrType", getIntent().getStringExtra("AddrType"));
					intent1.setClass(DongActivity1.this, HuXingActivity.class);
					DongActivity1.this.startActivity(intent1);

				}

				tempqbdz.clear();

			}
		});

	}

	protected void onResume() {
		super.onResume();
	}
}
