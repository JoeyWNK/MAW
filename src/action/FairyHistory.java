package action;

import info.CreateXML;
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
		Info.errorPos = "FairyHistory";
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
		al.add(new BasicNameValuePair("race_type", fairyInfo.race_type));
		al.add(new BasicNameValuePair("serial_id", fairyInfo.serialId));
		al.add(new BasicNameValuePair("user_id", fairyInfo.userId));
		try {
			result = Process.connect.connectToServer(URL_FAIRY_HISTORY, al);
		} catch (Exception ex) {
			throw ex;
		}
		Info.errorPos += 1;
		try {
			doc = Process.ParseXMLBytes(result);
			Info.errorPos += 21;
			CreateXML.createXML(doc, "FairyHistoryInfo");
		} catch (Exception ex) {
			return run(fairyInfo);
		}
		Info.errorPos += 2;
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
			int attack_times = 0;
			Info.errorPos += 11;
			NodeList list = (NodeList) xpath.evaluate(
					"//fairy_history/fairy/attacker_history/attacker[user_id="
							+ Process.info.userId + "]", doc,
					XPathConstants.NODESET);
			if (list.getLength() > 0) {
				attack_times = Integer.parseInt(xpath.evaluate(
						"//fairy_history/fairy/attacker_history/attacker[user_id="
								+ Process.info.userId + "]/attack_times", doc));
			}
			fairyInfo.LimitTime = Integer.parseInt(xpath.evaluate(
					"//fairy_history/fairy/time_limit", doc));
			Info.errorPos += 12;
			if (list.getLength()==0 || (fairyInfo.LimitTime <600 && attack_times < 4) || (Process.info.bcCurrent > Process.info.bcMax - 10)) {
				fairyInfo.currentHp = Integer.parseInt(xpath.evaluate(
						"//fairy_history/fairy/hp", doc));
				fairyInfo.maxHp = Integer.parseInt(xpath.evaluate(
						"//fairy_history/fairy/hp_max", doc));
				fairyInfo.LimitTime = Integer.parseInt(xpath.evaluate(
						"//fairy_history/fairy/time_limit", doc));
				Process.info.canBattleFairyInfos.add(fairyInfo);
			} else
				return false;
		} catch (Exception ex) {
			throw ex;
		}
		Info.errorPos += 13;
		return true;
	}
}
