package com.until.readXML;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.pjo.web.ServletRecord;

public class ReadXml {

	private static String path="D:\\Users\\wb-limeng.g\\workspaceNews\\zest\\WebContent\\WEB-INF\\web.xml";
	
	public static void read() {
		File f = new File(path);
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(f);
			
			getServletList(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static List<ServletRecord> getServletList(Document doc){
		List<ServletRecord> servletList=new ArrayList<ServletRecord>();
		NodeList nodes=doc.getElementsByTagName("servlet-mapping");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node=nodes.item(i);
			NodeList nodeChildren=node.getChildNodes();
			for (int j = 0; j < nodeChildren.getLength(); j++) {
				Node nodeChild=nodeChildren.item(j);
				System.out.println(nodeChild.getNodeName());
				System.out.println(nodeChild.getTextContent());
			}
		}
		
		return servletList;
	}
	
	public static void main(String[] args) {
		read();
	}

}
