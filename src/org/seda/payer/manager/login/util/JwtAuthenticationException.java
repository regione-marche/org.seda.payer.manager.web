package org.seda.payer.manager.login.util;

public class JwtAuthenticationException extends Exception
{
	  private static final long serialVersionUID = 1L;
	  
	  public JwtAuthenticationException(String msg, Throwable t)
	  {
	    super(msg, t);
	  }
	  
	  public JwtAuthenticationException(String msg)
	  {
	    super(msg);
	  }
}

