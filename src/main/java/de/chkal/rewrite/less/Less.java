package de.chkal.rewrite.less;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.ocpsoft.logging.Logger;
import org.ocpsoft.rewrite.config.Rule;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;
import org.ocpsoft.rewrite.servlet.http.event.HttpInboundServletRewrite;

/**
 * 
 * Implementation of {@link Rule} that renders LESS files to CSS.
 * 
 * @author Christian Kaltepoth
 * 
 */
public class Less implements Rule
{

   private final Logger log = Logger.getLogger(Less.class);

   private final String suffix;

   private final LessEngine lessEngine = new LessEngine();

   public static Less fileType(String fileType)
   {
      return new Less("." + fileType);
   }

   private Less(String suffix)
   {
      this.suffix = suffix;
   }

   @Override
   public String getId()
   {
      return null;
   }

   @Override
   public boolean evaluate(Rewrite event, EvaluationContext context)
   {

      // rendering effects only inbound requests
      if (event instanceof HttpInboundServletRewrite) {

         // the rule matches if the path ends with the suffix
         String path = ((HttpInboundServletRewrite) event).getRequestPath();
         if (path.endsWith(suffix)) {
            return true;
         }

      }

      return false;

   }

   @Override
   public void perform(Rewrite event, EvaluationContext context)
   {

      // rendering effects only inbound requests
      if (event instanceof HttpInboundServletRewrite) {

         HttpInboundServletRewrite inboundRewrite = (HttpInboundServletRewrite) event;

         // try to load the accessed LESS source
         ServletContext servletContext = inboundRewrite.getRequest().getServletContext();
         InputStream inputStream = servletContext.getResourceAsStream(inboundRewrite.getRequestPath());

         // proceed only if requested resource has been found
         if (inputStream != null) {

            // IO errors must be handled here
            try {

               // read the LESS source and render it to CSS
               String lessData = IOUtils.toString(inputStream);
               String cssData = lessEngine.process(lessData);

               // write the CSS to the client
               HttpServletResponse response = inboundRewrite.getResponse();
               response.getOutputStream().write(cssData.getBytes(Charset.forName("UTF8")));
               response.flushBuffer();

               // the application doesn't need to process the request anymore
               inboundRewrite.abort();

            }
            catch (IOException e) {
               log.error("Failed to process LESS file", e);
            }

         }

      }

   }

}
