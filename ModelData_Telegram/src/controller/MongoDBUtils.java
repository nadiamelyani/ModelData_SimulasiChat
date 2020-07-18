package controller;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;

import com.mongodb.AggregationOutput;
import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;

import model.Account;
import model.Contact;
import model.Chatroom;
import model.Chat;
import model.Message;

public class MongoDBUtils {
	MongoDatabase database;
	MongoCollection<Account> accountCollection;
	MongoCollection<Chatroom> chatroomCollection;
	static final int DELETE = 1;
	static final int UNSEND = 2;
	static Account currentUser;
	Contact selectedContact;
	static long sendedMessage;
	static long receivedMessage;
	
	public MongoDBUtils() {
		// Creating Credentials 
		MongoCredential credential; 
		credential = MongoCredential.createCredential("sampleUser", "telegramDb", 
				"password".toCharArray()); 
		System.out.println("Connected to the database successfully");  
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));		
		 MongoClient mongo = new MongoClient("localhost", MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
		// Accessing the database 
		database = mongo.getDatabase("telegramDb"); 
		database = database.withCodecRegistry(pojoCodecRegistry);
		System.out.println("Credentials ::"+ credential);
		accountCollection = database.getCollection("accountsCollection", Account.class);
		chatroomCollection = database.getCollection("chatroomCollection", Chatroom.class);
	}
	
	/*----------------------------
	 * Akun User Pengguna
	 * ---------------------------*/
	public ArrayList<Account> getUser() throws IOException {		
		ArrayList<Account> resultList = new ArrayList<>();
		FindIterable<Account> userIterable = accountCollection.find();
		for (Account user : userIterable) {
			resultList.add(user);
		}		
		return resultList;
	}
	
	public boolean userExist(String phone, String password) {
		ArrayList<Account> accountList = new ArrayList<>();
		boolean exist = false;
		Account temp = null;
		try {
			accountList = getUser();
			Iterable<Account> userIter = accountList;
			for(Account user : userIter) {
				if(phone.equals(user.getPhone())) {
					if(password.equals(user.getPassword())){
						temp = user;
						exist = true;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		currentUser = temp;
		return exist;
	}
	
	public Account getAccount(String phone) {
		ArrayList<Account> accountList = new ArrayList<>();
		boolean exist = false;
		Account account = null;
		try {
			accountList = getUser();
			Iterable<Account> userIter = accountList;
			for(Account user : userIter) {
				if(phone.equals(user.getPhone())) {
					account = user;
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return account;
	}
	
	public boolean createAccount(String phone, String name, String password) {
		String id = new ObjectId().toString();
		try {
			Account account = new Account(phone, password);
			account.setName(name);
			account.setUser_id(id);
			accountCollection.insertOne(account);
			System.out.println("account created");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateProfile(String name, String username, String bio) {
		try {	
			accountCollection.updateOne(
					Filters.eq("user_id", currentUser.getUser_id()),
	                Updates.combine(Updates.set("name", name), 
	                		Updates.set("username", username), Updates.set("bio", bio)));		
			System.out.println("data updated");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	public boolean logout() {
		this.currentUser = null;
		this.selectedContact = null;
		return true;
	}
	
	/*----------------------------
	 * Kontak
	 * ---------------------------*/
	public boolean newContact(String phone, String name) {
		try {
			Contact contact = new Contact(name, phone);
			currentUser.addContact(contact);
			accountCollection.updateOne(
					Filters.eq("user_id", currentUser.getUser_id()),
	                Updates.combine(Updates.set("contact", currentUser.getContact())));
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Contact getContact(String phone) {
		ArrayList<Contact> contactList = new ArrayList<>();
		Contact contact = new Contact();
		boolean blockedContact = isBlockedContact(phone);
		try {
			if(!blockedContact) {
				contactList = currentUser.getContact();
			} else {
				contactList = currentUser.getBlockedContact();
			}
			Iterable<Contact> iter = contactList;
			for(Contact tContact : iter) {
				if(phone.equals(tContact.getContactPhone())) {
					contact = tContact;
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return contact;
	}
	
	public ArrayList<Contact> getChattedContact() throws IOException {
		ArrayList<Contact> chattedList = new ArrayList<>();
		FindIterable<Account> userIter = accountCollection.find();
		Iterable<String> chatroomUserId = currentUser.getChatroom_id();
		Contact chattedContact = new Contact();
		for(String chatroomId : chatroomUserId) {
			for(Account chattedUser : userIter) {
				if(chattedUser != currentUser) {
					Iterable<String> chatroomChattedUser = chattedUser.getChatroom_id();
					for(String chatroomChattedId : chatroomChattedUser) {
						if(chatroomId.equals(chatroomChattedId)) {
							chattedContact = getContact(chattedUser.getPhone());
							if(chattedContact.getContactName() != null) {
								chattedList.add(chattedContact);
							} else if(!chattedUser.getName().equals(currentUser.getName())){
								String name = chattedUser.getName();
								String phone = chattedUser.getPhone();
								Contact contact = new Contact(name, phone);
								chattedList.add(contact);
							}
						}
					}
				}
			}
		}
		return chattedList;
	}
	
	public boolean updateContact(Contact beforeUpdate, Contact afterUpdate) {
		try {	
			//boolean deleteContact = deleteContact(beforeUpdate.getContactName());
			Contact contact = getContact(afterUpdate.getContactPhone());
			contact.setContactName(afterUpdate.getContactName());
			//currentUser.addContact(afterUpdate);
			accountCollection.updateOne(
					Filters.eq("user_id", currentUser.getUser_id()),
	                Updates.combine(Updates.set("contact", currentUser.getContact())));
			System.out.println("data updated");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
		return true;
	}
	
	public boolean deleteContact(String name) {
		ArrayList<Contact> contactList = new ArrayList<>();
		Contact deletedContact = new Contact();
		try {
			Iterable<Contact> iter = currentUser.getContact();
			for(Contact contact : iter) {
				if(name.equals(contact.getContactName())) {
					deletedContact = contact;
					currentUser.removeContact(deletedContact);
					accountCollection.updateOne(
							Filters.eq("user_id", currentUser.getUser_id()),
			                Updates.combine(Updates.set("contact", currentUser.getContact())));
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean blockContact(String phone) {
		try {
			Contact contact = getContact(phone);
			currentUser.addBlockedContact(contact);
			currentUser.removeContact(contact);
			accountCollection.updateOne(
					Filters.eq("user_id", currentUser.getUser_id()),
	                Updates.combine(Updates.set("contact", currentUser.getContact())));
			accountCollection.updateOne(
					Filters.eq("user_id", currentUser.getUser_id()),
	                Updates.combine(Updates.set("blockedContact", currentUser.getBlockedContact())));
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean unblockContact(String phone) {
		try {
			Contact contact = getContact(phone);
			currentUser.removeBlockedContact(contact);
			currentUser.addContact(contact);
			accountCollection.updateOne(
					Filters.eq("user_id", currentUser.getUser_id()),
	                Updates.combine(Updates.set("contact", currentUser.getContact())));
			accountCollection.updateOne(
					Filters.eq("user_id", currentUser.getUser_id()),
	                Updates.combine(Updates.set("blockedContact", currentUser.getBlockedContact())));
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean isBlockedContact(String phone) {
		boolean exist = false;
		try {
			Iterable<Contact> iter = currentUser.getBlockedContact();
			for(Contact contact : iter) {
				if(phone.equals(contact.getContactPhone())) {
					exist = true;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return exist;
	}
	
	/*-----------------------------
	 * Chatroom
	 * ---------------------------*/
	
	public Chatroom getChatroom(String chatroomId) throws IOException {
		Chatroom chatroom = chatroomCollection.find(Filters.eq("chatroom_id", chatroomId)).first();
		return chatroom;
	}
	

	public String getChatroomId(String phone) {
		Account receiver = getAccount(phone);
		String idChatroom = "";
		Iterable<String> chatroomUserId = currentUser.getChatroom_id();
		Iterable<String> chatroomReceiverId = receiver.getChatroom_id();
		for(String chatroomUser : chatroomUserId) {
			for(String chatroomReceiver : chatroomReceiverId) {
				if(chatroomUser.equals(chatroomReceiver)) {
					idChatroom = chatroomUser;
						break;
				}
			}
		}
		return idChatroom;
	}
	
	public boolean NewChat(String phone, Message message) {
		String cht_id = new ObjectId().toString();
		String chtroom_id = new ObjectId().toString();
		Date date = new Date();
		boolean newChat = isNewChat(phone);
		boolean result = true;
		if(newChat) {
			try {
				message.setLink(extractUrls(message.getText()));
				Chat chat = new Chat(cht_id, currentUser.getPhone(), message, date);
				chat.setDelete_status(false);
				chat.setUnsend_status(false);
				Chatroom chatroom = new Chatroom(chtroom_id);
				Account receiver = getAccount(phone);
				receiver.addChatroom(chtroom_id);
				currentUser.addChatroom(chtroom_id);
				System.out.println("sending message.." + message.getText());
				accountCollection.updateOne(
						Filters.eq("user_id", receiver.getUser_id()),
		                Updates.combine(Updates.set("chatroom_id", receiver.getChatroom_id())));
				accountCollection.updateOne(
						Filters.eq("user_id", currentUser.getUser_id()),
		                Updates.combine(Updates.set("chatroom_id", currentUser.getChatroom_id())));
				chatroomCollection.insertOne(chatroom);
				chatroom.addChat(chat);
				chatroomCollection.updateOne(
						Filters.eq("chatroom_id", chtroom_id),
			            Updates.combine(Updates.set("chat", chatroom.getChat())));
			} catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			result = continueChat(phone, message);
		}
		return result;
	}
	
	public boolean continueChat(String phone, Message message) {
		String chatroomId = getChatroomId(phone);
		String chat_id = new ObjectId().toString();
		Date date = new Date();
		try {
			if(!isBlockedContact(phone)) {
				message.setLink(extractUrls(message.getText()));
				Chat chat = new Chat(chat_id, currentUser.getPhone(), message, date);
				chat.setDelete_status(false);
				chat.setUnsend_status(false);
				Chatroom chatroom = chatroomCollection.find(Filters.eq("chatroom_id", chatroomId)).first();
				chatroom.addChat(chat);
				chatroomCollection.updateOne(
					Filters.eq("chatroom_id", chatroomId),
		            Updates.combine(Updates.set("chat", chatroom.getChat())));
			} else {
				return false;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ArrayList<Chat> getChat(String phone) {
		String chatroomId = getChatroomId(phone);
		ArrayList<Chat> chatList = new ArrayList<>();
		try {
			Chatroom chatroom = getChatroom(chatroomId);
			//if chatroom is not a new chat
			if(chatroom != null) {
				if(isEndedChat(chatroomId) == false) {
					Iterable<Chat> chatIter = chatroom.getChat();
					for(Chat chat: chatIter) {
						if(chat.isUnsend_status() == false) {
							if(chat.getSender().equals(currentUser.getPhone())) {
								if(!chat.isDelete_status()) {
									chat.setSender(currentUser.getName());
									chatList.add(chat);
								}
							} else {
								if(getContact(phone).getContactName() != null) {
									chat.setSender(getContact(phone).getContactName());
									chatList.add(chat);
								} else {
									chat.setSender(getAccount(phone).getName());
									chatList.add(chat);
								}
								
							}
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return chatList;
	}
	
	//Kode ini belum bekerja dengan seharusnya
	public boolean endChat(String phone) {
		String chatroomId = getChatroomId(phone);
		ArrayList<String> chatroomList = new ArrayList<>();
		chatroomList = currentUser.getChatroom_id();
		Iterator<String> iter = chatroomList.iterator();
		Chatroom chatroom;
		try {
			while(iter.hasNext()) {
				if(chatroomId.equals(iter.next())) {
					System.out.println(chatroomId);
					currentUser.removeChatroom(chatroomId);
					currentUser.addEndChatroom(chatroomId);
					chatroom = getChatroom(chatroomId);
					accountCollection.updateOne(
							Filters.eq("user_id", currentUser.getUser_id()),
			                Updates.combine(Updates.set("chatroom_id", currentUser.getChatroom_id())));
					accountCollection.updateOne(
							Filters.eq("user_id", currentUser.getUser_id()),
			                Updates.combine(Updates.set("endchattedroom", currentUser.getEndchattedroom())));
					System.out.println("==chat ended succesfully==");
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean isEndedChat(String chatroomId) {
		Iterable<String> iter = currentUser.getEndchattedroom();
		boolean endedChat = false;
		for(String id : iter) {
			if(chatroomId.equals(id)) {
				endedChat = true;
			}
		}
		return endedChat;
	}
	
	public boolean isNewChat(String phone) {
		boolean newchat = true;
		try {
			ArrayList<Contact> chattedList = getChattedContact();
			Iterable<Contact> iter = chattedList;
			for(Contact chattedContact : iter) {
				if(phone.equals(chattedContact.getContactPhone())) {
					newchat = false;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return newchat;
	}
	
	public boolean removeChat(int type, String chatId, String sender, String receiver) {
		String chatroomId = getChatroomId(receiver);
		try {
			Chatroom chatroom = getChatroom(chatroomId);
			ArrayList<Chat> chatList = chatroom.getChat();
			Iterable<Chat> iter = chatList;
			for(Chat chat : iter) {
				if(chatId.equals(chat.getChat_id())) {
					if(sender.equals(currentUser.getName())) {
						if(type == DELETE) {
							chat.setDelete_status(true);
							System.out.println("deleting chat..");
						} else if(type == UNSEND){
							chat.setUnsend_status(true);
							System.out.println("unsend chat");
						}
						System.out.println(type);
					} else {
						return false;
					}
					break;
				}
			}
			chatroomCollection.updateOne(
					Filters.eq("chatroom_id", chatroomId),
	                Updates.combine(Updates.set("chat", chatroom.getChat())));
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateChat(String chatId, String sender, String text, String receiver) {
		Message message = new Message(text);
		System.out.println("UPDATING " + message.getText() + " " + text);
		String chatroomId = getChatroomId(receiver);
		try {
			Chatroom chatroom = getChatroom(chatroomId);
			ArrayList<Chat> chatList = chatroom.getChat();
			Iterable<Chat> iter = chatList;
			for(Chat chat : iter) {
				if(chatId.equals(chat.getChat_id())) {
					if(sender.equals(currentUser.getName())) {
						chat.setMessage(message);
						System.out.println("EDITED. " + message.getText());
					} else {
						System.out.println("false");
						return false;
					}
					break;
				}
			}
			chatroomCollection.updateOne(
					Filters.eq("chatroom_id", chatroomId),
	                Updates.combine(Updates.set("chat", chatroom.getChat())));
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;	
	}
	
	/*----------------------------
	 * Saved Message
	 * ---------------------------*/
	
	public boolean savedMessage(Message message) {
		String chat_id = new ObjectId().toString();
		Date date = new Date();
		try {
			Chat chat = new Chat(chat_id, currentUser.getName(), message, date);
			currentUser.addSavedMessage(chat);
			accountCollection.updateOne(
					Filters.eq("user_id", currentUser.getUser_id()),
	                Updates.combine(Updates.set("savedmessage", currentUser.getSavedmessage())));
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteSavedMessage() {
		currentUser.getSavedmessage().clear();
		try {
			accountCollection.updateOne(
					Filters.eq("user_id", currentUser.getUser_id()),
	                Updates.combine(Updates.set("savedmessage", currentUser.getSavedmessage())));
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	/*----------------------------
	 * Queries
	 * ---------------------------*/
	
	public ArrayList<Contact> searchContact(String name) {
		ArrayList<Contact> searchResult = new ArrayList<>();
		boolean found;
		try {
			Iterable<Contact> contactIter = currentUser.getContact();
			for(Contact contact : contactIter) {
				found = contact.getContactName().matches(".*" + Pattern.quote(name) + ".*");
				if(found) {
					searchResult.add(contact);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return searchResult;
	}
	
	public ArrayList<Account> searchAccount(String username) {
		ArrayList<Account> account = new ArrayList<>();
		ArrayList<Account> newAccountList = new ArrayList<>();
		try {
			Pattern regex = Pattern.compile(".*" + Pattern.quote(username) + ".*", Pattern.CASE_INSENSITIVE);
			FindIterable<Account> accountUsername = accountCollection.find(
					Filters.eq("username", regex));
			FindIterable<Account> accountName = accountCollection.find(
					Filters.eq("name", regex));
			for(Account acc : accountUsername) {
				account.add(acc);
			}
			for(Account acc : accountName) {
				account.add(acc);
			}
			
			newAccountList = removeDuplicates(account);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return newAccountList;
	}

	public ArrayList<String> links(String receiver) {
//		String chatroomId = getChatroomId(receiver);
//		//MongoCollection<Document> chatrooms = database.getCollection("chatroomCollection");
//		Bson match = match(Filters.eq("chat.chat_id", chatroomId));
//		//Bson group = group("$chat.message.link");
//		Bson project = project(fields(excludeId(), include("chat.message.link")));
////		ArrayList<Document> results = chatrooms.aggregate(Arrays.asList(match, group, project)).into(new ArrayList<>());
////		results.forEach(printDocuments());
//		System.out.println(chatroomCollection.aggregate(Arrays.asList(match, project)).toString());
//		//List<DBObject> pipeline;
//		AggregateIterable<Chatroom> output = chatroomCollection.aggregate(Arrays.asList(match, project));
//		
		ArrayList<String> links = new ArrayList<>();
		String chatroomId = getChatroomId(receiver);
		try {
			Chatroom chatroom = getChatroom(chatroomId);
			Iterable<Chat> chatIter = chatroom.getChat();
			for(Chat chat : chatIter) {
				if(chat.getMessage().getLink() != null) {
					Iterable<String> linkIter = chat.getMessage().getLink();
					for(String link : linkIter) {
						links.add(link);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return links;
	}
	
	public ArrayList<Chat> searchWord(String word, String receiver) {
		ArrayList<Chat> searchResult = new ArrayList<>();
		String chatroomId = getChatroomId(receiver);
		Contact contact = getContact(receiver);
		Account account = getAccount(receiver);
		boolean contain;
		try {
			Chatroom chatroom = getChatroom(chatroomId);
			Iterable<Chat> chatIter = chatroom.getChat();
			for(Chat chat : chatIter) {
				contain = chat.getMessage().getText().contains(word);
				if(contain) {
					if(chat.getSender().equals(currentUser.getPhone())) {
						chat.setSender(currentUser.getName());
					} else if(contact.getContactName() != null){
						chat.setSender(contact.getContactName());
					} else {
						chat.setSender(account.getName());
					}
					searchResult.add(chat);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return searchResult;
	}
	
	public void countDataUsage() {
		Iterable<String> idIter = currentUser.getChatroom_id();
		for(String id : idIter) {
			Chatroom chatroom = chatroomCollection.find(Filters.eq("chatroom_id", id)).first();
			ArrayList<Chat> chatlist = chatroom.getChat();
			sendedMessage = sendedMessage + chatlist.stream().filter(c -> c.getSender().equals(currentUser.getPhone())).count();
			receivedMessage = receivedMessage + chatlist.stream().filter(c -> !c.getSender().equals(currentUser.getPhone())).count();
		}
		System.out.println(sendedMessage);
		System.out.println(receivedMessage);
	}
	
	
	/*----------------------------
	 * Method for extracting URL from a string
	 * source : https://stackoverflow.com/questions/5713558/detect-and-extract-url-from-a-string
	 * 			https://stackoverflow.com/questions/3809401/what-is-a-good-regular-expression-to-match-a-url
	 * ---------------------------*/
	public static ArrayList<String> extractUrls(String text) {
	    ArrayList<String> containedUrls = new ArrayList<String>();
	    String urlRegex = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]"
	    		+ "+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\."
	    		+ "[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}"
	    		+ "|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
	    Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
	    Matcher urlMatcher = pattern.matcher(text);

	    while(urlMatcher.find()) {
	        containedUrls.add(text.substring(urlMatcher.start(0),
	                urlMatcher.end(0)));
	    }
	    return containedUrls;
	}
	
	/*----------------------------
	 * Method for remove duplication in ArrayList
	 * source : https://www.geeksforgeeks.org/how-to-remove-duplicates-from-arraylist-in-java/
	 * ---------------------------*/
    public static <T>ArrayList<T> removeDuplicates(ArrayList<T> list) 
    { 
  
        // Create a new LinkedHashSet 
        Set<T> set = new LinkedHashSet<>(); 
  
        // Add the elements to set 
        set.addAll(list); 
  
        // Clear the list 
        list.clear(); 
  
        // add the elements of set 
        // with no duplicates to the list 
        list.addAll(set); 
  
        // return the list 
        return list; 
    } 
}
