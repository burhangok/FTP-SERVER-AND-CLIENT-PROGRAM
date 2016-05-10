package ftpclient;

/**
 *
 * @author burhan
 */

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;


public class SystemInformation {
    
    private String address;

    public String getAddress() {
        return address;
    }

    private void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    private void setPort(int port) {
        this.port = port;
    }
    private int port;
        
    public SystemInformation(File file) {
        
        FetchDetailsFromXML(file);
    }
    
    private void FetchDetailsFromXML(File file)
    {        
      
        try 
        {
            File fXMLfile = file;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXMLfile);
            
            doc.getDocumentElement().normalize();
            
            Node node = doc.getElementsByTagName("kullanıcı").item(0);
            
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element)node;                
                setAddress(element.getElementsByTagName("adres").item(0).getTextContent());
                setPort(Integer.parseInt(element.getElementsByTagName("port").item(0).getTextContent()));
            }
        } 
        catch (ParserConfigurationException | SAXException | IOException | DOMException e) 
        {
            e.printStackTrace();
        }
    }
    
}
