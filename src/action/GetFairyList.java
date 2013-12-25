package action;

import info.CreateXML;
import info.FairyInfo;
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

public class GetFairyList {

	// 获取妖精列表
	private static final String URL_FAIRY_LIST = Info.LoginServer
			+ "/connect/app/menu/fairyselect?cyt=1";

	// 返回结果
	private static byte[] result;

	public static boolean run() throws Exception {
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
		try {
			result = Process.connect.connectToServer(URL_FAIRY_LIST, al);
		} catch (Exception ex) {
			throw ex;
		}
		try {
			doc = Process.ParseXMLBytes(result);

		} catch (Exception ex) {
			throw ex;
		}
		try {
			return parse(doc);
		} catch (Exception ex) {
			throw ex;
		}
	}

	private static boolean parse(Document doc) throws Exception {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();

			if (ExceptionCatch.catchException(doc)) {
				return false;
			}
			CreateXML.createXML(doc, "Fairylist");
			NodeList list = (NodeList) xpath.evaluate(
					"//fairy_select/fairy_event[put_down=1]", doc,
					XPathConstants.NODESET);
			FairyInfo fairyInfo = null;
			List<FairyInfo> fairyInfos = new ArrayList<FairyInfo>();
			int run = 0;
			Process.info.hasPartyFairy = false;
			if (list.getLength() > 0) {
				for (int i = 0; i < list.getLength(); i++) {
					Node f = list.item(i).getFirstChild();
					fairyInfo = new FairyInfo();
					do {
						if (f.getNodeName().equals("user")) {
							Node f1 = f.getFirstChild();
							do {
								if (f1.getNodeName().equals("id")) {
									fairyInfo.userId = f1.getFirstChild()
											.getNodeValue();
								} else if (f1.getNodeName().equals("name")) {
									fairyInfo.userName = f1.getFirstChild()
											.getNodeValue();
								}
								f1 = f1.getNextSibling();
							} while (f1 != null);
						} else if (f.getNodeName().equals("fairy")) {
							Node f1 = f.getFirstChild();
							do {
								if (f1.getNodeName().equals("serial_id")) {
									fairyInfo.serialId = f1.getFirstChild()
											.getNodeValue();
								} else if (f1.getNodeName().equals("name")) {
									fairyInfo.name = f1.getFirstChild()
											.getNodeValue();
								} else if (f1.getNodeName().equals("lv")) {
									fairyInfo.lv = f1.getFirstChild()
											.getNodeValue();
								} else if (f1.getNodeName().equals("race_type")) {
									fairyInfo.race_type = f1.getFirstChild().getNodeValue();
				                }
								f1 = f1.getNextSibling();
							} while (f1 != null);
						}
						f = f.getNextSibling();
					} while (f != null);
					if (fairyInfo.userId.equals(Process.info.userId) && !fairyInfo.race_type.equals("12")) {
						run++;
						if (Info.isBattlePrivateFariy.equals("1")) {
							fairyInfos.add(fairyInfo);
						}
					} else {
						fairyInfos.add(fairyInfo);
					}
				}
			} else {
				if (Info.isRun.equals("1")) {
					Info.canRun = 1;
				}
				return false;
			}
			// 是否开启了有妖停跑
			if (Info.hasPrivateFairyStopRun == 1 && Info.isRun.equals("1")) {
				// 大于0表示身上有妖精，停止跑图
				if (run > 0) {
					Info.canRun = 0;
				} else {
					Info.canRun = 1;
				}
			}
			Process.info.fairyInfos = fairyInfos;
			
			Process.info.fairyRewardCount = Integer.parseInt(xpath.evaluate("//fairy_select/remaining_rewards", doc));
		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}

}
