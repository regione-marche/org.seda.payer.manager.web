package com.tonbeller.wcf.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.seda.commons.logger.CustomLoggerManager;
import com.seda.commons.logger.LoggerWrapper;

public class WcfController extends Controller implements Serializable {

	private static LoggerWrapper logger = CustomLoggerManager.get(WcfController.class);
	  private List requestListeners = new LinkedList();

	  WcfController() {
	  }

	  private static final String WEBKEY = WcfController.class.getName() + ".dispatcher";

	  public static Controller instance(HttpSession session) {
	    WcfController ctrl = (WcfController) session.getAttribute(WEBKEY);
	    if (ctrl == null) {
	      ctrl = new WcfController();
	      session.setAttribute(WEBKEY, ctrl);
	    }
	    return ctrl;
	  }

	  public void addRequestListener(RequestListener l) {
	    requestListeners.add(l);
	  }

	  public void removeRequestListener(RequestListener l) {
	    requestListeners.remove(l);
	  }

	  public void setNextView(String uri) {
	    RequestContext context = RequestContext.instance();
	    context.getRequest().setAttribute(RequestFilter.NEXTVIEW, uri);
	  }
	  
	  public String getNextView() {
	    RequestContext context = RequestContext.instance();
	    return (String)context.getRequest().getAttribute(RequestFilter.NEXTVIEW);
	  }

	  public void request(RequestContext context) throws Exception {
	    // avoid ConcurrentModificationException - when a Component
	    // registers child components while dispatching the request
	    ArrayList list = new ArrayList(requestListeners);
	    for (Iterator it = list.iterator(); it.hasNext();) {
	      RequestListener l = (RequestListener) it.next();
	      l.request(context);
	    }
	  }

	  public List getRootListeners() {
	    return Collections.unmodifiableList(requestListeners);
	  }
}
