package ru.yandex.strictweb.ajaxtools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.yandex.strictweb.ajaxtools.annotation.Arguments;
import ru.yandex.strictweb.ajaxtools.exception.MethodNotArgumentized;
import ru.yandex.strictweb.ajaxtools.orm.ORMManager;
import ru.yandex.strictweb.ajaxtools.orm.ORMManagerProvider;
import ru.yandex.strictweb.ajaxtools.presentation.JsonParanoidPresentation;
import ru.yandex.strictweb.ajaxtools.presentation.JsonRefPresentation;
import ru.yandex.strictweb.ajaxtools.presentation.Presentation;
import ru.yandex.strictweb.ajaxtools.presentation.XmlPresentation;
import ru.yandex.strictweb.ajaxtools.representation.JsonRePresentation;
import ru.yandex.strictweb.ajaxtools.representation.ParseException;
import ru.yandex.strictweb.ajaxtools.representation.XmlRePresentation;
import ru.yandex.strictweb.ajaxtools.representation.Yytoken;
import ru.yandex.strictweb.scriptjava.base.ajax.AjaxRequestResult;

public class AjaxService extends HttpServlet {
    private static final long serialVersionUID = 1755139421903673627L;
    
    private static final String HEADER_PRAGMA = "Pragma";
    private static final String HEADER_EXPIRES = "Expires";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    
    ORMManagerProvider ormManagerProvider;
    
	public void setOrmManagerProvider(ORMManagerProvider ormManagerProvider) {
        this.ormManagerProvider = ormManagerProvider;
    }
	
	BeanProvider beanProvider;

    public void setBeanProvider(BeanProvider beanProvider) {
        this.beanProvider = beanProvider;
    }
    
    AuthorityProvider authorityProvider;

    public void setAuthorityProvider(AuthorityProvider authorityProvider) {
        this.authorityProvider = authorityProvider;
    }

    protected Presentation getPresentation(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String view = request.getParameter("view");
		
		Presentation p = null;
		if(view != null) {
			if(view.equals("xml")) {
				response.setContentType("application/xml");
				p = new XmlPresentation();
			}else if(view.equals("jsonparanoid")) {
//				response.setContentType("text/javascript");
				p = new JsonParanoidPresentation();				
			}
		}
		
		if(p == null) {
//			response.setContentType("text/javascript");
			p = new JsonRefPresentation();
		}
		
		return p;
	}
	
	
	Object[] getParams(JsonRePresentation rep, Method method, HttpServletRequest request) throws Exception {
		Arguments arguments = method.getAnnotation(Arguments.class);
		if(arguments==null) throw new MethodNotArgumentized(method);
		
		Class<?>[] parameterTypes = method.getParameterTypes();
		
		int nParams = parameterTypes.length;
		if(rep.lexer.yylex() != Yytoken.TYPE_LEFT_SQUARE) throw new ParseException("Method "+method.getName()+" args["+nParams+"] expected");
		
		if(nParams == 0) {
	        if(rep.lexer.yylex() != Yytoken.TYPE_RIGHT_SQUARE) throw new ParseException("Method "+method.getName()+" empty args[] expected");
		    return null;
		}
		
		Type[] genericParameterTypes = method.getGenericParameterTypes();
		
		Object[] params = new Object[nParams];
        
		for(int i = 0; i < nParams; i++) {
		    
			Class<?> clazz = parameterTypes[i];
			Type type = genericParameterTypes[i];
	        
		    if(clazz.isAssignableFrom(HttpServletRequest.class)) {
		    	if(rep.lexer.yylex() != Yytoken.TYPE_VALUE || rep.lexer.value != null) throw new ParseException("HttpServlet should be null");
		        params[i] =  request;
		    } else {
		    	params[i] = rep.getObject(clazz, type);
		    }
		    
		    rep.lexer.yylex();
		    
            if(i == nParams-1 && rep.lexer.type == Yytoken.TYPE_RIGHT_SQUARE) break;
			if(rep.lexer.type != Yytoken.TYPE_COMMA) throw new ParseException("no , or ] in method params");					
		}
		
        if(rep.lexer.type != Yytoken.TYPE_RIGHT_SQUARE) throw new ParseException("Method "+method.getName()+": bad args");

		return params;
	}
	
    protected final void preventCaching(HttpServletResponse response) {
        response.setHeader(HEADER_PRAGMA, "no-cache");
        // HTTP 1.0 header
        response.setDateHeader(HEADER_EXPIRES, 1L);
        // HTTP 1.1 header: "no-cache" is the standard value,
        // "no-store" is necessary to prevent caching on FireFox.
        response.setHeader(HEADER_CACHE_CONTROL, "no-cache");
        response.addHeader(HEADER_CACHE_CONTROL, "no-store");
    }
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
    	doRequests(request, response, new StringReader(request.getParameter("a")));
    }
    
    void doRequests(HttpServletRequest request, HttpServletResponse response, Reader input) throws ServletException, IOException {
    	preventCaching(response);
    	
		ORMManager orm = ormManagerProvider != null ? ormManagerProvider.getORMManager() : null;
		
        Presentation present = getPresentation(request, response);
        
		try {
			if(null!=orm) orm.begin();

			response.setCharacterEncoding("utf-8");
			
            if(authorityProvider != null) {
                authorityProvider.checkRequest(request);
            }
						
			List<AjaxRequestResult> results = new ArrayList<AjaxRequestResult>();
						
			final EntityManager em = orm == null ? null : orm.get();
			
			
			XmlRePresentation.EntityFinder ef = new XmlRePresentation.EntityFinder() {
				public Object find(Class clazz, Object primaryKey) {
				    if(em == null) return createFakeEntity(clazz, primaryKey);
					return em.find(clazz, primaryKey);
				}
			};
			
			JsonRePresentation rep = new JsonRePresentation(ef).reset(input);
			
			if(rep.lexer.yylex() != Yytoken.TYPE_LEFT_SQUARE) throw new ParseException("request args should be passed as array");
			
			for(int i=0;;i++) {
			    AjaxRequestResult result = new AjaxRequestResult();
			    			    
    			try {				
    		        String beanName = getStringFromJsonArray("Bean name expected", rep);
    		        if(beanName==null) break;
    		        
    		        String methodName = getStringFromJsonArray("Method name expected", rep);
    		        if(methodName==null) break;
    		        
    		        rep.lexer.yylex();
    	            if(rep.lexer.type != Yytoken.TYPE_COMMA) {
    	                throw new ParseException("Method " + methodName + " args expected as array");
    	            }
    		            			    
                    if(!doAction(beanName, methodName, rep, request, result)) break;
    				
    				if(orm != null) orm.commit();
    
    			}catch(Throwable e) {
    				if(e.getClass().getCanonicalName().equals("org.eclipse.jetty.continuation.ContinuationThrowable")) {
    					throw e;
    				}
    				e.printStackTrace();
    				if(null!=orm) orm.rollback();
    				result.setError(e);
    				if(e instanceof ParseException) {
    				    results.add(result);
    				    break;
    				}
    			}
    			
    			results.add(result);
			}
			present.present(response.getWriter(), results);
		}catch(Throwable e) {
			if(e.getClass().getCanonicalName().equals("org.eclipse.jetty.continuation.ContinuationThrowable")) {
				throw (Error)e;
			}
			e.printStackTrace();
			if(null!=orm) orm.rollback();

			try {
                present.present(response.getWriter(), e);
            } catch (Throwable e1) {
                response.sendError(500, e.getMessage());
            }
		} finally {
			if(null!=orm) orm.close();
		}
	}

    private String getStringFromJsonArray(String errMessage, JsonRePresentation rep) throws IOException, ParseException {
    	for(;;) {
    		int type = rep.lexer.yylex();
    		if(type == Yytoken.TYPE_COMMA) continue;
    		if(type == Yytoken.TYPE_RIGHT_SQUARE) return null;
            if(type == Yytoken.TYPE_EOF) return null;
    		if(type == Yytoken.TYPE_VALUE) return rep.lexer.value;
    		throw new ParseException(errMessage);
    	}
	}

	/** Creates a fake entity */
	protected Object createFakeEntity(Class<?> clazz, Object primaryKey) {
	    try {
	        Object ins = clazz.newInstance();
	        for(Field f: clazz.getFields()) {
	            if(f.isAnnotationPresent(Id.class)) {
	                f.set(ins, primaryKey);
	                break;
	            }
	        }
	        return ins;
	    }catch(Exception e) {
	        throw new RuntimeException(e);
	    }
    }

    protected boolean doAction(String beanName, String methodName, JsonRePresentation rep, HttpServletRequest request, AjaxRequestResult result) throws Throwable {		
		Object bean = beanProvider == null ? Class.forName(beanName).newInstance() : beanProvider.getBeanInstance(beanName);
		
        Method method = null;
        
        for(Method m : bean.getClass().getMethods()) if(m.getName().equals(methodName)) {
            method = m;
            break;
        }
        
        if(method == null) throw new RuntimeException("Unknown method " + methodName);
        
        Arguments arguments = method.getAnnotation(Arguments.class);
        
        if(null == arguments) throw new RuntimeException("Method is not visible for ajax call");
        
        if(false && arguments.checkReferer()) {
            String ref = request.getHeader("Referer");
            if(ref == null) throw new RuntimeException("Invalid referer");
            
            ref = ref.replaceFirst("http://", "").replaceFirst("/.*$", "");
            
            String host = request.getHeader("Host");
            if(host == null) throw new RuntimeException("Invalid host");
            if(!ref.equalsIgnoreCase(host)) {
                throw new RuntimeException("Bad referer " + ref + " not equals " + host +" host");
            }
        }

		
		Object[] params = getParams(rep, method, request);
		
		if(authorityProvider != null) {
		    authorityProvider.checkMethodRequest(request, method, params);
		}
		
//		System.out.println(Arrays.toString(params));
		try {
			result.setData(method.invoke(bean, params));
		}catch(Throwable th) {
			throw th.getCause()!=null ? th.getCause() : th;
		}
		
		return true;
	}

	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		String a = request.getParameter("a");
		Reader reader = a == null ? request.getReader() : new StringReader(a);
		doRequests(request, response, reader);
	}
	
//	static DocumentBuilderFactory domFact = DocumentBuilderFactory.newInstance();
//	public static Document newDocument(InputStream in) throws Exception {
//		return domFact.newDocumentBuilder().parse(in);
//	}	
//
//	public static Document newDocument(String in) throws Exception {
//		org.xml.sax.InputSource inputSource = new org.xml.sax.InputSource();
//		inputSource.setCharacterStream(new java.io.StringReader(in));
//		return domFact.newDocumentBuilder().parse(inputSource);
//	}
//
//	public static Document newDocument() throws Exception {
//		return domFact.newDocumentBuilder().newDocument();
//	}
//	public static Document newDocument(Reader reader) throws Exception {
//		return domFact.newDocumentBuilder().parse(new InputSource(reader));
//	}	
	
	public static void sendFile(HttpServletResponse response, String fileName) throws IOException {
		File file = new File(fileName);
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
		
		byte[] buf = new byte[4096];
		int len;
		InputStream in = new FileInputStream(file);
		OutputStream out = response.getOutputStream();
		while(0 < (len = in.read(buf))) {
			out.write(buf, 0, len);
		}
	}
}

