package autoBattle;

import net.Process;

public class AutoBattle {

	public static boolean Init(int x) {
		boolean res = true;
		res = res && Info.ReadCard();
		res = res && Info.ReadDeck();
		Info.race = x;
		return res;
	}

	static double p[][][] = new double[4][50][11];

	static double HpCombo(Deck d) {
		double ans = 1;
		if (d.num == 6)
			ans += 0.2;
		else if (d.num == 9)
			ans += 0.3;
		else if (d.num == 12)
			ans += 0.4;
		return ans;
	}

	static double AtkCombo(Deck d) {
		double ans = 1;
		return ans;
	}

	static double min(double a, double b) {
		return a < b ? a : b;
	}

	static int Battle(Deck mydeck, int fairyhp, int fairyatk, int EX) {
		int hp = 0;
		int[] atk = { 0, 0, 0, 0 };
		int total_atk = 0;
		double hpc = HpCombo(mydeck);
		double atkc = AtkCombo(mydeck);

		for (int i = 0; i < mydeck.num; i++) {
			hp += hpc * mydeck.c[i].hp;
			atk[i / 3] += atkc * mydeck.c[i].atk;

			if (mydeck.c[i].race == Info.race) {
				hp += +0.05 * mydeck.c[i].hp;
			}
		}

		int total_row = (mydeck.num - 1) / 3 + 1;

		for (int i = 0; i < total_row; i++) {
			p[i][0][0] = 1;
		}

		int row = 0, round = 1, max_fairyhp = fairyhp, max_hp = hp;

		while (fairyhp > 0 && hp > 0) {
			// System.out.println("round="+round+" fairyhp="+fairyhp+" hp="+hp
			// );

			if (EX == 100) {
				fairyhp -= (max_hp + total_atk) / 2.0;
				EX = 0;
			}
			EX += 5;

			double[] pp = { 0, 0, 0 };
			int[] type = { 0, 0, 0 };

			for (int i = 0; i < 3 && i < mydeck.num; i++) {
				type[i] = mydeck.c[row * 3 + i].type;
				pp[i] = mydeck.c[row * 3 + i].p;

				if (type[i] == 0)// ���ֵ����ʴ���
				{
					if (round != 1)
						pp[i] = 0;
				} else if (type[i] == 1)// �����̸��ʴ���
				{
					if (hp > max_hp * mydeck.c[row * 3 + i].condition / 100.0)
						pp[i] = 0;
				} else if (type[i] == 6)// ���Ƶ����ʴ���
				{
					if (hp > max_hp * mydeck.c[row * 3 + i].condition / 100.0)
						pp[i] = 0;
				}
			}
			// System.out.println(pp[0]+" "+pp[1]+" "+pp[2]);

			int this_round = (round - 1) / total_row + 1;
			int last_round = (round - 1) / total_row;

			p[row][this_round][0] = p[row][last_round][0] * (1 - pp[0])
					* (1 - pp[1]) * (1 - pp[2]);

			p[row][this_round][1] = p[row][last_round][1] * (1 - pp[1])
					* (1 - pp[2]) + p[row][last_round][0] * pp[0];
			p[row][this_round][2] = p[row][last_round][2] * (1 - pp[0])
					* (1 - pp[2]) + p[row][last_round][0] * (1 - pp[0]) * pp[1];
			p[row][this_round][4] = p[row][last_round][4] * (1 - pp[0])
					* (1 - pp[1]) + p[row][last_round][0] * (1 - pp[0])
					* (1 - pp[1]) * pp[2];

			p[row][this_round][3] = p[row][last_round][3] * (1 - pp[2])
					+ p[row][last_round][1] * pp[1] + p[row][last_round][2]
					* pp[0];
			p[row][this_round][5] = p[row][last_round][5] * (1 - pp[1])
					+ p[row][last_round][1] * (1 - pp[1]) * pp[2]
					+ p[row][last_round][4] * pp[0];
			p[row][this_round][6] = p[row][last_round][6] * (1 - pp[0])
					+ p[row][last_round][2] * (1 - pp[0]) * pp[2]
					+ p[row][last_round][4] * (1 - pp[0]) * pp[1];

			p[row][this_round][7] = p[row][last_round][7]
					+ p[row][last_round][3] * pp[2] + p[row][last_round][5]
					* pp[1] + p[row][last_round][6] * pp[0];

			p[row][this_round][8] = p[row][this_round][1]
					+ p[row][this_round][3] + p[row][this_round][5]
					+ p[row][this_round][7];
			p[row][this_round][9] = p[row][this_round][2]
					+ p[row][this_round][3] + p[row][this_round][6]
					+ p[row][this_round][7];
			p[row][this_round][10] = p[row][this_round][4]
					+ p[row][this_round][5] + p[row][this_round][6]
					+ p[row][this_round][7];

			double[] pc = new double[3];
			pc[0] = p[row][this_round][8] - p[row][last_round][8];
			pc[1] = p[row][this_round][9] - p[row][last_round][9];
			pc[2] = p[row][this_round][10] - p[row][last_round][10];

			// System.out.println(pc[0]+" "+pc[1]+" "+pc[2]);
			// System.out.println("����ǰfairyhp="+fairyhp);

			for (int i = 0; i < 3 && i < mydeck.num; i++) {
				double effect = mydeck.c[row * 3 + i].effect;
				int recover;

				// System.out.println("effect="+effect);

				switch (mydeck.c[row * 3 + i].type) {
				case 0:
					fairyhp -= pc[i] * effect;
					break;
				case 1:
					recover = (int) min(effect, max_hp - hp);
					hp += pc[i] * recover;
					break;
				case 2:
					fairyhp -= pc[i] * effect;
					break;
				case 3:
					fairyhp -= pc[i] * effect / 10.0 * round;
					break;
				case 4:
					recover = (int) min(effect, max_hp - hp);
					hp += pc[i] * recover;
					break;
				case 5:
					recover = (int) min(effect / 10.0 * round, max_hp - hp);
					hp += pc[i] * recover;
					break;
				case 6:
					fairyhp -= pc[i] * effect;
					break;
				}
				// System.out.println(fairyhp);
			}

			// System.out.println("���ܺ�fairyhp="+fairyhp);

			fairyhp -= atk[row];
			// System.out.println(fairyhp);
			if (fairyhp <= 0) {
				return max_fairyhp;
			}
			hp -= fairyatk;
			if (hp <= 0) {
				return max_fairyhp - fairyhp;
			}
			round++;
			row = (row + 1) % total_row;
		}
		return 0;
	}

	static double get(int gather, int maxhp, int dmg, int cost)// ���������
	{
		double ans = 1.0 * gather * dmg / maxhp;
		ans /= 10;
		ans = 10 * Math.round(ans);
		if (ans < 10)
			ans = 10;

		return ans / cost;
	}

	public static String GetWinDeck(int lv, int fairyhp, int remainhp,
			int fairyatk, int bc, int EX) {

		int ans = -1;

		for (int i = 0; i < Info.Deck_num && Info.UserDeck[i].cost <= bc; i++) {
			int dmg = Battle(Info.UserDeck[i], remainhp, fairyatk, EX);
			if (dmg >= remainhp)// ��ʤ
			{
				ans = i;
				break;
			}
		}

		String r = new String();

		if (ans >= 0) {
			r += Info.UserDeck[ans].c[0].sid;
			Process.info.CurrentDeck = Info.UserDeck[ans].c[0].name;

			int i;
			for (i = 1; i < Info.UserDeck[ans].num; i++) {
				r += ",";
				r += Info.UserDeck[ans].c[i].sid;
				Process.info.CurrentDeck += "," + Info.UserDeck[ans].c[i].name;
			}
			for (; i < 12; i++)
				r += ",empty";
		} else {
			r = "null";
		}
		// Info.UserDeck[ans].Print();
		return r;
	}

	public static String GetCollectionDeck(int lv, int type, int fairyhp,
			int remainhp, int fairyatk, int bc, double expect, int EX) {

		int ans = -1;
		int gather = 0;
		double k = expect;

		if (type == 1 || type == 3)
			gather = 40 * lv + 1000;
		else
			gather = 10 * lv;

		for (int i = 0; i < Info.Deck_num && Info.UserDeck[i].cost <= bc; i++) {
			// Info.UserDeck[i].Print();
			int dmg = Battle(Info.UserDeck[i], remainhp, fairyatk, EX);

			double x = get(gather, fairyhp, dmg, Info.UserDeck[i].cost);

			if (k <= x) {
				k = x;
				ans = i;
			}
		}

		String r = new String();

		if (ans >= 0) {
			r += Info.UserDeck[ans].c[0].sid;
			Process.info.CurrentDeck = Info.UserDeck[ans].c[0].name;
			int i;
			for (i = 1; i < Info.UserDeck[ans].num; i++) {
				r += ",";
				r += Info.UserDeck[ans].c[i].sid;
				Process.info.CurrentDeck += "," + Info.UserDeck[ans].c[i].name;
			}
			for (; i < 12; i++)
				r += ",empty";
		} else {
			r = "null";
		}
		// Info.UserDeck[ans].Print();
		return r;
	}
}
