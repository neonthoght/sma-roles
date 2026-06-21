package my.user.control.sma_roles.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
//import my.user.control.sma_roles.repositories.UserSMARepository;

import my.user.control.sma_roles.services.UserSMADetailService;

@Controller
@RequestMapping("/auth")
public class UserSMAController {
    
    UserSMADetailService userService;

    public UserSMAController(UserSMADetailService userService) {
        this.userService = userService;
    }

    @GetMapping("/rights")
    public ResponseEntity<String> getRights(@RequestParam String user) {
        return ResponseEntity.ok(userService.loadUserByUsername(user).getAuthorities().toString());
    }
    
}
