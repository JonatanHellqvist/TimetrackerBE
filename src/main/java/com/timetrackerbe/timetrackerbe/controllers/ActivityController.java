package com.timetrackerbe.timetrackerbe.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.timetrackerbe.timetrackerbe.models.Activity;
import com.timetrackerbe.timetrackerbe.services.ActivityService;


@RestController
@CrossOrigin(origins = "*")
public class ActivityController {

	private ActivityService activityService;

	public ActivityController(ActivityService activityService) {
		this.activityService = activityService;
	}

	@PostMapping("/activity")
	public Activity addActivity(@RequestBody Activity activity) {
		return activityService.addActivity(activity);
	}

	@GetMapping("/activity/{id}")
	public Activity getActivity(@PathVariable String id) {
		return activityService.getActivity(id);
	}

	@GetMapping("/activities")
	public List<Activity> getActivities() {
		return activityService.getActivities();
	}
	
	@PutMapping("/activity/start/{id}")
	public String startActivity(@PathVariable String id) {
		return activityService.startActivity(id);
	}
	

	@PutMapping("/activity/stop/{id}")
	public String stopActivity(@PathVariable String id) {
		return activityService.stopActivity(id);
	}
	
}
