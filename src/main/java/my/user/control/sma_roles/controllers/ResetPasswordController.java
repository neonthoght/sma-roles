package my.user.control.sma_roles.controllers;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import my.user.control.sma_roles.entity.UserSMA;
import my.user.control.sma_roles.services.ResetPasswordService;
import my.user.control.sma_roles.entity.ChangePasswordSMA;


@RestController
@RequestMapping("/auth")
public class ResetPasswordController {
    
    private ResponseEntity<String> response;
    private ResetPasswordService resetPasswordService;

    @Autowired
    public ResetPasswordController(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    // Отправить письмо для подтверждения сброса пароля
    @PostMapping("/confirm-reset-password")
    public ResponseEntity<String> confirmResetPassword(@RequestBody UserSMA user, HttpSession session) {
        
        resetPasswordService.confirmResetPassword(user.getEmail());
        
        return response;
    }

    //сбросить пароль и отправить новый пароль на почту
    @GetMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam UUID token, HttpSession session) {
        try {
            resetPasswordService.resetPassword(token);
            return response.ok("Пароль сброшен. Новый пароль отправлен на почту ");
        } catch (Exception e) {
            return response.badRequest().body(e.getMessage());
        }
    }
    
}


