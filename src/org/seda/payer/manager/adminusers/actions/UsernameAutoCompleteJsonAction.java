package org.seda.payer.manager.adminusers.actions;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.string.Convert;
import com.seda.j2ee5.maf.core.action.ActionException;

import flexjson.JSONSerializer;


public class UsernameAutoCompleteJsonAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public Object service(HttpServletRequest request) throws ActionException 
	{
		super.service(request);
		HttpSession session = request.getSession();
		//se sto facendo l'autocompletamento "filtrato" devo sempre reinterrogare il db
		// e non recuperare i dati dalla session
		if (request.getParameter("filter") == null && session.getAttribute("listaUserNameJson") != null)
		{
			request.setAttribute("listaUserNameJson", session.getAttribute("listaUserNameJson"));
		}
		else
		{
			//inizio LP PG21XX04 Leak
			WebRowSet wrs = null;
			//fine LP PG21XX04 Leak
			try {
				com.seda.security.webservice.dati.ListaUtentiRequestType req = new com.seda.security.webservice.dati.ListaUtentiRequestType();
				req.setPageNumber(0);
				req.setRowsPerPage(0);
				req.setOrder(""); //per default è ordinato per username DESC
				if (request.getParameter("filter") != null && !request.getParameter("filter").equals(""))
					req.setUserName((String)request.getParameter("filter"));
				
				com.seda.security.webservice.dati.ListaUtentiResponseType res = WSCache.securityServer.listaUtenti(req, request);
				
				List<String> listUserName = new ArrayList<String>();
				if (res != null && res.getResponse() != null && res.getResponse().getRetCode().equals("00"))
				{
					if (res.getListXml() != null)
					{
						//inizio LP PG21XX04 Leak
						//WebRowSet wrs = Convert.stringToWebRowSet(res.getListXml());
						wrs = Convert.stringToWebRowSet(res.getListXml());
						//fine LP PG21XX04 Leak
						while (wrs.next())
							listUserName.add(wrs.getString(5));
					}
				}
	
				String jsonObject = new JSONSerializer().deepSerialize(listUserName);
				
				request.setAttribute("listaUserNameJson", jsonObject);
				//li salvo in sessione per non doverli ricaricare ogni volta 
				//all'interno della stessa navigazione della form
				session.setAttribute("listaUserNameJson", jsonObject);
			} 
			catch (Exception e) {	}
			//inizio LP PG21XX04 Leak
			finally
			{
		    	try {
		    		if(wrs != null) {
		    			wrs.close();
		    		}
		    	} catch (SQLException e) {
		    		e.printStackTrace();
				}
			}
			//fine LP PG21XX04 Leak
		}
		return null;
	}

}
