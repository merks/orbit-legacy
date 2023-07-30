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

package org.apache.wsil.impl.extension.uddi;

import java.util.*;
import org.apache.wsil.*;
import org.apache.wsil.extension.*;
import org.apache.wsil.extension.uddi.*;
import org.apache.wsil.util.*;

import org.w3c.dom.*;

import org.uddi4j.util.*;

/**
 * The UDDI extension reader class is used to parse the UDDI extension elements.
 *
 * @version 1.0
 * @author: Peter Brittenham
 */
public class UDDIExtensionReader 
  implements ExtensionReader 
{
  /**
   * WSDLExtensionReader constructor comment.
   */
  public UDDIExtensionReader() 
  {
    super();
  }

  
  /**
   * Parse extension element.
   */
  public ExtensionElement parseElement(Element element, WSILElement wsilElement)
    throws WSILException
  {
    ExtensionElement extElement = null;


    // Get QName for the element
    QName qname = new QName(element);

    // If this is the ServiceDescription element
    if ((qname.equals(ServiceDescription.QNAME)) ||
        (qname.equals(ServiceDescription.QNAME_V2)))
    {
      // Parse ServiceDescription element
      extElement = parseServiceDescription(element, wsilElement);
    }

    // If this is the BusinessDescription element
    else if ((qname.equals(BusinessDescription.QNAME)) ||
             (qname.equals(BusinessDescription.QNAME_V2)))
    {
      // Parse businessDescription element
      extElement = parseBusinessDescription(element, wsilElement);
    }
      
    else
    {
      // Throw exception
      throw new WSILException("WSIL document contains undefined UDDI extension element: " + 
                              element.getTagName() + ".");
    }

    // Return extension element
    return extElement;
  }
 
  
  /**
   * Parse service description element.
   */
  protected ExtensionElement parseServiceDescription(Element serviceDescriptionElement, 
                                                     WSILElement wsilElement) 
    throws WSILException
  {
    ServiceDescription serviceDescription = new ServiceDescriptionImpl();
    
    // Get a list of attributes
    NamedNodeMap attrList = serviceDescriptionElement.getAttributes();

    // Get the first attribute
    Attr attr = (Attr)attrList.item(0);

    // Get the attribute value
    String location = attr.getValue();

    // Set the location
    serviceDescription.setLocation(location);

    // Get the first child element
    Element element = XMLUtil.getFirstChild(serviceDescriptionElement);

    // Process each child element
    while (element != null)
    {
      // ServiceKey
      if ((ServiceDescription.SERVICE_KEY_QNAME.equals(element)) ||
          (ServiceDescription.SERVICE_KEY_QNAME_V2.equals(element)))
      {
        serviceDescription.setServiceKey(parseServiceKey(element, serviceDescription));
      }

      // DiscoveryURL
      else if ((serviceDescription.DISCOVERY_URL_QNAME.equals(element)) ||
               (serviceDescription.DISCOVERY_URL_QNAME_V2.equals(element)))
      {
          serviceDescription.setDiscoveryURL(parseDiscoveryURL(element, serviceDescription));
      }

      else
      {
        // Throw exception
        throw new WSILException("WSIL document contains undefined UDDI extension element: " + 
                                element.getTagName() + ".");
      }
      
      // Get next child element
      element = XMLUtil.getNextSibling(element);
    }

    // Return service description element
    return serviceDescription;
  }


  /**
   * Parse business description element.
   */
  protected ExtensionElement parseBusinessDescription(Element businessDescriptionElement, WSILElement wsilElement) 
  {
    BusinessDescription businessDescription = new BusinessDescriptionImpl();

    // Get a list of attributes
    NamedNodeMap attrList = businessDescriptionElement.getAttributes();

    // Get the first attribute
    Attr attr = (Attr)attrList.item(0);

    // Get the attribute value
    String location = attr.getValue();

    // Set the location
    businessDescription.setLocation(location);

    // Get the first child element
    Element element = XMLUtil.getFirstChild(businessDescriptionElement);

    // Process each child element
    while (element != null)
    {
      // BusinessKey
      if (BusinessDescription.BUSINESS_KEY_QNAME.equals(element))
      {
        businessDescription.setBusinessKey(parseBusinessKey(element, businessDescription));
      }
      else 
        // DiscoveryURL
        if (BusinessDescription.DISCOVERY_URL_QNAME.equals(element))
        {
          businessDescription.setDiscoveryURL(parseDiscoveryURL(element, businessDescription));
        }
      
      // Get next child element
      element = XMLUtil.getNextSibling(element);
    }

    // Return business description element
    return businessDescription;
  }

  /**
   * Parse businessKey element.
   */
  protected BusinessKey parseBusinessKey(Element element, WSILElement wsilElement) 
  {
    BusinessKey businessKey = new BusinessKey();

    // Set business key
    businessKey.setText(XMLUtil.getText(element));    

    // Return businessKey element
    return businessKey;
  }

  /**
   * Parse discoveryURL element.
   */
  protected DiscoveryURL parseDiscoveryURL(Element element, WSILElement wsilElement) 
  {
    DiscoveryURL discoveryURL = new DiscoveryURL();

    // Get a list of attributes
    NamedNodeMap attrList = element.getAttributes();

    // Get the first attribute
    Attr attr = (Attr)attrList.item(0);

    // Get the attribute value
    String useType = attr.getValue();

    // Set the use type
    discoveryURL.setUseType(useType);

    // Set value
    discoveryURL.setText(XMLUtil.getText(element));    

    // Return discoveryURL element
    return discoveryURL;
  }

  /**
   * Parse serviceKey element.
   */
  protected ServiceKey parseServiceKey(Element element, WSILElement wsilElement) 
  {
    ServiceKey serviceKey = new ServiceKey();

    // Set service key
    serviceKey.setText(XMLUtil.getText(element));    

    // Return serviceKey element
    return serviceKey;
  }


  /**
   * Parse CategoryBag element.
   */
  protected CategoryBag parseCategoryBag(Element categoryBagElement, WSILElement wsilElement) 
  {
    CategoryBag categoryBag = new CategoryBag();

    // Get the first child element
    Element element = XMLUtil.getFirstChild(categoryBagElement);

    // Get a list of attributes
    NamedNodeMap attrList = element.getAttributes();

    // Get keyName
    Attr attr = (Attr)attrList.item(0);

    // Get keyName value
    String keyName = attr.getValue();

    // Get keyValue
    attr = (Attr)attrList.item(1);

    // Get keyValue value
    String keyValue = attr.getValue();

    // Create KeyedRerence
    KeyedReference keyedReference = new KeyedReference(keyName, keyValue);

    Vector keyedReferenceVector = new Vector();

    keyedReferenceVector.add(keyedReference);

    // Add keyedReference
    categoryBag.setKeyedReferenceVector(keyedReferenceVector);

    // Return categoryBag element
    return categoryBag;
  }
}
