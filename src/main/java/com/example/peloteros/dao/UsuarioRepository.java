





package com.example.peloteros.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.peloteros.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}




