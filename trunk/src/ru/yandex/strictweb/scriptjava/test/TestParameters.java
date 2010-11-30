package ru.yandex.strictweb.scriptjava.test;

import java.util.List;
import java.util.Vector;


class Param {
	String get() {
		return "Hello";
	}
}

class ParamChild extends Param {
	String get() {
		return "World";
	}
	
	String get2() {
		return get();
	}
}

class ParamType<K extends Param> {
	K ret(K o) {
		return o;
	}
}

@SkipTest
public class TestParameters {
	public boolean test() {
		return testParamType() && testParamMethod();
	}

	static <T extends Param, K extends ParamType<T>> T ret(T s, K o) {
		return o.ret(s);
//		return null;
	}
	
	private boolean testParamMethod() {
		return ret(new ParamChild(), new ParamType<ParamChild>()).get2().substring(1) == "ello";
//		return true;
	}

	private boolean testParamType() {
		List<ParamType<Param>> list = new Vector<ParamType<Param>>();
		
		list.add(new ParamType<Param>());
		
		ParamType<Param> pt = new ParamType<Param>();
		
		String s = list.get(0).ret(new ParamChild() {
			String get() {
				return "Hello " + super.get();
			}
		}).get().substring(2);
		
		UnitTest.println(s);
		
		return s == "llo World";
	}
}
