package start;

import info.CardConfigInfo;
import info.FairyInfo;
import info.FloorInfo;
import info.MAPConfigInfo;
import info.NoNameInfo;
import info.UserCardsInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Info {

	public static boolean devMode = true ; 
	
	// login info
	public static String LoginId = "";
	public static String LoginPw = "";
	public static String LoginServer = "";
	public static int sleepTime = 0;
	public static String isPVP = "";
	public static String isRun = "";
	public static String dayFirst = "";
	public static int whatMap = 0;
	public static String isBattlePrivateFariy = "";
	public static int waitTime = 5;
	public static int stopRunWhenBcMore = 0;
	public static int stopRunWhenApLess = 0;
	public static String autoPoint = "bc";
	public static String userAgent = "";
	public static String runFactor = "0";
	public static int hasPrivateFairyStopRun = 0;
	public static boolean log = false;
	public static boolean simplelog = false;
	// 用于标记当前是否可以跑图，默认可以
	public static int canRun = 1;
	
	// 卡组资料
	public static int whenBcMoreThan = 0;	
	public static String pvpCard = "";
	public static String pvpLr = "";
	public static int pvpCost = 0;
	public static String battleCard = "";
	public static String battleLr = "";
	public static int battleCost = 0;
	public static String wolf = "";
	public static String wolfLr = "";
	public static int lickCost = 2;
	public static String battleBoss = "";
	public static String battleBossLr = "";

	// user info
	public String cookie = "";
	public String userId = "";
	public String userName = "";
	public String userLv = "";
	public int apMax = 0;
	public int apCurrent = 0;
	public int bcMax = 0;
	public int bcCurrent = 0;
	public int freeApBcPoint = 0; // 可分配点数
	public int friendpoint = 0;
	public int fullAp = 0;
	public int fullBc = 0;
	
	// floor info
	public String floorId = "";
	public int floorCost = 0;
	public String event_type = "";
	public int progress = 0;
	public int lvup = 0;
	public int getExp = 0;
	public int runGold = 0;
	public String nextFloorId = "";
	public int nextFloorCost = 0;
	public int areaClear = 0;
	public int bossId = 0;
	public static boolean maptimelimit = false;//发现限时秘境时，或者不在发现时段，为true 在可能出现时段，但未发现时为 false
	public static List<MAPConfigInfo> MAPConfigInfos = new ArrayList<MAPConfigInfo>();
	public static int maptimelimitDown = 0;
	public static int maptimelimitUp = 0;
	// battle result
	public String battleResult = "";
	public int gold = 0;
	// 默认10000
	public int nextExp = 0;
	public int exp = 0;
	public boolean isLvUp = false;
	public int gather = 0;

	// event
	public enum EventType {
		notLoggedIn, cookieOutOfDate, needFloorInfo, innerMapJump, areaComplete, fairyAppear, fairyTransform, fairyReward, fairyCanBattle, fairyBattleWin, fairyBattleLose, fairyBattleEnd, cardFull, privateFairyAppear, guildTopRetry, guildBattle, guildTop, ticketFull, getFairyReward, needAPBCInfo, levelUp, pvp, getNoNameList, fairyHistory, changeCardItems, getCardItem, fairyInfo
	}

	public Stack<EventType> events;

	public static double timepoverty = 5;
	public static int timepovertyMAX;
	// 无名集合
	public List<NoNameInfo> noNameList;

	// 妖精集合
	public List<FairyInfo> fairyInfos;

	// 未攻击过的妖精集合
	public List<FairyInfo> canBattleFairyInfos;

	public List<FloorInfo> floorInfos;

	// 用户的所有卡片
	public static List<UserCardsInfo> userCardsInfos;
	public int cardMax;
	public static ArrayList<String> CanBeSold = new ArrayList<String>();
	public static boolean autoSellCards = false;
	public static boolean smartSell = false;

	// 卡组信息
	public static List<CardConfigInfo> cardConfigInfos = new ArrayList<CardConfigInfo>();
	public static String PVPEvent = "48";
	
	
	

	public Info() {
		noNameList = new ArrayList<NoNameInfo>();
		fairyInfos = new ArrayList<FairyInfo>();
		canBattleFairyInfos = new ArrayList<FairyInfo>();
		events = new Stack<EventType>();
		events.push(EventType.notLoggedIn);
	}

}
