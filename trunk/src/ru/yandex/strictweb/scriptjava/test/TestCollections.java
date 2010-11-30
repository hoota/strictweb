package ru.yandex.strictweb.scriptjava.test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

public class TestCollections {
	public boolean test() {
		return testArrayAndVector() && testStringAndVector() && testSet() && testMap();
	}

	String a = "Hello world!";

	private boolean testMap() {
		Map<String, Integer> map = new TreeMap<String, Integer>();
		
		for(int i=0; i<a.length(); i++) {
			String k = a.charAt(i) + "";
			if(map.get(k) != null) {
				map.put(k, map.get(k) + 1);
			} else {
				map.put(k, 1);
			}
		}
		
		return map.get("l") == 3 && map.get("?") == null;
	}

	private boolean testSet() {
		Set<String> set = new TreeSet<String>();
	
		for(int i=0; i<a.length(); i++) {
			set.add(a.toLowerCase().charAt(i) + "");
		}
		
		String b = "";
		for(String s : set) {
			b += s;
		}
		
		return b == "wd e!hlor";
	}

	private boolean testStringAndVector() {
		List<String> list = new Vector<String>();
		
		for(int i=0; i<a.length(); i++) {
			list.add(a.charAt(i) + "");
		}
		
		String b = "";
		
		for(String s : list) b += s;
		
		return a == b;
	}

	private boolean testArrayAndVector() {
		int[] inta = new int[10];
		List<Integer> intv = new Vector<Integer>();
		
		for(int i = 0; i<10; i++) {
			inta[i] = i;
			intv.add(i);
		}

		int sum =  0;
		
		for(int i = 0; i<10; i++) {
			sum += inta[i] + intv.get(i);
		}
		
		for(Integer i : intv) sum -= i*2;
		
		return
			intv.size() == 10 &&
			inta.length == intv.size()
			&& sum == 0
		;
	}
}
