/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplemysqlide.mysqlserver.xmlhandler;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import simplemysqlide.mysqlserver.ApplicationConstants;
import simplemysqlide.mysqlserver.Server;

/**
 *
 * @author Agent47
 */
public class XMLReader implements ApplicationConstants{
    
    private final String xmlSource = "";
    private Server server;
    
    public ArrayList<Server> getServerList() throws ParserConfigurationException, SAXException, IOException
    {
        File file = new File(CEDENTIAL_PATH);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        DocumentBuilder db = dbf.newDocumentBuilder();  
        Document doc = db.parse(file);  
        doc.getDocumentElement().normalize();  
        System.out.println("Root element: " + doc.getDocumentElement().getNodeName()); 
        NodeList nodeList = doc.getElementsByTagName("Servers");
        ArrayList<Server> serverList = new ArrayList<Server>();
        for (int itr = 0; itr < nodeList.getLength(); itr++)   
        {
            Server server = new Server();
            Node node = nodeList.item(itr);  
            System.out.println("\nNode Name :" + node.getNodeName());  
            if (node.getNodeType() == Node.ELEMENT_NODE)   
            {  
                Element eElement = (Element) node;
                server.setHost(eElement.getElementsByTagName("serverHost").item(0).getTextContent());
                server.setDatabase(eElement.getElementsByTagName("serverHost").item(0).getTextContent());
                server.setPassword(eElement.getElementsByTagName("serverHost").item(0).getTextContent());
                server.setPort(Integer.parseInt(eElement.getElementsByTagName("serverHost").item(0).getTextContent()));
                server.setUser(eElement.getElementsByTagName("serverHost").item(0).getTextContent());
                serverList.add(server);
            }
        }
        return serverList;
    }
    
    
    public String[] getDataSourceList() throws ParserConfigurationException, SAXException, IOException{
        File file = new File(System.getProperty("user.home")+"\\Documents\\credentials.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        DocumentBuilder db = dbf.newDocumentBuilder();  
        Document doc = db.parse(file);  
        doc.getDocumentElement().normalize();  
        System.out.println("File Path : "+System.getProperty("user.home")+"\\Documents\\credentials.xml");
        System.out.println("Root element: " + doc.getDocumentElement().getNodeName()); 
        NodeList nodeList = doc.getElementsByTagName("Server");
        int nodeListLength =  0;
        nodeListLength = nodeList.getLength();
        String[] serverList = new String[nodeListLength];
        for (int itr = 0; itr < nodeListLength; itr++)   
        {
            Node node = nodeList.item(itr);  
            System.out.println("\nNode Name :" + node.getNodeName());  
            if (node.getNodeType() == Node.ELEMENT_NODE)   
            {  
                Element eElement = (Element) node;
                serverList[itr] = eElement.getElementsByTagName("serverHost").item(0).getTextContent(); 
            }
        }
        return serverList;
    }
    
    
    public Server getServer(String servername) throws ParserConfigurationException, SAXException, IOException
    {
        File file = new File(CEDENTIAL_PATH);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        DocumentBuilder db = dbf.newDocumentBuilder();  
        Document doc = db.parse(file);  
        doc.getDocumentElement().normalize();  
        System.out.println("Root element: " + doc.getDocumentElement().getNodeName()); 
        NodeList nodeList = doc.getElementsByTagName("Server");
        Server server = new Server();
        for (int itr = 0; itr < nodeList.getLength(); itr++)   
        {
            Node node = nodeList.item(itr);  
            System.out.println("\nNode Name :" + node.getNodeName());  
            if (node.getNodeType() == Node.ELEMENT_NODE && node != null)   
            {  
                Element eElement = (Element) node;
                String srv = eElement.getElementsByTagName("serverHost").item(0).getTextContent().toString();
                if(srv.equals(servername)){
                    server.setHost(eElement.getElementsByTagName("serverHost").item(0).getTextContent());
                    server.setDatabase(eElement.getElementsByTagName("serverDatabase").item(0).getTextContent());
                    server.setPassword(eElement.getElementsByTagName("serverPassword").item(0).getTextContent());
                    server.setPort(Integer.parseInt(eElement.getElementsByTagName("serverPort").item(0).getTextContent()));
                    server.setUser(eElement.getElementsByTagName("serverUser").item(0).getTextContent());
                }else{
                    System.out.println("Not Found");
                }

            }
        }
        return server;
    }
    
    
    
}
