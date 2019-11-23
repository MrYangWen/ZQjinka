package com.ximei.tiny.chaobiao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tiny.gasxm.R;
import com.ximei.tiny.tools.IniReader;


/*
 * 选择抄表方式activity
 * 一种普通集抄14秒
 * 一种快速集抄4.5秒
 * 使用GridView控件
 * 
 */
public class GroupCBFSActivity extends Activity {
	private TextView hint;
	Intent intent;
	String overmsg;
	String ScannerTypeStr;
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消标题状态栏
		requestWindowFeature(1);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.groupcbfs1);
		// 得到GridView空间句柄
		GridView localGridView = (GridView) findViewById(R.id.gridview);
		this.hint = ((TextView) findViewById(R.id.cbfshint));
		this.hint.setText("选择抄表方式");
		this.intent = getIntent();
		this.overmsg = this.intent.getStringExtra("overmsg");
		try {
			ScannerTypeStr = new IniReader("SysSet.ini", GroupCBFSActivity.this).getValue("FreqSet", "ScannerType");
            if(ScannerTypeStr==null)ScannerTypeStr="00";
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			ScannerTypeStr="00";
			e.printStackTrace();
		}		
		// 把普通集抄和快速集抄两种方式放入localArrayList中形成数据源
		ArrayList<HashMap<String, Object>> localArrayList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> localHashMap1 = new HashMap<String, Object>();
		localHashMap1.put("ItemImage", R.drawable.notes);
		localHashMap1.put("ItemText", "普通集抄");
		HashMap<String, Object> localHashMap2 = new HashMap<String, Object>();
		localHashMap2.put("ItemImage", R.drawable.kscb);
		localHashMap2.put("ItemText", "快速集抄");
		localArrayList.add(localHashMap1);
		if(ScannerTypeStr.equals("01"))
		   localArrayList.add(localHashMap2);
		// 设置适配器和数据源
		localGridView.setAdapter(new SimpleAdapter(this, localArrayList,
				R.layout.gridview_meun,new String[] { "ItemImage", "ItemText" }, 
				                       new int[] {R.id.ItemImage, R.id.ItemText }));
		// 设置监听器
		localGridView.setOnItemClickListener(new ItemClickListener());
	}

	// 监听相应普通集抄和快速集抄事件
	class ItemClickListener implements AdapterView.OnItemClickListener {

		public void onItemClick(AdapterView<?> paramAdapterView,
				View paramView, int paramInt, long paramLong) {
			String str = ((HashMap) paramAdapterView
					.getItemAtPosition(paramInt)).get("ItemText").toString();
			if (str.equals("普通集抄")) {
				GroupCBFSActivity.this.intent.putExtra("cbtype", "putongcb");
			}
			if (str.equals("快速集抄")) {
				GroupCBFSActivity.this.intent.putExtra("cbtype", "kuaisucb");
			}
			intent.putExtra("overmsg", overmsg);
			intent.setClass(GroupCBFSActivity.this, TargetBCActivity.class);
			GroupCBFSActivity.this.startActivity(intent);
		}
	}
}
