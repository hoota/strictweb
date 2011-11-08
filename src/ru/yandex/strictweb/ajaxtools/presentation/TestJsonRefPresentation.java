package ru.yandex.strictweb.ajaxtools.presentation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TestJsonRefPresentation {

	@Test
	public void doListRecurseTest() throws Exception {
		Map m = new LinkedHashMap();
		List a = new ArrayList();
		
		m.put("b", "c");
		m.put("a", a);
		
		a.add("Hello");
		a.add(a);
		
		a.add(m);
		
		String json = new JsonRefPresentation().toString(a);
		System.out.println(json);
	}
	
}
