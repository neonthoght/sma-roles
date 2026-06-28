package my.user.control.sma_roles.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import my.user.control.sma_roles.entity.UserSMA;
import my.user.control.sma_roles.services.UserSMADetailService;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    
    // 1. Define the repository to persist context across requests
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
            
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Autowired
    UserSMADetailService userSMAService;

    UserSMA user;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserSMA loginRequest, 
                      HttpServletRequest request, 
                      HttpServletResponse response) {

        
                UsernamePasswordAuthenticationToken token = 
                UsernamePasswordAuthenticationToken.unauthenticated(
                        loginRequest.getUsername(), 
                        loginRequest.getPassword()
                        //userSMAService.loadUserByUsername(loginRequest.getUsername()).getAuthorities()
                );
        
        /*
        // 2. Create an unauthenticated token
        UsernamePasswordAuthenticationToken token = 
                UsernamePasswordAuthenticationToken.authenticated(
                        loginRequest.getUsername(), 
                        loginRequest.getPassword(),
                        userSMAService.loadUserByUsername(loginRequest.getUsername()).getAuthorities()
                );
        

        UserDetails user = userSMAService.loadUserByUsername(loginRequest.getUsername());
        System.out.println(user.getUsername() + " " + user.getPassword() + " " + user.getAuthorities());
        Authentication token = new UsernamePasswordAuthenticationToken(
            user, 
            null, 
            user.getAuthorities()
        );
        */

        // 3. Verify user credentials
        Authentication authentication = authenticationManager.authenticate(token);

        // 4. Create an empty context and assign the authenticated token
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);

        // 5. Update the local thread strategy
        securityContextHolderStrategy.setContext(context);

        // 6. Explicitly save the context to the repository (Session/Cookies)
        securityContextRepository.saveContext(context, request, response);
        

        return ResponseEntity.ok("Login success");
    }
}

