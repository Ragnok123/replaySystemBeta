package ru.ragnok123.replaySystemBeta.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.crypto.Data;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;

public class Record {
	
	public Map<Integer, List<Data>> replay = new HashMap<Integer, List<Data>>();
	public RecordedPlayer recordedPlayer;
	
}