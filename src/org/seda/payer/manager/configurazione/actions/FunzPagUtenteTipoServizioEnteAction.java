package org.seda.payer.manager.configurazione.actions;


import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.FunzPagamentoConf;
import org.seda.payer.manager.util.FunzPagamentoMap;
import org.seda.payer.manager.util.FunzioniPagamento;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.string.Convert;
import com.seda.commons.validator.Validation;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.components.validation.ValidationContext;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.funzpagtpserv.dati.FunzPagTpServ;
import com.seda.payer.pgec.webservice.funzpagtpservente.dati.FunzPagTpServEnte;
import com.seda.payer.pgec.webservice.funzpagtpservente.dati.FunzPagTpServEnteCancelAggregatoRequest;
import com.seda.payer.pgec.webservice.funzpagtpservente.dati.FunzPagTpServEnteDetailAggregatoRequest;
import com.seda.payer.pgec.webservice.funzpagtpservente.dati.FunzPagTpServEnteDetailAggregatoResponse;
import com.seda.payer.pgec.webservice.funzpagtpservente.dati.FunzPagTpServEnteResponse;
import com.seda.payer.pgec.webservice.funzpagtpservente.dati.FunzPagTpServEnteResponsePageInfo;
import com.seda.payer.pgec.webservice.funzpagtpservente.dati.FunzPagTpServEnteSaveAggregatoRequest;
import com.seda.payer.pgec.webservice.funzpagtpservente.dati.FunzPagTpServEnteSearchAggregatoRequest;
import com.seda.payer.pgec.webservice.funzpagtpservente.dati.FunzPagTpServEnteSearchAggregatoResponse;
import com.seda.payer.pgec.webservice.funzpagtpservente.dati.StatusResponse;
import com.sun.rowset.WebRowSetImpl;


public class FunzPagUtenteTipoServizioEnteAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private String usernameAutenticazione = null;
	private HttpSession session;
	
	@Override
	public void start(HttpServletRequest request) {
		rowsPerPage = (request.getParameter("rowsPerPage") == null) 
				   ? getDefaultListRows() : Integer.parseInt(request.getParameter("rowsPerPage"));
		pageNumber = (request.getParameter("pageNumber") == null) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		order = (request.getParameter("order") == null) ? "" : request.getParameter("order");
		
		session = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		
		if( user!=null) 
		   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
		usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
		
		String firedButton = (String)request.getAttribute("tx_button_nuovo");
		//System.out.println(firedButton+"index");
		if (firedButton!=null){
			if(firedButton.equals("Nuovo")){				
				try {
					add(request);
				} catch (ActionException e) {
					e.printStackTrace();
				}
			}
		}
		firedButton = (String)request.getAttribute("tx_button_indietro");
		if (firedButton!=null){
			if(firedButton.equals("Indietro")){
				try {
					index(request);
				} catch (ActionException e) {
					e.printStackTrace();
				}
			}
		}	
		
	}

	@Override
	public Object index(HttpServletRequest request) throws ActionException {
		//saveadd(request);
		search(request);
		
		return null;
	}
	
	public Object search(HttpServletRequest request) throws ActionException {
		try {
			
			String descrizioneSocieta = ((String)request.getAttribute("funzpagutentetiposervizioente_searchcompanyCode")==null ? "":  (String)request.getAttribute("funzpagutentetiposervizioente_searchcompanyCode"));
			String descrizioneUtente = ((String)request.getAttribute("funzpagutentetiposervizioente_searchuserCode")==null ? "":  (String)request.getAttribute("funzpagutentetiposervizioente_searchuserCode"));
			String descrizioneEnte = ((String)request.getAttribute("funzpagutentetiposervizioente_searchchiaveEnte")==null ? "":  (String)request.getAttribute("funzpagutentetiposervizioente_searchchiaveEnte"));
			String descrizioneTipologiaServizio = ((String)request.getAttribute("funzpagutentetiposervizioente_searchcodiceTipologiaServizio")==null ? "":  (String)request.getAttribute("funzpagutentetiposervizioente_searchcodiceTipologiaServizio"));
			
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonN = (String)request.getAttribute("tx_button_nuovo");
			if (firedButton!=null||firedButtonN!=null){
					
				request.setAttribute("funzpagutentetiposervizioente_searchcompanyCode", "");
				request.setAttribute("funzpagutentetiposervizioente_searchuserCode", "");
				request.setAttribute("funzpagutentetiposervizioente_searchcodiceTipologiaServizio","");
				request.setAttribute("funzpagutentetiposervizioente_searchchiaveEnte","");
				descrizioneSocieta="";
				descrizioneUtente="";
				descrizioneTipologiaServizio="";
				descrizioneEnte="";
				
			}
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				descrizioneSocieta="";
				descrizioneUtente="";
				descrizioneTipologiaServizio="";
				descrizioneEnte="";
			}
			
			FunzPagTpServEnteSearchAggregatoResponse searchResponse = getFunzPagTpServEnteSearchResponse(descrizioneSocieta, descrizioneUtente, descrizioneEnte, descrizioneTipologiaServizio, rowsPerPage, pageNumber, order, request);
			if (searchResponse != null && searchResponse.getResponse() != null && searchResponse.getResponse().getRetCode().equals("00"))
			{
				FunzPagTpServEnteResponse funzpagutentetiposervizioenteResponse = searchResponse.getListResponse();
				FunzPagTpServEnteResponsePageInfo responsePageInfo = funzpagutentetiposervizioenteResponse.getPageInfo();
				PageInfo pageInfo = new PageInfo(); 
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
				
				request.setAttribute("funzpagutentetiposervizioentes", funzpagutentetiposervizioenteResponse.getListXml());
				request.setAttribute("funzpagutentetiposervizioentes.pageInfo", pageInfo);
			}
			
			request.setAttribute("funzpagutentetiposervizioente_searchcompanyCode", descrizioneSocieta);
			request.setAttribute("funzpagutentetiposervizioente_searchuserCode", descrizioneUtente);
			request.setAttribute("funzpagutentetiposervizioente_searchcodiceTipologiaServizio", descrizioneTipologiaServizio);
			request.setAttribute("funzpagutentetiposervizioente_searchchiaveEnte", descrizioneEnte);
						
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			/*
			 * La DDL Società/Utente viene caricata tenendo conto della profilazione utente:
			 * "userCodiceSocieta" infatti, se l'utente è un "AMSO" ha il valore del codice Società
			 * associato all'utente
			 */ 
			
			loadSocietaUtenteServizioEnteXml_DDL(request, session);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {	  
		request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		request.setAttribute("funzpagutentetiposervizioente_companyCode",request.getParameter("funzpagutentetiposervizioente_companyCode"));
		request.setAttribute("funzpagutentetiposervizioente_userCode", request.getParameter("funzpagutentetiposervizioente_userCode"));
		request.setAttribute("funzpagutentetiposervizioente_codiceTipologiaServizio", request.getParameter("funzpagutentetiposervizioente_codiceTipologiaServizio"));
		request.setAttribute("funzpagutentetiposervizioente_chiaveEnte", request.getParameter("funzpagutentetiposervizioente_chiaveEnte"));
		return null; 
	}
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			loadSocietaUtenteServizioEnteXml_DDL(request, session);
			
			String codiceSocieta = request.getParameter("funzpagutentetiposervizioente_companyCode");		
			String codiceUtente = request.getParameter("funzpagutentetiposervizioente_userCode");
			String chiaveEnte = request.getParameter("funzpagutentetiposervizioente_chiaveEnte");
			String tipologiaServizio = request.getParameter("funzpagutentetiposervizioente_codiceTipologiaServizio");
			
			request.setAttribute("funzpagutentetiposervizioente_companyCode", codiceSocieta);
			request.setAttribute("funzpagutentetiposervizioente_userCode", codiceUtente);
			request.setAttribute("funzpagutentetiposervizioente_chiaveEnte", chiaveEnte);
			request.setAttribute("funzpagutentetiposervizioente_codiceTipologiaServizio", tipologiaServizio);
			
			//recupero il tipo bollettino relativo all'elemento selezionato
			String tipoBollettino = getTipoBollettino(request, codiceSocieta, codiceUtente, chiaveEnte, tipologiaServizio );
			request.setAttribute("funzpagutentetiposervizioente_tipoboll", tipoBollettino);
			
			loadCampiForm(request, codiceSocieta, codiceUtente, chiaveEnte, tipologiaServizio, tipoBollettino, true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object saveadd(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("message", Messages.INS_OK.format());
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			FiredButton firedButtonChanged2 = getFiredButton(request);
			if(firedButtonChanged2.equals(FiredButton.TX_BUTTON_CHANGED))
			{
				loadSocietaUtenteServizioEnteXml_DDL(request, session);
				String arrStr = request.getParameter("funzpagutentetiposervizio_strUtentetiposervizioentes");

				if (arrStr!=null && arrStr.length()>0)
				{
					  String[] strUtentetiposervizios = arrStr.split("\\|");
					  if (strUtentetiposervizios.length == 5)
					  {
						  String codiceSocieta = strUtentetiposervizios[0];
						  String codiceUtente = strUtentetiposervizios[1];
						  String chiaveEnte = strUtentetiposervizios[2];
						  String tipologiaServizio = strUtentetiposervizios[3];
						  String tipoBoll = strUtentetiposervizios[4];
						  
						  
						  //PG130040
							request.setAttribute("funzpagutentetiposervizioente_companyCode", codiceSocieta);
							request.setAttribute("funzpagutentetiposervizioente_userCode", codiceUtente);
							request.setAttribute("funzpagutentetiposervizioente_chiaveEnte", chiaveEnte);
							request.setAttribute("funzpagutentetiposervizioente_codiceTipologiaServizio", tipologiaServizio); 
							request.setAttribute("funzpagutentetiposervizioente_tipoboll", tipoBoll);
						  

						  //FINE PG130040
						  
						//controllo che non siano già stati inseriti valori per soc/ute/servizio selezionati
						  FunzPagTpServEnte[] listFunzPagTpServEnte = getListFunzPagTpServEnteDB(codiceSocieta, codiceUtente, chiaveEnte, tipologiaServizio, request);
						  if (listFunzPagTpServEnte != null && listFunzPagTpServEnte.length > 0)
							  setFormMessage("frmAction", "Valore già presente", request);
						  else
						  {
							  loadCampiForm(request, codiceSocieta, codiceUtente, chiaveEnte, tipologiaServizio, tipoBoll, false);
							  request.setAttribute("campiFormCaricati", "Y");
						  }
						  request.setAttribute("funzpagutentetiposervizio_strUtentetiposervizioentes", arrStr);
					  }
				}
			}
			else if (firedButton!=null){
				if(firedButton.equals("Indietro")){					
					index(request);					
				}
			}
			else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){					
				//	resetParametri(request);
					request.setAttribute("action", "add");
		//PG130040 CG			add(request);
					edit(request);
				}
			}else save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}
	
	public Object saveedit(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("message", Messages.UPDT_OK.format());
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){					
					index(request);					
				}
			}
			else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){
					request.setAttribute("action", "edit");
					edit(request);					
				}
			}
			else save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
			
		}
		return null;
	}
	
	
	//PG130040
	private boolean checkLunghezze(HttpServletRequest request, String tipoBollettino) throws ActionException {
		boolean result = true;
		FunzPagamentoMap funzPagamentoMap  = getElencoCampiNew(tipoBollettino);	
		StringBuffer messages = new StringBuffer();
		
		for (String chiave : funzPagamentoMap.keySet() )
		{
			FunzPagamentoConf pagamentoConf = funzPagamentoMap.get(chiave);
			String   lunghezza = getLunghezzaParameter(request, chiave, pagamentoConf);
			int lunghezzaInt = Integer.parseInt(lunghezza);
			
			if (lunghezzaInt < 0 || lunghezzaInt > Integer.parseInt( pagamentoConf.getLunghezzaMax()) ) {
				
				result = false; 
				
				messages.append( "La lunghezza per il campo " + pagamentoConf.getDescrizione() + " deve essere compresa tra 0 e " + pagamentoConf.getLunghezzaMax() + "<br/>"  );
				
			}
		} 

		setFormMessage("frmAction", messages.toString() , request);
		
		return result;
	}

	// PG130040
	
	private List<FunzPagamentoConf> valorizzaFunzPagamentoConf(HttpServletRequest request) throws ActionException {
		
		String tipoBollettino = request.getParameter("funzpagutentetiposervizioente_tipoboll");
		String[] aCampi = (String[])request.getAttribute("grCampi");
		
		FunzPagamentoMap funzPagamentoMap = null;	
		if (tipoBollettino != null && tipoBollettino.length() > 0)
			funzPagamentoMap = getElencoCampiNew(tipoBollettino);
							
		List<FunzPagamentoConf> listFunzPagamentoConf = new ArrayList<FunzPagamentoConf>();
		for (String chiave : funzPagamentoMap.keySet() )
		{
			FunzPagamentoConf pagamentoConf = funzPagamentoMap.get(chiave);
					
			FunzPagamentoConf funz = new FunzPagamentoConf();
			funz.setChiave(chiave);
			funz.setDescrizione(pagamentoConf.getDescrizione());
			funz.setObbligatorieta(checkCampoSelected(aCampi, chiave));
			funz.setTipoValore( checkTipoValore( request,chiave, pagamentoConf) );		//PG130040 GC
			funz.setTipoLunghezza( checkFlagLunghezza( request, chiave, pagamentoConf )  );//PG130040 GC
			funz.setLunghezza(Integer.parseInt( getLunghezzaParameter(request, chiave, pagamentoConf )  )); //PG130040 GC
			funz.setLunghezzaMax(pagamentoConf.getLunghezzaMax()); //PG130040 GC
			funz.setTipoDescrizioneAlternativa(checkDescrAlternativa(request,chiave, pagamentoConf )  ); //PG130040 GC
			funz.setDescrizioneAlternativa(getDescrAlternativa(request,chiave, pagamentoConf )  ); //PG130040 GC
			funz.setFlagBloccoDescrizioneAlternativaModificabile(pagamentoConf.isFlagBloccoDescrizioneAlternativaModificabile());
			funz.setFlagBloccoLunghezzaModificabile(pagamentoConf.isFlagBloccoLunghezzaModificabile());
			funz.setFlagBloccoObbligatorioModificabile(pagamentoConf.isFlagBloccoObbligatorioModificabile());
			funz.setFlagBloccoTipoValoreModificabile(pagamentoConf.isFlagBloccoTipoValoreModificabile());
			listFunzPagamentoConf.add(funz);
		}
		
		
		return listFunzPagamentoConf;
	}
	//FINE PG130040
	
	
	private void save(HttpServletRequest request) throws ActionException {
		String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		
		String codiceSocieta = request.getParameter("funzpagutentetiposervizioente_companyCode");		
		String codiceUtente = request.getParameter("funzpagutentetiposervizioente_userCode");;
		String chiaveEnte = request.getParameter("funzpagutentetiposervizioente_chiaveEnte");
		String tipologiaServizio = request.getParameter("funzpagutentetiposervizioente_codiceTipologiaServizio");;
		String tipoBollettino = request.getParameter("funzpagutentetiposervizioente_tipoboll");
		
		String arrStr = request.getParameter("funzpagutentetiposervizio_strUtentetiposervizioentes");
		
		if (codOp!=null && codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0 && arrStr!=null && arrStr.length()>0)
		{
			 String[] strUtentetiposervizios = arrStr.split("\\|");
			 codiceSocieta = strUtentetiposervizios[0];
			 codiceUtente= strUtentetiposervizios[1];
 		     chiaveEnte = strUtentetiposervizios[2];
 		     tipologiaServizio = strUtentetiposervizios[3];
 		     tipoBollettino = strUtentetiposervizios[4];
 		}
	
		
		if(checkLunghezze(request, tipoBollettino)){	//PG130040
			String[] aCampi = (String[])request.getAttribute("grCampi");
			
			//nell'array aCampi ho sempre 2 elementi fittizi denominati "hidden1" e "hidden2"
			//quindi la dimensione reale si ottiene con aCampi.length - 2
			int iSize = aCampi.length - 2;
			if (codOp!=null && codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0 && iSize == 0)
			{
				//sono in inserimento e non ho selezionato nemmeno un elemento
				setFormMessage("frmAction", "Selezionare almeno uno fra i campi obbligatori", request);
				request.setAttribute("done", null);
				loadSocietaUtenteServizioEnteXml_DDL(request, session);
				loadCampiForm(request, codiceSocieta, codiceUtente, chiaveEnte, tipologiaServizio, tipoBollettino, false);
				request.setAttribute("campiFormCaricati", "Y");
			}
			else
			{
				boolean bOk = true;
				//se sono in modifica devo prima eliminare tutti i campi obbligatori e poi reinserirli
				//se sono in inserimento li inserisco direttamente
				if (codOp!=null && codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0)
					bOk = cancel_Aggregato(codiceSocieta, codiceUtente, chiaveEnte, tipologiaServizio, request);
		
				if (bOk)
				{
					try {
						FunzPagTpServEnteSaveAggregatoRequest req = new FunzPagTpServEnteSaveAggregatoRequest();
						
						//PG130040 String[] sElencoCampi = null;
						FunzPagamentoMap funzPagamentoMap = null; //PG130040	
						if (tipoBollettino != null && tipoBollettino.length() > 0)
							funzPagamentoMap = getElencoCampiNew(tipoBollettino);
							//PG130040 sElencoCampi = FunzPagUtenteTipoServizioAction.getElencoCampi(tipoBollettino);
						
						List<FunzPagTpServEnte> listFunzPagTpServEnte = new ArrayList<FunzPagTpServEnte>();
						for (String chiave : funzPagamentoMap.keySet() )
						{
							// String[] sCampoDett = campo.split("\\|");
							FunzPagamentoConf pagamentoConf = funzPagamentoMap.get(chiave);
							
							FunzPagTpServEnte funz = new FunzPagTpServEnte();
							funz.setCompanyCode(codiceSocieta);
							funz.setUserCode(codiceUtente);
							funz.setChiaveEnte(chiaveEnte);
							funz.setCodiceTipologiaServizio(tipologiaServizio);
							funz.setNomeForm(chiave);
							funz.setLabelFormFunPagamento(pagamentoConf.getDescrizione());
							funz.setImmissioneVal(checkCampoSelected(aCampi, chiave));
							funz.setFlagTipoValore( checkTipoValore( request,chiave, pagamentoConf) );		//PG130040 GC
							funz.setFlagLunghezza( checkFlagLunghezza( request, chiave, pagamentoConf )  );//PG130040 GC
							funz.setLunghezza( getLunghezzaParameter(request, chiave, pagamentoConf )  ); //PG130040 GC
							funz.setFlagDescrAlt(checkDescrAlternativa(request,chiave, pagamentoConf )  ); //PG130040 GC
							funz.setDescrAlternativa(getDescrAlternativa(request,chiave, pagamentoConf )  ); //PG130040 GC
							funz.setOperatorCode(usernameAutenticazione);
							listFunzPagTpServEnte.add(funz);
						}
						
						req.setListFunzPagTpServEnte((FunzPagTpServEnte[])listFunzPagTpServEnte.toArray(new FunzPagTpServEnte[listFunzPagTpServEnte.size()]));
						req.setCodOp(codOp);
						
						StatusResponse res = WSCache.funzPagTpServEnteServer.save_Aggregato(req, request);
						if (res != null && res.getResponse() != null && res.getResponse().getRetCode().equals("00"))
							bOk = true;
						else
							bOk = false;
			
					} catch (Exception e) {
						bOk = false;
					}
				}
				
				if (!bOk)
				{
					if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
					if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.UPDT_ERR.format());
					request.setAttribute("error", "error"); 
				}
			}
		} else {		//PG130040
			
			request.setAttribute("done", null);
			request.setAttribute("richiestacanc", null);

			// PG130040 edit(request);
			loadSocietaUtenteServizioEnteXml_DDL(request, session);

			request.setAttribute("listaFunzPagamentoConf", valorizzaFunzPagamentoConf(request) );
		} //fine PG130040
		
		

	}
	
	//PG130040 CG 
	private String getDescrAlternativa(HttpServletRequest request, String nomeChiave, FunzPagamentoConf pagamentoConf)
	{ 
		if (request.getParameter("descrizioneAlternativa_" + nomeChiave)==null) {
			return pagamentoConf.getDescrizioneAlternativa();
		} else {
			return  request.getParameter("descrizioneAlternativa_" + nomeChiave );	
		}
	}
	
	private String checkDescrAlternativa(HttpServletRequest request, String chiave, FunzPagamentoConf pagamentoConf) 
	{
		if (request.getParameter("FlagDescrAlternativa_" + chiave )==null) {
			return pagamentoConf.getTipoDescrizioneAlternativa();
		} else {
			return  request.getParameter("FlagDescrAlternativa_" + chiave );	
		}
	}
	
	//PG130040 CG
	public static FunzPagamentoMap getElencoCampiNew(String tipoBoll)
	{
		String[] sElencoCampi = null;
		//recupero la lista di tutti i campi possibili per la tipologia bollettino selezionata
		if (tipoBoll.startsWith("ICI"))
			sElencoCampi = FunzioniPagamento.ICI.format().split(",");
		else if (tipoBoll.startsWith("ISC"))
			sElencoCampi = FunzioniPagamento.ISCOP.format().split(",");
		else if (tipoBoll.startsWith("BOL"))
			sElencoCampi = FunzioniPagamento.BOLLO.format().split(",");
		else if (tipoBoll.startsWith("PRE"))
			sElencoCampi = FunzioniPagamento.PREMARCATO.format().split(",");
		else if (tipoBoll.startsWith("FRE"))
			sElencoCampi = FunzioniPagamento.FRECCIA.format().split(",");
		else if (tipoBoll.startsWith("MAV"))
			sElencoCampi = FunzioniPagamento.MAV.format().split(",");
		else if (tipoBoll.startsWith("CDS"))
			sElencoCampi = FunzioniPagamento.CDS.format().split(",");
		else if (tipoBoll.startsWith("SPO"))
			sElencoCampi = FunzioniPagamento.SPONTANEO.format().split(",");
		
		FunzPagamentoMap mappa = new FunzPagamentoMap();
		
		for (String riga : sElencoCampi) {
			String[] dati = riga.split("\\|");
			FunzPagamentoConf conf = new FunzPagamentoConf(); 

			conf.setChiave(dati[0] ) ;
			conf.setDescrizione( dati[1] ) ;
			conf.setObbligatorieta(dati[2] ) ;
			conf.setTipoValore(dati[3] ) ;
			conf.setTipoLunghezza(dati[4] ) ;
			conf.setLunghezza(Integer.parseInt(dati[5] )) ;
			conf.setLunghezzaMax(dati[5] ) ;
			conf.setTipoDescrizioneAlternativa(dati[6] ) ;
			//conf.setDescrizioneAlternativa(dati[7] ) ;
			conf.setFlagBloccoObbligatorioModificabile(dati[8] ) ;
			conf.setFlagBloccoTipoValoreModificabile(dati[9] ) ;
			conf.setFlagBloccoLunghezzaModificabile(dati[10] ) ;
			conf.setFlagBloccoDescrizioneAlternativaModificabile(dati[11] ) ;

			mappa.put(dati[0], conf);
		}
		
		return mappa;
	}
	//PG130040 CG 
	private String checkTipoValore(HttpServletRequest request, String nomeChiave, FunzPagamentoConf pagamentoConf)
	{
		if (request.getParameter("FlagTipoValore_" + nomeChiave)==null) {
			return pagamentoConf.getTipoValore();
		} else {
			return  request.getParameter("FlagTipoValore_" + nomeChiave );	
		}
	}
	
	private String checkFlagLunghezza(HttpServletRequest request, String nomeChiave, FunzPagamentoConf pagamentoConf)
	{
		if (request.getParameter("flagLunghezza_" + nomeChiave)==null) {
			return pagamentoConf.getTipoLunghezza();
		} else {
			return  request.getParameter("flagLunghezza_" + nomeChiave );	
		}
		
	}

	private String getLunghezzaParameter(HttpServletRequest request, String nomeChiave, FunzPagamentoConf pagamentoConf)
	{ 
		if (request.getParameter("lunghezza_" + nomeChiave)==null) {
			return pagamentoConf.getLunghezzaMax();
		} else {
			return  request.getParameter("lunghezza_" + nomeChiave );	
		}
	}
	//PG130040 CG
	
	
	private String checkCampoSelected(String[] aCampi, String campoToCheck)
	{
		for (String campo : aCampi)
		{
			if (campo.equals(campoToCheck))
				return "Y";
		}
		return "N";
	}
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String codiceSocieta = request.getParameter("funzpagutentetiposervizioente_companyCode");		
		String codiceUtente = request.getParameter("funzpagutentetiposervizioente_userCode");
		String chiaveEnte = request.getParameter("funzpagutentetiposervizioente_chiaveEnte");
		String tipologiaServizio = request.getParameter("funzpagutentetiposervizioente_codiceTipologiaServizio");;
		
	    
		try {
			request.setAttribute("varname", "funzpagutentetiposervizioente");
			
			boolean bRes = cancel_Aggregato(codiceSocieta, codiceUtente, chiaveEnte, tipologiaServizio, request);
			if (bRes)
				request.setAttribute("message", Messages.CANC_OK.format());
			else
				request.setAttribute("message", Messages.CANCEL_ERR.format());
		
		} catch (Exception e) {
			request.setAttribute("message", Messages.CANCEL_ERRDIP.format());
		}
		return null;
	}	

	
	protected int getDefaultListRows()
	{
		int defaultListRows = 4;
		PropertiesTree configuration = (PropertiesTree) context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		if(configuration != null)
		{
			String s_defaultListRows = configuration.getProperty(PropertiesPath.defaultListRows.format());
			if (s_defaultListRows != null) defaultListRows = Integer.parseInt(s_defaultListRows);
		}
		return defaultListRows;
	}
	
	private FunzPagTpServEnteSearchAggregatoResponse getFunzPagTpServEnteSearchResponse(
			String descrizioneSocieta, String descrizioneUtente, String descrizioneEnte, String descrizioneTipologiaServizio, int rowsPerPage,int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		FunzPagTpServEnteSearchAggregatoRequest in = new FunzPagTpServEnteSearchAggregatoRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setDescrizioneSocieta(descrizioneSocieta == null ? "" : descrizioneSocieta);
		in.setDescrizioneUtente(descrizioneUtente  == null ? "" : descrizioneUtente);
		in.setDescrizioneEnte(descrizioneEnte == null ? "" :  descrizioneEnte);
		in.setDescrizioneTipologiaservizio(descrizioneTipologiaServizio == null ? "" :  descrizioneTipologiaServizio);
		
		return WSCache.funzPagTpServEnteServer.getListaFunzPagTpServ_Aggregato(in, request);
	}
	
	private boolean cancel_Aggregato(String codiceSocieta, String codiceUtente, String chiaveEnte, String tipologiaServizio, HttpServletRequest request)
	{
		try {
			FunzPagTpServEnteCancelAggregatoRequest req = new FunzPagTpServEnteCancelAggregatoRequest(codiceSocieta, codiceUtente, chiaveEnte, tipologiaServizio);
			StatusResponse res = WSCache.funzPagTpServEnteServer.cancel_Aggregato(req, request);
			if (res != null && res.getResponse() != null && res.getResponse().getRetCode().equals("00"))
				return true;
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return false;
	}


	private static void loadCampiForm(HttpServletRequest request, String codiceSocieta, String codiceUtente, String chiaveEnte, String tipologiaServizio, String tipoBoll, boolean bModifica) throws ActionException
	{
		// PG130040 CG - inizio
		FunzPagamentoMap sElencoCampi = new FunzPagamentoMap();
		if (tipoBoll != null && tipoBoll.length() > 0)
			sElencoCampi = getElencoCampiNew(tipoBoll);
		
		//recupero da DB la lista dei campi e il flag obbligatorio
		FunzPagTpServEnte[] listFunzPagTpServEnteDB = null; 
		if (bModifica)
			listFunzPagTpServEnteDB = getListFunzPagTpServEnteDB(codiceSocieta,codiceUtente,chiaveEnte,tipologiaServizio, request);
		
		if (sElencoCampi != null && sElencoCampi.size() > 0)
		{
			ArrayList<FunzPagamentoConf> arrayFunzPagamentoConfs = new ArrayList<FunzPagamentoConf>();
			try
			{
				for (FunzPagamentoConf funzPagamentoConf :  sElencoCampi.values())
				{	
					if (funzPagamentoConf != null) 
					{
						if (bModifica) {

							FunzPagamentoConf funzPagamentoConfDB = getFields(funzPagamentoConf , listFunzPagTpServEnteDB);
							if (funzPagamentoConfDB!=null) {
								arrayFunzPagamentoConfs.add(funzPagamentoConfDB);
							} else {
								arrayFunzPagamentoConfs.add(funzPagamentoConf);	
							}
						} else {
							arrayFunzPagamentoConfs.add(funzPagamentoConf);	
						}
					}
				}
			}
			catch (Exception ex) {
				throw new ActionException(ex);
			}
			request.setAttribute("listaFunzPagamentoConf", arrayFunzPagamentoConfs );
		}
		
//		String sCampiWebRowSet = null;
//		//preparo il cached row set l
//		try
//		{
//			WebRowSetImpl rowSet = new WebRowSetImpl();
//			RowSetMetaDataImpl rsMdData = new RowSetMetaDataImpl();
//
//			rsMdData.setColumnCount(3);
//			rsMdData.setColumnType(1, Types.VARCHAR);
//			rsMdData.setColumnType(2, Types.VARCHAR);
//			rsMdData.setColumnType(3, Types.VARCHAR);
//			rowSet.setMetaData(rsMdData);
//
//			for (String campo :  sElencoCampi)
//			{
//				//campo = nomecampo|descrizione
//				String[] sCampoDett = campo.split("\\|");
//				if (sCampoDett != null && sCampoDett.length == 2)
//				{
//					rowSet.moveToInsertRow();
//					rowSet.updateString(1, sCampoDett[0]); //nomecampo
//					rowSet.updateString(2, sCampoDett[1]); //descrizione
//					if (bModifica)
//						rowSet.updateString(3, getFlagObbligatorio(sCampoDett[0], listFunzPagTpServEnteDB));
//					else //in inserimento metto tutti i checkbox deselezionati
//						rowSet.updateString(3, "N");
//					rowSet.insertRow();
//				}
//			}
//			rowSet.moveToCurrentRow();
//			sCampiWebRowSet = Convert.webRowSetToString(rowSet);
//		}
//		catch (Exception ex) {}
//		request.setAttribute("listacampiform", sCampiWebRowSet);
//  
	}
	
	//PG130040 - INIZIO - CG
	private static FunzPagamentoConf getFields(FunzPagamentoConf funzPagamentoConf, FunzPagTpServEnte[] listFunzPagTpServEnteDB)
	{
		String nomeCampo = funzPagamentoConf.getChiave();
		
		if (listFunzPagTpServEnteDB != null && listFunzPagTpServEnteDB.length > 0)
		{
			for (FunzPagTpServEnte funz : listFunzPagTpServEnteDB)
			{
				if (funz.getNomeForm().equals(nomeCampo)){
					return new FunzPagamentoConf(funz.getNomeForm(),
							funz.getLabelFormFunPagamento(),	//TODO PG130040 getLabelFormPagamento cambiato ingetLabelFormFunPagamento ??
							funz.getImmissioneVal(),
							funz.getFlagTipoValore(),funz.getFlagLunghezza(),funz.getLunghezza(),funz.getFlagDescrAlt(),
							funz.getDescrAlternativa(),
							funzPagamentoConf.getLunghezzaMax(),
							funzPagamentoConf.isFlagBloccoObbligatorioModificabile(),
							funzPagamentoConf.isFlagBloccoTipoValoreModificabile(),
							funzPagamentoConf.isFlagBloccoLunghezzaModificabile(),
							funzPagamentoConf.isFlagBloccoDescrizioneAlternativaModificabile() ); 
				}
			}
		}
		return null;
	}
	//PG130040 - FINE - CG
	
	@SuppressWarnings("unchecked")
	protected void resetParametri(HttpServletRequest request) throws ActionException {
//PG130040 - INIZIO - GC
		String codiceSocieta = request.getParameter("funzpagutentetiposervizioente_companyCode");		
		String codiceUtente = request.getParameter("funzpagutentetiposervizioente_userCode");
		String chiaveEnte = request.getParameter("funzpagutentetiposervizioente_chiaveEnte");
		String tipologiaServizio = request.getParameter("funzpagutentetiposervizioente_codiceTipologiaServizio");
		
		//recupero il tipo bollettino relativo all'elemento selezionato
		String tipoBollettino = getTipoBollettino(request, codiceSocieta, codiceUtente, chiaveEnte, tipologiaServizio );
		loadCampiForm(request, codiceSocieta, codiceUtente, chiaveEnte, tipologiaServizio, tipoBollettino, false);
			
//		Enumeration e = request.getParameterNames();
//		String p = "";
//		while (e.hasMoreElements()) {
//			
//			p = (String) e.nextElement();
//			request.setAttribute(p, "");
//		}
//		return;
//PG130040 - FINE - GC
	}

	private static FunzPagTpServEnte[] getListFunzPagTpServEnteDB(String codiceSocieta, String codiceUtente,String chiaveEnte, String tipologiaServizio, HttpServletRequest request)
	{
		try {
		
			FunzPagTpServEnteDetailAggregatoRequest req = new FunzPagTpServEnteDetailAggregatoRequest();
			req.setCodiceSocieta(codiceSocieta);
			req.setCodiceUtente(codiceUtente);
			req.setChiaveEnte(chiaveEnte);
			req.setTipologiaServizio(tipologiaServizio);
			FunzPagTpServEnteDetailAggregatoResponse res = WSCache.funzPagTpServEnteServer.getFunzPagTpServ_Aggregato(req, request);
			if (res != null && res.getResponse() != null)
			{
				if (res.getResponse().getRetCode().equals("00"))
					return res.getListFunzPagTpServEnte();
				else if (res.getResponse().getRetCode().equals("02"))
					return new FunzPagTpServEnte[0];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static String getFlagObbligatorio(String nomeCampo, FunzPagTpServEnte[] listFunzPagTpServEnteDB)
	{
		if (listFunzPagTpServEnteDB != null && listFunzPagTpServEnteDB.length > 0)
		{
			for (FunzPagTpServEnte funz : listFunzPagTpServEnteDB)
			{
				if (funz.getNomeForm().equals(nomeCampo))
					return funz.getImmissioneVal();
			}
		}
		
		return "N";
	}
	
	private String getTipoBollettino(HttpServletRequest request, String companyCode, String userCode, String chiaveEnte, String codiceTipologiaServizio)
	{
		if (request.getAttribute("listaSocietaUtenteServizioEnte") != null)
		{
			//inizio LP PG21XX04 Leak
			WebRowSet wrs = null;
			//fine LP PG21XX04 Leak
			try
			{
				//inizio LP PG21XX04 Leak
				//WebRowSet wrs = Convert.stringToWebRowSet((String)request.getAttribute("listaSocietaUtenteServizioEnte"));
				wrs = Convert.stringToWebRowSet((String)request.getAttribute("listaSocietaUtenteServizioEnte"));
				//fine LP PG21XX04 Leak
				while (wrs.next())
				{
					if (wrs.getString(1).equals(companyCode) && wrs.getString(2).equals(userCode) && 
							wrs.getString(3).equals(chiaveEnte) && wrs.getString(4).equals(codiceTipologiaServizio))
						return wrs.getString(19);
				}
				
			} catch (Exception e) {}
			//inizio LP PG21XX04 Leak
			finally {
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
		return "";
	}

	//inizio PG130040 CG
	public static String FLAG_TIPO_VALORE_NUMERICO = "N";
	public static String FLAG_TIPO_VALORE_ALFANUMERICO = "A";
	public static String FLAG_LUNGHEZZA_FISSA  = "F";
	public static String FLAG_LUNGHEZZA_VARIABILE = "V";
	public static String FLAG_DESCRIZIONE_DEFAULT = "D";
	public static String FLAG_DESCRIZIONE_ALTERNATIVA = "A";
	//fine PG130040 CG
}
