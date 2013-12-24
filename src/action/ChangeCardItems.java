package action;

import java.util.ArrayList;
import net.Process;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;

import start.Info;

public class ChangeCardItems {

	// 换卡组url
	private static String URL_CHANGE_CARD_ITEMS = Info.LoginServer
			+ "/connect/app/cardselect/savedeckcard?cyt=1";

	// 返回结果
	private static byte[] result;

	public static boolean run(String card, String lr) throws Exception {
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
		al.add(new BasicNameValuePair("deck_id", "1"));
		al.add(new BasicNameValuePair("C", card));
		al.add(new BasicNameValuePair("lr", lr));
		try {
			result = Process.connect.connectToServer(URL_CHANGE_CARD_ITEMS, al);
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
