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

package org.apache.wsil.extension.uddi;

import org.apache.wsil.*;
import org.apache.wsil.extension.*;

import org.uddi4j.util.*;


/**
 * UDDI extension element for WSIL document - businessDescription element.
 *
 * @version: 1.0
 * @author: Alfredo da Silva
 */
public interface BusinessDescription
  extends ExtensionElement 
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = UDDIConstants.ELEM_BUSINESS_DESCRIPTION;

  /**
   * BusinessKey Element name.
   */
  public static final String BUSINESS_KEY_ELEM_NAME = UDDIConstants.ELEM_BUSINESS_KEY;

  /**
   * DiscoveryURL Element name.
   */
  public static final String DISCOVERY_URL_ELEM_NAME = UDDIConstants.ELEM_DISCOVERY_URL;

  /**
   * QName.
   */
  public static final QName QNAME = new QName(UDDIConstants.NS_URI_WSIL_UDDI, ELEM_NAME);
  
  /**
   * QName for UDDI V2.
   */
  public static final QName QNAME_V2 = new QName(UDDIConstants.NS_URI_WSIL_UDDI_V2, ELEM_NAME);

  /**
   * BusinessKey QName.
   */
  public static final QName BUSINESS_KEY_QNAME = new QName(UDDIConstants.NS_URI_WSIL_UDDI, 
                                                           BUSINESS_KEY_ELEM_NAME);

  /**
   * BusinessKey QName for UDDI V2.
   */
  public static final QName BUSINESS_KEY_QNAME_V2 = new QName(UDDIConstants.NS_URI_WSIL_UDDI_V2, 
                                                              BUSINESS_KEY_ELEM_NAME);

  /**
   * DiscoveryURL QName.
   */
  public static final QName DISCOVERY_URL_QNAME = new QName(UDDIConstants.NS_URI_WSIL_UDDI, 
                                                            DISCOVERY_URL_ELEM_NAME);

  /**
   * DiscoveryURL QName for UDDI V2.
   */
  public static final QName DISCOVERY_URL_QNAME_V2 = new QName(UDDIConstants.NS_URI_WSIL_UDDI_V2, 
                                                               DISCOVERY_URL_ELEM_NAME);

   
  /**
   * Create business key.
   */
  public BusinessKey createBusinessKey();
 
  
  /**
   * Set business key.
   */
  public void setBusinessKey(BusinessKey businessKey);

   
  /**
   * Get business key.
   */
  public BusinessKey getBusinessKey();
 
  
  /**
   * Create discoveryURL
   */
  public DiscoveryURL createDiscoveryURL();
 
  
  /**
   * Set discoveryURL
   */
  public void setDiscoveryURL(DiscoveryURL discoveryURL);

  
  /**
   * Get discoveryURL
   */
  public DiscoveryURL getDiscoveryURL();

  /**
   * Set location
   */
  public void setLocation(String location);

  
  /**
   * Get location for this ServiceDescription.
   */
  public String getLocation();
}

