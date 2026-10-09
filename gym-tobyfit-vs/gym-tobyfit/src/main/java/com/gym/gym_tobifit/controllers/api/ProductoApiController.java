package com.gym.gym_tobifit.controllers.api;

import com.gym.gym_tobifit.models.Producto;
import com.gym.gym_tobifit.services.ProductoService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoApiController {
    private final ProductoService productoService;
    public ProductoApiController(ProductoService productoService) { this.productoService = productoService; }

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() { return ResponseEntity.ok(productoService.listarProductos()); }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) { return ResponseEntity.ok(productoService.guardarProducto(producto)); }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        producto.setId(id);
        return ResponseEntity.ok(productoService.actualizarProducto(producto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) { productoService.eliminarProducto(id); return ResponseEntity.ok().build(); }
}
