package org.seda.payer.manager.defaults.actions;

import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.components.security.CollectionMenu;
import org.seda.payer.manager.components.security.MenuKey;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.application.ApplicationBinderFactory;
import com.seda.j2ee5.maf.core.security.SignOnException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.pgec.webservice.adminusers.dati.ResponseType;
import com.seda.payer.pgec.webservice.adminusers.dati.UserBeanType;
import com.seda.payer.pgec.webservice.adminusers.dati.UserInfoRequestType;
import com.seda.payer.pgec.webservice.adminusers.dati.UserInfoResponseType;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class SceltaProfiloAction extends BaseManagerAction {

	private static String dbSchemaCodSocieta;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException
	{
		HttpSession session = request.getSession();
		String userKey = null;
		UserBean userBean = null;
		
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, session); 
        if(!template.equalsIgnoreCase("regmarche")) {
		//fine LP PG200060

        //inizio - PG190080_001
		dbSchemaCodSocieta = (String)session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		
		ServletContext context = request.getSession(false).getServletContext();
		PropertiesTree propertiesTree = (PropertiesTree) context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		long ggAvvisoScadPassword = 0;
		if(propertiesTree.getProperty(PropertiesPath.ggAvvisoScadenzaPassword.format(dbSchemaCodSocieta))!=null)
			ggAvvisoScadPassword= Long.parseLong(propertiesTree.getProperty(PropertiesPath.ggAvvisoScadenzaPassword.format(dbSchemaCodSocieta)));
		
		request.setAttribute(ManagerKeys.GG_AVVISO_SCADENZA_PASSWORD, ggAvvisoScadPassword);
		session.setAttribute(ManagerKeys.GG_AVVISO_SCADENZA_PASSWORD, ggAvvisoScadPassword);
		//fine - PG190080_001
		//inizio LP PG200060
        }
		//fine LP PG200060
		
		FiredButton firedButton = getFiredButton(request);
		
		//inizio LP PG200060
        if(!template.equalsIgnoreCase("regmarche")) {
		//fine LP PG200060
		//inizio - PG190080_001
		if(firedButton.equals(FiredButton.TX_BUTTON_CANCEL)){
			request.setAttribute(ManagerKeys.GG_SCADENZA_PASSWORD, null);
			session.setAttribute(ManagerKeys.GG_SCADENZA_PASSWORD, null);
		}
		//fine - PG190080_001
		//inizio LP PG200060
        }
		//fine LP PG200060

        
        

		if (firedButton.equals(FiredButton.TX_BUTTON_AVANTI))
		{
			userKey = request.getParameter("profilo");
			
			if(session.getAttribute("listaVisibileProfili")!=null && 
					session.getAttribute("listaVisibileProfili").toString().trim().equals("Y")) {
				session.setAttribute("userBeanInitProf", userKey);
			}
			
			if (userKey != null && !userKey.equals(""))
			{
				if (session.getAttribute(SignOnKeys.USER_BEAN) != null)
					userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
				
				//valorizzazione dello userBean in base alla scelta dell'utente e caricamento del menu
				try {
					setUserBeanAndMenu(userBean, session, Long.parseLong(userKey), false, request);
					
					//rieseguo il bind per associare il nuovo profilo scelto alle varie applicazioni
					ApplicationBinderFactory.instance().getApplicationBinder().bind(request, userBean);

					request.getSession().setAttribute(ManagerKeys.NUMERO_PROFILI, "1");
					
				} catch (SignOnException e) {
					setFormMessage("sceltaProfilo_form", e.getMessage(), request);
				}
			}
			else
				setFormMessage("sceltaProfilo_form", Messages.SELEZIONARE_UN_PROFILO.format(), request);
		}

		System.out.println("profilo prima della grafica");
		return null;
		
	}
	
	public static void setUserBeanAndMenu(UserBean userBean, HttpSession session, Long chiaveUtente, boolean bSingleProfilo, HttpServletRequest request) throws SignOnException
	{

		UserInfoResponseType userInfoResponse = null;
		UserInfoRequestType userInfoRequest = null;
		ResponseType pyResponse = null;
		UserBeanType pyUser = null;
		String[] listaApplicazioni = null;

		//inizio LP PG200060
		String template = getLocTemplateCurrentApplication(request, session); 
        if(!template.equalsIgnoreCase("regmarche")) {
		//fine LP PG200060
		//inizio - PG190080_001
		dbSchemaCodSocieta = (String)session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		
		long ggAvvisoScadPassword=0;
		
		ServletContext context = request.getSession(false).getServletContext();
		PropertiesTree propertiesTree = (PropertiesTree) context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		if(propertiesTree.getProperty(PropertiesPath.ggAvvisoScadenzaPassword.format(dbSchemaCodSocieta))!=null){
			 ggAvvisoScadPassword = Long.parseLong(propertiesTree.getProperty(PropertiesPath.ggAvvisoScadenzaPassword.format(dbSchemaCodSocieta)));
		}
	   
		request.setAttribute(ManagerKeys.GG_AVVISO_SCADENZA_PASSWORD, ggAvvisoScadPassword);
		session.setAttribute(ManagerKeys.GG_AVVISO_SCADENZA_PASSWORD, ggAvvisoScadPassword);
		//fine - PG190080_001
		//inizio LP PG200060
        }
		//fine LP PG200060


		if (userBean == null)
			throw new SignOnException(Messages.ERRORE_BEAN_PROFILAZIONE.format());
		
		/*
		 * Chiamo il WS per recuperare i parametri di profilazione dell'utente
		 */
		try {
			userInfoRequest = new UserInfoRequestType(chiaveUtente);
			userInfoResponse = WSCache.adminUsersServer.getUserInfo(userInfoRequest, request);
		
			pyResponse = userInfoResponse.getResponse();
			/*
			 * Se la risposta e positiva e se gli oggetti non sono nulli
			 * genero lo UserBean e carico il menu applicativo.
			 */
			if(pyResponse.getRetCode().equals("00"))
			{
				pyUser = userInfoResponse.getUserBean();
				listaApplicazioni = userInfoResponse.getApplicazioni();
				if(pyUser != null && listaApplicazioni != null)
				{
					//aggiorno lo userbean con i dati del profilo e delle applicazioni
					userBean.setPyUserBean_ListaAppl(pyUser, listaApplicazioni);
					
					caricaMenuApplicativo(chiaveUtente, session, request);
					
					session.setAttribute(SignOnKeys.USER_BEAN, userBean);
				}
				else
				{
					if(listaApplicazioni == null)
					{
						if (bSingleProfilo)
							throw new SignOnException(Messages.NESSUNA_APPLICAZIONE_ABILITATA_UTENTE.format(pyUser.getUserName()));
						else
							throw new SignOnException(Messages.NESSUNA_APPLICAZIONE_ABILITATA_PROFILO.format(pyUser.getUserName()));
					}
					if(pyUser == null)
						throw new SignOnException(Messages.ERRORE_BEAN_PROFILAZIONE.format());
				}
			}
		} catch (FaultType e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void caricaMenuApplicativo(Long chiaveUtente, HttpSession session, HttpServletRequest request) throws SQLException
	{
		WebRowSet[] menus = CollectionMenu.getMenuLivello123(chiaveUtente, request);
		if (menus != null && menus.length == 3)
		{
				session.setAttribute(MenuKey.MENU_LIVELLO_UNO, (new CollectionMenu(menus[0])).getMenuCollection());
				session.setAttribute(MenuKey.MENU_LIVELLO_DUE, (new CollectionMenu(menus[1])).getMenuCollection());
				session.setAttribute(MenuKey.MENU_LIVELLO_TRE, (new CollectionMenu(menus[2])).getMenuCollection());
		}
	}

//inizio LP PG200060
	private static String getLocTemplateCurrentApplication(HttpServletRequest request, HttpSession session)
	{
		MAFRequest mafReq = new MAFRequest(request);
		String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
		if (applicationName != null)
		{
			UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
			if (userBean != null)
			{
				String templateName = userBean.getTemplate(applicationName);
				if (templateName != null && !templateName.equals(""))
					return templateName;
				else
					return "default";
			}
		}
		else
		{
			//recupero il name del template dal file di properties
			if (ManagerStarter.configuration != null)
			{
				String templateName = ManagerStarter.configuration.getProperty(PropertiesPath.templateBase.format() + request.getServerName());

				if (templateName != null && !templateName.equals(""))
					return templateName;
			}
		}
		return "default";
	}
	//fine LP PG200060
}
