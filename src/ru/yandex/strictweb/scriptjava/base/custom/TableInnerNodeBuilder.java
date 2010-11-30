package ru.yandex.strictweb.scriptjava.base.custom;

import ru.yandex.strictweb.scriptjava.base.DOMBuilder;

public class TableInnerNodeBuilder extends DOMBuilder<TableInnerNode, TableRowNode, TableInnerNodeBuilder> {
	public TableInnerNodeBuilder(String tag) {
		super(tag);
	}
}
