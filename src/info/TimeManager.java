package info;

import java.util.Calendar;

import action.GetAreaInfo;

import net.Process;
import start.Go;
import start.Info;

public class TimeManager {
	private static int day = 0;
	private static Calendar c = Calendar.getInstance();
	private static boolean showed = false;
	private static int hour = 0;
	private static boolean found = false;
	public static void Check(){
		if (day != c.get(Calendar.DAY_OF_WEEK)){
			day = c.get(Calendar.DAY_OF_WEEK);
			found = false;
			for (MAPConfigInfo MapConfigInfo : Info.MAPConfigInfos) {
				if (MapConfigInfo.day == day) {
					Info.maptimelimitDown = MapConfigInfo.maptimelimitDown;
					Info.maptimelimitUp = MapConfigInfo.maptimelimitUp;
					Info.dayFirst = MapConfigInfo.daily;
					String str = (  c.get(Calendar.MONTH) + 1 + "月" + c.get(Calendar.DAY_OF_MONTH) + "日" + " 星期" + (day - 1));
					if (MapConfigInfo.maptimelimitUp != -1)
						str += " 限时秘境时段 " + Info.maptimelimitDown + ":00-" + Info.maptimelimitUp + ":00";
					if (MapConfigInfo.daily.contains("1"))
						str += " 每日秘境优先";
					Go.log(str);
					break;
				}
			}
		}
		if (Info.maptimelimitUp == -1 && found){
			Info.maptimelimit = true;
			return;
		}
		int minute = c.get(Calendar.MINUTE);
		if (hour != c.get(Calendar.HOUR_OF_DAY) && minute > 1){
			hour =c.get(Calendar.HOUR_OF_DAY);
			if (hour >= Info.maptimelimitDown && hour <= Info.maptimelimitUp){
				try {
					if (GetAreaInfo.run(true)){
						if (Process.info.floorInfos.size() > 0) { 
								for (FloorInfo fInfo1 : Process.info.floorInfos) {
									if (!Info.maptimelimit && fInfo1.name.contains("限时")){
										Info.maptimelimit = true;
										Process.info.events.push(Info.EventType.needFloorInfo);
										found =true;
										if (GetAreaInfo.run(false))
											if (Process.info.floorInfos.size() > 0)
												for (FloorInfo fInfo2 : Process.info.floorInfos)
													if (!Info.maptimelimit && fInfo2.name.contains("限时"))
														Info.hasPrivateFairyStopRun = 0;
													else
														Info.hasPrivateFairyStopRun = 1;
										Go.log("发现限时秘境!");
										break;
									}
								}
							
						}
					}
				} catch (Exception e) {
					Go.log("地图刷新失败:"+e.toString());
				}
			}
		}
			

		if ( Info.maptimelimitDown - (Info.stopRunWhenApLess - Process.info.apCurrent) / 20 <= hour){ 
			if( 60 - minute >= (Info.stopRunWhenApLess - Process.info.apCurrent) / 3 
					&& hour <= Info.maptimelimitUp)
				if(!showed){
					showed = true;
					Go.log("保留AP");
				}
			Info.maptimelimit = false;
			
			}
		else {
			Info.maptimelimit = true;
			showed = false;
		}
	}
}
