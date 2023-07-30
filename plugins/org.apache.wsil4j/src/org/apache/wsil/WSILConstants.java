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

package org.apache.wsil;


/**
 * Constants used when processing WS-Inspection documents.
 *
 * version 1.0
 * @author Peter Brittenham
 */
public interface WSILConstants 
{
  /**
   * XML declaration statement.
   */
  public static final String XML_DECL = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

  /**
   * XML namespace URI.
   */
  public static final String NS_URI_XMLNS = "http://www.w3.org/2000/xmlns/";
   
  /**
   * XML schema namespace URI.
   */
  public static final String NS_URI_XSD = "http://www.w3.org/2001/XMLSchema";
   
  /**
   * XML schema namespace name.
   */
  public static final String NS_XSD = "xsd";

  /**
   * WSIL namespace URI.
   */
  public static final String NS_URI_WSIL = "http://schemas.xmlsoap.org/ws/2001/10/inspection/";

  /**
   * WSIL namespace name.
   */
  public static final String NS_WSIL = "wsil";

  /**
   * Default language for abstract and name elements.
   */
  public static final String DEF_LANG = "en-US";

  /**
   * Inspection element name.
   */
  public static final String ELEM_INSPECTION = "inspection";

  /**
   * Service element name.
   */
  public static final String ELEM_SERVICE = "service";

  /**
   * Service name element name.
   */
  public static final String ELEM_NAME = "name";

  /**
   * Description element name.
   */
  public static final String ELEM_DESCRIPTION = "description";

  /**
   * Import element name.
   */
  public static final String ELEM_LINK = "link";

  /**
   * Abstract element name.
   */
  public static final String ELEM_ABSTRACT = "abstract";

  /**
   * XML namespace attribute name.
   */
  public static final String ATTR_XMLNS = "xmlns";
  
  /**
   * XML lang namespace.
   */
  public static final String ATTR_XML_NS = "xml";
  
  /**
   * XML lang attribute.
   */
  public static final String ATTR_XML_LANG = "lang";

  /**
   * Target namespace attribute name.
   */
  public static final String ATTR_TARGET_NAMESPACE = "targetNamespace";

  /**
   * Referenced namespace attribute name.
   */
  public static final String ATTR_REF_NAMESPACE = "referencedNamespace";

  /**
   * Location attribute name.
   */
  public static final String ATTR_LOCATION = "location";

  /**
   * EndpointPresent attribute name.
   */
  public static final String ATTR_ENDPOINT_PRESENT = "endpointPresent";
}
