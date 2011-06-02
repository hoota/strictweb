package ru.yandex.strictweb.scriptjava.base;

@Native
public class Node {
	public String className;
	public String href;
	public Style style;
	public Node offsetParent;
	public int offsetTop;
	public int offsetLeft;
	public int scrollTop;
	public int scrollLeft;
	public int offsetHeight;
	public int offsetWidth;
	public Node firstChild;
	public String tagName;
	public String nodeName;
	public Node lastChild;
	public String type;
	public String name;
	public String method;
	public String action;
	public String enctype;
	public Object value;
	public Object temp1;
	public Object temp2;
	public Object temp3;
	public String width;
    public String height;
	public String vAlign;
	public String src;
	public String title;
	public String field;
	public InputValidator validator;
	public Node parentNode;
	public Node nextSibling, previousSibling;
	public String align;
	public String innerHTML;
	
	public boolean checked;
	public Node[] childNodes;
	public int size;
	public int border;
	public boolean readOnly;
	public int selectedIndex;
	public String id;
	public boolean disabled;
	public int colSpan;
	public int rowSpan;
	public String target;
	public int vspace;
	public int hspace;
	public int cellSpacing;
	public int cellPadding;
	public String htmlFor;
	public Document ownerDocument;
	
	protected Node() {}

	public void appendChild(Node child) {
	}
	
	public void insertBefore(Node newChild, Node existed) {		
	}

	public void removeChild(Node child) {
	}

	public Integer clientWidth, clientHeight;
	public Integer clientLeft, clientTop;
    public int nodeType;
	
	public void focus() {
	}

	public void blur() {
	}

    public Node[] getElementsByTagName(String tagName) {
        return null;
    }
}
