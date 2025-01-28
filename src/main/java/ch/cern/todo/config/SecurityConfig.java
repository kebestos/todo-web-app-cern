package ch.cern.todo.config;

import ch.cern.todo.infrastructure.model.CustomUser;
import ch.cern.todo.infrastructure.model.Role;
import ch.cern.todo.infrastructure.model.Task;
import ch.cern.todo.infrastructure.model.TaskCategory;
import ch.cern.todo.infrastructure.repository.CustomUserRepository;
import ch.cern.todo.infrastructure.repository.RoleRepository;
import ch.cern.todo.infrastructure.repository.TaskCategoryRepository;
import ch.cern.todo.infrastructure.repository.TaskRepository;
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

import java.time.LocalDateTime;
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
    public CommandLineRunner initUsersAndRoles(RoleRepository roleRepository,
                                               CustomUserRepository customUserRepository,
                                               TaskCategoryRepository taskCategoryRepository,
                                               TaskRepository taskRepository) {
        System.out.println("DATA initialized");
        return args -> {
            Role roleAdmin = new Role(null, "ADMIN");
            Role roleUser = new Role(null, "USER");

            Set<Role> roles_admin = new HashSet<>();
            roles_admin.add(roleAdmin);
            Set<Role> roles_user = new HashSet<>();
            roles_user.add(roleUser);

            TaskCategory taskCategoryDev = new TaskCategory(null, "Dev", "Technical development task");
            TaskCategory taskCategoryStudy = new TaskCategory(null, "Study", "Research task");

            CustomUser admin = new CustomUser(null, "admin", "admin", roles_admin, null);
            CustomUser user = new CustomUser(null, "user", "user", roles_user, null);

            Task task = new Task(null, "API Post task", "make an api rest to create task",
                    LocalDateTime.of(2013, 4, 23, 18, 30, 20), taskCategoryDev, admin);

            Task task_1 = new Task(null, "task Name 1", "task description 1",
                    LocalDateTime.of(2013, 4, 20, 18, 30, 20), taskCategoryDev, user);
            Task task_2 = new Task(null, "task Name 2", "task description 2",
                    LocalDateTime.of(2013, 4, 25, 18, 30, 20), taskCategoryDev, user);
            Task task_3 = new Task(null, "task Name 3", "task description 3",
                    LocalDateTime.of(2013, 4, 23, 18, 30, 20), taskCategoryStudy, user);
            Task task_4 = new Task(null, "diff", "diff",
                    LocalDateTime.of(2013, 4, 23, 18, 30, 20), taskCategoryStudy, user);

            roleRepository.save(roleAdmin);
            roleRepository.save(roleUser);

            customUserRepository.save(admin);
            customUserRepository.save(user);

            taskCategoryRepository.save(taskCategoryDev);
            taskCategoryRepository.save(taskCategoryStudy);

            taskRepository.save(task);
            taskRepository.save(task_1);
            taskRepository.save(task_2);
            taskRepository.save(task_3);
            taskRepository.save(task_4);
        };
    }
}
