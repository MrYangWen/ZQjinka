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
import com.ximei.tiny.tools.SubDong;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
 * 户型抄表activity
 * 点击相应的户型进行抄表
 * 
 */
public class HuXingActivity extends Activity {
	private TextView biaocelogin;
	private String cbfangshi,metertype;
	private String databasename;
	ArrayList<String> huxinglist;
	Intent intent;
	Intent intent1;
	ListView myListView;
	private String overmsg;
	String[] qbdzlist,sendqbdz;
	SubDong subdong;
	List<String> tempqbdz = new ArrayList<String>();

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		//取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.biaoce);
		this.intent1 = new Intent();
		this.subdong = new SubDong();
		this.intent = getIntent();
		//根据intent传输得到相应的数据
		this.huxinglist = this.intent.getStringArrayListExtra("huxingsing");
		this.databasename = this.intent.getStringExtra("databasename");
		this.cbfangshi = this.intent.getStringExtra("cbfangshi");
		this.qbdzlist = this.intent.getStringArrayExtra("qbdz");
		this.overmsg = this.intent.getStringExtra("overmsg");
		this.metertype = this.intent.getStringExtra("metertype");
		this.myListView = ((ListView) findViewById(R.id.myListView));
		this.biaocelogin = ((TextView) findViewById(R.id.biaocelogin));
		

		ArrayList<HashMap<String, String>> localArrayList = new ArrayList<HashMap<String, String>>();
		// 遍历楼栋标识huxinglist
		if (this.huxinglist.size() == 0) {
			this.biaocelogin.setText("没有可选择的户型");
			this.biaocelogin.setTextSize(25.0F);
		} else {

			this.biaocelogin.setText("所有户型");
			this.biaocelogin.setTextSize(30.0F);
			// 存放到相应的localArrayList中形成数据源
			for (int i = 0; i < this.huxinglist.size(); i++) {
				HashMap<String, String> localHashMap = new HashMap<String, String>();
				localHashMap.put("baioceTitle", (String) this.huxinglist.get(i)
						+ "号户型");
				localArrayList.add(localHashMap);
			}

		}
		// 设置适配器数据源
		SimpleAdapter localSimpleAdapter = new SimpleAdapter(
				HuXingActivity.this, localArrayList, R.layout.list_biaoce,
				new String[] { "baioceTitle" }, new int[] { R.id.biaocename });
		this.myListView.setAdapter(localSimpleAdapter);
		// 设置监听器
		this.myListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// 取得点击的String（02号户型）
				String str1 = (String) ((HashMap) HuXingActivity.this.myListView
						.getItemAtPosition(arg2)).get("baioceTitle");
				// 取得栋前的标识（多少户型）
				String dongflag = str1.substring(0, str1.indexOf("号户型"));
				
				
				// 找出气表地址中包含该 户型的地址tempqbdz
				for (int i = 0; i < qbdzlist.length; i++) {

					if (subdong.QueryDong(qbdzlist[i], "号").equals(dongflag)) {

						tempqbdz.add(qbdzlist[i]);

					}
					//Toast.makeText(HuXingActivity.this, qbdzlist[i], Toast.LENGTH_SHORT).show();

				}
				
				// 把取得的气表地址tempqbdz转化为String数组sendqbdz
				sendqbdz = new String[tempqbdz.size()];

				for (int i = 0; i < tempqbdz.size(); i++) {

					sendqbdz[i] = tempqbdz.get(i);
					

				}
				//发动数据抄表
				if(sendqbdz.length>0){

				intent1.putExtra("sendqbdzlist", sendqbdz);
				intent1.putExtra("databasename",databasename);
				intent1.putExtra("cbfangshi",cbfangshi);
				intent1.putExtra("metertype",metertype);
				intent1.putExtra("overmsg",overmsg);
               
				intent1.putExtra("AddrType", getIntent().getStringExtra("AddrType"));               
				intent1.setClass(HuXingActivity.this,DataBaseService.class);
				HuXingActivity.this.startService(intent1);
				intent1.setClass(HuXingActivity.this,BackBiaoCeActivity.class);
				HuXingActivity.this.startActivity(intent1);
				}else{
					
					Toast.makeText(HuXingActivity.this, "没有表号", Toast.LENGTH_SHORT).show();
				}  
				 
				tempqbdz.clear();
				

			}
		});

	}
}
