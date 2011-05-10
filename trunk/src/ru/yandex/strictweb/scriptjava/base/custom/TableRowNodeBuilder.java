package ru.yandex.strictweb.scriptjava.base.custom;

import ru.yandex.strictweb.scriptjava.base.DOMBuilder;

public class TableRowNodeBuilder extends DOMBuilder<TableRowNode, TableColumnNode, TableRowNodeBuilder> {
	public TableRowNodeBuilder() {
		super("tr");
	}
	
	/**
	 * Adds TD with text, if header is NULL - does nothing
	 */
	public TableRowNodeBuilder TDH(String header) {
		return add(header==null ? null : new TableColumnNodeBuilder().text(header));
	}

	/**
	 * Adds TD with text, if header is NULL - does nothing
	 */
	public TableRowNodeBuilder TDHW(String header, String width) {
		return add(header==null ? null : new TableColumnNodeBuilder().styleWidth(width).text(header));
	}
	
	/**
	 * Adds TD with child node, if <i>node</i> is NULL - TD is empty
	 */
	public TableRowNodeBuilder TDN(DOMBuilder b) {
		return add(new TableColumnNodeBuilder().add(b));
	}
}
