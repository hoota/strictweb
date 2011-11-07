package ru.yandex.strictweb.example.sampleajax;

import java.util.Date;
import java.util.Map;

import ru.yandex.strictweb.scriptjava.base.CommonDelegate;
import ru.yandex.strictweb.scriptjava.base.CommonElements;
import ru.yandex.strictweb.scriptjava.base.InputValidator;
import ru.yandex.strictweb.scriptjava.base.Node;
import ru.yandex.strictweb.scriptjava.base.NodeBuilder;
import ru.yandex.strictweb.scriptjava.base.StrictWeb;
import ru.yandex.strictweb.scriptjava.base.ValidatorHelperBase;
import ru.yandex.strictweb.scriptjava.base.VoidDelegate;
import ru.yandex.strictweb.scriptjava.base.ajax.Ajax;

public class SampleUiForm extends CommonElements {
    SampleHelperBean helper = new SampleHelperBean();
    
    static {
        // точка входа
        initAjax();
        
        // берем <body>
        NodeBuilder.wrap(StrictWeb.document.body)
        
        // убираем из <body> все надписи
        .removeChilds()
        
        // добавляем туда формочку
        .add(new SampleUiForm().drawForm());
    }

    private NodeBuilder drawForm() {
        NodeBuilder div = $DIV();
        
        div.add(drawServerTimeButton());
        
        
        SomeModel o = helper.getObject(123L);
        div.add(drawObjectForm(o));

        drawServerProperties(div);
        
        return div;
    }

    private NodeBuilder drawObjectForm(SomeModel o) {
        final NodeBuilder form = $DIV().field("Object")
            .text("Object ID: " + o.id).add($HIDDEN(ajaxName(o.id), o.id)).BR()
            .text("Object name: ").add($INPUT().name(ajaxName(o.name)).value(o.name)).BR()
            .text("Object email: ").add($INPUT().name(ajaxName(o.email)).value(o.email).validator(new InputValidator() {
                public boolean isValid(Node n) {
                    return n.value.toString().indexOf("@") > 0; 
                }
                
                public String getMessage() {
                    return "Realy bad email =)";
                }
            }))
        ;
        
        form.add($BTN("Save", new CommonDelegate<Boolean, NodeBuilder>() {
            public Boolean delegate(NodeBuilder n) {
                if(!new ValidatorHelperBase().validate(form)) return false;
                window.alert(helper.saveObject((SomeModel)(Object)form.node));
                return false;
            }
        }));
        
        return form;
    }

    private void drawServerProperties(NodeBuilder div) {
        // синхронный вызов
        Map<Object, Object> prop = helper.getSystemProperties();
        
        for(Object key: prop.keySet()) {
            div.textB(key+"").text(" :: " + prop.get(key)).BR();
        }
    }

    private NodeBuilder drawServerTimeButton() {
        return $BTN("Show server time&date", new CommonDelegate<Boolean, NodeBuilder>() {
            public Boolean delegate(NodeBuilder n) {
                ajaxAsyncCall(helper.getServerDate(), new VoidDelegate<Date>() {
                    public void voidDelegate(Date date) {
                        window.alert(dateToStringSmart(date));
                    }
                });
                return false;
            }
        });
    }

    private static void initAjax() {
    	Ajax.helper = new Ajax() {
    		public String getLoadingImageUrl() {
    			return "http://mbo.market.yandex.ru/js/yui/assets/skins/sam/wait.gif";
    		}
    		
    		public void onError(Throwable exc) {
    			if(exc == null) return;
    			window.alert("Error: " + exc.getMessage());
    		}
    	};
    }
}
