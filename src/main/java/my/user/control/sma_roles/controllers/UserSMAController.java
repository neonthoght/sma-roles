package my.user.control.sma_roles.controllers;

import my.user.control.sma_roles.entity.ChangePasswordSMA;
import my.user.control.sma_roles.entity.UserInfoSMA;
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
import org.springframework.security.core.GrantedAuthority;

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
import java.util.ArrayList;
import java.util.Iterator;

@Controller
@RequestMapping("/auth")
public class UserSMAController {
    
    private UserSMARepository userRepo;
    private UserSMADetailService userService;
    private ResponseEntity<UserInfoSMA> response;
    
    public UserSMAController (UserSMADetailService userService, UserSMARepository userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;

    }

    @GetMapping("/user-info") // пользователь может получить информацию только о себе, в остальных случаях возвращает ошибку forbidden 403
    public ResponseEntity<UserInfoSMA> getUserInfo(HttpSession session, @RequestParam String username) {

        UserSMA user = (UserSMA) userService.loadUserByUsername(username);
        UserInfoSMA userInfo = new UserInfoSMA();

        ArrayList<String> authoritiesInfo = new ArrayList();

        userInfo.setActive(user.getIsActive());
        userInfo.setUsername(user.getUsername());

        Iterator<? extends GrantedAuthority> authoritiesIterator = user.getAuthorities().iterator(); 
        while (authoritiesIterator.hasNext()) {
            authoritiesInfo.add(authoritiesIterator.next().toString());
        }
        userInfo.setAuthorities(authoritiesInfo);

        userInfo.setEmail(user.getEmail());

        response = response.ok().body(userInfo);

        return response;
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
