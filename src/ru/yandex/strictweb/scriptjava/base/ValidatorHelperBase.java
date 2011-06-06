package ru.yandex.strictweb.scriptjava.base;

import ru.yandex.strictweb.scriptjava.base.util.TimeoutIdentifier;


public class ValidatorHelperBase extends CommonElements {
	public String msgClassName = "invalidFieldMessage";
	public boolean ignoreDisabledFields = true;
    private boolean hasError;
	
	public ValidatorHelperBase() {
    }
	
	@MayBeExcluded
	public ValidatorHelperBase setIgnoreDisabledFields(boolean ignoreDisabledFields) {
        this.ignoreDisabledFields = ignoreDisabledFields;
        return this;
    }
	
    @MayBeExcluded
	public ValidatorHelperBase setMsgClassName(String msgClassName) {
        this.msgClassName = msgClassName;
        return this;
    }
    
    @MayBeExcluded
    public boolean validate(DOMBuilder root) {
		try {
		    hasError = false;
			root.forEachSubchild(new CommonDelegate<Boolean, Node>() {
				public Boolean delegate(Node n) {
					if(ignoreDisabledFields && n.field == DOMBuilder.DISABLED) return false;
					
					if(n.className == msgClassName) n.parentNode.removeChild(n);
					else
					if(null != n.validator) {
					    boolean isValid = n.validator.isValid(n);
					    decorateElement(n, n.validator.getMessage(), isValid);
					    if(!isValid) hasError = true;
					}
					
					return true;
				}
			});
			
			return !hasError;
		}catch(RuntimeException e) {
			return false;
		}
	}

	public void decorateElement(final Node n, String message, boolean isValid) {
	    if(!isValid) {
            Node errorNode = $DIV().className(msgClassName).text(message).node;
            n.parentNode.insertBefore(errorNode, n);
            
    		focusOnInput(n, message, isValid);
	    }
	}

    public void focusOnInput(final Node n, String message, boolean isValid) {
        if(!isValid && !hasError) {
		    n.focus();
		    StrictWeb.setTimeout(new VoidDelegate<TimeoutIdentifier>() {
		        public void voidDelegate(TimeoutIdentifier arg) {
		            int top = 0;
		            for(Node q = n; q!=null && q.offsetTop>0; q = q.offsetParent) top += q.offsetTop;
		            if(top - StrictWeb.document.body.scrollTop < 50) {
		                StrictWeb.window.scrollBy(0, -100);
		            }
		        }
		    }, 100);
		}
    }
}
