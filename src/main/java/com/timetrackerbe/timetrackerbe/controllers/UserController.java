package com.timetrackerbe.timetrackerbe.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.timetrackerbe.timetrackerbe.models.Activity;
import com.timetrackerbe.timetrackerbe.models.User;
import com.timetrackerbe.timetrackerbe.services.UserService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
	
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/user")
	public User addUser(@RequestBody User user) {
		return userService.addUser(user);
	}

	@GetMapping("/user/{id}")
	public User getUser(@PathVariable String id) {
		return userService.getUser(id);
	}

	@GetMapping("/users")
	public List<User> getUsers() {
		return userService.getUsers();
	}

	@PostMapping("/user/login")
	public ResponseEntity<User> login(@RequestBody User user) {
		User loggedInUser = userService.getUserByUsername(user.getUserName());
		//kolla om user inte är null och password stämmer med getpassword för usern.
		if (loggedInUser != null && loggedInUser.getPassword().equals(user.getPassword())) {

			//test
			if (loggedInUser.isAdmin()) {
				return ResponseEntity.ok(loggedInUser);
			} else {
				return ResponseEntity.ok(loggedInUser);
			} 
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	//ta bort sen bara för att testa
	@PutMapping("admin/toggle")
	public User adminToggle (@RequestBody User user) {
		return userService.adminToggle(user);
	}
	//personliga listan
	@GetMapping ("/{userId}/list/")
	public List<Activity> getUserActivities(@PathVariable String userId) {
		return userService.getUserActivities(userId);
	}
	@GetMapping ("/historylist/{userId}")
	public List<Activity> getActivityHistory(@PathVariable String userId) {
		return userService.getUserActivityHistory(userId);
	}
	@PostMapping("/{userId}/list/addactivity")
    public Activity addUserActivity(@PathVariable String userId, @RequestBody Activity activity) {
        return userService.addUserActivity(userId, activity);
    }
	@PutMapping("/activity/start/{userId}/{activityId}")
	public String startActivity(@PathVariable String userId, @PathVariable String activityId) {
		return userService.startUserActivity(userId,activityId);
	}
	@PutMapping("/activity/reset/{userId}/{activityId}")
	public String resetActivity(@PathVariable String userId, @PathVariable String activityId) {
		return userService.resetAndStartUserActivity(userId,activityId);
	}
	@PutMapping("/activity/startactivity/{userId}/{activityId}")
	public String startActivityFromHistory(@PathVariable String userId, @PathVariable String activityId) {
		return userService.startUserActivityFromHistory(userId,activityId);
	}
	@PutMapping("/activity/stop/{userId}/{activityId}")
	public String stopActivity(@PathVariable String userId, @PathVariable String activityId) {
		return userService.stopUserActivity(userId,activityId);
	}
	@GetMapping("/{userId}/trackedtime")
	public Long getTotalTrackedTime (@PathVariable String userId) {
		return userService.getUserTotalTrackedTime(userId);
	}
	@PutMapping("/{userId}/{activityId}/activity/delete") 
	public String deleteActivity(@PathVariable String userId, @PathVariable String activityId) {
		return userService.deleteUserActivity(userId,activityId);
	}
	@PutMapping("/{userId}/{activityId}/activityhistory/delete") 
	public String deleteActivityFromHistory(@PathVariable String userId, @PathVariable String activityId) {
		return userService.deleteUserActivityFromHistory(userId,activityId);
	}
	@PutMapping("/{userId}/{activityId}/activity/movetohistory")
	public String moveActivityToActivityHistory(@PathVariable String userId, @PathVariable String activityId) {
		return userService.moveUserActivityToHistory(userId,activityId);
	}

	
}
