package io.loyloy.fe.command;

import io.loyloy.fe.Fe;
import io.loyloy.fe.FeCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeCommandCompleter implements TabCompleter {

	Fe plugin;
	FeCommand feCommand;
	
	public FeCommandCompleter(Fe plugin, FeCommand feCommand) {
		this.plugin = plugin;
		this.feCommand = feCommand;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> list;
		switch(args.length) {
			case 1:
				list = firstArgument(sender);
				return StringUtil.copyPartialMatches(args[0], list, new ArrayList<>());
			case 2:
				list = secondArgument(sender, args);
				return StringUtil.copyPartialMatches(args[1], list, new ArrayList<>());
			case 3:
				list = thridArgument(sender, args);
				return StringUtil.copyPartialMatches(args[2], list, new ArrayList<>());
			default:
				return Collections.singletonList("");
		}
	}
	
	private List<String> firstArgument(CommandSender sender) {
		List<String> list = new ArrayList<>();
		for(SubCommand subCommand : feCommand.getCommands()) {
			if(sender.hasPermission(subCommand.getPermission()))
				if(subCommand.getUsage().equalsIgnoreCase("(name)"))
					Bukkit.getOnlinePlayers().forEach(player -> list.add(player.getName()));
				else
					list.add(subCommand.getFirstName());
		}
		return list;
	}
	
	private List<String> secondArgument(CommandSender sender,  String[] args) {
		for(SubCommand subCommand : feCommand.getCommands()) {
			if(args[0].equalsIgnoreCase(subCommand.getFirstName()) && subCommand.getUsage().contains("[name]") && sender.hasPermission(subCommand.getPermission())) {
				List<String> list = new ArrayList<>();
				Bukkit.getOnlinePlayers().forEach(player -> list.add(player.getName()));
				return list;
			}
		}
		
		return Collections.singletonList("");
	}
	
	private List<String> thridArgument(CommandSender sender,  String[] args) {
		for(SubCommand subCommand : feCommand.getCommands()) {
			if(args[0].equalsIgnoreCase(subCommand.getFirstName()) && subCommand.getUsage().contains("[amount]") && sender.hasPermission(subCommand.getPermission())) 
				return new ArrayList<>();
		}
		
		return Collections.singletonList("");
	}

}