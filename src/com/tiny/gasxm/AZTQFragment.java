package com.tiny.gasxm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


import org.json.JSONObject;


import com.ximei.tiny.service.BtXiMeiService;
import com.ximei.tiny.tools.FileUtils;
import com.ximei.tiny.tools.GetTotalPack;
import com.ximei.tiny.tools.StrToHex;
import com.ximei.tiny.wlwmeter.GasPaymentActivity;
import com.ximei.tiny.wlwmeter.HtmlService;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

@TargetApi(11)
public class AZTQFragment extends Fragment {
	private static final String[] chaobiaohint = { "读卡类型", "清除数据", "设置卡",
			"恢复卡", "换表卡", "管理卡", "时间卡", "用户卡","校验密码" };
	private static final int[] imageid = { R.drawable.cbxxtj, R.drawable.qjtz,
			R.drawable.shdown, R.drawable.amrdown, R.drawable.ccsz,
			R.drawable.gbdz, R.drawable.szsj, R.drawable.dgcb ,R.drawable.jdtb};
	String backmsg, ordertype, orderpsw, pathsdcard, filepath, backmsgtype,
			ordermsg, crcmsg;
	String qlbzts, maxql, gfz, tzl, sbz;
	FileUtils fileutil;
	GetTotalPack daoxu;
	String[] cardata, cs;
	Intent intent;
	TextView cardhint;
	private String urlpath,ipadd;
	String setcardmsg  = "20200000FFFFFFFFA2204CCEE6730A9FDAB9CAA900001E00F4152CCDD0071E00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF0000640002FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFA3ED00000000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF0000FFFFFFFF0DFFFFFFFFFFFFFFFFFFFF";
	String usercardmsg = "5050000021000000FFFFFFFFFFFFFFFF1014010BFFFFFFFF401F0000000000000000000000000000605501340100FFFFFFFFFFFF00FF00000000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF40CEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
	private BroadcastReceiver recardrecevie;
	private ProgressDialog progressdialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View cbLayout = inflater.inflate(R.layout.cardxml, container, false);
		GridView localGridView = (GridView) cbLayout
				.findViewById(R.id.gridview);

		intent = new Intent();
		cardhint = (TextView) cbLayout.findViewById(R.id.cardhint);
		cardhint.setText("请读卡");
		ipadd = getArguments().getString("ipadd");
		Log.e("test", ipadd);

		// 用list<map>存放所用功能菜单作为数据源
		ArrayList<HashMap<String, Object>> localArrayList = new ArrayList<HashMap<String, Object>>();

		for (int i = 0; i < chaobiaohint.length; i++) {

			HashMap<String, Object> localHashMap = new HashMap<String, Object>();
			localHashMap.put("ItemImage", imageid[i]);
			localHashMap.put("ItemText", chaobiaohint[i]);
			localArrayList.add(localHashMap);

		}
		// GridView设置数据源
		localGridView.setAdapter(new SimpleAdapter(cbLayout.getContext(),
				localArrayList, R.layout.gridview_meun, new String[] {
						"ItemImage", "ItemText" }, new int[] { R.id.ItemImage,
						R.id.ItemText }));

		// 初始化数据

		fileutil = new FileUtils();
		pathsdcard = this.fileutil.getSDPATH();
		daoxu = new GetTotalPack();

		// filepath = pathsdcard + "alldata/setcard.txt";
		// cardata = readFile(filepath).split("/");
		//
		// String setdata = cardata[1];
		// qlbzts = setdata.substring(44, 48);
		// String qlbztsstr = String.valueOf(Integer.parseInt(
		// daoxu.gettotalpack(qlbzts, 4), 16) / 10.0);
		// maxql = setdata.substring(56, 60);
		// String maxqlstr = String.valueOf(Integer.parseInt(
		// daoxu.gettotalpack(maxql, 4), 16));
		// gfz = setdata.substring(60, 64);
		// String gfzstr = String.valueOf(Integer.parseInt(
		// daoxu.gettotalpack(gfz, 4), 16));
		// tzl = setdata.substring(96, 100);
		// String tzlstr = String.valueOf(Integer.parseInt(
		// daoxu.gettotalpack(tzl, 4), 16));
		// sbz = setdata.substring(106, 108);
		// String sbzstr = String.valueOf(Integer.parseInt(
		// daoxu.gettotalpack(sbz, 4), 16));
		//
		// cs = new String[] { "量不足提示值" + qlbztsstr, "表内最大存量" + maxqlstr,"关阀值" +
		// gfzstr, "透支值" + tzlstr, "死表状态" + sbzstr };

		recardrecevie = new RwCardRecevie();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction("android.intent.tiny.rwcard");
		getActivity().registerReceiver(recardrecevie, filter2);

		// 设置点击事件
		localGridView.setOnItemClickListener(new ItemClickListener());

		return cbLayout;
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
//				 crcmsg = StrToHex.ToHexString("30CF");
//				 ordermsg = "READ[30CF" + crcmsg + "]\r";

			}
			
			if (str.equals("校验密码")) {
				ordertype = "psvtype";
				crcmsg=StrToHex.ToHexString("A3752C");
				ordermsg="PSV[A3752C"+crcmsg+"]\r";

			}

			if (str.equals("清除数据")) {
				ordertype = "clearcard";
				crcmsg = StrToHex.ToHexString("3004");
				ordermsg = "READ[3004" + crcmsg + "]\r";

			}

			if (str.equals("设置卡")) {
				// AlertDialog.Builder builder = new Builder(
				// getActivity());
				//
				// builder.setTitle("这是一张设置卡");
				// builder.setItems(cs, new DialogInterface.OnClickListener() {
				//
				// public void onClick(DialogInterface arg0, int arg1) {
				//
				// }
				//
				// }).show();
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
				// crcmsg = StrToHex.ToHexString("3004");
				// ordermsg = "READ[3004" + crcmsg + "]\r";
				crcmsg = StrToHex.ToHexString("30CF");
				ordermsg = "READ[30CF" + crcmsg + "]\r";
				
			

			}

			intent.putExtra("order", ordermsg);
			intent.setClass(getActivity(), BtXiMeiService.class);
			getActivity().startService(intent);

		}
	}

	public class RwCardRecevie extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			backmsg = intent.getStringExtra("backmsg").trim();

			if (backmsg.equals("FFFFFFFF")) {
				// 空卡密码
				orderpsw = "FFFFFF";
			} else if (backmsg.equals("30300000")) {
				// 管理卡密码
				// orderpsw = "DC2514";
				orderpsw = setcardmsg.substring(24, 30);

			} else if (backmsg.equals("10100000")) {
				// 恢复卡密码
				// orderpsw = "E0EFD4";
				orderpsw = setcardmsg.substring(32, 38);

			} else if (backmsg.equals("20200000")) {
				// 设置卡密码，时间卡密码，测试卡密码
				orderpsw = "704032";

			} else if (backmsg.equals("40400000")) {
				// 换表卡密码
				// orderpsw = "CE4332";
				orderpsw = setcardmsg.substring(48, 54);

			} else if (backmsg.equals("50500000")) {
				// 用户卡密码
				orderpsw = setcardmsg.substring(16, 22);
			}
			//Log.e("test", "卡密码"+orderpsw);

			// 判断卡类型
			if (ordertype.equals("rdtype")) {
				if (backmsg.equals("FFFFFFFF")) {
					cardhint.setText("这是一张空卡");

				} else if (backmsg.equals("30300000")) {

					cardhint.setText("这是一张管理卡");

				} else if (backmsg.equals("10100000")) {

					cardhint.setText("这是一张恢复卡");

				} else if (backmsg.equals("20200000")) {

					cardhint.setText("这是一张设置卡");

				} else if (backmsg.equals("40400000")) {

					cardhint.setText("这是一张换表卡");

				} else if (backmsg.equals("50500000")) {
					cardhint.setText("这是一张用户卡");

				}

			}
			// 清除卡数据
			if (ordertype.equals("clearcard")) {

				if (backmsg.equals("OK")) {

					cardhint.setText("清除成功");

				} else if (backmsg.equals("FAIL")) {
					cardhint.setText("清除失败");
				} else {
                     Log.e("test", "写卡密码"+orderpsw);
					crcmsg = StrToHex.ToHexString(orderpsw);
					ordermsg = "CLEAR[" + orderpsw + crcmsg + "]\r";
					Log.e("test", ordermsg);

					intent.putExtra("order", ordermsg);
					intent.setClass(getActivity(), BtXiMeiService.class);
					getActivity().startService(intent);
				}

			}

			if (ordertype.equals("managecard")) {
				// 管理卡

				if (backmsg.equals("OK")) {

					cardhint.setText("管理卡制作完成");

				} else if (backmsg.equals("FAIL")) {
					cardhint.setText("管理卡制作失败");
				} else {
					 String glpsw=setcardmsg.substring(24, 30);
					String managemsg = "30300000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF3030FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
					crcmsg = StrToHex.ToHexString("FFFFFF"+glpsw+30 + managemsg);
					ordermsg = "WCWPSW[FFFFFF"+glpsw+30 + managemsg + crcmsg
							+ "]\r";
					Log.e("test", ordermsg);
					intent.putExtra("order", ordermsg);
					intent.setClass(getActivity(), BtXiMeiService.class);
					getActivity().startService(intent);
				}

			}

			if (ordertype.equals("usercard")) {
				// 用户卡

				if (backmsg.equals("OK")) {

					cardhint.setText("用户卡制作完成");

				} else if (backmsg.equals("FAIL")) {
					cardhint.setText("用户卡制作失败");
				} else {
					try{
					usercardmsg = backmsg+"FF";
					// Log.e("test", "读出用户卡原始信息"+usercardmsg);
					Log.e("test", String.valueOf(usercardmsg.length()));
					String userid = daoxu.gettotalpack(
							usercardmsg.substring(8, 16), 8);
					String czcs = daoxu.gettotalpack(
							usercardmsg.substring(88, 92), 4);
					String jmpsw=usercardmsg.substring(80, 88);
					Log.e("test", "用户ID" + userid);
					Log.e("test", "充值次数" + czcs);
					Log.e("test", "加密密钥"+jmpsw);

					urlpath = "http://"+ipadd+":8080/gashub/api/imeter/query?userCode="+ userid + "&payNum=" + czcs+"&content="+usercardmsg+"&meterType=00";
                    
                    progressdialog =ProgressDialog.show(getActivity(),"读卡请求", "正在读卡请等待...");
					new Thread(new DowNetString()).start();
					new Thread(new TimeOutThread()).start();

					}catch(Exception e){
						Log.e("test", e.toString());
					}

				}

			}

			if (ordertype.equals("setcard")) {
				// 设置卡

				if (backmsg.equals("OK")) {

					cardhint.setText("设置卡制作完成	");

				} else if (backmsg.equals("FAIL")) {
					cardhint.setText("设置卡制作失败");
				} else {
					
					crcmsg = StrToHex.ToHexString("FFFFFF70403230" + setcardmsg);
					ordermsg = "WCWPSW[FFFFFF70403230" + setcardmsg + crcmsg
							+ "]\r";
					// Log.e("test", ordermsg);
					intent.putExtra("order", ordermsg);
					intent.setClass(getActivity(), BtXiMeiService.class);
					getActivity().startService(intent);
				}

			}

			if (ordertype.equals("restcard")) {
				// 恢复卡

				if (backmsg.equals("OK")) {

					cardhint.setText("恢复卡制作完成");

				} else if (backmsg.equals("FAIL")) {
					cardhint.setText("恢复卡制作失败");
				} else {
					String hfpsw=setcardmsg.substring(32, 38);
					String managemsg = "10100000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF1010FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF0000FFFFFFFF0DFFFFFFFFFFFFFFFFFFFF";

					crcmsg = StrToHex.ToHexString("FFFFFF"+hfpsw+30 + managemsg);
					ordermsg = "WCWPSW[FFFFFF"+hfpsw+30 + managemsg + crcmsg
							+ "]\r";
					Log.e("test", ordermsg);
					intent.putExtra("order", ordermsg);
					intent.setClass(getActivity(), BtXiMeiService.class);
					getActivity().startService(intent);
				}

			}
			if (ordertype.equals("tradecard")) {
				// 换表卡

				if (backmsg.equals("OK")) {

					cardhint.setText("换表卡制作完成");

				} else if (backmsg.equals("FAIL")) {
					cardhint.setText("换表卡制作失败");
				} else {
					
					String hbpsw=setcardmsg.substring(48, 54);
					String managemsg = "40400000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFBF40FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF0000FFFFFFFF0DFFFFFFFFFFFFFFFFFFFF";

					crcmsg = StrToHex.ToHexString("FFFFFF"+hbpsw+30 + managemsg);
					ordermsg = "WCWPSW[FFFFFF"+hbpsw+30 + managemsg + crcmsg
							+ "]\r";
					Log.e("test", ordermsg);
					intent.putExtra("order", ordermsg);
					intent.setClass(getActivity(), BtXiMeiService.class);
					getActivity().startService(intent);
				}

			}

		}

	}

	class DowNetString implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			try {

				String netString = HtmlService.gethtml(urlpath);
				JSONObject dataJson = new JSONObject(netString);
				String statusCode = dataJson.getString("statusCode");
				String backmsg = dataJson.getString("message");

				JSONObject attrs = dataJson.getJSONObject("attrs");
				String meterInfo = attrs.getString("meterInfo");
				//Log.e("test", meterInfo);
			
				if (statusCode.equals("200")) {
					//Log.e("test", meterInfo);
					Message message = NetHandler.obtainMessage();
					message.what = 1;
					Bundle b = new Bundle();
					b.putString("meterInfo", meterInfo);
					message.setData(b);
					NetHandler.sendMessage(message);
				}

				

			} catch (Exception e) {
				Log.e("test", e.toString());
				
			}

		}

	}

	Handler NetHandler = new Handler() {

		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				progressdialog.dismiss();
				Bundle b= msg.getData();
				String gasPayment=b.getString("meterInfo");
				Log.e("test", gasPayment);
			    Intent intent = new Intent();
			    intent.putExtra("ipadd", ipadd);
			    intent.putExtra("paymentmsg", gasPayment);
			    intent.putExtra("usercardmsg", usercardmsg);
			    intent.setClass(getActivity(), GasPaymentActivity.class);
			    getActivity().startActivity(intent);
			    

				

			}
			if(msg.what==2){
				progressdialog.dismiss();
				cardhint.setText("请求超时");
			}

		}

	};

	private class TimeOutThread implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(6000);
				Message msg = new Message();
				msg.what = 2;
				NetHandler.sendMessage(msg);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

}
