package org.seda.payer.manager.ws;

import com.seda.payer.pgec.webservice.commons.srv.FaultType;;
import com.seda.payer.pgec.webservice.prenotazioneFatturazione.dati.PrenotazioneFatturazioneSaveRequest;
import com.seda.payer.pgec.webservice.prenotazioneFatturazione.dati.StatusResponse;
import com.seda.payer.pgec.webservice.prenotazioneFatturazione.source.PrenotazioneFatturazioneImplementationStub;
import com.seda.payer.pgec.webservice.prenotazioneFatturazione.source.PrenotazioneFatturazioneServiceLocator;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;

public class PrenotazioneFatturazioneServer extends BaseServer {

    private static final long serialVersionUID = 1L;
    private PrenotazioneFatturazioneImplementationStub prenotazioneFatturazioneCaller = null;

    private void setCodSocietaHeader(PrenotazioneFatturazioneImplementationStub stub, HttpServletRequest request) {
        stub.clearHeaders();
        stub.setHeader("", DBSCHEMACODSOCIETA, getCodSocieta(request));
    }

    public PrenotazioneFatturazioneServer(String endPoint) {
        PrenotazioneFatturazioneServiceLocator lsService = new PrenotazioneFatturazioneServiceLocator();
        lsService.setPrenotazioneFatturazionePortEndpointAddress(endPoint);
        try {
            prenotazioneFatturazioneCaller = (PrenotazioneFatturazioneImplementationStub)lsService.getPrenotazioneFatturazionePort();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public StatusResponse savePrenotazioneFatturazione(PrenotazioneFatturazioneSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType {
        setCodSocietaHeader(prenotazioneFatturazioneCaller, request);
        return prenotazioneFatturazioneCaller.save(in);
    }
}
