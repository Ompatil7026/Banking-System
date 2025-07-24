package com.banking.Banking_System.controller;

import org.springframework.ui.Model;
import com.banking.Banking_System.entities.User;
import com.banking.Banking_System.service.AccountService;
import com.banking.Banking_System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @Autowired
    UserService userService;
    @Autowired
    AccountService accountService;

    @GetMapping("/register")
    public String showRegForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    //to create user acc to role
    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model){
        if(userService.isUsernameTaken(user.getUsername())){
            model.addAttribute("error", "Username already taken");
            return "register"; // show same page with error
        }
        userService.createUser(user);
        accountService.createAccountForCustomer(user);
        return "redirect:/login?success";
    }


    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/role-redirect")
    public String roleRedirect() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findFirst().get().getAuthority();
        if ("ROLE_ADMIN".equals(role)) return "redirect:/admin/dashboard";
        if ("ROLE_MANAGER".equals(role)) return "redirect:/manager/dashboard";
        if ("ROLE_CUSTOMER".equals(role)) return "redirect:/customer/dashboard";
        return "redirect:/login";
    }
}
