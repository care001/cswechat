package com.jc.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * XML and MAP conversion to each other
 * @author tom
 * 2015.12.10
 */
public abstract class XmlStringMap {
	
	/**
     * @description MAP conversion to XML
     * @param map
     * @return xml
     */
	public static String mapToXmlString(Map<String, String> map) {
        String xmlResult = "";

        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (String key : map.keySet()) {
            String value = "<![CDATA[" + map.get(key) + "]]>";
            sb.append("<" + key + ">" + value + "</" + key + ">");
        }
        sb.append("</xml>");
        xmlResult = sb.toString();

        return xmlResult;
    }

    /**
     * @description XML conversion to MAP
     * @param xml
     * @return Map
     */
    public static Map<String, String> xmlStringToMap(String xml) {
        Map<String, String> map = new HashMap<String, String>();
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
            Element rootElt = doc.getRootElement();
            processElement(map, rootElt);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    
    private static void processElement(Map<String, String> map, Element element){
    	@SuppressWarnings("unchecked")
		List<Element> list = element.elements();
    	for(Element e : list){
    		if(e.elements().size() != 0){
    			processElement(map, e);
        		continue;
        	}
    		map.put(e.getName(), e.getText());
    	}
    }
}