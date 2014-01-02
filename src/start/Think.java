package start;

import java.util.List;

import action.ActionRegistry.Action;

public class Think {

	public static Action doIt(List<Action> possible) {
		Action best = Action.NOTHING;
		for (int i = 0; i < possible.size(); i++) {
			switch (possible.get(i)) {
			case LOGIN:
				return Action.LOGIN;
			case GET_NONAME_LIST:
				return Action.GET_NONAME_LIST;
			case PVP:
				return Action.PVP;
			case GET_FAIRY_LIST:
				return Action.GET_FAIRY_LIST;
			case FAIRY_HISTORY:
				return Action.FAIRY_HISTORY;
			case CHANGE_CARD_ITEMS:
				return Action.CHANGE_CARD_ITEMS;
			case PRIVATE_FAIRY_BATTLE:
				return Action.PRIVATE_FAIRY_BATTLE;
			case LV_UP:
				return Action.LV_UP;
			case GET_FLOOR_INFO:
				return Action.GET_FLOOR_INFO;
			case SELL_CARD:
				return Action.SELL_CARD;
			case ADD_FRIENDS:
				return Action.ADD_FRIENDS;
			default:
			}
		}
		return best;
	}

}
