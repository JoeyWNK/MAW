package action;

import info.FairyInfo;
import java.util.ArrayList;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import net.Process;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import start.Info;

public class FairyHistory {
	// 获取妖精列表
	private static final String URL_FAIRY_HISTORY = Info.LoginServer
			+ "/connect/app/exploration/fairyhistory?cyt=1";

	// 返回结果
	private static byte[] result;

	public static boolean run(FairyInfo fairyInfo) throws Exception {
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
		al.add(new BasicNameValuePair("serial_id", fairyInfo.serialId));
		al.add(new BasicNameValuePair("user_id", fairyInfo.userId));
		try {
			result = Process.connect.connectToServer(URL_FAIRY_HISTORY, al);
		} catch (Exception ex) {
			throw ex;
		}
		try {
			doc = Process.ParseXMLBytes(result);

		} catch (Exception ex) {
			throw ex;
		}
		try {
			return parse(doc, fairyInfo);
		} catch (Exception ex) {
			throw ex;
		}
	}

	private static boolean parse(Document doc, FairyInfo fairyInfo)
			throws Exception {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();

			if (ExceptionCatch.catchException(doc)) {
				return false;
			}

			NodeList list = (NodeList) xpath.evaluate(
					"//fairy_history/fairy/attacker_history/attacker[user_id="
							+ Process.info.userId + "]", doc,
					XPathConstants.NODESET);
			if (list.getLength() > 0) {
				return false;
			} else {
				int currentHp = Integer.parseInt(xpath.evaluate(
						"//fairy_history/fairy/hp", doc));
				int maxHp = Integer.parseInt(xpath.evaluate(
						"//fairy_history/fairy/hp_max", doc));
				fairyInfo.currentHp = currentHp;
				fairyInfo.maxHp = maxHp;
				Process.info.canBattleFairyInfos.add(fairyInfo);
			}

		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}
}
