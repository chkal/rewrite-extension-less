package de.chkal.rewrite.less;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.ocpsoft.rewrite.config.Rule;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;
import org.ocpsoft.rewrite.servlet.config.IPath;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.event.BaseRewrite.Flow;
import org.ocpsoft.rewrite.servlet.http.event.HttpInboundServletRewrite;

public class Less implements Rule
{

   private final IPath resourcePath;

   private final LessEngine lessEngine = new LessEngine();

   public static Less matches(String pattern)
   {
      return new Less(Path.matches(pattern));
   }

   public Less(IPath resourcePath)
   {
      this.resourcePath = resourcePath;
   }

   @Override
   public String getId()
   {
      return null;
   }

   @Override
   public boolean evaluate(Rewrite event, EvaluationContext context)
   {

      if (event instanceof HttpInboundServletRewrite) {

         if (resourcePath.evaluate(event, context)) {
            return true;
         }

      }

      return false;

   }

   @Override
   public void perform(Rewrite event, EvaluationContext context)
   {
      if (event instanceof HttpInboundServletRewrite) {

         HttpInboundServletRewrite inboundServletRewrite = (HttpInboundServletRewrite) event;

         String path = inboundServletRewrite.getRequestPath();

         ServletContext servletContext = inboundServletRewrite.getRequest().getServletContext();

         InputStream inputStream = servletContext.getResourceAsStream(path);

         if (inputStream != null) {

            try {

               String lessData = IOUtils.toString(inputStream);
               String cssData = lessEngine.process(lessData);

               HttpServletResponse response = inboundServletRewrite.getResponse();
               response.getOutputStream().write(cssData.getBytes(Charset.forName("UTF8")));
               response.flushBuffer();

               inboundServletRewrite.setFlow(Flow.HANDLED);
            }
            catch (IOException e) {
               // TODO
            }

         }

      }

   }

}
