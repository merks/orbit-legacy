/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "xml-axis-wsil" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.wsil;

import org.apache.wsil.util.*;
import org.apache.wsil.xml.*;
import org.apache.wsil.extension.*;

import java.net.*;
import java.io.*;


/**
 * Instance of a WS-Inspection document.
 *
 * @version 1.0
 * @author Peter Brittenham
 */
public abstract class WSILDocument
{
  /**
   * Set default document reader.
   */
  protected DocumentReader docReader = new XMLReader();
  
  /**
   * Set default document writer.
   */
  protected DocumentWriter docWriter = new XMLWriter();

  /**
   * WS-Inspection extension registry.
   */
  protected ExtensionRegistry extRegistry = null;

  /**
   * Inspection element.
   */
  protected Inspection inspection = null;

   
  /**
   * WSIL document URL.  
   * NOTE: This field will not be set when the document is read from a Reader.
   */
  protected String documentURL = null;


  /**
   * Create abstract element.
   *
   * @return Returns an abstract object.
   */
  public abstract Abstract createAbstract();
  
  
  /**
   * Create description element.
   *
   * @return Returns a description object.   
   */
  public abstract Description createDescription();
  
  
  /**
   * Create link element.
   *
   * @return Returns a link object.
   */
  public abstract Link createLink();
  
  
  /**
   * Create service element.
   *
   * @return Returns a service object.
   */
  public abstract Service createService();
  
  
  /**
   * Create service name element.
   *
   * @return Returns a serviceName object.   
   */
  public abstract ServiceName createServiceName();
  
  
  /**
   * Get the WS-Inspection extension registry implementation.
   * 
   * @param Returns a reference to the extension registry.
   */
  public ExtensionRegistry getExtensionRegistry()
  {
    // Return WS-Inspection extension registry
    return this.extRegistry;
  }
  
  
  /**
   * Get the inspection element for this document.
   *
   * @return Returns a reference to the inspection element.
   */
  public Inspection getInspection()
  {
    // Return inspection element
    return inspection;
  }
  
  
  /**
   * Get the WS-Inspection reader implementation.
   * 
   * @return Returns the document reader.
   */
  public DocumentReader getReader()
  {
    // Return WS-Inspection reader
    return this.docReader;
  }
  
  
  /**
   * Get the WS-Inspection writer implementation.
   * 
   * @return Returns the document writer.
   */
  public DocumentWriter getWriter()
  {
    // Return WS-Inspection writer
    return this.docWriter;
  }
  
  
  /**
   * Read a WS-Inspection document and display its contents.
   * 
   * @param args command line arguments
   */
  public static void main(String[] args)
  {
    String filename = null;

    if (args.length == 0) 
    {
      System.out.println("Usage: WSILDocument <filename>");
    }

    else
    {
      try
      {
        filename = args[0];

        // Display message
        System.out.println("Reading WS-Inspection document [" + filename + "]...");

        // Get an new instance of a WS-Inspection document
        WSILDocument wsilDocument = newInstance();
        
        // Read WS-Inspection document
        wsilDocument.read(filename);
        
        // Display contents of document
        System.out.println(wsilDocument.toString());
      }
    
      catch (Exception e)
      {
        // Display exception
        System.out.println("EXCEPTION: Could not process WS-Inspection document [" + filename + "].");
        e.printStackTrace();
      }
    }

    // Exit
    System.exit(0);
  }
  
  
  /* 
   * Create a new instance of a WS-Inspection document.
   *
   * @return Returns a new instance of a WS-Inspection document.
   */
  public static WSILDocument newInstance()
    throws WSILException
  {
    WSILDocument wsilDocument = null;


    // Get the WS-Inspection document factory
    WSILDocumentFactory factory = WSILDocumentFactory.newInstance();

    // Create new WS-Inspection document instance
    wsilDocument = factory.newDocument();
    
    // Return document
    return wsilDocument;
  }
  
  
  /**
   * Read the WS-Inspection document from a input reader.
   * 
   * @param reader the input reader
   */
  public void read(Reader reader)
    throws WSILException
  {
    // Read WS-Inspection document
    docReader.parseDocument(this, reader);
  }
  
  
  /**
   * Read the WS-Inspection document from a file URL string.
   *
   * @param urlString the file URL string for the WS-Inspection document
   */
  public void read(String urlString)
    throws WSILException
  {
    try
    {
      // Read the WSIL document
      read(new URL(urlString));
    }

    catch (Exception e)
    {
      // Throw exception
      throw new WSILException("Could not read WS-Inspection file URL string: " + urlString + ".", e);
    }
  }
  
  
  /**
   * Read the WS-Inspection document from a location specified using a URL.
   *
   * @param url location of the document
   */
  public void read(URL url)
    throws WSILException
  {
    try
    {
      InputStream is;

      // Save the URL passed
      documentURL = url.toString();

      // Get proxy userName and password
      String proxyUserName = System.getProperty("http.proxyUserName");
      String proxyPassword = System.getProperty("http.proxyPassword");

      if (proxyUserName != null && proxyPassword != null)
      {
        // Handle authenticating proxies
        String userPassword = proxyUserName + ":" + proxyPassword;

        // Encode userid and password
        String encoding = Base64Converter.encode(userPassword);

        // Open connection and set encoding property
        URLConnection uc = url.openConnection();
        uc.setRequestProperty("Proxy-authorization", "Basic " + encoding);

        // Get input stream
        is = uc.getInputStream();
      }
      else 
        // Open input stream
        is = (InputStream) url.openStream();
      
      // Read the WS-Inspection document
      read(new InputStreamReader(is));
    }
    catch (Exception e)
    {
       // Throw exception
       throw new WSILException("Could not read WS-Inspection document from: " + url.toString(), e);
    }
  }

  
  /**
   * Set the WS-Inspection extension registry implementation.
   *
   * @param extRegistry the extension registry implementation
   */
  public void setExtensionRegistry(ExtensionRegistry extRegistry)
    throws WSILException
  {
    // Save reference to WS-Inspection extension registry
    if (this.extRegistry == null)
      this.extRegistry = extRegistry;
    
    else
    {
      // Throw exception
      throw new WSILException("ExtensionRegistry is already set in WSILDocument.");
    }
  }
  
  
  /**
   * Set the WS-Inspection reader implementation.
   *
   * @param docReader the WS-Inpsection document reader
   */
  public void setReader(DocumentReader docReader)
  {
    // Save reference to WS-Inspection reader
    this.docReader = docReader;
  }
  
  
  /**
   * Set the WS-Inspection writer implementation.
   *
   * @param docWriter the WS-Inpsection document writer
   */
  public void setWriter(DocumentWriter docWriter)
  {
    // Save reference to WS-Inspection writer
    this.docWriter = docWriter;
  }
  
  
  /**
   * Return string representation of this object.
   *
   * @return Returns the string representation of this object.
   */
  public String toString()
  { 
    // Return string
    return "WS-Inspection Document: \n" + inspection.toString();
  }
  
  
  /**
   * Write the WS-Inpsection document to an output writer.
   *
   * @param writer the output writer
   */
  public void write(Writer writer)
    throws WSILException
  {
    // Write WS-Inspection document
    docWriter.writeDocument(this, writer);
  }
  
  
  /**
   * Write the WS-Inspection document to an output file.
   * 
   * @param filename the filename for the WS-Inspection document
   */
  public void write(String filename)
    throws WSILException
  {
    try
    {
      // Create file writer and then write output
      write(new FileWriter(filename));
    }

    catch (IOException ioe)
    {
      // Throw exception
      throw new WSILException("Could not write to " + filename + ".", ioe);
    }
  }


  /**
   * This method is used to resolve a relative URL into a full URL.
   *
   * @param relativeURL the relative URL value
   * @return Returns the full URL.
   */
  public String resolveURL(String relativeURL)
  {
    String resolvedURL = relativeURL;


    // If the relative URL is not a full URL (example: http://...)
    if ((documentURL != null) && (relativeURL != null) && (relativeURL.indexOf("://") == -1))
    {
      // Find location of last "/" in document URL
      int index = documentURL.lastIndexOf('/');

      // Create resolved URL
      resolvedURL = documentURL.substring(0, index+1) + relativeURL;
    }
    
    return resolvedURL;
  }


  /**
   * Set the location for the WS-Inspection document.
   *
   * @param documentURL the document URL
   */
  public void setDocumentURL(String documentURL)
  {
    this.documentURL = documentURL;
  }
}
