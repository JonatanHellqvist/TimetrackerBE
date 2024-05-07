package com.timetrackerbe.timetrackerbe.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

	//lägga till personlig aktivitet
	@PostMapping("/{userId}/list/addactivity")
    public Activity addUserActivity(@PathVariable String userId, @RequestBody Activity activity) {
        return userService.addUserActivity(userId, activity);
    }

	//starta

	@PutMapping("/activity/start/{userId}/{activityId}")
	public String startActivity(@PathVariable String userId, @PathVariable String activityId) {
		return userService.startUserActivity(userId,activityId);
	}

	@PutMapping("/activity/startactivity/{userId}/{activityId}")
	public String startActivityFromHistory(@PathVariable String userId, @PathVariable String activityId) {
		return userService.startUserActivityFromHistory(userId,activityId);
	}


	//stoppa

	@PutMapping("/activity/stop/{userId}/{activityId}")
	public String stopActivity(@PathVariable String userId, @PathVariable String activityId) {
		return userService.stopUserActivity(userId,activityId);
	}


	//totaltrackedtime

	@GetMapping("/{$userId}/trackedtime")
	public Long getTotalTrackedTime (@PathVariable String userId) {
		return userService.getUserTotalTrackedTime(userId);
	}
	// //delete
	// @DeleteMapping("/activity/delete/{id}") 
	// public String deleteActivity(@PathVariable String id) {
	// 	Activity activityToDelete = activityService.getActivity(id);
	// 	activityService.deleteActivity(id);

	// 	return "{ " + activityToDelete.getActivityName() + " has been deleted }";
	// }

	// @PostMapping("/activity")
	// public Activity addActivity(@RequestBody Activity activity) {
	// 	return activityService.addActivity(activity);
	// }

	
}
