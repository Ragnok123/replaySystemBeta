package ru.ragnok123.replaySystemBeta.data;

import java.util.UUID;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;

public class RecordedPlayer	{
	public Position position;
	public double yaw;
	public double pitch;
	public long eid;
	public long rid;
	public UUID uuid;
	public Item itemInHand;
	public Skin skin;
}