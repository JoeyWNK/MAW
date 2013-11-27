package action;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import net.Process;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;

import start.Info;

import action.ActionRegistry.Action;

public class SellCard {
	public static final Action Name = Action.SELL_CARD;
	
	private static final String URL_SELL_CARD = Info.LoginServer + "trunk/sell";
	private static byte[] response;
	
	public static boolean run() throws Exception {
		if (Info.autoSellCards) return false;
		ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
		String SellList = "";
		for (int i = 0; i < Info.userCardsInfos.size(); i++){
			if (
					(
							(Info.smartSell 
					&& Info.userCardsInfos.get(i).lv < 5 
					&& (Info.userCardsInfos.get(i).sale_price > 60 && Info.userCardsInfos.get(i).sale_price < 200 /**|| Info.userCardsInfos.get(i).sale_price == 600 **/) 
					&& (Info.userCardsInfos.get(i).hp > 5 || Info.userCardsInfos.get(i).atk > 5)
					)
					|| Info.userCardsInfos.get(i).master_card_id != 0
					)
					&& !Info.userCardsInfos.get(i).holography
					)
				SellList += Integer.toString(Info.userCardsInfos.get(i).serialId);
			}
		post.add(new BasicNameValuePair("serial_id", SellList));
		try {
			response = Process.connect.connectToServer(URL_SELL_CARD, post);
		} catch (Exception ex) {
/**			ErrorData.currentDataType = ErrorData.DataType.text;
			ErrorData.currentErrorType = ErrorData.ErrorType.ConnectionError;
			ErrorData.text = ex.getLocalizedMessage(); 
**/			throw ex;
		}

		Document doc;
		try {
			doc = Process.ParseXMLBytes(response);
		} catch (Exception ex) {
/**			ErrorData.currentDataType = ErrorData.DataType.bytes;
			ErrorData.currentErrorType = ErrorData.ErrorType.SellCardDataError;
			ErrorData.bytes = response;
**/			throw ex;
		}
		
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		
		try {
			if (!xpath.evaluate("/response/header/error/code", doc).equals("1010")) {
/**				ErrorData.currentErrorType = ErrorData.ErrorType.SellCardResponse;
				ErrorData.currentDataType = ErrorData.DataType.text;
				ErrorData.text = xpath.evaluate("/response/header/error/message", doc);
**/				return false;
			} else {
//				ErrorData.text = xpath.evaluate("/response/header/error/message", doc);
//				Process.info.toSell = "";
				return true;
			}
			
		} catch (Exception ex) {
/**			if (ErrorData.currentErrorType != ErrorData.ErrorType.none) throw ex;
			ErrorData.currentDataType = ErrorData.DataType.bytes;
			ErrorData.currentErrorType = ErrorData.ErrorType.SellCardDataError;
			ErrorData.bytes = response;
**/			throw ex;
		}
		

	}
	
}
