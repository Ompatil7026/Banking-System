package com.banking.Banking_System.service;

import com.banking.Banking_System.entities.User;
import com.banking.Banking_System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepo;
    private PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.encoder = passwordEncoder;
    }

    public boolean isUsernameTaken(String username) {
        return userRepo.findByUsername(username).isPresent();
    }

    // UserService.java
    public User createUser(User user) {
        // Only allow specific roles
        String role = user.getRole();
        if (role == null ||
                !(role.equals("CUSTOMER") || role.equals("MANAGER") || role.equals("ADMIN"))) {
            user.setRole("CUSTOMER");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(true);
        return userRepo.save(user);
    }


    public Optional<User> findByUsername(String uname) {
        return userRepo.findByUsername(uname);
    }

    // Add getAll, delete, assignRole, etc.
    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    public void deleteUserById(Long id){
        userRepo.deleteById(id);
    }

    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    public void updateUser(User user) {
        userRepo.save(user);
    }

    public String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    // Returns the total number of users
    public long countUsers() {
        return userRepo.count();
    }

    //Returns the total number of users with their roles
    public long countUsersByRole(String role) {
        return userRepo.countByRole(role);
    }

}
