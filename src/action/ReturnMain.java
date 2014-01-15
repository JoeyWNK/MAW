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
		Info.errorPos = "ReturnMain";
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
		try {
			result = Process.connect.connectToServer(URL_LOGIN, al);
		} catch (Exception ex) {
			throw ex;
		}
		Info.errorPos += 1;
		try {
			doc = Process.ParseXMLBytes(result);

		} catch (Exception ex) {
			throw ex;
		}
		try {
			Info.errorPos += 2;
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
			Info.errorPos += 21;
			GetUserInfo.getUserInfo(doc, false);
			Info.errorPos += 22;
		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}
}
