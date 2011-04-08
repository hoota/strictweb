package ru.yandex.strictweb.scriptjava.test;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import ru.yandex.strictweb.scriptjava.base.StrictWeb;
import ru.yandex.strictweb.scriptjava.base.ajax.Ajax;

public class TestObjectToXml {
	String str = "qqq";
	boolean bool = true;
	Date date = new Date();
	List<Object> array = null;
	
	public boolean test() {
		array = new Vector<Object>();
		array.add(new TestObjectToXml());
		array.add("Hello");
		array.add(new int[] {1, 2,3});
		array.add(null);
		
		String res = Ajax.objectToXml(this, null);
		String d = StrictWeb.dateToStringSmart(date);
		
		return res == "<o><a id=\"array\"><o><s id=\"str\">qqq</s><b id=\"bool\">1</b><d id=\"date\">"+d+"</d></o><s>Hello</s><a><n>1</n><n>2</n><n>3</n></a><null/></a><s id=\"str\">qqq</s><d id=\"date\">"+d+"</d><b id=\"bool\">1</b></o>";
	}
}
