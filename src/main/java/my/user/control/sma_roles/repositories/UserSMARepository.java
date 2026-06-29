package my.user.control.sma_roles.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import my.user.control.sma_roles.entity.UserSMA;
import java.util.Optional;

@Repository
public interface UserSMARepository extends JpaRepository<UserSMA, String> {
    Optional<UserSMA> findByUsername(String username);

    
}
