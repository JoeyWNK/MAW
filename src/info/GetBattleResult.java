package info;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.Process;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import start.Info.EventType;

public class GetBattleResult {

	public static void getBattleResult(Document doc) throws Exception {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		Process.info.battleResult = xpath.evaluate(
				"/response/body/battle_result/winner", doc).equals("0") ? "lose"
				: "win";
		int beforeGold = Integer.parseInt(xpath.evaluate(
				"/response/body/battle_result/before_gold", doc));
		int afterGold = Integer.parseInt(xpath.evaluate(
				"/response/body/battle_result/after_gold", doc));
		Process.info.gold = afterGold - beforeGold;
		int before_exp = Integer.parseInt(xpath.evaluate(
				"/response/body/battle_result/before_exp", doc));
		int after_exp = Integer.parseInt(xpath.evaluate(
				"/response/body/battle_result/after_exp", doc));
		Process.info.exp = before_exp - after_exp;
		Process.info.nextExp = after_exp;
		String spec = xpath.evaluate(
				"//private_fairy_reward_list/special_item/after_count", doc);
		if (spec.length() == 0) {
			spec = xpath.evaluate("//battle_result/special_item/after_count", doc);
		}
		if (spec.length() != 0) {
			if (xpath.evaluate("/response/body/race_type", doc).equals("12")){
				Process.info.GuildGather = Integer.parseInt(spec);
				Process.info.GuildGatherID = Integer.parseInt(xpath.evaluate(
						"//battle_result/special_item/item_id", doc));
			}
			Process.info.gatherID = Integer.parseInt(xpath.evaluate(
					"//battle_result/special_item/item_id", doc));
			Process.info.gather = Integer.parseInt(spec);
		} else {
			Process.info.gather = 0;
		}		
		// 战斗后经验比战斗前多的话，说明升级了
		if (after_exp > before_exp) {
			Process.info.isLvUp = true;
			Process.info.events.push(EventType.levelUp);
		} else {
			Process.info.isLvUp = false;
		}
		if(Process.info.battleResult.contains("win")){
			Process.info.cardNum = ((NodeList)xpath.evaluate("//owner_card_list/user_card", doc, XPathConstants.NODESET)).getLength();
			CreateXML.UserInfo = doc;
		}
	}

}
