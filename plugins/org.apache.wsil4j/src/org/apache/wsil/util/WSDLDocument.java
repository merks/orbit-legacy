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

package org.apache.wsil.util;

import javax.wsdl.*;
import javax.wsdl.factory.*;
import com.ibm.wsdl.Constants;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.*;
import java.net.*;
import java.util.*;


/**
 * Provides an easy way to read and write information to and from
 * WSDL documents.
 */
public class WSDLDocument
{
  /**
   * Class name.
   */
  protected static final String className = "WSDLDocument";

  // The primary Definition element for our WSDL document.
  private Definition wsdlDocumentDef = null;

  /**
   * WSDL related namespaces.
   */
  public static final String NS_URI_XSD = "http://www.w3.org/2001/XMLSchema";
  public static final String NS_URI_WSDL_SOAP = "http://schemas.xmlsoap.org/wsdl/soap/";
  public static final String NS_URI_WSDL_HTTP = "http://schemas.xmlsoap.org/wsdl/http/";
  public static final String NS_URI_WSDL_MIME = "http://schemas.xmlsoap.org/wsdl/mime/";

  /**
   * WSDL factory class
   */
  public static final String WSDL_FACTORY = "com.ibm.wsdl.factory.WSDLFactoryImpl";


  protected static WSDLFactory wsdlFactory= null;
  protected static WSDLException wsdlException= null;
  static
  {
    try
    {
      wsdlFactory= WSDLFactory.newInstance(WSDL_FACTORY);
    }catch(WSDLException e)
    {
      wsdlException= e;
    }
  }


  /**
   * Constructs a new WSDL document from the specified file.
   *
   * @param  fileName The name of the WSDL file to be read.  Specify either
   *                  the full path to the file, or a URL address.
   *
   * @exception WSDLException if the WSDL document cannot be created.
   */
  public WSDLDocument(String fileName)
    throws WSDLException
  {
    read(fileName);
  }


  /**
   * Constructs a new WSDL document from the specified URL.
   *
   * @param documentURL the URL where the WSDL document is located
   *
   * @exception WSDLException if the WSDL document cannot be created.
   */
  public WSDLDocument(URL documentURL)
    throws WSDLException
  {
    this (documentURL.toExternalForm());
  }


  /**
   * Constructs a new WSDL document from the specified Reader.
   *
   * @param reader the reader that will be used to get the contents
   *               of the WSDL document
   *
   * @exception WSDLException if the WSDL document cannot be created.
   */
  public WSDLDocument(Reader reader)
    throws WSDLException
  {
    this.wsdlDocumentDef = getWSDLReader().readWSDL(null,new InputSource(reader));
  }

  /**
   * Constructs a WSDLReader 
   *
   * Set verbose option off.
   * Sets reslove imports off.
   * 
   * @exception WSDLException if the WSDL document cannot be created.
   */
  public static WSDLReader getWSDLReader()
    throws WSDLException
  {
    return getWSDLReader(false);
  }

  /**
   * Constructs a WSDLReader 
   *
   * Set verbose option off.
   *
   * @param  imports set to true if imports are to be reslolved, false if not. 
   * 
   * @exception WSDLException if the WSDL document cannot be created.
   */
  public static WSDLReader getWSDLReader(final boolean imports)
    throws WSDLException
  {
     if(null!= wsdlException) throw wsdlException;

     WSDLReader ret= null;
     ret= wsdlFactory.newWSDLReader();
     ret.setFeature(com.ibm.wsdl.Constants.FEATURE_VERBOSE, false);
     ret.setFeature(com.ibm.wsdl.Constants.FEATURE_IMPORT_DOCUMENTS, imports);
     return ret;
  }

  /**
   * Constructs a WSDLWriter
   *
   * 
   * @exception WSDLException if the WSDL document cannot be created.
   */
  public static WSDLWriter getWSDLWriter()
    throws WSDLException
  {
     if(null!= wsdlException) throw wsdlException;

     WSDLWriter ret= null;
     ret= wsdlFactory.newWSDLWriter();
     return ret;
  }


  /**
   * Constructs a new WSDL document from a Document DOM.
   *
   * @param document A parsed XML document that conforms to the WSDL schema.
   *
   * @exception WSDLException if the WSDL document cannot be created.
   */
  public WSDLDocument( Document document )
    throws WSDLException
  {
    this.wsdlDocumentDef = getWSDLReader().readWSDL(null,document);
  }


  /**
   * Constructs a new WSDL document from a UDDI businessService.
   *
   * @param definition A WSDL definition element.
   *
   * @exception WSDLException if the WSDL document cannot be created.
   */
  public WSDLDocument(Definition definition)
    throws WSDLException
  {
    this.wsdlDocumentDef = definition;
  }


  /**
   * Read the information contained in a WSDL file.
   *
   * @param fileName The name of the WSDL file to be read.  Specify either
   *                 the full path to the file, or a URL address.
   *
   * @exception WSDLException if a error occurs processing the file.
   */
  public void read(String fileName)
    throws WSDLException
  {
    read(fileName,true);
  }

  /**
   * Read the information contained in a WSDL file
   * 
   * @param fileName the name of the WSDL file to be read. Specify either 
   *                  the full path to the file, or a URL address.
   * @param imports  true if imports in the WSDL file should be resolved
   * @exception WSDLException if an error occurs processing the file.
   */
  public void read(String fileName, boolean imports) 
    throws WSDLException
  {
    this.wsdlDocumentDef = getWSDLReader(imports).readWSDL(null,fileName);    
  }

  /**
   * Write the WSDL document to the specified file.
   *
   * @param fileName The name of the file to write to.
   */
  public void write(String fileName)
  {
    try
    {
      FileWriter fw = new FileWriter(fileName);
      write(fw);
      fw.close();
    }
    catch (IOException e)
    {
      // An IOException is possible when you create the FileWriter
      // or when you invoke the close() method.
      System.out.println("IOException occurred attempting to write document to file: " + fileName);
    }
  }


  /**
   * Write the WSDL document content to the specified writer.
   *
   * @param writer The Writer to use.
   */
  public void write(Writer writer)
  {
    try
    {
      getWSDLWriter().writeWSDL(wsdlDocumentDef,writer);
    }
    catch (WSDLException e)
    {
      System.out.println("Exception occurred writing the document.");
      e.printStackTrace();
    }
  }


  /**
   * Generate an XML string that represents the WSDL document content.
   *
   * @return String The string containing this document serialized as XML.
   */
  public String serializeToXML()
  {
     StringWriter sw = new StringWriter();
     write(sw);
     try
     {
       sw.close();
     }
     catch (IOException e)
     {
       // An IOException is possible when you invoke the close() method.
       System.out.println("IOException occurred closing the writer");
     }

    return sw.toString();
  }


  /**
   * Get the definition element of the WSDL document.
   *
   * @return The definition element
   */
  public Definition getDefinitions()
  {
    // Return the definition element
    return wsdlDocumentDef;
  }


  /**
   * Get all the message elements from the WSDL document.
   *
   * @return The list of message elements.
   */
  public Message[] getMessages()
  {
    // Generate an array of Messages from the Message map for this Definition
    return (Message[])wsdlDocumentDef.getMessages().values().toArray(new Message[0]);
  }


  /**
   * Get all the portType elements from the WSDL document.
   *
   * @return The list of portType elements.
   */
  public PortType[] getPortTypes()
  {
    // Generate an array of PortTypes from the PortType map for this Definition
    return (PortType[])wsdlDocumentDef.getPortTypes().values().toArray(new PortType[0]);
  }


  /**
   * Get all the binding elements from the WSDL document.
   *
   * @return The list of binding elements.
   */
  public Binding[] getBindings()
  {
    // Generate an array of Bindings from the Binding map for this Definition
    return (Binding[])wsdlDocumentDef.getBindings().values().toArray(new Binding[0]);
  }


  /**
   * Get all the import elements from the WSDL document.
   *
   * @return The list of import elements.
   */
  public Import[] getImports()
  {
    /*
     * Each entry in the Import map is actually a list of Imports.
     * Gather all the Imports for this Definition.
     */
    Iterator importListIterator = wsdlDocumentDef.getImports().values().iterator();
    Vector importVector = new Vector();
    while (importListIterator.hasNext())
    {
      importVector.addAll((List)importListIterator.next());
    }

    // Generate an array of Imports
    return (Import[])importVector.toArray(new Import[0]);
  }


  /**
   * Get all the service elements from the WSDL document.
   *
   * @return The list of service elements.
   */
  public Service[] getServices()
  {
    // Generate an array of Services from the Service map for this Definition
    return (Service[])wsdlDocumentDef.getServices().values().toArray(new Service[0]);
  }


  /**
   * Check if this document is a service interface.
   * That means it must contain Message, PortType and Binding elements.
   *
   * @return true  if Message, PortType and Binding elements are all found;
   *         false otherwise
   */
  public boolean isServiceInterface() {
    return WSDLDocumentIdentifier.isInterfaceDocument( this );
  }


  /**
   * Check if this document is a service interface with bindings only.
   * That means it must contain Binding elements only.
   *
   * @return true  if Binding element is found;
   *         false otherwise
   */
  public boolean isServiceInterfaceWithBindingsOnly() {
    return WSDLDocumentIdentifier.isBindingInterfaceDocument( this );
  }


  /**
   * Check if this document is a service implementation.
   * That means it must contain at least one Import and one Service element.
   * No attempt is made to verify the content of any imported document.
   *
   * @return true  if at least one Import and one Service element is found;
   *         false otherwise
   */
  public boolean isServiceImplementation() {
    return WSDLDocumentIdentifier.isImplementationDocument( this );
  }


  /**
   * Check if this document is both a service interface and a service implementation.
   * That means it must contain Message, PortType, Binding and Service elements.
   *
   * @return true  if Message, PortType, Binding and Service elements are all found;
   *         false otherwise
   */
  public boolean isCompleteServiceDefinition() {
    return WSDLDocumentIdentifier.isCompleteDocument( this );
  }


  /**
   * Find the Service Interface document which this Service Implementation
   * document references.
   *
   * A Service Implementation document is expected to contain an Import
   * statement that references the Service Interface document.
   *
   * @return WSDLDocument the Service Interface document.  A null is returned
   *                      if the source document is not a service implementation
   *                      document or we didn't find an import for a service
   *                      implementation document.
   *
   * @exception WSDLException if an error occurs while reading
   *            an imported document.
   */
  public WSDLDocument findServiceInterface()
    throws WSDLException
  {
    String documentURI;
    WSDLDocument wsdlDoc = null;
    /*
     * If this is a Service Implementation document, scan the list of
     * imports, looking for the first one that could be a Service Interface
     * document.
     */
    if (isServiceImplementation())
    {
      // Get the list of import statements for this source document
      Import [] wsdlImportList = getImports();
      for (int i = 0; i < wsdlImportList.length; i++)
      {
        // If the imported WSDL document is a Service Interface, we are done.
        documentURI = wsdlImportList[i].getLocationURI();
        wsdlDoc = new WSDLDocument(documentURI);
        if (wsdlDoc.isServiceInterface())
        {
          return wsdlDoc;
        }
        else
        {
          wsdlDoc = null;
        }
      }
    }

    // Return null
    return wsdlDoc;
  }


  /**
   * Return string representation of this object.
   */
  public String toString() 
  {
    // Return string
    return (wsdlDocumentDef == null) ? "WSDL definition element is null." : wsdlDocumentDef.toString();
  }


  /**
   * Command line interface for validating WSDL documents.
   */
  public static void main(String[] args) 
  {
    String filename = null;

    if (args.length == 0) 
    {
      System.out.println("Usage: WSDLDocument <filename>");
    }

    else
    {
      try
      {
        filename = args[0];

        // Display message
        System.out.println("Reading WSDL document [" + filename + "]...");
        
        // Read WSDL document
        WSDLDocument wsdlDocument = new WSDLDocument(filename);
        
        // Display message
        System.out.println("Document contents: ");

        // Display contents of document
        System.out.println(wsdlDocument.toString());
      }
    
      catch (Exception e)
      {
        // Display exception
        System.out.println("EXCEPTION: Could not process WSDL document [" + filename + "].");
        e.printStackTrace();
      }
    }

    // Exit
    System.exit(0);
  }

  private static String WSDL4J_DEFINITION_FACTORY_PROP= "javax.wsdl.factory.DefinitionFactory"; //see WSDL4J documentation.
  static
  {
    //This defines to WSDL4J additional extensions used in WSDL generated by Microsoft's SOAP toolkit. 
    String existing= System.getProperty(WSDL4J_DEFINITION_FACTORY_PROP);
    if(null == existing) System.setProperty(WSDL4J_DEFINITION_FACTORY_PROP,"com.ibm.wsdl.factory.IBMWSTKDefinitionFactoryImpl");
  }
}
