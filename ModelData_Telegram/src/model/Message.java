package model;

import java.util.ArrayList;

public class Message {
	String picture;
	String video;
	String audio;
	String text;
	ArrayList<String> link;
	
	public Message() {
		
	}
	
	public Message(String picture, String video, String audio, String text) {
		super();
		this.picture = picture;
		this.video = video;
		this.audio = audio;
		this.text = text;
	}
	
	public Message(String text) {
		super();
		this.text = text;
		this.picture = null;
		this.video = null;
		this.audio = null;
	}
	
	public String getPicture() {
		return picture;
	}
	
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public String getVideo() {
		return video;
	}
	
	public void setVideo(String video) {
		this.video = video;
	}
	
	public String getAudio() {
		return audio;
	}
	
	public void setAudio(String audio) {
		this.audio = audio;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public ArrayList<String> getLink() {
		return link;
	}

	public void setLink(ArrayList<String> link) {
		this.link = link;
	}
	
	public void addLink(String link) {
		this.link.add(link);
	}
	
}
