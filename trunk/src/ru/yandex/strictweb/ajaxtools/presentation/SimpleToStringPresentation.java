package ru.yandex.strictweb.ajaxtools.presentation;

import java.io.IOException;

public class SimpleToStringPresentation extends AbstractPresentation {
	@Override
	public void present(Appendable out, String rootKey, Object o) throws Exception {
		if(o!=null) out.append(o.toString());
	}

	@Override
	boolean hashBegin(String key, Object x) throws IOException {
		return false;
	}

	@Override
	void hashEnd(String key, Object x) throws IOException {
	}

	@Override
	boolean listBegin(String key, Object x) throws IOException {
		return false;
	}

	@Override
	void listEnd(String key) throws IOException {
	}

	@Override
	void addSeparator() throws IOException {
	}

	@Override
	void presentString(String key, String val, boolean forceItem) throws IOException {
	}

	@Override
	void presentNull(String key, boolean forceItem) throws IOException {
	}

	@Override
	void presentNumber(String key, String val, boolean forceItem) throws IOException {
	}
}
