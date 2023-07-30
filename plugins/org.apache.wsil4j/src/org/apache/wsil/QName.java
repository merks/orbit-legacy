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

import org.w3c.dom.*;


/**
 * This class represents a qualified name, which has a local name
 * and a namespace URI.
 *
 * @version 1.0
 * @author Peter Brittenham
 */
public class QName 
  implements java.io.Serializable 
{
  /**
   * Namespace URI.
   */
  protected String namespaceURI;

  /**
   * Local name.
   */
  protected String localName;

  
  /**
   * Create a QName using a namespace URI and a local name.
   *
   * @param namespaceURI the namespace URI
   * @param localName the local name
   */
  public QName(String namespaceURI, String localName)
  {
    this.namespaceURI = namespaceURI;
    this.localName = localName;
  }
  
  
  /**
   * Create a QName using the namespace URI and local name for a node.
   * 
   * @param node the node element
   */
  public QName(Node node)
  {
    this.namespaceURI = node.getNamespaceURI();
    this.localName = node.getLocalName();
  }

  
  /**
   * Get the namespace URI.
   * 
   * @return Returns the namespace URI for the QName.
   */
  public String getNamespaceURI()
  {
    return this.namespaceURI;
  }

  
  /**
   * Get the local name.
   * 
   * @return Returns the local name part of the QName.
   */
  public String getLocalName()
  {
    return this.localName;
  }


  /**
   * Determine if the input QName equals this QName.
   * @param qname the QName to compare
   * 
   * @return Returns true if the QNames are equal.
   */
  public boolean equals(QName qname)
  {
    // Return true if equal
    return (qname != null && 
	        this.namespaceURI.equals(qname.getNamespaceURI()) && 
	        this.localName.equals(qname.getLocalName()));
  }

  
  /**
   * Determine if the QName for the input Node equals this QName.
   * @param node the DOM node
   * 
   * @return Returns true if the specified node was the same QName.
   */
  public boolean equals(Node node)
  {
    // Return true if they are equal
    return (node != null && this.equals(new QName(node)));
  }

  
  /**
   * String representation of this object.
   * 
   * @return Returns the string representation of this object.
   */
  public String toString()
  {
    // Return string
    return "QName: namespaceURI=" + namespaceURI + ", localName=" + localName;
  }
}
