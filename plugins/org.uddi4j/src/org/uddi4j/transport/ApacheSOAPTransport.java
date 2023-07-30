/*
 * The source code contained herein is licensed under the IBM Public License
 * Version 1.0, which has been approved by the Open Source Initiative.
 * Copyright (C) 2001, International Business Machines Corporation
 * All Rights Reserved.
 *
 */

package org.uddi4j.transport;

/**
 * Transport implementation for Apache SOAP stack.
 *
 * @author David Melgar (dmelgar@us.ibm.com)
 * @author Ozzy (ozzy@hursley.ibm.com)
 */
public abstract class ApacheSOAPTransport extends TransportBase {
}
//public class ApacheSOAPTransport extends TransportBase {
//
//        private boolean debug = false;
//        private SOAPHTTPConnection connection;
//
//        /**
//         * Default constructor
//        */
//        public ApacheSOAPTransport() throws TransportException {
//        }
//
//
//        /**
//         * Sends a Element to URL.
//         *
//         * @param el     Element to send
//         * @param url    Destination URL
//         * @return An element representing a XML DOM tree containing the UDDI response.
//         * @exception TransportException
//         *                   Thrown if a problem occurs during transmission
//         */
//        public Element send(Element el, URL url) throws TransportException {
//                debug = logEnabled();
//
//                if (connection == null)
//                {
//                    setConnection();
//                }
//
//                Envelope sendEnv = new Envelope();
//                Body sendBody = new Body();
//
//                Vector bodyEntry = new Vector();
//                bodyEntry.add(el);
//                sendBody.setBodyEntries(bodyEntry);
//
//                sendEnv.setBody(sendBody);
//
//                Message soapMessage = new Message();
//
//                soapMessage.setSOAPTransport(connection);
//
//                Element base = null;
//
//                try {
//                        if (debug) {
//                                System.err.println(
//                                        "\nRequest body:\n" + DOMWriter.nodeToString(el));
//                        }
//                        soapMessage.send(url, "", sendEnv);
//                        Envelope responseEnv = soapMessage.receiveEnvelope();
//
//                        Body responseBody = responseEnv.getBody();
//                        base = (Element) responseBody.getBodyEntries().firstElement();
//                        if (debug) {
//                                System.err.println(
//                                        "\nResponse body:\n" + DOMWriter.nodeToString(base));
//                        }
//                } catch (Exception e) {
//                        throw new TransportException(e);
//                }
//
//                return base;
//        }
//
//        /*
//         * Sets the cconnection.
//         *
//         * @exception TransportException
//         *                   Thrown if a problem occurs during setup of connection
//         */
//        private void setConnection() {
//            
//            connection = new SOAPHTTPConnection();
//            
//            // Initialize variables based on config properties
//            Properties config = getConfiguration();
//            connection = new SOAPHTTPConnection();
//        
//            connection.setUserName(config.getProperty(UDDIProxy.HTTP_BASIC_AUTH_USERNAME_PROPERTY));  
//            connection.setPassword(config.getProperty(UDDIProxy.HTTP_BASIC_AUTH_PASSWORD_PROPERTY));            
//        
//            connection.setProxyHost(config.getProperty(UDDIProxy.HTTP_PROXY_HOST_PROPERTY));
//            connection.setProxyUserName(config.getProperty(UDDIProxy.HTTP_BASIC_AUTH_USERNAME_PROPERTY));
//            connection.setProxyPassword(config.getProperty(UDDIProxy.HTTP_PROXY_PASSWORD_PROPERTY));            
//        
//            try
//            {
//                int proxyPort = Integer.parseInt(config.getProperty(UDDIProxy.HTTP_PROXY_PORT_PROPERTY));
//                connection.setProxyPort(proxyPort);    
//            }
//            catch (NumberFormatException e)
//            {
//                //prop is null, empty or not an int.
//                //don't do anything, just don't set the value
//            }           
//
//        }
//}
