package action;

import info.CreateXML;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.Process;

import org.apache.http.NameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import start.Go;
import start.Info;

public class CheckFairyReward {

	private static final String URL_FAIRY_REWARD = Info.LoginServer
			+ "/connect/app/menu/fairyrewards?cyt=1";

	public static boolean run() throws Exception {
		Info.errorPos = "CheckFairyReward.run";
		if (!Info.CheckFairyRewards)
			return false;
		if (!SellCard.tried
				&& Info.autoSellCards
				&& Process.info.fairyRewardCount >= net.Process.info.cardMax
						- net.Process.info.cardNum) {
			Document doc;
			byte[] result;
			Go.log("现有奖励:" + Process.info.fairyRewardCount + " 领取作战奖励，并卖卡");
			Process.info.fairyRewardCount = 0;
			ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
			try {
				result = Process.connect.connectToServer(URL_FAIRY_REWARD, al);
				doc = Process.ParseXMLBytes(result);
				XPathFactory factory = XPathFactory.newInstance();
				XPath xpath = factory.newXPath();

				if (ExceptionCatch.catchException(doc)) {
					return false;
				}
				CreateXML.createXML(doc, "FairyRewards");
				CreateXML.UserInfo = doc;
				int rewardCount = ((NodeList) xpath.evaluate(
						"//fairy_rewards/reward_details", doc,
						XPathConstants.NODESET)).getLength();
				for (int i = 1; i <= rewardCount; i++) {
					String msg = "";
					msg = xpath
							.evaluate(
									String.format(
											"//fairy_rewards/reward_details[%d]/fairy/name",
											i), doc).trim();
					msg = msg
							+ " Lv."
							+ xpath.evaluate(
									String.format(
											"//fairy_rewards/reward_details[%d]/fairy/lv",
											i), doc).trim();
					msg = msg
							+ " 获得 "
							+ xpath.evaluate(
									String.format(
											"//fairy_rewards/reward_details[%d]/item_name",
											i), doc).trim();
					System.out.println(msg);
				}
			} catch (Exception ex) {
				throw ex;
			}
			try {
				Go.log("尝试卖卡");
				if (SellCard.run()) {
					Go.log("卖卡成功");
					ReturnMain.run();
				} else
					Go.log("等待用户");
			} catch (Exception ex) {
				throw ex;
			}
			return true;
		}
		return false;

	}

}
