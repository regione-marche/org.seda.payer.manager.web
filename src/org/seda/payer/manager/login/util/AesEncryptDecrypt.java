package org.seda.payer.manager.login.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AesEncryptDecrypt
{
  private static String CYPHER = "AES";
  protected SecretKeySpec secretKey;
  protected Cipher cipher;
  private static String UTF8 = "UTF-8";
  
  public AesEncryptDecrypt(String secret)
    throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException
  {
    this(secret, 16, CYPHER);
  }
  
  private AesEncryptDecrypt(String secret, int length, String algorithm)
    throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException
  {
    if (secret == null) {
      throw new InvalidKeySpecException("Secret key must be defined. Check your spring configuration file.");
    }
    byte[] key = new byte[length];
    key = fixSecret(secret, length);
    this.secretKey = new SecretKeySpec(key, algorithm);
    this.cipher = Cipher.getInstance(algorithm);
  }
  
  private static byte[] fixSecret(String s, int length)
    throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException
  {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(s.toCharArray(), String.valueOf(s.length()).getBytes(UTF8), 65536, 128);
    SecretKey tmp = factory.generateSecret(spec);
    return tmp.getEncoded();
  }
  
  public String getSecret()
  {
    return DatatypeConverter.printBase64Binary(this.secretKey.getEncoded());
  }
  
  public void encryptFile(File f)
    throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException
  {
    this.cipher.init(1, this.secretKey);
    writeToFile(f);
  }
  
  public void decryptFile(File f)
    throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException
  {
    this.cipher.init(2, this.secretKey);
    writeToFile(f);
  }
  
  public String encryptString(String s)
    throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException
  {
    this.cipher.init(1, this.secretKey);
    

    return DatatypeConverter.printBase64Binary(writeToString(s).getBytes("ISO-8859-1"));
  }
  
  public String decryptString(String s)
    throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException
  {
    String decrypted = "";
    try
    {
      s = new String(DatatypeConverter.parseBase64Binary(new String(s.getBytes("ISO-8859-1"))), "ISO-8859-1");
      this.cipher.init(2, this.secretKey);
      decrypted = writeToString(s);
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      decrypted = s;
    }
    return decrypted;
  }
  
  public void writeToFile(File f)
    throws IOException, IllegalBlockSizeException, BadPaddingException
  {
    FileInputStream in = new FileInputStream(f);
    byte[] input = new byte[(int)f.length()];
    in.read(input);
    FileOutputStream out = new FileOutputStream(f);
    byte[] output = this.cipher.doFinal(input);
    out.write(output);
    out.flush();
    out.close();
    in.close();
  }
  
  public String writeToString(String s)
    throws IOException, IllegalBlockSizeException, BadPaddingException
  {
    byte[] input = s.getBytes("ISO-8859-1");
    byte[] output = this.cipher.doFinal(input);
    return new String(output, "ISO-8859-1");
  }
  
  public String JWTbuildSignature(String header, String payload)
    throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException
  {
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    sha256_HMAC.init(this.secretKey);
    String base64Header = DatatypeConverter.printBase64Binary(header.getBytes(UTF8));
    String base64payload = DatatypeConverter.printBase64Binary(payload.getBytes(UTF8));
    String data = base64Header.concat(".").concat(base64payload);
    return DatatypeConverter.printBase64Binary(sha256_HMAC.doFinal(data.getBytes(UTF8)));
  }
  
  public static String JWTbuildSignature(String secret, String header, String payload)
    throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeySpecException
  {
    byte[] key = new byte[16];
    key = fixSecret(secret, 16);
    SecretKeySpec secretKey = new SecretKeySpec(key, CYPHER);
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    sha256_HMAC.init(secretKey);
    String base64Header =  DatatypeConverter.printBase64Binary(header.getBytes(UTF8));
    String base64payload = DatatypeConverter.printBase64Binary(payload.getBytes(UTF8));
    String data = base64Header.concat(".").concat(base64payload);
    return DatatypeConverter.printBase64Binary(sha256_HMAC.doFinal(data.getBytes(UTF8)));
  }
  
  public static void main(String[] args)
    throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException
  {
    AesEncryptDecrypt encryptDecript = new AesEncryptDecrypt("a10so3kd#2@3è][01]");
    String notEncrytpted = "";
    System.out.println("notEncrytpted   : " + notEncrytpted);
    String encrypted = encryptDecript.encryptString(notEncrytpted);
    System.out.println("Encrypted :" + encrypted);
    String decrypted = encryptDecript.decryptString(encrypted);
    System.out.println("decrypted   : " + decrypted);
    System.out.println("Encrypting test was " + decrypted.equals(notEncrytpted) + " !");
    


    encryptDecript.decryptString("*************************");
  }
}
