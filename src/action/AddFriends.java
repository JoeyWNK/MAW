package action;

import info.CreateXML;
import info.UserInfo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.Process;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import start.Go;
import start.Info;

public class AddFriends {
	private static final String URL_FRIEND_NOTICE = Info.LoginServer
			+ "/connect/app/menu/friend_notice?cyt=1";
	private static final String URL_ADD_FRIEND = Info.LoginServer
			+ "/connect/app/friend/approve_friend?cyt=1";
	
	public static boolean run() throws Exception {
			Document doc;
			byte[] result;
			Go.log("现有好友邀请:" + Process.info.invitations + ",查看列表");
			ArrayList<NameValuePair> al = new ArrayList<NameValuePair>();
			al.add(new BasicNameValuePair("move","1"));
			try {
				result = Process.connect.connectToServer(URL_FRIEND_NOTICE, al);
				doc = Process.ParseXMLBytes(result);

				if (ExceptionCatch.catchException(doc)) {
					return false;
				}
				CreateXML.createXML(doc, "FriendNotice");
				prase(doc);
			} catch (Exception ex) {
				throw ex;
			}				
			try {
			}catch (Exception ex){
				throw ex;
			}
			return true;
			
		
	}

	private static void prase(Document doc) {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		try {
			NodeList list = (NodeList) xpath.evaluate(
					"/response/body/friend_notice/user_list/user", doc,
					XPathConstants.NODESET);
			if (list.getLength() > 0) {
				List<UserInfo> userlists = new ArrayList<UserInfo>();
				UserInfo user = new UserInfo();
				for (int i = 0; i < list.getLength(); ++i) {
					Node f = list.item(i).getFirstChild();
					user = new UserInfo();
					do {
						if (f.getNodeName().equals("id"))
							user.id = f.getFirstChild().getNodeValue();
						else if (f.getNodeName().equals("name"))
							user.name = f.getFirstChild().getNodeValue();
						else if (f.getNodeName().equals("cost"))
							user.cost = Integer.parseInt(f.getFirstChild()
									.getNodeValue());
						else if (f.getNodeName().equals("last_login"))
							user.last_login = f.getFirstChild().getNodeValue();
						else if (f.getNodeName().equals("town_level")) {
							user.level = Integer.parseInt(f.getFirstChild()
									.getNodeValue());
						} else if (f.getNodeName().equals("friends")){
							user.friendsNum = Integer.parseInt(f.getFirstChild()
									.getNodeValue());
						}else if (f.getNodeName().equals("friend_max")){
							user.friendsMax = Integer.parseInt(f.getFirstChild()
									.getNodeValue());
							}
						f = f.getNextSibling();
					} while (f != null);
					userlists.add(user);
				}
				for (int i = 0; i < userlists.size(); ++i) {
					if(userlists.get(i).last_login.equals("今天") && userlists.get(i).friendsNum < userlists.get(i).friendsMax){
						ArrayList<NameValuePair> al = new ArrayList<>();
						al.add(new BasicNameValuePair("dialog", "1"));
						al.add(new BasicNameValuePair("user_id",
								userlists.get(i).id));
						Go.log("添加好友:" +  userlists.get(i).name
								+ " Lv." + userlists.get(i).level
								+ " MaxBC:" + userlists.get(i).cost
								+ " 最后一次登录:" + userlists.get(i).last_login
								);
						try {
							byte[] result = Process.connect.connectToServer(
									URL_ADD_FRIEND, al);
							doc = Process.ParseXMLBytes(result);
							CreateXML.createXML(doc, "FriendAdd");
							ExceptionCatch.catchException(doc);
						} catch (Exception ex) {
							Go.log(ex.toString());
						}
					}
				}
				
				Process.info.invitations = 0;
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
}
