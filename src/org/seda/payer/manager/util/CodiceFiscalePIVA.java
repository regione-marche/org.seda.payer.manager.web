package org.seda.payer.manager.util;

import org.seda.payer.manager.ws.WSCache;

public class CodiceFiscalePIVA {

	public static String controlloCodiceFiscale_PartitaIva_NomeEmpty(String codFiscalePIVA, String labelCampo)
	{
		//il controllo required viene effettuato dal validatore
		String sCF_PI = codFiscalePIVA.trim();
		String esitoPartitaIva = "";
		
		//PG170060 12042017 - inizio
		if (sCF_PI.length() >= 5 && sCF_PI.substring(0, 5).equals("CFKO0")){
			if(sCF_PI.length()==16 && isNumeric(sCF_PI.substring(6)))
				return "";
			
			else
				return labelCampo + ": Codice fiscale non valido.";	
		}
		//PG170060 12042017 - fine
		
		if (sCF_PI.length() > 0)
		{
			//lunghezza 11: controllo partita iva
			if (sCF_PI.length() == 11) 
			{
				if (!IsInt64(sCF_PI))
				{
					return labelCampo + ": Valore non valido. ";
				}
				// else è intero è partita iva
				
				//controllo completo partita iva
				try
				{
					esitoPartitaIva = controllaPIVA(sCF_PI, labelCampo);
					if(!esitoPartitaIva.equals(""))	
						return esitoPartitaIva;
				}
				catch (Exception ex)
				{			
					WSCache.logWriter.logError(esitoPartitaIva, ex);
					return esitoPartitaIva;
				}		
			}
			else
			{
				//controllo codice fiscale
				try
				{			
					UCheckDigit uCodFiscal = new UCheckDigit(sCF_PI);
					boolean bIsCFCorretto = uCodFiscal.controllaCorrettezza();
					if (!bIsCFCorretto)
					{
						return labelCampo + ": Codice fiscale non valido.";
					}
				}
				catch (Exception ex)
				{			
					WSCache.logWriter.logError(labelCampo + ": Valore errato", ex);
					return labelCampo + ": Valore errato";
				}
			}
		}
		
		return "";
	}
	
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	//metodo per il controllo della partita IVA
	private static String controllaPIVA(String pi, String labelCampo)
	{
	    int i, c, s;
	    s = 0;
	    for( i=0; i<=9; i+=2 )
	    	s += pi.charAt(i) - '0';
	    for( i=1; i<=9; i+=2 ){
	       c = 2*( pi.charAt(i) - '0' );
	       if( c > 9 )  c = c - 9;
	       s += c;
	    }
	    if( ( 10 - s%10 )%10 != pi.charAt(10) - '0' || pi.equals("00000000000"))
	    {
	    	if (labelCampo.length() > 0)
	    		return labelCampo + ": partita IVA non valida. Il codice di controllo non corrisponde.";
	    	else
	    		return "Partita IVA: valore non valido. Il codice di controllo non corrisponde.";
	    }
	    return "";
	}
	

	
	
	public static boolean IsInt64(String sValue)
	{
		try
		{
			Long.valueOf(sValue);
			return true;
		}
		catch (Exception e) {
			WSCache.logWriter.logError("Conversione Int fallita", e);
			return false;
		}
	}
	
}
