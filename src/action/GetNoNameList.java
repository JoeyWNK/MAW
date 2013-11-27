package action;

import info.NoNameInfo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.Process;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import start.Info;

public class GetNoNameList {
	// 获取无名亚瑟
	public static final String URL_GET_NONAME = Info.LoginServer
			+ "/connect/app/menu/player_search?cyt=1";

	// 无名亚瑟
	private static final String NONAME = "4";

	// 返回结果
	private static byte[] result;

	public static boolean run() throws Exception {
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
		al.add(new BasicNameValuePair("name", NONAME));
		try {
			result = Process.connect.connectToServer(URL_GET_NONAME, al);
		} catch (Exception ex) {
			throw ex;
		}
		try {
			doc = Process.ParseXMLBytes(result);

		} catch (Exception ex) {
			throw ex;
		}
		try {
			return parse(doc);
		} catch (Exception ex) {
			throw ex;
		}
	}

	private static boolean parse(Document doc) throws Exception {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();

			if (ExceptionCatch.catchException(doc)) {
				return false;
			}

			// 将noma放入list
			NodeList list = (NodeList) xpath
					.evaluate(
							"/response/body/player_search/user_list/user[town_level=4]",
							doc, XPathConstants.NODESET);
			NoNameInfo nameInfo = null;
			List<NoNameInfo> nameInfos = new ArrayList<NoNameInfo>();
			if (list.getLength() > 0) {
				for (int i = 0; i < list.getLength(); i++) {
					Node f = list.item(i).getFirstChild();
					nameInfo = new NoNameInfo();
					do {
						if (f.getNodeName().equals("id")) {
							nameInfo.userId = f.getFirstChild().getNodeValue();
						} else if (f.getNodeName().equals("cost")) {
							nameInfo.cost = Integer.parseInt(f.getFirstChild()
									.getNodeValue());
						}
						f = f.getNextSibling();
					} while (f != null);
					nameInfos.add(nameInfo);
				}
			}
			Process.info.noNameList = nameInfos;
		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}
}
