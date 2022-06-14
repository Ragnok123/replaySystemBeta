package ru.ragnok123.replaySystemBeta.task;

import cn.nukkit.scheduler.Task;
import ru.ragnok123.replaySystemBeta.ReplayClient;
import ru.ragnok123.replaySystemBeta.ReplaySystem;

public class CaptureTask extends Task {

	
	@Override
	public void onRun(int currentTick) {
		for(ReplayClient cl : ReplaySystem.get().clients.values()) {
			if(cl.isRecording()) {
				cl.currentTick++;
			}
		}
	}

}
