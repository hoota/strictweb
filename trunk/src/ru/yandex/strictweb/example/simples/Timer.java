package ru.yandex.strictweb.example.simples;

import java.util.Date;

import ru.yandex.strictweb.scriptjava.base.CommonElements;
import ru.yandex.strictweb.scriptjava.base.NodeBuilder;
import ru.yandex.strictweb.scriptjava.base.VoidDelegate;
import ru.yandex.strictweb.scriptjava.base.util.IntervalIdentifier;

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
