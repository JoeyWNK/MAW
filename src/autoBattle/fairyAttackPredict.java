package autoBattle;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import info.FairyInfo;

public class fairyAttackPredict {

	/**
	 * @param args
	 */
	public static ArrayList<radio> Record = new ArrayList<>();

	public static int Predict(FairyInfo fairy) {
		int atk = 0;
		int lv = Integer.parseInt(fairy.lv);
		int hpMax = fairy.maxHp;
		ArrayList<Integer> pos = new ArrayList<>();
		for (int i = 0; i < Record.size(); i++) {
			if (Record.get(i).FairyName.equals(fairy.name)) {
				pos.add(i);
				if (Record.get(i).lv == lv) {
					if (Record.get(i).atk > 0)
						return Record.get(i).atk;
				} else if (Record.get(i).atk == 0)
					pos.remove(i);
			}
		}
		if (pos.size() <= 1) {
			try {
				if (URLEncoder.encode(fairy.name, "utf-8")
						.contains("%E7%9A%84")) {
					if ((hpMax - 965000) / lv > 39000)
						atk = 18116 + lv * 625;
					else
						atk = 10303 + lv * 355;
				} else {
					if ((hpMax - 15000) / lv > 8000) {
						if ((hpMax - 965000) / lv > 39000)
							atk = 18116 + lv * 625;
						else
							atk = 1987 + lv * 199;
					} else
						atk = 1260 + lv * 126;
				}
			} catch (UnsupportedEncodingException e) {

			}
		} else {
			if (Record.get(pos.get(0)).rate > -1)
				atk = (int) (Record.get(pos.get(0)).rate * lv + Record.get(pos
						.get(0)).base);
			else {
				double rate = (Record.get(pos.get(pos.size() - 1)).atk - Record
						.get(pos.get(0)).atk)
						/ Record.get(pos.get(pos.size() - 1)).lv
						- Record.get(pos.get(0)).lv;
				int base = (int) (Record.get(pos.get(0)).atk - rate
						* Record.get(pos.get(0)).lv);
				Record.get(pos.get(0)).rate = rate;
				Record.get(pos.get(0)).base = base;
				atk = (int) (Record.get(pos.get(0)).rate * lv + Record.get(pos
						.get(0)).base);
			}
		}
		if (lv > 50)
			atk = (int) (atk * (lv / 95 + 100) / 100);
		else
			atk = (int) (atk / 1.3);
		return atk;
	}

	public static void Record(FairyInfo fairy, int atk) {
		ArrayList<Integer> pos = new ArrayList<>();
		for (int i = 0; i < Record.size(); i++)
			if (Record.get(i).FairyName.equals(fairy.name))
				pos.add(i);
		if (pos.size() >= 2) {
			if (Integer.parseInt(fairy.lv) > Record
					.get(pos.get(pos.size() - 1)).lv) {
				double rate = (atk - Record.get(pos.get(0)).atk)
						/ Integer.parseInt(fairy.lv)
						- Record.get(pos.get(0)).lv;
				int base = (int) (atk - rate * Integer.parseInt(fairy.lv));
				Record.get(pos.get(0)).rate = (rate + Record.get(pos.get(0)).rate) / 2;
				Record.get(pos.get(0)).base = (int) ((base + Record.get(pos
						.get(0)).base) / 2);
				radio a = new radio();
				a.FairyName = fairy.name;
				a.lv = Integer.parseInt(fairy.lv);
				a.atk = atk;
				Record.set(pos.get(pos.size() - 1), a);
			} else {
				double rate = (atk - Record.get(pos.get(0)).atk)
						/ Integer.parseInt(fairy.lv)
						- Record.get(pos.get(0)).lv;
				int base = (int) (atk - rate * Integer.parseInt(fairy.lv));
				Record.get(pos.get(0)).rate = (rate + Record.get(pos.get(0)).rate) / 2;
				Record.get(pos.get(0)).base = (int) ((base + Record.get(pos
						.get(0)).base) / 2);
			}
		} else {
			radio a = new radio();
			a.FairyName = fairy.name;
			a.lv = Integer.parseInt(fairy.lv);
			a.atk = atk;
			Record.add(a);
			if (!pos.isEmpty()){
				double rate = (atk - Record.get(pos.get(0)).atk)
						/ Integer.parseInt(fairy.lv) - Record.get(pos.get(0)).lv;
				int base = (int) (atk - rate * Integer.parseInt(fairy.lv));
				Record.get(pos.get(0)).rate = rate;
				Record.get(pos.get(0)).base = base;
			}
		}
		try {
			FileWriter recordfile = new FileWriter(fairy.name + ".log",true);
			recordfile.write(fairy.lv + "," + atk + "\r\n");
			recordfile.close();
		} catch (IOException e) {
		}
		

	}

}

class radio {
	String FairyName = " ";

	int lv = 0;

	int atk = 0;

	double rate = -1;

	int base = 0;
}
