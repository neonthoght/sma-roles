package my.user.control.sma_roles.controllers;

import my.user.control.sma_roles.entity.UserSMA;
import my.user.control.sma_roles.repositories.UserSMARepository;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;
//import my.user.control.sma_roles.repositories.UserSMARepository;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.ListeningSecurityContextHolderStrategy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import my.user.control.sma_roles.services.UserSMADetailService;

@Controller
@RequestMapping("/auth")
public class UserSMAController {
    
    @Autowired
    public UserSMARepository userRepo;
    UserSMADetailService userService;
    //private AuthenticationManager authenticationManager;

    public UserSMAController (UserSMADetailService userService) {
        this.userService = userService;

    }

    /* 
    public UserSMAController(UserSMADetailService userService) {
        this.userService = userService;
        authProvider = new DaoAuthenticationProvider(userService);
        authenticationManager = new ProviderManager(authProvider);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
    }

    // поправить метод, сделал солянку из гугловского ответа и мануала документации спринг
    // сама дока https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserSMA user,  HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(token); 
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();

        //DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userService);
        //authenticationManager = new ProviderManager(authProvider);

        context.setAuthentication(authentication); 
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response); 
        return ResponseEntity.ok("OK");
    }
    */

    @GetMapping("/rights")
    public ResponseEntity<String> getRights(@RequestParam String user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println( auth.getAuthorities().toString());
        return ResponseEntity.ok(userService.loadUserByUsername(user).getAuthorities().toString());
    }

    @GetMapping("/hello")
    public ResponseEntity<String> getHello() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println( auth.getAuthorities().toString());
        return ResponseEntity.ok("hello!");
    }
    
}
