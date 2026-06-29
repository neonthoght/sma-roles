package my.user.control.sma_roles.services;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import my.user.control.sma_roles.entity.UserSMA;
import my.user.control.sma_roles.repositories.UserSMARepository;

@Service
public class RegistrationSMAService {
    
    @Autowired public JavaMailSenderImpl mailSender;
    @Autowired public UserSMARepository userRepository;
    @Autowired public PasswordEncoder passwordEncoder;
    
    UUID token;
    UserSMA user;
    public int result = 0; // результат выполнения метода


    // Отправить письмо для подтверждения email
    public void sendVerificationEmail(String recipientEmail, UUID token)throws MailAuthenticationException {
        
        String confirmationUrl = "http://localhost:8081/auth/verify?token=" + token.toString();
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Подтверждение регистрации");
        message.setText("Перейдите по ссылке для подтверждения аккаунта: " + confirmationUrl);
        
        System.out.println("smtp user " + mailSender.getUsername());
        System.out.println("smtp password " + mailSender.getPassword());
        System.out.println("smtp port " + mailSender.getPort());
        mailSender.send(message);

    }

    // пользователь перешёл по ссылке - подтверждает почту при регистрации.
    // в ссылке токен, который надо сравнить с токеном из БД
    // 0 - email подтвержден, зарегистрированная учетная запись (УЗ) становится активной.
    // 1 - ошибка, токен не найден.
    public int verifyEmail(UUID token) {
        if (compareToken(token) == 0) {
            user = userRepository.findByToken(token).get();
            user.setIsActive(true);
            userRepository.save(user);
            result = 0;
        } else result = 1;
        return result;
    }

    // Зарегистрировать пользователя
    // 0 - успешно создан, 
    // 1 - пользователь уже существует
    // 2 - email уже используется
    public int signup(String username, String password, String email, HttpSession session) throws Exception {
        
        if (userExists(username)) {
            result = 1;
        } 
        else if (emailExists(email)) {
            result = 2;
        }
        else {
            //шифруем пароль
            password = passwordEncoder.encode(password);

            

            // сохраняем пользователя в БД и отправляем письмо для подтверждения email
            token = UUID.randomUUID();
            user = new UserSMA(username, password, email, token);
            sendVerificationEmail(email, token);
            userRepository.save(user);
            result = 0;

            // Добавляем в сессию имя зарегистрированного пользователя
            session.setAttribute("username", username);

        }
        return result;
    }

    // Токен совпадает с тем, что лежит в auth.users(token) - 0 
    // Токен не совпадает - 1
    public int compareToken(UUID token) {

        if (token.toString().equals(userRepository.findByToken(token).get().getToken().toString())) {

            result = 0;
        }
        else result = 1;

        return result;
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
