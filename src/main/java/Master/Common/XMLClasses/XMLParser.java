package Master.Common.XMLClasses;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Hashtable;

/**
 * Created by dic on 13-11-2015.
 */
public class XMLParser {

    public String[][] parseSendMultipleFiles(String xmlString)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        try
        {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName("File");
        String[][] list = new String[nodeList.getLength()][3];
        for (int temp = 0; temp < nodeList.getLength(); temp++) {

            Node nNode = nodeList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE ) {

                Element eElement = (Element) nNode;
                String fileName= eElement.getAttribute("FileName") ;
                String fileSize = eElement.getAttribute("FileSize");
                String filePath = eElement.getAttribute("FilePath");
                list[temp][0] = fileName;
                list[temp][1] = fileSize;
                list[temp][2] = filePath;

                //list[]
            }
        }

        //Node node = document.getElementById("TaskType");
        for (int i=0; i< list.length; i++)
        {
            System.out.println(list[i][0] + ":" + list[i][1] + ":" + list[i][2]  );
        }

        return list;
    }




    public Hashtable parseSendFile(String xmlString)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        try
        {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        document.getDocumentElement().normalize();
        Element root =  document.getDocumentElement();
        String fileName= root.getAttribute("FileName") ;
        String fileSize = root.getAttribute("FileSize");
        String filePath = root.getAttribute("FilePath");

        System.out.println(root.getNodeName() +   "   :  " + fileName +   "   :  " + fileSize  +   "   :  " + filePath );

        Hashtable hashtable = new Hashtable();
        hashtable.put("FileName", fileName);
        hashtable.put("FileSize", fileSize);
        hashtable.put("FilePath", filePath);

        return hashtable;
    }

    public Hashtable parseScript(String xmlString)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        try
        {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        document.getDocumentElement().normalize();
        Element root =  document.getDocumentElement();
        String scriptName= root.getAttribute("ScriptName") ;
        String filePath = root.getAttribute("FilePath");
        String resultExtension = root.getAttribute("ResultExtension");
        String scriptVersion = root.getAttribute("ScriptVersion");
        Hashtable hashtable = new Hashtable();
        hashtable.put("ScriptName", scriptName);
        hashtable.put("FilePath", filePath);
        hashtable.put("ResultExtension", resultExtension);
        hashtable.put("ScriptVersion", scriptVersion);
        System.out.println(root.getNodeName() + "   :  " + scriptName + "   :  " + filePath + "   :  " + resultExtension + "   :  " + scriptVersion);


        return hashtable;
    }

    public Hashtable parseOsInfo(String xmlString)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        try
        {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        document.getDocumentElement().normalize();
        Element root =  document.getDocumentElement();
        String PcName= root.getAttribute("PcName") ;
        String OsName = root.getAttribute("OsName");
        String resultExtension = root.getAttribute("ResultExtension");
        String scriptVersion = root.getAttribute("ScriptVersion");
        Hashtable hashtable = new Hashtable();
        hashtable.put("PcName", PcName);
        hashtable.put("OsName", OsName);

        NodeList nodeList = document.getElementsByTagName("ScriptLanguage");
        String[] list = new String[nodeList.getLength()];
        for (int temp = 0; temp < nodeList.getLength(); temp++) {

            Node nNode = nodeList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                String Location = eElement.getAttribute("Location");
                list[temp] = Location;
                //list[]
            }
        }

        System.out.println("Size before putting it in hashtable: " + list.length);
        hashtable.put("ScriptLanguages", list);

        return hashtable;
    }

    public Hashtable parseXmlResult(File xml)
    {
        File fXmlFile = xml;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc.getDocumentElement().normalize();

        Element root =  doc.getDocumentElement();
        root.toString();


        Hashtable hashtable = new Hashtable();

        hashtable.put("failures", root.getAttribute("failures"));
        hashtable.put("errors", root.getAttribute("errors"));
        hashtable.put("test-cases", root.getAttribute("test-cases"));

        return hashtable;


    }

    public String parseXmlFile(File xml)
    {
        File fXmlFile = xml;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc = null;
        String s ="";
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc.getDocumentElement().normalize();

        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
             s = sw.toString();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return s;

    }


}
