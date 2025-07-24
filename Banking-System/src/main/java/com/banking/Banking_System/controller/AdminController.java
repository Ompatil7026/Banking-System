package com.banking.Banking_System.controller;

import com.banking.Banking_System.entities.User;
import com.banking.Banking_System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Add any attributes needed by your admin dashboard template,
        // for example, users list, system stats, audit logs, etc.
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "admin/dashboard"; // map to src/main/resources/templates/admin/dashboard.html
    }

    // View all users
    @GetMapping("/users")
    public String viewUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    // Delete a user
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }

    // Add more operations as needed (e.g., edit user, assign roles, etc.)
    // 1. Show edit form for user with id
    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
        model.addAttribute("user", user);
        return "admin/edit-user"; // template to create
    }

    // 2. Handle edit form submission
    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User updatedUser, Model model) {
        // Optional: validate updatedUser fields here (e.g., username unique, valid email, etc.)

        // Set id explicitly (to prevent creating new user)
        updatedUser.setId(id);

        // Possibly handle password encoding if password field changed:
        // (if password empty in form, do not overwrite existing password)
        User existingUser = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));

        if(updatedUser.getPassword() == null || updatedUser.getPassword().isEmpty()) {
            updatedUser.setPassword(existingUser.getPassword()); // keep old password
        } else {
            // encode new password
            updatedUser.setPassword(userService.encodePassword(updatedUser.getPassword()));
        }

        userService.updateUser(updatedUser);
        return "redirect:/admin/users";
    }
}
