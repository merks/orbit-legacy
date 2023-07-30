/*
 * The source code contained herein is licensed under the IBM Public License
 * Version 1.0, which has been approved by the Open Source Initiative.
 * Copyright (C) 2001, International Business Machines Corporation
 * All Rights Reserved.
 *
 */

package org.uddi4j.transport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPBodyElement;
import org.uddi4j.client.UDDIProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Transport implementation for Apache AXIS SOAP stack.
 * <p>
 * <b>
 * Note: Axis does not support proxies on a per connection basis 
 * </b><p>
 * Axis uses the JVM property support.  
 *
 * @author David Melgar (dmelgar@us.ibm.com)
 * @author Ozzy (ozzy@hursley.ibm.com)
 */
public class ApacheAxisTransport extends TransportBase {

    /**
     * Sends a UDDIElement to URL.
     *
     * @param el     UDDIElement to send
     * @param url    Destination URL
     * @return An element representing a XML DOM tree containing the UDDI response.
     * @exception TransportException
     *                   Thrown if a problem occurs during transmission
     */
    public Element send(Element el, URL url) throws TransportException {

        Element base = null;
        boolean debug = logEnabled();
        Call call = null;

        try {
            Service service = new Service();
            call = (Call) service.createCall();
            
            Properties config = getConfiguration();
            String username = config.getProperty(UDDIProxy.HTTP_BASIC_AUTH_USERNAME_PROPERTY);
            String password = config.getProperty(UDDIProxy.HTTP_BASIC_AUTH_PASSWORD_PROPERTY);
            if (username != null)
            {
                call.setUsername(username);
            }
            if (password != null)
            {
                call.setPassword(password);
            }

            call.setTargetEndpointAddress(url);
            //call.setProperty(HTTPConstants.MC_HTTP_SOAPACTION, "");

            Vector result = null;

            // Rebuild the body. This convoluted process lets Axis handle the level 1 DOM tree
            // from UDDI4J. When UDDI4J moves to a level 2 DOM tree, the more obvious method
            // can be tried.
            String str = null;

            str = org.apache.axis.utils.XMLUtils.ElementToString(el);
            SOAPBodyElement body =
                new SOAPBodyElement(
                    new java.io.ByteArrayInputStream(str.getBytes("UTF8")));

            // if DOM level 2 use this more obvious approach
            // SOAPBodyElement body = new SOAPBodyElement(el);

            Object[] params = new Object[] { body };

            if (debug) {
                System.err.println("\nRequest message:\n" + params[0]);
            }

            result = (Vector) call.invoke(params);

            base = stringToElement(((SOAPBodyElement) result.elementAt(0)).toString());
        } catch (AxisFault fault) {
            try {
                Message m = call.getResponseMessage();
                base = stringToElement(m.getSOAPEnvelope().getFirstBody().toString());
            } catch (Exception e) {
                throw new TransportException(e);
            }
        } catch (Exception e) {
            throw new TransportException(e);
        }
        if (debug && base != null) {
            System.err.println(
                "\nResponse message:\n"
                    + org.apache.axis.utils.XMLUtils.ElementToString(base));
        }

        return base;
    }

    public Element stringToElement(String s) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new ByteArrayInputStream(s.getBytes("UTF8")));
        return doc.getDocumentElement();
     }

}
