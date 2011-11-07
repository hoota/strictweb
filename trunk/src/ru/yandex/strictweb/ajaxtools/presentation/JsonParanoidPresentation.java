package ru.yandex.strictweb.ajaxtools.presentation;

import java.io.IOException;
import java.util.regex.Pattern;

public class JsonParanoidPresentation extends JsonPresentation {
	static Pattern dblQuotePattern = Pattern.compile("\"");
	static final char[] hex = new char[] {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	
	public static Appendable staticSafe(Appendable buf, String s) throws IOException {
	    int length = s.length();
        for(int i=0; i<length;i++) {
            int c = s.charAt(i);
            if((c>='0'&&c<='9') || (c>='a'&&c<='z') || (c>='A'&&c<='Z') || c=='_' || c=='.') buf.append((char)c);
            else buf.append("\\u").append(hex[(c>>12)&0xf]).append(hex[(c>>8)&0xf]).append(hex[(c>>4)&0xf]).append(hex[c&0xf]);
        }
        return buf;	    
	}
	
	@Override
	public Appendable safe(String s) throws IOException {
	    return staticSafe(out.append('"'), s).append('"');
	}

    @Override
	public Appendable safeKey(String key) throws IOException {
	    return staticSafe(out.append('"'), key).append("\":");
	}	
}
