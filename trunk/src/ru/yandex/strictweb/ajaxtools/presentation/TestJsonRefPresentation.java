package ru.yandex.strictweb.ajaxtools.presentation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.junit.Test;

import ru.yandex.strictweb.ajaxtools.representation.TestJsonRepresentation.TestEnum;

public class TestJsonRefPresentation {
	@Entity
	public static class TestBean {
		@Id
		public Long id;
		public String name = "beanName";
		public List<TestEnum> data;
		public TestBean bean0;
		public TestBean bean1;
		public TestBean bean2;
	}
	
	@Test
	public void doListRecurseTest() throws Exception {
		Map m = new LinkedHashMap();
		List a = new ArrayList();
		
		m.put("b", "c");
		m.put("c", "d");
		m.put("a", a);
		
		a.add("'\"Привет");
		a.add(a);
		
		a.add(m);
		
		a.add(new LinkedHashMap());
		
		a.add(new TestBean());
		a.add(new TestBean());
		
		String json = new JsonRefPresentation().toString(a);
		System.out.println(json);
	}
	
}
