package com.ximei.tiny.collector;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import java.util.ArrayList;
import java.util.List;

import com.tiny.gasxm.R;

/*
 * 组网统计activity
 * 包含实际组网表号和测试组网表号
 * 
 * 
 */
public class ShowZWInFoActivity extends TabActivity
{
  String cbfangshi;
  String databasename;
  Intent intent;
  private TabHost m_tabHost;
  ArrayList<Integer> numlist;
  String overmsg;
  String[] qbdzlist;
  List<String> tempqbdz = new ArrayList<String>();

  public void addOneTab()
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("flagtype", "jzqflag");
    localIntent.putExtra("databasename", this.databasename);
    localIntent.setClass(this, ZWTongJiActivity.class);
    TabHost.TabSpec localTabSpec = this.m_tabHost.newTabSpec("One");
    localTabSpec.setIndicator("测试组网表号", null);
    localTabSpec.setContent(localIntent);
    this.m_tabHost.addTab(localTabSpec);
  }

  public void addTwoTab()
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("flagtype", "flag");
    localIntent.putExtra("databasename", this.databasename);
    localIntent.setClass(this, ZWTongJiActivity.class);
    TabHost.TabSpec localTabSpec = this.m_tabHost.newTabSpec("Two");
    localTabSpec.setIndicator("实际组网表号", null);
    localTabSpec.setContent(localIntent);
    this.m_tabHost.addTab(localTabSpec);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    //去掉标题状态栏
    requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(1024, 1024);
    setContentView(R.layout.groupcbview);
    this.m_tabHost = getTabHost();
    this.intent = getIntent();
    this.overmsg = this.intent.getStringExtra("overmsg");
    this.databasename = this.intent.getStringExtra("databasename");
    addOneTab();
    addTwoTab();
  }
}
