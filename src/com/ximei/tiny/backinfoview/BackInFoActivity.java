package com.ximei.tiny.backinfoview;

import com.tiny.gasxm.R;
import com.ximei.tiny.tools.GetmsgID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
/*
 * 通信成功返回界面
 * 
 * 只有显示作用
 * 
 */
public class BackInFoActivity extends Activity
{
  private TextView allmsg;
  private TextView backlogin;
  private String backorder;
  private TextView fail;
  private ProgressBar pro;
  private TextView succeed;
  GetmsgID getmsg;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(1024, 1024);
    setContentView(R.layout.backsinglecb);
    this.pro = ((ProgressBar)findViewById(R.id.pro1));
    this.succeed = ((TextView)findViewById(R.id.succeed));
    this.fail = ((TextView)findViewById(R.id.fail));
    this.allmsg = ((TextView)findViewById(R.id.allmsg));
    this.backlogin = ((TextView)findViewById(R.id.backlogin));
    this.backorder = getIntent().getStringExtra("backorder");
   
    
	Intent intentBusy = new Intent("android.intent.action.busy");
	intentBusy.putExtra("State", "idle");
	sendBroadcast(intentBusy);	  
    if (this.backorder.equals("F0"))
    {
      this.succeed.setText("操作成功");
      this.backlogin.setText("");
      this.pro.setVisibility(8);
    }
    if (this.backorder.equals("F1"))
    {
      this.fail.setText("操作失败");
      this.backlogin.setText("");
      this.pro.setVisibility(8);
    }
    if (this.backorder.equals("90"))
    {
      this.fail.setText("表已安装");
      this.backlogin.setText("");
      this.pro.setVisibility(8);
    }
    if (this.backorder.equals("91"))
    {
      this.fail.setText("调价失败,表未安装");
      this.backlogin.setText("");
      this.pro.setVisibility(8);
    }
    if (this.backorder.equals("D6"))
    {
      this.fail.setText("充值次数错误");
      this.backlogin.setText("");
      this.pro.setVisibility(8);
    }
  }
  
 
}
