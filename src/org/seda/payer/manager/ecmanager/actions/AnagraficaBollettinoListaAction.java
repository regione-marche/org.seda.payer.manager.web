package org.seda.payer.manager.ecmanager.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.AnagraficaBollettino;
import com.seda.payer.core.bean.AnagraficaBollettinoPageList;
import com.seda.payer.core.dao.AnagraficaBollettinoDAO;
import com.seda.payer.core.dao.AnagraficaBollettinoDAOFactory;
import com.seda.payer.core.exception.DaoException;
import com.seda.security.webservice.dati.ResponseType;
import com.seda.security.webservice.dati.SelezionaUtentePIVAResponseType;
import com.seda.security.webservice.dati.SelezionaUtentePIVAbyCFRequestType;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.TextField;

public class AnagraficaBollettinoListaAction extends AnaBollettinoManagerBaseManagerAction {
private static final long serialVersionUID = 1L;

	private String codiceSocieta="";
	private String cutecute="";
	private String chiaveEnte="";

	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		try
		{	 
			this.setErrorMessage("");
			HttpSession session = request.getSession();
			FiredButton firedButton = getFiredButton(request);
			if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) 
				setProfile(request);
			
			
			loadStaticXml_DDL(request, session);
			loadSocietaUtenteEnteXml_DDL(request, session);
			
			if (session.getAttribute("vista")!=null){
				if (session.getAttribute("vista").equals("anagraficacontribuenti")){
					firedButton=FiredButton.TX_BUTTON_CERCA;
					if (session.getAttribute("tx_error_message")!=null)
						this.setErrorMessage(session.getAttribute("tx_error_message").toString());
					if (session.getAttribute("tx_message")!=null)
						this.setMessage(session.getAttribute("tx_message").toString());
					session.setAttribute("tx_error_message",null);
					session.setAttribute("tx_message",null);
					session.setAttribute("vista",null);

					if(session.getAttribute("pageNumber_")!=null)
							request.setAttribute("pageNumber",session.getAttribute("pageNumber_"));

				}
			}
			
				
			if(request.getAttribute("tx_genera_lettera") != null) {

				dividiSocUtenteEnte(request);
				String codfisc = (String)request.getAttribute("codfisc");				
				
				AnagraficaBollettino ana = selectAnagrafica(request, codfisc);
				String templateFilePath = this.getWelcomekitTemplatePath(); // "D:\\FileTemporanei\\Payer\\pdf\\estrattoConto\\WelcomeKit\\template\\soris-welcome-template.pdf";
				String outputFileFolderPath = this.getWelcomekitOutputPath(); // "D:\\FileTemporanei\\Payer\\pdf\\estrattoConto\\WelcomeKit\\";
				String outputFileName = ana.getCodiceFiscale() + "-welcome.pdf";
				String outputFileFullPath = new File(new File(outputFileFolderPath), outputFileName).getPath();
				
				String codiceAttivazione = ana.getCodiceAttivazione();
				codiceAttivazione = "0000000000" + codiceAttivazione;
				codiceAttivazione = codiceAttivazione.substring(codiceAttivazione.length() - 8);
				 
				GeneraPdfBenvenuto(ana, codiceAttivazione, templateFilePath, outputFileFullPath);
				
				if(request.getAttribute("tx_invia_lettera") != null) {
					//Invia per email
					String emailTo = (ana.getMailPec() != "" ? ana.getMailPec() : ana.getMail());
					String emailBody = this.getEmailWelcomekitBody();
					String denominazione = (ana.getNome() + " " + ana.getCognome());
					emailBody = MessageFormat.format(emailBody, denominazione);
					
					WSCache.emailServer.sendEMail(this.getEmailWelcomekitFrom(), emailTo, "", "", this.getEmailWelcomekitSubject(), emailBody, outputFileFullPath);
					firedButton=FiredButton.TX_BUTTON_CERCA;
					System.out.println("INVIA lettera benvenuta a: " + emailTo);
					
					
					this.setMessage("Lettera di benvenuto inviata con successo");
				}
				else {
					request.setAttribute("filename",outputFileName);
					request.setAttribute("welcomekit_pdf", outputFileFullPath);
					request.setAttribute("download", "OK");
				}
				
			}
				
			
			switch(firedButton)
			{
				case TX_BUTTON_CERCA: 
					try {
							dividiSocUtenteEnte(request);
							AnagraficaBollettinoPageList anags = getLista(request,false);
							PageInfo pageInfo = anags.getPageInfo();
							
							if (anags.getRetCode()!="00") {
								setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
							} 
							else 
							{
									if(pageInfo != null)
									{
										if(pageInfo.getNumRows() > 0)
										{
											session.setAttribute("numRows",pageInfo.getNumRows()); 
											request.setAttribute("lista_anaboll", anags.getAnagraficaBollettinoListXml());
											request.setAttribute("lista_anaboll.pageInfo", pageInfo);
										}
										else setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
									}
							}

						} catch(Exception e) {
							setErrorMessage(e.getMessage());
							e.printStackTrace();
						}
						break;

					
				case TX_BUTTON_RESET:
					resetParametri(request);
					session.setAttribute("tx_codfisc",null);
					session.setAttribute("tx_denom",null);
					session.setAttribute("tx_anagraficaattiva",null); //PG170280 CT
					session.setAttribute("rowsPerPage",null);
					session.setAttribute("order",null);
					break;
					
				case TX_BUTTON_SOCIETA_CHANGED:
					break;
					
				case TX_BUTTON_PROVINCIA_CHANGED:
					break;
				
				case TX_BUTTON_ENTE_CHANGED:
					break;
					
					
				case TX_BUTTON_NULL: 
					session.setAttribute("tx_codfisc",null);
					session.setAttribute("tx_denom",null);
					session.setAttribute("tx_anagraficaattiva",null); //PG170280 CT
					session.setAttribute("rowsPerPage",null);
					session.setAttribute("order",null);
					break;
					
				case TX_BUTTON_STAMPA:
					break;
					
				case TX_BUTTON_DOWNLOAD:
					try {
						request.setAttribute("download", "Y");
						dividiSocUtenteEnte(request);
						loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
						
						/* Tolto il controllo sul numero di righe di download TK 2015100188000055 di Soris perchè query di estrazione non complessa
						Integer maxRigheDownload = (Integer) context.getAttribute(ManagerKeys.MAXRIGHE_DOWNLOAD_BORSELLINO);
						if ((Integer) session.getAttribute("numRows") <= maxRigheDownload ){
							getLista(request,true);
						} else {
							setFormMessage("form_selezione", Messages.SUPERATO_MAXRIGHE.format(), request);
							request.setAttribute("download", "N");
							break;
						}
						*/
						getLista(request,true);
					} catch(Exception e) {
						setErrorMessage(e.getMessage());
						e.printStackTrace();
					}
					break;
				
				
			}
			
			request.setAttribute("tx_error_message", this.getErrorMessage());
			request.setAttribute("tx_message", this.getMessage());
			return null;
			
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private AnagraficaBollettinoPageList getLista(HttpServletRequest request, boolean download) {
		AnagraficaBollettinoDAO controller;
		AnagraficaBollettinoPageList res = null;
		String codiceFiscale = ((String)request.getAttribute("tx_codfisc") == null) ? "" : (String)request.getAttribute("tx_codfisc").toString().toUpperCase();
		String Denominazione = ((String)request.getAttribute("tx_denom") == null) ? "" : (String)request.getAttribute("tx_denom").toString().toUpperCase();
		String anagraficaAttiva = ((String)request.getAttribute("tx_anagraficaattiva") == null) ? "" : (String)request.getAttribute("tx_anagraficaattiva").toString().toUpperCase(); //PG170280 CT
		/*PG190020_000_LP*/
		String flagMail = ((String)request.getAttribute("tx_mail") == null && (String)request.getAttribute("tx_mail") != " ") ? "" : (String)request.getAttribute("tx_mail").toString().toUpperCase();
		String flagSms = ((String)request.getAttribute("tx_sms") == null && (String)request.getAttribute("tx_sms") != " ") ? "" : (String)request.getAttribute("tx_sms").toString().toUpperCase();
		/*FINE PG190020_000_LP*/
		// paginazione ed ordinamento
		int rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		int pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		String order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
		//inizio LP PG21XX04 Leak
		Connection connection = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//controller = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(getAnagraficaDataSource(), getAnagraficaDbSchema());
			connection = getAnagraficaDataSource().getConnection();
			controller = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(connection, getAnagraficaDbSchema());
			//fine LP PG21XX04 Leak
			if (download) {
				//PG170280 CT - Aggiunto parametro anagraficaAttiva
				//String angraficaListCsv = controller.doListCsv(codiceSocieta, cutecute, chiaveEnte, codiceFiscale, Denominazione, anagraficaAttiva, 0, 0, order).toString();
				
				/*PG190020_000_LP*/
				String angraficaListCsv = controller.doListCsv(codiceSocieta, cutecute, chiaveEnte, codiceFiscale, Denominazione, anagraficaAttiva, 0, 0, order, flagMail, flagSms).toString();
				/*FINE PG190020_000_LP*/
				
				//PG170280 CT --- 
//				String template = getTemplateCurrentApplication(request, request.getSession());
//				if  (template.equals("soris")) {
//					//Ciclo sui cf presenti nel file e per ciascuno cerco se presente nella SEC
//					String outPutText = "";
//					BufferedReader reader = new BufferedReader(new StringReader(angraficaListCsv));
//					String line = "";
//					try {
//						outPutText += reader.readLine() + "Autenticazione Presente;\r\n"; //Leggo header
//						while((line = reader.readLine()) != null) {
//							String cF = line.split(";")[0].trim();
//							String cfInSec = "NO";
//							if(!cF.isEmpty()) {
//								///Eseguo SP su SEC //SEUSRSP_SEL_PIVA_BY_CFIS
//								SelezionaUtentePIVAbyCFRequestType in = new SelezionaUtentePIVAbyCFRequestType();
//								in.setCodiceFiscale(cF);
//								SelezionaUtentePIVAResponseType out = WSCache.securityServer.selezionaUtentePIVAbyCF(in , request);
//								if (out != null && out.getResponse() != null)
//								{
//									ResponseType response = out.getResponse();
//									String retCode = response.getRetCode();
//									String retMessage = response.getRetMessage();
//									if (retCode.equals("00") && out.getSelezionaUtentePIVAResponse() != null) {
//										cfInSec = "SI";
//									}
//								}							
//							}
//							outPutText += line + cfInSec + ";" + "\r\n";
//						}
//						angraficaListCsv = outPutText;
//					}
//					catch (Exception e) {
//						e.printStackTrace();
//						
//					}
//				}
				//--- PG170280 CT
				
				
				Calendar cal = Calendar.getInstance();
				String timestamp = GenericsDateNumbers.formatCalendarData(cal, "yyyyMMdd_HHmmss");
				request.setAttribute("nome_file_csv",timestamp + "_AnagraficaBollettinoAnag.txt");
				request.setAttribute("anagraficabollettinoAnag_csv", angraficaListCsv);
				//request.setAttribute("nome_file_csv",timestamp + "_MonitoraggioWalletAnag.txt");
				//request.setAttribute("monitoraggio_walletAnag_csv", angraficaListCsv);
				
			} else  {
				System.out.println("codiceSocieta = "+ codiceSocieta);
				System.out.println("cutecute = "+ cutecute);
				System.out.println("chiaveEnte = "+ chiaveEnte);
				System.out.println("codiceFiscale = "+ codiceFiscale);
				System.out.println("anagraficaAttiva = "+ anagraficaAttiva);
				System.out.println("Denominazione = "+ Denominazione);
				System.out.println("rowsPerPage = "+ rowsPerPage);
				System.out.println("pageNumber = "+ pageNumber);
				/*PG190020_000_LP*/
				System.out.println("flagEmail = "+ flagMail);
				System.out.println("flagSms = "+ flagSms);
				/*FINE PG190020_000_LP*/
//				System.out.println("order = "+ order);
				//PG170280 CT - Aggiunto parametro anagraficaAttiva
				//res = controller.doList(codiceSocieta, cutecute, chiaveEnte, codiceFiscale, Denominazione, anagraficaAttiva, rowsPerPage, pageNumber, order);
				/*PG190020_000_LP*/
				res = controller.doList(codiceSocieta, cutecute, chiaveEnte, codiceFiscale, Denominazione, anagraficaAttiva, rowsPerPage, pageNumber, order, flagMail, flagSms);
				/*FINE PG190020_000_LP*/
			}
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
		return res;
	}
	
	private void dividiSocUtenteEnte(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String ddlSocietaUtenteEnte ="";
		if (request.getAttribute("ddlSocietaUtenteEnte")!=null){
			ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
			if (session.getAttribute("ddlSocietaUtenteEnte")==null){
				session.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);
			}
			if (!request.getAttribute("ddlSocietaUtenteEnte").toString().equals(session.getAttribute("ddlSocietaUtenteEnte").toString())){
				session.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);
			}
		}
		else{
			if (session.getAttribute("ddlSocietaUtenteEnte")!=null){
				ddlSocietaUtenteEnte = (String)session.getAttribute("ddlSocietaUtenteEnte");
				request.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);
			}
		}
		
		if (!ddlSocietaUtenteEnte.equals(""))
		{
			String[] codici = ddlSocietaUtenteEnte.split("\\|");
			codiceSocieta = codici[0];
			cutecute = codici[1];
			chiaveEnte = codici[2];
			request.setAttribute("tx_societa", codiceSocieta);
			request.setAttribute("tx_utente", cutecute);
			request.setAttribute("tx_ente", chiaveEnte);
		}
		
		//Mantenimento di variabili in caso di paginazione La paginazione viene gfestita da un'altro FORM
		if (request.getAttribute("tx_codfisc") != null)
			session.setAttribute("tx_codfisc",request.getAttribute("tx_codfisc"));
		else if(session.getAttribute("tx_codfisc")!=null)
				request.setAttribute("tx_codfisc",session.getAttribute("tx_codfisc"));

		if (request.getAttribute("tx_denom") != null)
			session.setAttribute("tx_denom",request.getAttribute("tx_denom"));
		else if(session.getAttribute("tx_denom")!=null)
				request.setAttribute("tx_denom",session.getAttribute("tx_denom"));
		
		//PG170280 CT
		if (request.getAttribute("tx_anagraficaattiva") != null)
			session.setAttribute("tx_anagraficaattiva",request.getAttribute("tx_anagraficaattiva"));
		else if(session.getAttribute("tx_anagraficaattiva")!=null)
				request.setAttribute("tx_anagraficaattiva",session.getAttribute("tx_anagraficaattiva"));
		
		if (request.getAttribute("rowsPerPage") != null)
			session.setAttribute("rowsPerPage",request.getAttribute("rowsPerPage"));
		else if(session.getAttribute("rowsPerPage")!=null)
				request.setAttribute("rowsPerPage",session.getAttribute("rowsPerPage"));
		
		if (request.getAttribute("order") != null)
			session.setAttribute("order",request.getAttribute("order"));
		else if(session.getAttribute("order")!=null)
				request.setAttribute("order",session.getAttribute("order"));
		
		if (request.getAttribute("pageNumber") != null)
			session.setAttribute("pageNumber_",request.getAttribute("pageNumber"));
		/*PG190020_000_LP*/
		else if (session.getAttribute("pageNumber") != null)
			request.setAttribute("pageNumber_",session.getAttribute("pageNumber"));
		
		if (request.getAttribute("tx_mail") != null)
			session.setAttribute("tx_mail",request.getAttribute("tx_mail"));
		else if(session.getAttribute("tx_mail")!=null)
				request.setAttribute("tx_mail",session.getAttribute("tx_mail"));
		
		if (request.getAttribute("tx_sms") != null)
			session.setAttribute("tx_sms",request.getAttribute("tx_sms"));
		else if(session.getAttribute("tx_sms")!=null)
				request.setAttribute("tx_sms",session.getAttribute("tx_sms"));
		/*FINE PG190020_000_LP*/
	}
	
	public AnagraficaBollettino selectAnagrafica(HttpServletRequest request,String CodiceFicale) throws DaoException {
		AnagraficaBollettinoDAO controller;
		AnagraficaBollettino res = null;
		//inizio LP PG21XX04 Leak
		//controller = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(getAnagraficaDataSource(), getAnagraficaDbSchema());
		//res = controller.doDetail(codiceSocieta,cutecute,chiaveEnte,CodiceFicale);
		Connection connection = null;
		try {
			connection = getAnagraficaDataSource().getConnection();
			controller = AnagraficaBollettinoDAOFactory.getAnagraficaBollettinoDAO(connection, getAnagraficaDbSchema());
			res = controller.doDetail(codiceSocieta,cutecute,chiaveEnte,CodiceFicale);
		} catch (DaoException e) {
			throw new DaoException(e);
		} catch (SQLException e) {
			throw new DaoException(e);
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
		return res;
	}	
	
	private void GeneraPdfBenvenuto(AnagraficaBollettino ana, String codiceAttivazione, String templateFilePath, String outputFilePath) {
		//Genera il pdf della lettera di benvenuto
		File file = null; 
		FileOutputStream fOut=null; 
		PdfReader reader =null;
		PdfStamper pdfStamper=null;
		try {
			file = new File(outputFilePath);
			fOut  = new FileOutputStream( file );
		
			reader = new PdfReader(templateFilePath);
			pdfStamper = new PdfStamper(reader,fOut);
			pdfStamper.getWriter().setCloseStream(false);
			
			Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Font.NORMAL);

//			TextField intestazione = new TextField(pdfStamper.getWriter(), new Rectangle(300,620,530,670), "intestazione");
//			intestazione.setVisibility(TextField.VISIBLE);
//			intestazione.setText(ana.getIntestatario() + "\r\n" + ana.getIndirizzo() + "\r\n" + ana.getCap() + " " + ana.getComune() + " " + ana.getProvincia());
//			intestazione.setOptions(TextField.MULTILINE);
//			
//			PdfFormField formFieldTxt = intestazione.getTextField();
//			pdfStamper.addAnnotation(formFieldTxt, 1);
			
			PdfContentByte cb = pdfStamper.getOverContent(1);
			ColumnText ct = new ColumnText(cb);
			//ct.setSimpleColumn(300,600,530,670);
			ct.setSimpleColumn(300,600,530,680);
			Paragraph p = new Paragraph(ana.getIntestatario(), font);
			ct.addElement(p);
			p = new Paragraph(ana.getIndirizzo(), font);
			ct.addElement(p);
			p = new Paragraph(ana.getCap() + " " + ana.getComune() + " " + ana.getProvincia(), font);
			ct.addElement(p);
			ct.go();					
			
			
			ct = new ColumnText(cb);
			ct.setSimpleColumn(60f, 570f, 300f, 600f);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			p = new Paragraph("Torino, " + sdf.format(new Date()), font);
			ct.addElement(p);
			ct.go();
			
			Font fontCode = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.NORMAL);
			cb = pdfStamper.getOverContent(2);
			ct = new ColumnText(cb);
			ct.setSimpleColumn(60f, 500f, 560f, 570f);
			p = new Paragraph(codiceAttivazione, fontCode);
			p.setAlignment(Paragraph.ALIGN_CENTER);
			ct.addElement(p);
			ct.go();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (pdfStamper!=null){
					pdfStamper.close();
				}
				
				if (fOut!=null) {
					fOut.flush();
					fOut.close();
				}
				
				if (reader!=null)
					reader.close();
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}		
	}
}
