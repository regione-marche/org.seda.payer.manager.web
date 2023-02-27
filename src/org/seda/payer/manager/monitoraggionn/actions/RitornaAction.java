/**
 * 
 */
package org.seda.payer.manager.monitoraggionn.actions;

import javax.servlet.http.HttpServletRequest;
import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.action.ActionException;

public class RitornaAction extends BaseManagerAction {

	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		String viewStateId = replyAttributes(true, request);		
		return viewStateId;
	}
}
