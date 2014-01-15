package info;

import java.io.File;

public class CheckDeck {

	public static void run() {
		File file = new File("deck_info");
		if (file.exists()) {
			System.out.println("感谢/duoy云/xy的指导，现提供测试版的自动配卡，谢谢keyminori的测试及帮助");
			return;
		}
		System.out.println("请运行cal.exe来生成 deck_info 和  data.csv，否则无法自动配卡，只能狼娘");
	}

}
