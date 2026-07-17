package com.smart.controller;

import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ContactRepository contactRepo;

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String name = principal.getName();
		User user = userRepo.getUserByUserName(name);
//		System.out.println("name-->" + user);
		model.addAttribute("user", user);
	}

	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		return "non-admin/user_dashboard";
	}

	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "non-admin/addContact";
	}

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, RedirectAttributes redirectAttributes) {
		try {

			String name = principal.getName();
			User user = userRepo.getUserByUserName(name);
			if (file.isEmpty()) {
				System.out.println("File is empty");
				contact.setImage("contact.png");
			} else {
				contact.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				// to copy in the target folder
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.println("image is uploaded");
			}
			contact.setUser(user);
			user.getContacts().add(contact);
			userRepo.save(user);
			redirectAttributes.addFlashAttribute("message", new Message("Your contact is added !!!", "success"));

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", new Message("Your contact is  not added !!!", "danger"));
			e.printStackTrace();
		}
		System.out.println("Contact saved !!");
		return "redirect:/user/view-contacts/1";// redirect back to View contact page
	}

	// pagination: per page n=5 and currentPage=0[page]
	@GetMapping("/view-contacts/{page}")
	public String getContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "Show all contacts");
		String userName = principal.getName();

		User user = userRepo.getUserByUserName(userName);

		Pageable pageRequest = PageRequest.of(page - 1, 5);
		Page<Contact> contacts = contactRepo.findContactsByUser(user.getId(), pageRequest);

		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page - 1);
		model.addAttribute("totalPages", contacts.getTotalPages());

		return "non-admin/contacts-list";
	}

	@GetMapping("/{cId}/contact")
	public String getContactDetails(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		Optional<Contact> contactOptional = contactRepo.findById(cId);
		Contact contact = contactOptional.get();

		String usernameLoggedIn = principal.getName();
		User user = userRepo.getUserByUserName(usernameLoggedIn);
		if (contact.getUser().getId() == user.getId()) {
			model.addAttribute("title", contact.getName());
			model.addAttribute("contact", contact);
		}
		return "non-admin/contact_details";
	}

	@GetMapping("/delete-contact/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Model model, Principal principal,
			RedirectAttributes redirectAttributes) {

		Optional<Contact> optionalContact = contactRepo.findById(cid);
		Contact contact = optionalContact.get();

		String username = principal.getName();
		User user = userRepo.getUserByUserName(username);
		if (contact.getUser().getId() == user.getId()) {
			contactRepo.delete(contact);
		}

		redirectAttributes.addFlashAttribute("message", new Message("Contact Delete Successfully", "success"));
		return "redirect:/user/view-contacts/1";
	}

	// edit contact
	@PostMapping("/update-contact/{cid}")
	public String editContact(@PathVariable("cid") Integer cid, Principal principal, Model model) {

		String LoggedInUser = principal.getName();
		User user = userRepo.getUserByUserName(LoggedInUser);

		Optional<Contact> optional = contactRepo.findById(cid);
		Contact contact = optional.get();
		if (user.getId() == contact.getUser().getId())
			model.addAttribute("contact", contact);
		model.addAttribute("title", "Update Contact");
		return "non-admin/edit-contact";
	}

	// update the contact
	@PostMapping("/process-update-contact")
	public String updateSelectedContact(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file, Model model, HttpSession httpSession, Principal principal,
			RedirectAttributes redirectAttributes) {

		Contact oldContact = contactRepo.findById(contact.getCid()).get();

		try {
			if (!file.isEmpty()) {

				// Delete old image from source
				if (oldContact.getImage() != null) {

					File deleteFile = new ClassPathResource("static/image").getFile();
					File fileDelete = new File(deleteFile, oldContact.getImage());
					fileDelete.delete();
				}

				// Add new image from source
				File saveFile = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				// to copy in the target folder
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());

			} else {
				contact.setImage(oldContact.getImage());
			}

			User user = userRepo.getUserByUserName(principal.getName());
			contact.setUser(user);
			contactRepo.save(contact);
			redirectAttributes.addFlashAttribute("message", new Message("You cantact is updated", "success"));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "redirect:/user/" + contact.getCid() + "/contact";
	}

	@GetMapping("/profile")
	public String profileDetails(Model model, Principal principal) {
		String loggedInUser = principal.getName();
		User user = userRepo.getUserByUserName(loggedInUser);
		model.addAttribute("title", "Profile Details");
		model.addAttribute("user", user);
		return "non-admin/profile-details-page";
	}

	@GetMapping("/search-contact")
	public String searchContact(@RequestParam("query") String query, Model model) {

		Pageable pageable = PageRequest.of(0, 5);

	    Page<Contact> contacts = contactRepo.findContactsByName(query, pageable);

	    model.addAttribute("contacts", contacts);
	    model.addAttribute("currentPage", 0);
	    model.addAttribute("totalPages", contacts.getTotalPages());

	    return "non-admin/contacts-list";
	}
}
