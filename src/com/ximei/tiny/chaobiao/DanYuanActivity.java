package com.ximei.tiny.chaobiao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.ximei.tiny.tools.SubDong;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/*
 * 按单元抄表activity界面
 * 按传过来的气表地址区分有几个单元
 * 
 * 
 */
public class DanYuanActivity extends Activity {
	private TextView biaocelogin;
	private String cbfangshi,metertype;
	ClearReportArray clearreport;
	ArrayList<String> danyuanlist;
	private String databasename;
	ArrayList<String> huxinglist;
	Intent intent;
	Intent intent1;
	Containstr contain;

	ListView myListView;
	private String overmsg;
	String[] qbdzlist,sendlist;
	SubDong subdong;
	List<String> tempqbdz = new ArrayList<String>();

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		//取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.biaoce);
		this.intent1 = new Intent();
		this.subdong = new SubDong();
		this.contain = new Containstr();
		this.clearreport = new ClearReportArray();
		this.intent = getIntent();
		//根据intent传输得到相应的数据
		this.danyuanlist = this.intent.getStringArrayListExtra("danyuansing");
		this.databasename = this.intent.getStringExtra("databasename");
		this.cbfangshi = this.intent.getStringExtra("cbfangshi");
		this.qbdzlist = this.intent.getStringArrayExtra("qbdz");
		this.overmsg = this.intent.getStringExtra("overmsg");
		this.metertype = this.intent.getStringExtra("metertype");
		this.myListView = ((ListView) findViewById(R.id.myListView));
		this.biaocelogin = ((TextView) findViewById(R.id.biaocelogin));
		ArrayList<HashMap<String, String>> localArrayList = new ArrayList<HashMap<String, String>>();
		// 遍历楼栋标识danyuanlist
		if (this.danyuanlist.size() == 0) {
			this.biaocelogin.setText("没有可选择的单元");
			this.biaocelogin.setTextSize(25.0F);
		} else {

			this.biaocelogin.setText("所有单元");
			this.biaocelogin.setTextSize(30.0F);
			// 存放到相应的localArrayList中形成数据源
			for (int i = 0; i < this.danyuanlist.size(); i++) {
				HashMap<String, String> localHashMap = new HashMap<String, String>();
				localHashMap.put("baioceTitle",
						(String) this.danyuanlist.get(i) + "单元");
				localArrayList.add(localHashMap);
			}

		}
		// 设置适配器数据源
		SimpleAdapter localSimpleAdapter = new SimpleAdapter(
				DanYuanActivity.this, localArrayList, R.layout.list_biaoce,
				new String[] { "baioceTitle" }, new int[] { R.id.biaocename });
		this.myListView.setAdapter(localSimpleAdapter);
		// 设置监听器
		this.myListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// 取得点击的String（02单元）
				String str1 = (String) ((HashMap) DanYuanActivity.this.myListView
						.getItemAtPosition(arg2)).get("baioceTitle");
				// 取得栋前的标识（多少单元）
				String dongflag = str1.substring(0, str1.indexOf("单元"));
				
				// 找出气表地址中包含该栋的地址tempqbdz
				for (int i = 0; i < qbdzlist.length; i++) {

					if (subdong.QueryDong(qbdzlist[i], "单元").equals(dongflag)) {
						//Toast.makeText(DanYuanActivity.this, qbdzlist[i],Toast.LENGTH_SHORT).show();

						tempqbdz.add(qbdzlist[i]);
					}

				}
				// 把取得的气表地址tempqbdz转化为String数组qbdzlist
				sendlist = new String[tempqbdz.size()];

				for (int i = 0; i < tempqbdz.size(); i++) {

					sendlist[i] = tempqbdz.get(i);

				}
				
                //判断qbdzlist是否有户型号
				if (contain.isHave(sendlist, "号")) {
                    //找到点击的单元里面含有的户型数
					String[] allhuxing = new String[sendlist.length];
					for (int i = 0; i < sendlist.length; i++) {
						allhuxing[i] = subdong.QueryDong(sendlist[i], "号");
					}
					Arrays.sort(allhuxing);
					huxinglist = clearreport.ClearReport(allhuxing);
					intent1.putExtra("databasename", databasename);
					intent1.putExtra("cbfangshi", cbfangshi);
					intent1.putExtra("overmsg", overmsg);
					intent1.putExtra("qbdz", sendlist);
					intent1.putExtra("huxingsing", huxinglist);
					intent1.putExtra("metertype", metertype);
					intent1.putExtra("AddrType", getIntent().getStringExtra("AddrType"));
					intent1.setClass(DanYuanActivity.this, HuXingActivity.class);
					DanYuanActivity.this.startActivity(intent1);

				} else {

					DanYuanActivity.this.intent1.putExtra("sendqbdzlist",qbdzlist);
					intent1.putExtra("metertype", metertype);
					DanYuanActivity.this.intent1.putExtra("databasename",databasename);
					DanYuanActivity.this.intent1.putExtra("cbfangshi",cbfangshi);
					DanYuanActivity.this.intent1.putExtra("overmsg",overmsg);
				}
				tempqbdz.clear(); 

			}
		});

	}

	protected void onResume() {
		super.onResume();
	}
}
