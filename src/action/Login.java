package action;

import info.CreateXML;
import info.GetUserInfo;

import java.util.ArrayList;
import net.Process;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;

import start.Info;

public class Login {

	// 登陆url
	private static final String URL_LOGIN = Info.LoginServer
			+ "/connect/app/login?cyt=1";

	// 返回结果
	private static byte[] result;

	public static boolean run() throws Exception {
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
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
