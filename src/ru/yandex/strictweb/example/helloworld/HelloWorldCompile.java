package ru.yandex.strictweb.example.helloworld;

import ru.yandex.strictweb.scriptjava.CommonCompiler;
import ru.yandex.strictweb.scriptjava.compiler.Compiler;

// Запускайте этот файл из папочки strict-web: корневой папки проекта
public class HelloWorldCompile extends CommonCompiler {
    @Override
    public void addToCompiler(Compiler compiler) throws Exception {
        // здесь перечисляем какие классы надо скомпилить
        compiler.parseClass(HelloWorld.class);
    }
    
    public static void main(String[] args) {
        try {
            new HelloWorldCompile()
            .setObfuscate("true")
            // устанавливаем откуда брать исходники
            .setBasePath("src/")
            // устанавливаем в какой файл компилить
            .setJsGenPath("src/ru/yandex/strictweb/example/www-root/hello-world.js")
            .build(args);
        }catch(Throwable th) {
            System.out.println("\n\nЗапускайте этот файл из папочки strict-web: корневой папки проекта\n\n");
            th.printStackTrace();
        }
    }
}
