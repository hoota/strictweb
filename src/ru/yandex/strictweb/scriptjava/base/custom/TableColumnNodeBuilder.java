package ru.yandex.strictweb.scriptjava.base.custom;

import ru.yandex.strictweb.scriptjava.base.DOMBuilder;
import ru.yandex.strictweb.scriptjava.base.MayBeExcluded;
import ru.yandex.strictweb.scriptjava.base.Node;

public class TableColumnNodeBuilder extends DOMBuilder<TableColumnNode, Node, TableColumnNodeBuilder> {
	public TableColumnNodeBuilder() {
		super("td");
	}
	
    @MayBeExcluded
	public TableColumnNodeBuilder colSpan(int cs) {
		node.colSpan = cs;
		return this;
	}

    @MayBeExcluded
	public TableColumnNodeBuilder rowSpan(int rs) {
		node.rowSpan = rs;
		return this;
	}

}
