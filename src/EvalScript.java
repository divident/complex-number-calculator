import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


public class EvalScript {
	private ScriptEngineManager manager;
	private ScriptEngine engine;
	
	public EvalScript(Map<String, ComplexNumber> variables, TreeMain treeMain) throws Exception{
		manager = new ScriptEngineManager();
		engine = manager.getEngineByName("nashorn");
		engine.put("tree", treeMain);
		engine.put("variables", variables);
		engine.eval("var ComplexNumber = Java.type('ComplexNumber')");
	}
	
	public void eval(String script) throws ScriptException {
		engine.eval(script);
	}
}
