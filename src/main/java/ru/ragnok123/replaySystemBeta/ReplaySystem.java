package ru.ragnok123.replaySystemBeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.crypto.Data;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelChunkPacket;
import cn.nukkit.network.protocol.RequestChunkRadiusPacket;
import cn.nukkit.network.protocol.ResourcePackChunkDataPacket;
import cn.nukkit.network.protocol.ResourcePackChunkRequestPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import ru.ragnok123.replaySystemBeta.command.ReplayCommand;
import ru.ragnok123.replaySystemBeta.data.Record;
import ru.ragnok123.replaySystemBeta.data.impl.BlockData;
import ru.ragnok123.replaySystemBeta.data.impl.PacketData;
import ru.ragnok123.replaySystemBeta.task.CaptureTask;
import ru.ragnok123.replaySystemBeta.task.ViewTask;

public class ReplaySystem extends PluginBase implements Listener{
	
	public static ReplaySystem instance;
	public HashMap<String, ReplayClient> clients = new HashMap<String, ReplayClient>();
	public HashMap<String, ArrayList<Record>> replays = new HashMap<String, ArrayList<Record>>();
	private List<Byte> ignoredPackets = new ArrayList<Byte>();
	
	public static ReplaySystem get() {
		return instance;
	}
	
	public void onEnable() {
		instance = this;
		this.getServer().getCommandMap().register("replay", new ReplayCommand("replay"));
		this.getServer().getScheduler().scheduleRepeatingTask(new CaptureTask(), 20);
		
		ignoredPackets.add(LevelChunkPacket.NETWORK_ID);
		ignoredPackets.add(RequestChunkRadiusPacket.NETWORK_ID);
		ignoredPackets.add(ResourcePackChunkRequestPacket.NETWORK_ID);
		ignoredPackets.add(ResourcePackChunkDataPacket.NETWORK_ID);
	}
	
	public ReplayClient getClient(Player p) {
		return getClient(p.getName());
	}
	
	public ReplayClient getClient(String name) {
		name = name.toLowerCase();
		if(clients.containsKey(name)) {
			return clients.get(name);
		}
		return null;
	}
	
	public ArrayList<Record> getRecords(String name){
		name = name.toLowerCase();
		return replays.get(name);
	}
	
	public String getPrefix() {
		return TextFormat.YELLOW + "[ReplaySystem]";
	}

	public String getPrefix(String c) {
		return getPrefix() + " §"+c;
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		clients.put(p.getName().toLowerCase(), new ReplayClient(p));
	}
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		getClient(p).quit();
	}

	public void startCapture(Player p) {
		getClient(p).startRecording();
		
	}
	
	public void stopCapture(Player p) {
		getClient(p).stopRecording();
		
	}
	
	public void viewRecord(Player p, Record record) {
		ViewTask replay = new ViewTask(p, record);
		getServer().getScheduler().scheduleRepeatingTask(replay, 20);
	}
	
	/*
	 * 
	 * 				EVENTS
	 * 
	 */
	
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		ReplayClient cl = getClient(p);
		if(!cl.isRecording()) return;
		
		BlockData blData = new BlockData();
		blData.preBlock = e.getBlock();
		blData.block = Block.get(0,0,e.getBlock());
		cl.addData((Data) blData);
	}
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		ReplayClient cl = getClient(p);
		if(!cl.isRecording()) return;
		
		BlockData blData = new BlockData();
		blData.block = e.getBlock();
		blData.preBlock = Block.get(0,0,e.getBlock());
		cl.addData((Data) blData);
	}
	
	@EventHandler
	public void receivePackets(DataPacketReceiveEvent e) {
		Player p = e.getPlayer();
		ReplayClient cl = getClient(p);
		if(!cl.isRecording()) return;
		
		DataPacket pk = e.getPacket();
		if(!ignoredPackets.contains(pk.pid())) {
			PacketData pkData = new PacketData();
			pkData.packet = pk;
			cl.addData((Data) pkData);
		}
	}
	
}