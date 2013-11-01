package com.copyright.ccc.web.dispatcher;

import javax.servlet.http.HttpServletRequest;

public interface DispatcherRequest
{
  /**
   * Identifies a specific Dispatcher Request
   * @param type
   * @param target
   * @return true if the input type and target match
   */
  public boolean isMyType(String type, String target);

  /**
   * Assembles the target URL
   * @param request
   * @return the target URL that represents the dispatch request or if an error
   * is encountered, returns the <code>Dispatcher.Contstants.ND_DEFAULT_URL</code>.
   */
  public String generateTargetURL(HttpServletRequest request);

  /**
   * Specifies whether the implemetor will use redirection or not.
   */
  public boolean isUseRedirect();
  
  /**
   * Specifies whether the implemetor will dispatch requested to 
   * an external resource (generally, a web application other than Naples).
   */
  public boolean isExternalResource();
}
