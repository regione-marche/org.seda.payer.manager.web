package org.seda.payer.manager.util;

import java.util.ArrayList;

import javax.xml.crypto.dsig.XMLSignature;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class MySOAPClient {
	
	private String url = "", soapAction = "";
	private Document envelopeXml = null;
	//private Document wsdlXml = null;
	private String responseEnvelope = "";
	
	public MySOAPClient(String url) throws Exception{
		this.url=url;
		//setWSDL(null);
	} 

	public void addEnvelopeHeader(Element headerEl){
		envelopeXml.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Header").item(0).appendChild(headerEl);
	}
	
	public Element createEnvelopeElement(String namespace, String name){
		return envelopeXml.createElementNS(namespace, name);
	}
	public void setTextToEnvelopeElement(Element element, String text){
		Text textVal = envelopeXml.createTextNode(text);
		element.appendChild(textVal);
	}
	/*
	public void setWSDL(String wsdlURI){
		if(wsdlURI == null)
			wsdlURI = url + "?wsdl";
		try{
			if(wsdlURI.startsWith("http"))
				wsdlXml = XMLUtility.getXmlDocFromString(new String(NETUtility.sendHTTPGET(wsdlURI, null, true, true),"UTF-8"));
			else
				wsdlXml = XMLUtility.getXmlDocFromFile(wsdlURI);
		}catch(Exception e){}
	}
	*/
	public void createEnvelope(String namespace, String methodName, String soapAction, ArrayList<String[]> parameterList) throws Exception{
		setSOAPAction(soapAction);
		createEnvelope(namespace, methodName, parameterList);
	}
	
	public void createEnvelope(String namespace, String methodName, ArrayList<String[]> parameterList) throws Exception{
		
		envelopeXml = XMLUtility.createNewDocument();
		org.w3c.dom.Element envelope = envelopeXml.createElementNS("http://schemas.xmlsoap.org/soap/envelope/", "Envelope");
		envelopeXml.appendChild(envelope);
		org.w3c.dom.Element header = envelopeXml.createElementNS("http://schemas.xmlsoap.org/soap/envelope/", "Header");
		org.w3c.dom.Element body = envelopeXml.createElementNS("http://schemas.xmlsoap.org/soap/envelope/", "Body");
		envelope.appendChild(header);
		envelope.appendChild(body);
		
		org.w3c.dom.Element methodEl = envelopeXml.createElementNS(namespace, methodName);
		envelopeXml.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/","Body").item(0).appendChild(methodEl);
		
		for(String[] parameter:parameterList)
			if(parameter.length == 2){
				org.w3c.dom.Element paramEl = envelopeXml.createElementNS(namespace, parameter[0]);
				Text paramVal = envelopeXml.createTextNode(parameter[1]);
				paramEl.appendChild(paramVal);
				methodEl.appendChild(paramEl);
			}
	}
	public void setSOAPAction(String soapAction){
		this.soapAction = soapAction;
	}
	public void loadEnvelope(Document envelope){
		envelopeXml = envelope;
	}
	public void loadEnvelope(String envelope) throws Exception{
		envelopeXml = XMLUtility.getXmlDocFromString(envelope);
	}
	public Document getEnvelope(){
		return envelopeXml;
	}
	
	public void callWS() throws Exception{
		callWS(null, false , false);
	}
	
	public void callWS(boolean ignoreSSLSelfSigned, boolean ignoreSSLWrongCN) throws Exception{
		callWS(null, ignoreSSLSelfSigned , ignoreSSLWrongCN);
	}
	
	public void callWS(ArrayList<String[]> additionalHtmlHeaderList, boolean ignoreSSLSelfSigned, boolean ignoreSSLWrongCN) throws Exception{
		
		ArrayList<String[]> htmlHeaderList = new ArrayList<String[]>();
		htmlHeaderList.add(new String[]{"Content-Type", "text/xml; charset=UTF-8"});
		if(soapAction != null && soapAction != "")
			htmlHeaderList.add(new String[]{"SOAPAction", soapAction});
		
		if(additionalHtmlHeaderList != null)
			for(String[] additionalHtmlHeader:additionalHtmlHeaderList)
				if(additionalHtmlHeader.length == 2)
					htmlHeaderList.add(new String[]{additionalHtmlHeader[0],additionalHtmlHeader[1]});
		
		String dataToSend = XMLUtility.getStringFromXmlDoc(envelopeXml);
		
		responseEnvelope = new String(NETUtility.sendHTTPPOST(url, dataToSend, htmlHeaderList, ignoreSSLSelfSigned, ignoreSSLWrongCN),"UTF-8");
	}
	
	public String getWsRequestDUMP() throws Exception{
		return XMLUtility.getStringFromXmlDoc(envelopeXml);
	}
	public String getWsResponseDUMP(){
		return responseEnvelope;
	}
	public String getWsResponseBodyEnvelope(){
		try {
			Document ret = XMLUtility.getXmlDocFromString(responseEnvelope);
			NodeList nodeLst = ret.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/","Body").item(0).getChildNodes();
			
			String bodyString = "";
			for(int i=0;i<nodeLst.getLength();i++)
				bodyString += XMLUtility.getStringFromXmlDoc(nodeLst.item(i));
			
			if(bodyString=="")
				bodyString = ret.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/","Body").item(0).getTextContent();
			return bodyString;
		} catch (Exception e) {e.printStackTrace();}
		return responseEnvelope;
	}
	
	public void signEnvelope(String keystorePath, String keystoreType, String keystorePassword, String certificateAlias, String certificatePassword) throws Exception{
		
		envelopeXml = XMLUtility.getXmlDocFromString(XMLUtility.getStringFromXmlDoc(envelopeXml)); //NECESSARIO PER AVERE LA FIRMA VALIDA
		
		envelopeXml = XMLUtility.signXMLDocument(envelopeXml, keystorePath, keystoreType, keystorePassword, certificateAlias, certificatePassword);
		org.w3c.dom.Node sign = envelopeXml.getElementsByTagNameNS(XMLSignature.XMLNS,"Signature").item(0);
		envelopeXml.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Envelope").item(0).removeChild(sign);
		envelopeXml.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/","Header").item(0).appendChild(sign);
	}
	
	/*
	public static void main(String[] args) {
		try{
			
			MySOAPClient mySOAPClient = new MySOAPClient("http://cohesion.regione.marche.it/sso/WsCheckSessionSSO.asmx");
			
			ArrayList<String[]> parameterList= new ArrayList<String[]>();
			parameterList.add(new String[]{"IdSessioneSSO","aa"});
			parameterList.add(new String[]{"IdSessioneASPNET","aa"});
			
			mySOAPClient.createEnvelope("http://tempuri.org/", "GetCredential", parameterList);
			mySOAPClient.setSOAPAction("http://tempuri.org/GetCredential");
			
			//Aggiungo Header Cohesion
			Element cohesionHeader = mySOAPClient.createEnvelopeElement("http://uddi.regione.marche.it/Cohesion","Cohesion");
			Element enteIdHeader = mySOAPClient.createEnvelopeElement("http://uddi.regione.marche.it/Cohesion", "enteId");
			Element userProfileHeader = mySOAPClient.createEnvelopeElement("http://uddi.regione.marche.it/Cohesion", "userProfile");
			mySOAPClient.setTextToEnvelopeElement(enteIdHeader, "test");
			cohesionHeader.appendChild(enteIdHeader);
			cohesionHeader.appendChild(userProfileHeader);
			mySOAPClient.addEnvelopeHeader(cohesionHeader);
			
			mySOAPClient.signEnvelope("D:\\Universita\\Cohesion\\COHESION DA VEDERE\\SERVLET TEST\\my.p12", "PKCS12", "pwd", "be7446f14507a331c1b5b8ff70a66520_d0e8d7b1-bcb5-4128-9d8e-2fc4dc332132", "pwd");
			
			mySOAPClient.callWS(true, true);
			
			System.out.println("REQUEST : " + mySOAPClient.getWsRequestDUMP());
			System.out.println("RESPONSE: " + mySOAPClient.getWsResponseBodyEnvelope());
			
		}catch(Exception e){e.printStackTrace();}
	}
	*/
}