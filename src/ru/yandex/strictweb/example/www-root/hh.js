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
ScriptJava.textNode = function(string){return document.createTextNode(string);}
ScriptJava.EL = function($0) {
return new NodeBuilder($0);
}

ScriptJava.$CHECKBOX = function($1, $2) {
return ScriptJava.EL('input').className('cb').type('checkbox').name($1).checked($2);
}

ScriptJava.$RADIO = function($1, $2) {
return ScriptJava.EL('input').className('cb').type('radio').name($1).checked($2);
}

ScriptJava.$CHECKBOXcustom = function(name, checked, text, checkedUrl, uncheckedUrl){var nb = ScriptJava.EL('b');nb.node = $CHECKBOX(name, checked, text, checkedUrl, uncheckedUrl);return nb;}
ScriptJava.extend = function(dest, source){for(var k in source) if(k!='prototype')dest[k] = source[k];return dest;}
ScriptJava.stopEvent = function(){e = ScriptJava.globalEvent;if(!e) return;e.stopPropagation?e.stopPropagation():e.cancelBubble=true;return e;}
ScriptJava.setDOMEventCallback = function(obj, name, cb){if(cb==null){obj[name]=null;return;};obj[name] = typeof cb=='function'?cb:function(ev, nullNode) {ScriptJava.swTarget=null;ScriptJava.globalEvent=ev||window.event;if(cb&&cb.delegate)return cb.delegate(nullNode ? null : this);return false;}}
ScriptJava.setVoidEventCallback = function(obj, name, cb){if(cb==null){obj[name]=null;return;};obj[name] = typeof cb=='function'?cb:function(ev) {if(cb&&cb.voidDelegate) cb.voidDelegate(this);return false;}}
ScriptJava.removeEventListener = function(obj, event, func, useCapture){obj.removeEventListener(event, func, useCapture);}
ScriptJava.addEventListener = function(obj, event, cb, useCapture){if(cb==null)return null;var f = function(ev, nullNode) {ScriptJava.swTarget=null;ScriptJava.globalEvent=ev||window.event;return cb.delegate(nullNode ? null : this);};obj.addEventListener(event, f, useCapture); return f;}
ScriptJava.onLoadWindow = function($3) {
ScriptJava.setDOMEventCallback(ScriptJava.window, 'onload', $3);
}

ScriptJava.regExp = function(base, flags){return new RegExp(base, flags);}
ScriptJava.compareDates = function($4, $5) {
var $6 = $4.getFullYear();
var $7 = $5.getFullYear();
if(($6<$7))return -1;
if(($6>$7))return 1;
$6=$4.getMonth();
$7=$5.getMonth();
if(($6<$7))return -1;
if(($6>$7))return 1;
$6=$4.getDate();
$7=$5.getDate();
if(($6<$7))return -1;
if(($6>$7))return 1;
return 0;
}

ScriptJava.compareTimes = function(d1, d2){if(d1<d2)return -1;if(d1>d2)return 1;return 0;}
ScriptJava.compareStrings = function(str1, str2){if(str1<str2)return -1;if(str1>str2)return 1;return 0;}
ScriptJava.compareNumbers = function(n1, n2){if(n1<n2)return -1;if(n1>n2)return 1;return 0;}
ScriptJava.jsSortList = function(list, comparator){return list.sort(comparator.compare);}
ScriptJava.isNaN = function(o){return isNaN(o);}
ScriptJava.arrayRemove = function($8, $9) {
var $a = [];
{var _list1=$8;for(var _i0=0;_i0<_list1.length;_i0++) {var $b=_list1[_i0];if(($b!=$9))$a.push($b);
}}
return $a;
}

ScriptJava.getCookie = function($1) {
var $c = ScriptJava.document.cookie;
var $d = $1+'=';
var $e = $c.indexOf('; '+$d);
if(($e==-1)) {
$e=$c.indexOf($d);
if(($e!=0))return null;
}
 else  {
$e+=2;
}
var $f = $c.indexOf(';', $e);
if(($f==-1)) {
$f=$c.length;
}
return ScriptJava.window.unescape($c.substring($e+$d.length, $f));
}

ScriptJava.setCookie = function($1, $g) {
ScriptJava.setCookieFull($1, $g, null, null, null, false);
}

ScriptJava.setCookieFull = function($1, $g, $h, $i, $j, $k) {
if((null==$h)) {
$h=new Date();
$h.setFullYear($h.getFullYear()+10);
}
ScriptJava.document.cookie=$1+'='+ScriptJava.window.escape($g)+('; expires='+$h.toGMTString())+($i!=null?'; path='+$i:'')+($j!=null?'; domain='+$j:'')+($k?'; secure':'');
}

ScriptJava.setInterval = function(cb, millis){var iid=setInterval(function(){cb.voidDelegate(iid);}, millis);return iid;};
ScriptJava.setTimeout = function(cb, millis){var iid=setTimeout(function(){cb.voidDelegate(iid);}, millis);return iid;};
ScriptJava.clearInterval = function(intervalId){clearInterval(intervalId);}
ScriptJava.clearTimeout = function(timeoutId){clearInterval(timeoutId);}
ScriptJava.typeOf = function(obj){return typeof obj;}
ScriptJava.isEnum = function(obj){return obj._isEnum;}
ScriptJava.isInstanceOfDate = function(obj){return obj instanceof Date;}
ScriptJava.isInstanceOfArray = function(obj){return obj instanceof Date || (typeof obj.length != 'undefined' && typeof obj['0'] != 'undefined');}
ScriptJava.isInstanceOfNode = function(obj){return obj.tagName;}
ScriptJava.dateToString = function($l) {
if((null==$l))return '';
return ($l.getDate()<10?'0':'')+$l.getDate()+'.'+($l.getMonth()<9?'0':'')+($l.getMonth()+1)+'.'+$l.getFullYear();
}

ScriptJava.dateTimeToString = function($l) {
if((null==$l))return '';
return ScriptJava.dateToString($l)+' '+($l.getHours()<10?'0':'')+$l.getHours()+':'+($l.getMinutes()<10?'0':'')+$l.getMinutes();
}

ScriptJava.dateToStringSmart = function($l) {
if((null==$l))return '';
return $l.getHours()==0&&$l.getMinutes()==0?ScriptJava.dateToString($l):ScriptJava.dateTimeToString($l);
}

ScriptJava.listJoin = function(list, sep){return list.join(sep);}
ScriptJava.scrollToVisible = function($m) {
var $n = 0;
for(;$m!=null;$m=$m.offsetParent) {
$n+=$m.offsetTop;
}
ScriptJava.window.scroll(0, $n);
}

ScriptJava.newJsMap = function(){return {};}
ScriptJava.newJsSet = function(){return {};}
ScriptJava.newJsList = function(){return [];}

function JsException($o){this.JsException_initInstanceFields();this.JsException_constructor($o);}

JsException.prototype.JsException_initInstanceFields = function() {
}
JsException.prototype.JsException_constructor = function($o) {
this.message=$o;
}

JsException.prototype.JsException_toString =
JsException.prototype.toString = function() {
return this.message;
}


function InputValidator() {this.InputValidator_initInstanceFields();}
InputValidator.prototype.InputValidator_constructor = function(){}

InputValidator.prototype.InputValidator_initInstanceFields = function() {
}

function DOMBuilder($0){this.DOMBuilder_initInstanceFields();this.DOMBuilder_constructor($0);}

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

DOMBuilder.prototype.DOMBuilder_constructor = function($0) {
this.node=ScriptJava.createNode($0);
}

DOMBuilder.prototype.fireEvent = function(eventName, nullNode){this.node[eventName](ScriptJava.globalEvent, nullNode);return this;}
DOMBuilder.prototype.onClick = function($p) {
ScriptJava.setDOMEventCallback(this.node, DOMBuilder.EV_ONCLICK, $p);
return this;
}

DOMBuilder.prototype.onMouseMove = function($q) {
ScriptJava.setDOMEventCallback(this.node, DOMBuilder.EV_ONMOUSEMOVE, $q);
return this;
}

DOMBuilder.prototype.onMouseOut = function($r) {
ScriptJava.setDOMEventCallback(this.node, DOMBuilder.EV_ONMOUSEOUT, $r);
return this;
}

DOMBuilder.prototype.onMouseUp = function($s) {
ScriptJava.setDOMEventCallback(this.node, DOMBuilder.EV_ONMOUSEUP, $s);
return this;
}

DOMBuilder.prototype.onMouseDown = function($t) {
ScriptJava.setDOMEventCallback(this.node, DOMBuilder.EV_ONMOUSEDOWN, $t);
return this;
}

DOMBuilder.prototype.onSubmit = function($p) {
ScriptJava.setDOMEventCallback(this.node, DOMBuilder.EV_ONSUBMIT, $p);
return this;
}

DOMBuilder.prototype.onDblClick = function($p) {
ScriptJava.setDOMEventCallback(this.node, DOMBuilder.EV_ONDBLCLICK, $p);
return this;
}

DOMBuilder.prototype.text = function($u) {
return this.append($u);
}

DOMBuilder.prototype.textB = function($u) {
return this.add(new NodeBuilder('b').text($u));
}

DOMBuilder.prototype.removeChilds = function() {
while((null!=this.node.firstChild)) {
this.node.removeChild(this.node.firstChild);
}
return this;
}

DOMBuilder.prototype.append = function(c){if(null!=c) { var n = this.node; if(!c.nodeName) n.appendChild(document.createTextNode(c)); else {  n.appendChild(c);}} return this;}
DOMBuilder.prototype.className = function($v) {
this.node.className=$v;
return this;
}

DOMBuilder.prototype.DOMBuilder_href =
DOMBuilder.prototype.href = function($w) {
this.node.href=$w;
return this;
}

DOMBuilder.prototype.DOMBuilder_action =
DOMBuilder.prototype.action = function($x) {
this.node.action=$x;
return this;
}

DOMBuilder.prototype.DOMBuilder_appendList =
DOMBuilder.prototype.appendList = function($y) {
{var _list3=$y;for(var _i2=0;_i2<_list3.length;_i2++) {var $z=_list3[_i2];this.append($z);
}}
return this;
}

DOMBuilder.prototype.DOMBuilder_appendAll =
DOMBuilder.prototype.appendAll = function($y) {
{var _list5=$y;for(var _i4=0;_i4<_list5.length;_i4++) {var $z=_list5[_i4];this.append($z);
}}
return this;
}

DOMBuilder.prototype.DOMBuilder_styleDisplay =
DOMBuilder.prototype.styleDisplay = function($l) {
this.node.style.display=$l;
return this;
}

DOMBuilder.prototype.DOMBuilder_add =
DOMBuilder.prototype.add = function($10) {
if((null!=$10))this.append($10.node);
return this;
}

DOMBuilder.prototype.DOMBuilder_addList =
DOMBuilder.prototype.addList = function($y) {
if((null!=$y)){var _list7=$y;for(var _i6=0;_i6<_list7.length;_i6++) {var $10=_list7[_i6];this.append($10.node);
}}
return this;
}

DOMBuilder.prototype.DOMBuilder_addAll =
DOMBuilder.prototype.addAll = function($y) {
if((null!=$y)){var _list9=$y;for(var _i8=0;_i8<_list9.length;_i8++) {var $10=_list9[_i8];this.append($10.node);
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
DOMBuilder.prototype.showHide = function($11) {
this.node.style.display=$11?'':'none';
return this;
}

DOMBuilder.prototype.DOMBuilder_toggle =
DOMBuilder.prototype.toggle = function() {
this.node.style.display=this.node.style.display==''?'none':'';
}

DOMBuilder.prototype.DOMBuilder_type =
DOMBuilder.prototype.type = function($12) {
this.node.type=$12;
return this;
}

DOMBuilder.prototype.DOMBuilder_name =
DOMBuilder.prototype.name = function($1) {
this.node.name=null==$1?'':$1;
return this;
}

DOMBuilder.prototype.DOMBuilder_value =
DOMBuilder.prototype.value = function($g) {
this.node.value=null==$g?'':$g;
return this;
}

DOMBuilder.prototype.DOMBuilder_temp1 =
DOMBuilder.prototype.temp1 = function($u) {
this.node.temp1=$u;
return this;
}

DOMBuilder.prototype.DOMBuilder_temp2 =
DOMBuilder.prototype.temp2 = function($u) {
this.node.temp2=$u;
return this;
}

DOMBuilder.prototype.DOMBuilder_temp3 =
DOMBuilder.prototype.temp3 = function($u) {
this.node.temp3=$u;
return this;
}

DOMBuilder.prototype.DOMBuilder_width =
DOMBuilder.prototype.width = function($13) {
this.node.width=$13;
return this;
}

DOMBuilder.prototype.DOMBuilder_method =
DOMBuilder.prototype.method = function($14) {
this.node.method=$14;
return this;
}

DOMBuilder.prototype.DOMBuilder_enctype =
DOMBuilder.prototype.enctype = function($15) {
this.node.enctype=$15;
return this;
}

DOMBuilder.prototype.DOMBuilder_width100 =
DOMBuilder.prototype.width100 = function() {
this.node.width='100%';
return this;
}

DOMBuilder.prototype.DOMBuilder_onChange =
DOMBuilder.prototype.onChange = function($p) {
ScriptJava.setDOMEventCallback(this.node, DOMBuilder.EV_ONCHANGE, $p);
return this;
}

DOMBuilder.prototype.DOMBuilder_vAlign =
DOMBuilder.prototype.vAlign = function($16) {
this.node.vAlign=$16;
return this;
}

DOMBuilder.prototype.DOMBuilder_src =
DOMBuilder.prototype.src = function($17) {
this.node.src=$17;
return this;
}

DOMBuilder.prototype.DOMBuilder_title =
DOMBuilder.prototype.title = function($u) {
this.node.title=$u;
return this;
}

DOMBuilder.prototype.DOMBuilder_field =
DOMBuilder.prototype.field = function($18) {
this.node.field=$18;
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
DOMBuilder.prototype.validator = function($19) {
this.node.validator=$19;
return this;
}

DOMBuilder.prototype.DOMBuilder_removeFromDom =
DOMBuilder.prototype.removeFromDom = function() {
if((null!=this.node.parentNode))this.node.parentNode.removeChild(this.node);
return this;
}

DOMBuilder.prototype.DOMBuilder_align =
DOMBuilder.prototype.align = function($x) {
this.node.align=$x;
return this;
}

DOMBuilder.prototype.DOMBuilder_innerHTML =
DOMBuilder.prototype.innerHTML = function($1a) {
this.node.innerHTML=$1a;
return this;
}

DOMBuilder.prototype.DOMBuilder_checked =
DOMBuilder.prototype.checked = function($1b) {
this.node.checked=$1b;
return this;
}

DOMBuilder.prototype.DOMBuilder_forEachSubchild =
DOMBuilder.prototype.forEachSubchild = function($3) {
var $1c = [];
$1c.push(this.node);
while(($1c.length>0)) {
var $m = $1c.shift();
if(($3.delegate($m))){var _list11=$m.childNodes;for(var _i10=0;_i10<_list11.length;_i10++) {var $1d=_list11[_i10]; {
if((null!=$1d.tagName))$1c.push($1d);
}
}}
}
}

DOMBuilder.prototype.DOMBuilder_size =
DOMBuilder.prototype.size = function($1e) {
this.node.size=$1e;
return this;
}

DOMBuilder.prototype.DOMBuilder_readOnly =
DOMBuilder.prototype.readOnly = function($1f) {
this.node.readOnly=$1f;
return this;
}

DOMBuilder.prototype.DOMBuilder_valueAsNum =
DOMBuilder.prototype.valueAsNum = function() {
if((null==this.node.value||this.node.value==''))return null;
return 1*this.node.value;
}

DOMBuilder.prototype.DOMBuilder_isChecked =
DOMBuilder.prototype.isChecked = function() {
return this.node.checked;
}

DOMBuilder.prototype.DOMBuilder_onKeyUp =
DOMBuilder.prototype.onKeyUp = function($p) {
ScriptJava.setDOMEventCallback(this.node, DOMBuilder.EV_ONKEYUP, $p);
return this;
}

DOMBuilder.prototype.DOMBuilder_onKeyPress =
DOMBuilder.prototype.onKeyPress = function($p) {
ScriptJava.setDOMEventCallback(this.node, DOMBuilder.EV_ONKEYPRESS, $p);
return this;
}

DOMBuilder.prototype.DOMBuilder_onBlur =
DOMBuilder.prototype.onBlur = function($p) {
ScriptJava.setDOMEventCallback(this.node, DOMBuilder.EV_ONBLUR, $p);
return this;
}

DOMBuilder.prototype.DOMBuilder_onKeyDown =
DOMBuilder.prototype.onKeyDown = function($p) {
ScriptJava.setDOMEventCallback(this.node, DOMBuilder.EV_ONKEYDOWN, $p);
return this;
}

DOMBuilder.prototype.DOMBuilder_valueAsStr =
DOMBuilder.prototype.valueAsStr = function() {
return this.node.value;
}

DOMBuilder.prototype.DOMBuilder_appendTo =
DOMBuilder.prototype.appendTo = function($1g) {
this.removeFromDom();
if((null!=$1g))$1g.append(this.node);
}

DOMBuilder.prototype.DOMBuilder_id =
DOMBuilder.prototype.id = function($1h) {
this.node.id=$1h;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleFontSize =
DOMBuilder.prototype.styleFontSize = function($1i) {
this.node.style.fontSize=$1i;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleFontWeight =
DOMBuilder.prototype.styleFontWeight = function($1i) {
this.node.style.fontWeight=$1i;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleTextDecoration =
DOMBuilder.prototype.styleTextDecoration = function($1j) {
this.node.style.textDecoration=$1j;
return this;
}

DOMBuilder.prototype.DOMBuilder_disabled =
DOMBuilder.prototype.disabled = function($l) {
this.node.disabled=$l;
return this;
}

DOMBuilder.prototype.DOMBuilder_valignTop =
DOMBuilder.prototype.valignTop = function() {
this.node.vAlign='top';
return this;
}

DOMBuilder.prototype.DOMBuilder_stylePaddingLeft =
DOMBuilder.prototype.stylePaddingLeft = function($1k) {
this.node.style.paddingLeft=$1k;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleMargin =
DOMBuilder.prototype.styleMargin = function($1l) {
this.node.style.margin=$1l;
return this;
}

DOMBuilder.prototype.DOMBuilder_stylePadding =
DOMBuilder.prototype.stylePadding = function($1m) {
this.node.style.padding=$1m;
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
DOMBuilder.prototype.styleWidth = function($1n) {
this.node.style.width=$1n;
return this;
}

DOMBuilder.prototype.DOMBuilder_style =
DOMBuilder.prototype.style = function(key, value){this.node.style[key] = value; return this;}
DOMBuilder.prototype.DOMBuilder_styleHeight =
DOMBuilder.prototype.styleHeight = function($1o) {
this.node.style.height=$1o;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleTop =
DOMBuilder.prototype.styleTop = function($u) {
this.node.style.top=$u;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleLeft =
DOMBuilder.prototype.styleLeft = function($1p) {
this.node.style.left=$1p;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleMarginTop =
DOMBuilder.prototype.styleMarginTop = function($1l) {
this.node.style.marginTop=$1l;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleMarginLeft =
DOMBuilder.prototype.styleMarginLeft = function($1l) {
this.node.style.marginLeft=$1l;
return this;
}

DOMBuilder.prototype.DOMBuilder_target =
DOMBuilder.prototype.target = function($u) {
this.node.target=$u;
return this;
}

DOMBuilder.prototype.DOMBuilder_targetBlank =
DOMBuilder.prototype.targetBlank = function() {
this.node.target='_blank';
return this;
}

DOMBuilder.prototype.DOMBuilder_vspace =
DOMBuilder.prototype.vspace = function($1e) {
this.node.vspace=$1e;
return this;
}

DOMBuilder.prototype.DOMBuilder_hspace =
DOMBuilder.prototype.hspace = function($1e) {
this.node.hspace=$1e;
return this;
}

DOMBuilder.prototype.DOMBuilder_border =
DOMBuilder.prototype.border = function($10) {
this.node.border=$10;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleColor =
DOMBuilder.prototype.styleColor = function($1d) {
this.node.style.color=$1d;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleBackground =
DOMBuilder.prototype.styleBackground = function($1q) {
if((null!=$1q))this.node.style.background=$1q;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleBorder =
DOMBuilder.prototype.styleBorder = function($10) {
this.node.style.border=$10;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleBoxShadow =
DOMBuilder.prototype.styleBoxShadow = function($1r) {
this.node.style.boxShadow=$1r;
this.node.style.webkitBoxShadow=$1r;
this.node.style.MozBoxShadow=$1r;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleTransform =
DOMBuilder.prototype.styleTransform = function($1s) {
this.node.style.transform=$1s;
this.node.style.webkitTransform=$1s;
this.node.style.MozTransform=$1s;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleTransformOrigin =
DOMBuilder.prototype.styleTransformOrigin = function($1t) {
this.node.style.transformOrigin=$1t;
this.node.style.webkitTransformOrigin=$1t;
this.node.style.MozTransformOrigin=$1t;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleBorderRadius =
DOMBuilder.prototype.styleBorderRadius = function($1u) {
this.node.style.borderRadius=$1u;
this.node.style.webkitBorderRadius=$1u;
this.node.style.MozBorderRadius=$1u;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleBorderBottom =
DOMBuilder.prototype.styleBorderBottom = function($10) {
this.node.style.borderBottom=$10;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleOverflow =
DOMBuilder.prototype.styleOverflow = function($1v) {
this.node.style.overflow=$1v;
return this;
}

DOMBuilder.prototype.DOMBuilder_styleFloat =
DOMBuilder.prototype.styleFloat = function($1w) {
this.node.style.cssFloat=this.node.style.styleFloat=$1w;
return this;
}

DOMBuilder.prototype.DOMBuilder_isEmpty =
DOMBuilder.prototype.isEmpty = function() {
return this.node.childNodes.length==0;
}

DOMBuilder.prototype.DOMBuilder_insertAfter =
DOMBuilder.prototype.insertAfter = function(newChild, existed){if(existed.node.nextSibling == null) this.add(newChild);else this.node.insertBefore(newChild.node, existed.node.nextSibling);return this;}

function NodeBuilder($1x){this.NodeBuilder_initInstanceFields();this.NodeBuilder_constructor($1x);}

ScriptJava.extend(NodeBuilder, DOMBuilder);
ScriptJava.extend(NodeBuilder.prototype, DOMBuilder.prototype);
NodeBuilder.prototype.NodeBuilder_initInstanceFields = function() {
this.DOMBuilder_initInstanceFields();
}
NodeBuilder.prototype.NodeBuilder_constructor = function($1x) {
this.DOMBuilder_constructor($1x);
;
}

NodeBuilder.wrap = function($1y) {
if(($1y==null))return null;
var $1z = new NodeBuilder(null);
$1z.node=$1y;
return $1z;
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
TableColumnNodeBuilder.prototype.colSpan = function($20) {
this.node.colSpan=$20;
return this;
}

TableColumnNodeBuilder.prototype.TableColumnNodeBuilder_rowSpan =
TableColumnNodeBuilder.prototype.rowSpan = function($21) {
this.node.rowSpan=$21;
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
TableRowNodeBuilder.prototype.TDH = function($22) {
return this.add($22==null?null:new TableColumnNodeBuilder().text($22));
}

TableRowNodeBuilder.prototype.TableRowNodeBuilder_TDHW =
TableRowNodeBuilder.prototype.TDHW = function($22, $13) {
return this.add($22==null?null:new TableColumnNodeBuilder().width($13).text($22));
}

TableRowNodeBuilder.prototype.TableRowNodeBuilder_TDN =
TableRowNodeBuilder.prototype.TDN = function($10) {
return this.add(new TableColumnNodeBuilder().add($10));
}


function TableInnerNodeBuilder($1x){this.TableInnerNodeBuilder_initInstanceFields();this.TableInnerNodeBuilder_constructor($1x);}

ScriptJava.extend(TableInnerNodeBuilder, DOMBuilder);
ScriptJava.extend(TableInnerNodeBuilder.prototype, DOMBuilder.prototype);
TableInnerNodeBuilder.prototype.TableInnerNodeBuilder_initInstanceFields = function() {
this.DOMBuilder_initInstanceFields();
}
TableInnerNodeBuilder.prototype.TableInnerNodeBuilder_constructor = function($1x) {
this.DOMBuilder_constructor($1x);
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
TableNodeBuilder.prototype.cellSpacing = function($23) {
this.node.cellSpacing=$23;
return this;
}

TableNodeBuilder.prototype.TableNodeBuilder_cellPadding =
TableNodeBuilder.prototype.cellPadding = function($23) {
this.node.cellPadding=$23;
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
SelectNodeBuilder.prototype.setLastSelected = function($24) {
if(($24))this.node.selectedIndex=this.node.childNodes.length-1;
return this;
}

SelectNodeBuilder.prototype.SelectNodeBuilder_option =
SelectNodeBuilder.prototype.option = function($g, $25) {
return this.add(new SelectOptionNodeBuilder().value($g).text($25));
}


function NoChildNodeBuilder($1x){this.NoChildNodeBuilder_initInstanceFields();this.NoChildNodeBuilder_constructor($1x);}

ScriptJava.extend(NoChildNodeBuilder, DOMBuilder);
ScriptJava.extend(NoChildNodeBuilder.prototype, DOMBuilder.prototype);
NoChildNodeBuilder.prototype.NoChildNodeBuilder_initInstanceFields = function() {
this.DOMBuilder_initInstanceFields();
}
NoChildNodeBuilder.prototype.NoChildNodeBuilder_constructor = function($1x) {
this.DOMBuilder_constructor($1x);
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
delegate: function($m) {
ScriptJava.stopEvent();
return false;
}

};
CommonElements.prototype.CommonElements_initInstanceFields = function() {
this.ScriptJava_initInstanceFields();
var self = this;
}
CommonElements.$A = function($w) {
return new NodeBuilder('a').href($w);
}

CommonElements.$DIV = function() {
return new NodeBuilder('div');
}

CommonElements.$FIELDSET = function($26) {
var $1i = new NodeBuilder('fieldset');
if((null!=$26))$1i.add(new NodeBuilder('legend').text($26));
return $1i;
}

CommonElements.$FORM = function($27) {
return new NodeBuilder('form').action($27);
}

CommonElements.$SPAN = function() {
return new NodeBuilder('span');
}

CommonElements.$LABEL = function($25, $1z) {
var $1p = new NodeBuilder('label').append($25);
$1p.node.htmlFor=$1z.node.id;
if((null==$1z.node.id))$1z.node.id=$1z.node.name;
return [$1p,$1z];
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

CommonElements.$B = function($28) {
return new NodeBuilder('b').text($28);
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

CommonElements.$OPTION = function($29, $28) {
return new SelectOptionNodeBuilder().value($29).text($28);
}

CommonElements.$IMG = function($17) {
return new NoChildNodeBuilder('img').src($17);
}

CommonElements.$BTN = function($25, $3) {
return new NodeBuilder('button').text($25).onClick($3);
}

CommonElements.$INPUT = function() {
return new NoChildNodeBuilder('input');
}

CommonElements.$HIDDEN = function($1, $g) {
return new NoChildNodeBuilder('input').type('hidden').name($1).value($g);
}

CommonElements.$TEXTBOX = function($1) {
return new NoChildNodeBuilder('input').className('text').type('text').name($1);
}

CommonElements.$TEXTAREA = function($1) {
return new NoChildNodeBuilder('textarea').className('text').name($1);
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
ValidatorHelperBase.prototype.validate = function($2a) {
var self1 = this;

try {
$2a.forEachSubchild({
delegate: function($m) {
if(($m.field==DOMBuilder.DISABLED))return false;
if(($m.className==ValidatorHelperBase.msgClassName))$m.parentNode.removeChild($m);
 else if((null!=$m.validator&&!$m.validator.isValid($m))) {
self1.showInvalidMessage($m, $m.validator.getMessage());
throw new RuntimeException();
}
return true;
}

});
return true;
}
catch($2b) {
return false;
}
}

ValidatorHelperBase.prototype.ValidatorHelperBase_showInvalidMessage =
ValidatorHelperBase.prototype.showInvalidMessage = function($m, $o) {
var self1 = this;

$m.parentNode.insertBefore(CommonElements.$DIV().className(ValidatorHelperBase.msgClassName).text($o).node, $m);
$m.focus();
ScriptJava.setTimeout({
voidDelegate: function($2c) {
var $n = 0;
for(var $2d = $m;
$2d!=null&&$2d.offsetTop>0;$2d=$2d.offsetParent)$n+=$2d.offsetTop;
if(($n-ScriptJava.document.body.scrollTop<50)) {
ScriptJava.window.scrollBy(0, -100);
}
}

}, 100);
}


function HelloWorld() {this.HelloWorld_initInstanceFields();}
HelloWorld.prototype.HelloWorld_constructor = function(){}

ScriptJava.extend(HelloWorld, CommonElements);
ScriptJava.extend(HelloWorld.prototype, CommonElements.prototype);
HelloWorld.prototype.HelloWorld_initInstanceFields = function() {
this.CommonElements_initInstanceFields();
}
HelloWorld.prototype.HelloWorld_drawForm =
HelloWorld.prototype.drawForm = function() {
var self1 = this;

return CommonElements.$DIV().add(CommonElements.$BTN('Нажми меня!', {
delegate: function($m) {
ScriptJava.window.alert('Hello, World!');
return false;
}

}));
}


 {
ScriptJava.window=ScriptJava.getWindow();
ScriptJava.document=ScriptJava.getDocument();
ScriptJava.console=ScriptJava.getConsole();
}
 {
NodeBuilder.wrap(ScriptJava.document.body).removeChilds().add(new HelloWorld().drawForm());
}
