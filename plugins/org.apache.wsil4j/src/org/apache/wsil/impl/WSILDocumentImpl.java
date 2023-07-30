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
import org.apache.wsil.extension.wsdl.*;
import org.apache.wsil.extension.uddi.*;

import java.io.*;


/**
 * This class is an implementation of the WSILDocument class.
 *
 * @version 1.0
 * @author: Peter Brittenham
 */
public class WSILDocumentImpl 
  extends WSILDocument 
{
  /**
   * Create a WSIL document.
   */
  public WSILDocumentImpl()
  {
    // Create an inspection element
    this.inspection = new InspectionImpl();
  }

  
  /**
   * Create abstract element.
   */
  public Abstract createAbstract()
  {
    return new AbstractImpl();
  }
  
  
  /**
   * Create description element.
   */
  public Description createDescription()
  {
    return new DescriptionImpl();
  }
  
  
  /**
   * Create link element.
   */
  public Link createLink()
  {
    return new LinkImpl();
  }
  
  
  /**
   * Create service element.
   */
  public Service createService()
  {
    return new ServiceImpl();
  }
  
  
  /**
   * Create service name element.
   */
  public ServiceName createServiceName()
  {
    return new ServiceNameImpl();
  }

  
  /**
   * Command line interface for unit testing only.
   */
  public static void main(String[] args)
  {
    try
    {
      String filename;

	  
      // Get filename from first arg
      if (args.length == 0)
        filename = "c:\\temp\\inspection.wsil";
      else
        filename = args[0];
	    
      // Create new WSIL document instance
      WSILDocument wdRead = WSILDocument.newInstance();

      // Read the contents of the WSIL document
      wdRead.read(filename);

      // Test writer function
      wdRead.write(new PrintWriter(System.out));

      // =======================================================

      System.out.println("\n================================================\n");
       
      // Create new document
      WSILDocument wdCreate = WSILDocument.newInstance();

      // Create new service
      Service service = wdCreate.createService();

      // Create new desc
      Description desc = wdCreate.createDescription();
      
      // Get WSDL ext builder
      ExtensionBuilder extBuilder = wdCreate.getExtensionRegistry().getBuilder(WSDLConstants.NS_URI_WSIL_WSDL);

      // Create WSDL reference
      Reference ref = (Reference) extBuilder.createElement(Reference.QNAME);

      // Create WSDL implementedBinding
      ImplementedBinding ib = (ImplementedBinding) extBuilder.createElement(ImplementedBinding.QNAME);

      // Add implementedBinding to reference
      ref.addImplementedBinding(ib);

      // Add reference to description
      desc.setExtensionElement(ref);

      // Add description to service
      service.addDescription(desc);
      
      // Add service
      wdCreate.getInspection().addService(service);

      // Test writer function
      wdCreate.write(new PrintWriter(System.out));
    }

    catch (Exception e)
    {
      // Display exception
      System.out.println("EXCEPTION: " + e.toString());
      e.printStackTrace();
    }    
  }
}
