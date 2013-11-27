package info;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import net.Process;

import org.w3c.dom.Document;

public class FloorRunInfo {
	
	public static void floorRunInfo(Document doc) throws Exception{
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		
		String event_type = xpath.evaluate("//explore/event_type", doc);
		if(event_type.equals("1")){
			Process.info.event_type = "遇到妖精";
		}else if(event_type.equals("2")){
			Process.info.event_type = "遇到小伙伴";
		}else if(event_type.equals("3")){
			Process.info.event_type = "获得卡片";
		}else if(event_type.equals("5")){
			Process.info.event_type = "地图踏破";
		}else if(event_type.equals("12")){
			Process.info.event_type = "AP回复";
		}else if(event_type.equals("13")){
			Process.info.event_type = "BC回复";
		}else if(event_type.equals("15")){
			Process.info.event_type = "获得卡片";
		}else if(event_type.equals("19")){
			Process.info.event_type = "获得道具";
			try{
			Process.info.gather = Integer.parseInt(xpath.evaluate(
					"//special_item/after_count", doc));
			}catch(Exception e){
				
			}
		}else{
			Process.info.event_type = event_type;
		}
		Process.info.progress = Integer.parseInt(xpath.evaluate("//explore/progress", doc));
		Process.info.lvup = Integer.parseInt(xpath.evaluate("//explore/lvup", doc));
		Process.info.getExp = Integer.parseInt(xpath.evaluate("//explore/get_exp", doc));
		Process.info.runGold = Integer.parseInt(xpath.evaluate("//explore/gold", doc));
		try{
			if(Process.info.progress == 100){
				Process.info.nextFloorId = xpath.evaluate("//explore/next_floor/floor_info/id", doc);
				Process.info.nextFloorCost = Integer.parseInt(xpath.evaluate("//explore/next_floor/floor_info/cost", doc));
			}
		}catch(Exception e){
			//出异常则表示地图clear
			Process.info.areaClear = 1;
		}
	}

}
