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

package org.apache.wsil.xml;

import org.apache.wsil.*;
import org.apache.wsil.util.*;
import org.apache.wsil.extension.*;

import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.parsers.*;

import java.net.*;
import java.io.*;


/**
 * This class will use a DOM parser to read and process the contents 
 * of a WS-Inspection document.
 *
 * @version 1.0
 * @author: Peter Brittenham
 */
public class XMLReader
  implements DocumentReader
{
  /**
   * Reader which is used to obtain contents of a WSIL document.
   */
  protected Reader reader = null;

  /**
   * WSIL document.
   */
  protected WSILDocument wsilDocument = null;

  /**
   * WSIL extension registry.
   */
  protected ExtensionRegistry extRegistry = null;

  
  /**
   * Create a WSIL document reader.
   */
  public XMLReader()
  {
  }

  
  /**
   * Parse abstract element.
   *
   * @param abstractElement the DOM element that contains the abstract element
   * @param wsilElement the WSIL element with that contains the abstract element
   */
  protected Abstract parseAbstract(Element abstractElement, WSILElementWithAbstract wsilElement)
    throws WSILException
  {
    String lang;


    // Create abstract
    Abstract abs = wsilDocument.createAbstract();

    // Set lang
    if ((lang = 
         XMLUtil.getAttributeValue(abstractElement, 
                                   WSILConstants.ATTR_XML_NS + ":" + 
                                   WSILConstants.ATTR_XML_LANG)) != null) 
    {
      abs.setLang(lang);
    }

    // Set text
    abs.setText(XMLUtil.getText(abstractElement));    

    // Return abstract
    return abs;
  }

  
  /**
   * Parse description element.
   *
   * @param descriptionElement the DOM element that contains the description element
   * @param service the service element
   */
  protected Description parseDescription(Element descriptionElement, Service service)
    throws WSILException
  {
    // Create description 
    Description description = wsilDocument.createDescription();

    // Set location and referencedNamespace
    description.setLocation(XMLUtil.getAttributeValue(descriptionElement, WSILConstants.ATTR_LOCATION));
    description.setReferencedNamespace(XMLUtil.getAttributeValue(descriptionElement, WSILConstants.ATTR_REF_NAMESPACE));

    // Get the first child element
    Element element = XMLUtil.getFirstChild(descriptionElement);

    // Process each child element
    while (element != null)
    {
      // Abstract
      if (Abstract.QNAME.equals(element))
      {
        description.addAbstract(parseAbstract(element, description));
      }

      // Extension
      else 
      {
        description.setExtensionElement(parseExtensionElement(element, description));
      }

      // Get next child element
      element = XMLUtil.getNextSibling(element);
    }

    // Return description
    return description;
  }

  
  /**
   * Create a WSIL document from the contents of a reader.
   *
   * @param wsilDocument WS-Inspection document object
   * @param reader the reader from which to obtain the contents of the document
   */
  public void parseDocument(WSILDocument wsilDocument, Reader reader)
    throws WSILException
  {
    // Save input references
    this.reader = reader;
    this.wsilDocument = wsilDocument;

    // Get the extension registry
    if ((this.extRegistry = wsilDocument.getExtensionRegistry()) == null)
    {
      // Throw exception
      throw new WSILException("WSIL document does not have a defined extension registry.");
    }
	  
    // Start parsing the WSIL document
    parseInspection(parseXML(reader).getDocumentElement()); 
  }

  
  /**
   * Parse extension element.
   *
   * @param element the DOM element that contains the extension element
   * @param wsilElement the WSIL element that contains the extension element
   */
  protected ExtensionElement parseExtensionElement(Element element, WSILElement wsilElement)
    throws WSILException
  {
    ExtensionElement extElement = null;
    ExtensionReader extReader = null;


    // Get QName for the extension element
    QName qname = new QName(element);

    // Get the reader for this extension namespace
    if ((extReader = extRegistry.getReader(qname.getNamespaceURI())) == null)
    {
      // Use UnknownExtensionElement
      extElement = new UnknownExtensionElement(qname, element);

      // Throw exception
      //throw new WSILException("Could not locate handler for extension element: " + element.getTagName() + ".");
    }
    
    else
    {
      extElement = extReader.parseElement(element, wsilElement);
    }

    // Return extension
    return extElement;
  }

  
  /**
   * Parse inspection element.
   *
   * @param inspectionElement the DOM element that contains the inspection element
   */
  protected void parseInspection(Element inspectionElement)
    throws WSILException
  {
    // Get inspection object from WSIL document
    Inspection inspection = wsilDocument.getInspection();
	
    // Get the target namespace 
    inspection.setTargetNamespace(XMLUtil.getAttributeValue(inspectionElement,
                                                            WSILConstants.ATTR_TARGET_NAMESPACE));

    // Save namespace specifications
    saveNS(inspectionElement, inspection);

    // Get the first child element
    Element element = XMLUtil.getFirstChild(inspectionElement);

    // Process each child element
    while (element != null)
    {
      // Abstract
      if (Abstract.QNAME.equals(element))
      {
        inspection.addAbstract(parseAbstract(element, inspection));
      }

      // Link
      else if (Link.QNAME.equals(element))
      {
        inspection.addLink(parseLink(element, inspection));
      }

	  // Service
      else if (Service.QNAME.equals(element))
      {
        inspection.addService(parseService(element, inspection));
      }

      // Else throw exception
      else
      {
        throw new WSILException("WSIL document contains undefined element: " + element.getTagName() + ".");
      }

      // Get next child element
      element = XMLUtil.getNextSibling(element);
    }
  }

  
  /**
   * Parse link element.
   *
   * @param linkElement the DOM element that contains the link element
   * @param inspection the inspection element
   */
  protected Link parseLink(Element linkElement, Inspection inspection)
    throws WSILException
  {
    // Create link
    Link link = wsilDocument.createLink();

    // Set location and referencedNamespace
    link.setLocation(XMLUtil.getAttributeValue(linkElement, WSILConstants.ATTR_LOCATION));
    link.setReferencedNamespace(XMLUtil.getAttributeValue(linkElement, WSILConstants.ATTR_REF_NAMESPACE));

    // Get the first child element
    Element element = XMLUtil.getFirstChild(linkElement);

    // Process each child element
    while (element != null)
    {
      // Abstract
      if (Abstract.QNAME.equals(element))
      {
        link.addAbstract(parseAbstract(element, link));
      }

      // Extension
      else 
      {
        link.setExtensionElement(parseExtensionElement(element, link));
      }

      // Get next child element
      element = XMLUtil.getNextSibling(element);
    }

    // Return link 
    return link;
  }

  
  /**
   * Parse service element.
   *
   * @param linkElement the DOM element that contains the service element
   * @param inspection the inspection element
   */
  protected Service parseService(Element serviceElement, Inspection inspection)
    throws WSILException
  {
    // Create service
    Service service = wsilDocument.createService();

    // Get the first child element
    Element element = XMLUtil.getFirstChild(serviceElement);

    // Process each child element
    while (element != null)
    {
      // Abstract
      if (Abstract.QNAME.equals(element))
      {
        service.addAbstract(parseAbstract(element, service));
      }

      // Name
      else if (ServiceName.QNAME.equals(element))
      {
        service.addServiceName(parseServiceName(element, service));
      }

      // Description
      else if (Description.QNAME.equals(element))
      {
        service.addDescription(parseDescription(element, service));
      }

      // Else throw exception
      else
      {
        throw new WSILException("WSIL document contains undefined element: " + element.getTagName() + ".");
      }

      // Get next child element
      element = XMLUtil.getNextSibling(element);
    }

    // Return service
    return service;
  }

  
  /**
   * Parse service name element.
   *
   * @param linkElement the DOM element that contains the name element
   * @param service the service element
   */
  protected ServiceName parseServiceName(Element nameElement, Service service)
    throws WSILException
  {
    String lang;


    // Create name
    ServiceName serviceName = wsilDocument.createServiceName();

    // Set lang
    if ((lang = 
         XMLUtil.getAttributeValue(nameElement, 
                                   WSILConstants.ATTR_XML_NS + ":" + 
                                   WSILConstants.ATTR_XML_LANG)) != null) 
    {
      serviceName.setLang(lang);
    }

    // Set text
    serviceName.setText(XMLUtil.getText(nameElement));    

    // Return service name
    return serviceName;
  }

  
  /**
   * Parse the XML document.
   */
  protected Document parseXML(Reader reader)
    throws WSILException
  {
    Document doc = null;

    
    // Create input source
    InputSource inputSource = new InputSource(reader);
    
    // Get the document factory
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    // Set namespace aware, but for now do not validate
    factory.setNamespaceAware(true);
    factory.setValidating(false);

    try
    {
      // Parse the document
      DocumentBuilder builder = factory.newDocumentBuilder();
      doc = builder.parse(inputSource);
    }
    
    catch (Exception e)
    {
      throw new WSILException("Could not parse WSIL document.", e);
    }

    // Return document
    return doc;
  }

  
  /**
   * Save namespace settings.
   */
  protected void saveNS(Element element, Inspection inspection)
  {
    Attr attr;
    String namespaceURI, localName, attrValue;

    
    // Get a list of attributes
    NamedNodeMap attrList = element.getAttributes();

    // For each attribute, 
    for (int i = 0; i < attrList.getLength(); i++)
    {
      // Get the next attribute
      attr = (Attr) attrList.item(i);

      // Get the attribute value
      attrValue = attr.getValue();

      // If the namespace URI is the XML namespace, then save local name setting
      if ((namespaceURI = attr.getNamespaceURI()) != null && 
	      namespaceURI.equals(WSILConstants.NS_URI_XMLNS))
      {
        // If the local name is xmlns, then save just the attribute value
        if ((localName = attr.getLocalName()) != null && 
	        localName.equals(WSILConstants.ATTR_XMLNS))
        {
          inspection.addNamespace(null, attrValue);
        }

        // Else save the local name and the attribute value 
        else
        {
          inspection.addNamespace(localName, attrValue);
        }
      }
    }
  }
}
