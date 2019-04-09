package com.tiny.gasxm;

import java.util.ArrayList;
import java.util.HashMap;




import com.ximei.tiny.chaobiao.AlterBHActivity;
import com.ximei.tiny.chaobiao.BugHandleActivity;
import com.ximei.tiny.chaobiao.DelBiaoCeActivity;
import com.ximei.tiny.chaobiao.TargetBCActivity;
import com.ximei.tiny.chaobiao.ToolCardActivity;
import com.ximei.tiny.chaobiao.UMSetActivity;

import android.annotation.TargetApi;
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

@TargetApi(11)
public class SHWHFragment extends Fragment {
//	private static final String[] chaobiaohint ={"设表地址", "改表地址", "表初始化",
//		"出厂设置", "机电同步","气表充值", "气表透支","气价调整","死表检测","参数设置","参数读取", "删除表册","制工具卡"};
//	private static final int[] imageid ={R.drawable.sbdz,R.drawable.gbdz,R.drawable.bcsh,R.drawable.ccsz,R.drawable.jdtb,R.drawable.qbcz,R.drawable.qbtz,R.drawable.qjtz,R.drawable.qjtz,R.drawable.qjtz,R.drawable.qjtz,R.drawable.qjtz,R.drawable.qjtz};
	private static final String[] chaobiaohint ={"设表地址", "改表地址", "表初始化","出厂设置", "机电同步","阀门漏气", "干簧管坏","强磁攻击","超声波系数","关于版本","获取电压","气价调整"};
    private static final int[] imageid ={R.drawable.sbdz,R.drawable.gbdz,R.drawable.bcsh,R.drawable.ccsz,R.drawable.jdtb,R.drawable.qbcz,R.drawable.qbtz,R.drawable.qjtz,R.drawable.qjtz,R.drawable.qjtz,R.drawable.qjtz,R.drawable.qjtz};
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
  		for (int i = 0; i < chaobiaohint.length; i++) {
  			HashMap<String, Object> localHashMap = new HashMap<String, Object>();
  			localHashMap.put("ItemImage", imageid[i]);
  			localHashMap.put("ItemText", chaobiaohint[i]);
  			localArrayList.add(localHashMap);
  		}
      		 //GridView设置数据源
      	localGridView.setAdapter(new SimpleAdapter(cbLayout.getContext(), localArrayList,
      				R.layout.gridview_meun,new String[] { "ItemImage", "ItemText" }, new int[] {R.id.ItemImage, R.id.ItemText }));
      	//设置点击事件
    	localGridView.setOnItemClickListener(new ItemClickListener());
        return cbLayout;  
    }  
	 //点击相应的功能菜单进行相应的操作
		class ItemClickListener implements OnItemClickListener {
			public void onItemClick(AdapterView<?> paramAdapterView,
					View paramView, int paramInt, long paramLong) {
				String str = ((HashMap) paramAdapterView.getItemAtPosition(paramInt)).get("ItemText").toString();
				if(str.equals("关于版本")){
					intent.putExtra("overmsg",overmsg);
					intent.putExtra("bugtype", "txcssz");					
					intent.setClass(getActivity(),AboutversionActivity.class);
					startActivity(intent);					
				}
				if (str.equals("设表地址")) {
					intent.putExtra("overmsg",overmsg);
					intent.putExtra("bugtype", "sbdz");
					intent.setClass(getActivity(),BugHandleActivity.class);
					startActivity(intent);
				}
				
				if (str.equals("改表地址")) {
					intent.putExtra("overmsg",overmsg);
					intent.putExtra("shouhoutype", "gbdz");
					intent.setClass(getActivity(),AlterBHActivity.class);
					startActivity(intent);
				}
				
				if (str.equals("表初始化")) {
					intent.putExtra("overmsg",overmsg);
					intent.putExtra("bugtype", "bcsh");
					intent.setClass(getActivity(),BugHandleActivity.class);
					startActivity(intent);
				}
				
				if (str.equals("出厂设置")) {
					intent.putExtra("overmsg",overmsg);
					intent.putExtra("bugtype", "czsz");
					intent.setClass(getActivity(),BugHandleActivity.class);
					startActivity(intent);
				}
				if (str.equals("机电同步")) {
					intent.putExtra("overmsg",overmsg);
					intent.putExtra("shouhoutype", "jdtb");
					intent.setClass(getActivity(),AlterBHActivity.class);
					startActivity(intent);
				}
				if (str.equals("气价调整")) {
					intent.putExtra("overmsg",overmsg);
					intent.putExtra("shouhoutype", "PriceAdjust");
					intent.setClass(getActivity(),AlterBHActivity.class);
					startActivity(intent);
				}				
				if (str.equals("阀门漏气")) {
					intent.putExtra("overmsg",overmsg);
					intent.putExtra("bugtype", "fmlq");
					intent.setClass(getActivity(),BugHandleActivity.class);
					startActivity(intent);
				}
				if (str.equals("干簧管坏")) {
					intent.putExtra("overmsg",overmsg);
					intent.putExtra("bugtype", "ghgh");
					intent.setClass(getActivity(),BugHandleActivity.class);
					startActivity(intent);
				}
				if (str.equals("强磁攻击")) {
					intent.putExtra("overmsg",overmsg);
					intent.putExtra("bugtype", "qcgj");
					intent.setClass(getActivity(),BugHandleActivity.class);
					startActivity(intent);
				}				
				if (str.equals("删除表册")) {
					intent.setClass(getActivity(),DelBiaoCeActivity.class);
					startActivity(intent);
				}
				if(str.equals("超声波系数"))
				{
					intent.putExtra("overmsg", overmsg);
					intent.setClass(getActivity(),UMSetActivity.class);
					startActivity(intent);											
					
				}
				if (str.equals("获取电压")) {
					intent.putExtra("overmsg",overmsg);
					intent.putExtra("bugtype", "GetVoltage");
					intent.setClass(getActivity(),BugHandleActivity.class);
					startActivity(intent);
				}					
				if (str.equals("制工具卡")) {
//					intent.putExtra("overmsg",overmsg);
//					intent.setClass(getActivity(),ToolCardActivity.class);
//					startActivity(intent);
				}
				
				
			
				
			
			}
		}
	}