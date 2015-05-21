package com.haoge.luanru.music.service;

import java.io.File;

import com.haoge.luanru.music.entity.Music;
import com.haoge.luanru.music.util.BroadcastActions;

import android.app.IntentService;
import android.content.Intent;

public class DeleteFileService extends IntentService implements
		BroadcastActions {

	public DeleteFileService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public DeleteFileService() {
		super("");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Music music = (Music) intent.getSerializableExtra("m");
		boolean flag = new File(music.getL_albumpic()).delete();
		if (flag) {
			flag = new File(music.getL_musicpath()).delete();
		}
		if (flag) {
			sendBroadcast(new Intent().setAction(SERVICE_DELETE_FILE));
		}
	}

}
