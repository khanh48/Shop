package Limbo.Message;

import Limbo.SimpleShop;

public enum Message {
	TRADE_VILLAGE_MSG(SimpleShop.getIntance().message.getConfig().getString("message.trade_msg")),
	TAKE_MONEY(SimpleShop.getIntance().message.getConfig().getString("message.take_money")),
	BALANCE(SimpleShop.getIntance().message.getConfig().getString("message.balance")),
	HASNT_PERM(SimpleShop.getIntance().message.getConfig().getString("message.hasnt_perm")),
	LIMIT(SimpleShop.getIntance().message.getConfig().getString("message.limit")),
	CANT_BUY(SimpleShop.getIntance().message.getConfig().getString("message.cant_buy")),
	ENTER_MONEY(SimpleShop.getIntance().message.getConfig().getString("message.enter_money")),
	TRADE_ERROR(SimpleShop.getIntance().message.getConfig().getString("message.trade_error")),
	CONSOLE(SimpleShop.getIntance().message.getConfig().getString("message.console")),
	RELOAD(SimpleShop.getIntance().message.getConfig().getString("message.reload"));
	
	public final String getMsg;
	private Message(String msg) {
		this.getMsg = msg;
	}
}