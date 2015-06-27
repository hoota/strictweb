## StrictWeb ##
Java to JavaScript compiler and AJAX framework, based on java servlets.

## Simple examples ##
  * Hello World
```
import ru.yandex.strictweb.scriptjava.base.StrictWeb;

public class HelloWorld {
    static {
        StrictWeb.window.alert("Hello, World!");
    }
}
```

And the result will be
```
// ... here will be some code from StrictWeb
{
StrictWeb.window.alert('Hello, World!');
}
```

[More examples](Examples.md)

[Немного описания на русском](RussianDoc.md)