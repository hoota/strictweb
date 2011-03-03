package ru.yandex.strictweb.scriptjava.base.util;

import ru.yandex.strictweb.scriptjava.base.Native;

@Native
public abstract class Console {
	abstract public void debug(Object message);
	abstract public void info(Object message);
	abstract public void log(Object message);
	abstract public void warn(Object message);
	abstract public void error(Object message);
	abstract public void trace();
}
