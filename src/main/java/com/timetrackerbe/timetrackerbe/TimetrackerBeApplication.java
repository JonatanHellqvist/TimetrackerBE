package com.timetrackerbe.timetrackerbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)  //fuldisable av security 
public class TimetrackerBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimetrackerBeApplication.class, args);
		// TimeZone.setDefault(TimeZone.getTimeZone("Europe/Stockholm"));
	}

}
