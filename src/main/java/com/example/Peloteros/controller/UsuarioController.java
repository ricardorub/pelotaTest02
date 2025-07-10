

package com.example.peloteros.controller;

import com.example.peloteros.model.Usuario;
import com.example.peloteros.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public String registrarUsuario(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String telefono,
            Model model) {
        
        // Verificar si el usuario ya existe
        if (usuarioService.obtenerUsuarioPorEmail(email) != null) {
            model.addAttribute("error", "El correo electrónico ya está registrado");
            return "registro";
        }
        
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setTelefono(telefono);
        
        usuarioService.registrarUsuario(usuario);
        model.addAttribute("success", "¡Registro exitoso! Por favor inicia sesión.");
        return "login";
    }

    @PostMapping("/login")
    public String loginUsuario(
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session) {
        
        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(email);
        if (usuario == null || !usuario.getPassword().equals(password)) {
            model.addAttribute("error", "Credenciales incorrectas");
            return "login";
        }
        
        session.setAttribute("usuario", usuario);
        return "redirect:/reservar";
    }

    @GetMapping("/logout")
    public String logoutUsuario(HttpSession session) {
        session.removeAttribute("usuario");
        return "redirect:/";
    }
}