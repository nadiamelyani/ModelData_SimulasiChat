package model;

import java.util.ArrayList;

public class Group {
	String group_id;
	String group_name;
	int member;
	String group_invite_url;
	String profil_pic;
	ArrayList<String> admin = new ArrayList<>();
	ArrayList<String> memberList = new ArrayList<>();
	
	public Group(String group_id, String group_name, String Admin) {
		this.group_id = group_id;
		this.group_name = group_name;
		this.admin.add(Admin);
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public int getMember() {
		return member;
	}

	public void setMember(int member) {
		this.member = member;
	}

	public String getGroup_invite_url() {
		return group_invite_url;
	}

	public void setGroup_invite_url(String group_invite_url) {
		this.group_invite_url = group_invite_url;
	}

	public String getProfil_pic() {
		return profil_pic;
	}

	public void setProfil_pic(String profil_pic) {
		this.profil_pic = profil_pic;
	}

	public ArrayList<String> getAdmin() {
		return admin;
	}

	public void setAdmin(ArrayList<String> admin) {
		this.admin = admin;
	}

	public ArrayList<String> getMemberList() {
		return memberList;
	}

	public void setMemberList(ArrayList<String> memberList) {
		this.memberList = memberList;
	}
	
	
}
