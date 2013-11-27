package start;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import net.Crypto;
import net.Process;

@SuppressWarnings("unused")
public class Go {
	static double totaltime = 0;
	static boolean waited = false;
	static boolean neterror = false;
	public static void main(String[] args) {
		try {
			GetConfig.readConfig(args[0]);
			Process proc = new Process();
			// System.out.println(Crypto.DecryptBase64NoKey2Str("NzgOGTK08BvkZN5q8XvG6Q"));
			while (true) {
				proc.start();
			}
		} catch (Exception e) {
			log("文件解析错误");
		}
	}

	public static void log(String msg) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		if (msg == null || msg.isEmpty()) 
			return;
		if (msg.contains(".com")){
			if (!neterror)
				neterror = true;
			else return;
			msg = "连接错误";
		}
		else if(neterror){
			log("连接恢复");
			neterror = false;
			}
		if (Info.log){
				try {
					if (!Info.simplelog || msg.contains("HP") || msg.contains("BC")){
						FileWriter fileWriter=new FileWriter("log.log", true);
						fileWriter.write(df.format(new Date())+"> ");
						fileWriter.write(msg);
						fileWriter.write("\r\n");						
						fileWriter.close();
					}
				} catch (IOException e) {
					System.out.println("无法生成记录");
					Info.log = false;
				}
		
		}
		if (!msg.contains("\n")) {
			if (waited){
				System.out.println("已等待"+(int)totaltime+"秒");
				totaltime = 0;
				waited = false;
			}
			System.out.print(df.format(new Date())+"> ");// new Date()为获取当前系统时间
			System.out.println(msg);
			return;
		}
		for (String l : msg.split("\n")) {
			if (waited){
				System.out.println("已等待"+(int)totaltime+"秒");
				totaltime = 0;
				waited = false;
			}
			System.out.print(df.format(new Date()));
			System.out.println("> "+ l);
		}
	}
	public static void log(double time, boolean show){
		if(neterror){
			log("连接恢复");
			neterror = false;
		}
		if (show)
		System.out.print((int)time + " ");
		waited = true;
		totaltime = totaltime + time;
	}

}
