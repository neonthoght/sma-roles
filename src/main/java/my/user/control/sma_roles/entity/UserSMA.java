package my.user.control.sma_roles.entity;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.jspecify.annotations.Nullable;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Column;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Set;
import my.user.control.sma_roles.entity.RoleSMA;


@Entity
@Table(name="users", schema = "auth")
public class UserSMA implements UserDetails { // user of steam market analisys system 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id; 

    
    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password; 

    @Column(name = "email")
    String email; 

    @Column(name = "is_active")
    boolean isActive;

    //используется для подтверждения email при регистрации (uuid). Испоьзовать для разных целей связанных с токеном
    @Column(name = "token")
    UUID token;

    //Collection<SimpleGrantedAuthority> authorities;

    //default constructor. Не удалять!
    UserSMA() {}

    public UserSMA(String username, String password) {
        this.username = username;
        this.password = password; // шифрованный пароль
    }

    public UserSMA(String username, String password, String email, UUID token) {
        this.username = username;
        this.password = password; // шифрованный пароль
        this.email = email;
        this.token = token;
    }
    //в id подставляется не цифра, а username - из-за этого ошибка
    @ManyToMany(fetch = FetchType.EAGER) // EAGER ensures roles load with the user
    @JoinTable(
        name = "user_roles",
        schema = "auth",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleSMA> roles = new HashSet<>();

    // получить роли польозвателя
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword( String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

        UUID getToken() {
        return this.token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

}
