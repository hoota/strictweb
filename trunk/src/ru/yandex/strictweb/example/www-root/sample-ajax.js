function ScriptJava() {this.ScriptJava_initInstanceFields();}
ScriptJava.prototype.ScriptJava_constructor = function(){}

ScriptJava.prototype.ScriptJava_initInstanceFields = function() {
}
ScriptJava.evalFunction = function(code){var f = new Function(code); return f();};String.prototype.trim = function() {return this.replace(/^\s+/, '').replace(/\s+$/, '');};String.prototype.toHTML = function() {return ScriptJava.toHTML(this);}
ScriptJava.toHTML = function(str){return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');}
ScriptJava.round2 = function(val){if(typeof val != 'number') return '';val = '00'+Math.round(val*100.0);return val.replace(/([0-9]{2})$/, '.$1').replace(/\.0+$/, '').replace(/^0+([^\.])/, '$1');}
ScriptJava.getDocument = function(){return document;}
ScriptJava.getConsole = function(){return typeof console!='undefined' && typeof console.debug == 'function' ? console : null;}
ScriptJava.$ = function(id){var n = document.getElementById(id); return null==n?null:NodeBuilder.wrap(n);}
ScriptJava.$$ = function(id){return document.getElementById(id);}
ScriptJava.getWindow = function(){return window;}
ScriptJava.createNode = function(tagName){if(tagName!=null) return document.createElement(tagName);}
ScriptJava.textNode = function(s){return document.createTextNode(s);}
ScriptJava.EL = function(tagName) {
return new NodeBuilder(tagName);
}

ScriptJava.$CHECKBOX = function(name,checked) {
return ScriptJava.EL('input').className('cb').type('checkbox').name(name).checked(checked);
}

ScriptJava.$RADIO = function(name,checked) {
return ScriptJava.EL('input').className('cb').type('radio').name(name).checked(checked);
}

ScriptJava.$CHECKBOXcustom = function(name,checked,text,checkedUrl,uncheckedUrl){var nb = ScriptJava.EL('b');nb.node = ScriptJava.$CHECKBOX(name, checked, text, checkedUrl, uncheckedUrl);return nb;}
ScriptJava.extend = function(dst,src){for(var k in src) if(k!='prototype')dst[k] = src[k];return dst;}
ScriptJava.stopEvent = function(){e = ScriptJava.globalEvent;if(!e) return;e.stopPropagation?e.stopPropagation():e.cancelBubble=true;return e;}
ScriptJava.setDOMEventCallback = function(obj,name,cb){if(cb==null){obj[name]=null;return;};obj[name] = typeof cb=='function'?cb:function(ev, nullNode) {ScriptJava.swTarget=null;ScriptJava.globalEvent=ev||window.event;if(cb&&cb.delegate)return cb.delegate(nullNode ? null : this);return false;}}
ScriptJava.setVoidEventCallback = function(obj,name,cb){if(cb==null){obj[name]=null;return;};obj[name] = typeof cb=='function'?cb:function(ev) {if(cb&&cb.voidDelegate) cb.voidDelegate(this);return false;}}
ScriptJava.removeEventListener = function(obj,event,func,useCapture){obj.removeEventListener(event, func, useCapture);}
ScriptJava.addEventListener = function(obj,event,cb,useCapture){if(cb==null)return null;var f = function(ev, nullNode) {ScriptJava.swTarget=null;ScriptJava.globalEvent=ev||window.event;return cb.delegate(nullNode ? null : this);};obj.addEventListener(event, f, useCapture); return f;}
ScriptJava.onLoadWindow = function(cb) {
ScriptJava.setDOMEventCallback(ScriptJava.window,'onload',cb);
}

ScriptJava.regExp = function(base,flags){return new RegExp(base, flags);}
ScriptJava.compareDates = function(d1,d2) {
var t1 = d1.getFullYear();
var t2 = d2.getFullYear();
if(t1<t2)return -1;
if(t1>t2)return 1;
t1=d1.getMonth();
t2=d2.getMonth();
if(t1<t2)return -1;
if(t1>t2)return 1;
t1=d1.getDate();
t2=d2.getDate();
if(t1<t2)return -1;
if(t1>t2)return 1;
return 0;
}

ScriptJava.compareTimes = function(d1,d2){if(d1<d2)return -1;if(d1>d2)return 1;return 0;}
ScriptJava.compareStrings = function(str1,str2){if(str1<str2)return -1;if(str1>str2)return 1;return 0;}
ScriptJava.compareNumbers = function(n1,n2){if(n1<n2)return -1;if(n1>n2)return 1;return 0;}
ScriptJava.jsSortList = function(list,comparator){return list.sort(comparator.compare);}
ScriptJava.isNaN = function(o){return isNaN(o);}
ScriptJava.arrayRemove = function(list,obj) {
var res = [];
{var _list1=list;for(var _i0=0;_i0<_list1.length;_i0++) {var o=_list1[_i0];if(o!=obj)res.push(o);
}}
return res;
}

ScriptJava.getCookie = function(name) {
var dc = ScriptJava.document.cookie;
var prefix = name+'=';
var begin = dc.indexOf('; '+prefix);
if(begin==-1) {
begin=dc.indexOf(prefix);
if(begin!=0)return null;
}
 else  {
begin+=2;
}
var end = dc.indexOf(';',begin);
if(end==-1) {
end=dc.length;
}
return ScriptJava.window.unescape(dc.substring(begin+prefix.length,end));
}

ScriptJava.setCookie = function(name,value) {
ScriptJava.setCookieFull(name,value,null,null,null,false);
}

ScriptJava.setCookieFull = function(name,value,expires,path,domain,secure) {
if(null==expires) {
expires=new Date();
expires.setFullYear(expires.getFullYear()+10);
}
ScriptJava.document.cookie=name+'='+ScriptJava.window.escape(value)+('; expires='+expires.toGMTString())+(path!=null?'; path='+path:'')+(domain!=null?'; domain='+domain:'')+(secure?'; secure':'');
}

ScriptJava.setInterval = function(cb,millis){var iid=setInterval(function(){cb.voidDelegate(iid);}, millis);return iid;};
ScriptJava.setTimeout = function(cb,millis){var iid=setTimeout(function(){cb.voidDelegate(iid);}, millis);return iid;};
ScriptJava.clearInterval = function(intervalId) {
ScriptJava.window.clearInterval(intervalId);
}

ScriptJava.clearTimeout = function(timeoutId) {
ScriptJava.window.clearTimeout(timeoutId);
}

ScriptJava.typeOf = function(obj){return typeof obj;}
ScriptJava.isEnum = function(obj){return obj._isEnum;}
ScriptJava.isInstanceOfDate = function(obj){return obj instanceof Date;}
ScriptJava.isInstanceOfArray = function(obj){return obj instanceof Date || (typeof obj.length != 'undefined' && typeof obj['0'] != 'undefined');}
ScriptJava.isInstanceOfNode = function(obj){return obj.tagName;}
ScriptJava.dateToString = function(d) {
if(null==d)return '';
return (d.getDate()<10?'0':'')+d.getDate()+'.'+(d.getMonth()<9?'0':'')+(d.getMonth()+1)+'.'+d.getFullYear();
}

ScriptJava.dateTimeToString = function(d) {
if(null==d)return '';
return ScriptJava.dateToString(d)+' '+(d.getHours()<10?'0':'')+d.getHours()+':'+(d.getMinutes()<10?'0':'')+d.getMinutes();
}

ScriptJava.dateToStringSmart = function(d) {
if(null==d)return '';
return d.getHours()==0&&d.getMinutes()==0?ScriptJava.dateToString(d):ScriptJava.dateTimeToString(d);
}

ScriptJava.listJoin = function(list,sep){return list.join(sep);}
ScriptJava.scrollToVisible = function(n) {
var top = 0;
for(;n!=null;n=n.offsetParent) {
top+=n.offsetTop;
}
ScriptJava.window.scroll(0,top);
}

ScriptJava.newJsMap = function(){return {};}
ScriptJava.newJsSet = function(){return {};}
ScriptJava.newJsList = function(){return [];}

function JsException(message){this.JsException_initInstanceFields();this.JsException_constructor(message);}

JsException.prototype.JsException_initInstanceFields = function() {
}
JsException.prototype.JsException_constructor = function(message) {
this.message=message;
}

JsException.prototype.JsException_toString =
JsException.prototype.toString = function() {
return this.message;
}


function InputValidator() {this.InputValidator_initInstanceFields();}
InputValidator.prototype.InputValidator_constructor = function(){}

InputValidator.prototype.InputValidator_initInstanceFields = function() {
}

function DOMBuilder(tagName){this.DOMBuilder_initInstanceFields();this.DOMBuilder_constructor(tagName);}

ScriptJava.extend(DOMBuilder, ScriptJava);
ScriptJava.extend(DOMBuilder.prototype, ScriptJava.prototype);
DOMBuilder.EV_ONKEYDOWN = 'onkeydown';
DOMBuilder.EV_ONBLUR = 'onblur';
DOMBuilder.EV_ONKEYPRESS = 'onkeypress';
DOMBuilder.EV_ONKEYUP = 'onkeyup';
DOMBuilder.EV_ONCHANGE = 'onchange';
DOMBuilder.EV_ONDBLCLICK = 'ondblclick';
DOMBuilder.EV_ONSUBMIT = 'onsubmit';
DOMBuilder.EV_ONMOUSEDOWN = 'onmousedown';
DOMBuilder.EV_ONMOUSEUP = 'onmouseup';
DOMBuilder.EV_ONMOUSEOUT = 'onmouseout';
DOMBuilder.EV_ONMOUSEMOVE = 'onmousemove';
DOMBuilder.EV_ONCLICK = 'onclick';
DOMBuilder.DISABLED = 'disabled';
DOMBuilder.prototype.DOMBuilder_initInstanceFields = function() {
this.ScriptJava_initInstanceFields();
}
DOMBuilder.prototype.DOMBuilder_constructor = function() {
}

DOMBuilder.prototype.DOMBuilder_constructor = function(tagName) {
this.node=ScriptJava.createNode(tagName);
}

DOMBuilder.prototype.fireEvent = function(eventName,nullNode){this.node[eventName](ScriptJava.globalEvent, nullNode);return this;}
DOMBuilder.prototype.onClick = function(callback) {
ScriptJava.setDOMEventCallback(this.node,DOMBuilder.EV_ONCLICK,callback);
return this;
}

DOMBuilder.prototype.onMouseMove = function(onMouseMoveCallback) {
ScriptJava.setDOMEventCallback(this.node,DOMBuilder.EV_ONMOUSEMOVE,onMouseMoveCallback);
return this;
}

DOMBuilder.prototype.onMouseOut = function(onMouseOutCallback) {
ScriptJava.setDOMEventCallback(this.node,DOMBuilder.EV_ONMOUSEOUT,onMouseOutCallback);
return this;
}

DOMBuilder.prototype.onMouseUp = function(onMouseUpCallback) {
ScriptJava.setDOMEventCallback(this.node,DOMBuilder.EV_ONMOUSEUP,onMouseUpCallback);
return this;
}

DOMBuilder.prototype.onMouseDown = function(onMouseDownCallback) {
ScriptJava.setDOMEventCallback(this.node,DOMBuilder.EV_ONMOUSEDOWN,onMouseDownCallback);
return this;
}

DOMBuilder.prototype.onSubmit = function(callback) {
ScriptJava.setDOMEventCallback(this.node,DOMBuilder.EV_ONSUBMIT,callback);
return this;
}

DOMBuilder.prototype.onDblClick = function(callback) {
ScriptJava.setDOMEventCallback(this.node,DOMBuilder.EV_ONDBLCLICK,callback);
return this;
}

DOMBuilder.prototype.text = function(t) {
return this.append(t);
}

DOMBuilder.prototype.textB = function(t) {
return this.add(new NodeBuilder('b').text(t));
}

DOMBuilder.prototype.removeChilds = function() {
while(null!=this.node.firstChild) {
this.node.removeChild(this.node.firstChild);
}
return this;
}

DOMBuilder.prototype.append = function(c) {
if(c!=null) {
this.node.appendChild(c.nodeName==null?ScriptJava.document.createTextNode(c):c);
}
return this;
}

DOMBuilder.prototype.className = function(className) {
this.node.className=className;
return this;
}

DOMBuilder.prototype.DOMBuilder_href =
DOMBuilder.prototype.href = function(href) {
this.node.href=href;
return this;
}

DOMBuilder.prototype.DOMBuilder_action =
DOMBuilder.prototype.action = function(a) {
this.node.action=a;
return this;
}

DOMBuilder.prototype.DOMBuilder_appendList =
DOMBuilder.prototype.appendList = function(items) {
{var _list3=items;for(var _i2=0;_i2<_list3.length;_i2++) {var item=_list3[_i2];this.append(item);
}}
return this;
}

DOMBuilder.prototype.DOMBuilder_appendAll =
DOMBuilder.prototype.appendAll = function(items) {
{var _list5=items;for(var _i4=0;_i4<_list5.length;_i4++) {var item=_list5[_i4];this.append(item);
}}
return this;
}

DOMBuilder.prototype.DOMBuilder_styleDisplay =
DOMBuilder.prototype.styleDisplay = function(d) {
this.node.style.display=d;
return this;
}

DOMBuilder.prototype.DOMBuilder_add =
DOMBuilder.prototype.add = function(b) {
if(null!=b)this.append(b.node);
return this;
}

DOMBuilder.prototype.DOMBuilder_addList =
DOMBuilder.prototype.addList = function(items) {
if(null!=items){var _list7=items;for(var _i6=0;_i6<_list7.length;_i6++) {var b=_list7[_i6];this.append(b.node);
}}
return this;
}

DOMBuilder.prototype.DOMBuilder_addAll =
DOMBuilder.prototype.addAll = function(items) {
if(null!=items){var _list9=items;for(var _i8=0;_i8<_list9.length;_i8++) {var b=_list9[_i8];this.append(b.node);
}}
return this;
}

DOMBuilder.prototype.DOMBuilder_hide =
DOMBuilder.prototype.hide = function() {
this.node.style.display='none';
return this;
}

DOMBuilder.prototype.DOMBuilder_show =
DOMBuilder.prototype.show = function() {
this.node.style.display='';
return this;
}

DOMBuilder.prototype.DOMBuilder_showHide =
DOMBuilder.prototype.showHide = function(show) {
this.node.style.display=show?'':'none';
return this;
}

DOMBuilder.prototype.DOMBuilder_toggle =
DOMBuilder.prototype.toggle = function() {
this.node.style.display=this.node.style.display==''?'none':'';
}

DOMBuilder.prototype.DOMBuilder_type =
DOMBuilder.prototype.type = function(type) {
this.node.type=type;
return this;
}

DOMBuilder.prototype.DOMBuilder_name =
DOMBuilder.prototype.name = function(name) {
this.node.name=null==name?'':name;
return this;
}

DOMBuilder.prototype.DOMBuilder_value =
DOMBuilder.prototype.value = function(value) {
this.node.value=null==value?'':value;
return this;
}

DOMBuilder.prototype.DOMBuilder_temp1 =
DOMBuilder.prototype.temp1 = function(t) {
this.node.temp1=t;
return this;
}

DOMBuilder.prototype.DOMBuilder_temp2 =
DOMBuilder.prototype.temp2 = function(t) {
this.node.temp2=t;
return this;
}

DOMBuilder.prototype.DOMBuilder_temp3 =
DOMBuilder.prototype.temp3 = function(t) {
this.node.temp3=t;
return this;
}

DOMBuilder.prototype.DOMBuilder_width =
DOMBuilder.prototype.width = function(width) {
this.node.width=width;
return this;
}

DOMBuilder.prototype.DOMBuilder_method =
DOMBuilder.prototype.method = function(method) {
this.node.method=method;
return this;
}

DOMBuilder.prototype.DOMBuilder_enctype =
DOMBuilder.prototype.enctype = function(enctype) {
this.node.enctype=enctype;
return this;
}

DOMBuilder.prototype.DOMBuilder_width100 =
DOMBuilder.prototype.width100 = function() {
this.node.width='100%';
return this;
}

DOMBuilder.prototype.DOMBuilder_onChange =
DOMBuilder.prototype.onChange = function(callback) {
ScriptJava.setDOMEventCallback(this.node,DOMBuilder.EV_ONCHANGE,callback);
return this;
}

DOMBuilder.prototype.DOMBuilder_vAlign =
DOMBuilder.prototype.vAlign = function(va) {
this.node.vAlign=va;
return this;
}

DOMBuilder.prototype.DOMBuilder_src =
DOMBuilder.prototype.src = function(src) {
this.node.src=src;
return this;
}

DOMBuilder.prototype.DOMBuilder_title =
DOMBuilder.prototype.title = function(t) {
this.node.title=t;
return this;
}

DOMBuilder.prototype.DOMBuilder_field =
DOMBuilder.prototype.field = function(f) {
this.node.field=f;
return this;
}

DOMBuilder.prototype.DOMBuilder_fieldDisabled =
DOMBuilder.prototype.fieldDisabled = function() {
this.node.field=DOMBuilder.DISABLED;
return this;
}

DOMBuilder.prototype.DOMBuilder_BR =
DOMBuilder.prototype.BR = function() {
return this.append(ScriptJava.createNode('br'));
}

DOMBuilder.prototype.DOMBuilder_validator =
DOMBuilder.prototype.validator = function(v) {
this.node.validator=v;
return this;
}

DOMBuilder.prototype.DOMBuilder_removeFromDom =
DOMBuilder.prototype.removeFromDom = function() {
if(null!=this.node.parentNode)this.node.parentNode.removeChild(this.node);
return this;
}

DOMBuilder.prototype.DOMBuilder_align =
DOMBuilder.prototype.align = function(a) {
this.node.align=a;
return this;
}

DOMBuilder.prototype.DOMBuilder_innerHTML =
DOMBuilder.prototype.innerHTML = function(ih) {
this.node.innerHTML=ih;
return this;
}

DOMBuilder.prototype.DOMBuilder_checked =
DOMBuilder.prototype.checked = function(ch) {
this.node.checked=ch;
return this;
}

DOMBuilder.prototype.DOMBuilder_forEachSubchild =
DOMBuilder.prototype.forEachSubchild = function(cb) {
var nodes = [];
nodes.push(this.node);
while(nodes.length>0) {
var n = nodes.shift();
if(cb.delegate(n)){var _list11=n.childNodes;for(var _i10=0;_i10<_list11.length;_i10++) {var c=_list11[_i10]; {
if(null!=c.tagName)nodes.push(c);
}
}}
}
}

DOMBuilder.prototype.DOMBuilder_size =
DOMBuilder.prototype.size = function(s) {
this.node.size=s;
return this;
}

DOMBuilder.prototype.DOMBuilder_readOnly =
DOMBuilder.prototype.readOnly = function(ro) {
this.node.readOnly=ro;
return this;
}

DOMBuilder.prototype.DOMBuilder_valueAsNum =
DOMBuilder.prototype.valueAsNum = function() {
if(null==this.node.value||this.node.value=='')return null;
return 1*this.node.value;
}

DOMBuilder.prototype.DOMBuilder_isChecked =
DOMBuilder.prototype.isChecked = function() {
return this.node.checked;
}

DOMBuilder.prototype.DOMBuilder_onKeyUp =
DOMBuilder.prototype.onKeyUp = function(callback) {
ScriptJava.setDOMEventCallback(this.node,DOMBuilder.EV_ONKEYUP,callback);
return this;
}

DOMBuilder.prototype.DOMBuilder_onKeyPress =
DOMBuilder.prototype.onKeyPress = function(callback) {
ScriptJava.setDOMEventCallback(this.node,DOMBuilder.EV_ONKEYPRESS,callback);
return this;
}

DOMBuilder.prototype.DOMBuilder_onBlur =
DOMBuilder.prototype.onBlur = function(callback) {
ScriptJava.setDOMEventCallback(this.node,DOMBuilder.EV_ONBLUR,callback);
return this;
}

DOMBuilder.prototype.DOMBuilder_onKeyDown =
DOMBuilder.prototype.onKeyDown = function(callback) {
ScriptJava.setDOMEventCallback(this.node,DOMBuilder.EV_ONKEYDOWN,callback);
return this;
}

DOMBuilder.prototype.DOMBuilder_valueAsStr =
DOMBuilder.prototype.valueAsStr = function() {
return this.node.value;
}

DOMBuilder.prototype.DOMBuilder_appendTo =
DOMBuilder.prototype.appendTo = function(parent) {
this.removeFromDom();
if(null!=parent)parent.append(this.node);
}

DOMBuilder.prototype.DOMBuilder_id =
DOMBuilder.prototype.id = function(id) {
this.node.id=id;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleFontSize =
DOMBuilder.prototype.styleFontSize = function(fs) {
this.node.style.fontSize=fs;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleFontWeight =
DOMBuilder.prototype.styleFontWeight = function(fs) {
this.node.style.fontWeight=fs;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleTextDecoration =
DOMBuilder.prototype.styleTextDecoration = function(td) {
this.node.style.textDecoration=td;
return this;
}

DOMBuilder.prototype.DOMBuilder_disabled =
DOMBuilder.prototype.disabled = function(d) {
this.node.disabled=d;
return this;
}

DOMBuilder.prototype.DOMBuilder_valignTop =
DOMBuilder.prototype.valignTop = function() {
this.node.vAlign='top';
return this;
}

DOMBuilder.prototype.DOMBuilder_stylePaddingLeft =
DOMBuilder.prototype.stylePaddingLeft = function(pl) {
this.node.style.paddingLeft=pl;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleMargin =
DOMBuilder.prototype.styleMargin = function(m) {
this.node.style.margin=m;
return this;
}

DOMBuilder.prototype.DOMBuilder_stylePadding =
DOMBuilder.prototype.stylePadding = function(p) {
this.node.style.padding=p;
return this;
}

DOMBuilder.prototype.DOMBuilder_absolutePosition =
DOMBuilder.prototype.absolutePosition = function() {
this.node.style.position='absolute';
return this;
}

DOMBuilder.prototype.DOMBuilder_alignRight =
DOMBuilder.prototype.alignRight = function() {
this.node.align='right';
return this;
}

DOMBuilder.prototype.DOMBuilder_alignCenter =
DOMBuilder.prototype.alignCenter = function() {
this.node.align='center';
return this;
}

DOMBuilder.prototype.DOMBuilder_styleWidth =
DOMBuilder.prototype.styleWidth = function(w) {
this.node.style.width=w;
return this;
}

DOMBuilder.prototype.DOMBuilder_style =
DOMBuilder.prototype.style = function(key,value){this.node.style[key] = value; return this;}
DOMBuilder.prototype.DOMBuilder_styleHeight =
DOMBuilder.prototype.styleHeight = function(h) {
this.node.style.height=h;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleTop =
DOMBuilder.prototype.styleTop = function(t) {
this.node.style.top=t;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleLeft =
DOMBuilder.prototype.styleLeft = function(l) {
this.node.style.left=l;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleMarginTop =
DOMBuilder.prototype.styleMarginTop = function(m) {
this.node.style.marginTop=m;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleMarginLeft =
DOMBuilder.prototype.styleMarginLeft = function(m) {
this.node.style.marginLeft=m;
return this;
}

DOMBuilder.prototype.DOMBuilder_target =
DOMBuilder.prototype.target = function(t) {
this.node.target=t;
return this;
}

DOMBuilder.prototype.DOMBuilder_targetBlank =
DOMBuilder.prototype.targetBlank = function() {
this.node.target='_blank';
return this;
}

DOMBuilder.prototype.DOMBuilder_vspace =
DOMBuilder.prototype.vspace = function(s) {
this.node.vspace=s;
return this;
}

DOMBuilder.prototype.DOMBuilder_hspace =
DOMBuilder.prototype.hspace = function(s) {
this.node.hspace=s;
return this;
}

DOMBuilder.prototype.DOMBuilder_border =
DOMBuilder.prototype.border = function(b) {
this.node.border=b;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleColor =
DOMBuilder.prototype.styleColor = function(c) {
this.node.style.color=c;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleBackground =
DOMBuilder.prototype.styleBackground = function(bg) {
if(null!=bg)this.node.style.background=bg;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleBorder =
DOMBuilder.prototype.styleBorder = function(b) {
this.node.style.border=b;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleBoxShadow =
DOMBuilder.prototype.styleBoxShadow = function(bs) {
this.node.style.boxShadow=bs;
this.node.style.webkitBoxShadow=bs;
this.node.style.MozBoxShadow=bs;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleTransform =
DOMBuilder.prototype.styleTransform = function(tr) {
this.node.style.transform=tr;
this.node.style.webkitTransform=tr;
this.node.style.MozTransform=tr;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleTransformOrigin =
DOMBuilder.prototype.styleTransformOrigin = function(to) {
this.node.style.transformOrigin=to;
this.node.style.webkitTransformOrigin=to;
this.node.style.MozTransformOrigin=to;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleBorderRadius =
DOMBuilder.prototype.styleBorderRadius = function(br) {
this.node.style.borderRadius=br;
this.node.style.webkitBorderRadius=br;
this.node.style.MozBorderRadius=br;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleBorderBottom =
DOMBuilder.prototype.styleBorderBottom = function(b) {
this.node.style.borderBottom=b;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleOverflow =
DOMBuilder.prototype.styleOverflow = function(ov) {
this.node.style.overflow=ov;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleFloat =
DOMBuilder.prototype.styleFloat = function(fl) {
this.node.style.cssFloat=this.node.style.styleFloat=fl;
return this;
}

DOMBuilder.prototype.DOMBuilder_isEmpty =
DOMBuilder.prototype.isEmpty = function() {
return this.node.childNodes.length==0;
}

DOMBuilder.prototype.DOMBuilder_insertAfter =
DOMBuilder.prototype.insertAfter = function(newChild,existed){if(existed.node.nextSibling == null) this.add(newChild);else this.node.insertBefore(newChild.node, existed.node.nextSibling);return this;}

function NodeBuilder(tag){this.NodeBuilder_initInstanceFields();this.NodeBuilder_constructor(tag);}

ScriptJava.extend(NodeBuilder, DOMBuilder);
ScriptJava.extend(NodeBuilder.prototype, DOMBuilder.prototype);
NodeBuilder.prototype.NodeBuilder_initInstanceFields = function() {
this.DOMBuilder_initInstanceFields();
}
NodeBuilder.prototype.NodeBuilder_constructor = function(tag) {
this.DOMBuilder_constructor(tag);
;
}

NodeBuilder.wrap = function(node) {
if(node==null)return null;
var nb = new NodeBuilder(null);
nb.node=node;
return nb;
}


function TableColumnNodeBuilder(){this.TableColumnNodeBuilder_initInstanceFields();this.TableColumnNodeBuilder_constructor();}

ScriptJava.extend(TableColumnNodeBuilder, DOMBuilder);
ScriptJava.extend(TableColumnNodeBuilder.prototype, DOMBuilder.prototype);
TableColumnNodeBuilder.prototype.TableColumnNodeBuilder_initInstanceFields = function() {
this.DOMBuilder_initInstanceFields();
}
TableColumnNodeBuilder.prototype.TableColumnNodeBuilder_constructor = function() {
this.DOMBuilder_constructor('td');
;
}

TableColumnNodeBuilder.prototype.TableColumnNodeBuilder_colSpan =
TableColumnNodeBuilder.prototype.colSpan = function(cs) {
this.node.colSpan=cs;
return this;
}

TableColumnNodeBuilder.prototype.TableColumnNodeBuilder_rowSpan =
TableColumnNodeBuilder.prototype.rowSpan = function(rs) {
this.node.rowSpan=rs;
return this;
}


function TableRowNodeBuilder(){this.TableRowNodeBuilder_initInstanceFields();this.TableRowNodeBuilder_constructor();}

ScriptJava.extend(TableRowNodeBuilder, DOMBuilder);
ScriptJava.extend(TableRowNodeBuilder.prototype, DOMBuilder.prototype);
TableRowNodeBuilder.prototype.TableRowNodeBuilder_initInstanceFields = function() {
this.DOMBuilder_initInstanceFields();
}
TableRowNodeBuilder.prototype.TableRowNodeBuilder_constructor = function() {
this.DOMBuilder_constructor('tr');
;
}

TableRowNodeBuilder.prototype.TableRowNodeBuilder_TDH =
TableRowNodeBuilder.prototype.TDH = function(header) {
return this.add(header==null?null:new TableColumnNodeBuilder().text(header));
}

TableRowNodeBuilder.prototype.TableRowNodeBuilder_TDHW =
TableRowNodeBuilder.prototype.TDHW = function(header,width) {
return this.add(header==null?null:new TableColumnNodeBuilder().width(width).text(header));
}

TableRowNodeBuilder.prototype.TableRowNodeBuilder_TDN =
TableRowNodeBuilder.prototype.TDN = function(b) {
return this.add(new TableColumnNodeBuilder().add(b));
}


function TableInnerNodeBuilder(tag){this.TableInnerNodeBuilder_initInstanceFields();this.TableInnerNodeBuilder_constructor(tag);}

ScriptJava.extend(TableInnerNodeBuilder, DOMBuilder);
ScriptJava.extend(TableInnerNodeBuilder.prototype, DOMBuilder.prototype);
TableInnerNodeBuilder.prototype.TableInnerNodeBuilder_initInstanceFields = function() {
this.DOMBuilder_initInstanceFields();
}
TableInnerNodeBuilder.prototype.TableInnerNodeBuilder_constructor = function(tag) {
this.DOMBuilder_constructor(tag);
;
}


function TableNodeBuilder(){this.TableNodeBuilder_initInstanceFields();this.TableNodeBuilder_constructor();}

ScriptJava.extend(TableNodeBuilder, DOMBuilder);
ScriptJava.extend(TableNodeBuilder.prototype, DOMBuilder.prototype);
TableNodeBuilder.prototype.TableNodeBuilder_initInstanceFields = function() {
this.DOMBuilder_initInstanceFields();
}
TableNodeBuilder.prototype.TableNodeBuilder_constructor = function() {
this.DOMBuilder_constructor('table');
;
}

TableNodeBuilder.prototype.TableNodeBuilder_cellPaddingSpacing0 =
TableNodeBuilder.prototype.cellPaddingSpacing0 = function() {
return this.cellPadding(0).cellSpacing(0);
}

TableNodeBuilder.prototype.TableNodeBuilder_cellSpacing =
TableNodeBuilder.prototype.cellSpacing = function(i) {
this.node.cellSpacing=i;
return this;
}

TableNodeBuilder.prototype.TableNodeBuilder_cellPadding =
TableNodeBuilder.prototype.cellPadding = function(i) {
this.node.cellPadding=i;
return this;
}


function SelectOptionNodeBuilder(){this.SelectOptionNodeBuilder_initInstanceFields();this.SelectOptionNodeBuilder_constructor();}

ScriptJava.extend(SelectOptionNodeBuilder, DOMBuilder);
ScriptJava.extend(SelectOptionNodeBuilder.prototype, DOMBuilder.prototype);
SelectOptionNodeBuilder.prototype.SelectOptionNodeBuilder_initInstanceFields = function() {
this.DOMBuilder_initInstanceFields();
}
SelectOptionNodeBuilder.prototype.SelectOptionNodeBuilder_constructor = function() {
this.DOMBuilder_constructor('option');
;
}


function SelectNodeBuilder(){this.SelectNodeBuilder_initInstanceFields();this.SelectNodeBuilder_constructor();}

ScriptJava.extend(SelectNodeBuilder, DOMBuilder);
ScriptJava.extend(SelectNodeBuilder.prototype, DOMBuilder.prototype);
SelectNodeBuilder.prototype.SelectNodeBuilder_initInstanceFields = function() {
this.DOMBuilder_initInstanceFields();
}
SelectNodeBuilder.prototype.SelectNodeBuilder_constructor = function() {
this.DOMBuilder_constructor('select');
;
}

SelectNodeBuilder.prototype.SelectNodeBuilder_setLastSelected =
SelectNodeBuilder.prototype.setLastSelected = function(yes) {
if(yes)this.node.selectedIndex=this.node.childNodes.length-1;
return this;
}

SelectNodeBuilder.prototype.SelectNodeBuilder_option =
SelectNodeBuilder.prototype.option = function(value,title) {
return this.add(new SelectOptionNodeBuilder().value(value).text(title));
}


function NoChildNodeBuilder(tag){this.NoChildNodeBuilder_initInstanceFields();this.NoChildNodeBuilder_constructor(tag);}

ScriptJava.extend(NoChildNodeBuilder, DOMBuilder);
ScriptJava.extend(NoChildNodeBuilder.prototype, DOMBuilder.prototype);
NoChildNodeBuilder.prototype.NoChildNodeBuilder_initInstanceFields = function() {
this.DOMBuilder_initInstanceFields();
}
NoChildNodeBuilder.prototype.NoChildNodeBuilder_constructor = function(tag) {
this.DOMBuilder_constructor(tag);
;
}


function VoidDelegate() {this.VoidDelegate_initInstanceFields();}
VoidDelegate.prototype.VoidDelegate_constructor = function(){}

VoidDelegate.prototype.VoidDelegate_initInstanceFields = function() {
}

function DOMEventCallback() {this.DOMEventCallback_initInstanceFields();}
DOMEventCallback.prototype.DOMEventCallback_constructor = function(){}

DOMEventCallback.prototype.DOMEventCallback_initInstanceFields = function() {
}

function CommonElements() {this.CommonElements_initInstanceFields();}
CommonElements.prototype.CommonElements_constructor = function(){}

ScriptJava.extend(CommonElements, ScriptJava);
ScriptJava.extend(CommonElements.prototype, ScriptJava.prototype);
CommonElements.doNothing = {
delegate: function(n) {
ScriptJava.stopEvent();
return false;
}

};
CommonElements.prototype.CommonElements_initInstanceFields = function() {
this.ScriptJava_initInstanceFields();
var self = this;
}
CommonElements.$A = function(href) {
return new NodeBuilder('a').href(href);
}

CommonElements.$DIV = function() {
return new NodeBuilder('div');
}

CommonElements.$FIELDSET = function(legend) {
var fs = new NodeBuilder('fieldset');
if(null!=legend)fs.add(new NodeBuilder('legend').text(legend));
return fs;
}

CommonElements.$FORM = function(action) {
return new NodeBuilder('form').action(action);
}

CommonElements.$SPAN = function() {
return new NodeBuilder('span');
}

CommonElements.$LABEL = function(title,nb) {
var l = new NodeBuilder('label').append(title);
l.node.htmlFor=nb.node.id;
if(null==nb.node.id)nb.node.id=nb.node.name;
return [l,nb];
}

CommonElements.$TR = function() {
return new TableRowNodeBuilder();
}

CommonElements.$TD = function() {
return new TableColumnNodeBuilder();
}

CommonElements.$TABLE = function() {
return new TableNodeBuilder();
}

CommonElements.$TBODY = function() {
return new TableInnerNodeBuilder('tbody');
}

CommonElements.$THEAD = function() {
return new TableInnerNodeBuilder('thead');
}

CommonElements.$SELECT = function() {
return new SelectNodeBuilder();
}

CommonElements.$B = function(text) {
return new NodeBuilder('b').text(text);
}

CommonElements.$HR = function() {
return new NoChildNodeBuilder('hr');
}

CommonElements.$I = function() {
return new NodeBuilder('i');
}

CommonElements.$P = function() {
return new NodeBuilder('p');
}

CommonElements.$OPTION = function(val,text) {
return new SelectOptionNodeBuilder().value(val).text(text);
}

CommonElements.$IMG = function(src) {
return new NoChildNodeBuilder('img').src(src);
}

CommonElements.$BTN = function(title,cb) {
return new NodeBuilder('button').text(title).onClick(cb);
}

CommonElements.$INPUT = function() {
return new NoChildNodeBuilder('input');
}

CommonElements.$HIDDEN = function(name,value) {
return new NoChildNodeBuilder('input').type('hidden').name(name).value(value);
}

CommonElements.$TEXTBOX = function(name) {
return new NoChildNodeBuilder('input').className('text').type('text').name(name);
}

CommonElements.$TEXTAREA = function(name) {
return new NoChildNodeBuilder('textarea').className('text').name(name);
}


function ValidatorHelperBase() {this.ValidatorHelperBase_initInstanceFields();}
ValidatorHelperBase.prototype.ValidatorHelperBase_constructor = function(){}

ScriptJava.extend(ValidatorHelperBase, CommonElements);
ScriptJava.extend(ValidatorHelperBase.prototype, CommonElements.prototype);
ValidatorHelperBase.msgClassName = 'invalideFieldMessage';
ValidatorHelperBase.prototype.ValidatorHelperBase_initInstanceFields = function() {
this.CommonElements_initInstanceFields();
}
ValidatorHelperBase.prototype.ValidatorHelperBase_validate =
ValidatorHelperBase.prototype.validate = function(root) {
var self1 = this;

try {
root.forEachSubchild({
delegate: function(n) {
if(n.field==DOMBuilder.DISABLED)return false;
if(n.className==ValidatorHelperBase.msgClassName)n.parentNode.removeChild(n);
 else if(null!=n.validator&&!n.validator.isValid(n)) {
self1.showInvalidMessage(n,n.validator.getMessage());
throw new RuntimeException();
}
return true;
}

});
return true;
}
catch(e) {
return false;
}
}

ValidatorHelperBase.prototype.ValidatorHelperBase_showInvalidMessage =
ValidatorHelperBase.prototype.showInvalidMessage = function(n,message) {
var self1 = this;

n.parentNode.insertBefore(CommonElements.$DIV().className(ValidatorHelperBase.msgClassName).text(message).node,n);
n.focus();
ScriptJava.setTimeout({
voidDelegate: function(arg) {
var top = 0;
for(var q = n;
q!=null&&q.offsetTop>0;q=q.offsetParent)top+=q.offsetTop;
if(top-ScriptJava.document.body.scrollTop<50) {
ScriptJava.window.scrollBy(0,-100);
}
}

},100);
}


function AjaxException() {
}

function AjaxRequestResult() {
}

function Log() {this.Log_initInstanceFields();}
Log.prototype.Log_constructor = function(){}

ScriptJava.extend(Log, CommonElements);
ScriptJava.extend(Log.prototype, CommonElements.prototype);
Log.logDivId = 'log-output';
Log.prototype.Log_initInstanceFields = function() {
this.CommonElements_initInstanceFields();
}
Log.info = function(msg) {
if(ScriptJava.console!=null)ScriptJava.console.info(msg);
var ld = ScriptJava.$(Log.logDivId);
if(ld==null)return null;
ld.add(CommonElements.$DIV().className('info').innerHTML(msg));
}

Log.error = function(msg) {
if(ScriptJava.console!=null)ScriptJava.console.error(msg);
var ld = ScriptJava.$(Log.logDivId);
if(ld==null)return null;
ld.add(CommonElements.$DIV().className('error').innerHTML(msg));
}

Log.warn = function(msg) {
if(ScriptJava.console!=null)ScriptJava.console.warn(msg);
var ld = ScriptJava.$(Log.logDivId);
if(ld==null)return null;
ld.add(CommonElements.$DIV().className('warn').innerHTML(msg));
}

Log.debug = function(msg) {
if(ScriptJava.console!=null)ScriptJava.console.debug(msg);
var ld = ScriptJava.$(Log.logDivId);
if(ld==null)return null;
ld.add(CommonElements.$DIV().className('debug').innerHTML(msg));
}

Log.makeLogDiv = function() {
ScriptJava.document.write('<div id=\''+Log.logDivId+'\' style=\'border: 1px solid green; margin-top: 100px; height: 100px; overflow: auto;\'/>');
}


function AjaxUrlFormer() {this.AjaxUrlFormer_initInstanceFields();}
AjaxUrlFormer.prototype.AjaxUrlFormer_constructor = function(){}

AjaxUrlFormer.prototype.AjaxUrlFormer_initInstanceFields = function() {
}

function Ajax() {this.Ajax_initInstanceFields();}
Ajax.prototype.Ajax_constructor = function(){}

Ajax.MICROSOFT_XMLHTTP = 'Microsoft.XMLHTTP';
Ajax.EV_ONREADYSTATECHANGE = 'onreadystatechange';
Ajax.XML_DATA_PARAM = 'xml-data';
Ajax.defaultUrlFormer = {
getUrl: function(clazz,method) {
return '/ajax';
}

,
getQueryString: function(clazz,method) {
return '_rnd='+Math.random()+'&bean='+clazz+'&action='+method;
}

};
Ajax.baseErrorHandler = {
voidDelegate: function(exc) {
if(exc==null)return null;
Log.error(exc.message);
if(exc.stackTrace!=null){var _list18=exc.stackTrace;for(var _i17=0;_i17<_list18.length;_i17++) {var ste=_list18[_i17]; {
Log.error(' at '+ste.className+'.'+ste.methodName+' : '+ste.lineNumber);
}
}}
if(exc.cause!=null) {
Log.error('Caused by:');
this.voidDelegate(exc.cause);
}
}

};
Ajax.defaultErrorHandler = Ajax.baseErrorHandler;
Ajax.prototype.Ajax_initInstanceFields = function() {
var self = this;
}
Ajax.getHttpRequest = function() {
if(ScriptJava.window.XMLHttpRequest!=null) {
return new XMLHttpRequest();
}
return new ActiveXObject(Ajax.MICROSOFT_XMLHTTP);
}

Ajax.syncCall = function(clazz,method,args,callBack,errorHandler) {
var self1 = this;

Ajax.defaultErrorHandler.voidDelegate(null);
var async = callBack!=null;
Log.info('<b>DOING ASYNC CALL</b>');
var request = Ajax.getHttpRequest();
var url = Ajax.defaultUrlFormer.getUrl(clazz,method);
Log.debug('<br><b>Request:</b> '+url);
ScriptJava.window.alert('OK');
var postXml = args==null?null:(Ajax.objectToXml(args,null));
var eventTargetNodes = [];
Ajax.eventTargetDisable(eventTargetNodes);
if(async) {
ScriptJava.setVoidEventCallback(request,Ajax.EV_ONREADYSTATECHANGE,{
voidDelegate: function(r) {
if(r.readyState==4)callBack.voidDelegate(Ajax.parseRequestResult(r,url,method,eventTargetNodes,errorHandler));
}

});
}
if(null==postXml) {
request.open('GET',url,async,null,null);
request.send(null);
}
 else  {
postXml=Ajax.defaultUrlFormer.getQueryString(clazz,method)+'&'+Ajax.XML_DATA_PARAM+'='+postXml.replace(new RegExp('%', 'g'), '%25').replace(new RegExp('&', 'g'), '%26').replace(new RegExp(';', 'g'), '%3B').replace(new RegExp('\\+', 'g'), '%2B');
Log.debug(postXml.replace(new RegExp('<', 'g'), '&lt;').replace(new RegExp('>', 'g'), '&gt;'));
request.open('POST',url,async,null,null);
request.setRequestHeader('Content-type','application/x-www-form-urlencoded');
request.send(postXml);
}
return async?null:Ajax.parseRequestResult(request,url,method,eventTargetNodes,errorHandler);
}

Ajax.parseRequestResult = function(request,url,method,eventTargetNodes,errorHandler) {
var status = request.status;
var responseText = request.responseText;
var statusText = request.statusText;
request=null;
var result = null;
if(status==200) {
Log.debug('<br><b>results:</b>');
Log.debug(responseText);
try {
if(responseText.charAt(0)=='v') {
result=ScriptJava.evalFunction(responseText+'\nreturn o._a;');
}
 else  {
result=ScriptJava.evalFunction('return '+responseText);
}
}
catch(e) {
Ajax.throwError('Ajax.syncCall('+method+'): eval error',e,eventTargetNodes,errorHandler);
}
}
 else  {
Ajax.throwError('Ajax.syncCall('+method+'): http error:\n'+'URL: '+url+'\nCode: '+status+'\nMessage: '+statusText,null,eventTargetNodes,errorHandler);
}
if(result==null) {
Ajax.throwError('Ajax.syncCall('+method+'): result is null: '+responseText,null,eventTargetNodes,errorHandler);
}
var error = result.error;
if(error!=null) {
Ajax.throwError('Ajax.syncCall('+method+'): server-side exception',error,eventTargetNodes,errorHandler);
}
Ajax.eventTargetEnable(eventTargetNodes);
return result.data;
}

Ajax.eventTargetDisable = function(eventTargetNodes) {
var ev = ScriptJava.globalEvent;
var el = ScriptJava.swTarget;
if(el==null) {
if(null==ev)return null;
el=ev.target;
}
if(el==null)el=ev.srcElement;
if(el==null)el=ev.fromElement;
if(el==null||null==el.tagName||el.parentNode==null)return null;
eventTargetNodes[0]=el;
eventTargetNodes[1]=el.parentNode;
eventTargetNodes[2]=(null!=Ajax.DEFAULT_LOADING_IMG?ScriptJava.EL('span').add(ScriptJava.EL('img').src(Ajax.DEFAULT_LOADING_IMG)):ScriptJava.EL('span').text('loading...')).node;
eventTargetNodes[1].insertBefore(eventTargetNodes[2],eventTargetNodes[0]);
eventTargetNodes[1].removeChild(eventTargetNodes[0]);
}

Ajax.eventTargetEnable = function(eventTargetNodes) {
if(null==eventTargetNodes||eventTargetNodes.length!=3)return null;
eventTargetNodes[1].insertBefore(eventTargetNodes[0],eventTargetNodes[2]);
eventTargetNodes[1].removeChild(eventTargetNodes[2]);
eventTargetNodes[0]=eventTargetNodes[1]=eventTargetNodes[2]=null;
}

Ajax.throwError = function(shortMsg,th,eventTargetNodes,errorHandler) {
Ajax.eventTargetEnable(eventTargetNodes);
if(th==null)th=new JsException(shortMsg);
if(errorHandler!=null) {
errorHandler.voidDelegate(th);
}
 else if(Ajax.defaultErrorHandler!=null) {
Ajax.defaultErrorHandler.voidDelegate(th);
}
Ajax.nativeJsThrow(th);
}

Ajax.nativeJsThrow = function(th){throw th;}
Ajax.objectToXml = function(obj,_id) {
var id = (_id!=null?' id="'+_id+'"':'');
if(ScriptJava.typeOf(obj)=='undefined')return '<null'+id+'/>';
if(ScriptJava.typeOf(obj)=='string')return '<s'+id+'>'+ScriptJava.toHTML(obj)+'</s>';
if(ScriptJava.typeOf(obj)=='boolean')return (obj)?'<b'+id+'>1</b>':'<b'+id+'>0</b>';
if(ScriptJava.typeOf(obj)=='number')return '<n'+id+'>'+obj+'</n>';
if(ScriptJava.typeOf(obj)=='object') {
if(obj==null)return '<null'+id+'/>';
if(ScriptJava.isEnum(obj))return '<e'+id+'>'+obj.toString()+'</e>';
if(ScriptJava.isInstanceOfDate(obj))return '<d'+id+'>'+ScriptJava.dateToStringSmart(obj)+'</d>';
if(ScriptJava.isInstanceOfArray(obj))return Ajax.arrayToXml(obj,id);
if(ScriptJava.isInstanceOfNode(obj)) {
return (_id!=null?'<form'+id+'>':'')+Ajax.formToXml(obj)+(_id!=null?'</form>':'');
}
var map = obj;
var xml = '<o'+id+'>';
for(var key in map) {
var val = map[key];
if(ScriptJava.typeOf(val)!='function')xml+=Ajax.objectToXml(val,key);
}
return xml+'</o>';
}
return '<'+ScriptJava.typeOf(obj)+'/>';
}

Ajax.formToXml = function(start) {
var self1 = this;

var xml = '';
if(start.field==DOMBuilder.DISABLED)return xml;
if(start.field!=null) {
if(ScriptJava.typeOf(start.field)=='string') {
xml='<f id="'+start.field+'">';
}
 else xml='<f>';
}
{var _list21=start.childNodes;for(var _i20=0;_i20<_list21.length;_i20++) {var el=_list21[_i20]; {
if((el.id!=null||el.name!=null)&&(el.tagName=='INPUT'||el.tagName=='SELECT'||el.tagName=='TEXTAREA')) {
if(el.type=='radio'&&!el.checked)continue;
xml+='<f id="'+(el.id!=''?el.id:el.name)+'">';
if(el.type=='checkbox')xml+=el.checked?'1':'0';
 else xml+=ScriptJava.toHTML(el.value);
xml+='</f>';
}
 else if(el.className=='field.multiselect') {
var val = [];
NodeBuilder.wrap(el).forEachSubchild({
delegate: function(n) {
if(n.checked)val.push((n.id!=null?n.id:n.name));
return true;
}

});
xml+='<ms id="'+(el.id!=null?el.id:el.name)+'">'+(val.length>0?'<q>':'')+ScriptJava.listJoin(val,'</q><q>')+(val.length>0?'</q>':'')+'</ms>';
}
 else xml+=Ajax.formToXml(el);
}
}}
if(start.field!=null)xml+='</f>';
return xml;
}

Ajax.arrayToXml = function(a,id) {
var xml = '<a'+id+'>';
for(var i = 0;
i<a.length;i++) {
if(ScriptJava.typeOf(a[i])!='function')xml+=Ajax.objectToXml(a[i],null);
}
return xml+'</a>';
}

Ajax.prepareCompiler = function(compiler){}

function SampleHelperBean() {}
SampleHelperBean.prototype.getServerDate = function (params, callback, eh) {
	return Ajax.syncCall('sampleHelperBean', 'getServerDate', params, callback, eh);
}
SampleHelperBean.prototype.getHelloWorldString = function (params, callback, eh) {
	return Ajax.syncCall('sampleHelperBean', 'getHelloWorldString', params, callback, eh);
}
SampleHelperBean.prototype.getSystemProperties = function (params, callback, eh) {
	return Ajax.syncCall('sampleHelperBean', 'getSystemProperties', params, callback, eh);
}
SampleHelperBean.prototype.getObject = function (params, callback, eh) {
	return Ajax.syncCall('sampleHelperBean', 'getObject', params, callback, eh);
}
SampleHelperBean.prototype.saveObject = function (params, callback, eh) {
	return Ajax.syncCall('sampleHelperBean', 'saveObject', params, callback, eh);
}

function SampleUiForm() {this.SampleUiForm_initInstanceFields();}
SampleUiForm.prototype.SampleUiForm_constructor = function(){}

ScriptJava.extend(SampleUiForm, CommonElements);
ScriptJava.extend(SampleUiForm.prototype, CommonElements.prototype);
SampleUiForm.prototype.SampleUiForm_initInstanceFields = function() {
this.CommonElements_initInstanceFields();
this.helper = new SampleHelperBean();
}
SampleUiForm.prototype.SampleUiForm_drawForm =
SampleUiForm.prototype.drawForm = function() {
var div = CommonElements.$DIV();
div.add(this.drawServerTimeButton());
var o = this.helper.getObject([123]);
div.add(this.drawObjectForm(o));
this.drawServerProperties(div);
return div;
}

SampleUiForm.prototype.SampleUiForm_drawObjectForm =
SampleUiForm.prototype.drawObjectForm = function(o) {
var self1 = this;

var form = CommonElements.$DIV().field('Object').text('Object ID: '+o.id).add(CommonElements.$HIDDEN("id",o.id)).BR().text('Object name: ').add(CommonElements.$INPUT().name("name").value(o.name)).BR().text('Object email: ').add(CommonElements.$INPUT().name("email").value(o.email).validator({
isValid: function(n) {
return n.value.toString().indexOf('@')>0;
}

,
getMessage: function() {
return 'Realy bad email =)';
}

}));
form.add(CommonElements.$BTN('Save',{
delegate: function(n) {
if(!new ValidatorHelperBase().validate(form))return false;
ScriptJava.window.alert(self1.helper.saveObject([form.node]));
return false;
}

}));
return form;
}

SampleUiForm.prototype.SampleUiForm_drawServerProperties =
SampleUiForm.prototype.drawServerProperties = function(div) {
var prop = this.helper.getSystemProperties([]);
for(var key in prop) {
div.textB(key+'').text(' :: '+prop[key]).BR();
}
}

SampleUiForm.prototype.SampleUiForm_drawServerTimeButton =
SampleUiForm.prototype.drawServerTimeButton = function() {
var self1 = this;

return CommonElements.$BTN('Show server time&date',{
delegate: function(n) {
var self2 = this;

self1.helper.getServerDate([], {
voidDelegate: function(date) {
ScriptJava.window.alert(ScriptJava.dateToStringSmart(date));
}

});
return false;
}

});
}

SampleUiForm.initAjax = function() {
var self1 = this;

Ajax.DEFAULT_LOADING_IMG='http://mbo.market.yandex.ru/js/yui/assets/skins/sam/wait.gif';
Ajax.defaultErrorHandler={
voidDelegate: function(exc) {
if(exc==null)return null;
ScriptJava.window.alert('Error: '+exc.message);
}

};
}


function SomeModel() {this.SomeModel_initInstanceFields();}
SomeModel.prototype.SomeModel_constructor = function(){}

SomeModel.prototype.SomeModel_initInstanceFields = function() {
}

 {
ScriptJava.window=ScriptJava.getWindow();
ScriptJava.document=ScriptJava.getDocument();
ScriptJava.console=ScriptJava.getConsole();
}
 {
SampleUiForm.initAjax();
NodeBuilder.wrap(ScriptJava.document.body).removeChilds().add(new SampleUiForm().drawForm());
}
