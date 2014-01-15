package action;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.Connect;

import org.w3c.dom.Document;

import start.Go;
import start.Info;

public class ExceptionCatch {

	public static boolean catchException(Document doc) throws Exception {
		Info.errorPos = "catchException";
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
				} else if (errorCode.equals("8000")) {
					if (errorMessage.replace("\n", "，").contains("成功")) {
						Go.log("成功领取\n");
						return false;
					}
					if (errorMessage.replace("\n", "，").contains("卡片")) {
						throw new Exception(errorCode + "："
								+ errorMessage.replace("\n", "，"));
					}
					if (errorMessage.replace("\n", "，").contains("失败")) {
						throw new Exception(errorCode + "："
								+ errorMessage.replace("\n", "，"));
					}
				} else if (errorCode.equals("1010")
						|| errorMessage.replace("\n", "，").contains("消灭")) {
					if (Info.timepoverty > 1)
						Info.timepoverty = Info.timepoverty / 2;
					throw new Exception(errorCode + "："
							+ errorMessage.replace("\n", "，"));
				} else if (errorCode.equals("1030")
						|| errorMessage.replace("\n", "，").contains("更新")) {
					if (!net.Process.update)
						Go.log("提示更新，尝试自动升级版本号\n");
					Info.userAgent = (Integer.parseInt(Info.userAgent) + 1)
							+ "";
					System.out.print(Info.userAgent + " ");
					net.Process.update = true;
					net.Process.connect = new Connect();
					throw new Exception(errorCode + "："
							+ errorMessage.replace("\n", "，"));
				} else if (errorCode.equals("1020")
						|| errorMessage.replace("\n", "，").contains("维护")) {
					Go.log("等待官网更新");
					Thread.sleep(1000 * (long) (10 * 60 + Math.random() * 20));
					throw new Exception(errorCode + "："
							+ errorMessage.replace("\n", "，"));
				}
				Go.log("错误code：" + errorCode + ",错误内容："
						+ errorMessage.replace("\n", "，"));
				return true;
			}
			if (errorMessage.contains("卡")) {
				net.Process.info.events.push(Info.EventType.cardFull);
				return true;
			}
		} catch (XPathExpressionException e) {
			Go.log("解析xml错误");
			return true;
		}
		return false;
	}

}
