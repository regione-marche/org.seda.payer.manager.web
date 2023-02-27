package org.seda.payer.manager.configurazione.actions;


import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
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
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.funzpagtpserv.dati.FunzPagTpServ;
import com.seda.payer.pgec.webservice.funzpagtpserv.dati.FunzPagTpServCancelAggregatoRequest;
import com.seda.payer.pgec.webservice.funzpagtpserv.dati.FunzPagTpServDetailAggregatoRequest;
import com.seda.payer.pgec.webservice.funzpagtpserv.dati.FunzPagTpServDetailAggregatoResponse;
import com.seda.payer.pgec.webservice.funzpagtpserv.dati.FunzPagTpServResponse;
import com.seda.payer.pgec.webservice.funzpagtpserv.dati.FunzPagTpServResponsePageInfo;
import com.seda.payer.pgec.webservice.funzpagtpserv.dati.FunzPagTpServSaveAggregatoRequest;
import com.seda.payer.pgec.webservice.funzpagtpserv.dati.FunzPagTpServSearchAggregatoRequest;
import com.seda.payer.pgec.webservice.funzpagtpserv.dati.FunzPagTpServSearchAggregatoResponse;
import com.seda.payer.pgec.webservice.funzpagtpserv.dati.StatusResponse;
import com.sun.rowset.WebRowSetImpl;




public class FunzPagUtenteTipoServizioAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private String usernameAutenticazione = null;
	@SuppressWarnings("unused")
	private String userCodiceSocieta = null;
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
		
		/*
		    * VBruno
		    * "userCodiceSocieta" serve per gestire la profilazione utente; permette 
		    * di limitare la visibilità e l'operatività agli ambiti consentiti.
		    * E' sufficiente la società perché "configurazione" è accessibile 
		    * solo ad AMMI ed AMSO
		    */ 
		if (user != null)
			   userCodiceSocieta = (user.getProfile().equals("AMMI") ? "" : user.getCodiceSocieta());
		   else
			   userCodiceSocieta = "XXXXX";
		
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
			
			
			String descrizioneSocieta = ((String)request.getAttribute("funzpagutentetiposervizio_searchcompanyCode")==null ? "":  (String)request.getAttribute("funzpagutentetiposervizio_searchcompanyCode"));
			String descrizioneUtente = ((String)request.getAttribute("funzpagutentetiposervizio_searchuserCode")==null ? "":  (String)request.getAttribute("funzpagutentetiposervizio_searchuserCode"));
			String descrizioneTipologiaServizio = ((String)request.getAttribute("funzpagutentetiposervizio_searchcodiceTipologiaServizio")==null ? "":  (String)request.getAttribute("funzpagutentetiposervizio_searchcodiceTipologiaServizio"));
			
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonN = (String)request.getAttribute("tx_button_nuovo");
			if (firedButton!=null||firedButtonN!=null){
					
				request.setAttribute("funzpagutentetiposervizio_searchcompanyCode", "");
				request.setAttribute("funzpagutentetiposervizio_searchuserCode", "");
				request.setAttribute("funzpagutentetiposervizio_searchcodiceTipologiaServizio","");
				descrizioneSocieta="";
				descrizioneUtente="";
				descrizioneTipologiaServizio="";
				
			}
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				descrizioneSocieta="";
				descrizioneUtente="";
				descrizioneTipologiaServizio="";
			}
			
			FunzPagTpServSearchAggregatoResponse searchResponse = getFunzPagTpServSearchResponse(descrizioneSocieta, descrizioneUtente, descrizioneTipologiaServizio, rowsPerPage, pageNumber, order, request);
			if (searchResponse != null && searchResponse.getResponse() != null && searchResponse.getResponse().getRetCode().equals("00"))
			{
				FunzPagTpServResponse funzpagutentetiposervizioResponse = searchResponse.getListResponse();
				FunzPagTpServResponsePageInfo responsePageInfo = funzpagutentetiposervizioResponse.getPageInfo();
				PageInfo pageInfo = new PageInfo(); 
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			
				request.setAttribute("funzpagutentetiposervizios", funzpagutentetiposervizioResponse.getListXml());
				request.setAttribute("funzpagutentetiposervizios.pageInfo", pageInfo);
			}
			
			//request.setAttribute("funzpagutentetiposervizio", searchRequest);
			request.setAttribute("funzpagutentetiposervizio_searchcompanyCode", descrizioneSocieta);
			request.setAttribute("funzpagutentetiposervizio_searchuserCode", descrizioneUtente);
			request.setAttribute("funzpagutentetiposervizio_searchcodiceTipologiaServizio", descrizioneTipologiaServizio);
			
			
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
			loadSocietaUtenteServizioXml_DDL(request, session);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {	  
		request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		request.setAttribute("funzpagutentetiposervizio_companyCode",request.getParameter("funzpagutentetiposervizio_companyCode"));
		request.setAttribute("funzpagutentetiposervizio_userCode", request.getParameter("funzpagutentetiposervizio_userCode"));
		request.setAttribute("funzpagutentetiposervizio_codiceTipologiaServizio", request.getParameter("funzpagutentetiposervizio_codiceTipologiaServizio"));
		return null; 
	}
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			loadSocietaUtenteServizioXml_DDL(request, session);
			
			String codiceSocieta = request.getParameter("funzpagutentetiposervizio_companyCode");		
			String codiceUtente = request.getParameter("funzpagutentetiposervizio_userCode");
			String tipologiaServizio = request.getParameter("funzpagutentetiposervizio_codiceTipologiaServizio");
		     
			
			request.setAttribute("funzpagutentetiposervizio_companyCode", codiceSocieta);
			request.setAttribute("funzpagutentetiposervizio_userCode", codiceUtente);
			request.setAttribute("funzpagutentetiposervizio_codiceTipologiaServizio", tipologiaServizio);
			
			//recupero il tipo bollettino relativo all'elemento selezionato
			String tipoBollettino = getTipoBollettino(request, codiceSocieta, codiceUtente, tipologiaServizio);
			request.setAttribute("funzpagutentetiposervizio_tipoboll", tipoBollettino);
			
			loadCampiForm(request, codiceSocieta, codiceUtente, tipologiaServizio, tipoBollettino, true);
			
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
				loadSocietaUtenteServizioXml_DDL(request, session);
				String arrStr = request.getParameter("funzpagutentetiposervizio_strUtentetiposervizios");

				if (arrStr!=null && arrStr.length()>0)
				{
					  String[] strUtentetiposervizios = arrStr.split("\\|");
					  if (strUtentetiposervizios.length == 4)
					  {
						  String codiceSocieta = strUtentetiposervizios[0];
						  String codiceUtente = strUtentetiposervizios[1];
						  String tipologiaServizio = strUtentetiposervizios[2];
						  String tipoBoll = strUtentetiposervizios[3];
						  
						  //PG130040
						  request.setAttribute("funzpagutentetiposervizio_companyCode", codiceSocieta);
						  request.setAttribute("funzpagutentetiposervizio_userCode", codiceUtente);
						  request.setAttribute("funzpagutentetiposervizio_codiceTipologiaServizio", tipologiaServizio); 
						  request.setAttribute("funzpagutentetiposervizio_tipoboll", tipoBoll);
						  
						  //FINE PG130040
						  
						  //controllo che non siano già stati inseriti valori per soc/ute/servizio selezionati
						  FunzPagTpServ[] listFunzPagTpServ = getListFunzPagTpServDB(codiceSocieta, codiceUtente, tipologiaServizio, request);
						  if (listFunzPagTpServ != null && listFunzPagTpServ.length > 0)
							  setFormMessage("frmAction", "Valore già presente", request);
						  else
						  {
							  loadCampiForm(request, codiceSocieta, codiceUtente, tipologiaServizio, tipoBoll, false);
							  request.setAttribute("campiFormCaricati", "Y");
						  }
						  
						  request.setAttribute("funzpagutentetiposervizio_strUtentetiposervizios", arrStr);
					  }
				}
			}
			else if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					request.setAttribute("funzpagutentetiposervizio_companyCode", "");
					request.setAttribute("funzpagutentetiposervizio_userCode", "");
					request.setAttribute("funzpagutentetiposervizio_codiceTipologiaServizio", "");
					index(request);					
				}
			}
			else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){					
					//PG130040 CG  resetParametri(request);
					request.setAttribute("action", "add");
					//PG130040 CG - add(request);
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
					request.setAttribute("funzpagutentetiposervizio_companyCode", "");
					request.setAttribute("funzpagutentetiposervizio_userCode", "");
					request.setAttribute("funzpagutentetiposervizio_codiceTipologiaServizio", "");
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
		} catch (Exception ignore) {
			// errore gestito dalla catch del metodo save
		}
		return null;
	}
	
	private void save(HttpServletRequest request) throws ActionException {
		String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		
		String codiceSocieta = request.getParameter("funzpagutentetiposervizio_companyCode");		
		String codiceUtente = request.getParameter("funzpagutentetiposervizio_userCode");		
		String tipologiaServizio = request.getParameter("funzpagutentetiposervizio_codiceTipologiaServizio");
		String tipoBollettino = request.getParameter("funzpagutentetiposervizio_tipoboll");
		
		String arrStr = request.getParameter("funzpagutentetiposervizio_strUtentetiposervizios");
		
		if (codOp!=null && codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0 && arrStr!=null && arrStr.length()>0)
		{
			 String[] strUtentetiposervizios = arrStr.split("\\|");
			 codiceSocieta = strUtentetiposervizios[0];
			 codiceUtente = strUtentetiposervizios[1];
 		     tipologiaServizio = strUtentetiposervizios[2];
 		     tipoBollettino = strUtentetiposervizios[3];
 		}
		
		if(checkLunghezze(request, tipoBollettino)) {	//PG130040
			String[] aCampi = (String[])request.getAttribute("grCampi"); 
			
			//nell'array aCampi ho sempre 2 elementi fittizi denominati "hidden1" e "hidden2"
			//quindi la dimensione reale si ottiene con aCampi.length - 2
			int iSize = aCampi.length - 2;
			if (codOp!=null && codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0 && iSize == 0)
			{
				//sono in inserimento e non ho selezionato nemmeno un elemento
				setFormMessage("frmAction", "Selezionare almeno uno fra i campi obbligatori", request);
				request.setAttribute("done", null);
				loadSocietaUtenteServizioXml_DDL(request, session);
				loadCampiForm(request, codiceSocieta, codiceUtente, tipologiaServizio, tipoBollettino, false);
				request.setAttribute("campiFormCaricati", "Y");
			}
			else
			{
				boolean bOk = true;
				//se sono in modifica devo prima eliminare tutti i campi obbligatori e poi reinserirli
				//se sono in inserimento li inserisco direttamente
				if (codOp!=null && codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0)
					bOk = cancel_Aggregato(codiceSocieta, codiceUtente, tipologiaServizio, request);
		
				if (bOk)
				{
					try {
						FunzPagTpServSaveAggregatoRequest req = new FunzPagTpServSaveAggregatoRequest();
						
		//PG130040				String[] sElencoCampi = null;
						FunzPagamentoMap funzPagamentoMap = null; //PG130040	
						if (tipoBollettino != null && tipoBollettino.length() > 0){
	
		//PG130040						sElencoCampi = getElencoCampi(tipoBollettino);
							funzPagamentoMap = getElencoCampiNew(tipoBollettino);
						}
						
						List<FunzPagTpServ> listFunzPagTpServ = new ArrayList<FunzPagTpServ>();
						for (String chiave : funzPagamentoMap.keySet() )
						{
							// String[] sCampoDett = campo.split("\\|");
							FunzPagamentoConf pagamentoConf = funzPagamentoMap.get(chiave);
							
							FunzPagTpServ funz = new FunzPagTpServ();
							funz.setCompanyCode(codiceSocieta);
							funz.setUserCode(codiceUtente);
							funz.setCodiceTipologiaServizio(tipologiaServizio);
					//		funz.setNomeForm(sCampoDett[0]);
							funz.setNomeForm(chiave); //PG130040
					//		funz.setLabelFormPagamento(sCampoDett[1]);
							funz.setLabelFormPagamento(pagamentoConf.getDescrizione()); //PG130040
					//		funz.setImmissioneVal(checkCampoSelected(aCampi, sCampoDett[0]));
							funz.setImmissioneVal(checkCampoSelected(aCampi, chiave)); //PG130040
							funz.setFlagTipoValore( checkTipoValore( request, chiave, pagamentoConf) );		//PG130040 GC
							funz.setFlagLunghezza( checkFlagLunghezza( request, chiave, pagamentoConf )  );//PG130040 GC
							funz.setLunghezza( getLunghezzaParameter(request,chiave, pagamentoConf )  ); //PG130040 GC
							funz.setFlagDescrAlt(checkDescrAlternativa(request,chiave, pagamentoConf )  ); //PG130040 GC
							funz.setDescrAlternativa(getDescrAlternativa(request,chiave, pagamentoConf )  ); //PG130040 GC
							funz.setOperatorCode(usernameAutenticazione);
							listFunzPagTpServ.add(funz);
						}
						
						req.setListFunzPagTpServ((FunzPagTpServ[])listFunzPagTpServ.toArray(new FunzPagTpServ[listFunzPagTpServ.size()]));
						req.setCodOp(codOp);
						
						StatusResponse res = WSCache.funzPagTpServServer.save_Aggregato(req, request);
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
			loadSocietaUtenteServizioXml_DDL(request, session);

			request.setAttribute("listaFunzPagamentoConf", valorizzaFunzPagamentoConf(request) );
		} //fine PG130040
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
		
		String tipoBollettino = request.getParameter("funzpagutentetiposervizio_tipoboll");
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
	
	
	
	//PG130040 CG 
	private String getDescrAlternativa(HttpServletRequest request, String nomeChiave,  FunzPagamentoConf pagamentoConf)
	{ 
		if (request.getParameter("descrizioneAlternativa_" + nomeChiave)==null) {
			return pagamentoConf.getDescrizioneAlternativa();
		} else {
			return  request.getParameter("descrizioneAlternativa_" + nomeChiave );	
		}
	}
	
	private String checkDescrAlternativa(HttpServletRequest request, String chiave,  FunzPagamentoConf pagamentoConf) 
	{
		if (request.getParameter("FlagDescrAlternativa_" + chiave )==null) {
			return pagamentoConf.getTipoDescrizioneAlternativa();
		} else {
			return  request.getParameter("FlagDescrAlternativa_" + chiave );	
		}
	}

	private String checkCampoSelected(String[] aCampi, String campoToCheck)
	{
		for (String campo : aCampi)
		{
			if (campo.equals(campoToCheck))
				return "Y";
		}
		return "N";
	}

	private String checkTipoValore(HttpServletRequest request, String nomeChiave,  FunzPagamentoConf pagamentoConf)
	{
		if (request.getParameter("FlagTipoValore_" + nomeChiave)==null) {
			return pagamentoConf.getTipoValore();
		} else {
			return  request.getParameter("FlagTipoValore_" + nomeChiave );	
		}
	}
	
	private String checkFlagLunghezza(HttpServletRequest request, String nomeChiave,  FunzPagamentoConf pagamentoConf)
	{

		if (request.getParameter("flagLunghezza_" + nomeChiave)==null) {
			return pagamentoConf.getTipoLunghezza();
		} else {
			return  request.getParameter("flagLunghezza_" + nomeChiave );	
		}
	}

	private String getLunghezzaParameter(HttpServletRequest request, String nomeChiave,  FunzPagamentoConf pagamentoConf)
	{ 
		if (request.getParameter("lunghezza_" + nomeChiave)==null) {
			return pagamentoConf.getLunghezzaMax();
		} else {
			return  request.getParameter("lunghezza_" + nomeChiave );	
		}
	}
	//PG130040 CG
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String codiceSocieta = ((String)request.getAttribute("funzpagutentetiposervizio_companyCode")==null ? "":  (String)request.getAttribute("funzpagutentetiposervizio_companyCode"));
		String codiceUtente = ((String)request.getAttribute("funzpagutentetiposervizio_userCode")==null ? "":  (String)request.getAttribute("funzpagutentetiposervizio_userCode"));
		String tipologiaServizio = ((String)request.getAttribute("funzpagutentetiposervizio_codiceTipologiaServizio")==null ? "":  (String)request.getAttribute("funzpagutentetiposervizio_codiceTipologiaServizio"));
		
	    
		try {
			request.setAttribute("varname", "funzpagutentetiposervizio");
			
			boolean bRes = cancel_Aggregato(codiceSocieta, codiceUtente, tipologiaServizio, request);
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
	
	private FunzPagTpServSearchAggregatoResponse getFunzPagTpServSearchResponse(
			String descrizioneSocieta, String descrizioneUtente,  String descrizioneTipologiaServizio, int rowsPerPage,int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		FunzPagTpServSearchAggregatoRequest in = new FunzPagTpServSearchAggregatoRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setDescrizioneSocieta(descrizioneSocieta == null ? "" : descrizioneSocieta);
		in.setDescrizioneUtente(descrizioneUtente  == null ? "" : descrizioneUtente);
		in.setDescrizioneTipologiaServizio(descrizioneTipologiaServizio == null ? "" :  descrizioneTipologiaServizio);
		
		return WSCache.funzPagTpServServer.getListaFunzPagTpServ_Aggregato(in, request);
	}
	
	private boolean cancel_Aggregato(String codiceSocieta, String codiceUtente, String tipologiaServizio, HttpServletRequest request)
	{
		try {
			FunzPagTpServCancelAggregatoRequest req = new FunzPagTpServCancelAggregatoRequest(codiceSocieta, codiceUtente, tipologiaServizio);
			StatusResponse res = WSCache.funzPagTpServServer.cancel_Aggregato(req, request);
			if (res != null && res.getResponse() != null && res.getResponse().getRetCode().equals("00"))
				return true;
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	protected void resetParametri(HttpServletRequest request) {
		Enumeration e = request.getParameterNames();
		String p = "";
		while (e.hasMoreElements()) {
			
			p = (String) e.nextElement();
			request.setAttribute(p, "");
		}
		return;
	}
	

	private static void loadCampiForm(HttpServletRequest request, String codiceSocieta, String codiceUtente, String tipologiaServizio, String tipoBoll, boolean bModifica) throws ActionException
	{
		
		// PG130040 CG - inizio
		FunzPagamentoMap sElencoCampi = new FunzPagamentoMap();
		if (tipoBoll != null && tipoBoll.length() > 0)
			sElencoCampi = getElencoCampiNew(tipoBoll);
		
		//recupero da DB la lista dei campi e il flag obbligatorio
		FunzPagTpServ[] listFunzPagTpServDB = null; 
		if (bModifica)
			listFunzPagTpServDB = getListFunzPagTpServDB(codiceSocieta,codiceUtente,tipologiaServizio, request);
		
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

							FunzPagamentoConf funzPagamentoConfDB = getFields(funzPagamentoConf, listFunzPagTpServDB);
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
//
//			// PG130040 CG - fine
//			FunzPagamentoMap map = getElencoCampiNew(tipoBoll);
//			
//			request.setAttribute("listaFunzPagamentoConf", new ArrayList<FunzPagamentoConf>(map.values()) );
//			//FINE PG130040
//	  
		}

		
		
//		if (sElencoCampi != null && sElencoCampi.size() > 0)
//		{
//			String sCampiWebRowSet = null;
//			//preparo il cached row set l
//			try
//			{
//				WebRowSetImpl rowSet = new WebRowSetImpl();
//				RowSetMetaDataImpl rsMdData = new RowSetMetaDataImpl();
//
//				rsMdData.setColumnCount(8);
//				rsMdData.setColumnType(1, Types.VARCHAR);
//				rsMdData.setColumnType(2, Types.VARCHAR);
//				rsMdData.setColumnType(3, Types.VARCHAR);
//				rsMdData.setColumnType(4, Types.VARCHAR);//PG130040 CG
//				rsMdData.setColumnType(5, Types.VARCHAR);//PG130040 CG
//				rsMdData.setColumnType(6, Types.VARCHAR);//PG130040 CG
//				rsMdData.setColumnType(7, Types.VARCHAR);//PG130040 CG
//				rsMdData.setColumnType(8, Types.VARCHAR);//PG130040 CG
//				rowSet.setMetaData(rsMdData);
//
//				for (String campo :  sElencoCampi)
//				{
//					//campo = nomecampo|descrizione
//					String[] sCampoDett = campo.split("\\|");
//					if (sCampoDett != null && sCampoDett.length == 2)
//					{
//						rowSet.moveToInsertRow();
//						rowSet.updateString(1, sCampoDett[0]); //nomecampo
//						rowSet.updateString(2, sCampoDett[1]); //descrizione
//						if (bModifica) {
//							// INIZIO PG130040 - CG
//							String fields = getFields(sCampoDett[0], listFunzPagTpServDB);
//							String[] fieldsSplittati = fields.split("\\~");
//							rowSet.updateString(3, fieldsSplittati[0]);
//							rowSet.updateString(4, fieldsSplittati[1]);
//							rowSet.updateString(5, fieldsSplittati[2]);
//							rowSet.updateString(6, fieldsSplittati[3]);
//							rowSet.updateString(7, fieldsSplittati[4]);
//							rowSet.updateString(8, fieldsSplittati[5]);
//						} else {
//							//in inserimento valorizzo con i valori di default
//							rowSet.updateString(3, "N");//FACOLTATIVO
//							rowSet.updateString(4, "A");//ALFANUMERICO
//							rowSet.updateString(5, "F");//LUNGHEZZA FISSA
//							rowSet.updateString(6, "0");//LUNGHEZZA ZERO
//							rowSet.updateString(7, "D");//USO IL DEFAULT NELLA DESCR. ALTERNATIVA
//							rowSet.updateString(8, " ");//SPAZIO NELLA DESCRIZIONE ALTERNATIVA
//						}
//						//FINE PG130040 - CG
//						rowSet.insertRow();
//					}
//				}
//				rowSet.moveToCurrentRow();
//				sCampiWebRowSet = Convert.webRowSetToString(rowSet);
//			}
//			catch (Exception ex) {}
//			request.setAttribute("listacampiform", sCampiWebRowSet);
//
//			//PG130040
//			FunzPagamentoMap map = getElencoCampiNew(tipoBoll);
//			
//			request.setAttribute("listaFunzPagamentoConf", new ArrayList<FunzPagamentoConf>(map.values()) );
//			//FINE PG130040
//	  
//		}
	  
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
	
//	public static String[] getElencoCampi(String tipoBoll)
//	{
//		String[] sElencoCampi = null;
//		//recupero la lista di tutti i campi possibili per la tipologia bollettino selezionata
//		if (tipoBoll.startsWith("ICI"))
//			sElencoCampi = FunzioniPagamento.ICI.format().split(",");
//		else if (tipoBoll.startsWith("ISC"))
//			sElencoCampi = FunzioniPagamento.ISCOP.format().split(",");
//		else if (tipoBoll.startsWith("BOL"))
//			sElencoCampi = FunzioniPagamento.BOLLO.format().split(",");
//		else if (tipoBoll.startsWith("PRE"))
//			sElencoCampi = FunzioniPagamento.PREMARCATO.format().split(",");
//		else if (tipoBoll.startsWith("FRE"))
//			sElencoCampi = FunzioniPagamento.FRECCIA.format().split(",");
//		else if (tipoBoll.startsWith("MAV"))
//			sElencoCampi = FunzioniPagamento.MAV.format().split(",");
//		else if (tipoBoll.startsWith("CDS"))
//			sElencoCampi = FunzioniPagamento.CDS.format().split(",");
//		else if (tipoBoll.startsWith("SPO"))
//			sElencoCampi = FunzioniPagamento.SPONTANEO.format().split(",");
//		return sElencoCampi;
//	}
	
	private static FunzPagTpServ[] getListFunzPagTpServDB(String codiceSocieta, String codiceUtente,String tipologiaServizio, HttpServletRequest request)
	{
		try {
		
			FunzPagTpServDetailAggregatoRequest req = new FunzPagTpServDetailAggregatoRequest();
			req.setCodiceSocieta(codiceSocieta);
			req.setCodiceUtente(codiceUtente);
			req.setTipologiaServizio(tipologiaServizio);
			FunzPagTpServDetailAggregatoResponse res = WSCache.funzPagTpServServer.getFunzPagTpServ_Aggregato(req, request);
			if (res != null && res.getResponse() != null)
			{
				if (res.getResponse().getRetCode().equals("00"))
					return res.getListFunzPagTpServ();
				else if (res.getResponse().getRetCode().equals("02"))
					return new FunzPagTpServ[0];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
//	private static String getFlagObbligatorio(String nomeCampo, FunzPagTpServ[] listFunzPagTpServDB)
//	{
//		if (listFunzPagTpServDB != null && listFunzPagTpServDB.length > 0)
//		{
//			for (FunzPagTpServ funz : listFunzPagTpServDB)
//			{
//				if (funz.getNomeForm().equals(nomeCampo))
//					return funz.getImmissioneVal();
//			}
//		}
//		
//		return "N";
//	}
	
	//PG130040 - INIZIO - CG
	private static FunzPagamentoConf getFields(FunzPagamentoConf funzPagamentoConf, FunzPagTpServ[] listFunzPagTpServDB)
	{
		String nomeCampo = funzPagamentoConf.getChiave();
		
		if (listFunzPagTpServDB != null && listFunzPagTpServDB.length > 0)
		{
			for (FunzPagTpServ funz : listFunzPagTpServDB)
			{
				if (funz.getNomeForm().equals(nomeCampo)){
					return new FunzPagamentoConf(funz.getNomeForm(),funz.getLabelFormPagamento(),funz.getImmissioneVal(),
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
	
	private String getTipoBollettino(HttpServletRequest request, String codiceSocieta, String codiceUtente, String tipologiaServizio)
	{
		if (request.getAttribute("listaSocietaUtenteServizio") != null)
		{
			//inizio LP PG21XX04 Leak
			WebRowSet wrs = null;
			//fine LP PG21XX04 Leak
			try
			{
				//inizio LP PG21XX04 Leak
				//WebRowSet wrs = Convert.stringToWebRowSet((String)request.getAttribute("listaSocietaUtenteServizio"));
				wrs = Convert.stringToWebRowSet((String)request.getAttribute("listaSocietaUtenteServizio"));
				//fine LP PG21XX04 Leak
				while (wrs.next())
				{
					if (wrs.getString(1).equals(codiceSocieta) && wrs.getString(2).equals(codiceUtente) && wrs.getString(3).equals(tipologiaServizio))
						return wrs.getString(17);
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
