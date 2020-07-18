package model;

import java.util.ArrayList;

public class Chatroom {
	String chatroom_id;
	ArrayList<Chat> chat = new ArrayList<>();
	
	public Chatroom() {
		
	}
	
	public Chatroom(String chatroom_id) {
		this.chatroom_id = chatroom_id;
	}
	
	public Chatroom(String chatroom_id, ArrayList<Chat> chat) {
		this.chatroom_id = chatroom_id;
		this.chat = chat;
	}

	public String getChatroom_id() {
		return chatroom_id;
	}

	public void setChatroom_id(String chatroom_id) {
		this.chatroom_id = chatroom_id;
	}

	public ArrayList<Chat> getChat() {
		return chat;
	}

	public void setChat(ArrayList<Chat> chat) {
		this.chat = chat;
	}
	
	public void addChat(Chat chat) {
		this.chat.add(chat);
	}
	
}
