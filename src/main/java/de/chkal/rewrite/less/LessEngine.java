package de.chkal.rewrite.less;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class LessEngine
{

   private final String baseScript;

   public LessEngine()
   {
      StringBuilder scriptBuilder = new StringBuilder();
      scriptBuilder.append("function print(s) {}"); // required by env.rhino
      scriptBuilder.append(getClasspathResourceAsString("env.rhino.1.2.js"));
      scriptBuilder.append(getClasspathResourceAsString("less-1.3.0.min.js"));
      scriptBuilder.append(getClasspathResourceAsString("api.js"));
      baseScript = scriptBuilder.toString();
   }

   public String process(String less)
   {

      Context context = Context.enter();

      try {

         context.setOptimizationLevel(-1);
         context.setLanguageVersion(Context.VERSION_1_8);

         Scriptable scope = context.initStandardObjects();

         String script = new StringBuilder(baseScript)
                  .append("lessToCss('")
                  .append(escape(less))
                  .append("');")
                  .toString();

         Object result = context.evaluateString(scope, script, this.getClass().getSimpleName(), 1, null);

         if (result != null) {
            return result.toString();
         }

      }
      finally {
         Context.exit();
      }
      return null;

   }

   private static String getClasspathResourceAsString(String resource)
   {
      try {
         InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
         if (inputStream == null) {
            throw new IllegalStateException("Could not find resource on the classpath: " + resource);
         }
         return IOUtils.toString(inputStream, Charset.forName("UTF-8"));
      }
      catch (IOException e) {
         throw new IllegalArgumentException(e);
      }
   }

   private String escape(String s)
   {
      return s.replace("\r", "").replace("\n", "\\n");
   }

}
