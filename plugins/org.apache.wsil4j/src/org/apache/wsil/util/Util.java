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

import org.apache.wsil.*;

import java.util.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.text.DateFormat;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.StringTokenizer;


/**
 * This class will ...
 * @version 1.0
 * @author: Peter Brittenham
 */
public final class Util 
{ 
  /**
   * Class name.
   */
  protected static String sClassName = Util.class.getName();

  /**
   * Line separator character.
   */
  public static final String LINE_SEPARATOR = System.getProperty("line.separator"); 

  /** 
   * Screen size (width and height). 
   */
  //public final static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();


  /**
   * Do not allow an object to be created.
   */
  private Util() 
  {
  }


  /**
   * Local a thread group using its name.
   *
   * @param sName thread group name
   * 
   * @return Returns thread group.
   */
  public static ThreadGroup findThreadGroup
  (
    String  sName
  )

  {
    boolean bThgFound = false;
    ThreadGroup thgReturn   = null;


    // Get current thread group
    ThreadGroup thgCurrent  = Thread.currentThread().getThreadGroup();

    // If the current thread group is the one we are looking for,
    // then return a reference to it
    if (thgCurrent.getName().equals(sName))
    {
      bThgFound = true;
      thgReturn = thgCurrent;
    }

    // If the thread group was not found, then try to go through parent chain
    if (!(bThgFound))
    {
      // Setup to search parent chain
      ThreadGroup thgPrev     = null;
      ThreadGroup thgNext     = thgCurrent;

      // Go through each parent till requested group found
      for (thgPrev = thgNext;
          thgNext != null && !(bThgFound);
          thgNext = thgPrev.getParent())
      {
        // Save current reference
        thgPrev = thgNext;

        // If this is the requested thread group, then stop
        if (thgNext.getName().equals(sName))
        {
          bThgFound = true;
          thgReturn = thgNext;
        }
      }
    }

    // If the thread group was not found, then try to go down the chain
    if (!(bThgFound))
    {
      // Allocate array to hold active thread groups
      ThreadGroup thg[] = new ThreadGroup[thgCurrent.activeGroupCount()];

      // Get all of the active thread groups
      int iCnt = thgCurrent.enumerate(thg);

      for (int i = 0; i < iCnt  && !(bThgFound); i++)
      {
        // If this is the requested thread group, then stop
        if (thg[i].getName().equals(sName))
        {
          bThgFound = true;
          thgReturn = thg[i];
        }
      }
    }

    // Return thread group
    return thgReturn;
  }


  /**
   * This method prints a formatted message using System.out.prinln.
   * 
   * @param className class name which originated message
   * @param methodName method name which originated message
   * @param e exception
   */
  public static void out
  (
    String className,
    String methodName,
    Exception e
  )

  {
    out(className, methodName, e.toString());
  }

  /**
   * This method prints a formatted message using System.out.prinln.
   * 
   * @param className class name which originated message
   * @param methodName method name which originated message
   * @param msg message text
   */
  public static void out
  (
    String className,
    String methodName,
    String msg
  )

  {
    // Print formatted message string
    System.out.println("[" + className + "] [" + methodName + "()] " + msg);
  }


  /**
   * This method prints a formatted message using System.out.prinln.
   * 
   * @param className class name which originated message
   * @param methodName method name which originated message
  * @param e exception
   */
  public static void debug
  (
    String className,
    String methodName,
    Exception e
  )

  {
    // Print formatted message string
    debug(className, methodName, e.toString());
  }


  /**
   * This method prints a formatted message using System.out.prinln.
   * 
   * @param className class name which originated message
   * @param methodName method name which originated message
   * @param msg message text
   */
  public static void debug
  (
    String className,
    String methodName,
    String msg
  )

  {
    // Print formatted message string
    System.out.println("[" + className + "] [" + methodName + "()] " + msg);
  }


  /**
   * Get date/time stamp for current date/time.
   * @param sName thread group name
   */
  public static String getDateTimeStamp()
  {
    // Return date/time stamp
    return getDateTimeStamp(new Date());
  }


  /**
   * Get date/time stamp for current date/time.
   * @param sName thread group name
   */
  public static String getDateTimeStamp
  (
    Date date
  )

  {
    // Return date/time stamp
    return "UTC-Timestamp=" + DateFormat.getDateTimeInstance().format(date);
  }


  /**
   * Get contents of a resource and return as a input stream.
   * 
   * @param sResourceName the name of the resource to get and return as
   * an input stream
   */
  public static InputStream getInputStream
  (
    String sResourceName
  ) throws IOException

  {
    InputStream is = null;


    // If resource reference is a URL, then input stream from URL
    try
    {
      // Try to create URL
      URL urlResource = new URL(sResourceName);

      // If successful, then get URL input stream
      is = getInputStream(urlResource);
    }

    // Else try to read resource directly
    catch (MalformedURLException mue)
    {
      boolean bTryClassLoader = false;

      try
      {
        // Open file input stream
        is = new BufferedInputStream(new FileInputStream(sResourceName));
      }
      catch (FileNotFoundException fnfe)
      {
        // Set try class loader flag
        bTryClassLoader = true;
      }
      catch (SecurityException se)
      {
        // Set try class loader flag
        bTryClassLoader = true;
      }
      catch (Exception e)
      {
        // DEBUG:
        out(sClassName, "getInputStream", e.toString());
      }

      // If try class loader, then use it to get input stream
      if (bTryClassLoader)
      {
        // Use class loader to load resource
        is = ClassLoader.getSystemResourceAsStream(sResourceName);
      }
    }

    // If the input stream is null, then throw FileNotFoundException
    if (is == null)
    {
      //try this
      is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sResourceName);
    }

    // If the input stream is null, then throw FileNotFoundException
    if (is == null)
    {
      //try this
      URL aURL = Thread.currentThread().getContextClassLoader().getResource(sResourceName);
      if ( aURL != null ) is = getInputStream(aURL);
    }

    if (is == null) 
      // Throw execption
      throw new FileNotFoundException("Could not locate resource file: " + sResourceName);


    // Return input stream
    return is;
  }


  /**
   * Get the input stream from a URL.
   * @param url the URL to get the input stream from
   */
  public static InputStream getInputStream
  (
    URL urlFile
  ) throws IOException, 
           ConnectException

  {
    InputStream is = null;


    // ADD: how are URLs that are password protected handled????

    try
    {
      // Open file input stream
      is = new BufferedInputStream(urlFile.openStream());
    }

    catch (ConnectException e)
    {
      // Re-throw this excpetion with additional information
      throw new java.net.ConnectException("Could not connect to URL: " + 
                                          urlFile.toExternalForm() + ".");
    }

    // Return input stream
    return is;
  }

  
  /**
   * Get local host name.
   */
  public static String getLocalHostName()
  {
    String sLocalHostName;
    
    try
    {
      // Get local host name
      sLocalHostName = InetAddress.getLocalHost().getHostName();
    }
    catch (Exception e)
    {
      // Set default local host name
      sLocalHostName = "127.0.0.1";
    }

    // Return local host name
    return sLocalHostName;
  }


  /**
   * Build a URL string from hostname, port and URN.
   *
   * @param hostname the hostname
   * @param port the port
   * @param urn the URN
   *
   * @return Returns formatted URL string.
   */
  public static String formatURL(String hostname, String port, String urn)
  {
    // Build URN
    String formatURN = urn;
    
    // If URN doesn't start with "/", then add it 
    if (!(formatURN.startsWith("/"))) 
    {
      // Add "/" to beginning of the string
      formatURN = "/" + urn;
    }

    // Return URL string
    return "http://" + hostname + ":" + port + formatURN;
  }


  /**
   * This method will replace all of the occurances of a string 
   * with a substitution string.
   * 
   * @param sText String to udpate.
   * @param sFind String to find.
   * @param sReplace String to use for substitution.
   * 
   * @return Returns updated string.
   */
  public static String replaceString
  (
    String sText, 
    String sFind, 
    String sReplace
  )

  {
    int iPrevIndex = 0;

    int iFindLen = sFind.length();
    int iReplaceLen = sReplace.length();

    String sUpdatedText = sText;


    // Replace all occurances of the find string
    for (int iIndex = sUpdatedText.indexOf(sFind); 
        iIndex < (sUpdatedText.length()-1) && iIndex != -1;
        iIndex = sUpdatedText.indexOf(sFind, iPrevIndex+iReplaceLen))
    {
      // Set updated text from the front portion + replacement text + back portion
      sUpdatedText = sUpdatedText.substring(0, iIndex) + 
                     sReplace + 
                     sUpdatedText.substring(iIndex + iFindLen);

      // Set the previous index field
      iPrevIndex = iIndex;
    }

    // Return updated text string
    return sUpdatedText;
  }


  /**
   * Convert a string into a string array.
   * @param s string to convert to a string array
   */
  public static String[] toStringArray
  (
    String s
  )

  {
    return toStringArray(s, " ");
  }


  /**
   * Convert a string into a string array.
   * @param s string to convert to a string array
   */
  public static String[] toStringArray
  (
    String s,
    String sDelim
  )

  {
    int iCnt = 0;
    String sa[] = null;


    // If string is null, then bypass tokenize process
    if (s == null)
    {
      sa = new String[0];
    }

    // Else tokenize the argument string
    else
    {
      // Get string tokenizer
      StringTokenizer st = new StringTokenizer(s, " ");

      // Allocate string array
      sa = new String[st.countTokens()];

      // Get all of the string tokens
      while (st.hasMoreTokens())
        sa[iCnt++] = st.nextToken();
    }

    // Return the string array
    return sa;
  }


  /**
   * Creates a URL object.  If the URL uses the https protocol, then the correct URLStreamHandler
   * is associated with the URL object.
   *
   * @param urlString URL string to format into a URL object
   *
   * @return Returns a URL object.  A null value is returned if the input URL string is null.
   */
  public static URL createURL(String urlString)
    throws MalformedURLException, ClassNotFoundException, 
           InstantiationException, IllegalAccessException
  {
    URL url = null;


    // If input URL string is NOT null, then create URL object
    if (urlString != null) 
    {
      if (urlString.startsWith("https")) 
      {
        String t1 = new String(urlString.substring(8));
        String host = new String(t1.substring(0, t1.indexOf('/')));
        String file = new String(t1.substring(t1.indexOf('/')));

        int index;
        if ((index = host.indexOf(":443")) != -1)
          host = host.substring(0, index);

        url = new URL("https", host, 443, file,
                      (URLStreamHandler)Class.forName("com.ibm.net.ssl.internal.www.protocol.https.Handler").newInstance());
      }

      // Else just create URL object
      else 
      {
        url = new URL(urlString);
      }
    }

    // Return URL object
    return url;
  }


  /**
   * Create an HTTPS URL.
   */
  public static String createHttpsURL(String url)
  {
    String returnURL = null;

    if (url != null) 
    {
      if (url.startsWith("https")) 
      {
        String t1 = new String(url.substring(8));
        String prot = new String(url.substring(0, 8));
        String host = new String(t1.substring(0, t1.indexOf('/')));
        String file = new String(t1.substring(t1.indexOf('/')));

        int index;

        if ((index = host.indexOf(":443")) != -1)
          host = host.substring(0, index);

        returnURL = prot + host + ":443" + file;
      }
      else 
      {
        returnURL = url;
      }
    }
     
    // Return URL
    return returnURL;
  }

  /**
   * Get XML string representation for WSIL element.
   */
  public static String toXMLString(Collection collection) 
  {
	StringBuffer sb = new StringBuffer("");

	
    // Get list of WSIL elements
    Iterator iterator = collection.iterator();

    // Process each element
    while (iterator.hasNext())
    {
      sb.append(((WSILElement) iterator.next()).toXMLString());
    }

    // Return string
    return sb.toString();
  }
}
