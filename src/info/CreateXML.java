package info;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import start.Info;

public class CreateXML {

	public static void createXML(Document doc, String xmlName) {
		StringWriter strWtr = new StringWriter();
		StreamResult strResult = new StreamResult(strWtr);
		TransformerFactory tfac = TransformerFactory.newInstance();
		if (Info.devMode || xmlName.contains("user"))
		{
			try {
				javax.xml.transform.Transformer t = tfac.newTransformer();
				t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				t.setOutputProperty(OutputKeys.INDENT, "yes");
				t.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,
				// text
				t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
						"4");
				t.transform(new DOMSource(doc.getDocumentElement()), strResult);
			} catch (Exception e) {
				System.err.println("XML.toString(Document): " + e);
			}
			String str = strResult.getWriter().toString();
			File file = new File(xmlName + ".xml");
			FileOutputStream fileOutputStream;
			try {
				fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(str.getBytes());
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				strWtr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
