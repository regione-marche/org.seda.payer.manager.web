package org.seda.payer.manager.blackboxconfig.actions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.walletmanager.actions.WalletBaseManagerAction;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.bean.BlackBoxPagelist;
import com.seda.payer.core.bean.ConfigurazioneBlackBox;
import com.seda.payer.core.dao.BlackBoxDAOFactory;
import com.seda.payer.core.dao.ConfigurazioneBlackBoxDao;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.ConfigurazioneSolleciti;
import com.seda.payer.core.wallet.bean.WalletPageList;
import com.seda.payer.core.wallet.dao.ConfigurazioneSollecitiDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;

public class BlackBoxAction extends BlackBoxBaseManagerAction{

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
		
		switch(firedButton)
		{
			case TX_BUTTON_CERCA: 
				try {
					dividiSocUtenteEnte(request);
					//loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
					//ConfigurazioneBlackBox configurazioneBlackBox ;				
					BlackBoxPagelist blackBoxPageListlist = blackboxList(request);
					
					PageInfo pageInfo = blackBoxPageListlist.getPageInfo();
					
					if (blackBoxPageListlist.getRetCode()!="00") {
						setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
					} else {
						if(pageInfo != null)
						{
							if(pageInfo.getNumRows() > 0)
							{
								request.setAttribute("lista_blackbox", blackBoxPageListlist.getBlackboxListXml());
								request.setAttribute("lista_blackbox.pageInfo", pageInfo);
							}
							else {
								request.setAttribute("lista_blackbox", null);
								
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
 
		

	private BlackBoxPagelist blackboxList(HttpServletRequest request) {
		ConfigurazioneBlackBoxDao configurazioneBlackBoxDAO;
		ConfigurazioneBlackBox configurazioneBlackBox = new ConfigurazioneBlackBox();
		configurazioneBlackBox.setCodiceEnte(chiaveEnteda5);
		configurazioneBlackBox.setCodiceIdentificativoDominio((String)request.getAttribute("tx_idDominio_s"));
		configurazioneBlackBox.setFlagIuv((String)request.getAttribute("tx_calcoloIuv_s"));
		BlackBoxPagelist balckboxPageList = new BlackBoxPagelist();
		//inizio LP PG21XX04 Leak
		Connection connection = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(getBlackBoxDataSource(), getBlackBoxDbSchema());
			connection = getBlackBoxDataSource().getConnection();
			configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(connection, getBlackBoxDbSchema());
			//fine LP PG21XX04 Leak
			
			balckboxPageList = configurazioneBlackBoxDAO.blackboxList(configurazioneBlackBox, rowsPerPage, pageNumber,"");
		} catch (DaoException e1) {
			e1.printStackTrace();
		//inizio LP PG21XX04 Leak
		//}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		
		return balckboxPageList;
		
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
	
}

