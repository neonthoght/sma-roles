package my.user.control.sma_roles.controllers;

import my.user.control.sma_roles.entity.ChangePasswordSMA;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.ListeningSecurityContextHolderStrategy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordSMA dto,
            @AuthenticationPrincipal UserDetails currentUser) {
        
        try {
            userService.changeUserPassword(currentUser.getUsername(), dto);
            return ResponseEntity.ok("Password updated successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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
