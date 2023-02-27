package org.seda.payer.manager.configurazione.actions;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.configurazione.views.TemplateReportDinamiciView;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.views.BaseView.BaseListName;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.CreaAssociaTemplateRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.DettaglioAssociaTemplateRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.DettaglioAssociaTemplateResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.EliminaAssociaTemplateRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.ListResponseTypePageInfo;
import com.seda.payer.ottico.webservice.configurazione.dati.ListaAssociaTemplateRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.ListaAssociaTemplateResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.ModificaAssociaTemplateRequest;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
/**
 * @author marco.montisano
 */
public class TemplateReportDinamiciAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private TemplateReportDinamiciView view;
	private boolean bDdlCascade = false;

	@Override
	public void start(HttpServletRequest request) {
		view = new TemplateReportDinamiciView(context, request);
		if (view.getFiredButton() != null) {
			if (view.getFiredButton().equals(TemplateReportDinamiciView.NewButton)) {
				try { add(request);
				} catch (ActionException e) { e.printStackTrace(); }
			}
		}
		if (view.getFiredButtonBack() != null) {
			if (view.getFiredButtonBack().equals(TemplateReportDinamiciView.BackButton)) {
				try { index(request);
				} catch (ActionException e) { e.printStackTrace(); }
			}
		}
	}

	@Override
	public Object index(HttpServletRequest request) throws ActionException {
		bDdlCascade = false;
		search(request);
		return null;
	}
	

	public Object search(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, TemplateReportDinamiciView.SearchScope);
			if (view.getFiredButton() != null)
				view.reset();

			if (view.getFiredButtonReset() != null)
				view.reset();

			if (!bDdlCascade)
			{
				view.setBaseList(BaseListName.listaSocieta, false);
				view.setBaseList(BaseListName.listaTipologiaServizi, false);
				view.setBaseList(BaseListName.listaProvince, false);
				view.setBaseList(BaseListName.listaUtenti, false);
				view.setBaseList(BaseListName.listaEnti, false);
			}
			// we retry listaAssociaTemplate
			String userCode = view.getUserCode();
			String chiaveEnte = view.getChiaveEnte();
			if (userCode != null && (userCode.length()>0))
				userCode = userCode.substring(5);
			if (chiaveEnte != null && (chiaveEnte.length()>0))
				chiaveEnte = chiaveEnte.substring(10);
			ListaAssociaTemplateResponse response = WSCache.configurazioneServer.listaAssociaTemplate(
					new ListaAssociaTemplateRequest(view.getCompanyCode(), userCode, chiaveEnte, 
							view.getSiglaProvincia(), view.getDataDa(), view.getDataA(), view.getTipologiaServizio(), view.getTipoDocumento(), 
							view.getRowsPerPage(), view.getPageNumber()), request);
			ListResponseTypePageInfo responsePageInfo = response.getListaTemplate().getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(view.getRowsPerPage());
			}
			view = view.setListaTemplate(response.getListaTemplate().getListXml()).setListaTemplatePageInfo(pageInfo);

		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			bDdlCascade = true;
			view = view.populate(request, TemplateReportDinamiciView.AddScope);
			view.setBaseList(BaseListName.listaSocieta, false, true, request);
			view.setBaseList(BaseListName.listaTipologiaServizi, false, true, request);
			view.setBaseList(BaseListName.listaUtenti, false, true, request);
			view.setBaseList(BaseListName.listaEnti, false, true, request);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}

	public Object richiestacanc(HttpServletRequest request) {
		view = view.populate(request, TemplateReportDinamiciView.RichiestaCancScope);
		return null; 
	}

	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			
			view = view.populate(request, TemplateReportDinamiciView.EditScope);
			// we retry detail
			view(request);
			bDdlCascade = true;
			// we retry base list
			changeBaseList(request);

		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}

	public Object view(HttpServletRequest request) throws ActionException {
		try {
			// we retry detail
			DettaglioAssociaTemplateResponse detailResponse = WSCache.configurazioneServer.dettaglioAssociaTemplate(
					new DettaglioAssociaTemplateRequest(view.getChiaveTemplate()), request);
			view = view.setCompanyCode(detailResponse.getCodiceSocieta())
						.setUserCode(detailResponse.getCodiceSocieta() + detailResponse.getCodiceUtente())
						.setChiaveEnte(detailResponse.getCodiceSocieta() + detailResponse.getCodiceUtente() + detailResponse.getCodiceEnte())
						.setTipologiaServizio(detailResponse.getTipologiaServizio())
						.setTipoBollettino(detailResponse.getTipoBollettino())
						.setTipoDocumento(detailResponse.getTipoDocumento())
						.setRiferimentoTemplate(detailResponse.getRiferimentoTemplate());
			view.setDataDa(detailResponse.getDataInizio());
			view.setDataA(detailResponse.getDataFine());			
			view.setCodiceSocieta(detailResponse.getCodiceSocieta());
			view.setCodiceUtente(detailResponse.getCodiceUtente());

		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}
	
	public Object changeBaseList(HttpServletRequest request) throws ActionException {
		try {
			view.setBaseList(BaseListName.listaSocieta, false, true, request);
			view.setBaseList(BaseListName.listaTipologiaServizi, false, true, request);
			view.setBaseList(BaseListName.listaUtenti, false, true, request);
			view.setBaseList(BaseListName.listaEnti, false, true, request);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
	public Object saveadd(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, TemplateReportDinamiciView.SaveAddScope);
			view.setMessage(Messages.INS_OK.format());
			if (view.getFiredButton() != null) {
				if (view.getFiredButton().equals(TemplateReportDinamiciView.BackButton)) {
					view.reset();
					index(request);					
				}
			} else if (view.getFiredButtonReset() != null) {
				if (view.getFiredButtonReset().equals(TemplateReportDinamiciView.ResetButton)) {
					view.reset();
					view.setScope(TemplateReportDinamiciView.AddScope);
					add(request);
				}
			} else if (view.isBaseListChanged()) {
				view.setScope(TemplateReportDinamiciView.AddScope);
				add(request);
			} else {
				if (!view.validate()) {
					view.setScope(TemplateReportDinamiciView.AddScope);
					add(request);
				}
				// we save
				else save(request);
			}
		} catch (Exception ignore) { }
		return null;
	}

	public Object saveedit(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, TemplateReportDinamiciView.SaveEditScope);
			view.setMessage(Messages.UPDT_OK.format());
			if (view.getFiredButton() != null) {
				if (view.getFiredButton().equals(TemplateReportDinamiciView.BackButton)) {
					view.reset();
					index(request);					
				}
			} else if (view.getFiredButtonReset() != null) {
				if (view.getFiredButtonReset().equals(TemplateReportDinamiciView.ResetButton)) {
					view.setScope(TemplateReportDinamiciView.EditScope);
					edit(request);					
				}
			} else if (view.isBaseListChanged()) {
				view.setScope(TemplateReportDinamiciView.EditScope);
				view = view.populate(request, TemplateReportDinamiciView.BaseListScope);
				changeBaseList(request);
			} else {
				if (!view.validate()) {
					view.setScope(TemplateReportDinamiciView.EditScope);
					edit(request);
				}
				// we save
				else save(request);
			}
		} catch (Exception ignore) { }
		return null;
	}

	private void save(HttpServletRequest request) throws ActionException {
		bDdlCascade = false;
		view = view.populate(request, TemplateReportDinamiciView.SaveScope);
		view.setSuccess(view.getTypeRequest());
		try {
			if (view.getTypeRequest().compareTo(TypeRequest.ADD_SCOPE.scope()) == 0)
				/* we prepare object for save */
				WSCache.configurazioneServer.creaAssociaTemplate(
						new CreaAssociaTemplateRequest(view.getCompanyCode(), view.getUserCode(), view.getChiaveEnte(), 
								view.getTipoBollettino(), view.getDataDa(), view.getDataA(), view.getTipologiaServizio(), 
								view.getTipoDocumento(), view.getRiferimentoTemplate(), Calendar.getInstance(), view.getCodiceOperatore()), request);
			else {
				/* we prepare object for save */
				WSCache.configurazioneServer.modificaAssociaTemplate(
						new ModificaAssociaTemplateRequest(view.getChiaveTemplate(), view.getCompanyCode(), view.getUserCode(), view.getChiaveEnte(), 
								view.getTipoBollettino(), view.getDataDa(), view.getDataA(), view.getTipologiaServizio(), 
								view.getTipoDocumento(), view.getRiferimentoTemplate(), Calendar.getInstance(), view.getCodiceOperatore()), request);
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
		view = view.populate(request, TemplateReportDinamiciView.CancelScope);
		view.setActionName(TemplateReportDinamiciView.ActionName);
		try {
			/* we cancel object */
			WSCache.configurazioneServer.eliminaAssociaTemplate(new EliminaAssociaTemplateRequest(view.getChiaveTemplate()), request);
			view.setMessage(Messages.CANC_OK.format());
		} catch (FaultType f) { view.setMessage(Messages.valueOf("ERR_" + f.getCode()).format());
		} catch (Exception e) { view.setMessage(Messages.CANCEL_ERRDIP.format()); }
		return null;
	}
}