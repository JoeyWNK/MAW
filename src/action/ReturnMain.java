package action;

import info.GetUserInfo;
import java.util.ArrayList;
import net.Process;
import org.apache.http.NameValuePair;
import org.w3c.dom.Document;

import start.Info;

public class ReturnMain {
	// 回城url
	private static final String URL_LOGIN = Info.LoginServer
			+ "/connect/app/mainmenu?cyt=1";

	// 返回结果
	private static byte[] result;

	public static boolean run() throws Exception {
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
		try {
			result = Process.connect.connectToServer(URL_LOGIN, al);
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

		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}
}
