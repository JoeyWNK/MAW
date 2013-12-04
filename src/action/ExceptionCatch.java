package action;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import start.Go;
import start.Info;

public class ExceptionCatch {

	public static boolean catchException(Document doc) throws Exception {

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		try {
			String errorCode = xpath.evaluate("/response/header/error/code",
					doc);
			String errorMessage = xpath.evaluate(
					"/response/header/error/message", doc);
			if (!errorCode.equals("0")) {
				if (errorCode.equals("9000")) {
					Info.timepoverty = 5;
					throw new Exception(errorCode + "："
							+ errorMessage.replace("\n", "，"));
				}else if (errorCode.equals("8000") && errorMessage.replace("\n", "，").contains("卡片")) {
					throw new Exception(errorCode + "："
							+ errorMessage.replace("\n", "，"));
				}else if (errorCode.equals("1010") || errorMessage.replace("\n", "，").contains("消灭")) {
					if (Info.timepoverty > 1)
					Info.timepoverty = Info.timepoverty / 2 ;
					throw new Exception(errorCode + "："
							+ errorMessage.replace("\n", "，"));
				}
				Go.log("错误code：" + errorCode + ",错误内容："
						+ errorMessage.replace("\n", "，"));
				return true;
			}
		} catch (XPathExpressionException e) {
			Go.log("解析xml错误");
			return true;
		}
		return false;
	}

}
