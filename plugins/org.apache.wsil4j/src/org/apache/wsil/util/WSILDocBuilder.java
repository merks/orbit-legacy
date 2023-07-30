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

import org.apache.wsil.*;
import org.apache.wsil.extension.*;
import org.apache.wsil.extension.wsdl.*;
import org.apache.wsil.extension.uddi.*;
import org.apache.wsil.impl.extension.wsdl.*;
import org.apache.wsil.impl.extension.uddi.*;

import org.uddi4j.*;
import org.uddi4j.util.*;
import org.uddi4j.transport.*;
import org.uddi4j.client.*;
import org.uddi4j.response.*;
import org.uddi4j.datatype.business.*;
import org.uddi4j.datatype.service.*;
import org.uddi4j.datatype.Name;

import javax.wsdl.*;

import java.io.*;
import java.net.*;
import java.util.*;


/**
 * This class builds a WSIL document.
 *
 * @author Alfredo da Silva
 */
public class WSILDocBuilder
{
  /**
   * Max number of rows returned by the UDDI registry.
   *
   */
  protected static final int MAX_ROWS = 100;


  /**
   * WSIL inspection document reference.
   *
   */
  protected WSILDocument wsilDocument = null;


  /**
   * Creates a new <code>WSILDocBuilder</code> instance.
   *
   */
  public WSILDocBuilder()
    throws WSILException
  {
    // Create WSIL document object
    wsilDocument = WSILDocument.newInstance();
  }


  /**
   * Creates a new <code>WSILDocBuilder</code> instance.
   *
   * @param root a <code>String</code> value
   * @exception WSILException if an error occurs
   * @exception WSDLException if an error occurs
   * @exception IOException if an error occurs
   */
  public WSILDocBuilder(String root)
    throws WSILException, WSDLException, IOException
  {
    initWSDL(root);
  }


  /**
   * Creates a new <code>WSILDocBuilder</code> instance.
   *
   * @param registryURL an <code>URL</code> value
   * @exception WSILException if an error occurs
   */
  public WSILDocBuilder(URL registryURL) throws WSILException
  {
    initUDDI(registryURL, null);
  }


  /**
   * Creates a new <code>WSILDocBuilder</code> instance.
   *
   * @param registryURL an <code>URL</code> value
   * @param businessKey a <code>String</code> value
   * @exception WSILException if an error occurs
   */
  public WSILDocBuilder(URL registryURL, String businessKey) 
    throws WSILException
  {
    initUDDI(registryURL, businessKey);
  }


  /**
   * Creates a new <code>initWSDL</code> instance.
   *
   * @param root a <code>String</code> value
   * @exception WSILException if an error occurs
   * @exception WSDLException if an error occurs
   * @exception IOException if an error occurs
   */
  private void initWSDL(String root)
    throws WSILException, WSDLException, IOException
  {
    // Create WSIL document object
    wsilDocument = WSILDocument.newInstance();

    wsilDocument.setDocumentURL(Util.formatURL(WSILProperties.WSIL_HOSTNAME,
                                                WSILProperties.WSIL_PORT, ""));

    // Get ExtensionRegistry object
    ExtensionRegistry extensionRegistry = 
      wsilDocument.getExtensionRegistry();

    WSDLExtensionBuilder wsdlExtensionBuilder = 
      (WSDLExtensionBuilder)extensionRegistry.getBuilder(WSDLConstants.NS_URI_WSIL_WSDL);

    // Get list of WSDL documents
    FileListBuilder fileListBuilder = new FileListBuilder(root, 
                                                          WSILProperties.WSDL_EXTENSION);

    File[] filesList = fileListBuilder.getFiles();
    
    for (int i = 0; i < filesList.length; i++) 
    {
      try
      {
        WSDLDocument wsdlDocument = new WSDLDocument(filesList[i].getPath());

        String wsdlDocLocation = null;
        org.apache.wsil.QName qName = null;
        javax.wsdl.Service[] serviceList = null;

        if (wsdlDocument.isServiceImplementation() == true)
        {
          String originalPath = filesList[i].getPath();

          // Replace '\' with '/'
          originalPath = originalPath.replace('\\', '/');

          // Replace '\' with '/'
          root = root.replace('\\', '/');

          int index = originalPath.indexOf(root) != -1 ? originalPath.indexOf(root) + root.length() + 1 : 0;

          String filteredPath = originalPath.substring(index);

          // Get filtered WSDL document location
          wsdlDocLocation = filteredPath.replace('\\', '/');

          // Get service list
          serviceList = wsdlDocument.getServices();

          // Create Description object
          Description description = wsilDocument.createDescription();

          description.setReferencedNamespace(WSDLConstants.NS_URI_WSDL);
          description.setLocation(wsdlDocLocation);

          // Create Reference object
          Reference reference = 
            (Reference)wsdlExtensionBuilder.createElement(Reference.QNAME);

          // Get all Services
          for (int j = 0; j < serviceList.length; j++) 
          {
            // Get all ports
            Object[] portsArray = serviceList[j].getPorts().values().toArray();

            org.apache.wsil.QName currentQName = null;

            for (int k = 0; k < portsArray.length; k++)
            {
              qName = new org.apache.wsil.QName(((Port)portsArray[k]).getBinding().getQName().getNamespaceURI(), ((Port)portsArray[k]).getBinding().getQName().getLocalPart());

              // Avoid duplicates
              if (currentQName != null && currentQName.equals(qName))
                continue;
              
              currentQName = qName;

              // Create ImplementedBinding object
              ImplementedBinding implementedBinding = 
                (ImplementedBinding)wsdlExtensionBuilder.createElement(ImplementedBinding.QNAME);

              implementedBinding.setBindingName(qName);
              
              // Set endpointPresent accordingly
              // ADS            if (wsdlDocument.isServiceInterface() == true)
              //            {
                  reference.setEndpointPresent(new Boolean("true"));
                  //            }

              // Add referencedService if there are more then 1 service defined
              if (serviceList.length > 1)
              {
                  // Create ReferencedService object
                  ReferencedService referencedService = 
                (ReferencedService)wsdlExtensionBuilder.createElement(ReferencedService.QNAME);
                  org.apache.wsil.QName refServQName = new org.apache.wsil.QName(wsdlDocument.getDefinitions().getTargetNamespace(), serviceList[j].getQName().getLocalPart());

                  referencedService.setReferencedServiceName(refServQName);

                  // Set ReferencedService
                  reference.setReferencedService(referencedService);
                 }

              // Add ImplementedBinding
              reference.addImplementedBinding(implementedBinding);
            }

            // Add Reference object
            description.setExtensionElement(reference);
          
            // Create new Service object
            org.apache.wsil.Service service = wsilDocument.createService();

            // Create ServiceName object
            ServiceName serviceName = wsilDocument.createServiceName();

            // Set text
            serviceName.setText(serviceList[j].getQName().getLocalPart());

            // Add service name
            service.addServiceName(serviceName);

            service.addDescription(description);

            // Add Service object
            wsilDocument.getInspection().addService(service);
          }
        }
      }
      catch( WSDLException e)
      {
        System.err.println("WSDL Exception occurred for:\"" + filesList[i].getPath()+ "\" error is: " + e.getMessage());
      }
      catch( WSILException e)
      {
        System.err.println("WSILExceptione occurred for:\"" + filesList[i].getPath()+ "\" error is: " + e.getMessage());
      }
    }
  }


  /**
   * Creates a new <code>initUDDI</code> instance.
   *
   * @param registryURL an <code>URL</code> value
   * @param businessKey a <code>String</code> value
   * @exception WSILException if an error occurs
   */
  private void initUDDI(URL registryURL, String businessKey) 
    throws WSILException
  {
    // Create WSIL document object
    wsilDocument = WSILDocument.newInstance();

    // Get ExtensionRegistry object
    ExtensionRegistry extensionRegistry = 
      wsilDocument.getExtensionRegistry();

    UDDIExtensionBuilder uddiExtensionBuilder = 
      (UDDIExtensionBuilder)extensionRegistry.getBuilder(UDDIConstants.NS_URI_WSIL_UDDI);

    try 
    {
      Properties p = new Properties();
              
      // Set SOAP transport
      p.setProperty(TransportFactory.PROPERTY_NAME, 
                    WSILProperties.TRANSPORT_CLASS);
    
      // Set inquiry URL
      p.setProperty("org.uddi4j.inquiryURL", registryURL.toString());
    
      // Create UDDIProxy object
      UDDIProxy up = new UDDIProxy(p);

      ServiceList serviceList = null;

      if (businessKey == null) 
      {
        // Find all services
        serviceList = up.find_service("", "%", null, MAX_ROWS);
      }
      else 
      {
        // Find BusinessDetail object
        BusinessDetail businessDetail = up.get_businessDetail(businessKey);

        Vector businessEntityVector = businessDetail.getBusinessEntityVector();

        BusinessEntity businessEntity = null;
      
        // Get BusinessEntity object
        if (businessEntityVector.size() > 0)
          businessEntity = (BusinessEntity)businessEntityVector.firstElement();
      
        // Find all services given a BusinessEntity
        serviceList = up.find_service("", 
                                      businessEntity.getDefaultNameString(), 
                                      null, MAX_ROWS);
      }

      BusinessService[] businessServiceArray = findServices(serviceList, up);

      for (int i = 0; i < businessServiceArray.length; i++) 
      {
        // Get service name
        String serviceNameString = businessServiceArray[i].getDefaultNameString();

        // Get service key
        String serviceKeyUUID = businessServiceArray[i].getServiceKey();

        // Create Description object
        Description description = wsilDocument.createDescription();

        description.setReferencedNamespace(UDDIConstants.NS_URI_UDDI);

        ////////////////////////////////////////////////////////////////////
        //ServiceDescription ///////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////

        // Create ServiceDescription object
        ServiceDescription serviceDescription = 
          (ServiceDescription)uddiExtensionBuilder.createElement(ServiceDescription.QNAME);
        
        // Set location
        serviceDescription.setLocation(registryURL.toString());

        //ServiceKey /////////////////////////////////////////////////////

        // Create ServiceKey object
        ServiceKey serviceKey = serviceDescription.createServiceKey();

        // Set service key
        serviceKey.setText(serviceKeyUUID);

        // Set service key
        serviceDescription.setServiceKey(serviceKey);

        //DiscoveryURL /////////////////////////////////////////////////////

        // Find BusinessDetail object
        BusinessDetail businessDetail = 
          up.get_businessDetail(businessServiceArray[i].getBusinessKey());

        Vector businessEntityVector = businessDetail.getBusinessEntityVector();

        BusinessEntity businessEntity = null;
      
        // Get BusinessEntity object
        if (businessEntityVector.size() > 0)
        {
          businessEntity = (BusinessEntity)businessEntityVector.firstElement();
          
          // Get DiscoverURLs
          DiscoveryURLs discoveryURLs = businessEntity.getDiscoveryURLs();

          Vector discoveryURLsVector = discoveryURLs.getDiscoveryURLVector();

          for (int j = 0; j < discoveryURLsVector.size(); j++) 
          {
            // Set discoveryURL
            serviceDescription.setDiscoveryURL((DiscoveryURL)discoveryURLsVector.elementAt(j));
          }      
        }  

        // Add serviceDescription object
        description.setExtensionElement(serviceDescription);
        
        // Create new Service object
        org.apache.wsil.Service service = wsilDocument.createService();

        // Create ServiceName object
        ServiceName serviceName = wsilDocument.createServiceName();
      
        // Set text
        serviceName.setText(serviceNameString);

        // Add service name
        service.addServiceName(serviceName);
      
        service.addDescription(description);
      
        // Add Service object
        wsilDocument.getInspection().addService(service);
      }
    }
    catch(Exception e) 
    {
      throw new WSILException("Unable to create WSILDocBuilder", e);
    }    
  }

  /**
   * Returns a WSIL document.
   *
   * @return a <code>WSILDocument</code> value
   */
  public WSILDocument getWSILDocument()
  {
    return wsilDocument;
  }


  /**
   * Adds a link object to the WSIL document
   *
   * @param referencedNamespace a <code>String</code> value
   * @param location a <code>String</code> value
   */
  public void addLink(String referencedNamespace, String location)
  {
    // Create link object
    Link link = wsilDocument.createLink();

    link.setReferencedNamespace(referencedNamespace);
    link.setLocation(location);

    // Add link object
    wsilDocument.getInspection().addLink(link);
  }


  /**
   * Adds a link object to the WSIL document
   *
   * @param registryURL an <code>URL</code> value
   * @exception WSILException if an error occurs
   */
  public void addLink(URL registryURL) throws WSILException
  {
    // Get ExtensionRegistry object
    ExtensionRegistry extensionRegistry = 
      wsilDocument.getExtensionRegistry();

    UDDIExtensionBuilder uddiExtensionBuilder = 
      (UDDIExtensionBuilder)extensionRegistry.getBuilder(UDDIConstants.NS_URI_WSIL_UDDI);

    try 
    {
      Properties p = new Properties();
              
      // Set SOAP transport
      p.setProperty(TransportFactory.PROPERTY_NAME, 
                    WSILProperties.TRANSPORT_CLASS);
    
      // Set inquiry URL
      p.setProperty("org.uddi4j.inquiryURL", registryURL.toString());
    
      // Create UDDIProxy object
      UDDIProxy up = new UDDIProxy(p);

      // Find all Service providers
      BusinessList businessList = up.find_business("%", null, MAX_ROWS);

      BusinessEntity[] businessEntityArray = null;

      businessEntityArray = 
        findBusinessEntities(businessList.getBusinessInfos(), up);

      for (int i = 0; i < businessEntityArray.length; i++) 
      {
        // Create link object
        Link link = wsilDocument.createLink();

        link.setReferencedNamespace(UDDIConstants.NS_URI_UDDI);

        // Get Business key
        String businessKeyUUID = businessEntityArray[i].getBusinessKey();

        ////////////////////////////////////////////////////////////////////
        //businessDescription ///////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////

        // Create BusinessDescription object
        BusinessDescription businessDescription = 
          (BusinessDescription)uddiExtensionBuilder.createElement(BusinessDescription.QNAME);
        
        // Set location
        businessDescription.setLocation(registryURL.toString());

        //BusinessKey /////////////////////////////////////////////////////

        // Create BusinessKey object
        BusinessKey businessKey = businessDescription.createBusinessKey();

        // Set business key String
        businessKey.setText(businessKeyUUID);

        // Set business key
        businessDescription.setBusinessKey(businessKey);

        //DiscoveryURL /////////////////////////////////////////////////////

        // Get DiscoverURLs
        DiscoveryURLs discoveryURLs = 
          businessEntityArray[i].getDiscoveryURLs();

        Vector discoveryURLsVector = discoveryURLs.getDiscoveryURLVector();

        for (int j = 0; j < discoveryURLsVector.size(); j++) 
        {
          // Set discoveryURL
          businessDescription.setDiscoveryURL((DiscoveryURL)discoveryURLsVector.elementAt(j));
        }

        // Add businessDescription object
        link.setExtensionElement(businessDescription);
        
        // Add link object
        wsilDocument.getInspection().addLink(link);
      }
    }
    catch(Exception e) 
    {
      throw new WSILException("Unable to create WSILDocBuilder", e);
    }    
  }

  
  /**
   * Adds a list of WSIL Service objects to the WSIL Document.
   *
   * @param root a <code>String</code> value
   * @exception WSILException if an error occurs
   * @exception WSDLException if an error occurs
   * @exception IOException if an error occurs
   */
  public void addService(String root)
    throws WSILException, WSDLException, IOException
  {
    initWSDL(root);
  }


  /**
   * Adds a list of UDDI Service objects to the WSIL Document.
   *
   * @param registryURL an <code>URL</code> value
   * @exception WSILException if an error occurs
   */
  public void addService(URL registryURL) throws WSILException
  {
    initUDDI(registryURL, null);
  }


  /**
   * Find all BusinessService objects given a serviceList object
   *
   * @param serviceList 
   * @return Return a BusinessService array
   */
  protected BusinessService[] findServices(ServiceList serviceList, 
                                           UDDIProxy up)
    throws WSILException
  {
    BusinessService[] businessServiceList = null;
    ServiceDetail serviceDetail;
    Vector siList, skList = new Vector();


    try
    {
      // Get the list of services
      siList = serviceList.getServiceInfos().getServiceInfoVector();


      // Build service key list
      for (int i = 0; i < siList.size(); i++)
      {
        // Get the service detail for the next service 
        skList.addElement(((ServiceInfo) siList.elementAt(i)).getServiceKey());
      }

      // Get the service detail for all of the services
      serviceDetail = up.get_serviceDetail(skList);

      // Get the business service list 
      Vector bsList = serviceDetail.getBusinessServiceVector();

      // Build BusinessService list
      if (bsList.size() > 0)
      {
        businessServiceList = new BusinessService[bsList.size()];
      
        bsList.copyInto(businessServiceList);
      }
    }
    catch (Exception e)
    {
      // Throw exception
      throw new WSILException("Find services exception: " + e.toString(), e);
    }

    return businessServiceList;
  }


  /**
   * Get all BusinessEntity objects from a BusinessInfos object.
   *
   * @param businessInfos
   *
   * @return Return a BusinessEntity array
   */
  protected BusinessEntity[] findBusinessEntities(BusinessInfos businessInfos,
                                                  UDDIProxy up)
    throws WSILException
  {
    BusinessEntity[] businessEntityList = null;
    BusinessDetail businessDetail;
    Vector keyList;


    try
    {
      // Get business info list
      keyList = getBusinessKeyVector(businessInfos);

      if (keyList.size() > 0)
      {
        // Get business detail
        businessDetail = up.get_businessDetail(keyList);
      
        // Get business entity list
        Vector beList = businessDetail.getBusinessEntityVector();
        
        // Create businessEntityList
        if (beList.size() > 0)
        {
          businessEntityList = new BusinessEntity[beList.size()];
          
          beList.copyInto(businessEntityList);
        }
      }
    }
    catch(Exception e)
    {
      // Throw exception
      throw new WSILException("Could not list all business entities.", e);
    }

    return businessEntityList;
  }


  /**
   * Get business keys from a list of businessInfos.
   *
   * @return Return a list of business keys.
   */
  public static Vector getBusinessKeyVector(BusinessInfos businessInfos)
  {
    Vector keyList = new Vector();

        
    // Get list of business infos
    Vector biList = businessInfos.getBusinessInfoVector();

    // Process each business info
    for (int i = 0; i < biList.size(); i++)
    {
      // Get serviceInfos
      keyList.add(((BusinessInfo) biList.elementAt(i)).getBusinessKey());
    }

    // Return key list
    return keyList;
  }
}
