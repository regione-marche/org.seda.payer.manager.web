package org.seda.payer.manager.ecmanager.actions.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
//import org.springframework.util.M;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NotificatoreHelper {
	
	private String endPoint = "";
	private String operation = "";
	private RestTemplate restTemplate = null;
	private boolean proxy=false;
    private String proxyAddress;
	private int portNumber;
	private Logger logger;
	private ObjectMapper objectMapper = null;
	
	/**
	 * @return if is proxy enabled
	 */
	public boolean isProxy() {
		return proxy;
	}

	/**
	 * @return proxyAddress
	 */
	public String getProxyAddress() {
		return proxyAddress;
	}

	/**
	 * @return portNumber
	 */
	public int getPortNumber() {
		return portNumber;
	}
	
	/**
	 * 
	 * @param endPoint     url
	 * @param operation    operation
	 * @param proxy        true if behind proxy
	 * @param proxyAddress proxy address
	 * @param port         proxy port
	 */
	public NotificatoreHelper(String endPoint, String operation, boolean proxy, String proxyAddress, int port, Logger log) {
		super();
		this.proxy=proxy;
		this.proxyAddress=proxyAddress;
		this.portNumber=port;
		this.endPoint=endPoint;
		this.operation=operation;
		this.logger=log;
		if (restTemplate == null) {
			init();
		}
	}
	
	void init() {
		logger.info("NotificatoreHelper - init: Inizializzazione RestTemplate");
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
			logger.error("NotificatoreHelper - init: Errore in inizializzazione RestTemplate "
					+ e.getMessage());
		}
	}
		
	public ResponseEntity<NotificheResponseWrapper> listNotifiche(NotificheLis notificheLis,String bearer,String parameterSel) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization",bearer);
		
		HttpEntity<NotificheLis> request = new HttpEntity<NotificheLis>(notificheLis, headers);
		
		try {
			System.out.println(objectMapper.writeValueAsString(notificheLis));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		String url = endPoint.trim().concat(operation.trim()).concat("?codAttoOrder=ASC&cfOrder=ASC&pageNumber=5&rowsPerPage=1");
		String url = endPoint.trim().concat(operation.trim()).concat("?").concat(parameterSel);
		System.out.println("URLNotifiche:"+url);
		ResponseEntity<NotificheResponseWrapper> rssResponse = restTemplate.exchange(
				url,
				HttpMethod.GET,
				request,
				NotificheResponseWrapper.class);
		return rssResponse;
	}
	
	public ResponseEntity<NotificaResponseWrapper> selNotifica(Notifica notifica,String bearer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization",bearer);
		
		HttpEntity<Notifica> request = new HttpEntity<Notifica>(notifica, headers);
		
		try {
			System.out.println(objectMapper.writeValueAsString(notifica));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = endPoint.trim().concat(operation.trim());
		ResponseEntity<NotificaResponseWrapper> rssResponse = restTemplate.exchange(
				url,
				HttpMethod.GET,
				request,
				NotificaResponseWrapper.class);
		return rssResponse;
	}
	
	public ResponseEntity<NotificheResponseWrapper> reSendNotifica(Notifiche notifiche,String  bearer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization",bearer);
		HttpEntity<Notifiche> request = new HttpEntity<Notifiche>(notifiche, headers);
		try {
			System.out.println(objectMapper.writeValueAsString(notifiche));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		ResponseEntity<NotificheResponseWrapper> rssResponse = restTemplate.exchange(
						endPoint.concat(operation),
					    HttpMethod.POST,
					    request,
					    NotificheResponseWrapper.class);
		return rssResponse;
	}
	
	
//	}

}
