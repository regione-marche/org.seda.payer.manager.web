package org.seda.payer.manager.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class XMLUtility {

	
	@SuppressWarnings({ "unchecked", "restriction" })
	public static Document signXMLDocument(Document doc, String keystorePath, String keystoreType, String keystorePassword, String certificateAlias, String certificatePassword) throws Exception{

//		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
		XMLSignatureFactory fac = 
		    XMLSignatureFactory.getInstance
		        ("DOM", new org.jcp.xml.dsig.internal.dom.XMLDSigRI());


		Reference ref = fac.newReference("", fac.newDigestMethod(DigestMethod.SHA1, null), Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)), null, null);
		
		SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,(C14NMethodParameterSpec) null), fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

		KeyStore ks = KeyStore.getInstance(keystoreType);
		//inizio LP PG21XX04 Leak
		//ks.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());
		FileInputStream fsi = null;
		try {
			fsi = new FileInputStream(keystorePath);
			ks.load(fsi, keystorePassword.toCharArray());
		//fine LP PG21XX04 Leak
		KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry (certificateAlias, new KeyStore.PasswordProtection(certificatePassword.toCharArray()));
		X509Certificate cert = (X509Certificate) keyEntry.getCertificate();

		KeyInfoFactory kif = fac.getKeyInfoFactory();
		@SuppressWarnings("rawtypes")
		List x509Content = new ArrayList();
		x509Content.add(cert.getSubjectX500Principal().getName());
		x509Content.add(cert);
		X509Data xd = kif.newX509Data(x509Content);
		KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));
		
		DOMSignContext dsc = new DOMSignContext(keyEntry.getPrivateKey(), doc.getDocumentElement());

		XMLSignature signature = fac.newXMLSignature(si, ki);

		signature.sign(dsc);
		
		return doc;
		//inizio LP PG21XX04 Leak
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if(fsi != null) {
				try {
					fsi.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		
	}
	
	public static boolean verifySignature(Document doc , String pathCert) {
		//inizio LP PG21XX04 Leak
		FileInputStream fsi = null;
		//fine LP PG21XX04 Leak
		try{
			if (doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature").getLength() == 0)
				throw new Exception("Cannot find Signature element");
	
			//inizio LP PG21XX04 Leak
			//X509Certificate cert = (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(new File(pathCert)));
			fsi = new FileInputStream(new File(pathCert));
			X509Certificate cert = (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(fsi);
			//fine LP PG21XX04 Leak
			DOMValidateContext valContext = new DOMValidateContext(cert.getPublicKey(), doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature").item(0));
	
			XMLSignature signature = XMLSignatureFactory.getInstance("DOM").unmarshalXMLSignature(valContext);
	
			return signature.validate(valContext); 
		}catch(Exception e){ e.printStackTrace();}
		//inizio LP PG21XX04 Leak
	    finally {
		  if(fsi != null) {
			  try {
				fsi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
	    }
		//fine LP PG21XX04 Leak
		return false;
	}
	
	public static Document getXmlDocFromString(String xml) throws Exception{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		return dbf.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
	}
	
	public static Document getXmlDocFromFile(String xmlFile) throws Exception{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		return dbf.newDocumentBuilder().parse(new File(xmlFile));
	}
	
	public static String getStringFromXmlDoc(org.w3c.dom.Node node) throws Exception{
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(node), new StreamResult(writer));
		return writer.getBuffer().toString().replaceAll("\n|\r", "");
    }
	
	public static Document createNewDocument() throws Exception{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		return dbf.newDocumentBuilder().newDocument();
	}
	
	public static String base64Encode(byte[] data){
		return javax.xml.bind.DatatypeConverter.printBase64Binary(data);
	}
	public static byte[] base64Decode(String data){
		return javax.xml.bind.DatatypeConverter.parseBase64Binary(data);
	}
	public static byte[] base64DecodeV2(byte[] sArr)
	{
		int[] IA = new int[256];
		char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
		
		Arrays.fill(IA, -1);
		for (int i = 0, iS = CA.length; i < iS; i++)
			IA[CA[i]] = i;
		IA['='] = 0;
		int sLen = sArr.length;
		int sepCnt = 0;
		for (int i = 0; i < sLen; i++)
			if (IA[sArr[i] & 0xff] < 0)
				sepCnt++;

		if ((sLen - sepCnt) % 4 != 0)
			return null;

		int pad = 0;
		for (int i = sLen; i > 1 && IA[sArr[--i] & 0xff] <= 0;)
			if (sArr[i] == '=')
				pad++;

		int len = ((sLen - sepCnt) * 6 >> 3) - pad;
		byte[] dArr = new byte[len];

		for (int s = 0, d = 0; d < len;) {
			int i = 0;
			for (int j = 0; j < 4; j++) {
				int c = IA[sArr[s++] & 0xff];
				if (c >= 0)
				    i |= c << (18 - j * 6);
				else
					j--;
			}
			dArr[d++] = (byte) (i >> 16);
			if (d < len) {
				dArr[d++]= (byte) (i >> 8);
				if (d < len)
					dArr[d++] = (byte) i;
			}
		}
		return dArr;
	}
	
	public static byte[] cipher3DES(boolean encrypt, byte[] message, byte[] key, byte[] ivParameter, boolean noPadding) throws Exception {
		try{
			/*
			MessageDigest md = MessageDigest.getInstance("md5");
	    	byte[] digestOfPassword = md.digest(key.getBytes("utf-8"));
	    	byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
	    	//for (int j = 0, k = 16; j < 8;)
	    		//keyBytes[k++] = keyBytes[j++];
			*/
	    	if(key.length != 24)
	    		throw new Exception("key size must be 24 bytes");
	    	
			String blockMode = "CBC";
			if(ivParameter == null)
				blockMode = "ECB";
			
			int cipherMode = Cipher.DECRYPT_MODE;
			if(encrypt)
				 cipherMode = Cipher.ENCRYPT_MODE;
			String padding = "PKCS5Padding";
			if(noPadding)
				padding = "NoPadding";
			Cipher sendCipher = Cipher.getInstance("DESede/"+blockMode+"/"+padding);
			SecretKey myKey =  new SecretKeySpec(key, "DESede");
			
			if(ivParameter==null)
				sendCipher.init(cipherMode, myKey);
			else
				sendCipher.init(cipherMode, myKey, new IvParameterSpec(ivParameter));
			
			return sendCipher.doFinal(message);
			
		}catch(Exception e){e.printStackTrace();}
		return new byte[0];
    }
	/*
	public static void main(String[] args){
		try {
			//Document xml = XMLUtility.getXmlDocFromFile("C:\\Users\\mio\\Desktop\\aa.xml");
			//System.out.println(XMLUtility.verifySignature(xml, "C:\\Users\\mio\\Desktop\\cohesion2.cer"));
			String message = "ciao a tutti quantiiiiiiii";
			while(message.length()%8 != 0 )
				message += " ";
			String key = "aaaaaaaaaaaaaaaaaaaaaaaa";
			System.out.println(new String(XMLUtility.cipher3DES(false, XMLUtility.cipher3DES(true, message.getBytes(), key.getBytes(), new byte[8],true),key.getBytes(), new byte[8],true)));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
}
