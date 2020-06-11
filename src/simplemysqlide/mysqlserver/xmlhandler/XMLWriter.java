/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplemysqlide.mysqlserver.xmlhandler;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import simplemysqlide.mysqlserver.Server;
import simplemysqlide.mysqlserver.ApplicationConstants;
/**
 *
 * @author Agent47
 */
public class XMLWriter implements ApplicationConstants{
    public Server server;
    //private File xmlFile = new File(CEDENTIAL_PATH);

    public XMLWriter(Server server, String operation) {
        
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;
            dBuilder = dbFactory.newDocumentBuilder();
            // parse xml file and load into document
            File xmlFile = new File(CEDENTIAL_PATH);
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            if(operation == "create"){
                addElement(server);
            }else if(operation == "update"){
                updateElementValue(doc);
                writeXMLFile(doc);
            }else if(operation == "delete"){
                deleteElement(doc);
                writeXMLFile(doc);
            }else{
                System.out.println("Invalid Operation");
            }
        
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public XMLWriter() {
        super();
    }
    
    
    public boolean writeXMLFile(Document doc)
    throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException 
    {
        try
        {
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(CEDENTIAL_PATH));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            System.out.println("XML file updated successfully");
            return true;
        }catch(TransformerFactoryConfigurationError tfc){
            System.out.println(tfc);
            return false;
        }catch(TransformerConfigurationException tce){
            System.out.println(tce);
            return false;
        }catch(TransformerException te){
            System.out.println(te);
            return false;
        }
    }
    
    private  void addElement(Server server)  {
        try{
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        System.out.println("Writing Document " + CEDENTIAL_PATH);
        Document document = documentBuilder.parse(CEDENTIAL_PATH);
        Element root = document.getDocumentElement();
        Element newServer = document.createElement("Server");
        NodeList ServerList = document.getElementsByTagName("Server");
        Integer id = ServerList.getLength() + 1;
        
        Element serverId = document.createElement("serverId");
        serverId.appendChild(document.createTextNode(id.toString()));
        Element serverHost = document.createElement("serverHost");
        serverHost.appendChild(document.createTextNode(server.getHost()));
        Element serverPort = document.createElement("serverPort");
        serverPort.appendChild(document.createTextNode(server.getPort().toString()));
        Element databaseName = document.createElement("serverDatabase");
        databaseName.appendChild(document.createTextNode(server.getDatabase()));
        Element serverUser = document.createElement("serverUser");
        serverUser.appendChild(document.createTextNode(server.getUser()));
        Element serverPassword = document.createElement("serverPassword");
        serverPassword.appendChild(document.createTextNode(server.getPassword()));
        
        newServer.appendChild(serverId);
        newServer.appendChild(serverHost);
        newServer.appendChild(serverPort);
        newServer.appendChild(databaseName);
        newServer.appendChild(serverUser);
        newServer.appendChild(serverPassword);
        
        root.appendChild(newServer);
        DOMSource source = new DOMSource(document);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StreamResult result = new StreamResult(CEDENTIAL_PATH);
        transformer.transform(source, result);
        System.out.println("Server Saved!");
        } catch (IOException | IllegalArgumentException | ParserConfigurationException | TransformerException | DOMException | SAXException e){
            System.out.println("Error!");
        }
    }
    
    
    private static void deleteElement(Document doc) {
        NodeList users = doc.getElementsByTagName("User");
        Element user = null;
        // loop for each user
        for (int i = 0; i < users.getLength(); i++) {
            user = (Element) users.item(i);
            Node genderNode = user.getElementsByTagName("gender").item(0);
            user.removeChild(genderNode);
        }
    }
    
    private static void updateElementValue(Document doc) {
        NodeList users = doc.getElementsByTagName("User");
        Element user = null;
        // loop for each user
        for (int i = 0; i < users.getLength(); i++) {
            user = (Element) users.item(i);
            Node name = user.getElementsByTagName("firstName").item(0).getFirstChild();
            name.setNodeValue(name.getNodeValue().toUpperCase());
        }
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
    
   public static void initializeXML() {
       try
       { 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder = factory.newDocumentBuilder();
         Document doc = builder.newDocument();
         Element root = doc.createElement("Servers");
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

         File myFile = new File(CEDENTIAL_PATH);

         StreamResult console = new StreamResult(System.out);
         StreamResult file = new StreamResult(myFile);

         transf.transform(source, console);
         transf.transform(source, file);
       }catch (Exception e){
           e.printStackTrace();
       }
   }
   
    
    
    public void setServer(Server server){
        this.server = server;
    }
    
    public Server getServer(){
        return this.server;
    }
    
    
    
    
    
    
}
