package org.seda.payer.manager.util;

import java.io.UnsupportedEncodingException;

import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.security.ChryptoServiceException;
import com.seda.commons.security.TripleDESChryptoService;

public class Crypto {

	private Crypto(){}
	
	/**
	 * Crittografa una stringa con l'algoritmo 3DES in base 64
	 * @param stringToEncrypt
	 * @return String
	 * @throws UnsupportedEncodingException 
	 * @throws ChryptoServiceException 
	 */
	public static String encrypt(String stringToEncrypt) throws UnsupportedEncodingException, ChryptoServiceException
	{
		String securityIV = WSCache.securityIV;
		String securityKey = WSCache.securityKey;
		String encryptedString = null;
		TripleDESChryptoService desChrypto = new TripleDESChryptoService();
		desChrypto.setIv(securityIV);
		desChrypto.setKeyValue(securityKey);
		encryptedString = desChrypto.encryptBase64(stringToEncrypt);
		return encryptedString;
	}

	/**
	 * Decrittografa una stringa crittografata con l'algoritmo 3DES in base 64
	 * @param encryptedString
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws ChryptoServiceException 
	 */
	public static String decrypt(String encryptedString) throws UnsupportedEncodingException, ChryptoServiceException
	{
		String securityIV = WSCache.securityIV;
		String securityKey = WSCache.securityKey;
		String decryptedString = null;
		TripleDESChryptoService desChrypto = new TripleDESChryptoService();
		desChrypto.setIv(securityIV);
		desChrypto.setKeyValue(securityKey);
		decryptedString = desChrypto.decryptBase64(encryptedString);
		return decryptedString;
	}

}
