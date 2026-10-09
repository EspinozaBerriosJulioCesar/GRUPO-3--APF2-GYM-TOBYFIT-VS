package com.gym.gym_tobifit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.gym_tobifit.models.Producto;
import com.gym.gym_tobifit.models.Usuario;
import com.gym.gym_tobifit.repositories.ProductoRepository;
import com.gym.gym_tobifit.repositories.UsuarioRepository;
import com.gym.gym_tobifit.security.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Repositorios JPA reales y cadena de seguridad activa. No utiliza usuarios simulados. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JwtSecurityIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;
    @Autowired UsuarioRepository usuarios;
    @Autowired ProductoRepository productos;
    @Autowired PasswordEncoder encoder;
    @Autowired JwtUtil jwtUtil;
    @Value("${jwt.secret}") String secret;
    private static final String ADMIN = "admin@test.local";
    private static final String USER = "usuario@test.local";
    private static final String PASSWORD = "Prueba2026!";

    @BeforeEach
    void datos() {
        productos.deleteAll();
        usuarios.deleteAll();
        usuarios.save(new Usuario(null, "Admin", "Prueba", "900000001", 25,
                ADMIN, encoder.encode(PASSWORD), Usuario.Rol.ADMIN));
        usuarios.save(new Usuario(null, "Usuario", "Prueba", "900000002", 22,
                USER, encoder.encode(PASSWORD), Usuario.Rol.USUARIO));
        productos.save(new Producto(null, "Polo de prueba", "Ropa", "Unisex",
                new BigDecimal("59.90"), "polo.jpg", "[]", "[]", true));
    }

    private String login(String username) throws Exception {
        return mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Map.of("username", username, "password", PASSWORD))))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn().getResponse().getContentAsString();
    }

    @Test void loginGeneraTokenFirmadoDesdeBaseDeDatos() throws Exception {
        String token = login(ADMIN);
        assertThat(token.split("\\.")).hasSize(3);
        var parsed = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build().parseClaimsJws(token);
        assertThat(parsed.getHeader().getAlgorithm()).isEqualTo("HS256");
        assertThat(parsed.getBody().getSubject()).isEqualTo(ADMIN);
        assertThat(parsed.getBody().getExpiration().getTime() - parsed.getBody().getIssuedAt().getTime())
                .isEqualTo(3600000);
        assertThat(parsed.getBody()).doesNotContainKeys("password", "contrasena");
        assertThat(encoder.matches(PASSWORD, usuarios.findByCorreoIgnoreCase(ADMIN).orElseThrow().getContrasena()))
                .isTrue();
    }

    @Test void claveIncorrectaNoEntregaToken() throws Exception {
        mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Map.of("username", ADMIN, "password", "incorrecta"))))
                .andExpect(status().isUnauthorized()).andExpect(jsonPath("$.message").value("Credenciales inválidas"));
    }

    @Test void usuarioDesconocidoNoEntregaToken() throws Exception {
        mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"nadie@test.local\",\"password\":\"incorrecta\"}"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"{}", "{\"username\":\"\",\"password\":\"x\"}", "{\"username\":\"a\"}", "[1,2]", "{"})
    void loginRechazaCuerpoInvalido(String body) throws Exception {
        mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test void productosSinTokenRetorna401() throws Exception {
        mockMvc.perform(get("/api/productos")).andExpect(status().isUnauthorized())
                .andExpect(header().string("WWW-Authenticate", "Bearer"));
    }

    @Test void usuariosSinTokenRetorna401() throws Exception {
        mockMvc.perform(get("/api/usuarios")).andExpect(status().isUnauthorized());
    }

    @Test void productosConTokenDeLoginRetorna200() throws Exception {
        mockMvc.perform(get("/api/productos").header("Authorization", "Bearer " + login(ADMIN)))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].nombre").value("Polo de prueba"));
    }

    @Test void usuarioAutenticadoPuedeLeerProductos() throws Exception {
        mockMvc.perform(get("/api/productos").header("Authorization", "Bearer " + login(USER)))
                .andExpect(status().isOk());
    }

    @Test void usuarioSinRolAdminRecibe403() throws Exception {
        mockMvc.perform(get("/api/usuarios").header("Authorization", "Bearer " + login(USER)))
                .andExpect(status().isForbidden()).andExpect(jsonPath("$.status").value(403));
    }

    @Test void listadoDeUsuariosNoExponeHash() throws Exception {
        mockMvc.perform(get("/api/usuarios").header("Authorization", "Bearer " + login(ADMIN)))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].contrasena").doesNotExist())
                .andExpect(jsonPath("$[1].contrasena").doesNotExist());
    }

    @Test void usuarioNoPuedeCrearProductos() throws Exception {
        mockMvc.perform(post("/api/productos").header("Authorization", "Bearer " + login(USER))
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test void adminPuedeCrearProductos() throws Exception {
        String product = "{\"nombre\":\"Nuevo\",\"categoria\":\"Ropa\",\"genero\":\"Unisex\","
                + "\"precio\":10,\"imagenPrincipal\":\"nuevo.jpg\"}";
        mockMvc.perform(post("/api/productos").header("Authorization", "Bearer " + login(ADMIN))
                .contentType(MediaType.APPLICATION_JSON).content(product))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nombre").value("Nuevo"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Bearer no-es-un-jwt", "Bearer ", "Basic dXNlcjpwYXNz", "Bearer a.b.c"})
    void encabezadoInvalidoRetorna401(String header) throws Exception {
        mockMvc.perform(get("/api/productos").header("Authorization", header))
                .andExpect(status().isUnauthorized());
    }

    @Test void tokenAlteradoRetorna401() throws Exception {
        String[] parts = login(ADMIN).split("\\.");
        parts[2] = (parts[2].startsWith("A") ? "B" : "A") + parts[2].substring(1);
        mockMvc.perform(get("/api/productos").header("Authorization", "Bearer " + String.join(".", parts)))
                .andExpect(status().isUnauthorized());
    }

    @Test void tokenVencidoRetorna401() throws Exception {
        String expired = Jwts.builder().setSubject(ADMIN).setIssuedAt(new Date(System.currentTimeMillis() - 60000))
                .setExpiration(new Date(System.currentTimeMillis() - 30000))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256).compact();
        mockMvc.perform(get("/api/productos").header("Authorization", "Bearer " + expired))
                .andExpect(status().isUnauthorized());
    }

    @Test void usuarioEliminadoYaNoPuedeUsarSuToken() throws Exception {
        String token = login(USER);
        usuarios.delete(usuarios.findByCorreoIgnoreCase(USER).orElseThrow());
        mockMvc.perform(get("/api/productos").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test void cambioDeRolSeAplicaAlMismoToken() throws Exception {
        String token = login(ADMIN);
        Usuario admin = usuarios.findByCorreoIgnoreCase(ADMIN).orElseThrow();
        admin.setRol(Usuario.Rol.USUARIO);
        usuarios.save(admin);
        mockMvc.perform(get("/api/usuarios").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test void autenticacionNoSeConservaSinToken() throws Exception {
        var result = mockMvc.perform(get("/api/productos").header("Authorization", "Bearer " + login(ADMIN)))
                .andExpect(status().isOk()).andReturn();
        assertThat(result.getRequest().getSession(false)).isNull();
        mockMvc.perform(get("/api/productos")).andExpect(status().isUnauthorized());
    }

    @Test void usuarioSinRolNoPuedeAutenticarse() throws Exception {
        Usuario user = usuarios.findByCorreoIgnoreCase(USER).orElseThrow();
        user.setRol(null);
        usuarios.save(user);
        mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Map.of("username", USER, "password", PASSWORD))))
                .andExpect(status().isUnauthorized());
    }
}
