package ch.cern.todo.config;

import ch.cern.todo.infrastructure.entity.CustomUser;
import ch.cern.todo.infrastructure.entity.Role;
import ch.cern.todo.infrastructure.repository.RoleRepository;
import ch.cern.todo.infrastructure.repository.CustomUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
//        return  new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CommandLineRunner initUsersAndRoles(RoleRepository roleRepository, CustomUserRepository customUserRepository) {
        System.out.println("DATA initialized");
        return args -> {
            Role roleAdmin = new Role(null,"ADMIN");
            Role roleUser = new Role(null,"USER");

            Set<Role> roles_admin = new HashSet<>();
            roles_admin.add(roleAdmin);
            Set<Role> roles_user = new HashSet<>();
            roles_user.add(roleUser);

            roleRepository.save(roleAdmin);
            roleRepository.save(roleUser);
            customUserRepository.save(new CustomUser(null,"admin", "admin", roles_admin,null));
            customUserRepository.save(new CustomUser(null,"user", "user", roles_user,null));
        };
    }
}
