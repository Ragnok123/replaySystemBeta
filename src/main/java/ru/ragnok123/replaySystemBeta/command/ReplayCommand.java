package ru.ragnok123.replaySystemBeta.command;

import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.ragnok123.replaySystemBeta.ReplaySystem;
import ru.ragnok123.replaySystemBeta.data.Record;

public class ReplayCommand extends Command {

	public ReplayCommand(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player)sender;
		if(args[0].equalsIgnoreCase("start")) {
			if(args.length == 1) {
				ReplaySystem.get().startCapture(p);
			} else if(args.length > 1 && args.length < 3){
				
			}
		} else if(args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("save")) {
			if(args.length == 1) {
				ReplaySystem.get().stopCapture(p);
			} else if(args.length > 1 && args.length < 3){
				
			}
		} else if(args[0].equalsIgnoreCase("watch")) {
			if(args.length == 1) {
				p.sendMessage(ReplaySystem.get().getPrefix());
				p.sendMessage("§c/replay watch list <player>");
				p.sendMessage("§c/replay watch <player> <replay id>");
			} else {
				if(args[1].equalsIgnoreCase("list")) {
					if(args.length < 3) {
						p.sendMessage(ReplaySystem.get().getPrefix("c") + "You didn't specified player");
					} else {
						ArrayList<Record> records = ReplaySystem.get().getRecords(args[2]);
						p.sendMessage(ReplaySystem.get().getPrefix("a") + "Number of player replays: " + records.size());
					}
				} else {
					if(args.length < 3) {
						p.sendMessage(ReplaySystem.get().getPrefix("c") + "You didn't specified replay id");
					} else {
						int id = Integer.valueOf(args[2]);
						String target = args[1];
						if(ReplaySystem.get().replays.containsKey(target)) {
							if(ReplaySystem.get().getRecords(target).get(id) != null) {
								Record recording = ReplaySystem.get().getRecords(target).get(id);
								ReplaySystem.get().viewRecord(p, recording);
							} else {
								p.sendMessage(ReplaySystem.get().getPrefix("c") + "Player doesn't have such replay id");
							}
						} else {
							p.sendMessage(ReplaySystem.get().getPrefix("c") + "This player doesn't have any records");
						}
					}
				}
			}
		}
		return false;
	}

}
