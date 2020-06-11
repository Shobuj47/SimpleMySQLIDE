
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import simplemysqlide.mysqlserver.Server;
import simplemysqlide.mysqlserver.xmlhandler.XMLWriter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Agent47
 */
public class testxmlperser {
    public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException{
        Server addNewServer = new Server();
        addNewServer.setDatabase("demo");
        addNewServer.setHost("somehost");
        addNewServer.setPassword("somepass");
        addNewServer.setPort(3306);
        addNewServer.setUser("someuser");
        XMLWriter xmlwrite = new XMLWriter(addNewServer, "create");
        
//        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
//        Document document = documentBuilder.parse(System.getProperty("user.home")+"/Documents/pika.xml");
//        Element root = document.getDocumentElement();
//        Element newServer = document.createElement("Server");
//
//        Element name = document.createElement("serverHost");
//        name.appendChild(document.createTextNode(addNewServer.getHost()));
//        newServer.appendChild(name);
//
//        Element port = document.createElement("serverPort");
//        port.appendChild(document.createTextNode(addNewServer.getPort().toString()));
//        newServer.appendChild(port);
//        root.appendChild(newServer);
//        DOMSource source = new DOMSource(document);
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer();
//        StreamResult result = new StreamResult(System.getProperty("user.home")+"/Documents/pika.xml");
//        transformer.transform(source, result);
        
    }
    
    
    private void readXML() throws ParserConfigurationException, SAXException, IOException{
        File xmlFile = new File("credentials.xml");
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

        NodeList nList = doc.getElementsByTagName("Server");

        for (int i = 0; i < nList.getLength(); i++) {

            Node nNode = nList.item(i);

            System.out.println("\nCurrent Element: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element elem = (Element) nNode;

                String uid = elem.getAttribute("serverId");

                Node node1 = elem.getElementsByTagName("serverHost").item(0);
                String fname = node1.getTextContent();

                Node node2 = elem.getElementsByTagName("serverPort").item(0);
                String lname = node2.getTextContent();

                Node node3 = elem.getElementsByTagName("databaseName").item(0);
                String occup = node3.getTextContent();

                System.out.printf("User id: %s%n", uid);
                System.out.printf("First name: %s%n", fname);
                System.out.printf("Last name: %s%n", lname);
                System.out.printf("Occupation: %s%n", occup);
            }
        }
    }
    
    private void writeXML() throws ParserConfigurationException, SAXException, IOException{
        File xmlFile = new File("credentials.xml");
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();
        
        
        Element server = doc.createElement("Server");
        server.setAttribute("id", "3");
        server.appendChild(createServerElement(doc, "serverHost", "3"));
        server.appendChild(createServerElement(doc, "serverPassword", "A Passwords"));
        
        
        
    }
    
    
    private static Node createServerElement(Document doc, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));

        return node;
    }
    
   private static Node createServer(Document doc,Integer id, Server server) {
        Element serverElement = doc.createElement("Server");
        serverElement.setAttribute("id", id.toString());
        serverElement.appendChild(createServerElement(doc, "serverHost", server.getHost()));
        serverElement.appendChild(createServerElement(doc, "serverPort", server.getPort().toString()));
        serverElement.appendChild(createServerElement(doc, "serverUser", server.getUser()));
        serverElement.appendChild(createServerElement(doc, "serverPassword", server.getPassword()));
        serverElement.appendChild(createServerElement(doc, "serverDatabase", server.getDatabase()));
        return serverElement;
    } 
    
   private static void initializeXML() throws ParserConfigurationException, TransformerConfigurationException, TransformerException{
       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElementNS("simplemysqlide.mysqlserver", "Servers");
        doc.appendChild(root);
        Server server  = new Server();
        server.setHost("127.0.0.1");
        server.setPort(3306);
        server.setUser("root");
        server.setPassword("");
        server.setDatabase("mysql");
        root.appendChild(createServer(doc, 1, server));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transf = transformerFactory.newTransformer();
        
        transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transf.setOutputProperty(OutputKeys.INDENT, "yes");
        transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        
        DOMSource source = new DOMSource(doc);

        File myFile = new File("credentials.xml");
        
        StreamResult console = new StreamResult(System.out);
        StreamResult file = new StreamResult(myFile);

        transf.transform(source, console);
        transf.transform(source, file);
   
   }
   
   
}
