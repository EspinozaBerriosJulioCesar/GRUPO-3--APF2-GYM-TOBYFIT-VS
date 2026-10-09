package com.gym.gym_tobifit.config;

import com.gym.gym_tobifit.models.Producto;
import com.gym.gym_tobifit.models.Usuario;
import com.gym.gym_tobifit.repositories.ProductoRepository;
import com.gym.gym_tobifit.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

/** Solo carga datos ficticios cuando se solicita explícitamente el perfil demo. */
@Configuration
@Profile("demo")
public class DemoDataConfig {
    @Bean
    CommandLineRunner demoData(UsuarioRepository usuarios, ProductoRepository productos, PasswordEncoder encoder) {
        return args -> {
            if (usuarios.findByCorreoIgnoreCase("admin.jwt@tobyfit.local").isEmpty()) {
                usuarios.save(new Usuario(null, "Admin", "Demo", "900000001", 25,
                        "admin.jwt@tobyfit.local", encoder.encode("DemoAdmin2026!"), Usuario.Rol.ADMIN));
            }
            if (usuarios.findByCorreoIgnoreCase("usuario.jwt@tobyfit.local").isEmpty()) {
                usuarios.save(new Usuario(null, "Usuario", "Demo", "900000002", 22,
                        "usuario.jwt@tobyfit.local", encoder.encode("DemoUsuario2026!"), Usuario.Rol.USUARIO));
            }
            if (productos.count() == 0) {
                productos.save(new Producto(null, "Polo deportivo demo", "Ropa", "Unisex",
                        new BigDecimal("59.90"), "polo-demo.jpg", "[]", "[]", true));
            }
        };
    }
}
