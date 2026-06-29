package my.user.control.sma_roles.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import my.user.control.sma_roles.entity.UserSMA;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSMARepository extends JpaRepository<UserSMA, String> {
    Optional<UserSMA> findByUsername(String username);
    Optional<UserSMA> findByEmail(String email);
    Optional<UserSMA> findByToken(UUID token);


}
