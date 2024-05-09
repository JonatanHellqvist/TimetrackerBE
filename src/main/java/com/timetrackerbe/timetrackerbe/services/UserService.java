package com.timetrackerbe.timetrackerbe.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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
//ta bort
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
	//reseta
	public String resetAndStartUserActivity(String userId,String activityId) {
		User user = mongoOperations.findById(userId, User.class);
		if (user != null) {
			List <Activity> activityList = user.getActivityList();
			boolean activityFound = false;

			for (Activity activity : activityList) {
				if (activity.getId().equals(activityId)) {

						activity.setStartTime(LocalDateTime.now());
						activity.setEndTime(null);
						activityFound = true;
						break;
				}
				}if (activityFound) {
				mongoOperations.save(user);
						return "{Activity started}";
				} else {
						return "{Activity not found}";
				}
			} else {
			return "{User not found}";
			} 
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
			List<Activity> activityList = user.getActivityList();
			for (Activity activity : activityList) {
				if (activity.getId().equals(activityId)) {
					if (activity.getEndTime() == null) {
						activity.setEndTime(LocalDateTime.now());

						Duration duration = Duration.between(activity.getStartTime(), activity.getEndTime());
						long trackedDuration = duration.toMinutes();

						activity.setTrackedTime(activity.getTrackedTime() + trackedDuration);
						activity.setTotalTrackedTime(activity.getTotalTrackedTime() + trackedDuration);
						

						Long newtotalTrackedTime = user.getTotalTrackedTime() + activity.getTrackedTime();
						user.setTotalTrackedTime(newtotalTrackedTime);

						activity.setTrackedTime(null);

						mongoOperations.save(user);

						return "{Activity: " + activity.getActivityName() + " stopped after " + trackedDuration + "minutes!}";
					} else {
						return "{Activity: " + activity.getActivityName() + " is not started or already stopped}";
					}
					}
				}
				return "{Activity not found}";
			}
			return "{User not found}";
		}				

	public String deleteUserActivityFromHistory(String userId,String activityId) {
		User user = mongoOperations.findById(userId, User.class);
		if (user != null) {
			List <Activity> activityHistory = user.getActivityHistory();
			boolean activityFound = false;
			for (Activity activity : activityHistory) {
				if (activity.getId().equals(activityId)) {
						activityHistory.remove(activity);
						activityFound = true;
				break;			
				}
			}
			if (activityFound) {
				user.setActivityHistory(activityHistory);
				mongoOperations.save(user);	
				return "{ActivityId: " + activityId + " removed}";
			} else {
				return "{Activity not found}";	
			}
		} else {
			return "{User not found}";
		}	
	}
	public String deleteUserActivity(String userId,String activityId) {
		User user = mongoOperations.findById(userId, User.class);
		if (user != null) {
			List <Activity> activityList = user.getActivityList();
			boolean activityFound = false;
			for (Activity activity : activityList) {
				if (activity.getId().equals(activityId)) {
					activityList.remove(activity);
					activityFound = true;
				break;
				}
			}
			if (activityFound) {
				user.setActivityList(activityList);
				mongoOperations.save(user);	
				return "{ActivityId: " + activityId + " removed}";
			} else {
				return "{Activity not found}";	
			}
		} else {
			return "{User not found}";
		}	
	}

	//getuseractivitrybyid
	public Activity getUserActivityById(String userId, String activityId) {
		User user = mongoOperations.findById(userId,User.class);
		if (user != null) {

			//kollar activityList
			for(Activity activity : user.getActivityList()) {
				if (activity.getId().equals(activityId)) {
					return activity;
				}
			}
			//kollar historyList
			for(Activity activity : user.getActivityHistory()) {
				if (activity.getId().equals(activityId)) {
					return activity;
				}
			}
		}
		return null;
	}

	public String moveUserActivityToHistory(String userId,String activityId) {
		User user = mongoOperations.findById(userId,User.class);
		if (user != null) {
			List <Activity> activityHistory = user.getActivityHistory();
			List <Activity> activityList = user.getActivityList();

			Activity activityToMove = null;
			for (Activity activity : activityList) {
				if (activity.getId().equals(activityId)) {
					activityToMove = activity;
					break;
				}
			}
			if (activityToMove != null) {
				if (activityToMove.getStartTime() == null){
					activityToMove.setStartTime(LocalDateTime.now());
				} if (activityToMove.getEndTime() == null){
					activityToMove.setEndTime(LocalDateTime.now());	
				}
				//uppdatera tracked time och avbryt
				Duration duration = Duration.between(activityToMove.getStartTime(), activityToMove.getEndTime());
				long trackedDuration = duration.toMinutes();

				activityToMove.setTotalTrackedTime(activityToMove.getTotalTrackedTime() + trackedDuration);
				Long newtotalTrackedTime = user.getTotalTrackedTime() + trackedDuration;
				user.setTotalTrackedTime(newtotalTrackedTime);

				activityToMove.setTrackedTime(null);
						
				activityList.remove(activityToMove);
				activityHistory.add(activityToMove);
				mongoOperations.save(user);
				return "{Activity: " + activityToMove.getActivityName() + " moved to history}";
			} else {
				return "{Activity not found}";
			}
		} else {
			return "{User not found}";
		}			
	}	
}