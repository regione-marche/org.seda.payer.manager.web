package org.seda.payer.manager.configurazione.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.configurazione.views.GruppoView;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.configgruppo.dati.ConfigGruppoCreaRequest;
import com.seda.payer.pgec.webservice.configgruppo.dati.ConfigGruppoDettaglioRequest;
import com.seda.payer.pgec.webservice.configgruppo.dati.ConfigGruppoDettaglioResponse;
import com.seda.payer.pgec.webservice.configgruppo.dati.ConfigGruppoEliminaRequest;
import com.seda.payer.pgec.webservice.configgruppo.dati.ConfigGruppoListaRequest;
import com.seda.payer.pgec.webservice.configgruppo.dati.ConfigGruppoListaResponse;
import com.seda.payer.pgec.webservice.configgruppo.dati.ConfigGruppoModificaRequest;
import com.seda.payer.pgec.webservice.configgruppo.dati.ListResponseTypePageInfo;

public class GruppoAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private GruppoView view;

	@Override
	public void start(HttpServletRequest request) {
		view = new GruppoView(context, request);
		if (view.getFiredButton() != null) {
			if (view.getFiredButton().equals(GruppoView.NewButton)) {
				try { add(request);
				} catch (ActionException e) { e.printStackTrace(); }
			}
		}
		if (view.getFiredButtonBack() != null) {
			if (view.getFiredButtonBack().equals(GruppoView.BackButton)) {
				try { index(request);
				} catch (ActionException e) { e.printStackTrace(); }
			}
		}
	}

	@Override
	public Object index(HttpServletRequest request) throws ActionException {
		search(request);
		return null;
	}
	

	public Object search(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, GruppoView.SearchScope);
			if (view.getFiredButton() != null)
				view.reset();
			if (view.getFiredButtonReset() != null)
				view.reset();
			ConfigGruppoListaResponse response = WSCache.configGruppoServer.lista(
					new ConfigGruppoListaRequest(
							view.getCodiceGruppo(),
					        view.getDescrizioneLinguaItaliana(),
					        view.getDescrizioneAltraLingua(),
					        view.getRowsPerPage(),
					        view.getPageNumber(),
							view.getOrder()),
					        request);
			ListResponseTypePageInfo responsePageInfo = response.getListaGruppo().getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(view.getRowsPerPage());
			}
			view = view.setListaGruppo(response.getListaGruppo().getListXml()).setListaGruppoPageInfo(pageInfo);

		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, GruppoView.AddScope);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}

	public Object richiestacanc(HttpServletRequest request) {
		view = view.populate(request, GruppoView.RichiestaCancScope);
		return null; 
	}

	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, GruppoView.EditScope);
			view(request);
		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}

	public Object view(HttpServletRequest request) throws ActionException {
		ConfigGruppoDettaglioResponse res = null;
		try {
			ConfigGruppoDettaglioRequest in = new ConfigGruppoDettaglioRequest(view.getChiaveGruppo());
			res = WSCache.configGruppoServer.dettaglio(in , request);
			view = view.setChiaveGruppo(res.getChiaveGruppo())
				   .setCodiceGruppo(res.getCodiceGruppo())
				   .setDescrizioneLinguaItaliana(res.getDescrizioneLinguaItaliana())
				   .setDescrizioneAltraLingua(res.getDescrizioneAltraLingua());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object saveadd(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, GruppoView.SaveAddScope);
			view.setMessage(Messages.INS_OK.format());
			if (view.getFiredButton() != null) {
				if (view.getFiredButton().equals(GruppoView.BackButton)) {
					view.reset();
					index(request);					
				}
			} else if (view.getFiredButtonReset() != null) {
				if (view.getFiredButtonReset().equals(GruppoView.ResetButton)) {
					view.reset();
					view.setScope(GruppoView.AddScope);
					add(request);
				}
			} else if (view.isBaseListChanged()) {
				view.setScope(GruppoView.AddScope);
				add(request);
			} else {
				if (!view.validate()) {
					view.setScope(GruppoView.AddScope);
					add(request);
				}
				else save(request);
			}
		} catch (Exception ignore) { }
		return null;
	}

	public Object saveedit(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, GruppoView.SaveEditScope);
			view.setMessage(Messages.UPDT_OK.format());
			if (view.getFiredButton() != null) {
				if (view.getFiredButton().equals(GruppoView.BackButton)) {
					view.reset();
					index(request);					
				}
			} else if (view.getFiredButtonReset() != null) {
				if (view.getFiredButtonReset().equals(GruppoView.ResetButton)) {
					view.setScope(GruppoView.EditScope);
					edit(request);					
				}
			} else if (view.isBaseListChanged()) {
				view.setScope(GruppoView.EditScope);
				view = view.populate(request, GruppoView.BaseListScope);
			} else {
				if (!view.validate()) {
					view.setScope(GruppoView.EditScope);
					edit(request);
				}
				else save(request);
			}
		} catch (Exception ignore) { }
		return null;
	}

	private void save(HttpServletRequest request) throws ActionException {
		view = view.populate(request, GruppoView.SaveScope);
		view.setSuccess(view.getTypeRequest());
		try {
			if (view.getTypeRequest().compareTo(TypeRequest.ADD_SCOPE.scope()) == 0) {
				ConfigGruppoCreaRequest in = new ConfigGruppoCreaRequest();
				in.setCodiceGruppo(view.getCodiceGruppo());
				in.setDescrizioneLinguaItaliana(view.getDescrizioneLinguaItaliana());
				in.setDescrizioneAltraLingua(view.getDescrizioneAltraLingua());
				in.setOperatoreUltimoAggiornamento(view.getCodiceOperatore());
				WSCache.configGruppoServer.crea(in, request);
			} else {
				ConfigGruppoModificaRequest in = new ConfigGruppoModificaRequest();
				in.setChiaveGruppo(view.getChiaveGruppo());
				in.setCodiceGruppo(view.getCodiceGruppo());
				in.setDescrizioneLinguaItaliana(view.getDescrizioneLinguaItaliana());
				in.setDescrizioneAltraLingua(view.getDescrizioneAltraLingua());
				in.setOperatoreUltimoAggiornamento(view.getCodiceOperatore());
				WSCache.configGruppoServer.modifica(in, request);
			}
		} catch (Exception e) {
			if (view.getTypeRequest().compareTo(TypeRequest.ADD_SCOPE.scope()) == 0) 
				view.setMessage(Messages.INS_ERRD.format());
			else if (view.getTypeRequest().compareTo(TypeRequest.EDIT_SCOPE.scope()) == 0) 
				view.setMessage(Messages.UPDT_ERR.format());

			view.setError(true);
			System.out.println(e.getMessage());
		}
	}

	public Object cancel(HttpServletRequest request) throws ActionException {
		view = view.populate(request, GruppoView.CancelScope);
		view.setActionName(GruppoView.ActionName);
		try {
			WSCache.configGruppoServer.elimina(new ConfigGruppoEliminaRequest(view.getChiaveGruppo()), request);
			view.setMessage(Messages.CANC_OK.format());
		} catch (FaultType f) { view.setMessage(Messages.valueOf("ERR_" + f.getCode()).format());
		} catch (Exception e) { view.setMessage(Messages.CANCEL_ERRDIP.format()); }
		return null;
	}
}