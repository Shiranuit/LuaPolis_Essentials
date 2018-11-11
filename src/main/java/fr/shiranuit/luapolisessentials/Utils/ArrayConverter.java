package fr.shiranuit.luapolisessentials.Utils;

import java.util.ArrayList;

public class ArrayConverter {
	

	public static  ArrayList<String> convert(String[] lst) {
		ArrayList<String> nlst = new ArrayList<String>();
		for (int i=0; i<lst.length; i++) {
			nlst.add(lst[i]);
		}
		return nlst;
	}
	
	public static  String[] convert(ArrayList<String> lst) {
		String[] stockArr = new String[lst.size()];
		stockArr = lst.toArray(stockArr);
		return stockArr;
	}
}
