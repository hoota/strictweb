package ru.yandex.strictweb.scriptjava.test;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import junit.framework.TestCase;
import ru.yandex.strictweb.scriptjava.base.ExtendsNative;
import ru.yandex.strictweb.scriptjava.base.Native;
import ru.yandex.strictweb.scriptjava.base.ajax.Ajax;
import ru.yandex.strictweb.scriptjava.compiler.Compiler;
import ru.yandex.strictweb.scriptjava.plugins.AjaxServiceHelperCompilerPlugin;

@Native
@ExtendsNative("String")
public class UnitTest extends TestCase {
	
	public static void println(String msg) {
		System.out.println(msg);
	}
	
	public void testA() {
		File dir = new File(this.getClass().getCanonicalName().replace('.', '/')+".java");
		if(!dir.exists()) {
			throw new RuntimeException("Please, correct run path");
		}
	}

	public void testAll() throws Exception {
		File dir = new File(this.getClass().getCanonicalName().replace('.', '/')+".java").getParentFile();

		for(File f : dir.listFiles(new FilenameFilter() {			
			public boolean accept(File dir, String name) {
				return name.startsWith("Test");
			}
		})) {
			
			try {
				Class<?> cl = Class.forName(this.getClass().getCanonicalName().replace(
					this.getClass().getSimpleName(), f.getName().replace(".java", ""))
				);
				
				if(cl.isAnnotationPresent(SkipTest.class)) continue;
									
				Compiler compiler = new Compiler("");
				Ajax.prepareCompiler(compiler);
				
				compiler
					.addPlugin(new AjaxServiceHelperCompilerPlugin())
									
					.parseClass(this.getClass())
					.parseClass(cl)
					.compileAndSave("temp.js");
				
				ScriptEngineManager mgr = new ScriptEngineManager();
				ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
				jsEngine.put("out", System.out);
				jsEngine.eval(
					"function UnitTest() {}; " +
					"UnitTest.println = function(msg) {out.println(msg);}"
				); 
				
				FileReader reader = null;
				try {
					jsEngine.eval(reader = new FileReader("temp.js"));
					if((Boolean)jsEngine.eval("new " + cl.getSimpleName()+"().test()") != true) {
						throw new RuntimeException("js return != true");
					}
					
//					jsEngine.eval("UnitTest.println('Hello')");
				} finally {
					if(reader != null) reader.close();
				}

			}catch(Throwable e) {
				throw new RuntimeException("test failed on file " + f.getName(), e);
			}
		}
		
	}
}
