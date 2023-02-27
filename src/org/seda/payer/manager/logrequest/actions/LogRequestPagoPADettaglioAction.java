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

import com.esed.log.req.dati.LogPap;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;


	@SuppressWarnings("serial")
	public class LogRequestPagoPADettaglioAction extends BaseManagerAction {
		
		
		private static String ritornaViewstate = "logrequest_pagoPA_search";

		public Object service(HttpServletRequest request) throws ActionException {
			
			tx_SalvaStato(request);
			
			FiredButton firedButton = getFiredButton(request);
	
		
		
			switch(firedButton)
			{
			case TX_BUTTON_RESET:
			case TX_BUTTON_NULL:
				
				Client client = null;
				Response responseWS = null;
				
				
				try {

					LogPap logPap = new LogPap();
					logPap.setDbSchemaCodSocieta(getCodSocieta(request));
					logPap.setIdLog(getIdLogRequest(request));
					logPap.setTagSuffissoTabella(getTagSuffissoTabella(request));
				
					
					Entity<LogPap> entity =  Entity.entity(logPap, MediaType.APPLICATION_JSON);
					String uri =  ManagerStarter.configuration.getProperty(PropertiesPath.wsLogRequest.format(PropertiesPath.defaultnode.format()));
					uri += "/getPap";

					client = ClientBuilder.newClient();
					WebTarget target = client.target(uri);
					Builder builder = target.request(MediaType.APPLICATION_JSON);
					responseWS = builder.post(entity);
					if(responseWS != null) {
						System.out.println("+response = " + responseWS.getStatus());
						System.out.println("+Response info = " + responseWS.getStatusInfo());

						if(responseWS.getStatus() == Status.OK.getStatusCode()) {
							LogPap result = responseWS.readEntity(LogPap.class);
							System.out.println("esito = " + result.toString());
						
							request.setAttribute("dataInserimentoUserFormat",  tsToString(result.getDataInserimento(), "dd/MM/yyyy HH:mm:ss"));
							request.setAttribute("dataInizioChiamataUserFormat",  tsToString(result.getDataInizioChiamata(), "dd/MM/yyyy HH:mm:ss"));
							request.setAttribute("dataFineChiamataUserFormat",  tsToString(result.getDataFineChiamata(), "dd/MM/yyyy HH:mm:ss"));
					
							request.setAttribute("logPap", result);
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

	

	
		
		private void recuperaFiltriRicerca(HttpServletRequest request) {

			HttpSession session = request.getSession();

			request.setAttribute("tx_pa_idDominio", session.getAttribute("tx_pa_idDominio")!=null?session.getAttribute("tx_pa_idDominio"):"");
			request.setAttribute("tx_pa_ciuv", session.getAttribute("tx_pa_ciuv")!=null?session.getAttribute("tx_pa_ciuv"):"");
			request.setAttribute("tx_pa_esito", session.getAttribute("tx_pa_esito")!=null?session.getAttribute("tx_pa_esito"):"");
			request.setAttribute("tx_pa_esito", session.getAttribute("tx_pa_esito")!=null?session.getAttribute("tx_pa_esito"):"");
			request.setAttribute("tx_pa_operazione", session.getAttribute("tx_pa_operazione")!=null?session.getAttribute("tx_pa_operazione"):"");
			request.setAttribute("tx_pa_errore", session.getAttribute("tx_errore")!=null?session.getAttribute("tx_errore"):"");
			request.setAttribute("dataChiamataDA", session.getAttribute("dataChiamataDA")!=null?session.getAttribute("dataChiamataDA"):"");
			request.setAttribute("dataChiamataTimeDA", session.getAttribute("dataChiamataTimeDA")!=null?session.getAttribute("dataChiamataTimeDA"):"");
			request.setAttribute("dataChiamataA", session.getAttribute("dataChiamataA")!=null?session.getAttribute("dataChiamataA"):"");
			request.setAttribute("dataChiamataTimeA", session.getAttribute("dataChiamataTimeA")!=null?session.getAttribute("dataChiamataTimeA"):"");
			
		}
		
		private BigInteger getIdLogRequest(HttpServletRequest request)
		{
			HttpSession session = request.getSession(false);
			session.setAttribute("idLogRequestPap", request.getParameter("idLogRequestPap"));
			BigInteger idLogRequest = BigInteger.valueOf(Integer.parseInt(session.getAttribute("idLogRequestPap").toString()));
			
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
			session.setAttribute("tagSuffissoTablogRequestPap", request.getParameter("tagSuffissoTablogRequestPap"));
			
			tagSuffissoTabLogRequest = session.getAttribute("tagSuffissoTablogRequestPap").toString();
			
			return tagSuffissoTabLogRequest;
		}


	}

