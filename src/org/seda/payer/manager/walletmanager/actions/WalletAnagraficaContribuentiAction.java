package org.seda.payer.manager.walletmanager.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
//import org.seda.payer.manager.util.Generics;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.Wallet;
import com.seda.payer.core.wallet.dao.WalletDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import com.seda.data.spi.PageInfo;
import com.seda.payer.core.wallet.bean.Servizio;
import com.seda.payer.core.wallet.bean.WalletPageList;
import com.seda.payer.core.wallet.dao.ServizioDAO;
import com.seda.tag.core.DdlOption;

import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;

	/*
	 * --------------------------------------------------------------------
	 * VBRUNO - 5 agosto 2011
	 * Modificato il caricamento della DDL UtentiEnti per tenere conto
	 * dell'opzione "Multiutente" dello user loggato
	 * -------------------------------------------------------------------
	 */

	public class WalletAnagraficaContribuentiAction extends WalletBaseManagerAction {

		private static final long serialVersionUID = 1L;
		private String operatore="";
		private String codiceSocieta="";
		private String cutecute="";
		private String chiaveEnte="";
		
		public Object service(HttpServletRequest request) throws ActionException {
			super.service(request);
			
			
			//
			
			HttpSession session = request.getSession();
			/*
			 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
			 */
			replyAttributes(false, request,"pageNumber","rowsPerPage","order");
			/*
			 * Individuo la richiesta dell'utente
			 */
			FiredButton firedButton = getFiredButton(request);
			/*
			 * Chiamo il metodo per i settaggi relativi al profilo utente
			 */
			if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) 
				setProfile(request);
			/*
			 * Carico le DDl statiche
			 */
			//loadStaticXml_DDL(request, session);
			//getServiziDDL(request, session);
			
			loadSocietaUtenteEnteXml_DDL(request, session);
			
			UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
			operatore = user.getUserName();
			
			if ((String) session.getAttribute("recordInsert") != null) 
			{
				
				if(session.getAttribute("recordInsert").toString().equals("OK"))
					setFormMessage("form_selezione", Messages.INS_OK.format(), request);
				else
					setFormMessage("form_selezione", session.getAttribute("messaggio.recordInsert").toString(), request);
				
				session.removeAttribute("recordInsert");
			}
			
			if ((String) session.getAttribute("recordUpdate") != null) 
			{
				
				if(session.getAttribute("recordUpdate").toString().equals("OK"))
					setFormMessage("form_selezione", Messages.UPDT_OK.format(), request);
				else
					setFormMessage("form_selezione", session.getAttribute("messaggio.recordUpdate").toString(), request);
				
				session.removeAttribute("recordUpdate");
			}
			if ((String) session.getAttribute("recordDelete") != null) 
			{
				
				if(session.getAttribute("recordDelete").toString().equals("OK"))
					setFormMessage("form_selezione", Messages.CANC_OK.format(), request);
				else
					setFormMessage("form_selezione", session.getAttribute("messaggio.recordDelete").toString(), request);
				
				session.removeAttribute("recordDelete");
			}
			
			
			switch(firedButton)
			{
				case TX_BUTTON_CERCA: 
					try {
							dividiSocUtenteEnte(request);
							//loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
							//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), false);
							//loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), false);
							//loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), false);
							//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);
							//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), provincia, getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), false);
							//LoadListaUtentiEntiXml_DDL(request, session, societa, provincia, utenteEnte, (isMultiUtenteEnabled() ? "" : utenteEnte), false);
							WalletPageList walletPageListlist = ListAnagraficaContribuenti(request,false);
							
							PageInfo pageInfo = walletPageListlist.getPageInfo();

							if(pageInfo != null)
							{
								if(pageInfo.getNumRows() > 0)
								{
									session.setAttribute("numRows",pageInfo.getNumRows());
									request.setAttribute("lista_wallet", walletPageListlist.getWalletListXml());
									request.setAttribute("lista_wallet.pageInfo", pageInfo);
								}
								else setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
							}
							else { 
								setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
							}

						} catch(Exception e) {
							setErrorMessage(e.getMessage());
							e.printStackTrace();
						}
						break;

					
				case TX_BUTTON_RESET:
					resetParametri(request);
					request.setAttribute("tx_idwallet", null);
					request.setAttribute("tx_codicefiscalegenitore", null);
					request.setAttribute("tx_denominazione", null);
					
					setProfile(request);
					loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
					//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
					loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
					loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
					break;
					
				case TX_BUTTON_SOCIETA_CHANGED:
					String societa = getParamCodiceSocieta();
					loadProvinciaXml_DDL(request, session, societa,true);
					loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
					loadListaGatewayXml_DDL(request, session, societa, (societa.equals("") ? "" : getParamCodiceUtente()), true);
					break;
					
				case TX_BUTTON_PROVINCIA_CHANGED:
					loadProvinciaXml_DDL(request, session, null,false);
					//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, (siglaProvincia.equals("") ? "" : getParamCodiceEnte()), (siglaProvincia.equals("") ? "" : getParamCodiceUtente()), true);
					//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, (siglaProvincia.equals("") ? "" : getParamCodiceEnte()), (siglaProvincia.equals("") ? "" : (isMultiUtenteEnabled() ? "" : getParamCodiceUtente())), true);
					loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
					loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
					break;
				
				case TX_BUTTON_ENTE_CHANGED:
					loadProvinciaXml_DDL(request, session, "",false);
					LoadListaUtentiEntiXml_DDL(request, session, "" , "", "", "", false);
					//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
					// GC_2013_06_24
					//loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);					
					//loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
					break;
					
					
				case TX_BUTTON_NULL: 
					loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
					//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
					loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
					loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
					//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
					//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), true);
					break;
					
				case TX_BUTTON_STAMPA:
					break;
					
				case TX_BUTTON_DOWNLOAD:
					try {
						request.setAttribute("download", "Y");
						dividiSocUtenteEnte(request);
						loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
						
						Integer maxRigheDownload = (Integer) context.getAttribute(ManagerKeys.MAXRIGHE_DOWNLOAD_BORSELLINO);
						if ((Integer) session.getAttribute("numRows") <= maxRigheDownload ){
							ListAnagraficaContribuenti(request,true);
						} else {
							setFormMessage("form_selezione", Messages.SUPERATO_MAXRIGHE.format(), request);
							request.setAttribute("download", "N");
							break;
						}
						
						
						
						
						
						
					} catch(Exception e) {
						setErrorMessage(e.getMessage());
						e.printStackTrace();
					}
					break;
				
				
			}
			return null;
		}
	 
			

		
		@SuppressWarnings("unchecked")
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
				conn = getWalletDataSource().getConnection();
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
				e1.printStackTrace();
			}
			//inizio LP PG21XX04 Leak
			catch (SQLException e1) {
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
		

		
		private WalletPageList ListAnagraficaContribuenti(HttpServletRequest request, boolean download) {
			WalletDAO walletDAO;
			Wallet wallet = new Wallet();
			//wallet.setCodiceSocieta(getParamCodiceSocieta());
			//wallet.setCuteCute(getParamCodiceUtente()==null ? "" : getParamCodiceUtente());
			//wallet.setChiaveEnte(getParamCodiceEnte()==null ? "" : getParamCodiceEnte());
			wallet.setCodiceSocieta(codiceSocieta);
			wallet.setCuteCute(cutecute);
			wallet.setChiaveEnte(chiaveEnte);
			wallet.setIdWallet((String)(request.getAttribute("tx_idwallet")==null ? "" : request.getAttribute("tx_idwallet")));
			wallet.setCodiceFiscaleGenitore((String)(request.getAttribute("tx_codicefiscalegenitore")==null ? "" : request.getAttribute("tx_codicefiscalegenitore")));
			wallet.setDenominazioneGenitore((String)(request.getAttribute("tx_denominazione")==null ? "" : request.getAttribute("tx_denominazione")));
			wallet.setAnagraficaDaBonificare((String)(request.getAttribute("tx_anagrafica_da_bonificare")==null ? "" : (String)request.getAttribute("tx_anagrafica_da_bonificare")));
			wallet.setCodiceRid((String)(request.getAttribute("tx_sepa")==null ? "" : (String)request.getAttribute("tx_sepa")));
			if(request.getAttribute("tx_flagAttivazione")!=null && !request.getAttribute("tx_flagAttivazione").equals(""))
			 {
				if(request.getAttribute("tx_flagAttivazione").equals("N"))
					wallet.setFlagPrimoAccesso(false);
				else
					wallet.setFlagPrimoAccesso(true);
			 }
			wallet.setAttribute("IDBOLLETTINO", (String)(request.getAttribute("tx_idBollettino")==null ? "" : (String)request.getAttribute("tx_idBollettino")));
			// paginazione ed ordinamento
			int rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
			int pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
			String order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");

			
			WalletPageList walletPageList = new WalletPageList();
			//inizio LP PG21XX04 Leak
			Connection conn = null;
			//fine LP PG21XX04 Leak
			try {
				//inizio LP PG21XX04 Leak
				//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
				conn = getWalletDataSource().getConnection();
				walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
				//fine LP PG21XX04 Leak
				if (download) {
					String walletListCsv = walletDAO.walletListAnagraficaContribuentiCsv(wallet, 0, 0, order).toString();
					Calendar cal = Calendar.getInstance();
					String timestamp = GenericsDateNumbers.formatCalendarData(cal, "yyyyMMdd_HHmmss");
					request.setAttribute("nome_file_csv",timestamp + "_MonitoraggioWalletAnag.txt");
					request.setAttribute("monitoraggio_walletAnag_csv", walletListCsv);
					
				} else  {
				
					walletPageList = walletDAO.walletListAnagraficaContribuenti(wallet, rowsPerPage, pageNumber, order);
				}
			} catch (DaoException e1) {
				e1.printStackTrace();
			}
			//inizio LP PG21XX04 Leak
			catch (SQLException e1) {
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

		private void dividiSocUtenteEnte(HttpServletRequest request) {
			// TODO Auto-generated method stub
			if(request.getAttribute("ddlSocietaUtenteEnte")!=null)
			{
				String ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
				if (!ddlSocietaUtenteEnte.equals(""))
				{
					String[] codici = ddlSocietaUtenteEnte.split("\\|");
					codiceSocieta = codici[0];
					chiaveEnte = codici[1];
					chiaveEnte = codici[2];
					request.setAttribute("tx_societa", codiceSocieta);
					request.setAttribute("tx_utente", chiaveEnte);
					request.setAttribute("tx_ente", chiaveEnte);
				}
			
			}
		}
	}

