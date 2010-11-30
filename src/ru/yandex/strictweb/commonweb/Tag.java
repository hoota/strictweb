package ru.yandex.strictweb.commonweb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Tag {
	private static int ver = -1;

//	public static String js(String uri) {
//		File file = new File(ContextListener.rootPath + uri);
//		long lm = file.lastModified();
//
//		return "<script src='"+uri+"?ver="+lm+"'></script>";
//	}
	
	static {
		readVersion();
	}

	private static void readVersion() {
		try {
			FileReader r = new FileReader("WEB-INF/appengine-web.xml");
			char[] buf = new char[10000];
			String all = new String(buf, 0, r.read(buf));
			all = all.replaceAll("\\s+", "");
			Matcher m = Pattern.compile("<version>([0-9]+)</version>").matcher(all);
			if(m.find()) ver = Integer.parseInt(m.group(1));

			r.close();
		} catch (Throwable e) {
			e.printStackTrace();
			ver = (int)(Math.random() * 1000);
		}
	}
	
	public static String js(String uri) {
		return "<script src='"+uri+"?ver="+ver+"'></script>";
	}	
}
