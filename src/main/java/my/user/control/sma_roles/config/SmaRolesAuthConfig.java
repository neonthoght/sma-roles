package my.user.control.sma_roles.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
            .authorizeHttpRequests( req -> req.requestMatchers("/auth/login").permitAll())
            .authorizeHttpRequests( req -> req.requestMatchers("/auth/signup").permitAll())
            .authorizeHttpRequests( req -> req.requestMatchers("/auth/confirm-reset-password").permitAll())
            .authorizeHttpRequests( req -> req.requestMatchers("/auth/reset-password").permitAll())
            .authorizeHttpRequests( req -> req.requestMatchers("/auth/verify").permitAll())
            .authorizeHttpRequests( req -> req.requestMatchers("/auth/hello").hasRole("HELLO"))
            .authorizeHttpRequests( req -> req.requestMatchers("/auth/**").authenticated())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // Enable session creation
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
                UserDetailsService userDetailsService,
                PasswordEncoder passwordEncoder) {
            // DaoAuthenticationProvider is a common provider that uses UserDetailsService
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
            //authProvider.setUserDetailsService(userDetailsService);
            authProvider.setPasswordEncoder(passwordEncoder);

            // ProviderManager is the default implementation of AuthenticationManager
            return new ProviderManager(authProvider);
        }
}
