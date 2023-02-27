package org.seda.payer.manager.util;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class NETUtility {
	
	public static byte[] sendHTTPPOST(String url, String dataToSend, ArrayList<String[]> htmlHeaderList, boolean ignoreSSLSelfSigned, boolean ignoreSSLWrongCN) throws Exception{
		return sendHTTP(url, "POST", dataToSend, htmlHeaderList, ignoreSSLSelfSigned, ignoreSSLWrongCN);
	}
	
	public static byte[] sendHTTPGET(String url, ArrayList<String[]> htmlHeaderList, boolean ignoreSSLSelfSigned, boolean ignoreSSLWrongCN) throws Exception{
		return sendHTTP(url, "GET", null, htmlHeaderList, ignoreSSLSelfSigned, ignoreSSLWrongCN);
	}
	
	public static byte[] sendHTTP(String url, String mode, String dataToSend, ArrayList<String[]> htmlHeaderList, boolean ignoreSSLSelfSigned, boolean ignoreSSLWrongCN) throws Exception{
		
		if(ignoreSSLSelfSigned){
			TrustManager[] trustAllCerts = new TrustManager[]{
				    new X509TrustManager() {
				        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				            return null;
				        }
				        public void checkClientTrusted(
				            java.security.cert.X509Certificate[] certs, String authType) {
				        }
				        public void checkServerTrusted(
				            java.security.cert.X509Certificate[] certs, String authType) {
				        }
				    }
				};
			SSLContext sc = SSLContext.getInstance("SSL");
		    sc.init(null, trustAllCerts, new java.security.SecureRandom());
		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		}
		if(ignoreSSLWrongCN){
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, javax.net.ssl.SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		}
		
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		
		if(htmlHeaderList != null)
			for(String[] htmlHeader:htmlHeaderList)
				if(htmlHeader.length==2)
					connection.setRequestProperty(htmlHeader[0], htmlHeader[1]);

		if(mode.equals("POST") && dataToSend != null){
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(dataToSend.getBytes().length));
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(dataToSend);
			wr.flush();
			wr.close();
		}
		
		byte[] output = new byte[0];
		//inizio LP PG21XX04 Leak
		//if(connection.getResponseCode() >= 400)
		//	output = IOUtility.toByteArray(connection.getErrorStream());
		//else
		//	output = IOUtility.toByteArray(connection.getInputStream());
		//
		//connection.disconnect();
		//try {
		InputStream err = null;
		if(connection.getResponseCode() >= 400) {
			err = connection.getErrorStream();
			output = IOUtility.toByteArray(err);
		} else {
			err = connection.getInputStream();
			output = IOUtility.toByteArray(err);
		}
		connection.disconnect();
		//fine LP PG21XX04 Leak
		
		return output;
	}
	
	public static String getPOSTRedirectPage(String urlWithParameters){
		String url = urlWithParameters.split("\\?")[0];
		String[] paramterList = null;
		if(urlWithParameters.split("\\?").length==2)
			paramterList = urlWithParameters.split("\\?")[1].split("\\&");
		
		String postRedirect="<html><head><script type=\"text/javascript\">function redirect() {document.myForm.submit();}</script></head><body OnLoad='redirect();'><FORM name=\"myForm\" method=\"POST\" action=\""+url+"\"><input type=\"submit\" value=\"Continue\">";
		
		if(paramterList!=null)
			for(String parameter:paramterList)
				if(parameter.contains("="))
					if(parameter.split("=").length==2){
						postRedirect += "<input type=\"hidden\" name=\""+parameter.split("=")[0]+"\" value=\""+parameter.split("=")[1]+"\">";
					}else{
						postRedirect += "<input type=\"hidden\" name=\""+parameter.split("=")[0]+"\" value=\"\">";
					}
		
		postRedirect += "</FORM></body></html>";
		return postRedirect;
	}
	
	public static String getRandomUserAgent(){
		return getUserAgent(-1);
	}
	
	/*
	 * impostare -1 per avere random
	 * se si imposta un indice fuori range ritorna null
	*/
	public static String getUserAgent(int index){
		ArrayList<String> userAgentList = new ArrayList<String>();
		userAgentList.add("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
		userAgentList.add("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.5 Safari/537.17");
		userAgentList.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
		
		userAgentList.add("Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3 (FM Scene 4.6.1)");
		userAgentList.add("Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3 (.NET CLR 3.5.30729) (Prevx 3.0.5)");
		userAgentList.add("Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/525.19 (KHTML, like Gecko) Chrome/0.2.153.1 Safari/525.19");
		userAgentList.add("Mozilla/5.0 (X11; U; Linux i686; en-GB; rv:1.7.6) Gecko/20050405 Epiphany/1.6.1 (Ubuntu) (Ubuntu package 1.0.2)");
		userAgentList.add("Mozilla/5.0 (X11; U; Linux i686; en-US; Nautilus/1.0Final) Gecko/20020408");
		userAgentList.add("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:0.9.3) Gecko/20010801");
		userAgentList.add("Mozilla/5.0 (X11; Linux i686; U;rv: 1.7.13) Gecko/20070322 Kazehakase/0.4.4.1");
		userAgentList.add("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.2b) Gecko/20021007 Phoenix/0.3");
		userAgentList.add("Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.1) Gecko/2008092215 Firefox/3.0.1 Orca/1.1 beta 3");
		userAgentList.add("Mozilla/5.0 (X11; U; Linux i686; de-AT; rv:1.8.0.2) Gecko/20060309 SeaMonkey/1.0");
		userAgentList.add("Mozilla/5.0 (Macintosh; U; PPC Mac OS X Mach-O; en-US; rv:1.0.1) Gecko/20021219 Chimera/0.6");
		userAgentList.add("Mozilla/5.0 (Macintosh; U; PPC Mac OS X Mach-O; en-US; rv:1.0.1) Gecko/20030306 Camino/0.7");

		if(index < 0)
			return userAgentList.get(new Random().nextInt(userAgentList.size()));
		
		if(index >= userAgentList.size())
			return null;
		
		return userAgentList.get(index);
	}
	
	public static String removeAllHTMLTag(String htmlContent) throws Exception{

//		System.out.println(it.unicam.cs.utils.StringUtility.getCurrentTime("HH:mm:ss:SSS"));

		String lowHtmlContent = htmlContent.toLowerCase();
		String ret = "";
		for(int i=0;i<htmlContent.length();i++){
			if(htmlContent.charAt(i) == '<'){
				int partialEndTagId = htmlContent.indexOf('>',i);
				String tag = lowHtmlContent.substring(i, partialEndTagId+1);
				
				if(tag.equals("<br/>")){
					ret += "\n";
					i = partialEndTagId;
					continue;
				}
				
				//if(tag.endsWith("/>")){
				if(htmlContent.charAt(partialEndTagId-1)=='/'){
					i = partialEndTagId;
					continue;
				}
				
				if(tag.startsWith("<pre ") || tag.equals("<pre>")){
					int endIndex = lowHtmlContent.indexOf("</pre>",i);
					ret += " ";
					for(int j=i+tag.length();j<endIndex;j++)
						ret += htmlContent.charAt(j);
					ret += " ";
					i = endIndex + 5;
					continue;
				}
				if(tag.startsWith("<xmp ") || tag.equals("<xmp>")){
					int endIndex = lowHtmlContent.indexOf("</xmp>",i);
					ret += " ";
					for(int j=i+tag.length();j<endIndex;j++)
						ret += htmlContent.charAt(j);
					ret += " ";
					i = endIndex + 5;
					continue;
				}
				if(tag.startsWith("<textarea ") || tag.equals("<textarea>")){
					int endIndex = lowHtmlContent.indexOf("</textarea>",i);
					ret += " ";
					for(int j=i+tag.length();j<endIndex;j++)
						ret += htmlContent.charAt(j);
					ret += " ";
					i = endIndex + 10;
					continue;
				}
				
				if(tag.startsWith("<script ") || tag.equals("<script>")){
					int endIndex = lowHtmlContent.indexOf("</script>",i);
					i = endIndex + 8;
					continue;
				}
				
				if(tag.startsWith("<style ") || tag.equals("<style>")){
					int endIndex = lowHtmlContent.indexOf("</style>",i);
					i = endIndex + 7;
					continue;
				}
				
				ret += " ";
				i = partialEndTagId;
				continue;
			}
			else
				ret+=htmlContent.charAt(i);
		}
		//System.out.println(getCurrentTime("HH:mm:ss:SSS"));
		htmlContent = ret;
		htmlContent = htmlContent.replaceAll("( |\\t)+", " ");
		htmlContent = htmlContent.replaceAll("(\\r\\n|\\n)+(( )*(\\r\\n|\\n)*)*", "\n");
		/*
		//rimuovo tutto il codice tra script e styles
		htmlContent = htmlContent.replaceAll("<(?i)(script|style)(?s:.)*?>(?s:.)*?</(?i)(script|style)>", "");
		//sostituisco tutti i tag </br>
		htmlContent = htmlContent.replaceAll("(?i)<br/>", "\n");
		//rimuovo tutti i tag
		//htmlContent = htmlContent.replaceAll("<(?s:.)*?>", " ");
		//normalizzo spazi e a capo multipli
		htmlContent = htmlContent.replaceAll("( |\\t)+", " ");
		htmlContent = htmlContent.replaceAll("(\\r\\n|\\n)+(( )*(\\r\\n|\\n)*)*", "\n");
		//sarebbe da convertire tutti gli escape in caratteri
		*/
//		System.out.println(it.unicam.cs.utils.StringUtility.getCurrentTime("HH:mm:ss:SSS"));
		
		return htmlContent;
	}
	
	/*
	public static ArrayList<String> extractAllLinks(String url) throws Exception{
		ArrayList<String> ret = new ArrayList<String>();
		
		String path = "";
		if(url.split("/").length==2 || url.endsWith("/"))
			path = url;
		else
			path = url.substring(0, url.lastIndexOf("/"));
		
		ArrayList<String[]> params= new ArrayList<String[]>();
		params.add(new String[]{"User-Agent",getUserAgent(5)});

		String page = new String(sendHTTPGET(url,params,true,true));
		
		return ret;
	}
	*/
	public static void main(String[] args) {
		try {
			ArrayList<String[]> params= new ArrayList<String[]>();
			params.add(new String[]{"User-Agent",getUserAgent(5)});
			//System.out.println(removeAllHTMLTag("<div>asd</div><Br/>aaa<textarea ss>ad</textarea>jj<ddd>"));
			System.out.println(removeAllHTMLTag(new String(sendHTTPGET("http://stackoverflow.com/questions/704319/how-the-substring-function-of-string-class-works",params,false,false))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
