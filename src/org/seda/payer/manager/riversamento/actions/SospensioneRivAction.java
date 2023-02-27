package org.seda.payer.manager.riversamento.actions;

/*
import javax.servlet.http.HttpServletRequest;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;


public class SospensioneRivAction extends BaseRiversamentoAction {

	
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
  	    super.service(request);
//		setProfile(request);	

		try {

	         preparazioneDatiDettaglio(request);

		} 
		catch (Exception e) {
			WSCache.logWriter.logError("errore:", e);
//			setErrorMessage(e.getMessage());
			setFormMessage("schedaRiversamentoForm", "errore: " + testoErrore, request);
		}
		return null;
	}

    public void preparazioneDatiDettaglio(HttpServletRequest request) 
    {
		requestData(request, "revData", "c1S");	//c1
		requestData(request, "c14", "c14S");
		requestData(request, "c15", "c15S");
		requestData(request, "c16", "c16S");
//		requestData(request, "c17", "c17S");

		String valore = request.getParameter("revTipo");	//c5
        if (valore.equals("R"))
        	request.setAttribute("c5S", "Rivers.");
        else
        	request.setAttribute("c5S", "Rendic.");

		valore = request.getParameter("revRive");	
        if (valore.equals("B"))
        	request.setAttribute("c6S", "Bonifico");
        else
        	request.setAttribute("c6S", "Altro");

        int c = (int)(request.getParameter("c7").charAt(0));
        switch (c)
        {
        case (int)'A': valore = "Aperto";
        		  	   break;
        case (int)'C': valore = "Chiuso";
        			   break;
        case (int)'S': valore = "Sospeso";
        			   break;
        case (int)'E': valore = "Eseguito";
        			   break;
        }
        
        request.setAttribute("c7S", valore);

        valore = request.getParameter("c3") + "/" + request.getParameter("c4");
        if (valore.equals(" /      "))
        	valore = "";
        
        request.setAttribute("c3S", valore);
    		
    }

	private void requestData(HttpServletRequest request, String chiaveS, String chiaveD)
	{
		String data = request.getParameter(chiaveS);
		if (data != null && data.length()>0 && !data.contains("1000-01-01"))
		{
			data = data.substring(0,10);
			request.setAttribute(chiaveD, data.substring(8,10) + "/" + 
					data.substring(5,7) + "/" + data.substring(0,4));
		}
		else
			request.setAttribute(chiaveD, "");

//		java.util.Calendar cal = getCalendarS(request, data);		
//		request.setAttribute("c1d", cal);

	}
}
*/