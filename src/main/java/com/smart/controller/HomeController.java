package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepo;

	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("title", "Home- Smart Contact Manager");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About- Smart Contact Manager");
		return "about";
	}

	@GetMapping("/test")
	@ResponseBody
	public String test() {

		User user = new User();
		user.setName("Atul");
		user.setEmail("AtulSantosh@gmail.com");

		Contact contact = new Contact();
		contact.setDescription("this is description for contact");
		user.getContacts().add(contact);
		contact.setUser(user);

		userRepo.save(user);

		return "Working ";
	}
}
