package com.gym.gym_tobifit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.gym_tobifit.models.Usuario;
import com.gym.gym_tobifit.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Pruebas con JPA/H2 y toda la cadena JWT activa, sin simular servicios. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class RubricaIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;
    static final String PASSWORD = "Prueba2026!";

    @BeforeEach void preparar() {
        usuarios.deleteAll();
        usuarios.save(new Usuario(null, "Administrador", "Prueba", "900000001", 25,
                "admin@rubrica.local", encoder.encode(PASSWORD), Usuario.Rol.ADMIN));
        usuarios.save(new Usuario(null, "Usuario", "Prueba", "900000002", 22,
                "usuario@rubrica.local", encoder.encode(PASSWORD), Usuario.Rol.USUARIO));
    }

    String login(String cuenta) throws Exception {
        var response = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(Map.of("correo", cuenta + "@rubrica.local", "contrasena", PASSWORD))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value(cuenta.equals("admin") ? "ADMIN" : "USUARIO"))
                .andReturn().getResponse().getContentAsString();
        return json.readTree(response).get("token").asText();
    }

    static Stream<Arguments> escrituras() {
        return Stream.of("productos", "membresias", "rutinas", "promociones")
                .flatMap(recurso -> Stream.of("POST", "PUT", "DELETE")
                        .map(metodo -> Arguments.of(metodo, "/api/" + recurso + (metodo.equals("POST") ? "" : "/999999"))));
    }

    @ParameterizedTest @MethodSource("escrituras")
    void usuarioNoPuedeModificarCatalogos(String metodo, String ruta) throws Exception {
        mvc.perform(request(HttpMethod.valueOf(metodo), ruta)
                .header("Authorization", "Bearer " + login("usuario"))
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest @MethodSource("escrituras")
    void invitadoNoPuedeModificarCatalogos(String metodo, String ruta) throws Exception {
        mvc.perform(request(HttpMethod.valueOf(metodo), ruta)
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest @ValueSource(strings = {"GET", "PUT", "DELETE"})
    void usuarioNoPuedeGestionarCuentas(String metodo) throws Exception {
        Long id = usuarios.findByCorreoIgnoreCase("usuario@rubrica.local").orElseThrow().getId();
        String ruta = "/api/usuarios" + (metodo.equals("GET") ? "" : "/" + id);
        mvc.perform(request(HttpMethod.valueOf(metodo), ruta)
                .header("Authorization", "Bearer " + login("usuario"))
                .contentType(MediaType.APPLICATION_JSON).content("{\"rol\":\"ADMIN\"}"))
                .andExpect(status().isForbidden());
        assertThat(usuarios.findById(id).orElseThrow().getRol()).isEqualTo(Usuario.Rol.USUARIO);
    }

    @ParameterizedTest @ValueSource(strings = {"GET", "PUT", "DELETE"})
    void invitadoNoPuedeGestionarCuentas(String metodo) throws Exception {
        mvc.perform(request(HttpMethod.valueOf(metodo), "/api/usuarios" + (metodo.equals("GET") ? "" : "/1"))
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest @ValueSource(strings = {"productos", "membresias", "rutinas", "promociones"})
    void adminCompletaCrudConPersistenciaReal(String recurso) throws Exception {
        String token = login("admin");
        String body = switch (recurso) {
            case "productos" -> "{\"nombre\":\"Prueba CRUD\",\"categoria\":\"Ropa\",\"genero\":\"UNISEX\",\"imagenPrincipal\":\"demo.jpg\",\"precio\":10,\"activo\":true}";
            case "membresias" -> "{\"nombre\":\"Prueba CRUD\",\"duracionDias\":30,\"precio\":90,\"descripcion\":\"Ensayo\",\"activa\":true}";
            case "rutinas" -> "{\"nombre\":\"Prueba CRUD\",\"objetivo\":\"Fuerza\",\"nivel\":\"Principiante\",\"duracionSemanas\":4,\"descripcion\":\"Ensayo\"}";
            default -> "{\"nombre\":\"Prueba CRUD\",\"categoria\":\"Membresías\",\"precio\":70,\"descripcion\":\"Ensayo\",\"inicio\":\"2026-11-14\",\"fin\":\"2026-11-30\",\"activa\":true}";
        };
        String base = "/api/" + recurso;
        var creado = mvc.perform(post(base).header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().is(recurso.equals("productos") ? 200 : 201))
                .andReturn().getResponse().getContentAsString();
        long id = json.readTree(creado).get("id").asLong();
        mvc.perform(get(base)).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
        mvc.perform(get(base + "/" + id)).andExpect(status().isOk()).andExpect(jsonPath("$.nombre").value("Prueba CRUD"));
        mvc.perform(put(base + "/" + id).header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(body.replace("Prueba CRUD", "Editado")))
                .andExpect(status().isOk());
        mvc.perform(get(base + "/" + id)).andExpect(jsonPath("$.nombre").value("Editado"));
        mvc.perform(delete(base + "/" + id).header("Authorization", "Bearer " + token))
                .andExpect(status().is(recurso.equals("productos") ? 200 : 204));
        mvc.perform(get(base + "/" + id)).andExpect(status().isNotFound());
    }

    @Test void registroPublicoNoPermiteElegirAdmin() throws Exception {
        String body = "{\"nombre\":\"Nuevo\",\"apellido\":\"Ensayo\",\"telefono\":\"900000003\",\"edad\":20,\"correo\":\"nuevo@rubrica.local\",\"contrasena\":\"Prueba2026!\",\"rol\":\"ADMIN\"}";
        mvc.perform(post("/api/auth/registro").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());
        var user = usuarios.findByCorreoIgnoreCase("nuevo@rubrica.local").orElseThrow();
        assertThat(user.getRol()).isEqualTo(Usuario.Rol.USUARIO);
        assertThat(encoder.matches(PASSWORD, user.getContrasena())).isTrue();
        mvc.perform(post("/api/auth/registro").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test void loginAngularRechazaClaveIncorrecta() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"correo\":\"admin@rubrica.local\",\"contrasena\":\"incorrecta\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test void preflightAngularPermiteCabeceraJwt() throws Exception {
        mvc.perform(options("/api/productos").header("Origin", "http://localhost:4200")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "Authorization, Content-Type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }
}
