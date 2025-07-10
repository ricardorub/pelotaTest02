package com.example.peloteros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.peloteros.dao.UsuarioRepository;
import com.example.peloteros.model.Usuario;

import java.util.List; // Added import for List
import java.util.Optional;
// Consider adding: import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRoles("ROLE_USER"); // Assign default role
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public void actualizarUsuario(Usuario usuario) {
        // For now, let's ensure it saves if called, though full implementation might be more complex
        // It should ideally check if the user exists and handle password changes carefully
        if (usuario.getId() != null && usuarioRepository.existsById(usuario.getId())) {
            // If password is provided and seems changed (not already encoded), encode it
            // This is a simplistic check; a real scenario needs more robust logic
            // For example, by checking if the password field is different from the one in DB and not already hashed
            if (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2a$")) { // Basic check if not BCrypt hashed
                 usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
            usuarioRepository.save(usuario);
        } else {
            throw new IllegalArgumentException("Usuario no encontrado o ID no proporcionado para actualizar.");
        }
    }

    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    public void deleteUser(Long userId) {
        // Before deleting a user, consider implications for related entities.
        // For example, what happens to their Reservas?
        // Option 1: Delete them (cascade delete if configured in JPA).
        // Option 2: Disassociate them (set user_id to null if allowed by schema).
        // Option 3: Prevent deletion if they have active reservations.
        // For this initial implementation, we'll proceed with direct deletion.
        
        if (!usuarioRepository.existsById(userId)) {
            // Consider throwing something like:
            // throw new EntityNotFoundException("Usuario with ID " + userId + " not found.");
            // For now, deleteById will simply do nothing if ID doesn't exist, which is acceptable.
            // Logging a warning might be good in a real application.
        }
        usuarioRepository.deleteById(userId);
    }
}