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

package org.apache.wsil.client;

import org.apache.wsil.*;
import org.apache.wsil.util.*;
import org.apache.wsil.Service;
import org.apache.wsil.extension.*;
import org.apache.wsil.extension.wsdl.*;
import org.apache.wsil.extension.uddi.*;
import org.apache.wsil.WSILDocument;
import org.apache.wsil.util.WSILDocBuilder;
import org.apache.wsil.util.WSDLDocument;
import com.ibm.wsdl.util.xml.DOM2Writer;

import org.uddi4j.*;
import org.uddi4j.datatype.service.*;
import org.uddi4j.util.*;
import org.uddi4j.transport.*;
import org.uddi4j.client.*;
import org.uddi4j.response.*;

import org.w3c.dom.Element;

import javax.wsdl.*;
import java.io.*;
import java.util.*;
import java.net.*;


/**
 * This class gives access to an inspection.wsil document and its
 * contents. This information can be use to find, deploy and bind services.
 *
 * @version 1.0
 * @author Alfredo da Silva
 */
public class WSILProxy
{
  
  /**
   *  WSIL inspection document reference.
   */
  private WSILDocument wsilDocument = null;


  /**
   *  WSIL inspection document vector
   */
  private Vector wsilDocVector = new Vector();


  /**
   * Creates a new <code>WSILProxy</code> instance.
   *
   * @exception WSILException if an error occurs
   */
  public WSILProxy() throws WSILException
  {
    // WSIL Document URL
    String wsilDocumentURL = Util.formatURL(WSILProperties.WSIL_HOSTNAME,
                                            WSILProperties.WSIL_PORT,
                                            WSILProperties.WSIL_DOCUMENT_NAME);

    init(wsilDocumentURL);
  }


  /**
   * Creates a new <code>WSILProxy</code> instance.
   *
   * @param wsilDocumentURL an <code>URL</code> value
   * @exception WSILException if an error occurs
   */
  public WSILProxy(String wsilDocumentURL) throws WSILException
  {
    init(wsilDocumentURL);
  }


  /**
   * Creates a new <code>WSILProxy</code> instance.
   *
   * @param inspectionElement an <code>Element</code> value
   * @exception WSILException if an error occurs
   */
  public WSILProxy(Element inspectionElement) throws WSILException
  {
    try
    {
      // Create a StringWriter
      StringWriter swriter = new StringWriter();

      // Serialize the XML to the string writer
      DOM2Writer.serializeAsXML(inspectionElement, swriter);

      // Create a StringReader
      StringReader sreader = new StringReader(swriter.toString());

      // Create new WSIL document instance
      wsilDocument = WSILDocument.newInstance();

      wsilDocument.read(sreader);
      
      // Add wsilDocument to wsilDocVector
      wsilDocVector.add(wsilDocument);
    }
    catch (Exception e)
    {
      throw new WSILException("Invalid inspection element", e);
    }
  }

  /**
   * Creates a new WSIL document.
   *
   * @param wsilDocumentURL an <code>URL</code> value
   * @exception WSILException if an error occurs
   */
  private void init(String wsilDocumentURL) throws WSILException
  {
    // Create new WSIL document instance
    wsilDocument = WSILDocument.newInstance();

    // Read the contents of the WSIL document
    try 
    {
      URL inURL = new URL(wsilDocumentURL);
      wsilDocument.read(inURL);

      // Add wsilDocument to wsilDocVector
      wsilDocVector.add(wsilDocument);
    } 
    catch (MalformedURLException e) 
    {
      throw new WSILException("Invalid inspection.wsil document URL", e);
    }      
  }

  /**
   * Returns a WSIL document.
   *
   * @return a <code>WSILDocument</code> value.
   */
  public WSILDocument getWSILDocument()
  {
    return wsilDocument;
  }


  /**
   * Returns an array of WSDLDocuments given a service name.
   *
   * @param serviceName a <code>String</code> value
   * @return a <code>WSDLDocument[]</code> value or null if no documents
   *         were found.
   * @exception WSILException if an error occurs
   */
  public WSDLDocument[] getWSDLDocumentByServiceName(String serviceName)
    throws WSILException
  {
    WSDLDocument[] wsdlDocArray = null;


    // Get WSDL location array
    String[] wsdlDocLocationArray = getWSDLDocumentLocations(serviceName);

    if (wsdlDocLocationArray != null) 
    {
      wsdlDocArray = new WSDLDocument[wsdlDocLocationArray.length];

      for (int i = 0; i < wsdlDocLocationArray.length; i++) 
      {
        // Read WSDL document
        wsdlDocArray[i] = readWSDLDocument(wsdlDocLocationArray[i]);
      }
    }

    return wsdlDocArray;
  }

  /**
   * Returns an array of WSDLDocuments given a name.
   *
   * @param wsdlDocName a <code>String</code> value
   * @return a <code>WSDLDocument</code> value or null if no documents were
   *         found.
   * @exception WSILException if an error occurs
   */
  public WSDLDocument[] getWSDLDocumentByName(String wsdlDocName) 
    throws WSILException
  {
    WSDLDocument[] wsdlDocArray = null;
    Vector wsdlDocVector = new Vector();


    // Process links
    handleLinks(this.wsilDocument);

    for (int k = 0; k < wsilDocVector.size(); k++) 
    {
      WSILDocument currentWSILDoc = (WSILDocument)wsilDocVector.elementAt(k);

      Service[] serviceArray = currentWSILDoc.getInspection().getServices();

      String wsdlDocLocation = null;

      for (int i = 0; i < serviceArray.length; i++) 
      {
        Description[] descriptionArray = serviceArray[i].getDescriptions();

        for (int j = 0; j < descriptionArray.length; j++) 
        {
          wsdlDocLocation = descriptionArray[j].getLocation();

          if (wsdlDocLocation != null && 
              wsdlDocLocation.indexOf(wsdlDocName) != -1)
          {
            // Read WSDL document
            wsdlDocVector.add(readWSDLDocument(currentWSILDoc.resolveURL(wsdlDocLocation)));
          }
        }
      }
    }

    // Create wsdlDocArray
    if (wsdlDocVector.size() != 0)
    {
      wsdlDocArray = new WSDLDocument[wsdlDocVector.size()];
      wsdlDocVector.copyInto(wsdlDocArray);
    }

    return wsdlDocArray;
  }
  

  /**
   * Returns an array of WSDLDocuments given a binding name.
   *
   * @param bindingName a <code>QName</code> value
   * @return a <code>WSDLDocument</code> value or null if no documents were
   *         found.
   * @exception WSILException if an error occurs
   */
  public WSDLDocument[] getWSDLDocumentByBinding(org.apache.wsil.QName bindingName)
    throws WSILException
  {
    WSDLDocument[] wsdlDocArray = null;
    Vector wsdlDocVector = new Vector();


    // Process links
    handleLinks(this.wsilDocument);

    for (int l = 0; l < wsilDocVector.size(); l++) 
    {
      WSILDocument currentWSILDoc = (WSILDocument)wsilDocVector.elementAt(l);

      Service[] serviceArray = currentWSILDoc.getInspection().getServices();

      String wsdlDocLocation = null;

      for (int i = 0; i < serviceArray.length; i++) 
      {
        Description[] descriptionArray = serviceArray[i].getDescriptions();

        for (int j = 0; j < descriptionArray.length; j++) 
        {
          Reference reference = 
            (Reference)descriptionArray[j].getExtensionElement();

          if (reference != null) 
          {
            ImplementedBinding[] implementedBindingArray = 
              reference.getImplementedBindings();

            for (int k = 0; k < implementedBindingArray.length; k++) 
            {
              org.apache.wsil.QName tmpQName = 
                implementedBindingArray[k].getBindingName();

              if (tmpQName.equals(bindingName))
              {
                // Read WSDL document
                wsdlDocVector.add(readWSDLDocument(currentWSILDoc.resolveURL(descriptionArray[j].getLocation())));
              }
            }
          }
        }
      }
    }

    // Create wsdlDocArray
    if (wsdlDocVector.size() != 0)
    {
      wsdlDocArray = new WSDLDocument[wsdlDocVector.size()];
      wsdlDocVector.copyInto(wsdlDocArray);
    }

    return wsdlDocArray;
  }


  /**
   * Returns an array of WSDLDocuments given a portType name.
   *
   * @param portTypeName a <code>QName</code> value
   * @return a <code>WSDLDocument</code> value or null if no documents were
   *         found.
   * @exception WSILException if an error occurs
   */
  public WSDLDocument[] getWSDLDocumentByPortType(org.apache.wsil.QName portTypeName)
    throws WSILException
  {
    
    WSDLDocument[] wsdlDocArray = null;
    Vector wsdlDocVector = new Vector();

    // Process links
    handleLinks(this.wsilDocument);

    for (int l = 0; l < wsilDocVector.size(); l++) 
    {
      WSILDocument currentWSILDoc = (WSILDocument)wsilDocVector.elementAt(l);

      Service[] serviceArray = currentWSILDoc.getInspection().getServices();

      String wsdlDocLocation = null;

      for (int i = 0; i < serviceArray.length; i++) 
      {
        Description[] descriptionArray = serviceArray[i].getDescriptions();

        for (int j = 0; j < descriptionArray.length; j++) 
        {
          Description description = descriptionArray[j];
                
          Reference reference = 
            (Reference)description.getExtensionElement();

          if (reference != null) 
          {
            ImplementedBinding[] implementedBindingArray = 
              reference.getImplementedBindings();

            WSDLDocument definition = null;
            try {
              definition = 
                readWSDLDocument(
                  currentWSILDoc.resolveURL(
                    description.getLocation()));
            } catch (Exception e) {}

            if (definition != null) {
              for (int k = 0; k < implementedBindingArray.length; k++) 
              {
                if (!wsdlDocVector.contains(definition)) {
                  org.apache.wsil.QName tmpQName = 
                    implementedBindingArray[k].getBindingName();
                  javax.xml.namespace.QName nqname = 
                    new javax.xml.namespace.QName(
                      tmpQName.getNamespaceURI(),
                      tmpQName.getLocalName());
                  Binding binding = definition.getDefinitions().getBinding(nqname);
  
                  if (binding != null) {
                    PortType portType = binding.getPortType();
                    if (portType != null) {
                      if (portType.getQName().getLocalPart().equals(portTypeName.getLocalName()) &&
                          portType.getQName().getNamespaceURI().equals(portTypeName.getNamespaceURI())) {
                        wsdlDocVector.add(definition);
                      }
                    }
                  }  
                }
              }
            }
          }
        }
      }
    }

    // Create wsdlDocArray
    if (wsdlDocVector.size() != 0)
    {
      wsdlDocArray = new WSDLDocument[wsdlDocVector.size()];
      wsdlDocVector.copyInto(wsdlDocArray);
    }

    return wsdlDocArray;
  }

  /**
   * Returns an array of BusinessServices given a service name.
   *
   * @param serviceName a <code>String</code> value
   * @return a <code>BusinessService</code> value or null if no elements were
   *         found.
   * @exception WSILException if an error occurs
   */
  public BusinessService[] getBusinessServiceByServiceName(String serviceName)
    throws WSILException
  {
    Vector businessServiceVector = new Vector();
    BusinessService[] businessServiceArray = null;


    // Process links
    handleLinks(this.wsilDocument);

    for (int l = 0; l < wsilDocVector.size(); l++) 
    {
      WSILDocument currentWSILDoc = (WSILDocument)wsilDocVector.elementAt(l);

      Service[] serviceArray = currentWSILDoc.getInspection().getServices();

      // Get services
      for (int i = 0; i < serviceArray.length; i++) 
      {
        if (serviceArray[i].getServiceNames()[0].getText().equals(serviceName)) 
        {
          Description[] descriptionArray = serviceArray[i].getDescriptions();

          for (int j = 0; j < descriptionArray.length; j++) 
          {
            ServiceDescription serviceDescription = 
              (ServiceDescription)descriptionArray[j].getExtensionElement();

            if (serviceDescription != null) 
            {
              String location = serviceDescription.getLocation();
              ServiceKey serviceKey = serviceDescription.getServiceKey();

              try 
              {
                Properties p = new Properties();
              
                // Set SOAP transport
                p.setProperty(TransportFactory.PROPERTY_NAME, 
                              WSILProperties.TRANSPORT_CLASS);

                // Set inquiry URL
                p.setProperty("org.uddi4j.inquiryURL", 
                              new URL(location).toString());

                // Create UDDIProxy object
                UDDIProxy up = new UDDIProxy(p);

                // Get ServiceDetail object
                ServiceDetail serviceDetail = 
                  up.get_serviceDetail(serviceKey.getText());

                Vector bServiceVector = 
                  serviceDetail.getBusinessServiceVector();

                if (bServiceVector.size() > 0) 
                {
                  BusinessService businessService = 
                    (BusinessService)bServiceVector.firstElement();
                  
                  businessServiceVector.add((businessService));
                }
              }
              catch (MalformedURLException e) 
              {
                throw new WSILException("Problems connecting to registry", e);
              }
              catch (UDDIException e) 
              {
                throw new WSILException("Unable to find service " + serviceName, e);
              }
              catch (TransportException e) 
              {
                throw new WSILException("SOAP transport problems", e);
              }
            }
          }
        }
      }
    }

    // Create businessServiceArray
    if (businessServiceVector.size() != 0)
    {
      businessServiceArray= new BusinessService[businessServiceVector.size()];
      businessServiceVector.copyInto(businessServiceArray);
    }

    return businessServiceArray;
  }

  /**
   * Print the internal WSIL document.
   *
   * @param out an <code>OutputStream</code> value
   * @exception WSILException if an error occurs
   */
  public void print(OutputStream out) throws WSILException
  {
    wsilDocument.write(new PrintWriter(out));
  }


  /**
   * Returns the an array of WSDL document locations given a service name.
   *
   * @param serviceName a <code>String</code> value
   * @return a <code>String[]</code> value or null if no locations were found.
   */
  public String[] getWSDLDocumentLocations(String serviceName) throws WSILException
  {
    String[] wsdlDocLocationArray = null;
    Vector wsdlDocLocationVector = new Vector();

   
    // Process links
    handleLinks(this.wsilDocument);

    for (int l = 0; l < wsilDocVector.size(); l++) 
    {
      WSILDocument currentWSILDoc = (WSILDocument)wsilDocVector.elementAt(l);

      // Get service array
      Service[] serviceArray = currentWSILDoc.getInspection().getServices();

      for (int i = 0; i < serviceArray.length; i++) 
      {
        ServiceName[] serviceNameArray = serviceArray[i].getServiceNames();

        // Search for service name passed
        for (int j = 0; j < serviceNameArray.length; j++) 
        {  
          if (serviceNameArray[j].getText().equals(serviceName))
          {
            Description[] descriptionArray = serviceArray[i].getDescriptions();

            String currentLocation = null;

            for (int k = 0; k < descriptionArray.length; k++) 
            {
              String location = descriptionArray[k].getLocation();

              // Avoid duplicates
              if (currentLocation != null && currentLocation.equals(location))
                continue;
            
              // Add to vector only if this is a WSDL service description
              if (descriptionArray[k].getReferencedNamespace().equals(WSDLConstants.NS_URI_WSDL)) 
              {
                currentLocation = location;
                wsdlDocLocationVector.add(currentWSILDoc.resolveURL(location));
              }
            }
          }
        }
      }
    }

    // Create wsdlDocLocationArray
    if (wsdlDocLocationVector.size() != 0)
    {
      wsdlDocLocationArray = new String[wsdlDocLocationVector.size()];
      wsdlDocLocationVector.copyInto(wsdlDocLocationArray);
    }
    
    return wsdlDocLocationArray;
  }


  /**
   * Reads a WSDLDocument given a location.
   *
   * @param wsdlDocLocation a <code>String</code> value
   * @return a <code>WSDLDocument</code> value or null if the document could
   *         not be read.
   * @exception WSILException if an error occurs
   */
  private WSDLDocument readWSDLDocument(String wsdlDocLocation)
    throws WSILException
  {
    WSDLDocument wsdlDoc = null;


    // Read the contents of the WSDL document
    try 
    {
      wsdlDoc = new WSDLDocument(new URL(wsdlDocLocation));
    } 
    catch (MalformedURLException e) 
    {
      throw new WSILException("Invalid WSDL document URL", e);
    }      
    catch (WSDLException e) 
    {
      throw new WSILException("Problems creating WSDL document", e);
    }      

  return wsdlDoc;
  }


  /**
   * Traverse all link objects and fill wsilDocArray.
   *
   * @param wsilDocument a <code>WSILDocument</code> value
   * @exception WSILException if an error occurs
   */
  private void handleLinks(WSILDocument wsilDocument) 
    throws WSILException
  {
    // Get Link
    Link[] linkArray = wsilDocument.getInspection().getLinks();

    for (int i = 0; i < linkArray.length; i++) 
    {
      String linkNamespace = linkArray[i].getReferencedNamespace();
      String linkLocation = wsilDocument.resolveURL(linkArray[i].getLocation());
      ExtensionElement linkExtensionElem = linkArray[i].getExtensionElement();

      if (linkNamespace.equals(WSILConstants.NS_URI_WSIL)) 
      {
        // Create new WSIL document instance
        WSILDocument wsilDoc = WSILDocument.newInstance();

        // Read wsil document
        try 
        {
          URL inURL = new URL(linkLocation);
          wsilDoc.read(inURL);
        } 
        catch (MalformedURLException e) 
        {
          throw new WSILException("Invalid wsil document URL", e);
        }      

        // Add it to the WSIL document vector
        wsilDocVector.add(wsilDoc);

        // Get all the links
        handleLinks(wsilDoc);
      }
      else 
      {
        if ((linkNamespace.equals(UDDIConstants.NS_URI_UDDI)) ||
            (linkNamespace.equals(UDDIConstants.NS_URI_UDDI_V2)))
        {
          // Get BusinessDescription
          BusinessDescription businessDescription = 
            (BusinessDescription)linkArray[i].getExtensionElement();

          if (businessDescription != null)
          {
            String location = businessDescription.getLocation();

            String businessKey = businessDescription.getBusinessKey().getText();

            try 
            {
              WSILDocBuilder wsilDocBuilder = 
                new WSILDocBuilder(new URL(location), businessKey);
              
              // Add it to the WSIL document vector
              wsilDocVector.add(wsilDocBuilder.getWSILDocument());
            }
            catch (MalformedURLException e) 
            {
              throw new WSILException("Problems creating WSIL document", e);
            }
          }
        }
        else 
        {
          throw new WSILException("Invalid wsil link namespace: " +linkNamespace);
        }
      }      
    }
  }
  
  
  /**
   * Displays the contents of the default WS-Inspection document
   * or the specified document.
   * 
   * @param args an array of command-line arguments
   */
  public static void main(String[] args) 
  {
    WSILProxy proxy;
    String serviceName;
    Vector wsdlList = new Vector();
    
    try
    {
      // If there is no input argument, then throw exception
      if ((args.length > 0) && (args[0].equals("-?")))
      {
        throw new IllegalArgumentException("Usage: WSILProxy <wsinspectionURL>");
      }

      // Create a new instance of a WS-Inspection proxy
      proxy = (args.length == 0) ? new WSILProxy() : new WSILProxy(args[0]);

      // Get the WSIL document and display the contents
      System.out.println(proxy.getWSILDocument().toString());
    }

    catch (Exception e)
    {
      // Display exception message
      System.out.println("EXCEPTION: " + e.getMessage());
      e.printStackTrace();
    }

    // Exit
    System.exit(0);
  }
}
