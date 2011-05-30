package ru.yandex.strictweb.scriptjava.base.custom;

import ru.yandex.strictweb.scriptjava.base.DOMBuilder;
import ru.yandex.strictweb.scriptjava.base.MayBeExcluded;
import ru.yandex.strictweb.scriptjava.base.Node;

public class TableNodeBuilder extends DOMBuilder<Node, TableInnerNode, TableNodeBuilder> {
	public TableNodeBuilder() {
		super("table");
	}

    @MayBeExcluded
	public TableNodeBuilder cellPaddingSpacing0() {
        node.cellSpacing = node.cellPadding = 0;
		return this;
	}

    @MayBeExcluded
	public TableNodeBuilder cellSpacing(int i) {
		node.cellSpacing = i;
		return this;
	}

    @MayBeExcluded
	public TableNodeBuilder cellPadding(int i) {
		node.cellPadding = i;
		return this;
	}
}
