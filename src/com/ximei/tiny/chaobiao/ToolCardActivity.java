package com.ximei.tiny.chaobiao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.tiny.gasxm.R;
import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.FileUtils;
import com.ximei.tiny.tools.GetTotalPack;
import com.ximei.tiny.tools.HtmlService;
import com.ximei.tiny.tools.StrToHex;

public class ToolCardActivity extends Activity {

	private static final String[] chaobiaohint = { "读卡类型", "清除数据", "设置卡",
			"恢复卡", "换表卡", "管理卡", "时间卡", "用户卡" };
	private static final int[] imageid = { R.drawable.dgcb, R.drawable.bccb,
			R.drawable.cjsj, R.drawable.cbtj };
	private Button rdtype, setcard, restcard, managecard, tradecard, clearcard,
			usercard, timecard;
	private TextView infohint;
	private Intent intent;
	private String ordermsg, timemsg, crcmsg;
	private BroadcastReceiver recardrecevie;
	String backmsg, ordertype, orderpsw, pathsdcard, filepath, backmsgtype;
	String[] cardata;
	FileUtils fileutil;
	String qlbzts, maxql, gfz, tzl, sbz, urlpath, nethtml,setpsw,managepsw,restpsw,tradepsw,clearpsw;
	String[] cs;
	GetTotalPack daoxu;
	private AlertDialog selfdialog;
	private ProgressDialog progressdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.toolcard);

		daoxu = new GetTotalPack();

		GridView localGridView = (GridView) findViewById(R.id.gridview);

		// 用list<map>存放所用功能菜单作为数据源
		ArrayList<HashMap<String, Object>> localArrayList = new ArrayList<HashMap<String, Object>>();

		for (int i = 0; i < chaobiaohint.length; i++) {

			HashMap<String, Object> localHashMap = new HashMap<String, Object>();
			localHashMap.put("ItemImage", R.drawable.test1);
			localHashMap.put("ItemText", chaobiaohint[i]);
			localArrayList.add(localHashMap);

		}
		// GridView设置数据源
		localGridView.setAdapter(new SimpleAdapter(ToolCardActivity.this,
				localArrayList, R.layout.gridview_meun, new String[] {
						"ItemImage", "ItemText" }, new int[] { R.id.ItemImage,
						R.id.ItemText }));

		// 设置点击事件
		localGridView.setOnItemClickListener(new ItemClickListener());

		// 网络获取设置卡参数
		urlpath = "http://192.168.1.102:8080/ServerForImage/ServletForJSON";
		new Thread(new DowHttpString()).start();

		intent = getIntent();
		infohint = (TextView) findViewById(R.id.cardtypehint);
		infohint.setTextSize(30);
		ordertype = "";

		recardrecevie = new RwCardRecevie();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction("android.intent.tiny.rwcard");
		registerReceiver(recardrecevie, filter2);

	}

	// 点击相应的功能菜单进行相应的操作
	class ItemClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			String str = ((HashMap) arg0.getItemAtPosition(arg2)).get(
					"ItemText").toString();

			if (str.equals("读卡类型")) {
				ordertype = "rdtype";
				crcmsg = StrToHex.ToHexString("3004");
				ordermsg = "READ[3004" + crcmsg + "]\r";

			}

			if (str.equals("清除数据")) {
				ordertype = "clearcard";
				crcmsg = StrToHex.ToHexString("3004");
				ordermsg = "READ[3004" + crcmsg + "]\r";

			}

			if (str.equals("设置卡")) {
				AlertDialog.Builder builder = new Builder(ToolCardActivity.this);

				builder.setTitle("这是一张设置卡");
				builder.setItems(cs, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {

					}

				}).show();
				ordertype = "setcard";
				crcmsg = StrToHex.ToHexString("3004");
				ordermsg = "READ[3004" + crcmsg + "]\r";

			}
			if (str.equals("恢复卡")) {

				ordertype = "restcard";
				crcmsg = StrToHex.ToHexString("3004");
				ordermsg = "READ[3004" + crcmsg + "]\r";

			}
			if (str.equals("换表卡")) {

				ordertype = "tradecard";
				crcmsg = StrToHex.ToHexString("3004");
				ordermsg = "READ[3004" + crcmsg + "]\r";

			}
			if (str.equals("管理卡")) {

				ordertype = "managecard";
				crcmsg = StrToHex.ToHexString("3004");
				ordermsg = "READ[3004" + crcmsg + "]\r";

			}
			if (str.equals("时间卡")) {
				ordertype = "timecard";
				crcmsg = StrToHex.ToHexString("3004");
				ordermsg = "READ[3004" + crcmsg + "]\r";

			}
			if (str.equals("用户卡")) {
				ordertype = "usercard";
				crcmsg = StrToHex.ToHexString("3004");
				ordermsg = "READ[3004" + crcmsg + "]\r";

			}

			intent.putExtra("order", ordermsg);
			Log.e("test", ordermsg);
			intent.setClass(ToolCardActivity.this, BtXiMeiService.class);
			ToolCardActivity.this.startService(intent);

		}
	}

	class DowHttpString implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.e("test", urlpath);
			try {

				nethtml = HtmlService.gethtml(urlpath);
				Message msg = new Message();
				msg.what = 1;
				NetHandler.sendMessage(msg);
			} catch (Exception e) {
				Log.e("test", "网络异常"+e.toString());
				// Toast.makeText(DataActivity.this, "获得图片失败", 1).show();
			}

		}

	}

	Handler NetHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				// htmltext.setText(nethtml);
				// 处理获得的设置卡参数
				String setdata = nethtml;
				
				managepsw=setdata.substring(26,32);
				restpsw=setdata.substring(34, 40);
				tradepsw=setdata.substring(50, 56);
				
				
				Log.e("test", managepsw);
				Log.e("test", restpsw);
				Log.e("test", tradepsw);
				
				
				
				
				Log.e("test", setdata);
				qlbzts = setdata.substring(44, 48);
				String qlbztsstr = String.valueOf(Integer.parseInt(
						daoxu.gettotalpack(qlbzts, 4), 16) / 10.0);
				maxql = setdata.substring(56, 60);
				String maxqlstr = String.valueOf(Integer.parseInt(
						daoxu.gettotalpack(maxql, 4), 16));
				gfz = setdata.substring(60, 64);
				String gfzstr = String.valueOf(Integer.parseInt(
						daoxu.gettotalpack(gfz, 4), 16));
				tzl = setdata.substring(96, 100);
				String tzlstr = String.valueOf(Integer.parseInt(
						daoxu.gettotalpack(tzl, 4), 16));
				sbz = setdata.substring(106, 108);
				String sbzstr = String.valueOf(Integer.parseInt(
						daoxu.gettotalpack(sbz, 4), 16));

				cs = new String[] { "量不足提示值" + qlbztsstr, "表内最大存量" + maxqlstr,
						"关阀值" + gfzstr, "透支值" + tzlstr, "死表状态" + sbzstr };

			}

		}

	};

	public class RwCardRecevie extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			backmsg = intent.getStringExtra("backmsg").trim();

			if (backmsg.equals("FFFFFFFF")) {

				orderpsw = "FFFFFF";
			} else if (backmsg.equals("30300000")) {

				orderpsw = "DC2514";

			} else if (backmsg.equals("10100000")) {

				orderpsw = "E0EFD4";

			} else if (backmsg.equals("20200000")) {

				orderpsw = "704032";

			} else if (backmsg.equals("40400000")) {

				orderpsw = "CE4332";

			}

			// 判断卡类型
			if (ordertype.equals("rdtype")) {
				if (backmsg.equals("FFFFFFFF")) {
					infohint.setText("这是一张空卡");

				} else if (backmsg.equals("30300000")) {

					infohint.setText("这是一张管理卡");

				} else if (backmsg.equals("10100000")) {

					infohint.setText("这是一张恢复卡");

				} else if (backmsg.equals("20200000")) {

					infohint.setText("这是一张设置卡");

				} else if (backmsg.equals("40400000")) {

					infohint.setText("这是一张换表卡");

				}

			}
			// 清除卡数据
			if (ordertype.equals("clearcard")) {

				if (backmsg.equals("OK")) {

					infohint.setText("清除成功");

				} else if (backmsg.equals("FAIL")) {
					infohint.setText("清除失败");
				} else {

					crcmsg = StrToHex.ToHexString(orderpsw);
					ordermsg = "CLEAR[" + orderpsw + crcmsg + "]\r";
					Log.e("test", ordermsg);

					intent.putExtra("order", ordermsg);
					intent.setClass(ToolCardActivity.this, BtXiMeiService.class);
					ToolCardActivity.this.startService(intent);
				}

			}

			if (ordertype.equals("managecard")) {
				// 管理卡

				if (backmsg.equals("OK")) {

					infohint.setText("管理卡制作完成");

				} else if (backmsg.equals("FAIL")) {
					infohint.setText("管理卡制作失败");
				} else {
					String managemsg = "30300000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF3030FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
					crcmsg = StrToHex.ToHexString("FFFFFFDC251430" + managemsg);
					ordermsg = "WCWPSW[FFFFFFDC251430" + managemsg + crcmsg
							+ "]\r";
					Log.e("test", ordermsg);
					intent.putExtra("order", ordermsg);
					intent.setClass(ToolCardActivity.this, BtXiMeiService.class);
					ToolCardActivity.this.startService(intent);
				}

			}

			if (ordertype.equals("setcard")) {
				// 设置卡

				if (backmsg.equals("OK")) {

					infohint.setText("设置卡制作完成	");

				} else if (backmsg.equals("FAIL")) {
					infohint.setText("设置卡制作失败");
				} else {
					String managemsg = "20200000FFFFFFFF987652BCDC2514EDE0EFD4DB00003200CE4332BFE8030000FFFFFFFF00FFFFFFFFFFFFFFF"
							+ "FFFFFFF000070170200FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF4201FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
					crcmsg = StrToHex.ToHexString("FFFFFF70403230" + managemsg);
					ordermsg = "WCWPSW[FFFFFF70403230" + managemsg + crcmsg
							+ "]\r";
					Log.e("test", ordermsg);
					intent.putExtra("order", ordermsg);
					intent.setClass(ToolCardActivity.this, BtXiMeiService.class);
					ToolCardActivity.this.startService(intent);
				}

			}

			if (ordertype.equals("restcard")) {
				// 恢复卡

				if (backmsg.equals("OK")) {

					infohint.setText("恢复卡制作完成");

				} else if (backmsg.equals("FAIL")) {
					infohint.setText("恢复卡制作失败");
				} else {
					String managemsg = "10100000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF1010FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"
							+ "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";

					crcmsg = StrToHex.ToHexString("FFFFFFE0EFD430" + managemsg);
					ordermsg = "WCWPSW[FFFFFFE0EFD430" + managemsg + crcmsg
							+ "]\r";
					Log.e("test", ordermsg);
					intent.putExtra("order", ordermsg);
					intent.setClass(ToolCardActivity.this, BtXiMeiService.class);
					ToolCardActivity.this.startService(intent);
				}

			}
			if (ordertype.equals("tradecard")) {
				// 换表卡

				if (backmsg.equals("OK")) {

					infohint.setText("换表卡制作完成");

				} else if (backmsg.equals("FAIL")) {
					infohint.setText("换表卡制作失败");
				} else {
					String managemsg = "40400000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFBF40FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"
							+ "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";

					crcmsg = StrToHex.ToHexString("FFFFFFCE433230" + managemsg);
					ordermsg = "WCWPSW[FFFFFFCE433230" + managemsg + crcmsg
							+ "]\r";
					Log.e("test", ordermsg);
					intent.putExtra("order", ordermsg);
					intent.setClass(ToolCardActivity.this, BtXiMeiService.class);
					ToolCardActivity.this.startService(intent);
				}

			}

		}

	}

	public String readFile(String fileName) {

		File file = new File(fileName);
		String lineTxt = null;
		if (file.exists()) {
			try {

				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file));// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String readmsg = null;
				while ((readmsg = bufferedReader.readLine()) != null) {
					lineTxt = readmsg;
				}
				read.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			} finally {
				// close
			}

		}
		return lineTxt;

	}

	

	private class TimeOutThread implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(5000);
				progressdialog.dismiss();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
