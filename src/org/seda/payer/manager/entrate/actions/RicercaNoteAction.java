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
import com.seda.payer.bancadati.webservice.dati.RecuperaDettaglioTributiRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDettaglioTributiResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaNoteRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaNoteResponse;

public class RicercaNoteAction extends BaseEntrateAction {

	private static final long serialVersionUID = 1L;
	//costanti pagina chiamante per INDIETRO
	private final String RICERCA_DOCUMENTI = "ricercaDocumenti";
	private int rowsPerPage = 5;
	private int pageNumber = 1;
	private String order = "";
	
	
	public Object service(HttpServletRequest request) throws ActionException {

		salvaStato(request);

		super.service(request);
		try
		{	
/*
			DettaglioDocumentoAction dettAction = new DettaglioDocumentoAction();
					
			dettAction.ricercaDettaglioDocumento(request);
*/			
			//CHIAMO IL DETTAGLIO DEI TRIBUTI
			ricercaDettaglioTributo(request);
			
			ricercaNote(request);
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
		return null;
	}

    public void ricercaNote(HttpServletRequest request) throws Exception
    {
    	
			RicercaNoteResponse out;
			RicercaNoteRequest in;
			in = prepareRicercaNote(request);
						
			WSCache.logWriter.logDebug("Pagina interrogazione di test");
			out =  WSCache.entrateBDServer.ricercaNote(in, request);
			
			if(out.getPInfo().getNumRows() > 0) {
				request.setAttribute("listaNote", out.getListXml());

				request.setAttribute("listaNote.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));		

			}else{
				String messaggio="";
				if (request.getAttribute("messaggioRis")!=null)
					messaggio = request.getAttribute("messaggioRis").toString() + " - ";
				setFormMessage("ricercaNoteForm", messaggio + Messages.NO_DATA_FOUND.format(), request);
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
    
    
    
    
    private RicercaNoteRequest prepareRicercaNote(HttpServletRequest request)
	{
    	resetPage(request);
		
    	RicercaNoteRequest ris;

		ris = new RicercaNoteRequest();
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
		//&chiaveCodTrib={2}&chiaveAnnoTrib={3}&chiaveProgTrib{1}
		
		ris.setCodiceSocieta(request.getParameter(""));
        ris.setCodiceUtente(request.getParameter("chiaveCodUte"));
        ris.setCodiceEnte(request.getParameter("chiaveCodEnte"));
        ris.setCodiceUfficio(request.getParameter("chiaveCodUff"));
        ris.setTipoUfficio(request.getParameter("chiaveTipoUff"));
        ris.setImpostaServizio(request.getParameter("chiaveIS"));
        ris.setNumeroDocumento(request.getParameter("chiaveDoc"));
        
        ris.setChiaveProg(request.getParameter("chiaveProgTrib"));
        ris.setChiaveArticolo(request.getParameter("chiaveAnnoTrib"));
        ris.setChiaveTributo(request.getParameter("chiaveCodTrib"));

        ris.setChiaveFlusso(request.getParameter("chiaveFlTrib"));
        ris.setChiaveServizio(request.getParameter("chiaveSrTrib"));
        ris.setChiaveTomb(request.getParameter("chiaveTbTrib"));
        
        return ris;
		
	}
    
	private RecuperaDettaglioTributiRequest prepareRicercaDettaglioTributo(HttpServletRequest request)
	{

		RecuperaDettaglioTributiRequest rec = new RecuperaDettaglioTributiRequest();
		rec.setCodiceSocieta(request.getParameter(""));

		
		
		rec.setCodiceUtente(request.getParameter("chiaveCodUte"));
		rec.setCodiceEnte(request.getParameter("chiaveCodEnte"));
		rec.setCodiceUfficio(request.getParameter("chiaveCodUff"));
		rec.setTipoUfficio(request.getParameter("chiaveTipoUff"));
        rec.setChiaveFlusso(request.getParameter("chiaveFlTrib"));
        rec.setImpostaServizio(request.getParameter("chiaveIS"));
        rec.setNumeroDocumento(request.getParameter("chiaveDoc"));
        rec.setChiaveServizio(request.getParameter("chiaveSrTrib"));
        rec.setChiaveTomb(request.getParameter("chiaveTbTrib"));
        rec.setChiaveProg(request.getParameter("chiaveProgTrib"));
        rec.setChiaveArticolo(request.getParameter("chiaveAnnoTrib"));
        rec.setChiaveTributo(request.getParameter("chiaveCodTrib"));

		return rec;
		
	}

    
    
    
    public void ricercaDettaglioTributo(HttpServletRequest request) throws Exception
    {
    	RecuperaDettaglioTributiResponse out;
		RecuperaDettaglioTributiRequest in;
		in = prepareRicercaDettaglioTributo(request);
						
		WSCache.logWriter.logDebug("Pagina dettaglio riversamento");
		out =  WSCache.entrateBDServer.dettaglioTributo(in, request); //.//RecuperaD(in);
        
		request.setAttribute("dettaglioTributo", out.getListXml());

    }

	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
		/*
		 * Salvo le date che richiedono l'utilizzo di un tipo "Calendar"
		 */
	}



}
