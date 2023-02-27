package org.seda.payer.manager.configurazione.actions;

import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.configurazione.views.ParametriOttivoView;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.views.BaseView.BaseListName;
import org.seda.payer.manager.ws.WSCache;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.CreaParametriOtticoRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.DettaglioParametriOtticoRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.DettaglioParametriOtticoResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.EliminaParametriOtticoRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.ListResponseTypePageInfo;
import com.seda.payer.ottico.webservice.configurazione.dati.ListaParametriOtticoRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.ListaParametriOtticoResponse;
import com.seda.payer.ottico.webservice.configurazione.dati.ModificaParametriOtticoRequest;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
/**
 * @author marco.montisano
 */
public class ParametriOtticoAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private ParametriOttivoView view;
	private boolean bDdlCascade = false;
	
	@Override
	public void start(HttpServletRequest request) {
		view = new ParametriOttivoView(context, request);
		if (view.getFiredButton() != null) {
			if (view.getFiredButton().equals(ParametriOttivoView.NewButton)) {
				try { add(request);
				} catch (ActionException e) { e.printStackTrace(); }
			}
		}
		if (view.getFiredButtonBack() != null) {
			if (view.getFiredButtonBack().equals(ParametriOttivoView.BackButton)) {
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
			view = view.populate(request, ParametriOttivoView.SearchScope);
			if (view.getFiredButton() != null)
				view.reset();

			if (view.getFiredButtonReset() != null)
				view.reset();

			if (!bDdlCascade)
			{
				view.setBaseList(BaseListName.listaSocieta, false);
				view.setBaseList(BaseListName.listaProvince, false);
				view.setBaseList(BaseListName.listaUtenti, false);
				view.setBaseList(BaseListName.listaEntiGenerici, false);
			}
			// we retry listaParametriOttico
			String userCode = view.getUserCode();
			String chiaveEnte = view.getChiaveEnte();
			if (userCode != null && (userCode.length()>0))
				userCode = userCode.substring(5);
			if (chiaveEnte != null && (chiaveEnte.length()>0))
				chiaveEnte = chiaveEnte.substring(10);
			ListaParametriOtticoResponse response = WSCache.configurazioneServer.listaParametriOttico(
					new ListaParametriOtticoRequest(view.getCompanyCode(), userCode, chiaveEnte, 
							view.getSiglaProvincia(), view.getSorgenteImmagini(), view.getRowsPerPage(), view.getPageNumber()), request);
			ListResponseTypePageInfo responsePageInfo = response.getListaOttico().getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(view.getRowsPerPage());
			}
			view = view.setListaOttico(response.getListaOttico().getListXml()).setListaOtticoPageInfo(pageInfo);

		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			bDdlCascade = true;
			view = view.populate(request, ParametriOttivoView.AddScope);
			view.setBaseList(BaseListName.listaSocieta, false, true, request);
			view.setBaseList(BaseListName.listaUtenti, false, true, request);
			view.setBaseList(BaseListName.listaEntiGenerici, false, true, request);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}

	public Object richiestacanc(HttpServletRequest request) {
		view = view.populate(request, ParametriOttivoView.RichiestaCancScope);

		System.out.println("richiestacanc companyCode - " + view.getCompanyCode());
		System.out.println("richiestacanc userCode - " + view.getUserCode());
		System.out.println("richiestacanc chiaveEnte - " + view.getChiaveEnte());

		return null; 
	}

	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, ParametriOttivoView.EditScope);
			// we retry detail
			DettaglioParametriOtticoResponse detailResponse = WSCache.configurazioneServer.dettaglioParametriOttico(
					new DettaglioParametriOtticoRequest(view.getCompanyCode(), view.getUserCode(), view.getChiaveEnte()), request);
			view = view.setCompanyCode(detailResponse.getCodiceSocieta())
						.setUserCode(detailResponse.getCodiceUtente())
						.setChiaveEnte(detailResponse.getCodiceEnte())
						.setDirInputFlussiDati(detailResponse.getDirectoryFlussiDatiOttico())
						.setDirOutputFlussiDati(detailResponse.getDirectoryFlussiDatiOtticoOld())
						.setDirInputFlussiImmagini(detailResponse.getDirectoryFlussiImmaginiOttico())
						.setDirOutputFlussiImmagini(detailResponse.getDirectoryFlussiImmaginiOtticoOld())
						.setDirImmagini(detailResponse.getDirectoryImmaginiOttico())
						.setDirLogFlussi(detailResponse.getDirectoryLogOtticoOld())
						.setBollettino(detailResponse.getFlagBollettino())
						.setDocumento(detailResponse.getFlagDocumento())
						.setQuietanza(detailResponse.getFlagQuietanza())
						.setRelata(detailResponse.getFlagRelata())
						.setSorgenteImmagini(detailResponse.getFlagWebServiceOttico())
						.setServer(detailResponse.getIndirizzoWebServiceOttico())
						.setPasswordServer(detailResponse.getPasswordWebServiceOttico())
						.setUserServer(detailResponse.getUserWebServiceOttico())
						.setEmailAmministratore(detailResponse.getEmailAmministratoreOttico());
			// we retry base list
			view.setBaseList(BaseListName.listaSocieta, false);
			view.setBaseList(BaseListName.listaUtenti, false);
			view.setBaseList(BaseListName.listaEntiGenerici, false);
		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}

	public Object saveadd(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, ParametriOttivoView.SaveAddScope);
			view.setMessage(Messages.INS_OK.format());
			if (view.getFiredButton() != null) {
				if (view.getFiredButton().equals(ParametriOttivoView.BackButton)) {
					view.reset();
					index(request);					
				}
			} else if (view.getFiredButtonReset() != null) {
				if (view.getFiredButtonReset().equals(ParametriOttivoView.ResetButton)) {
					view.reset();
					view.setScope(ParametriOttivoView.AddScope);
					add(request);
				}
			} else if (view.isBaseListChanged()) {
				view.setScope(ParametriOttivoView.AddScope);
				add(request);
			} else 
				save(request);
			

		} catch (Exception ignore) { }
		return null;
	}

	public Object saveedit(HttpServletRequest request) throws ActionException {
		try {
			view = view.populate(request, ParametriOttivoView.SaveEditScope);
			view.setMessage(Messages.UPDT_OK.format());
			if (view.getFiredButton() != null) {
				if (view.getFiredButton().equals(ParametriOttivoView.BackButton)) {
					view.reset();
					index(request);					
				}
			} else if (view.getFiredButtonReset() != null) {
				if (view.getFiredButtonReset().equals(ParametriOttivoView.ResetButton)) {
					view.setScope(ParametriOttivoView.EditScope);
					edit(request);					
				}
			} else if (view.isBaseListChanged()) {
				view.setScope(ParametriOttivoView.EditScope);
				edit(request);
			} else 
				save(request);
		} catch (Exception ignore) { }
		return null;
	}

	private void save(HttpServletRequest request) throws Exception {
		bDdlCascade = false;
		view = view.populate(request, ParametriOttivoView.SaveScope);
		//view.setSuccess(view.getTypeRequest());
		String userCode = view.getUserCode().substring(5);
		String chiaveEnte = view.getChiaveEnte().substring(10);
		String esito = checkInputFields();
		if (esito.equals(""))
		{
			view.setSuccess(view.getTypeRequest());
			try {
				if (view.getTypeRequest().compareTo(TypeRequest.ADD_SCOPE.scope()) == 0) {
					
					System.out.println("companyCode - " + view.getCompanyCode());
					System.out.println("userCode - " + userCode);
					System.out.println("chiaveEnte - " + chiaveEnte);
					System.out.println("documento - " + view.getDocumento());
					System.out.println("relata - " + view.getRelata());
					System.out.println("bollettino - " + view.getBollettino());
					System.out.println("quietanza - " + view.getQuietanza());
					
					/* we prepare object for save */
					WSCache.configurazioneServer.creaParametriOttico(
							new CreaParametriOtticoRequest(view.getCompanyCode(), userCode, chiaveEnte, view.getDocumento(), 
									view.getRelata(), view.getBollettino(), view.getQuietanza(), view.getSorgenteImmagini(), view.getServer(), 
									view.getUserServer(), view.getPasswordServer(), 
									view.getEmailAmministratore(), // view.getUser().getEmailNotifiche(), 
									view.getDirInputFlussiDati(), 
									view.getDirOutputFlussiDati(), view.getDirInputFlussiImmagini(), view.getDirOutputFlussiImmagini(), 
									view.getDirImmagini(), view.getDirLogFlussi(), Calendar.getInstance(), view.getCodiceOperatore()), request);
				} else {
	
					System.out.println("mod companyCode - " + view.getCompanyCode());
					System.out.println("mod userCode - " + view.getUserCode());
					System.out.println("mod chiaveEnte - " + view.getChiaveEnte());
					System.out.println("mod documento - " + view.getDocumento());
					System.out.println("mod relata - " + view.getRelata());
					System.out.println("mod bollettino - " + view.getBollettino());
					System.out.println("mod quietanza - " + view.getQuietanza());
					
					/* we prepare object for save */
					WSCache.configurazioneServer.modificaParametriOttico(
							new ModificaParametriOtticoRequest(view.getCompanyCode(), view.getUserCode(), view.getChiaveEnte(), view.getDocumento(), 
									view.getRelata(), view.getBollettino(), view.getQuietanza(), view.getSorgenteImmagini(), view.getServer(), 
									view.getUserServer(), view.getPasswordServer(), 
									view.getEmailAmministratore(), //view.getUser().getEmailNotifiche(), 
									view.getDirInputFlussiDati(), 
									view.getDirOutputFlussiDati(), view.getDirInputFlussiImmagini(), view.getDirOutputFlussiImmagini(), 
									view.getDirImmagini(), view.getDirLogFlussi(), Calendar.getInstance(), view.getCodiceOperatore()), request);
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
		else
		{
			setFormMessage("frmAction", esito, request);
			
			view.setCodiceSocieta(view.getCompanyCode());
			view.setCodiceUtente(userCode);
			if (view.getTypeRequest().compareTo(TypeRequest.ADD_SCOPE.scope()) == 0)
			{			
				view.setBaseList(BaseListName.listaSocieta, false, true, request);
				view.setBaseList(BaseListName.listaUtenti, false, true, request);
				view.setBaseList(BaseListName.listaEntiGenerici, false, true, request);
			}
			else
			{
				view.setBaseList(BaseListName.listaSocieta, false);
				view.setBaseList(BaseListName.listaUtenti, false);
				view.setBaseList(BaseListName.listaEntiGenerici, false);
			}
		}
	}

	private String checkInputFields()
	{
		String esito = "";
		if (view.getSorgenteImmagini().equals("P"))
		{
			//accesso Payer
			if (view.getServer().length() > 0 || view.getUserServer().length() > 0 || view.getPasswordServer().length() > 0)
				esito = Messages.OTTICO_CONTROLLO_URL.format();
		}
		else
		{
			//accesso esterno
			if (view.getServer().length() == 0)
				esito = Messages.OTTICO_CONTROLLO_URL_EMPTY.format();
		}
		
		return esito;
	}
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		view = view.populate(request, ParametriOttivoView.CancelScope);
		view.setActionName(ParametriOttivoView.ActionName);
		try {
//			String userCode = view.getUserCode().substring(5);
//			String chiaveEnte = view.getChiaveEnte().substring(10);

			System.out.println("cancel companyCode - " + view.getCompanyCode());
			System.out.println("cancel userCode - " + view.getUserCode());
			System.out.println("cancel chiaveEnte - " + view.getChiaveEnte());

			/* we cancel object */
			WSCache.configurazioneServer.eliminaParametriOttico(
					new EliminaParametriOtticoRequest(view.getCompanyCode(), view.getUserCode(), view.getChiaveEnte()), request);
			view.setMessage(Messages.CANC_OK.format());
		} catch (FaultType f) { view.setMessage(Messages.valueOf("ERR_" + f.getCode()).format());
		} catch (Exception e) { view.setMessage(Messages.CANCEL_ERRDIP.format()); }
		return null;
	}
}