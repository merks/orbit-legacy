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

import java.util.*;
import java.io.*;

import org.apache.wsil.util.*;


/**
 * The WS-Inspection properties can be defined in one of three locations.  
 *
 *   1. The wsil.properties file which can be located anywhere in the classpath.
 *   2. The system property settings.
 *   3. The thread local property settings.
 * 
 * The properties are set based on this ordering.  For example, the properties 
 * from the wsil.properties file will be replaced by properties from the system 
 * property settings, which will be replaced by the thread local property settings.
 *
 * The properties that are used to define the implementation classes can be
 * specified as system properties or as properties that are set in a thread 
 * local variable.  System properties should be used if only one implementation
 * is needed per JVM.  If two or more implementations are needed per JVM, then
 * the properties should be set using the thread local variable.
 *
 * @version 1.0
 * @author: Peter Brittenham
 */
public final class WSILProperties 
{
  /**
   * Properties object.
   */
  protected static Properties props = new Properties();
    
  /**
   * Default WSILDocumentFactory class name.
   */
  protected static final String DEF_DOCUMENT_FACTORY = "org.apache.wsil.impl.WSILDocumentFactoryImpl";

  /**
   * Property that contains WSILDocumentFactory class name.
   */
  protected static final String PROP_DOCUMENT_FACTORY = "wsil.document.factory";

  /**
   * Property file name.
   */
  protected static final String PROP_FILENAME = "wsil.properties";


  /**
   * WSDL extension.
   */
  public static final String WSDL_EXTENSION = "wsdl";


  /**
   * Tranport class.
   */
  public static String TRANSPORT_CLASS = "org.uddi4j.transport.ApacheAxisTransport";
  public static final String PROP_TRANSPORT_CLASS = "wsil.transport.class";


  /**
   * WSIL hostname.
   */
  public static String WSIL_HOSTNAME = "localhost";
  public static final String PROP_WSIL_HOSTNAME = "wsil.hostname";


  /**
   * WSIL port.
   */
  public static String WSIL_PORT = "8080";
  public static final String PROP_WSIL_PORT = "wsil.port";


  /**
   *  WSIL inspection document name.
   */
  public static String WSIL_DOCUMENT_NAME = "inspection.wsil";
  public static final String PROP_WSIL_DOCUMENT_NAME = "wsil.document.name";


  /**
   * Thread local variable.
   */
  private static ThreadLocal propsThreadLocal = new ThreadLocal();

  
  static
  {
    loadProperties();
  }


  public static void loadProperties() 
  {
    try 
    {
      // Get properties file
      props.load(Util.getInputStream(PROP_FILENAME));
    
      // Get tranport class
      TRANSPORT_CLASS = props.getProperty(PROP_TRANSPORT_CLASS, 
                                          TRANSPORT_CLASS);

      // Get hostname
      WSIL_HOSTNAME = props.getProperty(PROP_WSIL_HOSTNAME, WSIL_HOSTNAME);

      // Get port
      WSIL_PORT = props.getProperty(PROP_WSIL_PORT, WSIL_PORT);

      // Get inspection document name
      WSIL_DOCUMENT_NAME = props.getProperty(PROP_WSIL_DOCUMENT_NAME, 
                                             WSIL_DOCUMENT_NAME);

      // Debug
//       System.out.println("TRANSPORT_CLASS = " + TRANSPORT_CLASS);
//       System.out.println("WSIL_HOSTNAME = " + WSIL_HOSTNAME);
//       System.out.println("WSIL_PORT = " + WSIL_PORT);
//       System.out.println("WSIL_DOCUMENT_NAME = " + WSIL_DOCUMENT_NAME);
    }
    catch (IOException e) 
    {
    }

    // Check for properties in the localThread and in the System.
    getProperties();
  }


  /** 
   * Do not allow this class to be instantiated.
   */
  private WSILProperties()
  {
  }

  
  /** 
   * Get properties that were set for this thread only.
   */
  public static Properties getProperties()
  {
    // Get the properties from the thread local variable
    if ((props = (Properties) propsThreadLocal.get()) == null)
    {
      // Get the system properties
      props = System.getProperties();
    }

    // Return properties
    return props;
  }

  
  /** 
   * Set properties for this thread only.
   */
  public static void setProperties(Properties props)
  {
    // Save the properties as thread local variables
    propsThreadLocal.set(props);
  }

  
  /** 
   * Get the WSIL document factory class name.
   */
  public static String getDocumentFactory()
  {
    // Get document factory class name from the properties settings
    return getProperties().getProperty(PROP_DOCUMENT_FACTORY, DEF_DOCUMENT_FACTORY);
  }

  
  /**
   * This method is used to unit test this class.
   */
  public static void main(String[] args)
  {
    try
    {
      // Start four threads to verify that the multithreaded use of tread local vars works
      for (int i = 0; i < 5; i++)
      {
        // Create new thread
        (new Thread(new ThreadTest("test" + i))).start();
        
        try
        {
          // Sleep
          Thread.sleep(200);

          // Display properties, which should be system properties
          System.out.println("System Properties? - " + WSILProperties.getProperties());
        }
        
        catch(Exception e){}
      }
    }

    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  
  /**
   * Inner class used for unit test.
   */
  static private class ThreadTest extends Thread
  {
    String name;
	  
    ThreadTest(String name)
    {
      this.name = name;
    }
	  
    public void run()
    {
      Properties props = new Properties();
      props.setProperty(PROP_DOCUMENT_FACTORY , name);
      WSILProperties.setProperties(props);
      //System.out.println("Thread Properyties? - " + WSILProperties.getProperties());
    }
  }
}
