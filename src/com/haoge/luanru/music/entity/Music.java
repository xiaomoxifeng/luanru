package com.haoge.luanru.music.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;


public class Music extends BmobObject implements Serializable {
	private String name;
	private String singer;
	private String author;
	public String getL_albumpic() {
		return L_albumpic;
	}
	public void setL_albumpic(String l_albumpic) {
		L_albumpic = l_albumpic;
	}
	public String getL_musicpath() {
		return L_musicpath;
	}
	public void setL_musicpath(String l_musicpath) {
		L_musicpath = l_musicpath;
	}


	private String composer;
	private String album;
	private String durationtime;
	private BmobFile albumpic;
	private BmobFile musicpath;
	//本地数据库用的
	private String L_albumpic;
	private String L_musicpath;
	
	public Music() {
		// TODO Auto-generated constructor stub
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getComposer() {
		return composer;
	}
	public void setComposer(String composer) {
		this.composer = composer;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public BmobFile getAlbumpic() {
		return albumpic;
	}
	public void setAlbumpic(BmobFile albumpic) {
		this.albumpic = albumpic;
	}
	public BmobFile getMusicpath() {
		return musicpath;
	}
	public void setMusicpath(BmobFile musicpath) {
		this.musicpath = musicpath;
	}
	public String getDurationtime() {
		return durationtime;
	}
	public void setDurationtime(String durationtime) {
		this.durationtime = durationtime;
	}
	public Music( String name, String singer, String author,
			String composer, String album, String L_albumpic, String L_musicpath,
			String durationtime) {
		super();
		this.name = name;
		this.singer = singer;
		this.author = author;
		this.composer = composer;
		this.album = album;
		this.L_albumpic = L_albumpic;
		this.L_musicpath = L_musicpath;
		this.durationtime = durationtime;
	}


	@Override
	public String toString() {
		return this.name;
	}

}
