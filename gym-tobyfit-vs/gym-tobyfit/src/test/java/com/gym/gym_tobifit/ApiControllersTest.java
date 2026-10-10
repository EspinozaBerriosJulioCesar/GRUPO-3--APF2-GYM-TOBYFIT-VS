
package com.gym.gym_tobifit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.gym_tobifit.controllers.api.ProductoApiController;
import com.gym.gym_tobifit.controllers.api.UsuarioApiController;
import com.gym.gym_tobifit.models.Producto;
import com.gym.gym_tobifit.models.Usuario;
import com.gym.gym_tobifit.services.ProductoService;
import com.gym.gym_tobifit.services.UsuarioService;
import com.gym.gym_tobifit.security.JwtService;
import com.gym.gym_tobifit.security.CustomUserDetailsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {
        ProductoApiController.class,
        UsuarioApiController.class
})
@AutoConfigureMockMvc(addFilters = false)
class ApiControllersTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductoService productoService;

    @MockBean
    UsuarioService usuarioService;

    @MockBean
    JwtService jwtService;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    Producto producto;
    Usuario usuario;

    @BeforeEach
    void datos() {
        producto = new Producto(
                1L,
                "Polo deportivo",
                "Ropa",
                "Masculino",
                new BigDecimal("59.90"),
                "polo.jpg",
                "[]",
                "[]",
                true
        );

        usuario = new Usuario(
                1L,
                "Julio",
                "Espinoza",
                "934365639",
                25,
                "julio@gmail.com",
                "123456",
                Usuario.Rol.USUARIO
        );
    }

    @Test
    void test01ListarProductos() throws Exception {
        when(productoService.listarProductos())
                .thenReturn(Collections.singletonList(producto));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void test02ListarProductosVacio() throws Exception {
        when(productoService.listarProductos())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void test03ListarVariosProductos() throws Exception {
        Producto p2 = new Producto(
                2L,
                "Short deportivo",
                "Ropa",
                "Masculino",
                new BigDecimal("39.90"),
                "short.jpg",
                "[]",
                "[]",
                true
        );

        when(productoService.listarProductos())
                .thenReturn(Arrays.asList(producto, p2));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void test04BuscarProductoExistente() throws Exception {
        when(productoService.obtenerProductoPorId(1L))
                .thenReturn(Optional.of(producto));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Polo deportivo"));
    }

    @Test
    void test05BuscarProductoNoExistente() throws Exception {
        when(productoService.obtenerProductoPorId(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void test06BuscarProductoOtroId() throws Exception {
        producto.setId(5L);

        when(productoService.obtenerProductoPorId(5L))
                .thenReturn(Optional.of(producto));

        mockMvc.perform(get("/api/productos/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void test07CrearProducto() throws Exception {
        when(productoService.guardarProducto(any()))
                .thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Polo deportivo"));
    }

    @Test
    void test08CrearProductoCategoria() throws Exception {
        producto.setNombre("Pantalon deportivo");

        when(productoService.guardarProducto(any()))
                .thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria").value("Ropa"));
    }

    @Test
    void test09CrearProductoActivo() throws Exception {
        when(productoService.guardarProducto(any()))
                .thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(true));
    }

    @Test
    void test10ActualizarProducto() throws Exception {
        producto.setNombre("Polo actualizado");

        when(productoService.actualizarProducto(any()))
                .thenReturn(producto);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Polo actualizado"));
    }

    @Test
    void test11ActualizarPrecioProducto() throws Exception {
        producto.setPrecio(new BigDecimal("79.90"));

        when(productoService.actualizarProducto(any()))
                .thenReturn(producto);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.precio").value(79.90));
    }

    @Test
    void test12ActualizarEstadoProducto() throws Exception {
        producto.setActivo(false);

        when(productoService.actualizarProducto(any()))
                .thenReturn(producto);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));
    }

    @Test
    void test13EliminarProducto() throws Exception {
        doNothing().when(productoService).eliminarProducto(1L);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void test14EliminarProductoOtroId() throws Exception {
        doNothing().when(productoService).eliminarProducto(5L);

        mockMvc.perform(delete("/api/productos/5"))
                .andExpect(status().isOk());
    }

    @Test
    void test15EliminarProductoTercerId() throws Exception {
        doNothing().when(productoService).eliminarProducto(10L);

        mockMvc.perform(delete("/api/productos/10"))
                .andExpect(status().isOk());
    }

    @Test
    void test16ListarUsuarios() throws Exception {
        when(usuarioService.findAll())
                .thenReturn(Collections.singletonList(usuario));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void test17ListarUsuariosVacio() throws Exception {
        when(usuarioService.findAll())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void test18ListarVariosUsuarios() throws Exception {
        Usuario u2 = new Usuario(
                2L,
                "Ana",
                "Lopez",
                "988888888",
                24,
                "ana@gmail.com",
                "123456",
                Usuario.Rol.USUARIO
        );

        when(usuarioService.findAll())
                .thenReturn(Arrays.asList(usuario, u2));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void test19ActualizarUsuario() throws Exception {
        when(usuarioService.actualizarUsuario(any()))
                .thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void test20ActualizarNombreUsuario() throws Exception {
        usuario.setNombre("Julio Cesar");

        when(usuarioService.actualizarUsuario(any()))
                .thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Julio Cesar"));
    }

    @Test
    void test21ActualizarApellidoUsuario() throws Exception {
        usuario.setApellido("Espinoza Berrios");

        when(usuarioService.actualizarUsuario(any()))
                .thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apellido").value("Espinoza Berrios"));
    }

    @Test
    void test22EliminarUsuario() throws Exception {
        doNothing().when(usuarioService).eliminarUsuario(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void test23EliminarUsuarioOtroId() throws Exception {
        doNothing().when(usuarioService).eliminarUsuario(5L);

        mockMvc.perform(delete("/api/usuarios/5"))
                .andExpect(status().isOk());
    }

    @Test
    void test24EliminarUsuarioTercerId() throws Exception {
        doNothing().when(usuarioService).eliminarUsuario(10L);

        mockMvc.perform(delete("/api/usuarios/10"))
                .andExpect(status().isOk());
    }
}
