package com.example.peloteros.controller;

import com.example.peloteros.model.Reserva;
import com.example.peloteros.model.Usuario;
import com.example.peloteros.service.ReservaService;
import com.example.peloteros.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaApiController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/activas")
    public ResponseEntity<List<Reserva>> getActivas(Principal principal) {
        return getReservasByEstado(principal, "PENDIENTE");
    }

    @GetMapping("/pasadas")
    public ResponseEntity<List<Reserva>> getPasadas(Principal principal) {
        return getReservasByEstado(principal, "COMPLETADA");
    }

    @GetMapping("/canceladas")
    public ResponseEntity<List<Reserva>> getCanceladas(Principal principal) {
        return getReservasByEstado(principal, "CANCELADA");
    }

    private ResponseEntity<List<Reserva>> getReservasByEstado(Principal principal, String estado) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = principal.getName();
        return usuarioService.obtenerUsuarioPorEmail(email)
                .map(usuario -> {
                    List<Reserva> reservas = reservaService.obtenerReservasPorUsuarioYEstado(usuario, estado);
                    if (reservas == null) {
                        reservas = new ArrayList<>();
                    }
                    return ResponseEntity.ok(reservas);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
