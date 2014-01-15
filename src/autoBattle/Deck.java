package autoBattle;

public class Deck {
	public int num = 0;
	public int cost = 0;
	public Card[] c = new Card[12];

	public boolean Read(String S) {
		String[] sArray = S.split(" ");
		this.num = Integer.parseInt(sArray[0]);

		for (int i = 0; i < num; i++) {
			this.c[i] = new Card();
			int sid = Integer.parseInt(sArray[i + 1]);

			boolean find = false;
			for (int j = 0; j < Info.Card_num; j++) {
				if (sid == Info.UserCard[j].sid) {
					c[i] = Info.UserCard[j];
					this.cost += c[i].cost;
					find = true;
					break;
				}
			}
			if (!find)
				return false;
		}
		return true;
	}

	public void Print() {
		System.out.println(num);
		for (int i = 0; i < num; i++)
			System.out.print(c[i].name + " ");
		System.out.println();
	}
}
