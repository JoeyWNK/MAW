package start;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class GUI extends JFrame {

	public GUI() {
		JOptionPane.showMessageDialog(null, "未选择Config.xml文件", "警告",
				JOptionPane.WARNING_MESSAGE, null);
	}

	public void run() {
		String previous = "";
		while (true){
			if (!previous.equals(Info.errorPos)){
				previous = Info.errorPos;
				
			}
		}
	}

}
