package ru.yandex.strictweb.example.helloworld;

import ru.yandex.strictweb.scriptjava.base.CommonDelegate;
import ru.yandex.strictweb.scriptjava.base.CommonElements;
import ru.yandex.strictweb.scriptjava.base.NodeBuilder;
import ru.yandex.strictweb.scriptjava.base.StrictWeb;

public class HelloWorld extends CommonElements {

	static {
		// точка входа
	    NodeBuilder.wrap(StrictWeb.document.body)
	    .removeChilds()
		.add(new HelloWorld().drawForm());
	}

	private NodeBuilder drawForm() {
		// создаем DIV и в нем обычную кнопку
		return $DIV().add($BTN("Нажми меня!".replace("!", "?"), new CommonDelegate<Boolean, NodeBuilder>() {
			public Boolean delegate(NodeBuilder n) {
				window.alert("Hello, World!");
				return false;
			}
		}));
	}
}
