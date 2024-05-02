package com.timetrackerbe.timetrackerbe.models;



import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.internal.connection.Time;

@Document (collection = "Activities")
public class Activity {
	@Id
	private String id;
	private String activityName;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Long trackedTime;

	//kanske boolean?
	// private boolean active;
	
	//TODO - Något vettigare sett att hantera tiden 


	public Activity(String id, String activityName) {

		this.id = id;
		this.activityName = activityName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.trackedTime = trackedTime;
	}

	public void setTrackedTime(Long trackedTime) {
		this.trackedTime = trackedTime;
	}

	// public boolean isActive() {
	// 	return active;
	// }

	// public void setActive(boolean active) {
	// 	this.active = active;
	// }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Long getTrackedTime() {
		return trackedTime;
	}

	public void setTrackedTime(long l) {
		this.trackedTime = l;
	}

	
}
