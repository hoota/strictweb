package ru.yandex.strictweb.scriptjava.base.custom;

import ru.yandex.strictweb.scriptjava.base.DOMBuilder;
import ru.yandex.strictweb.scriptjava.base.MayBeExcluded;
import ru.yandex.strictweb.scriptjava.base.Node;

public class SelectNodeBuilder extends DOMBuilder<Node, SelectOptionNode,SelectNodeBuilder> {
	public SelectNodeBuilder() {
		super("select");
	}

    @MayBeExcluded
	public SelectNodeBuilder setLastSelected(boolean yes) {
		if(yes)	node.selectedIndex = node.childNodes.length-1;
		return this;
	}
	
    @MayBeExcluded
	public SelectNodeBuilder option(Object value, String title) {
		return this.add(new SelectOptionNodeBuilder().value(value).text(title));
	}
}
