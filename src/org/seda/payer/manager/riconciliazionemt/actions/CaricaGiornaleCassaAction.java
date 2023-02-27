package org.seda.payer.manager.riconciliazionemt.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.j2ee5.maf.core.action.ActionException;

public class CaricaGiornaleCassaAction extends BaseRiconciliazioneTesoreriaAction {
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		
		tx_SalvaStato(request);
		String siglaProvincia = request.getParameter("tx_provincia");
		if ("".equals(siglaProvincia)) {
			siglaProvincia = null;
		}
		super.service(request);
        aggiornamentoCombo(request, session);
        loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
        LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);

       return null; 
	}
}
