package ru.yandex.strictweb.scriptjava.base.custom;

import ru.yandex.strictweb.scriptjava.base.DOMBuilder;
import ru.yandex.strictweb.scriptjava.base.Node;
import ru.yandex.strictweb.scriptjava.base.NodeBuilder;

public class NoChildNodeBuilder extends DOMBuilder<Node, NoNode, NoChildNodeBuilder>{
	public NoChildNodeBuilder(String tag) {
		super(tag);
	}
}
