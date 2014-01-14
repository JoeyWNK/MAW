package action;

import info.CreateXML;
import info.FairyInfo;
import info.GetBattleResult;
import info.GetUserInfo;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.Process;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import autoBattle.fairyAttackPredict;

import start.Info;

public class FairyBattle {

	// 获取妖精列表
	private static final String URL_FAIRY_BATTLE = Info.LoginServer
			+ "/connect/app/exploration/fairybattle?cyt=1";

	// 返回结果
	private static byte[] result;

	public static boolean run(FairyInfo fairyInfo) throws Exception {
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
		al.add(new BasicNameValuePair("race_type", fairyInfo.race_type));
		al.add(new BasicNameValuePair("serial_id", fairyInfo.serialId));
		al.add(new BasicNameValuePair("user_id", fairyInfo.userId));
		try {
			result = Process.connect.connectToServer(URL_FAIRY_BATTLE, al);
		} catch (Exception ex) {
			throw ex;
		}
		try {
			doc = Process.ParseXMLBytes(result);

		} catch (Exception ex) {
			throw ex;
		}
		try {
			return parse(fairyInfo,doc);
		} catch (Exception ex) {
			throw ex;
		}
	}

	private static void record(FairyInfo fairyInfo, Document doc) {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		int atk;
		try {
			atk = Integer.parseInt(xpath.evaluate("/response/body/battle_vs_info/player/user_card[serial_id=1]/power", doc));
			fairyAttackPredict.Record(fairyInfo,atk);
		} catch (NumberFormatException e) {
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
	}

	private static boolean parse(FairyInfo fairyInfo, Document doc) throws Exception {
		try {

			if (ExceptionCatch.catchException(doc)) {
				return false;
			}
			CreateXML.createXML(doc, "FairyBattleInfo");
			GetBattleResult.getBattleResult(doc);
			GetUserInfo.getUserInfo(doc, false);
			record(fairyInfo,doc);
		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}

}
