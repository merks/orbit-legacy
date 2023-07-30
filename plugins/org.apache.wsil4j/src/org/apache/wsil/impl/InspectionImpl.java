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

package org.apache.wsil.impl;

import org.apache.wsil.*;
import org.apache.wsil.util.*;
import org.apache.wsil.extension.wsdl.*;
import org.apache.wsil.extension.uddi.*;

import java.util.*;


/**
 * This class provides the support for the &lt;inspection&gt; element.
 *
 * @version 1.0
 * @author: Peter Brittenham
 */
public class InspectionImpl
  extends WSILElementWithAbstractImpl 
  implements Inspection 
{
  /**
   * Links.
   */
  protected List links = new Vector();

  /**
   * Services.
   */
  protected List services = new Vector();
  
  /**
   * Target namespace.
   */
  protected String targetNamespace;
  
  /**
   * Namespace list.
   */
  protected List namespaces = new Vector();

  
  /**
   * InspectionImpl constructor comment.
   */
  public InspectionImpl() 
  {
    super();
  }

  
  /**
   * Add link element.
   */
  public void addLink(Link link) 
  {
    // Add link to list
    links.add(link);
  }
  
  
  /**
   * Remove link element.
   */
  public void removeLink(Link link) 
  {
    // Remove link from list
    links.remove(link);
  }
  
  
  /**
   * Get link elements.
   */
  public Link[] getLinks() 
  {
    // Allocate array
    Link[] array = new Link[links.size()];

    // Get array from vector
    Object[] objArray = links.toArray();

    // Copy array
    for (int i = 0; i < objArray.length; i++)
      array[i] = (Link) objArray[i];
    
    // Return array
    return (array);
  }  

   
  /**
   * Add namespace.
   */
  public void addNamespace(String localName, String value) 
  {
    // Add namespace
    this.namespaces.add(new QName(localName, value));
  }
  
  
  /**
   * Get list of namespaces.
   */
  public QName[] getNamespaces() 
  {
    // Get array from vector
    QName[] array = new QName[namespaces.size()];

    Object[] objArray = abstracts.toArray();

    // Copy array
    for (int i = 0; i < objArray.length; i++)
      array[i] = (QName) objArray[i];
    
    // Return array
    return (array);
  }
  
  
  /**
   * Remove namespace.
   */
  public void removeNamespace(String localName, String value) 
  {
    QName tmpQName = new QName(localName, value);
    int i;


    for (i = 0; i < namespaces.size(); i++) 
    {
      if (tmpQName.equals((QName)namespaces.get(i))) 
        break;
    }    

    // Remove namespace
    this.namespaces.remove(i);
  }
  
  
  /**
   * Add service element.
   */
  public void addService(Service service) 
  {
    // Add service to list
    services.add(service);
  }
  
  
  /**
   * Removed service element.
   */
  public void removeService(Service service) 
  {
    // Remove service from list
    services.remove(service);
  }
  
  
  /**
   * Get service elements.
   */
  public Service[] getServices() 
  {
    // Allocate array
    Service[] array = new Service[services.size()];

    // Get array from vector
    Object[] objArray = services.toArray();

    // Copy array
    for (int i = 0; i < objArray.length; i++)
      array[i] = (Service) objArray[i];
    
    // Return array
    return (array);
  }
  
  
  /**
   * Get the target namespace in which the WSDL elements
   * are defined.
   *
   * @return the target namespace
   */
  public String getTargetNamespace() 
  {
    return targetNamespace;
  }
  
  
  /**
   * Set the target namespace in which WSDL elements are defined.
   *
   * @param namespace the target namespace
   */
  public void setTargetNamespace(String targetNamespace) 
  {
    this.targetNamespace = targetNamespace;
  }

  
  /**
   * XML string representation of this object.
   */
  public String toXMLString() 
  {
    // Start inspection element
    StringBuffer sb = new StringBuffer("<" + ELEM_NAME);
	  
    // Namespace
    sb.append(" xmlns=\"" + WSILConstants.NS_URI_WSIL  + "\"\n");
    sb.append("xmlns:" + WSDLConstants.NS_WSIL_WSDL  + "=\"" + WSDLConstants.NS_URI_WSIL_WSDL  + "\"\n");
    sb.append("xmlns:" + UDDIConstants.NS_WSIL_UDDI  + "=\"" + UDDIConstants.NS_URI_WSIL_UDDI  + "\"\n");
    sb.append("xmlns:" + UDDIConstants.NS_UDDI  + "=\"" + UDDIConstants.NS_URI_UDDI  + "\">\n");


    // Abstract elements
    sb.append(Util.toXMLString(abstracts));

    // Link elements
    sb.append(Util.toXMLString(links));
    
    // Service elements
    sb.append(Util.toXMLString(services));

    // End inspection element
    sb.append("</" + ELEM_NAME + ">\n");

    // Return string
    return sb.toString();
  }
}
