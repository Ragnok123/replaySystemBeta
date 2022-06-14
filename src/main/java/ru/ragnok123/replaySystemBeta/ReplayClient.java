package ru.ragnok123.replaySystemBeta;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import ru.ragnok123.replaySystemBeta.data.RecordedPlayer;
import ru.ragnok123.replaySystemBeta.data.Record;

import java.util.*;

import javax.xml.crypto.Data;

public class ReplayClient {
	
	private Player player;
	
	public Map<Integer, List<Data>> replayTicks = new HashMap<Integer, List<Data>>();
	public Record record;

	public Integer currentTick = 0;
	public Integer currentReplay = 0;
	private boolean recording;
	
	public ReplayClient(Player p) {
		this.player = p;
	}
	
	public void startRecording() {
		this.recording = true;
		
		record = new Record();
		
		RecordedPlayer dat = new RecordedPlayer();
		dat.position = player.getPosition().clone();
		dat.yaw = player.getYaw();
		dat.pitch = player.getPitch();
		dat.eid = dat.rid = Entity.entityCount++;
		dat.uuid = UUID.randomUUID();
		dat.skin = player.getSkin();
		dat.itemInHand = player.getInventory().getItemInHand();
		record.recordedPlayer = dat;
	}
	
	public void stopRecording() {
		this.recording = false;
		this.currentTick = 0;
		
		record.replay = replayTicks;
		this.replayTicks = new HashMap<Integer, List<Data>>();
		this.currentReplay++;
		if(ReplaySystem.get().replays.get(this.player.getName().toLowerCase()).isEmpty()) {
			ArrayList<Record> replays = new ArrayList<Record>();
			replays.add(record);
			ReplaySystem.get().replays.put(this.player.getName().toLowerCase(), replays);
		} else {
			ReplaySystem.get().getRecords(this.player.getName()).add(record);
		}
		record = null;
	}
	
	public boolean isRecording() {
		return this.recording;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public List<Data> getReplayDatas(int tick){
		return this.replayTicks.get(tick);
	}
	
	public void addData(Data data) {
		this.replayTicks.get(this.currentTick).add(data);
	}
	
	public void quit() {
		if(isRecording()) {
			stopRecording();
		}
		ReplaySystem.get().clients.remove(this.player.getName().toLowerCase());
	}
	
}