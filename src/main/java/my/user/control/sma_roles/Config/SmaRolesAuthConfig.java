package my.user.control.sma_roles.Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SmaRolesAuthConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/backapi/**") // Only matches these paths
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Disables session creation
            )
            .csrf(csrf -> csrf.disable()) // CSRF creates sessions by default; disable it for stateless paths
            //.authorizeHttpRequests(auth -> auth
            //    .anyRequest().authenticated()
            //)
            .securityMatcher("/auth/**")
            .authorizeHttpRequests( req -> req.requestMatchers("/auth/**").permitAll())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // Enable session creation
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
