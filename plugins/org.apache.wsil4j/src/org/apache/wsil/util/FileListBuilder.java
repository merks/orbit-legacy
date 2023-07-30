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

import java.io.*;
import java.util.*;


/**
 * This class builds a file list.
 *
 * @author Alfredo da Silva
 */
public class FileListBuilder
{
  /**
   * Filter to be used.
   */
  protected FileFilterImpl filter = null;


  /**
   * Creates a new <code>FileListBuilder</code> instance.
   *
   * @param root a <code>String</code> value
   * @param extension a <code>String</code> value
   * @exception IOException if an error occurs
   */
  public FileListBuilder(String root, String extension) throws IOException
  {
    filter = new FileFilterImpl(root, extension);
  }

  /**
   * Returns a File array.
   *
   * @return a <code>File[]</code> value
   */
  public File[] getFiles()
  {
    return filter.getFiles();
  }

  public static void main (String[] args) 
  {
    try 
    {
      FileListBuilder fileListBuilder = 
        new FileListBuilder("c:\\wstk-2.3\\demos", "wsdl");

      File[] filesList = fileListBuilder.getFiles();

      for (int i = 0; i < filesList.length; i++) 
      {
        System.out.println(filesList[i].getPath());
      }      
    }
    catch (IOException e) 
    {
      e.printStackTrace();
    }
  }
}


/**
 * This class implements a filter.
 *
 */
class FileFilterImpl implements FileFilter
{
  /**
   * File extension.
   */
  private String extension;

  /**
   * Directory root.
   */
  private File root;

  /**
   * File list.
   */
  private Vector fileVector = new Vector();


  /**
   * Creates a new <code>FileFilterImpl</code> instance.
   *
   * @param root a <code>String</code> value
   * @param extension a <code>String</code> value
   * @exception IOException if an error occurs
   */
  public FileFilterImpl(String root, String extension) throws IOException
  {
    this.root = new File(root);

    if (this.root.isDirectory() == false)
    {
      this.root = null;
      throw new IOException("Not a directory: " + this.root );
    }

    this.extension = extension;
  }

  /**
   * Called every time a filter is evaluated.
   *
   * @param pathname a <code>String</code> value
   * @return true if parameter matches filter criteria.
   */
  public boolean accept(File pathname)
  {
    if (pathname.isDirectory() == true) 
      pathname.listFiles(this);

    String filename = pathname.getName();

    if (filename.substring(filename.lastIndexOf(".") + 1).equals(extension))
    {
      if (pathname.isDirectory() == false) 
      {
        fileVector.add(pathname);
        return true;
      }
    }

    return false;
  }

  /**
   * Returns a File list.
   *
   * @return a <code>File[]</code> value
   */
  public File[] getFiles()
  {
    root.listFiles(this);

    return getFileList();
  }


  /**
   * Converts Vector to array.
   *
   * @return a <code>File[]</code> value
   */
  private File[] getFileList()
  {
    File[] fileList = new File[fileVector.size()];

    fileVector.copyInto(fileList);

    return fileList;
  }
}
