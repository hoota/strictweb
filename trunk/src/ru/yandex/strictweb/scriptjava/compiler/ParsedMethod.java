package ru.yandex.strictweb.scriptjava.compiler;

import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

public class ParsedMethod {
	public String name;
	public VarType retType;
	public ParsedClass cl;
	public JCMethodDecl decl;

	public ParsedMethod(String name, JCMethodDecl decl, ParsedClass cl) {
		this.name = name;
		if(null!=decl) {
			this.retType = new VarType(decl.getReturnType()).ifItIsParameter(cl.parameters);
			//System.out.println(cl.name+"."+name+" :: "+retType);
		}
		this.decl = decl;
		this.cl = cl;
	}
	
	public ParsedMethod(ParsedMethod m, VarType newRetType) {
		name = m.name;
		cl = m.cl;
		decl = m.decl;
		retType = newRetType;
	}
}
