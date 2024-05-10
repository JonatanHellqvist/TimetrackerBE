package com.timetrackerbe.timetrackerbe.models;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document (collection = "Users")
public class User {
	@Id
	private String id;
	private String userName;

	//Json ignore för att inte skicka med det i json responset jsonproperty på settern
	@JsonIgnore
	private String password;
	@JsonIgnore
	private String passwordHash;

	private boolean isAdmin = false;
	private List<Activity> activityList = new ArrayList<>();
	
	private List<Activity> activityHistory = new ArrayList<>();
	private Long totalTrackedTime = 0l;
	
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

	public Long getTotalTrackedTime() {
		return totalTrackedTime;
	}

	public void setTotalTrackedTime(Long totalTrackedTime) {
		this.totalTrackedTime = totalTrackedTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return isAdmin;
	}
	@JsonIgnore
	public String getPasswordHash() {
		return passwordHash;
	}
	@JsonProperty
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
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
