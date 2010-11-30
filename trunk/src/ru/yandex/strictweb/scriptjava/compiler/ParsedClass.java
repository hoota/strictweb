package ru.yandex.strictweb.scriptjava.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

public class ParsedClass {
	public String name;
	public char[] sourceCode;
	public String fileName;
	public Map<String, ParsedField> fields = new TreeMap<String, ParsedField>();
	public Map<String, ParsedMethod> methods = new TreeMap<String, ParsedMethod>();
	public JCClassDecl type;
	public JCMethodDecl constructor;
	public boolean isNative;
	public boolean skipInnerObfuscation;
	public Set<String> staticFields = new TreeSet<String>();
	public Set<String> staticMethods = new TreeSet<String>();
	public VarType varType;
	public String selfPrefix;
	public List<VarType> parameters = new ArrayList<VarType>();
	public boolean isEnum = false;
	public boolean canCreateNewInstance = true;
	public ParsedClass superClass;
	public boolean compiled = false;
	public boolean isInterface = false;
	public List<String> importList;
	
	public ParsedClass(String name, JCClassDecl type) {
		this.name = name;
		this.type = type;
		varType = new VarType(this);
	}
	
	boolean hasProperty(String name) {
		return fields.containsKey(name) || methods.containsKey(name);
	}
	
	VarType getThrowParameters(VarType t) {
		return t.ifItIsParameter(parameters);
	}
	
	@Override
	public String toString() {
		return name + "::" +selfPrefix;
	}
}
