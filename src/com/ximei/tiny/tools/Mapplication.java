package com.ximei.tiny.tools;

import java.util.LinkedList;
import java.util.List;

import android.app.Application;
import android.app.Activity;

public class Mapplication extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();
	private static Mapplication instance;

	private Mapplication() {
	}

	// 单例模式中获取唯一的MyApplication实例
	public static Mapplication getInstance() {
		if (null == instance) {
			instance = new Mapplication();
		}
		return instance;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}
