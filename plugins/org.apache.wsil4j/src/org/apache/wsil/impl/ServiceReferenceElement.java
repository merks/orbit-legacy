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
import org.apache.wsil.extension.*;
import org.apache.wsil.util.*;

import java.util.*;


/**
 * This class is the base class for elements that contain service references.
 *
 * @version 1.0
 * @author: Peter Brittenham
 */
public abstract class ServiceReferenceElement
  extends WSILElementWithAbstractImpl
{
  /**
   * Reference namespace.
   */
  protected String referencedNamespace = null;

  /**
   * Location.
   */
  protected String location = null;


  /**
   * Extension element.
   */
  protected ExtensionElement extElement = null;

  
  /**
   * ServiceReferenceElement constructor comment.
   */
  public ServiceReferenceElement() 
  {
    super();
  }

  
  /**
   * Set location for this link.
   */
  public void setLocation(String location) 
  {
    this.location = location;
  }

  
  /**
   * Get location for this link.
   */
  public String getLocation() 
  {
    return location;
  }
  
  
  /**
   * Set referenced namespace for this link.
   */
  public void setReferencedNamespace(String referencedNamespace) 
  {
    this.referencedNamespace = referencedNamespace;
  }

  
  /**
   * Get referenced namespace for this link.
   */
  public String getReferencedNamespace() 
  {
	return referencedNamespace;
  }

  
  /**
   * Set extension element.
   */
  public void setExtensionElement(ExtensionElement extElement)
  {
    this.extElement = extElement;
  }

  
  /**
   * Get extension elment.
   */
  public ExtensionElement getExtensionElement()
  {
    return extElement;
  }

   
  /**
   * XML string representation of this object.
   */
  protected String toXMLString(String elementName) 
  {
    // Start inspection element
    StringBuffer sb = new StringBuffer("<" + elementName);

    // Add referenced namespace
    sb.append(" " + WSILConstants.ATTR_REF_NAMESPACE + "=\"" + referencedNamespace + "\"");

    // Add location
    if (location != null)
      sb.append(" " + WSILConstants.ATTR_LOCATION + "=\"" + location + "\""); 
    
	sb.append(">\n");
	  
    // Abstract elements
    sb.append(Util.toXMLString(abstracts));

    // Link elements
    if (extElement != null)
      sb.append(extElement.toXMLString());

    // End inspection element
    sb.append("</" + elementName + ">\n");

    // Return string
    return sb.toString();
  }
}
