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

import com.gym.gym_tobifit.models.Membresia;
import com.gym.gym_tobifit.services.MembresiaService;

@RestController
@RequestMapping("/api/membresias")
public class MembresiaApiController {

    private final MembresiaService service;

    public MembresiaApiController(MembresiaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Membresia> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Membresia> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Membresia> crear(@RequestBody Membresia membresia) {
        membresia.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(membresia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Membresia> actualizar(
            @PathVariable Long id,
            @RequestBody Membresia datos) {

        return service.buscarPorId(id).map(membresia -> {
            membresia.setNombre(datos.getNombre());
            membresia.setDuracionDias(datos.getDuracionDias());
            membresia.setPrecio(datos.getPrecio());
            membresia.setDescripcion(datos.getDescripcion());
            membresia.setActiva(datos.getActiva());

            return ResponseEntity.ok(service.guardar(membresia));
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
