package gts.spring.musicManagement.config;

import gts.spring.musicManagement.entity.Role;
import gts.spring.musicManagement.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            Role userRole = roleRepository.findByName("USER").orElseGet(() -> roleRepository.save(Role.builder().name("USER").build()));
            Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").build()));
        };
    }
}
