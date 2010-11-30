package ru.yandex.strictweb.scriptjava.base.custom;

import ru.yandex.strictweb.scriptjava.base.DOMBuilder;
import ru.yandex.strictweb.scriptjava.base.NodeBuilder;

public class SelectOptionNodeBuilder extends DOMBuilder<SelectOptionNode, NoNode, SelectOptionNodeBuilder> {
	public SelectOptionNodeBuilder() {
		super("option");
	}
}
