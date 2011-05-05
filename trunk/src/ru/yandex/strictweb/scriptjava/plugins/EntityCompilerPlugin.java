package ru.yandex.strictweb.scriptjava.plugins;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.sun.tools.internal.ws.wsdl.framework.Entity;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

import ru.yandex.strictweb.ajaxtools.annotation.AjaxTransient;
import ru.yandex.strictweb.ajaxtools.annotation.Presentable;
import ru.yandex.strictweb.scriptjava.base.StrictWeb;
import ru.yandex.strictweb.scriptjava.compiler.CompilerPlugin;
import ru.yandex.strictweb.scriptjava.compiler.IgnoreExtends;
import ru.yandex.strictweb.scriptjava.compiler.ParsedClass;
import ru.yandex.strictweb.scriptjava.compiler.Parser;
import ru.yandex.strictweb.scriptjava.compiler.VarType;

public class EntityCompilerPlugin implements CompilerPlugin {
	public static String LABEL = "ENTITY";
	
	Parser parser;
	Map<String, Boolean> knownClasses = new TreeMap<String, Boolean>();
	
	public boolean compileClassFieldsAndMethods(ParsedClass cl) {
		if(!isEntityClass(cl)) return false;
		cl.skipInnerObfuscation = true;
		
		parser.code.append("function " + parser.getObfuscatedName(cl));
		JCMethodDecl constructor = null;
		
		for(JCTree tr : cl.type.getMembers()) if(tr instanceof JCMethodDecl){
			JCMethodDecl m = (JCMethodDecl)tr;
			
			if(parser.isConstructor(m) && parser.hasAnnotation("AJAX", m.getModifiers()) && constructor==null) {
				constructor = m;
				//System.out.println(cl.name + " - " +m.parameters());
				parser.parseParameters("(", m.getParameters(), ") {\n");
				//compiler.parse(m.parameters());
			}
		}
		
		if(constructor==null) {
			parser.code.append("() {\n");
		}

		Map<String, JCVariableDecl> fields = new TreeMap<String, JCVariableDecl>();
		
		for(JCTree tr : cl.type.getMembers()) if(tr instanceof JCVariableDecl) {
			JCVariableDecl f = (JCVariableDecl)tr;
			if(parser.isStatic(f.getModifiers())) continue;
//			System.out.println(f);
			if(parser.hasAnnotation(AjaxTransient.class.getSimpleName(), f.getModifiers())) continue;
			if(null!=f.getInitializer()) {
				fields.put(f.getName().toString(), f);					
			}
		}
		
		for(JCTree tr : cl.type.getMembers()) if(tr instanceof JCMethodDecl) {
			JCMethodDecl m = (JCMethodDecl)tr;
			
			if(parser.isConstructor(m)) continue;
			
			//System.out.println(m.modifiers());
			String mName = m.getName().toString();
			boolean isAbstract = parser.isAbstract(m.getModifiers());
			boolean isStatic = parser.isStatic(m.getModifiers());
			boolean isAjax = !parser.hasAnnotation(AjaxTransient.class.getSimpleName(), m.getModifiers())
				|| parser.hasAnnotation("Id", m.getModifiers());

			if(isAbstract || isStatic || !isAjax) continue;
			
			if(mName.startsWith("get") && m.getParameters().size() == 0) {
				String name = Character.toLowerCase(mName.charAt(3)) + mName.substring(4);
	            fields.remove(name);
		        VarType type = cl.methods.get(mName).retType;
				printFieldInitializer(cl, name, type, fields, VarType.isPrimitiveType(m.getReturnType()));
			}
		}
		
		for(Map.Entry<String, JCVariableDecl> fe: fields.entrySet()) {
            VarType type = cl.fields.get(fe.getKey()).type;
            printFieldInitializer(cl, fe.getKey(), type, fields, VarType.isPrimitiveType(fe.getValue().getType()));		    
		}
		
//		System.out.println(cl.name + " :: " + parser.getObfuscatedName(cl) + " :: " + fields.keySet());
		
		if(constructor!=null) {
			parser.currentClass.add(cl);
			parser.currentClass.lastElement().selfPrefix = "this";
			parser.parseList(constructor.getBody().getStatements(), "");
			parser.currentClass.remove(cl);
		}
		
		parser.code.append("}\n");
		
		parser.code.append("\n");
		
		if(null!=cl.type.getExtendsClause()) {
			String superType = cl.type.getExtendsClause().toString();			
			if(!parser.hasAnnotation(IgnoreExtends.class.getSimpleName(), cl.type.getModifiers()) && !parser.classes.get(superType).isNative) {
				parser.code.append(parser.getObfuscatedName(StrictWeb.class.getSimpleName())+"."+parser.getObfuscatedName("extend") + "("+cl.name+".prototype, "+superType+".prototype)\n");
			}
		}		
		
		parser.localVars.clear();
		
		return true;
	}

	private void printFieldInitializer(ParsedClass cl, String name, VarType type, Map<String, JCVariableDecl> fields, boolean isPrimitive) {
        String val = null;
        
        ParsedClass fcl = parser.classes.get(type.getName());
        if(fields.containsKey(name) && (null!=fcl&&fcl.isEnum || type.nameIs(String.class.getSimpleName()) || isPrimitive)) {
            parser.code.append("\tthis."+name+"=");
            parser.parse(fields.get(name).getInitializer());
            parser.code.append(";\n");
            return;
        }               
        
        if(type.nameIs("List") || type.nameIs("ArrayList")) val = "[]";
        else if(type.nameIs("Map") || type.nameIs("Set") 
            || type.nameIs("TreeMap") || type.nameIs("TreeSet") 
            || type.nameIs("HashMap") || type.nameIs("HashSet")) val = "{}";        

        if(null == val || "null".equals(val.toString())) return;
        
        parser.code.append("\tthis."+name+"="+val+";\n");
    }

    public boolean compileClassInitializers(ParsedClass cl) {
		return isEntityClass(cl);
	}

	private boolean isEntityClass(ParsedClass cl) {
//		if(cl.type==null) return false;
//		if(cl.name.equals("Entity")) return true;
		if(knownClasses.containsKey(cl.name)) return knownClasses.get(cl.name);
		boolean res = 
			(cl.type != null && (
				parser.hasAnnotation(Entity.class.getSimpleName(), cl.type.getModifiers())
				|| parser.hasAnnotation(Presentable.class.getSimpleName(), cl.type.getModifiers())
			)) || parser.hasClassLabel(cl.name, LABEL)
		;
		knownClasses.put(cl.name, res);
//		System.out.println(cl.name + " :: " + (res ? "entity" : "not ent"));
//		System.out.println(parser.classLabels);
		return res;
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}

	public boolean invokeMethod(String mName, JCMethodInvocation inv, List arguments) {
//		System.out.println(inv);
		
		ParsedClass cl;
		try {
			cl = parser.getParsedClassByName(parser.currentType.getName());
		} catch (NoSuchMethodException e1) {
			return false;
		}
		if(null == cl) return false;
		StringBuilder code = parser.code;
		
//		System.out.println("OK");
		if(!isEntityClass(cl)) return false;

		
//		System.out.println(parser.currentType.getName()+" :: " + mName);
		if(mName.equals("toString") && arguments.size() == 0) {
			code.append(mName + "()");
			return true;
		}
		
		boolean callGetter = (mName.startsWith("get") || mName.startsWith("is")) && arguments.size() == 0;
		boolean callSetter = mName.startsWith("set") && arguments.size() == 1;
		
		if(!callGetter && !callSetter) throw new RuntimeException("Not a getter or setter: " + mName);
		
		int nBegin = mName.startsWith("is") ? 2 : 3;
		
		String name = Character.toLowerCase(mName.charAt(nBegin)) + mName.substring(nBegin+1);
//		System.out.println(mName);

		JCMethodDecl mDecl = cl.methods.get(mName).decl;
		boolean isAjaxTransient = mDecl!=null && parser.hasAnnotation(AjaxTransient.class.getSimpleName(), mDecl.getModifiers());
		
		if(isAjaxTransient) {
			throw new RuntimeException("Only @AJAX getXXX(), isXXX() and plain setXXX(value) are supported: " + mName);			
		}
		
//		boolean isAjax = parser.hasAnnotation("AJAX", cl.methods.get(mName).decl.getModifiers())
//			|| parser.hasAnnotation("Id", cl.methods.get(mName).decl.getModifiers());
		
		
		if(callGetter) {
			code.append(name);
			//System.out.println(cl.name+"."+mName+" :: "+compiler.currentType);
			try {
				parser.currentType = parser.getMethodType(parser.currentType, mName);
//				System.out.println("    returns :: "+parser.currentType);
			} catch(NoSuchMethodException e) {
				throw new RuntimeException(e.getMessage());
			}
		} else {
			code.append(name);
			parser.parseArguments("=", arguments, "");
			parser.currentType = null;
		}
		
		return true;
	}
}