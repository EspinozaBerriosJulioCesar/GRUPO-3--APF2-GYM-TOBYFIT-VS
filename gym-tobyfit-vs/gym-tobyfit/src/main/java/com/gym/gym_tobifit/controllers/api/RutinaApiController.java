
package com.gym.gym_tobifit.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.gym_tobifit.models.Rutina;
import com.gym.gym_tobifit.services.RutinaService;

@RestController
@RequestMapping("/api/rutinas")
public class RutinaApiController {

    private final RutinaService service;

    public RutinaApiController(RutinaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Rutina> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rutina> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Rutina> crear(@RequestBody Rutina rutina) {
        rutina.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(rutina));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rutina> actualizar(
            @PathVariable Long id,
            @RequestBody Rutina datos) {

        return service.buscarPorId(id).map(rutina -> {
            rutina.setNombre(datos.getNombre());
            rutina.setObjetivo(datos.getObjetivo());
            rutina.setNivel(datos.getNivel());
            rutina.setDuracionSemanas(datos.getDuracionSemanas());
            rutina.setDescripcion(datos.getDescripcion());

            return ResponseEntity.ok(service.guardar(rutina));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
