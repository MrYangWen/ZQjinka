package com.tiny.gasxm;

import java.util.ArrayList;
import java.util.HashMap;

import com.ximei.tiny.chaobiao.AlterBHActivity;
import com.ximei.tiny.chaobiao.BugHandleActivity;
import com.ximei.tiny.chaobiao.GroupCBFSActivity;
import com.ximei.tiny.chaobiao.ReadFaultActivit;
import com.ximei.tiny.chaobiao.ReadHistoryActivity;
import com.ximei.tiny.chaobiao.SingleCBActivity;
import com.ximei.tiny.chaobiao.TargetBCActivity;
import com.ximei.tiny.chaobiao.TargetBCActivity01;
import com.ximei.tiny.chaobiao.YCbiaocelist;
import com.ximei.tiny.collector.CaiJiBiaoCeActivity;
import com.ximei.tiny.wlwmeter.MainWLWMeterActivity;
import com.ximei.tiny.wlwmeter.WLWBugHandleActivity;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

@TargetApi(11)
public class CBFragment extends Fragment {

//	private static final String[] chaobiaohint = { "单个抄表", "表册抄表", "采集数据","抄表统计", "强制关阀", "取消强关","改表地址","物联网表", "上传表册" };
//	private static final int[] imageid = { R.drawable.dgcb, R.drawable.bccb,
//			R.drawable.cjsj, R.drawable.cbxxtj,R.drawable.qzgf,R.drawable.qxqg,R.drawable.gbdz,R.drawable.cjzwbh,R.drawable.scbc };
	private static final String[] chaobiaohint = {"单个抄表","表册抄表", "采集数据","强制关阀", "取消强关","物联网表", "上传表册","写RTC","读RTC","读历史纪录","多次抄表","设表地址","出厂设置","表唤醒","恢复出厂","故障记录"};
	private static final int[]    imageid = {R.drawable.dgcb,R.drawable.bccb,R.drawable.cjsj,R.drawable.qzgf,R.drawable.qxqg,R.drawable.cjzwbh,R.drawable.scbc,R.drawable.dgcb,R.drawable.dgcb ,R.drawable.dgcb,R.drawable.dgcb,R.drawable.dgcb,R.drawable.dgcb,R.drawable.dgcb,R.drawable.dgcb,R.drawable.dgcb};

	private Intent intent;
	private String overmsg;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View cbLayout = inflater.inflate(R.layout.cb, container, false);
		GridView localGridView = (GridView) cbLayout
				.findViewById(R.id.gridview);

		overmsg = getArguments().getString("overmsg");
		Log.e("test", overmsg);

		intent = getActivity().getIntent();

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
			if (str.equals("单个抄表")) {

				intent.putExtra("overmsg", overmsg);
				intent.putExtra("function", "singlecb");
				intent.setClass(getActivity(), SingleCBActivity.class);
				CBFragment.this.startActivity(intent);
			}
			if (str.equals("多次抄表")) {

				intent.putExtra("overmsg", overmsg);
				intent.putExtra("function", "singlecb1");
				intent.setClass(getActivity(), SingleCBActivity.class);
				CBFragment.this.startActivity(intent);
			}
			if (str.equals("表册抄表")) {

				intent.putExtra("overmsg", overmsg);
				intent.setClass(getActivity(), GroupCBFSActivity.class);
				CBFragment.this.startActivity(intent);
			}
			if (str.equals("采集数据")) {

				intent.putExtra("caijitype", "caijidata");
				intent.putExtra("overmsg", overmsg);
				intent.setClass(getActivity(),CaiJiBiaoCeActivity.class);
				 CBFragment.this.startActivity(intent);
			}
			if (str.equals("抄表统计")) {
				intent.putExtra("cbtype", "infoview");
				intent.putExtra("overmsg", overmsg);
				intent.setClass(getActivity(), TargetBCActivity01.class);
				CBFragment.this.startActivity(intent);
			}
			if (str.equals("强制关阀")) {
				intent.putExtra("overmsg", overmsg);
				intent.putExtra("bugtype", "qcgf");
				intent.setClass(getActivity(),BugHandleActivity.class);
				CBFragment.this.startActivity(intent);
			}

			if (str.equals("取消强关")) {
				intent.putExtra("overmsg", overmsg);
				intent.putExtra("bugtype", "qxqg");
				intent.setClass(getActivity(),BugHandleActivity.class);
				CBFragment.this.startActivity(intent);
			}
			
			if (str.equals("物联网表")) {
				
				intent.setClass(getActivity(),WLWBugHandleActivity.class);
				CBFragment.this.startActivity(intent);
			}
			
			if (str.equals("上传表册")) {
//				intent.putExtra("overmsg", overmsg);
//				intent.putExtra("bugtype", "qxqg");
				intent.setClass(getActivity(),YCbiaocelist.class);
				CBFragment.this.startActivity(intent);
			}
			if (str.equals("删除表册")) {
				intent.putExtra("overmsg",overmsg);
				intent.putExtra("shouhoutype", "scbc");
				intent.setClass(getActivity(),AlterBHActivity.class);
				startActivity(intent);
			}
			if (str.equals("写RTC")) {
				intent.putExtra("overmsg",overmsg);
				intent.putExtra("bugtype", "xrtc");
				intent.setClass(getActivity(),BugHandleActivity.class);
				startActivity(intent);
			}
			if (str.equals("读RTC")) {
				intent.putExtra("overmsg",overmsg);
				intent.putExtra("bugtype", "drtc");
				intent.setClass(getActivity(),BugHandleActivity.class);
				startActivity(intent);
			}
			if (str.equals("读历史纪录")) {
				intent.putExtra("overmsg",overmsg);
				intent.putExtra("bugtype", "history");
				intent.setClass(getActivity(),ReadHistoryActivity.class);
				startActivity(intent);
			}
			if (str.equals("设表地址")) {
				intent.putExtra("overmsg",overmsg);
				intent.putExtra("bugtype", "sbdz");
				intent.setClass(getActivity(),ReadHistoryActivity.class);
				startActivity(intent);
			}
			if (str.equals("出厂设置")) {
				intent.putExtra("overmsg",overmsg);
				intent.putExtra("bugtype", "ccsz");
				intent.setClass(getActivity(),ReadHistoryActivity.class);
				startActivity(intent);
			}
			if (str.equals("表唤醒")) {
				intent.putExtra("overmsg",overmsg);
				intent.putExtra("bugtype", "bhx");
				intent.setClass(getActivity(),ReadHistoryActivity.class);
				startActivity(intent);
			}
			if (str.equals("恢复出厂")) {
				intent.putExtra("overmsg",overmsg);
				intent.putExtra("bugtype", "hhcc");
				intent.setClass(getActivity(),ReadHistoryActivity.class);
				startActivity(intent);
			}
			if (str.equals("故障记录")) {
				intent.putExtra("overmsg",overmsg);
				intent.putExtra("bugtype", "gzjl");
				intent.setClass(getActivity(),ReadFaultActivit.class);
				startActivity(intent);
			}
		}
	}

}
