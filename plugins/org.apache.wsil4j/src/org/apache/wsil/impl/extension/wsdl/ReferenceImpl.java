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

import org.apache.wsil.impl.extension.*;

import java.util.*;


/**
 * This class contians the implementation for a 
 * &lt;wsilwsdl:reference element&gt;.
 * 
 * @version 1.0
 * @author: Peter Brittenham
 */
public class ReferenceImpl
  extends ExtensionElementImpl
  implements Reference 
{
  /**
   * referencedService
   */
  protected ReferencedService referencedService = null;

  /**
   * endpointPresent
   */
  protected Boolean endpointPresent = null;

  /**
   * Implemented bindings.
   */
  protected List implementedBindings = new Vector(); 

  
  /**
   * ReferenceImpl constructor comment.
   */
  public ReferenceImpl() 
  {
    super();
  }

  
  /**
   * Get extension element qname.
   */
  public QName getQName()
  {
    return QNAME;
  }
 
  
  /**
   * Add implemented binding.
   */
  public void addImplementedBinding(ImplementedBinding binding) 
  {
    this.implementedBindings.add(binding);
  }
 
  
  /**
   * Get list of implemented bindings.
   */
  public ImplementedBinding[] getImplementedBindings() 
  {
    // Get array from vector
    ImplementedBinding[] array = 
      new ImplementedBinding[implementedBindings.size()];

    Object[] objArray = implementedBindings.toArray();

    // Copy array
    for (int i = 0; i < objArray.length; i++)
      array[i] = (ImplementedBinding) objArray[i];
    
    // Return array
    return (array);
  }
 
  
  /**
   * Remove implemented binding.
   */
  public void removeImplementedBinding(ImplementedBinding binding) 
  {
    this.implementedBindings.remove(binding);
  }

  
  /**
   * Set endpointPresent
   */
  public void setEndpointPresent(Boolean value) 
  {
    this.endpointPresent = value;
  }

  
  /**
   * Get endpointPresent
   */
  public Boolean getEndpointPresent() 
  {
    return this.endpointPresent;
  }
  
  /**
   * Set referenced service
   */
  public void setReferencedService(ReferencedService referencedService) 
  {
    this.referencedService = referencedService;
  }
 
  /**
   * Get referencedService
   */
  public ReferencedService getReferencedService() 
  {
    return this.referencedService;
  }
  
  
  /**
   * Create referencedService
   */
  public ReferencedService createReferencedService() 
  {
    return new ReferencedServiceImpl();
  }


  /**
   * XML string representation of this object.
   */
  public String toXMLString() 
  {
    String elementName = WSDLConstants.NS_WSIL_WSDL + ":" + ELEM_NAME;

    
    // Start reference element
    StringBuffer sb = new StringBuffer("<" + elementName);

    if (endpointPresent != null)
      // Add endpointPresent attribute
      sb.append(" " + WSILConstants.ATTR_ENDPOINT_PRESENT + "=\"" + endpointPresent.booleanValue() + "\">\n");
    else
      sb.append(">\n");

    // ReferencedServices
    if (referencedService != null)
        sb.append(referencedService.toXMLString());

    // ImplementedBindings
    for (int i = 0; i < implementedBindings.size(); i++) 
    {
      sb.append(((ImplementedBinding)implementedBindings.get(i)).toXMLString());
    }

    // End reference element
    sb.append("</" + elementName + ">\n");

    // Return string
    return sb.toString();
  }
}
