package org.seda.payer.manager.logrequest.actions;



import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;

import com.esed.log.req.dati.CollectionDto;
import com.esed.log.req.dati.LogRequest;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;


	@SuppressWarnings("serial")
	public class DettaglioLogRequestAction extends BaseManagerAction {
		
		
		private static String ritornaViewstate = "logrequest_search";

		public Object service(HttpServletRequest request) throws ActionException {
			
			HttpSession session = request.getSession();
			tx_SalvaStato(request);
			
			FiredButton firedButton = getFiredButton(request);
	
			loadSocietaXml_DDL(request);
			loadCanaliPagamentoXml_DDL(request);
	
			loadPayerApplicationsXml_DDL_properties(request, session, true, true);
		
		
			switch(firedButton)
			{
			case TX_BUTTON_RESET:
			case TX_BUTTON_NULL:
				
				Client client = null;
				Response responseWS = null;
				
				
				try {

					LogRequest logRequest = new LogRequest();
					logRequest.setDbSchemaCodSocieta(getCodSocieta(request));
					logRequest.setIdRequest(getIdLogRequest(request));
					logRequest.setTagSuffissoTabella(getTagSuffissoTabella(request));
				
					
					Entity<LogRequest> entity =  Entity.entity(logRequest, MediaType.APPLICATION_JSON);
					String uri =  ManagerStarter.configuration.getProperty(PropertiesPath.wsLogRequest.format(PropertiesPath.defaultnode.format()));
					uri += "/get";

					client = ClientBuilder.newClient();
					WebTarget target = client.target(uri);
					Builder builder = target.request(MediaType.APPLICATION_JSON);
					responseWS = builder.post(entity);
					if(responseWS != null) {
						System.out.println("+response = " + responseWS.getStatus());
						System.out.println("+Response info = " + responseWS.getStatusInfo());

						if(responseWS.getStatus() == Status.OK.getStatusCode()) {
							LogRequest result = responseWS.readEntity(LogRequest.class);
							System.out.println("esito = " + result.toString());
							
							request.setAttribute("dataRequestUserFormat",  tsToString(result.getDataRequest(), "dd/MM/yyyy hh:mm:ss"));						
							request.setAttribute("logRequest", result);
						} else { 
							setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
						}
						responseWS.close();
						responseWS = null;
					}
					client.close();
					client = null;
				} catch (Exception e) {
					setFormMessage("form_selezione", "Errore: " + e.getMessage(), request);
					e.printStackTrace();
				} finally {
					if(responseWS != null)
						responseWS.close();
					if(client != null)
						client.close();
				}
				break;
				
			
				
			case TX_BUTTON_INDIETRO:
				
				recuperaFiltriRicerca(request);
				
				request.setAttribute("vista",ritornaViewstate);
				break;

			}
			return null;
		}

	

	
		
		
		
		private BigInteger getIdLogRequest(HttpServletRequest request)
		{
			HttpSession session = request.getSession(false);
			session.setAttribute("idLogRequest", request.getParameter("idLogRequest"));
			BigInteger idLogRequest = BigInteger.valueOf(Integer.parseInt(session.getAttribute("idLogRequest").toString()));
			
			return idLogRequest;
		}
		
		private String getCodSocieta(HttpServletRequest request)
		{
			HttpSession session = request.getSession(false);
			String dbSchemaCodSocieta = null;
			if (session != null && session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA) != null)
				dbSchemaCodSocieta = (String) session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
			else
			{
				PropertiesTree configuration = (PropertiesTree) session.getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
				dbSchemaCodSocieta=configuration.getProperty(PropertiesPath.societa.format(getTemplateCurrentApplication(request, session)));
				session.setAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA,dbSchemaCodSocieta);
			}
			return dbSchemaCodSocieta;
		}
		
		private String getTagSuffissoTabella(HttpServletRequest request)
		{
			HttpSession session = request.getSession(false);
			String tagSuffissoTabLogRequest = null;
			session.setAttribute("tagSuffissoTablogRequest", request.getParameter("tagSuffissoTablogRequest"));
			
			tagSuffissoTabLogRequest = session.getAttribute("tagSuffissoTablogRequest").toString();
			
			return tagSuffissoTabLogRequest;
		}

		
		private void recuperaFiltriRicerca(HttpServletRequest request) {
			HttpSession session = request.getSession();
			
			request.setAttribute("tx_username", session.getAttribute("tx_username")!=null?session.getAttribute("tx_username"):"");
			request.setAttribute("tx_userprofile", session.getAttribute("tx_userprofile")!=null?session.getAttribute("tx_userprofile"):"");
			request.setAttribute("tx_applicazione", session.getAttribute("tx_applicazione")!=null?session.getAttribute("tx_applicazione"):"");
			request.setAttribute("indirizzo_ip", session.getAttribute("indirizzo_ip")!=null?session.getAttribute("indirizzo_ip"):"");
			request.setAttribute("tx_app", session.getAttribute("tx_app")!=null?session.getAttribute("tx_app"):"");
			request.setAttribute("tx_canale_pagamento", session.getAttribute("tx_canale_pagamento")!=null?session.getAttribute("tx_canale_pagamento"):"");
			request.setAttribute("tx_metodo", session.getAttribute("tx_metodo")!=null?session.getAttribute("tx_metodo"):"");
			request.setAttribute("tx_azione", session.getAttribute("tx_azione")!=null?session.getAttribute("tx_azione"):"");
			request.setAttribute("tx_codfis", session.getAttribute("tx_codfis")!=null?session.getAttribute("tx_codfis"):"");
			request.setAttribute("tx_numdoc", session.getAttribute("tx_numdoc")!=null?session.getAttribute("tx_numdoc"):"");
			request.setAttribute("tx_numbol", session.getAttribute("tx_numbol")!=null?session.getAttribute("tx_numbol"):"");
			request.setAttribute("tx_provincia", session.getAttribute("tx_provincia")!=null?session.getAttribute("tx_provincia"):"");
			request.setAttribute("tx_provres", session.getAttribute("tx_provres")!=null?session.getAttribute("tx_provres"):"");
			request.setAttribute("tx_comures", session.getAttribute("tx_comures")!=null?session.getAttribute("tx_comures"):"");
			request.setAttribute("tx_numiuv", session.getAttribute("tx_numiuv")!=null?session.getAttribute("tx_numiuv"):"");
			request.setAttribute("tx_keytra", session.getAttribute("tx_keytra")!=null?session.getAttribute("tx_keytra"):"");
			request.setAttribute("tx_request", session.getAttribute("tx_request")!=null?session.getAttribute("tx_request"):"");
			request.setAttribute("tx_sessione", session.getAttribute("tx_sessione")!=null?session.getAttribute("tx_sessione"):"");
			request.setAttribute("tx_error", session.getAttribute("tx_error")!=null?session.getAttribute("tx_error"):"");
			request.setAttribute("tx_ope", session.getAttribute("tx_ope")!=null?session.getAttribute("tx_ope"):"");
			request.setAttribute("inizioSessioneDA", session.getAttribute("inizioSessioneDA")!=null?session.getAttribute("inizioSessioneDA"):"");
			request.setAttribute("inizioSessioneTimeDA", session.getAttribute("inizioSessioneTimeDA")!=null?session.getAttribute("inizioSessioneTimeDA"):"");
			request.setAttribute("inizioSessioneA", session.getAttribute("inizioSessioneA")!=null?session.getAttribute("inizioSessioneA"):"");
			request.setAttribute("inizioSessioneTimeA", session.getAttribute("inizioSessioneTimeA")!=null?session.getAttribute("inizioSessioneTimeA"):"");
			
			
		}

	}

