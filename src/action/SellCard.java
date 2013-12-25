package action;

import info.CreateXML;
import info.GetUserInfo;
import info.UserCardsInfo;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
	private static boolean tried = false;
	private static final String URL_SELL_CARD = Info.LoginServer + "/connect/app/trunk/sell?cyt=1";
	private static byte[] response;
	static SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
	static FileWriter fileWriter = null;
	public static boolean run() throws Exception {		
		GetUserInfo.CardCheck(CreateXML.UserInfo);	
		if (!Info.autoSellCards) 
			return false;
		System.out.print("读取卡片");
		ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
		String SellList = "";
		int number = 0;
		int price = 0;
		try {
			if(Info.log){
				fileWriter=new FileWriter("CardSell.log", true);
				fileWriter.write(df.format(new Date())+"\r\n");
			}
		for (int i = 0; i < Process.info.userCardsInfos.size(); i++){
			UserCardsInfo card = Process.info.userCardsInfos.get(i);
			if (					
					(
						(
							(Info.smartSell && ( card.lv < 5 
									&& (card.sale_price > 60 && card.sale_price < 200) ||(card.sale_price == 600)
									&& (card.hp > 5 || card.atk > 5)
									)
								)
							||
							(Info.CanBeSold.contains(card.master_card_id + "")
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
				price =+ card.sale_price;
				if(Info.log)
					fileWriter.write(card.master_card_id +" " + card.sale_price + " " + card.lv +"\r\n");						
					
				}
			}
		if(Info.log)
			fileWriter.write("总计 " + number + " 张 " + price + "Gold\r\n");
			fileWriter.close();
		} catch(IOException e) {
			System.out.println("无法生成记录");
			Info.log = false;
		}
		post.add(new BasicNameValuePair("serial_id", SellList));
		if (number > 0){
			tried = false;
			System.out.println("读取完成，总计 " + number + " 张");
			}
		else{
			if (!tried){
			System.out.println("无卡可卖,尝试重新登录");
			Login.run();
			tried = true;
			if (run())
				return true;
			}
			System.out.println("无卡可卖");
			tried = false;
			return false;
			}
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
			GetUserInfo.getUserInfo(doc, false);
			CreateXML.UserInfo = doc;
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
