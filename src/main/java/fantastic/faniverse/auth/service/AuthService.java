package fantastic.faniverse.auth.service;

import fantastic.faniverse.auth.dto.UserDto;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String register(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            return "User already exists";
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(user);
        return "User registered successfully";
    }

    public String login(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());

        if (user != null && passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return "Login successful";
        } else {
            return "Invalid email or password";
        }
    }
}
