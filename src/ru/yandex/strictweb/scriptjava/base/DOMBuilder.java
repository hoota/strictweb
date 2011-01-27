package ru.yandex.strictweb.scriptjava.base;

import java.util.LinkedList;
import java.util.List;

public abstract class DOMBuilder<N extends Node, C extends Node, S extends DOMBuilder> extends ScriptJava {
	public static final String EV_ONKEYDOWN = "onkeydown";
	public static final String EV_ONBLUR = "onblur";
	public static final String EV_ONFOCUS = "onfocus";
	public static final String EV_ONKEYPRESS = "onkeypress";
	public static final String EV_ONKEYUP = "onkeyup";
	public static final String EV_ONCHANGE = "onchange";
	public static final String EV_ONDBLCLICK = "ondblclick";
	public static final String EV_ONSUBMIT = "onsubmit";
	public static final String EV_ONMOUSEDOWN = "onmousedown";
	public static final String EV_ONMOUSEUP = "onmouseup";
	public static final String EV_ONMOUSEOUT = "onmouseout";
	public static final String EV_ONMOUSEMOVE = "onmousemove";
	public static final String EV_ONCLICK = "onclick";
	public static final String DISABLED = "disabled";
	
	public N node;
	
	DOMBuilder() {}
	
	public DOMBuilder(String tagName) {
		node = (N)createNode(tagName);
	}
	
	@NativeCode("{this.node[eventName](ScriptJava.globalEvent, nullNode);return this;}")
	final public S fireEvent(String eventName, boolean nullNode) {
		return (S)this;
	}
	
	final public S onClick(DOMEventCallback callback) {
        node.style.cursor = "pointer";
		ScriptJava.setDOMEventCallback(node, EV_ONCLICK, callback);
		return (S)this;
	}
	
	final public S onMouseMove(DOMEventCallback onMouseMoveCallback) {
		ScriptJava.setDOMEventCallback(node, EV_ONMOUSEMOVE, onMouseMoveCallback);
		return (S)this;
	}
	
	final public S onMouseOut(DOMEventCallback onMouseOutCallback) {
		ScriptJava.setDOMEventCallback(node, EV_ONMOUSEOUT, onMouseOutCallback);
		return (S)this;
	}
	
	final public S onMouseUp(DOMEventCallback onMouseUpCallback) {
		ScriptJava.setDOMEventCallback(node, EV_ONMOUSEUP, onMouseUpCallback);
		return (S)this;
	}

	final public S onMouseDown(DOMEventCallback onMouseDownCallback) {
		ScriptJava.setDOMEventCallback(node, EV_ONMOUSEDOWN, onMouseDownCallback);
		return (S)this;
	}
	
	final public S onSubmit(DOMEventCallback callback) {
		ScriptJava.setDOMEventCallback(node, EV_ONSUBMIT, callback);
		return (S)this;
	}
	
	final public S onDblClick(DOMEventCallback callback) {
		ScriptJava.setDOMEventCallback(node, EV_ONDBLCLICK, callback);
		return (S)this;
	}

	//@NativeCode("{if(null!=t)this.node.appendChild(t.tagName?t:document.createTextNode(t)); return this;}")
	final public S text(String t) {
		//if(null!=string) node.appendChild(textNode(string));
		return append((C)(Object)t);
	}
	
	/** Adds BOLD text */
	final public S textB(String t) {
		//if(null!=string) node.appendChild(textNode(string));
		return add((DOMBuilder<C, Node, ?>)new NodeBuilder("b").text(t));
	}

//	@NativeCode("{" +
//			"var f = isIE && this.node.ownerDocument && __fixIECheckedUnchecked;" +
//			"var n = this.node;" +
//			"while(n.firstChild) {if(f)__fixIECheckedUnchecked(n.firstChild);n.removeChild(n.firstChild);}" +
//			"return this;" +
//			"}")
	final public S removeChilds() {
		while(null!=node.firstChild) {
			node.removeChild(node.firstChild);
		}
		return (S)this;
	}

//	@NativeCode("{if(null!=c) {" +
//			" var n = this.node;" +
//			" var f = isIE && n.ownerDocument && __fixIEChecked2_Checked;" +
//			" if(!c.nodeName) n.appendChild(document.createTextNode(c)); else {" +
//			"  if(f) __fixIEChecked2_Checked(c);" +
//			"  n.appendChild(c);" +
//			"  if(f) __fixIE_Checked2Checked(c);" +
//			"}} return this;}")
	final public S append(C c) {
		if(c != null) {
			node.appendChild(c.nodeName == null ? ScriptJava.document.createTextNode(c): c);
		}
		return (S)this;
	}

	final public S className(String className) {
		node.className = className;
		return (S)this;
	}

	public S href(String href) {
		node.href = href;
		return (S)this;
	}

	public S action(String a) {
		node.action = a;
		return (S)this;
	}
	
	public S appendList(List<C> items) {
		for(C item : items) append(item);
		return (S)this;
	}

	public S appendAll(C[] items) {
		for(C item : items) append(item);
		return (S)this;
	}
	
	public S styleDisplay(String d) {
		node.style.display = d;
		return (S)this;
	}
	
	public S cssText(String cssText) {
	    node.style.cssText = cssText;
        return (S)this;
	}
	
    public S cursor(String cursor) {
        node.style.cursor = cursor;
        return (S)this;
    }
    
    public S cursorPointer() {
        node.style.cursor = "pointer";
        return (S)this;
    }

    public S position(String position) {
        node.style.position = position;
        return (S)this;
    }
    
	public S add(DOMBuilder<? extends C, ? extends Node, ?> b) {
		if(null!=b) append(b.node);
		return (S)this;
	}
	
	public S addFirst(DOMBuilder<? extends C, ? extends Node, ?> b) {
		if(null!=b) node.insertBefore(b.node, node.firstChild);
		return (S)this;
	}
	
	public S addList(List<DOMBuilder<C, ? extends Node, ?>> items) {
		if(null!=items)
		for(DOMBuilder<C, ? extends Node, ?> b : items)  append(b.node);
		return (S)this;
	}

	public S addAll(DOMBuilder<C, ? extends Node, ?>[] items) {
		if(null!=items)
		for(DOMBuilder<C, ? extends Node, ?> b : items)  append(b.node);
		return (S)this;
	}

	public S hide() {
		node.style.display = "none";
		return (S)this;
	}

	public S show() {
		node.style.display = "";
		return (S)this;
	}
	
	public S showHide(boolean show) {
		node.style.display = show ? "" : "none";
		return (S)this;
	}

	
	public String toggle() {
		return (node.style.display = node.style.display=="" ? "none" : "");
	}

	public S type(String type) {
		node.type = type;
		return (S)this;
	}

	public S name(String name) {
		node.name = null==name ? "" : name;
		return (S)this;
	}
	public S value(Object value) {
		node.value = null==value?"":value;
		return (S)this;
	}
	
	public S temp1(Object t) {
		node.temp1 = t;
		return (S)this;
	}
	public S temp2(Object t) {
		node.temp2 = t;
		return (S)this;
	}
	public S temp3(Object t) {
		node.temp3 = t;
		return (S)this;
	}

	public S width(String width) {
		node.width = width;
		return (S)this;
	}
	
	public S method(String method) {
		node.method = method;
		return (S)this;
	}
	
	public S enctype(String enctype) {
		node.enctype = enctype;
		return (S)this;
	}

	public S width100() {
		node.width = "100%";
		return (S)this;
	}

	public S onChange(DOMEventCallback callback) {
		ScriptJava.setDOMEventCallback(node, EV_ONCHANGE, callback);
		return (S)this;
	}

	public S vAlign(String va) {
		node.vAlign = va;
		return (S)this;
	}

	public S src(String src) {
		node.src = src;
		return (S)this;
	}

	public S title(String t) {
		node.title = t;
		return (S)this;
	}

	public S field(String f) {
		node.field = f;
		return (S)this;
	}
	
	public S fieldDisabled() {
		node.field = DISABLED;
		return (S)this;
	}

	public S BR() {
		return append((C)createNode("br"));
	}

	public S validator(InputValidator v) {
		node.validator = v;
		return (S)this;
	}

	public S removeFromDom() {
		if(null!=node.parentNode) node.parentNode.removeChild(node);
		return (S)this;
	}

	public S align(String a) {
		node.align = a;
		return (S)this;
	}

	public S innerHTML(String ih) {
		node.innerHTML = ih;
		return (S)this;
	}

	public S checked(boolean ch) {
		node.checked = ch;
		return (S)this;
	}

	public void forEachSubchild(DOMEventCallback cb) {
		LinkedList<Node> nodes = new LinkedList<Node>();
		nodes.push(this.node);
		while(nodes.size() > 0) {
			Node n = nodes.poll();
			if(cb.delegate(n))
			for(Node c : n.childNodes) {
				if(null!=c.tagName) nodes.push(c);
			}
		}
	}

	public S size(int s) {
		node.size = s;
		return (S)this;
	}

	public S readOnly(boolean ro) {
		node.readOnly = ro;
		return (S)this;
	}

	public Integer valueAsInt() {
		if(node.value==null || node.value=="") return null;
		return 1 * (Integer)node.value;
	}
	
    public Long valueAsLong() {
        if(node.value==null || node.value=="") return null;
        return 1 * (Long)node.value;
    }
    
	public boolean isChecked() {
		return node.checked;
	}

	public S onKeyUp(DOMEventCallback callback) {
		ScriptJava.setDOMEventCallback(node, EV_ONKEYUP, callback);
		return (S)this;
	}

	public S onKeyPress(DOMEventCallback callback) {
		ScriptJava.setDOMEventCallback(node, EV_ONKEYPRESS, callback);
		return (S)this;
	}

	public S onBlur(DOMEventCallback callback) {
		ScriptJava.setDOMEventCallback(node, EV_ONBLUR, callback);
		return (S)this;
	}

	public S onFocus(DOMEventCallback callback) {
		ScriptJava.setDOMEventCallback(node, EV_ONFOCUS, callback);
		return (S)this;
	}
	
	public S onKeyDown(DOMEventCallback callback) {
		ScriptJava.setDOMEventCallback(node, EV_ONKEYDOWN, callback);
		return (S)this;
	}
	
	public String valueAsStr() {
		return (String)node.value;
	}

	public void appendTo(DOMBuilder<? extends Node, N, ?> parent) {
		removeFromDom();
		if(null!=parent) parent.append(this.node);
	}

	public S id(String id) {
		node.id = id;
		return (S)this;
	}

	public S styleFontSize(String fs) {
		node.style.fontSize = fs;
		return (S)this;
	}

	public S styleFontWeight(String fs) {
		node.style.fontWeight = fs;
		return (S)this;
	}
	
	public S styleTextDecoration(String td) {
		node.style.textDecoration = td;
		return (S)this;
	}
	
	public S disabled(boolean d) {
		node.disabled = d;
		return (S)this;
	}
	
	public S valignTop() {
		node.vAlign = "top";
		return (S)this;
	}

	public S stylePaddingLeft(String pl) {
		node.style.paddingLeft = pl;
		return (S)this;
	}

	public S styleMargin(String m) {
		node.style.margin = m;
		return (S)this;
	}

	public S stylePadding(String p) {
		node.style.padding = p;
		return (S)this;
	}
	

	public S absolutePosition() {
		node.style.position = "absolute";
		return (S)this;
	}

	
	public S alignRight() {
		node.align = "right";
		return (S)this;
	}

	public S alignCenter() {
		node.align = "center";
		return (S)this;
	}
	
	public S styleWidth(Object w) {
		node.style.width = w;
		return (S)this;
	}

	/**
	 * Set common style attribute
	 * node.style[key] = value
	 */
	@NativeCode("{this.%node%.style[key] = value; return this;}")
	public S style(String key, Object value) {
		return (S)this;
	}
	
	public S styleOpacity(double o) {
	    node.style.opacity = o;
	    node.style.filter = "alpha(opacity="+Math.round(o*100)+")";
	    return (S)this;
	}
	
	public S styleHeight(Object h) {
		node.style.height = h;
		return (S)this;
	}
	
	public S styleTop(Object t) {
		node.style.top = t;
		return (S)this;
	}

	public S styleLeft(Object l) {
		node.style.left = l;
		return (S)this;
	}

	public S styleMarginTop(String m) {
		node.style.marginTop = m;
		return (S)this;
	}

	public S styleMarginLeft(String m) {
		node.style.marginLeft = m;
		return (S)this;
	}	
	
	public S target(String t) {
		node.target = t;
		return (S)this;
	}

	public S targetBlank() {
		node.target = "_blank";
		return (S)this;
	}

	public S vspace(int s) {
		node.vspace = s;
		return (S)this;
	}
	
	public S hspace(int s) {
		node.hspace = s;
		return (S)this;
	}
	
	public S border(int b) {
		node.border = b;
		return (S)this;
	}

	public S styleColor(String c) {
		node.style.color = c;
		return (S)this;
	}
	
	public S styleBackground(String bg) {
		if(null!=bg) node.style.background = bg;
		return (S)this;
	}

	public S styleBorder(String b) {
		node.style.border = b;
		return (S)this;
	}

	public S styleBoxShadow(String bs) {
		node.style.boxShadow = bs;
		node.style.webkitBoxShadow = bs;
		node.style.MozBoxShadow = bs;
		return (S)this;
	}

	public S styleTransform(String tr) {
		node.style.transform = tr;
		node.style.webkitTransform = tr;
		node.style.MozTransform = tr;
		return (S)this;
	}
	
	public S styleTransformOrigin(String to) {
		node.style.transformOrigin = to;
		node.style.webkitTransformOrigin = to;
		node.style.MozTransformOrigin = to;
		return (S)this;
	}
	
	public S styleBorderRadius(String br) {
		node.style.borderRadius = br;
		node.style.webkitBorderRadius = br;
		node.style.MozBorderRadius = br;
		return (S)this;
	}
	

	public S styleBorderBottom(String b) {
		node.style.borderBottom = b;
		return (S)this;
	}

	public S styleOverflow(String ov) {
		node.style.overflow = ov;
		return (S)this;
	}
	
	public S styleFloat(String fl) {
		node.style.cssFloat = node.style.styleFloat = fl;
		return (S)this;
	}

    public boolean isEmpty() {
        return node.childNodes.length == 0;
    }

	
	@NativeCode("{if(existed.%node%.nextSibling == null) this.%add%(newChild);" +
			"else this.%node%.insertBefore(newChild.node, existed.%node%.nextSibling);" +
			"return this;}")
	public S insertAfter(DOMBuilder<? extends C, ? extends Node, ?> newChild, DOMBuilder<? extends C, ? extends Node, ?> existed) {
		return null;
	}
	
	@NativeCode("{alert(this.%node% + ' :: ' + existed.nextSibling);if(existed.nextSibling == null) this.%add%(newChild);" +
			"else this.%node%.insertBefore(newChild.node, existed.nextSibling);" +
			"return this;}")
	public S insertAfterN(DOMBuilder<? extends C, ? extends Node, ?> newChild, Node existed) {
		return null;
	}
}