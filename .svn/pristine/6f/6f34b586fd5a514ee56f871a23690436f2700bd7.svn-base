package org.easywechat.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

import com.jc.entity.AesKey;
import com.jc.listeners.InitializeListerner;

public class MessageUtil {
	private static Logger logger = Logger.getLogger(MessageUtil.class);

	public static Map<String, String> parseXml(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		InputStream inputStream = null;
		
		String type = request.getParameter("encrypt_type");
		
		if(type != null && type.equals("aes")){
			String resp = "";
			try {
				inputStream = request.getInputStream();
				resp = InputStreamTOString(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (inputStream != null) {
						inputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			InitializeListerner.aesKey = new AesKey(request.getParameter("msg_signature"), request.getParameter("timestamp"), request.getParameter("nonce"));
			map = InitializeListerner.aesKey.decryptAseKey(resp);
		}else{
			try {
				inputStream = request.getInputStream();
				XMLInputFactory factory = XMLInputFactory.newInstance();
				XMLEventReader reader = factory.createXMLEventReader(inputStream);
				while (reader.hasNext()) {
					XMLEvent event = reader.nextEvent();
					if (event.isStartElement()) {
						String tagName = event.asStartElement().getName()
								.toString();
						if (!tagName.equals("xml")) {
							String text = reader.getElementText();
							map.put(tagName, text);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XMLStreamException e) {
				e.printStackTrace();
			} finally {
				try {
					if (inputStream != null) {
						inputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			InitializeListerner.aesKey = new AesKey(false);
			
		}
		logger.info(map);
		return map;
	}

    private static String InputStreamTOString(InputStream in) throws Exception{  
          
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] data = new byte[4096];  
        int count = -1;  
        while((count = in.read(data,0,4096)) != -1)  
            outStream.write(data, 0, count);  
          
        data = null;  
        return new String(outStream.toByteArray(),"UTF-8");  
    }

}
