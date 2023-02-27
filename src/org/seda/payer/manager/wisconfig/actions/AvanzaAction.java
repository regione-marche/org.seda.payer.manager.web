package org.seda.payer.manager.wisconfig.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.action.ActionException;

@SuppressWarnings("serial")
public class AvanzaAction  extends BaseManagerAction{
	public Object service(HttpServletRequest request) throws ActionException {
		
		request.setAttribute("start", "A");
		return null;
	}

}
