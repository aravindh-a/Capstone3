package gts.spring.musicManagement.config;

import gts.spring.musicManagement.entity.Role;
import gts.spring.musicManagement.entity.User;
import gts.spring.musicManagement.repository.RoleRepository;
import gts.spring.musicManagement.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
@Profile("test")
public class TestDataInitializer {
    @Bean
    public CommandLineRunner loadData(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            Role userRole = roleRepository.findByName("USER").orElseGet(() -> roleRepository.save(Role.builder().name("USER").build()));
            Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").build()));

            User user = new User();
            user.setUsername("testuser");
            user.setPassword(new BCryptPasswordEncoder().encode("password"));
            user.setRoles(Set.of(userRole));
            if (userRepository.findByUsername("testuser").isPresent()) {
                return;
            }
            else userRepository.save(user);

            User admin = new User();
            admin.setUsername("testadmin");
            admin.setPassword(new BCryptPasswordEncoder().encode("adminpassword"));
            admin.setRoles(Set.of(userRole, adminRole));
            if (userRepository.findByUsername("testadmin").isPresent()) {
                return;
            }
            else userRepository.save(user);
        };
    }
}
