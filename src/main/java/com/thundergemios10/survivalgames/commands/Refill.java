package com.thundergemios10.survivalgames.commands;


import org.bukkit.entity.Player;
import com.thundergemios10.survivalgames.GameManager;
import com.thundergemios10.survivalgames.MessageManager;
import com.thundergemios10.survivalgames.MessageManager.PrefixType;
import com.thundergemios10.survivalgames.logging.QueueManager;
import com.thundergemios10.survivalgames.SettingsManager;

public class Refill implements SubCommand {

	MessageManager msgmgr = MessageManager.getInstance();

	public boolean onCommand(Player player, String[] args) {

		if (!player.hasPermission(permission()) && !player.isOp()) {
			MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.nopermission", player);
			return true;
		}
		int game = -1;
		if(args.length >= 1){
			game = Integer.parseInt(args[0]);

		}
		else
			game  = GameManager.getInstance().getPlayerGameId(player);
		if(game == -1){
			MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.notingame", player);
			return true;
		}

		QueueManager.getInstance().restockChests(game);
		
		msgmgr.sendFMessage(PrefixType.INFO, "game.refill", player, "arena-" + game);

		return true;
	}

	public String help() {
		return "/sg refill [<id>] " + SettingsManager.getInstance().getMessageConfig().getString("messages.help.refill", "Refill the chests");
	}

	public String permission() {
		return "sg.arena.refill";
	}
}