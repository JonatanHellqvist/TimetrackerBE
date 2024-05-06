package com.timetrackerbe.timetrackerbe.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.timetrackerbe.timetrackerbe.models.User;
import com.timetrackerbe.timetrackerbe.services.UserService;

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
	public ResponseEntity<String> login(@RequestBody User user) {

		
		User loggedInUser = userService.getUserByUsername(user.getUserName());

		//kolla om user inte är null och password stämmer med getpassword för usern.
		if (loggedInUser != null && loggedInUser.getPassword().equals(user.getPassword())) {
			
			return ResponseEntity.ok(loggedInUser.getUserName());
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username or password! Try again!");
		}
	}
}
