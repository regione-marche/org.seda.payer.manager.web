package org.seda.payer.manager.monitoraggio.actions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.reports.stampe.ReportsCreator;
import org.seda.payer.manager.util.PropertiesPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.seda.commons.properties.tree.PropertiesNodeException;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.compatibility.SystemVariable;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.taglibs.ViewStateManager;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.pgec.webservice.commons.dati.BeanFreccia;
import com.seda.payer.pgec.webservice.commons.dati.BeanIV;
import com.seda.payer.pgec.webservice.commons.dati.BeanIci;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;



public class DettaglioTransazioneAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static PropertiesTree configuration;
	private static DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols(Locale.ITALY);
	private static DecimalFormat numberFormat = new DecimalFormat("###,##0.00", unusualSymbols);


	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession(false);

		//PG150180_001 GG - inizio
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		//PG150180_001 GG - fine

		loadDettaglioTransazione(request, session);
		
		//PG150180_001 GG - inizio
		switch(firedButton) {
			case TX_BUTTON_DOWNLOAD_RT:
				//inizio LP PG21XX04 Bug configuration
				//try {
				//	getConfiguration();
				//} catch (PropertiesNodeException e1) {
				//	// TODO Auto-generated catch block
				//	e1.printStackTrace();
				//}
				getConfiguration(request);
				//fine LP PG21XX04 Bug configuration
				String iuv = isNull(request.getAttribute("iuv"));
				String idtra = isNull(request.getAttribute("tx_codice_transazione_hidden"));
				//inizio LP PG190220
				String rt_Annullata = isNull(request.getAttribute("rt_annullata"));
				boolean bRtAnnullta = rt_Annullata.equals("1"); 
				//fine LP PG190220
				String rt = "";
				
				BeanIci[] beanIcis = (BeanIci[])request.getAttribute(Field.TX_LISTA_ICI.format());
				BeanFreccia[] beanFreccias = (BeanFreccia[])request.getAttribute(Field.TX_LISTA_FRECCIA.format());
				BeanIV[] beanIVs = (BeanIV[])request.getAttribute(Field.TX_LISTA_DETTAGLIO.format());
				        
				for (BeanIci bean : beanIcis) {
					if(bean.getNodoSpcIuv().equalsIgnoreCase(iuv)) {
						//inizio LP PG190220
						if (bRtAnnullta)
							rt = bean.getNodoSpcRtAnnullata();
						else
						//fine LP PG190220
						rt = bean.getNodoSpcRt();
						break;
					}
				}
				for (BeanFreccia bean : beanFreccias) {
					if(bean.getNodoSpcIuv().equalsIgnoreCase(iuv)) {
						//inizio LP PG190220
						if (bRtAnnullta)
							rt = bean.getNodoSpcRtAnnullata();
						else
						//fine LP PG190220
						rt = bean.getNodoSpcRt();
						break;
					}
				}
				for (BeanIV bean : beanIVs) {
					if(bean.getNodoSpcIuv().equalsIgnoreCase(iuv)) {
						//inizio LP PG190220
						if (bRtAnnullta)
							rt = bean.getNodoSpcRtAnnullata();
						else
						//fine LP PG190220
						rt = bean.getNodoSpcRt();
						break;
					}
				}
				
				String pathNomeFileReport = "";
				String templateName="";
				
				MAFRequest mafReq = new MAFRequest(request);
				String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
				UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
				templateName = userBean.getTemplate(applicationName);
				//request.setAttribute("filename","RT.xml");
				//request.setAttribute("dettaglioTransazioneViewRT", rt);
				
				// trasformazione dell'XML in PDF come fa la notifica.facade.
				ReportsCreator reportsCreator=new ReportsCreator(
						getBirtOutputPathRT(templateName),
						getBirtDesignPath(templateName), 
						null);
				// recupero delle variabili necessarie dall'RT per la produzione del PDF
				DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
				domFactory.setNamespaceAware(true);
				try{
					
				//inizio LP PG190220
				if (bRtAnnullta)
					System.out.println("RT Annullata= " + rt);
				else	
				//fine LP PG190220
				System.out.println("RT = " + rt);
				DocumentBuilder builder = domFactory.newDocumentBuilder();
				Document doc = builder.parse( new ByteArrayInputStream(rt.getBytes()));
				
				XPathFactory factory = XPathFactory.newInstance();
				XPath xpath = factory.newXPath();
				System.out.println("inizio lettura TAG da RT");
				
				Node numeroAutorizzazioneNode = (Node)(xpath.evaluate("//*[local-name()='identificativoMessaggioRicevuta']", doc, XPathConstants.NODE));
				System.out.println("numeroAutorizzazioneNode letto");
				Node dataNode = (Node)(xpath.evaluate("//*[local-name()='riferimentoDataRichiesta']", doc, XPathConstants.NODE));
				Node dataOraMessaggioNode = (Node)(xpath.evaluate("//*[local-name()='dataOraMessaggioRicevuta']", doc, XPathConstants.NODE));
				Node codiceIdentificativoUnivocoPSPNode = (Node)(xpath.evaluate("//*[local-name()='istitutoAttestante']/*[local-name()='identificativoUnivocoAttestante']/*[local-name()='codiceIdentificativoUnivoco']", doc, XPathConstants.NODE));
				Node denominazioneAttestanteNode = (Node)(xpath.evaluate("//*[local-name()='istitutoAttestante']/*[local-name()='denominazioneAttestante']", doc, XPathConstants.NODE));
				Node identificativoDominioNode = (Node)(xpath.evaluate("//*[local-name()='enteBeneficiario']/*[local-name()='identificativoUnivocoBeneficiario']/*[local-name()='codiceIdentificativoUnivoco']", doc, XPathConstants.NODE));
				Node denominazioneEnteNode = (Node)(xpath.evaluate("//*[local-name()='enteBeneficiario']/*[local-name()='denominazioneBeneficiario']", doc, XPathConstants.NODE));
				
				
				
				Node codiceIdentificativoUnivocoNode = (Node)(xpath.evaluate("//*[local-name()='soggettoPagatore']/*[local-name()='identificativoUnivocoPagatore']/*[local-name()='codiceIdentificativoUnivoco']", doc, XPathConstants.NODE));
				Node anagraficaPagatoreNode = (Node)(xpath.evaluate("//*[local-name()='soggettoPagatore']/*[local-name()='anagraficaPagatore']", doc, XPathConstants.NODE));
				Node emailPagatoreNode = (Node)(xpath.evaluate("//*[local-name()='soggettoPagatore']/*[local-name()='e-mailPagatore']", doc, XPathConstants.NODE));
				
				Node importoTotalePagatoNode = (Node)(xpath.evaluate("//*[local-name()='datiPagamento']/*[local-name()='importoTotalePagato']", doc, XPathConstants.NODE));
				Node identificativoUnivocoVersamentoNode = (Node)(xpath.evaluate("//*[local-name()='datiPagamento']/*[local-name()='identificativoUnivocoVersamento']", doc, XPathConstants.NODE));
				Node identificativoUnivocoRiscossioneNode = (Node)(xpath.evaluate("//*[local-name()='datiPagamento']/*[local-name()='datiSingoloPagamento']/*[local-name()='identificativoUnivocoRiscossione']", doc, XPathConstants.NODE));

				
				Node nodeEsito = (Node)(xpath.evaluate("//*[local-name()='codiceEsitoPagamento']", doc, XPathConstants.NODE));
				String esitoPagamento = "NON PAGATA";
				if(nodeEsito != null) {
					if(nodeEsito.getTextContent().trim().equals("0")){
						esitoPagamento = "PAGATA";
					}
					if(nodeEsito.getTextContent().trim().equals("1")){
						esitoPagamento = "NON PAGATA";
					}
//							0 Pagamento eseguito
//							1 Pagamento non eseguito
//							2 Pagamento parzialmente eseguito
//							3 Decorrenza termini
//							4 Decorrenza termini parziale
					
					
					
				}
				
				
					String anagraficaPagatore = anagraficaPagatoreNode.getTextContent();
					String codiceIdentificativoUnivoco = codiceIdentificativoUnivocoNode.getTextContent();
					//20072021 GG - allineamento con ws notifiche - inizio
					//String emailPagatore  = emailPagatoreNode==null?"":emailPagatoreNode.getTextContent();
					String emailPagatore  ="";
 					if(emailPagatoreNode!=null)
 						emailPagatore =  emailPagatoreNode.getTextContent();
 					else
 					{
 						// se non ho la mail pagatore.....ho il versante
 						Node emailVersanteNode = (Node)(xpath.evaluate("//*[local-name()='soggettoVersante']/*[local-name()='e-mailVersante']", doc, XPathConstants.NODE));
 						if(emailVersanteNode!=null) //LP PG200060 su RM c'era questo controllo forse è ridontante...
 						emailPagatore =  emailVersanteNode.getTextContent();
 						
 						Node anagraficaVersanteNode = (Node)(xpath.evaluate("//*[local-name()='soggettoVersante']/*[local-name()='anagraficaVersante']", doc, XPathConstants.NODE));
 						if(anagraficaVersanteNode!=null) //LP PG200060 su RM c'era questo controllo forse è ridontante...
 						anagraficaPagatore = anagraficaVersanteNode.getTextContent();
 						
 						Node codiceIdentificativoVersante = (Node)(xpath.evaluate("//*[local-name()='soggettoVersante']/*[local-name()='identificativoUnivocoVersante']/*[local-name()='codiceIdentificativoUnivoco']", doc, XPathConstants.NODE));
 						if(codiceIdentificativoVersante!=null) //LP PG200060 su RM c'era questo controllo forse è ridontante...
 						codiceIdentificativoUnivoco = codiceIdentificativoVersante.getTextContent();
 					}
 					//20072021 GG - allineamento con ws notifiche - fine
										
					String codiceIUVRT = identificativoUnivocoVersamentoNode.getTextContent();
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					//inizio LP PAGONET-323
					//Date dataPagamento = sdf.parse(dataNode.getTextContent());
					//sdf.applyPattern("dd/MM/yyyy");
					//String data = sdf.format(dataPagamento);
					SimpleDateFormat sdfITA =  new SimpleDateFormat("dd/MM/yyyy");
					Date dataApplicativa = sdf.parse(dataNode.getTextContent());
					String sDataApplicativa = sdfITA.format(dataApplicativa);

					String data = "";
					if(dataOraMessaggioNode.getTextContent().trim().length() >= 10) {
						Date DataPagamento = sdf.parse(dataOraMessaggioNode.getTextContent());
						data = sdfITA.format(DataPagamento);
					}
					//fine LP PAGONET-323

					String oraMessaggio="";
					if(dataOraMessaggioNode.getTextContent().trim().length()==19){
						oraMessaggio = dataOraMessaggioNode.getTextContent().trim().substring(11,19);
					}
						
					
					
					
					//PG160150_003 GG - inizio
					Node commissioniApplicatePSPNode = (Node)(xpath.evaluate("//*[local-name()='datiPagamento']/*[local-name()='datiSingoloPagamento']/*[local-name()='commissioniApplicatePSP']", doc, XPathConstants.NODE));
					BigDecimal commissioniApplicatePSP = null;
					if (commissioniApplicatePSPNode!=null) {
						commissioniApplicatePSP = new BigDecimal(commissioniApplicatePSPNode.getTextContent());
					}
					//PG160150_003 GG - fine
					
					Node causaleVersamentoNode = (Node)(xpath.evaluate("//*[local-name()='datiPagamento']/*[local-name()='datiSingoloPagamento']/*[local-name()='causaleVersamento']", doc, XPathConstants.NODE));
					
					BigDecimal importo_totale_transazione = new BigDecimal(importoTotalePagatoNode.getTextContent());
					String denominazioneAttestante = denominazioneAttestanteNode.getTextContent();
					String identificativoDominio = identificativoDominioNode.getTextContent();
					String identificativoUnivocoRiscossione  = identificativoUnivocoRiscossioneNode.getTextContent();
					String codiceIdentificativoUnivocoPSP = codiceIdentificativoUnivocoPSPNode.getTextContent();
					String numeroAutorizzazione = numeroAutorizzazioneNode.getTextContent();
					String denominazioneEnte = denominazioneEnteNode.getTextContent();
			    	//inizio LP PAGONET-323
					//String causaleVersamento = null;
					//if(templateName.equalsIgnoreCase("creset")) {
					//	causaleVersamento = causaleVersamentoNode.getTextContent();
					//}
					String causaleVersamento = (causaleVersamentoNode.getTextContent() != null ? causaleVersamentoNode.getTextContent() : "");
			    	//fine LP PAGONET-323
					
				
					String imageRelative = getBirtImagePathRelative(templateName);
					String pathLogoPAReport = getPathLogoPAReport(templateName);
					String paramPathLogoSocieta = getParamPathLogoSocieta(templateName);

					
				
				//inizio LP PAGONET-323
				//pathNomeFileReport=reportsCreator.renderRTdaMonitoraggio(anagraficaPagatore, codiceIdentificativoUnivoco, emailPagatore, codiceIUVRT,
				//		data, importo_totale_transazione, denominazioneAttestante, identificativoDominio,
				//		identificativoUnivocoRiscossione, codiceIdentificativoUnivocoPSP, idtra, 
				//		numberFormat, commissioniApplicatePSP, oraMessaggio,numeroAutorizzazione, denominazioneEnte,pathLogoPAReport,paramPathLogoSocieta,esitoPagamento, causaleVersamento);
				pathNomeFileReport=reportsCreator.renderRTdaMonitoraggio(anagraficaPagatore, codiceIdentificativoUnivoco, emailPagatore, codiceIUVRT,
						data, importo_totale_transazione, denominazioneAttestante, identificativoDominio,
						identificativoUnivocoRiscossione, codiceIdentificativoUnivocoPSP, idtra, 
						numberFormat, commissioniApplicatePSP, oraMessaggio,numeroAutorizzazione, denominazioneEnte,pathLogoPAReport,paramPathLogoSocieta,esitoPagamento, causaleVersamento,
						sDataApplicativa
						);
				//fine LP PAGONET-323

				
				
				if(pathNomeFileReport!=null && new File( pathNomeFileReport ).exists() ){
					File f = new File(pathNomeFileReport);

					request.setAttribute("pathFileToDownload", pathNomeFileReport);
					request.setAttribute("nome_file_pdf",  f.getName());
				} else {
					
					request.setAttribute("paginaTo", (String) request.getAttribute("PaginaFrom"));
					ViewStateManager viewStateManager = new ViewStateManager(request, (String) request.getAttribute("PaginaFrom") );
					viewStateManager.replyParameters();
					viewStateManager.replyAttributes();
					request.setAttribute("messaggio", "Errore nella creazione del file PDF della ricevuta telematica" );
				}	
				} catch (MalformedURLException e) {
						System.out.println("errore manager:" + e.getMessage());
						e.printStackTrace();
					}  catch (FaultType e) {
						System.out.println("errore manager:" + e.getMessage());
						e.printStackTrace();
					} catch (RemoteException e) {
						System.out.println("errore manager:" + e.getMessage());
						e.printStackTrace();
					} catch (Exception e) {
						System.out.println("errore manager:" + e.getMessage());
						e.printStackTrace();
					} catch (Throwable e) {
						System.out.println("errore manager:" + e.getMessage());
							e.printStackTrace();
						} 
				
				// FINE CREAZIONE PDF
								
			break;
			
		}
		//PG150180_001 GG - fine

		request.setAttribute(Field.TX_MESSAGE.format(), getMessage());
		request.setAttribute(Field.TX_ERROR_MESSAGE.format(), getErrorMessage());
		return null;
	}
	
	public static String getBirtOutputPathRT(String template) {
		return configuration.getProperty(PropertiesPath.birtOutputPath.format(template)).trim();
	}
	public static String getBirtDesignPath(String template) {
		return configuration.getProperty(PropertiesPath.birtDesignPath.format(template)).trim();
	}
	
	public static String getBirtImagePathRelative(String template) {
		return configuration.getProperty(PropertiesPath.birtImagePathRelative.format(template)).trim();	
	}
	
	public static String getPathLogoPAReport(String template) {
		return configuration.getProperty(PropertiesPath.pathLogoPAReport.format(template)).trim();	
	}
	public static String getParamPathLogoSocieta(String template) {
		return configuration.getProperty(PropertiesPath.paramPathLogoSocieta.format(template)).trim();	
	}
	//inizio LP PG21XX04 Bug configuration
	//public void getConfiguration () throws PropertiesNodeException {
	//	SystemVariable sv = new SystemVariable();
	//	String rootPath=sv.getSystemVariableValue(ManagerKeys.ENV_CONFIG_FILE);
	//	sv=null;
	//
	//	if (rootPath!=null) {
	//		// caricamento configurazioni esterne
	//		configuration = new PropertiesTree(rootPath);
	//	}
	//}
	public void getConfiguration(HttpServletRequest request) {
		configuration = (PropertiesTree) (request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
	}
	//fine LP PG21XX04 Bug configuration
}
