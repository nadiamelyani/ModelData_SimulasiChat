package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Account;
import model.Chat;
import model.Chatroom;
import model.Contact;
import model.Message;
//import model.Student;

public class ActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String receiver;
	Contact beforeUpdate;
	String actionBefore;
	String chatId;
	String sender;
	static final int CHATROOM = 1;
	static final int UPDATE_CHATROOM = 2;
	public ActionController() {
		super();
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		System.out.println("ACTION = "+action);
		MongoDBUtils mongodbUtils = new MongoDBUtils();
		if("create".equals(action)){
			String phone = request.getParameter("phone");
			String name = request.getParameter("name");
			String password = request.getParameter("confirm_password");
			boolean result = mongodbUtils.createAccount(phone, name, password);
			if(result) {
				System.out.println("\nWelcome!");
				RequestDispatcher rd = request.getRequestDispatcher("/Login.jsp");
				rd.forward(request, response);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
				rd.forward(request, response);
			}
		} else if("Create Account".equals(action)) {
			RequestDispatcher rd = request.getRequestDispatcher("/NewAccount.jsp");
			rd.forward(request, response);
		} else if("Login".equals(action)) {
			String phone = request.getParameter("phone");
			String password = request.getParameter("password");
			boolean result = mongodbUtils.userExist(phone, password);
			if(result) {
				System.out.println("\nLogin Success");
				showChat(request, response, mongodbUtils);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
				rd.forward(request, response);
			}
		} else if("edit profile".equals(action)) {
			request.setAttribute("name", mongodbUtils.currentUser.getName());
			request.setAttribute("username", mongodbUtils.currentUser.getUsername());
			request.setAttribute("bio", mongodbUtils.currentUser.getBio());
			RequestDispatcher rd = request.getRequestDispatcher("/edit.jsp");
			rd.forward(request, response);
		} else if("submit edit profile".equals(action)) {
			String name = request.getParameter("name");
			String username = request.getParameter("username");
			String bio = request.getParameter("bio");
			boolean result = mongodbUtils.updateProfile(name, username, bio);
			if(result) {
				showProfile(request, response, mongodbUtils, mongodbUtils.currentUser.getPhone());
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
				rd.forward(request, response);
			}
		} else if("profile".equals(action) || "view profile".equals(action)) {
			String phone;
			if("view profile".equals(action)) {
				phone = request.getParameter("phone");
			} else {
				phone = MongoDBUtils.currentUser.getPhone();
			}
			showProfile(request, response, mongodbUtils, phone);
		} else if("new chat".equals(action) || "contact".equals(action)) {
			showAllData(request, response, mongodbUtils);
		} else if("add contact".equals(action)) {
			System.out.println("add a new contact");
			RequestDispatcher rd = request.getRequestDispatcher("/addContact.jsp");
			rd.forward(request, response);
		} else if("insert contact".equals(action)) {
			String phone = request.getParameter("phone");
			String name = request.getParameter("name");
			boolean result = mongodbUtils.newContact(phone, name);
			if(result) {
				showAllData(request, response, mongodbUtils);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
				rd.forward(request, response);
			}
		} else if("back".equals(action)) {
				RequestDispatcher rd = request.getRequestDispatcher("/Login.jsp");
				rd.forward(request, response);
		} else if("retrieve".equals(action)) {
//			RequestDispatcher rd = request.getRequestDispatcher("/contact.jsp");
//			rd.forward(request, response);
			showAllData(request, response, mongodbUtils);
		}  else if("chat".equals(action) || "continue chat".equals(action)) {
			receiver = request.getParameter("phone");
			actionBefore = request.getParameter("action");
			displayChat(request, response, mongodbUtils, receiver, CHATROOM);
			System.out.println("Starting a new chat..");
//			RequestDispatcher rd = request.getRequestDispatcher("/chatroom.jsp");
//			rd.forward(request, response);
		} else if("send message".equals(action)) {
			String text = request.getParameter("message");
			Message message = new Message(text);
			boolean result;
			if(actionBefore.equals("continue chat")) {
				result = mongodbUtils.continueChat(receiver, message);
			} else {
				result = mongodbUtils.NewChat(receiver, message);
			}
			if(result) {
				System.out.println("send success");
				displayChat(request, response, mongodbUtils, receiver, CHATROOM);
//				RequestDispatcher rd = request.getRequestDispatcher("/chatroom.jsp");
//				rd.forward(request, response);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
				rd.forward(request, response);
			}
		} else if("end chat".equals(action)) {
			String contactPhone = request.getParameter("phone");
			boolean result = mongodbUtils.endChat(contactPhone);
			if(result) {
				showChat(request, response, mongodbUtils);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
				rd.forward(request, response);
			}
		} else if("edit contact".equals(action)) {
			String contactName = request.getParameter("name");
			String contactPhone = request.getParameter("phone");
			beforeUpdate = new Contact(contactName, contactPhone);
			request.setAttribute("contactName", contactName);
			RequestDispatcher rd = request.getRequestDispatcher("/editContact.jsp");
			rd.forward(request, response);
		} else if("submit edit contact".equals(action)) {
			String contactName = request.getParameter("name");
			String contactPhone = beforeUpdate.getContactPhone();
			Contact afterUpdate = new Contact(contactName, contactPhone);
			boolean result = mongodbUtils.updateContact(beforeUpdate, afterUpdate);
			if(result) {
				showAllData(request, response, mongodbUtils);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
				rd.forward(request, response);
			}
		} else if("delete contact".equals(action)) {
			String contactName = request.getParameter("name");
			String contactPhone = request.getParameter("phone");
			Contact contact = new Contact(contactName, contactPhone);
			boolean result = mongodbUtils.deleteContact(contact.getContactName());
			if(result) {
				showAllData(request, response, mongodbUtils);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
				rd.forward(request, response);
			}
		} else if("main".equals(action)) {
			showChat(request, response, mongodbUtils);
		} else if("block".equals(action)) {
			String contactPhone = request.getParameter("phone");
			boolean result = mongodbUtils.blockContact(contactPhone);
			if(result) {
				showAllData(request, response, mongodbUtils);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
				rd.forward(request, response);
			}
		} else if("blocked contact".equals(action)) {
			showBlockedContact(request, response, mongodbUtils);
		} else if("back to contact".equals(action)) {
			showAllData(request, response, mongodbUtils);
		} else if("unblock".equals(action)) {
			String contactPhone = request.getParameter("phone");
			boolean result = mongodbUtils.unblockContact(contactPhone);
			if(result) {
				showAllData(request, response, mongodbUtils);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
				rd.forward(request, response);
			}
		} else if("delete chat".equals(action) || "unsend".equals(action)) {
			String chatId = request.getParameter("chatId");
			String sender = request.getParameter("sender");
			System.out.println(sender);
			boolean result = true;
			if("delete chat".equals(action)) {
				result = mongodbUtils.removeChat(MongoDBUtils.DELETE, chatId, sender, receiver);
			} else if("unsend".equals(action)){
				result = mongodbUtils.removeChat(MongoDBUtils.UNSEND, chatId, sender, receiver);
			}
			if(result) {
				displayChat(request, response, mongodbUtils, receiver, CHATROOM);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
				rd.forward(request, response);
			}
		} else if("select".equals(action)) {
			String sender = request.getParameter("sender");
			displayChat(request, response, mongodbUtils, receiver, UPDATE_CHATROOM);
		} else if("edit chat".equals(action)) {
			String sender = request.getParameter("sender");
			String chatId = request.getParameter("chatId");
			String chatBeforeUpdate = request.getParameter("chat");
			request.setAttribute("message", chatBeforeUpdate);
			request.setAttribute("chatId", chatId);
			request.setAttribute("sender", sender);
			RequestDispatcher rd = request.getRequestDispatcher("/editChat.jsp");
			rd.forward(request, response);
		} else if("finish".equals(action)) {
			String sender = request.getParameter("sender");
			String chatId = request.getParameter("chatId");
			String chat = request.getParameter("message");
			System.out.println(chat);
			boolean result = mongodbUtils.updateChat(chatId, sender, chat, receiver);
			if(result) {
				displayChat(request, response, mongodbUtils, receiver, CHATROOM);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
				rd.forward(request, response);
			}
		} else if("cancel".equals(action)) {
			displayChat(request, response, mongodbUtils, receiver, CHATROOM);
		} else if("log out".equals(action)) {
			receiver = null;
			beforeUpdate = null;
			actionBefore = null;
			chatId = null;
			sender = null;
			boolean result = mongodbUtils.logout();
			if(result) {
				System.out.println("\nWelcome!");
				RequestDispatcher rd = request.getRequestDispatcher("/Login.jsp");
				rd.forward(request, response);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
				rd.forward(request, response);
			}
		} else if("saved message".equals(action)) {
			showSavedMessage(request, response, mongodbUtils);
		} else if("send".equals(action)) {
			String text = request.getParameter("message");
			Message message = new Message(text);
			boolean result = mongodbUtils.savedMessage(message);
			if(result) {
				showSavedMessage(request, response, mongodbUtils);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
				rd.forward(request, response);
			}
		} else if("delete saved message".equals(action)) {
			mongodbUtils.deleteSavedMessage();
			showChat(request, response, mongodbUtils);
		} else if("search user".equals(action)) {
			String keyword = request.getParameter("user");
			showSearchUserResult(request, response, mongodbUtils, keyword);
		} else if("search contact".equals(action)) {
			String keyword = request.getParameter("contact_name");
			showSearchContactResult(request, response, mongodbUtils, keyword);
		} else if("links".equals(action)) {
			showUrls(request, response, mongodbUtils, receiver);
		} else if("data usage".equals(action)) {
			mongodbUtils.countDataUsage();
			showDataUsage(request, response, mongodbUtils);
			mongodbUtils.sendedMessage = 0;
			mongodbUtils.receivedMessage = 0;
		} else if("search message".equals(action)) {
			String keyword = request.getParameter("keyword");
			showSearchMessageResult(request, response, mongodbUtils, keyword, receiver);
		}
	}

	public void showAllData(HttpServletRequest request, HttpServletResponse response,
			MongoDBUtils mongodbUtils) {
		try {
			ArrayList<Contact> contactList = new ArrayList<>();
			contactList = mongodbUtils.currentUser.getContact();
			request.setAttribute("dataList", contactList);
			request.getRequestDispatcher("/contact.jsp").forward(request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showChat(HttpServletRequest request, HttpServletResponse response,
			MongoDBUtils mongodbUtils) {
		try {
			ArrayList<Contact> chattedList = new ArrayList<>();
			chattedList = mongodbUtils.getChattedContact();
			request.setAttribute("dataList", chattedList);
			System.out.println(chattedList);
			request.getRequestDispatcher("/main.jsp").forward(request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void displayChat(HttpServletRequest request, HttpServletResponse response,
			MongoDBUtils mongodbUtils, String phone, int type) {
		try {
			ArrayList<Chat> chatList = new ArrayList<>();
			chatList = mongodbUtils.getChat(phone);
			request.setAttribute("dataList", chatList);
			System.out.println(chatList);
			if(type == CHATROOM) {
				request.getRequestDispatcher("/chatroom.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/updateChatroom.jsp").forward(request, response);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showBlockedContact(HttpServletRequest request, HttpServletResponse response,
			MongoDBUtils mongodbUtils) {
		try {
			ArrayList<Contact> contactList = new ArrayList<>();
			contactList = mongodbUtils.currentUser.getBlockedContact();
			System.out.println(contactList);
			request.setAttribute("dataList", contactList);
			request.getRequestDispatcher("/blockedContact.jsp").forward(request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showProfile(HttpServletRequest request, HttpServletResponse response,
			MongoDBUtils mongodbUtils, String phone) {
		try {
			ArrayList<Account> accountl = new ArrayList<>();
			Account account = new Account();
			account = mongodbUtils.getAccount(phone);
			if(!phone.equals(mongodbUtils.currentUser.getPhone())) {
				Contact contact = mongodbUtils.getContact(phone);
				if(contact != null) {
					account.setName(contact.getContactName());
				}
			}
			System.out.println(account);
			accountl.add(account);
			request.setAttribute("dataList", accountl);
			request.getRequestDispatcher("/viewProfile.jsp").forward(request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showSearchContactResult(HttpServletRequest request, HttpServletResponse response,
			MongoDBUtils mongodbUtils, String keyword) {
		try {
			ArrayList<Contact> contactList = new ArrayList<>();
			contactList = mongodbUtils.searchContact(keyword);
			request.setAttribute("dataList", contactList);
			request.getRequestDispatcher("/searchContactResult.jsp").forward(request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showSearchUserResult(HttpServletRequest request, HttpServletResponse response,
			MongoDBUtils mongodbUtils, String keyword) {
		try {
			ArrayList<Account> accountList = new ArrayList<>();
			accountList = mongodbUtils.searchAccount(keyword);
			request.setAttribute("dataList", accountList);
			request.getRequestDispatcher("/searchUserResult.jsp").forward(request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showSearchMessageResult(HttpServletRequest request, HttpServletResponse response,
			MongoDBUtils mongodbUtils, String keyword, String receiver) {
		try {
			ArrayList<Chat> chatList = new ArrayList<>();
			chatList = mongodbUtils.searchWord(keyword, receiver);
			if(chatList == null) {
				request.getRequestDispatcher("/noResult.jsp").forward(request, response);
			} else {
				request.setAttribute("dataList", chatList);
				request.getRequestDispatcher("/searchWordResult.jsp").forward(request, response);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showUrls(HttpServletRequest request, HttpServletResponse response,
			MongoDBUtils mongodbUtils, String receiver) {
		try {
			ArrayList<String> urls = new ArrayList<>();
			urls = mongodbUtils.links(receiver);
			request.setAttribute("dataList", urls);
			request.getRequestDispatcher("/viewUrls.jsp").forward(request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public void showDataUsage(HttpServletRequest request, HttpServletResponse response,
			MongoDBUtils mongodbUtils) {
		try {
			long sended = mongodbUtils.sendedMessage;
			long received = mongodbUtils.receivedMessage;
			request.setAttribute("sended", sended);
			request.setAttribute("received", received);
			request.getRequestDispatcher("/viewDataUsage.jsp").forward(request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public void showSavedMessage(HttpServletRequest request, HttpServletResponse response,
			MongoDBUtils mongodbUtils) {
		try {
			ArrayList<Chat> messageList = mongodbUtils.currentUser.getSavedmessage();
			request.setAttribute("dataList", messageList);
			request.getRequestDispatcher("/savedMessageRoom.jsp").forward(request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}	
}