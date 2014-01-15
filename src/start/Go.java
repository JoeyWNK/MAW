package start;

import info.TimeManager;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import net.Crypto;
import net.Process;

@SuppressWarnings("unused")
public class Go {
	static SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");// 设置日期格式
	static double totaltime = 0;
	static boolean waited = false;
	static boolean neterror = false;

	public static void main(String[] args) {
		System.out.println("---MAWalker---");
		try {
			System.out.println("正在启动");
			System.out.println("加载配置文件……");
			if (args.length == 1)
				GetConfig.readConfig(args[0]);
			else {
				GUI gui = new GUI();
				gui.run();
			}
			System.out.println("加载完成");
			System.out.println("启动程序……");
			Process proc = new Process();
			System.out.println("启动完成");
			// System.out.println(Crypto.DecryptBase64NoKey2Str("NzgOGTK08BvkZN5q8XvG6Q"));
			System.out.println("开始行动");
			while (true) {
				proc.start();
			}
		} catch (Exception e) {
			System.out.print(e.toString());
			log("文件解析错误");
		}
	}

	public static void log(String msg) {
		if (msg == null || msg.isEmpty())
			return;
		if (msg.contains(".com") || msg.contains("connect")) {
			if (!neterror)
				neterror = true;
			else
				return;
			msg = "连接错误";
		} else if (msg.contains("time out")) {
			if (!neterror)
				neterror = true;
			else
				return;
			msg = "连接超时";
		} else if (msg.contains("服务器未响应")) {
			if (!neterror)
				neterror = true;
			else
				return;
			msg = "服务器未响应:" + Info.errorPos;
		} else if (neterror) {
			neterror = false;
			log("连接恢复");
		}
		if (Info.log) {
			try {
				if (!Info.simplelog || msg.contains("HP")
						|| (msg.contains("BC") && msg.contains("AP"))) {
					FileWriter fileWriter = new FileWriter("log.log", true);
					fileWriter.write(df.format(new Date()) + "> ");
					fileWriter.write(msg);
					fileWriter.write("\r\n");
					fileWriter.close();
				}
			} catch (IOException e) {
				System.out.println("无法生成记录");
				Info.log = false;
			}

		}
		if (!neterror)
			TimeManager.Check(Integer.parseInt(df.format(new Date()).substring(
					0, 2)));
		if (!msg.contains("\n")) {
			if (waited) {
				if (totaltime > 60)
					System.out.println("已等待" + (int) (totaltime / 60) + "分钟"
							+ (int) (totaltime % 60) + "秒");
				else
					System.out.println("已等待" + (int) totaltime + "秒");
				totaltime = 0;
				waited = false;
			}
			System.out.print(df.format(new Date()) + "> ");// new
															// Date()为获取当前系统时间
			System.out.println(msg);
			return;
		}
		for (String l : msg.split("\n")) {
			if (waited) {
				if (totaltime > 60)
					System.out.println("已等待" + (int) (totaltime / 60) + "分钟"
							+ (int) (totaltime % 60) + "秒");
				else
					System.out.println("已等待" + (int) totaltime + "秒");
				totaltime = 0;
				waited = false;
			}
			System.out.print(df.format(new Date()) + "> ");
			System.out.println(l);
		}
	}

	public static void log(double time, boolean show) {
		if (neterror) {
			neterror = false;
			log("连接恢复");
		}
		if (show)
			System.out.print((int) time + " ");
		waited = true;
		totaltime = totaltime + time;
		TimeManager.Check(Integer.parseInt(df.format(new Date())
				.substring(0, 2)));
	}

}
