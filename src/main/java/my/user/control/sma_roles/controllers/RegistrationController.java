package my.user.control.sma_roles.controllers;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import my.user.control.sma_roles.entity.UserSMA;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import my.user.control.sma_roles.services.RegistrationService;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    private ResponseEntity<String> response;
    private int result = 0;
    private RegistrationService registrationService;
    private UserSMA user;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    public RegistrationController(){}

    @PostMapping(value="/signup")
    public ResponseEntity<String> signup(@RequestBody UserSMA user, HttpSession session) throws Exception {
        System.out.println(user.getUsername() + user.getPassword());
        result = registrationService.signup(user.getUsername(), user.getPassword(), user.getEmail(), session);
        if (result == 0) {
            response = ResponseEntity.status(200).body("user created!");
        } 
        else if (result == 2) { // email уже используется
            response = ResponseEntity.status(200).body("email already used!");
        } 
        else {
            response = ResponseEntity.status(200).body("user already exists!");
            System.out.println(response);
        }

        return response;
    }
    
    @GetMapping(value="/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam UUID token) {
        registrationService.verifyEmail(token);
        return  response;
    }

    // поменять email
    @PostMapping("/change-email")
    public ResponseEntity<String> changeEmail(@RequestBody UserSMA user) {
        registrationService.changeEmail(user.getEmail());
    }
}
