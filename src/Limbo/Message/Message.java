package Limbo.Message;

import java.lang.String;

import Limbo.SimpleShop;

public enum Message {
	TRADE_VILLAGE_MSG(message("trade_village_msg")),
	TAKE_MONEY(message("take_money")),
	TAKE_FROM(message("take_from")),
	BOUGHT(message("bought")),
	BALANCE(message("balance")),
	HASNT_PERM(message("hasnt_perm")),
	LIMIT(message("limit")),
	CANT_BUY(message("cant_buy")),
	SELLING(message("selling")),
	ENTER_MONEY(message("enter_money")),
	TRADE_ERROR(message("trade_error")),
	CONSOLE(message("console")),
	RELOAD(message("reload"));
	
	public final String getMessage;
	private Message(String msg) {
		this.getMessage = msg;
	}
	
	public boolean contains(String string) {
		return this.getMessage.toLowerCase().contains(string.toLowerCase());
	}
	
	public String replace(String target, String replacement) {
        return this.getMessage.replaceAll("(?i)" + target, replacement);
    }
	
	private static String message(String name) {
		return SimpleShop.getIntance().message.getConfig().getString("message." + name);
	}

	public static String replace(String src, String target, String replacement) {
        return src.replaceAll("(?i)" + target, replacement);
    }
}
