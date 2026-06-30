package my.user.control.sma_roles.services;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import lombok.Data;
import my.user.control.sma_roles.repositories.UserSMARepository;
import org.springframework.beans.factory.annotation.Autowired;
import my.user.control.sma_roles.entity.UserSMA;
import my.user.control.sma_roles.entity.ChangePasswordSMA;
import org.apache.commons.text.RandomStringGenerator;

@Service
@Data
public class ResetPasswordService {

    private UUID token;
    private JavaMailSenderImpl mailSender;
    private UserSMARepository userRepo;
    private UserSMA user;
    private UserSMADetailService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ResetPasswordService (JavaMailSenderImpl mailSender, UserSMARepository userRepo, UserSMADetailService userService, PasswordEncoder passwordEncoder) {
        this.mailSender = mailSender;
        this.userRepo = userRepo;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // отправить письмо подтверждения сброса пароля. Записываем токен в БД. По этому токену будем искать пользователя чтобы изменить ему пароль.
    public void confirmResetPassword(String recipientEmail) {
        token = UUID.randomUUID();
        String confirmationUrl = "http://localhost:8080/auth/reset-password?token=" + token.toString();
        
        // сохраняем токен в БД, перед тем как отправить письмо
        user = userRepo.findByEmail(recipientEmail).get();
        user.setToken(token);
        userRepo.save(user);

        // отправляем письмо
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Подтверждение сброса пароля");
        message.setText("Перейдите по ссылке " + confirmationUrl + " для подтверждения сброса пароля. Новый пароль прийдет в следующем письме.");
        
        System.out.println("smtp user " + mailSender.getUsername());
        System.out.println("smtp password " + mailSender.getPassword());
        System.out.println("smtp port " + mailSender.getPort());
        mailSender.send(message);

    }

    // Найти пользователя по токену и изменить у него пароль
    public void resetPassword (UUID token) {

        
        RandomStringGenerator generator = new RandomStringGenerator.Builder().selectFrom("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789#&!".toCharArray()).get();
        String newPassword = generator.generate(6);
        user = userRepo.findByToken(token).get();
        user.setPassword(newPassword);

        // сохранить новый пароль в БД
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userRepo.save(user);

        //Отправить новый пароль на почту
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Новый пароль");
        message.setText("Пользователь " + user.getUsername() + " пароль " + newPassword);
        
        System.out.println("smtp user " + mailSender.getUsername());
        System.out.println("smtp password " + mailSender.getPassword());
        System.out.println("smtp port " + mailSender.getPort());
        mailSender.send(message);
    }
}
