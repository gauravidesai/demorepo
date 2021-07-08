package com.app.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONObject;

import javax.xml.validation.*;
import org.json.XML;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import org.w3c.dom.Document;


//used to create RESTful web services using Spring MVC
@RestController
//Spring Service annotation can be applied only to classes.
@Service
//used to map web requests onto specific handler classes and/or handler methods
@RequestMapping("/rest/auth")

public class CodingTestService {
	
	
	
	
	@PostMapping("/users")
	public boolean validateInput(File inputFile) throws XMLStreamException, IOException {
//Acts as a holder for an XML Source in the form of a StAX reader
		StAXSource source = null;
		FileInputStream inputStr = null;
		boolean flag = false;
		File xsdFile = new File(System.getProperty("user.dir")+File.separator+"data"+File.separator+"schema.XSD");
		try
		{
//It reads external representations of schemas and prepares them for validation.
			SchemaFactory fact = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = fact.newSchema(xsdFile);
//This form of validation enables a client application to receive information about validation errors and warnings detected in the Java content tree.
			Validator validator = schema.newValidator();
//Defines an abstract implementation of a factory for getting streams.
			XMLInputFactory xmlIpFact = XMLInputFactory.newInstance();
			inputStr = new FileInputStream(inputFile);
			XMLStreamReader xmlStrReader = xmlIpFact.createXMLStreamReader(inputStr);
			source = new StAXSource(xmlStrReader);
			validator.validate(source);
			flag = true;	
		}
		catch(SAXException e){
			e.printStackTrace();
		}
		
		return flag;
	}
	
	@PostMapping("/convert")
		public void readXml(File f) throws MalformedURLException
		{
			if(!f.exists()) {
				System.out.println("Failed to convert");
			}
			
			URL url= f.toURL();
			String file=url.getFile();
//Defines a factory API that enables applications to obtain a parser that produces DOM (Document Object Model) object trees from XML documents.
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        try {
//Using this class, an application programmer can obtain a Document from XML.
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            Document doc = db.parse(file);
	            String xml = toStringFromDoc(doc);
	            xmlToJson(xml);
	            
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }	
	      
		}
		
		public String toStringFromDoc(Document document) {
	        String result = null;
	        if (document != null) {
//enables you to write to a string synchronously or asynchronously
	            StringWriter strWtr = new StringWriter();
//Acts as an holder for a transformation result, which may be XML, plain Text, HTML, or some other form of markup.	            
	            StreamResult strResult = new StreamResult(strWtr);
//system property that determines which Factory implementation to create is named "javax.xml.transform.TransformerFactory	            
	            TransformerFactory tfac = TransformerFactory.newInstance();
	            try {
	                javax.xml.transform.Transformer t = tfac.newTransformer();
//Set the output properties for the transformation. These properties will override properties set in the Templates with xsl:output	           
	                t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	                t.setOutputProperty(OutputKeys.INDENT, "yes");
	                t.setOutputProperty(OutputKeys.METHOD, "xml");
	                t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	                t.transform(new DOMSource(((org.w3c.dom.Document) document).getDocumentElement()), strResult);
	            } catch (Exception e) {
	                System.err.println("XML.toString(Document): " + e);
	            }
	            result = strResult.getWriter().toString();
	            try {
	                strWtr.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return result;
	        
	        
	    }
		
		
		public void xmlToJson(String xml) {
// The first method, use the method provided by JSON-JAVA */ convert xml to json
			org.json.JSONObject xmlJSONObj = XML.toJSONObject(xml);
//Set indentation
			String jsonPrettyPrintString = xmlJSONObj.toString(4);
//Output formatted json
			System.out.println(jsonPrettyPrintString);
			
			outJsonToFile(jsonPrettyPrintString);
			
		}
		
		public void outJsonToFile(String jsonObject) {
	        byte[] buff = new byte[]{};
	        String jsonStr = jsonObject;
	        FileOutputStream out = null;
	        File file = new File(System.getProperty("user.dir")+File.separator+"data"+File.separator+"sampleOutput2.json");
	        if (!file.getParentFile().exists()) {
	            file.getParentFile().mkdirs();
	        }
	        try {
	            buff = jsonStr.getBytes();
	            out = new FileOutputStream(file);
	            out.write(buff, 0, buff.length);
	                         System.out.println("Output json data to file successfully");
	           
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (out != null) {
	                try {
	                    out.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
			
	    }




}
