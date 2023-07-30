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

package org.apache.wsil.impl.extension.wsdl;

import org.apache.wsil.*;
import org.apache.wsil.extension.*;
import org.apache.wsil.extension.wsdl.*;
import org.apache.wsil.util.*;

import org.w3c.dom.*;


/**
 * The WSDL extension reader class is used to parse the WSDL extension elements.
 *
 * @version 1.0
 * @author: Peter Brittenham
 */
public class WSDLExtensionReader 
  implements ExtensionReader 
{
  /**
   * WSDLExtensionReader constructor comment.
   */
  public WSDLExtensionReader() 
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

    // If this is the reference element
    if (qname.equals(Reference.QNAME))
    {
      // Parse reference element
      extElement = parseReference(element, wsilElement);
    }

    // If this is the referencedService element
    else if (qname.equals(ReferencedService.QNAME))
    {
      // Parse referencedService element
      extElement = parseReferencedService(element, wsilElement);
    }

    // If this is the implemented binding element
    else if (qname.equals(ImplementedBinding.QNAME))
    {
      // Parse implementedBinding element
      extElement = parseImplementedBinding(element, wsilElement);
    }

    else
    {
      // Throw exception
      throw new WSILException("WSIL document contains undefined WSDL extension element: " + 
                              element.getTagName() + ".");
    }

    // Return extension element
    return extElement;
  }
 
  
  /**
   * Parse reference element.
   */
  protected ExtensionElement parseReference(Element referenceElement, 
                                            WSILElement wsilElement) 
  {
    Reference reference = new ReferenceImpl();

    
    // Get a list of attributes
    NamedNodeMap attrList = referenceElement.getAttributes();

    // Get the first attribute
    Attr attr = (Attr)attrList.item(0);

    if (attr != null) 
    {
      // Get the attribute value
      String endpointPresent = attr.getValue();

      if (endpointPresent != null)
        // Set the endpointPresent
        reference.setEndpointPresent(Boolean.valueOf(endpointPresent));
    }

    // Get the first child element
    Element element = XMLUtil.getFirstChild(referenceElement);

    // EndPointPresent found
    if (element == null) 
      return reference;

    // Get QName for the element
    QName qname = new QName(element);

    // If this is the referencedService element
    if (qname.equals(ReferencedService.QNAME))
    {
      // Parse referencedService element
      reference.setReferencedService((ReferencedService)parseReferencedService(element, wsilElement));
    }
    
    // If this is the implementedBinding element
    if (qname.equals(ImplementedBinding.QNAME))
    {
        // Process each child element
        while (element != null)
        {
            reference.addImplementedBinding((ImplementedBinding)parseImplementedBinding(element, reference));

            // Get next child element
            element = XMLUtil.getNextSibling(element);
        }
    }

    // Return reference element
    return reference;
  }
 
  
  /**
   * Parse implementedBinding element.
   */
  protected ExtensionElement parseImplementedBinding(Element element, 
                                                     WSILElement wsilElement) 
  {
    ImplementedBinding implementedBinding = new ImplementedBindingImpl();

    int index = XMLUtil.getText(element).lastIndexOf(":");
    String localName = XMLUtil.getText(element).substring(index + 1);

    // Get a list of attributes
    NamedNodeMap attrList = element.getAttributes();

    // Get the first attribute
    Attr attr = (Attr)attrList.item(0);

    // Get the attribute value
    String namespaceURI = attr.getValue();

    // Set the binding name
    implementedBinding.setBindingName(new QName(namespaceURI, localName));
    
    // Return implementedBinding element.
    return implementedBinding;
  }

  /**
   * Parse referencedService element.
   */
  protected ExtensionElement parseReferencedService(Element element, 
                                                    WSILElement wsilElement) 
  {
    ReferencedService referencedService = new ReferencedServiceImpl();

    int index = XMLUtil.getText(element).lastIndexOf(":");
    String localName = XMLUtil.getText(element).substring(index + 1);

    // Get a list of attributes
    NamedNodeMap attrList = element.getAttributes();

    // Get the first attribute
    Attr attr = (Attr)attrList.item(0);

    // Get the attribute value
    String namespaceURI = attr.getValue();

    // Set the referencedService name
    referencedService.setReferencedServiceName(new QName(namespaceURI, localName));
    
    // Return referencedService element.
    return referencedService;
  }
}
