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

package org.apache.wsil.extension;

import java.util.*;


/**
 * This class is the base class for all extension registry implementations.
 * 
 * @version 1.0
 * @author: Peter Brittenham
 */
public abstract class ExtensionRegistry 
{
  /**
   * List of extension readers.
   */
  protected Map readers = new HashMap();

  /**
   * List of extension writers.
   */
  protected Map writers = new HashMap();

  /**
   * List of extension builders.
   */
  protected Map builders = new HashMap();
  
  
  /**
   * Add an extension reader for a specific namespace.
   */
  public void addReader(String namespace, ExtensionReader reader) 
  {
    // Add reader
    readers.put(namespace, reader);
  }
  
  
  /**
   * Get an extension reader for a specific namespace.
   */
  public ExtensionReader getReader(String namespace) 
  {
    // Return reader
    return (ExtensionReader) readers.get(namespace);
  }
  
  
  /**
   * Add an extension writer for a specific namespace.
   */
  public void addWriter(String namespace, ExtensionWriter writer) 
  {
    // Add writer
    writers.put(namespace, writer);
  }
  
  
  /**
   * Get an extension writer for a specific namespace.
   */
  public ExtensionWriter getWriter(String namespace) 
  {
    // Return writer
    return (ExtensionWriter) writers.get(namespace);
  }
  
  
  /**
   * Add an extension builder for a specific namespace.
   */
  public void addBuilder(String namespace, ExtensionBuilder builder) 
  {
    // Add builder
    builders.put(namespace, builder);
  }
  
  
  /**
   * Get an extension builder for a specific namespace.
   */
  public ExtensionBuilder getBuilder(String namespace) 
  {
    // Return builder
    return (ExtensionBuilder) builders.get(namespace);
  }
}
