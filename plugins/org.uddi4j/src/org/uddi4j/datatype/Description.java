/*
 * The source code contained herein is licensed under the IBM Public License
 * Version 1.0, which has been approved by the Open Source Initiative.
 * Copyright (C) 2001, International Business Machines Corporation
 * Copyright (C) 2001, Hewlett-Packard Company
 * All Rights Reserved.
 *
 */

package org.uddi4j.datatype;

import org.uddi4j.UDDIElement;
import org.uddi4j.UDDIException;
import org.w3c.dom.Element;

/**
 * Represents the description element within the UDDI version 2.0 schema.
 * This class contains the following types of methods:
 * 
 * <ul>
 *   <li>Constructor passing required fields.
 *   <li>Constructor that will instantiate the object from an appropriate XML
 *       DOM element.
 *   <li>Get/set methods for each attribute that this element can contain.
 *   <li>A get/setVector method is provided for sets of attributes.
 *   <li>SaveToXML method. Serializes this class within a passed in element.
 * </ul>
 * 
 * Typically, this class is used to construct parameters for, or interpret
 * responses from methods in the UDDIProxy class.
 *
 * @author David Melgar (dmelgar@us.ibm.com)
 * @author Ravi Trivedi (ravi_trivedi@hp.com)
 */
public class Description extends UDDIElement
{
	public static final String UDDI_TAG = "description";

	protected Element base = null;

	String text = null;
	String lang = null;

	/**
	 * Default constructor.
	 * Avoid using the default constructor for validation. It does not validate
	 * required fields. Instead, use the required fields constructor to perform
	 * validation.
	 */
	public Description()
	{
	}

	/**
	 * Construct the object with required fields.
	 *
	 * @param value  String value
	 */
	public Description(String value)
	{
		setText(value);
	}

	/**
	  * Construct the object with required fields.
	  *
	  * @param value  String value
	  * @param lang  String Language used for the name. Possible values defined
	  * in ISO3166.
	  */
	public Description(String value, String lang)
	{
		setText(value);
		setLang(lang);
	}

	/**
	 * Construct the object from a DOM tree. Used by
	 * UDDIProxy to construct an object from a received UDDI
	 * message.
	 *
	 * @param base   Element with the name appropriate for this class.
	 * @exception UDDIException Thrown if DOM tree contains a SOAP fault or
	 *                          disposition report indicating a UDDI error.
	 */
	public Description(Element base) throws UDDIException
	{
		// Check if it is a fault. Throws an exception if it is.
		super(base);
		text = getText(base);
		lang = base.getAttribute("xml:lang");
	}

	public void setText(String s)
	{
		text = s;
	}

	/**
	 * Set the language identifier for this string element.
	 * Possible values are defined in ISO3166.
	 *
	 * @param s  Language string.
	 */
	public void setLang(String s)
	{
		lang = s;
	}

	public String getText()
	{
		return text;
	}

	public String getLang()
	{
		return lang;
	}

	/**
	 * Save an object to the DOM tree. Used to serialize an object
	 * to a DOM tree, usually to send a UDDI message.
	 *
	 * <BR>Used by UDDIProxy.
	 *
	 * @param parent Object will serialize as a child element under the
	 *               passed in parent element.
	 */
	public void saveToXML(Element parent)
	{
	    base = parent.getOwnerDocument().createElementNS(UDDIElement.XMLNS, UDDIElement.XMLNS_PREFIX + UDDI_TAG);
		// Save attributes
		if (text != null)
		{
			base.appendChild(parent.getOwnerDocument().createTextNode(text));
		}
		if ((lang != null) && !(lang.equals("")))
		{
			base.setAttribute("xml:lang", lang);
		}
		parent.appendChild(base);
	}

	public boolean equals(Object object)
	{
		boolean result = false;
		if (object != null && object instanceof Description)
		{
			Description otherDescription = (Description) object;
			if ((this.text == null && otherDescription.text == null) || (this.text != null && this.text.equals(otherDescription.text)))
			{
				//text matches
				if ((this.lang == null && otherDescription.lang == null) || (this.lang != null && this.lang.equals(otherDescription.lang)))
				{
					//lang matches as well - objects are equal
					result = true;
				}
				//else lang does not match - result is false.   						
			}
			//else text does not match - result is false.   		
		}
		return result;
	}
}
