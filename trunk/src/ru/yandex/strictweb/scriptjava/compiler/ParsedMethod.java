package ru.yandex.strictweb.scriptjava.compiler;

import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import ru.yandex.strictweb.scriptjava.base.MayBeExcluded;

public class ParsedMethod {
    private static long counter = 0;
    
    public final String id;
    public int invokeCount = 0;
    public final boolean mayBeExcluded;
	public String name;
	public VarType retType;
	public ParsedClass cl;
	public JCMethodDecl decl;

	public ParsedMethod(String name, JCMethodDecl decl, ParsedClass cl) {
	    id = System.currentTimeMillis() + "-" + (++counter);
		this.name = name;
		if(null!=decl) {
			this.retType = new VarType(decl.getReturnType()).ifItIsParameter(cl.parameters);
			mayBeExcluded = Parser.hasAnnotation(MayBeExcluded.class.getSimpleName(), decl.getModifiers());
			//System.out.println(cl.name+"."+name+" :: "+retType);
		} else {
		    mayBeExcluded = false;
		}
		this.decl = decl;
		this.cl = cl;
	}
	
	public ParsedMethod(ParsedMethod m, VarType newRetType) {
        id = System.currentTimeMillis() + "-" + (++counter);
		name = m.name;
		cl = m.cl;
		decl = m.decl;
		retType = newRetType;
		mayBeExcluded = false;
	}
}
