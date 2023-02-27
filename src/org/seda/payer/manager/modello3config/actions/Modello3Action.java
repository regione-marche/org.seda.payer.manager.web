package org.seda.payer.manager.modello3config.actions;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.bean.ConfigurazioneModello3;
import com.seda.payer.core.bean.ConfigurazioneModello3Pagelist;
import com.seda.payer.core.dao.ConfigurazioneModello3DAOFactory;
import com.seda.payer.core.dao.ConfigurazioneModello3Dao;
import com.seda.payer.core.exception.DaoException;

public class Modello3Action extends Modello3BaseManagerAction{

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber; 
	private String username;
	private String codiceSocieta="";
	private String cutecute="";
	private String chiaveEnte="";
	private String chiaveEnteda5="";
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		
		acquisisciFiltriRicerca(request,session); 	//PREJAVA18_LUCAP_01092020
		
		FiredButton firedButton = getFiredButton(request);
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) 
			setProfile(request);
		// carico le DDL della società, cutecute ed Ente
		
		HttpSession sessionlocal = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		username = user.getUserName();
		if ((String) session.getAttribute("recordInsert") != null) 
		{
			firedButton = firedButton.TX_BUTTON_CERCA; 
			if(session.getAttribute("recordInsert").toString().equals("OK"))
				setFormMessage("form_selezione", Messages.INS_OK.format(), request);
			else
				setFormMessage("form_selezione", session.getAttribute("messaggio.recordInsert").toString(), request);
			
			session.removeAttribute("recordInsert");
		}
		
		if ((String) session.getAttribute("recordUpdate") != null) 
		{
			firedButton = firedButton.TX_BUTTON_CERCA;
			if(session.getAttribute("recordUpdate").toString().equals("OK"))
				setFormMessage("form_selezione", Messages.UPDT_OK.format(), request);
			else
				setFormMessage("form_selezione", session.getAttribute("messaggio.recordUpdate").toString(), request);
			
			session.removeAttribute("recordUpdate");
		}
		if ((String) session.getAttribute("recordDelete") != null) 
		{
			firedButton = firedButton.TX_BUTTON_CERCA;
			if(session.getAttribute("recordDelete").toString().equals("OK"))
				setFormMessage("form_selezione", Messages.CANC_OK.format(), request);
			else
				setFormMessage("form_selezione", session.getAttribute("messaggio.recordDelete").toString(), request);
			
			session.removeAttribute("recordDelete");
			
		}
		
		
		//loadStaticXml_DDL(request, session);
		loadSocietaUtenteEnteXml_DDL(request, session);
		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		
		setFiltriRicerca(request, session); 	//PREJAVA18_LUCAP_01092020
		
		switch(firedButton)
		{
			case TX_BUTTON_CERCA: 
				try {
					
					dividiSocUtenteEnte(request);
					//loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);

					ConfigurazioneModello3Pagelist configurazioneModello3PageListlist = getConfigurazioneModello3List(request);
					
					PageInfo pageInfo = configurazioneModello3PageListlist.getPageInfo();
					
					if (configurazioneModello3PageListlist.getRetCode()!="00") {
						setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
					} else {
						if(pageInfo != null)
						{
							if(pageInfo.getNumRows() > 0)
							{
								request.setAttribute("lista_configurazionemodello3", configurazioneModello3PageListlist.getConfigurazioneModello3ListXml());
								request.setAttribute("lista_configurazionemodello3.pageInfo", pageInfo);
							}
							else {
								request.setAttribute("lista_configurazionemodello3", null);
								
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
				
				setProfile(request);				
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				break;
				
			case TX_BUTTON_SOCIETA_CHANGED:
				String societa = getParamCodiceSocieta();
				loadProvinciaXml_DDL(request, session, societa,true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				loadListaGatewayXml_DDL(request, session, societa, (societa.equals("") ? "" : getParamCodiceUtente()), true);
				request.setAttribute("tx_provincia","");
				request.setAttribute("tx_UtenteEnte", "");
				break;
				
			case TX_BUTTON_PROVINCIA_CHANGED:
				loadProvinciaXml_DDL(request, session, null,false);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				request.setAttribute("tx_UtenteEnte", "");
				break;
			
			case TX_BUTTON_ENTE_CHANGED:
				loadProvinciaXml_DDL(request, session, "",false);
				LoadListaUtentiEntiXml_DDL(request, session, "" , "", "", "", false);
				//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				break;
				
				
			case TX_BUTTON_NULL: 
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				break;
				
			case TX_BUTTON_STAMPA:
				break;
				
			case TX_BUTTON_DOWNLOAD:
				break;
		}
		
		return null;
	}
 
		

	private ConfigurazioneModello3Pagelist getConfigurazioneModello3List(HttpServletRequest request) {
		ConfigurazioneModello3Dao configurazioneModello3DAO;
		ConfigurazioneModello3 configurazioneModello3 = new ConfigurazioneModello3();
//		configurazioneModello3.setCodiceEnte(chiaveEnteda5);
//		configurazioneModello3.setCodiceIdentificativoDominio((String)request.getAttribute("tx_idDominio_s"));
//		configurazioneModello3.setFlagIuv((String)request.getAttribute("tx_calcoloIuv_s"));
		configurazioneModello3.setCodiceSocieta(codiceSocieta);
		configurazioneModello3.setCodiceUtente(cutecute);
		configurazioneModello3.setChiaveEnte(chiaveEnte);
		configurazioneModello3.setCodiceIdentificativoDominio((String) request.getAttribute("tx_idDominio_s"));
		configurazioneModello3.setAuxDigit((String) request.getAttribute("tx_auxDigit"));
		configurazioneModello3.setCodiceSegregazione((String) request.getAttribute("tx_codiceSegregazione"));
		configurazioneModello3.setCarattereDiServizio((String) request.getAttribute("tx_carattereDiServizio")); //SVILUPPO_002_SB
		
		ConfigurazioneModello3Pagelist configurazioneModello3PageList = new ConfigurazioneModello3Pagelist();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneModello3DAO = ConfigurazioneModello3DAOFactory.getConfigurazioneModello3(getConfigurazioneModello3DataSource(), getConfigurazioneModello3DbSchema());
			conn = getConfigurazioneModello3DataSource().getConnection();
			configurazioneModello3DAO = ConfigurazioneModello3DAOFactory.getConfigurazioneModello3(conn, getConfigurazioneModello3DbSchema());
			//fine LP PG21XX04 Leak
			configurazioneModello3PageList = configurazioneModello3DAO.configurazioneModello3List(configurazioneModello3, rowsPerPage, pageNumber,"");
		} catch (DaoException e1) {
			e1.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		return configurazioneModello3PageList;
		
	}
//	private void dividiSocUtenteEnte(HttpServletRequest request) {
//		// TODO Auto-generated method stub
//		if(request.getAttribute("ddlSocietaUtenteEnte")!=null)
//		{
//			String ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
//			if (!ddlSocietaUtenteEnte.equals(""))
//			{
//				String[] codici = ddlSocietaUtenteEnte.split("\\|");
//				codiceSocieta = codici[0];
//				 = codici[1];
//				chiaveEnte = codici[2];
//				chiaveEnte = codici[3];
//				request.setAttribute("tx_societa", codiceSocieta);
//				request.setAttribute("tx_utente", chiaveEnte);
//				request.setAttribute("tx_ente", chiaveEnte);
//				request.setAttribute("tx_ente", chiaveEnte.substring(1, 6));
//			}
//		
//		}
//	}
	
	private void dividiSocUtenteEnte(HttpServletRequest request) {
		// TODO Auto-generated method stub
		if(request.getAttribute("ddlSocietaUtenteEnte")!=null)
		{
			String ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
			String[] codici = ddlSocietaUtenteEnte.split("\\|");
			if (!ddlSocietaUtenteEnte.equals("")&&codici.length>0) 
			{
				
				codiceSocieta = codici[0];
				cutecute = codici[1];
				chiaveEnte = codici[2];
				chiaveEnteda5 = codici[3].substring(1, 6);
				request.setAttribute("tx_societa", codiceSocieta);
				request.setAttribute("tx_utente", cutecute);
				request.setAttribute("tx_ente", chiaveEnte);
				request.setAttribute("tx_enteda5", chiaveEnteda5);
				
				
			}
		
		}
	}
	
	//PREJAVA18_LUCAP_01092020
	private void acquisisciFiltriRicerca(HttpServletRequest request, HttpSession session) {
		session.setAttribute("tx_idDominio_s", (String)(request.getAttribute("tx_idDominio_s")==null?(session.getAttribute("tx_idDominio_s")==null?"":(session.getAttribute("tx_idDominio_s"))):request.getAttribute("tx_idDominio_s")));
		session.setAttribute("tx_auxDigit", (String)(request.getAttribute("tx_auxDigit")==null?(session.getAttribute("tx_auxDigit")==null?"":(session.getAttribute("tx_auxDigit"))):request.getAttribute("tx_auxDigit")));
		session.setAttribute("tx_codiceSegregazione", (String)(request.getAttribute("tx_codiceSegregazione")==null?(session.getAttribute("tx_codiceSegregazione")==null?"":(session.getAttribute("tx_codiceSegregazione"))):request.getAttribute("tx_codiceSegregazione")));
		session.setAttribute("tx_carattereDiServizio", (String)(request.getAttribute("tx_carattereDiServizio")==null?(session.getAttribute("tx_carattereDiServizio")==null?"":(session.getAttribute("tx_carattereDiServizio"))):request.getAttribute("tx_carattereDiServizio")));
		session.setAttribute("ddlSocietaUtenteEnte", (String)(request.getAttribute("ddlSocietaUtenteEnte")==null?(session.getAttribute("ddlSocietaUtenteEnte")==null?"":(session.getAttribute("ddlSocietaUtenteEnte"))):request.getAttribute("ddlSocietaUtenteEnte")));
	}
	private void setFiltriRicerca(HttpServletRequest request, HttpSession session) {
		request.setAttribute("tx_idDominio_s", session.getAttribute("tx_idDominio_s"));
		request.setAttribute("tx_auxDigit", session.getAttribute("tx_auxDigit"));
		request.setAttribute("tx_codiceSegregazione", session.getAttribute("tx_codiceSegregazione"));
		request.setAttribute("tx_carattereDiServizio", session.getAttribute("tx_carattereDiServizio"));
		request.setAttribute("ddlSocietaUtenteEnte", session.getAttribute("ddlSocietaUtenteEnte"));
	}
	//FINE PREJAVA18_LUCAP_01092020
	
}

