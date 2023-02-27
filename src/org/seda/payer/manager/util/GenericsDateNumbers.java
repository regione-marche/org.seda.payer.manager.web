package org.seda.payer.manager.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GenericsDateNumbers {

	/*********************************** OPERAZIONI DATE - CALENDAR **************************/
	public static Calendar getMinDate()
	{
		Timestamp timestamp = Timestamp.valueOf("1000-01-01 00:00:00.000");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp.getTime());

		//Calendar cal = Calendar.getInstance();
		//cal.set(1970,0,1,0,0,0);
		return cal;
	}
	
	public static Calendar getCalendarFromDate(java.util.Date date) {
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	public static Calendar setLocaleTimeZone(Calendar cal)
	{
		if (cal == null)
			return null;
		
		Calendar calOut = Calendar.getInstance(Locale.ITALIAN);
		calOut.setTimeInMillis(cal.getTimeInMillis());
		return calOut;
	}
	

	public static String formatCalendarData(Calendar data, String sFormato)
	{
		if (data != null)
		{
			Calendar cal = Calendar.getInstance(Locale.ITALIAN);
			cal.setTime(data.getTime());
		
			SimpleDateFormat formatDateTime = new SimpleDateFormat(sFormato);
			return formatDateTime.format(cal.getTime());
		}
		else
			return "";
	}
	/**
	 * Converte una data stringa da un formato ad un altro
	 * @param sData
	 * @param sFormatoOld
	 * @param sFormatoNew
	 * @return
	 */
	public static String formatData(String sData, String sFormatoOld, String sFormatoNew)
	{
		if (sData != null && !sData.equals(""))
		{
			SimpleDateFormat formatDateTimeOld = new SimpleDateFormat(sFormatoOld);
			SimpleDateFormat formatDateTimeNew = new SimpleDateFormat(sFormatoNew);
	    	try
	    	{
				Date date = formatDateTimeOld.parse(sData);
				
				return formatDateTimeNew.format(date);
	    	}
	    	catch (Exception e) {}
		}
		return "";
	}
	
	/***
	 * Ritorna un Calendar da una data passata nel formato passato come parametro
	 * @param sDataDDMMYYYY
	 * @return
	 */
	public static Calendar getCalendarFromDateString(String sData, String sDateFormat)
	{
		if (sData != null && !sData.equals(""))
		{
			try
			{
				SimpleDateFormat formatDateTime = new SimpleDateFormat(sDateFormat);
				Date date = formatDateTime.parse(sData);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				return cal;
			} 
			catch (Exception e) {
				return getMinDate();
			}
		}
		
		return getMinDate();
	}
	
	/*********************************** OPERAZIONI NUMERI **************************/

	public static String formatDecimalNumber(BigDecimal bdValue)
	{
		DecimalFormat dcFormat = getDecimalFormat();
		bdValue = bdValue.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		return dcFormat.format(bdValue);
	}
	
	public static DecimalFormat getDecimalFormat()
	{
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(); 
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator('.');

		DecimalFormat dcFormat = new DecimalFormat("#0.00", symbols);
		return dcFormat;
	}
	
	public static String formatNumToString(int iLenght, String sNumToFormat)
	{
		String formattedString = sNumToFormat;

		while(formattedString.length() < iLenght) 
		{
			formattedString = "0" + formattedString;
		}
		return formattedString;
	}
	
	public static BigDecimal getBigDecimalFromString(String sParteIntera, String sParteDecimale)
	{
		if (sParteIntera.equals(""))
			sParteIntera = "0";
		if (sParteDecimale.equals(""))
			sParteDecimale = "00";
		
		BigDecimal bdRes = new BigDecimal(0);
		if (sParteIntera.length()!=0 && sParteDecimale.length()!=0)
		{
			double dParteDecimale = Integer.valueOf(sParteDecimale) / 100.0;
			double dRes = Long.valueOf(sParteIntera) + dParteDecimale;
			
			bdRes = new BigDecimal(dRes);
		}
		
		bdRes = bdRes.setScale(2, BigDecimal.ROUND_HALF_UP);
		return bdRes;
	}
	
	/**
	 * Splitta con l'elemento "|"
	 * @param iNumStrings: numero di elementi attesi nello split
	 * @param sToSPlit: stringa da separare
	 * @return l'array con le stringhe splittate
	 */
	public static String[] getSplit_NString(int iNumStrings, String sToSplit)
	{
		String[] sSplit = null;
		if (sToSplit != null)
		{
			String[] sSplitTemp = sToSplit.split("\\|",-1);
			
			if (sSplitTemp.length != iNumStrings)
			{
				sSplit = new String[iNumStrings];
				for (int k=0; k<iNumStrings; k++)
					sSplit[k] = "";
			}
			else
				sSplit = sSplitTemp;	
		}
		else
		{
			sSplit = new String[iNumStrings];
			for (int k=0; k<iNumStrings; k++)
				sSplit[k] = "";
		}
			
		return sSplit;
	}
	//inizio LP PG200420
	public static boolean verificaNumeroCCIbanPostale(String numCC, String codiceIban, String secondoCodiceIban) {
		if(numCC == null) {
			return false;
		}
		numCC = numCC.trim();
		if(numCC.length() != 12) {
			return false;
		}
		if(isIbanPostale(codiceIban)) {
			if(codiceIban.substring(15).equals(numCC))
				return true;
		}
		if(isIbanPostale(secondoCodiceIban)) {
			if(secondoCodiceIban.substring(15).equals(numCC))
				return true;
		}
		return false;
	}

	public static boolean isIbanPostale(String codiceIban) {
		String abiPosteItaliane = "07601";
		try {
			//              1          2 
			//01 23 4 56789 01234 567890123456
		    //IT 60 X 05428 11101 000000123456
			return codiceIban.substring(5, 10).equals(abiPosteItaliane);
		} catch (Exception e) {
		}
		return false;
	}
	//fine LP PG200420
}
