package info;

import java.io.FileWriter;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import net.Process;

public class GetBattleDetail {

	public static void run(Document doc) throws Exception {
		if (Process.info.CurrentDeck.equals("null"))
			return;
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		NodeList list = (NodeList) xpath.evaluate(
				"/response/body/battle_battle/battle_action_list", doc,
				XPathConstants.NODESET);
		String Detail = " ";
		boolean jump = false;
		if (list.getLength() != 0)
			for (int i = 0; i < list.getLength() && !jump; ++i) {
				Node f = list.item(i).getFirstChild();
				do {
					if (f.getNodeName().equals("turn")) {
						jump = true;
						break;
					} else if (f.getNodeName().equals("combo_name"))
						Detail += "Combo:" + f.getNodeValue();
					else if (f.getNodeName().equals("combo_type"))
						Detail += "ComboType:" + f.getNodeValue();
					else if (f.getNodeName().equals("combo_card_list_player"))
						Detail += "ComboByCards:" + f.getNodeValue();
					f = f.getNextSibling();
				} while (f != null);
			}
		Detail = "卡组:" + Process.info.CurrentDeck + Detail + "\r\n";
		FileWriter comboinfo = new FileWriter("comboinfo", true);
		comboinfo.write(Detail);
		comboinfo.close();
	}

}
