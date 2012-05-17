package de.chkal.rewrite.less;

import org.ocpsoft.rewrite.config.Rule;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;
import org.ocpsoft.rewrite.servlet.config.IPath;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.http.event.HttpInboundServletRewrite;

public class Less implements Rule {

  private final IPath resourcePath;

  public static Less fileExtension(String pattern) {
    return new Less( Path.matches(pattern));
  }
  
  public Less(IPath resourcePath) {
    this.resourcePath = resourcePath;
  }
  
  @Override
  public String getId() {
    return null;
  }

  @Override
  public boolean evaluate(Rewrite event, EvaluationContext context) {
    if(event instanceof HttpInboundServletRewrite) {
      
      if(resourcePath.evaluate(event, context)) {
        return true;
      }
      
    }
    return false;
    
  }

  @Override
  public void perform(Rewrite event, EvaluationContext context) {

    
    
    
  }

}
