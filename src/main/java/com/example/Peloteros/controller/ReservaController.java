/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.peloteros.controller;

import com.example.peloteros.model.Cancha;
import com.example.peloteros.model.Reserva;
import com.example.peloteros.model.Usuario;
import com.example.peloteros.service.CanchaService;
import com.example.peloteros.service.ReservaService;
import com.example.peloteros.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReservaController {

    private final CanchaService canchaService;
    private final ReservaService reservaService;
    private final UsuarioService usuarioService;

    @Autowired
    public ReservaController(CanchaService canchaService, 
                            ReservaService reservaService, 
                            UsuarioService usuarioService) {
        this.canchaService = canchaService;
        this.reservaService = reservaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/reservar")
    public String reservar(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        List<Cancha> canchas = canchaService.obtenerTodasCanchas();
        model.addAttribute("canchas", canchas);
        return "reservar";
    }

    @PostMapping("/reservar")
    public String realizarReserva(
            @RequestParam Long canchaId,
            @RequestParam String fecha,
            @RequestParam String horaInicio,
            @RequestParam String horaFin,
            HttpSession session,
            Model model) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        Cancha cancha = canchaService.obtenerCanchaPorId(canchaId);
        if (cancha == null) {
            model.addAttribute("error", "Cancha no encontrada");
            return "reservar";
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime fechaHoraInicio = LocalDateTime.parse(fecha + " " + horaInicio, formatter);
        LocalDateTime fechaHoraFin = LocalDateTime.parse(fecha + " " + horaFin, formatter);
        
        // Calcular duración en horas
        long horas = java.time.Duration.between(fechaHoraInicio, fechaHoraFin).toHours();
        double precioTotal = cancha.getPrecioPorHora() * horas;
        
        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setCancha(cancha);
        reserva.setFechaHoraInicio(fechaHoraInicio);
        reserva.setFechaHoraFin(fechaHoraFin);
        reserva.setPrecioTotal(precioTotal);
        reserva.setEstado("PENDIENTE");
        
        reservaService.crearReserva(reserva);
        
        model.addAttribute("reserva", reserva);
        return "confirmacion";
    }

    @GetMapping("/misreservas")
    public String misReservas(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        List<Reserva> reservas = reservaService.obtenerReservasPorUsuario(usuario);
        model.addAttribute("reservas", reservas);
        return "misreservas";
    }

    @PostMapping("/cancelar-reserva")
    public String cancelarReserva(@RequestParam Long reservaId, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        reservaService.cancelarReserva(reservaId);
        return "redirect:/misreservas";
    }
}
