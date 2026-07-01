package my.user.control.sma_roles.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.security.authentication.BadCredentialsException;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    
    // 1. Define the repository to persist context across requests
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
            
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Autowired
    UserSMADetailService userSMAService;

    HttpSession session;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserSMA loginRequest, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        try {
            UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.authenticated(
                loginRequest.getUsername(), 
                loginRequest.getPassword(),
                userSMAService.loadUserByUsername(loginRequest.getUsername()).getAuthorities()
            );
                    
            System.out.println("is authenticated: " + token.isAuthenticated());
            
            session.setAttribute("username", loginRequest.getUsername());

            UserDetails user = userSMAService.loadUserByUsername(loginRequest.getUsername());
            // 3. Verify user credentials. Проверяем пользователя. Вернёт ошибку если будет введён неправильный пароль.
            Authentication authentication = authenticationManager.authenticate(token);
            System.out.println(user.getUsername() + " " + user.getPassword() + " " + user.getAuthorities());

            // 4. Create an empty context and assign the authenticated token. Создаем пустой контекст и назначаем токен авторизации
            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);

            // 5. Update the local thread strategy Сохраняем контекст локально в самом приложении
            securityContextHolderStrategy.setContext(context);

            // 6. Explicitly save the context to the repository (Session/Cookies) / Сохраняем контекст в репозитории
            securityContextRepository.saveContext(context, request, response);

        } catch (BadCredentialsException e) {
            System.out.println(e + " (неправильный логин/пароль)");
            securityContextHolderStrategy.clearContext();
            request.getSession().invalidate(); // при удалении сессии из запроса, сессия автоматически удаляется из БД
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Внутренняя ошибка сервера: " +  e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("Login success");
    }
}

