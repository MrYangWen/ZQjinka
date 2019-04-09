package com.ximei.tiny.collector;

import java.util.ArrayList;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

import com.tiny.gasxm.R;

public class ZwcsTabActivity extends TabActivity {
	String cbfangshi,caijitype;
	String databasename,bcpath;
	Intent intent;
	private TabHost m_tabHost;
	ArrayList<String>  numlist =new ArrayList<String>();
	ArrayList<String>  danyuansing =new ArrayList<String>();
	String overmsg,dongtype,metertype;
	String[] qbdzlist;
	
	
	

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 取消标题状态栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(1024, 1024);
		setContentView(R.layout.groupcbview);
		this.m_tabHost = getTabHost();
		this.intent = getIntent();
		// 得到intent传递过的数据
		this.qbdzlist = this.intent.getStringArrayExtra("qbdz");
		this.numlist = this.intent.getStringArrayListExtra("alldong");
		this.cbfangshi = this.intent.getStringExtra("cbtype");
		this.caijitype = this.intent.getStringExtra("caijitype");
		this.metertype = this.intent.getStringExtra("metertype");
		this.overmsg = this.intent.getStringExtra("overmsg");
		this.databasename = this.intent.getStringExtra("databasename");
		this.dongtype = this.intent.getStringExtra("dongtype");
		
			
		
		
			addOneTab();
			addTwoTab();
		
		
	}
	


	// 楼栋抄表activity
	public void addOneTab() {
		Intent localIntent = new Intent();
		localIntent.putStringArrayListExtra("alldong", this.numlist);
		localIntent.putExtra("overmsg", this.overmsg);
		localIntent.putExtra("qbdz", this.qbdzlist);
		localIntent.putExtra("databasename", databasename);
		localIntent.putExtra("caijitype", this.caijitype);
		localIntent.putExtra("metertype", this.metertype);
		localIntent.setClass(this, CaiJiDongActivity1.class);
		TabHost.TabSpec localTabSpec = this.m_tabHost.newTabSpec("One");
		localTabSpec.setIndicator("楼栋测试", null);
		localTabSpec.setContent(localIntent);
		this.m_tabHost.addTab(localTabSpec);
	}

	

	// 户型抄表activity
	public void addTwoTab() {
		Intent localIntent = new Intent();
		localIntent.putStringArrayListExtra("alldong", this.numlist);
		localIntent.putExtra("overmsg", this.overmsg);
		localIntent.putExtra("qbdz", this.qbdzlist);
		localIntent.putExtra("databasename", databasename);
		localIntent.putExtra("caijitype", this.caijitype);
		localIntent.putExtra("metertype", this.metertype);
		localIntent.setClass(this, CaiJiDongActivity.class);
		TabHost.TabSpec localTabSpec = this.m_tabHost.newTabSpec("Two");
		localTabSpec.setIndicator("户型测试", null);
		localTabSpec.setContent(localIntent);
		this.m_tabHost.addTab(localTabSpec);
	}
	

}
