package my.user.control.sma_roles.entity;
import lombok.Data;

@Data
public class ChangePasswordSMA {
    private String oldPassword;
    private String newPassword;
}
