package ru.yandex.strictweb.scriptjava.compiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;

import ru.yandex.strictweb.scriptjava.base.BrowserHistory;
import ru.yandex.strictweb.scriptjava.base.CommonDelegate;
import ru.yandex.strictweb.scriptjava.base.CommonElements;
import ru.yandex.strictweb.scriptjava.base.DOMBuilder;
import ru.yandex.strictweb.scriptjava.base.DOMEvent;
import ru.yandex.strictweb.scriptjava.base.Document;
import ru.yandex.strictweb.scriptjava.base.InputValidator;
import ru.yandex.strictweb.scriptjava.base.JavaScriptFunction;
import ru.yandex.strictweb.scriptjava.base.JsException;
import ru.yandex.strictweb.scriptjava.base.Location;
import ru.yandex.strictweb.scriptjava.base.Navigator;
import ru.yandex.strictweb.scriptjava.base.Node;
import ru.yandex.strictweb.scriptjava.base.NodeBuilder;
import ru.yandex.strictweb.scriptjava.base.Screen;
import ru.yandex.strictweb.scriptjava.base.StrictWeb;
import ru.yandex.strictweb.scriptjava.base.Style;
import ru.yandex.strictweb.scriptjava.base.ValidatorHelperBase;
import ru.yandex.strictweb.scriptjava.base.VoidDelegate;
import ru.yandex.strictweb.scriptjava.base.Window;
import ru.yandex.strictweb.scriptjava.base.custom.NoChildNodeBuilder;
import ru.yandex.strictweb.scriptjava.base.custom.NoNode;
import ru.yandex.strictweb.scriptjava.base.custom.SelectNodeBuilder;
import ru.yandex.strictweb.scriptjava.base.custom.SelectOptionNode;
import ru.yandex.strictweb.scriptjava.base.custom.SelectOptionNodeBuilder;
import ru.yandex.strictweb.scriptjava.base.custom.TableColumnNode;
import ru.yandex.strictweb.scriptjava.base.custom.TableColumnNodeBuilder;
import ru.yandex.strictweb.scriptjava.base.custom.TableInnerNode;
import ru.yandex.strictweb.scriptjava.base.custom.TableInnerNodeBuilder;
import ru.yandex.strictweb.scriptjava.base.custom.TableNodeBuilder;
import ru.yandex.strictweb.scriptjava.base.custom.TableRowNode;
import ru.yandex.strictweb.scriptjava.base.custom.TableRowNodeBuilder;
import ru.yandex.strictweb.scriptjava.base.util.Console;
import ru.yandex.strictweb.scriptjava.base.util.IntervalIdentifier;
import ru.yandex.strictweb.scriptjava.base.util.TimeoutIdentifier;


public class Compiler {
	private Parser parser = new Parser();
	
	private String pathPrefix = "";
	//private boolean anonymousClassDeclarationTest;
	private File outputFile;
	private long lastFileModificationTime = 0;
//	private char[] lastSource;

	public Compiler(String pathPrefix) throws Exception {
		this.pathPrefix = pathPrefix;
		
		this
			.parseClass(Style.class)
			.parseClass(JavaScriptFunction.class)
			
			.parseClass(Node.class)
			.parseClass(TableColumnNode.class)
			.parseClass(TableRowNode.class)
			.parseClass(TableInnerNode.class)
			.parseClass(SelectOptionNode.class)
			.parseClass(NoNode.class)
			
			.parseClass(Location.class)
			.parseClass(Document.class)
			.parseClass(BrowserHistory.class)
			.parseClass(Navigator.class)
			.parseClass(Console.class)
			.parseClass(Screen.class)
			.parseClass(Window.class)
			
			.parseClass(IntervalIdentifier.class)
			.parseClass(TimeoutIdentifier.class)
			.parseClass(StrictWeb.class)
			.parseClass(JsException.class)
			.parseClass(InputValidator.class)
			
			.parseClass(DOMBuilder.class)
			.parseClass(NodeBuilder.class)
			.parseClass(TableColumnNodeBuilder.class)
			.parseClass(TableRowNodeBuilder.class)
			.parseClass(TableInnerNodeBuilder.class)
			.parseClass(TableNodeBuilder.class)
			.parseClass(SelectOptionNodeBuilder.class)
			.parseClass(SelectNodeBuilder.class)
			.parseClass(NoChildNodeBuilder.class)
			
			.parseClass(DOMEvent.class)
			.parseClass(VoidDelegate.class)
			.parseClass(CommonDelegate.class)	
			.parseClass(CommonElements.class)
			
			.parseClass(ValidatorHelperBase.class)
			;
	}
	
	public Compiler setDebugLevel(int dl) {
		parser.setDebugLevel(dl);
		return this;
	}
	
	public Compiler setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
		return this;
	}

	public Compiler parseClass(Class clas) throws Exception {
		if(clas.isEnum()) {
			parser.parseEnum(clas);
			return this;
		}
		return parseFile(clas.getName().replace('.', '/')+".java");
	}

	public Compiler parseFile(String fileName) throws Exception {
//		lastSource = readFile(pathPrefix + fileName);
		parser.lastFileName = fileName;
		
//		File f = new File(pathPrefix + fileName);
//		lastFileModificationTime = f.lastModified();
		parser.parseFile(pathPrefix + fileName);
		//System.out.println(fileName);
//		parse(getASTNode(lastSource));
		return this;
	}
	
	public Compiler includeJsFile(String fileName) {
		
		throw new RuntimeException("TODO");
		
//		return this;
	}
	

//	private ASTNode getASTNode(char[] source) {
//		ASTParser parser = ASTParser.newParser(AST.JLS3);
//		parser.setKind(ASTParser.K_COMPILATION_UNIT);
//		parser.setSource(source);
//		parser.setResolveBindings(true); // we need bindings later on
//		return parser.createAST(null /* IProgressMonitor */); // parse
//	}

//	private char[] readFile(String fileName) throws IOException {
//		FileInputStream in = null;
//		try {
//			File file = new File(fileName);
//			lastFileModificationTime = Math.max(lastFileModificationTime, file.lastModified());
//			byte[] source = new byte[(int)file.length()];
//			in = new FileInputStream(file);
//			in.read(source);
//			return new String(source, 0, source.length, "utf8").toCharArray();
//		} finally {
//			if(in!=null) in.close();
//		}
//	}

	public Compiler compile() throws Exception {
		parser.compile();
		
		return this;
	}
	
	public Compiler compileAndSave(String outputFileName) throws Exception {
		lastFileModificationTime = 0;
		compile();
		outputFile = new File(outputFileName);
		save();
		parser.code.setLength(0);
		return this;
	}

//	private boolean hasAnonymousClassDeclaration(ASTNode node) {
//		anonymousClassDeclarationPresent = false;
//		anonymousClassDeclarationTest = true;
//		StringBuffer temp = code;
//		code = new StringBuffer();
//		
//		parse(node);
//		
//		code = temp;
//		anonymousClassDeclarationTest = false;
//		return anonymousClassDeclarationPresent;
//	}
	
	public void save() throws Exception {
		if(outputFile.exists() && lastFileModificationTime < outputFile.lastModified()) {
//			return;
		}
		OutputStream out = new FileOutputStream(outputFile);
		byte[] buf = parser.getAllCode().getBytes("utf8");
		out.write(buf);
		out.close();
	}

	public Compiler addPlugin(CompilerPlugin plugin) {
		parser.plugins.add(plugin);
		plugin.setParser(parser);
		return this;
	}

	public Compiler addLibrary(CompilerPreparer preparer) throws Exception {
		preparer.prepareCompiler(this);
		return this;
	}
	
	public Compiler parseClassWithLabel(Class clazz, String label) throws Exception {
		parser.addClassLabel(clazz, label);
		return parseClass(clazz);
	}

	public Compiler ignoreClasses(Collection<String> ignoredClasses) {
		parser.addIgnoredClasses(ignoredClasses);
		return this;
	}

	public Compiler setObfuscate(boolean obfuscate) {
		parser.obfuscate = obfuscate;
		return this;
	}

    public Compiler parseClassAsPOJOBean(Class<?> clas) {
        ParsedClass parsedClass = parser.parseAsPOJOBean(clas);
        parser.classes.put(parsedClass.name, parsedClass);

        return this;
    }
}
