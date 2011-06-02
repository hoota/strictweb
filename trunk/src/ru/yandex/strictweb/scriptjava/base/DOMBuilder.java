package ru.yandex.strictweb.scriptjava.base;

import java.util.LinkedList;
import java.util.List;

public abstract class DOMBuilder<N extends Node, C extends Node, S extends DOMBuilder> extends StrictWeb {
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
	public static final String EV_ONMOUSEOVER = "onmouseover";
	public static final String EV_ONMOUSEMOVE = "onmousemove";
	public static final String EV_ONCLICK = "onclick";
	public static final String DISABLED = "disabled";
	
	public N node;
	
	DOMBuilder() {}
	
	public DOMBuilder(String tagName) {
		node = (N)createNode(tagName);
	}
	
    @MayBeExcluded
	@NativeCode("{this.%node%[eventName](%StrictWeb%.%globalEvent%);return this;}")
	final public S fireEvent(String eventName) {
		return (S)this;
	}
	
    @MayBeExcluded
    @NativeCode(
        "{if(cb==null){this.%node%[name]=null;return;}" +
        "var b=this;" +
        "b.%node%[name] = function(ev) {" +
            "%StrictWeb%.%swTarget%=null;" +
            "%StrictWeb%.%globalEvent%=ev||window.event;" +
            "return cb.%delegate%(b);" +
        "}}")
    public void setEventDelegate(String name, CommonDelegate<Boolean, S> cb) {
    }
    
    @MayBeExcluded
	final public S onClick(CommonDelegate<Boolean, S> callback) {
        node.style.cursor = "pointer";
        setEventDelegate(EV_ONCLICK, callback);
		return (S)this;
	}
	
    @MayBeExcluded
	final public S onMouseMove(CommonDelegate<Boolean, S> onMouseMoveCallback) {
		setEventDelegate(EV_ONMOUSEMOVE, onMouseMoveCallback);
		return (S)this;
	}
	
    @MayBeExcluded
	final public S onMouseOut(CommonDelegate<Boolean, S> onMouseOutCallback) {
		setEventDelegate(EV_ONMOUSEOUT, onMouseOutCallback);
		return (S)this;
	}
	
    @MayBeExcluded
    final public S onMouseOver(CommonDelegate<Boolean, S> onMouseOutCallback) {
        setEventDelegate(EV_ONMOUSEOVER, onMouseOutCallback);
        return (S)this;
    }
	
    @MayBeExcluded
	final public S onMouseUp(CommonDelegate<Boolean, S> onMouseUpCallback) {
		setEventDelegate(EV_ONMOUSEUP, onMouseUpCallback);
		return (S)this;
	}

    @MayBeExcluded
	final public S onMouseDown(CommonDelegate<Boolean, S> onMouseDownCallback) {
		setEventDelegate(EV_ONMOUSEDOWN, onMouseDownCallback);
		return (S)this;
	}
	
    @MayBeExcluded
	final public S onSubmit(CommonDelegate<Boolean, S> callback) {
		setEventDelegate(EV_ONSUBMIT, callback);
		return (S)this;
	}
	
	@MayBeExcluded
	final public S onDblClick(CommonDelegate<Boolean, S> callback) {
		setEventDelegate(EV_ONDBLCLICK, callback);
		return (S)this;
	}

	final public S text(String t) {
		return append((C)(Object)t);
	}
	
	/** Adds BOLD text */
    @MayBeExcluded
	final public S textB(String t) {
		return add((DOMBuilder<C, Node, ?>)new NodeBuilder("b").text(t));
	}

    @MayBeExcluded
	final public S removeChilds() {
		while(null!=node.firstChild) {
			node.removeChild(node.firstChild);
		}
		return (S)this;
	}

	final public S append(C c) {
		if(c != null) {
			node.appendChild(c.nodeName == null ? StrictWeb.document.createTextNode(c): c);
		}
		return (S)this;
	}

    @MayBeExcluded
	final public S className(String className) {
		node.className = className;
		return (S)this;
	}

    @MayBeExcluded
	public S href(String href) {
		node.href = href;
		return (S)this;
	}

    @MayBeExcluded
	public S action(String a) {
		node.action = a;
		return (S)this;
	}
	
    @MayBeExcluded
	public S appendList(List<C> items) {
		for(C item : items) append(item);
		return (S)this;
	}

    @MayBeExcluded
	public S appendAll(C[] items) {
		for(C item : items) append(item);
		return (S)this;
	}
	
    @MayBeExcluded
	public S styleDisplay(String d) {
		node.style.display = d;
		return (S)this;
	}
	
    @MayBeExcluded
	public S cssText(String cssText) {
	    node.style.cssText = cssText;
        return (S)this;
	}
	
    @MayBeExcluded
    public S cursor(String cursor) {
        node.style.cursor = cursor;
        return (S)this;
    }
    
    @MayBeExcluded
    public S cursorPointer() {
        node.style.cursor = "pointer";
        return (S)this;
    }

    @MayBeExcluded
    public S position(String position) {
        node.style.position = position;
        return (S)this;
    }
    
    @MayBeExcluded
	public S add(DOMBuilder<? extends C, ? extends Node, ?> b) {
		if(null!=b) append(b.node);
		return (S)this;
	}
	
    @MayBeExcluded
	public S addFirst(DOMBuilder<? extends C, ? extends Node, ?> b) {
		if(null!=b) node.insertBefore(b.node, node.firstChild);
		return (S)this;
	}
	
    @MayBeExcluded
	public S addList(List<DOMBuilder<? extends C, ? extends Node, ?>> items) {
		if(null!=items)
		for(DOMBuilder<? extends C, ? extends Node, ?> b : items)  append(b.node);
		return (S)this;
	}

    @MayBeExcluded
	public S addAll(DOMBuilder<? extends C, ? extends Node, ?>[] items) {
		if(null!=items)
		for(DOMBuilder<? extends C, ? extends Node, ?> b : items)  append(b.node);
		return (S)this;
	}

    @MayBeExcluded
	public S hide() {
		node.style.display = "none";
		return (S)this;
	}

    @MayBeExcluded
	public S show() {
		node.style.display = "";
		return (S)this;
	}
	
    @MayBeExcluded
	public S showHide(boolean show) {
		node.style.display = show ? "" : "none";
		return (S)this;
	}
	
    @MayBeExcluded
	public String toggle() {
		return (node.style.display = node.style.display=="" ? "none" : "");
	}

    @MayBeExcluded
	public S type(String type) {
		node.type = type;
		return (S)this;
	}

    @MayBeExcluded
	public S name(String name) {
		node.name = null==name ? "" : name;
		return (S)this;
	}

    @MayBeExcluded
	public S value(Object value) {
		node.value = null==value?"":value;
		return (S)this;
	}
	
    @MayBeExcluded
	public S temp1(Object t) {
		node.temp1 = t;
		return (S)this;
	}

    @MayBeExcluded
	public S temp2(Object t) {
		node.temp2 = t;
		return (S)this;
	}

    @MayBeExcluded
	public S temp3(Object t) {
		node.temp3 = t;
		return (S)this;
	}

    @MayBeExcluded
	public S width(String width) {
		node.width = width;
		return (S)this;
	}
	
    @MayBeExcluded
    public S height(String width) {
        node.height = width;
        return (S)this;
    }
    
    @MayBeExcluded
	public S method(String method) {
		node.method = method;
		return (S)this;
	}
	
    @MayBeExcluded
	public S enctype(String enctype) {
		node.enctype = enctype;
		return (S)this;
	}

    @MayBeExcluded
	public S width100() {
		node.width = "100%";
		return (S)this;
	}

    @MayBeExcluded
	public S onChange(CommonDelegate<Boolean, S> callback) {
		setEventDelegate(EV_ONCHANGE, callback);
		return (S)this;
	}

    @MayBeExcluded
	public S vAlign(String va) {
		node.vAlign = va;
		return (S)this;
	}

    @MayBeExcluded
	public S src(String src) {
		node.src = src;
		return (S)this;
	}

    @MayBeExcluded
	public S title(String t) {
		node.title = t;
		return (S)this;
	}

    @MayBeExcluded
	public S field(String f) {
		node.field = f;
		return (S)this;
	}
	
    @MayBeExcluded
	public S fieldDisabled() {
		node.field = DISABLED;
		return (S)this;
	}

    @MayBeExcluded
	public S BR() {
		return append((C)createNode("br"));
	}

    @MayBeExcluded
	public S validator(InputValidator v) {
		node.validator = v;
		return (S)this;
	}

    @MayBeExcluded
	public S removeFromDom() {
		if(null!=node.parentNode) node.parentNode.removeChild(node);
		return (S)this;
	}

    @MayBeExcluded
	public S align(String a) {
		node.align = a;
		return (S)this;
	}

    @MayBeExcluded
	public S innerHTML(String ih) {
		node.innerHTML = ih;
		return (S)this;
	}

    @MayBeExcluded
	public S checked(boolean ch) {
		node.checked = ch;
		return (S)this;
	}

    @MayBeExcluded
	public void forEachSubchild(CommonDelegate<Boolean, Node> cb) {
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

    @MayBeExcluded
	public S size(int s) {
		node.size = s;
		return (S)this;
	}

    @MayBeExcluded
	public S readOnly(boolean ro) {
		node.readOnly = ro;
		return (S)this;
	}

    @MayBeExcluded
	public Integer valueAsInt() {
		if(node.value==null || node.value=="") return null;
		return 1 * (Integer)node.value;
	}
	
    @MayBeExcluded
    public Long valueAsLong() {
        if(node.value==null || node.value=="") return null;
        return 1 * (Long)node.value;
    }
    
    @MayBeExcluded
	public boolean isChecked() {
		return node.checked;
	}

    @MayBeExcluded
	public S onKeyUp(CommonDelegate<Boolean, S> callback) {
		setEventDelegate(EV_ONKEYUP, callback);
		return (S)this;
	}

    @MayBeExcluded
	public S onKeyPress(CommonDelegate<Boolean, S> callback) {
		setEventDelegate(EV_ONKEYPRESS, callback);
		return (S)this;
	}

    @MayBeExcluded
	public S onBlur(CommonDelegate<Boolean, S> callback) {
		setEventDelegate(EV_ONBLUR, callback);
		return (S)this;
	}

    @MayBeExcluded
	public S onFocus(CommonDelegate<Boolean, S> callback) {
		setEventDelegate(EV_ONFOCUS, callback);
		return (S)this;
	}
	
    @MayBeExcluded
	public S onKeyDown(CommonDelegate<Boolean, S> callback) {
		setEventDelegate(EV_ONKEYDOWN, callback);
		return (S)this;
	}
	
    @MayBeExcluded
	public String valueAsStr() {
		return (String)node.value;
	}

    @MayBeExcluded
	public void appendTo(DOMBuilder<? extends Node, N, ?> parent) {
		removeFromDom();
		if(null!=parent) parent.append(this.node);
	}

    @MayBeExcluded
	public S id(String id) {
		node.id = id;
		return (S)this;
	}

    @MayBeExcluded
	public S styleFontSize(String fs) {
		node.style.fontSize = fs;
		return (S)this;
	}

    @MayBeExcluded
	public S styleFontWeight(String fs) {
		node.style.fontWeight = fs;
		return (S)this;
	}
	
    @MayBeExcluded
	public S styleTextDecoration(String td) {
		node.style.textDecoration = td;
		return (S)this;
	}
	
    @MayBeExcluded
	public S disabled(boolean d) {
		node.disabled = d;
		return (S)this;
	}
	
    @MayBeExcluded
	public S valignTop() {
		node.vAlign = "top";
		return (S)this;
	}

    @MayBeExcluded
	public S stylePaddingLeft(String pl) {
		node.style.paddingLeft = pl;
		return (S)this;
	}

    @MayBeExcluded
	public S styleMargin(String m) {
		node.style.margin = m;
		return (S)this;
	}

    @MayBeExcluded
	public S stylePadding(String p) {
		node.style.padding = p;
		return (S)this;
	}
	
    @MayBeExcluded
	public S absolutePosition() {
		node.style.position = "absolute";
		return (S)this;
	}

    @MayBeExcluded
	public S alignRight() {
		node.align = "right";
		return (S)this;
	}

    @MayBeExcluded
	public S alignCenter() {
		node.align = "center";
		return (S)this;
	}
	
    @MayBeExcluded
	public S styleWidth(Object w) {
		node.style.width = w;
		return (S)this;
	}

	/**
	 * Set common style attribute
	 * node.style[key] = value
	 */
    @MayBeExcluded
	@NativeCode("{this.%node%.style[key] = value; return this;}")
	public S style(String key, Object value) {
		return (S)this;
	}
	
    @MayBeExcluded
    @NativeCode("{this.%node%[key] = value; return this;}")
    public S attribute(String key, Object value) {
        return (S)this;
    }
    
    @MayBeExcluded
	public S styleOpacity(double o) {
	    node.style.opacity = o;
	    node.style.filter = "alpha(opacity="+Math.round(o*100)+")";
	    return (S)this;
	}
	
    @MayBeExcluded
	public S styleHeight(Object h) {
		node.style.height = h;
		return (S)this;
	}
	
    @MayBeExcluded
	public S styleTop(Object t) {
		node.style.top = t;
		return (S)this;
	}

    @MayBeExcluded
	public S styleLeft(Object l) {
		node.style.left = l;
		return (S)this;
	}

    @MayBeExcluded
	public S styleMarginTop(String m) {
		node.style.marginTop = m;
		return (S)this;
	}

    @MayBeExcluded
	public S styleMarginLeft(String m) {
		node.style.marginLeft = m;
		return (S)this;
	}	
	
    @MayBeExcluded
	public S target(String t) {
		node.target = t;
		return (S)this;
	}

    @MayBeExcluded
	public S targetBlank() {
		node.target = "_blank";
		return (S)this;
	}

    @MayBeExcluded
	public S vspace(int s) {
		node.vspace = s;
		return (S)this;
	}
	
    @MayBeExcluded
	public S hspace(int s) {
		node.hspace = s;
		return (S)this;
	}
	
    @MayBeExcluded
	public S border(int b) {
		node.border = b;
		return (S)this;
	}

    @MayBeExcluded
	public S styleColor(String c) {
		node.style.color = c;
		return (S)this;
	}
	
    @MayBeExcluded
	public S styleBackground(String bg) {
		if(null!=bg) node.style.background = bg;
		return (S)this;
	}

    @MayBeExcluded
	public S styleBorder(String b) {
		node.style.border = b;
		return (S)this;
	}

    @MayBeExcluded
	public S styleBoxShadow(String bs) {
		node.style.boxShadow = bs;
		node.style.webkitBoxShadow = bs;
		node.style.MozBoxShadow = bs;
		return (S)this;
	}

    @MayBeExcluded
	public S styleTransform(String tr) {
		node.style.transform = tr;
		node.style.webkitTransform = tr;
		node.style.MozTransform = tr;
		return (S)this;
	}
	
    @MayBeExcluded
	public S styleTransformOrigin(String to) {
		node.style.transformOrigin = to;
		node.style.webkitTransformOrigin = to;
		node.style.MozTransformOrigin = to;
		return (S)this;
	}
	
    @MayBeExcluded
	public S styleBorderRadius(String br) {
		node.style.borderRadius = br;
		node.style.webkitBorderRadius = br;
		node.style.MozBorderRadius = br;
		return (S)this;
	}
	
    @MayBeExcluded
	public S styleBorderBottom(String b) {
		node.style.borderBottom = b;
		return (S)this;
	}

    @MayBeExcluded
	public S styleOverflow(String ov) {
		node.style.overflow = ov;
		return (S)this;
	}
	
    @MayBeExcluded
	public S styleFloat(String fl) {
		node.style.cssFloat = node.style.styleFloat = fl;
		return (S)this;
	}

    @MayBeExcluded
    public boolean isEmpty() {
        return node.childNodes.length == 0;
    }

	
    @MayBeExcluded
	@NativeCode("{if(existed.%node%.nextSibling == null) this.%add%(newChild);" +
			"else this.%node%.insertBefore(newChild.node, existed.%node%.nextSibling);" +
			"return this;}")
	public S insertAfter(DOMBuilder<? extends C, ? extends Node, ?> newChild, DOMBuilder<? extends C, ? extends Node, ?> existed) {
		return null;
	}
	
    @MayBeExcluded
	@NativeCode("{alert(this.%node% + ' :: ' + existed.nextSibling);if(existed.nextSibling == null) this.%add%(newChild);" +
			"else this.%node%.insertBefore(newChild.node, existed.nextSibling);" +
			"return this;}")
	public S insertAfterN(DOMBuilder<? extends C, ? extends Node, ?> newChild, Node existed) {
		return null;
	}
	
    @MayBeExcluded
    public static DOMBuilder wrap(Node node) {
        if(node == null) return null;
        DOMBuilder b = new NodeBuilder(null);
        b.node = node;
        return b;
    }
}