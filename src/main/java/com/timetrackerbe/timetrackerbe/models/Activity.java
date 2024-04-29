package com.timetrackerbe.timetrackerbe.models;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.internal.connection.Time;

@Document (collection = "Activities")
public class Activity {
	@Id
	private String id;
	private String activityName;
	private Time startTime;
	private Time endTime;
	private Time trackedTime;
	
	public Activity(String id, String activityName, Time startTime, Time endTime, Time trackedTime) {
		this.id = id;
		this.activityName = activityName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.trackedTime = trackedTime;
	}

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

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Time getTrackedTime() {
		return trackedTime;
	}

	public void setTrackedTime(Time trackedTime) {
		this.trackedTime = trackedTime;
	}

	
}
