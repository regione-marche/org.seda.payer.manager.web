package org.seda.payer.manager.actions;
import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.util.PayerCommandInvoker;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.action.HtmlAction;

@SuppressWarnings("serial")
public class ActionCaller  extends HtmlAction {


	public Object service(HttpServletRequest arg0) throws ActionException {
		PayerCommandInvoker.instance().executeCommand(arg0);
		return null;
	}
	
}
