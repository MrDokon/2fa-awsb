package awsb.security.example.app2fa.auth;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import javax.servlet.http.HttpServletRequest;


public class AuthDetails extends WebAuthenticationDetails {

    private String user2FaCode;

    public AuthDetails(HttpServletRequest request) {
        super(request);
        user2FaCode = request.getParameter("auth-token");
    }

    public String getUser2FaCode() {
        return user2FaCode;
    }

}