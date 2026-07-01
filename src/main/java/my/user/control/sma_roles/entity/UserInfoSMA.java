package my.user.control.sma_roles.entity;
import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class UserInfoSMA implements Serializable {
    private String username;
    private boolean isActive;
    private String email;
    private ArrayList <String> authorities;



}
