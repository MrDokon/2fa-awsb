package awsb.security.example.app2fa.service;

import awsb.security.example.app2fa.model.dto.Principal;
import awsb.security.example.app2fa.model.dto.User;
import awsb.security.example.app2fa.repository.UserRepository;
import com.amdelamar.jotp.OTP;
import com.amdelamar.jotp.type.Type;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class UserService implements UserDetailsService {

    private static final String QR_PREFIX = "https://quickchart.io/qr?text=";
    private static final String APP_ISSUER = "AWSB_BezpieczenstwoAplikacjiWebowych";
    private static final String APP_ISSUER_EMAIL = "AWSB_Projekt@example.com";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void createNewUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null){
            throw new UserExistsException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setTwoFactorAuthenticationKey(OTP.randomBase32(20));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User byUsername = userRepository.findByUsername(username);
        if (byUsername == null){
            throw new UsernameNotFoundException("Nie znaleziono użytkownika: " + username);
        }
        return new Principal(byUsername);
    }

    private String getOtpUrl(User user, Model model) {
        return OTP.getURL(user.getTwoFactorAuthenticationKey(), 6, Type.TOTP, APP_ISSUER, APP_ISSUER_EMAIL);
    }

    public String getQrUrl(User user, Model model) {
        String qrUrl = "";
        try {
            qrUrl = QR_PREFIX + URLEncoder.encode(getOtpUrl(user, model), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(e.getMessage());
        }
        return qrUrl;
    }


    public static class UserExistsException extends RuntimeException { }

    static class ServiceException extends RuntimeException {
        String message;
        public ServiceException(String message) {
            this.message = message;

        }
    }

}
