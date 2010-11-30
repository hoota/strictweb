package ru.yandex.strictweb.scriptjava.base.custom;

import ru.yandex.strictweb.scriptjava.base.DOMBuilder;
import ru.yandex.strictweb.scriptjava.base.Node;
import ru.yandex.strictweb.scriptjava.base.NodeBuilder;

public class TableNodeBuilder extends DOMBuilder<Node, TableInnerNode, TableNodeBuilder> {
	public TableNodeBuilder() {
		super("table");
	}

	public TableNodeBuilder cellPaddingSpacing0() {
		return cellPadding(0).cellSpacing(0);
	}

	public TableNodeBuilder cellSpacing(int i) {
		node.cellSpacing = i;
		return this;
	}

	public TableNodeBuilder cellPadding(int i) {
		node.cellPadding = i;
		return this;
	}
}
