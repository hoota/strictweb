package ru.yandex.strictweb.scriptjava.base.custom;

import ru.yandex.strictweb.scriptjava.base.DOMBuilder;
import ru.yandex.strictweb.scriptjava.base.Node;
import ru.yandex.strictweb.scriptjava.base.NodeBuilder;

public class TableColumnNodeBuilder extends DOMBuilder<TableColumnNode, Node, TableColumnNodeBuilder> {
	public TableColumnNodeBuilder() {
		super("td");
	}
	
	public TableColumnNodeBuilder colSpan(int cs) {
		node.colSpan = cs;
		return this;
	}

	public TableColumnNodeBuilder rowSpan(int rs) {
		node.rowSpan = rs;
		return this;
	}

}
