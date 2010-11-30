package ru.yandex.strictweb.scriptjava.compiler;

import java.util.List;

import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;

public interface CompilerPlugin {

	boolean invokeMethod(String mName, JCMethodInvocation inv, List arguments);

	void setParser(Parser parser);

	boolean compileClassFieldsAndMethods(ParsedClass cl);

	boolean compileClassInitializers(ParsedClass cl);
	
}
