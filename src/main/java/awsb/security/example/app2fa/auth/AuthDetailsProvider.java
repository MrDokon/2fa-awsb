package awsb.security.example.app2fa.auth;

import com.amdelamar.jotp.OTP;
import com.amdelamar.jotp.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import awsb.security.example.app2fa.repository.UserRepository;
import awsb.security.example.app2fa.model.dto.User;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AuthDetailsProvider extends DaoAuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    public AuthDetailsProvider(UserDetailsService userDetailsService,
                                        PasswordEncoder passwordEncoder) {
        super();
        this.setUserDetailsService(userDetailsService);
        this.setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        User user = userRepository.findByUsername(authentication.getName());
        if (user != null) {
            try {
                String authToken = OTP.create(user.getTwoFactorAuthenticationKey(), OTP.timeInHex(System.currentTimeMillis()), 6, Type.TOTP);
                AuthDetails userLoginDetails = (AuthDetails) authentication.getDetails();

                logger.info("Kod 2FA po stronie serwera: " + authToken);
                logger.info("Kod 2FA wprowadzony przez użytkownika " + userLoginDetails.getUser2FaCode());

                if (!authToken.equals(userLoginDetails.getUser2FaCode())) {
                    throw new BadCredentialsException("Wprowadzony kod logowania jest niepoprawny");
                }
            } catch (IOException | InvalidKeyException | NoSuchAlgorithmException  e) {
                logger.warn("Wystąpił problem podczas logowania" );
                throw new AuthenticationServiceException("Nie udało się zalogować ", e);
            }

        }
        return super.authenticate(authentication);
    }
}
