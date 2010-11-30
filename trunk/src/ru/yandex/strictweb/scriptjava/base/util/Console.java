package ru.yandex.strictweb.scriptjava.base.util;

import ru.yandex.strictweb.scriptjava.base.Native;

@Native
public abstract class Console {
	abstract public void debug(String message);
	abstract public void info(String message);
	abstract public void log(String message);
	abstract public void warn(String message);
	abstract public void error(String message);
	abstract public void trace();
}
