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

import org.apache.wsil.*;
import org.apache.wsil.extension.uddi.*;

import org.apache.wsil.impl.extension.*;

import org.uddi4j.util.*;


/**
 * This class contains the implementation for a 
 * &lt;wsiluddi:serviceDescription&gt; element.
 *
 * @version 1.0
 * @author: Peter Brittenham
 */
public class ServiceDescriptionImpl
  extends ExtensionElementImpl
  implements ServiceDescription 
{
  /**
   * Service key.
   */
  protected ServiceKey serviceKey = null;

  /**
   * Discovery URL
   */
  protected DiscoveryURL discoveryURL = null;

  /**
   * location
   */
  protected String location = null;

  /**
   * ServiceDescriptionImpl constructor comment.
   */
  public ServiceDescriptionImpl() 
  {
    super();
  }
 
  
  /**
   * Create service key.
   */
  public ServiceKey createServiceKey() 
  {
    return new ServiceKey();
  }
 
  
  /**
   * Set serviceKey.
   */
  public void setServiceKey(ServiceKey serviceKey) 
  {
    this.serviceKey = serviceKey;
  }
 
  
  /**
   * Get service key.
   */
  public ServiceKey getServiceKey() 
  {
    return serviceKey;
  }

   
  /**
   * Create discoveryURL.
   */
  public DiscoveryURL createDiscoveryURL() 
  {
    return new DiscoveryURL();
  }
 
  
  /**
   * Set discoveryURL.
   */
  public void setDiscoveryURL(DiscoveryURL discoveryURL) 
  {
    this.discoveryURL = discoveryURL;
  }

   
  /**
   * Get discoveryURL.
   */
  public DiscoveryURL getDiscoveryURL() 
  {
    return discoveryURL;
  }


  /**
   * Get location for this ServiceDescription.
   */
  public String getLocation() 
  {
    return location;
  }


  /**
   * Set location for this ServiceDescription.
   */
  public void setLocation(String location) 
  {
    this.location = location;
  }


  /**
   * XML string representation of this object.
   */
  public String toXMLString() 
  {
    String elementName = UDDIConstants.NS_WSIL_UDDI + ":" + ELEM_NAME;

    
    // Start ServiceDescription element
    StringBuffer sb = new StringBuffer("<" + elementName);

    // Location
    sb.append(" location=" + "\"" + location + "\">\n");

    if (serviceKey != null) 
    {
      String serviceKeyName = 
        UDDIConstants.NS_WSIL_UDDI + ":" + UDDIConstants.ELEM_SERVICE_KEY;

      sb.append("<" + serviceKeyName + ">");

      sb.append(serviceKey.getText());

      sb.append("</" + serviceKeyName + ">\n");
    }

    if (discoveryURL != null) 
    {
      String discoveryURLName = 
        UDDIConstants.NS_WSIL_UDDI + ":" + UDDIConstants.ELEM_DISCOVERY_URL;

      sb.append("<" + discoveryURLName + " useType=" + "\"" + discoveryURL.getUseType() + "\">");

      sb.append(discoveryURL.getText());

      sb.append("</" + discoveryURLName + ">\n");
    }

    // End ServiceDescription element
    sb.append("</" + elementName + ">\n");

    // Return string
    return sb.toString();
  }
}
