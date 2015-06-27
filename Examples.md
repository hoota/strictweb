## More Simple Examples ##

**DOM Elements builder
```
// extending from CommonElements just for quick access to some methods
public class FirstDiv extends CommonElements {
    static {
        NodeBuilder div = $DIV()
            .text("This method wil add a text-node to div")
            .add(
                $P().text("Adding <p> with text in it")
                .styleColor("red")
                .styleBorder("1px solid black")
            )
            .add(
                $SPAN().innerHTML("Adding <b>html</b> using innerHTML, but this is not good way")
            )
        ;

        document.body.appendChild(EL("h2").text(ajaxName(FirstDiv.class) + " example").node);
        document.body.appendChild(div.node);
    }
}
```**

**Timer
```
/** extends from CommonElements only for simplier access to static fields and methods */
public class Timer extends CommonElements {
    /** this is an entry point */
    static {
        // creating DIV node wrapper
        final NodeBuilder div = $DIV();

        // creating javascript timer with delegate
        setInterval(new VoidDelegate<IntervalIdentifier>() {
            public void voidDelegate(IntervalIdentifier iid) {
                div.removeChilds().text(new Date().toString());
            }
        }, 1000);

        // append node to document.body
        document.body.appendChild(div.node);
    }
}
```**

**Event and call backs examples
```
public class EventExample extends CommonElements implements DOMEventCallback {
    private NoChildNodeBuilder input;

    private NodeBuilder draw() {
        return $DIV()
            // you can use existing instance as callback
            .add(input = $INPUT().onChange(this))
            .BR()
            // or create new anonym class
            .add($BTN("Push me", new DOMEventCallback() {
                public boolean delegate(Node n) {
                    window.alert(input.valueAsStr());
                    return false;
                }
            }))
        ;
    }

    // input onChange
    public boolean delegate(Node n) {
        window.alert("input.onChange: " + n.value);
        return true; // we need it
    }

    static {
        document.body.appendChild(EL("h2").text(ajaxName(EventExample.class) + " example").node);
        document.body.appendChild(new EventExample().draw().node);
    }
}
```**

**Table and Times example
```
public class TableTimerExample extends CommonElements {
    static {
        document.body.appendChild(EL("h2").text(ajaxName(TableTimerExample.class) + " example").node);
        document.body.appendChild(new TableTimerExample().draw().node);
    }

    int n = 0;

    private NodeBuilder draw() {
        final TableInnerNodeBuilder tbody = $TBODY();

        setInterval(new VoidDelegate<IntervalIdentifier>() {
            public void voidDelegate(IntervalIdentifier intervalId) {
                n++;
                tbody.add($TR()
                    .add($TD().text("" + n))
                    .add($TD().text(dateToStringSmart(new Date())))
                );

                if(n >= 100) clearInterval(intervalId);
            }
        }, 1000);

        return $DIV().style("maxHeight", "200px").styleOverflow("auto")
        .add(
            $TABLE().border(1)
            .add(
                $THEAD().add(
                    $TR().TDH("#").TDH("Date & Time")
                )
            ).add(tbody)
        )
        ;
    }
}
```**

### Compiler ###
To compile all these examples, we can use one java class
```
public class SimplesCompiler extends CommonCompiler {	
    @Override
    public void addToCompiler(Compiler compiler) throws Exception {
        compiler
        .parseClass(HelloWorld.class)
        .parseClass(FirstDiv.class)
        .parseClass(Timer.class)
        .parseClass(EventExample.class)
        .parseClass(TableTimerExample.class)
        ;
    }

    public static void main(String[] args) throws Exception {
        new SimplesCompiler()
        .setJsGenPath("src/java/ru/yandex/strictweb/example/www-root/simples.js")
        .setBasePath("src/java/")
        .build(args);
    }
}
```