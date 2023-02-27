package org.seda.payer.manager.entrate.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.actions.DispatchHtmlAction.FiredButton;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class RicercaDocumentiCarichiFlow extends DispatchHtmlAction implements FlowManager {

  private static final long serialVersionUID = 1L;
  
  public void end(HttpServletRequest arg0) {
  }

  public String process(HttpServletRequest request) throws FlowException {
    // Action o Screen da invocare successivamente...
    String screen = (String) request.getAttribute("screen");
    return screen;
  }
  
  
  public void start(HttpServletRequest request) {
    
  }

  @Override
  public Object index(HttpServletRequest request) throws ActionException {
    return null;
  }
  
}