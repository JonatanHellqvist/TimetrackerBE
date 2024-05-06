package com.timetrackerbe.timetrackerbe.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "Users")
public class User {
	@Id
	private String id;
	private String userName;
	private String password;
	private boolean isAdmin = false;
	private List<Activity> activityList;
	
	private List<Activity> activityHistory;
	
	public User(String id, String userName, String password) {
		this.id = id;
		this.userName = userName;
		this.password = password;
	}

	public List<Activity> getActivityHistory() {
		return activityHistory;
	}

	public void setActivityHistory(List<Activity> activityHistory) {
		this.activityHistory = activityHistory;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public List<Activity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<Activity> activityList) {
		this.activityList = activityList;
	}
	
}
