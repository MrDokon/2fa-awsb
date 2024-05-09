package awsb.security.example.app2fa.model.dto;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user_tab")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotEmpty(message = "{username.notempty}")
    private String username;

    @NotNull
    @NotEmpty(message = "{password.notempty}")
    private String password;

    @Column(name = "two_factor_auth_key")
    private String twoFactorAuthenticationKey;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTwoFactorAuthenticationKey() {
        return twoFactorAuthenticationKey;
    }

    public void setTwoFactorAuthenticationKey(String twoFactorAuthenticationKey) {
        this.twoFactorAuthenticationKey = twoFactorAuthenticationKey;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
