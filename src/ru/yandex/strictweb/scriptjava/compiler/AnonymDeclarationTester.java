package ru.yandex.strictweb.scriptjava.compiler;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree.JCNewClass;

public class AnonymDeclarationTester {
	
	private class AnonymException extends Exception {
		public AnonymException(String message) {
			super(message);
		}
	}
	
	public boolean test(Object node) {
		
		try {
			testRecurcive(node);
		}catch(AnonymException e) {
			return true;
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		return false;
	}
	
	private void testRecurcive(Object tree) throws Exception {
		if(null == tree) return;
		
		if(tree instanceof com.sun.tools.javac.tree.JCTree.JCNewClass) {
			com.sun.tools.javac.tree.JCTree.JCNewClass nc = (JCNewClass)tree;
			if(nc.def != null) throw new AnonymException("Anonym found :: " + tree);
		}
		
		if(tree instanceof List) {
			for(Object o : (List)tree) testRecurcive(o);
		} else if(tree instanceof Tree) {
			for(Field f : tree.getClass().getFields()) {
				if((f.getModifiers()&(Modifier.PUBLIC|Modifier.STATIC)) == Modifier.PUBLIC) {
					testRecurcive(f.get(tree));
				}
			}
		}
	}
}
