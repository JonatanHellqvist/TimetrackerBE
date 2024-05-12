package com.timetrackerbe.timetrackerbe.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.timetrackerbe.timetrackerbe.models.Activity;

@Service
public class ActivityService {
	private final MongoOperations mongoOperations;

	public ActivityService(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public Activity addActivity(Activity activity) {
		return mongoOperations.insert(activity);
	}

	public void deleteActivity(String id) {
		Query query = Query.query(Criteria.where("id").is(id));
		mongoOperations.remove(query, Activity.class);
	}

	public List<Activity> getActivities() {
		return mongoOperations.findAll(Activity.class);
	}

	public Activity getActivity(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		
		return mongoOperations.findOne(query, Activity.class);
	}

	public String startActivity(String id) {
		Activity activity = getActivity(id);

		if (activity.getStartTime() == null) {
			activity.setStartTime(LocalDateTime.now());
			mongoOperations.save(activity);

			return "{Activity: " + activity.getActivityName() + " started}";
		} else {
			return "{Activity: " + activity.getActivityName() + " is already started}";
		}}

	public String stopActivity(String id) {
		Activity activity = getActivity(id);

		if (activity != null && activity.getStartTime() != null && activity.getEndTime() == null) {
			activity.setEndTime(LocalDateTime.now());

			Duration duration = Duration.between(activity.getStartTime(), activity.getEndTime());
			//Konvertera till timmar/minuter/sekunder
			//@@@@@@@@@@@@@ TODO fixa snyggare konvertering @@@@@@@@@@@@@
			long trackedDuration = duration.toMinutes();
			activity.setTrackedTime(trackedDuration);
			mongoOperations.save(activity);
			return "{Activity: " + activity.getActivityName() + " stopped after " + trackedDuration + "minutes!}";
		} else {
			return "{Activity: " + activity.getActivityName() + " is not started or already stopped}";
		}}
	}

	

	

