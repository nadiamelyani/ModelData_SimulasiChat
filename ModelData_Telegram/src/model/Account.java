package model;

import java.io.IOException;
import java.util.ArrayList;

public class Account {
	String user_id;
	String name;
	String phone;
	String password;
	String username;
	String bio;
	String profil_pic;
	String group_id;
	ArrayList<String> chatroom_id = new ArrayList<>();
	ArrayList<Contact> contact = new ArrayList<>();
	ArrayList<Contact> blockedContact = new ArrayList<>();
	ArrayList<String> endchattedroom = new ArrayList<>();
	ArrayList<Chat> savedmessage = new ArrayList<>();
	
	public Account() {
		super();
	}
	
	public Account(String phone, String password) {
		this.phone = phone;
		this.password = password;
	}

	public Account(String phone, String password, ArrayList<Contact> contact) {
		this.phone = phone;
		this.password = password;
		this.contact = contact;
	}
	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getProfil_pic() {
		return profil_pic;
	}

	public void setProfil_pic(String profil_pic) {
		this.profil_pic = profil_pic;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public ArrayList<String> getChatroom_id() {
		return chatroom_id;
	}

	public void setChatroom_id(ArrayList<String> chatroom_id) {
		this.chatroom_id = chatroom_id;
	}

	public ArrayList<Contact> getContact() throws IOException {
		return contact;
	}

	public void setContact(ArrayList<Contact> contact) {
		this.contact = contact;
	}

	public ArrayList<Contact> getBlockedContact() {
		return blockedContact;
	}

	public void setBlockedContact(ArrayList<Contact> blockedContact) {
		this.blockedContact = blockedContact;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void addContact(Contact contact) {
		this.contact.add(contact);
	}
	
	public void removeContact(Contact contact) {
		this.contact.remove(contact);
	}
	
	public void addBlockedContact(Contact contact) {
		this.blockedContact.add(contact);
	}
	
	public void removeBlockedContact(Contact contact) {
		this.blockedContact.remove(contact);
	}
	
	public void addChatroom(String id) {
		this.chatroom_id.add(id);
	}
	
	public void removeChatroom(String id) {
		this.chatroom_id.remove(id);
	}
	
	public void addEndChatroom(String id) {
		this.endchattedroom.add(id);
	}
	
	public void removeEndChatroom(String id) {
		this.endchattedroom.remove(id);
	}

	public ArrayList<String> getEndchattedroom() {
		return endchattedroom;
	}

	public void setEndchattedroom(ArrayList<String> endchattedroom) {
		this.endchattedroom = endchattedroom;
	}
	
	public void addSavedMessage(Chat chat) {
		this.savedmessage.add(chat);
	}

	public ArrayList<Chat> getSavedmessage() {
		return savedmessage;
	}

	public void setSavedmessage(ArrayList<Chat> savedmessage) {
		this.savedmessage = savedmessage;
	}

}
