package autoBattle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Info {
		
	public static Deck[] UserDeck = new Deck[358];
	public static Card[] UserCard = new Card[105];
	
	public static int race = 3;
	public static int Card_num = 0;
	public static int Deck_num = 0;
	
	public static boolean ReadCard(){
	       try { 
	    	    File file=new File("data.csv");    
	    	    if(!file.exists())return false;     
	    		   
				BufferedReader br1 = new BufferedReader(new FileReader("data.csv"));
	            String line = br1.readLine();//����ͷ
	            
	            while ((line = br1.readLine()) != null) {
	            	UserCard[Card_num]=new Card();
	            	if(!UserCard[Card_num].Read(line)){
	            		br1.close();
	            		return false;
	            	}
	            	Card_num++;
	            }
	            br1.close();
	        }
	        catch(IOException e) {
	            System.out.println("IO Problem");
	        }
	       return true;
	}
	
	public static boolean ReadDeck(){
	       try { 
	    	    File file=new File("deck_info");    
	    	    if(!file.exists())return false;     
	    		   
	    	    BufferedReader br2 = new BufferedReader(new FileReader("deck_info"));
	            
	    	    String line;
	    	    
	            while ((line = br2.readLine()) != null) {
	            	UserDeck[Deck_num]=new Deck();
	            	if(!UserDeck[Deck_num].Read(line)){
	            		br2.close();
	            		return false;
	            	}
	            	//UserDeck[Deck_num].Print();
	            	Deck_num++;
	            }
	            br2.close();
	        }
	        catch(IOException e) {
	            System.out.println("IO Problem");
	        }
	       return true;
	}
}
