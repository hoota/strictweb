package ru.yandex.strictweb.scriptjava.plugins;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;

import ru.yandex.strictweb.ajaxtools.annotation.AjaxServiceHelper;
import ru.yandex.strictweb.scriptjava.base.StrictWeb;
import ru.yandex.strictweb.scriptjava.base.ajax.Ajax;
import ru.yandex.strictweb.scriptjava.compiler.CompilerPlugin;
import ru.yandex.strictweb.scriptjava.compiler.ParsedClass;
import ru.yandex.strictweb.scriptjava.compiler.Parser;
import ru.yandex.strictweb.scriptjava.compiler.VarType;

public class AjaxServiceHelperCompilerPlugin implements CompilerPlugin {
	Parser parser;
	Map<String, Boolean> knownClasses = new TreeMap<String, Boolean>();
	
	private Class<?> ajaxServiceHelperAnnotation = AjaxServiceHelper.class;
	private String ajaxSyncCallClass = Ajax.class.getSimpleName();
	private String ajaxSyncCallMethod = "syncCall";
	
	private boolean isAjaxServiceClass(ParsedClass cl) {
		if(cl.type==null) return false;
		if(knownClasses.containsKey(cl.name)) return knownClasses.get(cl.name);
		
		boolean res = parser.hasAnnotation(ajaxServiceHelperAnnotation.getSimpleName(), cl.type.getModifiers());
		knownClasses.put(cl.name, res);
		return res;
	}
	
	public boolean compileClassFieldsAndMethods(ParsedClass cl) {
		if(!isAjaxServiceClass(cl)) return false;
		parser.code.append("function " + parser.getObfuscatedName(cl.name) + "() {}\n");
		
		for(JCTree tr : cl.type.getMembers()) if(tr instanceof JCMethodDecl){
			JCMethodDecl m = (JCMethodDecl)tr;
			if(parser.isConstructor(m)) continue;
			//System.out.println(m.modifiers());
			String mName = m.getName().toString();
			boolean isAjax = parser.hasAnnotation("Arguments", m.getModifiers());

			if(parser.isAbstract(m.getModifiers()) || parser.isStatic(m.getModifiers()) || !isAjax) continue;
			String clName = cl.name;
			clName = Character.toLowerCase(cl.name.charAt(0)) + clName.substring(1);
			
			String params = parser.getObfuscatedName("params");
            String callback = parser.getObfuscatedName("callback");
            String errorHandler = parser.getObfuscatedName("errorHandler");
            
            if(parser.obfuscate) {
                parser.code.append(parser.getObfuscatedName(cl.name) + ".prototype." + parser.getObfuscatedName(mName)
                    + "=function(" + params+","+callback+","+errorHandler + "){return "
                    + parser.getObfuscatedName(ajaxSyncCallClass) + "." + parser.getObfuscatedName(ajaxSyncCallMethod)
                    + "('"+clName+"','"+mName+"',"+params+","+callback+","+errorHandler
                    + ");}\n"
                );                
            } else {
                parser.code.append(cl.name + ".prototype." + mName
                    + " = function(" + params+", "+callback+", "+errorHandler + ") {\n\treturn "
                    + ajaxSyncCallClass + "." + ajaxSyncCallMethod
                    + "('"+clName+"', '"+mName+"', "+params+", "+callback+", "+errorHandler
                    + ");\n}\n"
                );                
            }
		}

		
		parser.code.append("\n");
		
		if(null!=cl.type.getExtendsClause()) {
			String superType = cl.type.extending.toString();
			if(!parser.classes.get(superType).isNative) {
				parser.code.append(parser.getObfuscatedName(StrictWeb.class.getSimpleName())+"."+parser.getObfuscatedName("extend") +
					"("+parser.getObfuscatedName(cl.name)+".prototype, "+parser.getObfuscatedName(superType)+".prototype)\n");
			}
		}		
		
		return true;
	}
	
	public boolean compileClassInitializers(ParsedClass cl) {
		return isAjaxServiceClass(cl);
	}
	
	public boolean invokeMethod(String mName, JCMethodInvocation inv, List arguments) {
		
		ParsedClass cl = parser.classes.get(parser.currentType.getName());
		if(null == cl) return false;
		StringBuilder code = parser.code;
		
		if(checkForAjaxAsyncCallMagicMethod(mName, cl, code, arguments)) return true;
        if(checkForAjaxAsyncCallWithErrorsMagicMethod(mName, cl, code, arguments)) return true;
		
//		System.out.println(cl.name + "." + mName);
		if(!isAjaxServiceClass(cl)) return false;
		
		code.append(parser.getObfuscatedName(mName));
//		code.append("/* ajax sync call*/");
		//System.out.println(inv);
		parser.parseArguments("([", arguments, "])");
		
		return true;
	}
	
	private boolean checkForAjaxAsyncCallMagicMethod(String mName, ParsedClass cl, StringBuilder code, List<JCExpression> arguments) {
		if(!cl.name.equals(StrictWeb.class.getSimpleName())
			|| !mName.equals("ajaxAsyncCall")
			|| arguments.size()!=2) return false;
		
		while('.'==code.charAt(code.length()-1) || Character.isJavaIdentifierPart(code.charAt(code.length()-1))) {
			code.setLength(code.length() - 1);
		}
		
		parser.parse(arguments.get(0));
		// remove )
		code.setLength(code.length() - 1);
		code.append(", ");
		parser.parse(arguments.get(1));
		code.append(")");

		
		parser.currentType = VarType.VOID;
		return true;
	}
	
	   private boolean checkForAjaxAsyncCallWithErrorsMagicMethod(String mName, ParsedClass cl, StringBuilder code, List<JCExpression> arguments) {
	        if(!cl.name.equals(StrictWeb.class.getSimpleName())
	            || !mName.equals("ajaxAsyncCallWithErrors")
	            || arguments.size()!=3) return false;
	        
	        while('.'==code.charAt(code.length()-1) || Character.isJavaIdentifierPart(code.charAt(code.length()-1))) {
	            code.setLength(code.length() - 1);
	        }
	        
	        parser.parse(arguments.get(0));
	        // remove )
	        code.setLength(code.length() - 1);
	        code.append(", ");
	        parser.parse(arguments.get(1));
            code.append(", ");
            parser.parse(arguments.get(2));
	        code.append(")");

	        
	        parser.currentType = VarType.VOID;
	        return true;
	    }
	
	
	public void setParser(Parser parser) {
		this.parser = parser;
	}

	public void setAjaxSyncCallClass(String ajaxSyncCallClass) {
		this.ajaxSyncCallClass = ajaxSyncCallClass;
	}

	public void setAjaxSyncCallMethod(String ajaxSyncCallMethod) {
		this.ajaxSyncCallMethod = ajaxSyncCallMethod;
	}
}
