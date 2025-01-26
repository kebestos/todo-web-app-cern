package ch.cern.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

//    private final UserDetailsServiceImpl userDetailsServices;
//
//    public SecurityConfig(UserDetailsServiceImpl userDetailsServices) {
//        this.userDetailsServices = userDetailsServices;
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers(HttpMethod.GET,"/").permitAll()
//                        .requestMatchers("/h2-console/**").permitAll()
//                        .requestMatchers("/api/task/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(Customizer.withDefaults())
//                .formLogin(Customizer.withDefaults());
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
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

    //    @Bean
//    UserDetailsService userDetailsService() {
//        return userDetailsServices;
////        UserDetails user = User
////                .withUsername("user")
////                .password("{noop}password")
////                .roles("USER")
////                .build();
////
////        UserDetails admin = User
////                .withUsername("admin")
////                .password("{noop}admin")
////                .roles("ADMIN")
////                .build();
////        return new InMemoryUserDetailsManager(user, admin);
//    }

//    @Bean
//    public AuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService());
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//
//    }

//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        return http
//                .authenticationManager(authManagerBuilder ->
//                        authManagerBuilder
//                                .userDetailsService(userDetailsService)
//                                .passwordEncoder(passwordEncoder())
//                )
//                .getSharedObject(AuthenticationManager.class);
//    }


//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        return http
//                .authenticationManager(authManagerBuilder ->
//                        authManagerBuilder
//                                .userDetailsService(userDetailsService())
//                                .passwordEncoder(passwordEncoder())
//                )
//                .getSharedObject(AuthenticationManager.class);
//    }

//    @Bean
//    public CommandLineRunner init(UserRepository userRepository) {
//        return args -> {
//            userRepository.save(new CustomUser(1L, "admin", "admin","ROLE_ADMIN", 1L));
//        };
//    }
}
