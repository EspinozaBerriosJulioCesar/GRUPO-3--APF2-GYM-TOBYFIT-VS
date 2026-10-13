
package com.gym.gym_tobifit;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.gym_tobifit.controllers.api.MembresiaApiController;
import com.gym.gym_tobifit.controllers.api.RutinaApiController;
import com.gym.gym_tobifit.models.Membresia;
import com.gym.gym_tobifit.models.Rutina;
import com.gym.gym_tobifit.security.JwtService;
import com.gym.gym_tobifit.security.JwtUtil;
import com.gym.gym_tobifit.security.MyUserDetailsService;
import com.gym.gym_tobifit.security.SecurityErrorHandler;
import com.gym.gym_tobifit.services.MembresiaService;
import com.gym.gym_tobifit.services.RutinaService;

@WebMvcTest(controllers = {
        MembresiaApiController.class,
        RutinaApiController.class
})
@AutoConfigureMockMvc(addFilters = false)
class MembresiaRutinaApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    MembresiaService membresiaService;

    @MockitoBean
    RutinaService rutinaService;

    @MockitoBean
    JwtUtil jwtUtil;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    MyUserDetailsService myUserDetailsService;

    @MockitoBean
    SecurityErrorHandler securityErrorHandler;

    Membresia membresia;
    Rutina rutina;

    @BeforeEach
    void prepararDatos() {
        membresia = new Membresia();
        membresia.setId(1L);
        membresia.setNombre("Plan mensual");
        membresia.setDuracionDias(30);
        membresia.setPrecio(99.90);
        membresia.setDescripcion("Acceso mensual al gimnasio");
        membresia.setActiva(true);

        rutina = new Rutina();
        rutina.setId(1L);
        rutina.setNombre("Rutina inicial");
        rutina.setObjetivo("Fuerza");
        rutina.setNivel("Principiante");
        rutina.setDuracionSemanas(4);
        rutina.setDescripcion("Plan de entrenamiento inicial");
    }

    @Test
    void listarMembresiasDevuelve200() throws Exception {
        when(membresiaService.listar())
                .thenReturn(Collections.singletonList(membresia));

        mockMvc.perform(get("/api/membresias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Plan mensual"));
    }

    @Test
    void buscarMembresiaInexistenteDevuelve404() throws Exception {
        when(membresiaService.buscarPorId(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/membresias/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearMembresiaDevuelve201() throws Exception {
        when(membresiaService.guardar(any(Membresia.class)))
                .thenReturn(membresia);

        mockMvc.perform(post("/api/membresias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(membresia)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Plan mensual"));
    }

    @Test
    void actualizarMembresiaInexistenteDevuelve404() throws Exception {
        when(membresiaService.buscarPorId(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/membresias/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(membresia)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarMembresiaInexistenteDevuelve404() throws Exception {
        when(membresiaService.buscarPorId(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/membresias/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarRutinasDevuelve200() throws Exception {
        when(rutinaService.listar())
                .thenReturn(Collections.singletonList(rutina));

        mockMvc.perform(get("/api/rutinas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Rutina inicial"));
    }

    @Test
    void buscarRutinaInexistenteDevuelve404() throws Exception {
        when(rutinaService.buscarPorId(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/rutinas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearRutinaDevuelve201() throws Exception {
        when(rutinaService.guardar(any(Rutina.class)))
                .thenReturn(rutina);

        mockMvc.perform(post("/api/rutinas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rutina)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Rutina inicial"));
    }

    @Test
    void actualizarRutinaInexistenteDevuelve404() throws Exception {
        when(rutinaService.buscarPorId(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/rutinas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rutina)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarRutinaInexistenteDevuelve404() throws Exception {
        when(rutinaService.buscarPorId(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/rutinas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarMembresiaExistenteDevuelve200() throws Exception {
        when(membresiaService.buscarPorId(1L))
                .thenReturn(Optional.of(membresia));

        mockMvc.perform(get("/api/membresias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Plan mensual"));
    }

    @Test
    void actualizarMembresiaExistenteDevuelve200() throws Exception {
        when(membresiaService.buscarPorId(1L))
                .thenReturn(Optional.of(membresia));
        when(membresiaService.guardar(any(Membresia.class)))
                .thenReturn(membresia);

        mockMvc.perform(put("/api/membresias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(membresia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Plan mensual"));
    }

    @Test
    void eliminarMembresiaExistenteDevuelve204() throws Exception {
        when(membresiaService.buscarPorId(1L))
                .thenReturn(Optional.of(membresia));

        mockMvc.perform(delete("/api/membresias/1"))
                .andExpect(status().isNoContent());

        verify(membresiaService).eliminar(1L);
    }

    @Test
    void buscarRutinaExistenteDevuelve200() throws Exception {
        when(rutinaService.buscarPorId(1L))
                .thenReturn(Optional.of(rutina));

        mockMvc.perform(get("/api/rutinas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Rutina inicial"));
    }

    @Test
    void actualizarRutinaExistenteDevuelve200() throws Exception {
        when(rutinaService.buscarPorId(1L))
                .thenReturn(Optional.of(rutina));
        when(rutinaService.guardar(any(Rutina.class)))
                .thenReturn(rutina);

        mockMvc.perform(put("/api/rutinas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rutina)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Rutina inicial"));
    }

    @Test
    void eliminarRutinaExistenteDevuelve204() throws Exception {
        when(rutinaService.buscarPorId(1L))
                .thenReturn(Optional.of(rutina));

        mockMvc.perform(delete("/api/rutinas/1"))
                .andExpect(status().isNoContent());

        verify(rutinaService).eliminar(1L);
    }

    @Test
    void listarMembresiasVaciasDevuelve200() throws Exception {
        when(membresiaService.listar())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/membresias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(membresiaService).listar();
    }

    @Test
    void crearMembresiaConJsonInvalidoDevuelve400() throws Exception {
        mockMvc.perform(post("/api/membresias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{json-invalido"))
                .andExpect(status().isBadRequest());

        verify(membresiaService, never())
                .guardar(any(Membresia.class));
    }

    @Test
    void listarRutinasVaciasDevuelve200() throws Exception {
        when(rutinaService.listar())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/rutinas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(rutinaService).listar();
    }

    @Test
    void crearRutinaConJsonInvalidoDevuelve400() throws Exception {
        mockMvc.perform(post("/api/rutinas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{json-invalido"))
                .andExpect(status().isBadRequest());

        verify(rutinaService, never())
                .guardar(any(Rutina.class));
    }


}
