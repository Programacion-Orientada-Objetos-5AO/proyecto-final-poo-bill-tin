package ar.edu.huergo.rlgastos.billetin.config;

import ar.edu.huergo.rlgastos.billetin.entity.security.Rol;
import ar.edu.huergo.rlgastos.billetin.entity.security.Usuario;
import ar.edu.huergo.rlgastos.billetin.repository.security.transaccion.RolRepository;
import ar.edu.huergo.rlgastos.billetin.repository.security.transaccion.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Crear roles si no existen
        crearRoles();
        
        // 2. Crear usuario admin si no existe
        crearUsuarioAdmin();
        
        System.out.println("✅ Datos iniciales creados!");
        System.out.println("👤 Usuario admin creado - Username: admin, Password: admin123");
    }

    private void crearRoles() {
        // Crear rol USER si no existe
        if (rolRepository.findByNombre("USER").isEmpty()) {
            Rol rolUser = new Rol();
            rolUser.setNombre("USER");
            rolUser.setDescripcion("Usuario básico del sistema");
            rolRepository.save(rolUser);
            System.out.println("🔧 Rol USER creado");
        }

        // Crear rol ADMIN si no existe
        if (rolRepository.findByNombre("ADMIN").isEmpty()) {
            Rol rolAdmin = new Rol();
            rolAdmin.setNombre("ADMIN");
            rolAdmin.setDescripcion("Administrador del sistema");
            rolRepository.save(rolAdmin);
            System.out.println("🔧 Rol ADMIN creado");
        }
    }

    private void crearUsuarioAdmin() {
        // Crear usuario admin si no existe
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setEmail("admin@billetin.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNombre("Administrador");
            admin.setApellido("Sistema");
            admin.setEnabled(true);

            // Asignar rol ADMIN
            Rol rolAdmin = rolRepository.findByNombre("ADMIN").get();
            admin.setRoles(Set.of(rolAdmin));

            usuarioRepository.save(admin);
            System.out.println("👤 Usuario admin creado");
        }
    }
}