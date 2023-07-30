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

import java.util.MissingResourceException;
import java.lang.reflect.InvocationTargetException;


/**
 * Primary WS-Inpsection exception.
 *
 * @version 1.0
 * @author Peter Brittenham
 */
public class WSILException 
  extends Exception
{
  /**
   * Throwable.
   */
  protected Throwable throwable = null;


  /**
   * Create an exception without a message.
   */
  public WSILException()
  {
    // Exception
    super();
  }


  /**
   * Create an exception with a message.
   * 
   * @param msg the exception message
   */
  public WSILException(String msg)
  {
  	// Exception
  	super(msg);
  }


  /**
   * Create an exception with a message and related exception.
   * 
   * @param msg the exception message
   * @param throwable throwable that is related to this exception
   */
  public WSILException(String msg, Throwable throwable)
  {
  	// Exception
  	super(msg);
    
    // Save input reference
    this.throwable = throwable;
  }


  /**
   * Returns the String representation of this object's values.
   *
   * @return Returns the detail message of this throwable object.
   */
  public String getMessage()
  {
  	// Always get message from super class
  	String msg = super.getMessage();
  
  	// If this exception contains a reference to another exception,
  	// then add it to the message
  	if (throwable != null)
  	{
      msg += "\n" +
        "------------------------------------------------------------------------------\n" +
        "  Nested exception is: \n\n  " + throwable.toString() + "\n";
  
      // If throwable is MissingResourceException, 
      // then add class name and key to output
      if (throwable instanceof MissingResourceException)
      {
        MissingResourceException mre = (MissingResourceException) throwable;
  
        // Get class name and key
        msg += " - " + mre.getKey() + "\n\t" + "[Class Name: " + mre.getClassName() + "]";
      }
  
      // If throwable is InvocationTargetException, 
      // then target of exception
      else if (throwable instanceof InvocationTargetException)
      {
        InvocationTargetException ite = (InvocationTargetException) throwable;
      
        // Get target
        msg += " - " + ite.getTargetException().toString();
      }
    }
  
    // Return message
    return msg;
  }


  /**
   * Returns the exception that caused this exception to be created.
   * 
   * @return Returns the encapsulated throwable object.
   */
  public Throwable getTargetException()
  {
  	// Return throwable
  	return throwable;
  }
}
