package ru.yandex.strictweb.scriptjava;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.yandex.strictweb.ajaxtools.annotation.Arguments;



public class CommonCompiler {
	int debugLevel = 0;
	boolean obfuscate = false;
	String basePath = "classpath:/";
	String jsGenPath = "generated.js";
	List<String> ignoredClasses = new ArrayList<String>();
	
	public final void build(String[] args) throws Exception {
	    long startTime = System.currentTimeMillis();
	    
		try {
			for(int i=0; i<args.length; i++) {
				if("-h".equals(args[i])) printUsage(0, args);

				String k = "set" + Character.toUpperCase(args[i].charAt(1)) + args[i].substring(2);
				Method m = this.getClass().getMethod(k, String.class);
				if(!m.isAnnotationPresent(Arguments.class)) throw new RuntimeException();
				m.invoke(this, args[++i]);
			}
		}catch(Throwable e) {
			e.printStackTrace();
			printUsage(1, args);
		}
		
		ru.yandex.strictweb.scriptjava.compiler.Compiler compiler = 
			new ru.yandex.strictweb.scriptjava.compiler.Compiler(basePath);
		
		compiler.setObfuscate(obfuscate);
		
		addToCompiler(compiler);
		
		compiler
		.ignoreClasses(ignoredClasses)
		.compileAndSave(jsGenPath);
		
		System.out.println("Build time: " + (System.currentTimeMillis() - startTime)/1000D + " sec");
	}

	public static void main(String[] args) throws Exception {
		new CommonCompiler().build(args);
	}
	
	public final void printUsage(int exitCode, String[] args) {
		System.err.println("Args: " + Arrays.toString(args));
		System.err.println("Usage: java " + this.getClass().getCanonicalName());
		System.err.println("-h\t Print this help and exit");
		for(Method m : this.getClass().getMethods()) {
			if(m.getName().startsWith("set") 
					&& m.getParameterTypes().length==1 
					&& m.getParameterTypes()[0] == String.class
					&& m.isAnnotationPresent(Arguments.class)) {
				String mName = Character.toLowerCase(m.getName().charAt(3)) + m.getName().substring(4);
				System.err.println(" -" + mName + " VALUE\t " + m.getAnnotation(Arguments.class).description());
			}
		}
		System.exit(exitCode);
	}
	
	/**
	 * this method is for overrite
	 * @param compiler
	 * @throws Exception 
	 */
	public void addToCompiler(ru.yandex.strictweb.scriptjava.compiler.Compiler compiler) throws Exception {
		
	}

	@Arguments(description = "debug level (int, default 0)")
	public final void setDebugLevel(String debugLevel) {
		System.out.println("Setting debug level to " + debugLevel);
		this.debugLevel = Integer.parseInt(debugLevel);
	}

	@Arguments(description = "base path to strict-web sources")
	public final CommonCompiler setBasePath(String basePath) {
		System.out.println("Setting base path to strict-web sources " + basePath);
		this.basePath = basePath;
		
		return this;
	}

	@Arguments(description = "path and name for generated java script file")
	public final CommonCompiler setJsGenPath(String jsGenPath) {
		System.out.println("Setting path and name for generated java script file " + jsGenPath);
		this.jsGenPath = jsGenPath;
		
		return this;
	}

	@Arguments(description = "do obfuscate or not (boolean, default false)")
	public final CommonCompiler setObfuscate(String obfuscate) {
		this.obfuscate = "true".equalsIgnoreCase(obfuscate);
		return this;
	}
	
	@Arguments(description = "ignores all calls to the specified class)")
	public final CommonCompiler setIgnoredClass(String clazzName) {
		this.ignoredClasses.add(clazzName);
		return this;
	}
}
