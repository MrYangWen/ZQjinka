package com.tiny.gasxm;

import java.util.ArrayList;
import java.util.HashMap;





import com.ximei.tiny.chaobiao.AlterBHActivity;
import com.ximei.tiny.chaobiao.TargetBCActivity01;
import com.ximei.tiny.collector.CaiJiBiaoCeActivity;
import com.ximei.tiny.collector.CaiJiInputActivity;
import com.ximei.tiny.collector.SetMeterParActivity;
import com.ximei.tiny.collector.SetzjqParActivity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class AMRFragment extends Fragment {
	private static final String[] shouhouhint = {"设置中继器参数","设置地址", "抄表统计","查询状态","采集数据", "设置时间", "组网测试", "抄表测试", "删除表号", "添加单只表号","组网统计","单次采集数据","无须组网传输表号","调价参数设置"};
	private static final int[] imageid ={R.drawable.szzjqdz,R.drawable.szzjqdz,R.drawable.cbxxtj,R.drawable.cxzt,R.drawable.cjsj,R.drawable.szsj,R.drawable.zwcs,R.drawable.csbh,R.drawable.scbh,R.drawable.zwtj,R.drawable.zwtj,R.drawable.cjsj,R.drawable.wxzwcsbh,R.drawable.szzjqdz};
	private Intent intent;
	private String overmsg;
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {  
        View cbLayout = inflater.inflate(R.layout.cb,container, false); 
        GridView localGridView=(GridView)cbLayout.findViewById(R.id.gridview);   
        intent = getActivity().getIntent();
        overmsg = getArguments().getString("overmsg");
      //用list<map>存放所用功能菜单作为数据源
      		ArrayList<HashMap<String, Object>> localArrayList = new ArrayList<HashMap<String, Object>>();
      		for (int i = 0; i < shouhouhint.length; i++) {
      			HashMap<String, Object> localHashMap = new HashMap<String, Object>();
      			localHashMap.put("ItemImage",imageid[i]);
      			localHashMap.put("ItemText", shouhouhint[i]);
      			localArrayList.add(localHashMap);
      		}
      		 //GridView设置数据源
      		localGridView.setAdapter(new SimpleAdapter(cbLayout.getContext(), localArrayList,R.layout.gridview_meun,
      				new String[] { "ItemImage", "ItemText" }, new int[] {R.id.ItemImage, R.id.ItemText }));
      		localGridView.setOnItemClickListener(new ItemClickListener());
        
        
        return cbLayout;  
    } 
	// 点击相应的功能菜单进行相应的操作
		class ItemClickListener implements OnItemClickListener {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				String str = ((HashMap) arg0.getItemAtPosition(arg2)).get("ItemText").toString();
				if (str.equals("设置时间"))
			      {
					 Intent intent = new Intent(getActivity(),CaiJiInputActivity.class);
			        intent.putExtra("caijitype", "settime");
			        intent.putExtra("overmsg", overmsg);
			        intent.setClass(getActivity(), CaiJiInputActivity.class);
			        startActivity(intent);
			      }
			      if (str.equals("查询状态"))
			      {
			       intent.putExtra("caijitype", "queryzt");
			       intent.putExtra("overmsg", overmsg);
			       intent.setClass(getActivity(), CaiJiInputActivity.class);
			       startActivity(intent);
			      }			      
			      if (str.equals("删除表号"))
			      {
			        intent.putExtra("caijitype", "delbh");
			        intent.putExtra("overmsg", overmsg);
			        intent.setClass(getActivity(), CaiJiInputActivity.class);
			        startActivity(intent);
			      }
			      if (str.equals("添加单只表号"))
			      {
			        intent.putExtra("caijitype", "addbh");
			        intent.putExtra("overmsg", overmsg);
			        intent.setClass(getActivity(), CaiJiInputActivity.class);
			        startActivity(intent);
			      }			      
			      if (str.equals("设置中继器参数"))
			      {
			        intent.putExtra("shouhoutype", "szzjqcs");
			        intent.putExtra("overmsg", overmsg);
			        intent.setClass(getActivity(), SetzjqParActivity.class);
			        startActivity(intent);
			      }
			      if (str.equals("调价参数设置"))
			      {
			        intent.putExtra("shouhoutype", "szbdcs");
			        intent.putExtra("overmsg", overmsg);
			        intent.setClass(getActivity(), SetMeterParActivity.class);
			        startActivity(intent);
			      }				      
			      if (str.equals("设置地址"))
			      {
			        intent.putExtra("shouhoutype", "alterjzq");
			        intent.putExtra("overmsg", overmsg);
			        intent.setClass(getActivity(), AlterBHActivity.class);
			        startActivity(intent);
			      }
				  if (str.equals("抄表统计")) {
						intent.putExtra("cbtype", "infoview");
						intent.putExtra("overmsg", overmsg);
						intent.setClass(getActivity(), TargetBCActivity01.class);
						startActivity(intent);
				  }     
			      if (str.equals("采集数据"))
			      {
			        intent.putExtra("caijitype", "caijidata");
			        intent.putExtra("overmsg", overmsg);
			        intent.setClass(getActivity(), CaiJiBiaoCeActivity.class);
			        startActivity(intent);
			      }
			      if (str.equals("组网测试"))
			      {
			       intent.putExtra("caijitype", "caijizwcs");
			        intent.putExtra("overmsg", overmsg);
			        intent.setClass(getActivity(), CaiJiBiaoCeActivity.class);
			        startActivity(intent);
			      }
			      if (str.equals("抄表测试"))
			      {
//			        intent.putExtra("caijitype", "caijicsbh");
//			        intent.putExtra("overmsg", overmsg);
//			        intent.setClass(getActivity(), CaiJiBiaoCeActivity.class);
//			        startActivity(intent);
			        intent.putExtra("caijitype", "cbcs");
			        intent.putExtra("overmsg", overmsg);
			        intent.setClass(getActivity(), CaiJiInputActivity.class);
			        startActivity(intent);			    	  
			      }
			    
			      if (str.equals("组网统计"))
			      {
			        intent.putExtra("caijitype", "caijizwtj");
			        intent.putExtra("overmsg", overmsg);
			        intent.setClass(getActivity(), CaiJiBiaoCeActivity.class);
			        startActivity(intent);
			      }
			      
			      if (str.equals("单次采集数据"))
			      {
			    	    intent.putExtra("shouhoutype", "dccjsj");
				        intent.putExtra("overmsg", overmsg);
				        intent.setClass(getActivity(), AlterBHActivity.class);
				        startActivity(intent);
			      }
			      
			      if (str.equals("无须组网传输表号"))
			      {
			        intent.putExtra("caijitype", "nozwcsbh");
			        intent.putExtra("overmsg", overmsg);
			        intent.setClass(getActivity(), CaiJiBiaoCeActivity.class);
			        startActivity(intent);
			      }
			     
			      
			    }
			  }
			}
