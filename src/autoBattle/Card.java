package autoBattle;

public class Card {
		public int sid;
		public String name;
		public int star;
		public int cost;
		public int hp;
		public int atk;
		public int type;
		public int condition;
		public double effect;
		public double p;
		public int race;
		public double atkrate;
		
		public boolean Read(String S)
		{	
			String[] sArray=S.split(",");
			
			if(sArray.length!=12)return false;
			
			this.sid=Integer.parseInt(sArray[0]);
			this.name=sArray[1];
			this.star=Integer.parseInt(sArray[2]);
			this.cost=Integer.parseInt(sArray[3]);
			this.hp=Integer.parseInt(sArray[4]);
			this.atk=Integer.parseInt(sArray[5]);
			this.type=Integer.parseInt(sArray[6]);
			this.condition=Integer.parseInt(sArray[7]);
			this.effect=Double.parseDouble(sArray[8]);
			this.p=Double.parseDouble(sArray[9]);
			this.race=Integer.parseInt(sArray[10]);
			this.atkrate=Double.parseDouble(sArray[11]);
			
			this.atk *= this.atkrate;
			this.p/=100;
		
			
			switch(this.type)//effectԤ����
			{
				case 0:this.effect *=this.atk;break;
				case 1:this.effect *=this.hp;break;
				case 2:this.effect *=this.atk;break;
				case 3:this.effect *=this.atk;break;
				case 4:this.effect *=this.hp;break;
				case 5:this.effect *=this.hp;break;
				case 6:this.effect *=this.atk;break;
			}
			
			this.effect /=100;
			
			return true;
		}

		public void Print()
		{	
			System.out.println(sid + name);
		}
}
