package my.user.control.sma_roles.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import my.user.control.sma_roles.repositories.UserSMARepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserSMADetailService implements UserDetailsService {

    @Autowired
    public UserSMARepository userRepo;

    public UserDetails loadUserByUsername(String username) {
        return userRepo.findByUsername(username).get();
    }
    
}
