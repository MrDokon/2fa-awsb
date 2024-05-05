package awsb.security.example.app2fa.repository;

import awsb.security.example.app2fa.model.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
