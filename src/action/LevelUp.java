package action;

import info.GetUserInfo;
import java.util.ArrayList;
import net.Process;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;

import start.Go;
import start.Info;

public class LevelUp {
	// 获取无名亚瑟
	public static final String URL_LEVEL_UP = Info.LoginServer
			+ "/connect/app/town/pointsetting?cyt=1";

	// 返回结果
	private static byte[] result;

	public static boolean run() throws Exception {
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
		if (Info.autoPoint.equals("ap")) {
			Go.log("分配剩余属性点到ap");
			al.add(new BasicNameValuePair("ap", "" + Process.info.freeApBcPoint));
			al.add(new BasicNameValuePair("bc", "0"));
		} else if (Info.autoPoint.equals("bc")){
			Go.log("分配剩余属性点到bc");
			al.add(new BasicNameValuePair("ap", "0"));
			al.add(new BasicNameValuePair("bc", "" + Process.info.freeApBcPoint));
		} else {
			Go.log("分配剩余属性点随机分配");
			int ap = (int) (Process.info.freeApBcPoint * Math.random());
			al.add(new BasicNameValuePair("ap", "" + ap));
			al.add(new BasicNameValuePair("bc", "" + (Process.info.freeApBcPoint - ap)));
		}
		try {
			result = Process.connect.connectToServer(URL_LEVEL_UP, al);
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
