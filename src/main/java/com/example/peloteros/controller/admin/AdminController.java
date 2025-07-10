/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Peloteros.controller.admin;

// Corrected import paths based on existing project structure
import com.example.peloteros.model.Usuario; 
import com.example.peloteros.service.UsuarioService; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin") // Base path for all admin actions
public class AdminController {

    private final UsuarioService usuarioService;

    @Autowired
    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Display list of users
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<Usuario> users = usuarioService.getAllUsers();
        model.addAttribute("users", users);
        // The view name will be resolved to "templates/admin/user-list.html"
        // by Thymeleaf's prefix/suffix configuration.
        return "admin/user-list"; 
    }

    // Handle user deletion
    @PostMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        try {
            // Optional: Add a check to prevent admin from deleting themselves,
            // or to ensure at least one admin remains.
            // For now, direct deletion.
            
            // It might be good to check if the user exists before attempting deletion
            // and show a specific message, though deleteUser in service might already handle it.
            // Example: if(!usuarioService.obtenerUsuarioPorId(userId).isPresent()) { /* handle */ }
            
            usuarioService.deleteUser(userId);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully.");
        } catch (Exception e) {
            // Log the exception e using a proper logger in a real application
            System.err.println("Error deleting user: " + e.getMessage()); // Simple error logging
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/admin/users"; // Redirect back to the user list
    }
}


