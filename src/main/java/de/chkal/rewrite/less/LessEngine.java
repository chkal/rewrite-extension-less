package de.chkal.rewrite.less;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.apache.commons.io.IOUtils;


/*
 * http://code.google.com/p/wro4j/source/browse/wro4j-extensions/src/main/java/ro/isdc/wro/extensions/processor/support/less/
 */
class LessEngine {

  private ScriptEngine scriptEngine;
  
  private String lessScript;

  public LessEngine() {

    // setup ScriptEngine
    ScriptEngineManager manager = new ScriptEngineManager();
    scriptEngine = manager.getEngineByName("JavaScript");

    try {
      InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("less-1.3.0.min.js");
      if (inputStream == null) {
        throw new IllegalStateException("Could not find less.js");
      }
      lessScript = IOUtils.toString(inputStream, Charset.forName("UTF-8"));
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }

  }

  public String process(String script) {

    try {

      SimpleBindings binding = new SimpleBindings();
      binding.put("input", script);
      
      
      StringBuilder builder = new StringBuilder();
      builder.append(lessScript);
      builder.append("var window.less = {}; ");
      builder.append("less.render(input, function (e, css) { console.log(css); });");
      
      
      scriptEngine.eval(builder.toString(), binding);
      return "";

    } catch (ScriptException e) {
      throw new IllegalArgumentException("LESS script invokation failed", e);
    }

  }

  public static void main(String[] args) {

    LessEngine engine = new LessEngine();
    engine.process(".class { width: 1 + 1 }");

  }

}
