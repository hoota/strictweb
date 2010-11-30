package ru.yandex.strictweb.example.sampleajax;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.DefaultServlet;
import org.mortbay.jetty.servlet.ServletHolder;

import ru.yandex.strictweb.ajaxtools.AjaxService;
import ru.yandex.strictweb.ajaxtools.BeanProvider;
import ru.yandex.strictweb.scriptjava.CommonCompiler;
import ru.yandex.strictweb.scriptjava.base.ajax.Ajax;
import ru.yandex.strictweb.scriptjava.compiler.Compiler;

// Запускайте этот файл из папочки strict-web: корневой папки проекта
public class SampleAjaxCompile extends CommonCompiler {
    @Override
    public void addToCompiler(Compiler compiler) throws Exception {
        Ajax.prepareCompiler(compiler);
        
        compiler
        .parseClass(SampleHelperBean.class)
        .parseClass(SampleUiForm.class)
        .parseClass(SomeModel.class)
        ;
    }
    
    public static void main(String[] args) throws Exception {
        try {
            new SampleAjaxCompile()
            .setJsGenPath("src/ru/yandex/strictweb/example/www-root/sample-ajax.js")
            .setBasePath("src/")
            .build(args);
            
        }catch(Throwable th) {
            System.out.println("\n\nЗапускайте этот файл из папочки strict-web: корневой папки проекта\n\n");
            th.printStackTrace();
        }
        
        startJetty();
    }

    private static void startJetty() throws Exception {
        Server server = new Server(3128);
        Context context = new Context();
        context.setContextPath("/");
        context.setResourceBase("src/ru/yandex/strictweb/example/www-root/");
        context.addServlet(DefaultServlet.class, "/");
        
        AjaxService ajaxService = new AjaxService();
        ajaxService.setBeanProvider(new BeanProvider() {
            public Object getBeanInstance(String beanName) {
                try {
                    beanName = Character.toUpperCase(beanName.charAt(0)) + beanName.substring(1);
                    return Class.forName(SampleHelperBean.class.getPackage().getName() + "." + beanName).newInstance();
                }catch(Throwable th) {
                    throw new RuntimeException(th);
                }
            }
        });
        context.addServlet(new ServletHolder(ajaxService), "/ajax");
                
        server.addHandler(context);
        
        server.start();
        server.join();
    }
}
