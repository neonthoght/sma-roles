package my.user.control.sma_roles.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import my.user.control.sma_roles.repositories.UserSMARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import my.user.control.sma_roles.entity.ChangePasswordSMA;
import my.user.control.sma_roles.entity.UserSMA;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserSMADetailService implements UserDetailsService {

    public UserSMARepository userRepo;
    public PasswordEncoder passwordEncoder;

    @Autowired
    public UserSMADetailService(UserSMARepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails loadUserByUsername(String username) {
        return userRepo.findByUsername(username).get();
    }

    public void changeUserPassword(String username, ChangePasswordSMA password) {
        // 1. Fetch user from DB
        UserSMA user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 2. Verify old password matches database record
        if (!passwordEncoder.matches(password.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect existing password");
        }

        // 3. Encrypt and save new password
        String encryptedPassword = passwordEncoder.encode(password.getNewPassword());
        user.setPassword(encryptedPassword);
        userRepo.save(user);
    }
    
}
