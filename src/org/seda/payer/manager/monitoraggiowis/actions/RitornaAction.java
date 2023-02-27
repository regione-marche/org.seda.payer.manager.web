package org.seda.payer.manager.monitoraggiowis.actions;

import javax.servlet.http.HttpServletRequest;

import com.seda.j2ee5.maf.core.action.ActionException;
import org.seda.payer.manager.actions.BaseManagerAction;

public class RitornaAction extends BaseManagerAction {

	
	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		replyAttributes(true, request, "validator.message");
		
		return null;
	}
}
