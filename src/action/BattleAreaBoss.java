package action;

import info.CreateXML;
import info.FloorInfo;

import java.util.ArrayList;

import net.Process;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;

import start.Info;

public class BattleAreaBoss {
	// 跑图url
	private static final String BATTLE_AREA_BOSS = Info.LoginServer
			+ "/connect/app/exploration/battle?cyt=1";

	// 返回结果
	private static byte[] result;

	public static boolean run(FloorInfo floorInfo) throws Exception {
		Info.errorPos = "BattleAreaBoss.run";
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
		al.add(new BasicNameValuePair("area_id", floorInfo.id));
		al.add(new BasicNameValuePair("floor_id", Process.info.floorId));
		try {
			result = Process.connect.connectToServer(BATTLE_AREA_BOSS, al);
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
		Info.errorPos = "BattleAreaBoss.parse";
		try {

			if (ExceptionCatch.catchException(doc)) {
				return false;
			}
			CreateXML.createXML(doc, "AreaBossInfo");

			Process.info.bossId = 0;
		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}
}
