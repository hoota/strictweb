package ru.yandex.strictweb.example.simples;

import ru.yandex.strictweb.scriptjava.CommonCompiler;

public class Compiler extends CommonCompiler {	
	@Override
	public void addToCompiler(ru.yandex.strictweb.scriptjava.compiler.Compiler compiler) throws Exception {
		compiler
		.parseClass(HelloWorld.class)
		.parseClass(FirstDiv.class)
		.parseClass(EventExample.class)
		.parseClass(TableTimerExample.class)
		;
	}
	
	public static void main(String[] args) throws Exception {
		new Compiler()
		.setJsGenPath("src/java/ru/yandex/strictweb/example/www-root/simples.js")
        .setBasePath("src/java/")
		.build(args);
	}
}
