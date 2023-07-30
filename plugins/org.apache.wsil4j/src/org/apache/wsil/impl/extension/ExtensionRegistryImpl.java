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

package org.apache.wsil.impl.extension;

import org.apache.wsil.extension.*;
import org.apache.wsil.extension.wsdl.*;
import org.apache.wsil.extension.uddi.*;

import org.apache.wsil.impl.extension.wsdl.*;
import org.apache.wsil.impl.extension.uddi.*;

import java.util.*;


/**
 * This class is an implementation of an ExtensionRegistry.
 *
 * @version 1.0
 * @author: Peter Brittenham
 */
public class ExtensionRegistryImpl 
  extends ExtensionRegistry 
{
  /**
   * ExtensionRegistryImpl constructor comment.
   */
  public ExtensionRegistryImpl()
  {
    // Set WSDL extension reader and writer
    addReader(WSDLConstants.NS_URI_WSIL_WSDL, new WSDLExtensionReader());
    addWriter(WSDLConstants.NS_URI_WSIL_WSDL, new WSDLExtensionWriter());
    addBuilder(WSDLConstants.NS_URI_WSIL_WSDL, new WSDLExtensionBuilder());

    // Set UDDI V1 extension reader and writer
    addReader(UDDIConstants.NS_URI_WSIL_UDDI, new UDDIExtensionReader());
    addWriter(UDDIConstants.NS_URI_WSIL_UDDI, new UDDIExtensionWriter());
    addBuilder(UDDIConstants.NS_URI_WSIL_UDDI, new UDDIExtensionBuilder());

    // Set UDDI V2 extension reader and writer
    addReader(UDDIConstants.NS_URI_WSIL_UDDI_V2, new UDDIExtensionReader());
    addWriter(UDDIConstants.NS_URI_WSIL_UDDI_V2, new UDDIExtensionWriter());
    addBuilder(UDDIConstants.NS_URI_WSIL_UDDI_V2, new UDDIExtensionBuilder());
  }
}
