package ru.yandex.strictweb.scriptjava.base;

import ru.yandex.strictweb.scriptjava.base.ajax.XMLHttpRequest;
import ru.yandex.strictweb.scriptjava.base.util.IntervalIdentifier;
import ru.yandex.strictweb.scriptjava.base.util.TimeoutIdentifier;

@Native
public abstract class Window {
	public Document document;
	public Window opener;
	public String name;
	public Window parent;
	public Window self;
	public String status;
	public Window top;
	public Window window;
	public Window[] frames;
	public int lenght;
	public boolean closed;
	
	public int outerWidth, outerHeight;
	public int innerWidth, innerHeight;
	
	public int pageXOffset, scrollX;
	public int pageYOffset, scrollY;
	public int scrollMaxX, scrollMaxY;
	
	/** Please Use ScriptJava.event to get global event */
	@Deprecated
	public DOMEvent event;
	
	public Navigator navigator;
	public BrowserHistory history;
	public XMLHttpRequest XMLHttpRequest;
	public Screen screen;
	
	abstract public Object open(String url);
	abstract public void alert(Object msg);
	abstract public String escape(String str);
	abstract public String unescape(String substring);
	abstract public void scrollBy(int deltaX, int deltaY);
	abstract public void scroll(int x, int y);
	abstract public void close();
	abstract public boolean confirm(String message);
    abstract public <K> K prompt(String message, String defValue);
	abstract public void focus();
	abstract public void clearInterval(IntervalIdentifier intervalId);
	abstract public void clearTimeout(TimeoutIdentifier timeoutId);
}
