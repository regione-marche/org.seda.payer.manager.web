package org.seda.payer.manager.configurazione.actions;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.configurazione.views.TassonomiaView;
import org.seda.payer.manager.util.Messages;
//import org.seda.payer.manager.views.BaseView.BaseListName;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaCreaRequest;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaDettaglioRequest;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaDettaglioResponse;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaEliminaRequest;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaListaRequest;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaListaResponse;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaModificaRequest;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ListResponseTypePageInfo;

public class TassonomiaAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private TassonomiaView view;
	//private boolean bDdlCascade = false;

	@Override
	public void start(HttpServletRequest request) {
		view = new TassonomiaView(context, request);
		if (view.getFiredButton() != null) {
			if (view.getFiredButton().equals(TassonomiaView.NewButton)) {
				try { add(request);
				} catch (ActionException e) { e.printStackTrace(); }
			}
		}
		if (view.getFiredButtonBack() != null) {
			if (view.getFiredButtonBack().equals(TassonomiaView.BackButton)) {
				try { index(request);
				} catch (ActionException e) { e.printStackTrace(); }
			}
		}
	}

	@Override
	public Object index(HttpServletRequest request) throws ActionException {
		//bDdlCascade = false;
		search(request);
		return null;
	}
	

	public Object search(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, TassonomiaView.SearchScope);
			if (view.getFiredButton() != null)
				view.reset();

			if (view.getFiredButtonReset() != null)
				view.reset();
			
			/*
			if (!bDdlCascade)
			{
				view.setBaseList(BaseListName.listaSocieta, false);
				view.setBaseList(BaseListName.listaTipologiaServizi, false);
				view.setBaseList(BaseListName.listaProvince, false);
				view.setBaseList(BaseListName.listaUtenti, false);
				view.setBaseList(BaseListName.listaEnti, false);
			}
			*/
			String dtDa = null;
			String dtA = null;
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			if(view.getDataDa() != null) {
				dtDa = format1.format(view.getDataDa().getTime());
			}
			if(view.getDataA() != null) {
				dtA = format1.format(view.getDataA().getTime());
			}
			ConfigTassonomiaListaResponse response = WSCache.configTassonomiaServer.lista(
					new ConfigTassonomiaListaRequest(
							view.getCodiceTipoEnteCreditore(),
							view.getTipoEnteCreditore(),
					        view.getProgressivoMacroAreaPerEnteCreditore(),
					        view.getNomeMacroArea(),
					        view.getDescrizioneMacroArea(),
					        view.getCodiceTipologiaServizio(),
					        view.getTipoServizio(),
					        view.getDescrizioneServizio(),
					        view.getMotivoGiuridicoDellaRiscossione(),
					        view.getVersioneTassonomia(),
					        dtDa, //DataInizioValidita_da,
					        dtA, //DataFineValidita_a,
					        view.getRowsPerPage(),
					        view.getPageNumber(),
							view.getOrder()),
					        request);
			ListResponseTypePageInfo responsePageInfo = response.getListaTassonomia().getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(view.getRowsPerPage());
			}
			view = view.setListaTassonomia(response.getListaTassonomia().getListXml()).setListaTassonomiaPageInfo(pageInfo);

		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			//bDdlCascade = true;
			view = view.populate(request, TassonomiaView.AddScope);
			/*
			view.setBaseList(BaseListName.listaSocieta, false, true, request);
			view.setBaseList(BaseListName.listaTipologiaServizi, false, true, request);
			view.setBaseList(BaseListName.listaUtenti, false, true, request);
			view.setBaseList(BaseListName.listaEnti, false, true, request);
			*/
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}

	public Object richiestacanc(HttpServletRequest request) {
		view = view.populate(request, TassonomiaView.RichiestaCancScope);
		return null; 
	}

	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, TassonomiaView.EditScope);
			// we retry detail
			view(request);
			/*
			bDdlCascade = true;
			// we retry base list
			changeBaseList(request);
		    */

		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}

	public Object view(HttpServletRequest request) throws ActionException {
		ConfigTassonomiaDettaglioResponse res = null;
		try {
			ConfigTassonomiaDettaglioRequest in = new ConfigTassonomiaDettaglioRequest(view.getChiaveTassonomia());
			res = WSCache.configTassonomiaServer.dettaglio(in , request);
			view = view.setChiaveTassonomia(res.getChiaveTassonomia())
				   .setCodiceTipoEnteCreditore(res.getCodiceTipoEnteCreditore())
				   .setCodiceTipologiaServizio(res.getCodiceTipologiaServizio())
				   .setDatiSpecificiIncasso(res.getDatiSpecificiIncasso())
				   .setDescrizioneMacroArea(res.getDescrizioneMacroArea())
				   .setDescrizioneServizio(res.getDescrizioneServizio())
				   .setMotivoGiuridicoDellaRiscossione(res.getMotivoGiuridicoDellaRiscossione())
				   .setNomeMacroArea(res.getNomeMacroArea())
				   .setProgressivoMacroAreaPerEnteCreditore(res.getProgressivoMacroAreaPerEnteCreditore())
				   .setTipoEnteCreditore(res.getTipoEnteCreditore())
				   .setTipoServizio(res.getTipoServizio())
				   .setVersioneTassonomia(res.getVersioneTassonomia());
			view.setDataDa(res.getDataInizioValidita());
			view.setDataA(res.getDataFineValidita());			
						   
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
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
	*/
	
	public Object saveadd(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, TassonomiaView.SaveAddScope);
			view.setMessage(Messages.INS_OK.format());
			if (view.getFiredButton() != null) {
				if (view.getFiredButton().equals(TassonomiaView.BackButton)) {
					view.reset();
					index(request);					
				}
			} else if (view.getFiredButtonReset() != null) {
				if (view.getFiredButtonReset().equals(TassonomiaView.ResetButton)) {
					view.reset();
					view.setScope(TassonomiaView.AddScope);
					add(request);
				}
			} else if (view.isBaseListChanged()) {
				view.setScope(TassonomiaView.AddScope);
				add(request);
			} else {
				if (!view.validate()) {
					view.setScope(TassonomiaView.AddScope);
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
			view = view.populate(request, TassonomiaView.SaveEditScope);
			view.setMessage(Messages.UPDT_OK.format());
			if (view.getFiredButton() != null) {
				if (view.getFiredButton().equals(TassonomiaView.BackButton)) {
					view.reset();
					index(request);					
				}
			} else if (view.getFiredButtonReset() != null) {
				if (view.getFiredButtonReset().equals(TassonomiaView.ResetButton)) {
					view.setScope(TassonomiaView.EditScope);
					edit(request);					
				}
			} else if (view.isBaseListChanged()) {
				view.setScope(TassonomiaView.EditScope);
				view = view.populate(request, TassonomiaView.BaseListScope);
				//changeBaseList(request);
			} else {
				if (!view.validate()) {
					view.setScope(TassonomiaView.EditScope);
					edit(request);
				}
				// we save
				else save(request);
			}
		} catch (Exception ignore) { }
		return null;
	}

	private void save(HttpServletRequest request) throws ActionException {
		//bDdlCascade = false;
		view = view.populate(request, TassonomiaView.SaveScope);
		view.setSuccess(view.getTypeRequest());
		try {
			if (view.getTypeRequest().compareTo(TypeRequest.ADD_SCOPE.scope()) == 0) {
				ConfigTassonomiaCreaRequest in = new ConfigTassonomiaCreaRequest();
				in.setCodiceTipoEnteCreditore(view.getCodiceTipoEnteCreditore());
				in.setCodiceTipologiaServizio(view.getCodiceTipologiaServizio());
				in.setDescrizioneMacroArea(view.getDescrizioneMacroArea());
				in.setDescrizioneServizio(view.getDescrizioneServizio());
				in.setMotivoGiuridicoDellaRiscossione(view.getMotivoGiuridicoDellaRiscossione());
				in.setNomeMacroArea(view.getNomeMacroArea());
				in.setProgressivoMacroAreaPerEnteCreditore(view.getProgressivoMacroAreaPerEnteCreditore());
				in.setTipoEnteCreditore(view.getTipoEnteCreditore());
				in.setTipoServizio(view.getTipoServizio());
				in.setVersioneTassonomia(view.getVersioneTassonomia());
				in.setDataInizioValidita(view.getDataDa());
				in.setDataFineValidita(view.getDataA());
				in.setOperatoreUltimoAggiornamento(view.getCodiceOperatore());
				WSCache.configTassonomiaServer.crea(in, request);
			} else {
				ConfigTassonomiaModificaRequest in = new ConfigTassonomiaModificaRequest();
				in.setChiaveTassonomia(view.getChiaveTassonomia());
				in.setCodiceTipoEnteCreditore(view.getCodiceTipoEnteCreditore());
				in.setCodiceTipologiaServizio(view.getCodiceTipologiaServizio());
				in.setDescrizioneMacroArea(view.getDescrizioneMacroArea());
				in.setDescrizioneServizio(view.getDescrizioneServizio());
				in.setMotivoGiuridicoDellaRiscossione(view.getMotivoGiuridicoDellaRiscossione());
				in.setNomeMacroArea(view.getNomeMacroArea());
				in.setProgressivoMacroAreaPerEnteCreditore(view.getProgressivoMacroAreaPerEnteCreditore());
				in.setTipoEnteCreditore(view.getTipoEnteCreditore());
				in.setTipoServizio(view.getTipoServizio());
				in.setVersioneTassonomia(view.getVersioneTassonomia());
				in.setDataInizioValidita(view.getDataDa());
				in.setDataFineValidita(view.getDataA());
				in.setOperatoreUltimoAggiornamento(view.getCodiceOperatore());
				WSCache.configTassonomiaServer.modifica(in, request);
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
		view = view.populate(request, TassonomiaView.CancelScope);
		view.setActionName(TassonomiaView.ActionName);
		try {
			WSCache.configTassonomiaServer.elimina(new ConfigTassonomiaEliminaRequest(view.getChiaveTassonomia()), request);
			view.setMessage(Messages.CANC_OK.format());
		} catch (FaultType f) { view.setMessage(Messages.valueOf("ERR_" + f.getCode()).format());
		} catch (Exception e) { view.setMessage(Messages.CANCEL_ERRDIP.format()); }
		return null;
	}
}