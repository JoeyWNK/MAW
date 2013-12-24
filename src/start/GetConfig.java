package start;

import info.CardConfigInfo;
import info.MAPConfigInfo;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.Process;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GetConfig {

	private static final String SERVER_PART1 = "http://game";
	private static final String SERVER_PART2 = "-CBT.ma.sdo.com:10001";
	@SuppressWarnings("unused")
	private static String PATH = "";
	private static XPath xpath;
	private static Document doc;
	public static void readConfig(String path) throws Exception {
		PATH = path;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		try {
			is = new FileInputStream(path);
			byte[] b = new byte[0x2800];
			int n;
			while ((n = is.read(b)) != -1)
				baos.write(b, 0, n);
		} catch (Exception ex) {
			throw ex;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception ex) {
				throw ex;
			}
		}

		doc = Process.ParseXMLBytes(baos.toByteArray());

		try {
			XPathFactory factory = XPathFactory.newInstance();
			xpath = factory.newXPath();
			
			System.out.print("读取登录信息");
			Info.LoginId = xpath.evaluate("/config/username", doc).trim();
			Info.LoginPw = xpath.evaluate("/config/password", doc).trim();
			Info.userAgent = xpath.evaluate("/config/user_agent", doc).trim();
			String server = xpath.evaluate("/config/server", doc).trim();
			Info.LoginServer = SERVER_PART1 + server + SERVER_PART2;
			System.out.println("[OK]");
			
			System.out.print("读取扫描参数");
			Info.sleepTime = Integer.parseInt(xpath.evaluate("/config/sleep",
					doc).trim());
			Info.timepoverty = Integer.parseInt(xpath.evaluate("/config/poverty",
					doc).trim());
			Info.timepovertyMAX = Integer.parseInt(xpath.evaluate("/config/povertyMAX",
					doc).trim())/2;
			System.out.println("[OK]");
			
			System.out.print("读取日志设定");
			Info.log = xpath.evaluate("/config/log",
					doc).trim() != "0";
			Info.simplelog = xpath.evaluate("/config/simple",
					doc).trim() != "0";
			System.out.println("[OK]");
			
			System.out.print("读取行动设定");
			Info.autoSellCards = xpath.evaluate("/config/SellCards",
					doc).trim() == "1";
			Info.smartSell= xpath.evaluate("/config/smartSell",
					doc).trim() == "1";
			Info.isPVP = xpath.evaluate("/config/option/is_pvp", doc).trim();
			Info.PVPEvent = xpath.evaluate("/config/option/pvp_eventid", doc).trim();
			Info.isRun = xpath.evaluate("/config/option/is_run", doc).trim();
			Info.dayFirst = xpath.evaluate("/config/option/day_first", doc)
					.trim();
			Info.whatMap = Integer.parseInt(xpath.evaluate(
					"/config/option/what_map", doc).trim());
			Info.isBattlePrivateFariy = xpath.evaluate(
					"/config/option/is_battle_private_fairy", doc).trim();
			Info.waitTime = Integer.parseInt(xpath.evaluate(
					"/config/wait_time", doc).trim());
			Info.stopRunWhenBcMore = Integer.parseInt(xpath.evaluate(
					"/config/stop_run_when_bc_more", doc).trim());
			Info.stopRunWhenApLess = Integer.parseInt(xpath.evaluate(
					"/config/stop_run_when_ap_less", doc).trim());
			Info.autoPoint = xpath.evaluate("/config/auto_point", doc).trim();
			Info.hasPrivateFairyStopRun = Integer.parseInt(xpath.evaluate(
					"/config/option/has_private_fairy_stop_run", doc).trim());
			Info.hasPrivateFairyStopRunOriginal = Info.hasPrivateFairyStopRun;
			String runFactor = xpath.evaluate("/config/option/run_factor", doc)
					.trim();
			if (runFactor.equals("1")) {
				Info.runFactor = "0";
			} else {
				Info.runFactor = "1";
			}
			NodeList MAPlist = (NodeList) xpath.evaluate(
					"//config/mapsettings/mapsetting", doc, XPathConstants.NODESET);
			List<MAPConfigInfo> MAPConfigInfos = new ArrayList<MAPConfigInfo>();
			MAPConfigInfo MAPConfigInfo = null;
			if (MAPlist.getLength() > 0) {
				for (int i = 0; i < MAPlist.getLength(); i++) {
					Node f = MAPlist.item(i).getFirstChild();
					MAPConfigInfo = new MAPConfigInfo();
					do {
						try {
							if (f.getNodeName().equals("day")) {
						MAPConfigInfo.day = Integer.parseInt(f
								.getFirstChild().getNodeValue().trim());
						} else if (f.getNodeName().equals("hour")) {
							if (xpath.evaluate("/config/mapsettings/hastimelimit", doc).contains("true")){
								MAPConfigInfo.maptimelimitDown = Integer.parseInt(f
										.getFirstChild().getNodeValue().trim()
										.split("-")[0]);
								MAPConfigInfo.maptimelimitUp = Integer.parseInt(f
										.getFirstChild().getNodeValue().trim()
										.split("-")[1]);
							}else MAPConfigInfo.maptimelimitUp = -1;
							
						} else if (f.getNodeName().equals("daily")) {
							MAPConfigInfo.daily = f.getFirstChild().getNodeValue().trim();							
						} else if (f.getNodeName().equals("fixed")) {
							MAPConfigInfo.fixed = f.getFirstChild().getNodeValue().contains("ture");							
						}
						f = f.getNextSibling();
						} catch (Exception ex){
							if (f.getNodeName().equals("hour")) {
								if (f.getFirstChild().getNodeValue().trim() == null){
									MAPConfigInfo.maptimelimitUp = -1;
									MAPConfigInfo.maptimelimitDown = -1;
								}
							}
							if (f.getNodeName().equals("daily")) {
								if (f.getFirstChild().getNodeValue().trim() == null){
									MAPConfigInfo.daily = Info.dayFirst;
								}
							}							
						}
					} while (f != null);
					MAPConfigInfos.add(MAPConfigInfo);					
				}
				Info.MAPConfigInfos = MAPConfigInfos;
			}
			System.out.println("[OK]");
			System.out.print("读取战斗卡组");
			String pvpCard = xpath.evaluate("/config/card/pvp/pvp_card", doc)
					.trim();
			if (!pvpCard.equals("")) {
				Info.pvpCard = pvpCard;
				Info.pvpLr = pvpCard.split(",")[0];
			}
			String lickCard = xpath.evaluate("/config/card/lick_fairy/card",
					doc).trim();
			if (!lickCard.equals("")) {
				Info.wolf = lickCard;
				Info.wolfLr = lickCard;
				Info.lickCost = Integer.parseInt(xpath.evaluate(
						"/config/card/lick_fairy/cost", doc).trim());
			}
			String battleBoss = xpath.evaluate("/config/card/battle_area_boss",
					doc).trim();
			if (!battleBoss.equals("")) {
				Info.battleBoss = battleBoss;
				Info.battleBossLr = battleBoss.split(",")[0];
			}
			String whenBcMoreThan = xpath.evaluate(
					"/config/card/pvp/pvp_when_bc_more_than", doc).trim();
			if (whenBcMoreThan.equals("")) {
				Info.whenBcMoreThan = 30;
			} else {
				Info.whenBcMoreThan = Integer.parseInt(whenBcMoreThan);
			}

			NodeList list = (NodeList) xpath.evaluate(
					"//config/card/battle_fairy", doc, XPathConstants.NODESET);
			List<CardConfigInfo> cardConfigInfos = new ArrayList<CardConfigInfo>();
			CardConfigInfo cardConfigInfo = null;
			if (list.getLength() > 0) {
				for (int i = 0; i < list.getLength(); i++) {
					Node f = list.item(i).getFirstChild();
					cardConfigInfo = new CardConfigInfo();
					do {
						if (f.getNodeName().endsWith("wake")) {
							cardConfigInfo.wake = Integer.parseInt(f
									.getFirstChild().getNodeValue().trim());
						} else if (f.getNodeName().equals("fairy_lv")) {
							cardConfigInfo.lvMin = Integer.parseInt(f
									.getFirstChild().getNodeValue().trim()
									.split("-")[0]);
							cardConfigInfo.lvMax = Integer.parseInt(f
									.getFirstChild().getNodeValue().trim()
									.split("-")[1]);
						} else if (f.getNodeName().equals("fairy_hp")) {
							cardConfigInfo.hp = Double.parseDouble(f
									.getFirstChild().getNodeValue().trim());
						} else if (f.getNodeName().equals("battle_card")) {
							cardConfigInfo.cardItem = f.getFirstChild()
									.getNodeValue();
							cardConfigInfo.cardLr = f.getFirstChild()
									.getNodeValue().trim().split(",")[0];
						} else if (f.getNodeName().equals("battle_cost")) {
							cardConfigInfo.cardCost = Integer.parseInt(f
									.getFirstChild().getNodeValue().trim());
						}
						f = f.getNextSibling();
					} while (f != null);
					cardConfigInfos.add(cardConfigInfo);
				}
			}
			Info.cardConfigInfos = cardConfigInfos;
			System.out.println("[OK]");
			
			System.out.print("读取售卡设定");
			NodeList idl = (NodeList)xpath.evaluate("/config/sell_card/id", doc, XPathConstants.NODESET);
			Info.CanBeSold = new ArrayList<String>();
			for (int i = 0; i < idl.getLength(); i++) {
				Node idx = idl.item(i);
				try {
					Info.CanBeSold.add(idx.getFirstChild().getNodeValue());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
			}
			System.out.println("[OK]");
		} catch (Exception e) {

		}
	}

	public static void saveConfig(String userAgent, int i) {
		try{
			switch(i){
			
			case 1030:
				
				}
			}catch(Exception e){
				Go.log("写入失败");
			}
		
	}

}
