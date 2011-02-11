package ru.yandex.strictweb.ajaxtools;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import ru.yandex.strictweb.ajaxtools.annotation.Arguments;
import ru.yandex.strictweb.ajaxtools.exception.MethodNotArgumentized;
import ru.yandex.strictweb.ajaxtools.orm.ORMManager;
import ru.yandex.strictweb.ajaxtools.orm.ORMManagerProvider;
import ru.yandex.strictweb.ajaxtools.presentation.JsonParanoidPresentation;
import ru.yandex.strictweb.ajaxtools.presentation.JsonRefPresentation;
import ru.yandex.strictweb.ajaxtools.presentation.Presentation;
import ru.yandex.strictweb.ajaxtools.presentation.XmlPresentation;
import ru.yandex.strictweb.scriptjava.base.ajax.Ajax;
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
		if(view!=null) {
			if(view.equals("xml")) {
				response.setContentType("application/xml");
				p = new XmlPresentation();
			}else if(view.equals("jsonparanoid")) {
				response.setContentType("text/plain");
				p = new JsonParanoidPresentation();				
			}
		} else {
			response.setContentType("text/plain");
			p = new JsonRefPresentation();
		}
		
		return p;
	}
	
	
	Object[] getParams(RePresentation.EntityFinder ef, Method method, HttpServletRequest request) throws Exception {
		int nParams = method.getParameterTypes().length;
		if(nParams == 0) return null;
		
		Arguments arguments = method.getAnnotation(Arguments.class);
		if(arguments==null) throw new MethodNotArgumentized(method);
		
		Object[] params = new Object[nParams];
		Document doc = null;
		
		doc = newDocument(request.getParameter(Ajax.XML_DATA_PARAM));
		
		RePresentation rep = new RePresentation(ef);			

		NodeList properties = doc.getFirstChild().getChildNodes();
		
		for(int i = 0; i < properties.getLength() && i<params.length; i++) {
		    Class<?> parameterType = method.getParameterTypes()[i];
		    if(parameterType.isAssignableFrom(HttpServletRequest.class)) {
		        params[i] =  request;
		    } else {
    			Node node = properties.item(i);
                params[i] = rep.getObject(node, parameterType, method.getGenericParameterTypes()[i]);
		    }
		}

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
    	preventCaching(response);
    	
		ORMManager orm = ormManagerProvider != null ? ormManagerProvider.getORMManager() : null;
		
		try {
			if(null!=orm) orm.begin();

			response.setCharacterEncoding("utf-8");
			
			Presentation present = getPresentation(request, response);
			AjaxRequestResult result = new AjaxRequestResult();
			
			try {				
				final EntityManager em = orm == null ? null : orm.get();
				
				doAction(new RePresentation.EntityFinder() {
					public Object find(Class clazz, Object primaryKey) {
					    if(em == null) return createFakeEntity(clazz, primaryKey);
						return em.find(clazz, primaryKey);
					}
				}, request, result);
				
				if(orm != null) orm.commit();

			}catch(Throwable e) {
				e.printStackTrace();
				if(null!=orm) orm.rollback();
				result.setError(e);
			}

            if(result.getData() != AjaxRequestResult.NO_DATA) {
                response.getWriter().print(present.toString(result));
            }

		}catch(Throwable e) {
			e.printStackTrace();
			if(null!=orm) orm.rollback();
			
			CharArrayWriter charOut = new CharArrayWriter();
			
			e.printStackTrace(new PrintWriter(charOut));
			
			response.sendError(500, charOut.toString());
		} finally {
			if(null!=orm) orm.close();
		}
	}

    /** Creates a fake entity */
	protected Object createFakeEntity(Class clazz, Object primaryKey) {
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

    protected void doAction(RePresentation.EntityFinder ef, HttpServletRequest request, AjaxRequestResult result) throws Throwable {
		String beanName = request.getParameter(Ajax.BEAN_NAME_PARAM);
		String methodName = request.getParameter(Ajax.METHOD_NAME_PARAM);
		
		Object bean = beanProvider == null ? Class.forName(beanName).newInstance() : beanProvider.getBeanInstance(beanName);
		
        Method method = null;
        
        for(Method m : bean.getClass().getMethods()) if(m.getName().equals(methodName)) {
            method = m;
            break;
        }
        
        if(method == null) throw new RuntimeException("Unknown method " + methodName);
        
        Arguments arguments = method.getAnnotation(Arguments.class);
        
        if(null == arguments) throw new RuntimeException("Method is not visible for ajax call");
        
        if(arguments.checkReferer()) {
            String ref = request.getHeader("Referer");
            if(ref == null) throw new RuntimeException("Invalid referer");
            
            ref = ref.replaceFirst("http://", "").replaceFirst("/.*$", "");
            
            String host = request.getHeader("Host");
            if(host == null) throw new RuntimeException("Invalid host");
            if(!ref.equalsIgnoreCase(host)) {
                throw new RuntimeException("Bad referer " + ref + " not equals " + host +" host");
            }
        }

        if(authorityProvider != null) {
        	authorityProvider.checkRequest(request);
        }
        
        if(arguments.roles().length > 0) {
        	authorityProvider.checkRequest(request, arguments.roles());
        }
		
		Object[] params = getParams(ef, method, request);
//		System.out.println(Arrays.toString(params));
		try {
			result.setData(method.invoke(bean, params));
		}catch(Throwable th) {
			throw th.getCause()!=null ? th.getCause() : th;
		}
	}

	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}
	
	static DocumentBuilderFactory domFact = DocumentBuilderFactory.newInstance();
	public static Document newDocument(InputStream in) throws Exception {
		return domFact.newDocumentBuilder().parse(in);
	}	

	public static Document newDocument(String in) throws Exception {
		org.xml.sax.InputSource inputSource = new org.xml.sax.InputSource();
		inputSource.setCharacterStream(new java.io.StringReader(in));
		return domFact.newDocumentBuilder().parse(inputSource);
	}

	public static Document newDocument() throws Exception {
		return domFact.newDocumentBuilder().newDocument();
	}
	public static Document newDocument(Reader reader) throws Exception {
		return domFact.newDocumentBuilder().parse(new InputSource(reader));
	}	
	
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

