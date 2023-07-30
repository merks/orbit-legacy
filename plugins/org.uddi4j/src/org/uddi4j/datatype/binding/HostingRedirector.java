/*
 * The source code contained herein is licensed under the IBM Public License
 * Version 1.0, which has been approved by the Open Source Initiative.
 * Copyright (C) 2001, International Business Machines Corporation
 * All Rights Reserved.
 *
 */

package org.uddi4j.datatype.binding;

import org.uddi4j.UDDIElement;
import org.uddi4j.UDDIException;
import org.w3c.dom.Element;

/**
 * Represents the hostingRedirector element within the UDDI version 2.0 schema.
 * This class contains the following types of methods:
 * 
 * <ul>
 *   <li>Constructor passing required fields.
 *   <li>Constructor that will instantiate the object from an appropriate XML
 *       DOM element.
 *   <li>Get/set methods for each attribute that this element can contain.
 *   <li>A get/setVector method is provided for sets of attributes.
 *   <li>SaveToXML method. Serializes this class within a passed in element. *
 * </ul>
 * 
 * Typically, this class is used to construct parameters for, or interpret
 * responses from, methods in the UDDIProxy class.
 *
 * <p><b>Element description:</b>
 * 
 * <p>Data: present only when the service is provisioned via remote hosting,
 * load balancing, etc.  Mutually exclusive with accessPoint.
 *
 * @author David Melgar (dmelgar@us.ibm.com)
 */
public class HostingRedirector extends UDDIElement {

   public static final String UDDI_TAG = "hostingRedirector";

   protected Element base = null;

   String bindingKey = null;

   /**
    * Default constructor.
    * Avoid using the default constructor for validation. It does not validate
    * required fields. Instead, use the required fields constructor to perform
    * validation.
    */
   public HostingRedirector() {
   }

   /**
    * Construct the object with required fields.
    *
    * @param bindingKey String
    */
   public HostingRedirector(String bindingKey) {
      this.bindingKey = bindingKey;
   }

   /**
    * Construct the object from a DOM tree. Used by
    * UDDIProxy to construct an object from a received UDDI
    * message.
    *
    * @param base   Element with name appropriate for this class.
    *
    * @exception UDDIException Thrown if DOM tree contains a SOAP fault or
    *  disposition report indicating a UDDI error.
    */
   public HostingRedirector(Element base) throws UDDIException {
      // Checks for a fault. Throws exception if it is a fault.
      super(base);
      bindingKey = base.getAttribute("bindingKey");
   }

   public void setBindingKey(String s) {
      bindingKey = s;
   }

   public String getBindingKey() {
      return bindingKey;
   }


   /**
    * Save object to DOM tree. Used to serialize object
    * to a DOM tree, usually to send a UDDI message.
    *
    * <BR>Used by UDDIProxy.
    *
    * @param parent Object will serialize as a child element under the
    *  passed in parent element.
    */
   public void saveToXML(Element parent) {
      base = parent.getOwnerDocument().createElementNS(UDDIElement.XMLNS, UDDIElement.XMLNS_PREFIX + UDDI_TAG);
      // Save attributes
      if (bindingKey!=null) {
         base.setAttribute("bindingKey", bindingKey);
      }
      parent.appendChild(base);
   }
}
