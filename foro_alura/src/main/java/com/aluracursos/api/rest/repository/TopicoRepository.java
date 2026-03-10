package com.aluracursos.api.rest.repository;


import com.aluracursos.api.rest.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

// Interfaz para manejar la persistencia de Topico
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    // Método para verificar si ya existe un tópico con mismo título y mensaje
    boolean existsByTituloAndMensaje(String titulo, String mensaje);

    List<Topico> findByCurso(String curso);

    List<Topico> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
}