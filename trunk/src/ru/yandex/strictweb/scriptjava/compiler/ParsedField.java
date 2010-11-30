package ru.yandex.strictweb.scriptjava.compiler;

import com.sun.tools.javac.tree.JCTree;


public class ParsedField {
	public String name;
	public VarType type;
	public ParsedClass cl;
	
	public ParsedField(String name, JCTree type, ParsedClass cl) {
		this.name = name;
		this.type = new VarType(type).ifItIsParameter(cl.parameters);
		this.cl = cl;
	}
	
	public ParsedField(String name, VarType type, ParsedClass cl) {
		this.name = name;
		this.type = type;
		this.cl = cl;
	}

	public ParsedField(ParsedField f, VarType t) {
		name = f.name;
		cl = f.cl;
		type = t;
	}
}
