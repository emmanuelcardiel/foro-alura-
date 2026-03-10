package com.aluracursos.api.rest.controller;

import com.aluracursos.api.rest.model.Topico;
import com.aluracursos.api.rest.dto.DatosRegistroTopico;
import com.aluracursos.api.rest.repository.TopicoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    // ===================== POST /topicos =====================
    @PostMapping
    @Transactional
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroTopico datos) {

        if (repository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            return ResponseEntity.badRequest().body("Tópico duplicado");
        }

        Topico topico = new Topico(
                null,
                datos.titulo(),
                datos.mensaje(),
                LocalDateTime.now(),
                datos.autor(),
                datos.curso()
        );

        repository.save(topico);

        return ResponseEntity.ok("Tópico registrado");
    }

    // ===================== GET /topicos =====================
    @GetMapping
    public ResponseEntity listarTodos() {
        List<Topico> topicos = repository.findAll();
        return ResponseEntity.ok(topicos);
    }

    // ===================== GET /topicos/ordenados =====================
    @GetMapping("/ordenados")
    public ResponseEntity listarOrdenados() {
        List<Topico> topicos = repository.findAll(Sort.by(Sort.Direction.ASC, "fechaCreacion"));
        return ResponseEntity.ok(topicos);
    }

    // ===================== GET /topicos/buscar =====================
    @GetMapping("/buscar")
    public ResponseEntity buscar(
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) Integer año
    ) {
        List<Topico> resultado;

        if (curso != null) {
            resultado = repository.findByCurso(curso);
        } else if (año != null) {
            var inicio = LocalDateTime.of(año, 1, 1, 0, 0);
            var fin = LocalDateTime.of(año, 12, 31, 23, 59);
            resultado = repository.findByFechaCreacionBetween(inicio, fin);
        } else {
            resultado = repository.findAll();
        }

        return ResponseEntity.ok(resultado);
    }

    // ===================== GET /topicos/paginado =====================
    @GetMapping("/paginado")
    public ResponseEntity listarPaginado(@PageableDefault(size = 10) Pageable pageable) {
        Page<Topico> page = repository.findAll(pageable);
        return ResponseEntity.ok(page);
    }
    @GetMapping("/{id}")
    public ResponseEntity detalle(@PathVariable Long id) {
        // Buscar el tópico por ID
        return repository.findById(id)
                .map(topico -> ResponseEntity.ok(topico))  // Si existe, devuelve 200 + JSON
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no existe, 404
    }
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity actualizar(@PathVariable Long id,
                                     @RequestBody @Valid DatosRegistroTopico datos) {

        // Verificar si el tópico existe
        var optionalTopico = repository.findById(id);

        if (optionalTopico.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 si no existe
        }

        Topico topico = optionalTopico.get();

        // Verificar duplicados (título y mensaje) en otro tópico
        boolean duplicado = repository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())
                && (!topico.getTitulo().equals(datos.titulo()) || !topico.getMensaje().equals(datos.mensaje()));

        if (duplicado) {
            return ResponseEntity.badRequest().body("Tópico duplicado");
        }

        // Actualizar los campos
        topico.setTitulo(datos.titulo());
        topico.setMensaje(datos.mensaje());
        topico.setAutor(datos.autor());
        topico.setCurso(datos.curso());
        // fechaCreacion no se cambia

        repository.save(topico); // Guardar cambios

        return ResponseEntity.ok(topico);
    }
    // ===================== DELETE /topicos/{id} =====================
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminar(@PathVariable Long id) {
        // Buscar el tópico por ID
        var optionalTopico = repository.findById(id);

        if (optionalTopico.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 si no existe
        }

        // Eliminar el tópico
        repository.deleteById(id);

        return ResponseEntity.ok("Tópico eliminado"); // 200 OK
    }
}