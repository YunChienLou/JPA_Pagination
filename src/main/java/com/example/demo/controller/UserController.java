package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@CrossOrigin(origins = "https://localhost:8443")
@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@GetMapping("/users")
	public ResponseEntity<Map<String, Object>> getAllUsers(@RequestParam(required = false) String list,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
		try {
			List<User> users = new ArrayList<User>();
			Pageable paging = PageRequest.of(page, size);
			Page<User> pageTuts;
			if (list == null) {
				pageTuts = userRepository.findAll(paging);
			} else {
				pageTuts = userRepository.findByListContaining(list, paging);
			}
			users = pageTuts.getContent();
			Map<String, Object> response = new HashMap<>();
			response.put("users", users);
			response.put("currentPage", pageTuts.getNumber());
			response.put("totalItems", pageTuts.getTotalElements());
			response.put("totalPages", pageTuts.getTotalPages());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	@PostMapping("/users/nameSearch")
	public ResponseEntity<Map<String, Object>> getNameLikeUser(@RequestBody KeywordSearch keywords , @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
		try {
			List<User> users = new ArrayList<User>();
			Pageable paging = PageRequest.of(page, size);
			System.out.println("nameSearch page:"+page);
			Page<User> pageTuts;
			if (keywords == null) {
				pageTuts = userRepository.findAll(paging);
			} else {
				String keywordsModify = keywords.getText();
				System.out.println("keyword:"+keywordsModify);
				pageTuts = userRepository.findByNameLike(keywordsModify, paging);
			}
			users = pageTuts.getContent();
			Map<String, Object> response = new HashMap<>();
			response.put("users", users);
			response.put("currentPage", pageTuts.getNumber());
			response.put("totalItems", pageTuts.getTotalElements());
			response.put("totalPages", pageTuts.getTotalPages());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/users/textSearch")
	public ResponseEntity<Map<String, Object>> getTextLikeUser(@RequestBody KeywordSearch keywords , @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
		try {
			List<User> users = new ArrayList<User>();
//			裝USER的容器
			Pageable paging = PageRequest.of(page, size);
//			Paginaion的用法
			
			System.out.println("textSearch : 搜尋第" + page + "頁");
			Page<User> pageTuts;
			if (keywords == null) {
				pageTuts = userRepository.findAll(paging);
				System.out.println("textSearch: findAll()");
			} else {
				System.out.println("textSearch: findByTextLike()");
				String keywordsModify = keywords.getText();
				pageTuts = userRepository.findByTextLike(keywordsModify, paging);
				System.out.println(keywords);
				System.out.println(keywordsModify);
				System.out.println(pageTuts.getContent());
			}
			users = pageTuts.getContent();
			Map<String, Object> response = new HashMap<>();
			response.put("users", users);
			response.put("currentPage", pageTuts.getNumber());
			response.put("totalItems", pageTuts.getTotalElements());
			response.put("totalPages", pageTuts.getTotalPages());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/users/addressSearch")
	public ResponseEntity<Map<String, Object>> getAddressLikeUser(@RequestBody KeywordSearch keywords , @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
		try {
			List<User> users = new ArrayList<User>();
			Pageable paging = PageRequest.of(page, size);
			System.out.println("addressSearch:"+page);
			Page<User> pageTuts;
			if (keywords == null) {
				pageTuts = userRepository.findAll(paging);
			} else {
				String keywordsModify = keywords.getText();
				pageTuts = userRepository.findByAddressLike(keywordsModify, paging);
			}
			users = pageTuts.getContent();
			System.out.println(users);
			Map<String, Object> response = new HashMap<>();
			response.put("users", users);
			response.put("currentPage", pageTuts.getNumber());
			response.put("totalItems", pageTuts.getTotalElements());
			response.put("totalPages", pageTuts.getTotalPages());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") long id){
		Optional<User> userData = userRepository.findById(id);
		if(userData.isPresent()) {
			return new ResponseEntity<>(userData.get(), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		
		try {
			User _user = userRepository
					.save(new User(user.getName(), user.getText(), user.getList(), user.getAddress(), true));
			return new ResponseEntity<>(_user, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<User> deleteTutorial(@PathVariable("id") long id) {
		try {
			userRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
		System.out.printf("######## updateUser: {}\n ,{}", user,id);
		Optional<User> userData = userRepository.findById(id);

		if (userData.isPresent()) {
			User _user = userData.get();
			_user.setName(user.getName());
			_user.setList(user.getList());
			_user.setText(user.getText());
			_user.setAddress(user.getAddress());
			return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	@PutMapping("/users/editPublished/{id}")
	public ResponseEntity<User> updateUserPublished(@PathVariable("id") long id){
		System.out.printf("######## updateUserPublished: %d%n",id);
		Optional<User> userData = userRepository.findById(id);
		if (!userData.get().isPublished()) {
			userRepository.publishUser(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	@PutMapping("/users/editPublishedFalse/{id}")
	public ResponseEntity<User> updateUserPublishedFalse(@PathVariable("id") long id){
		System.out.printf("######## updateUserPublishedFalse: %d%n",id);
		Optional<User> userData = userRepository.findById(id);
		if (userData.get().isPublished()) {
			userRepository.publishUserFalse(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


	@GetMapping("/users/published")
	public ResponseEntity<Map<String, Object>> findByPublished(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "6") int size) {
		try {
			List<User> users = new ArrayList<User>();
			Pageable paging = PageRequest.of(page, size);

			Page<User> pageTuts = userRepository.findByPublished(true, paging);
			users = pageTuts.getContent();
			System.out.println(users);
			Map<String, Object> response = new HashMap<>();
			response.put("users", users);
			response.put("currentPage", pageTuts.getNumber());
			response.put("totalItems", pageTuts.getTotalElements());
			response.put("totalPages", pageTuts.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
