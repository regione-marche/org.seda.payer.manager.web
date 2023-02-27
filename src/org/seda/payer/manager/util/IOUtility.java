package org.seda.payer.manager.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.sql.rowset.CachedRowSet;

public class IOUtility {

	public static void writeFile(byte[] data, String filePath, boolean appendData) throws Exception{
		FileOutputStream fos = new FileOutputStream(new File(filePath), appendData);
		fos.write(data);
		fos.flush();
		fos.close();
	}
	
	public static byte[] readFile(String file) throws Exception{
		//inizio LP PG21XX04 Leak
		//return toByteArray(new FileInputStream(file));
		FileInputStream fsi = null;
		try {
			fsi = new FileInputStream(file); 
			return toByteArray(fsi);
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
	public static byte[] readFile(File file) throws Exception{
		//inizio LP PG21XX04 Leak
		//return toByteArray(new FileInputStream(file));
		FileInputStream fsi = null;
		try {
			fsi = new FileInputStream(file); 
			return toByteArray(fsi);
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
	
	public static void copyInputStreamToOutputStream(InputStream input, OutputStream output) throws Exception{
		int n = 0;
		int DEFAULT_BUFFER_SIZE = 1024 * 4;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		while (-1 != (n = input.read(buffer)))
			output.write(buffer, 0, n);
	}
	
	public static byte[] toByteArray(InputStream is) throws Exception{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copyInputStreamToOutputStream(is, out);
	    return out.toByteArray();
	}
}
