package com.ximei.tiny.tools;
import java.util.ArrayList;
import java.util.List;

public class Comparelist {
	
	public  List<Integer> compare(Integer total, List<Integer> send) {
		List<Integer> lost = new ArrayList<Integer>();
		for(int i=0; i<total; i++) {
			lost.add(i);
		}
//		总共的减去发送过来的（接收到的）等于丢失的
		for(Integer i : send) {
			if(lost.contains(i)) {
				lost.remove(i);
			}
		}
		
		return lost;
	}
	
	

}
