DROP TABLE IF EXISTS user_tab;

CREATE TABLE user_tab (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          username VARCHAR(250) NOT NULL,
                          password VARCHAR(250) NOT NULL,
                          two_factor_auth_key VARCHAR(250) NOT NULL
);

INSERT INTO user_tab (username, password, two_factor_auth_key) VALUES
    ('user123', '$2a$10$QhPT1UGYxJ7o1mzImaB2cOV2ZVQ/klU6kOK5zgpnkecrZdMlcz3h.', '');