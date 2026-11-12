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

import com.gym.gym_tobifit.models.Promocion;
import com.gym.gym_tobifit.services.PromocionService;

@RestController
@RequestMapping("/api/promociones")
public class PromocionApiController {

    private final PromocionService service;

    public PromocionApiController(PromocionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Promocion> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promocion> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Promocion> crear(@RequestBody Promocion promocion) {
        promocion.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(promocion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promocion> actualizar(
            @PathVariable Long id,
            @RequestBody Promocion datos) {

        return service.buscarPorId(id).map(promocion -> {
            promocion.setNombre(datos.getNombre());
            promocion.setCategoria(datos.getCategoria());
            promocion.setDescripcion(datos.getDescripcion());
            promocion.setInicio(datos.getInicio());
            promocion.setFin(datos.getFin());
            promocion.setPrecio(datos.getPrecio());
            promocion.setActiva(datos.getActiva());

            return ResponseEntity.ok(service.guardar(promocion));
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

