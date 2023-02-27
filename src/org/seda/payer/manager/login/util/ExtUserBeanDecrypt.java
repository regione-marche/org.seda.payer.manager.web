package org.seda.payer.manager.login.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Date;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtUserBeanDecrypt
{
  static Logger logger = LoggerFactory.getLogger(ExtUserBeanDecrypt.class);
  
  public static ExtUserBean decryptBearer(String token, String secret)
    throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
  {
    if (token.startsWith("Bearer ")) {
      token = token.substring(7);
    }
    AesEncryptDecrypt encryptDecrypt = new AesEncryptDecrypt(secret);
    if (token == null) {
      throw new IOException("Token is null");
    }
    JwtPayload jwtPayload = null;
    try
    {
      jwtPayload = checkTokenSignature(token, encryptDecrypt);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
    String completeIssuer = jwtPayload.getJwtIssuer();
    String issuer = encryptDecrypt.decryptString(completeIssuer.split(" ")[0]);
    System.out.println("issuer" + issuer);
    
    String jwtId = encryptDecrypt.decryptString(jwtPayload.getJwtId());
    Long expirationTime = jwtPayload.getLongExpirationTime();
    Date date = new Date();
    if (expirationTime.longValue() < date.getTime()) {
      logger.error("Token expired");
    }
    String[] splittedId = issuer.split("\\s+");
    ExtUserBean extUserBean = new ExtUserBean();
    extUserBean.setUsername(splittedId[0]);
    return extUserBean;
  }
  
  private static JwtPayload checkTokenSignature(String token, AesEncryptDecrypt encryptDecrypt)
    throws JwtAuthenticationException, InvalidKeyException, NoSuchAlgorithmException, IOException
  {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
    objectMapper.configure(MapperFeature.USE_ANNOTATIONS, true);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    String[] jwtTokenStrings = token.split("\\.");
    if (jwtTokenStrings.length != 3) {
      throw new JwtAuthenticationException("Failed to verify JWT token format");
    }
    String tokenJsonHeader = new String(Base64.getDecoder().decode(jwtTokenStrings[0].getBytes("UTF-8")), "UTF-8"); 
    String tokenJsonpayload = new String(Base64.getDecoder().decode(jwtTokenStrings[1].getBytes("UTF-8")), "UTF-8");
    String tokenSignature = jwtTokenStrings[2];
    JwtHeader jwtHeader = (JwtHeader)objectMapper.readValue(tokenJsonHeader, JwtHeader.class);
    JwtPayload jwtPayload = (JwtPayload)objectMapper.readValue(tokenJsonpayload, JwtPayload.class);
    if (!jwtHeader.getAlgorithm().equals("HS256")) {
      throw new JwtAuthenticationException("Failed to verify signature, only HS256  is supported!");
    }
    if (!jwtHeader.getType().equals("JWT")) {
      throw new JwtAuthenticationException("Failed to verify signature, only JWT type is supported!");
    }
    if (!tokenSignature.equals(encryptDecrypt.JWTbuildSignature(tokenJsonHeader, tokenJsonpayload))) {
      throw new JwtAuthenticationException("Failed to verify signature!");
    }
    return jwtPayload;
  }
}
