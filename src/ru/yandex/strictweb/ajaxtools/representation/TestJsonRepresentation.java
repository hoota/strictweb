package ru.yandex.strictweb.ajaxtools.representation;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.junit.Assert;
import org.junit.Test;

import ru.yandex.strictweb.ajaxtools.annotation.Arguments;
import ru.yandex.strictweb.ajaxtools.representation.XmlRePresentation.EntityFinder;

public class TestJsonRepresentation {

	private static final EntityFinder entityFinder = new EntityFinder() {
		@Override
		public Object find(Class<?> clazz, Object primaryKey) {
			if(clazz == TestBean.class && primaryKey.equals(1234L)) {
				TestBean bean = new TestBean();
				bean.id = 1234L;
				bean.name = "entityFinderName";
				return bean;
			}
			throw new RuntimeException("Cant create bean " + clazz + "(" + primaryKey + ")");
		}
	};

	@Arguments(description="[1, 2]")
	void intTest(int a, Integer b) {
		Assert.assertEquals(1, a);
		Assert.assertEquals(2, b.intValue());
	}
	
	@Arguments(description="[\"a\tsd\", \"1\n23\"]")
	void stringTest(String a, String b) {
		Assert.assertEquals("a\tsd", a);		
		Assert.assertEquals("1\n23", b);		
	}
	
	@Arguments(description="[1, \"y\", \"true\", null]")
	void stringBoolean(boolean a, boolean b, boolean c, Boolean d) {
		Assert.assertTrue(a);		
		Assert.assertTrue(b);		
		Assert.assertTrue(c);		
		Assert.assertTrue(d == null);		
	}
	
	public static enum TestEnum {AAA, BBB};
	
	@Arguments(description="[\"AAA\", \"BBB\"]")
	void stringEnum(TestEnum a, TestEnum b) {
		Assert.assertEquals(TestEnum.AAA, a);		
		Assert.assertEquals(TestEnum.BBB, b);		
	}
	
	@Arguments(description="[[1, 2, 3], [\"a\", \"b\", \"c\"]]")
	void listTest(List<Integer> a, List<String> b) {
		Assert.assertTrue(a.size() == 3);
		Assert.assertTrue(b.size() == 3);
		Assert.assertEquals(a.get(2).intValue(), 3);
		Assert.assertEquals(b.get(2), "c");
	}
	
	@Arguments(description="[{\"aaa\": [1, 2, 3], \"bbb\": [1, 2, 3, 4, 5]}]")
	void mapTest(Map<String, List<Integer>> map) {
		Assert.assertTrue(map.size() == 2);		
		Assert.assertTrue(map.get("aaa").size() == 3);		
		Assert.assertTrue(map.get("bbb").size() == 5);		
	}
	
	@Arguments(description="[1234567890123, \"2011.10.10 12:22\"]")
	void dateTest(Date a, Date b) {
		Assert.assertTrue(a.getTime() == 1234567890123L);
	}
	
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
	
	@Arguments(description="[{\"name\": \"aaa\", \"data\": [\"AAA\", \"BBB\"], \"bean0\":\"new\", \"bean1\":1234, \"bean2\":{\"id\":1234, \"name\": \"argName\"}}]")
	void beanTest(TestBean bean) {
		Assert.assertEquals(bean.name, "aaa");
		Assert.assertEquals(bean.data.get(1), TestEnum.BBB);
		
		Assert.assertEquals(bean.bean1.id.longValue(), 1234);
		Assert.assertEquals(bean.bean2.id.longValue(), 1234);
		
		Assert.assertEquals(bean.bean0.name, "beanName");
		Assert.assertEquals(bean.bean1.name, "entityFinderName");
		Assert.assertEquals(bean.bean2.name, "argName");
	}
	
	@Test
	public void doTest() throws Exception {
		for(Method method: this.getClass().getDeclaredMethods()) {
			Arguments nc = method.getAnnotation(Arguments.class);
			if(nc != null) {
				JsonRePresentation rep = new JsonRePresentation(entityFinder).reset(new StringReader(nc.description()));
				
				Class<?>[] parameterTypes = method.getParameterTypes();
				Type[] genericParameterTypes = method.getGenericParameterTypes();

				Object[] params = new Object[parameterTypes.length];
				if(rep.lexer.yylex() != Yytoken.TYPE_LEFT_SQUARE) throw new RuntimeException("not an array");
				
				for(int i = 0; i < parameterTypes.length; i++) {
					Class<?> clazz = parameterTypes[i];
					Type type = genericParameterTypes[i];
					
					params[i] = rep.getObject(clazz, type);
					rep.lexer.yylex();
					if(rep.lexer.type == Yytoken.TYPE_RIGHT_SQUARE) break;
					if(rep.lexer.type != Yytoken.TYPE_COMMA) throw new RuntimeException("not a ,");					
				}

				method.invoke(this, params);
			}
		}
	}
}
