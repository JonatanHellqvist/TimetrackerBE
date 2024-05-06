package com.timetrackerbe.timetrackerbe.services;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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
}
