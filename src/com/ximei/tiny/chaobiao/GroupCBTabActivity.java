package com.ximei.tiny.chaobiao;

/*
 * 抄表主界面Activity
 * 包含楼栋抄表activity。
 * 户型抄表activity
 * 未抄信息activity
 * 已抄信息activity
 * 
 * 
 */

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.tiny.gasxm.R;



public class GroupCBTabActivity extends TabActivity {
	String cbfangshi;
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
		this.metertype = this.intent.getStringExtra("metertype");
		this.overmsg = this.intent.getStringExtra("overmsg");
		this.databasename = this.intent.getStringExtra("databasename");
		this.dongtype = this.intent.getStringExtra("dongtype");
		Log.e("test", metertype);
		
		
		
		
		// 通过不同的方式add不同的界面
		if (this.cbfangshi.equals("infoview")) {
			addThreeTab();
			addFourTab();
			return;
		}
		if(this.dongtype.equals("yes")){
		  addOneTab();
		  addTwoTab();
		  addThreeTab();
		  addFourTab();
		  return;
		}
		if(this.dongtype.equals("no")){
		 //addOneTab();
		 // addTwoTab();
		 addFiveTab();
		 addThreeTab();
		 addFourTab();
		 return;
		}	
		
	}
	

	// 未抄activity
	public void addFourTab() {
		Intent localIntent = new Intent();
		localIntent.putExtra("databasename",databasename);
		localIntent.putExtra("overmsg", this.overmsg);
		localIntent.putExtra("cbfangshi", this.cbfangshi);
		localIntent.putExtra("metertype", this.metertype);
		localIntent.setClass(this, WCListActivity.class);
		TabHost.TabSpec localTabSpec = this.m_tabHost.newTabSpec("Four");
		localTabSpec.setIndicator("未抄信息", null);
		localTabSpec.setContent(localIntent);
		this.m_tabHost.addTab(localTabSpec);
	}

	// 楼栋抄表activity
	public void addOneTab() {
		Intent localIntent = new Intent();
		localIntent.putStringArrayListExtra("alldong", this.numlist);
		localIntent.putExtra("overmsg", this.overmsg);
		localIntent.putExtra("qbdz", this.qbdzlist);
		localIntent.putExtra("databasename", databasename);
		localIntent.putExtra("cbfangshi", this.cbfangshi);
		localIntent.putExtra("metertype", this.metertype);
		localIntent.putExtra("AddrType", intent.getStringExtra("AddrType"));
		localIntent.setClass(this, DongActivity.class);
		TabHost.TabSpec localTabSpec = this.m_tabHost.newTabSpec("One");
		localTabSpec.setIndicator("楼栋抄表", null);
		localTabSpec.setContent(localIntent);
		this.m_tabHost.addTab(localTabSpec);
	}

	// 已抄activity
	public void addThreeTab() {
		Intent localIntent = new Intent();
		localIntent.putExtra("databasename",databasename);
		localIntent.putExtra("cbfangshi", this.cbfangshi);
		localIntent.putExtra("metertype", this.metertype);
		localIntent.putExtra("AddrType", intent.getStringExtra("AddrType"));
		localIntent.setClass(this, YCListActivity.class);
		TabHost.TabSpec localTabSpec = this.m_tabHost.newTabSpec("Three");
		localTabSpec.setIndicator("已抄信息", null);
		localTabSpec.setContent(localIntent);
		this.m_tabHost.addTab(localTabSpec);
	}

	// 户型抄表activity
	public void addTwoTab() {
		Intent localIntent = new Intent();
		localIntent.putStringArrayListExtra("alldong", this.numlist);
		localIntent.putExtra("overmsg", this.overmsg);
		localIntent.putExtra("qbdz", this.qbdzlist);
		localIntent.putExtra("AddrType", intent.getStringExtra("AddrType"));
		localIntent.putExtra("databasename", databasename);
		localIntent.putExtra("cbfangshi", this.cbfangshi);
		localIntent.putExtra("metertype", this.metertype);
		localIntent.setClass(this, DongActivity1.class);
		TabHost.TabSpec localTabSpec = this.m_tabHost.newTabSpec("Two");
		localTabSpec.setIndicator("户型抄表", null);
		localTabSpec.setContent(localIntent);
		this.m_tabHost.addTab(localTabSpec);
	}
	// 单元抄表activity
		public void addFiveTab() {
			Intent localIntent = new Intent();
			localIntent.putStringArrayListExtra("danyuansing", numlist);
			localIntent.putExtra("overmsg", overmsg);
			localIntent.putExtra("qbdz", qbdzlist);
			localIntent.putExtra("AddrType", intent.getStringExtra("AddrType"));
			localIntent.putExtra("databasename",databasename);
			localIntent.putExtra("cbfangshi", cbfangshi);
			localIntent.putExtra("metertype", this.metertype);
			localIntent.setClass(GroupCBTabActivity.this,DanYuanActivity.class);
			TabHost.TabSpec localTabSpec = this.m_tabHost.newTabSpec("Two");
			localTabSpec.setIndicator("户型抄表", null);
			localTabSpec.setContent(localIntent);
			this.m_tabHost.addTab(localTabSpec);
		}

}
