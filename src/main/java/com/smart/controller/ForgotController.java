package com.smart.controller;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.model.IModel;

import java.security.Principal;
import java.util.Random;

@Controller
public class ForgotController {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/forgot_email")
    public String openEmailForm(Model model) {
        model.addAttribute("title", "Forgot Password Page");
        return "forgot_email_form";
    }

    @PostMapping("/sendOtp")
    public String sendOTP(@RequestParam("email_id") String email, Model model, RedirectAttributes redirectAttributes, HttpSession session) {

        model.addAttribute("title", "Send OTP Page");

        User user = userRepo.getUserByUserName(email);


        //checking if user is present in DB
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", new Message("No user Found for this email ID ", "danger"));
            return "redirect:/forgot_email";
        }
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);


        //setting otp and mail in session
        session.setAttribute("otp", otp);
        session.setAttribute("email", email);

        //sending mail for OTP
        SimpleMailMessage simpleMail = new SimpleMailMessage();
        simpleMail.setFrom("atulpatankar55555@gmail.com");
        simpleMail.setTo(email);
        simpleMail.setSubject("OTP for Smart Contact Manager: OTP Change");
        simpleMail.setText("New OTP is " + otp);
        javaMailSender.send(simpleMail);

        //setting success message
        redirectAttributes.addFlashAttribute("message", new Message("OTP sent successfully on " + email, "success"));

        return "redirect:/verify_otp";// goes to GET mapping for Verify OTP

    }

    @GetMapping("/verify_otp")
    public String verifyOtpPage(Model model) {
        model.addAttribute("title", "Verify OTP Page");
        return "verify_otp";
    }

    @PostMapping("/submit_verify_otp")
    public String verifyOTP(@RequestParam("enteredotp") int enteredotp, RedirectAttributes redirectAttributes, HttpSession session, Model model) {

        String email = (String) session.getAttribute("email");
        User user = userRepo.getUserByUserName(email);

        int generatedOTP = (Integer) session.getAttribute("otp");
        if (generatedOTP == enteredotp) {

            model.addAttribute("email", email);

            return "newPassword";
        } else {
            redirectAttributes.addFlashAttribute(
                    "message",
                    new Message("Invalid OTP", "danger"));

            return "redirect:/verify_otp";
        }

    }

    @PostMapping("/setNewPassword")
    public String setNewPassword(@RequestParam("newPassword") String newPassword, RedirectAttributes redirectAttributes, HttpSession session) {
        String name=(String)session.getAttribute("email");
        User user = userRepo.getUserByUserName(name);

        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepo.save(user);
        redirectAttributes.addFlashAttribute(
                "message",
                new Message("Password changed!! Login Again with New Password", "success"));
        session.removeAttribute("otp");
        session.removeAttribute("email");
        return "redirect:/signin";
    }


}
