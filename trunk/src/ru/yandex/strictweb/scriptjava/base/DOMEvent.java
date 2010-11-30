package ru.yandex.strictweb.scriptjava.base;

@Native
public class DOMEvent {
	public int keyCode;
	public boolean ctrlKey;
	public boolean altKey;
	public boolean shiftKey;
	public Integer pageX, pageY;
	public Integer clientX, clientY;
	public int button;
	
	/** script web target */
	public Node target;
	public Node srcElement;
	
	/** IE */
	@Deprecated
	public Node fromElement;
}
