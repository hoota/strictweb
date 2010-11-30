package ru.yandex.strictweb.scriptjava.base;

import ru.yandex.strictweb.scriptjava.base.util.TimeoutIdentifier;


public class ValidatorHelperBase extends CommonElements {
	public static final String msgClassName = "invalideFieldMessage";
	
	public boolean validate(DOMBuilder root) {
		try {
			root.forEachSubchild(new DOMEventCallback() {
				public boolean delegate(Node n) {
					if(n.field == DOMBuilder.DISABLED) return false;
					
					if(n.className == msgClassName) n.parentNode.removeChild(n);
					else
					if(null != n.validator && !n.validator.isValid(n)) {
						showInvalidMessage(n, n.validator.getMessage());
						throw new RuntimeException();
					}
					
					return true;
				}
			});
			
			return true;
		}catch(RuntimeException e) {
			return false;
		}
	}

	public void showInvalidMessage(final Node n, String message) {
		n.parentNode.insertBefore(
			$DIV().className(msgClassName).text(message).node, 
			n//.parentNode.firstChild
		);
		
		n.focus();
		ScriptJava.setTimeout(new VoidDelegate<TimeoutIdentifier>() {
			public void voidDelegate(TimeoutIdentifier arg) {
				int top = 0;
				for(Node q = n; q!=null && q.offsetTop>0; q = q.offsetParent) top += q.offsetTop;
				if(top - ScriptJava.document.body.scrollTop < 50) {
					ScriptJava.window.scrollBy(0, -100);
				}
			}
		}, 100);
		
	}
}
