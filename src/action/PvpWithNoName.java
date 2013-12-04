package action;

import info.GetBattleResult;
import info.GetUserInfo;
import info.NoNameInfo;
import java.util.ArrayList;
import net.Process;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;

import start.Info;

public class PvpWithNoName {

	// 获取无名亚瑟
	private static final String URL_GET_PVP = Info.LoginServer
			+ "/connect/app/battle/battle?cyt=1";
//	private static final String URL_Fresh_PVP = Info.LoginServer + "/connect/app/battle/battle_userlist?cyt=1";
	// 返回结果
	private static byte[] result;

	public static boolean run(NoNameInfo nameInfo) throws Exception {
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
		al.add(new BasicNameValuePair("battle_type", "0"));
		al.add(new BasicNameValuePair("event_id", Info.PVPEvent));
		al.add(new BasicNameValuePair("user_id", nameInfo.userId));
//		al.add(new BasicNameValuePair("parts_id", "0"));
//		al.add(new BasicNameValuePair("lake_id", "0"));
//		ArrayList<NameValuePair> a2 = new ArrayList<NameValuePair>();
//		a2.add(new BasicNameValuePair("event_id", Info.PVPEvent));
//		a2.add(new BasicNameValuePair("move", "1"));
		try {
//			Process.connect.connectToServer(URL_Fresh_PVP, a2);
			result = Process.connect.connectToServer(URL_GET_PVP, al);
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

			if (ExceptionCatch.catchException(doc)) {
				return false;
			}

			GetUserInfo.getUserInfo(doc, false);
			GetBattleResult.getBattleResult(doc);
		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}

}
