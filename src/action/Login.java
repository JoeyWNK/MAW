package action;

import info.CreateXML;
import info.GetUserInfo;
import java.util.ArrayList;
import net.CryptoCn;
import net.Process;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import start.Info;

public class Login {
	private static final String URL_CHECK_INSPECTION = Info.LoginServer
			+ "/connect/app/check_inspection?cyt=1";
	private static final String URL_LOGIN = Info.LoginServer
			+ "/connect/app/login?cyt=1";
	private static byte[] result;

	public static boolean run() throws Exception {
		try {
			return run(true);
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static boolean run(boolean jump) throws Exception {
		Info.errorPos = "Login";
		if (!jump) {
			try {
				System.out.println(Process.connect.connectToServer(
						URL_CHECK_INSPECTION, new ArrayList<NameValuePair>())
						.toString());
			} catch (Exception ex) {
				throw ex;
			}
		}

		CryptoCn.set_dynamic_aes_key_json("");
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<>();
		al.add(new BasicNameValuePair("login_id", Info.LoginId));
		al.add(new BasicNameValuePair("password", Info.LoginPw));
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
			CreateXML.createXML(doc, "userInfo");

			if (ExceptionCatch.catchException(doc)) {
				return false;
			}

			GetUserInfo.getUserInfo(doc, true);
		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}
}