package org.seda.payer.manager.walletconfig.actions;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.j2ee5.jndi.JndiProxy;
import com.seda.j2ee5.jndi.JndiProxyException;
import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.walletmanager.actions.WalletBaseManagerAction;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.AbilitazioneDiscarico;
import com.seda.payer.core.wallet.bean.Servizio;
import com.seda.payer.core.wallet.bean.Tributo;
import com.seda.payer.core.wallet.bean.WalletPageList;
import com.seda.payer.core.wallet.dao.AbilitazioneDiscaricoDAO;
import com.seda.payer.core.wallet.dao.ServizioDAO;
import com.seda.payer.core.wallet.dao.TributoDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest2;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;
import com.seda.tag.core.DdlOption;

public class AbilitazioneDiscarichiAction extends WalletBaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String ente;
	private String societa;
	private String utente;
	
	
	
	@Override
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		//ddl società utente ente
		loadSocietaUtenteEnteXml_DDL(request, session);
		//ddl tributi
		getTributiDDL(request,session);
		//ddl servizi
		//getServiziDDL(request, session);
		//ddl discaricabile
		getDiscaricabileDDL(request, session);
		//setRequestParameters
		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		//setrequestparameters
		setRequestParameters(request);
		//button selection
		//se è stato messo un messaggio nella session dalla pagina di edit in update lo stampiamo
		
		FiredButton firedButton = getFiredButton(request);
		if ((String) session.getAttribute("recordDelete") != null){
			firedButton = firedButton.TX_BUTTON_CERCA; 
			if(session.getAttribute("recordDelete").toString().trim().equals("OK"))
				setFormMessage("form_selezione", Messages.UPDT_OK.format(), request);
			else
				if (session.getAttribute("messaggio.recordDelete")!=null){
					setFormMessage("form_selezione", session.getAttribute("messaggio.recordDelete").toString(), request);
				}
			
			session.removeAttribute("recordDelete");
		}
		if ((String) session.getAttribute("updateEsito") != null) 
		{
			firedButton = firedButton.TX_BUTTON_CERCA; 
			if(session.getAttribute("updateEsito").toString().trim().equals("OK"))
				setFormMessage("form_selezione", Messages.UPDT_OK.format(), request);
			else
				if (session.getAttribute("messaggio.recordUpdate")!=null){
					setFormMessage("form_selezione", session.getAttribute("messaggio.recordUpdate").toString(), request);
				}
			
			session.removeAttribute("updateEsito");
		}
		switch(firedButton)
		{
			
			case TX_BUTTON_CERCA: 
				try {
                    WalletPageList walletPageListlist = listaAbilitazioneDiscarichi(request);
					PageInfo pageInfo = walletPageListlist.getPageInfo();
					
					if (walletPageListlist.getRetCode()!="00") {
						setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
					} else {
						if(pageInfo != null)
						{
							if(pageInfo.getNumRows() > 0)
							{
								request.setAttribute("lista_abilitazione_discarichi", walletPageListlist.getWalletListXml());
								request.setAttribute("lista_abilitazione_discarichi.pageInfo", pageInfo);
							}
							else {
								request.setAttribute("lista_abilitazione_discarichi", null);
								
								setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
							}
						}
						else { 
							setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
						}
					}
				} catch(Exception e) {
					setErrorMessage(e.getMessage());
					e.printStackTrace();
				}
				break;
				
			case TX_BUTTON_RESET:
				resetParametri(request);
				request.setAttribute("tx_data_pag_da", null);
				request.setAttribute("tx_data_pag_a", null);
				request.setAttribute("tx_data_cre_da", null);
				request.setAttribute("tx_data_cre_a", null);
				request.setAttribute("tx_codice_tributo2",null );
				request.setAttribute("tx_tipologia_servizio",null );
				request.setAttribute("tx_flag_servizio",null);
				setProfile(request);
				break;
				
			case TX_BUTTON_SOCIETA_CHANGED:
				request.setAttribute("tx_provincia","");
				request.setAttribute("tx_UtenteEnte", "");
				break;
				
			case TX_BUTTON_PROVINCIA_CHANGED:
				break;
			
			case TX_BUTTON_ENTE_CHANGED:
				loadProvinciaXml_DDL(request, session, "",false);
				LoadListaUtentiEntiXml_DDL(request, session, "" , "", "", "", false);
				//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				break;
				
				
			case TX_BUTTON_NULL: 
				break;
				
			case TX_BUTTON_STAMPA:
				break;
				
			case TX_BUTTON_DOWNLOAD:
				break;
			case TX_BUTTON_ADD:
				
				break;
			
		}
		return null;
	
 
	}
    
	
	protected void loadSocietaUtenteEnteXml_DDL(HttpServletRequest request,HttpSession session)
	{
		String societaUtenteEnte=(String) request.getAttribute("ddlSocietaUtenteEnte");
		if(societaUtenteEnte!=null){
			if(!societaUtenteEnte.equals("")){
			String [] splitString=societaUtenteEnte.split("\\|");
			societa=splitString[0];
			utente=splitString[1];
			ente=splitString[2];
			session.setAttribute("ddlSocietaUtenteEnte", societaUtenteEnte);
			}else{
				societa="";
				utente="";
				ente="";
			session.setAttribute("ddlSocietaUtenteEnte", "");
			}
		}else{
			societaUtenteEnte=(String) session.getAttribute("ddlSocietaUtenteEnte");
			if(session.getAttribute("ddlSocietaUtenteEnte")!=null&&!societaUtenteEnte.equals("")){
				String [] splitString=societaUtenteEnte.split("\\|");
				societa=splitString[0];
				utente=splitString[1];
				ente=splitString[2];
			}else{
				societa="";
				utente="";
				ente="";
			}
			
		}
		
		ConfigUtenteTipoServizioEnteSearchRequest2 in = null;
		ConfigUtenteTipoServizioEnteSearchResponse res = null;
		ConfigUtenteTipoServizioEnteResponse response = null;
		
		String xml = null;
		String lista = "listaSocietaUtenteEnte";
		/*
		 * Cerco la lista  in sessione
		 */
		if (session.getAttribute(lista) != null)
		{
			request.setAttribute(lista, (String)session.getAttribute(lista));
		}
		else
		{
			/*
			 * Se non c'è lo carico tramite il WS e poi lo metto in sessione
			 */
			in = new ConfigUtenteTipoServizioEnteSearchRequest2();
			in.setCompanyCode("");
			in.setCodiceUtente("");
			in.setChiaveEnte("");
			in.setProcName("");
			
			try {
				res = WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEntes2(in, request);
				if (res != null)
				{
					response = res.getResponse();
					if (response != null)
					{
						xml = response.getListXml();
					}
				}
				request.setAttribute(lista, xml);
				session.setAttribute(lista, xml);
			} catch (com.seda.payer.pgec.webservice.srv.FaultType e) {
				setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
				e.printStackTrace();
			}
		}
	}

   

	@SuppressWarnings("unchecked")
	private void getTributiDDL(HttpServletRequest request, HttpSession session) {
		//creo ddl option:
		ArrayList<Tributo> listaTributi = null;
		List<DdlOption> tributiDDLList  = new ArrayList<DdlOption>();
		TributoDAO tributoDAO;
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//tributoDAO = WalletDAOFactory.getTributoDAO(getWalletDataSource(), getWalletDbSchema());
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			tributoDAO = WalletDAOFactory.getTributoDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			if(!societa.trim().equals("")||!utente.trim().equals("")||!ente.trim().equals("")){
			listaTributi = tributoDAO.listTributoServizio(societa,utente,ente);
			}else{
				listaTributi=new ArrayList<Tributo>();
			}
			
			for (Iterator iterator = listaTributi.iterator(); iterator.hasNext();) {
				Tributo tribito = (Tributo) iterator.next();
				DdlOption optionServizio = new DdlOption();
				optionServizio.setSValue(tribito.getCodiceTributo());
				optionServizio.setSText(tribito.getDescrizioneTributo());
				tributiDDLList.add(optionServizio);
			}
		} catch (DaoException e1) {
			setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
			e1.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		catch (JndiProxyException e1) {
			setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
			e1.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		request.setAttribute("tributiDDLList", tributiDDLList);
		//value selected

		String valueSelected=(String) request.getAttribute("tx_codice_tributo2");
		if(valueSelected!=null){
			request.setAttribute("tx_codice_tributo2", valueSelected);
			request.getSession().setAttribute("tx_codice_tributo2", valueSelected);
		}else{
			valueSelected=(String) request.getSession().getAttribute("tx_codice_tributo2");
			if(valueSelected!=null&&!valueSelected.equals("")){
				request.setAttribute("tx_codice_tributo2", valueSelected);
			}
		}
		
	}
	
	private void getServiziDDL(HttpServletRequest request, HttpSession session) {
		//creo ddl option:
		ArrayList<Servizio> listaServizi = null;
		List<DdlOption> serviziDDLList  = new ArrayList<DdlOption>();
		ServizioDAO servizioDAO;
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//servizioDAO = WalletDAOFactory.getServizioDAO(getWalletDataSource(), getWalletDbSchema());
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			servizioDAO = WalletDAOFactory.getServizioDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			listaServizi = servizioDAO.listServizi();
			
			for (Iterator iterator = listaServizi.iterator(); iterator.hasNext();) {
				Servizio servizio = (Servizio) iterator.next();
				DdlOption optionServizio = new DdlOption();
				optionServizio.setSValue(servizio.getCodiceServizio());
				optionServizio.setSText(servizio.getDescrizioneServizio());
				serviziDDLList.add(optionServizio);
			}
		} catch (DaoException e1) {
			setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
			e1.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		catch (JndiProxyException e1) {
			setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
			e1.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		request.setAttribute("serviziDDLList", serviziDDLList);
		//session.setAttribute("serviziDDLList", serviziDDLList);
	}
	private void getDiscaricabileDDL(HttpServletRequest request,HttpSession session){
		List<DdlOption> serviziDDLList  = new ArrayList<DdlOption>();
		//ddl fixed data
		DdlOption optionServizio = new DdlOption(); 
		//optionServizio.setSText("Tutti");
		//optionServizio.setSValue(" ");
		//serviziDDLList.add(optionServizio);
		//
		optionServizio = new DdlOption();
		optionServizio.setSText("SI");
		optionServizio.setSValue("Y");
		serviziDDLList.add(optionServizio);
		//
		optionServizio = new DdlOption();
		optionServizio.setSText("NO");
		optionServizio.setSValue("N");
		serviziDDLList.add(optionServizio);
		//
		request.setAttribute("flagDDLList", serviziDDLList);
		
		String valueSelected=(String) request.getAttribute("tx_flag_servizio");
		if(valueSelected!=null){
			request.setAttribute("tx_flag_servizio", valueSelected);
			request.getSession().setAttribute("tx_flag_servizio", valueSelected);
		}else{
			valueSelected=(String) request.getSession().getAttribute("tx_flag_servizio");
			if(valueSelected!=null){
				request.setAttribute("tx_flag_servizio", valueSelected);
			}
		}
	}
	
    private void setRequestParameters(HttpServletRequest request){
    	String codice_tributo=(String) request.getAttribute("tx_codice_tributo2");
    	if(codice_tributo!=null&&!codice_tributo.trim().equals("")){
    	request.setAttribute("tx_codice_tributo2",codice_tributo );
    	}
    	
    	
    	
    	String flag_servizio=(String) request.getAttribute("tx_flag_servizio");
    	if(flag_servizio!=null&&!flag_servizio.trim().equals("")){
    	request.setAttribute("tx_flag_servizio",flag_servizio );
    	}
    	
    }
    
    private WalletPageList listaAbilitazioneDiscarichi(HttpServletRequest request){
    	WalletPageList walletPageList=null;
    	AbilitazioneDiscaricoDAO abilitazioneDiscaricoDAO=null;
    	AbilitazioneDiscarico abilitazioneDiscaricoFilter=null;
    	abilitazioneDiscaricoFilter=setFilterFromRequest(request);
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
    	try {
			//inizio LP PG21XX04 Leak
			//abilitazioneDiscaricoDAO=WalletDAOFactory.getAbilitazioneDiscarico(getWalletDataSource(), getWalletDbSchema());
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			abilitazioneDiscaricoDAO = WalletDAOFactory.getAbilitazioneDiscarico(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			walletPageList=abilitazioneDiscaricoDAO.abilitazioneList(abilitazioneDiscaricoFilter, rowsPerPage, pageNumber, "");
		} catch (DaoException e) {
			setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		catch (JndiProxyException e1) {
			setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
			e1.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
    	return walletPageList;
    }
    
    private AbilitazioneDiscarico setFilterFromRequest(HttpServletRequest request){
    	AbilitazioneDiscarico abilitazioneDiscaricoFilter=new AbilitazioneDiscarico();
    	String codiceTributo=(String) request.getAttribute("tx_codice_tributo2");
    	String descrizioneTributo="";
    	String flagAbilitazioneDiscarico="";
    	//recuperiamo il valore dalla dll
    	List<DdlOption> tributiDDLList=(List<DdlOption>) request.getAttribute("tributiDDLList");
    	if(codiceTributo!=null&&!codiceTributo.trim().equalsIgnoreCase("")){
    		int index=getIndexTributo(codiceTributo,tributiDDLList);
    		descrizioneTributo=tributiDDLList.get(index).getSText();
    	}else{
    		codiceTributo="";
    	}
    	//recuperiamo il flag
    	flagAbilitazioneDiscarico=(String) request.getAttribute("tx_flag_servizio");
    	if(flagAbilitazioneDiscarico==null){
    		flagAbilitazioneDiscarico="";
    	}
    	//i valori non definiti sono ""
    	abilitazioneDiscaricoFilter.setCodSoc(societa.trim());
    	abilitazioneDiscaricoFilter.setCuteCute(utente.trim());
    	abilitazioneDiscaricoFilter.setChiaveEnte(ente.trim());
    	abilitazioneDiscaricoFilter.setCodiceTributo(String.valueOf(codiceTributo).trim());
    	abilitazioneDiscaricoFilter.setDescrizioneTributo(descrizioneTributo.trim());
    	abilitazioneDiscaricoFilter.setFlagAbilitazioneDiscarico(flagAbilitazioneDiscarico.trim());
    	return abilitazioneDiscaricoFilter;
    }
    
    private int getIndexTributo(String codiceTributo,List<DdlOption> tributiDDLList){
    	int index=0;
    	for (DdlOption ddlOption : tributiDDLList) {
			if(ddlOption.sValue.equals(codiceTributo)){
				index=tributiDDLList.indexOf(ddlOption);
				break;
			}
		}
    	return index;
    }

}
