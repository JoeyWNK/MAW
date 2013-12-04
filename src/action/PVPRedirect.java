package action;

import java.util.ArrayList;
import net.Process;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;

import start.Info;

public class PVPRedirect {
	// 获取无名亚瑟
	private static final String URL_GET_REDIRECT = Info.LoginServer
			+ "/connect/app/battle/battle_userlist?cyt=1";

	// 返回结果
	private static byte[] result;

	public static boolean run() throws Exception {
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
		al.add(new BasicNameValuePair("event_id","48"));
		al.add(new BasicNameValuePair("move","1"));
		try {
			result = Process.connect.connectToServer(URL_GET_REDIRECT, al);
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

		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}
}
