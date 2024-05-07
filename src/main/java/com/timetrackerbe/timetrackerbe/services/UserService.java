package com.timetrackerbe.timetrackerbe.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.timetrackerbe.timetrackerbe.models.Activity;
import com.timetrackerbe.timetrackerbe.models.User;

@Service
public class UserService {
	private final MongoOperations mongoOperations;

	public UserService(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}
	
	public List<User> getUsers() {
		return mongoOperations.findAll(User.class);
	}

	public User getUser(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		
		return mongoOperations.findOne(query, User.class);
	}
	
	public User addUser(User user) {
		return mongoOperations.insert(user);
	}

	public void deleteUser(String id) {
		Query query = Query.query(Criteria.where("id").is(id));
		mongoOperations.remove(query, User.class);
	}

	public User getUserByUsername(String username) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userName").is(username));
		return mongoOperations.findOne(query, User.class);
	}

	public User adminToggle(User user) {
        Query query = new Query();
		query.addCriteria(Criteria.where("userName").is(user.getUserName()));
        user.setAdmin(!user.isAdmin());
        mongoOperations.save(user);
        return user;
    }


	public List<Activity> getUserActivities(String userId) {
        User user = mongoOperations.findById(userId, User.class);
        if (user != null) {
            return user.getActivityList();
        }
        return null;
    }

	public List <Activity> getUserActivityHistory(String userId) {
		User user = mongoOperations.findById(userId, User.class);
		if (user != null) {
            return user.getActivityHistory();
        }
        return null;
	}

	public Activity addUserActivity(String userId, Activity activity) {
        User user = mongoOperations.findById(userId, User.class);
        if (user != null) {
            List<Activity> activityList = user.getActivityList();
			if (activityList == null) {
				activityList = new ArrayList<>();
			}
            activityList.add(activity);
            user.setActivityList(activityList);
			mongoOperations.insert(activity);
            mongoOperations.save(user);
            return activity;
        }
        return null;
    }


	// @PostMapping("/{userId}/list/addactivity")
    // public Activity addUserActivity(@PathVariable String userId, @RequestBody Activity activity) {
    //     return userService.addUserActivity(userId, activity);
    // }


	//starta

	public String startUserActivity(String userId,String activityId) {

		User user = mongoOperations.findById(userId, User.class);
		if (user != null) {
			List <Activity> activityList = user.getActivityList();
			for (Activity activity : activityList) {
				if (activity.getId().equals(activityId)) {
					if (activity.getStartTime() == null) {
						activity.setStartTime(LocalDateTime.now());
						mongoOperations.save(user);
						return "{Activity: " + activity.getActivityName() + " started}";
					} else {
						return "{Activity: " + activity.getActivityName() + " is already started}";
					}
				}
			} 
			return "{Activity not found}";
		} 
		return  "{User not found}";
	}

	//återuppta från historiken
	public String startUserActivityFromHistory(String userId,String activityId) {

		User user = mongoOperations.findById(userId, User.class);
		if (user != null) {
			List <Activity> activityHistory = user.getActivityHistory();
			List <Activity> activityList = user.getActivityList();
			for (Activity activity : activityHistory) {
				if (activity.getId().equals(activityId)) {
					
						activity.setEndTime(null);
						activity.setStartTime(LocalDateTime.now());
						activityList.add(activity);
						activityHistory.remove(activity);

						mongoOperations.save(user);
						return "{Activity: " + activity.getActivityName() + " started and moved to active}";
					} 
				}
			
			return "{Activity not found}";
		} 
		return  "{User not found}";
	}

	//stoppa

	public String stopUserActivity(String userId,String activityId) {

		User user = mongoOperations.findById(userId, User.class);

		if (user != null) {

			List <Activity> activityList = user.getActivityList();
			List <Activity> activityHistory = user.getActivityHistory();
			if (activityHistory == null) {
				activityHistory = new ArrayList<>();
			}

			for (Activity activity : activityList) {
				if (activity.getId().equals(activityId)) {
					if (activity.getEndTime() == null) {
						activity.setEndTime(LocalDateTime.now());

//@@@@@@@@@@@@@ TODO fixa snyggare konvertering @@@@@@@@@@@@@

						//uträkning för tid
						Duration duration = Duration.between(activity.getStartTime(), activity.getEndTime());
						long trackedDuration = duration.toMinutes();

						Long newtotalTrackedTime = user.getTotalTrackedTime() + activity.getTrackedTime() + trackedDuration;

						activity.setTrackedTime(activity.getTrackedTime() + trackedDuration);

						activityList.remove(activity);
						activityHistory.add(activity);

						user.setActivityList(activityList);
						user.setActivityHistory(activityHistory);
						user.setTotalTrackedTime(newtotalTrackedTime);
						
						mongoOperations.save(user);

						return "{Activity: " + activity.getActivityName() + " stopped after " + trackedDuration + "minutes!}";
					} else {
						return "{Activity: " + activity.getActivityName() + " is not started or already stopped}";
					}
				}
			} 
			return "{Activity not found}";
		} 
		return  "{User not found}";
	}
		



//gettotaltrackedtime

	public Long getUserTotalTrackedTime(String userId) {

		User user = mongoOperations.findById(userId, User.class);
		Long totalTrackedTime = 0l;

		if (user != null) {
			List <Activity> activityList = user.getActivityList();
			List <Activity> activityHistory = user.getActivityHistory();

			totalTrackedTime += addTotalTrackedTime(activityList);
			totalTrackedTime += addTotalTrackedTime(activityHistory);

			user.setTotalTrackedTime(totalTrackedTime);
			mongoOperations.save(user);

		}
		return totalTrackedTime;
	}

	private Long addTotalTrackedTime(List<Activity> activities) {
		Long totalTrackedTime = 0l;
		for (Activity activity : activities) {
			Long trackedTime = activity.getTrackedTime();
			// kollar så värdet inte är null innan det läggs till
			if (trackedTime != null) {
				totalTrackedTime += trackedTime;
			}
			
			
		}
		return totalTrackedTime;
	}
}
// 	if (activity != null && activity.getStartTime() != null && activity.getEndTime() == null) {
// 		activity.setEndTime(LocalDateTime.now());

// 		Duration duration = Duration.between(activity.getStartTime(), activity.getEndTime());
		
// 		//Konvertera till timmar/minuter/sekund

// 		//@@@@@@@@@@@@@ TODO fixa snyggare konvertering @@@@@@@@@@@@@
// 		long trackedDuration = duration.toMinutes();


// 		activity.setTrackedTime(trackedDuration);
// 		mongoOperations.save(activity);

// 		return "{Activity: " + activity.getActivityName() + " stopped after " + trackedDuration + "minutes!}";
// 	} else {
// 		return "{Activity: " + activity.getActivityName() + " is not started or already stopped}";
// 	}}
// }
