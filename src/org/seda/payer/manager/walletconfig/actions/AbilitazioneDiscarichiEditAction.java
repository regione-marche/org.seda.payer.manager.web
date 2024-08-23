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
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.walletmanager.actions.WalletBaseManagerAction;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.AbilitazioneDiscarico;
import com.seda.payer.core.wallet.bean.EsitoRisposte;
import com.seda.payer.core.wallet.bean.Tributo;
import com.seda.payer.core.wallet.dao.AbilitazioneDiscaricoDAO;
import com.seda.payer.core.wallet.dao.TributoDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest2;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;
import com.seda.tag.core.DdlOption;

public class AbilitazioneDiscarichiEditAction extends WalletBaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String societa = "";
	private String utente = "";
	private String ente = "";
	private String codiceTributo="";
	private String operatore = "";
	HttpSession session=null;

	@Override
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		session = request.getSession();
		HttpSession sessionlocal = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		//ddl società utente ente
		loadSocietaUtenteEnteXml_DDL(request, session);
		//ddl tributi
		getTributiDDL(request,session);
		//ddl discaricabile
		//setFormParameters(request);
		getDiscaricabileDDL(request, session);
		operatore = user.getUserName();
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
		case TX_BUTTON_ADD:
		//è stato premuto nuovo	
		resetFormValues(request);	
		setProfile(request);
		
		
		break;
		case TX_BUTTON_AGGIUNGI:
		//salvataggio
		request.setAttribute("codop", "add");	
		doSave(request);
		
		//è stato premuto salva	
		
		break;
		case TX_BUTTON_AGGIORNA:
		//salvataggio
		doUpdate(request);
		setFormParameters(request);
		request.setAttribute("vista","abilitazionediscarichi");
		//è stato premuto salva	
		break;
		case TX_BUTTON_INDIETRO:
		request.setAttribute("vista","abilitazionediscarichi");
		break;	
		case TX_BUTTON_NULL:
			String codOp=(String) request.getAttribute("codop");
			if(codOp!=null&&codOp.trim().equals("edit")){
			request.setAttribute("codop", "edit");	
			setFormParameters(request);
			}else{
			request.setAttribute("codop", "add");	
			}
			break;
		case TX_BUTTON_CANCEL:	
			saveCancelParameters(request);
			
		break;	
		case TX_BUTTON_DELETE:
			doDelete(request);
		break;
		}
		
		return null;
	}
	
	

	private void resetFormValues(HttpServletRequest request){
		request.setAttribute("tx_codice_tributo2",null );
		request.setAttribute("tx_flag_servizio",null );
		request.setAttribute("ddlSocietaUtenteEnte", null);
		
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
		
	}
	
	protected void loadSocietaUtenteEnteXml_DDL(HttpServletRequest request,HttpSession session)
	{
		String societaUtenteEnte=(String) request.getAttribute("ddlSocietaUtenteEnte");
		if(societaUtenteEnte!=null&&!societaUtenteEnte.trim().equals("")){
			String [] splitString=societaUtenteEnte.split("\\|");
			societa=splitString[0];
			utente=splitString[1];
			ente=splitString[2];
			
		}else{
			societaUtenteEnte=(String) request.getAttribute("hiddenDdlSocietaUtenteEnte");
			if(societaUtenteEnte!=null&&!societaUtenteEnte.trim().equals("")){
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
	
	private void doSave(HttpServletRequest request){
		AbilitazioneDiscaricoDAO abilitazioneDiscaricoDAO=null;
    	AbilitazioneDiscarico abilitazioneDiscaricoFilter=null;
    	abilitazioneDiscaricoFilter=setFilterFromRequest(request);
    	String es="";
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
    	try {
			//inizio LP PG21XX04 Leak
			//abilitazioneDiscaricoDAO=WalletDAOFactory.getAbilitazioneDiscarico(getWalletDataSource(), getWalletDbSchema());
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			abilitazioneDiscaricoDAO=WalletDAOFactory.getAbilitazioneDiscarico(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			es=abilitazioneDiscaricoDAO.insertAbilitazione(abilitazioneDiscaricoFilter);
			if(es.trim().equals("OK")){
			setFormMessage("form_selezione", "Record inserito correttamente", request);
			}else{
		    setFormMessage("form_selezione", "Configurazione già presente", request);	
			}
		} catch (DaoException e) {
			setFormMessage("form_selezione", e.getErrorMessage(), request);
			e.printStackTrace();
		}	
		//inizio LP PG21XX04 Leak
		catch (JndiProxyException e) {
			setFormMessage("form_selezione", e.getMessage(), request);
			e.printStackTrace();
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
	}
	
	private void doUpdate(HttpServletRequest request){
		AbilitazioneDiscaricoDAO abilitazioneDiscaricoDAO=null;
    	AbilitazioneDiscarico abilitazioneDiscaricoFilter=null;
    	abilitazioneDiscaricoFilter=setFilterFromRequestUpdate(request);
    	int recordAggiornati=0;
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
    	try {
			//inizio LP PG21XX04 Leak
			//abilitazioneDiscaricoDAO=WalletDAOFactory.getAbilitazioneDiscarico(getWalletDataSource(), getWalletDbSchema());
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			abilitazioneDiscaricoDAO=WalletDAOFactory.getAbilitazioneDiscarico(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			recordAggiornati=abilitazioneDiscaricoDAO.updateAbilitazione(abilitazioneDiscaricoFilter);
			if(recordAggiornati>0){
			setFormMessage("form_selezione", "Aggiornamento effettuato correttamente", request);
			session.setAttribute("updateEsito", "OK");
			}else{
		    setFormMessage("form_selezione", "Aggiornamento non effettuato", request);	
		    session.setAttribute("updateEsito", "KO");
			}
		} catch (DaoException e) {
			setFormMessage("form_selezione", e.getErrorMessage(), request);
			session.setAttribute("updateEsito", "KO");
			e.printStackTrace();
		}	
		//inizio LP PG21XX04 Leak
		catch (JndiProxyException e) {
			setFormMessage("form_selezione", e.getMessage(), request);
			session.setAttribute("updateEsito", "KO");
			e.printStackTrace();
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
	}
	
	
    private void doDelete(HttpServletRequest request){
    	AbilitazioneDiscaricoDAO abilitazioneDiscaricoDAO=null;
    	AbilitazioneDiscarico abilitazioneDiscaricoFilter=null;
    	abilitazioneDiscaricoFilter=setFilterFromRequestDelete(request);
    	EsitoRisposte esito=null;
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
    	try {
			//inizio LP PG21XX04 Leak
			//abilitazioneDiscaricoDAO=WalletDAOFactory.getAbilitazioneDiscarico(getWalletDataSource(), getWalletDbSchema());
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			abilitazioneDiscaricoDAO=WalletDAOFactory.getAbilitazioneDiscarico(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			esito=abilitazioneDiscaricoDAO.deleteAbilitazione(abilitazioneDiscaricoFilter);
			if(esito.getCodiceMessaggio().equals("OK"))
			{
				session.setAttribute("recordDelete", "OK");
				//setFormMessage("form_selezione", Messages.CANC_OK.format(), request);
			}
			else
			{
				session.setAttribute("recordDelete", "KO");
				session.setAttribute("messaggio.recordDelete", esito.getDescrizioneMessaggio());
				//setFormMessage("form_selezione", esito.getDescrizioneMessaggio(), request);
			}
		} catch (DaoException e) {
			session.setAttribute("recordDelete", "KO");
			//inizio LP PG21XX04 Leak
			//session.setAttribute("messaggio.recordDelete", esito.getDescrizioneMessaggio());
			session.setAttribute("messaggio.recordDelete", e.getErrorMessage());
			//fine LP PG21XX04 Leak
			e.printStackTrace();
		}	
		//inizio LP PG21XX04 Leak
		catch (JndiProxyException e) {
			session.setAttribute("recordDelete", "KO");
			session.setAttribute("messaggio.recordDelete", e.getMessage());
			e.printStackTrace();
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
	}
	
    	
   
	
	private AbilitazioneDiscarico setFilterFromRequest(HttpServletRequest request){
    	AbilitazioneDiscarico abilitazioneDiscaricoFilter=new AbilitazioneDiscarico();
    	String codiceTributo=(String) request.getAttribute("tx_codice_tributo2");
    	String descrizioneTributo="";
    	String flagAbilitazioneDiscarico="";
    	//recuperiamo il valore dalla dll
    	List<DdlOption> tributiDDLList=(List<DdlOption>) request.getAttribute("tributiDDLList");
    	if(!tributiDDLList.isEmpty()&&codiceTributo!=null&&!codiceTributo.trim().equalsIgnoreCase("")){
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
    	abilitazioneDiscaricoFilter.setOperatoreInserimento(operatore);
    	return abilitazioneDiscaricoFilter;
    }
	
	private AbilitazioneDiscarico setFilterFromRequestUpdate(HttpServletRequest request){
    	AbilitazioneDiscarico abilitazioneDiscaricoFilter=new AbilitazioneDiscarico();
    	String codiceTributo=(String) request.getAttribute("hiddenTx_codice_tributo2");
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
    	abilitazioneDiscaricoFilter.setOperatoreInserimento(operatore);
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
	 
	 private void getDiscaricabileDDL(HttpServletRequest request,HttpSession session){
			List<DdlOption> serviziDDLList  = new ArrayList<DdlOption>();
			//ddl fixed data
			DdlOption optionServizio = new DdlOption();
			
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
			
		}
	 
	 
	 
    private void setFormParameters(HttpServletRequest request){
    	String codice_tributo=(String) request.getParameter("tx_codice_tributo2");
    	if(codice_tributo!=null&&!codice_tributo.trim().equals("")){
    	request.setAttribute("tx_codice_tributo2",codice_tributo );
    	}else{
    	codice_tributo=(String) request.getParameter("hiddenTx_codice_tributo2");
    	request.setAttribute("tx_codice_tributo2",codice_tributo );	
    	}
    	
    	String flag_servizio=(String) request.getAttribute("tx_flag_servizio");
    	if(flag_servizio!=null&&!flag_servizio.trim().equals("")){
    	request.setAttribute("tx_flag_servizio",flag_servizio );
    	}
    	
    	String ddlSocietaUtenteEnte=(String) request.getParameter("ddlSocietaUtenteEnte");
    	if(ddlSocietaUtenteEnte!=null&&!ddlSocietaUtenteEnte.trim().equals("")){
    	request.setAttribute("ddlSocietaUtenteEnte",ddlSocietaUtenteEnte );
    	}else{
    	ddlSocietaUtenteEnte=(String) request.getParameter("hiddenDdlSocietaUtenteEnte");
    	request.setAttribute("ddlSocietaUtenteEnte",ddlSocietaUtenteEnte );
    	}	
    	
    	request.setAttribute("codop", "edit");
    	
    }
    
    private void saveCancelParameters(HttpServletRequest request) {
		String ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
		if (!ddlSocietaUtenteEnte.equals(""))
		{
			String[] codici = ddlSocietaUtenteEnte.split("\\|");
			societa = codici[0];
			utente = codici[1];
			ente = codici[2];
			request.setAttribute("tx_societa", societa);
			request.setAttribute("tx_utente", utente);
			request.setAttribute("tx_ente", ente);
			String codice_tributo=(String) request.getParameter("tx_codice_tributo2");
	    	if(codice_tributo!=null&&!codice_tributo.trim().equals("")){
	    	request.setAttribute("tx_codice_tributo2",codice_tributo );
	    	}
		}
	}
    
    private AbilitazioneDiscarico setFilterFromRequestDelete(HttpServletRequest request){
    	societa=(String) request.getAttribute("tx_societa");
    	utente=(String)request.getAttribute("tx_utente");
    	ente=(String) request.getAttribute("tx_ente");
    	codiceTributo=(String)request.getAttribute("tx_codice_tributo2");
    	AbilitazioneDiscarico abilitazioneDiscaricoFilter=new AbilitazioneDiscarico();
    	abilitazioneDiscaricoFilter.setCodSoc(societa.trim());
    	abilitazioneDiscaricoFilter.setCuteCute(utente.trim());
    	abilitazioneDiscaricoFilter.setChiaveEnte(ente.trim());
    	abilitazioneDiscaricoFilter.setCodiceTributo(String.valueOf(codiceTributo).trim());
    	abilitazioneDiscaricoFilter.setDescrizioneTributo("");
    	abilitazioneDiscaricoFilter.setFlagAbilitazioneDiscarico("");
    	abilitazioneDiscaricoFilter.setOperatoreInserimento(operatore);
    	return abilitazioneDiscaricoFilter;
    }
    
    
}
