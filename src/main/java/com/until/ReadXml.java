package com.until;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.pjo.web.FilterRecord;
import com.pjo.web.ListenerRecord;
import com.pjo.web.ServletRecord;
import com.servlerBean.ServlertContent;

public class ReadXml {

	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ServlertContent read() {
		ServlertContent servletContent=new ServlertContent();
		if(path==null)return null;
		try {
		File f = new File(path);
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(f);

			servletContent.setFilterList(getFilterList(doc));
			servletContent.setServletList(getServletList(doc));
			servletContent.setListenerList(getListenerList(doc));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return servletContent;
	}

	public static Map<String, String> getMap(Document doc,String []a){
		NodeList nodes = doc.getElementsByTagName(a[0]);
		Map<String, String> servlet = new HashMap<String, String>();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			NodeList nodeChildren = node.getChildNodes();
			String key="";
			String value="";
			for (int j = 0; j < nodeChildren.getLength(); j++) {
				Node nodeChild = nodeChildren.item(j);
				if (!"#text".equals(nodeChild.getNodeName())) {
					if (a[1].equals(nodeChild.getNodeName())) {
						key=nodeChild.getTextContent();
					}else if (a[2].equals(nodeChild.getNodeName())) {
						value=nodeChild.getTextContent();
					}
				}
			}
			servlet.put(key,value);
		}
		return servlet;
	}
	
	public static List<ServletRecord> getServletList(Document doc) {
		List<ServletRecord> servletList = new ArrayList<ServletRecord>();
		Map<String, String> servletPath=getMap(doc,new String[]{"servlet","servlet-name","servlet-class"});
		Map<String, String> servlet=getMap(doc,new String[]{"servlet-mapping","servlet-name","url-pattern"});
		
		for (String key : servletPath.keySet()) {
			ServletRecord srd = new ServletRecord();
			srd.setServletClass(servletPath.get(key));
			srd.setServletName(key);
			srd.setUrlpattern(servlet.get(key));
			servletList.add(srd);
		}
		return servletList;
	}

	public static List<FilterRecord> getFilterList(Document doc){
		List<FilterRecord> filterList = new ArrayList<FilterRecord>();
		Map<String, String> filter=getMap(doc,new String[]{"filter","filter-name","filter-class"});
		Map<String, String> filterMap=getMap(doc,new String[]{"filter-mapping","filter-name","url-pattern"});
		
		for (String key : filter.keySet()) {
			FilterRecord srd = new FilterRecord();
			srd.setFilterClass(filter.get(key));
			srd.setFilterName(key);
			srd.setUrlpattern(filterMap.get(key));
			filterList.add(srd);
		}
		return filterList;
	}
	
	public static List<ListenerRecord> getListenerList(Document doc){
		List<ListenerRecord> listenerList = new ArrayList<ListenerRecord>();
		NodeList nodes = doc.getElementsByTagName("listener");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			NodeList nodeChildren = node.getChildNodes();
			for (int j = 0; j < nodeChildren.getLength(); j++) {
				Node nodeChild = nodeChildren.item(j);
				if (!"#text".equals(nodeChild.getNodeName())) {
					if ("listener-class".equals(nodeChild.getNodeName())) {
						ListenerRecord lrd=new ListenerRecord();
						lrd.setListenerClass(nodeChild.getTextContent());
						listenerList.add(lrd);
					}
				}
			}
		}
		return listenerList;
	}

}
