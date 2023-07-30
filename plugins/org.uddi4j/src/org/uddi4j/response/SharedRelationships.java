/*
 * The source code contained herein is licensed under the IBM Public License
 * Version 1.0, which has been approved by the Open Source Initiative.
 * Copyright (C) 2001, Hewlett-Packard Company
 * All Rights Reserved.
 *
 */

package org.uddi4j.response;

import java.util.Vector;

import org.uddi4j.UDDIElement;
import org.uddi4j.UDDIException;
import org.uddi4j.util.KeyedReference;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Represents the sharedRelationships element within the UDDI version 2.0 schema.
 * This class contains the following types of methods:
 * 
 * <ul>
 *   <li>A constructor that passes the required fields.
 *   <li>A Constructor that will instantiate the object from an appropriate XML
 *       DOM element.
 *   <li>Get/set methods for each attribute that this element can contain.
 *   <li>A get/setVector method is provided for sets of attributes.
 *   <li>A SaveToXML method that serializes this class within a passed in
 *       element.
 * </ul>
 * 
 * Typically, this class is used to construct parameters for, or interpret
 * responses from, methods in the UDDIProxy class.
 *
 * <p><b>Element description:</b>
 * <p>This is a collection element that contains zero or more keyedReference elements.
 * These elements form part of the RelatedBusinessInfo structure.
 * The RelatedBusinessInfo structure is a response message to the
 * find_relatedBusinesses inquiry message.
 * The information in the keyedReference and businessKey elements, for a specific
 * businessEntity (present in relatedBusinessInfo structure), represent complete
 * relationships when they match publisher assertions made by the publisher for each
 * businessEntity.
 *
 * @author Ravi Trivedi (ravi_trivedi@hp.com)
 */
public class SharedRelationships extends UDDIElement {

    public static final String UDDI_TAG = "sharedRelationships";

    public static final String DIRECTION_TOKEY = "toKey";
    public static final String DIRECTION_FROMKEY = "fromKey";

    protected Element base = null;
    Vector keyedReference = new Vector();
    String direction = null;

   /**
    * Default constructor.
    * Avoid using the default constructor for validation. It does not validate
    * required fields. Instead, use the required fields constructor to perform
    * validation.
    */

   public SharedRelationships() {
   }


   /**
    * Construct the object from a DOM tree. Used by
    * UDDIProxy to construct an object from a received UDDI
    * message.
    *
    * @param base   Element with the name appropriate for this class.
    *
    * @exception UDDIException Thrown if DOM tree contains a SOAP fault
    *  or a disposition report indicating a UDDI error.
    */

   public SharedRelationships(Element base) throws UDDIException {
     // Check if it is a fault. Throws an exception if it is.
     super(base);
     direction = base.getAttribute("direction");
     NodeList nl = null;
     nl = getChildElementsByTagName(base, KeyedReference.UDDI_TAG);
     for (int i=0; i < nl.getLength(); i++) {
        keyedReference.addElement(new KeyedReference((Element)nl.item(i)));
     }
    }

   public Vector getKeyedReferenceVector() {
       return this.keyedReference;
   }

   public void setKeyedReferenceVector(Vector keyedReference) {
       this.keyedReference = keyedReference;
   }

   public String getDirection() {
       return direction;
   }

   public void setDirection(String d) {
          direction = d ;
   }

   /**
    * Save an object to the DOM tree. Used to serialize an object
    * to a DOM tree, usually to send a UDDI message.
    *
    * <BR>Used by UDDIProxy.
    *
    * @param parent Object will serialize as a child element under the
    *  passed in parent element.
    */

   public void saveToXML(Element parent) {
        base = parent.getOwnerDocument().createElementNS(UDDIElement.XMLNS, UDDIElement.XMLNS_PREFIX + UDDI_TAG);
        if(direction !=null ) {
            base.setAttribute("direction", direction);
        }

         // Save attributes
         if (keyedReference!=null) {
            for (int i=0; i < keyedReference.size(); i++) {
               ((KeyedReference)(keyedReference.elementAt(i))).saveToXML(base);
            }
         }
         parent.appendChild(base);

   }
}
