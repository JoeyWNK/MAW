package action;

import info.FloorInfo;
import info.FloorRunInfo;
import info.GetUserInfo;

import java.util.ArrayList;

import net.Process;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;

import start.Info;

public class FloorRun {

	// 跑图url
	private static final String URL_AREA_FLOOR = Info.LoginServer
			+ "/connect/app/exploration/explore?cyt=1";

	// 返回结果
	private static byte[] result;

	public static boolean run(FloorInfo floorInfo) throws Exception {
		Document doc;
		ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
		al.add(new BasicNameValuePair("area_id", floorInfo.id));
		al.add(new BasicNameValuePair("auto_build", "1"));
		al.add(new BasicNameValuePair("floor_id", Process.info.floorId));
		try {
			result = Process.connect.connectToServer(URL_AREA_FLOOR, al);
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
			FloorRunInfo.floorRunInfo(doc);

		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}

}
