package org.seda.payer.manager.rendicontazione.actions;

import javax.servlet.http.HttpServletRequest;
import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;

public class DownloadFlusso extends BaseManagerAction {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {

		String template = getTemplateCurrentApplication(request,request.getSession());
		
		PropertiesTree configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		
		
		String directoryFlussi = WSCache.directoryFlussi;
		if(configuration.getProperty(PropertiesPath.directoryFlussi.format().concat("."+template))!=null){
			directoryFlussi = configuration.getProperty(PropertiesPath.directoryFlussi.format().concat("."+template));
		}
		System.out.println("directoryFlussi: "+directoryFlussi);
		
		String tx_nome_flusso = request.getParameter("tx_nome_flusso");
		if(tx_nome_flusso == null || tx_nome_flusso.equals("") || 
				directoryFlussi == null || directoryFlussi.equals("")) return null;
		request.setAttribute("nome_flusso", tx_nome_flusso);
		request.setAttribute("pathname_flusso", directoryFlussi + tx_nome_flusso);
		
		return null;
	}

}
