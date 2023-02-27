package org.seda.payer.manager.federa.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class MetadataPublisherServletAR extends HttpServlet
{

    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String metadataFile;
    protected String metadataFileEncoding;

    public MetadataPublisherServletAR()
    {
        metadataFile = null;
        metadataFileEncoding = null;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(metadataFile), metadataFileEncoding);
        BufferedReader filebuf = new BufferedReader(isr);
        String metadataXml = new String("".getBytes(), metadataFileEncoding);
        for(String temp = filebuf.readLine(); temp != null; temp = filebuf.readLine())
        {
            metadataXml = (new StringBuilder(String.valueOf(metadataXml))).append(temp).append("\n").toString();
        }

        filebuf.close();
        response.setContentType((new StringBuilder("application/samlmetadata+xml; charset=")).append(metadataFileEncoding).toString());
		//inizio LP PG21XX04 Leak
        //response.getWriter().write(metadataXml);
		PrintWriter out = response.getWriter();
        out.write(metadataXml);
        out.close();
		//fine LP PG21XX04 Leak
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        metadataFile = config.getServletContext().getRealPath(config.getServletContext().getInitParameter("metadataFileAR"));
        metadataFileEncoding = config.getServletContext().getInitParameter("metadataFileEncoding");
        if(metadataFileEncoding == null)
        {
            metadataFileEncoding = "utf-8";
        }
    }

}
