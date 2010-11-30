package ru.yandex.strictweb.scriptjava.compiler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
import com.sun.tools.javac.tree.JCTree.JCTypeApply;
import com.sun.tools.javac.tree.JCTree.JCWildcard;
import com.sun.tools.javac.util.Name;

public class VarType {
	static public VarType STRING = new VarType("String");
	static public VarType VOID = new VarType("void");
	static public VarType NUMBER = new VarType("number");
	static public VarType BOOLEAN = new VarType("boolean");
	static private String ARRAY = "array";
	
	private String name;
	private List<VarType> parameters;
	private VarType bound;

	private VarType(String name) {
		this.name = name;
	}
	
	public static boolean isSimpleType(JCTree type) {
		return type instanceof JCPrimitiveTypeTree || type instanceof JCIdent;
	}

	public static boolean isPrimitiveType(JCTree type) {
		return type instanceof JCPrimitiveTypeTree;
	}
	
	public static boolean isParameterizedType(JCTree type) {
		return type instanceof JCTypeApply;
	}
	

	public VarType(Name sn) {
		this.name = sn.toString();
	}
	
	public VarType(JCTree type) {
		init(type);
	}

	private void init(JCTree type) {
		if(isSimpleType(type)) {
			name = type.toString();
		} else if(type instanceof JCArrayTypeTree) {
			JCArrayTypeTree at = (JCArrayTypeTree)type;
			name = ARRAY;
			parameters = new ArrayList<VarType>(1);
			parameters.add(new VarType(at.elemtype));
		} else if(isParameterizedType(type)) {
			JCTypeApply pt = (JCTypeApply)type;
			name = pt.getType().toString();
			parameters = new ArrayList<VarType>();
			
			for(JCExpression t : pt.getTypeArguments()) {
				parameters.add(new VarType(t));
			}
		} else if(type instanceof JCWildcard) {
			JCWildcard wt = (JCWildcard)type;
			if(null == wt.getBound()) name = "?"; else init(wt.getBound());
		} else throw new RuntimeException("Unsupported type: " + type + " :: " + type.getClass());
	}

	public VarType(ParsedClass cl) {
		name = cl.name;
	}

	public VarType(VarType t) {
		name = t.name;
		parameters = t.parameters;
		bound = t.bound;
	}

	public VarType(Type gType) {
		if(gType instanceof Class) {
			Class c = (Class)gType;
			if(c.isArray()) {
				name = ARRAY;
				parameters = new ArrayList<VarType>();
				parameters.add(new VarType(c.getComponentType()));
			} else {
				name = c.getSimpleName();
			}
//			System.out.println(name + " :: " + c.isArray());
		} else {
			throw new RuntimeException("Unknown type: " + gType);
		}
	}

	public boolean isArray() {
		return nameIs(ARRAY);
	}

	public boolean nameIs(String n) {
		if(bound!=null) return bound.name.equals(n);
		return name.equals(n);
	}

	public String getName() {
		if(bound!=null) return bound.name;
		return name;
	}

	public int parametersSize() {
		if(bound!=null) return bound.parametersSize();
		return parameters.size();
	}

	public VarType parametersGet(int i) {
		if(bound!=null) return bound.parametersGet(i);
		return parameters.get(i);
	}
	
//	@Override
//	public boolean equals(Object obj) {
//		if(!(obj instanceof VarType)) return false;
//		VarType t = (VarType)obj;
//		boolean e = name.equals(t.name);
//		if(null!=parameters) e &= parameters.equals(t.parameters);
//		
//		return e;
//	}

	public VarType implement(List<VarType> find, VarType replace) {
		if(null == replace.parameters) return this;
		if(null==parameters) {
			for(int i=0; i<Math.min(find.size(), replace.parameters.size()); i++) {
				if(find.get(i).name.equals(name)) return replace.parameters.get(i);
			}
			
			return this;
		}

		VarType res = new VarType(name);
		res.parameters = new ArrayList<VarType>();
		for(VarType t : parameters) res.parameters.add(t.implement(find, replace));
		
		return res;
	}

	public static VarType ARRAY(VarType of) {
		VarType t = new VarType(ARRAY);
		t.parameters = new ArrayList<VarType>(1);
		t.parameters.add(of);
		return t;
	}
	
	@Override
	public String toString() {
		if(bound!=null) return bound.toString();
		return name + (parameters!=null ? parameters : "");
	}

	public void setName(String n) {
		name = n;
	}

	public void setBound(VarType type) {
		bound = type;
	}

	public VarType ifItIsParameter(List<VarType> params) {
		for(VarType t : params) if(name.equals(t.name)) return t;
		return this;
	}
}
