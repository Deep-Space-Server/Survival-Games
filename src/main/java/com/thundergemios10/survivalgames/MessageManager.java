package com.thundergemios10.survivalgames;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import com.thundergemios10.survivalgames.util.MessageUtil;



public class MessageManager {

	public static MessageManager instance = new MessageManager();
	public String pre = ChatColor.BLUE + "" + ChatColor.BOLD + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "SG" + ChatColor.BLUE + "" + ChatColor.BOLD + "] " + ChatColor.RESET;
	private HashMap<PrefixType, String>prefix = new HashMap<PrefixType, String>();
	public enum PrefixType {

		MAIN, INFO, WARNING, ERROR;
		
	}
	
	public static MessageManager getInstance() {
		return instance;
	}
	
	public void setup() {
		FileConfiguration f = SettingsManager.getInstance().getMessageConfig();
		prefix.put(PrefixType.MAIN, MessageUtil.replaceColors(f.getString("prefix.main")));
		prefix.put(PrefixType.INFO, MessageUtil.replaceColors(f.getString("prefix.states.info")));
		prefix.put(PrefixType.WARNING, MessageUtil.replaceColors(f.getString("prefix.states.warning")));
		prefix.put(PrefixType.ERROR, MessageUtil.replaceColors(f.getString("prefix.states.error")));
		
	}
	
	
	/**
	 * SendMessage
	 * 
	 * Loads a Message from messages.yml, converts its colors and replaces vars in the form of {$var} with its correct values, 
	 * then sends to the player, adding the correct prefix 
	 *  
	 * @param type Type of message to send
	 * @param input What message you want to send from messages.yml
	 * @param player Player you want to send message to
	 * @param vars
	 */
	public void sendFMessage(PrefixType type, String input, Player player, String ... args) {
		player.sendMessage(getFMessage(type, input, args));
	}
	public void sendFMessage(PrefixType type, String input, CommandSender sender, String ... args) {
		sender.sendMessage(getFMessage(type, input, args));
	}
	private String getFMessage(PrefixType type, String input, String ... args) {
		String msg = SettingsManager.getInstance().getMessageConfig().getString("messages."+input);
		boolean enabled = SettingsManager.getInstance().getMessageConfig().getBoolean("messages."+input+"_enabled", true);
		if(msg == null)return ChatColor.RED+"Failed to load message for messages."+input; 
		if(!enabled)return null;
		if(args != null && args.length != 0){msg = MessageUtil.replaceVars(msg, args);}
		msg = MessageUtil.replaceColors(msg);
		
		return prefix.get(PrefixType.MAIN)+ " "+prefix.get(type)+ msg;
	}
	
	/**
	 * SendMessage
	 * 
	 * Sends a pre formatted message from the plugin to a player, adding correct prefix first
	 * 
	 * @param type Type of message to send
	 * @param msg The message you want to send
	 * @param player Player you want to send the message to
	 */
	
	public void sendMessage(PrefixType type, String msg, Player player){
		player.sendMessage(prefix.get(PrefixType.MAIN)+ " "+prefix.get(type)+ msg );
	}
	/**
	 * SendMessage
	 * 
	 * Sends a pre formatted message from the plugin to a CommandSender, adding correct prefix first
	 * 
	 * @param type Type of message to send
	 * @param msg The message you want to send
	 * @param CommandSender CommandSender you want to send the message to
	 */
	public void sendMessage(PrefixType type, String msg, CommandSender sender){
		sender.sendMessage(prefix.get(PrefixType.MAIN)+ " "+prefix.get(type)+ msg );
	}
	
	public void logMessage(PrefixType type, String msg) {
		Logger logger = Bukkit.getServer().getLogger();
		switch (type) {
		case INFO:  logger.info(prefix.get(type)+ msg); break;
		case WARNING: logger.warning(prefix.get(type) + msg); break;
		case ERROR: logger.severe(prefix.get(type) + msg); break;
		default:
			break;
		}
	}
	
	public void broadcastFMessage(PrefixType type, String input, String ...args ) {
		String msg = SettingsManager.getInstance().getMessageConfig().getString("messages."+input);
		boolean enabled = SettingsManager.getInstance().getMessageConfig().getBoolean("messages."+input+"_enabled", true);
		if(msg == null){Bukkit.broadcastMessage(ChatColor.RED+"Failed to load message for messages."+input);return;}
		if(!enabled)return;
		if(args != null && args.length != 0){msg = MessageUtil.replaceVars(msg, args);}
		msg = MessageUtil.replaceColors(msg);
		Bukkit.broadcastMessage(prefix.get(PrefixType.MAIN)+ prefix.get(type)+ " "+msg);
	}
	
	public void broadcastMessage(PrefixType type, String msg, Player player){
		Bukkit.broadcastMessage(prefix.get(PrefixType.MAIN)+ " "+prefix.get(type)+ " "+msg );
	}
	


}
