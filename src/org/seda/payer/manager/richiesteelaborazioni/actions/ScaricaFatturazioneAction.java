package org.seda.payer.manager.richiesteelaborazioni.actions;

import com.seda.commons.properties.tree.PropertiesTree;
import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;

import javax.servlet.http.HttpServletRequest;

public class ScaricaFatturazioneAction extends BaseManagerAction {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private PropertiesTree configuration = null;
	private String dbSchemaCodSocieta = "";

	public Object service(HttpServletRequest request) {
		this.configuration = (PropertiesTree) (request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		this.dbSchemaCodSocieta = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);

		String fileName = request.getParameter("fatturazioneFileName");
		String pathFileToDownload = configuration.getProperty(PropertiesPath.csvFatturazionePath.format(dbSchemaCodSocieta)).concat("/"+fileName);

		request.setAttribute("pathFatturazione", pathFileToDownload);
		request.setAttribute("fileNameFatturazione", fileName);
		return null;
	}

}
