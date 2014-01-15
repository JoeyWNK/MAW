package action;

import info.FloorInfo;
import java.util.ArrayList;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import net.Process;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;

import start.Info;

public class GetFloorInfo {

	// 地图楼层url
	private static final String URL_AREA_FLOOR = Info.LoginServer
			+ "/connect/app/exploration/floor?cyt=1";

	// 返回结果
	private static byte[] result;

	public static boolean run(FloorInfo floorInfo, boolean isClear)
			throws Exception {
		Info.errorPos = "GetFloorInfo";
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
		al.add(new BasicNameValuePair("area_id", floorInfo.id));
		try {
			result = Process.connect.connectToServer(URL_AREA_FLOOR, al);
		} catch (Exception ex) {
			throw ex;
		}
		try {
			doc = Process.ParseXMLBytes(result);

		} catch (Exception ex) {
			throw ex;
		}
		try {
			return parse(doc, isClear);
		} catch (Exception ex) {
			throw ex;
		}
	}

	private static boolean parse(Document doc, boolean isClear)
			throws Exception {
		try {

			if (ExceptionCatch.catchException(doc)) {
				return false;
			}

			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			if (isClear) {
				Process.info.floorId = xpath.evaluate(
						"//exploration_floor/floor_info_list/floor_info/id",
						doc);
				Process.info.floorCost = Integer.parseInt(xpath.evaluate(
						"//exploration_floor/floor_info_list/floor_info/cost",
						doc));
			} else {
				Process.info.floorId = xpath
						.evaluate(
								"//exploration_floor/floor_info_list/floor_info[progress!=100]/id",
								doc);
				Process.info.floorCost = Integer
						.parseInt(xpath
								.evaluate(
										"//exploration_floor/floor_info_list/floor_info[progress!=100]/cost",
										doc));
				String bossId = xpath
						.evaluate(
								"//exploration_floor/floor_info_list/floor_info[progress!=100]/boss_id",
								doc);
				if (null != bossId && !bossId.equals("")) {
					Process.info.bossId = Integer.parseInt(bossId);
				} else {
					Process.info.bossId = 0;
				}
			}

		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}

}
