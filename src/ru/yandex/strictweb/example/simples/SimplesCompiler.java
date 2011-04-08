package ru.yandex.strictweb.example.simples;

import ru.yandex.strictweb.scriptjava.CommonCompiler;
import ru.yandex.strictweb.scriptjava.compiler.Compiler;

public class SimplesCompiler extends CommonCompiler {	
    @Override
    public void addToCompiler(Compiler compiler) throws Exception {
        compiler
        .parseClass(HelloWorld.class)
        .parseClass(FirstDiv.class)
        .parseClass(Timer.class)
        .parseClass(EventExample.class)
        .parseClass(TableTimerExample.class)
        ;
    }

    public static void main(String[] args) throws Exception {
        new SimplesCompiler()
        .setJsGenPath("src/java/ru/yandex/strictweb/example/www-root/simples.js")
        .setBasePath("src/java/")
        .build(args);
    }
}
