package ru.yandex.strictweb.scriptjava.base;

public class NodeBuilder extends DOMBuilder<Node, Node, NodeBuilder> {
	
	public NodeBuilder(String tag) {
		super(tag);
	}

	public static NodeBuilder wrap(Node node) {
		if(node == null) return null;
		NodeBuilder nb = new NodeBuilder(null);
		nb.node = node;
		return nb;
	}
}
