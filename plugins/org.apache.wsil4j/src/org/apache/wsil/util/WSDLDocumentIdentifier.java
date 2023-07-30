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

package org.apache.wsil.util;

import javax.wsdl.*;
import com.ibm.wsdl.Constants;

import java.util.*;

public class WSDLDocumentIdentifier 
{
  /**
   * Determine whether the document meets the criteria for an Interface document.
   * The document must contain Message, PortType and Binding elements.
   *
   * @param  wsdlDocument the document to be evaluated
   * @return true  if Message, PortType and Binding elements are found;
   *         false otherwise
   */
  public static boolean isInterfaceDocument( WSDLDocument wsdlDocument ) {
    Definition definition = wsdlDocument.getDefinitions();
    return ( hasMessageElement( definition ) && hasPortTypeElement( definition )
             && hasBindingElement( definition ) );
  }

  /**
   * Determine whether the document meets the criteria for an Interface document   * that has bindings only.
   * The document must contain Binding elements.
   *
   * @param  wsdlDocument the document to be evaluated
   * @return true  if Binding element is found;
   *         false otherwise
   */
  public static boolean isBindingInterfaceDocument( WSDLDocument wsdlDocument ) {
    Definition definition = wsdlDocument.getDefinitions();
    return (  hasBindingElement( definition ) );
  }

  /**
   * Determine whether the document meets the criteria for an Implementation document.
   * The document must contain at least one Import and one Service element.
   * We do not make any effort to verify the content of any import documents.
   *
   * @param  wsdlDocument the document to be evaluated
   * @return true  if at least one Import and one Service element are found;
   *         false otherwise
   */
  public static boolean isImplementationDocument( WSDLDocument wsdlDocument ) {
    Definition definition = wsdlDocument.getDefinitions();
    return ( hasImportElement( definition ) && hasServiceElement( definition ) );
  }

  /**
   * Determine whether the document meets the criteria for a Complete document.
   * The document must contain Message, PortType, Binding and Service elements.
   *
   * @param  wsdlDocument the document to be evaluated
   * @return true  if Message, PortType, Binding and Service elements are found;
   *         false otherwise
   */
  public static boolean isCompleteDocument( WSDLDocument wsdlDocument ) {
    Definition definition = wsdlDocument.getDefinitions();
    return ( hasMessageElement( definition ) && hasPortTypeElement( definition )
             && hasBindingElement( definition ) && hasServiceElement( definition ));
  }


  private static boolean hasMessageElement( Definition definition ) {
    /*
     *  Our usage of WSDL documents does not require the IMPORT statement
     *  to be read in and processed. This means our Message map can contain
     *  defined and undefined entries.  A defined entry would represent
     *  a Message defined in our current document.  An undefined entry would
     *  represent a Message that is referenced in our current document, but
     *  most likely defined in some other (Imported) document.
     *
     *  Get the Message map for this WSDL Definition and see if it
     *  contains any entries that are marked as defined.
     *
     */
    Iterator theIterator = definition.getMessages().values().iterator();
    while (theIterator.hasNext())
    {
      Message x = (Message)theIterator.next();
      if (!x.isUndefined())
      {
        // Found a defined Message.
        return true;
      }
    }
    // No defined Messages were found.
    return false;
  }


  private static boolean hasPortTypeElement( Definition definition ) {
    /*
     *  Our usage of WSDL documents does not require the IMPORT statement
     *  to be read in and processed. This means our PortType map can contain
     *  defined and undefined entries.  A defined entry would represent
     *  a PortType defined in our current document.  An undefined entry would
     *  represent a PortType that is referenced in our current document, but
     *  most likely defined in some other (Imported) document.
     *
     *  Get the PortType map for this WSDL Definition and see if it
     *  contains any entries that are marked as defined.
     *
     */
    Iterator theIterator = definition.getPortTypes().values().iterator();
    while (theIterator.hasNext())
    {
      PortType x = (PortType)theIterator.next();
      if (!x.isUndefined())
      {
        // Found a defined PortType.
        return true;
      }
    }
    // No defined PortTypes were found.
    return false;
  }


  private static boolean hasBindingElement( Definition definition ) {
    /*
     *  Our usage of WSDL documents does not require the IMPORT statement
     *  to be read in and processed. This means our Binding map can contain
     *  defined and undefined entries.  A defined entry would represent
     *  a Binding defined in our current document.  An undefined entry would
     *  represent a Binding that is referenced in our current document, but
     *  most likely defined in some other (Imported) document.
     *
     *  Get the Binding map for this WSDL Definition and see if it
     *  contains any entries that are marked as defined.
     *
     */
    Iterator theIterator = definition.getBindings().values().iterator();
    while (theIterator.hasNext())
    {
      Binding x = (Binding)theIterator.next();
      if (!x.isUndefined()){
        // Found a defined Binding.
        return true;
      }
    }
    // No defined Bindings were found.
    return false;
  }


  private static boolean hasImportElement( Definition definition ) {
    // Check to see if the Import map has any elements.
    return !(definition.getImports().isEmpty());
  }


  private static boolean hasServiceElement( Definition definition ) {
    // Check to see if the Service map has any elements.
    return !(definition.getServices().isEmpty());
  }

}
