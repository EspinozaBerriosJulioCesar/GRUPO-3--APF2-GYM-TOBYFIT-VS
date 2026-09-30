package com.gym.gym_tobifit.services;

import com.gym.gym_tobifit.models.Producto;
import com.gym.gym_tobifit.repositories.ProductoRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    public ProductoService(ProductoRepository productoRepository) { this.productoRepository = productoRepository; }
    public List<Producto> listarProductos() { return productoRepository.findAll(); }
    public Optional<Producto> obtenerProductoPorId(Long id) { return productoRepository.findById(id); }
    public Producto guardarProducto(Producto producto) { return productoRepository.save(producto); }
    public Producto actualizarProducto(Producto producto) {
        if (producto.getId() == null) throw new RuntimeException("El ID es obligatorio");
        Producto actual = productoRepository.findById(producto.getId()).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if (producto.getNombre() != null) actual.setNombre(producto.getNombre());
        if (producto.getCategoria() != null) actual.setCategoria(producto.getCategoria());
        if (producto.getGenero() != null) actual.setGenero(producto.getGenero());
        if (producto.getPrecio() != null) actual.setPrecio(producto.getPrecio());
        if (producto.getImagenPrincipal() != null) actual.setImagenPrincipal(producto.getImagenPrincipal());
        if (producto.getImagenes() != null) actual.setImagenes(producto.getImagenes());
        if (producto.getModelos() != null) actual.setModelos(producto.getModelos());
        if (producto.getActivo() != null) actual.setActivo(producto.getActivo());
        return productoRepository.save(actual);
    }
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) throw new RuntimeException("Producto no encontrado");
        productoRepository.deleteById(id);
    }
}
