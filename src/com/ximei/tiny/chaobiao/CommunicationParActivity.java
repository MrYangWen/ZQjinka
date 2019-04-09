package com.ximei.tiny.chaobiao;

import java.util.ArrayList;
import java.util.HashMap;

import com.tiny.gasxm.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CommunicationParActivity extends Activity {
	TextView hint, backmsgtv;
	String qbbh, urlpath, netString, netinfo;
	Intent intent;
	private static final String[] wlwmeterhint = {"单个抄表","关阀参数设置","关阀参数查询","通信参数设置","通信参数查询","抄表时间设置","抄表时间查询"};
    private static final int[] imageid = { R.drawable.dgcb,R.drawable.qxqg, R.drawable.qzgf,R.drawable.cxzt, R.drawable.qbcz, R.drawable.cb,R.drawable.cb};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.xmwlw);
		// 得到GridView控件
		GridView localGridView = (GridView) findViewById(R.id.gridview);
		hint = ((TextView) findViewById(R.id.cbfshint));
		backmsgtv = ((TextView) findViewById(R.id.backmsg));
		hint.setText("通信参数操作");
		intent = getIntent();
		netinfo = "";
		// 用list<map>存放所用功能菜单作为数据源
		ArrayList<HashMap<String, Object>> localArrayList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < wlwmeterhint.length; i++) {
			HashMap<String, Object> localHashMap = new HashMap<String, Object>();
			localHashMap.put("ItemImage", imageid[i]);
			localHashMap.put("ItemText", wlwmeterhint[i]);
			localArrayList.add(localHashMap);
		}
		// 设置适配器数据源
		localGridView.setAdapter(new SimpleAdapter(this, localArrayList,R.layout.gridview_meun,
				new String[] { "ItemImage", "ItemText" }, new int[] {R.id.ItemImage, R.id.ItemText }));
		// 设置监听器
		localGridView.setOnItemClickListener(new ItemClickListener());		
	}
	// 设置点击表册名的监听事件
	class ItemClickListener implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			String str = ((HashMap) arg0.getItemAtPosition(arg2)).get("ItemText").toString();
			if (str.equals("通信参数设置")){
				
			}
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.communication_par, menu);
		return true;
	}

}
