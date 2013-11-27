package action;

import info.FloorInfo;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.Process;
import org.apache.http.NameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import start.Info;

public class GetAreaInfo {

	// 地图url
	private static final String URL_AREA_INFO = Info.LoginServer
			+ "/connect/app/exploration/area?cyt=1";

	// 返回结果
	private static byte[] result;

	public static boolean run(boolean isClear) throws Exception {
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
		try {
			result = Process.connect.connectToServer(URL_AREA_INFO, al);
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
			NodeList list = (NodeList) xpath.evaluate(
					"//exploration_area/area_info_list/area_info", doc,
					XPathConstants.NODESET);
			FloorInfo floorInfo = null;
			List<FloorInfo> floorInfos = new ArrayList<FloorInfo>();
			if (list.getLength() > 0) {
				for (int i = 0; i < list.getLength(); i++) {
					Node f = list.item(i).getFirstChild();
					floorInfo = new FloorInfo();
					do {
						if (f.getNodeName().equals("id")) {
							floorInfo.id = f.getFirstChild().getNodeValue();
						} else if (f.getNodeName().equals("name")) {
							floorInfo.name = f.getFirstChild().getNodeValue();
						} else if (f.getNodeName().equals("prog_area")) {
							floorInfo.prog_area = f.getFirstChild()
									.getNodeValue();
						} else if (f.getNodeName().equals("area_type")) {
							floorInfo.area_type = f.getFirstChild()
									.getNodeValue();
						} else if (f.getNodeName().equals("x")) {
							floorInfo.x = f.getFirstChild().getNodeValue();
						} else if (f.getNodeName().equals("y")) {
							floorInfo.y = f.getFirstChild().getNodeValue();
						}
						f = f.getNextSibling();
					} while (f != null);

					if (Info.whatMap != 0) {
						if (floorInfo.area_type.equals(Info.runFactor)) {
							floorInfos.add(floorInfo);
						}
					} else {
						if (isClear) {
							if (floorInfo.area_type.equals(Info.runFactor)) {
								floorInfos.add(floorInfo);
							}
						} else {
							if (floorInfo.area_type.equals(Info.runFactor)
									&& !floorInfo.prog_area.equals("100")) {
								floorInfos.add(floorInfo);
							}
						}
					}
				}
			} else {
				return false;
			}
			Process.info.floorInfos = floorInfos;
			Process.info.areaClear = 0;
		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}

}
