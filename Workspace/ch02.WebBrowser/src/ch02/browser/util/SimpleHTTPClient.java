/*******************************************************************************
 * Copyright (c) 2006 Vladimir Silva and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Vladimir Silva - initial API and implementation
 *******************************************************************************/
package ch02.browser.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


public class SimpleHTTPClient 
{
	private static final Logger logger = Logger.getLogger(SimpleHTTPClient.class);
	
	public static final String CT_KMZ = "application/vnd.google-earth.kmz";
	public static final String CT_KML = "application/vnd.google-earth.kml+xml";
	public static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14";
	
	private int READ_TIMEOUT = 8000;
	
	private Map<String, List<String>> headers;
	private URL url;
    private int status;
    private HttpURLConnection uc;
    private String responseMessage;
    
	public SimpleHTTPClient(String url) throws MalformedURLException {
		this.url = new URL(url);
	}
	public SimpleHTTPClient(URL url) throws MalformedURLException {
		this.url = url;
	}
	
    /**
     * Simple HTTP Get for URLs that return text data
     * @param os stream where the output will be written
     * @throws MalformedURLException
     * @throws IOException
     */
    public void doGet (OutputStream os)
		throws  IOException //, MalformedURLException
	{
    	try {
    	    uc = (HttpURLConnection)url.openConnection();
    	    uc.setRequestProperty("User-Agent", USER_AGENT);
    	    uc.setReadTimeout(READ_TIMEOUT);
    	    
    	    logger.debug("Connect timeout=" + uc.getConnectTimeout() 
    	    		+ " read timeout=" + uc.getReadTimeout() + " u=" + url);
    	    
    	    InputStream buffer  = new BufferedInputStream(uc.getInputStream());   
    	    
    	    int c;
    	    
    	    while ((c = buffer.read()) != -1) 
    	    {
    	      os.write(c);
    	    } 
    	    
    	    headers = uc.getHeaderFields();
    	    status = uc.getResponseCode();
    	    responseMessage = uc.getResponseMessage();
    	    
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
		finally {
			if ( status != 200)
				logger.error("Download failed status: " + status + " " + responseMessage + " for " + url);
			else
				logger.debug("HTTP status=" + status + " " + uc.getResponseMessage());
			
			os.close();
			uc.disconnect();
		}
	}

    /**
     * Simple HTTP get request
     * @return HTTP response text
     * @throws MalformedURLException
     * @throws IOException
     */
    public String doGet ()
		throws MalformedURLException, IOException
	{
	    uc = (HttpURLConnection)url.openConnection();
	    
	    BufferedInputStream buffer  = new BufferedInputStream(uc.getInputStream());   
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    
	    int c;
	    while ((c = buffer.read()) != -1) 
	    {
	      bos.write(c);
	    } 
	    bos.close();
	    
	    headers = uc.getHeaderFields();
	    status = uc.getResponseCode();
	    
	    return bos.toString();
	}
    
    public InputStream getInputStream() throws IOException {
	    HttpURLConnection uc = (HttpURLConnection)url.openConnection();
	    return uc.getInputStream();
    }
    
	public Map<String, List<String>> getHeaders(){
		return headers;
	}
	
	public int getStatus (){
		return status;
	}
	
	public String getResponseMessage () throws IOException {
		return responseMessage;
	}
	
	public String getContentType() {
		return headers.get("Content-Type").get(0);
	}
	
	public boolean isContentTypeKML () {
		return getContentType().indexOf(CT_KML) != -1;
	}

	public boolean isContentTypeKMZ () {
		return getContentType().indexOf(CT_KMZ) != -1;
	}

	public void close () {
		uc.disconnect();
		uc = null;
		headers = null;
		url = null;
	}
	
}
