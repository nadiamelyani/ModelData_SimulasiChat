package model;

import java.util.Date;

public class Reply {
	String reply_id;
	String message_id; //reference to chat id
	String sender;
	Message message;
	Date timestamp;
	public Reply(String reply_id, String message_id, String sender, 
			Message message, Date timestamp) {
		super();
		this.reply_id = reply_id;
		this.message_id = message_id;
		this.sender = sender;
		this.message = message;
		this.timestamp = timestamp;
	}
	
	public String getReply_id() {
		return reply_id;
	}
	
	public void setReply_id(String reply_id) {
		this.reply_id = reply_id;
	}
	
	public String getMessage_id() {
		return message_id;
	}
	
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
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
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
