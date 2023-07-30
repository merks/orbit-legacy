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

import java.util.*;


/**
 * This class provides the support for the &lt;service&gt; element.
 *
 * @version 1.0
 * @author: Peter Brittenham
 */
public class ServiceImpl
  extends WSILElementWithAbstractImpl
  implements Service 
{
  /**
   * Service names.
   */
  protected Vector serviceNames = new Vector();

  /**
   * Descriptions.
   */
  protected List descriptions = new Vector();

  
  /**
   * ServiceImpl constructor comment.
   */
  public ServiceImpl() 
  {
    super();
  }

  
  /**
   * Add description element.
   */
  public void addDescription(Description description)
  {
    // Add description
    descriptions.add(description);
  }
  
  
  /**
   * Remove description element.
   */
  public void removeDescription(Description description)
  {
    // Remove description
    descriptions.remove(description);
  }

  
  /**
   * Get description elements.
   */
  public Description[] getDescriptions()
  {
    Description[] descriptionList = null;

    Object[] array = descriptions.toArray();

    descriptionList = new Description[array.length];

    for (int i = 0; i < array.length; i++) 
    {
      descriptionList[i] = (Description)array[i];
    }    

    return descriptionList;
  }

  
  /**
   * Add service name.
   */
  public void addServiceName(ServiceName serviceName) 
  {
    // Add service name
    this.serviceNames.add(serviceName);
  }
  
  
  /**
   * Remove a service name.
   */
  public void removeServiceName(ServiceName serviceName) 
  {
    // Remove service name
    this.serviceNames.remove(serviceName);
  }
  
  
  /**
   * Get the service name.
   */
  public ServiceName[] getServiceNames() 
  {    
    // Get array from vector
    ServiceName[] array = new ServiceName[serviceNames.size()];

    Object[] objArray = serviceNames.toArray();

    // Copy array
    for (int i = 0; i < objArray.length; i++)
      array[i] = (ServiceName) objArray[i];
    
    // Return array
    return (array);
  }

  
  /**
   * XML string representation of this object.
   */
  public String toXMLString() 
  {
    // Start inspection element
    StringBuffer sb = new StringBuffer("<" + ELEM_NAME + ">\n");

    // Abstract elements
    sb.append(Util.toXMLString(abstracts));

    // Link elements
    sb.append(Util.toXMLString(serviceNames));
    
    // Service elements
    sb.append(Util.toXMLString(descriptions));

    // End inspection element
    sb.append("</" + ELEM_NAME + ">\n");

    // Return string
    return sb.toString();
  }
}
