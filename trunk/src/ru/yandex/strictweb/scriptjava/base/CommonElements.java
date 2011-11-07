package ru.yandex.strictweb.scriptjava.base;

import ru.yandex.strictweb.scriptjava.base.custom.NoChildNodeBuilder;
import ru.yandex.strictweb.scriptjava.base.custom.SelectNodeBuilder;
import ru.yandex.strictweb.scriptjava.base.custom.SelectOptionNodeBuilder;
import ru.yandex.strictweb.scriptjava.base.custom.TableColumnNodeBuilder;
import ru.yandex.strictweb.scriptjava.base.custom.TableInnerNodeBuilder;
import ru.yandex.strictweb.scriptjava.base.custom.TableNodeBuilder;
import ru.yandex.strictweb.scriptjava.base.custom.TableRowNodeBuilder;


public class CommonElements extends StrictWeb {
	public static final CommonDelegate<Boolean, NodeBuilder> doNothing = new CommonDelegate<Boolean, NodeBuilder>() {
		public Boolean delegate(NodeBuilder n) {
			stopEvent();
			return false;
		}
	};
	
    @MayBeExcluded
	public static final NodeBuilder $A(String href) {
		return new NodeBuilder("a").href(href);
	}
	
    @MayBeExcluded
	public static final NodeBuilder $DIV() {
		return new NodeBuilder("div");
	}
	
    @MayBeExcluded
	public static final NodeBuilder $FIELDSET(String legend) {
		NodeBuilder fs = new NodeBuilder("fieldset");
		if(null!=legend) fs.add(new NodeBuilder("legend").text(legend));
		return fs;
	}
	
    @MayBeExcluded
	public static final NodeBuilder $FORM(String action) {
		return new NodeBuilder("form").action(action);
	}

    @MayBeExcluded
	public static final NodeBuilder $SPAN() {
		return new NodeBuilder("span");
	}
	
    @MayBeExcluded
	public static final NodeBuilder $LABEL(Object title, Node node) {
		NodeBuilder l = new NodeBuilder("label").append((Node)title);
		if(!(Boolean)(Object)node.id) node.id = node.name + Math.random();
		l.node.htmlFor = node.id;
		return l;
	}

    @MayBeExcluded
	public static final TableRowNodeBuilder $TR() {
		return new TableRowNodeBuilder();
	}

    @MayBeExcluded
	public static final TableColumnNodeBuilder $TD() {
		return new TableColumnNodeBuilder();
	}
	
    @MayBeExcluded
	public static final TableNodeBuilder $TABLE() {
		return new TableNodeBuilder();//.cellPaddingSpacing0();
	}
	
    @MayBeExcluded
	public static final TableInnerNodeBuilder $TBODY() {
		return new TableInnerNodeBuilder("tbody");
	}
	
    @MayBeExcluded
	public static final TableInnerNodeBuilder $THEAD() {
		return new TableInnerNodeBuilder("thead");
	}

    @MayBeExcluded
	public static final SelectNodeBuilder $SELECT() {
		return new SelectNodeBuilder();
	}

    @MayBeExcluded
	public static final NodeBuilder $B(String text) {
		return new NodeBuilder("b").text(text);
	}

    @MayBeExcluded
	public static final NoChildNodeBuilder $HR() {
		return new NoChildNodeBuilder("hr");
	}

    @MayBeExcluded
	public static final NodeBuilder $I() {
		return new NodeBuilder("i");
	}
	
    @MayBeExcluded
	public static final NodeBuilder $P() {
		return new NodeBuilder("p");
	}

    @MayBeExcluded
	public static final SelectOptionNodeBuilder $OPTION(Object val, String text) {
		return new SelectOptionNodeBuilder().value(val).text(text);
	}
	
    @MayBeExcluded
	public static final NoChildNodeBuilder $IMG(String src) {
		return new NoChildNodeBuilder("img").src(src==null?"about:blank":src);
	}
	
    @MayBeExcluded
	public static final NodeBuilder $BTN(String title, CommonDelegate<Boolean, NodeBuilder> cb) {
		return new NodeBuilder("button").text(title).onClick(cb);
	}

    @MayBeExcluded
	public static final NoChildNodeBuilder $INPUT() {
		return new NoChildNodeBuilder("input");
	}
	
    @MayBeExcluded
	public static final NoChildNodeBuilder $HIDDEN(String name, Object value) {
		return new NoChildNodeBuilder("input").type("hidden").name(name).value(value);
	}

    @MayBeExcluded
	public static final NoChildNodeBuilder $TEXTBOX(String name) {
		return new NoChildNodeBuilder("input").className("text").type("text").name(name);
	}

    @MayBeExcluded
	public static final NoChildNodeBuilder $TEXTAREA(String name) {
		return new NoChildNodeBuilder("textarea").className("text").name(name);
	}
	    
    @MayBeExcluded
    public static final NoChildNodeBuilder $CHECKBOX(String name, boolean checked) {
        return new NoChildNodeBuilder("input").className("cb").type("checkbox").name(name).checked(checked);
    }

    @MayBeExcluded
    public static final NoChildNodeBuilder $RADIO(String name, boolean checked) {
        return new NoChildNodeBuilder("input").className("cb").type("radio").name(name).checked(checked);
    }	
}
