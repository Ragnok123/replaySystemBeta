package ru.ragnok123.replaySystemBeta.task;

import java.util.List;
import java.util.Map;

import javax.xml.crypto.Data;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.PlayerSkinPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.scheduler.Task;
import ru.ragnok123.replaySystemBeta.data.RecordedPlayer;
import ru.ragnok123.replaySystemBeta.data.Record;
import ru.ragnok123.replaySystemBeta.data.impl.BlockData;
import ru.ragnok123.replaySystemBeta.data.impl.PacketData;

public class ViewTask extends Task {
	
	private Player player;
	private Map<Integer, List<Data>> replay;

	public ViewTask(Player p, Record record) {
		this(p, record.recordedPlayer, record.replay);
	}
	
	public ViewTask(Player p, RecordedPlayer dat, Map<Integer, List<Data>> replay) {
		this.player = p;
		this.replay = replay;
		
		AddPlayerPacket add = new AddPlayerPacket();
		add.x = (float) dat.position.getX();
		add.y = (float) dat.position.getY();
		add.z = (float) dat.position.getZ();
		add.yaw = (float) dat.yaw;
		add.pitch = (float) dat.pitch;
		add.entityRuntimeId = dat.eid;
		add.entityRuntimeId = dat.rid;
		add.uuid = dat.uuid;
		add.item = dat.itemInHand;
		player.dataPacket(add);
		
		PlayerSkinPacket skin = new PlayerSkinPacket();
		skin.uuid = dat.uuid;
		skin.skin = dat.skin;
		player.dataPacket(skin);
		
		List<Data> tick = replay.get(0);
		for(Data data : tick) {
			if(data instanceof BlockData) {
				BlockData blData = (BlockData)data;
				
				UpdateBlockPacket pk = new UpdateBlockPacket();
				pk.x = (int) blData.preBlock.getX();
				pk.y = (int) blData.preBlock.getY();
				pk.z = (int) blData.preBlock.getZ();
				pk.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(blData.preBlock.getId(),0);
				pk.flags = UpdateBlockPacket.FLAG_NONE;
				
				this.player.dataPacket(pk);
			}
		}
	}
	
	@Override
	public void onRun(int currentTick) {
		int i = 0;
		i++;
		List<Data> tick = replay.get(i);
		for(Data data : tick) {
			if(data instanceof PacketData) {
				PacketData pkData = (PacketData)data;
				DataPacket pk = pkData.packet;
				if(!(pk instanceof UpdateBlockPacket)) {
					this.player.dataPacket(pk);
				}
			} else if(data instanceof BlockData) {
				BlockData blData = (BlockData)data;
				
				UpdateBlockPacket pk = new UpdateBlockPacket();
				pk.x = (int) blData.block.getX();
				pk.y = (int) blData.block.getY();
				pk.z = (int) blData.block.getZ();
				pk.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(blData.block.getId(),0);
				pk.flags = UpdateBlockPacket.FLAG_NONE;
				
				this.player.dataPacket(pk);
			}
		}
	}

}
