/*
 * The source code contained herein is licensed under the IBM Public License
 * Version 1.0, which has been approved by the Open Source Initiative.
 * Copyright (C) 2001, International Business Machines Corporation
 * All Rights Reserved.
 *
 */

package org.uddi4j.datatype;

import org.uddi4j.UDDIElement;
import org.uddi4j.UDDIException;
import org.w3c.dom.Element;

/**
 * Represents the overviewURL element within the UDDI version 2.0 schema.
 * This class contains the following types of methods:
 * 
 * <ul>
 *   <li>Constructor passing the required fields.
 *   <li>Constructor that will instantiate the object from an appropriate XML
 *       DOM element.
 *   <li>Get/set methods for each attribute that this element can contain.
 *   <li>For sets of attributes, a get/setVector method is provided.
 *   <li>SaveToXML method. Serializes this class within a passed in element.
 * </ul>
 * 
 * Typically, this class is used to construct parameters for, or interpret
 * responses from, methods in the UDDIProxy class.
 *
 *
 * @author David Melgar (dmelgar@us.ibm.com)
 */
public class OverviewURL extends UDDIElement
{
	public static final String UDDI_TAG = "overviewURL";

	protected Element base = null;

	String text = null;

	/**
	  * Default constructor.
	  * Avoid using the default constructor for validation. It does not validate
	  * required fields. Instead, use the required fields constructor to perform
	  * validation.
	  */
	public OverviewURL()
	{
	}

	/**
	 * Construct the object using required fields.
	 *
	 * @param value  String value
	 */
	public OverviewURL(String value)
	{
		setText(value);
	}

	/**
	 * Construct the object from a DOM tree. Used by
	 * UDDIProxy to construct an object from a received UDDI
	 * message.
	 *
	 * @param base   Element with the name appropriate for this class.
	 * @exception UDDIException Thrown if DOM tree contains a SOAP fault
	 *  or a disposition report indicating a UDDI error.
	 */
	public OverviewURL(Element base) throws UDDIException
	{
		// Check if it is a fault. Throws an exception if it is.
		super(base);
		text = getText(base);
	}

	public void setText(String s)
	{
		text = s;
	}

	public String getText()
	{
		return text;
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
	public void saveToXML(Element parent)
	{
	    base = parent.getOwnerDocument().createElementNS(UDDIElement.XMLNS, UDDIElement.XMLNS_PREFIX + UDDI_TAG);
		// Save attributes.
		if (text != null)
		{
			base.appendChild(parent.getOwnerDocument().createTextNode(text));
		}
		parent.appendChild(base);
	}

	public boolean equals(Object object)
	{
		boolean result = false;
		if (object != null && object instanceof OverviewURL)
		{
			OverviewURL otherOverviewURL = (OverviewURL) object;
			if ((this.text == null && otherOverviewURL.text == null)
			|| (this.text != null && this.text.equals(otherOverviewURL.text)))
			{
				//text matches - objects are equal.
				result = true;								
			}
			//else text does not match - result is false.   		
		}
		return result;
	}
}
