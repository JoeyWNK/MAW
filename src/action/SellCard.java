package action;

import info.CreateXML;
import info.GetUserInfo;
import info.UserCardsInfo;

import java.util.ArrayList;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import net.Process;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;

import start.Go;
import start.Info;

import action.ActionRegistry.Action;

public class SellCard {
	public static final Action Name = Action.SELL_CARD;
	
	private static final String URL_SELL_CARD = Info.LoginServer + "/connect/app/trunk/sell?cyt=1";
	private static byte[] response;
	
	public static boolean run() throws Exception {
		if (!Info.autoSellCards) 
			return false;
		System.out.print("读取卡片");
		ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
		String SellList = "";
		int number = 0;
		for (int i = 0; i < Info.userCardsInfos.size(); i++){
			UserCardsInfo card = Info.userCardsInfos.get(i);
			if (					
					(
						(
							(Info.smartSell 
								&& (
									card.lv < 5 
									&& (card.sale_price > 60 && card.sale_price < 200) ||(card.sale_price == 600)
									&& (card.hp > 5 || card.atk > 5)
								)								
							)
							|| 
							(Info.CanBeSold.contains(card.master_card_id)
								&& card.lv < 5 									
							)
						)
					)&& !card.holography
					
					)
			{
				if (SellList == "" || SellList.isEmpty() || SellList == null)
					SellList = Integer.toString(card.serialId);
				else
				SellList += "," + card.serialId;
				System.out.print(".");
				number++;
				}
			}
		post.add(new BasicNameValuePair("serial_id", SellList));
		System.out.println("读取完成，总计 " + number + " 张");
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
			GetUserInfo.getUserInfo(doc, true);
			CreateXML.createXML(doc, "SellCards");
		} catch (Exception ex) {
/**			ErrorData.currentDataType = ErrorData.DataType.bytes;
			ErrorData.currentErrorType = ErrorData.ErrorType.SellCardDataError;
			ErrorData.bytes = response;
**/			throw ex;
		}
		
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		String errorMessage = xpath.evaluate("/response/header/error/message", doc);
		try {
			if (!xpath.evaluate("/response/header/error/code", doc).equals("1010")) {
/**				ErrorData.currentErrorType = ErrorData.ErrorType.SellCardResponse;
				ErrorData.currentDataType = ErrorData.DataType.text;
				
**/			
				Go.log(errorMessage);
			return false;
			} else {
//				ErrorData.text = xpath.evaluate("/response/header/error/message", doc);
//				Process.info.toSell = "";
				Go.log(errorMessage);
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
