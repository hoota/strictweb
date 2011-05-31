package ru.yandex.strictweb.scriptjava.compiler;

import java.util.IdentityHashMap;

import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import ru.yandex.strictweb.scriptjava.base.MayBeExcluded;

public class ParsedMethod {
    private static final ParsedMethod UNKNOWN = new ParsedMethod();
    
    private static long counter = 0;
    
    public final String id;
    public final Counter invokeCounter;
    public final boolean mayBeExcluded;
	public final String name;
	public final VarType retType;
	public final ParsedClass cl;
	public final JCMethodDecl decl;

	public ParsedMethod(String name, JCMethodDecl decl, ParsedClass cl, VarType retType) {
        id = System.currentTimeMillis() + "-" + (++counter);
		this.name = name;
		this.retType = null!=decl ? new VarType(decl.getReturnType()).ifItIsParameter(cl.parameters) : retType;
		
		mayBeExcluded = null!=decl ? Parser.hasAnnotation(MayBeExcluded.class.getSimpleName(), decl.getModifiers()) : false;
		
		this.decl = decl;
		this.cl = cl;
		invokeCounter = new Counter();
		
	}
	
	
	public ParsedMethod(ParsedMethod m, VarType newRetType) {
        id = m.id;
		name = m.name;
		cl = m.cl;
		decl = m.decl;
		retType = newRetType;
		mayBeExcluded = m.mayBeExcluded;
		invokeCounter = m.invokeCounter;
	}
	
	private ParsedMethod() {
        id = "null";
        name = "-null-";
        cl = null;
        decl = null;
        retType = VarType.VOID;
        mayBeExcluded = false;
        invokeCounter = new Counter();
        invokeCounter.count = 1;
    }
	
	@Override
	public String toString() {
	    return cl.name + "::" + name;
	}

    static class Counter {
	    public int count = 0;
	    public final IdentityHashMap<ParsedMethod, Boolean> invokers = new IdentityHashMap<ParsedMethod, Boolean>();
	    public void inc(ParsedMethod currentParsedMethod) {
	        invokers.put(currentParsedMethod == null ? UNKNOWN : currentParsedMethod, Boolean.TRUE);
	        count ++;
	    }
	}
}
