package info;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import start.Info;

import net.Process;

public class GetUserInfo {

	public static void getUserInfo(Document doc, boolean getId)
			throws Exception {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		Process.info.cookie = xpath.evaluate("//session_id", doc);
		Process.info.userName = xpath.evaluate("//your_data/name", doc);
		Process.info.userLv = xpath.evaluate("//your_data/town_level", doc);
		Process.info.apMax = Integer.parseInt(xpath.evaluate(
				"//your_data/ap/max", doc));
		Process.info.apCurrent = Integer.parseInt(xpath.evaluate(
				"//your_data/ap/current", doc));
		Process.info.bcMax = Integer.parseInt(xpath.evaluate(
				"//your_data/bc/max", doc));
		Process.info.bcCurrent = Integer.parseInt(xpath.evaluate(
				"//your_data/bc/current", doc));
		Process.info.freeApBcPoint = Integer.parseInt(xpath.evaluate(
				"//your_data/free_ap_bc_point", doc));
		String cardMax = xpath.evaluate("//your_data/max_card_num",doc);
		if (cardMax != null && !cardMax.isEmpty()) {
			Process.info.cardMax = Integer.parseInt(cardMax);	
		}
		if (getId) {
			Process.info.userId = xpath.evaluate(
					"/response/body/login/user_id", doc);
			int cardCount = ((NodeList)xpath.evaluate("//owner_card_list/user_card", doc, XPathConstants.NODESET)).getLength();
			if (cardCount > 0) Info.userCardsInfos = new ArrayList<UserCardsInfo>();
			String wolf = null;
			int wolflv = 0;
			for (int i = 1; i < cardCount + 1; i++) {
				UserCardsInfo c = new UserCardsInfo();
				String p = String.format("//owner_card_list/user_card[%d]", i);
				c.serialId = Integer.parseInt(xpath.evaluate(p+"/serial_id", doc));
				c.master_card_id = Integer.parseInt(xpath.evaluate(p+"/master_card_id", doc));
				c.lv = Integer.parseInt(xpath.evaluate(p+"/lv", doc));
				if (c.master_card_id == 124 && c.lv > wolflv){
					wolf = c.serialId +"";
				}
				c.hp = Integer.parseInt(xpath.evaluate(p+"/hp", doc));
				c.atk = Integer.parseInt(xpath.evaluate(p+"/power", doc));
				c.sale_price = Integer.parseInt(xpath.evaluate(p+"/sale_price", doc));
				c.holography = xpath.evaluate(p+"/holography", doc).equals("1");
				Info.userCardsInfos.add(c);
			}
			
			if (null == wolf || wolf.equals("")) {
				Info.wolf = Info.wolf
						+ ",empty,empty,empty,empty,empty,empty,empty,empty,empty,empty,empty";
			} else {
				Info.wolf = wolf
						+ ",empty,empty,empty,empty,empty,empty,empty,empty,empty,empty,empty";
				Info.wolfLr = wolf;
				Info.lickCost = 2;
			}
		}
	}

}
