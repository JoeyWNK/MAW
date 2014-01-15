package info;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.Process;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import start.Info;

public class FloorRunInfo {

	public static void floorRunInfo(Document doc) throws Exception {
		CreateXML.createXML(doc, "FloorRunInfo");
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		String event_type = xpath.evaluate("//explore/event_type", doc);
		if (event_type.equals("1")) {
			Process.info.event_type = "遇到妖精";
		} else if (event_type.equals("2")) {
			Process.info.event_type = "遇到小伙伴";
		} else if (event_type.equals("3")) {
			Process.info.event_type = "获得卡片";
		} else if (event_type.equals("5")) {
			Process.info.event_type = "地图踏破";
		} else if (event_type.equals("12")) {
			Process.info.event_type = "AP回复";
		} else if (event_type.equals("13")) {
			Process.info.event_type = "BC回复";
		} else if (event_type.equals("15")) {
			Process.info.event_type = "获得卡片";
			try {
				Process.info.cardNum = ((NodeList) xpath.evaluate(
						"//owner_card_list/user_card", doc,
						XPathConstants.NODESET)).getLength();
				CreateXML.UserInfo = doc;
			} catch (Exception e) {
				if (!Info.devMode) {
					Info.devMode = true;
					CreateXML.createXML(doc, "FloorRunInfoWithCards");
					Info.devMode = false;
				}
			}
		} else if (event_type.equals("19")) {
			Process.info.event_type = "获得道具";
			if ((boolean) xpath.evaluate(
					"count(//explore/special_item/before_count)>0", doc,
					XPathConstants.BOOLEAN))
				Process.info.gather = Integer.parseInt(xpath.evaluate(
						"//explore/special_item/before_count", doc));
			if ((boolean) xpath.evaluate(
					"count(//explore/special_item/item_id)>0", doc,
					XPathConstants.BOOLEAN))
				Process.info.gatherID = Integer.parseInt(xpath.evaluate(
						"//explore/special_item/item_id", doc));
		} else {
			Process.info.event_type = event_type;
		}
		Process.info.progress = Integer.parseInt(xpath.evaluate(
				"//explore/progress", doc));
		Process.info.lvup = Integer.parseInt(xpath.evaluate("//explore/lvup",
				doc));
		Process.info.getExp = Integer.parseInt(xpath.evaluate(
				"//explore/get_exp", doc));
		Process.info.nextExp = Integer.parseInt(xpath.evaluate(
				"//explore/next_exp", doc));
		Process.info.runGold = Integer.parseInt(xpath.evaluate(
				"//explore/gold", doc));
		try {
			if (Process.info.progress == 100) {
				Process.info.nextFloorId = xpath.evaluate(
						"//explore/next_floor/floor_info/id", doc);
				Process.info.nextFloorCost = Integer.parseInt(xpath.evaluate(
						"//explore/next_floor/floor_info/cost", doc));
			}
		} catch (Exception e) {
			// 出异常则表示地图clear
			Process.info.areaClear = 1;
		}
	}

}
