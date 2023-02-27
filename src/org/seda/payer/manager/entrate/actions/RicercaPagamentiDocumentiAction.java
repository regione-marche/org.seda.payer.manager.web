package org.seda.payer.manager.entrate.actions;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;

import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.bancadati.webservice.srv.FaultType;
import com.seda.payer.bancadati.webservice.dati.Paginazione;
import com.seda.payer.bancadati.webservice.dati.RicercaPagamentiDocumentiRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaPagamentiDocumentiResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaPagamentiRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaTributiRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaTributiResponse;

public class RicercaPagamentiDocumentiAction extends BaseEntrateAction {

	private static final long serialVersionUID = 1L;
	//costanti pagina chiamante per INDIETRO
//	private final String RICERCA_DOCUMENTI = "ricercaDocumenti";
	private int rowsPerPage = 5;
	private int pageNumber = 1;
	private String order = "";
	
	
	public Object service(HttpServletRequest request) throws ActionException {

		salvaStato(request);

		super.service(request);
        switch(getFiredButton(request)) 
		{
		case TX_BUTTON_CERCA: 
/*
			if(chiamante == null )
				//errore
			if(chiamante.equals(PAGDOCUMENTI))	{
				request.setAttribute("rowsPerPage", request.getParameter("r2RowsPerPage"));
				request.setAttribute("pageNumber", request.getParameter("r2PageNumber"));
				request.setAttribute("order", request.getParameter("r2Order"));
			}
*/			
			break;
		case  TX_BUTTON_CERCA_EXP:
			try 
			{
/*				
				DettaglioDocumentoAction dettAction = new DettaglioDocumentoAction();
				
				dettAction.ricercaDettaglioDocumento(request);
*/
				//CHIAMO LA LISTA DEI TRIBUTI
				ricercaPagamenti(request);
			}
		catch (FaultType fte) 
		{
			WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
			fte.printStackTrace();
			setFormMessage("ricercaDocumentiForm", "errore: " + decodeMessaggio(fte), request);
		}
		catch (RemoteException af)
		{
			WSCache.logWriter.logError("errore: " + af.getMessage(),af);
			af.printStackTrace();
			setFormMessage("ricercaDocumentiForm", testoErroreColl, request);					
		}
		catch (Exception e) 
		   {
			WSCache.logWriter.logError("errore: " + e.getMessage(),e);
			//setErrorMessage(request, e.getMessage());
			setFormMessage("ricercaDocumentiForm", "errore: " + testoErrore, request);
			}
		break;
		}			
		return null;
	}

    public void ricercaPagamenti(HttpServletRequest request) throws Exception
    {
    	
			RicercaPagamentiDocumentiResponse out;
			RicercaPagamentiDocumentiRequest in;
			in = prepareRicercaPagamenti(request);
						
			WSCache.logWriter.logDebug("Pagina interrogazione di test");
			out =  WSCache.entrateBDServer.ricercaPagamentiDocumenti(in, request);
			
			if(out.getPInfo().getNumRows() > 0) {
				request.setAttribute("listaPagamenti", out.getListXml());

				request.setAttribute("listaPagamenti.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));		

			}else{
				String messaggio="";
				if (request.getAttribute("messaggioRis")!=null)
					messaggio = request.getAttribute("messaggioRis").toString() + " - ";
				setFormMessage("ricercaPagamentiForm", messaggio + Messages.NO_DATA_FOUND.format(), request);
			}
    }

   
    private String getFormatDate(java.util.Calendar data){
    	SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
    	return dateformat.format(data.getTime());
    } 
    
	private void resetPage(HttpServletRequest request){
		PropertiesTree configuration; 

		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

		rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

	      if (request.getAttribute("rowsPerPage")!=null&&!((String)request.getAttribute("rowsPerPage")).equals("")&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
	      {
	      	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
	      }
       
		pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();
		
	}
   
    private RicercaPagamentiDocumentiRequest prepareRicercaPagamenti(HttpServletRequest request)
	{
    	resetPage(request);
		
    	RicercaPagamentiDocumentiRequest ris;

		ris = new RicercaPagamentiDocumentiRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
		/*
		<s:textbox name="chiaveIS" label="chiaveISDett" bmodify="true" text="${chiaveIS}" cssclass="display_none" cssclasslabel="display_none"  />
		<!-- NUMERO DOCUMENTO EH1_CEH1NDOC -->
		<s:textbox name="chiaveDoc" label="chiaveDocDett" bmodify="true" text="${chiaveDoc}" cssclass="display_none" cssclasslabel="display_none"  />
		<!-- TIPO UFFICIO EH1_TANETUFF -->
		<s:textbox name="chiaveTipoUff" label="chiaveTipoUffDett" bmodify="true" text="${chiaveTipoUff}" cssclass="display_none" cssclasslabel="display_none"  />
		<!-- CODICE UFFICIO EH1_CANECUFF -->
		<s:textbox name="chiaveCodUff" label="chiaveCodUffDett" bmodify="true" text="${chiaveCodUff}" cssclass="display_none" cssclasslabel="display_none"  />
		<!-- CODICE UTENTE EH1_CUTECUTE -->
		<s:textbox name="chiaveCodUte" label="chiaveCodUteDett" bmodify="true" text="${chiaveCodUte}" cssclass="display_none" cssclasslabel="display_none"  />
		<!-- CODICE ENTE EH1_CANECENT -->
		<s:textbox name="chiaveCodEnte" label="chiaveCodEnteDett" bmodify="true" text="${chiaveCodEnte}" cssclass="display_none" cssclasslabel="display_none"  />
		<!-- CODICE SERVIZIO EH1_TEH1SERV -->
		<s:textbox name="chiaveServ" label="chiaveServDett" bmodify="true" text="${chiaveServ}" cssclass="display_none" cssclasslabel="display_none"  />
		<!-- CODICE TOMBSTONE EH1_CEH1TOMB -->
		<s:textbox name="chiaveTomb" label="chiaveTombDett" bmodify="true" text="${chiaveTomb}" cssclass="display_none" cssclasslabel="display_none"  />
		<!-- PROGR.FLUSSO EH1_PEH1FLUS -->
		<s:textbox name="chiaveFlusso" label="chiaveFlussoDett" bmodify="true" text="${chiaveFlusso}" cssclass="display_none" cssclasslabel="display_none"  />

		
		*/
		ris.setCodiceSocieta(request.getParameter(""));
        ris.setCodiceUtente(request.getParameter("chiaveCodUte"));
        ris.setCodiceEnte(request.getParameter("chiaveCodEnte"));
        ris.setCodiceUfficio(request.getParameter("chiaveCodUff"));
        ris.setTipoUfficio(request.getParameter("chiaveTipoUff"));
        String impostaServ = request.getParameter("dettImpostaServ");
        if(impostaServ== null) impostaServ = "";
        if( impostaServ.length()> 0 )
        	impostaServ = (impostaServ.substring(0,impostaServ.indexOf('-')).trim());
        else {
        	impostaServ = request.getParameter("chiaveIS"); 
        	if(impostaServ== null)
        		impostaServ = "";
        	else 
        		impostaServ = impostaServ.trim();
        }
        ris.setImpostaServizio(impostaServ);
        ris.setNumeroDocumento(request.getParameter("chiaveDoc"));

        return ris;
		
	}
	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
		/*
		 * Salvo le date che richiedono l'utilizzo di un tipo "Calendar"
		 */
	}



}
