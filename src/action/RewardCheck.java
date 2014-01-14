package action;

import info.CreateXML;
import java.util.ArrayList;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.Process;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import start.Go;
import start.Info;

public class RewardCheck {

	private static final String URL_REWARD_LIST = Info.LoginServer
			+ "/connect/app/menu/rewardbox?cyt=1";
	private static final String URL_GET_REWARD = Info.LoginServer
			+ "/connect/app/menu/get_rewards?cyt=1";
	private static byte[] result;

	public static boolean run() throws Exception {
		Document doc;
		try {
			result = Process.connect.connectToServer(URL_REWARD_LIST, new ArrayList<NameValuePair>());
		} catch (Exception ex) {
			System.out.println(ex.toString());
			throw null;
		}
		try {
			doc = Process.ParseXMLBytes(result);
		} catch (Exception ex) {
			System.out.println(ex.toString());
			throw null;
		}
		CreateXML.createXML(doc, "Rewardlist");
		try {
			return parse(doc); 
			
		} catch (Exception ex) {
			System.out.println(ex.toString());
			throw null;
		}
	}

	private static boolean parse(Document doc) throws Exception {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();

			if (ExceptionCatch.catchException(doc)) {
				return false;
			}
			
			NodeList list = (NodeList) xpath.evaluate(
					"//body/rewardbox", doc,
					XPathConstants.NODESET);
			for (int i = 0; i < list.getLength(); i++) {
				Node f = list.item(i).getFirstChild();
				String id = "";
				int type = 0;
				String content = "";
				int point = 0;
				do {
					if(f.getNodeName().equals("id")){
						id = f.getFirstChild().getNodeValue();
					} else if(f.getFirstChild().getNodeName().equals("type")){
						type = Integer.parseInt(f.getFirstChild().getNodeValue());
					} else if(f.getFirstChild().getNodeName().equals("content")){
						content = f.getFirstChild().getNodeValue();
					} else if(f.getFirstChild().getNodeName().equals("point")){
						point = Integer.parseInt(f.getFirstChild().getNodeValue());
					}else if(f.getFirstChild().getNodeName().equals("id")){
						id = f.getFirstChild().getNodeValue();
					} 
					f = f.getNextSibling();
				} while (f != null);					
				if(!(type == 1 
						|| (type == 4 
							&& (point > 1999 
									|| net.Process.info.friendpoint > 40000
								)
							)
						)
					){
					try {
						ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
						al.add(new BasicNameValuePair("notice_id", id));
						result = Process.connect.connectToServer(URL_GET_REWARD,al );
						Go.log("领取 " + content);
					} catch (Exception ex) {
						System.out.println(ex.toString());
						throw ex;
					}
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}

}

