package org.seda.payer.manager.walletconfig.actions;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.j2ee5.jndi.JndiProxy;
import com.seda.j2ee5.jndi.JndiProxyException;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.walletmanager.actions.WalletBaseManagerAction;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.Avviso;
import com.seda.payer.core.wallet.bean.WalletPageList;
import com.seda.payer.core.wallet.dao.GestioneAvvisiDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest2;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;

public class GestioneAvvisiAction extends WalletBaseManagerAction {

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
		try {
			request.setCharacterEncoding("ISO-8859-1");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		super.service(request);
		HttpSession session = request.getSession();
		//ddl società utente ente
		loadSocietaUtenteEnteXml_DDL(request, session);

		//setRequestParameters
		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		//setrequestparameters
		setRequestParameters(request);
		//button selection
		//se è stato messo un messaggio nella session dalla pagina di edit in update lo stampiamo
		
		FiredButton firedButton = getFiredButton(request);

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
                    WalletPageList walletPageListlist = listaAvvisi(request);
					PageInfo pageInfo = walletPageListlist.getPageInfo();
					
					if (walletPageListlist.getRetCode()!="00") {
						setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
					} else {
						if(pageInfo != null)
						{
							if(pageInfo.getNumRows() > 0)
							{
								request.setAttribute("lista_gestione_avvisi", walletPageListlist.getWalletListXml());
								request.setAttribute("lista_gestione_avvisi.pageInfo", pageInfo);
							}
							else {
								request.setAttribute("lista_gestione_avvisi", null);
								
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
    
    private WalletPageList listaAvvisi(HttpServletRequest request){
    	WalletPageList walletPageList=null;
    	GestioneAvvisiDAO gestioneAvvisiDAO=null;
    	Avviso avviso=null;
    	avviso=setFilterFromRequest(request);
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
    	try {
			//inizio LP PG21XX04 Leak
			//gestioneAvvisiDAO=WalletDAOFactory.getGestioneAvvisi(getWalletDataSource(), getWalletDbSchema());
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			gestioneAvvisiDAO=WalletDAOFactory.getGestioneAvvisi(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			walletPageList=gestioneAvvisiDAO.avvisiList(avviso, rowsPerPage, pageNumber, "");
		} catch (DaoException e) {
			setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		catch (JndiProxyException e) {
			setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
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
    	return walletPageList;
    }
    
    private Avviso setFilterFromRequest(HttpServletRequest request){
    	Avviso avviso=new Avviso();

    	//i valori non definiti sono ""
    	avviso.setCuteCute(utente.trim());
    	avviso.setCodSoc(societa.trim());
    	avviso.setChiaveEnte(ente.trim());

    	return avviso;
    }
    
}
