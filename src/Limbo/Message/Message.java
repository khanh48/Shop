package Limbo.Message;

import java.lang.String;

import Limbo.SimpleShop;

public class Message {
	public static String TRADE_VILLAGE_MSG = message("trade_village_msg");
	public static String TAKE_MONEY = message("take_money");
	public static String TAKE_FROM = message("take_from");
	public static String BOUGHT = message("bought");
	public static String BALANCE = message("balance");
	public static String HASNT_PERM = message("hasnt_perm");
	public static String LIMIT = message("limit");
	public static String CANT_BUY = message("cant_buy");
	public static String SELLING = message("selling");
	public static String ENTER_MONEY = message("enter_money");
	public static String TRADE_ERROR = message("trade_error");
	public static String CONSOLE = message("console");
	public static String RELOAD = message("reload");
	
	public static boolean contains(String src, String string) {
		return src.toLowerCase().contains(string.toLowerCase());
	}
	
	private static String message(String name) {
		return SimpleShop.getIntance().message.getConfig().getString("message." + name);
	}

	public static String replace(String src, String target, String replacement) {
        return src.replaceAll("(?i)" + target, replacement);
    }
	
	public static void reload() {
		TRADE_VILLAGE_MSG = message("trade_village_msg");
		TAKE_MONEY = message("take_money");
		TAKE_FROM = message("take_from");
		BOUGHT = message("bought");
		BALANCE = message("balance");
		HASNT_PERM = message("hasnt_perm");
		LIMIT = message("limit");
		CANT_BUY = message("cant_buy");
		SELLING = message("selling");
		ENTER_MONEY = message("enter_money");
		TRADE_ERROR = message("trade_error");
		CONSOLE = message("console");
		RELOAD = message("reload");
	}
}
