package org.seda.payer.manager.util;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.seda.commons.properties.tree.PropertiesTree;


public class ValidatoreHelper {
	
	private String endPoint="";
	private String operation="";
	private RestTemplate restTemplate = null;
	private boolean proxy=false;
    private String proxyAddress;
	private int portNumber;
	private ObjectMapper objectMapper = null;
	private String bearer="";
	
	public ValidatoreHelper(String endPoint, String operation, boolean proxy, String proxyAddress, int port){
		super();
		this.proxy = proxy;
		this.proxyAddress = proxyAddress;
		this.portNumber = port;
		this.endPoint = endPoint;
		this.operation = operation;
		if(restTemplate == null){
			init();
		}
	}
	
	public ValidatoreHelper(String dbSchemaCodSocieta, HttpServletRequest request, boolean proxy, String proxyAddress, int port) throws Exception{
		super();
		this.proxy = proxy;
		this.proxyAddress = proxyAddress;
		this.portNumber = port;
		try{
			getValidatoreUrl(dbSchemaCodSocieta, request);
		}catch(Exception e){
			throw e;
		}
		if(restTemplate == null){
			init();
		}
	}
	
	void init() {
		System.out.println("NotificatoreHelper - init: Inizializzazione RestTemplate");
		try {
			objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
			//objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
			objectMapper.configure(MapperFeature.USE_ANNOTATIONS, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
			messageConverter.setObjectMapper(objectMapper);
			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			messageConverters.add(messageConverter);
			// PROXY
			if(proxy){
				SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
				Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(proxyAddress, portNumber));
				requestFactory.setProxy(proxy);
				restTemplate = new RestTemplate(requestFactory);
			}else{
				restTemplate=new RestTemplate();	
			}
			restTemplate.setMessageConverters(messageConverters);
		} catch (Throwable e) {
			System.out.println("NotificatoreHelper - init: Errore in inizializzazione RestTemplate " + e.getMessage());
		}
	}
	
	
	
	public boolean validaMail(String bearer, String email, HttpServletRequest request) {

		//Se il bearer passato è presente utilizzo quello, altrimenti quello letto dal file di configurazione
		if(bearer!=null && !bearer.equals(""))
			this.bearer=bearer;
			
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization",this.bearer);
		
		ValidatoreRequest v = new ValidatoreRequest(email);
		ValidatoreRequest[] listV = new ValidatoreRequest[1];
		listV[0]=v;
		
		try {
			System.out.println(objectMapper.writeValueAsString(listV));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		HttpEntity<ValidatoreRequest[]> req = new HttpEntity<ValidatoreRequest[]>(listV, headers);
		
		String url = endPoint.trim().concat(operation.trim());
		System.out.println("URLNotifiche: "+url);
		ResponseEntity<ValidatoreResponseWrapper> rssResponse = restTemplate.exchange(
				url,
				HttpMethod.PUT,
				req,
				ValidatoreResponseWrapper.class);
		try {
			System.out.println(objectMapper.writeValueAsString(rssResponse.getBody()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		boolean emailValid = rssResponse.getBody().getWebServiceOutput().getElement()[0].isValid();
		
		String errorMessage = rssResponse.getBody().getWebServiceOutput().getElement()[0].getErrorMessage();
		System.out.println("------Response------");
		System.out.println("Email: "+rssResponse.getBody().getWebServiceOutput().getElement()[0].getEmail());
		System.out.println("Email valida: "+rssResponse.getBody().getWebServiceOutput().getElement()[0].isValid());
		System.out.println("ErrorMessage: "+rssResponse.getBody().getWebServiceOutput().getElement()[0].getErrorMessage());
		if(errorMessage!=null && errorMessage.contains("catchAllServer")){
			System.out.println("Il server è catchAll, quindi non è detto che l'email sia valida");
			emailValid=true;
			request.setAttribute("messaggio", "Non è possibile validare la mail "+ email+" nel dominio "+ email.substring(email.indexOf("@")+1,email.length())+". La mail potrebbe essere non corretta, ma è stato comunque possibile proseguire con il salvataggio.<br>");
			//error.addErrorvalue("Non è possibile validare la mail "+ email+" nel dominio "+ email.substring(email.indexOf("@")+1,email.length())+". La mail potrebbe essere non corretta, ma è stato comunque possibile proseguire con il salvataggio");
		}
			
		
		return emailValid;
		
	}
	
	private void getValidatoreUrl(String dbSchemaCodSocieta, HttpServletRequest request) throws Exception{
		
			  PropertiesTree configuration; 
			  configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
			  
			  if(configuration.getProperty(PropertiesPath.wsValidatoreMailUrl.format(dbSchemaCodSocieta))!=null)
			  		this.endPoint =  configuration.getProperty(PropertiesPath.wsValidatoreMailUrl.format(dbSchemaCodSocieta));
			  else
				  throw new Exception("Errore: configurazione URL ws non disponibile");
			  
			  if(configuration.getProperty(PropertiesPath.wsValidatoreMailBearer.format(dbSchemaCodSocieta))!=null)
				   this.bearer =  configuration.getProperty(PropertiesPath.wsValidatoreMailBearer.format(dbSchemaCodSocieta));
			  else
				  throw new Exception("Errore: configurazione Bearer non disponibile");
			  
			  if(configuration.getProperty(PropertiesPath.wsValidatoreMailOperation.format(dbSchemaCodSocieta))!=null)
				  this.operation = configuration.getProperty(PropertiesPath.wsValidatoreMailOperation.format(dbSchemaCodSocieta));
			  else
				  throw new Exception("Errore: configurazione operation non disponibile");
			 
	}
	

}
