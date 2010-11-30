package ru.yandex.strictweb.scriptjava.base;

@Native
public abstract class BrowserHistory {
	public int length;
	abstract public void back();
	abstract public void forward();	
	abstract public void go(int delta);
}
