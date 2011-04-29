package ru.yandex.strictweb.scriptjava.test;

import java.util.Date;

import ru.yandex.strictweb.scriptjava.base.CommonDelegate;
import ru.yandex.strictweb.scriptjava.base.CommonElements;
import ru.yandex.strictweb.scriptjava.base.NodeBuilder;
import ru.yandex.strictweb.scriptjava.base.custom.SelectNodeBuilder;
import ru.yandex.strictweb.scriptjava.base.custom.TableNodeBuilder;

public class ButtonForm<E extends ButtonForm> extends CommonElements {	
	String name;	
	EnumTest selected;
	E f2;

	public ButtonForm(String name) {
		this.name = name;
	}

	void test(E f) {
		f.drawForm();
		f2.createNode("QWE");
	}
	
	void testOperators() {
		int x = 123;
		x++;
		--x;
		
		x+=12;
		
		boolean b = !true && !false;
	}
	
	NodeBuilder drawForm() {
		final NodeBuilder log = $DIV();
		SelectNodeBuilder select = $SELECT();
		
		TableNodeBuilder b = $TABLE();
		
		select.onChange(new CommonDelegate<Boolean, SelectNodeBuilder>() {
			public Boolean delegate(SelectNodeBuilder s) {
				selected = EnumTest.valueOf(s.valueAsStr());
				Date d = new Date();
				log.add($B(d.getHours()+":"+d.getMinutes()+":"+d.getSeconds()))
					.text(" " + selected.getTitle()).BR();
				return true;
			}
		});
		
		for(EnumTest e : EnumTest.values()) {
			select.add($OPTION(e, e.getTitle()));
		}
		
		return $DIV()
		.add(EL("h2").text(name))
		.add($BTN("Press me!", new CommonDelegate<Boolean, NodeBuilder>() {
		    public Boolean delegate(NodeBuilder btn) {
		        window.alert(null!=selected ? selected + ": " +selected.getTitle() : "Nothing is selected");
		        return false;
		    }
		}))
		.text(" ")
		.add(select)
		.add($HR())
		.add(log)
		;		
	}
	
	static {
		document.body.appendChild(new ButtonForm("Button and select test").drawForm().node);
	}
}