package model;

import java.util.ArrayList;
import java.util.Date;
public class Chat {
	String chat_id;
	String sender;
	Message message;
	boolean sender_delete;
	boolean unsend_status;

	Date timestamp;
	ArrayList<String> reply = new ArrayList<>();
	
	public Chat() {
		
	}
	
	public Chat(String chat_id, String sender, Message message, Date timestamp) {
		this.chat_id = chat_id;
		this.sender = sender;
		this.message = message;
		this.timestamp = timestamp;
	}

	public String getChat_id() {
		return chat_id;
	}

	public void setChat_id(String chat_id) {
		this.chat_id = chat_id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public boolean isMessage_status() {
		return sender_delete;
	}

	public void setMessage_status(boolean sender_delete) {
		this.sender_delete = sender_delete;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public ArrayList<String> getReply() {
		return reply;
	}

	public void setReply(ArrayList<String> reply) {
		this.reply = reply;
	}
	
	public boolean isDelete_status() {
		return sender_delete;
	}

	public void setDelete_status(boolean sender_delete) {
		this.sender_delete = sender_delete;
	}

	public boolean isUnsend_status() {
		return unsend_status;
	}

	public void setUnsend_status(boolean unsend_status) {
		this.unsend_status = unsend_status;
	}	
	
}
