package org.seda.payer.manager.ottico.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.ottico.views.ElaborazioniView;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.views.BaseView.BaseListName;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.commons.utility.StringUtility;
import com.seda.payer.ottico.webservice.manager.dati.ListResponseTypePageInfo;
import com.seda.payer.ottico.webservice.manager.dati.RicercaListaElabOtticoRequest;
import com.seda.payer.ottico.webservice.manager.dati.RicercaListaElabOtticoResponse;
/**
 * @author marco.montisano
 */
public class ElaborazioniAction extends BaseManagerAction {

	private static final long serialVersionUID = 1L;
	private ElaborazioniView view;

	public Object service(HttpServletRequest request) throws ActionException {
		try {
			view = new ElaborazioniView(context, request);
			if (view.getFiredButtonReset() != null)
				view.reset();
			
			view = new ElaborazioniView(context, request);

			view.setBaseList(BaseListName.listaSocieta, false);
			view.setBaseList(BaseListName.listaProvince, false);
			view.setBaseList(BaseListName.listaUtenti, false);
			view.setBaseList(BaseListName.listaEnti, false);
			
			System.out.println(request.getAttribute("elaborazioni_codiceSocieta"));
			System.out.println(request.getAttribute("elaborazioni_searchuserCode"));
			System.out.println(request.getAttribute("elaborazioni_searchchiaveEnte"));

			if (!StringUtility.isEmpty(view.getFiredButtonCerca())) {
				// we retry listaElabOttico
 				String userCode = view.getUserCode();
				String chiaveEnte = view.getChiaveEnte();
				if (userCode != null && (userCode.length()>0))
					userCode = userCode.substring(5);
				if (chiaveEnte != null && (chiaveEnte.length()>0))
					chiaveEnte = chiaveEnte.substring(10);
				// PG22XX09_YL5
				RicercaListaElabOtticoResponse response = WSCache.managerOtticoServer.ricercaListaElabOttico(
						new RicercaListaElabOtticoRequest(view.getCodiceSocieta(), userCode, chiaveEnte,						
								view.getSiglaProvincia(), view.getDataElaborazioneDa(), view.getDataElaborazioneA(), 
								view.getDataCreazioneDa(), view.getDataCreazioneA(), 
								view.getTipologiaFlusso(),view.getStatoFlusso(), view.getRowsPerPage(), view.getPageNumber(), view.getOrder()), request);
				ListResponseTypePageInfo responsePageInfo = response.getListaOttico().getPageInfo();
				PageInfo pageInfo = new PageInfo(); 
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(view.getRowsPerPage());
				
				if (pageInfo.getNumRows() > 0)
					view = view.setListaOttico(response.getListaOttico().getListXml()).setListaOtticoPageInfo(pageInfo);
				else
					setFormMessage("form_ricerca", Messages.NO_DATA_FOUND.format(), request);
			}
			
		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}
}